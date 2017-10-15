package utilsLib.util.data;

import java.util.Iterator;
import java.util.Date;

import utilsLib.util.Utils;





/**
 * Implanta a recuperação parametrizada dos campos de um {@link Entity} por
 * meio de um {@link ParamDataInput}.
 *
 * @author Francisco Rodrigues, Leonardo Rodrigues
 * @version 1.0, 2002/02/06
 */
public class EntityParamDataInput extends ParamDataInput {
    private Entity entity; // Entity do qual serão retirados os campos.

    /**
     * Construtor.
     *
     * @param entity Entity que será utilizado para recuperar os valores por
     *               meio de chaves.
     */
    public EntityParamDataInput(Entity entity) {
        super(entity.getExtra());
        if (entity == null) {
            throw new IllegalArgumentException();
        }

        this.entity = entity;
    }

    public int getInt(String paramName) {
        Field field = entity.getField(paramName);
        if (field == null) {
            return -1;
        } else if (field.getType() != DataType.NUMBER) {
            return -1;
        } else {
            return Utils.parseInt(field.getValue(), Utils.UNDEFINED_NUMBER);
        }
    }

    public boolean getBoo(String paramName) {
        Field field = entity.getField(paramName);
        if (field == null) {
            return false;
        } else if (field.getType() != DataType.BOOL) {
            return false;
        } else {
            return Utils.toBoo(field.getValue());
        }
    }

    public String getStr(String paramName) {
        Field field = entity.getField(paramName);
        if (field == null) {
            return null;
        } else if (field.getType() != DataType.STRING) {
            return null;
        } else {
            return field.getValue();
        }
    }

    public Object getObject(String paramName) {
        Field field = entity.getField(paramName);
        if (field == null) {
            return null;
        } else {
            return field.getObj();
        }
    }

    public double getDouble(String paramName) {
        Field field = entity.getField(paramName);
        if (field == null) {
            return Utils.UNDEFINED_NUMBER;
        } else {
            double code = Double.parseDouble(field.getValue());
            return code;
        }
    }

    public Date getDate(String paramName, String pattern) {
        Field field = entity.getField(paramName);
        if (field == null) {
            return null;
        } else {
        	// 2011-04-01 : para não dar erro na conversão quando é nulo, mas retornar nulo
        	if (field.getValue() == null) {
        		return null;
			} else {
				return Utils.objToDate(field.getValue(), pattern);
			}
        }
    }

    public Iterator getParams() {
        return entity.getFields();
    }

    public void setExtra(String extra) {
        super.setExtra(extra);
    }
    
    /**
     * Joga em string todos os dados da entidade.
     */
    public String toString() {
    	Field[] fields = entity.getFieldsArray();
    	
    	String str = "";
    	
    	try {
    		for (int i = 0; i < fields.length; i++) {
				str += fields[i].getName() + ": " + fields[i].getValue() + " | ";
			}
		} catch (Exception e) {
		}
    	
    	return str;
    }
}
