package utilsLib.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class PropertiesManager {
	private Map props;
	private String[] keys;

	public PropertiesManager(String propsFilePath)
			throws FileNotFoundException, IOException {
		String filePath = propsFilePath;
		FileInputStream fis = new FileInputStream(filePath);
		Properties propsFile = new Properties();
		propsFile.load(fis);
		fis.close();
		Set keysSet = propsFile.keySet();
		keys = new String[0];
		props = new HashMap();
		if (keysSet != null) {
			keys = (String[]) propsFile.keySet().toArray(
					new String[keysSet.size()]);
			for (int i = 0; i < keys.length; i++) {
				props.put(keys[i], propsFile.get(keys[i]));
			}
		}
	}

	public String[] getKeys() {
		return keys;
	}

	public String getStr(String key) {
		if (key == null) {
			throw new IllegalArgumentException("Parametro inválido: key");
		}

		return (String) props.get(key);
	}

	public int getInt(String key, int defaultInt) {
		if (key == null) {
			throw new IllegalArgumentException("Parametro inválido: key");
		}

		return Utils.parseInt(props.get(key) + "", defaultInt);
	}

	public double getDouble(String key, double defaultDouble) {
		if (key == null) {
			throw new IllegalArgumentException("Parametro inválido: key");
		}

		return Utils.parseDouble(props.get(key) + "", defaultDouble);
	}

	/**
	 * Obtem o valor boolean, sendo que se não houve o valor, é falso. É
	 * verdadeiro se for "true" ou "1" e falso se for "false", "0" ou "".
	 * 
	 * @param key
	 *            Chave.
	 * @return Valor booelano.
	 */
	public boolean getBool(String key) {
		if (key == null) {
			throw new IllegalArgumentException("Parametro inválido: key");
		}

		String value = props.get(key) + "";
		if (value.equals("")) {
			return false;
		} else {
			return (value.equals("true") || value.equals("1"));
		}
	}
}
