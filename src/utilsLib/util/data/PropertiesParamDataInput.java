package utilsLib.util.data;

import java.io.*;
import java.util.*;

import utilsLib.util.*;




public class PropertiesParamDataInput
    extends ParamDataInput {
  private Properties props;

  public PropertiesParamDataInput(String extra, Properties props) {
    super(extra);
    if (props == null) {
      throw new IllegalArgumentException();
    }

    this.props = props;
  }

  public PropertiesParamDataInput(String extra, String filePath) throws FileNotFoundException, IOException {
    super(extra);
    if (filePath == null) {
      throw new IllegalArgumentException();
    }

    FileInputStream fis = new FileInputStream(filePath);

    Properties propsFile = new Properties();
    Properties props = new Properties();
    props.load(fis);
    this.props = props;

    fis.close();
  }

  public int getInt(String paramName) {
		String value = props.getProperty(paramName);

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
    String value = props.getProperty(paramName);

    if (value == null) {
      return false;
    }
    else {
      try {
        return Boolean.getBoolean(value);
      }
      catch (Exception error) {
        return false;
      }
    }
  }

  public String getStr(String paramName) {
    String value = props.getProperty(paramName);

    if (value == null) {
      return null;
    }

    return value;
  }

  public double getDouble(String paramName) {
    String value = props.getProperty(paramName);

    if (value == null) {
      return Utils.UNDEFINED_NUMBER;
    }

    return Double.parseDouble(value);
  }

  public Object getObject(String paramName) {
    String value = props.getProperty(paramName);

    if (value == null) {
      return null;
    }

    return value;
  }

  public Date getDate(String paramName, String pattern) {
    String value = props.getProperty(paramName);

    if (value == null) {
      return null;
    }
    else {
      try {
        return Utils.objToDate(value, pattern);
      }
      catch (Exception error) {
        return null;
      }
    }
  }

  public Iterator getParams() {
    throw new java.lang.UnsupportedOperationException(
        "Ainda não foi implementado!");
  }
}
