package utilsLib.util.data;


import java.util.Iterator;

import utilsLib.util.Utils;



/**
 * <p>
 * Classe que representa uma entidade e seus campos. Gerencia campos do tipo
 * {@link Field}, j� que esta classe � a destinada para defini��o de tipos de
 * dados, nomes de campos e valores.
 * </p>
 * 
 * <p>
 * � utilizada para obten��o de forma gen�rica dos nomes, tipos e valores de
 * classes b�sicas (contato, conta de telefone, etc.). Quais campos ser�o postos
 * e a formata��o para se colocar os dados fica a crit�rio da classe que a
 * utiliza.
 * </p>
 * 
 * @author Leonardo Rodrigues
 * @version 1.1, 2002/06/21
 */
public abstract class Entity {
	/**
	 * Retorna os campos. Os objetos retornados s�o do tipo {@link Field}.
	 * 
	 * @return <code>Iterator</code> dos campos existentes.
	 */
	public abstract Iterator getFields();

	public abstract Field[] getFieldsArray();

	/**
	 * Remove o campo do cadastro.
	 * 
	 * @param field
	 *            Campo a ser removido.
	 * @return <code>True</code> caso o campo exista e se foi removido com
	 *         sucesso.
	 */
	public abstract boolean removeField(Field field);

	/**
	 * Acrescenta um campo ao cadastro. N�o faz controle sobre a unicidade de
	 * campos, no que diz respeito ao nome dos mesmo. Ou seja, na listagem, pode
	 * haver campos cujos nomes sejam iguais.
	 * 
	 * @param field
	 *            Campo a ser acrescido. N�o pode ter valor <code>null</code>.
	 */
	public abstract void addField(Field field);

	/**
	 * Procura um campo no cadastro pela chave que o identifica. Caso haja mais
	 * de um campo com o mesmo nome, retorna o primeiro.
	 * 
	 * @param name
	 *            Nome do campo a ser procurado.
	 * @return Retorna o campo se for encontrado ou <code>null</code> caso
	 *         contr�rio.
	 */
	public abstract Field getField(String name);

	public abstract Field getFieldByReal(String realName);

	/**
	 * Faz uma c�pia da inst�ncia, inclusive de todos os campos cadastrados.
	 * 
	 * @return Nova inst�ncia <code>Entity</code>, c�pia da original.
	 */
	public abstract Object clone();

	/**
	 * Copia todos os valores os campos da entitdade cujos nomes tenham
	 * equivalentes. N�o leva em considera��o os tipos e ignora os campos que
	 * n�o foram relacionados.
	 * 
	 * @param entity
	 *            Da qual ser� extra�dos os valores.
	 * @return N�mero de campos alterados.
	 */
	public abstract int copyEntityValues(Entity entity);

	/**
	 * Altera o nome da entidade.
	 * 
	 * @param name
	 *            Novo nome da entidade.
	 */
	public abstract void setName(String name);

	/**
	 * Recupera o nome da entidade.
	 * 
	 * @return String do nome da entidade.
	 */
	public abstract String getName();

	/**
	 * Procura um campo no cadastro pela chave que o identifica. Caso haja mais
	 * de um campo com o mesmo nome, retorna o primeiro.
	 * 
	 * @param name
	 *            Nome do campo a ser procurado.
	 * @return Retorna o campo se for encontrado ou <code>null</code> caso
	 *         contr�rio.
	 */
	public Field getField(Field field) {
		return this.getField(field.getName());
	}

	/**
	 * Altera o conjunto dos nomes dos campos determin�sticos da entidade.
	 * 
	 * @param mainFieldName
	 */
	public abstract void setMainFieldsNames(String[] mainFieldName);

	/**
	 * Retorna um array dos campos determin�sticos da entidade.
	 * 
	 * @return Retorna os noms dos campos. Caso n�o existam campos
	 *         determin�sticos, retorna <code>null</code>.
	 */
	public abstract String[] getMainFieldNames();

	/**
	 * Retorna os campos determin�sticos da entidade. Estes campos s�o
	 * pr�-determinados.
	 * 
	 * @return Array com todos os campos determin�sticos. Caso n�o existam,
	 *         retorna <code>null</code>.
	 */
	public abstract Field[] getMainFields();

	/**
	 * Retorna se o nome do campo dado � um campo determin�stico. Note que este
	 * m�todo n�o avalia se existe o campo no conjunto de campos da entidade.
	 * Avalia apenas se o nome dado pertence ao conjunto dos campos
	 * determin�sticos configurado.
	 * 
	 * @param name
	 *            Nome do campo a ser testado.
	 * @return Retorna um booleano determinado a existencia deste campo.
	 */
	public abstract boolean existsMainField(String name);

	/**
	 * Retorna os nomes dos campos.
	 * 
	 * @return Array contendo os nomes dos campos.
	 */
	public abstract String[] getFieldNames();

	/**
	 * Retorna os valores dos campos.
	 * 
	 * @return Array contendo os valores dos campos.
	 */
	public abstract String[] getFieldValues();

	/**
	 * <p>
	 * Retorna informa��es extras associadas � entidade. Este campo � um
	 * par�metro extra que � opcional, sendo que se n�o for usado dever� possuir
	 * o valor <code>null</code> como padr�o.
	 * </p>
	 * 
	 * <p>
	 * Uma utilidade para este campo � na utiliza��o de f�bricas. Para se criar
	 * objetos a partir de entidades, a f�brica deve ter conhecimento de qual
	 * classe se deve instanciar o objeto pedido. Ent�o, a entidade passada pode
	 * dar esta informa��o via este c�digo.
	 * </p>
	 * 
	 * @return String de infomra��es.
	 */
	public abstract String getExtra();

	public abstract int removeMainFields();

	/**
	 * Testa a igualdade apenas entre as entidades, sem levar em considera��o os
	 * campos.
	 * 
	 * @param compareEntity
	 *            Entidade a ser comparada.
	 * @return Verdeiro caso os campos sejam iguais.
	 */
	protected boolean isEntityEqual(Entity compareEntity) {
		if (compareEntity == null) {
			throw new IllegalArgumentException(
					"Par�metro inv�lido: compareEntity");
		}

		return (compareEntity.getName().equals(getName()) && compareEntity
				.getExtra().equals(getExtra()));
	}

	/**
	 * Compara a equivalencia com a entidade passada por parametro. Verifica
	 * tambem a igualdade em todos os campos e, em rela��o � entidade
	 * propriamente dita, compara todos os campos, a menos dos campos
	 * principais.
	 * 
	 * @param compareEntity
	 *            Entidade a ser comparada.
	 * @return Verdadeiro, caso a entidade seja equivalente.
	 */
	public boolean equals(Object obj) {
		if (obj == null) {
			throw new IllegalArgumentException("Par�metro inv�lido: obj");
		}

		// Se nao for da classe de entidade, coloca a igualdade por objeto.
		if (!(obj instanceof Entity)) {
			return super.equals(obj);
		}

		boolean returnValue = true;
		Entity compareEntity = (Entity) obj;

		// Testa a igualdade da entidade em rela�ao ao nome e aos campos extra.
		// Se correto, testa os campos.
		if (!isEntityEqual(compareEntity)) {
			returnValue = false;
		} else {
			Field[] fields = getFieldsArray();

			// Verifica se a quantidade de campos � equivalente
			if (fields.length != compareEntity.getFieldsArray().length) {
				returnValue = false;
			} else {
				// Compara a igualdade campo a campo, parando o looping caso
				// contr�rio.
				for (int i = 0; i < fields.length; i++) {
					Field fieldCompare = compareEntity.getField(fields[i]
							.getName());
					if (fieldCompare == null || !fields[i].equals(fieldCompare)) {
						returnValue = false;
						break;
					}
				}
			}
		}

		return returnValue;
	}

	/**
	 * Verifica se a entidade passada � similar. Isso significa que apenas os
	 * campos da entidade a ser comparada batem com a entidade em quest�o,
	 * desconsderando os outros campos e os dados da entidade.
	 * 
	 * @param entity
	 *            Entidade a ser comparada por similirade.
	 * @return Verdadeiro se as duas entidades forem similares.
	 */
	public boolean isSimilar(Entity entity) {
		if (entity == null) {
			throw new IllegalArgumentException("Par�metro inv�lido: entity");
		}

		boolean returnValue = true;

		Field[] fields = entity.getFieldsArray();

		// Compara a igualdade campo a campo, parando o looping caso contr�rio.
		for (int i = 0; i < fields.length; i++) {
			Field fieldCompare = getField(fields[i].getName());

			if ((fieldCompare == null) || (!fields[i].equals(fieldCompare))) {
				returnValue = false;
				break;
			}
		}

		return returnValue;
	}

	/**
	 * Converte a entidade para a leitura em uma string, no formato
	 * field1=value1&field2=value2, por exemplo.
	 * 
	 * @param separator
	 *            Separador entre o par nomeCampo/valor e outro par.
	 * @param equal
	 *            Separador entre o nomeCampo e o seu valor.
	 * @return String parametrizada para a entidade.
	 */
	public String toParamString(String separator, String equal) {
		if (separator == null) {
			throw new IllegalArgumentException("Parametro inv�lido: separator");
		}

		String returnValue = "";
		Field[] fields = getFieldsArray();

		for (int i = 0; i < fields.length; i++) {
			if (i != 0) {
				returnValue += separator;
			}
			returnValue += fields[i].getName() + equal + fields[i].getValue();
		}

		return returnValue;
	}

	/**
	 * <p>
	 * Absorve em forma de campos informa��es parametrizadas de uma string.
	 * </p>
	 * <p>
	 * Por exemplo, caso o s�mbolo de igualdade seja <code>"="</code> e o de
	 * separa�ao entre o par campo/valor seja <code>"&"</code>, entao, dada a
	 * string <code>"nome=MeuNome&idade=10"</code>, este m�todo criar� campos
	 * para esta entidade correspondentemente.
	 * </p>
	 * 
	 * @param text
	 *            Texto com parametros.
	 * @param equalSymbol
	 *            S�mbolo que iguala um campo a um valor.
	 * @param divSymbol
	 *            Divisor entre uma dupla campo/valor e outra.
	 * @return N�mero de campos absorvidos.
	 */
	public int absorbFields(String text, String equalSymbol, String divSymbol) {
		if (text == null || equalSymbol == null || divSymbol == null) {
			throw new IllegalArgumentException("Parametro nao pode ser nulo");
		}

		int equalPos = -1;
		int divPos = -1;
		int fieldsCount = 0;

		equalPos = text.indexOf(equalSymbol);
		divPos = text.indexOf(divSymbol, equalPos);

		// Enquanto houver o s�mbolo de igualdade.
		while (equalPos != -1) {
			// Se nao h� s�mbolo de divis�o, a rotina varrer� at� o final da
			// String
			if (divPos == -1) {
				divPos = text.length();
			}

			String name = text.substring(0, equalPos);
			String value = text.substring(equalPos + equalSymbol.length(),
					divPos);

			Field field = new Field(name, value, DataType.STRING);
			fieldsCount++;
			addField(field);

			// Se � a �ltima dupla
			if (divPos == text.length()) {
				break;
			} else {
				text = text.substring(divPos + divSymbol.length(), text
						.length());
			}

			// Obtem as pr�ximas posi�oes a serem trabalhadas.
			equalPos = text.indexOf(equalSymbol);
			divPos = text.indexOf(divSymbol, equalPos);
		}

		return fieldsCount;
	}

	/**
	 * Absorve os valores do array, na ordem. Absorve a quantidade de campos do
	 * menor. Por exemplo, se houver 3 campos e forem passados 5, apenas os 3
	 * primeiros valores s�o absorvidos, na ordem. A rec�proca tb � verdadeira.
	 * 
	 * @param values
	 *            Array contendo os valores. Deve ter o mesma quantidade de
	 *            elementos que a entidade.
	 * @return Retorna se absorveu todos os valores.
	 */
	public boolean absorbFields(String[] values) {
		if (values == null) {
			throw new IllegalArgumentException("Parametro inv�lido: values");
		}

		// Povoamento da entidade.
		Field[] fields = getFieldsArray();

		int lowerIndex = fields.length;
		if (lowerIndex > values.length) {
			lowerIndex = values.length;
		}

		for (int j = 0; j < lowerIndex; j++) {
			fields[j].setValue(values[j]);
		}

		return fields.length == values.length;
	}
}
