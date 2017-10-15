package utilsLib.util.data;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Date;

import utilsLib.util.Utils;




/**
 * <p>Implementa a leitura parâmetrizada de dados de um <code>ResultSet</code>,
 * por meio da interface {@link ParamDataInput}.</p>
 *
 * <p>A <code>ResultSet</code> deve aceitar a recuperação do seu
 * <code>ResultSetMetaData</code>, pois isto é imprescindível para a
 * recuperação dos valores por meio dos nomes dos campos.
 *
 * @author Francisco Rodrigues, Leonardo Rodrigues
 * @version 1.0, 2002/02/06
 */
public class DBParamDataInput
    extends ParamDataInput {
  private ResultSet rs; // ResultSet associado.
  private ResultSetMetaData rsmd; // Meta dado (nome dos campos, etc.)

  /**
   * Construtor.
   *
   * @param rs <code>ResultSet</code>, com uma consulta aberta, do qual serão
   *           extraídos os valores.
   */
  public DBParamDataInput(String extra, ResultSet rs) {
    super(extra);
    if (rs == null) {
      throw new IllegalArgumentException("[DBParamDataInput] ResultSet" +
                                         " nulo.");
    }

    this.rs = rs;

    try {
      rsmd = rs.getMetaData();
    }
    catch (SQLException error) {
      throw new RuntimeException("[DBParamDataInput] ResultSet nao " +
                                 " permite a obtenção de MetaData.");
    }
  }

  public double getDouble(String paramName) {
    try {
      double code = Double.parseDouble(rs.getObject(paramName) + "");
      return code;
    }
    catch (SQLException error) {
      return -1;
    }
  }

  public int getInt(String paramName) {
      try {
          Object o = rs.getObject(paramName);

          if (o instanceof Number) {
              return ((Number)o).intValue();
          } else if (o != null) {
              return Utils.parseInt(o + "", Utils.UNDEFINED_NUMBER);
          } else {
              return Utils.UNDEFINED_NUMBER;
          }
      }
      catch (SQLException error) {
        return Utils.UNDEFINED_NUMBER;
      }
  }

  public boolean getBoo(String paramName) {
    try {
      return rs.getBoolean(paramName);
    }
    catch (SQLException error) {
      return false;
    }
  }

  public String getStr(String paramName) {
    try {
      return rs.getString(paramName);
    }
    catch (SQLException error) {
      return null;
    }
  }

  public Object getObject(String paramName) {
    try {
      return rs.getObject(paramName);
    }
    catch (SQLException error) {
      return null;
    }
  }

  public Date getDate(String paramName, String pattern) {
    Date date = null;
    try {
        Object obj = rs.getObject(paramName);
        if (pattern != null) {
            date = Utils.objToDate(obj + "", pattern);
        } else if (obj instanceof Date) {
            date = (Date)obj;
        } else {
            try {
                date = rs.getTime(paramName);
            } catch (SQLException error) {
                date = rs.getDate(paramName);
            }
        }

        return date;
    }
    catch (SQLException error) {
    }

    return date;
  }

  public Iterator getParams() {
    throw new UnsupportedOperationException("AInda não foi implementado.");
  }
}
