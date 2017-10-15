package utilsLib.util;


/**
 * tentativa, fazendo...
 */
public class FieldModifier {
	/**
	 * Usar apenas este campo (junto com eventais outroa), usar o que passar em
	 * substituição, count, distinct
	 */
	public enum ModifyngType {
		USE_ONLY, IN_REPLACE, COUNT, DISTINCT
	};

	private String field;
	private String exchangeField;
	private ModifyngType type;

	public FieldModifier(String field, String exchangeField, ModifyngType type) {
		this.field = field;
		this.exchangeField = exchangeField;
		this.type = type;
	}

	public FieldModifier(String field, ModifyngType type) {
		this.field = field;

		if (type == ModifyngType.COUNT) {
			exchangeField = "count(" + field + ")";
		} else if (type == ModifyngType.DISTINCT) {
			exchangeField = "distinct(" + field + ")";
		}

		this.type = type;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getExchangeField() {
		return exchangeField;
	}

	public void setExchangeField(String exchangeField) {
		this.exchangeField = exchangeField;
	}

	public ModifyngType getType() {
		return type;
	}

	public void setType(ModifyngType type) {
		this.type = type;
	}

}
