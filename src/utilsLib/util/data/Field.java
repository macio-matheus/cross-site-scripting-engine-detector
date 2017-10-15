package utilsLib.util.data;

import java.util.Date;

import utilsLib.util.*;




/**
 * Classe que representa um campo. Um campo é formado pelo nome do mesmo,
 * valor e tipo do valor. O tipo do valor é se ele é data, inteiro, string,
 * etc., sendo estes tipos tabelados em {@link Utils}. O valor deve ser
 * um valor string.
 *
 * @author Leonardo Rodrigues
 * @version 1.0, 2002/02/19
 */
public class Field {
    private String name; // nome.
    private String realName;
    private Object value; // valor.
    private DataType type; // tipo de dado.

    /**
     * Construtor que inicia todos os dados necessários para se identificar um
     * campo. Neste, o valor é dado por uma string que o represente, sendo
     * identificado com o tipo definido.
     *
     * @param name  Nome do campo. Não pode ter valor nulo.
     * @param value Valor associado.
     * @param type  Tipo de dado. Deve ser um dos campos de tipos de dados
     *              tabelados em {@link utils}, por que caso contrário é
     *              disparada exceção.
     */
    public Field(String name, DataType type) {
        this(name, null, type);
    }

    /**
     * 
     * @param name Nome do campo no banco.
     * @param realName Nome real, que fica no bean, no java.
     * @param value
     * @param type
     */
    public Field(String name, String realName, String value, DataType type) {
        this(name, value, type);
        setRealName(realName);
    }

    public Field(String name, String value, DataType type) {
        if (name == null) {
            throw new IllegalArgumentException("Argumento inválido: name");
        }

        this.name = name;
        this.value = value;
        this.type = type;
    }

    /**
     * Construtor que inicia todos os dados necessários para se identificar um
     * campo. Este construtor recebe como valor um objeto, que poderá ser
     * de qualquer classe. O campo <code>field</code> desta classe deve
     * especificar de forma adequada a instancia passada como valor.
     *
     * @param name  Nome do campo. Não pode ter valor nulo.
     * @param value Objeto associado.
     * @param type  Tipo de dado. Deve ser um dos campos de tipos de dados
     *              tabelados em {@link utils}, por que caso contrário é
     *              disparada exceção.
     */
    public Field(String name, Object value, DataType type) {
        if (name == null) {
            throw new IllegalArgumentException("");
        }

        this.name = name;
        this.value = value;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null) {
            throw new IllegalArgumentException();
        }
        this.name = name;
    }


    public void setValue(Object value) {
        if (value instanceof Enum) {
            this.setValue(((Enum)value).ordinal());
        } else if (value instanceof Date) {
            this.setValue((Date) value);
        } else {
            this.setValue(value + "");
        }
    }

    public void setValue(String value) {
        if (value != null && type == DataType.NUMBER &&
            value.equals(String.valueOf(Utils.UNDEFINED_NUMBER))) {
            value = null;
        }

        this.value = value;
    }

    public void setValue(double value) {
        if (value == Utils.UNDEFINED_NUMBER) {
            this.value = null;
        } else {
            this.value = String.valueOf(value);
        }
    }


    public void setValue(boolean value) {
        this.value = String.valueOf(value);
    }

    public void setValue(String[] value, String separator) {
        if (value == null) {
            this.value = null;
            return;
        }

        this.value = Utils.toStr(value, separator);
    }

    public void setValue(int[] value, String separator) {
        if (value == null) {
            this.value = null;
            return;
        }

        this.value = Utils.toStr(value, separator);
    }

    public void setValue(int value) {
        if (value == Utils.UNDEFINED_NUMBER) {
            // na LinkWS não é assim, mas no Meus Downloads teve q fazer isso por causa de um pau louco de NullPointException no registro das stats de download.
            this.value = "";
        } else {
            this.value = String.valueOf(value);
        }
    }

    public void setValue(Date value) {
        if (value == null) {
            this.value = null;
            return;
        }

        if (type == DataType.DATE) {
            this.value = Utils.dateToStr(value);
        } else if (type == DataType.DATE_TIME) {
            this.value = Utils.dateTimeToStr(value);
        } else if (type == DataType.TIME) {
            this.value = Utils.timeToStr(value);
        } else {
            throw new IllegalArgumentException("Este campo não é de data (campo: " + this.name + ")");
        }
    }

    public String getValue() {
        if (value == null) {
            return null;
        }

        return value + "";
    }

    public Object getObj() {
        return value;
    }

    public void setObj(Object value) {
        this.value = value;
    }

    public void setType(DataType type) {
        this.type = type;
    }

    public DataType getType() {
        return type;
    }

    /**
     * Faz uma cópia da instância desta classe.
     *
     * @return <code>Field</code> cópia da instância.
     */
    public Object clone() {
        return new Field(name, value, type);
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Parâmetro inválido: obj");
        }

        if (!(obj instanceof Field)) {
            return super.equals(obj);
        }

        Field fieldCompare = (Field) obj;

        if (fieldCompare.getValue() == null) {
            return (fieldCompare.getName().equals(getName())
                    && fieldCompare.getType() == getType()
                    && fieldCompare.getValue() == null && getValue() == null);
        } else {
            return (fieldCompare.getName().equals(getName())
                    && fieldCompare.getType() == getType()
                    && fieldCompare.getValue().equals(getValue()));
        }
    }

    public boolean isNull() {
        if (value == null) {
            return true;
        } else if (type == DataType.NUMBER &&
                   (value.equals("") ||
                    Utils.parseInt(value + "", Utils.UNDEFINED_NUMBER) ==
                    Utils.UNDEFINED_NUMBER)) {
            return true;
        }

        return false;
    }

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}
}
