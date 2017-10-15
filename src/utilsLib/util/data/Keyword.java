package utilsLib.util.data;

import utilsLib.util.Utils;

/**
 * Uma palavra-chave, que pode ser um termo-chave.
 *
 * @author Leonardo Rodrigues
 * @version 1.0, 2003-11-24
 */
public class Keyword {
    private String key;
    private String value;
    private boolean term;
    private boolean negation;

    public Keyword(String key) {
        setKey(key);
    }

    public String getKey() {
        return key;
    }

    public boolean isEmptyKeyword() {
        return key.equals("");
    }

    public void setKey(String key) {
        if (key == null) {
            throw new IllegalArgumentException("Parametro inválido: key");
        }

        key = Utils.cleanUpStr(key);
        if (key.equals("-")) {
            key = "";
        }

        if (key.length() > 0) {
            if (key.charAt(0) == '-') {
                this.value = key.substring(1, key.length());
                negation = true;
            }
        }

        // SE não definiu, coloca igual
        if (this.value == null) {
            this.value = key;
        }

        this.key = Utils.cleanUpStr(key);
        this.value = Utils.cleanUpStr(value);
        term = value.indexOf(" ") != -1;
    }

    public boolean isTerm() {
        return term;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isNegation() {
        return negation;
    }

    public void setNegation(boolean negation) {
        this.negation = negation;
    }
}
