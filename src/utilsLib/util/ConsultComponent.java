package utilsLib.util;

public class ConsultComponent {
    private String name;
    private String value;
    private int compareType;
    private int conjunction;

    public ConsultComponent(String name, String value, int compareType,
                            int conjunction) {
        setName(name);
        setValue(value);
        setCompareType(compareType);
        setConjunction(conjunction);
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }


    public int getCompareType() {
        return compareType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }


    public void setCompareType(int compareType) {
        if (compareType != Utils.EQUAL_COMPARE &&
            compareType != Utils.ALIKE_COMPARE &&
            compareType != Utils.DIFFERENT_COMPARE) {
            throw new IllegalArgumentException(
                    "Parametro invalido: compareType");
        }
        this.compareType = compareType;
    }

    public int getConjunction() {
        return conjunction;
    }

    public void setConjunction(int conjunction) {
        if (conjunction != Utils.OR_CONJUNCTION &&
            conjunction != Utils.AND_CONJUNCTION &&
            conjunction != Utils.UNDEFINED_NUMBER) {
            throw new IllegalArgumentException(
                    "Parametro invalido: conjunction");
        }
        this.conjunction = conjunction;
    }

    public boolean equals(ConsultComponent cc) {
        if (cc.getCompareType() != this.getCompareType() ||
            cc.getConjunction() != this.getConjunction() ||
            !cc.getName().equals(this.getName()) ||
            !cc.getValue().equals(this.getValue())) {
            return false;
        }

        return true;
    }
}
