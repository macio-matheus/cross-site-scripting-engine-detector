package utilsLib.util;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;



import system.*;
import utilsLib.*;
import utilsLib.jsp.JSPUtils;
import utilsLib.util.data.DBParamDataInput;
import utilsLib.util.data.Entity;

/**
 * <p>
 * Classe que carrega uma conex�o ao banco de dados na mem�ria e s� a retira
 * quando o objeto � finalizado ou a crit�rio do usu�rio.
 * </p>
 * 
 * <p>
 * Quando ocorre a inst�ncia, passa-se os parametros de conex�o.
 * </p>
 */
public abstract class ConnManager {
	/**
	 * N�mero de vezes que executa para garantir e testar que as conexz�es est�o ok.
	 */
	public static int DEFAULT_TEST_NUMBER = 20;
	
	/**
	 * Tempo entre os testes de ok da cone�o.
	 */
	public static int DEFAULT_INTER_TESTS = 100;
	
	public static final int GENERAL_CONN = 0;

	private static int executesCount;

	private DbConsultManager consultManager;

	private static boolean logging = true;
	private static boolean verboseLogging = true;

	public static String SYNCH_CONN_MANAGER = "";

	public ConnManager(DbConsultManager consultManager)
			throws ClassNotFoundException, SQLException {
		this.consultManager = consultManager;
	}

	/**
	 * Reinicia as conex�es, limpando as abertas e reabrindo novas.
	 * 
	 * @param connIndex
	 *            �ndice do conjunto de conex�es que se deseja abrir.
	 */
	public abstract void refreshConn(int connIndex) throws SQLException,
			ClassNotFoundException, IOException, FileNotFoundException;

	/**
	 * Retorna a conexao aberta. Caso nao esteja, retorna null.
	 * 
	 * @return Conex�o aberta quando se instancia a classe.
	 */
	public Connection getConn() throws SQLException {
		incExecuteCount(GENERAL_CONN);
		return getConn(GENERAL_CONN);
	}

	public abstract Connection getConn(int connIndex) throws SQLException;

	public abstract int getAvailableConnectionCount(int connIndex);

	public abstract int getInUseConnectionCount(int connIndex);

	public abstract int getMax(int connIndex);

	/**
	 * Libera mem�ria de uma conex�o.
	 * 
	 * @param conn
	 *            Conex�o da que se deseja desalocar mem�ria.
	 * @throws SQLException
	 */
	public abstract void release(Connection conn) throws SQLException;

	/**
	 * Executa um arquivo com SQLs
	 * 
	 * @param fileName
	 *            Caminho do arquivo.
	 * @param listener
	 *            Captador de enveontos. Pode ser nulo.
	 * @return N�mero de registros alterados.
	 */
	public int executeFile(String fileName, int offset, int limit,
			DataExtractingListener listener) throws FileNotFoundException,
			IOException {
		if (fileName == null) {
			throw new IllegalArgumentException("Param invalido: fileName");
		}

		int linesCount = 0;
		String line = null;
		FileReader fos = new FileReader(fileName);

		try {
			BufferedReader br = new BufferedReader(fos);

			while ((line = br.readLine()) != null) {
				linesCount++;

				if (linesCount - 1 < offset) {
					continue;
				}

				if (linesCount == offset + limit) {
					break;
				}

				line = line.trim();
				if (!line.equals("")) {
					executeSQL(line);
				}
				if (listener != null) {
					listener.populateDataEvent(fileName, linesCount, line);
				}
			}
		} catch (Exception error) {
			throw new RuntimeException("[" + linesCount + "] SQL: " + line
					+ " | Erro: " + error.getMessage());
		} finally {
			fos.close();
		}
		return linesCount;
	}

	public void log(Object who, String sql, long total) {
		if (logging) {
			synchronized (SYNCH_CONN_MANAGER) {
				String trace = "";

				if (verboseLogging) {
					try {
						throw new RuntimeException("");
					} catch (Exception ex) {
						trace = Utils.getStackTrace(ex, true);

						int index1 = trace.indexOf('|');
						int index2 = trace.indexOf('|', index1 + 1);
						int index3 = trace.indexOf('|', index2 + 1);

						if (index3 != -1) {
							trace = trace.substring(index3 + 1);

							// se ainda resta DBREpository
							index3 = trace.indexOf("md.util.DBRepository.");
							if (index3 != -1) {
								index3 = trace.indexOf("|");
								trace = trace.substring(index3 + 1);
							}
						}
					}

					trace = "[" + trace + "]" + Utils.getBreakLine()
							+ Utils.repeatStr(22, " ");
				}

				try {
					String content = "(" + who.getClass().getName() + ")"
							+ trace + " " + total + " " + sql.trim();
					JSPUtils.log("sqls/sqls", content);
				} catch (Exception ex) {
					JSPUtils.log("[ConnManager] Erro ao efetuar o log: "
							+ Utils.getStackTrace(ex, false));
				}
			}
		}
	}

	public void logError(Object who, String sql, long total) {
		if (logging) {
			synchronized (SYNCH_CONN_MANAGER) {
				String trace = "";

				if (verboseLogging) {
					try {
						throw new RuntimeException("");
					} catch (Exception ex) {
						trace = Utils.getStackTrace(ex, true);

						int index1 = trace.indexOf('|');
						int index2 = trace.indexOf('|', index1 + 1);
						int index3 = trace.indexOf('|', index2 + 1);

						if (index3 != -1) {
							trace = trace.substring(index3 + 1);

							// se ainda resta DBREpository
							index3 = trace.indexOf("md.util.DBRepository.");
							if (index3 != -1) {
								index3 = trace.indexOf("|");
								trace = trace.substring(index3 + 1);
							}
						}
					}

					trace = "[" + trace + "]" + Utils.getBreakLine()
							+ Utils.repeatStr(22, " ");
				}

				try {
					String content = "(" + who.getClass().getName() + ")"
							+ trace + " [ERRO] " + total + " " + sql.trim();
					JSPUtils.log("sqls/sqls", content);
				} catch (Exception ex) {
					JSPUtils.log("[ConnManager] Erro ao efetuar o log: "
							+ Utils.getStackTrace(ex, false));
				}
			}
		}
	}

	public int[] executeBatch(Object[] sqls) throws SQLException {
		if (sqls == null) {
			throw new IllegalArgumentException("Parametro inv�lido: sql");
		}

		int[] result = new int[0];

		Connection conn = getConn();
		try {
			Statement stmt = conn.createStatement();
			for (int i = 0; i < sqls.length; i++) {
				stmt.addBatch(sqls[i] + "");
			}
			result = stmt.executeBatch();
		} catch (Exception error) {
			release(conn);
			if (error instanceof SQLException) {
				throw (SQLException) error;
			} else {
				throw new RuntimeException("Erro n�o determinado: "
						+ error.getMessage());
			}
		}

		return result;
	}

	/**
	 * Executa m�ltiplas SQLs, de forma obtimizada.
	 * 
	 * @param sqls
	 *            Conjunto das SQLs a ser executadas.
	 * @return Array contendo a quantidade de registros alterados em cada uma
	 *         das SQLs.
	 * @throws SQLException
	 */
	public StringRange[] executeMultipleSQL(Object[] sqls, int connIndex)
			throws SQLException {
		if (sqls == null) {
			throw new IllegalArgumentException("Parametro inv�lido: sqls");
		}

		Statement stmt = null;
		StringRange[] results = new StringRange[sqls.length];

		for (int i = 0; i < sqls.length; i++) {
			String sqlTest = (sqls[i] + "").trim();
			results[i] = new StringRange("-1", "");
			if (!sqlTest.equals("") && !sqlTest.equals("null")) {
				try {
					results[i].setX(executeSQL(sqlTest, connIndex) + "");
				} catch (SQLException error) {
					results[i].setY(error.getMessage());
				}
			}
		}

		return results;
	}

	public StringRange[] executeMultipleSQL(Object[] sqls) throws SQLException {
		return executeMultipleSQL(sqls, GENERAL_CONN);
	}

	/**
	 * Executa um conjunto de instru��es SQL, uma por uma, sendo que tudo �
	 * passado por uma �nica String e a separa��o de uma para a outra � definida
	 * por um caracter divisor.
	 * 
	 * @param sql
	 *            SQL a ser executada.
	 * @param divisor
	 *            Caracter que separa uma instru��o da outra.
	 * @param multipleInserts
	 *            Se � inser��es seguidas, onde podem ser ignoradas
	 * @return Duas strings: x � a quantidade de linhas afetadas e y s�o
	 *         eventuais exce��es. Se x for -1 � porque houve exce��o.
	 * @throws SQLException
	 */
	public StringRange[] executeMultipleSQL(String sql, char divisor,
			int connIndex) {
		return executeMultipleSQL(sql, divisor, connIndex, false);
	}

	public StringRange[] executeMultipleSQL(String sql, char divisor,
			int connIndex, boolean multipleInserts) {
		if (sql == null) {
			throw new IllegalArgumentException("Parametro inv�lido: sql");
		}

		String[] strs = Utils.getLines(sql, divisor);
		StringRange[] results = new StringRange[strs.length];

		for (int i = 0; i < strs.length; i++) {
			String sqlTest = strs[i].trim();
			results[i] = new StringRange("-1", "");
			if (!sqlTest.equals("")) {
				try {
					results[i].setX(executeSQL(strs[i], connIndex) + "");
				} catch (SQLException error) {
					results[i].setY(error.getMessage());
				}
			}
		}

		return results;
	}

	public StringRange[] executeMultipleSQL(String sqls, char divisor)
			throws SQLException {
		return executeMultipleSQL(sqls, divisor, GENERAL_CONN);
	}

	/**
	 * Executa uma dada instru��o SQL, a qual dever� ser de atualiza��o
	 * (inser��o, atualiza��o, etc.).
	 * 
	 * @param connIndex
	 *            Indica qual conjunto de conex�es utilizar.
	 * @param sql
	 *            SQL a ser executada.
	 * @throws SQLException
	 */
	public int executeSQL(String sql, int connIndex) throws SQLException {
		if (sql == null) {
			throw new IllegalArgumentException("Parametro invalido: sql");
		}

		int updateCount = 0;

		Connection conn = getConn(connIndex);
		try {

			Statement st = conn.createStatement();

			long now = System.currentTimeMillis();
			updateCount = st.executeUpdate(sql);
			this.log(this, sql, System.currentTimeMillis() - now);

			st.close();
		} finally {
			release(conn);
		}

		return updateCount;
	}

	public int executeSQL(String sql) throws SQLException {
		return executeSQL(sql, GENERAL_CONN);
	}

	/**
	 * Abre uma consulta, paginando-se a consulta SQL. A string SQL n�o pode
	 * conter cl�usulas de limita��o de obten��o de registros, sendo isso feito
	 * a cargo deste m�todo.
	 * 
	 * @param stmt
	 *            <code>Statement</code> utilizado para executar o SQL.
	 * @param sql
	 *            SQL utilizada para abertura.
	 * @param pagingInfo
	 *            Informa��es sobre pagina��o.
	 * @return Retorna o conjunto de registros na posi��o definida pelo
	 *         <code>pagingInfo</code>. Emula a necessidade de ter que dar um
	 *         <code>next()</code>, da mesma forma que ocorre na abertura. Ou
	 *         seja, para obeter o primeiro registro do retorno, deve-se dar um
	 *         next().
	 */
	public ResultSet openQuery(Statement stmt, String sql, PagingInfo pagingInfo)
			throws SQLException {
		if (stmt == null) {
			throw new IllegalArgumentException("Parametro inv�lido: stmt");
		}
		if (sql == null) {
			throw new IllegalArgumentException("Parametro inv�lido: sql");
		}

		int offset = pagingInfo.getPageSize() * (pagingInfo.getCurrPage() - 1);
		if (offset < 0) {
			offset = 0;
		}

		int pageSize = pagingInfo.getPageSize();
		if (pageSize == 0) {
			pageSize = 1;
		}

		sql += " LIMIT " + pageSize + " OFFSET " + offset;

		ResultSet rs = null;

		long now = System.currentTimeMillis();
		rs = stmt.executeQuery(sql);
		this.log(this, sql, System.currentTimeMillis() - now);

		return rs;
	}

	/**
	 * Retorna o valor de uma SQL de apenas um campo. �til, por exemplo, em SQLs
	 * simples para obten��o da quantidade de registros.
	 * 
	 * @param sql
	 *            SQL a ser aberta.
	 * @return Retorna o valor do primeiro campo do primeiro registro. Caso n�o
	 *         exista, retorna <code>null</code>.
	 * @throws SQLException
	 */
	public String getFirstFirst(String sql) throws SQLException {
		return getFirstFirst(sql, GENERAL_CONN);
	}

	/**
	 * Retorna os valores que se selecionar no campo fields, pelo �ndice das
	 * colunas, come�ando com 1.
	 * 
	 * � uma consulta completa, passando a SQL de retorno e de quantidade, ainda
	 * com PagingInfo sendo carregado.
	 * 
	 * @param sql
	 *            String
	 * @param countingSql
	 *            String
	 * @param pagingInfo
	 *            PagingInfo
	 * @param fields
	 *            int[]
	 * @param connIndex
	 *            int
	 * @return String[][]
	 * @throws SQLException
	 */
	public String[][] getFieldValues(String sql, String countingSql,
			PagingInfo pagingInfo, int[] fields, int connIndex)
			throws SQLException {
		if (sql == null) {
			throw new IllegalArgumentException("Parametro inv�lido: sql");
		}

		if (pagingInfo == null) {
			throw new IllegalArgumentException("Parametro inv�lido: pagingInfo");
		}

		if (pagingInfo.getRegsCount() == 0) {
			return new String[0][fields.length];
		}

		ArrayList al = null;

		Connection conn = null;
		try {
			Statement st = null; // N�o instancia logo para aproveitar o
			// m�ximo de tempo sem pegar a conex�o.

			// Aqui vai ficar a consulta realizada.
			ResultSet rs = null;

			// Verifica se � para obter a quantidade de registros. Se � para
			// obter,
			// verifica se possui a SQL de contagem e a executa; caso contrario,
			// � para realizar a consulta sem ser limitada, para poder pegar
			// todos os registros.
			// Se n�o for para obter a quantidade de registros, limita. De
			// qualquer forma, o resultset � armazenado na posi��o correta.
			if (pagingInfo.getRegsCount() == Utils.UNDEFINED_NUMBER) {
				if (countingSql != null) {
					int regNumber = Utils.parseInt(this.getFirstFirst(
							countingSql, connIndex), Utils.UNDEFINED_NUMBER);

					if (regNumber == Utils.UNDEFINED_NUMBER) {
						throw new RuntimeException(
								"Erro na SQL de contagem. SQL: " + countingSql);
					}

					pagingInfo.setRegsCount(regNumber);

					// Se for zero, j� cai fora, como no come�o
					if (pagingInfo.getRegsCount() == 0) {
						return new String[0][fields.length];
					}

					// Pega e instacia o que for necessario
					conn = this.getConn(connIndex);
					;
					st = conn.createStatement();

					rs = this.openQuery(st, sql, pagingInfo);

					if (fields == null) {
						ResultSetMetaData rsmd = rs.getMetaData();
						fields = new int[rsmd.getColumnCount()];
						for (int i = 0; i < fields.length; i++) {
							fields[i] = i + 1;
						}
					}
				} else {
					// Pega e instacia o que for necessario
					conn = this.getConn(connIndex);
					st = conn.createStatement();

					long now = System.currentTimeMillis();
					rs = st.executeQuery(sql);
					this.log(this, sql, System.currentTimeMillis() - now);

					if (fields == null) {
						ResultSetMetaData rsmd = rs.getMetaData();
						fields = new int[rsmd.getColumnCount()];
						for (int i = 0; i < fields.length; i++) {
							fields[i] = i + 1;
						}
					}

					if (rs.last()) {
						pagingInfo.setRegsCount(rs.getRow());
						rs.beforeFirst();
					} else {
						pagingInfo.setRegsCount(0);
					}
				}
			} else {
				// Pega e instacia o que for necessario
				conn = this.getConn(connIndex);
				st = conn.createStatement();
				rs = this.openQuery(st, sql, pagingInfo);

				if (fields == null) {
					ResultSetMetaData rsmd = rs.getMetaData();
					fields = new int[rsmd.getColumnCount()];
					for (int i = 0; i < fields.length; i++) {
						fields[i] = i + 1;
					}
				}
			}

			al = new ArrayList(); // String

			// Instancia��o dos valores.
			int i = 0;

			for (; i < pagingInfo.getPageSize() && rs.next(); i++) {
				String[] values = new String[fields.length];

				for (int j = 0; j < values.length; j++) {
					values[j] = rs.getString(fields[j]);
				}

				al.add(values);
			}

			rs.close();
			st.close();
		} finally {
			if (conn != null) {
				this.release(conn);
			}
		}

		return (String[][]) al.toArray(new String[al.size()][]);
	}

	public String[] getFieldValues(String sql, String countingSql,
			PagingInfo pagingInfo) throws SQLException {
		String[][] values = getFieldValues(sql, countingSql, pagingInfo,
				new int[] { 1 }, ConnManager.GENERAL_CONN);
		String[] v1 = new String[values.length];

		for (int i = 0; i < values.length; i++) {
			v1[i] = values[i][0];
		}

		return v1;
	}

	public String[] getFieldValues(String sql) throws SQLException {
		PagingInfo pi = new PagingInfo();
		pi.setPageSize(Integer.MAX_VALUE);
		pi.setRegsCount(Integer.MAX_VALUE);

		String[][] values = getFieldValues(sql, null, pi, new int[] { 1 },
				ConnManager.GENERAL_CONN);
		String[] v1 = new String[values.length];

		for (int i = 0; i < values.length; i++) {
			v1[i] = values[i][0];
		}

		return v1;
	}

	public String[][] getFieldsValues(String sql) throws SQLException {
		PagingInfo pi = new PagingInfo();
		pi.setPageSize(Integer.MAX_VALUE);
		pi.setRegsCount(Integer.MAX_VALUE);

		return getFieldValues(sql, null, pi, null,
				ConnManager.GENERAL_CONN);
	}

	/**
	 * Retorna o valor de uma SQL de apenas um campo. �til, por exemplo, em SQLs
	 * simples para obten��o da quantidade de registros.
	 * 
	 * @param connIndex
	 *            �ndice do conjunto de conex�es a ser utilizado.
	 * @param sql
	 *            SQL a ser aberta.
	 * @return Retorna o valor do primeiro campo do primeiro registro. Caso n�o
	 *         exista, retorna <code>null</code>.
	 * @throws SQLException
	 */
	public String getFirstFirst(String sql, int connIndex) throws SQLException {
		if (sql == null) {
			throw new IllegalArgumentException();
		}

		String returnValue = null;

		Connection conn = getConn(connIndex);
		try {
			Statement st = conn.createStatement();

			long now = System.currentTimeMillis();
			ResultSet rs = st.executeQuery(sql);
			this.log(this, sql, System.currentTimeMillis() - now);

			if (rs.next()) {
				returnValue = rs.getString(1);
			}
			st.close();
		} finally {
			release(conn);
		}

		return returnValue;
	}

	/**
	 * Retorna um arry com os valores da primeira linha da SQWL
	 * 
	 * @param connIndex
	 *            �ndice do conjunto de conex�es a ser utilizado.
	 * @param sql
	 *            SQL a ser aberta.
	 * @return Nulo se for n�o veio nada.
	 * @throws SQLException
	 */
	public String[] getColumns(String sql) throws SQLException {
		return getColumns(sql, GENERAL_CONN);
	}

	public String[] getColumns(String sql, int connIndex) throws SQLException {
		if (sql == null) {
			throw new IllegalArgumentException();
		}

		Connection conn = getConn(connIndex);
		try {
			Statement st = conn.createStatement();

			long now = System.currentTimeMillis();
			ResultSet rs = st.executeQuery(sql);
			this.log(this, sql, System.currentTimeMillis() - now);

			ResultSetMetaData rsmd = rs.getMetaData();

			String[] values = null;

			if (rs.next()) {
				values = values = new String[rsmd.getColumnCount()];

				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					values[i - 1] = rs.getString(i);
				}
			}

			rs.close();
			st.close();

			return values;
		} finally {
			release(conn);
		}
	}

	/**
	 * Faz uma consulta no banco com objetivo de recupera��o de um registro.
	 * 
	 * @param entity
	 *            Entidade utilizada na consulta.
	 * @param factory
	 *            F�brica utilizada para produzir o objeto.
	 * @return Retorna o objeto que representa o primeiro registro de retorno.
	 *         Se n�o houver registros, retorna <code>null</code>.
	 */
	public Object getSearchedObj(Entity entity, ObjFactory factory)
			throws SQLException {
		Object c = null;

		String sql = consultManager.generateSelectSQL(entity);

		Connection conn = getConn();
		try {
			Statement st = conn.createStatement();

			// Executa SQL para filtro para o ID.
			long now = System.currentTimeMillis();
			ResultSet rs = st.executeQuery(sql);
			this.log(this, sql, System.currentTimeMillis() - now);

			// Utiliza-se de user para criar uma inst�ncia.
			if (rs.next()) {
				c = factory.paramDataToObj(new DBParamDataInput(entity
						.getExtra(), rs));
			}

			rs.close();
			st.close();
		} finally {
			release(conn);
		}

		return c;
	}

	public DbConsultManager getConsultManager() {
		return consultManager;
	}

	public boolean isVerboseLogging() {
		return verboseLogging;
	}

	public void setVerboseLogging(boolean verboseLogging) {
		this.verboseLogging = verboseLogging;
	}

	public static synchronized void incExecuteCount(int connIndex) {
		executesCount++;
	}

	public static int getExecutesCount() {
		return executesCount;
	}

	public static int getExecutesCount(int connIndex) {
		return executesCount;
	}

	public static void setExecutesCount(int executesCount) {
		ConnManager.executesCount = executesCount;
	}
	
	
	/**
	 * D� uma geral fazendo opera��es para testar e fazer as conex�es ok.
	 * 
	 * @param testsNumber
	 * @return
	 */
	public boolean makeAndTestConnsOk() {
	return makeAndTestConnsOk(DEFAULT_TEST_NUMBER);	
	}

	public boolean makeAndTestConnsOk(int testsNumber) {
		boolean ok = false;
		
		/**
		 * Fica ok = true toda vez que consegue e, deu erro, volta para false.
		 */
		for (int i = 0; i < testsNumber; i++) {
			Connection conn = null;
			try {
				this.getFirstFirst("show tables");
				ok = true;
			} catch (Exception e) {
				JSPUtils.log("[makeAndTestConnsOk] erro no teste " + i + ". Erro: " + Utils.getStackTrace(e));
				ok = false;
			}
			
			try {
				Thread.sleep(DEFAULT_INTER_TESTS);
			} catch (Exception e) {
			}
			
		}
		
		return ok;
	}
}
