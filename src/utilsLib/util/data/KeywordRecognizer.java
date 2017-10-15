package utilsLib.util.data;

import java.util.ArrayList;

import utilsLib.util.Utils;

/**
 * Reconhecedor de palavras-chave. Pode-se construir um conjunto de
 * palavras-chave a partir de uma string.
 * 
 * @version 1.0, 2003-11-24
 */
public class KeywordRecognizer {
	private ArrayList keywords;
	private String name;
	private boolean simple;

	public KeywordRecognizer(String name, String query) {
		this(name, query, true);
	}

	public KeywordRecognizer(String name, String query, boolean simple) {
		keywords = new ArrayList();
		setSimple(simple);
		setQuery(query);
		setName(name);
	}

	public void addKeyword(Keyword keyword) {
		if (keyword == null) {
			throw new IllegalArgumentException("Parametro inválido: ");
		}

		if (!keyword.isEmptyKeyword() && keywords.indexOf(keyword) == -1) {
			keywords.add(keyword);
		}
	}

	public Keyword[] getKeywords() {
		return (Keyword[]) keywords.toArray(new Keyword[keywords.size()]);
	}

	/**
	 * A versao original tentava ver aspas, tal, mas deu um pauzinho. Daí, agora
	 * é só palavra-chave na tora.
	 * 
	 * @param query
	 *            String
	 */
	public void setQuery(String query) {
		if (query == null) {
			throw new IllegalArgumentException("Parametro inválido: text");
		}

		keywords.clear();

		String[] keys = Utils.getKeyWords(query, null, 100, false);

		for (int i = 0; i < keys.length; i++) {
			addKeyword(new Keyword(keys[i]));
		}

		/*
		 * String partialQuery = null; if (simple) { partialQuery =
		 * Utils.purifyStr(query, new char[] {'-', '"'}); } else { partialQuery
		 * = query; }
		 * 
		 * String[] keys = Utils.getLines(partialQuery, ' '); // pode apagar
		 * Utils.getKeyWords(query, null, 100); boolean term = false; String
		 * currKey = "";
		 * 
		 * // Para todas as chaves encontradas... for (int i = 0; i <
		 * keys.length; i++) { if (keys[i].equals("")) { continue; }
		 * 
		 * char firstChar = keys[i].charAt(0);
		 * 
		 * if (firstChar == '-') { if (keys[i].length() == 1) { continue; }
		 * 
		 * if (keys[i].charAt(1) == '"') { StringBuffer sb = new
		 * StringBuffer(keys[i]); sb.setCharAt(0, '"'); sb.setCharAt(1, '-');
		 * keys[i] = sb.toString(); firstChar = '"'; } }
		 * 
		 * // Se não for termo, avalia se é o início de um. Se não for,
		 * acrescenta // a palavra. if (!term) { if (firstChar == '"') { currKey
		 * = keys[i].substring(1, keys[i].length()); term = true; } else {
		 * addKeyword(new Keyword(keys[i])); } } else if
		 * (keys[i].charAt(keys[i].length() - 1) == '"') { // Se for termo,
		 * avalia se é o fim de um. term = false; addKeyword(new Keyword(currKey
		 * + " " + keys[i].substring(0, keys[i].length() - 1))); } else {
		 * currKey += " " + keys[i]; // Se for termo e não for fim de um,
		 * concatena. } }
		 * 
		 * // É pq o último ficou com ". Ex: ["leo]. if (term) { addKeyword(new
		 * Keyword(currKey)); }
		 */
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isSimple() {
		return simple;
	}

	public void setSimple(boolean simple) {
		this.simple = simple;
	}
}
