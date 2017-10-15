package utilsLib.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.sql.Statement;
import java.util.Iterator;

import utilsLib.jsp.JSPUtils;
import utilsLib.util.DbConsultManager;
import utilsLib.util.DbIdsManager;
import utilsLib.util.ObjFactory;
import utilsLib.util.data.DBParamDataInput;
import utilsLib.util.data.Entity;
import utilsLib.util.data.Field;

public abstract class DBRepository {
	public static String SYNCH_THREAD_RUNNING = "DBRepository.SYNCH_THREAD_RUNNING";
	public static int threadRunning = 0;

	private ConnManager connManager;
	private Entity entity;
	private ObjFactory objFactory;
	private int classID;
	private int connIndex;
	protected static DbIdsManager dim = new DbIdsManager();
	private boolean alreadyStarted = false;
	private String dimName;

	/**
	 * Criação do repositório.
	 * 
	 * @param connManager
	 *            Gerenciador de conexões.
	 * @param objFactory
	 *            Fábrica.
	 * @param classID
	 *            ID da classe.
	 * @param connIndex
	 *            Índice da conexão a ser utilizada. Isto indica que todas as
	 *            operações da instancia deste repostiório fica vinculada a um
	 *            gerenciador distinto do padrão.
	 */
	public DBRepository(ConnManager connManager, ObjFactory objFactory,
			int classID, int connIndex) {
		if (connManager == null || objFactory == null) {
			throw new IllegalArgumentException();
		}

		this.connManager = connManager;
		this.objFactory = objFactory;
		this.classID = classID;
		this.entity = objFactory.getClassEntity(classID);
		this.connIndex = connIndex;
	}

	/**
	 * <p>
	 * Obtém-se por este método o próximo ID a ser utilizado para inserção. É
	 * utilizado no caso da necessidade de se implementar campos auto-incremento
	 * com controle via programação.
	 * </p>
	 * 
	 * <p>
	 * A cada chamda deste método, o número do ID é incrementado,
	 * independentemente se foi utilizado ou não. Outro detalhe é que o campo
	 * utilizado para as consultas ao banco de dados é o primeiro da lista de
	 * campos determinísticos da entidade.
	 * </p>
	 * 
	 * 
	 * @return Número para a próxima inserção.
	 */
	public int getNextID() {
		return getNextID(0);
	}

	public int getNextID(int index) {
		// Inicializa o gerenciador. Tenta, se der algum erro, é porque
		// já inicializou.
		if (this.dimName == null) {
			this.dimName = objFactory.getClassEntity(classID).getName()
					+ "."
					+ objFactory.getClassEntity(classID).getMainFieldNames()[index]
					+ "-" + classID;
		}

		initDim(dimName, index);
		int id = dim.getNext(dimName);
		return id;
	}

	/**
	 * Inicializa o gerenciador de identificadores, tanto para o contexto geral
	 * quanto para a classe descendente. Tem que ser chamado após a definição da
	 * fábrica e dos demais elementos da classe.
	 * 
	 * @param dimName
	 *            Nome do identificador da classe descendente.
	 */
	public void initDim(String dimName, int index) {
		if (alreadyStarted) {
			return;
		}

		if (dimName == null) {
			throw new IllegalArgumentException("Parametro inválido: dimName");
		}

		synchronized (dim) {
			if (!dim.containsId(dimName)) {
				int startValue = Utils.UNDEFINED_NUMBER;
				String sql = "";
				try {
					Connection conn = getConnManager().getConn(connIndex);
					try {
						sql = "SELECT MAX("
								+ getEntity().getMainFields()[index].getName()
								+ ") FROM " + getEntity().getName();

						Statement st = conn.createStatement();

						// Executa SQL de contato
						long now = System.currentTimeMillis();
						ResultSet rs = st.executeQuery(sql);
						this.getConnManager().log(this, sql,
								System.currentTimeMillis() - now);

						startValue = (rs.next()) ? rs.getInt(1) : 0;
						startValue++; // Incrementa, para o primeiro a
						// retorna, ser o próximo.

						rs.close();
						st.close();
					} finally {
						try {
							getConnManager().release(conn);
						} catch (Exception error) {
						}
					}
				} catch (SQLException error) {
					startValue = 1;
				} catch (Exception error) {
					startValue = 1;
				}
				dim.addId(dimName, startValue);
			}
			alreadyStarted = true;
		}
	}

	/**
	 * Insere os objsetos de memsa entidade. Por exemplo, todos de
	 * GERAL_USUARIOS.
	 * 
	 * @param objs
	 *            Objetos a serem inseridos.
	 * @param sameEntity
	 *            Indicativos de se são da mesma entidadade.
	 * @return Quantidade de objetos inseridos.
	 * @throws SQLException
	 */
	protected int[] insert(Object[] objs) throws SQLException {
		if (objs == null) {
			throw new IllegalArgumentException("Parametro invalido: obj");
		}

		if (objs.length == 0) {
			return new int[0];
		}

		Entity[] entities = new Entity[objs.length];

		for (int i = 0; i < objs.length; i++) {
			Entity entity = objFactory.getEntity(objs[i]);
			entities[i] = entity;
		}

		String sql = getConsultManager().generateInsertSQL(entities);

		long now = System.currentTimeMillis();

		int[] ids = new int[0];

		Connection conn = this.getConnManager().getConn(connIndex);
		try {

			Statement st = conn.createStatement();

			st.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);

			ResultSet generatedRs = st.getGeneratedKeys();

			int currIndex = 0;
			while (generatedRs.next()) {

				if (ids.length == 0) {
					ids = new int[objs.length];
				}

				ids[currIndex++] = generatedRs.getInt(1);
			}
			generatedRs.close();

			st.close();
		} catch (SQLException error) {
			this.getConnManager().logError(this, sql, -1);

			throw error;
		} finally {
			this.getConnManager().release(conn);
		}

		this.getConnManager().log(this, sql, System.currentTimeMillis() - now);

		return ids;
	}

	/**
	 * <p>
	 * Insere o objeto passado no repositório associado.
	 * </p>
	 * <p>
	 * 
	 * @param obj
	 *            Objeto a ser inserido. Ele primeiramente é convertido para
	 *            SQL, utilizando a fábrica associada e depois é posto para
	 *            inserir.
	 * @param excludeMainFields
	 *            Exclui os campos principais na inserção.
	 * @param execPolicy
	 *            Política de execução.
	 * @param delayed
	 *            Só funciona para MyIsam
	 * @return Retorna o ID gerado. Senão, retorna 0. Se nem inserir, retorna
	 *         -1.
	 * 
	 * @throws SQLException
	 */
	protected int insert(Object obj, boolean excludeMainFields, boolean delayed)
			throws SQLException {
		if (obj == null) {
			throw new IllegalArgumentException("Parametro invalido: obj");
		}

		Entity entity = objFactory.getEntity(obj);

		if (excludeMainFields) {
			entity.removeMainFields();
		}
		String sql = getConsultManager().generateInsertSQL(entity);

		// execução propriamente diga.
		int generatedID = -1;

		Connection conn = this.getConnManager().getConn(connIndex);
		try {

			Statement st = conn.createStatement();

			long now = System.currentTimeMillis();

			// lembrar que esta opção só serve para MyIsam
			if (delayed) {
				sql = Utils.replace(sql, "INSERT", "INSERT DELAYED");
			}

			if (st.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS) > 0) {
				generatedID = 0;
			}

			this.getConnManager().log(this, sql,
					System.currentTimeMillis() - now);

			ResultSet generatedRs = st.getGeneratedKeys();
			if (generatedRs.next()) {
				generatedID = generatedRs.getInt(1);
			}
			generatedRs.close();

			st.close();
		} catch (SQLException error) {
			this.getConnManager().logError(this, sql, -1);

			throw error;
		} finally {
			this.getConnManager().release(conn);
		}

		return generatedID;
	}

	protected int insert(Object obj) throws SQLException {
		return insert(obj, false, false);
	}

	protected int insert(Object obj, boolean delayed) throws SQLException {
		return insert(obj, false, delayed);
	}

	/**
	 * Insere, jogando na Thread. Lembre-se:
	 * 
	 * 1. Tem que ser uma operação sem pressa, porque está em cache, por
	 * exemplo. É sincronizado. 2. Não ha um retorno imediato (claro, é na
	 * cache). 3. Erros ficam em JSPUtils.
	 * 
	 * @param obj
	 *            Object
	 * @throws SQLException
	 */
	protected void insertViaThread(Object obj) throws SQLException {
		TheadOpAction pu = new TheadOpAction(this, obj, 1);
		Thread t = new Thread(pu);
		t.start();
	}

	/*
	 * protected synchronized void initSqlBuffer() { if (sqlRegister == null) {
	 * sqlRegister = new ObjectRegister(100); } } protected int
	 * createSqlBuffer(int sqlType) { initSqlBuffer(); ArrayList al = new
	 * ArrayList(); al.add(new Integer(sqlType)); return sqlRegister.put(al); }
	 * protected void addSqlBuffer(int id, String sql) { initSqlBuffer();
	 * ArrayList sqlBuffer = (ArrayList)sqlRegister.get(id); if (sqlBuffer !=
	 * null) { sqlBuffer.add(sql); } else { throw new
	 * IllegalArgumentException("Param: id. Não corresponde a um buffer"); } }
	 * protected void flushSqlBuffer(int id) { ArrayList al =
	 * (ArrayList)sqlRegister.get(id); if (al != null) { Integer sqlType =
	 * (Integer)al.get(0); } } protected void resetSqlBuffer() { sqlRegister =
	 * null; }
	 */
	protected int update(Entity entity, String condition,
			boolean ignoreNullValues) throws SQLException {
		if (entity == null) {
			throw new IllegalArgumentException();
		}

		String sql = getConsultManager().generateUpdateSQL(entity, condition,
				ignoreNullValues);

		long now = System.currentTimeMillis();
		int c = connManager.executeSQL(sql, connIndex);
		this.getConnManager().log(this, sql, System.currentTimeMillis() - now);

		return c;
	}

	protected int update(Object obj) throws SQLException {
		return update(obj, false);
	}

	/**
	 * Atualiza, jogando na Thread. Lembre-se:
	 * 
	 * 1. Tem que ser uma operação sem pressa, porque está em cache, por
	 * exemplo. É sincronizado. 2. Não ha um retorno imediato (claro, é na
	 * cache). 3. Erros ficam em JSPUtils.
	 * 
	 * @param obj
	 *            Object
	 * @throws SQLException
	 */
	protected void updateViaThread(Object obj) throws SQLException {
		TheadOpAction pu = new TheadOpAction(this, obj, 0);
		Thread t = new Thread(pu);
		t.start();
	}

	protected int update(Object obj, boolean ignoreNullValues)
			throws SQLException {
		if (obj == null) {
			throw new IllegalArgumentException("Parametro invalido: obj");
		}

		Entity entity = objFactory.getEntity(obj);
		String sql = getConsultManager().generateUpdateSQL(entity,
				ignoreNullValues);

		if (!Utils.isValidWhereSql(sql)) {
			throw new IllegalArgumentException(
					"[DbRepository.update(Entity ] Sql de update inválida: "
							+ sql + " | ignoreNullValues: " + ignoreNullValues);
		}

		// duplicado long now = System.currentTimeMillis();
		int c = connManager.executeSQL(sql, connIndex);
		// this.getConnManager().log(this, sql, System.currentTimeMillis() -
		// now);

		return c;
	}

	/**
	 * Converte uma consulta no <code>ResultSet</code> para um array, cujos
	 * itens são da classe do repositório.
	 * 
	 * @param rs
	 *            Consulta aberta.
	 * @return Array contendo os itens.
	 * @throws SQLException
	 */
	public Object[] getArray(ResultSet rs) throws SQLException {
		// Cria um array de tamnho zero.
		Object[] es = getObjFactory().getArray(classID, 0);
		;

		// Caso existam registros...
		if (rs.last()) {
			int size = rs.getRow();
			rs.first();

			// Cria um array com o numero de registros retornados
			es = getObjFactory().getArray(classID, size);

			// Povoa o array
			for (int i = 0; i < size; i++) {
				Object o = objFactory.paramDataToObj(new DBParamDataInput(
						getClassID() + "", rs));
				es[i] = o;
				rs.next();
			}
		}

		return es;
	}

	public Object getObj(String sql) throws SQLException {
		Object[] objs = getObjs(sql);

		if (objs != null && objs.length > 0) {
			return objs[0];
		} else {
			return null;
		}
	}

	public Object[] getObjs() throws SQLException {
		return getObjs(getEntity(), true);
	}

	/**
	 * Retorna um array contendo objetos cujas classes é a especificada pelo
	 * repositório.
	 * 
	 * @param sql
	 *            SQL para consulta. Deve retornar os campos necessarios para a
	 *            fábrica.
	 * @return Retorna o array.
	 * @throws SQLException
	 */
	public Object[] getObjs(String sql) throws SQLException {
		return this.getObjs(sql, this.getClassID());
	}

	public Object[] getObjs(String sql, int classID) throws SQLException {
		if (sql == null) {
			throw new IllegalArgumentException("Parametro invalid: sql");
		}

		ArrayList al = new ArrayList();

		// O que esta comentado é a implementação de controle de uso das sqls.
		// long now = System.currentTimeMillis();

		Connection conn = null;
		try {
			conn = connManager.getConn(connIndex);
			Statement st = conn.createStatement();

			// Executa SQL de contato
			long now = System.currentTimeMillis();
			ResultSet rs = st.executeQuery(sql);
			this.getConnManager().log(this, sql,
					System.currentTimeMillis() - now);

			// Povoa o array
			String classIDStr = classID + "";
			while (rs.next()) {
				Object o = objFactory.paramDataToObj(new DBParamDataInput(
						classIDStr, rs));
				al.add(o);
			}

			rs.close();
			st.close();
		} finally {
			if (conn != null) {
				connManager.release(conn);
			}
		}

		Object[] objs = objFactory.getArray(classID, al.size());
		return al.toArray(objs);
	}

	/**
	 * Retorna um array contendo objetos cujas classes é a especificada pelo
	 * repositório. Limita a quantidade e quais registros a serem obtidos de
	 * acordo com informações de paginação e atualiza as informações de
	 * paginação. Caso o <code>pagingInfo</code> (classe que carrega consigo
	 * informações sobre a paginação) não possui a quantidade de registros, este
	 * método obtém este número e o armazena em <code>pagingInfo</code>. Caso já
	 * possua a quantidade, simplesmente obtém os registros. Além da quantidade
	 * de registros, este método atualiza as outras informações associadas, como
	 * quantidade de registros da página, etc. Caso o valor da quantidade de
	 * registro definida no <code>pagingInfo</code> seja <code>0</code>, retorna
	 * um array vazio.
	 * 
	 * @param sql
	 *            SQL para consulta. Deve retornar os campos necessarios para a
	 *            fábrica.
	 * @param countingSql
	 *            SQL utilizada para a contagem dos registros. Se ela for nula,
	 *            o sistema utilizará a SQL de consulta para realizar a
	 *            contagem. Deve ser uma SQL que retorne apenas uma coluna e uma
	 *            linha contendo a quantidade de registros. Ela só é disparada
	 *            caso a quantidade de registros definida em
	 *            <code>pagingInfo</code> for
	 *            <code>Utils.UNDEFINED_NUMBER</code>.
	 * @param pagingInfo
	 *            Informações sobre a paginação a ser aplicada.
	 * @return Retorna o array.
	 * @throws SQLException
	 */
	public Object[] getObjs(String sql, String countingSql,
			PagingInfo pagingInfo, int classID) throws SQLException {
		if (sql == null) {
			throw new IllegalArgumentException("Parametro inválido: sql");
		}

		if (pagingInfo == null) {
			throw new IllegalArgumentException("Parametro inválido: pagingInfo");
		}

		if (pagingInfo.getRegsCount() == 0) {
			Object[] objs = objFactory.getArray(classID, 0);
			return objs;
		}

		ArrayList al = null;

		Connection conn = null;
		try {
			Statement st = null; // Não instancia logo para aproveitar o
			// máximo de tempo sem pegar a conexão.

			// Aqui vai ficar a consulta realizada.
			ResultSet rs = null;

			// Verifica se é para obter a quantidade de registros. Se é para
			// obter,
			// verifica se possui a SQL de contagem e a executa; caso contrario,
			// é para realizar a consulta sem ser limitada, para poder pegar
			// todos os registros.
			// Se não for para obter a quantidade de registros, limita. De
			// qualquer forma, o resultset é armazenado na posição correta.
			if (pagingInfo.getRegsCount() == Utils.UNDEFINED_NUMBER) {
				if (countingSql != null) {
					int regNumber = Utils.parseInt(
							connManager.getFirstFirst(countingSql, connIndex),
							Utils.UNDEFINED_NUMBER);

					if (regNumber == Utils.UNDEFINED_NUMBER) {
						throw new RuntimeException(
								"Erro na SQL de contagem. SQL: " + countingSql);
					}

					pagingInfo.setRegsCount(regNumber);

					// Se for zero, já cai fora, como no começo
					if (pagingInfo.getRegsCount() == 0) {
						Object[] objs = objFactory.getArray(classID, 0);
						return objs;
					}

					// Pega e instacia o que for necessario
					conn = connManager.getConn(connIndex);
					;

					// 2012-08-02 Mudou: se passar pagesize = 0, não faz
					// consulta. Antes, era sem esse if
					if (pagingInfo.getPageSize() != 0
							&& pagingInfo.getRegsCount() != 0) {
						st = conn.createStatement();
						rs = connManager.openQuery(st, sql, pagingInfo);
						// vai evitar de fechar...
						// } else {
						// rs = null;
						// st = null;
					}
				} else {
					// Pega e instacia o que for necessario
					conn = connManager.getConn(connIndex);
					;
					st = conn.createStatement();

					long now = System.currentTimeMillis();
					rs = st.executeQuery(sql);
					this.getConnManager().log(this, sql,
							System.currentTimeMillis() - now);

					if (rs.last()) {
						pagingInfo.setRegsCount(rs.getRow());
						rs.beforeFirst();
					} else {
						pagingInfo.setRegsCount(0);
					}
				}
			} else {
				// Pega e instacia o que for necessario
				conn = connManager.getConn(connIndex);
				;
				st = conn.createStatement();
				rs = connManager.openQuery(st, sql, pagingInfo);
			}

			al = new ArrayList();

			// 2012-08-02 Se não houver rs, retorna em branco.
			if (rs != null) {
				// Instanciação dos valores.
				int i = 0;

				for (; i < pagingInfo.getPageSize() && rs.next(); i++) {
					Object o = objFactory.paramDataToObj(new DBParamDataInput(
							classID + "", rs));
					al.add(o);
				}

				if (rs != null) {
					rs.close();
				}
			}

			if (st != null) {
				st.close();
			}
		} finally {
			if (conn != null) {
				connManager.release(conn);
			}
		}

		Object[] objs = objFactory.getArray(classID, al.size());
		return al.toArray(objs);
	}

	public Object[] getObjs(String sql, String countingSql,
			PagingInfo pagingInfo) throws SQLException {
		return getObjs(sql, countingSql, pagingInfo, getClassID());
	}

	public Object[] getObjs(PagingInfo pagingInfo, Field field)
			throws SQLException {
		if (field == null) {
			throw new IllegalArgumentException("Param: field");
		}

		String sql = getConsultManager().generateSelectSQL(entity.getName(),
				getConsultManager().getFieldAttribution(field));
		String countingSql = Utils.replace(sql, "*", "COUNT(*)");

		return getObjs(sql, countingSql, pagingInfo);
	}

	public Object[] getObjs(PagingInfo pagingInfo, String orderBy)
			throws SQLException {
		if (orderBy == null) {
			throw new IllegalArgumentException("Param: orderBy");
		}

		String sql = getConsultManager().generateSelectSQL(entity);
		String countingSql = Utils.replace(sql, "*", "COUNT(*)");

		sql += " ORDER BY " + orderBy;

		return getObjs(sql, countingSql, pagingInfo);
	}

	public Object[] getObjs(PagingInfo pagingInfo) throws SQLException {
		String sql = getConsultManager().generateSelectSQL(getEntity());
		String countingSql = "SELECT COUNT(*) FROM " + getEntity().getName();

		return getObjs(sql, countingSql, pagingInfo, getClassID());
	}

	public Object[] getObjs(Entity entity, boolean ignoreNullValues)
			throws SQLException {
		return getObjs(entity, ignoreNullValues, true);
	}

	public Object[] getObjs(Entity entity, boolean ignoreNullValues,
			boolean havingAllFields) throws SQLException {
		if (entity == null) {
			throw new IllegalArgumentException();
		}

		String sql = getConsultManager().generateSelectSQL(entity, "",
				ignoreNullValues, havingAllFields);

		return getObjs(sql);
	}

	/**
	 * Obtém os objetos com restrição quanto a um campo e de forma ordenada.
	 * 
	 * @param field
	 *            Canpo de restrição.
	 * @param sortFields
	 *            Campos a ordenar, na ordem.
	 * @param ascending
	 *            Ordenação ascendente, se verdadeiro.
	 * @return Instancia dos objetos.
	 * @throws SQLException
	 */
	protected Object[] getObjs(Field field, Field[] sortFields,
			boolean ascending) throws SQLException {
		if (field == null) {
			throw new IllegalArgumentException("Parametro invalido: field");
		}

		String sql = getConsultManager().generateSelectSQL(entity.getName(),
				getConsultManager().getFieldAttribution(field),
				Utils.getFieldNames(sortFields), ascending);

		return getObjs(sql);
	}

	protected Object[] getObjs(Field field) throws SQLException {
		if (field == null) {
			throw new IllegalArgumentException("Parametro invalido: field");
		}

		String sql = getConsultManager().generateSelectSQL(entity.getName(),
				getConsultManager().getFieldAttribution(field));

		// System.out.println(sql);

		return getObjs(sql);
	}

	protected Object[] getObjs(Entity entity, String where) throws SQLException {
		if (entity == null || where == null) {
			throw new IllegalArgumentException();
		}

		String sql = getConsultManager().generateSelectSQL(entity.getName(),
				where);

		return getObjs(sql);
	}

	/**
	 * Apaga o registro do banco de dados baseado no objeto passado por
	 * parametro. Este objeto é uma instancia da classe associada ao
	 * <code>DBRepository</code>.
	 * 
	 * @param obj
	 *            Instancia.
	 * @throws SQLException
	 */
	protected int delete(Object obj) throws SQLException {
		Entity entity = objFactory.getEntity(obj);
		String sql = getConsultManager().generateDeleteSQL(entity, true);

		long now = System.currentTimeMillis();
		int c = connManager.executeSQL(sql, connIndex);
		this.getConnManager().log(this, sql, System.currentTimeMillis() - now);

		return c;
	}

	/**
	 * Apaga os itens cujo campo seja igual ao passado. Deve-se ter cuidado com
	 * esta operação, pois pode-se apagar mais de um registro.
	 * 
	 * @param field
	 *            Campo que determinará os itens a serem apagados.
	 * @throws SQLException
	 */
	protected int delete(Field field) throws SQLException {
		if (field == null) {
			throw new IllegalArgumentException();
		}

		String condition = getConsultManager().getFieldAttribution(field);
		String sql = getConsultManager().generateDeleteSQL(getEntity(),
				condition);

		long now = System.currentTimeMillis();
		int c = connManager.executeSQL(sql, connIndex);
		this.getConnManager().log(this, sql, System.currentTimeMillis() - now);

		return c;
	}

	/**
	 * Apaga os itens cujo campo sejam iguais ao passado.
	 * 
	 * @param fields
	 *            Campos
	 * @throws SQLException
	 */
	protected int delete(Field[] fields) throws SQLException {
		if (fields == null) {
			throw new IllegalArgumentException("Param: fields");
		}

		if (fields.length == 0) {
			throw new IllegalArgumentException("Param: fields.length");
		}

		String condition = "";

		for (int i = 0; i < fields.length; i++) {
			condition += getConsultManager().getFieldAttribution(fields[i]);
			if (i != fields.length - 1) {
				condition += " and ";
			}
		}

		String sql = getConsultManager().generateDeleteSQL(getEntity(),
				condition);

		long now = System.currentTimeMillis();
		int c = connManager.executeSQL(sql, connIndex);
		this.getConnManager().log(this, sql, System.currentTimeMillis() - now);

		return c;
	}

	/**
	 * Apaga os itens cujo campo seja igual ao passado. Deve-se ter cuidado com
	 * esta operação, pois pode-se apagar mais de um registro.
	 * 
	 * @param field
	 *            Campo que determinará os itens a serem apagados.
	 * @param values
	 *            Possiveis valores que o campo poderá ter para apagar.
	 * @param conjunction
	 *            Conjunção, definoda por <code>Utils</code>, podendo ser "e" ou
	 *            "ou".
	 * @throws SQLException
	 */
	protected int delete(Field field, int[] values, int conjunction)
			throws SQLException {
		if (field == null) {
			throw new IllegalArgumentException();
		}

		if (conjunction != Utils.OR_CONJUNCTION
				&& conjunction != Utils.AND_CONJUNCTION) {
			throw new IllegalArgumentException(
					"Parametro inválido: conjunction. Ele tem que ser e ou ou, definido em Utils.");
		}

		String conjunctionStr = "AND";
		if (conjunction == Utils.OR_CONJUNCTION) {
			conjunctionStr = "OR";
		}

		// Obtém as atribuiçoes.
		String condition = getConsultManager().getFieldAttribution(field,
				values, conjunctionStr);

		String sql = getConsultManager().generateDeleteSQL(getEntity(),
				condition);

		long now = System.currentTimeMillis();
		int c = connManager.executeSQL(sql, connIndex);
		this.getConnManager().log(this, sql, System.currentTimeMillis() - now);

		return c;
	}

	/**
	 * Apaga os itens cujos campos sejam iguais ao do <code>Entity</code>
	 * passado. Os campos do <code>Entity</code> que tiverem seus valores nulos,
	 * não serao acrescentados na SQL.
	 * 
	 * @param entity
	 *            Campo que determinará os itens a serem apagados.
	 * @throws SQLException
	 */
	protected int delete(Entity entity) throws SQLException {
		if (entity == null) {
			throw new IllegalArgumentException();
		}

		String sql = getConsultManager().generateDeleteSQL(entity, false);

		long now = System.currentTimeMillis();
		int c = connManager.executeSQL(sql, connIndex);
		this.getConnManager().log(this, sql, System.currentTimeMillis() - now);

		return c;
	}

	public ConnManager getConnManager() {
		return connManager;
	}

	protected Entity getEntity() {
		return entity;
	}

	protected ObjFactory getObjFactory() {
		return objFactory;
	}

	protected int getClassID() {
		return classID;
	}

	/**
	 * Conta quantos registros para a restrião dada.
	 * 
	 * @param field
	 *            Campo restritor.
	 * @return Quatidade de registros.
	 * @throws SQLException
	 */
	protected int count(Field field) throws SQLException {
		String sql = "select count(*) FROM " + this.getEntity().getName()
				+ " where " + getConsultManager().getFieldAttribution(field);

		return Utils.parseInt(
				this.getConnManager().getFirstFirst(sql, this.getConnIndex()),
				0);
	}

	protected Object search(String sql) throws SQLException {
		if (sql == null) {
			throw new IllegalArgumentException("Param: sql");
		}

		Object o = null;

		Connection conn = connManager.getConn(connIndex);
		try {
			Statement stmt = conn.createStatement();

			long now = System.currentTimeMillis();
			ResultSet rs = stmt.executeQuery(sql);
			this.getConnManager().log(this, sql,
					System.currentTimeMillis() - now);

			// Se não achou registro...
			if (!rs.next()) {
				rs.close();
				stmt.close();
				return null;
			}

			o = objFactory.paramDataToObj(new DBParamDataInput(entity
					.getExtra(), rs));

			stmt.close();
			rs.close();
		} finally {
			connManager.release(conn);
		}

		return o;
	}

	protected Object search(Field field) throws SQLException {
		if (field == null) {
			throw new IllegalArgumentException("Parametro invalido: field");
		}
		String sql = getConsultManager().generateSelectSQL(entity.getName(),
				getConsultManager().getFieldAttribution(field));

		Object o = null;

		Connection conn = connManager.getConn(connIndex);
		try {
			Statement stmt = conn.createStatement();

			long now = System.currentTimeMillis();
			ResultSet rs = stmt.executeQuery(sql);
			this.getConnManager().log(this, sql,
					System.currentTimeMillis() - now);

			// Se não achou registro...
			if (!rs.next()) {
				rs.close();
				stmt.close();
				return null;
			}

			o = objFactory.paramDataToObj(new DBParamDataInput(entity
					.getExtra(), rs));

			stmt.close();
			rs.close();
		} finally {
			connManager.release(conn);
		}

		return o;
	}

	protected Object search(Field[] field) throws SQLException {
		if (field == null) {
			throw new IllegalArgumentException("Parametro invalido: field");
		}

		String where = "";

		for (int i = 0; i < field.length; i++) {
			where += getConsultManager().getFieldAttribution(field[i]);
			if (i != field.length - 1) {
				where += " and ";
			}
		}

		String sql = getConsultManager().generateSelectSQL(entity.getName(),
				where);

		Object o = null;

		Connection conn = connManager.getConn(connIndex);
		try {
			Statement stmt = conn.createStatement();

			long now = System.currentTimeMillis();
			ResultSet rs = stmt.executeQuery(sql);
			this.getConnManager().log(this, sql,
					System.currentTimeMillis() - now);

			// Se não achou registro...
			if (!rs.next()) {
				rs.close();
				stmt.close();
				return null;
			}
			o = objFactory.paramDataToObj(new DBParamDataInput(entity
					.getExtra(), rs));

			rs.close();
			stmt.close();
		} finally {
			connManager.release(conn);
		}

		return o;
	}

	protected DbConsultManager getConsultManager() {
		return connManager.getConsultManager();
	}

	public int getConnIndex() {
		return connIndex;
	}

	/**
	 * Realiza operação em segundo plano.
	 * 
	 * @author Leonardo Rodrigues
	 * @version 1.0, 2006-02-15
	 */
	static class TheadOpAction implements Runnable {
		DBRepository rep;
		Object obj;
		int op;

		/**
		 * 
		 * @param rep
		 *            DBRepository
		 * @param obj
		 *            Object
		 * @param op
		 *            0: update; 1: insert;
		 */
		public TheadOpAction(DBRepository rep, Object obj, int op) {
			this.rep = rep;
			this.obj = obj;
			this.op = op;
		}

		public void run() {
			threadRunning++;

			try {
				// JSPUtils.log("Esperando...");
				// Thread.sleep(5000);
				// JSPUtils.log("Soltou!");

				synchronized (SYNCH_THREAD_RUNNING) {
					if (op == 0) {
						rep.update(obj);
					} else if (op == 1) {
						rep.insert(obj);
					}
				}
			} catch (Exception error) {
				JSPUtils.log("[TheadOpAction.run()] Classe de chamada: "
						+ rep.getClass().getName() + " | Erro: "
						+ Utils.getStackTrace(error, false));
			} finally {
				threadRunning--;
			}
		}
	}

	public Object[] getObjs(SearchParams sp) throws SQLException {
		if (sp == null) {
			throw new IllegalArgumentException("Param: sp");
		}

		// String fieldsList = sp.getFieldsList();

		String sql = "select " + sp.getReturnFields() + " from "
				+ this.getEntity().getName() + " " + sp.getFieldPrefix();
		String countingSql = null;

		if (sp.getPi() != null) {
			countingSql = sp.getCountingSql();
		}

		String orderBy = sp.getOrder();
		String where = sp.getFilter();

		if (!where.equals("")) {
			sql += " where " + where;
		}

		if (!orderBy.equals("")) {
			sql += " order by " + orderBy;
		}

		// System.out.println(sql);

		if (sp.getPi() != null) {
			return (Object[]) this.getObjs(sql, countingSql, sp.getPi());
		} else {
			return (Object[]) this.getObjs(sql);
		}
	}
}
