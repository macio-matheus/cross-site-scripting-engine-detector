package utilsLib.util;

import java.util.Iterator;
import java.sql.ResultSet;
import java.sql.SQLException;

import utilsLib.util.data.*;




/**
 * Classe de operações para banco de dados. São funções relativas a, por
 * exemplo, construções de SQL's.
 * 
 */
public class DbConsultManager {

	public DbConsultManager() {
	}

	/**
	 * <p>
	 * Formata o valor de <code>String</code> para se posto em uma SQL.
	 * </p>
	 * 
	 * <p>
	 * Por exemplo, nos valores <code>String</code>'s de SQL não pode haver uma
	 * substring <code>"'"</code>, já que vai disparar vai montar uma SQL
	 * incosistente desta forma. Então, por esta função, onde há
	 * <code>"'"</code> ocorre a substituição por <code>"''"</code>.
	 * </p>
	 * 
	 * @param strValue
	 *            Valor a ser formatado. Aceita parametro <code>null</code>.
	 * @return Retorna o valor formatado. Caso o parâmetro tenha sido
	 *         <code>null</code>, retorna este mesmo valor.
	 */
	public String formatSQLStringValue(String strValue) {
		if (strValue == null) {
			return null;
		} else {
			strValue = Utils.replace(strValue, "'", "''", true);
			return Utils.replace(strValue, "\\", "\\\\", true);
		}
	}

	/**
	 * <p>
	 * Formata o valor de texto por tipo dado. O tipo é tabelado em
	 * {@link Utils}.
	 * </p>
	 * 
	 * <p>
	 * Por exemplo, se o texto dado for <code>"11/11/1911"</code> e o tipo de
	 * dado especificado for de data, esta funçao retornará
	 * <code>"#11/11/1911#"</code>, já que <code>"#"</code> é o caracter padrão
	 * para datas. Da mesma forma, sendo do tipo string, retornaria
	 * <code>"'11/11/1911'"</code>, e assim por diatente.
	 * 
	 * @param text
	 *            Texto string que represente qualquer tipo de dado, sem
	 *            bformatação alguma.
	 * @param type
	 *            Tipo de dado do texto.
	 * @return Texto formatado de acordo com o tipo.
	 */
	public String formatStringByType(String text, DataType type) {
		return formatStringByType(null, text, type);
	}

	public String formatStringByType(String debugFieldName, String text,
			DataType type) {
		text = (text == null) ? "" : text;

		// Coloca o valor na SQL dependendo do tipo de dado.
		if (type == DataType.STRING) {
			text = "\'" + formatSQLStringValue(text) + "\'";
		} else if (type == DataType.BOOL) {
			if (text.equals("")) {
				throw new IllegalArgumentException((debugFieldName == null ? ""
						: "[" + debugFieldName + "] ")
						+ "Tipos de dado "
						+ "inteiros ou booleanos não podem ter valores "
						+ "vazios.");
			}
			if ((text.equals("1") || text.equalsIgnoreCase("TRUE") || text
					.equalsIgnoreCase("T"))) {
				text = "1";
			} else if ((text.equals("0") || text.equalsIgnoreCase("FALSE") || text
					.equalsIgnoreCase("F"))) {
				text = "0";
			} else {
				throw new IllegalArgumentException(
						(debugFieldName == null ? "" : "[" + debugFieldName
								+ "] ")
								+ "Booleano tem que ser binário (0 ou 1) ou TRUE ou FALSE.");
			}
		} else if (type == DataType.NUMBER) {
			if (text.equals("")) {
				throw new IllegalArgumentException((debugFieldName == null ? ""
						: "[" + debugFieldName + "] ")
						+ "Tipos de dado "
						+ "inteiros ou booleanos não podem ter valores "
						+ "vazios.");
			}
		} else if (type == DataType.DATE) {
			if (text.equals("")) {
				throw new IllegalArgumentException((debugFieldName == null ? ""
						: "[" + debugFieldName + "] ")
						+ "Tipos de dado "
						+ "que representam datas não podem ter valores "
						+ "vazios.");
			}
			text = "'" + text + "'";
		} else {
			text = "'" + text + "'";
		}

		return text;
	}

	/*
	 * * Retorna se o valor do campos especial para o tipo de dado escolhido é
	 * válido. Entada-se por campo especial, um campo cujos valores possuem as
	 * seguintes restriçoes:<br> <br> - Campos de tipo numérico não podem ter
	 * valores menor que 1;<br> - Datas não podem ter valores vazios.<br>
	 * 
	 * @param value Valor a ser averiguado. @param type Tipo do valor. @return
	 * Retorna se o valor de campo para o tipo escolhido é válido.
	 * 
	 * public boolean isValidSpecialFieldValue(Object value, int type) { boolean
	 * returnValue = true; try { if (type == Utils.NUMBER_TYPE &&
	 * (Utils.parseInt(value + "") < 0 || Utils.parseInt(value + "") ==
	 * Utils.UNDEFINED_NUMBER)) { returnValue = false; } else if (type ==
	 * DataType.BOOL && value == null) { returnValue = false; } else if (type ==
	 * DateType.DATE && value == null) { returnValue = false; } else if (type ==
	 * Utils.STRING_TYPE && (value == null)) { returnValue = false; } } catch
	 * (Exception error) { returnValue = false; } return returnValue; }
	 */
	public String generateInsertSQL(Entity entity) {
		return generateInsertSQL(entity, false);
	}

	/**
	 * Gera a clásula de inserção em SQL, passando-se os parâmetros necessários
	 * para tal fim.
	 * 
	 * @param entity
	 *            Entidade que contém os campos.
	 * @param preservNull
	 *            se é para preservar o campo nulo.
	 * @return Cláusula SQL de inserção.
	 */
	public String generateInsertSQL(Entity entity, boolean preservNull) {
		String tableName = entity.getName();
		if (tableName == null || entity == null) {
			throw new IllegalArgumentException("[DBUtils](generateInsertSQL)"
					+ "Erro nos parametros passados para geraçao "
					+ "da SQL de Insert");
		}

		if (entity == null || tableName == null) {
			throw new IllegalArgumentException("[DBUtils](generateInsertSQL)"
					+ "Parâmetro inválidos.");
		}

		// Monta os valores: "Meu nome", 10, "Endereço", ...
		StringBuffer sqlValues = new StringBuffer();
		// Monta a parte inicial: campo1, campo2, campo3...
		StringBuffer sqlFields = new StringBuffer();
		// Iterator dos campos
		Iterator fields = entity.getFields();

		// Construçao dos campos e dos valores
		while (fields.hasNext()) {
			Field f = (Field) fields.next();

			// Caso o valor não seja válido, caminha ao próximo campo.
			if (!preservNull && f.isNull()) {
				continue;
			}

			// Caso já tenha colocado algum campo, acrescenta vírgula
			if (sqlValues.length() != 0) {
				sqlValues.append(", ");
				sqlFields.append(", ");
			}
			// Acrescenta o campo
			sqlFields.append(f.getName());
			// Acrescenta o valor formatado de acordo com o tipo

			if (f.isNull()) {
				sqlValues.append("null");
			} else {
				try {
					sqlValues.append(formatStringByType(f.getValue(), f
							.getType()));
				} catch (Exception error) {
					sqlValues.append("null");
				}
			}
		}

		// Caso nao acrescentou nenhum valor, é porque não houve nenhum campo,
		// e a SQL está vazia, logo está com erro.
		if (sqlValues.length() == 0) {
			throw new IllegalArgumentException("A Entity passada não possui "
					+ "campos.");
		}

		// Montagem da SQL
		// Cria "INSERT..." e junta os nomes dos campos aos valores.
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO ");
		sql.append(tableName);
		sql.append(" (");
		sql.append(sqlFields.toString());
		sql.append(") VALUES (");
		sql.append(sqlValues.toString());
		sql.append(")");

		return sql.toString();
	}

	/**
	 * Gera a clásula de inserção em SQL, passando-se os parâmetros necessários
	 * para tal fim, porém, são para várias inserções da mesma entidade.
	 * 
	 * @param entities
	 *            Entidade que contém os campos.
	 * @param preservNull
	 *            se é para preservar o campo nulo.
	 * @return Cláusula SQL de inserção.
	 */
	public String generateInsertSQL(Entity[] entities) {
		// ======================================================================
		// 1. Parâmetros
		// ======================================================================
		if (entities == null) {
			throw new IllegalArgumentException("Param: entities");
		}

		if (entities.length == 0) {
			throw new IllegalArgumentException("Param: entities.length == 0");
		}

		String tableName = entities[0].getName();
		if (tableName == null) {
			throw new IllegalArgumentException("[DBUtils](generateInsertSQL)"
					+ "Erro nos parametros passados para geraçao "
					+ "da SQL de Insert");
		}

		// Montagem da SQL
		StringBuffer sql = new StringBuffer();

		// ======================================================================
		// 2. Acréscimos dos valores e, internamente, a criação do cabeçalho
		// da inserção.
		// ======================================================================
		for (int i = 0; i < entities.length; i++) {
			Field[] fields = entities[i].getFieldsArray();

			// Se for a primeira rodada
			if (i == 0) {
				// ======================================================================
				// 2.1. Criação da listagem de campos
				// ======================================================================
				// Monta a parte inicial: campo1, campo2, campo3...
				StringBuffer sqlFields = new StringBuffer();

				// Construçao dos campos e dos valores
				for (int j = 0; j < fields.length; j++) {
					// Caso já tenha colocado algum campo, acrescenta vírgula
					if (sqlFields.length() != 0) {
						sqlFields.append(", ");
					}
					// Acrescenta o campo
					sqlFields.append(fields[j].getName());
				}

				// ======================================================================
				// 2.2. Criação do cabeçalho da inserção.
				// ======================================================================
				sql.append("INSERT INTO ");
				sql.append(tableName);
				sql.append(" (");
				sql.append(sqlFields.toString());
				sql.append(") VALUES (");
			}

			// Monta os valores: "Meu nome", 10, "Endereço", ...
			StringBuffer sqlValues = new StringBuffer();

			// Construçao dos campos e dos valores
			for (int j = 0; j < fields.length; j++) {
				// Caso já tenha colocado algum campo, acrescenta vírgula
				if (sqlValues.length() != 0) {
					sqlValues.append(", ");
				}

				// Acrescenta o valor formatado de acordo com o tipo
				if (fields[j].isNull()) {
					sqlValues.append("null");
				} else {
					try {
						sqlValues.append(formatStringByType(fields[j]
								.getValue(), fields[j].getType()));
					} catch (Exception error) {
						sqlValues.append("null");
					}
				}
			}

			if (i != 0) {
				sql.append(",(");
			}

			sql.append(sqlValues.toString());
			sql.append(")");
		}

		return sql.toString();
	}

	/**
	 * Gera a clásula de edição SQL, passando-se os parâmetros necessários para
	 * tal. Não acresenta na atualização o campos principal.
	 * 
	 * @param entity
	 *            Entidade que contém os campos.
	 * @param condition
	 *            Condição para se aplicar a atualização. Caso queira-se aplicar
	 *            a SQL a todos os registros, deve-se passar este parâmetro com
	 *            valor <code>""</code>.
	 * @return Cláusula SQL de edição.
	 */
	public String generateUpdateSQL(Entity entity, String condition,
			boolean ignoreNullValues) {
		if (entity == null || condition == null) {
			throw new IllegalArgumentException();
		}

		if (condition.equals("")) {
			throw new IllegalArgumentException("Condição de update em branco.");
		}

		String tableName = entity.getName();

		if (tableName == null) {
			throw new IllegalArgumentException("Deve ser atribuido algum "
					+ "nome a entidade.");
		}

		// Pares field1 = value1.
		StringBuffer sqlPairs = new StringBuffer();
		Iterator fields = entity.getFields();

		while (fields.hasNext()) {
			Field f = (Field) fields.next();

			if (ignoreNullValues && f.isNull()) {
				continue;
			}

			// Nao acrescenta na SQL o campo principal.
			if (!entity.existsMainField(f.getName())) {
				if (sqlPairs.length() != 0) {
					sqlPairs.append(", ");
				}

				// Monta o par
				sqlPairs.append(getFieldAttribution(f));
			}
		}

		// Caso nao acrescentou nenhum valor, é porque não houve nenhum campo,
		// e a SQL está vazia, logo está com erro.
		if (sqlPairs.length() == 0) {
			throw new IllegalArgumentException("A Entity passada não possui "
					+ "campos.");
		}

		// Montagem da SQL
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE ");
		sql.append(tableName);
		sql.append(" SET ");
		sql.append(sqlPairs.toString());

		// Acrescenta a condição
		// tem que ter uma condição.
		// if (!condition.equals("")) {
		sql.append(" WHERE " + condition);
		// }

		String sqlReturn = sql.toString();

		// para garantir redundantemente o where no update
		if (!Utils.isValidWhereSql(sqlReturn)) {
			throw new IllegalArgumentException(
					"[generateUpdateSQL] Sql de update inválida: " + sql
							+ " | ignoreNullValues: " + ignoreNullValues
							+ " | condition: " + condition);
		}

		return sqlReturn;
	}

	/**
	 * Gera a clásula de edição SQL, passando-se os parâmetros necessários para
	 * tal.
	 * 
	 * @param entity
	 *            Entidade cujas atribuições campos serão utilizados para a
	 *            atualização no banco.
	 * @return SQL formada.
	 */
	public String generateUpdateSQL(Entity entity, boolean ignoreNullValues) {
		if (entity == null) {
			throw new IllegalArgumentException();
		}

		Field[] fields = entity.getMainFields();

		// só pra verificar se há campo vazio.
		for (int j = 0; j < fields.length; j++) {
			if (fields[j].isNull()) {
				throw new IllegalArgumentException(
						"Campo principal UNDEFINED_NUMBER: "
								+ fields[j].getName() + " = "
								+ fields[j].getValue());
			}
		}

		String attr = "";

		if (fields != null) {
			attr = fieldArrayToStrAttribution(fields, " AND ");
		}

		if (attr.equals("")) {
			throw new IllegalArgumentException(
					"Campo principal UNDEFINED_NUMBER");
		}

		return generateUpdateSQL(entity, attr, ignoreNullValues);
	}

	/**
	 * Gera a clásula de remoção SQL, passando-se os parâmetros necessários para
	 * tal.
	 * 
	 * @param entity
	 *            Entidade da qual srão extraídos os valores para a SQL.
	 * @param condition
	 *            Condição para se aplicar a remoção. Caso queira-se aplicar a
	 *            SQL a todos os registros, deve-se passar este parâmetro com
	 *            valor <code>""</code>.
	 * @return Cláusula SQL de remoção.
	 */
	public String generateDeleteSQL(Entity entity, String condition) {
		if (entity == null) {
			throw new IllegalArgumentException();
		}

		String sql = "";

		// Declaraçoes iniciais;
		sql = "DELETE FROM " + entity.getName();
		if (condition != null) {
			sql += " WHERE " + condition;
		}
		return sql;
	}

	/**
	 * Gera SQL de exclusão de registro de banco de dados, baseadamente pelo
	 * <code>Entity</code>.
	 * 
	 * @param entity
	 *            <code>Entity</code> que serve de base para a SQL.
	 * @param justMainFields
	 *            Se verdadeiro, apenas os campos principais serão postos na
	 *            condição para geração da SQL. Caso contrário, todos os campos
	 *            não nulos.
	 * @return SQL de exlusão.
	 */
	public String generateDeleteSQL(Entity entity, boolean justMainFields) {
		if (entity == null) {
			throw new IllegalArgumentException();
		}

		String attr = null;

		if (justMainFields) {
			attr = fieldArrayToStrAttribution(entity.getMainFields(), " AND ");
		} else {
			attr = fieldArrayToStrAttribution(entity.getFieldsArray(), " AND ");
		}
		return generateDeleteSQL(entity, attr);
	}

	/**
	 * Gera uma query a partir de palavaras-chave.
	 * 
	 * @param keyRec
	 *            Organizador de palavaras-chave.
	 * @param queryMask
	 *            Máscara das buscas. Ex: palavras_busca LIKE '%<#word>%'.
	 * @return Query.
	 */
	public String generateQuery(KeywordRecognizer keyRec, String queryMask) {
		return this.generateQuery(keyRec, queryMask, Utils.OR_CONJUNCTION);
	}

	public String generateQuery(KeywordRecognizer keyRec, String queryMask,
			int conjuction) {
		if (keyRec == null) {
			throw new IllegalArgumentException("Param invalido: keyRec");
		}

		Keyword[] keywords = keyRec.getKeywords();
		StringBuffer query = new StringBuffer("");

		for (int i = 0; i < keywords.length; i++) {
			if (i != 0) {
				if (conjuction == Utils.AND_CONJUNCTION) {
					query.append(" AND ");
				} else {
					query.append(" OR ");
				}
			}

			if (keywords[i].isNegation()) {
				query.append("NOT ");
			}

			query
					.append("("
							+ Utils
									.replace(queryMask, "<#word>",
											formatSQLStringValue(keywords[i]
													.getValue())) + ")");
		}

		return query.toString();
	}

	public String generateQuery(String[] words, String queryMask, int conjuction) {
		if (words == null) {
			throw new IllegalArgumentException("Param invalido: keyRec");
		}

		if (words.length == 0) {
			return "";
		}

		StringBuffer query = new StringBuffer("");

		for (int i = 0; i < words.length; i++) {
			if (i != 0) {
				if (conjuction == Utils.AND_CONJUNCTION) {
					query.append(" AND ");
				} else {
					query.append(" OR ");
				}
			}

			query.append("("
					+ Utils.replace(queryMask, "<#word>",
							formatSQLStringValue(words[i])) + ")");
		}

		return query.toString();
	}

	public String generateSelectSQL(String tableName, String where) {
		if (tableName == null || where == null) {
			throw new IllegalArgumentException();
		}
		String sql = "SELECT * FROM " + tableName;

		if (!where.equals("")) {
			sql += " WHERE " + where;
		}
		return sql;
	}

	public String generateSelectSQL(String tableName, String where,
			String[] sortFields, boolean ascending) {
		if (tableName == null || where == null) {
			throw new IllegalArgumentException();
		}
		String sql = "SELECT * FROM " + tableName;

		if (!where.equals("")) {
			sql += " WHERE " + where;
		}

		if (sortFields != null && sortFields.length > 0) {
			String sorting = "";
			for (int i = 0; i < sortFields.length; i++) {
				if (i > 0) {
					sorting += ",";
				}
				sorting += sortFields[i];
			}

			if (!ascending) {
				sorting += " DESC";
			}
			sql += " ORDER BY " + sorting;
		}
		return sql;
	}

	public String generateSelectSQL(Entity entity, String where,
			boolean ignoreNullValues) {
		return generateSelectSQL(entity, where, ignoreNullValues, true);
	}

	public String generateSelectSQL(Entity entity, String where,
			boolean ignoreNullValues, boolean havingAllFields) {
		if (entity == null || where == null) {
			throw new IllegalArgumentException();
		}

		Field[] fields = entity.getFieldsArray();
		where = Utils.formatIfNull(where);

		String conjunction = null;

		if (havingAllFields) {
			conjunction = " AND ";
		} else {
			conjunction = " OR ";
		}

		for (int i = 0; i < fields.length; i++) {
			if (ignoreNullValues && fields[i].getValue() == null) {
				continue;
			}

			if (!where.equals("")) {
				where += conjunction;
			}

			where += getFieldAttribution(fields[i]);
		}

		return generateSelectSQL(entity.getName(), where);
	}

	public String generateSelectSQL(Entity entity) {
		if (entity == null) {
			throw new IllegalArgumentException();
		}

		return generateSelectSQL(entity, "", true);
	}

	/**
	 * Copia os valores da consulta para a entidade.
	 * 
	 * @param e
	 *            Entidade de destino cujos valores dos campos serão copiados a
	 *            partir da consulta.
	 * @param rs
	 *            Consulta aberta cujos campos devem bater com os campos da
	 *            entidade.
	 * @return Retorna o número de campos existentes na entidade que foram
	 *         copiados com sucesso.
	 */
	public int setEntityValuesFromRS(Entity e, ResultSet rs) {
		if (e == null || rs == null) {
			throw new IllegalArgumentException();
		}

		int sucessFields = 0;
		Iterator fields = e.getFields();

		while (fields.hasNext()) {
			Field field = (Field) fields.next();
			String fieldName = field.getName();
			try {
				DataType type = field.getType();

				if (type == DataType.STRING) {
					field.setValue(rs.getString(fieldName));
				} else if (type == DataType.BOOL) {
					field.setValue(rs.getBoolean(fieldName) + "");
				} else if (type == DataType.DATE) {
					field.setValue(Utils.dateToStr(rs.getDate(fieldName), Utils
							.getDatePattern()));
				} else if (type == DataType.NUMBER) {
					field.setValue(rs.getFloat(fieldName) + "");
				}

				sucessFields++;
			} catch (SQLException error) {
			}
		}

		return sucessFields;
	}

	public String getFieldAttribution(Field field) {
		if (field == null) {
			throw new IllegalArgumentException("Parametro invalido: field");
		}

		String returnValue = "";

		if (!field.isNull()) {
			String value = formatStringByType(field.getName(),
					field.getValue(), field.getType());
			returnValue = field.getName() + "=" + value;
		} else {
			returnValue = field.getName() + "= null";
		}

		return returnValue;
	}

	/**
	 * Cria uma string de múltiplas atribuiçoes, tomando-se por base o campo
	 * passado e colocando os valores de cada atribuicao como cada item do array
	 * de valores.
	 * 
	 * @param field
	 *            Campo base.
	 * @param values
	 *            Valores para povoar as múltiplas atribuições.
	 * @param separator
	 *            Separador de cada atribuiçao.
	 * @return String com as atribuiçoes.
	 */
	public String getFieldAttribution(Field field, int[] values,
			String separator) {
		if (field == null) {
			throw new IllegalArgumentException();
		}

		String returnValue = "";

		// Para cada item do array, cria atribuiçao, unindo-se cada uma das
		// atribuiçoes com o separador.
		for (int i = 0; i < values.length; i++) {
			if (i != 0) {
				returnValue += " " + separator + " ";
			}

			String value = formatStringByType(values[i] + "", field.getType());
			returnValue += field.getName() + "=" + value;
		}

		return returnValue;
	}

	/**
	 * Dado um array de campos, este método retorna uma string que contém as
	 * atribuições destes métodos, unindo-as com o separador.
	 * 
	 * @param fields
	 *            Campos quer terão suas atribuições cocatenadas.
	 * @param separator
	 *            Separador das atribuições.
	 * @return String com as atribuições dos campos não nulos.
	 */
	public String fieldArrayToStrAttribution(Field[] fields, String separator) {
		if (fields == null) {
			throw new IllegalArgumentException();
		}

		StringBuffer returnValue = new StringBuffer();
		for (int i = 0; i < fields.length; i++) {
			if (!fields[i].isNull()) {
				if (returnValue.length() != 0) {
					returnValue.append(separator);
				}

				returnValue.append(getFieldAttribution(fields[i]));
			}
		}

		return returnValue.toString();
	}

	public String generateQuery(ConsultInfo consultInfo) {
		return null;
	}
}
