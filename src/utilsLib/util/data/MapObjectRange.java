package utilsLib.util.data;

import java.util.Map;
import java.util.Date;
import java.util.Iterator;

import utilsLib.util.Utils;




/**
 * Caracteriza uma dupla de valores de string.
 *
 * @author Leonardo Rodrigues
 * @version 1.0, 2003/04/26
 */
public class MapObjectRange extends ParamDataInput {
    private Map map;
    public MapObjectRange(String extra, Map map) {
        super(extra);

        if (map == null) {
            throw new IllegalArgumentException("Param: map");
        }

        this.map = map;
    }

    public int getInt(String paramName) {
        String value = (String)map.get(paramName);

        if (value == null) {
            return -1;
        } else {
            try {
                return Integer.parseInt(value);
            } catch (Exception error) {
                return -1;
            }
        }
    }

    public boolean getBoo(String paramName) {
        String value = (String)map.get(paramName);

        if (value == null) {
            return false;
        } else {
            try {
                return Boolean.getBoolean(value);
            } catch (Exception error) {
                return false;
            }
        }
    }

    public String getStr(String paramName) {
        String value = (String)map.get(paramName);

        if (value == null) {
            return null;
        }

        return value;
    }

    public double getDouble(String paramName) {
        String value = (String)map.get(paramName);

        if (value == null) {
            return Utils.UNDEFINED_NUMBER;
        }

        return Double.parseDouble(value);
    }

    public Object getObject(String paramName) {
        String value = (String)map.get(paramName);

        if (value == null) {
            return null;
        }

        return value;
    }

    public Date getDate(String paramName, String pattern) {
        String value = (String)map.get(paramName);

        if (value == null) {
            return null;
        } else {
            try {
                return Utils.objToDate(value, pattern);
            } catch (Exception error) {
                return null;
            }
        }
    }

    public Iterator getParams() {
        return map.keySet().iterator();
    }

}
