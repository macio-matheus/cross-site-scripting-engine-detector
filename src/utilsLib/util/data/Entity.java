package utilsLib.util.data;


import java.util.Iterator;

import utilsLib.util.Utils;



/**
 * <p>
 * Classe que representa uma entidade e seus campos. Gerencia campos do tipo
 * {@link Field}, já que esta classe é a destinada para definição de tipos de
 * dados, nomes de campos e valores.
 * </p>
 * 
 * <p>
 * É utilizada para obtenção de forma genérica dos nomes, tipos e valores de
 * classes básicas (contato, conta de telefone, etc.). Quais campos serão postos
 * e a formatação para se colocar os dados fica a critério da classe que a
 * utiliza.
 * </p>
 * 
 * @author Leonardo Rodrigues
 * @version 1.1, 2002/06/21
 */
public abstract class Entity {
	/**
	 * Retorna os campos. Os objetos retornados são do tipo {@link Field}.
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
	 * Acrescenta um campo ao cadastro. Não faz controle sobre a unicidade de
	 * campos, no que diz respeito ao nome dos mesmo. Ou seja, na listagem, pode
	 * haver campos cujos nomes sejam iguais.
	 * 
	 * @param field
	 *            Campo a ser acrescido. Não pode ter valor <code>null</code>.
	 */
	public abstract void addField(Field field);

	/**
	 * Procura um campo no cadastro pela chave que o identifica. Caso haja mais
	 * de um campo com o mesmo nome, retorna o primeiro.
	 * 
	 * @param name
	 *            Nome do campo a ser procurado.
	 * @return Retorna o campo se for encontrado ou <code>null</code> caso
	 *         contrário.
	 */
	public abstract Field getField(String name);

	public abstract Field getFieldByReal(String realName);

	/**
	 * Faz uma cópia da instância, inclusive de todos os campos cadastrados.
	 * 
	 * @return Nova instância <code>Entity</code>, cópia da original.
	 */
	public abstract Object clone();

	/**
	 * Copia todos os valores os campos da entitdade cujos nomes tenham
	 * equivalentes. Não leva em consideração os tipos e ignora os campos que
	 * não foram relacionados.
	 * 
	 * @param entity
	 *            Da qual serã extraídos os valores.
	 * @return Número de campos alterados.
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
	 *         contrário.
	 */
	public Field getField(Field field) {
		return this.getField(field.getName());
	}

	/**
	 * Altera o conjunto dos nomes dos campos determinísticos da entidade.
	 * 
	 * @param mainFieldName
	 */
	public abstract void setMainFieldsNames(String[] mainFieldName);

	/**
	 * Retorna um array dos campos determinísticos da entidade.
	 * 
	 * @return Retorna os noms dos campos. Caso não existam campos
	 *         determinísticos, retorna <code>null</code>.
	 */
	public abstract String[] getMainFieldNames();

	/**
	 * Retorna os campos determinísticos da entidade. Estes campos são
	 * pré-determinados.
	 * 
	 * @return Array com todos os campos determinísticos. Caso não existam,
	 *         retorna <code>null</code>.
	 */
	public abstract Field[] getMainFields();

	/**
	 * Retorna se o nome do campo dado é um campo determinístico. Note que este
	 * método não avalia se existe o campo no conjunto de campos da entidade.
	 * Avalia apenas se o nome dado pertence ao conjunto dos campos
	 * determinísticos configurado.
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
	 * Retorna informações extras associadas à entidade. Este campo é um
	 * parâmetro extra que é opcional, sendo que se não for usado deverá possuir
	 * o valor <code>null</code> como padrão.
	 * </p>
	 * 
	 * <p>
	 * Uma utilidade para este campo é na utilização de fábricas. Para se criar
	 * objetos a partir de entidades, a fábrica deve ter conhecimento de qual
	 * classe se deve instanciar o objeto pedido. Então, a entidade passada pode
	 * dar esta informação via este código.
	 * </p>
	 * 
	 * @return String de infomrações.
	 */
	public abstract String getExtra();

	public abstract int removeMainFields();

	/**
	 * Testa a igualdade apenas entre as entidades, sem levar em consideração os
	 * campos.
	 * 
	 * @param compareEntity
	 *            Entidade a ser comparada.
	 * @return Verdeiro caso os campos sejam iguais.
	 */
	protected boolean isEntityEqual(Entity compareEntity) {
		if (compareEntity == null) {
			throw new IllegalArgumentException(
					"Parâmetro inválido: compareEntity");
		}

		return (compareEntity.getName().equals(getName()) && compareEntity
				.getExtra().equals(getExtra()));
	}

	/**
	 * Compara a equivalencia com a entidade passada por parametro. Verifica
	 * tambem a igualdade em todos os campos e, em relação à entidade
	 * propriamente dita, compara todos os campos, a menos dos campos
	 * principais.
	 * 
	 * @param compareEntity
	 *            Entidade a ser comparada.
	 * @return Verdadeiro, caso a entidade seja equivalente.
	 */
	public boolean equals(Object obj) {
		if (obj == null) {
			throw new IllegalArgumentException("Parâmetro inválido: obj");
		}

		// Se nao for da classe de entidade, coloca a igualdade por objeto.
		if (!(obj instanceof Entity)) {
			return super.equals(obj);
		}

		boolean returnValue = true;
		Entity compareEntity = (Entity) obj;

		// Testa a igualdade da entidade em relaçao ao nome e aos campos extra.
		// Se correto, testa os campos.
		if (!isEntityEqual(compareEntity)) {
			returnValue = false;
		} else {
			Field[] fields = getFieldsArray();

			// Verifica se a quantidade de campos é equivalente
			if (fields.length != compareEntity.getFieldsArray().length) {
				returnValue = false;
			} else {
				// Compara a igualdade campo a campo, parando o looping caso
				// contrário.
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
	 * Verifica se a entidade passada é similar. Isso significa que apenas os
	 * campos da entidade a ser comparada batem com a entidade em questão,
	 * desconsderando os outros campos e os dados da entidade.
	 * 
	 * @param entity
	 *            Entidade a ser comparada por similirade.
	 * @return Verdadeiro se as duas entidades forem similares.
	 */
	public boolean isSimilar(Entity entity) {
		if (entity == null) {
			throw new IllegalArgumentException("Parâmetro inválido: entity");
		}

		boolean returnValue = true;

		Field[] fields = entity.getFieldsArray();

		// Compara a igualdade campo a campo, parando o looping caso contrário.
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
			throw new IllegalArgumentException("Parametro inválido: separator");
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
	 * Absorve em forma de campos informações parametrizadas de uma string.
	 * </p>
	 * <p>
	 * Por exemplo, caso o símbolo de igualdade seja <code>"="</code> e o de
	 * separaçao entre o par campo/valor seja <code>"&"</code>, entao, dada a
	 * string <code>"nome=MeuNome&idade=10"</code>, este método criará campos
	 * para esta entidade correspondentemente.
	 * </p>
	 * 
	 * @param text
	 *            Texto com parametros.
	 * @param equalSymbol
	 *            Símbolo que iguala um campo a um valor.
	 * @param divSymbol
	 *            Divisor entre uma dupla campo/valor e outra.
	 * @return Número de campos absorvidos.
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

		// Enquanto houver o símbolo de igualdade.
		while (equalPos != -1) {
			// Se nao há símbolo de divisão, a rotina varrerá até o final da
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

			// Se é a última dupla
			if (divPos == text.length()) {
				break;
			} else {
				text = text.substring(divPos + divSymbol.length(), text
						.length());
			}

			// Obtem as próximas posiçoes a serem trabalhadas.
			equalPos = text.indexOf(equalSymbol);
			divPos = text.indexOf(divSymbol, equalPos);
		}

		return fieldsCount;
	}

	/**
	 * Absorve os valores do array, na ordem. Absorve a quantidade de campos do
	 * menor. Por exemplo, se houver 3 campos e forem passados 5, apenas os 3
	 * primeiros valores são absorvidos, na ordem. A recíproca tb é verdadeira.
	 * 
	 * @param values
	 *            Array contendo os valores. Deve ter o mesma quantidade de
	 *            elementos que a entidade.
	 * @return Retorna se absorveu todos os valores.
	 */
	public boolean absorbFields(String[] values) {
		if (values == null) {
			throw new IllegalArgumentException("Parametro inválido: values");
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
