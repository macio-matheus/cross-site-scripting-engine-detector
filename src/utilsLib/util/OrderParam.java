package utilsLib.util;


public class OrderParam {
	public static enum ORDERING_TYPE {NORMAL, MATCH_AGAINST_IN_BOOL, PRESERVE_ORDER, EQUAL, FIND_IN_SET};
	
    private String name;
    private boolean inverse;
    
    private ORDERING_TYPE type;
    private Object value;
    
    /**
     * Ordenação normal, pelo nome do campo.
     * 
     * @param name
     * @param inverse
     */
    public OrderParam(String name, boolean inverse) {
        setName(name);
        type = ORDERING_TYPE.NORMAL;
        setInverse(inverse);
    }
    
    /**	
     * Ouros tipos de ordenação, passando o tipo e o valor usado.
     * 
     * @param name
     * @param type
     * @param value
     * @param inverse
     */
    public OrderParam(String name, ORDERING_TYPE type, Object value, boolean inverse) {
        setName(name);
        setType(type);
        setValue(value);
        setInverse(inverse);
    }

    public boolean isInverse() {
        return inverse;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setInverse(boolean inverse) {
        this.inverse = inverse;
    }

	public ORDERING_TYPE getType() {
		return type;
	}

	public void setType(ORDERING_TYPE type) {
		this.type = type;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
}
