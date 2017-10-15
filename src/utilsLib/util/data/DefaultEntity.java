package utilsLib.util.data;

import java.util.Iterator;
import java.util.ArrayList;

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
public class DefaultEntity extends Entity {
	private ArrayList fields = new ArrayList(); // Armazena os campos.
	private String name; // nome da entitdade
	private String extra;
	private String[] mainFieldNames;

	/**
	 * Construtor.
	 * 
	 * @param name
	 *            Nome associado a entidade.
	 * @param mainFieldName
	 *            Nome do campo identificador. Se não houver, passa-se
	 *            <code>null</code>.
	 */
	public DefaultEntity(String name, String extra, String[] mainFieldNames) {
		if (name == null) {
			throw new IllegalArgumentException("O nome da entiedade não "
					+ "pode ser nulo.");
		}

		this.name = name;
		this.mainFieldNames = mainFieldNames;
		this.extra = extra;
	}

	/**
	 * Construtor.
	 * 
	 * @param name
	 *            Nome associado a entidade.
	 * @param mainFieldName
	 *            Nome do campo identificador. Se não houver, passa-se
	 *            <code>null</code>.
	 */
	public DefaultEntity(String name, String extra, String mainFieldName) {
		if (name == null) {
			throw new IllegalArgumentException();
		}

		this.name = name;
		this.mainFieldNames = new String[] { mainFieldName };
		this.extra = extra;
	}

	/**
	 * Retorna os campos. Os objetos retornados são do tipo {@link Field}.
	 * 
	 * @return <code>Iterator</code> dos campos existentes.
	 */
	public Iterator getFields() {
		return fields.iterator();
	}

	/**
	 * Retorna os campos. Os objetos retornados são do tipo {@link Field}.
	 * 
	 * @return <code>Iterator</code> dos campos existentes.
	 */
	public Field[] getFieldsArray() {
		Field[] fieldsArray = new Field[fields.size()];

		return (Field[]) fields.toArray(fieldsArray);
	}

	/**
	 * Remove o campo do cadastro.
	 * 
	 * @param field
	 *            Campo a ser removido.
	 * @return <code>True</code> caso o campo exista e se foi removido com
	 *         sucesso.
	 */
	public boolean removeField(Field field) {
		return fields.remove(field);
	}

	/**
	 * Acrescenta um campo ao cadastro. Não faz controle sobre a unicidade de
	 * campos, no que diz respeito ao nome dos mesmo. Ou seja, na listagem, pode
	 * haver campos cujos nomes sejam iguais.
	 * 
	 * @param field
	 *            Campo a ser acrescido. Não pode ter valor <code>null</code>.
	 */
	public void addField(Field field) {
		if (field == null) {
			throw new IllegalArgumentException();
		}

		fields.add(field);
	}

	public Field getFieldByReal(String realName) {
		if (realName == null) {
			throw new IllegalArgumentException("Parametro invalido: realName");
		}

		Field field = null;

		// Looping para busca do campo requesitado.
		for (int i = 0; i < fields.size(); i++) {
			Field f = (Field) fields.get(i);
			if (f.getRealName() != null
					&& f.getRealName().equalsIgnoreCase(realName)) {
				field = f;
				break;
			}
		}
		return field;
	}

	public Field getField(String name) {
		if (name == null) {
			throw new IllegalArgumentException("Parametro invalido: name");
		}

		Field field = null;

		// Looping para busca do campo requesitado.
		for (int i = 0; i < fields.size(); i++) {
			Field f = (Field) fields.get(i);
			if (f.getName().equalsIgnoreCase(name)) {
				field = f;
				break;
			}
		}
		return field;
	}

	/**
	 * Faz uma cópia da instância, inclusive de todos os campos cadastrados.
	 * 
	 * @return Nova instância <code>Entity</code>, cópia da original.
	 */
	public Object clone() {
		Entity e = new DefaultEntity(getName(), getExtra(), getMainFieldNames());

		Field[] fields = getFieldsArray();

		for (int i = 0; i < fields.length; i++) {
			if (fields[i] != null) {
				e.addField((Field) fields[i].clone());
			}
		}

		return e;
	}

	/**
	 * Copia todos os valores da entitdade cujos campos tenham o mesmo nomes.
	 * Não leva em consideração os tipos e ignora os campos que não foram
	 * relacionados.
	 * 
	 * @param entity
	 *            Da qual serã extraídos os valores.
	 * @return Número de campos alterados.
	 */
	public int copyEntityValues(Entity entity) {
		if (entity == null) {
			throw new IllegalArgumentException();
		}

		// Conta o número de campos alterados.
		int alteredFields = 0;
		// Recupera os campos dos quais serão copiados os valores
		Iterator fields = entity.getFields();

		// Percorre os campos.
		while (fields.hasNext()) {
			Field fromField = (Field) fields.next();
			Field toField = getField(fromField.getName());
			if (toField != null) {
				toField.setValue(fromField.getValue());
				alteredFields++;
			}
		}

		return alteredFields;
	}

	public void setName(String name) {
		if (name == null) {
			throw new IllegalArgumentException();
		}

		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setMainFieldsNames(String[] mainFieldName) {
		this.mainFieldNames = mainFieldNames;
	}

	public String[] getMainFieldNames() {
		return mainFieldNames;
	}

	public Field[] getMainFields() {
		int count = getMainFieldsCount();

		if (count == -1) {
			return null;
		}

		Field[] field = new Field[count];

		for (int i = 0; i < count; i++) {
			field[i] = getField(mainFieldNames[i]);
		}

		return field;
	}

	public int getMainFieldsCount() {
		if (mainFieldNames == null) {
			return -1;
		} else {
			return mainFieldNames.length;
		}
	}

	public boolean existsMainField(String name) {
		if (name == null) {
			throw new IllegalArgumentException();
		}

		int count = mainFieldNames.length;
		boolean returnValue = false;

		if (count == -1) {
			return false;
		}

		for (int i = 0; i < count; i++) {
			if (mainFieldNames[i].equals(name)) {
				returnValue = true;
				break;
			}
		}

		return returnValue;
	}

	public String[] getFieldNames() {
		String[] r = new String[fields.size()];

		for (int i = 0; i < r.length; i++) {
			r[i] = ((Field) fields.get(i)).getName();
		}

		return r;
	}

	public String[] getFieldValues() {
		String[] r = new String[fields.size()];

		for (int i = 0; i < r.length; i++) {
			r[i] = ((Field) fields.get(i)).getValue();
		}

		return r;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	public int removeMainFields() {
		int returnValue = 0;
		for (int i = 0; i < mainFieldNames.length; i++) {
			Field field = getField(mainFieldNames[i]);
			if (field != null) {
				removeField(field);
				returnValue++;
			}
		}
		return returnValue;
	}
}
