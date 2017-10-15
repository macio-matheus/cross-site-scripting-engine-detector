package utilsLib.util;

import java.io.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.Ostermiller.util.CSVParser;
import com.Ostermiller.util.CSVPrinter;

import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;

import utilsLib.jsp.JSPUtils;
import utilsLib.util.data.*;

import java.sql.*;
import java.text.*;
import java.util.*;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.Ostermiller.util.CSVParser;
import com.Ostermiller.util.CSVPrinter;

import utilsLib.jsp.JSPUtils;
import utilsLib.util.data.*;

import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.validator.UrlValidator;

/**
 * Métodos de uso geral, abrangendo strings, números, classes, etc.
 * 
 */
@SuppressWarnings({ "unused", "static-access", "rawtypes", "unchecked",
		"deprecation" })
public class Utils {
	private static int COPIES_MAX = 150;
	public static boolean COPIES_ONLY_CURRENT_PROJECT = true;

	public static int DEFAULT_READ_MAX_SIZE = 200 * 1024; // 200 kb
	public static int DEFAULT_READ_TIMEOUT = 4998;
	public static int DEFAULT_CONNECT_TIMEOUT = 5000;

	public static String[] DANGEROUS_EXTS = new String[] { "exe", "scr", "pif",
			"pac", "bat", "vbs", "vb", "reg", "link", "lnk", "php", "jsp",
			"shtml", "ssi", "pl", "com",
			/* "rar", */"cmd", /* "jar", */"js", "jse", "inf", "htaccess",
			/* interno do ticket */"ade", "adp", "bas", "chm", "cpl", "crt",
			"dll", "dat", "hlp", "hta", "htt", "ins", "isp", "mde", "msc",
			"msi", "msp", "mst", "pcd", "sc", "sct", "shb", "shs", "url",
			"vbe", "wsc", "wsf", "wsh", "tmp" };
	/**
	 * Extensões de imagens: melhor que excluir extensões perigosas é (também?)
	 * garantir quando puder apenas imagens.
	 */
	public static String[] IMAGE_EXTS = new String[] { "jpg", "jpeg", "gif",
			"bmp", "png" };

	public static char[] LETTERS;
	static {
		LETTERS = new char['z' - 'a' + 1];
		int index = 0;
		for (int i = 'a'; i < 'z' + 1; i++) {
			LETTERS[index++] = (char) i;
		}
	}

	public static String URL_ENCODE_ENC = "UTF-8"; // "ISO-8859-1"; // "Cp1252";

	// apra uso geral: antes de cristo (infito para tras.
	public static Date BC_DEFAULT_DATE;
	static {
		BC_DEFAULT_DATE = new GregorianCalendar(1982, 04, 23).getTime();
	}

	// Para trocar mensagens.
	private static MessagePool messagePool = new MessagePool();

	private static Pattern emailValidator = Pattern
			.compile(
					"^([a-zA-Z0-9_\\-])([a-zA-Z0-9_\\-\\.]*)@(\\[((25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9][0-9]|[0-9])\\.){3}|((([a-zA-Z0-9\\-]+)\\.)+))([a-zA-Z]{2,}|(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9][0-9]|[0-9])\\])$",
					Pattern.CASE_INSENSITIVE);

	private static Pattern urlValidator = Pattern
			.compile(
					// "^(http|https|ftp)\\://[a-zA-Z0-9\\-\\.]+\\.[a-zA-Z]{2,3}(:[a-zA-Z0-9]*)?/?([a-zA-Z0-9\\-\\._\\?\\,\\'/\\\\\\+&amp;%\\$#\\=~])*$",
					"^(http|https|ftp)\\://[a-zA-Z0-9_\\-\\.]+\\.[a-zA-Z]{2,3}(:[a-zA-Z0-9]*)?/?([a-zA-Z0-9()*@:!\\-\\._\\?\\,\\'/\\\\\\+&amp;%\\$#\\=~])*$",
					Pattern.CASE_INSENSITIVE);

	private static Pattern ipValidator = Pattern
			.compile(
					"^(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])$",
					Pattern.CASE_INSENSITIVE);

	// private static Pattern emailValidator =
	// Pattern.compile("^\\w(\\.?[\\w-])*@\\w(\\.?[-\\w])*\\.([a-z]{3}(\\.[a-z]{2})?|[a-z]{2}(\\.[a-z]{2})?)$",
	// Pattern.CASE_INSENSITIVE);

	private static String datePattern = "yyyy-MM-dd";

	private static String dateTimePattern = "yyyy-MM-dd HH:mm:ss";

	private static String timePattern = "HH:mm:ss";
	// a cada minuto verifica as novas tarefas agendadas, se houver, e as
	// processa.

	/**
	 * Formatações para datas e horas.
	 */
	public final static String DATE_PATTERN_D_M_Y = "dd/MM/yyyy";

	public final static String DATE_PATTERN_M_D_Y = "MM/dd/yyyy";

	public final static String DATE_PATTERN_Y_M_D = "yyyy/dd/MM";

	public final static String TIME_PATTERN_H_M_S = "HH:mm:ss";

	public final static String TIME_PATTERN_H_M_S_MS = "HH:mm:ss:SSS";

	public final static String DATE_TIME_PATTERN_M_D_Y_H_M_S = "MM/dd/yyyy HH:mm:ss";

	public final static String PATTERN_STRING_ENCODING = "ISO-8859-1";/* "ISO-8859-1" *//* Cp1252 */;

	public final static int UNDEFINED_NUMBER = Integer.MAX_VALUE;

	// public static Locale locale = new Locale("pt", "BR");;
	public static Locale locale = new Locale("pt", "BR");

	public static TimeZone timeZone = TimeZone.getTimeZone("GMT-03:00");

	/**
	 * Arredonda um inteiro pra o multiplo mais próximo passado por parametro.
	 * 
	 * ex: multiplexNumber (51, 5) return 50; multiplexNumber (53, 5) return 55
	 * 
	 * @autor Mácio
	 * @param number
	 *            é o numero a ser testado
	 * @param numberMultiplex
	 *            é o numero que é a referencia. Ex, saber se o "number" é
	 *            multiplo de "numberMultiplex"
	 * @return
	 */
	public static int multiplexNumber(int number, int numberMultiplex) {

		int tempNumber = number;
		int contInf = 0;
		int contSup = 0;
		int temp = 0;

		while ((tempNumber % 5) != 0) {
			tempNumber = tempNumber + 1;
		}
		contSup = tempNumber - number;
		temp = tempNumber;
		tempNumber = number;

		while ((tempNumber % 5) != 0) {
			tempNumber = tempNumber - 1;
		}

		contInf = number - tempNumber;

		if (contInf < contSup) {
			number = tempNumber;
		} else {
			number = temp;
		}

		return number;
	}

	/**
	 * Campo utilizado para parametrizar o método
	 * <code>compareDates(Date, Date, int)</code>, em relação ao método de
	 * comparação.
	 */
	public final static int DATE_TIME_COMPARE = 0;

	public final static int DATE_COMPARE = 1;

	public final static int TIME_COMPARE = 2;

	public static final int ALIKE_COMPARE = 0;

	public static final int EQUAL_COMPARE = 1;

	public static final int DIFFERENT_COMPARE = 2;

	public static final int GREATER_THAN_COMPARE = 3;

	public static final int GREATER_EQUAL_THAN_COMPARE = 4;

	public static final int LESS_THAN_COMPARE = 5;

	public static final int LESS_EQUAL_THAN_COMPARE = 6;

	public static final int OR_CONJUNCTION = 0;

	public static final int AND_CONJUNCTION = 1;

	public static String getConjuctionStr(int conjunction) {
		switch (conjunction) {
		case OR_CONJUNCTION: {
			return "OR";
		}
		case AND_CONJUNCTION: {
			return "AND";
		}
		default: {
			return "";
		}
		}
	}

	public static String getComparativeStr(int comparative) {
		switch (comparative) {
		case ALIKE_COMPARE: {
			return "LIKE";
		}
		case DIFFERENT_COMPARE: {
			return "NOT LIKE";
		}
		case EQUAL_COMPARE: {
			return "=";
		}
		case GREATER_THAN_COMPARE: {
			return ">";
		}
		case GREATER_EQUAL_THAN_COMPARE: {
			return ">=";
		}
		case LESS_THAN_COMPARE: {
			return "<";
		}
		case LESS_EQUAL_THAN_COMPARE: {
			return "<=";
		}
		default: {
			throw new IllegalArgumentException(
					"Parametro invalido: comparative");
		}
		}
	}

	public static void mergeArrays(Object[] dst, Object[] left, Object[] right) {
		if (left == null) {
			throw new IllegalArgumentException("Param: left");
		}

		if (right == null) {
			throw new IllegalArgumentException("Param: right");
		}

		if (dst == null) {
			throw new IllegalArgumentException("Param: dst");
		}

		if (dst.length != left.length + right.length) {
			throw new IllegalArgumentException(
					"Param: dst.length != left.length + right.length");
		}

		if (!dst.getClass().equals(right.getClass())) {
			throw new IllegalArgumentException(
					"Param: dst.getClass().equals(right.getClass())");
		}

		if (!dst.getClass().equals(left.getClass())) {
			throw new IllegalArgumentException(
					"Param: !dst.getClass().equals(left.getClass())");
		}

		System.arraycopy(left, 0, dst, 0, left.length);
		System.arraycopy(right, 0, dst, left.length, right.length);
	}

	public static void cutArray(Object[] src, Object[] dst, int start, int end) {
		if (src == null) {
			throw new IllegalArgumentException("Param: src");
		}

		if (dst == null) {
			throw new IllegalArgumentException("Param: dst");
		}

		// Se a quantidade a ser recortada é igual ao total.
		if (end - start + 1 == src.length) {
			if (dst.length != 0) {
				throw new IllegalArgumentException("Param: dst.length != 0");
			}

			return;
		}

		if (dst.length != src.length - (end - start + 1)) {
			throw new IllegalArgumentException(
					"Param: dst.length != src.length - (end - start + 1)");
		}

		if (start > end) {
			throw new IllegalArgumentException("Param: start < end");
		}

		if (start < 0) {
			throw new IllegalArgumentException("Param: start < 0");
		}

		if (src.length <= end) {
			throw new IllegalArgumentException("Param: src.length <= end");
		}

		if (!dst.getClass().equals(src.getClass())) {
			throw new IllegalArgumentException(
					"Param: !dst.getClass().equals(src.getClass())");
		}

		// Se não é para começar do começo.
		if (start > 0) {
			System.arraycopy(src, 0, dst, 0, start);
		}

		// Se possui rabo
		if (end + 1 != src.length) {
			/*
			 * System.out.println(src.length); System.out.println(dst.length);
			 * System.out.println(start); System.out.println(end);
			 */
			System.arraycopy(src, end + 1, dst, start, src.length - end - 1);
		}
	}

	public static Object getArrayItem(Object[] objs, int index,
			Object alternative) {
		if (objs == null || objs.length == 0 || objs.length <= index) {
			return alternative;
		}

		return objs[index];
	}

	/**
	 * Dado um array de String, esta função remove uma faixa de itens do array e
	 * retorna um novo.
	 * 
	 * @param strA
	 *            Array de strings para ser manipulado.
	 * @param iIni
	 *            Índice indicativo de que posição é para iniciar o corte do
	 *            array.
	 * @param iTo
	 *            Posição até a qual serão removidos os elementos do array.
	 * @return O novo array de strings.
	 */
	public static String[] cutArray(String[] strA, int iIni, int iTo) {
		if (iIni < 0 || iTo > strA.length - 1) {
			throw new IllegalArgumentException("Argumentos excedem os "
					+ "limites do array.");
		}

		if (iIni > iTo) {
			throw new IllegalArgumentException("Limite inferior maior que "
					+ "limite superior.");
		}

		int diff = iTo - iIni;
		String[] r = new String[strA.length - diff - 1];
		int i = 0;

		for (i = 0; i < iIni; i++) {
			r[i] = strA[i];
		}

		for (i = i; i < r.length; i++) {
			r[i] = strA[i + diff + 1];
		}

		return r;
	}

	public static void insertIntoArray(Object[] src, Object[] dest,
			Object element, int destIndex) {
		if (src == null) {
			throw new IllegalArgumentException("Param: src");
		}

		if (dest == null) {
			throw new IllegalArgumentException("Param: dest");
		}

		if (dest.length < src.length) {
			throw new IllegalArgumentException(
					"Param: dest.length < src.length");
		}

		System.arraycopy(src, 0, dest, 0, destIndex);
		dest[destIndex] = element;

		if (destIndex < src.length) {
			System.arraycopy(src, destIndex, dest, destIndex + 1, dest.length
					- destIndex - 1);
		}
	}

	/**
	 * Dado um array de <code>int</code>, esta função remove uma faixa de itens
	 * do array e retorna um novo.
	 * 
	 * @param strA
	 *            Array de <code>int</code>'s para ser manipulado.
	 * @param iIni
	 *            Índice indicativo de que posição é para iniciar o corte do
	 *            array.
	 * @param iTo
	 *            Posição até a qual serão removidos os elementos do array.
	 * @return O novo array de inteiros.
	 */
	public static int[] cutArray(int[] strA, int iIni, int iTo) {
		if (iIni < 0 || iTo > strA.length - 1) {
			throw new IllegalArgumentException("Argumentos excedem os "
					+ "limites do array.");
		}

		if (iIni > iTo) {
			throw new IllegalArgumentException("Limite inferior maior que "
					+ "limite superior.");
		}

		int diff = iTo - iIni;
		int[] r = new int[strA.length - diff - 1];
		int i = 0;

		for (i = 0; i < iIni; i++) {
			r[i] = strA[i];
		}

		for (i = i; i < r.length; i++) {
			r[i] = strA[i + diff + 1];
		}

		return r;
	}

	/**
	 * Divide o arquivo em vários outros, copiando linha a linha até acabar ou
	 * chegar em endAt.
	 * 
	 * @param sourceFile
	 *            Caminho do arquivo que sera fatiado.
	 * @param targetFile
	 *            Destino. Ex: c:\\issoCOUNT.txt. Deve possui uma máscara para
	 *            contagem. No caso dado, "COUNT".
	 * @param lineStartingWith
	 *            Com a linha começando por isto. Se for nulo, pega tudo.
	 * @param countMask
	 *            Máscara da contagem.
	 * @param startAt
	 *            LInha a partir da qual começar.
	 * @param endAt
	 *            Terminar na linha endAt. Se for menor ou igual a zero. só pára
	 *            quando terminar.
	 * @param linesPerFile
	 *            Máximo número de linhas por arquivo.
	 * @param listener
	 *            Ouvidor dos avanços.
	 * @return Quantidade de linhas.
	 */
	public static int splitLineFile(String sourceFile, String targetFile,
			String countMask, String lineStartingWith, int startAt, int endAt,
			int linesPerfile, DataExtractingListener listener)
			throws FileNotFoundException, IOException {
		if (sourceFile == null) {
			throw new IllegalArgumentException("Param invalido: sourceFile");
		}

		if (targetFile == null) {
			throw new IllegalArgumentException("Param: targetFile");
		}

		int linesCount = 0;
		int currFileLineCount = 0;
		int currFile = 1;
		String line = null;

		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(sourceFile), Utils.PATTERN_STRING_ENCODING));

		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(Utils.replace(targetFile, countMask,
						currFile + "", true)), Utils.PATTERN_STRING_ENCODING));

		while ((line = br.readLine()) != null) {
			linesCount++;

			if (linesCount - 1 < startAt) {
				continue;
			}

			if (endAt > 0 && linesCount >= endAt) {
				break;
			}

			if (lineStartingWith != null && !line.startsWith(lineStartingWith)) {
				continue;
			}

			currFileLineCount++;
			bw.write(line);
			bw.newLine();

			if (currFileLineCount >= linesPerfile) {
				bw.flush();
				bw.close();
				currFile++;

				currFileLineCount = 0;

				bw = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(Utils.replace(targetFile,
								countMask, currFile + "", true)),
						Utils.PATTERN_STRING_ENCODING));
			}

			if (listener != null) {
				listener.populateDataEvent(0 + "", linesCount, line);
			}
		}

		try {
			br.close();
			bw.flush();
			bw.close();
		} catch (Exception error) {
		}

		return linesCount;
	}

	public static void writeFileBytes(String filePath, byte[] bytes)
			throws IOException {
		FileOutputStream f = new FileOutputStream(filePath);

		f.write(bytes);

		f.close();
	}

	/**
	 * Obtém os bytes do arquivo.
	 * 
	 * @param filePath
	 *            String
	 * @return byte[]
	 * @throws IOException
	 */
	public static byte[] readFileBytes(String filePath) throws IOException {
		return readFileBytes(filePath, 1024);
	}

	public static byte[] readFileBytes(String filePath, int bufferSize)
			throws IOException {
		FileInputStream f = new FileInputStream(filePath);
		BufferedInputStream bis = new BufferedInputStream(f);

		int returnCount = 0;
		byte[] returnBytes = new byte[2 * bufferSize];

		byte[] readingBytes = new byte[bufferSize];

		while (true) {
			int count = f.read(readingBytes);

			if (count == -1) {
				break;
			}

			if (returnBytes.length - returnCount < count) {
				byte[] tempReturnBytes = new byte[returnCount + 2 * count];
				System.arraycopy(returnBytes, 0, tempReturnBytes, 0,
						returnCount);
				returnBytes = tempReturnBytes;
			}

			// coloca os novos bytes
			System.arraycopy(readingBytes, 0, returnBytes, returnCount, count);

			// adiciona para o final.
			returnCount += count;
		}

		bis.close();

		// agora, tira do array os itens em branco.
		if (returnCount < returnBytes.length) {
			byte[] tempReturnBytes = new byte[returnCount];
			System.arraycopy(returnBytes, 0, tempReturnBytes, 0, returnCount);
			returnBytes = tempReturnBytes;
		}

		return returnBytes;
	}

	/**
	 * Lê já particionando linhas.
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static String[] readStrLinesFile(String path) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(path), Utils.PATTERN_STRING_ENCODING));

		ArrayList<String> lines = new ArrayList<String>();

		while (true) {
			String line = br.readLine();

			if (line == null) {
				break;
			}

			lines.add(new String(line.getBytes()));
		}

		br.close();

		return (String[]) lines.toArray(new String[lines.size()]);
	}

	/**
	 * Le o arquivo, já partici8onando.
	 * 
	 * @param filePath
	 *            String
	 * @param partioning
	 *            boolean
	 * @param divisior
	 *            String
	 * @return String
	 * @throws IOException
	 */
	public static String[] readStrLinesFile(String filePath, char divisor)
			throws IOException {

		if (divisor == '\n') {
			return readStrLinesFile(filePath);
		}

		FileInputStream f = new FileInputStream(filePath);
		byte[] aBytes = new byte[1024];
		int count = 0;
		StringBuffer rStr = new StringBuffer();

		ArrayList lines = new ArrayList();

		while (true) {
			int initialSize = rStr.length();

			count = f.read(aBytes);
			if (count == -1) {
				break;
			}
			rStr.append(new String(aBytes, 0, count,
					Utils.PATTERN_STRING_ENCODING));

			for (int i = initialSize; i < rStr.length(); i++) {
				if (rStr.charAt(i) == divisor) {
					lines.add(rStr.substring(0, i));
					rStr.delete(0, i + 1);

					i = -1;
				}
			}
		}

		if (rStr.length() > 0) {
			lines.add(rStr.toString());
		}

		f.close();

		return (String[]) lines.toArray(new String[lines.size()]);
	}

	/**
	 * Método facilitador de leitura de arquivo texto.
	 * 
	 * @param filePath
	 *            Caminho do arquivo texto que se deseja ler.
	 * @return Conteúdo do arquivo lido.
	 * @throws IOException
	 */
	public static String readStrFile(String filePath) throws IOException {
		return readStrFile(filePath, Utils.PATTERN_STRING_ENCODING);
	}

	public static String readStrFile(String filePath, String charSet)
			throws IOException {
		if (filePath == null) {
			throw new IllegalArgumentException();
		}

		FileInputStream f = new FileInputStream(filePath);
		byte[] aBytes = new byte[1024];
		int count = 0;
		StringBuffer rStr = new StringBuffer();

		while (true) {
			count = f.read(aBytes);
			if (count == -1) {
				break;
			}
			rStr.append(new String(aBytes, 0, count, charSet));
		}

		f.close();

		return rStr.toString();
	}

	/**
	 * Criando um arquivo texto. Caso ele já exista, ele é sobreposto. Caso
	 * contrario, é criado.
	 * 
	 * @param filePath
	 *            Caminho do arquivo.
	 * @param content
	 *            Conteúdo a ser posto no arquivo.
	 * @throws IOException
	 *             Exceções de entrada e saída, relacionadas ao tratamento com
	 *             arquivos.
	 */
	public static void writeStrFile(String filePath, String content,
			boolean append) throws IOException {
		if (filePath == null) {
			throw new IllegalArgumentException("Parametro inválido: filePath");
		}

		if (content == null) {
			return;
		}

		FileOutputStream f = new FileOutputStream(filePath, append);
		OutputStreamWriter writer = new OutputStreamWriter(f,
				Utils.PATTERN_STRING_ENCODING);

		writer.write(content);
		writer.close();

		f.flush();
		f.close();
	}

	public static void writeStrFile(String filePath, String content)
			throws IOException {
		writeStrFile(filePath, content, false);
	}

	public static boolean isIpUrl(String url) {
		if (url == null) {
			return false;
		}

		if (!url.startsWith("http")) {
			url = "http://" + url;
		}

		int urlDataStart = 7;
		if (url.indexOf("https://") != -1) {
			urlDataStart = 8;
		}

		String domain = url.substring(urlDataStart, url.length());

		int slashPos = -1;

		if (domain.indexOf(":") == -1) {
			slashPos = domain.indexOf("/");
		} else {
			slashPos = domain.indexOf(":");
		}

		// Se existe barra ou : para porta, copia o http até antes dela.
		if (slashPos != -1) {
			domain = domain.substring(0, slashPos);
		}

		Matcher m = ipValidator.matcher(domain);
		return m.matches();
	}

	/**
	 * Testa se o e-mail passado é válido.
	 * 
	 * @param email
	 *            E-mail de teste.
	 * @return <code>True</code>, caso o e-mail seja válido.
	 */
	public static boolean isValidEmail(String email) {
		if (email == null) {
			return false;
		}

		Matcher m = emailValidator.matcher(email);
		return m.matches();
		/*
		 * boolean returnValue = true; if (email == null) { returnValue = false;
		 * } else { int atPos = email.indexOf("@"); int dotPos =
		 * email.indexOf("."); // Se for em branco, não existir @ ou se não
		 * existir . if (atPos < 1 || dotPos < 1 || email.length() < 6 || dotPos
		 * == email.length() - 1 || atPos == email.length() - 1 || dotPos ==
		 * atPos + 1 || dotPos == atPos - 1 || email.indexOf(' ') != -1 || atPos
		 * != email.lastIndexOf("@") || email.indexOf("..") != -1) { returnValue
		 * = false; } } return returnValue;
		 */
	}

	/**
	 * LImpa a URL. Retira as barras finais, espaços, etc.. Mas não retira os
	 * parametros.
	 * 
	 * @param path
	 *            Caminho a ser formatado.
	 * @return O caminho sem as barras, caso elas tenham existido.
	 */
	public static String cleanUpUrl(String path) {
		if (path == null) {
			return path;
		}

		path = path.trim();

		if (path.length() < 1 || path.equals("/")) {
			return path;
		}

		int i = 0;

		int hash = path.indexOf("#");
		if (hash != -1) {
			path = path.substring(0, hash);
		}

		boolean found = false;
		for (i = path.length() - 1; i >= 0; i--) {
			char lastChar = path.charAt(i);
			if (lastChar != '\\' && lastChar != '/') {
				break;
			}
			found = true;
		}

		if (found) {
			path = path.substring(0, i + 1);
		}

		return path;
	}

	/**
	 * Remove barras, espaços, sem tirar os parametros. Retira tambem http://,
	 * se houver.
	 * 
	 * @param url
	 *            A URL a ser limpada.
	 */
	public static String simplifyUrl(String url) {
		url = cleanUpUrl(url);

		if (url == null) {
			return null;
		}

		if (url.indexOf("http://") == 0) {
			url = url.substring("http://".length(), url.length());
		}

		return url;
	}

	/**
	 * Purifica a URL, retirando parametros e ancoras.
	 * 
	 * @param url
	 *            a ser purificada
	 * @return URL purificada.
	 */
	public static String purgeUrl(String url) {
		if (url == null) {
			throw new IllegalArgumentException("Parametro inválido: url");
		}

		int garbageIndex = url.indexOf('?');
		if (garbageIndex != -1) {
			url = url.substring(0, garbageIndex);
		} else {
			garbageIndex = 0;
		}

		garbageIndex = url.indexOf('#');
		if (garbageIndex != -1) {
			url = url.substring(0, garbageIndex);
		}

		return cleanUpUrl(url);
	}

	/**
	 * Converte uma string em um número. Caso esta string possua casas decimais,
	 * o número é arredondado.
	 * 
	 * @param num
	 *            String representativa de um número.
	 * @return Retorna o número inteiro, caso a string seja válida.
	 */
	public static int parseInt(String num) {
		if (num == null || num.equals("")) {
			throw new IllegalArgumentException("Param: " + num);
		}

		if (num.indexOf('.') != -1 || num.indexOf(',') != -1) {
			num = num.replace(',', '.');
			return Math.round(Float.parseFloat(num));
		} else {
			return Integer.parseInt(num);
		}

	}

	public static int parseInt(Object num) {
		if (num == null) {
			throw new IllegalArgumentException("Param: num");
		}

		int iNum = 0;

		// Se for inteiro, já pega; se não, converte.
		if (num instanceof Integer) {
			iNum = ((Integer) num).intValue();
		} else {
			iNum = parseInt((num + "").trim(), UNDEFINED_NUMBER);
		}

		return iNum;
	}

	/**
	 * Converte uma string em um número. Caso esta string possua casas decimais,
	 * o número é arredondado.
	 * 
	 * @param num
	 *            String representativa de um número.
	 * @return Retorna o número inteiro, caso a string seja válida.
	 */
	public static double parseDouble(String num) {
		if (num == null || num.equals("")) {
			throw new IllegalArgumentException("Param: " + num);
		}

		int dotIndex = num.indexOf('.');
		int commaIndex = num.indexOf(',');

		if (dotIndex != -1 && commaIndex != -1) {
			if (dotIndex < commaIndex) {
				num = num.substring(0, dotIndex)
						+ num.substring(dotIndex + 1, num.length());
			} else {
				num = num.substring(0, commaIndex)
						+ num.substring(commaIndex + 1, num.length());
			}
		}

		if (commaIndex != -1) {
			num = num.replace(',', '.');
		}

		return Double.parseDouble(num);
	}

	public static double parseDouble(String num, double onErrorValue) {
		try {
			return parseDouble(num);
		} catch (Exception error) {
			return onErrorValue;
		}
	}

	/**
	 * Converte um array de objetos para inteiro. Caso ocorra erro em algum dos
	 * itens, dispara exceção.
	 * 
	 * @param nums
	 *            Array que será convertido.
	 * @return Array de inteiros resultados da conversão dos objetos.
	 */
	public static int[] parseInt(Object[] nums) {
		if (nums == null) {
			throw new IllegalArgumentException("Parametro inválido: nums");
		}

		int[] rNums = new int[nums.length];

		for (int i = 0; i < nums.length; i++) {
			// Se for inteiro, já pega; se não, converte.
			if (nums[i] instanceof Integer) {
				rNums[i] = ((Integer) nums[i]).intValue();
			} else {
				rNums[i] = parseInt((nums[i] + "").trim(), UNDEFINED_NUMBER);
			}

			if (rNums[i] == UNDEFINED_NUMBER) {
				throw new IllegalArgumentException("Parametro inválido: nums. "
						+ "O valor " + nums[i] + " indexado em " + i
						+ " não é um inteiro válido.");
			}
		}

		return rNums;
	}

	/**
	 * Converte um array de objetos para inteiro. Caso ocorra erro em algum dos
	 * itens, "engole" o erro o retorna o valor passado no segundo parametro.
	 * 
	 * @param nums
	 *            Array que será convertido.
	 * @param defaultNums
	 *            Valor atribuido no caso exceção.
	 * @return Array de inteiros resultados da conversão dos objetos.
	 */
	public static int[] parseInt(Object[] nums, int[] defaultNums) {
		int[] rNums = null;
		try {
			rNums = parseInt(nums);
		} catch (Exception error) {
			rNums = defaultNums;
		}

		return rNums;
	}

	public static int[] parseInt(List list) {
		if (list == null) {
			throw new IllegalArgumentException("Parametro inválido: list");
		}

		Object[] objs = list.toArray();
		return parseInt(objs);
	}

	/**
	 * Converte uma string em um número. Caso haja qualquer erro, o número
	 * retornado é o passado no parametro via <code>onErrorValue</code>.
	 * 
	 * @param num
	 *            String representativa de um número.
	 * @param onErrorValue
	 *            Número padrão de retorno se houver erro na conversão.
	 * @return Retorna o número inteiro, caso a string seja válida.
	 */
	public static int parseInt(String num, int onErrorValue) {
		try {
			return parseInt(num);
		} catch (Exception error) {
			return onErrorValue;
		}
	}

	/**
	 * Converte uma string em um número. Caso haja qualquer erro, o número
	 * retornado é o passado no parametro via <code>onErrorValue</code>.
	 * 
	 * @param num
	 *            String representativa de um número.
	 * @param onErrorValue
	 *            Número padrão de retorno se houver erro na conversão.
	 * @return Retorna o número inteiro, caso a string seja válida.
	 */
	public static int parseInt(Object num, int onErrorValue) {
		try {
			return parseInt(num);
		} catch (Exception error) {
			return onErrorValue;
		}
	}

	/**
	 * Formata o número decimal de acordo com o número mínimo e máximo de casas
	 * decimais.
	 * 
	 * @param num
	 *            Número decimal a ser formatado.
	 * @param minIntDigits
	 *            Número mínimo de dígitos inteiros.
	 * @param maxIntDigits
	 *            Máximo de digítos inteiros.
	 * @param minFracDigit
	 *            Número mínimo de casas decimais. Caso o número possua menos, o
	 *            restante é completado com <code>0´s</code>.
	 * @param maxFracDigit
	 *            Número máximo de casas decimais. Caso o número possua mais, o
	 *            retorno é truncado.
	 * @return String representativa do número.
	 */
	public static String formatNumber(double num, int minIntDigits,
			int maxIntDigits, int minFracDigit, int maxFracDigit) {
		NumberFormat nf = NumberFormat.getInstance(locale);
		nf.setMaximumFractionDigits(maxFracDigit);
		nf.setMinimumFractionDigits(minFracDigit);
		nf.setMinimumIntegerDigits(minIntDigits);
		nf.setMaximumIntegerDigits(maxIntDigits);
		return nf.format(num);
	}

	public static String formatNumber(double num, int minFracDigit,
			int maxFracDigit) {
		return formatNumber(num, 1, 309, minFracDigit, maxFracDigit); // 1 e
		// 309
		// são
		// os
		// valores
		// padrão.
	}

	/**
	 * Função que substitui, dada uma string, um texto por outro. Retornará uma
	 * nova string, substiuindo-se a substr <code>source</code> por
	 * <code>target</code>. Caso não seja encontrada a substring
	 * <code>source</code>, retorna-se string inicial.
	 * 
	 * @param source
	 *            String que deseja-se subsutir-se algum texto por outro.
	 * @param search
	 *            Texto a ser procurado na string.
	 * @param target
	 *            Texto pelo qual será substituído o source.
	 * @param fromIndex
	 *            A partir de qual posição.
	 * @return Nova string.
	 */
	public static String replace(String source, String search, String target,
			int fromIndex) {
		if (!search.equals("")) {
			int index = source.indexOf(search, fromIndex);

			if (index != -1) {
				if (target == null) {
					target = "";
				}

				if (source.length() < 200) {
					source = source.substring(0, index) + target
							+ source.substring(index + search.length());
				} else {
					StringBuffer sb = new StringBuffer(source);
					source = sb.replace(index, index + search.length(), target)
							.toString();
				}
			}
		}

		return source;
	}

	/**
	 * Função que substitui, dada uma string, um texto por outro.
	 * 
	 * @param source
	 *            String que deseja-se subsutir-se algum texto por outro.
	 * @param search
	 *            Texto a ser procurado na string.
	 * @param target
	 *            Texto pelo qual será substituído o source.
	 * @return Retornara uma nova string, substiuindo-se a substr source por
	 *         target. Caso não seja encontrada a substr source, retorna-se str.
	 */
	public static String replace(String source, String search, String target) {
		return replace(source, search, target, 0);
	}

	/**
	 * Função que substitui substr em uma str. Possui-se a opcao de substiuir
	 * apenas a primeira ocorrência ou todas.
	 * 
	 * @param str
	 *            String que deseja-se subsutir-se algum texto por outro.
	 * @param source
	 *            Texto a ser procurado na string.
	 * @param target
	 *            Texto pelo qual será substituído o source.
	 * @param replaceAll
	 *            Se true, substitui todas as ocorrencais de source em str por
	 *            target.
	 * @return Retornara uma nova string, substiuindo-se a substr source por
	 *         target. Caso não seja encontrada a substr source, retorna-se str.
	 */
	/*
	 * angio: rotina merda public static String replace(String str, String
	 * source, String target, boolean replaceAll) { if (str.equals("") ||
	 * source.equals("")) { return str; }
	 * 
	 * String tempStr1 = str; String tempStr2 = str; int p = -1;
	 * 
	 * int substs = 0; // Faca a substituicao indicada enquanto for para
	 * substuir-se e ate // quando houver o que substituir. do { // procura a
	 * partir da última busca p = tempStr1.indexOf(source, p); // caso já não
	 * exista o source, para if (p == -1) { break; } // Substitui o texto, já
	 * sabendo atecipadamente a posição de // substituição. tempStr1 =
	 * replace(tempStr2, source, target, p); // Anda com o ponteiro a quantidade
	 * de caracteres que será postos // à frente da posição atual. p = p +
	 * target.length();
	 * 
	 * tempStr2 = tempStr1;
	 * 
	 * substs++;
	 * 
	 * if (substs % 10 == 0) { System.out.println("substs: " + substs); } }
	 * while (replaceAll);
	 * 
	 * return tempStr1; }
	 */

	public static String replace(String str, String source, String target,
			boolean replaceAll) {
		if (str.equals("") || source.equals("")) {
			return str;
		}

		StringBuffer strSb = new StringBuffer(str);
		int start = 0;

		// Faca a substituicao indicada enquanto for para substuir-se e ate
		// quando houver o que substituir.
		// O máximo de substituições possiveis é oi tamanho da string.
		for (int i = 0; i < str.length(); i++) {
			// procura a partir da última busca
			start = strSb.indexOf(source, start);

			// caso já não exista o source, pára
			if (start == -1) {
				break;
			}

			// Substitui o texto, já sabendo atecipadamente a posição de
			// substituição.
			strSb = strSb.replace(start, start + source.length(), target);

			// Anda com o ponteiro a quantidade de caracteres que será postos
			// à frente da posição atual.
			start = start + target.length();

			if (!replaceAll) {
				break;
			}
		}

		return strSb.toString();
	}

	/**
	 * Substitui todas as ocorrencias do nome de cada {@link Field} de
	 * {@link Entity} pelos seus respectivos valores. No texto de origem, os
	 * nomes dos campos devem vir acompanhados de <code>"%"</code>. Quando a
	 * substituição é feita, o valor de cada ocorrencia de <code>Field</code> é
	 * posto na string de origem.
	 * 
	 * @param text
	 *            Texto de origem.
	 * @param entity
	 *            <code>Entity</code> que possui os campos utilizados na
	 *            substituiçao.
	 * @return Retorna a string com todos "%" + nome do campo subtituídos pelos
	 *         valores dos campos.
	 */
	public static String replace(String text, Entity entity) {
		if (text == null || entity == null) {
			throw new IllegalArgumentException();
		}

		String[] fieldNames = entity.getFieldNames();
		String[] fieldValues = entity.getFieldValues();

		for (int i = 0; i < fieldNames.length; i++) {
			fieldNames[i] += "%" + fieldNames[i];
		}

		return format(text, fieldValues, fieldNames);
	}

	/**
	 * Obtém o nome dos campos dos elementos do array passado.
	 * 
	 * @param fields
	 *            Conjunto de campos a serem avaliados.
	 * @return Array contendo os nomes dos campos passados.
	 */
	public static String[] getFieldNames(Field[] fields) {
		if (fields == null) {
			return null;
		}

		String[] fieldNames = new String[fields.length];

		for (int i = 0; i < fields.length; i++) {
			fieldNames[i] = fields[i].getName();
		}

		return fieldNames;
	}

	/**
	 * Funçao que substitui a lista de argumentos string pelas ocorrencias
	 * definidas na string str. As substituicoes ocorrem onde houver "%s" em
	 * str. Tenta substuir todos os itens de arg na str, mesmo que nao haja mais
	 * "%s" para ser substiuido. A garantia da coerencia de numero de argumentos
	 * e numero de "%s" fica por conta da utilização da função.
	 * 
	 * @param str
	 *            String na qual sera feita a operacao de substituicao.
	 * @param values
	 *            Array de valores de string que substiuira a quantidade
	 *            correspondente de ocorrencias de "%s" em str.
	 * @return Retorna a nova string
	 */
	public static String format(String str, String[] values) {
		int i;
		for (i = 0; i < values.length; i++) {
			str = replace(str, "%s", values[i], true);
		}
		return str;
	}

	public static String format(String str, String[] values, String[] ids) {
		return format(str, values, ids, true);
	}

	/**
	 * 
	 * @param str
	 *            String original.
	 * @param values
	 *            Valores que substituirão.
	 * @param ids
	 *            O que procurar.
	 * @param replaceAll
	 *            Se é para substituir tudo ou parar no primeiro.
	 * @return String substituída.
	 */
	public static String format(String str, String[] values, String[] ids,
			boolean replaceAll) {
		if (str == null || values == null || ids == null) {
			throw new IllegalArgumentException("[Utils](format)"
					+ "Argumentos inválidos.");
		}

		if (values.length != ids.length) {
			throw new IllegalArgumentException("[Utils](format)"
					+ "Os valores e os identificadores devem possuir o"
					+ " mesmo tamanho.");
		}

		int i;
		for (i = 0; i < values.length; i++) {
			str = replace(str, ids[i], values[i], replaceAll);
		}
		return str;
	}

	/**
	 * Formata a string com texto alternativo caso a string esteja vazia. Ela
	 * está vazia se for nulo ou, com trim, estiver vazio ("").
	 * 
	 * @param str
	 *            String a ser formatada.
	 * @param alternative
	 *            Texto alternativo.
	 * @return Retorna a string ou o texto alternativo.
	 */
	public static String formatIfEmpty(String str, String alternative) {
		str = formatIfNull(str);

		return (str.trim().equals("")) ? alternative : str;
	}

	/**
	 * Formata uma string para, caso ela seja <code>null</code>, retornar
	 * <code>""</code>.
	 * 
	 * @param str
	 *            String que se deseja avaliar.
	 * @return String formatada quanto a nulidade.
	 */
	public static String formatIfNull(String str) {
		return (str == null) ? "" : str;
	}

	/**
	 * Formata uma string para, caso ela seja <code>null</code>, retornar
	 * <code>""</code>.
	 * 
	 * @param str
	 *            String que se deseja avaliar.
	 * @return String formatada quanto a nulidade.
	 */
	public static String formatIfNull(Object str) {
		return (str == null) ? "" : str + "";
	}

	/**
	 * Formata uma string para e , caso ela seja <code>null</code>, retorna o
	 * valor do parametro passado.
	 * 
	 * @param str
	 *            String que se deseja avaliar.
	 * @param alternative
	 *            Alternativa caso a string seja nula
	 * @return String formatada quanto a nulidade.
	 */
	public static String formatIfNull(Object str, Object alternative) {
		alternative = (alternative == null) ? "" : alternative;

		return (str == null) ? alternative + "" : str + "";
	}

	/**
	 * Realiza a concatenação repetida de uma determinada <code>String</code>.
	 * 
	 * @param num
	 *            Número de repetições.
	 * @param atomStr
	 *            <code>String</code> a ser concatenada em repetições.
	 * @return <code>String</code> com o contendo a concatenação da
	 *         <code>String</code> base. Caso o número de repetições for menor
	 *         que 1, retorna <code>""</code>.
	 */
	public static String repeatStr(int num, String atomStr) {
		String ident = "";
		int i;
		for (i = 1; i <= num; i++) {
			ident += atomStr;
		}
		return ident;
	}

	/**
	 * <p>
	 * Formata a data dada para os seus valores serem colocados em um padrão de
	 * string.
	 * </p>
	 * 
	 * @param date
	 *            Data da qual serão extraídos os valores quistos.
	 * @param pattern
	 *            Padrão de String contendo o padrão.
	 * @return String formatada.
	 */
	public static String dateToStr(Date date, String pattern) {
		if (date == null || pattern == null) {
			throw new IllegalArgumentException();
		}

		SimpleDateFormat sdf = new SimpleDateFormat(pattern, locale);
		sdf.setTimeZone(timeZone);

		return sdf.format(date);
	}

	/**
	 * Converte a data dada para um formato específico, de acordo com o índice
	 * dado. Os formatos disponíveis são: <br>
	 * a) dateFormat: 0: "dd-mes/yyyy". Ex: 01-fev/2004 1: "dia da semana, dd de
	 * mês de aaaa". Ex: segunda, 31 de março de 2004 2: "dd de mês de aaaa".
	 * Ex: 31 de março de 2004 3: "mês de aaaa". Ex: março de 2004 4:
	 * "dd de mês". Ex: 31 de março 5: mes/yyyy: jan/2009 6: mes/yyyy:
	 * janeiro/2009 b) outro retorna nulo;
	 * 
	 * @param date
	 *            Data para converter.
	 * @param dateFormat
	 *            Índice do formato dado.
	 * @return
	 */
	public static String dateToStr(Date date, int dateFormat) {
		if (date == null) {
			new IllegalArgumentException("Param: date");
		}

		if (dateFormat != 0 && dateFormat != 1) {
			new IllegalArgumentException("Param: dateFormat. Valor: "
					+ dateFormat);
		}

		String completeDateStr = null;

		switch (dateFormat) {
		case 0:
			completeDateStr = Utils.dateToStr(date, "dd-#/yyyy");
			completeDateStr = Utils.replace(completeDateStr, "#",
					Utils.getAbrevMonth(date));
			break;

		case 1: {
			GregorianCalendar gc = Utils.getGregorianCalendarInstance();
			gc.setTime(date);
			completeDateStr = Utils.getDayOfWeekName(gc.get(gc.DAY_OF_WEEK))
					.toLowerCase();
			completeDateStr += ", ";
			completeDateStr += gc.get(gc.DAY_OF_MONTH) + " de ";
			completeDateStr += Utils.getMonthName(date) + " de ";
			completeDateStr += gc.get(gc.YEAR);
			break;
		}

		case 2: {
			GregorianCalendar gc = Utils.getGregorianCalendarInstance();
			gc.setTime(date);
			completeDateStr = gc.get(gc.DAY_OF_MONTH) + " de ";
			completeDateStr += Utils.getMonthName(date) + " de ";
			completeDateStr += gc.get(gc.YEAR);
			break;

		}

		case 3: {
			GregorianCalendar gc = Utils.getGregorianCalendarInstance();
			gc.setTime(date);
			completeDateStr = Utils.getMonthName(date) + " de ";
			completeDateStr += gc.get(gc.YEAR);
			break;
		}

		case 4: {
			GregorianCalendar gc = Utils.getGregorianCalendarInstance();
			gc.setTime(date);
			completeDateStr = gc.get(gc.DAY_OF_MONTH) + " de ";
			completeDateStr += Utils.getMonthName(date);
			break;
		}

		case 5: {
			String just0 = Utils.dateToStr(date, 0);
			completeDateStr = just0.substring(just0.indexOf('-') + 1);

			break;
		}
		case 6: {
			GregorianCalendar gc = Utils.getGregorianCalendarInstance();
			gc.setTime(date);
			completeDateStr = Utils.getMonthName(date) + "/";
			completeDateStr += gc.get(gc.YEAR);
			break;
		}

		case 7: {
			GregorianCalendar gc = Utils.getGregorianCalendarInstance();
			gc.setTime(date);

			if (gc.get(gc.MONTH) < 10) {
				completeDateStr = "0";
			} else {
				completeDateStr = "";
			}

			completeDateStr += (gc.get(gc.MONTH) + 1) + "/";

			completeDateStr += gc.get(gc.YEAR);

			break;
		}

		}

		return completeDateStr;
	}

	/**
	 * Converte uma data para uma string contendo apenas a data, utilizando o
	 * padrao estabelecido. Um formato exemplo do que será retornada dd/mm/yyyy,
	 * não sendo necessariamente este, o que depende do padrão. Este padrao é
	 * estabelecido por setDatePatter();
	 * 
	 * @param date
	 *            Data a ser convertida.
	 * @return String da conversão.
	 */
	public static String dateToStr(Date date) {
		return dateToStr(date, getDatePattern());
	}

	/**
	 * Converte uma data para uma string contendo a data e hora, utilizando o
	 * padrao estabelecido. Um formato exemplo do que será retornada dd/mm/yyyy
	 * hh/mm/ss, não sendo necessariamente este, o que depende do padrão. Este
	 * padrao é estabelecido por setDateTimePatter();
	 * 
	 * @param date
	 *            Data a ser convertida.
	 * @return String da conversão.
	 */
	public static String dateTimeToStr(Date date) {
		return dateToStr(date, getDateTimePattern());
	}

	public static String timeToStr(Date date) {
		return dateToStr(date, getTimePattern());
	}

	public static String dateToStr(GregorianCalendar date, String pattern) {
		if (date == null || pattern == null) {
			throw new IllegalArgumentException();
		}

		return dateToStr(date.getTime(), pattern);
	}

	/**
	 * Converte o objeto dado em um {@link GregorianCalendar}, caso seja um
	 * objeto válido, como por exemplo <code>Date</code>.
	 * 
	 * @param date
	 *            Data a ser convertida.
	 * @return (@link GregorianCalendar) recem-gerado.
	 */
	public static GregorianCalendar dateToGregorian(Date date) {
		if (date == null) {
			throw new IllegalArgumentException();
		}

		GregorianCalendar g = Utils.getGregorianCalendarInstance();
		g.setTime((Date) date);
		return g;
	}

	/**
	 * Converte uma data, eliminando a hora dela.
	 * 
	 * @param date
	 *            Data a ser truncada.
	 * @return Data com a hora 00:00:00.
	 */
	public static Date truncateToDate(Date date) {
		if (date == null) {
			throw new IllegalArgumentException("Parametro inválido: date");
		}
		return Utils.strToDate(Utils.dateToStr(date, Utils.getDatePattern()),
				Utils.getDatePattern());
	}

	/**
	 * Converte uma data, eliminando a hora dela.
	 * 
	 * @param date
	 *            Data a ser truncada.
	 * @return Data com a hora 00:00:00.
	 */
	public static Date truncateToTime(Date date) {
		if (date == null) {
			throw new IllegalArgumentException("Parametro inválido: date");
		}
		return Utils.strToDate(Utils.dateToStr(date, Utils.getTimePattern()),
				Utils.getTimePattern());
	}

	public static Date strToDate(String dateStr) {
		return strToDate(dateStr, getDatePattern());
	}

	public static Date strToDateTime(String dateStr) {
		return strToDate(dateStr, getDateTimePattern());
	}

	public static Date strToTime(String dateStr) {
		return strToDate(dateStr, getTimePattern());
	}

	/**
	 * Formata a data dada para os seus valores serem colocados em um padrão de
	 * string.
	 * 
	 * @param dataStr
	 *            Data da qual serão extraídos os valores quistos.
	 * @param pattern
	 *            Padrão de String contendo o padrão.
	 * @return String formatada.
	 */
	public static Date strToDate(String dataStr, String pattern) {
		if (dataStr == null) {
			return null;
		}

		if (pattern == null) {
			throw new IllegalArgumentException("Padrão inválido.");
		}

		SimpleDateFormat sdf = new SimpleDateFormat(pattern, locale);
		sdf.setTimeZone(timeZone);

		try {
			return sdf.parse(dataStr);
		} catch (ParseException error) {
			throw new RuntimeException("Erro ao fazer o parser da data. Date: "
					+ dataStr + " Erro: " + error.getMessage());
		}
	}

	public static Date strToDate(String dataStr, String pattern,
			Date defaultDate) {
		Date rDate = null;

		try {
			rDate = Utils.strToDate(dataStr, pattern);
		} catch (Exception error) {

		}

		if (rDate == null) {
			rDate = defaultDate;
		}

		return rDate;
	}

	public static Date strToDate(String dataStr, Date defaultDate) {
		Date rDate = null;

		try {
			rDate = Utils.strToDate(dataStr);
		} catch (Exception error) {

		}

		if (rDate == null) {
			rDate = defaultDate;
		}

		return rDate;
	}

	public static GregorianCalendar strToGregorian(String dataStr,
			String pattern) {
		Date date = strToDate(dataStr, pattern);
		return dateToGregorian(date);
	}

	public static Date objToDate(Object o, String pattern) {
		if (o == null) {
			throw new IllegalArgumentException("Parametro inválido: o.");
		}
		if (pattern == null) {
			throw new IllegalArgumentException("Parametro inválido: pattern.");
		}

		if (o instanceof Date) {
			return (Date) o;
		} else if (o instanceof String) {
			return Utils.strToDate(o + "", pattern);
		} else {
			try {
				return Utils.strToDate(o + "", pattern);
			} catch (Exception error) {
				throw new IllegalArgumentException("Objeto é de uma classe "
						+ "inválida.");
			}
		}
	}

	public static GregorianCalendar objToGregorian(Object o, String pattern) {
		if (o == null || pattern == null) {
			throw new IllegalArgumentException();
		}

		if (o instanceof GregorianCalendar) {
			return (GregorianCalendar) o;
		} else if (o instanceof Date) {
			return dateToGregorian((Date) o);
		} else if (o instanceof String) {
			return Utils.strToGregorian(o + "", pattern);
		} else {
			throw new IllegalArgumentException("Objeto é de uma classe "
					+ "inválida.");
		}
	}

	/**
	 * <p>
	 * Obtém o caminho da string dada.
	 * </p>
	 * 
	 * <p>
	 * Por exemplo, se for passado <code>C:/dir/arquivo.txt</code>, esta função
	 * retornará <code>C:/dir</code>. Note que se for passado
	 * <code>C:/arquivo.txt</code>, será retornado <code>C:</code>.
	 * </p>
	 * 
	 * @param filePath
	 *            Um caminho de arquivo.
	 * @return Diretório.
	 */
	public static String getPath(String filePath) {
		String path = filePath + "";

		if (filePath == null) {
			throw new IllegalArgumentException();
		}

		int slashPos = filePath.lastIndexOf('/');
		if (slashPos == -1) {
			slashPos = filePath.lastIndexOf('\\');
		}

		if (slashPos != -1) {
			path = filePath.substring(0, slashPos);
		}

		return path;
	}

	public static String getDatePattern() {
		return datePattern;
	}

	public static String getDateTimePattern() {
		return dateTimePattern;
	}

	public static void setDatePattern(String pattern) {
		if (pattern == null) {
			throw new IllegalArgumentException();
		}

		datePattern = pattern;
	}

	public static String getDayOfWeekName(int dayOfWeek) {
		switch (dayOfWeek) {
		case 1:
			return "Domingo";
		case 2:
			return "Segunda";
		case 3:
			return "Terça";
		case 4:
			return "Quarta";
		case 5:
			return "Quinta";
		case 6:
			return "Sexta";
		case 7:
			return "Sábado";
		default:
			throw new IllegalArgumentException("O dia da semana "
					+ "deve estar entre 1 e 7, sendo que 1 representa domingo.");
		}
	}

	public static String getDayOfWeekName(Date date) {
		GregorianCalendar gc = Utils.getGregorianCalendarInstance();
		gc.setTime(date);
		int dayOfWeek = gc.get(gc.DAY_OF_WEEK);

		switch (dayOfWeek) {
		case 1:
			return "Domingo";
		case 2:
			return "Segunda";
		case 3:
			return "Terça";
		case 4:
			return "Quarta";
		case 5:
			return "Quinta";
		case 6:
			return "Sexta";
		case 7:
			return "Sábado";
		default:
			throw new IllegalArgumentException("O dia da semana "
					+ "deve estar entre 1 e 7, sendo que 1 representa domingo.");
		}
	}

	/**
	 * Obtém um array de objetos a partir de um <code>Iterator</code>.
	 * 
	 * @param its
	 *            <code>Iterator</code> do qual serão obtidos os objetos.
	 * @return Array contendo todos os elementos do <code>Iterator</code>.
	 */
	public static Object[] getArray(Iterator its) {
		return null;
	}

	/**
	 * <p>
	 * Formata textos longos de tal forma a quebrá-lo em um bloco de texto
	 * compreensível. Esta operação é util para URLs e caminhos de arquivos, por
	 * exemplo. Ele só efetua a formatação se ela for necessária.
	 * </p>
	 * <p>
	 * Ex: Para formatar URLs, chamando este método<br>
	 * <code>format("http://www.google.com", "/../", 5, 10);</code><br>
	 * retornará <code>"http:/../google.com"</code><br>
	 * .
	 * 
	 * @param text
	 *            Texto a ser formatado.
	 * @param extensionStr
	 *            Texto a ser formatado caso o texto seja quebrado. Se se passar
	 *            <code>null</code>, é utilizado o padrão <code>"/../"</code>.
	 * @param maxSize
	 *            Tamanho até o qual o texto não será formatado.
	 * @param ratio
	 *            Proporção da divisão do texto. A relação é começo/final.
	 * @return Retorna o texto formatado, se for necessário.
	 */
	public static String formatLongText(String text, String extensionStr,
			int maxSize, double ratio) {
		if (text == null) {
			return "";
		}

		// Se for vazio, coloca o valor padrão.
		if (extensionStr == null) {
			extensionStr = "/../";
		}

		String returnText = "";

		// Se não precisar formatar... caso contrário, formata.
		if (text.length() <= maxSize) {
			returnText = text;
		} else {
			maxSize -= extensionStr.length();
			// Posição mjáxima do bloco da esquerda
			int leftEnd = (int) (maxSize * ratio);
			// Posição de iniício do bloco da direita.
			int rightStart = (int) (maxSize * (1.0 - ratio));

			returnText = text.substring(0, leftEnd);
			returnText += extensionStr;
			returnText += text.substring(text.length() - rightStart,
					text.length());
		}

		return returnText;
	}

	/**
	 * <p>
	 * Extrai o domínio da url.
	 * </p>
	 * <p>
	 * Por exemplo, se se passar <code>http://www.google.com/isso=423432</code>
	 * será retornado <code>www.google.com</code>.
	 * </p>
	 * .
	 * 
	 * @param httpUrl
	 *            URL cujo domio sera extraido.
	 * @return <code>String</code> com o dominio.
	 */
	public static String extractDomainName(String httpUrl) {
		testHttpUrlValidity(httpUrl);

		int urlDataStart = 7;
		if (httpUrl.indexOf("https://") != -1) {
			urlDataStart = 8;
		}

		String http = httpUrl.substring(urlDataStart, httpUrl.length());
		int slashPos = http.indexOf("/");

		// Se existe barra, copia o http até antes dela.
		if (slashPos != -1) {
			http = http.substring(0, slashPos);
		}

		return http;
	}

	/**
	 * Remove o domínio da url.
	 * 
	 * @param httpUrl
	 *            URL da qual o domínio sera removido.
	 * @return O que sobra da URL, tendo sido removido o domínio.
	 */
	public static String removeDomainName(String httpUrl) {
		String domain = extractDomainName(httpUrl);
		return httpUrl.substring(httpUrl.indexOf(domain) + domain.length(),
				httpUrl.length());
	}

	public static boolean isValidUrl(String url) {
		if (url == null) {
			return false;
		}

		if (url.indexOf("www.") != -1) {
			url = Utils.replace(url, "www.", "");
		}

		Matcher m = urlValidator.matcher(url);
		return m.matches();
	}

	/**
	 * Avalia se a validade de uma url, lançado IllegalArgumentException caso
	 * não seja.
	 * 
	 * @param httpUrl
	 *            A ser avaliada.
	 */
	public static void testHttpUrlValidity(String httpUrl) {
		if (httpUrl == null) {
			throw new IllegalArgumentException("Parametro inválido: " + httpUrl);
		}

		if (!Utils.isValidUrl(httpUrl)) {
			throw new IllegalArgumentException("url inválida: " + httpUrl);
		}

		/*
		 * // Se contém o protocolo if (httpUrl.length() < "http://".length() +
		 * 1 && httpUrl.length() < "https://".length() + 1) { throw new
		 * IllegalArgumentException( "URL inválida (" + httpUrl + "). Ela " +
		 * "provavelmente não possui http:// ou não possui nenhum dado além
		 * disso."); } // Se contém o protocolo if (!httpUrl.substring(0,
		 * "http://".length()).equals("http://") && !httpUrl.substring(0,
		 * "https://".length()) .equals("https://")) { throw new
		 * IllegalArgumentException("URL inválida (" + httpUrl + "). Ela " +
		 * "deve conter http:// ou https://"); }
		 * 
		 * int fromWhereDotIsValid = 0;
		 * 
		 * if (httpUrl.indexOf("www.") != -1) { fromWhereDotIsValid =
		 * httpUrl.indexOf("www.") + "www.".length(); }
		 * 
		 * int dotPos = httpUrl.indexOf(".", fromWhereDotIsValid);
		 * 
		 * if (dotPos == -1) { throw new
		 * IllegalArgumentException("URL inválida (" + httpUrl + "). Ela " +
		 * "tem que conter um ponto."); } if (!(dotPos > "https://".length() &&
		 * dotPos > "http://".length() && dotPos < httpUrl .length() - 1)) {
		 * throw new IllegalArgumentException("URL inválida (" + httpUrl +
		 * "). Ela " + "tem que conter o caminho."); }
		 */
	}

	/**
	 * Testa se o intervalo é válido. Se as datas forem iguais, o intervalo é
	 * válido.
	 * 
	 * @param startDate
	 *            Data de início do intervalo.
	 * @param endDate
	 *            Data do fim do intervalo.
	 * @return Retorna se o intervalo é válido.
	 * @trhows Caso se passe parametros nulos, sao disparadas exceções de
	 *         invalidade dos parametros.
	 */
	public static boolean isValidDateInterval(Date startDate, Date endDate) {
		boolean returnValue = true;

		if (startDate == null) {
			throw new IllegalArgumentException("Deve-se ter a data de início");
		}

		if (endDate == null) {
			throw new IllegalArgumentException("Deve-se ter a data de fim");
		}

		// se não forem iguais e a data de início estiver depois da final, é
		// inválida.
		if (Utils.compareDates(startDate, endDate, Utils.DATE_COMPARE) > 0) {
			returnValue = false;
		}

		return returnValue;
	}

	// ******************************************************************************
	// * Funçoes de tags
	// ******************************************************************************
	/**
	 * Percorre o array de caracteres, a partir de uma dada posição, até
	 * encontrar um caracter que determine o fim do token: parametro, fim de
	 * tag.
	 * 
	 * @param chs
	 *            Array de caracteres a ser avaliado.
	 * @param start
	 *            Posiçao inicial.
	 * @return Posição do fim token.
	 */
	public static int goToEndCharToken(char[] chs, int start) {
		if (chs == null) {
			throw new IllegalArgumentException("Parâmetro inválido: chs");
		}

		int end = start;
		for (; end < chs.length && chs[end] != ' ' && chs[end] != '='; end++) {
			;
		}
		if (end == start || end == start) {
			end = -1;
		}
		return end;
	}

	/**
	 * Percorre o array de caracteres, a partir de uma dada posição, até
	 * encontrar um caracter que determine o início do token, ignorando-se
	 * espaços, quebras, etc.
	 * 
	 * @param chs
	 *            Array de caracteres a ser avaliado.
	 * @param start
	 *            Posiçao inicial.
	 * @return Posição do início token.
	 */
	public static int goToNextStartCharToken(char[] chs, int start) {
		if (chs == null) {
			throw new IllegalArgumentException("Parâmetro inválido: tag");
		}

		int next = start;
		for (; next < chs.length && chs[next] == ' '; next++) {
			;
		}
		if (next == chs.length) {
			next = -1;

		}
		return next;
	}

	/**
	 * Dada uma tag, retorna o nome dela. O método pré-supõe que o que está
	 * recebendo é uma tag, corretamente definida, e que tenha fim.
	 * 
	 * @param tag
	 *            Tag no formato <code>"nomeTag..."</code>
	 * @return <code>String</code> contendo o nome da Tag.
	 */
	public static String getTagName(String tag) {
		if (tag == null) {
			throw new IllegalArgumentException("Parâmetro inválido: tag.");
		}
		tag = tag.substring(1, tag.length() - 1);

		String tagName = "";
		char[] chs = tag.toCharArray();
		int pos = goToEndCharToken(chs, 0);
		tagName = new String(chs, 0, pos);

		return tagName;
	}

	/**
	 * <p>
	 * Distribui os valores em um número <code>n</code> de arrays, pegando o
	 * primeiro valor do array passado e colocando no primeiro array, o segunbdo
	 * no segundo array, e assim por diante.
	 * </p>
	 * <p>
	 * Por exemplo, se se passar {1, 2, 3, 4, 5, 6} e passar um array de dois
	 * arrays de números, sera retornado {1, 3, 5} e {2, 4, 6}. Se for passado
	 * três arrays, será retornado {1, 4}, {2, 5} e {3, 6}.
	 * </p>
	 * <p>
	 * As checagens de validades, tipos e de tamanho fica a cargo do utilizador
	 * do método. Uma forma de verificar isto é que a multiplicação das
	 * dimensões do array deve ser igual a quantidade de elementos em
	 * sourceObjs.
	 * </p>
	 * 
	 * @param sourceObjs
	 *            Fonte dos objetos, dos quais serão extraídos os valores.
	 * @param targetObjs
	 *            Array de array de objetos destino.
	 * @return Retorna um array de arrays com os valores distribuídos de acordo
	 *         com o número de arrays passado.
	 */
	public static Object[][] distributeElements(Object[] sourceObjs,
			Object[][] targetObjs) {
		if (sourceObjs == null) {
			throw new IllegalArgumentException("Parametro inválido: sourceObjs");
		}

		if (sourceObjs.length == 0) {
			return targetObjs;
		}

		if (targetObjs == null) {
			throw new IllegalArgumentException("Parametro inválido: targetObjs");
		}

		if (targetObjs.length == 0) {
			throw new IllegalArgumentException(
					"Parametro inválido: targetObjs.length. Ele deve ser arrays criados.");
		}

		if (sourceObjs.length != targetObjs.length * targetObjs[0].length) {
			throw new IllegalArgumentException(
					"Parametro inválido: targetObjs. Ele deve ser um cartesiano que contemple todos os elementos de sourceObjs.");
		}

		int in = 0;
		int out = 0;

		for (int i = 0; i < sourceObjs.length; i++) {
			targetObjs[in++][out] = sourceObjs[i];

			if (in == targetObjs.length) {
				out++;
				in = 0;
			}
		}

		return targetObjs;
	}

	/**
	 * Chama {@link getLines(String, char, boolean} sem juntar as frases.
	 * 
	 * @param text
	 *            Texto a ser quebrado.
	 * @param divisor
	 *            Divisor.
	 * @return Texto quebrado.
	 */
	public static String[] getLines(String text, char divisor) {
		return getLines(text, divisor, false, null);
	}

	public static String[] getLines(String text, String separator) {
		if (text == null) {
			throw new IllegalArgumentException("Param: text");
		}

		if (separator == null) {
			throw new IllegalArgumentException("Param: separator");
		}

		StringTokenizer st = new StringTokenizer(text, separator, true);
		ArrayList al = new ArrayList();

		while (st.hasMoreTokens()) {
			String value = st.nextToken();

			if (!value.equals(separator)) {
				al.add(value);

				// delimitador, joga fora.
				if (st.hasMoreTokens()) {
					st.nextToken();
				} else {
					break;
				}
			} else {
				al.add("");
			}
		}

		return (String[]) al.toArray(new String[al.size()]);
	}

	/**
	 * Obtém cada linha do texto dado de acordo com o divisor dado. Por exemplo,
	 * caso o divisor seja <code>'\n'</code> e a string dada for formada por um
	 * texto dividido em quebras de linhas, o valor retornado é cada uma das
	 * substrings delimitadas entre as quebras.
	 * 
	 * @param text
	 *            Texto a ser analisado.
	 * @param divisor
	 *            Divisor das linhas.
	 * @param phraseMerge
	 *            Se true, junta as frases indicadas por <code>"</code>. Por
	 *            exemplo, se o divisor é <code>" "</code>, a string
	 *            <code>palavra "palavra 2"</code> dará em duas linhas se este
	 *            parametro for <code>true</code> e tres se for
	 *            <code>false</code>.
	 * @param valueDefiner
	 *            Instancia de objeto para configurar a linha antes de colocá-la
	 *            na lista das linhas e para definir se a linha deve ou não
	 *            entrar na lista das linhas. Pode ser nulo, ou seja, toda linha
	 *            encontrada entra como ela é encontrada.
	 * @return Array contendo as linhas, caso elas existam.
	 */
	public static String[] getLines(String text, char divisor,
			boolean phraseMerge, ValueDefiner valueDefiner) {
		if (text == null) {
			throw new IllegalArgumentException("Parâmetro inválido: text");
		}

		if (text.length() == 0) {
			return new String[0];
		}

		StringBuffer buff = new StringBuffer(text);

		// Se na última posição não houver divisor, coloca-o.
		if (buff.charAt(buff.length() - 1) != divisor) {
			buff.append(divisor);
		}

		ArrayList al = new ArrayList();
		int last = 0;
		for (int i = 0; i < buff.length(); i++) {
			if (phraseMerge && buff.charAt(i) == '"') { // Caso seja para
				// considerar as frases
				// e o caracter atual
				// for uma aspas, então
				// encontra a próxima
				// aspas.
				for (i++; i < buff.length() - 1; i++) { // Corre o buffer, desde
					// a letra seguinte
					// (para não ser aspas)
					// até a penúltima
					// letra, verificando se
					// se trata do fim das
					// aspas. isso dará o
					// offset necessario.
					if (buff.charAt(i) == '"') { // Se for aspas, encontrou
						// as aspas e vai ao próximo
						// caracter. para análise.
						i++;
						break;
					}
				}

			}

			if (buff.charAt(i) == divisor) { // Se for o divisor, acrescenta
				// texto e quebra.
				if (valueDefiner == null) { // Se exisitir checador e ele
					// aprovar, então acrescenta a
					// linha.
					al.add(text.substring(last, i));
				} else {
					try {
						al.add(valueDefiner.defineValue(text.substring(last, i)));
					} catch (IllegalArgumentException error) {
						/* É porque não é para acrescentá-la */
					}
				}
				last = i + 1;
			}
		}

		return (String[]) al.toArray(new String[al.size()]);
	}

	/**
	 * Obtém o nome abreviado do mes
	 * 
	 * @param date
	 *            Data da qual se deseja extrair o nome do mes.
	 * @return Mes abreviado.
	 */
	public static String getAbrevMonth(Date date) {
		if (date == null) {
			throw new IllegalArgumentException("Parametro inválido: date");
		}

		GregorianCalendar gc = dateToGregorian(date);

		return getAbrevMonth(gc.get(gc.MONTH));
	}

	/**
	 * Obtém o nome abreviado do mes
	 * 
	 * @param month
	 *            0 é janeiro, 1 é fevereio e assim por diante.
	 * @return Mes abreviado.
	 */
	public static String getAbrevMonth(int month) {
		String returnMonth = "";

		switch (month) {
		case 0:
			returnMonth = "Jan";
			break;
		case 1:
			returnMonth = "Fev";
			break;
		case 2:
			returnMonth = "Mar";
			break;
		case 3:
			returnMonth = "Abr";
			break;
		case 4:
			returnMonth = "Mai";
			break;
		case 5:
			returnMonth = "Jun";
			break;
		case 6:
			returnMonth = "Jul";
			break;
		case 7:
			returnMonth = "Ago";
			break;
		case 8:
			returnMonth = "Set";
			break;
		case 9:
			returnMonth = "Out";
			break;
		case 10:
			returnMonth = "Nov";
			break;
		case 11:
			returnMonth = "Dez";
			break;
		}

		return returnMonth;
	}

	/**
	 * Obtém o nome do mes
	 * 
	 * @param date
	 *            Data da qual se deseja extrair o nome do mes.
	 * @return Mes.
	 */
	public static String getMonthName(Date date) {
		if (date == null) {
			throw new IllegalArgumentException("Parametro inválido: date");
		}

		GregorianCalendar gc = dateToGregorian(date);

		return getMonthName(gc.get(gc.MONTH));
	}

	/**
	 * Obtém o nome do mes
	 * 
	 * @param month
	 *            0 é janeiro, 1 é feveiroo e assim por diaten.
	 * @return Mes.
	 */
	public static String getMonthName(int month) {
		String returnMonth = "";

		switch (month) {
		case 0:
			returnMonth = "Janeiro";
			break;
		case 1:
			returnMonth = "Fevereiro";
			break;
		case 2:
			returnMonth = "Março";
			break;
		case 3:
			returnMonth = "Abril";
			break;
		case 4:
			returnMonth = "Maio";
			break;
		case 5:
			returnMonth = "Junho";
			break;
		case 6:
			returnMonth = "Julho";
			break;
		case 7:
			returnMonth = "Agosto";
			break;
		case 8:
			returnMonth = "Setembro";
			break;
		case 9:
			returnMonth = "Outubro";
			break;
		case 10:
			returnMonth = "Novembro";
			break;
		case 11:
			returnMonth = "Dezembro";
			break;
		}

		return returnMonth;
	}

	/**
	 * Obtém o nome do ano
	 * 
	 * @param date
	 *            Data da qual se deseja extrair o nome do mes.
	 * @return Ano.
	 */

	public static int getYearName(Date date) {
		if (date == null) {
			throw new IllegalArgumentException("Parametro inválido: date");
		}

		GregorianCalendar gc = dateToGregorian(date);
		return gc.get(gc.YEAR);
	}

	/**
	 * <p>
	 * Compara a cronologia de duas datas, podendo-se definir o método com o
	 * qual se realiza a comparação.
	 * </p>
	 * <p>
	 * O método é definido pelo argumento <code>compareMethod</code>, sendo dos
	 * seguintes tipos:
	 * </p>
	 * - 0: Comparação cronológica completa, tanto por data como pelo horário;<br>
	 * - 1: Comparação cronológica apenas pela data;<br>
	 * - 2: Apenas pelo horário.
	 * 
	 * @param date
	 *            Data fonte da comparação.
	 * @param dateToCompare
	 *            Data com a qual se deseja comparar.
	 * @param compareMethod
	 *            Método da comparação.
	 * @return <code>0</code>, caso as datas sejam iguais; menor que
	 *         <code>0</code> se a data fonte for antes e maior se a data fonte
	 *         for depois da que se deseja comparar.
	 */
	public static int compareDates(Date date, Date compareDate,
			int compareMethod) {
		if (date == null) {
			throw new IllegalArgumentException("Parametro inválido: date");
		}

		if (compareDate == null) {
			throw new IllegalArgumentException(
					"Parametro inválido: compareDate");
		}

		if (compareMethod != DATE_TIME_COMPARE && compareMethod != DATE_COMPARE
				&& compareMethod != TIME_COMPARE) {
			throw new IllegalArgumentException("Parametro inválido: "
					+ "compareMethod. Ele deve ser 0, 1 ou 2.");
		}

		int compareResult = 0;

		// Se for o método total, utiliza a comparação comum. Caso contrário,
		// antes de se realizar a comparação, deve-se igualar os parametros
		// que não se quer comparar.
		if (compareMethod == DATE_TIME_COMPARE) {
			compareResult = date.compareTo(compareDate);
		} else {
			// Objetos para trabalho mais detalhado com datas.
			GregorianCalendar gcDate = Utils.getGregorianCalendarInstance();
			gcDate.setTime(date);
			GregorianCalendar gcDateToCompare = Utils
					.getGregorianCalendarInstance();
			gcDateToCompare.setTime(compareDate);

			if (compareMethod == DATE_COMPARE) {
				gcDate.set(gcDate.get(gcDate.YEAR), gcDate.get(gcDate.MONTH),
						gcDate.get(gcDate.DAY_OF_MONTH), 1, 1, 1);
				gcDateToCompare.set(gcDateToCompare.get(gcDateToCompare.YEAR),
						gcDateToCompare.get(gcDateToCompare.MONTH),
						gcDateToCompare.get(gcDateToCompare.DAY_OF_MONTH), 1,
						1, 1);
				gcDate.set(gcDate.MILLISECOND, 0);
				gcDateToCompare.set(gcDate.MILLISECOND, 0);
				compareResult = gcDate.getTime().compareTo(
						gcDateToCompare.getTime());
			} else {
				gcDate.set(2000, 1, 1, gcDate.get(gcDate.HOUR_OF_DAY),
						gcDate.get(gcDate.MINUTE), gcDate.get(gcDate.SECOND));
				gcDateToCompare.set(2000, 1, 1,
						gcDateToCompare.get(gcDateToCompare.HOUR_OF_DAY),
						gcDateToCompare.get(gcDateToCompare.MINUTE),
						gcDateToCompare.get(gcDateToCompare.SECOND));
				compareResult = gcDate.getTime().compareTo(
						gcDateToCompare.getTime());
			}
		}

		return compareResult;
	}

	public static boolean isDateBetween(Date firstDate, Date lastDate,
			Date compareDate, int compareMethod, boolean inclusive) {
		if (firstDate == null) {
			throw new IllegalArgumentException("Param: firstDate");
		}
		if (lastDate == null) {
			throw new IllegalArgumentException("Param: lastDate");
		}
		if (compareDate == null) {
			throw new IllegalArgumentException("Param: compareDate");
		}

		if (inclusive) {
			return (Utils.compareDates(firstDate, compareDate, compareMethod) <= 0)
					&& (Utils
							.compareDates(lastDate, compareDate, compareMethod) >= 0);
		} else {
			return (Utils.compareDates(firstDate, compareDate, compareMethod) < 0)
					&& (Utils
							.compareDates(lastDate, compareDate, compareMethod) > 0);
		}

	}

	/**
	 * Compara apenas por data
	 */
	public static int compareDates(Date date, Date compareDate) {
		return compareDates(date, compareDate, Utils.DATE_COMPARE);
	}

	/**
	 * Obtém a posição do valor dentro de um conjunto. Este método aceita
	 * valores nulos, tanto para o valor a ser procurado, quanto para o
	 * conjunto. Caso o conjunto dado seja nulo, nenhum elemento percente ao
	 * conjunto.
	 * 
	 * @param value
	 *            Valor a ser procurado.
	 * @param values
	 *            Conjunto de valores.
	 * @return Retorna um índice do valor dentro do conjunto. Caso não encontre,
	 *         retorna <code>-1</code>.
	 */
	public static int indexOf(Object value, Object[] values) {
		int returnValue = -1;

		if (values != null) { // Caso haja array
			for (int i = 0; i < values.length; i++) { // Varre o array
				if (values[i] != null) { // Se existi o i-ésimo elemento
					if (values[i].equals(value)) { // Para, se eles forem
						// iguais
						returnValue = i;
						break;
					}
				} else {
					if (value == null) { // Se o elemento for nulo e o
						// i-ésimo tambem.
						returnValue = i;
						break;
					}
				}
			}
		}
		return returnValue;
	}

	public static Enum indexToEnum(int typeIndex, Enum[] types) {
		if (typeIndex >= 0 && typeIndex < types.length) {
			return types[typeIndex];
		}

		return null;
	}

	/**
	 * Retorna a oposição do inteiro.
	 * 
	 * @param value
	 *            Valor aser comparado.
	 * @param c1
	 *            Comparador 0.
	 * @param c2
	 *            Comparador 1.
	 * @return Se value for igual a c0, retorna 0, e assim por diante.
	 */
	public static int indexOf(int value, int c0, int c1) {
		if (value == c0) {
			return 0;
		}

		if (value == c1) {
			return 1;
		}

		return -1;
	}

	public static int indexOf(int value, int c0, int c1, int c2) {
		if (value == c0) {
			return 0;
		}

		if (value == c1) {
			return 1;
		}

		if (value == c2) {
			return 2;
		}

		return -1;
	}

	/**
	 * Gera artificialmente um stackttace falso.
	 * 
	 * @return
	 */
	public static String getStackTrace() {
		String stack = "";

		try {
			throw new RuntimeException();
		} catch (Exception e) {
			stack = Utils.getStackTrace(e);
		}

		return stack;
	}

	/**
	 * Obtém a pilha de erros da exceção dada. Essa pilha descreve, desde a
	 * raiz, a origem do erro.
	 * 
	 * @param error
	 *            Erro da qual se quer extrair a pilha de erros.
	 * @param simple
	 *            Indica se o erro é para ser formatado para ficar menor (simple
	 *            = true) ou não.
	 * @return Texto contendo todo o caminho do erro.
	 */
	public static String getStackTrace(Throwable error, boolean simple) {
		CharArrayWriter caw = new CharArrayWriter();
		PrintWriter pw = new PrintWriter(caw);
		error.printStackTrace(pw);
		String errorMsg = Utils.formatIfNull(caw.toString());

		if (!simple) {
			return errorMsg;
		} else {
			return Utils.format(
					errorMsg,
					new String[] { "", "|", "" },
					new String[] { "\tat ", new String(new byte[] { 13 }),
							new String(new byte[] { 10 }) }).trim();
		}
	}

	/**
	 * Obtém a pilha de erros da exceção dada. Essa pilha descreve, desde a
	 * raiz, a origem do erro. Este método realiza uma lipeza na lista de erros
	 * para que o texto fique mais curto.
	 * 
	 * @param error
	 *            Erro da qual se quer extrair a pilha de erros.
	 * @return Texto contendo todo o caminho do erro.
	 */
	public static String getStackTrace(Throwable error) {
		return getStackTrace(error, false);
	}

	/**
	 * Retorna um numero aleatorio
	 * 
	 * @return inteiro contendo um numero aleatorio, de pelo menos 9 digítios.
	 */
	public static int getRandomNumber() {
		return (int) (Math.random() * 1000000000);
	}

	public static char getRandomLetter() {
		return LETTERS[getRandomNumber() % LETTERS.length];
	}

	public static String generateUsernameByDomain(String domain) {
		String userName = "";

		for (int i = 0; i < domain.length() && userName.length() < 8; i++) {
			char c = domain.charAt(i);

			if (c != '-' && !Utils.isLetter(c) && !Utils.isNumber(c)) {
				continue;
			}

			if (userName.equals("") && (Utils.isNumber(c) || c == '-')) {
				userName += Utils.getRandomLetter();
			}

			userName += c;
		}

		while (userName.length() < 2) {
			userName += Utils.getRandomLetter();
		}

		return userName;
	}

	/**
	 * Permuta dois elementos do arary.
	 * 
	 * @param array
	 *            Array a ter os elemetos invertidos.
	 */
	public static void swapArrayElements(Object[] array, int src, int dst) {
		if (array == null) {
			throw new IllegalArgumentException("Parametro inválido: array");
		}

		if (src >= array.length || src < 0) {
			throw new IllegalArgumentException(
					"Parametro inválido: src. Ele deve ser um elemento do array.");
		}

		if (dst >= array.length || dst < 0) {
			throw new IllegalArgumentException(
					"Parametro inválido: dst. Ele deve ser um elemento do array.");
		}

		Object temp = array[src];
		array[src] = array[dst];
		array[dst] = temp;
	}

	/**
	 * Inverte a ordem dos elemetos de um array.
	 * 
	 * @param array
	 *            Array a ter os elemetos invertidos.
	 */
	public static void inverseArray(Object[] array) {
		if (array == null) {
			throw new IllegalArgumentException("Parametro inválido: array");
		}

		for (int i = 0; i < array.length / 2; i++) {
			swapArrayElements(array, i, array.length - i - 1);
		}

	}

	public static String inverse(String str) {
		if (str == null) {
			throw new IllegalArgumentException("Param: str");
		}

		String rS = "";

		for (int i = str.length() - 1; i >= 0; i--) {
			rS += str.charAt(i);
		}

		return rS;
	}

	/**
	 * Trunca o texto pelo tamanho dado. Caso não haja o que trunca, retorna o
	 * que foi obtido.
	 * 
	 * @param text
	 *            Texto a ser truncado.
	 * @param size
	 *            Tamanho máximo do texto.
	 * @return Texto truncado ou não.
	 */
	public static String truncateText(String text, int size) {
		if (text != null && text.length() > size) {
			text = text.substring(0, size);
		}

		return text;
	}

	/**
	 * Operacao de subtracao entre arrays.
	 * 
	 * @param e
	 *            Enumeration
	 * @param array2
	 *            Segundo Array
	 * @return Novo array
	 */
	public static String[] arrayMinus(Enumeration e, String[] array2) {
		boolean belongs;
		String element = null;
		String[] newArray = new String[1];
		ArrayList newList = new ArrayList(array2.length);

		for (; e.hasMoreElements();) {
			element = (String) e.nextElement();
			belongs = false;

			for (int j = 0; j < array2.length; j++) {
				if (element.equals(array2[j])) {
					belongs = true;
					break;
				}
			}

			if (!belongs) {
				newList.add(element);
			}
		}

		return (String[]) newList.toArray(newArray);
	}

	/**
	 * Dado um texto faz um texto qualquer virar uma variável.
	 * 
	 * Ex: Qual é o seu nome? => qual_e_o_seu_nome
	 * 
	 * Ex2: 2 é um númro legal? => _2_e_um_numero_legal
	 * 
	 * Outro: "leo e vira:	 * "leo__e"
	 * 
	 * Ou seja, se começa com número, bota _ antes para ser válida a variável.
	 * Retorna sem considerar nada de minúsculo ou maiúsculo. Do jeito que vier,
	 * retorna.
	 * 
	 * @param str
	 * @param maxSize
	 *            Tamanho máximo da variável.
	 * @return
	 */
	public static String strToVariable(String str, int maxSize) {
		str = Utils.formatIfNull(str).trim();

		if (str.equals("")) {
			throw new IllegalArgumentException(
					"Parametro inválido: str. Ele não pode ser branco.");
		}

		StringBuffer sb = new StringBuffer(str);
		int lastChatIndex = 0;
		boolean alreadyVar = false;

		for (int i = 0; i < sb.length(); i++) {
			// se já não precisa seguir, para.
			if (i > maxSize) {
				break;
			}

			lastChatIndex = i;

			char currChar = sb.charAt(i);

			boolean isNumber = Utils.isNumber(currChar);
			boolean isLetter = false;

			// se já não for número, testa se é letra.
			if (!isNumber) {
				isLetter = Utils.isLetter(currChar);

			}

			if (isLetter) { // se for letra, remove os acentos.
				sb.setCharAt(i, Utils.removeTones(currChar));

				alreadyVar = true;
			}

			if (!isLetter && !isNumber) {
				sb.setCharAt(i, '_');

				alreadyVar = true;
			}

			if (!alreadyVar && isNumber) {
				sb.insert(0, '_');

				// correção por causa da inserção
				i++;
				lastChatIndex++;

				alreadyVar = true;
			}
		}

		return sb.substring(0, lastChatIndex + 1).toLowerCase();
	}

	public static int max(int[] nums) {
		if (nums == null) {
			throw new IllegalArgumentException("Parametro inválido: nums");
		}

		int max = Integer.MIN_VALUE;
		for (int i = 0; i < nums.length; i++) {
			if (nums[i] > max) {
				max = nums[i];
			}
		}
		return max;
	}

	public static int min(int[] nums) {
		if (nums == null) {
			throw new IllegalArgumentException("Parametro inválido: nums");
		}

		int min = Integer.MAX_VALUE;
		for (int i = 0; i < nums.length; i++) {
			if (nums[i] < min) {
				min = nums[i];
			}
		}
		return min;
	}

	public static int modulus(int num) {
		return (num >= 0) ? num : -num;
	}

	public static double modulus(double num) {
		return (num >= 0) ? num : -num;
	}

	public static int substrCount(String txt, String substr) {
		if (txt == null) {
			throw new IllegalArgumentException("Parametro inválido: txt");
		}

		if (substr == null) {
			throw new IllegalArgumentException("Param: substr");
		}

		if (substr.equals("")) {
			return 0;
		}

		if (substr == null) {
			return 0;
		}

		int count = 0;
		int pos = -1;
		while (true) {
			pos = txt.indexOf(substr, pos);
			if (pos == -1) {
				break;
			}
			count++;
			pos += substr.length();
		}

		return count;
	}

	/**
	 * Verifica se as duas URLs são similares, o que significa que pertencem ao
	 * mesmo site.
	 * 
	 * @param url
	 *            Url a ser comparada.
	 * @param urlCompare
	 *            URL utilizada na comparação.
	 * @return Verdadeiro se forem similares.
	 */
	public static boolean isUrlSimilar(String url, String urlCompare) {
		if (url == null) {
			throw new IllegalArgumentException("Parametro inválido: url");
		}

		if (urlCompare == null) {
			throw new IllegalArgumentException("Parametro inválido: urlCompare");
		}

		String urlDomain = Utils.extractDomainName(url);
		String urlCompareDomain = Utils.extractDomainName(urlCompare);

		boolean returnValue = false;

		if (urlDomain.equals(urlCompareDomain)) {
			String lastUrlPart = Utils.removeDomainName(url);
			String lastUrlComparePart = Utils.removeDomainName(urlCompare);

			// Caso o domínio seja parte inicial do que se deseja comparar, eles
			// pertencem ao mesmo domínio.
			if (lastUrlComparePart.indexOf(lastUrlPart) == 0
					|| lastUrlPart.indexOf(lastUrlComparePart) == 0) {
				returnValue = true;
			} else {
				// Caso os tamanho do restante seja maior que a metade do maior.
				int sizeDiff = Utils.modulus(lastUrlPart.length()
						- lastUrlComparePart.length());
				int max = Utils.max(new int[] { lastUrlPart.length(),
						lastUrlComparePart.length() });

				if (lastUrlComparePart.equals("") || lastUrlPart.equals("")
						|| sizeDiff < max / 2) {
					returnValue = true;
				}
			}
		}

		return returnValue;
	}

	public static DataType strTypeToType(String fieldType) {
		DataType type = DataType.UNDEFINED_TYPE; // type armazenará o tipo do
		// campo
		fieldType = fieldType.toLowerCase() + ";";
		if (fieldType == null) {
			return type;
		} else if ("int;short;long;int4;int2;".indexOf(fieldType) != -1) { // if-else
			// de
			// inferencia
			// do
			// tipo
			// do
			// campo
			// atual
			type = DataType.NUMBER;
		} else if ("varchar;text;char;".indexOf(fieldType) != -1) {
			type = DataType.STRING;
		} else if ("boo;boolean;bool;logic;".indexOf(fieldType) != -1) {
			type = DataType.BOOL;
		} else if ("datetime;time;date;timestamp;timestamptz;"
				.indexOf(fieldType) != -1) {
			type = DataType.DATE;
		}

		return type;
	}

	/**
	 * Converte um ip em um número único. Fórmula:
	 * 
	 * PART1×256^3 + PART2×256^2 + PART3×256 + PART4
	 * 
	 * Esta fórmula foi obtida a partir dos cálculos das funççoes do mySQL
	 * (INET_ATON, INET_NTOA)
	 * 
	 * @param ip
	 *            IP no formato "XXX.XXX.XXX.XXX", sendo "X" um número, podendo
	 *            ser vazio.
	 * @return Número convertido. O tipo tem que ser longo porque o int não
	 *         suporta o máximo do IP, que é de 999999999999.
	 * 
	 * @throws Exception
	 */
	public static long ipToLong(String ip) {
		if (ip == null) {
			throw new IllegalArgumentException("Parametro inválido: ip");
		}

		long num = 0;
		StringTokenizer st = new StringTokenizer(ip, ".");

		for (int i = 3; i >= 0 && st.hasMoreTokens(); i--) {

			// PART1×256^3 + PART2×256^2 + PART3×256 + PART4
			num += Utils.parseInt(st.nextToken()) * Math.pow(256, i);
		}

		return num;
	}

	/**
	 * Igual a ipToLong, mas converte.
	 * 
	 * int é suficiente, mas estoura, aí vira negativo. Long tem a vantagem de
	 * sempre ser positivo.
	 * 
	 * @param ip
	 * @return
	 */
	public static int ipToInt(String ip) {
		return (int) ipToLong(ip);
	}

	/**
	 * Converte a string do ip para int.
	 * 
	 * @param ip
	 * @return
	 */
	public static String intToIp(int ip) {
		return ((ip >> 24) & 0xFF) + "." + ((ip >> 16) & 0xFF) + "."
				+ ((ip >> 8) & 0xFF) + "." + (ip & 0xFF);
	}

	public static int extractFromDate(Date date, int field) {
		if (date == null) {
			throw new IllegalArgumentException("Parametro inválido: date");
		}

		GregorianCalendar gc = Utils.getGregorianCalendarInstance();
		gc.setTime(date);
		return gc.get(field);
	}

	public static int getDatePart(Date date, int field) {
		if (date == null) {
			throw new IllegalArgumentException("Parametro inválido: date");
		}

		GregorianCalendar gc = Utils.getGregorianCalendarInstance();
		gc.setTime(date);
		return gc.get(field);
	}

	/**
	 * 
	 * @param date1
	 *            Data que vai ser subtraída.
	 * @param date2
	 *            Data que vai subtrair.
	 * @param field
	 *            Em relação a que campo haverá a subtração.
	 * @return Número resultante da subtração.
	 */
	public static int subDates(Date date1, Date date2, int field) {
		if (date1 == null) {
			throw new IllegalArgumentException("Parametro inválido: date1");
		}

		if (date2 == null) {
			new IllegalArgumentException("Param: date2");
		}

		GregorianCalendar gc1 = Utils.getGregorianCalendarInstance();
		gc1.setTime(date1);

		GregorianCalendar gc2 = Utils.getGregorianCalendarInstance();
		gc2.setTime(date2);

		return gc1.get(field) - gc2.get(field);
	}

	public static Date setDatePart(Date date, int field, int value) {
		if (date == null) {
			throw new IllegalArgumentException("Parametro inválido: date");
		}

		GregorianCalendar gc = Utils.getGregorianCalendarInstance();
		gc.setTime(date);
		gc.set(field, value);
		return gc.getTime();
	}

	public static Date addDate(Date date, int field, int amount) {
		if (date == null) {
			throw new IllegalArgumentException("Parametro inválido: date");
		}

		GregorianCalendar gc = Utils.getGregorianCalendarInstance();
		gc.setTime(date);
		gc.add(field, amount);
		return gc.getTime();
	}

	/**
	 * Realiza o acréscimo apeans no dia do mes.
	 */
	public static Date addDate(Date date, int amount) {
		return addDate(date, GregorianCalendar.DAY_OF_MONTH, amount);
	}

	/**
	 * Transforma uma string em um booleano.
	 * 
	 * @param value
	 *            Valor a ser convertido para booleano.
	 * @return Se for nulo, é falso. Se for uma string representativa de
	 *         verdadeiro (1, true, verdadeiro), retorna true.
	 */
	public static boolean toBoo(String value) {
		return toBoo(value, false);
	}

	/**
	 * Transforma uma string em um booleano.
	 * 
	 * @param value
	 *            Valor a ser convertido para booleano.
	 * @return Se for nulo, é falso. Se for uma string representativa de
	 *         verdadeiro (1, true, verdadeiro), retorna true.
	 */
	public static boolean toBoo(String value, boolean defaultValue) {
		boolean result = defaultValue;

		if (value != null) {
			if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("1")
					|| value.equalsIgnoreCase("verdadeiro")
					|| value.equalsIgnoreCase("on")
					|| value.equalsIgnoreCase("yes")) {
				result = true;
			} else if (value.equalsIgnoreCase("false")
					|| value.equalsIgnoreCase("0")
					|| value.equalsIgnoreCase("falso")
					|| value.equalsIgnoreCase("off")
					|| value.equalsIgnoreCase("no")) {
				result = false;
			}
		}

		return result;
	}

	public static String toStr(AbstractCollection ac, String separator) {
		if (ac == null) {
			throw new IllegalArgumentException("Parametro inválido: ac");
		}

		StringBuffer sb = new StringBuffer();

		Iterator it = ac.iterator();

		while (it.hasNext()) {
			sb.append(it.next());

			if (it.hasNext()) {
				sb.append(separator);
			}
		}

		return sb.toString();
	}

	public static String toStr(int[] nums, String separator) {
		if (nums == null) {
			throw new IllegalArgumentException("Parametro inválido: nums");
		}

		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < nums.length; i++) {
			sb.append(nums[i]);

			if (i < nums.length - 1) {
				sb.append(separator);
			}
		}

		return sb.toString();
	}

	public static String toStr(Object[] objs, String separator) {
		if (objs == null) {
			throw new IllegalArgumentException("Parametro inválido: objs");
		}

		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < objs.length; i++) {
			sb.append(objs[i] + "");

			if (i < objs.length - 1) {
				sb.append(separator);
			}
		}

		return sb.toString();
	}

	public static String getTimePattern() {
		return timePattern;
	}

	/**
	 * Quebra de linha composta: #13 #10.
	 * 
	 * @return String de quebra.
	 */
	public static String getBreakLine() {
		return "\r\n";
	}

	public static String getBreakLine(int count) {
		return Utils.repeatStr(count, Utils.getBreakLine());
	}

	public static String captalizeFirst(String text) {
		if (text == null) {
			throw new IllegalArgumentException("Parametro inválido: text");
		}

		if (text.equals("")) {
			return text;
		}

		String resultStr = text.substring(0, 1).toUpperCase();
		if (text.length() > 1) {
			resultStr += text.substring(1, text.length());
		}

		return resultStr;
	}

	/**
	 * <p>
	 * Dado o texto, este metodo ob´tem um número que pode representar o texto.
	 * Nao dá garantia de que este número será único para a string passada, mas
	 * da uma certa margem de acerto.
	 * </p>
	 * 
	 * <p>
	 * O cálculo implementado é simples: por exemplo, se indexPercent for 0.5,
	 * pega count caracteres para a direita e para a esquerda e realiza o
	 * somatório dos valores da tabela ASCII dos caracteres da substring. Depois
	 * disso, soma-se a quantidade de caracteres total do texto original.
	 * </p>
	 * 
	 * <p>
	 * Um cálculo mais elaborado seria este: sabendo que o maior valor de um
	 * longo é 9223372036854775807 com 19 casas decimais, então usa apenas 18
	 * casas, sendo a metade disto destinada para o somatório dos valores
	 * inteiros de todos os caracteres da String e a outra metade extraído da
	 * substring extraída com os parametros dados. Depois, concatena os dois e
	 * substring o longo resultante. Ou seja, o número retornado vai depender do
	 * texto como um todo passado e tambem da parte representativa dada, sendo
	 * um número único para textos iguais.
	 * </p>
	 * 
	 * @param text
	 *            Texto do qual será calculado um número.
	 * @param indexPercent
	 *            0.5 signifca que o método pegará a metade e copiara
	 *            <code>count</code> caracteres para a direita e esquerda.
	 * @param count
	 *            Quantidade de caracteres para a esquerda e direita extraídos;
	 * @param method
	 *            0: somando-se; 1: multiplicando; qq outro: somando-se.
	 * @return Longo representativo do texto. Se o texto passado for nulo ou em
	 *         branco, retorna 0.
	 */
	public static int getIntRepresentation(String text, double indexPercent,
			int count) {
		return getIntRepresentation(text, indexPercent, count, 0);
	}

	public static int getIntRepresentation(String text, double indexPercent,
			int count, int method) {
		if (text == null) {
			return 0;
		}

		if (indexPercent > 1 || indexPercent < 0) {
			throw new IllegalArgumentException(
					"Parametro inválido: indexPercent: " + indexPercent);
		}

		if (text.length() == 0) {
			return 0;
		}

		int pos = (int) ((text.length() + .0) * indexPercent);
		String substring = null;

		// Extrai
		if (pos - count < 0 || pos + count >= text.length()) {
			substring = text;
		} else {
			substring = text.substring(pos - count, pos + count);
		}

		byte[] bytes = substring.getBytes();
		int num = 0;

		if (method == 1) {
			num = bytes.length;
		}

		for (int i = 0; i < bytes.length; i++) {
			if (method != 1) {
				num += bytes[i];
			} else {
				num *= (bytes[i] + 127);
			}
		}

		return Math.abs(num + text.length());
	}

	public static GregorianCalendar getGregorianCalendarInstance(int year,
			int month, int day) {
		GregorianCalendar gc = new GregorianCalendar(year, month, day);
		gc.setTimeZone(timeZone);
		return gc;
	}

	public static GregorianCalendar getGregorianCalendarInstance(Date date,
			int hour, int minute, int second) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTimeZone(timeZone);

		gc.setTime(date);

		gc.set(gc.HOUR_OF_DAY, hour);
		gc.set(gc.MINUTE, minute);
		gc.set(gc.SECOND, second);

		return gc;
	}

	public static Date getYesterday() {
		return Utils.addDate(new Date(), -1);
	}

	public static GregorianCalendar getGregorianCalendarInstance() {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTimeZone(timeZone);
		return gc;
	}

	public static Locale getLocale() {
		return locale;
	}

	public static void setLocale(Locale newLocale) {
		locale = newLocale;
	}

	public static TimeZone getTimeZone() {
		return timeZone;
	}

	public static void setTimeZone(TimeZone newTimeZone) {
		timeZone = newTimeZone;
	}

	public static String charToHtml(char c) {
		if (c == '"') {
			return "&quot;";
		} else if (c == '&') {
			return "&amp;";
		} else if (c == '<') {
			return "&lt;";
		} else if (c == '>') {
			return "&gt;";
		} else if (c == ' ') {
			return "&nbsp;";
		} else if (c == '¡') {
			return "&iexcl;";
		} else if (c == '¢') {
			return "&cent;";
		} else if (c == '£') {
			return "&pound;";
		} else if (c == '¤') {
			return "&curren;";
		} else if (c == '¥') {
			return "&yen;";
		} else if (c == '¦') {
			return "&brvbar;";
		} else if (c == '§') {
			return "&sect;";
		} else if (c == '¨') {
			return "&uml;";
		} else if (c == '©') {
			return "&copy;";
		} else if (c == 'ª') {
			return "&ordf;";
		} else if (c == '«') {
			return "&laquo;";
		} else if (c == '¬') {
			return "&not;";
		} else if (c == '­') {
			return "&shy;";
		} else if (c == '®') {
			return "&reg;";
		} else if (c == '¯') {
			return "&macr;";
		} else if (c == '°') {
			return "&deg;";
		} else if (c == '±') {
			return "&plusmn;";
		} else if (c == '²') {
			return "&sup2;";
		} else if (c == '³') {
			return "&sup3;";
		} else if (c == '´') {
			return "&acute;";
		} else if (c == 'µ') {
			return "&micro;";
		} else if (c == '¶') {
			return "&para;";
		} else if (c == '·') {
			return "&middot;";
		} else if (c == '¸') {
			return "&cedil;";
		} else if (c == '¹') {
			return "&sup1;";
		} else if (c == 'º') {
			return "&ordm;";
		} else if (c == '»') {
			return "&raquo;";
		} else if (c == '¼') {
			return "&frac14;";
		} else if (c == '½') {
			return "&frac12;";
		} else if (c == '¾') {
			return "&frac34;";
		} else if (c == '¿') {
			return "&iquest;";
		} else if (c == 'À') {
			return "&Agrave;";
		} else if (c == 'Á') {
			return "&Aacute;";
		} else if (c == 'Â') {
			return "&Acirc;";
		} else if (c == 'Ã') {
			return "&Atilde;";
		} else if (c == 'Ä') {
			return "&Auml;";
		} else if (c == 'Å') {
			return "&Aring;";
		} else if (c == 'Æ') {
			return "&AElig;";
		} else if (c == 'Ç') {
			return "&Ccedil;";
		} else if (c == 'È') {
			return "&Egrave;";
		} else if (c == 'É') {
			return "&Eacute;";
		} else if (c == 'Ê') {
			return "&Ecirc;";
		} else if (c == 'Ë') {
			return "&Euml;";
		} else if (c == 'Ì') {
			return "&Igrave;";
		} else if (c == 'Í') {
			return "&Iacute;";
		} else if (c == 'Î') {
			return "&Icirc;";
		} else if (c == 'Ï') {
			return "&Iuml;";
		} else if (c == 'Ð') {
			return "&ETH;";
		} else if (c == 'Ñ') {
			return "&Ntilde;";
		} else if (c == 'Ò') {
			return "&Ograve;";
		} else if (c == 'Ó') {
			return "&Oacute;";
		} else if (c == 'Ô') {
			return "&Ocirc;";
		} else if (c == 'Õ') {
			return "&Otilde;";
		} else if (c == 'Ö') {
			return "&Ouml;";
		} else if (c == '×') {
			return "&times;";
		} else if (c == 'Ø') {
			return "&Oslash;";
		} else if (c == 'Ù') {
			return "&Ugrave;";
		} else if (c == 'Ú') {
			return "&Uacute;";
		} else if (c == 'Û') {
			return "&Ucirc;";
		} else if (c == 'Ü') {
			return "&Uuml;";
		} else if (c == 'Ý') {
			return "&Yacute;";
		} else if (c == 'Þ') {
			return "&THORN;";
		} else if (c == 'ß') {
			return "&szlig;";
		} else if (c == 'œ') {
			return "&oelig;";
		} else if (c == 'Œ') {
			return "&OElig;";
		} else if (c == 'à') {
			return "&agrave;";
		} else if (c == 'á') {
			return "&aacute;";
		} else if (c == 'â') {
			return "&acirc;";
		} else if (c == 'ã') {
			return "&atilde;";
		} else if (c == 'ä') {
			return "&auml;";
		} else if (c == 'å') {
			return "&aring;";
		} else if (c == 'æ') {
			return "&aelig;";
		} else if (c == 'ç') {
			return "&ccedil;";
		} else if (c == 'è') {
			return "&egrave;";
		} else if (c == 'é') {
			return "&eacute;";
		} else if (c == 'ê') {
			return "&ecirc;";
		} else if (c == 'ë') {
			return "&euml;";
		} else if (c == 'ì') {
			return "&igrave;";
		} else if (c == 'í') {
			return "&iacute;";
		} else if (c == 'î') {
			return "&icirc;";
		} else if (c == 'ï') {
			return "&iuml;";
		} else if (c == 'ð') {
			return "&eth;";
		} else if (c == 'ñ') {
			return "&ntilde;";
		} else if (c == 'ò') {
			return "&ograve;";
		} else if (c == 'ó') {
			return "&oacute;";
		} else if (c == 'ô') {
			return "&ocirc;";
		} else if (c == 'õ') {
			return "&otilde;";
		} else if (c == 'ö') {
			return "&ouml;";
		} else if (c == '÷') {
			return "&divide;";
		} else if (c == 'ø') {
			return "&oslash;";
		} else if (c == 'ù') {
			return "&ugrave;";
		} else if (c == 'ú') {
			return "&uacute;";
		} else if (c == 'û') {
			return "&ucirc;";
		} else if (c == 'ü') {
			return "&uuml;";
		} else if (c == 'ý') {
			return "&yacute;";
		} else if (c == 'þ') {
			return "&thorn;";
		} else {
			return c + "";
		}
	}

	/**
	 * 
	 * 
	 * @param text
	 *            String
	 * @return Rertorna '0' se não encontrar e caracter, encontrando.
	 */
	public static char htmlToChar(String text) {
		if (text == null) {
			throw new IllegalArgumentException("Param: text");
		}

		if (text.length() < 4) {
			return '0';
		}

		if (text.charAt(1) == '#') {
			// Tenta converter (se for um número). Erro, retorna '0'
			try {
				return (char) Utils.parseInt(text.substring(2,
						text.length() - 1));
			} catch (Exception error) {
				return '0';
			}
		} else {
			if (text.equals("&quot;")) {
				return '\"';
			} else if (text.equals("&amp;")) {
				return '&';
			} else if (text.equals("&lt;")) {
				return '<';
			} else if (text.equals("&gt;")) {
				return '>';
			} else if (text.equals("&nbsp;")) {
				return ' ';
			} else if (text.equals("&iexcl;")) {
				return '¡';
			} else if (text.equals("&cent;")) {
				return '¢';
			} else if (text.equals("&pound;")) {
				return '£';
			} else if (text.equals("&curren;")) {
				return '¤';
			} else if (text.equals("&yen;")) {
				return '¥';
			} else if (text.equals("&brvbar;")) {
				return '¦';
			} else if (text.equals("&sect;")) {
				return '§';
			} else if (text.equals("&uml;")) {
				return '¨';
			} else if (text.equals("&copy;")) {
				return '©';
			} else if (text.equals("&ordf;")) {
				return 'ª';
			} else if (text.equals("&laquo;")) {
				return '«';
			} else if (text.equals("&not;")) {
				return '¬';
			} else if (text.equals("&shy;")) {
				return '­';
			} else if (text.equals("&reg;")) {
				return '®';
			} else if (text.equals("&macr;")) {
				return '¯';
			} else if (text.equals("&deg;")) {
				return '°';
			} else if (text.equals("&plusmn;")) {
				return '±';
			} else if (text.equals("&sup2;")) {
				return '²';
			} else if (text.equals("&sup3;")) {
				return '³';
			} else if (text.equals("&acute;")) {
				return '´';
			} else if (text.equals("&micro;")) {
				return 'µ';
			} else if (text.equals("&para;")) {
				return '¶';
			} else if (text.equals("&middot;")) {
				return '·';
			} else if (text.equals("&cedil;")) {
				return '¸';
			} else if (text.equals("&sup1;")) {
				return '¹';
			} else if (text.equals("&ordm;")) {
				return 'º';
			} else if (text.equals("&raquo;")) {
				return '»';
			} else if (text.equals("&frac14;")) {
				return '¼';
			} else if (text.equals("&frac12;")) {
				return '½';
			} else if (text.equals("&frac34;")) {
				return '¾';
			} else if (text.equals("&iquest;")) {
				return '¿';
			} else if (text.equals("&Agrave;")) {
				return 'À';
			} else if (text.equals("&Aacute;")) {
				return 'Á';
			} else if (text.equals("&Acirc;")) {
				return 'Â';
			} else if (text.equals("&Atilde;")) {
				return 'Ã';
			} else if (text.equals("&Auml;")) {
				return 'Ä';
			} else if (text.equals("&Aring;")) {
				return 'Å';
			} else if (text.equals("&AElig;")) {
				return 'Æ';
			} else if (text.equals("&Ccedil;")) {
				return 'Ç';
			} else if (text.equals("&Egrave;")) {
				return 'È';
			} else if (text.equals("&Eacute;")) {
				return 'É';
			} else if (text.equals("&Ecirc;")) {
				return 'Ê';
			} else if (text.equals("&Euml;")) {
				return 'Ë';
			} else if (text.equals("&Igrave;")) {
				return 'Ì';
			} else if (text.equals("&Iacute;")) {
				return 'Í';
			} else if (text.equals("&Icirc;")) {
				return 'Î';
			} else if (text.equals("&Iuml;")) {
				return 'Ï';
			} else if (text.equals("&ETH;")) {
				return 'Ð';
			} else if (text.equals("&Ntilde;")) {
				return 'Ñ';
			} else if (text.equals("&Ograve;")) {
				return 'Ò';
			} else if (text.equals("&Oacute;")) {
				return 'Ó';
			} else if (text.equals("&Ocirc;")) {
				return 'Ô';
			} else if (text.equals("&Otilde;")) {
				return 'Õ';
			} else if (text.equals("&Ouml;")) {
				return 'Ö';
			} else if (text.equals("&times;")) {
				return '×';
			} else if (text.equals("&Oslash;")) {
				return 'Ø';
			} else if (text.equals("&Ugrave;")) {
				return 'Ù';
			} else if (text.equals("&Uacute;")) {
				return 'Ú';
			} else if (text.equals("&Ucirc;")) {
				return 'Û';
			} else if (text.equals("&Uuml;")) {
				return 'Ü';
			} else if (text.equals("&Yacute;")) {
				return 'Ý';
			} else if (text.equals("&THORN;")) {
				return 'Þ';
			} else if (text.equals("&szlig;")) {
				return 'ß';
			} else if (text.equals("&oelig;")) {
				return 'œ';
			} else if (text.equals("&OElig;")) {
				return 'Œ';
			} else if (text.equals("&agrave;")) {
				return 'à';
			} else if (text.equals("&aacute;")) {
				return 'á';
			} else if (text.equals("&acirc;")) {
				return 'â';
			} else if (text.equals("&atilde;")) {
				return 'ã';
			} else if (text.equals("&auml;")) {
				return 'ä';
			} else if (text.equals("&aring;")) {
				return 'å';
			} else if (text.equals("&aelig;")) {
				return 'æ';
			} else if (text.equals("&ccedil;")) {
				return 'ç';
			} else if (text.equals("&egrave;")) {
				return 'è';
			} else if (text.equals("&eacute;")) {
				return 'é';
			} else if (text.equals("&ecirc;")) {
				return 'ê';
			} else if (text.equals("&euml;")) {
				return 'ë';
			} else if (text.equals("&igrave;")) {
				return 'ì';
			} else if (text.equals("&iacute;")) {
				return 'í';
			} else if (text.equals("&icirc;")) {
				return 'î';
			} else if (text.equals("&iuml;")) {
				return 'ï';
			} else if (text.equals("&eth;")) {
				return 'ð';
			} else if (text.equals("&ntilde;")) {
				return 'ñ';
			} else if (text.equals("&ograve;")) {
				return 'ò';
			} else if (text.equals("&oacute;")) {
				return 'ó';
			} else if (text.equals("&ocirc;")) {
				return 'ô';
			} else if (text.equals("&otilde;")) {
				return 'õ';
			} else if (text.equals("&ouml;")) {
				return 'ö';
			} else if (text.equals("&divide;")) {
				return '÷';
			} else if (text.equals("&oslash;")) {
				return 'ø';
			} else if (text.equals("&ugrave;")) {
				return 'ù';
			} else if (text.equals("&uacute;")) {
				return 'ú';
			} else if (text.equals("&ucirc;")) {
				return 'û';
			} else if (text.equals("&uuml;")) {
				return 'ü';
			} else if (text.equals("&yacute;")) {
				return 'ý';
			} else if (text.equals("&thorn;")) {
				return 'þ';
			} else if (text.equals("&bull;")) {
				return ' ';
			}
		}

		return '0';
	}

	/**
	 * Obtém o caracter simplificado do dado. Por exemplo, "Á" retorna "A".
	 * 
	 * @param char Caracter a ser simplificado.
	 * @return Caracter simplificado.
	 */
	public static char simplifyText(char c) {
		if (c >= 'À' && c <= 'Å') {
			c = 'A';
		} else if (c >= 'à' && c <= 'å') {
			c = 'a';
		} else if (c >= 'È' && c <= 'Ë') {
			c = 'E';
		} else if (c >= 'è' && c <= 'è') {
			c = 'e';
		} else if (c >= 'Ì' && c <= 'Ï') {
			c = 'I';
		} else if (c >= 'ì' && c <= 'ï') {
			c = 'i';
		} else if (c >= 'Ò' && c <= 'Ö') {
			c = 'O';
		} else if (c >= 'ò' && c <= 'ö') {
			c = 'o';
		} else if (c >= 'Ù' && c <= 'Ü') {
			c = 'U';
		} else if (c >= 'ù' && c <= 'ü') {
			c = 'u';
		} else if (c == 'Ý') {
			c = 'Y';
		} else if (c == 'ý') {
			c = 'y';
		} else if (c == 'ñ') {
			c = 'n';
		} else if (c == 'Ñ') {
			c = 'N';
		} else if (c == 'ç') {
			c = 'c';
		} else if (c == 'Ç') {
			c = 'C';
		}

		return c;
	}

	public static String htmlEscape(String text) {
		if (text == null) {
			throw new IllegalArgumentException("Parametro inválido: text");
		}

		StringBuffer sb = new StringBuffer(text);
		StringBuffer result = new StringBuffer();

		for (int i = 0; i < sb.length(); i++) {
			result.append(charToHtml(text.charAt(i)));
		}

		return result.toString();
	}

	/**
	 * Dá htmlUnescape e substitui eventuais caracteres que não são bem
	 * interpretados por um formato simples, ex: “ e ”
	 * 
	 * @param text
	 * @return
	 */
	public static String htmlUnescapeAndClear(String text) {
		return Utils.format(Utils.htmlUnescape(text),
				new String[] { "\"", "\"" }, new String[] { "“", "”" });
	}

	/**
	 * Retira as marcações de acentos. Está completo.
	 * 
	 * @param text
	 *            String
	 * @return String
	 */
	public static String htmlUnescape(String text) {
		if (text == null) {
			throw new IllegalArgumentException("Parametro inválido: text");
		}

		char[] chars = text.toCharArray();
		StringBuffer result = new StringBuffer(text.length());

		for (int i = 0; i < chars.length; i++) {
			char currChar = chars[i];
			boolean ignoreChar = false;

			if (currChar == '&') {
				StringBuffer mark = new StringBuffer(currChar + "");
				int tempI = i + 1; // (já esta i++)
				boolean found = true;

				char tempCurrChar = currChar;
				while (tempI < chars.length && tempI - i < 10) { // O maior
					// detectado
					// tem 6
					// caracteres
					// entre & e
					// ;. Ex:
					// "&plusmn;"
					tempCurrChar = chars[tempI++];
					mark.append(tempCurrChar);

					if (tempCurrChar == ';') {
						break;
					} else if (tempCurrChar != '#'
							&& !isValidWordChar(tempCurrChar)) {
						found = false;
						break;
					}
				}

				// Se achou, converte. Se conseguiu converter, substuitu
				// no texto, coloca currChar como o caracter encontrado
				// e continua o processo.
				if (found) {
					char converted = Utils.htmlToChar(mark.toString());

					if (converted == '&'
							&& i + mark.toString().length() + 4 < chars.length
							&& chars[i + mark.toString().length()] == '#') {
						ignoreChar = true;
						chars[i + mark.toString().length() - 1] = '&';
						i += mark.length() - 2;
					} else if (converted != '0') {
						currChar = converted;
						i += mark.length() - 1;
					}
				}
			}

			if (!ignoreChar) {
				result.append(currChar);
			}
		}

		return result.toString();
	}

	/*
	 * public static boolean isPonctuationChar(char c) { return (c == '.' || c
	 * == ',' || c == ';' || c == ':' || c == '!' || c == '?' || c == '/' || c
	 * == '\\' || c == '|'); }
	 */
	public static boolean isPonctuationChar(char c) {
		return (c == '.' || c == ',' || c == ';' || c == ':' || c == '!'
				|| c == '?' || c == '/' || c == '\\' || c == '|' || c == ')'
				|| c == '(' || c == '|' || c == '}' || c == '{' || c == '\'' || c == '\"');
	}

	public static boolean isSpaceChar(char c) {
		return (c == '\n' || c == '\t' || c == '\r' || c == ' ');
	}

	public static boolean isNumber(char c) {
		return (c >= '0' && c <= '9');
	}

	public static boolean isVowel(char c) {
		c = Character.toLowerCase(c);

		if (c == 'æ' || c == 'œ') {
			return true;
		}

		c = Utils.removeTones(c);

		return (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u');
	}

	public static boolean isLetter(char c) {
		c = removeTones(Character.toLowerCase(c));
		return ((c >= 'a' && c <= 'z') || (c == 'ß' || c == 'æ') || c == 'œ');
	}

	public static boolean isValidWordChar(char c) {
		// ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖ×ØÙÚÛÜÝÞßàáâãäåæçèéêëìíîïðñòóôõö÷øùúûüýþ
		// a..z
		// A..Z
		// 0..9
		if (c == '-') {
			return true;
		} else if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')
				|| (c >= 'À' && c <= 'þ') || (c >= '0' && c <= '9')
				|| (c == 'Œ' || c == 'œ' || c == 'Æ' || c == 'æ')) {
			return true;
		}

		return false;
	}

	public static String removeTones(String txt) {
		char[] chs = txt.toCharArray();

		StringBuffer text = new StringBuffer();

		for (int i = 0; i < chs.length; i++) {
			// dígrafos
			if (chs[i] == 'ß') {
				text.append("ss");
			} else if (chs[i] == 'æ') {
				text.append("ae");
			} else if (chs[i] == 'Æ') {
				text.append("AE");
			} else if (chs[i] == 'œ') {
				text.append("oe");
			} else if (chs[i] == 'Œ') {
				text.append("OE");
			} else {
				text.append(Utils.removeTones(chs[i]));
			}
		}

		return text.toString();

		/*
		 * return Utils.format(Utils.formatIfNull(txt), new String[] { "n", "N",
		 * "a", "a", "a", "a", "a", "a", "e", "e", "e", "e", "i", "i", "i", "i",
		 * "c", "y", "o", "o", "o", "o", "o", "u", "u", "u", "u", "A", "A", "A",
		 * "A", "A", "A", "E", "E", "E", "E", "I", "I", "I", "I", "O", "O", "O",
		 * "O", "O", "U", "U", "U", "U", "C", "Y" }, new String[] { "ñ", "Ñ",
		 * "à", "á", "â", "ã", "ä", "å", "è", "é", "ê", "ë", "ì", "í", "î", "ï",
		 * "ç", "ý", "ô", "ó", "ò", "õ", "ö", "ù", "ú", "û", "ü", "À", "Á", "Â",
		 * "Ã", "Ä", "Å", "È", "É", "Ê", "Ë", "Ì", "Í", "Î", "Ï", "Ò", "Ó", "Ô",
		 * "Õ", "Ö", "Ù", "Ú", "Û", "Ü", "Ç", "Ý" });
		 */
	}

	public static char removeTones(char c) {
		char rC = c;
		boolean lowerCase = Character.isLowerCase(c);
		c = Character.toLowerCase(c);

		if (c == 'à' || c == 'á' || c == 'â' || c == 'ã' || c == 'ä'
				|| c == 'å') {
			rC = 'a';
		} else if (c == 'ê' || c == 'é' || c == 'è' || c == 'ë') {
			rC = 'e';
		} else if (c == 'ì' || c == 'í' || c == 'î' || c == 'ï' || c == 'î') {
			rC = 'i';
		} else if (c == 'ô' || c == 'ó' || c == 'ò' || c == 'õ' || c == 'ö') {
			rC = 'o';
		} else if (c == 'û' || c == 'ú' || c == 'ù' || c == 'ü') {
			rC = 'u';
		} else if (c == 'ç') {
			rC = 'c';
		} else if (c == 'ñ') {
			rC = 'n';
		} else if (c == 'ý') {
			rC = 'y';
		}

		if (!lowerCase) {
			rC = Character.toUpperCase(rC);
		}

		return rC;
	}

	/**
	 * Limpa a astring de acentos, colcando espaço em caractres desconhecidos
	 * que não sejam letras. Coloca tambem tudo me minúsculo.
	 * 
	 * @param txt
	 *            Texto a ser purificado.
	 * @param allowedChars
	 *            Caracteres que sao permitidos e não são transformados em
	 *            espaço.
	 * @return TExto purificado.
	 */
	public static String purifyStr(String txt, char[] allowedChars) {
		if (txt == null) {
			throw new IllegalArgumentException("Param: txt");
		}
		char[] cs = Utils.removeTones(txt).toLowerCase().toCharArray();
		for (int j = 0; j < cs.length; j++) {
			boolean allowed = false;

			for (int i = 0; i < allowedChars.length; i++) {
				if (allowedChars[i] == cs[j]) {
					allowed = true;
					break;
				}
			}

			if (!allowed && !Utils.isLetter(cs[j]) && !Utils.isNumber(cs[j])) {
				cs[j] = ' ';
			}
		}

		return new String(cs);
	}

	public static String purifyStr(String txt) {
		return Utils.purifyStr(txt, new char[] { '&', '-' });
	}

	/**
	 * 2006-09-11: revisção e mudança conssitente em que hífem agora não é
	 * excluído, mas transforma-se em espaço, com exceção de e-mail, que se
	 * transforma em email; e-book em ebook e assim por diante.
	 * 
	 * OBS (2006-09-11): terminação "oes" é para virar "ao". Ex: viloes,
	 * tubaroes.
	 * 
	 * @param description
	 *            String
	 * @param othersDeniesWords
	 *            ArrayList
	 * @param maxKeyWords
	 *            int
	 * @param ignorePlural
	 *            boolean
	 * @return String[]
	 */
	public static String[] getKeyWords(String description,
			ArrayList othersDeniesWords, int maxKeyWords, boolean ignorePlural) {
		String cleanDescription = Utils.purifyStr(description);
		boolean hasHifen = cleanDescription.indexOf('-') != -1;

		String[] words = Utils.getLines(cleanDescription, ' ');

		// tratamento do hífen
		if (hasHifen) {
			for (int i = 0; i < words.length; i++) {
				if (words[i].indexOf('-') != -1) {
					if ((words[i].startsWith("e-") && words[i].length() > 2)
							|| (words[i].startsWith("on-") && words[i].length() > 3)) {
						words[i] = Utils.replace(words[i], "-", "", true);
					} else {
						String[] hifenWords = Utils.getLines(words[i], '-');

						ArrayList newWords = new ArrayList(words.length
								+ hifenWords.length);

						for (int j = 0; j < i; j++) {
							newWords.add(words[j]);
						}

						for (int j = 0; j < hifenWords.length; j++) {
							newWords.add(hifenWords[j]);
						}

						for (int j = i + 1; j < words.length; j++) {
							newWords.add(words[j]);
						}

						words = (String[]) newWords.toArray(new String[newWords
								.size()]);
					}
				}
			}
		}

		ArrayList keys = new ArrayList();

		int count = 0;
		for (int i = 0; i < words.length; i++) {
			if (words[i].length() == 0) {
				continue;
			}

			String original = words[i];

			// retira o tipo de plural que termina em "oes": viloes, tubaroes...
			if (words[i].length() > 4 && words[i].endsWith("oes")) {
				words[i] = words[i].substring(0, words[i].length() - 3) + "ao";
			}

			char endChar = Character.toLowerCase(words[i].charAt(words[i]
					.length() - 1));

			/**
			 * pode apagar: 2006-09-12: agora, "-" é simplesmente excluído // se
			 * tem traço, então ou tira "ataca-la, remove-lo" ou (2006-09-11:
			 * antes era: então remove o traço) então transforma em duas
			 * palavras. int hifenIndex = words[i].indexOf('-'); char endChar =
			 * Character.toLowerCase(words[i].charAt(words[i]. length() - 1));
			 * // tratamento para ver se tem hifen if (hifenIndex != -1) { if
			 * (words[i].length() > 5) { if (endChar == 's') { words[i] =
			 * words[i].substring(0, words[i].length() - 1); }
			 * 
			 * if (words[i].endsWith("-la") || words[i].endsWith("-lo")) {
			 * words[i] = words[i].substring(0, words[i].length() - 2); } else
			 * if (words[i].endsWith("-lhe")) { words[i] = words[i].substring(0,
			 * words[i].length() - 3); } }
			 * 
			 * words[i] = Utils.replace(words[i], "-", "", true); }
			 */

			if (words[i].length() > 7 && words[i].endsWith("mente")) {
				words[i] = words[i].substring(0, words[i].length() - 5);
			} else if (words[i].equals("criacao")) {
				words[i] = "cria";
			} else if (words[i].length() > 1) {
				char startChar = Character.toLowerCase(words[i].charAt(0));

				// se começa ou terma com traços, tira-o
				// caso contrario, verficiar se está no infinitivo e remove.
				// Caso contrario, se for para ignorar plural e termina em "s"
				// se é o caso de alguns, mensagens, etc., tem q virar algum,
				// mensagem
				// caso contrario, simplesmente tira o s.
				// Caso contrario, se for gerúndio, remove o gerúndio, deixando
				// apenas o verbo sem o "r".
				if (endChar == '-' || endChar == '&') {
					words[i] = words[i].substring(0, words[i].length() - 1);
				} else if (endChar == 'r') {
					if (words[i].length() > 3) {
						char endChar2 = Character.toLowerCase(words[i]
								.charAt(words[i].length() - 2));
						// se for ar, er, ir ou or (por, propor, sobrepor...,
						// ignorando por)
						if (endChar2 == 'a' || endChar2 == 'e'
								|| endChar2 == 'i'
								|| (endChar2 == 'o' && !words[i].equals("por"))) {
							words[i] = words[i].substring(0,
									words[i].length() - 1);
						}
					}
				} else if (ignorePlural && (endChar == 's' || endChar == 'm')
						&& ("freeram;".indexOf(words[i] + ";") == -1)) { // tambem
					// não
					// for
					// uma
					// palavra
					// para
					// se
					// ignorar
					// o
					// plural..
					boolean cutted = false;

					if (words[i].length() > 4) {
						char endChar2 = Character.toLowerCase(words[i]
								.charAt(words[i].length() - 2));
						char endChar3 = Character.toLowerCase(words[i]
								.charAt(words[i].length() - 3));
						char endChar4 = Character.toLowerCase(words[i]
								.charAt(words[i].length() - 4));

						// se termina em m:
						// poderem, venderem, cantaram, irem (se transformam em
						// pode, vende, canta, ir)
						// caso contrario, termina em s, entao:
						// alguns, mensagens, carruagens...
						// corações, leões
						// contadores, tradutores (exclui daí: software,
						// spyware: havendo "ware". Nestes casos, retira apenas
						// o s final)
						// pessoais, normal
						if (endChar == 'm') {
							if ((endChar2 == 'e' || endChar2 == 'a')
									&& endChar3 == 'r') {
								words[i] = words[i].substring(0,
										words[i].length() - 3);
								cutted = true;
							}
						} else {
							if (endChar2 == 'n') {
								words[i] = words[i].substring(0,
										words[i].length() - 2) + 'm';
								cutted = true;
							} else if (endChar2 == 'e' && endChar3 == 'o'
									&& endChar4 == 'c') {
								words[i] = words[i].substring(0,
										words[i].length() - 3)
										+ "ao";
								cutted = true;
							} else if (endChar2 == 'e' && endChar3 == 'r'
									&& words[i].indexOf("ware") == -1) {
								words[i] = words[i].substring(0,
										words[i].length() - 2);
								cutted = true;
							} else if (endChar2 == 'i' && endChar3 == 'a') {
								words[i] = words[i].substring(0,
										words[i].length() - 2)
										+ "l";
								cutted = true;
							}
						}
					}

					if (!cutted && endChar == 's'
							&& "sims;cs;".indexOf(words[i] + ';') == -1) { // se
						// for
						// para
						// retirar
						// o
						// "s",
						// testa
						// se
						// não
						// é
						// alguma
						// palavra
						// especial,
						// como
						// "Sims"
						// de
						// "the
						// sims".
						words[i] = words[i].substring(0, words[i].length() - 1);
					}
				} else if (endChar == 'o') {
					if (words[i].length() > 4) {
						char endChar2 = Character.toLowerCase(words[i]
								.charAt(words[i].length() - 2));
						char endChar3 = Character.toLowerCase(words[i]
								.charAt(words[i].length() - 3));

						// se é asim: "o", "d", "n", ou seja, "ndo" (cantando,
						// vertendo, caminhando, acessando, programando...)
						// entao, removce o gerúndio "ndo" e coloca r.
						if (endChar2 == 'd' && endChar3 == 'n') {
							// existem palavras que naturalmente terminam em
							// "ndo". Então, se for elas, não faer o corte.
							if (!words[i].equals("quando")
									&& !words[i].equals("bando")) {
								words[i] = words[i].substring(0,
										words[i].length() - 3);
							}
						}
					}
				}
				if (startChar == '-' || endChar == '&') {
					words[i] = words[i].substring(1, words[i].length());
				}
			}

			// trata terminando com "ção"
			if (words[i].length() > 5
					&& (words[i].endsWith("dor") || words[i].endsWith("cao"))) {
				words[i] = words[i].substring(0, words[i].length() - 3);
			}

			words[i] = words[i].trim();

			// tamnho menor que 2
			if (words[i].length() < 2) { // menor que 3 e não palavra
				// permitida.
				continue;
			}

			String newWord = words[i];

			if (keys.indexOf(newWord) != -1) {
				continue;
			}
			// Palavras inglesas proibidas
			if (("that;but;which;the;and;").indexOf(words[i] + ";") != -1) {
				continue;
			}

			// Palavras portuguesas
			if (("lo;la;los;las;lhe;te;a;e;ao;num;em;no;na;nos;nas;este;esta;estes;estas;isto;"
					+ "nesse;nessa;esse;essa;isso;deste;desta;disso;"
					+ "de;do;da;dos;das;para;por;pelo;cada;todo;toda;"
					+ "pela;pelos;pelas;qual;mas;ou;caso;que;"
					+ "pois;seu;sua;seus;suas;dele;dela;deles;delas;pois;como;")
					.indexOf(words[i] + ";") != -1) {

				continue;
			}

			// Palavras portuguesas: todo não é bom por causa da expressao
			// eminglês "todo" - To Do
			if (("possui;permite;tem;foi;entre;quem;um;uma;tipo;dentre;"
					+ "outro;outra;otimo;excelente;melhor;mesmo;nao;vao;"
					+ "sao;muito;muita;diverso;diversa;tudo;toda;")
					.indexOf(words[i] + ";") != -1) {

				// se a palavra final for "pode", mas a original não for isso
				// (ex: poder vira pode, mas não vale como palavra de exceção)
				if (words[i].equals("pode") && !original.equals("pode")) {
				} else {
					continue;
				}
			}

			// Palavras espanholas
			if (("del;el;lo;la;los;las;un;una;con;").indexOf(words[i] + ";") != -1) {
				continue;
			}

			/*
			 * completo // Palavras inglesas proibidas if ((
			 * "shareware;software;softwares;beside;besides;especially;different;group;groups;according;was;east;"
			 * + "would;use;per;know;available;extra;items;then;" +
			 * "making;europen;can;used;but;" +
			 * "include;high;over;directly;service;services;pr essional;" +
			 * "professionals;always;way;ways;that;wish;performance;win;" +
			 * "since;fine;who;doing;two;" +
			 * "interested;its;none;which;looking;" + "whole;using;" +
			 * "clients;client;cash;between;main;" +
			 * "free;many;much;quality;best;pure;special;also;" +
			 * "too;around;volume;italian;others;other;make;even;ever;" +
			 * "solution;type;types;several;such;no;the;one;export;any;" +
			 * "some;like;kind;kinds;producer;already;got;very;good;with;" +
			 * "has;excelent;import;this;at;your;more;" +
			 * "organization;have;about;visit;new;other;instruments;instrument;in;on;you;thank;our;and;of;"
			 * +
			 * "unique;uniques;please;or;size;see;price;prices;sizes;from;united;latin;for;to;problem;we;as;well;made;all;"
			 * +
			 * "by;mb;kb;product;products;side;specialized;goods;find;both;open;necessary;various;inch;end;are;"
			 * ). indexOf(words[i] + ";") != -1) { continue; } // Palavras
			 * portuguesas if((
			 * "em;no;na;nos;nas;ate;simples;facil;facilmente;possui;possuir;programa;programas;de;do;dos;da;das;tudo;todos;todo;nenhuma;usar;toda;todas;marca;marcas;tecnica;tecnicas;por;pela;"
			 * +
			 * "pelo;pela;pelos;pelas;aqui;usa;uso;mas;mais;queiram;caso;similar;ano;anos;tenham;"
			 * + "interesse;desde;" +
			 * "baixo;este;estes;esta;estas;estao;estamos;" +
			 * "entre;principal;principais;automatico;automaticos;graca;item;" +
			 * "itens;qualidade;melhor;" +
			 * "desenvolvimento;especial;especiais;tambem;outro;outros;solucao;"
			 * + "solucoes;tipo;tipos;muito;muitos;muita;muitas;tal;algum;" +
			 * "alguns;alguma;algumas;etc;nao;tipo;tipos;" +
			 * "lado;cliente;clientes;" + "excelente;bem;bom;" +
			 * "achar;necessario;necessarios;tanto;varios;varias;" +
			 * "fim;seu;sua;seus;suas;mais;" +
			 * "sobre;novo;tem;outro;dentro;voce;obrigado;nosso;" +
			 * "para;problema;nos;comanhia;empresa;empresas;" +
			 * "padrao;unico;unicos;como;feito;veja;preco;precos;tamanho;todo;todos;todas;toda;por;produto;produtos;pais;paises;"
			 * + "sao;somos;ele;ela;eles;elas;geral;aquilo;isto;isso;esse;").
			 * indexOf(words[i] + ";") != -1) { continue; } // Palavras
			 * espanholas if ((
			 * "usted;profesionales;muy;del;producto;productos;nuestro;nuestros;principales;"
			 * + "precios;precio;otro;otros;soluciones;mucho;la;lo;en;" +
			 * "organizacion;el;un;accesorios;accesorio;con;una;" +
			 * "general;internacionales;").indexOf(words[i] + ";") != -1) {
			 * continue; }
			 */

			// Outras palavras proibidas (paises)
			if (othersDeniesWords != null
					&& othersDeniesWords.indexOf(words[i]) != -1) {
				continue;
			}

			// Se não é um pedaço de url
			if (("co;http;com;br;org;uk;www;").indexOf(words[i] + ";") != -1) {
				continue;
			}

			// System.out.println("newWord: " + newWord);

			// Agora, remove letras repetidas: tt, aa, ss, mm, nn...
			StringBuffer newWordSb = new StringBuffer(newWord);
			char lastChar = '-';
			for (int k = 0; k < newWordSb.length(); k++) {
				char currChar = newWordSb.charAt(k);
				if (currChar == lastChar) {
					newWordSb = newWordSb.delete(k, k + 1);
				} else if (lastChar == 'c' && currChar == 'h') {
					newWordSb.setCharAt(k - 1, 'x');
					newWordSb = newWordSb.delete(k, k + 1);
				}

				lastChar = currChar;
			}

			if (newWordSb.length() != newWord.length()) {
				newWord = newWordSb.toString();
			}

			keys.add(newWord);
			count++;

			if (count >= maxKeyWords) {
				break;
			}
		}

		return (String[]) keys.toArray(new String[keys.size()]);
	}

	/**
	 * Retorna as frases do texto, sendo que uma frase é quando termina com
	 * pontuação ou então aparece uma letra maíuscula que não está em aspas
	 * 
	 * @param text
	 *            Texto puro, texto limpo.
	 * @return
	 */
	public static String[] getTextSentences(String text) {
		text = " " + text + " ";

		ArrayList<String> sentences = new ArrayList<String>();

		char[] chars = text.toCharArray();
		StringBuffer currSentence = new StringBuffer();

		boolean igoreUpper = false;

		for (int i = 0; i < chars.length; i++) {
			boolean upper = Character.isUpperCase(chars[i]);
			boolean isSentenceEnd = chars[i] == '\n'
					|| i == chars.length - 1
					|| ((chars[i] == '.' || chars[i] == '!' || chars[i] == '?')
							&& chars[i + 1] == '\"' && chars[i + 1] == '\'');

			// se é o fim de uma senteça ou é maíusculo (internamente faz o
			// trabalhmento se é nome próprio)
			if (isSentenceEnd
					|| (upper && !igoreUpper && currSentence.toString().trim()
							.length() > 0)) {

				// caso tenha algum motivo para não quebrar, bota aqui.
				boolean skipBreak = false;

				// se é fim de setença, coloca
				// caso contrário se for maiúsculo, analisa só se é nome
				// próprio.
				if (isSentenceEnd) {
					currSentence.append(chars[i]);
				} else if (upper) {
					i--;

					// Se o caracter atual é espaço, o próxim caracter e o
					// anterior for caracter ou "," ou ";",
					// então não quebra, porque deve ser um nome próprio ou um
					// maíusculo da sentença.
					boolean name = chars[i] == ' '
							&& (Utils.isLetter(chars[i + 1]) || Utils
									.isNumber(chars[i + 1]))
							&& (Utils.isLetter(chars[i - 1])
									|| Utils.isNumber(chars[i - 1])
									|| chars[i - 1] == ','
									|| chars[i - 1] == ':' || chars[i - 1] == ';');

					boolean nameWithUpInside = (Utils.isLetter(chars[i]) || Utils
							.isNumber(chars[i]))
							|| (Utils.isLetter(chars[i - 1]) || Utils
									.isNumber(chars[i - 1]));

					if (name || nameWithUpInside) {
						i++;
						skipBreak = true;
					}

					// se já não for para cair fora
					if (!skipBreak) {
						// pega a próxima palavra, se for nome próprio, não
						// considera o maiúsculo
						StringBuffer word = new StringBuffer();
						int j = i;

						if (chars[j] == ' ') {
							j++;
						}

						boolean allUp = true;

						for (; j < chars.length; j++) {
							if (Utils.isLetter(chars[j])
									|| Utils.isNumber(chars[j])) {
								word.append(chars[j]);

								if (!Character.isUpperCase(chars[j])) {
									allUp = false;
								}
							} else {
								break;
							}
						}

						if (word.length() > 0 && allUp) {
							i++;
							skipBreak = true;
						}
						/*
						 * System.out.println("**** Palvra: " + allUp + " - " +
						 * word.toString());
						 */
					}
				}

				String trimSentence = currSentence.toString().trim();

				// se a frase tiver menos de 3 caracteres, não considera como
				// uma frase.
				if (!skipBreak && trimSentence.length() < 3) {
					skipBreak = true;

					i++;

					if (i >= chars.length) {
						break;
					}
				}

				// caso não seja para ignorar quebra.
				if (!skipBreak) {
					String sentence = currSentence.toString();

					// se for vazio, não adiciona.
					if (!trimSentence.equals("")) {
						sentences.add(sentence);
					}

					currSentence = new StringBuffer();

					igoreUpper = false;

					continue;
				}
			}

			// Isso é um jeito de ignorar o upper se for o caracter seguinte.
			if (chars[i] == '\'' || chars[i] == '\"' || chars[i] == '('
					|| chars[i] == '-') {
				igoreUpper = true;
			} else {
				igoreUpper = false;
			}

			currSentence.append(chars[i]);
		}

		return (String[]) sentences.toArray(new String[sentences.size()]);
	}

	public static String cleanUpStr(String text) {
		return cleanUpStr(text, " ");
	}

	/**
	 * Limpa a String, convertendo caracteres de quebra em espaços, espaços e
	 * removendo espaços duplicas e no final.
	 * 
	 * @param text
	 *            Texto a ser limpo.
	 * @param replaceStr
	 *            String usada para substituir os esçaos encontrados.
	 * @return Texto limp.
	 */
	public static String cleanUpStr(String text, String replaceStr) {
		if (text == null) {
			throw new IllegalArgumentException("Parametro inválido: text");
		}

		if (replaceStr == null) {
			new IllegalArgumentException("Param: replaceStr");
		}

		text = text.trim();

		if (text.equals("") || text.length() == 1) {
			return text;
		}

		StringBuffer textBuffer = new StringBuffer(text);

		for (int i = 0; i < textBuffer.length(); i++) {
			if (isSpaceChar(textBuffer.charAt(i))) {
				int j = i + 1;
				for (; j < textBuffer.length(); j++) {
					if (!isSpaceChar(textBuffer.charAt(j))) {
						break;
					}
				}

				textBuffer = textBuffer.replace(i, j, replaceStr);
			}
		}

		return textBuffer.toString();
	}

	/**
	 * Formata o prefixo da URL. No caso, remove ou coloca o prefixo "http://",
	 * apenas se for este protocolo. Se não houver "http://", não retira.<br>
	 * <br>
	 * Para o caso de !removePrefix, se já houver prefixo, não faz nada. SE não,
	 * colocar "http://" na frente.<br>
	 * <br>
	 * Só realiza qq operação, se a URL for realmente uma URL. Considera-se uma
	 * "URL" realmente "URL" se ela não estiver em branco, for nula e tem que
	 * possuir pelo menos 1 ponto.
	 * 
	 * @param url
	 *            URl a ser formatada.
	 * @param removePrefix
	 *            Se é para remover, caso seja "http://". Se for falso, coloca e
	 *            a URL não for vazia, coloca "http://" na frente.
	 * @return URL formatada.
	 */
	public static String formatURLPrefix(String url, boolean removePrefix) {
		if (url == null || url.length() == 0 || url.indexOf('.') == -1) {
			return url;
		}

		if (!removePrefix) {
			// Se não possuir protocolo, coloca.
			if (url.indexOf("://") == -1) {
				url = "http://" + url;
			}
		} else if (url.indexOf("http://") == 0) {
			url = url.substring("http://".length(), url.length());
		}

		return url;
	}

	/**
	 * Remove arquivos ou parametros que haja. Ex:
	 * http://www.MyDownloads.com/institucional gera http://www.MyDownloads.com.
	 * 
	 * @param url
	 *            URL a ser limpada.
	 * @return URL sem arquivos ou paametros.
	 */
	public static String getUrlQueryString(String url) {
		if (url == null) {
			throw new IllegalArgumentException("Param invalido: url");
		}

		String query = "";
		int queryIndex = url.indexOf('?');
		if (queryIndex != -1) {
			query = url.substring(queryIndex + 1);
		}

		return query;
	}

	/**
	 * Remove arquivos ou parametros que haja. Ex:
	 * http://www.MyDownloads.com/institucional gera http://www.MyDownloads.com.
	 * 
	 * @param url
	 *            URL a ser limpada.
	 * @return URL sem arquivos ou paametros.
	 */
	public static String getUrlDir(String url) {
		if (url == null) {
			throw new IllegalArgumentException("Param invalido: url");
		}

		// Retira os parametros
		int paramIndex = url.indexOf('?');
		if (paramIndex != -1) {
			url = url.substring(0, paramIndex);
		}

		int barPos = Utils.lastIndexOf(url, '/', url.indexOf("://") + 3);

		// Se não for diretorio já, tira o que vem depois. Caso contrario,
		// apenas
		// testa se há "/" no final para retirá-lo.
		if (barPos != -1 && url.indexOf('.', barPos) != -1) {
			url = url.substring(0, barPos);
		} else if (url.charAt(url.length() - 1) == '/') {
			url = url.substring(0, url.length() - 1);
		}

		return url;
	}

	public static int lastIndexOf(String text, char c, int tillIndex) {
		if (text == null) {
			throw new IllegalArgumentException("Param invalido: text");
		}
		int i = -1;
		for (i = text.length() - 1; i > tillIndex; i--) {
			if (text.charAt(i) == c) {
				break;

			}
		}

		if (i == tillIndex) {
			i = -1;
		}

		return i;
	}

	/**
	 * Corrige o e-mail como pode para ele ser enviado. Por exemplo, se vier
	 * hotmail.com.br, muda para hotmail.com.
	 * 
	 * @param email
	 *            E-mail para ser corrigido.
	 * @return E-mail corrigod.
	 */
	public static String ajustEmail(String email) {
		if (email == null) {
			return email;
		}

		// 2013-05-03 Super trim. Tem um espaço que não é pego... aí este trim
		// tira.
		email = Utils.trimEx(email);

		// Se tiver tamanho zero, ja retorna.
		if (email.length() == 0) {
			return email;
		}

		email = email.toLowerCase();

		// Erro de digitação de alguma coisa no lugar da arroba
		if (email.indexOf('@') == -1) {
			email = Utils.format(email, new String[] { "@" },
					new String[] { "!" });
		}

		// Apara caracteres estranhos
		email = Utils.format(email, new String[] { "", ".", "c", "a", "", "",
				"" }, new String[] { " ", ",", "ç", "ã", "!", "[", "]" });

		// Ajustes em terminações e inícios
		if (email.endsWith(".c")) {
			email = email.substring(0, email.length() - 2) + ".com";
		} else if (email.endsWith(".")) {
			email = email.substring(0, email.length() - 1);
		} else if (email.endsWith(".b")) {
			email = email.substring(0, email.length() - 2) + ".br";
		} else if (email.endsWith(".co.br")) {
			email = email.substring(0, email.length() - 6) + ".com.br";
		} else if (email.endsWith(".coom")) {
			email = email.substring(0, email.length() - 5) + ".com";
		} else if (email.endsWith(".com.be")) {
			email = email.substring(0, email.length() - 7) + ".com.br";
		}

		if (email.startsWith("mailto:")) {
			email = email.substring("mailto:".length()).trim();
		}

		if (email.startsWith("email:")) {
			email = email.substring("email:".length()).trim();
		}

		if (email.indexOf("@.") != -1) {
			email = Utils.replace(email, "@.", "@");
		}

		if (email.endsWith(".om")) {
			email = Utils.replace(email, ".om", ".com");
		}

		// caso do hotmail
		if (email.indexOf("@hot") != -1) {
			if (email.indexOf("@hotmail.com") == -1) {
				if (email.indexOf("@hotmal.c") != -1) {
					email = Utils.replace(email.toLowerCase(), "@hotmal.c",
							"@hotmail.c");
				} else if (email.indexOf("@hotimal.c") != -1) {
					email = Utils.replace(email.toLowerCase(), "@hotimal.c",
							"@hotmail.c");
				} else if (email.indexOf("@hotail.c") != -1) {
					email = Utils.replace(email.toLowerCase(), "@hotail.c",
							"@hotmail.c");
				} else if (email.indexOf("@hotmil.com") != -1) {
					email = Utils.replace(email.toLowerCase(), "@hotmil.com",
							"@hotmail.com");
				} else if (email.indexOf("@hotamil.com") != -1) {
					email = Utils.replace(email.toLowerCase(), "@hotamil.com",
							"@hotmail.com");
				} else if (email.indexOf("@hotimail.c") != -1) {
					email = Utils.replace(email.toLowerCase(), "@hotimail.c",
							"@hotmail.c");
				} else if (email.indexOf("@hotmaiol") != -1) {
					email = Utils.replace(email.toLowerCase(), "@hotmaiol",
							"@hotmail");
				}
			}

			// 2013-06-28 Hotmail agora tem .com.br
			// if (email.indexOf("@hotmail.com.br") != -1) {
			// email = Utils.replace(email.toLowerCase(), "@hotmail.com.br",
			// "@hotmail.com");
			// }
		} else if (email.indexOf("@gmail.com.br") != -1) {
			email = Utils.replace(email.toLowerCase(), "@gmail.com.br",
					"@gmail.com");
		} else if (email.indexOf("@yhoo.com") != -1) {
			email = Utils.replace(email.toLowerCase(), "@yhoo.com",
					"@yahoo.com");
		} else if (email.indexOf("@yaoo.com") != -1) {
			email = Utils.replace(email.toLowerCase(), "@yaoo.com",
					"@yahoo.com");
		} else if (email.indexOf("@yhaoo.com") != -1) {
			email = Utils.replace(email.toLowerCase(), "@yhaoo.com",
					"@yahoo.com");
		}

		return email;
	}

	public static MessagePool getMessagePool() {
		return messagePool;
	}

	public static void setMessagePool(MessagePool newMessagePool) {
		messagePool = newMessagePool;
	}

	public static String stuffStr(String txt, String stuffUnit, int min,
			boolean after) {
		if (txt == null) {
			throw new IllegalArgumentException("Param: txt");
		}

		if (stuffUnit == null) {
			throw new IllegalArgumentException("Param: stuffUnit");
		}

		if (min <= 0) {
			throw new IllegalArgumentException("Param: min <= 0");
		}

		if (txt.length() > min) {
			return txt;
		}

		if (after) {
			while (txt.length() < min) {
				txt += stuffUnit;
			}
		} else {
			while (txt.length() < min) {
				txt = stuffUnit + txt;
			}
		}

		return txt;
	}

	public static String miliToHour(int mili, String pattern) {
		if (pattern == null) {
			throw new IllegalArgumentException("Param: pattern");
		}

		GregorianCalendar gcTimeAvg = Utils.getGregorianCalendarInstance();
		gcTimeAvg.set(2004, 4, 23, 0, 0, 0);
		gcTimeAvg.add(gcTimeAvg.MILLISECOND, mili);
		return Utils.dateToStr(gcTimeAvg.getTime(), pattern);

	}

	public static double calculateHowMuchMore(double first, double second) {
		double result = 0;
		if (second > 0) {
			result = ((first + 0.0) - (second + 0.0)) / (second + 0.0);
		}

		return result;
	}

	public static String[] cleanUp(String[] values, boolean trim,
			boolean removeNull, boolean removeEmpty, boolean removeEquals) {
		if (values == null) {
			throw new IllegalArgumentException("Param: values");
		}

		ArrayList valuesAl = new ArrayList();

		// Para todos os elementso do conjunto, se for válido, acre3scenta
		// na lista.
		for (int i = 0; i < values.length; i++) {
			// Se for nulo, não é válido.
			if (removeNull && values[i] == null) {
				continue;
			}

			if (trim) {
				values[i] = values[i].trim();
			}

			if (removeEmpty && values[i].equals("")) {
				continue;
			}

			boolean hasEquals = false;

			if (removeEquals) {
				for (int j = 0; j < valuesAl.size(); j++) {
					if (valuesAl.get(j) != null && values[i] != null
							&& values[i].equals(valuesAl.get(j))) {
						hasEquals = true;
						break;
					}
				}
			}

			// Se não há iguais, é válido.
			if (!hasEquals) {
				valuesAl.add(values[i]);
			}
		}

		return (String[]) valuesAl.toArray(new String[valuesAl.size()]);

	}

	/**
	 * Limpa o conjunto, removendo iguais e nulos.
	 * 
	 * @param values
	 *            Valores que serão limpos.
	 * @param max
	 *            Quantidade máxima de valores.
	 * @param trim
	 *            Se é para apliacr trim.
	 * @param removeEmpty
	 *            Remove as linhas em branco.
	 * @return Array.
	 */
	public static String[] cleanUp(String[] values, boolean trim,
			boolean removeEmpty) {
		if (values == null) {
			throw new IllegalArgumentException("Param: values");
		}

		ArrayList valuesAl = new ArrayList();

		// Para todos os elementso do conjunto, se for válido, acre3scenta
		// na lista.
		for (int i = 0; i < values.length; i++) {
			// Se for nulo, não é válido.
			if (values[i] == null) {
				continue;
			}

			if (trim) {
				values[i] = values[i].trim();
			}

			if (values[i].equals("")) {
				continue;
			}

			boolean hasEquals = false;

			// Verifica se há iguais.
			for (int j = 0; j < values.length; j++) {
				if (j == i) {
					continue;
				}

				if (values[j] == null) {
					continue;
				}

				if (values[i].equals(values[j])) {
					values[i] = null;
					hasEquals = true;
					break;
				}
			}

			// Se não há iguais, é válido.
			if (!hasEquals) {
				valuesAl.add(values[i]);
			}
		}

		return (String[]) valuesAl.toArray(new String[valuesAl.size()]);

	}

	/**
	 * Limpa o conjunto, removendo iguais e nulos.
	 * 
	 * @param values
	 *            Valores que serão limpos.
	 * @param max
	 *            Quantidade máxima de valores.
	 * @param trim
	 *            Se é para apliacr trim.
	 * @return Array.
	 */
	public static String[] cleanUp(String[] values, boolean trim) {
		return cleanUp(values, trim, false);
	}

	public static String[] cleanUp(String[] values) {
		return cleanUp(values, false, false);
	}

	/**
	 * Embaralha as palavras de acordo com o conteúdo.
	 * 
	 * @param text
	 *            Palavra para ser embaralhada.
	 * @param password
	 *            Uma pavra-chave que influencia no embaralhamento.
	 * @return Palavra embaralahda.
	 */
	public static String shuffleLetters(String text, String password) {
		if (text == null) {
			throw new IllegalArgumentException("Param: text");
		}

		if (text.length() < 3) {
			return text;
		}

		StringBuffer sb = new StringBuffer(text);

		// Calcula o gerador da troca
		int sum = 0;
		for (int i = 0; i < text.length(); i++) {
			sum += (byte) text.charAt(i);
		}

		if (password != null) {
			for (int i = 0; i < password.length(); i++) {
				sum += (byte) password.charAt(i);
			}
		}

		// Troca de posição
		int offset = (sum % 4) + 2;
		int i = offset;
		while (true) {
			if (i + offset - 1 > text.length()) {
				break;
			}

			char c = sb.charAt(i - offset);
			sb.setCharAt(i - offset, sb.charAt(i));
			sb.setCharAt(i, c);

			i += offset + 1;
		}

		return sb.toString();
	}

	/**
	 * Obtem a diferença de datas em dias.
	 * 
	 * @param from
	 *            Data inicial.
	 * @param to
	 *            Data final.
	 * @return Quantos dias de form para to.
	 */
	public static long subDates(Date from, Date to) {
		if (from == null) {
			throw new IllegalArgumentException("Param: from");
		}

		if (to == null) {
			throw new IllegalArgumentException("Param: to");
		}

		Date fromClean = Utils.strToDate(Utils.dateToStr(from));
		Date toClean = Utils.strToDate(Utils.dateToStr(to));

		long diffMillis = toClean.getTime() - fromClean.getTime();
		long diffDays = diffMillis / (1000 * 60 * 60 * 24);

		return diffDays;
	}

	/**
	 * Ob´tem o nome da linguage dado o código padrão. Ex: pt_BR e en_US
	 * retonarm "Portugues (Brasil)" e "Ingles (EUA").
	 * 
	 * @param value
	 *            Valor da língua de acordo com o padrão.
	 * @return Nome da língua por extenso.
	 */
	public static String getLanguageName(String value) {
		if (value == null) {
			return value;
		}

		if (value.equals("en")) {
			value = "Inglês";
		} else if (value.equals("-")) {
			value = "Outros";
		} else if (value.equals("en_US")) {
			value = "Inglês (EUA)";
		} else if (value.equals("ar")) {
			value = "Árabe";
		} else if (value.equals("ar_AE")) {
			value = "Árabe (Emirados Árabes Unidos)";
		} else if (value.equals("ar_BH")) {
			value = "Árabe (Bahrain)";
		} else if (value.equals("ar_DZ")) {
			value = "Árabe (Algéria)";
		} else if (value.equals("ar_EG")) {
			value = "Árabe (Egito)";
		} else if (value.equals("ar_IQ")) {
			value = "Árabe (Iraque)";
		} else if (value.equals("ar_JO")) {
			value = "Árabe (Jordânia)";
		} else if (value.equals("ar_KW")) {
			value = "Árabe (Kuwait)";
		} else if (value.equals("ar_LB")) {
			value = "Árabe (Líbano)";
		} else if (value.equals("ar_LY")) {
			value = "Árabe (Líbia)";
		} else if (value.equals("ar_MA")) {
			value = "Árabe (Morrocos)";
		} else if (value.equals("ar_OM")) {
			value = "Árabe (Oman)";
		} else if (value.equals("ar_QA")) {
			value = "Árabe (Qatar)";
		} else if (value.equals("ar_SA")) {
			value = "Árabe (Arábia Saudita)";
		} else if (value.equals("ar_SD")) {
			value = "Árabe (Sudão)";
		} else if (value.equals("ar_SY")) {
			value = "Árabe (Síria)";
		} else if (value.equals("ar_TN")) {
			value = "Árabe (Tunísia)";
		} else if (value.equals("ar_YE")) {
			value = "Árabe (Iêmen)";
		} else if (value.equals("be")) {
			value = "Belorusso";
		} else if (value.equals("be_BY")) {
			value = "Belorusso (Belorrússia)";
		} else if (value.equals("bg")) {
			value = "Búlgaro";
		} else if (value.equals("bg_BG")) {
			value = "Búlgaro (Bulgária)";
		} else if (value.equals("ca")) {
			value = "Catalão";
		} else if (value.equals("ca_ES")) {
			value = "Catalão (Espanha)";
		} else if (value.equals("ca_ES_EURO")) {
			value = "Catalão (Espanha)";
		} else if (value.equals("cs")) {
			value = "Tcheco";
		} else if (value.equals("cs_CZ")) {
			value = "Tcheco (República Tcheca)";
		} else if (value.equals("da")) {
			value = "Dinamarquês";
		} else if (value.equals("da_DK")) {
			value = "Dinamarquês (Dinamarca)";
		} else if (value.equals("de")) {
			value = "Alemão";
		} else if (value.equals("de_AT")) {
			value = "Alemão (Áustria)";
		} else if (value.equals("de_AT_EURO")) {
			value = "Alemão (Áustria)";
		} else if (value.equals("de_CH")) {
			value = "Alemão (Suíça)";
		} else if (value.equals("de_DE")) {
			value = "Alemão (Alemanha)";
		} else if (value.equals("de_DE_EURO")) {
			value = "Alemão (Alemanha)";
		} else if (value.equals("de_LU")) {
			value = "Alemão (Luxemburgo)";
		} else if (value.equals("de_LU_EURO")) {
			value = "Alemão (Luxemburgo)";
		} else if (value.equals("el")) {
			value = "Grego";
		} else if (value.equals("el_GR")) {
			value = "Greek (Greece)";
		} else if (value.equals("en_AU")) {
			value = "Inglês (Austrália)";
		} else if (value.equals("en_CA")) {
			value = "Inglês (Canadá)";
		} else if (value.equals("en_GB")) {
			value = "Inglês (Reino Unido)";
		} else if (value.equals("en_IE")) {
			value = "Inglês (Irlândia)";
		} else if (value.equals("en_IE_EURO")) {
			value = "Inglês (Ireland,Euro)";
		} else if (value.equals("en_NZ")) {
			value = "Inglês (Nova Zelândia)";
		} else if (value.equals("en_ZA")) {
			value = "Inglês (South Africa)";
		} else if (value.equals("es")) {
			value = "Espanhol";
		} else if (value.equals("es_BO")) {
			value = "Espanhol (Bolívia)";
		} else if (value.equals("es_AR")) {
			value = "Espanhol (Argentina)";
		} else if (value.equals("es_CL")) {
			value = "Espanhol (Chile)";
		} else if (value.equals("es_CO")) {
			value = "Espanhol (Colômbia)";
		} else if (value.equals("es_CR")) {
			value = "Espanhol (Costa Rica)";
		} else if (value.equals("es_DO")) {
			value = "Espanhol (República Dominicana)";
		} else if (value.equals("es_EC")) {
			value = "Espanhol (Ecuador)";
		} else if (value.equals("es_ES")) {
			value = "Espanhol (Espanha)";
		} else if (value.equals("es_ES_EURO")) {
			value = "Espanhol (Spain,Euro)";
		} else if (value.equals("es_GT")) {
			value = "Espanhol (Guatemala)";
		} else if (value.equals("es_HN")) {
			value = "Espanhol (Honduras)";
		} else if (value.equals("es_MX")) {
			value = "Espanhol (México)";
		} else if (value.equals("es_NI")) {
			value = "Espanhol (Nicarágua)";
		} else if (value.equals("et")) {
			value = "Estoniano";
		} else if (value.equals("es_PA")) {
			value = "Espanhol (Panamá)";
		} else if (value.equals("es_PE")) {
			value = "Espanhol (Peru)";
		} else if (value.equals("es_PR")) {
			value = "Espanhol (Porto Rico)";
		} else if (value.equals("es_PY")) {
			value = "Espanhol (Paraguai)";
		} else if (value.equals("es_SV")) {
			value = "Espanhol (El Salvador)";
		} else if (value.equals("es_UY")) {
			value = "Espanhol (Uruguai)";
		} else if (value.equals("es_VE")) {
			value = "Espanhol (Venezuela)";
		} else if (value.equals("et_EE")) {
			value = "Estonian (Estonia)";
		} else if (value.equals("fi")) {
			value = "Finlândia";
		} else if (value.equals("fi_FI")) {
			value = "Finnish (Finland)";
		} else if (value.equals("fi_FI_EURO")) {
			value = "Finnish (Finland,Euro)";
		} else if (value.equals("fr")) {
			value = "Francês";
		} else if (value.equals("fr_BE")) {
			value = "Francês (Bélgica)";
		} else if (value.equals("fr_BE_EURO")) {
			value = "Francês (Belgium,Euro)";
		} else if (value.equals("fr_CA")) {
			value = "Francês (Canadá)";
		} else if (value.equals("fr_CH")) {
			value = "Francês (Suíça)";
		} else if (value.equals("fr_FR")) {
			value = "Francês (França)";
		} else if (value.equals("fr_FR_EURO")) {
			value = "Francês (France,Euro)";
		} else if (value.equals("fr_LU")) {
			value = "Francês (Luxemburgo)";
		} else if (value.equals("fr_LU_EURO")) {
			value = "Francês (Luxembourg,Euro)";
		} else if (value.equals("hr")) {
			value = "Croata";
		} else if (value.equals("hr_HR")) {
			value = "Croatian (Croatia)";
		} else if (value.equals("hu")) {
			value = "Húngaro";
		} else if (value.equals("hu_HU")) {
			value = "Hungarian (Hungary)";
		} else if (value.equals("is")) {
			value = "Islandês";
		} else if (value.equals("is_IS")) {
			value = "Icelandic (Iceland)";
		} else if (value.equals("it")) {
			value = "Italiano";
		} else if (value.equals("it_CH")) {
			value = "Italiano (Suíça)";
		} else if (value.equals("it_IT")) {
			value = "Italiano (Itália)";
		} else if (value.equals("it_IT_EURO")) {
			value = "Italian (Italy,Euro)";
		} else if (value.equals("iw")) {
			value = "Hebreu";
		} else if (value.equals("iw_IL")) {
			value = "Hebrew (Israel)";
		} else if (value.equals("ja")) {
			value = "Japonês";
		} else if (value.equals("ja_JP")) {
			value = "Japonês (Japan)";
		} else if (value.equals("ko")) {
			value = "Coreano";
		} else if (value.equals("ko_KR")) {
			value = "Korean (South Korea)";
		} else if (value.equals("lt")) {
			value = "Lituano";
		} else if (value.equals("lt_LT")) {
			value = "Lituano (Lituânia)";
		} else if (value.equals("lv")) {
			value = "Letoniano (Letônea)";
		} else if (value.equals("lv_LV")) {
			value = "Latvian (Lettish) (Latvia)";
		} else if (value.equals("mk")) {
			value = "Macedoniano";
		} else if (value.equals("mk_MK")) {
			value = "Macedoniano (Macedônia)";
		} else if (value.equals("nl")) {
			value = "Holandês";
		} else if (value.equals("nl_BE")) {
			value = "Holandês (Bélgica)";
		} else if (value.equals("nl_BE_EURO")) {
			value = "Dutch (Belgium,Euro)";
		} else if (value.equals("nl_NL")) {
			value = "Dutch (Netherlands)";
		} else if (value.equals("nl_NL_EURO")) {
			value = "Dutch (Netherlands,Euro)";
		} else if (value.equals("no")) {
			value = "Norueguês";
		} else if (value.equals("no_NO")) {
			value = "Norwegian (Norway)";
		} else if (value.equals("no_NO_NY")) {
			value = "Norwegian (Norway,Nynorsk)";
		} else if (value.equals("pl")) {
			value = "Polonês";
		} else if (value.equals("pl_PL")) {
			value = "Polish (Poland)";
		} else if (value.equals("pt")) {
			value = "Português";
		} else if (value.equals("pt_BR")) {
			value = "Português (Brasil)";
		} else if (value.equals("pt_PT")) {
			value = "Português (Portugal)";
		} else if (value.equals("pt_PT_EURO")) {
			value = "Português (Portugal)";
		} else if (value.equals("ro")) {
			value = "Romênio";
		} else if (value.equals("ro_RO")) {
			value = "Romanian (Romania)";
		} else if (value.equals("ru")) {
			value = "Russo";
		} else if (value.equals("ru_RU")) {
			value = "Russo (Rússia)";
		} else if (value.equals("sh")) {
			value = "Serbo-Croatian";
		} else if (value.equals("sh_YU")) {
			value = "Serbo-Croatian (Yugoslavia)";
		} else if (value.equals("sk")) {
			value = "Eslovaco";
		} else if (value.equals("sk_SK")) {
			value = "Slovak (Slovakia)";
		} else if (value.equals("sl")) {
			value = "Eslovênio";
		} else if (value.equals("sl_SI")) {
			value = "Slovenian (Slovenia)";
		} else if (value.equals("sq")) {
			value = "Albanês";
		} else if (value.equals("sq_AL")) {
			value = "Albanian (Albania)";
		} else if (value.equals("sr")) {
			value = "Sérvio";
		} else if (value.equals("sr_YU")) {
			value = "Sérvio (Yugoslavia)";
		} else if (value.equals("sv")) {
			value = "Sueco";
		} else if (value.equals("sv_SE")) {
			value = "Swedish (Sweden)";
		} else if (value.equals("th")) {
			value = "Tailandês";
		} else if (value.equals("th_TH")) {
			value = "Thai (Thailand)";
		} else if (value.equals("tr")) {
			value = "Turco";
		} else if (value.equals("tr_TR")) {
			value = "Turco (Turquia)";
		} else if (value.equals("uk")) {
			value = "Ucraniano";
		} else if (value.equals("uk_UA")) {
			value = "Ukrainian (Ukraine)";
		} else if (value.equals("zh")) {
			value = "Chinês";
		} else if (value.equals("zh_CN")) {
			value = "Chinês (China)";
		} else if (value.equals("zh_HK")) {
			value = "Chinês (Hong Kong)";
		} else if (value.equals("zh_TW")) {
			value = "Chinês (Taiwan)";
		}

		return value;
	}

	/**
	 * Preenche a string com o texto dito até ou mais do tamanho especificado.
	 * 
	 * @param txt
	 *            Texto a ser preenchido.
	 * @param size
	 *            Tamanho mínimo.
	 * @return String preenchiuda.
	 */
	public static String fillString(String txt, String stuff, int minSize) {
		while (txt.length() < minSize) {
			txt = stuff + txt;
		}

		return txt;
	}

	/**
	 * Mapeia um valor em relação ao seu correspondente em um array, retornando
	 * o correspondente no outro.
	 * 
	 * @param availValue
	 *            Valor a ser availiado.
	 * @param possibleValues
	 *            Array com os possiveis valores de availValue.
	 * @param mapValues
	 *            Array do mesmo tamanho de possibleValues e retorna o
	 *            correspondente igual de possibleValues em availValue.
	 * @param defaultValue
	 *            Valor padrão de retorno, caso não encontre. Pode ser nulo.
	 * @return Valor correspondente ou o defaultValue.
	 */
	public static Object mapValueToValue(Object availValue,
			Object[] possibleValues, Object[] mapValues, Object defaultValue) {
		if (availValue == null) {
			throw new IllegalArgumentException("Param: availValue");
		}

		if (possibleValues == null) {
			throw new IllegalArgumentException("Param: possibleValues");
		}

		if (mapValues == null) {
			throw new IllegalArgumentException("Param: mapValues");
		}

		if (possibleValues.length != mapValues.length) {
			throw new IllegalArgumentException(
					"Param: possibleValues.length != mapValues.length");
		}

		Object rValue = defaultValue;

		for (int i = 0; i < possibleValues.length; i++) {
			if (possibleValues[i].equals(availValue)) {
				rValue = mapValues[i];
				break;
			}
		}

		return rValue;
	}

	public static String insert(String str, String insertStr, int offset) {
		if (str == null) {
			throw new IllegalArgumentException("Param: str");
		}

		if (insertStr == null) {
			throw new IllegalArgumentException("Param: insertStr");
		}

		StringBuffer sb = new StringBuffer(str);
		return sb.insert(offset, insertStr).toString();
	}

	public static boolean fileMatch(String filePath, String[] words)
			throws IOException {
		if (filePath == null) {
			throw new IllegalArgumentException("Param: filePath");
		}

		if (words == null) {
			throw new IllegalArgumentException("Param: words");
		}

		File file = new File(filePath);
		if (!file.exists()) {
			throw new IllegalArgumentException(
					"Param: filePath. Não é um caminho válido.");
		}

		if (words.length == 0) {
			return true;
		}

		String fileStr = Utils.readStrFile(filePath);

		boolean match = true;

		for (int i = 0; i < words.length; i++) {
			if (fileStr.indexOf(words[i]) == -1) {
				match = false;
				break;
			}
		}

		return match;
	}

	/**
	 * Orderna a lista de arquivos.
	 * 
	 * @param files
	 *            Arquivos para serem ordernados.
	 * @param by
	 *            Critério. 0: nome; 1: data/hora da última modificação.
	 * @param ascending
	 *            Se é ascendente.
	 */
	public static void orderFiles(File[] files, int by, boolean ascending) {
		if (files == null) {
			throw new IllegalArgumentException("Param: files");
		}

		class FileNameComparator implements Comparator {
			public int compare(Object o1, Object o2) {
				return ((File) o1).getPath().compareTo(((File) o2).getPath());
			}
		}

		class FileTimeComparator implements Comparator {
			public int compare(Object o1, Object o2) {
				return (new Long(((File) o1).lastModified()))
						.compareTo((new Long(((File) o2).lastModified())));
			}
		}

		if (by == 0) {
			Arrays.sort(files, new FileNameComparator());
		} else {
			Arrays.sort(files, new FileTimeComparator());
		}

		if (!ascending) {
			Utils.inverseArray(files);
		}
	}

	@SuppressWarnings("resource")
	public static int getFileLinesCount(String path) throws IOException {
		if (path == null) {
			throw new IllegalArgumentException("Param: path");
		}

		int linesCount = 0;
		FileReader fos = new FileReader(path);

		try {
			BufferedReader br = new BufferedReader(fos);

			while (br.readLine() != null) {
				linesCount++;
			}
		} finally {
			fos.close();
		}

		return linesCount;
	}

	/**
	 * Apaga o arquivo dado ou, se for diretorio, apaga todos os arquivos dos
	 * subdiretorio dado.
	 * 
	 * @param file
	 *            Arquivo a ser apagado ou diretorio a partir do qual terá os
	 *            arquivos removidos.
	 * @return Quantidade de arquivos removidos.
	 */
	public static int deleteAllFiles(File file) {
		if (file == null) {
			throw new IllegalArgumentException("Param: file");
		}

		if (file.getPath().equals("c:\\") || file.getPath().equals("d:\\")
				|| file.getPath().equals("/") || file.getPath().equals("\\")) {
			throw new IllegalArgumentException("Param: rootPath. O "
					+ "caminho é absoluto, o " + "que pode gerar a remoção "
					+ "de todos os arquivos " + "do caminho.");
		}

		int count = 0;

		if (file.isDirectory()) {
			File[] subFiles = file.listFiles();

			for (int i = 0; i < subFiles.length; i++) {
				count += deleteAllFiles(subFiles[i]);
			}

			if (file.delete()) {
				count++;
			}
		}

		if (file.delete()) {
			count++;
		}

		return count;
	}

	/**
	 * Alterna o valor de acordo com o atual.
	 * 
	 * @param currValue
	 *            valor atual. e for nulo ou nao tiver na lista, retorna o
	 *            priemriao.
	 * @param values
	 *            Valores possiveis.
	 * @return Valor atual
	 */
	public static String alternateValues(String currValue, String[] values) {
		if (values == null) {
			return "";
		}

		if (values.length == 0) {
			return "";
		}

		if (currValue == null) {
			return values[0];
		}

		for (int i = 0; i < values.length; i++) {
			if (values[i] != null && values[i].equals(currValue)) {
				if (i < values.length - 1) {
					currValue = values[i + 1];
					break;
				} else {
					currValue = values[0];
					break;
				}
			}
		}

		return currValue;
	}

	public static String alternateValues(String currValue, String value1,
			String value2) {
		if (value1 == null) {
			return "";
		}

		if (value2 == null) {
			return "";
		}

		if (currValue == null) {
			return value1;
		}

		if (value1.equals(currValue)) {
			currValue = value2;
		} else {
			currValue = value1;
		}

		return currValue;
	}

	public static String formatSize(long bytes) {
		return formatSize(bytes, 800, 800, 800);
	}

	/**
	 * Formata o tamanho para String que seja melhor representavel
	 * 
	 * @param bytes
	 *            double
	 * @param maxBytes
	 *            int
	 * @param maxKB
	 *            int
	 * @param maxMB
	 *            int
	 * @return String
	 */
	public static String formatSize(long bytes, long maxBytes, long maxKB,
			long maxMB) {
		if (bytes < 0) {
			return 0 + " bytes";
		}

		if (bytes < maxBytes) {
			return Utils.formatNumber(bytes, 0, 0) + " bytes";
		} else if (Utils.div(bytes, 1024) < maxKB) {
			return Utils.formatNumber(Utils.div(bytes, 1024), 0, 2) + " KB";
		} else if (Utils.div(bytes, 1024 * 1024) < maxMB) {
			return Utils.formatNumber(Utils.div(bytes, 1024 * 1024), 0, 2)
					+ " MB";
		} else {
			return Utils.formatNumber(Utils.div(bytes, 1024 * 1024 * 1024), 0,
					2) + " GB";
		}
	}

	public static double div(int num1, int num2) {
		return (num1 + 0.0) / (num2 + 0.0);
	}

	public static double div(long num1, long num2) {
		return (num1 + 0.0) / (num2 + 0.0);
	}

	public static String toFileName(String fileName) {
		// completo return Utils.format(fileName,
		// new String[] {"_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_",
		// "_"},
		// new String[] {"\\", "/", ":", "*", "?", "\"", "<", ">", "|", "=",
		// "--", "&"}, true);
		return Utils.format(fileName, new String[] { "_", "_" }, new String[] {
				"\\", "/" }, true);
	}

	public static String reverseToFileName(String fileName) {
		// completo return Utils.format(fileName,
		// new String[] {"_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_",
		// "_"},
		// new String[] {"\\", "/", ":", "*", "?", "\"", "<", ">", "|", "=",
		// "--", "&"}, true);
		return Utils.format(fileName, new String[] { "/" },
				new String[] { "_" }, true);
	}

	/**
	 * 
	 * 
	 * @param query
	 *            String
	 * @param simplified
	 *            Se é para tirar hífens, acentos, sem plural, etc.
	 * @return String[]
	 */
	/*
	 * public static String[] extractKeyWords(String query, boolean simplified)
	 * { if (query == null) { throw new
	 * IllegalArgumentException("Param: query"); }
	 * 
	 * if (query.equals("")) { return new String[0]; }
	 * 
	 * if (simplified) { query = Utils.replace(Utils.removeTones(query), "-",
	 * "", true); }
	 * 
	 * 
	 * return Utils.getLines(query, ' ', simplified, null); }
	 */

	public static int indexOfEx(String text, String substring, int fromIndex,
			boolean ignoreCase, boolean ignoreTones) {
		// ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖ×ØÙÚÛÜÝÞßàáâãäåæçèéêëìíîïðñòóôõö÷øùúûüýþ

		// 1. Tratamentos inicias: se for em branco, retorna
		if (substring.equals("")) {
			return 0;
		}

		if (text.equals("")) {
			return -1;
		}

		// 2. Se não for para ignorar acentos, já retorna
		// Caso contrario, procura substring em text.
		if (!ignoreTones) {
			return text.indexOf(substring);
		} else {
			char[] textCs = text.toCharArray();
			char[] substringCs = substring.toCharArray();

			for (int i = fromIndex; i < textCs.length; i++) {
				if (isCharsEqualIgnoreTone(textCs[i], substringCs[0],
						ignoreCase)) {
					boolean equal = true;
					boolean noAvail = substringCs.length != 1;
					for (int j = 1; j < substringCs.length
							&& i + j < textCs.length; j++) {
						if (!isCharsEqualIgnoreTone(textCs[i + j],
								substringCs[j], ignoreCase)) {
							equal = false;
							break;
						}

						noAvail = false;
					}

					// Se não avaliou nenhuma vez, não é igual apenas por causa
					// do primeiro caracter (apenas se é do tamanho 1)
					if (noAvail) {
						equal = false;
					}

					// se correu toda a substring e não achou nada diferente,
					// então são iguias. Retorna o Index.
					if (equal) {
						return i;
					}
				}
			}
		}

		return -1;
	}

	public static boolean isCharsEqualIgnoreTone(char c1, char c2,
			boolean ignoreCase) {
		if (ignoreCase) {
			if (!Character.isLowerCase(c1)) {
				c1 = Character.toLowerCase(c1);
			}

			if (!Character.isLowerCase(c2)) {
				c2 = Character.toLowerCase(c2);
			}
		}

		boolean equal = isCharsEqualIgnoreToneTester(c1, c2);

		if (!equal) {
			equal = isCharsEqualIgnoreToneTester(c2, c1);
		}

		return equal;
	}

	protected static boolean isCharsEqualIgnoreToneTester(char c1, char c2) {
		if (c1 == c2) {
			return true;
		}

		// ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖ×ØÙÚÛÜÝÞßàáâãäåæçèéêëìíîïðñòóôõö÷øùúûüýþ
		if (c1 == 'a' && (c2 >= 'à' && c2 <= 'å')) { // àáâãäå
			return true;
		} else if (c1 == 'c' && c2 == 'ç') { // ç
			return true;
		} else if (c1 == 'e' && (c2 >= 'è' && c2 <= 'ë')) { // èéêë
			return true;
		} else if (c1 == 'i' && (c2 >= 'ì' && c2 <= 'ï')) { // ìíîï
			return true;
		} else if (c1 == 'n' && c2 == 'ñ') { // ñ
			return true;
		} else if (c1 == 'o' && (c2 >= 'ò' && c2 <= 'ö')) { // òóôõö
			return true;
		} else if (c1 == 'u' && (c2 >= 'ù' && c2 <= 'ü')) { // ùúûü
			return true;
		} else if (c1 == 'A' && (c2 >= 'À' && c2 <= 'Å')) { // ÀÁÂÃÄÅ
			return true;
		} else if (c1 == 'C' && c2 == 'Ç') { // Ç
			return true;
		} else if (c1 == 'E' && (c2 >= 'È' && c2 <= 'Ë')) { // ÈÉÊË
			return true;
		} else if (c1 == 'E' && (c2 >= 'Ì' && c2 <= 'Ï')) { // ÌÍÎÏ
			return true;
		} else if (c1 == 'N' && c2 == 'Ñ') { // Ñ
			return true;
		} else if (c1 == 'U' && (c2 >= 'Ù' && c2 <= 'Ü')) { // ÙÚÛÜ
			return true;
		} else if (c1 == 'Y' && c2 == 'Ý') { // Ý
			return true;
		} else {
			return false;
		}
	}

	public static boolean equalsEx(String text, String substring,
			boolean ignoreCase, boolean ignoreTones) {
		return text.length() == substring.length()
				&& indexOfEx(text, substring, 0, ignoreCase, ignoreTones) == 0;
	}

	/**
	 * 
	 * @param source
	 *            String
	 * @param search
	 *            String
	 * @param target
	 *            String
	 * @param ignoreCase
	 *            Se é ou não para ignorar o case.
	 * @param ignoreTone
	 *            Se é para ignorar os acentos.
	 * @param ignoreCase
	 *            Reserva o texto original, colocando-o dentro do target onde
	 *            houver o texto "%s". Por exemplo, se deseja suybsitiuir o
	 *            texto search por <b>texto</b>, ao invés de ser o texto
	 *            procurado <b>search</b>. Ou seja, o texto é preservado.
	 * @return String
	 */
	public static String replaceAll(String source, String search,
			String target, boolean ignoreCase, boolean ignoreTone,
			boolean preserveOriginal) {
		if (source == null) {
			throw new IllegalArgumentException("Param: source");
		}

		if (search == null) {
			throw new IllegalArgumentException("Param: search");
		}

		if (ignoreCase) {
			// Cria os SBs e o com caixa baixa. Passa tambem o q procurar
			// para caixa baixa.
			StringBuffer sbSource = new StringBuffer(source);
			StringBuffer sbLower = new StringBuffer(source.toLowerCase());

			search = search.toLowerCase();

			int currIndex = 0;

			// Tem o máximo de 10.000 substituições. Isto é apenas uma garantia.
			for (int i = 0; i < 10000; i++) {
				int index = -1;

				if (!ignoreTone) {
					index = sbLower.indexOf(search, currIndex);
				} else {
					index = Utils.indexOfEx(sbLower.toString(), search,
							currIndex, false, true);
				}

				if (index != -1) {
					int replaceTo = index + search.length();

					if (sbSource.length() < replaceTo) {
						replaceTo = sbSource.length();
					}

					// se não for para pegar o orginal e inserir, faz isso
					// Caso contrario, pega o original e substitui ond eha %s.
					if (!preserveOriginal) {
						sbSource.replace(index, replaceTo, target);
						sbLower.replace(index, replaceTo, target);
					} else {
						sbSource.replace(index, replaceTo, Utils.replace(
								target, "%s",
								sbSource.substring(index, replaceTo), true));
						sbLower.replace(index, replaceTo,
								Utils.replace(target, "%s",
										sbLower.substring(index, replaceTo),
										true));
					}

					currIndex = index + target.length();
				} else {
					break;
				}
			}

			return sbSource.toString();
		} else {
			return replace(source, search, target, true);
		}
	}

	/**
	 * Extrai parâmetros de uma string no formato de parâmetros de tags.
	 * 
	 * @param tagParamStr
	 * @return
	 */
	public static HashMap<String, String> parseTagParams(String tagParamStr) {
		HashMap<String, String> tagParamsHM = new HashMap<String, String>();

		char[] cs = tagParamStr.toCharArray();

		for (int i = 0; i < cs.length; i++) {
			if (Utils.isLetter(cs[i])) {
				StringBuffer paramName = new StringBuffer();

				for (; i < cs.length; i++) {
					if (Utils.isLetter(cs[i]) || cs[i] == '-') {
						paramName.append(cs[i]);
					} else {
						break;
					}
				}

				boolean paramOk = false;
				boolean quote = false;
				char whichQuote = '-';

				for (; i < cs.length; i++) {
					if (cs[i] == '=') {
						paramOk = true;
					} else if (cs[i] == '"' || cs[i] == '\'') {
						if (!quote) {
							quote = true;
							whichQuote = cs[i];
						} else {
							paramOk = false;
							break;
						}
					} else if (!Utils.isSpaceChar(cs[i])) {
						break;
					}
				}

				String paramValueStr = "";

				if (paramOk) {
					StringBuffer paramValue = new StringBuffer();

					for (; i < cs.length; i++) {
						if (quote) {
							if (whichQuote == cs[i]) {
								break;
							}
						} else if (Utils.isSpaceChar(cs[i])) {
							break;
						}

						paramValue.append(cs[i]);
					}

					paramValueStr = paramValue.toString().trim();
				} else {
					i--;
				}

				tagParamsHM.put(paramName.toString(), paramValueStr);
			}

		}

		return tagParamsHM;
	}

	/**
	 * OBS: Tem varias coisas incompletas: exclusão do conteúdo de script e
	 * style, letras htmlelizadas (trocar por letras de verdade). Fiz até aq
	 * porque este foi um método emergencial.
	 * 
	 * @param html
	 *            String
	 * @return String
	 */
	public static String extractTextFromHtml(String html) {
		if (html == null) {
			throw new IllegalArgumentException("Param: html");
		}

		char[] htmlC = html.toCharArray();
		StringBuffer text = new StringBuffer();
		text.ensureCapacity(htmlC.length / 2);

		int notContent = 0;

		for (int i = 0; i < htmlC.length; i++) {
			/*
			 * boolean isHtml = htmlC[i] == '<'; && i < htmlC.length - 1 &&
			 * (Utils.isLetter(htmlC[i + 1]) || htmlC[i + 1] == '/' || htmlC[i +
			 * 1] == '!')
			 */
			;

			// Se pode é tag (< acompanhado de uma letra
			if (htmlC[i] == '<') {
				// guarda a tag atual pq pode ser q a tag seja espaço.
				StringBuffer currTag = new StringBuffer();
				boolean tagging = true;

				// até o fim da tag.
				for (i = i + 1; i < htmlC.length; i++) {
					if (htmlC[i] == '>') {
						break;
					} else if (tagging) {
						if (htmlC[i] == ' ') {
							tagging = false;
						} else {
							currTag.append(htmlC[i]);
						}
					}
				}

				// Se for tag de espaço, coloca o espaço e bota pra avaliar
				// novamente. Caso contrario, vai sinbora.
				String tagName = currTag.toString().toLowerCase();
				;
				if (tagName.startsWith("br") || tagName.startsWith("div")
						|| tagName.startsWith("/div")
						|| tagName.startsWith("/td")
						|| tagName.startsWith("td")) {
					htmlC[i] = ' ';
					i--;
				} else if (tagName.startsWith("script")
						|| tagName.startsWith("style")) {
					notContent--;
				} else if (tagName.startsWith("/script")
						|| tagName.startsWith("/style")) {
					notContent++;
				}
			} else {
				if (notContent < 0) {
					continue;
				}

				for (; i < htmlC.length; i++) {
					// Se for um espaço, coloca e segue enquanto for espaço
					// (para imediatamente se não for, claro.
					// Caso contrario, se não for início de uma tag, adiciona
					// caso contrario, para para avaliação.
					if (htmlC[i] == '<') {
						i--;
						break;
					}

					if (Utils.isSpaceChar(htmlC[i])) {
						text.append(' ');

						// enqunto for espaço seguido.
						for (i = i + 1; i < htmlC.length
								&& Utils.isSpaceChar(htmlC[i]); i++) {
							;
						}

						i--;
					} else {
						text.append(htmlC[i]);
					}
				}
			}
		}

		return text.toString();
	}

	public static void copySingleFile(String srcFile, String dstFile)
			throws IOException {
		// OBS: tinha bug para copiar para o próprio arquivo, zerava. Agora tá
		// corrigido.
		try {
			FileInputStream lv_fileInputStream = new FileInputStream(srcFile);
			int lv_dateigroesse = lv_fileInputStream.available();
			byte[] lv_puffer = new byte[lv_dateigroesse];
			lv_fileInputStream.read(lv_puffer, 0, lv_dateigroesse);
			lv_fileInputStream.close();

			FileOutputStream lv_fileOutputStream = new FileOutputStream(dstFile);
			lv_fileOutputStream.write(lv_puffer, 0, lv_puffer.length);
			lv_fileOutputStream.close();
		} catch (FileNotFoundException e) {
			throw new IOException("File not found");
		}
	}

	/**
	 * Obtem o index item do array, se houver. Se não houver, retorna o padrao.
	 * 
	 * @param objs
	 *            Object[]
	 * @param index
	 *            int
	 * @param defaultItem
	 *            Object
	 * @return Object
	 */
	public static Object pickItem(Object[] objs, int index, Object defaultItem) {
		if (objs != null && objs.length > index) {
			return objs[index];
		}

		return defaultItem;
	}

	/**
	 * Se existe ao endereço dado, ou seja, se ele esta no ar. Voce deve estar
	 * conectado antes de chamar este método.
	 * 
	 * @param url
	 *            String
	 * @return Retorna nulo se estiver certo ou diferente de nulo (o erro) se
	 *         tiver problemas.
	 */
	public static String existsUrl(String url) {
		String erorrMsg = null;

		try {
			// abre a URL e apenas se conecata. Se não der erro, valeu!
			URL urlObject = new URL(url);
			URLConnection con = urlObject.openConnection();
			con.setRequestProperty("User-Agent",
					"Mozilla/4.0 (compatible; MSIE 5.5; Windows NT 5.0; H010818)");
			con.connect();
		} catch (IOException error) {
			erorrMsg = Utils.formatIfNull(error.getMessage());
		}

		return erorrMsg;
	}

	public static String urlEncode(String text) {
		try {
			return Utils.replace(URLEncoder.encode(text, URL_ENCODE_ENC), "+",
					"%20", true);
		} catch (Exception error) {
			/*
			 * try { URL_ENCODE_ENC = "UTF-8"; return URLEncoder.encode(text,
			 * "UTF-8"); } catch (Exception e) {
			 * JSPUtils.log("[Utils.urlEncode] Erro duplo: " +
			 * error.getMessage());
			 */
			return text;
			// }
		}
	}

	public static String urlEncode(String text, String encode) {
		try {
			return URLEncoder.encode(text, encode);
		} catch (Exception error) {
			JSPUtils.log("[Utils.urlEncode] Erro duplo: " + error.getMessage());
			return text;
		}
	}

	public static String urlDecode(String text) {
		try {
			return URLDecoder.decode(text, URL_ENCODE_ENC);
			// return Utils.replace(URLEncoder.encode(text, URL_ENCODE_ENC),
			// "+", "$20");
		} catch (Exception error) {
			/*
			 * try { URL_ENCODE_ENC = "UTF-8"; return URLDecoder.decode(text,
			 * "UTF-8"); } catch (Exception e) {
			 * JSPUtils.log("[Utils.urlEncode] Erro duplo: " +
			 * error.getMessage());
			 */
			return text;
			// }
		}
	}

	public static String getFileExtension(String text) {
		int ind = text.lastIndexOf(".");
		if (0 < ind && ind < (text.length() - 1)) {
			return text.substring(ind + 1).toLowerCase();
		}
		return (null);
	}

	public static String getFilename(String filename) {
		int ind = filename.lastIndexOf('/');

		if (ind == -1) {
			ind = filename.lastIndexOf('\\');
		}

		if (0 < ind && ind < (filename.length() - 1)) {
			return filename.substring(ind + 1);
		}

		return (null);
	}

	/**
	 * Executa uma busca em um array ordenado cujo custo do indexOf é de log(n)
	 * 
	 * @param words
	 *            Object[]
	 * @param word
	 *            Object
	 * @return int
	 */
	public static int orderedIndexOf(Comparable searchWord, Comparable[] words) {
		if (words.length == 0) {
			return -1;
		}

		int index = -1;
		int left = 0;
		int right = words.length - 1;

		int opCount = 0;

		if (words[left].equals(searchWord)) {
			index = left;
		} else if (words[right].equals(searchWord)) {
			index = right;
		} else if (words[left].compareTo(searchWord) < 0
				&& words[right].compareTo(searchWord) > 0) {
			while (true) {
				if (right - left < 2) {
					for (int i = left; i < right + 1; i++) {
						if (words[i].equals(searchWord)) {
							index = i;
							break;
						}
					}

					break;
				}

				int m = left + ((right - left) / 2);

				if (searchWord.compareTo(words[m]) < 0) {
					right = m;
				} else if (searchWord.compareTo(words[m]) > 0) {
					left = m;
				} else {
					index = m;
					break;
				}
			}
		}

		return index;
	}

	/**
	 * Retorna o nome do arquivo de imagem cujo nome é o passado.
	 * 
	 * Por exemplo, o nome pode ser "foto", mas o nome do arquivo pode ser
	 * "foto.jpg", "foto.jpeg", etc.
	 * 
	 * Este método não é sensível a caixa.
	 * 
	 * @param path
	 *            String
	 * @param imageName
	 *            String
	 * @return Nomes dos arquivos de imagem que tem este nome que xistirem.
	 */
	public static String[] getImageFilePath(String path, String imageName) {
		final class ImageFilenameFilterClass implements FilenameFilter {
			String[] possibleNames;

			public ImageFilenameFilterClass(String name) {
				possibleNames = new String[5];
				possibleNames[0] = name + ".jpg";
				possibleNames[1] = name + ".jpeg";
				possibleNames[2] = name + ".gif";
				possibleNames[3] = name + ".bmp";
				possibleNames[4] = name + ".png";
			}

			public boolean accept(File dir, String name) {
				for (int i = 0; i < possibleNames.length; i++) {
					if (name.toLowerCase().equals(possibleNames[i])) {
						return true;
					}
				}

				return false;
			}
		}

		return (new File(path)).list(new ImageFilenameFilterClass(imageName));
	}

	public static Object fromIndexToValue(Object indexObj, ArrayList indexList,
			ArrayList valueList) {
		if (indexList == null) {
			throw new IllegalArgumentException("Param: indexList");
		}

		if (valueList == null) {
			throw new IllegalArgumentException("Param: valueList");
		}

		int index = indexList.indexOf(indexObj);

		Object r = null;

		if (index != -1 && index < valueList.size()) {
			r = valueList.get(index);
		}

		return r;
	}

	public static void randomAl(ArrayList al) {
		if (al == null) {
			throw new IllegalArgumentException("Param: al");
		}

		for (int i = 0; i < al.size(); i++) {
			int newPos = Utils.getRandomNumber() % al.size();
			if (newPos != i) {
				Object currObj = al.get(newPos);
				al.set(newPos, al.get(i));
				al.set(i, currObj);
			}
		}
	}

	public static void randomArray(Object[] array) {
		if (array == null) {
			throw new IllegalArgumentException("Param: array");
		}

		for (int i = 0; i < array.length; i++) {
			int newPos = Utils.getRandomNumber() % array.length;
			if (newPos != i) {
				Object currObj = array[newPos];
				array[newPos] = array[i];
				array[i] = currObj;
			}
		}
	}

	public static String formatSQLStringValue(String strValue) {
		if (strValue == null) {
			return null;
		} else {
			strValue = Utils.replace(strValue, "'", "''", true);
			return Utils.replace(strValue, "\\", "\\\\", true);
		}
	}

	public static boolean isCommonWord(String word) {
		boolean is = false;

		// Palavras inglesas proibidas
		if (("of;for;by;you;can;in;your;their;him;her;much;many;more;this;these;that;which;it;he;she;they;we;that;but;which;the;and;is;are;done;some;any;if;welcome;new;make;from;something;get;have;has;my;about;all;up;down;with;must;into;")
				.indexOf(word + ";") != -1) {
			is = true;
		}

		// Palavras portuguesas
		if (!is
				&& ("aqui;leia;mb;tag;tags;voce;voces;o;la;los;las;lhe;te;a;e;i;o;i;u;ao;num;em;no;na;nos;nas;nesta;nestas;nisto;este;esta;estes;estas;isto;"
						+ "nesse;nessa;esse;essa;isso;deste;desta;disso;esses;nem;daquele;nosso;nossos;nossa;nossas;"
						+ "de;do;da;dos;das;para;por;pelo;cada;todo;toda;onde;quando;desde;ja;vai;apenas;algum;algumas;"
						+ "pela;pelos;pelas;qual;qualquer;durante;antes;consco;quais;sem;sobre;aos;mas;mais;menos;ou;caso;que;alem;mesmo;mesma;até;nehum;nenhuma;nunca;inclusive;depois;apos;"
						+ "pois;meu;minha;seu;sua;seus;suas;dele;dela;com;deles;delas;pois;como;ainda;ser;havia;tenho;sou;ver;possuir;estar;estao;esta;pode;podem;tambem;ter;seria;era;foram;sido;seja;disse;estava;feita;feito;fazer;"

						+ "dentro;junto;menor;tinha;mal;mau;junta;basta;bastam;fica;ficar;ficam;tenha;tenham;restante;restantes;tento;tenta;deve;devem;")
						.indexOf(word + ";") != -1) {
			is = true;
		}

		// Palavras portuguesas: todo não é bom por causa da expressao
		// eminglês "todo" - To Do
		if (!is
				&& ("possui;permite;tem;foi;entre;quem;um;uma;tipo;dentre;"
						+ "outro;outra;otimo;excelente;melhor;mesmo;nao;vao;"
						+ "sao;muito;muita;diverso;diversa;tudo;toda;com;br;net;")
						.indexOf(word + ";") != -1) {
			is = true;
		}

		// Palavras espanholas
		if (!is
				&& ("del;el;lo;la;los;las;un;una;con;").indexOf(word + ";") != -1) {
			is = true;
		}

		return is;
	}

	public static String removePlural(String word) {
		String originalWord = word;

		if (word.length() < 3) {
			return word;
		}

		char last1 = word.charAt(word.length() - 1);
		char last2 = word.charAt(word.length() - 2);
		char last3 = word.charAt(word.length() - 3);
		char last4 = '|';
		if (word.length() > 3) {
			last4 = word.charAt(word.length() - 3);
		}

		if (last1 == 's') {
			// exceções, ou seja, palavras com s que são com s mesmo.
			if ("apos;tres;news;".indexOf(word + ";") != -1) {
				return word;

			}

			if (last2 == 'e' && last3 == 'r') { // seres = ser
				word = word.substring(0, word.length() - 2);
			} else if (last2 == 'e' && (last3 == 's' || last3 == 'z')) { // vezes,
				// juízes,
				// meses
				word = word.substring(0, word.length() - 2);
			} else if (last2 == 'i' && last3 == 'a') { // pardais = pardal
				word = word.substring(0, word.length() - 2) + "l";
			} else if (last2 == 'e' && last3 == 'o') { // cançoes = canção
				word = word.substring(0, word.length() - 3) + "ao";
			} else if (last2 == 'n' && last3 == 'e') { // passagens = parssagem
				word = word.substring(0, word.length() - 2) + "m";
			} else { // no geral, tira
				word = word.substring(0, word.length() - 1);
			}
		}

		return word;
	}

	/**
	 * Remove flexção, gerúndio, particípio, diminutivo, advérbio, ou seja, se
	 * for verbo, retorna o infitnitvo, e palavra, retorna o radical.
	 * 
	 * Ou seja: deixa a palavra com a parte mais simples para busca. Age
	 * principalmente em verbos.
	 * 
	 * @param word
	 * @return
	 */
	public static String simplifyForSearch(String word) {
		String originalWord = word;

		if (word.length() < 3) {
			return word;
		}

		char last1 = word.charAt(word.length() - 1);
		char last2 = word.charAt(word.length() - 2);
		char last3 = word.charAt(word.length() - 3);
		char last4 = '|';
		char last5 = '|';

		if (word.length() > 3) {
			last4 = word.charAt(word.length() - 4);
		}

		if (word.length() > 4) {
			last5 = word.charAt(word.length() - 5);
		}

		// exceções, ou seja, palavras com s que são com s mesmo.
		if ("homem;senado;comente;dado;acao;cancao;tara;cara;vida;tira;comer;comido;comida;"
				.indexOf(word + ";") != -1) {
			return word;
		}

		if (word.endsWith("mente")) { // atualmente = atual
			word = word.substring(0, word.length() - 5);
		} else if (last1 == 'r') { // verbos
			if (last2 == 'a' || last2 == 'e') { // vender
				word = word.substring(0, word.length() - 2);
			}
		} else if ((last1 == 'o' || last1 == 'a') && last2 == 's'
				&& last3 == 'o') { // culposo/culposa
			word = word.substring(0, word.length() - 3)
					+ (last4 == 's' ? "" : "o");
		} else if (last1 == 'm' && last2 == 'e' && last3 == 'r'
				&& (last4 == 'e' || last4 == 'a')) { // cantarem,
														// venderem
			word = word.substring(0, word.length() - 4);
		} else if (word.length() > 3 && last1 == 'e' && last2 == 'c'
				&& last3 == 'e') { // acontece,
									// aparece,
									// tece
			word = word.substring(0, word.length() - 1);
		} else if (word.length() > 3
				&& ((last2 == 'e' && last1 == 'i') || (last2 == 'o' && last1 == 'u'))) {
			// pegou, tomou... peguei, tomei, deixei.. - > 3 para não pegar
			// coisas como rei
			word = word.substring(0, word.length() - 2);
		} else if (word.length() > 5 && last5 == 'c' && last4 == 'i'
				&& last3 == 'o' && last2 == 'n') { // tencione,
													// selecione,
			// seleciona
			word = word.substring(0, word.length() - 1);
		} else if (last1 == 'a' && last2 == 'r' && last3 == 'a' && last4 != '|') { // cantara,
			word = word.substring(0, word.length() - 3);
		} else if (last1 == 'o' && last2 == 'a' && last3 == 'r'
				&& word.length() > 4) { // cantarao
			word = word.substring(0, word.length() - 3);
		} else if (last1 == 'o' && last2 == 'd' && last3 == 'n' && last4 == 'a') { // cantarao
			word = word.substring(0, word.length() - 4);
		} else if (last1 == 'o' && last2 == 'a' && last3 == 'c' && last4 != '|') { // autorizacção
			word = word.substring(0, word.length() - 4);
		} else if (last1 == 'm' && (last2 == 'e' || last2 == 'a')
				&& word.length() > 4) { // cantem,
										// cantam,
										// dizem
										// (OBS:
										// TALVEZ
										// DÊ
			// PAU, EM BELEM, REALIVAR DEPOIS.
			word = word.substring(0, word.length() - 2);
		} else {
			if (word.length() > 3 && last2 == 'd'
					&& (last1 == 'o' || last1 == 'a')
					&& (last3 == 'a' || last3 == 'i')) { // participio:
				// catado,
				// partido, cantada
				word = word.substring(0, word.length() - 3);
			}
		}

		if (last1 == 'l') { // mal => mau
			word = word.substring(0, word.length() - 1) + 'u';
		}

		// agora trata repetida, por exemplo, carro vira caro.
		StringBuffer newWordSb = new StringBuffer(word);
		char lastChar = '-';
		for (int k = 0; k < newWordSb.length(); k++) {
			char currChar = newWordSb.charAt(k);
			if (currChar == lastChar) {
				newWordSb = newWordSb.delete(k, k + 1);
			} else if (lastChar == 'c' && currChar == 'h') {
				newWordSb.setCharAt(k - 1, 'x');
				newWordSb = newWordSb.delete(k, k + 1);
			}

			lastChar = currChar;
		}
		word = newWordSb.toString();

		return word;
	}

	public static String mimeTypeToExt(String mime) {
		if (mime == null) {
			return "";
		}

		if (mime.equals("application/envoy"))
			return "evy";
		if (mime.equals("application/fractals"))
			return "fif";
		if (mime.equals("application/futuresplash"))
			return "spl";
		if (mime.equals("application/hta"))
			return "hta";
		if (mime.equals("application/internet-property-stream"))
			return "acx";
		if (mime.equals("application/mac-binhex40"))
			return "hqx";
		if (mime.equals("application/msword"))
			return "doc";
		if (mime.equals("application/octet-stream"))
			return "exe";
		if (mime.equals("application/oda"))
			return "oda";
		if (mime.equals("application/olescript"))
			return "axs";
		if (mime.equals("application/pdf"))
			return "pdf";
		if (mime.equals("application/pics-rules"))
			return "prf";
		if (mime.equals("application/pkcs10"))
			return "p10";
		if (mime.equals("application/pkix-crl"))
			return "crl";
		if (mime.equals("application/postscript"))
			return "ai";
		if (mime.equals("application/postscript"))
			return "eps";
		if (mime.equals("application/postscript"))
			return "ps";
		if (mime.equals("application/rtf"))
			return "rtf";
		if (mime.equals("application/set-payment-initiation"))
			return "setpay";
		if (mime.equals("application/set-registration-initiation"))
			return "setreg";
		if (mime.equals("application/vnd.ms-excel"))
			return "xls";
		if (mime.equals("application/vnd.ms-outlook"))
			return "msg";
		if (mime.equals("application/vnd.ms-pkicertstore"))
			return "sst";
		if (mime.equals("application/vnd.ms-pkiseccat"))
			return "cat";
		if (mime.equals("application/vnd.ms-pkistl"))
			return "stl";
		if (mime.equals("application/vnd.ms-powerpoint"))
			return "ppt";
		if (mime.equals("application/vnd.ms-project"))
			return "mpp";
		if (mime.equals("application/vnd.ms-works"))
			return "wks";
		if (mime.equals("application/winhlp"))
			return "hlp";
		if (mime.equals("application/x-bcpio"))
			return "bcpio";
		if (mime.equals("application/x-cdf"))
			return "cdf";
		if (mime.equals("application/x-compress"))
			return "z";
		if (mime.equals("application/x-compressed"))
			return "tgz";
		if (mime.equals("application/x-cpio"))
			return "cpio";
		if (mime.equals("application/x-csh"))
			return "csh";
		if (mime.equals("application/x-director"))
			return "dir";
		if (mime.equals("application/x-dvi"))
			return "dvi";
		if (mime.equals("application/x-gtar"))
			return "gtar";
		if (mime.equals("application/x-gzip"))
			return "gz";
		if (mime.equals("application/x-hdf"))
			return "hdf";
		if (mime.equals("application/x-internet-signup"))
			return "ins";
		if (mime.equals("application/x-iphone"))
			return "iii";
		if (mime.equals("application/x-javascript"))
			return "js";
		if (mime.equals("application/x-latex"))
			return "latex";
		if (mime.equals("application/x-msaccess"))
			return "mdb";
		if (mime.equals("application/x-mscardfile"))
			return "crd";
		if (mime.equals("application/x-msclip"))
			return "clp";
		if (mime.equals("application/x-msdownload"))
			return "dll";
		if (mime.equals("application/x-msmediaview"))
			return "mvb";
		if (mime.equals("application/x-msmetafile"))
			return "wmf";
		if (mime.equals("application/x-msmoney"))
			return "mny";
		if (mime.equals("application/x-mspublisher"))
			return "pub";
		if (mime.equals("application/x-msschedule"))
			return "scd";
		if (mime.equals("application/x-msterminal"))
			return "trm";
		if (mime.equals("application/x-mswrite"))
			return "wri";
		if (mime.equals("application/x-netcdf"))
			return "nc";
		if (mime.equals("application/x-perfmon"))
			return "pmc";
		if (mime.equals("application/x-pkcs12"))
			return "pfx";
		if (mime.equals("application/x-pkcs7-certificates"))
			return "spc";
		if (mime.equals("application/x-pkcs7-certreqresp"))
			return "p7r";
		if (mime.equals("application/x-pkcs7-mime"))
			return "p7m";
		if (mime.equals("application/x-pkcs7-signature"))
			return "p7s";
		if (mime.equals("application/x-sh"))
			return "sh";
		if (mime.equals("application/x-shar"))
			return "shar";
		if (mime.equals("application/x-shockwave-flash"))
			return "swf";
		if (mime.equals("application/x-stuffit"))
			return "sit";
		if (mime.equals("application/x-sv4cpio"))
			return "sv4cpio";
		if (mime.equals("application/x-sv4crc"))
			return "sv4crc";
		if (mime.equals("application/x-tar"))
			return "tar";
		if (mime.equals("application/x-tcl"))
			return "tcl";
		if (mime.equals("application/x-tex"))
			return "tex";
		if (mime.equals("application/x-texinfo"))
			return "texinfo";
		if (mime.equals("application/x-troff"))
			return "roff";
		if (mime.equals("application/x-troff-man"))
			return "man";
		if (mime.equals("application/x-troff-me"))
			return "me";
		if (mime.equals("application/x-troff-ms"))
			return "ms";
		if (mime.equals("application/x-ustar"))
			return "ustar";
		if (mime.equals("application/x-wais-source"))
			return "src";
		if (mime.equals("application/x-x509-ca-cert"))
			return "crt";
		if (mime.equals("application/ynd.ms-pkipko"))
			return "pko";
		if (mime.equals("application/zip"))
			return "zip";
		if (mime.equals("audio/basic"))
			return "au";
		if (mime.equals("audio/basic"))
			return "snd";
		if (mime.equals("audio/mid"))
			return "mid";
		if (mime.equals("audio/mid"))
			return "rmi";
		if (mime.equals("audio/mpeg"))
			return "mp3";
		if (mime.equals("audio/x-aiff"))
			return "aiff";
		if (mime.equals("audio/x-mpegurl"))
			return "m3u";
		if (mime.equals("audio/x-pn-realaudio"))
			return "ram";
		if (mime.equals("audio/x-wav"))
			return "wav";
		if (mime.equals("image/bmp"))
			return "bmp";
		if (mime.equals("image/cis-cod"))
			return "cod";
		if (mime.equals("image/gif"))
			return "gif";
		if (mime.equals("image/ief"))
			return "ief";
		if (mime.equals("image/jpeg"))
			return "jpg";
		if (mime.equals("image/pipeg"))
			return "jfif";
		if (mime.equals("image/svg+xml"))
			return "svg";
		if (mime.equals("image/tiff"))
			return "tiff";
		if (mime.equals("image/x-cmu-raster"))
			return "ras";
		if (mime.equals("image/x-cmx"))
			return "cmx";
		if (mime.equals("image/x-icon"))
			return "ico";
		if (mime.equals("image/x-portable-anymap"))
			return "pnm";
		if (mime.equals("image/x-portable-bitmap"))
			return "pbm";
		if (mime.equals("image/x-portable-graymap"))
			return "pgm";
		if (mime.equals("image/x-portable-pixmap"))
			return "ppm";
		if (mime.equals("image/x-rgb"))
			return "rgb";
		if (mime.equals("image/x-xbitmap"))
			return "xbm";
		if (mime.equals("image/x-xpixmap"))
			return "xpm";
		if (mime.equals("image/x-xwindowdump"))
			return "xwd";
		if (mime.equals("message/rfc822"))
			return "mhtml";
		if (mime.equals("text/css"))
			return "css";
		if (mime.equals("text/h323"))
			return "323";
		if (mime.equals("text/html"))
			return "html";
		if (mime.equals("text/iuls"))
			return "uls";
		if (mime.equals("text/plain"))
			return "txt";
		if (mime.equals("text/richtext"))
			return "rtx";
		if (mime.equals("text/scriptlet"))
			return "sct";
		if (mime.equals("text/tab-separated-values"))
			return "tsv";
		if (mime.equals("text/webviewhtml"))
			return "htt";
		if (mime.equals("text/x-component"))
			return "htc";
		if (mime.equals("text/x-setext"))
			return "etx";
		if (mime.equals("text/x-vcard"))
			return "vcf";
		if (mime.equals("video/mpeg"))
			return "mpeg";
		if (mime.equals("video/quicktime"))
			return "mov";
		if (mime.equals("video/x-ms-asf"))
			return "asf";
		if (mime.equals("video/x-msvideo"))
			return "avi";
		if (mime.equals("video/x-sgi-movie"))
			return "movie";
		if (mime.equals("x-world/x-vrml"))
			return "vrml";

		return "";
	}

	public static String getEmailDomain(String email) {
		if (email == null) {
			return email;
		}

		int pos = email.indexOf('@');

		if (pos == -1 || email.length() == pos + 1) {
			return email;
		}

		return email.substring(pos + 1);
	}

	public static String getEmailAccount(String email) {
		if (email == null) {
			return "";
		}

		int pos = email.indexOf('@');

		if (pos == -1 || pos == 0) {
			return email;
		}

		return email.substring(0, pos);
	}

	public static int countSubstrings(String str, String substr) {
		int count = 0;

		int lastIndex = 0;

		while (true) {
			int index = str.indexOf(substr, lastIndex);

			if (index == -1) {
				break;
			}

			count++;
			lastIndex = index + substr.length();
		}

		return count;
	}

	public static String cleanUrl(String url) {
		if (url.indexOf("http://") == 0) {
			url = url.substring("http://".length(), url.length());
		}

		if (url.indexOf("www.") == 0) {
			url = url.substring("www.".length(), url.length());
		}

		while (url.endsWith("/")) {
			url = url.substring(0, url.length() - 1);
		}

		return url;
	}

	public static boolean isSubdomain(String url) {
		String domain = Utils
				.extractDomainName("http://" + Utils.cleanUrl(url));

		int dots = Utils.countSubstrings(domain, ".");
		boolean sub = false;

		/*
		 * if (dots == 1) { sub = false; } else
		 */
		if (dots > 2) {
			sub = true;
		} else if (dots == 2) {
			String ending = domain.substring(domain.indexOf('.') + 1);
			sub = ending.length() > 6;
		}

		return sub;
	}

	public static boolean isBotUrl(String url) {
		String domain = Utils.extractDomainName(url);

		return domain.indexOf("google") != -1
				|| domain.indexOf("yahoo.com") != -1
				|| domain.indexOf("live.com") != -1
				|| domain.indexOf("search.msn.com") != -1
				|| domain.indexOf("ajudanabusca") != -1
				|| domain.indexOf("busca.uol.com.br") != -1
				|| domain.indexOf("altavista") != -1
				|| domain.indexOf("biznetic") != -1
				|| domain.indexOf("search1.incredimail.com") != -1
				|| domain.indexOf("buscador.terra.com.br") != -1
				|| domain.indexOf("search.sweetim.com") != -1
				|| domain.indexOf("search.hiyo.com") != -1
				|| domain.indexOf("search.conduit.com") != -1
				|| domain.indexOf("search.mywebsearch.com") != -1
				|| domain.indexOf("pesquisa.clix.pt") != -1
				|| domain.indexOf("search.prodigy.msn.com") != -1;
	}

	public static String extractSearchFromUrl(String searchEngineUrl) {
		String paramName = null;

		if (searchEngineUrl.indexOf("&q=") != -1) {
			paramName = "&q=";
		}
		if (searchEngineUrl.indexOf("?q=") != -1) {
			paramName = "?q=";
		} else if (searchEngineUrl.indexOf("&p=") != -1) {
			paramName = "&p=";
		} else if (searchEngineUrl.indexOf("?p=") != -1) {
			paramName = "?p=";
		} else if (searchEngineUrl.indexOf("&query=") != -1) {
			paramName = "&query=";
		} else if (searchEngineUrl.indexOf("?query=") != -1) {
			paramName = "?query=";
		}

		if (paramName == null) {
			return null;
		}

		int fromIndex = searchEngineUrl.indexOf(paramName);
		int endIndex = searchEngineUrl.indexOf("&",
				fromIndex + paramName.length());
		if (endIndex == -1) {
			endIndex = searchEngineUrl.length();
		}

		String charset = "utf-8";

		if (searchEngineUrl.indexOf("busca.uol.com.br") != -1) {
			charset = "ISO-8859-1";
		}

		URLDecoder decoder = new URLDecoder();

		String search = null;

		try {
			search = decoder.decode(searchEngineUrl.substring(fromIndex
					+ paramName.length(), endIndex), charset);
			search = Utils.replace(search, "  ", " ", true).toLowerCase()
					.trim();
		} catch (Exception error) {
			throw new RuntimeException(error.getMessage());
		}

		return search;
	}

	/**
	 * Dá o "grep" em um texto.
	 * 
	 * @param text
	 * @param matchText
	 *            O que a linha tem q conter para ser coletada.
	 * @return
	 */
	public static String[] grep(String text, String matchText) {

		StringTokenizer st = new StringTokenizer(text, "\n");
		ArrayList<String> lines = new ArrayList<String>();

		while (st.hasMoreElements()) {
			String line = (String) st.nextToken();

			if (line.indexOf(matchText) != -1) {
				lines.add(line);
			}
		}

		return (String[]) lines.toArray(new String[lines.size()]);
	}

	public static String getSearchWordID(String name) {
		name = Utils.truncateText(Utils.formatIfNull(name), 128);

		name = Utils.format(name, new String[] { "e", "", "", "", "", "", "",
				"", "", " ", "", "a" }, new String[] { "&", ".", "(", ")", "!",
				",", ":", "'", "\"", "-", "#", "@" });
		name = Utils.format(name, new String[] { " " }, new String[] { " " }); // caracter
		// em
		// brancco
		// especial

		name = Utils.purifyStr(name);
		name = Utils.cleanUpStr(name, " ");

		name = name.trim().replace(' ', '-');

		name = Utils.urlEncode(name);

		return name;
	}

	/**
	 * Limpa URL quanto a maíusculas, respeitando parâmetros
	 * 
	 * @param url
	 * @return
	 */
	public static String toLowerCaseUrl(String url) {
		if (url == null) {
			return null;
		}

		url = url.trim();

		// só para garantir que vai ser depois do protoclo
		int startSlash = 0;

		if (url.startsWith("http") || url.startsWith("ftp")) {
			startSlash = "https://".length();
		}

		int slashIndex = url.indexOf('/', startSlash);

		if (slashIndex == -1) {
			slashIndex = url.length();
		}

		url = url.substring(0, slashIndex).toLowerCase()
				+ url.substring(slashIndex);

		return url;
	}

	public static String getCharByNumber(int num) {
		// 0 a 9
		if (num < 11) {
			if (num <= 0) {
				num = 1;
			}

			return String.valueOf(num - 1);
		} else {
			return String.valueOf(Utils.LETTERS[(num - 11)
					% Utils.LETTERS.length]);
		}
	}

	/**
	 * Obtém o domingo de Páscoa.
	 * 
	 * 1. Sexta da Paixão de Cristo é -2 dias 2. Carnaval é -47
	 * 
	 * @param year
	 * @return
	 */
	public static Date getEasterSunday(int year) {
		int g = year % 19;
		int c = year / 100;
		int h = (c - c / 4 - (8 * c + 13) / 25 + 19 * g + 15) % 30;
		int i = h - h / 28 * (1 - h / 28 * 29 / (h + 1) * (21 - g) / 11);
		int j = (year + year / 4 + i + 2 - c + c / 4) % 7;
		int l = i - j;
		int month = 3 + (l + 40) / 44;
		int day = l + 28 - 31 * (month / 4);
		return Utils.getGregorianCalendarInstance(year, month - 1, day)
				.getTime();
	}

	/**
	 * Converte um texto wm tags, no padrão: "," separa tags, espeço separa
	 * palavras de uma mesma tag.
	 * 
	 * @param textAsTags
	 * @throws Exception
	 */
	public static String[] toTags(String textAsTags, int maxTags,
			int maxWordsOnTag) throws Exception {
		String[] tags = Utils.getLines(Utils.formatIfEmpty(textAsTags, "")
				.toLowerCase(), ',');
		for (int i = 0; i < tags.length; i++) {
			tags[i] = Utils.cleanUpStr(tags[i], " ");
		}
		tags = Utils.cleanUp(tags, true, true, true, true);

		ArrayList<String> finalTags = new ArrayList<String>();

		for (int i = 0; i < tags.length; i++) {
			if (finalTags.size() >= maxTags) {
				break;
			}

			if (Utils.countSubstrings(tags[i], " ") < maxWordsOnTag) {
				finalTags.add(tags[i]);
			}
		}

		return (String[]) finalTags.toArray(new String[finalTags.size()]);
	}

	public static String simplifyFileNameForWeb(String filename) {
		filename = Utils.replace(Utils.toFileName(filename), "%", "_", true);
		filename = Utils.replace(Utils.toFileName(filename), "+", "_", true);
		filename = Utils.replace(Utils.toFileName(filename), "?", "_", true);
		// filename = Utils.replace(Utils.toFileName(filename), "=", "_", true);
		filename = Utils.replace(Utils.toFileName(filename), "!", "_", true);
		return Utils.removeTones(Utils.replace(Utils.toFileName(filename), " ",
				"_", true));
	}

	/**
	 * Junta caracteres iguais consecutivos.
	 * 
	 * @param text
	 * @param maxConsecutivePoncutiation
	 *            3 é o recomendado.
	 * @return
	 */
	public static String removePunctuationRepeatedChars(String text) {
		return removePunctuationRepeatedChars(text, 3);
	}

	public static String removePunctuationRepeatedChars(String text,
			int maxConsecutivePoncutiation) {
		char[] chs = text.toCharArray();
		StringBuffer newText = new StringBuffer();
		char lastChar = '¨';
		int count = 0;

		boolean isPonctuation = false;

		for (int i = 0; i < chs.length; i++) {
			if (chs[i] != lastChar) {
				isPonctuation = chs[i] == '.' || chs[i] == '!' || chs[i] == '?'
						|| chs[i] == '/' || chs[i] == '\\';
			}

			if (!isPonctuation) {
				newText.append(chs[i]);

				lastChar = chs[i];
				count = 1;
			} else {
				if (chs[i] != lastChar) {
					newText.append(chs[i]);

					lastChar = chs[i];
					count = 1;
				} else if (isPonctuation) {
					count++;

					if (count <= maxConsecutivePoncutiation) {
						newText.append(chs[i]);
					}
				}
			}
		}

		return newText.toString();
	}

	/**
	 * Substitui todas ocorrênvias de emails por um texto dado.
	 * 
	 * @param text
	 * @param subsStr
	 * @param exceptionsEmails
	 * @return
	 */
	public static String changeAllEmailOcurrencies(String text, String subsStr,
			String[] exceptionsEmails) {
		int currSearchPos = 0;

		while (true) {
			int atPos = text.indexOf("@", currSearchPos);

			if (atPos == -1) {
				break;
			}

			int beginEmailIndex = 0;
			for (beginEmailIndex = atPos - 1; beginEmailIndex > 0; beginEmailIndex--) {
				char c = text.charAt(beginEmailIndex);

				if (c != '.' && c != '_' && c != '-' && c != '$'
						&& !Utils.isLetter(c) && !Utils.isNumber(c)) {
					beginEmailIndex++;
					break;
				}
			}

			int endEmailIndex = 0;
			for (endEmailIndex = atPos + 1; endEmailIndex < text.length(); endEmailIndex++) {
				char c = text.charAt(endEmailIndex);

				if (c != '.' && c != '_' && c != '-' && c != '$'
						&& !Utils.isLetter(c) && !Utils.isNumber(c)) {
					break;
				}
			}

			if (endEmailIndex == text.length()) {
				char lastChar = text.charAt(endEmailIndex - 1);
				if (Utils.isSpaceChar(lastChar)
						|| Utils.isPonctuationChar(lastChar)) {
					endEmailIndex--;
				}
			}

			boolean removeEmail = true;

			if (exceptionsEmails != null && beginEmailIndex >= 0
					&& endEmailIndex <= text.length()) {
				String email = text.substring(beginEmailIndex, endEmailIndex);

				for (int k = 0; k < exceptionsEmails.length; k++) {
					if (exceptionsEmails[k].equals(email)) {
						removeEmail = false;
					}
				}
			}

			if (removeEmail && beginEmailIndex >= 0
					&& endEmailIndex <= text.length()) {
				text = text.substring(0, beginEmailIndex) + subsStr
						+ text.substring(endEmailIndex, text.length());
				currSearchPos = beginEmailIndex + subsStr.length() + 1;
			} else {
				currSearchPos = endEmailIndex;
			}

		}

		return text;
	}

	public static String explodeLongWords(String text, int limit,
			String splitter) {
		if (text == null) {
			return "";
		}

		char[] words = text.toCharArray();

		StringBuffer strBuffer = new StringBuffer();

		int count = 0;

		for (int i = 0; i < words.length; i++) {
			boolean isSpace = Utils.isSpaceChar(words[i]);

			if (isSpace) {
				count = 0;
				strBuffer.append(words[i]);
			} else {
				if (count >= limit) {
					strBuffer.append(splitter);
					count = 0;
				}

				strBuffer.append(words[i]);
				count++;
			}
		}

		return strBuffer.toString();
	}

	public static String captilizeFirsts(String text) {
		char[] chs = text.toCharArray();
		boolean another = true;

		for (int i = 0; i < chs.length; i++) {
			boolean isSpace = Utils.isSpaceChar(chs[i]);

			if (another && !isSpace) {
				chs[i] = Character.toUpperCase(chs[i]);
				another = false;
			} else if (isSpace) {
				another = true;
			}
		}

		return new String(chs);
	}

	/**
	 * Faz palavras firarem contornadas com prior e pos
	 * 
	 * @param sentence
	 * @param searchWords
	 * @param priorStr
	 * @param posStr
	 * @return
	 */
	public static StringBuffer replaceSentence(String sentence,
			String[] searchWords, String priorStr, String posStr) {

		String sentenceLower = sentence.toLowerCase(); // Transforma em caixa
		// baixa para teste
		sentenceLower = Utils.removeTones(sentenceLower); // Ignora acentos

		String[] wordsLower = new String[searchWords.length];

		StringBuffer sentenceBuffer = new StringBuffer(sentence);

		int positionWord = 0;
		int positionWordSpan = 0;
		int sizeSpan = 0;
		int difWordsSpan = 0;

		for (int i = 0; i < searchWords.length; i++) {

			wordsLower[i] = searchWords[i].toLowerCase();
			wordsLower[i] = Utils.removeTones(wordsLower[i]);

			while (true) {

				difWordsSpan = positionWord;

				positionWord = sentenceLower.indexOf(wordsLower[i],
						positionWord);

				difWordsSpan = positionWord - difWordsSpan;

				positionWordSpan = positionWordSpan + sizeSpan + difWordsSpan;

				if (positionWord == -1) {
					break;
				}

				// change = palavra com a marca
				String change = priorStr
						+ sentenceBuffer.substring(positionWordSpan,
								positionWordSpan + wordsLower[i].length())
						+ posStr;

				sentenceBuffer = sentenceBuffer.replace(positionWordSpan,
						positionWordSpan + wordsLower[i].length(), change);

				// sizeSpan = 34 + wordsLower[i].length() + 16;
				sizeSpan = priorStr.length() + wordsLower[i].length()
						+ posStr.length();
				positionWord = positionWord + wordsLower[i].length();
			}

			sentenceLower = sentenceBuffer.toString().toLowerCase();
			sentenceLower = Utils.removeTones(sentenceLower);
			sentenceBuffer = new StringBuffer(sentenceBuffer);

			positionWord = 0;
			positionWordSpan = 0;
			sizeSpan = 0;
			difWordsSpan = 0;

		}

		return sentenceBuffer;
	}

	/**
	 * Identifica e cria as ancoreas em URLs. Máximo de 20.
	 * 
	 * 
	 * OBS: FALTA O SEGUINTE: SE ACHAR UMA URL, MAS ELA JÁ TIVER DENTRO DE UM
	 * <A, ENTÃO NÃO LINKIFY. PRO CHAT, EU FIZ UMA GAMBI (VEJA EM MESSAGE)
	 * 
	 * FAZENDO ignoreTagUrls
	 * 
	 * @param text
	 *            String
	 * @param ignoreTagUrls
	 *            Ignora urls dentro de tags.
	 * @return Texto com todos os cantos onde havia URL com ancora agora.
	 */
	public static String linkfyTextUrls(String text, String priorLink,
			String aParams, boolean ignoreTagUrls) {
		priorLink = Utils.formatIfNull(priorLink);
		aParams = Utils.formatIfNull(aParams);

		StringBuffer sb = new StringBuffer(text);

		String leftDeniedChars = ",;:?!\\|\"'<>()\n\t\r ";
		String rightDeniedChars = ",;!\\|\"'<>()\n\t\r ";

		int startFrom = 0;

		int maxUrlCount = 20;
		int urlCount = 0;

		// centro do e-mail: @.
		int urlIndex = sb.indexOf("http", startFrom);
		if (urlIndex == -1) {
			urlIndex = sb.indexOf("www.", startFrom);
		}

		String[] sufixes = { ".com", ".org", ".tk", ".net", ".pt", ".br" };
		int sufixIndex = Integer.MAX_VALUE;
		for (int i = 0; i < sufixes.length && sufixIndex == Integer.MAX_VALUE; i++) {
			int sufixIndexTemp = sb.indexOf(sufixes[i], startFrom);

			if (sufixIndexTemp != -1 && sufixIndexTemp < sufixIndex) {
				sufixIndex = sufixIndexTemp;
			}

			// Super-gambi para o caso de quando ha .como?, por exemplo.
			// se for .com e achou e for como, e não .com mermo, cai fora.
			if (sufixIndex != Integer.MAX_VALUE
					&& Utils.indexOf(sufixes[i], sufixes) != -1) {
				int oPossiblePos = ".com".length() + sufixIndex;

				if (sb.length() > oPossiblePos
						&& sb.charAt(oPossiblePos) == 'o') {
					startFrom = sufixIndex + 1;
					i--;

					sufixIndex = Integer.MAX_VALUE;
				}
			}
		}

		if (sufixIndex == Integer.MAX_VALUE) {
			sufixIndex = -1;
		}

		if (urlIndex > sufixIndex || urlIndex == -1) {
			urlIndex = sufixIndex;
		}

		// se encontrou.
		while (sufixIndex != -1) {
			// no mínimo, vai para o próximo caracter.
			startFrom = sufixIndex + 1;

			urlCount++;
			if (urlCount >= maxUrlCount) {
				break;
			}

			int leftIndex = -1;
			int rightIndex = -1;
			boolean itsEmail = false; // se for e-mail, entao já para e vai ao
			// próximo sem pestanejar.

			// volta atrás do começo mesmo do início da URL.
			for (leftIndex = urlIndex - 1; leftIndex >= 0; leftIndex--) {
				char currChar = sb.charAt(leftIndex);

				if (leftDeniedChars.indexOf(currChar) != -1) {
					leftIndex++;
					break;
				}

				if (currChar == '@') {
					itsEmail = true;
					break;
				}
			}

			// se for igual a -1, é pq não há nada antes da URL.
			if (leftIndex == -1) {
				leftIndex = 0;
			}

			for (rightIndex = sufixIndex + 1; rightIndex < sb.length(); rightIndex++) {
				char currChar = sb.charAt(rightIndex);

				if (rightDeniedChars.indexOf(currChar) != -1) {
					break;
				}

				if (currChar == '@') {
					itsEmail = true;
				}
			}

			if (itsEmail) {
				startFrom = rightIndex + 1;
			} else if (leftIndex != -1 && rightIndex != -1
					&& leftIndex < rightIndex) {
				String url = sb.substring(leftIndex, rightIndex);

				// últimos ajustes no e-mail (ponto no final principalmente.
				if (url != null && url.length() != 0) {
					StringBuffer urlSb = new StringBuffer(url);

					boolean changed = false;

					while (urlSb.charAt(0) == '.') {
						changed = true;
						leftIndex++;
						urlSb.delete(0, 1);
					}

					while (urlSb.charAt(urlSb.length() - 1) == '.') {
						changed = true;
						rightIndex--;
						urlSb.delete(urlSb.length() - 1, urlSb.length());
					}

					if (changed) {
						url = urlSb.toString();
					}
				}

				int lastSize = sb.length();

				String newUrl = null;

				if (url.indexOf("http") == -1) {
					newUrl = "http://" + url;
				} else {
					newUrl = url;
				}

				String aUrl = newUrl;

				if (!priorLink.equals("")) {
					aUrl = Utils.urlEncode(aUrl);
				}

				// vê se o que achou está dentro de tag. Se tiver e for true,
				// ignora.
				boolean jump = false;
				if (ignoreTagUrls) {
					boolean urlInsideTagForward = false;
					boolean urlInsideTagBackward = false;

					// vê pra frente se tá dentro de tag
					// 200 é um número aleatório para andar "até"...
					for (int j = rightIndex, availCount = 0; j < sb.length()
							&& availCount < 200; j++, availCount++) {
						if (sb.charAt(j) == '>') {
							urlInsideTagForward = true;
							break;
						}
					}

					// vê pra tras se tá dentro de tag
					// 200 é um número aleatório para andar "até"...
					for (int j = leftIndex, availCount = 0; j >= 0
							&& availCount < 200 && urlInsideTagForward; j--, availCount++) {
						if (sb.charAt(j) == '<') {
							urlInsideTagBackward = true;
							break;
						}
					}

					// se está dentro de tag, e é para considerar isso, ignora.
					if (urlInsideTagForward && urlInsideTagBackward) {
						jump = true;
					}
				}

				if (!jump) {
					sb = sb.replace(leftIndex, rightIndex, "<a href='"
							+ priorLink + aUrl + "' " + aParams + ">" + url
							+ "</a>");
				}

				startFrom = rightIndex + (sb.length() - lastSize);
			}

			// tudo de novo....
			urlIndex = sb.indexOf("http", startFrom);
			if (urlIndex == -1) {
				urlIndex = sb.indexOf("www.", startFrom);
			}

			sufixIndex = sb.indexOf(".com", startFrom);
			if (sufixIndex == -1) {
				sufixIndex = sb.indexOf(".org", startFrom);
			}

			if (urlIndex > sufixIndex || urlIndex == -1) {
				urlIndex = sufixIndex;
			}
		}

		return sb.toString();
	}

	public static GregorianCalendar getSysGregorianCalendarInstance(int year,
			int month, int day) {
		GregorianCalendar gc = new GregorianCalendar(year, month, day);
		gc.setTimeZone(timeZone);
		return gc;
	}

	public static GregorianCalendar getSysGregorianCalendarInstance(int year,
			int month, int day, int hour, int minute, int second) {
		GregorianCalendar gc = new GregorianCalendar(year, month, day, hour,
				minute, second);
		gc.setTimeZone(timeZone);
		return gc;
	}

	public static GregorianCalendar getSysGregorianCalendarInstance() {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTimeZone(timeZone);
		return gc;
	}

	/**
	 * Feito para o CVTA, funciona legal. Só bota link quando a url tem www ou
	 * http
	 * 
	 * 
	 * @param originalText
	 * @param priorLink
	 * @param aParams
	 * @param linkTextOnlyDomain
	 *            Deixa o link apenas com o domínio.
	 * @param fixPonctuationSpaces
	 *            Conserta a pontuação tb, inserindo espaços depois de ".", ",",
	 *            etc.
	 * @return
	 */
	public static String linkfyTextUrls(String originalText, String priorLink,
			String aParams, boolean linkTextOnlyDomain,
			boolean fixPonctuationSpaces) {
		StringBuffer text = new StringBuffer(originalText + " ");
		StringBuffer lowerText = new StringBuffer(
				(originalText + " ").toLowerCase());

		int linkIndex = -1;

		ArrayList<String> searchFor = new ArrayList<String>();
		ArrayList<String> replaceBy = new ArrayList<String>();

		int id = 0;

		while (true) {
			int currLinkIndexWWW = lowerText.indexOf("www.", linkIndex);
			int currLinkIndexHTTP = lowerText.indexOf("http://", linkIndex);
			int currLinkIndex = -1;

			if (currLinkIndexWWW != -1 && currLinkIndexHTTP == -1) {
				currLinkIndex = currLinkIndexWWW;
			} else if (currLinkIndexWWW == -1 && currLinkIndexHTTP != -1) {
				currLinkIndex = currLinkIndexHTTP;
			} else if (currLinkIndexWWW < currLinkIndexHTTP) {
				currLinkIndex = currLinkIndexWWW;
			} else if (currLinkIndexWWW > currLinkIndexHTTP) {
				currLinkIndex = currLinkIndexHTTP;

			}

			if (currLinkIndex == -1) {
				break;
			}

			linkIndex = currLinkIndex;

			// copia até o final do link
			StringBuffer currLink = new StringBuffer();

			int linkEnd = linkIndex;

			for (; linkEnd < text.length(); linkEnd++) {
				if (Utils.isSpaceChar(text.charAt(linkEnd))) {
					break;
				}

				currLink.append(text.charAt(linkEnd));
			}

			// ===================================
			// tira coisas de pontuação final.
			int howMuchPonctuations = 0;

			for (int i = currLink.length() - 1; i >= 0; i--) {
				if (Utils.isPonctuationChar(currLink.charAt(i))) {
					howMuchPonctuations++;
				} else {
					break;
				}
			}

			if (howMuchPonctuations > 0) {
				currLink.delete(currLink.length() - howMuchPonctuations,
						currLink.length());
				linkEnd -= howMuchPonctuations;
			}
			// ===================================

			String aUrl = currLink.toString();

			if (aUrl.indexOf("http") == -1) {
				if (aUrl.indexOf("www.") == -1) {
					aUrl = "http://www." + aUrl;
				} else {
					aUrl = "http://" + aUrl;
				}
			}

			if (Utils.isValidUrl(aUrl)) {
				if (!priorLink.equals("")) {
					aUrl = Utils.urlEncode(currLink.toString());
				}

				aUrl = Utils.removeTones(aUrl.toLowerCase());

				String linkText = currLink.toString();

				if (linkTextOnlyDomain) {
					linkText = Utils.cleanUrl(currLink.toString());

					try {
						linkText = Utils
								.extractDomainName("http://" + linkText);
					} catch (Exception e) {
					}
				}

				String subsText = "<a href='" + priorLink + aUrl + "' "
						+ aParams + ">" + linkText + "</a>";

				String mark = "[#link_cvta_" + id++ + "]";

				replaceBy.add(subsText);
				searchFor.add(mark);

				text.replace(linkIndex, linkEnd, mark);
				lowerText.replace(linkIndex, linkEnd, mark);

				linkIndex = linkIndex + mark.length();
			} else {
				linkIndex = linkEnd;
			}
		}

		// agora, trata pontuação
		if (fixPonctuationSpaces) {
			String ponctuations = ",.;:?!";
			for (int i = 0; i < text.length(); i++) {
				char ch = text.charAt(i);

				int indexOf = ponctuations.indexOf(ch);

				if (indexOf != -1 && i < text.length() - 4 && i > 2) {
					char ch1 = text.charAt(i + 1);
					char ch0 = text.charAt(i - 1);

					if (ch1 == ' ') {
						continue;
					}

					if (ch == '!' && ch1 == '!') {
						continue;
					}

					if (ch == '.' || ch == ',') {
						String part3 = text.substring(i, i + 4);
						String part2 = text.substring(i, i + 3);
						char first = text.charAt(i + 1);

						if (part3.equals(".com") || part3.equals(".jsp")
								|| part3.equals(".htm") || part3.equals(".cgi")
								|| part3.equals(".jpg") || part3.equals(".gif")
								|| part3.equals(".wav") || part3.equals(".avi")
								|| part3.equals(".pdf") || part3.equals(".net")
								|| part2.equals(".br")) {
							continue;
						}

						if ((ch == '.' || ch == ',') && Utils.isNumber(first)) {
							continue;
						}
					}

					text.insert(i + 1, ' ');

					// se tem espaço antes, tira.
					if (ch0 == ' ') {
						text.delete(i - 1, i);
						i--;
					}

					i++;
				}
			}
		}

		// agora, as substituições
		for (int j = 0; j < searchFor.size(); j++) {
			String search = searchFor.get(j);

			int index = text.indexOf(search);

			text.replace(index, index + search.length(), replaceBy.get(j));
		}

		return text.toString();
	}

	public static String linkfyTextEmails(String text, String priorLink,
			String aParams, boolean linkTextOnlyDomain) {
		int currSearchPos = 0;

		while (true) {
			int atPos = text.indexOf("@", currSearchPos);

			if (atPos == -1) {
				break;
			}

			int beginEmailIndex = 0;
			for (beginEmailIndex = atPos - 1; beginEmailIndex > 0; beginEmailIndex--) {
				char c = text.charAt(beginEmailIndex);

				if (c != '.' && c != '_' && c != '-' && c != '$'
						&& !Utils.isLetter(c) && !Utils.isNumber(c)) {
					beginEmailIndex++;
					break;
				}
			}

			int endEmailIndex = 0;
			for (endEmailIndex = atPos + 1; endEmailIndex < text.length(); endEmailIndex++) {
				char c = text.charAt(endEmailIndex);

				if (c != '.' && c != '_' && c != '-' && c != '$'
						&& !Utils.isLetter(c) && !Utils.isNumber(c)) {
					break;
				}
			}

			if (endEmailIndex == text.length()) {
				char lastChar = text.charAt(endEmailIndex - 1);
				if (Utils.isSpaceChar(lastChar)
						|| Utils.isPonctuationChar(lastChar)) {
					endEmailIndex--;
				}
			}

			boolean removeEmail = true;

			String email = text.substring(beginEmailIndex, endEmailIndex);

			if (removeEmail && beginEmailIndex >= 0
					&& endEmailIndex <= text.length()) {

				String subsText = "<a href='" + priorLink + email + "' "
						+ aParams + ">" + email + "</a>";

				text = text.substring(0, beginEmailIndex) + subsText
						+ text.substring(endEmailIndex, text.length());

				currSearchPos = beginEmailIndex + subsText.length() + 1;
			} else {
				currSearchPos = endEmailIndex;
			}

		}

		return text;
	}

	/**
	 * Corrige textos com palavras ou caracteres que formam palavras grandes,
	 * que geram problemas de formatação. Ex:
	 * 
	 * Nome __________________________...
	 * 
	 * @param text
	 * @param maxWord
	 * @return
	 */
	public static String fixLongWords(String text, int maxWord) {
		String[] lines = Utils.getLines(text, " ");
		StringBuffer finalText = new StringBuffer();

		for (int i = 0; i < lines.length; i++) {
			lines[i] = Utils.formatLongText(lines[i], "...", maxWord, 1);
			finalText.append(lines[i] + " ");
		}

		return finalText.toString();
	}

	/**
	 * Testa se a sql tem where e se tem um igual (ou seja, tem alguma
	 * restrição)
	 * 
	 * @param sql
	 * @return
	 */
	public static boolean isValidWhereSql(String sql) {
		try {
			sql = sql.toLowerCase();

			int whereIndex = sql.lastIndexOf("where");

			if (whereIndex != -1) {
				return sql.indexOf('=', whereIndex) != -1;
			} else {
				return false;
			}
		} catch (Exception e) {
			JSPUtils.log("[isValidWhereSql] Erro: " + Utils.getStackTrace(e));
		}

		return false;
	}

	/**
	 * Formato especial de datas, ex: 2010-07-15T14:53:01.012-03:00
	 * 
	 * @param text
	 * @return
	 */
	public static Date strLongTextToDate(String text) {
		String dataStr = text.substring(0, 10) + " "
				+ text.substring(11, 11 + 8);

		TimeZone usedTimeZone = TimeZone.getTimeZone("GMT-"
				+ text.substring(text.length() - 5));

		SimpleDateFormat sdf = new SimpleDateFormat(dateTimePattern, locale);
		sdf.setTimeZone(usedTimeZone);

		try {
			return sdf.parse(dataStr);
		} catch (ParseException error) {
			throw new RuntimeException("Erro ao fazer o parser da data. Date: "
					+ dataStr + " Erro: " + error.getMessage());
		}
	}

	/***************************************************************************
	 * Dado um texto, extrai o primeiro email que houver.
	 * 
	 * @param t
	 * @return
	 */
	public static String extractFirstEmail(String t) {
		String firstEmail = null;

		int index = t.indexOf('@');

		if (index != -1) {
			char[] chs = t.toCharArray();

			// pra trás
			int left = -1;
			for (left = index; left > 0; left--) {
				if (chs[left] != '.' && Utils.isPonctuationChar(chs[left])
						|| Utils.isSpaceChar(chs[left])) {
					break;
				}
			}

			// pra frente
			int right = -1;
			for (right = index; right < chs.length; right++) {
				if (chs[right] != '.' && Utils.isPonctuationChar(chs[right])
						|| Utils.isSpaceChar(chs[right])) {
					break;
				}
			}

			if (left != -1 && right != -1) {
				firstEmail = t.substring(left, right).trim();
			}
		}

		return firstEmail;
	}

	/**
	 * Formata o número na forma de 1000 = 1K, 3.300 = 3,3K...
	 * 
	 * 1000 = 1K 2500 = 2,5K 4601 = 4,6k
	 * 
	 * 
	 * @param num
	 * @return
	 */
	public static String formatKNumber(int num) {
		String result = "";

		if (num >= 1000) {
			if (num % 1000 == 0) {
				result = (num / 1000) + "K";
			} else {
				double r = Utils.div(num, 1000);

				result += r + "";

				int pointIndex = result.indexOf('.');
				result = result.substring(0, pointIndex)
						+ result.substring(pointIndex, pointIndex + 2) + "K";
			}
		} else {
			result = num + "";
		}

		return result.replace('.', ',');

	}

	/**
	 * HTML escape apenas nas tags altamente perigosas: script, meta...
	 * 
	 * @param text
	 * @return
	 */
	public static String selectedDangerousHtmlEscape(String text) {
		StringBuffer textSb = new StringBuffer(text);
		StringBuffer lowerTextSb = new StringBuffer(text.toLowerCase());

		String[] potencialDangerous = new String[] { "script", "meta", "link",
				"applet", "object", "/script", "meta", "link", "applet",
				"object" };

		for (int i = 0; i < potencialDangerous.length; i++) {
			String currTag = "<" + potencialDangerous[i];

			while (true) {
				int tagIndex = lowerTextSb.indexOf(currTag);

				if (tagIndex == -1) {
					break;
				}

				textSb.replace(tagIndex, tagIndex + currTag.length(),
						Utils.htmlEscape(currTag));
				lowerTextSb.replace(tagIndex, tagIndex + currTag.length(),
						Utils.htmlEscape(currTag));
			}
		}

		return textSb.toString();
	}

	/**
	 * Retorna a diferença entre datas.
	 * 
	 * Ex: "2012-03-24 05:05:30" a "2012-03-27 04:05:35"
	 * 
	 * @param from
	 * @param to
	 * @param onlyTime
	 *            Apenas tempo.
	 * @return
	 */

	public static String getStrDatesDiff(Date from, Date to, boolean onlyTime) {
		Date cleanDate = Utils.truncateToDate(from);

		long milis = (to.getTime() - from.getTime());

		long daysD = milis / (24 * 60 * 60 * 1000);
		if (!onlyTime) {
			milis -= (24 * 60 * 60 * 1000) * daysD;
		} else {
			daysD = 0;
		}
		long hoursD = milis / (60 * 60 * 1000);
		milis -= (60 * 60 * 1000) * hoursD;
		long minutesD = milis / (60 * 1000);
		milis -= (60 * 1000) * minutesD;
		long secondsD = milis / (1000);

		String diffStr = Utils.stuffStr(hoursD + "", "0", 2, false) + ":"
				+ Utils.stuffStr(minutesD + "", "0", 2, false) + ":"
				+ Utils.stuffStr(secondsD + "", "0", 2, false);

		if (!onlyTime) {
			diffStr = daysD + " dia" + ((daysD > 1) ? "s e " : " ") + diffStr;
		}

		return diffStr;
	}

	/**
	 * 
	 * Coordenadas para informar onde foi tirada a foto
	 * 
	 * @param directionLat
	 *            : N ou S
	 * @param degreesLat
	 *            : graus
	 * @param minLat
	 *            : minutos
	 * @param secLat
	 *            : segundos
	 * @param directionLong
	 *            : W ou E
	 * @param degreesLong
	 *            : graus
	 * @param minLong
	 *            : minutos
	 * @param secLong
	 *            : segundos
	 * 
	 * @return
	 */
	public static String[] getLatAndLongByCoordinates(String latReference,
			double degreesLat, double minLat, double secLat,
			String longReference, double degreesLong, double minLong,
			double secLong) {
		// Latitude
		double latVal = degreesLat * 1 + minLat / 60 + secLat / 3600;

		if (latReference.toLowerCase().equals("s")) {
			latVal = -latVal;
		}

		double longVal = degreesLong * 1 + minLong / 60 + secLong / 3600;

		if (longReference.toLowerCase().equals("w")) {
			longVal = -longVal;
		}

		String latitude = Utils.replace(Utils.formatNumber(latVal, 3, 3), ",",
				".");
		String longitude = Utils.replace(Utils.formatNumber(longVal, 3, 3),
				",", ".");

		return new String[] { latitude, longitude };
	}

	/**
	 * Quebra uma coordenada para retornar graus, minutos e segundos separados
	 * 
	 * @param coordinate
	 *            : graus,minutos,segundos
	 * @return
	 */
	public static String[] getDataByCoordinates(String coordinate) {
		String[] dataCoordinates = new String[3];

		try {

			String[] cut = Utils.getLines(coordinate, '"');

			String degrees = cut[0];

			coordinate = cut[1];

			cut = Utils.getLines(coordinate, "'");

			String min = cut[0];
			String sec = cut[1];

			dataCoordinates[0] = degrees;
			dataCoordinates[1] = min;
			dataCoordinates[2] = sec;
		} catch (Exception e) {
			dataCoordinates = null;
		}

		return dataCoordinates;
	}

	/**
	 * Converte coordenadas e referências em double.
	 * 
	 * @param latReference
	 * @param latitude
	 * @param longReference
	 * @param longitude
	 * @return Array de 2, contendo latitude e longitude.
	 */
	public static double[] getLatAndLongByCoordinates(String latReference,
			String latitude, String longReference, String longitude) {

		String[] dataLatCoordinates = Utils.getDataByCoordinates(latitude);
		String[] dataLongCoordinates = Utils.getDataByCoordinates(longitude);

		if (dataLatCoordinates == null || dataLongCoordinates == null) {
			return null;
		}

		String[] coorsStr = getLatAndLongByCoordinates(latReference,
				Utils.parseDouble(dataLatCoordinates[0]),
				Utils.parseDouble(dataLatCoordinates[1]),
				Utils.parseDouble(dataLatCoordinates[2]), longReference,
				Utils.parseDouble(dataLongCoordinates[0]),
				Utils.parseDouble(dataLongCoordinates[1]),
				Utils.parseDouble(dataLongCoordinates[2]));

		double[] coors = new double[2];

		coors[0] = Utils.parseDouble(coorsStr[0]);
		coors[1] = Utils.parseDouble(coorsStr[1]);

		return coors;
	}

	/**
	 * Se é uma extenção de arquivo perigosta, ou seja, quando mandar arquivo e
	 * não for restrito a imagens, não podem ser essas extensões.
	 * 
	 * @param fileName
	 * @return
	 */
	public static boolean isDangerousExt(String fileName) {
		// procura e remove eventualmente algo com ?
		int questionMark = fileName.indexOf("?");
		if (questionMark != -1) {
			fileName = fileName.substring(0, questionMark);
		}

		String ext = fileName.substring(fileName.lastIndexOf('.') + 1,
				fileName.length());

		ext = Utils.toLowerCaseUrl(ext);

		for (int i = 0; i < DANGEROUS_EXTS.length; i++) {
			if (ext.equals(DANGEROUS_EXTS[i])) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Complementar a isDangerousExt: mais seguro, ou seja, em vez de dizer as
	 * perigosas, restringe apenas em imagens.
	 * 
	 * @param filename
	 * @return
	 */
	public static boolean isImageExt(String fileName) {
		// procura e remove eventualmente algo com ?
		int questionMark = fileName.indexOf("?");
		if (questionMark != -1) {
			fileName = fileName.substring(0, questionMark);
		}

		String ext = fileName.substring(fileName.lastIndexOf('.') + 1,
				fileName.length());

		ext = Utils.toLowerCaseUrl(ext);

		for (int i = 0; i < IMAGE_EXTS.length; i++) {
			if (ext.equals(IMAGE_EXTS[i])) {
				return true;
			}
		}

		return false;

	}

	/**
	 * Extrai as hashtags de um texto, preservando o case e acentos.
	 * 
	 * @param text
	 * @param collapseWords
	 *            Se é para colapsar traços e underline (ex: leo-legal vira
	 *            leolegal)
	 * @param tinyWords
	 *            Se em minúsculo
	 * @param removeTones
	 *            Tira acentos.
	 * @param removeRepeateds
	 * @return
	 * @throws Exeption
	 */
	public static String[] extractHashs(String text, boolean collapseWords,
			boolean tinyWords, boolean removeTones, boolean removeRepeateds) {
		ArrayList<String> hashs = new ArrayList<String>();

		char[] chars = text.toCharArray();
		StringBuffer currHash = null;

		for (int i = 0; i < chars.length; i++) {
			// se está em hash...
			if (currHash != null) {
				if (collapseWords && (chars[i] == '-' || chars[i] == '_')) {
					continue;
				}

				// ... e for letra ou número, pega. Caso contrário, terminou a
				// hash.
				if (Utils.isLetter(chars[i]) || Utils.isNumber(chars[i])) {
					currHash.append(chars[i]);
				} else {
					String hashtag = currHash.toString().trim();
					if (tinyWords) {
						hashtag = hashtag.toLowerCase();
					}

					if (removeTones) {
						hashtag = Utils.removeTones(hashtag);
					}

					if (!hashtag.equals("")
							&& (!removeRepeateds || hashs.indexOf(hashtag) == -1)) {
						hashs.add(hashtag);
					}

					currHash = null;
				}
			}

			// depois para o caso em que tem duas # seguidas.
			if (chars[i] == '#') {
				currHash = new StringBuffer();
				continue;
			}
		}

		// se chegou ao final e estava dentro de hash, pega.
		if (currHash != null) {
			String hashtag = currHash.toString().trim();

			if (tinyWords) {
				hashtag = hashtag.toLowerCase();
			}

			if (removeTones) {
				hashtag = Utils.removeTones(hashtag);
			}

			if (!hashtag.equals("")
					&& (!removeRepeateds || hashs.indexOf(hashtag) == -1)) {
				hashs.add(hashtag);
			}
		}

		return (String[]) hashs.toArray(new String[hashs.size()]);
	}

	/**
	 * Torna as hashtags passadas (sem #) em um texto formatado. Pode passar
	 * exceções, que não serão alteradas.
	 * 
	 * @param text
	 *            texto a ser formatado.
	 * @param substHashMaskedText
	 *            Texto com máscara a ser substituído. As máscaras são:
	 *            (hashtag) e (hashedtag), esta última inclui a {@link #clone()}
	 *            .
	 * @param exceptionsHashs
	 *            Exceçções a serem ignoradas.
	 * @return
	 */
	public static String formatHashedText(String text,
			String substHashMaskedText, String[] exceptionsHashs) {
		// se houver exceções, limpa.
		if (exceptionsHashs != null) {
			for (int i = 0; i < exceptionsHashs.length; i++) {
				exceptionsHashs[i] = Utils.hashWord(exceptionsHashs[i]);
			}
		}

		// para no futuro caso se queira para de colapsar
		boolean collapseWords = true;

		StringBuilder sb = new StringBuilder(text + " ");
		int foundHash = -1;
		int lastHashEnd = -1;

		for (int i = 0; i < sb.length(); i++) {
			char c = sb.charAt(i);

			if (foundHash != -1) {
				if (collapseWords && (c == '-' || c == '_')) {
					continue;
				}

				// ... e for letra ou número, pega. Caso contrário, terminou a
				// hash.
				if (Utils.isLetter(c) || Utils.isNumber(c)) {
					continue;
				}

				// é uma hash vazia, segue então.
				if (foundHash + 1 == i) {
					foundHash = -1;
					i--;
					continue;
				}

				// quebrou a hash. Para tirar hash.
				String currHash = sb.substring(foundHash + 1, i);

				// transforma em hash uma palavra.
				currHash = hashWord(currHash);

				// exceções
				if (exceptionsHashs != null
						&& Utils.indexOf(currHash, exceptionsHashs) != -1) {
					foundHash = -1;
					i--;
					continue;
				}

				// formata, substuindo onde houver com hash e sem hash
				String formattedText = Utils.format(substHashMaskedText,
						new String[] { currHash, "#" + currHash },
						new String[] { "(hashtag)", "(hashedtag)" });

				sb.replace(foundHash, i, formattedText);

				i += formattedText.length() - currHash.length() - 2;

				lastHashEnd = i;
				foundHash = -1;
			} else if (c == '#') {
				if (lastHashEnd != -1 && i == lastHashEnd + 1) {
					sb.insert(i, ' ');
					lastHashEnd = -1;
				}

				foundHash = i;
				continue;
			}

		}

		return sb.substring(0, sb.length() - 1);
	}

	/**
	 * Transforma uma palavra em hash, seguindo a regra: minúsculo, colapsa - e
	 * _.
	 * 
	 * @param word
	 * @return
	 */
	public static String hashWord(String word) {
		word = word.toLowerCase();
		word = Utils.removeTones(word);

		StringBuilder sb = new StringBuilder(word);
		for (int i = 0; i < sb.length(); i++) {
			if (sb.charAt(i) == '-' || sb.charAt(i) == '_') {
				sb.delete(i, i);
			}
		}

		return sb.toString();
	}

	/**
	 * Remove arquivos dentro de uma pasta
	 * 
	 * @param pathFolder
	 */
	public static void removeFilesInFolder(String pathFolder) {
		File file = new File(pathFolder);

		if (file.exists()) {
			String[] files = file.list();

			for (int i = 0; files != null && i < files.length; i++) {
				File fileRemove = new File(pathFolder + "/" + files[i]);

				fileRemove.delete();
			}
		}
	}

	/**
	 * 
	 * Verifica se é um login válido. Só não avalia se tá minúsculo e maíusculo.
	 * 
	 * 2013-03-01 Por Javascript, para o Xpress, fiz coisa melhor.
	 * 
	 * @param login
	 * @return
	 */
	public static boolean isValidLogin(String login) {
		boolean isValid = true;

		char[] chLogin = login.toCharArray();

		for (int i = 0; i < chLogin.length; i++) {
			if (!Utils.isLetter(chLogin[i]) && !Utils.isNumber(chLogin[i])
					&& chLogin[i] != '-' && chLogin[i] != '_'
					&& chLogin[i] != '.') {
				isValid = false;
				break;
			}
		}
		return isValid;
	}

	/**
	 * Gera senha. Tenta 100 vezes gerar, pois pode acontecer de dar dígitos
	 * inválidos (ex, 0, O...).
	 * 
	 * @return
	 */
	public static String createRandomPassword(int count) {
		StringBuffer code = new StringBuffer();
		// boolean repeat = true;
		// int countTimes = 0;

		// Enquanto for para repetir e as tentativas for menor que 100
		// while (repeat && countTimes < 100) {
		// fazer enquanto code não possui 15 caracteres
		for (int i = 0; code.length() < count; i++) {
			// Se o randomico for par, inclui um numero randomico no codigo
			// a ser gerado
			if (((Utils.getRandomNumber() % 10) % 2) == 0) {
				int number = Utils.getRandomNumber() % 10;

				// Se o numero for diferente de zero adiciona (para evitar
				// confundir com a letra "O")
				if (number != 0) {
					code.append(String.valueOf(number));
				}
				// Se não, se o randomico gerado for impar, inclui uma letra
				// randomico no codigo a ser gerado
			} else {
				char letter = Character.toUpperCase(Utils.getRandomLetter());

				// Se a letra for diferente de "O" adiciona (para evitar
				// confundir com o numero zero)
				if (letter != 'O') {
					code.append(letter);
				}
			}
			// }

			// Se o codigo já existir no banco, gerar novamente o codigo
			// if (this.searchPurchaseByRandomicCode(code) != null) {
			// code = "";
			// countTimes++;
			// // Se não, o codigo não existe e este gerado será armazenado
			// } else {
			// repeat = false;
			// }
		}

		// Se a quantidade de caracteres no codigo for diferente de 15
		// if (code.length() != count) {
		// code = "";
		// }

		return code.toString();
	}

	/**
	 * Retorna um sub-array pelo PagingInfo dado. São levados em consideração os
	 * campos curPage e pageSize
	 * 
	 * @param complete
	 * @param pi
	 * @return
	 */
	public static String[] getSubArray(String[] completeArray, PagingInfo pi) {

		pi.setRegsCount(completeArray.length);

		int from = pi.getFirstRegPos();

		ArrayList<String> r = new ArrayList<String>();
		for (int i = from - 1; i < completeArray.length
				&& r.size() < pi.getPageSize(); i++) {
			r.add(completeArray[i]);
		}

		return (String[]) r.toArray(new String[r.size()]);
	}

	/**
	 * É um trim que dá trim também em caracteres que o trim() de String não
	 * pega (ex: -96).
	 * 
	 * @param str
	 * @return
	 */
	public static String trimEx(String str) {
		str = str.trim();

		// O caracter é este [  ] -96 0xa0
		str = str.replace(" ", "");

		return str;
	}

	/**
	 * Colapsa um arquivo na url atual, muito comum em, ex., estou numa página
	 * com vários níveis de pastas e quero refencia a algo na raiz.
	 * 
	 * Exemplo:
	 * 
	 * arquivo: img.gif estando em: http://www.linkws.com/index.htm resultado:
	 * http://www.linkws.com/img.gif
	 * 
	 * arquivo: ../img.gif estando em: http://www.linkws.com/1/index.htm
	 * resultado: http://www.linkws.com/img.gif
	 * 
	 * arquivo: ../../img.gif estando em: http://www.linkws.com/1/2/index.htm
	 * resultado: http://www.linkws.com/img.gif
	 * 
	 * arquivo: /img.gif estando em: http://www.linkws.com/1/2/index.htm
	 * resultado: http://www.linkws.com/img.gif
	 * 
	 * arquivo: .././img.gif estando em: http://www.linkws.com/1/2/index.htm
	 * resultado: http://www.linkws.com/1/img.gif
	 * 
	 * arquivo: ../3/img.gif estando em: http://www.linkws.com/1/2/index.htm
	 * resultado: http://www.linkws.com/1/3/img.gif
	 * 
	 * @param url
	 * @param imgUrl
	 * @return
	 */
	public static String colapseUrl(String url, String imgUrl) {
		if (url == null || imgUrl == null || url.length() < 5
				|| imgUrl.length() < 5) {
			throw new IllegalArgumentException("url ou imgUrl inválido.");
		}

		// limpezinha de inutilidades
		if (imgUrl.startsWith("./")) {
			imgUrl = imgUrl.substring(2, imgUrl.length());
		}

		// 1. tratamento do caminho
		String pathAndDir = url;
		String completeUrl = "";

		if (url.lastIndexOf('/') > 0) {
			pathAndDir = url.substring(0, url.lastIndexOf('/')) + "/";

			if (pathAndDir.substring(pathAndDir.length() - 2,
					pathAndDir.length()).equals("//")) {
				pathAndDir = url;
			}
		}

		// se tiver / no final, tira, que a correção é depois
		if (pathAndDir.charAt(pathAndDir.length() - 1) == '/') {
			pathAndDir = pathAndDir.substring(0, pathAndDir.length() - 1);
		}

		// 2.1 Se tiver / na imagem, acabou, vai para a raiz.
		if (imgUrl.charAt(0) == '/') {
			// limpezinha de inutilidades
			if (imgUrl.startsWith("/./")) {
				imgUrl = imgUrl.substring(2, imgUrl.length());
			}

			completeUrl = url.substring(0, url.indexOf('/')) + "//"
					+ Utils.extractDomainName(url) + imgUrl;
		} else {
			String domain = Utils.extractDomainName(pathAndDir);

			// 2. tratamento do caminho em relação ao arquivo passado
			while (imgUrl.indexOf("../") != -1) {
				// limpezinha de inutilidades
				if (url.startsWith("./")) {
					url = url.substring(2, url.length());
				}

				// se não já chegou na raiz, ex., estou em um diretório e boto
				// ../../../IMAGEM.GIF
				if (!pathAndDir.endsWith(domain)) {
					if (url.lastIndexOf('/') > 0) {
						pathAndDir = pathAndDir.substring(0,
								pathAndDir.lastIndexOf('/'));
					}
				}

				imgUrl = Utils.replace(imgUrl, "../", "");
			}

			// limpezinha de inutilidades
			if (imgUrl.startsWith("./")) {
				imgUrl = imgUrl.substring(2, imgUrl.length());
			}

			if (pathAndDir.charAt(pathAndDir.length() - 1) != '/') {
				pathAndDir += '/';
			}

			completeUrl = pathAndDir + imgUrl;
		}

		return completeUrl;
	}

	/**
	 * Pega o texto e converte para uma versão mais simples caracteres
	 * especiais. Por exemlo, "—" (ASCII: 8212) vira "-" (traço normlal)
	 * 
	 * Outros caracteres especiais:
	 * 
	 * http://www.caracteresespeciais.com/
	 * 
	 * Sites, por exemplo, que tem aspas caidinhas, etc.:
	 * 
	 * String url =
	 * "http://blogs.estadao.com.br/herton-escobar/buracos-negros-a-caminho-pesquisadores-descobrem-26-deles-em-galaxia-que-vai-se-chocar-com-a-nossa/"
	 * ; String url =
	 * "http://tudonainternet.com/2013/02/harlem-shake-o-que-e-harlem-shake-musica-download-videos.html"
	 * ;
	 * 
	 * @param c
	 * @return
	 */
	public static String textToSimplerText(String text) {
		char[] cs = text.toCharArray();

		for (int i = 0; i < cs.length; i++) {
			if (cs[i] == '—') {
				cs[i] = '-';
			} else if (cs[i] == '»') {
				cs[i] = '>';
			} else if (cs[i] == '«') {
				cs[i] = '<';
			} else if (cs[i] == '“' || cs[i] == '”') {
				cs[i] = '"';
			} else if (cs[i] == '‘' || cs[i] == '’') {
				cs[i] = '\'';
			}
		}
		return new String(cs);
	}

	/**
	 * Faz esforço para limpar coisas inpúteis em um html (espaços, enter,
	 * etc.).
	 * 
	 * Por enquanto, tira apenas espaços duplicados.
	 * 
	 * Pendência mais simples para cortar mais: remover comentários em script.
	 * 
	 * @param html
	 * @param onlyJs
	 * @return
	 */
	public static String cleanUpHtml(String html, boolean onlyJs) {
		StringBuffer sbOut = new StringBuffer();

		char[] chs = html.toCharArray();

		boolean lasCharIsSpace = false;
		for (int i = 0; i < chs.length; i++) {
			if (Utils.isSpaceChar(chs[i])) {
				if (!lasCharIsSpace) {
					sbOut.append(chs[i]); // espaço vira espaço
				}

				lasCharIsSpace = true;
				continue;
			} else {
				lasCharIsSpace = false;
			}

			sbOut.append(chs[i]);
		}

		return sbOut.toString();
	}

	/**
	 * Extrai barras final na url.
	 * 
	 * @param url
	 * @return
	 */
	public static String extractBarUrl(String url) {
		if (url == null || url.equals("")) {
			return "";
		}

		int lastPosition = url.lastIndexOf("/");
		String newUrl = url;

		while ((lastPosition + 1) == newUrl.length()) {

			newUrl = url.substring(0, lastPosition);

			lastPosition = newUrl.lastIndexOf("/");

		}
		// System.out.println(newUrl);

		return newUrl;
	}

	/**
	 * Gera um código randômicc do tamanho passado, com letras e números.
	 * 
	 * Não está da melhor forma, mas leva 0 milissegundos para gerar um código
	 * de 50.
	 * 
	 * @param size
	 * @return
	 */
	public static String generateRandomCode(int size) {
		String r = "";

		for (int i = 0; i < size; i++) {
			// gera um randômico, para uso de decisão se vai ser letra ou número
			// (usando ele próprio
			// caso se for número)
			int rnd = Utils.getRandomNumber();

			if (rnd % 2 == 0) {
				r += Utils.getRandomLetter();
			} else {
				r += (rnd % 10);
			}
		}

		return r;
	}

	/**
	 * Cria uma cópia do diretório, ou seja, tudo que tem no diretório e nos
	 * subs em outro. É recursivo.
	 * 
	 * OBS: Respeita:
	 * 
	 * 
	 * COPIES_MAX: máximo de arquivos copiados COPIES_ONLY_CURRENT_PROJECT:
	 * origem e destino apenas no projeto corrente.
	 * 
	 * @param src
	 * @param dst
	 * @return Total de arquivos ou diretórios criados.
	 * 
	 * @throws IOException
	 */
	public static int copyFolder(String src, String dst) throws IOException {
		return copyFolder(src, dst, 0);
	}

	/**
	 * Copia recursivamente o diretório passado.
	 * 
	 * @param src
	 * @param dst
	 * @param currentStopTotal
	 * @return
	 * @throws IOException
	 */
	protected static int copyFolder(String src, String dst, int currentStopTotal)
			throws IOException {
		File srcFolder = new File(src);
		File dstFolder = new File(dst);

		// segurança: a cópia tem que envoolver coisas no diretóerio corrente
		// apenas.
		if (COPIES_ONLY_CURRENT_PROJECT
				&& (src.indexOf(JSPUtils.SITE_REAL_PATH) == -1 || dst
						.indexOf(JSPUtils.SITE_REAL_PATH) == -1)) {
			throw new IllegalArgumentException(
					"Os diretórios de origem e fim devem envolver apenas o projeto corrente.");
		}

		dstFolder.mkdirs();

		File[] srcFiles = srcFolder.listFiles();
		int total = 0;
		for (int i = 0; i < srcFiles.length; i++) {
			if (total + currentStopTotal > COPIES_MAX) {
				throw new RuntimeException("Máximo atingido: "
						+ (total + currentStopTotal));
			}

			if (srcFiles[i].isFile()) {
				total++;
				Utils.copySingleFile(
						srcFolder.getPath() + "/" + srcFiles[i].getName(),
						dstFolder.getPath() + "/" + srcFiles[i].getName());
			} else {
				File sub = new File(srcFolder.getPath() + "/"
						+ srcFiles[i].getName());

				// cria o novo diretório de qualquer jeito.
				(new File(dstFolder.getPath() + "/" + srcFiles[i].getName()))
						.mkdir();
				total++;

				total += copyFolder(
						srcFolder.getPath() + "/" + srcFiles[i].getName(),
						dstFolder.getPath() + "/" + srcFiles[i].getName(),
						total + currentStopTotal);
			}
		}

		return total;
	}

	/**
	 * Converte de extensão para mimetype. Se quiser desempenho, faz essa função
	 * com HashMap...
	 * 
	 * @param ext
	 * @return
	 */
	public static String extToMime(String ext) {
		if (ext.equals("evy")) {
			return "application/envoy";
		}
		if (ext.equals("fif")) {
			return "application/fractals";
		}
		if (ext.equals("spl")) {
			return "application/futuresplash";
		}
		if (ext.equals("hta")) {
			return "application/hta";
		}
		if (ext.equals("acx")) {
			return "application/internet-property-stream";
		}
		if (ext.equals("hqx")) {
			return "application/mac-binhex40";
		}
		if (ext.equals("doc")) {
			return "application/msword";
		}
		if (ext.equals("exe")) {
			return "application/octet-stream";
		}
		if (ext.equals("oda")) {
			return "application/oda";
		}
		if (ext.equals("axs")) {
			return "application/olescript";
		}
		if (ext.equals("pdf")) {
			return "application/pdf";
		}
		if (ext.equals("prf")) {
			return "application/pics-rules";
		}
		if (ext.equals("p10")) {
			return "application/pkcs10";
		}
		if (ext.equals("crl")) {
			return "application/pkix-crl";
		}
		if (ext.equals("ai")) {
			return "application/postscript";
		}
		if (ext.equals("eps")) {
			return "application/postscript";
		}
		if (ext.equals("ps")) {
			return "application/postscript";
		}
		if (ext.equals("rtf")) {
			return "application/rtf";
		}
		if (ext.equals("setpay")) {
			return "application/set-payment-initiation";
		}
		if (ext.equals("setreg")) {
			return "application/set-registration-initiation";
		}
		if (ext.equals("xls")) {
			return "application/vnd.ms-excel";
		}
		if (ext.equals("msg")) {
			return "application/vnd.ms-outlook";
		}
		if (ext.equals("sst")) {
			return "application/vnd.ms-pkicertstore";
		}
		if (ext.equals("cat")) {
			return "application/vnd.ms-pkiseccat";
		}
		if (ext.equals("stl")) {
			return "application/vnd.ms-pkistl";
		}
		if (ext.equals("ppt")) {
			return "application/vnd.ms-powerpoint";
		}
		if (ext.equals("mpp")) {
			return "application/vnd.ms-project";
		}
		if (ext.equals("wks")) {
			return "application/vnd.ms-works";
		}
		if (ext.equals("hlp")) {
			return "application/winhlp";
		}
		if (ext.equals("bcpio")) {
			return "application/x-bcpio";
		}
		if (ext.equals("cdf")) {
			return "application/x-cdf";
		}
		if (ext.equals("z")) {
			return "application/x-compress";
		}
		if (ext.equals("tgz")) {
			return "application/x-compressed";
		}
		if (ext.equals("cpio")) {
			return "application/x-cpio";
		}
		if (ext.equals("csh")) {
			return "application/x-csh";
		}
		if (ext.equals("dir")) {
			return "application/x-director";
		}
		if (ext.equals("dvi")) {
			return "application/x-dvi";
		}
		if (ext.equals("gtar")) {
			return "application/x-gtar";
		}
		if (ext.equals("gz")) {
			return "application/x-gzip";
		}
		if (ext.equals("hdf")) {
			return "application/x-hdf";
		}
		if (ext.equals("ins")) {
			return "application/x-internet-signup";
		}
		if (ext.equals("iii")) {
			return "application/x-iphone";
		}
		if (ext.equals("js")) {
			return "application/x-javascript";
		}
		if (ext.equals("latex")) {
			return "application/x-latex";
		}
		if (ext.equals("mdb")) {
			return "application/x-msaccess";
		}
		if (ext.equals("crd")) {
			return "application/x-mscardfile";
		}
		if (ext.equals("clp")) {
			return "application/x-msclip";
		}
		if (ext.equals("dll")) {
			return "application/x-msdownload";
		}
		if (ext.equals("mvb")) {
			return "application/x-msmediaview";
		}
		if (ext.equals("wmf")) {
			return "application/x-msmetafile";
		}
		if (ext.equals("mny")) {
			return "application/x-msmoney";
		}
		if (ext.equals("pub")) {
			return "application/x-mspublisher";
		}
		if (ext.equals("scd")) {
			return "application/x-msschedule";
		}
		if (ext.equals("trm")) {
			return "application/x-msterminal";
		}
		if (ext.equals("wri")) {
			return "application/x-mswrite";
		}
		if (ext.equals("nc")) {
			return "application/x-netcdf";
		}
		if (ext.equals("pmc")) {
			return "application/x-perfmon";
		}
		if (ext.equals("pfx")) {
			return "application/x-pkcs12";
		}
		if (ext.equals("spc")) {
			return "application/x-pkcs7-certificates";
		}
		if (ext.equals("p7r")) {
			return "application/x-pkcs7-certreqresp";
		}
		if (ext.equals("p7m")) {
			return "application/x-pkcs7-mime";
		}
		if (ext.equals("p7s")) {
			return "application/x-pkcs7-signature";
		}
		if (ext.equals("sh")) {
			return "application/x-sh";
		}
		if (ext.equals("shar")) {
			return "application/x-shar";
		}
		if (ext.equals("swf")) {
			return "application/x-shockwave-flash";
		}
		if (ext.equals("sit")) {
			return "application/x-stuffit";
		}
		if (ext.equals("sv4cpio")) {
			return "application/x-sv4cpio";
		}
		if (ext.equals("sv4crc")) {
			return "application/x-sv4crc";
		}
		if (ext.equals("tar")) {
			return "application/x-tar";
		}
		if (ext.equals("tcl")) {
			return "application/x-tcl";
		}
		if (ext.equals("tex")) {
			return "application/x-tex";
		}
		if (ext.equals("texinfo")) {
			return "application/x-texinfo";
		}
		if (ext.equals("roff")) {
			return "application/x-troff";
		}
		if (ext.equals("man")) {
			return "application/x-troff-man";
		}
		if (ext.equals("me")) {
			return "application/x-troff-me";
		}
		if (ext.equals("ms")) {
			return "application/x-troff-ms";
		}
		if (ext.equals("ustar")) {
			return "application/x-ustar";
		}
		if (ext.equals("src")) {
			return "application/x-wais-source";
		}
		if (ext.equals("crt")) {
			return "application/x-x509-ca-cert";
		}
		if (ext.equals("pko")) {
			return "application/ynd.ms-pkipko";
		}
		if (ext.equals("zip")) {
			return "application/zip";
		}
		if (ext.equals("au")) {
			return "audio/basic";
		}
		if (ext.equals("snd")) {
			return "audio/basic";
		}
		if (ext.equals("mid")) {
			return "audio/mid";
		}
		if (ext.equals("rmi")) {
			return "audio/mid";
		}
		if (ext.equals("mp3")) {
			return "audio/mpeg";
		}
		if (ext.equals("aiff")) {
			return "audio/x-aiff";
		}
		if (ext.equals("m3u")) {
			return "audio/x-mpegurl";
		}
		if (ext.equals("ram")) {
			return "audio/x-pn-realaudio";
		}
		if (ext.equals("wav")) {
			return "audio/x-wav";
		}
		if (ext.equals("bmp")) {
			return "image/bmp";
		}
		if (ext.equals("cod")) {
			return "image/cis-cod";
		}
		if (ext.equals("gif")) {
			return "image/gif";
		}
		if (ext.equals("ief")) {
			return "image/ief";
		}
		if (ext.equals("jpg") || ext.equals("jpeg")) {
			return "image/jpeg";
		}
		if (ext.equals("jfif")) {
			return "image/pipeg";
		}
		if (ext.equals("svg")) {
			return "image/svg+xml";
		}
		if (ext.equals("tiff")) {
			return "image/tiff";
		}
		if (ext.equals("ras")) {
			return "image/x-cmu-raster";
		}
		if (ext.equals("cmx")) {
			return "image/x-cmx";
		}
		if (ext.equals("ico")) {
			return "image/x-icon";
		}
		if (ext.equals("pnm")) {
			return "image/x-portable-anymap";
		}
		if (ext.equals("pbm")) {
			return "image/x-portable-bitmap";
		}
		if (ext.equals("pgm")) {
			return "image/x-portable-graymap";
		}
		if (ext.equals("ppm")) {
			return "image/x-portable-pixmap";
		}
		if (ext.equals("rgb")) {
			return "image/x-rgb";
		}
		if (ext.equals("xbm")) {
			return "image/x-xbitmap";
		}
		if (ext.equals("xpm")) {
			return "image/x-xpixmap";
		}
		if (ext.equals("xwd")) {
			return "image/x-xwindowdump";
		}
		if (ext.equals("mhtml")) {
			return "message/rfc822";
		}
		if (ext.equals("css")) {
			return "text/css";
		}
		if (ext.equals("323")) {
			return "text/h323";
		}
		if (ext.equals("html")) {
			return "text/html";
		}
		if (ext.equals("uls")) {
			return "text/iuls";
		}
		if (ext.equals("txt")) {
			return "text/plain";
		}
		if (ext.equals("rtx")) {
			return "text/richtext";
		}
		if (ext.equals("sct")) {
			return "text/scriptlet";
		}
		if (ext.equals("tsv")) {
			return "text/tab-separated-values";
		}
		if (ext.equals("htt")) {
			return "text/webviewhtml";
		}
		if (ext.equals("htc")) {
			return "text/x-component";
		}
		if (ext.equals("etx")) {
			return "text/x-setext";
		}
		if (ext.equals("vcf")) {
			return "text/x-vcard";
		}
		if (ext.equals("mpeg")) {
			return "video/mpeg";
		}
		if (ext.equals("mov")) {
			return "video/quicktime";
		}
		if (ext.equals("asf")) {
			return "video/x-ms-asf";
		}
		if (ext.equals("avi")) {
			return "video/x-msvideo";
		}
		if (ext.equals("movie")) {
			return "video/x-sgi-movie";
		}
		if (ext.equals("vrml")) {
			return "x-world/x-vrml";
		}

		return "";
	}

	public static String getMonthStr(Date date) {
		String monthStr = "";

		int month = date.getMonth();
		switch (month) {
		// Janeiro
		case 0: {
			monthStr = "Janeiro";
			break;
		}

		// Fevereiro
		case 1: {
			monthStr = "Fevereiro";
			break;
		}

		// Março
		case 2: {
			monthStr = "Março";
			break;
		}

		// Abril
		case 3: {
			monthStr = "Abril";
			break;
		}

		// Maio
		case 4: {
			monthStr = "Maio";
			break;
		}

		// Junho
		case 5: {
			monthStr = "Junho";
			break;
		}

		// Julho
		case 6: {
			monthStr = "Julho";
			break;
		}

		// Agosto
		case 7: {
			monthStr = "Agosto";
			break;
		}

		// Setembro
		case 8: {
			monthStr = "Setembro";
			break;
		}

		// Outubro
		case 9: {
			monthStr = "Outubro";
			break;
		}

		// Novembro
		case 10: {
			monthStr = "Novembro";
			break;
		}

		// Dezembro
		case 11: {
			monthStr = "Dezembro";
			break;
		}
		}

		return monthStr;
	}

	public static String getDayStr(Date date) {
		String dayStr = "";

		int month = date.getDay();
		switch (month) {
		// Domingo
		case 0: {
			dayStr = "Domingo";
			break;
		}

		// Segunda - feira
		case 1: {
			dayStr = "Segunda - feira";
			break;
		}

		// Terça - feira
		case 2: {
			dayStr = "Ter&ccedil;a - feira";
			break;
		}

		// Quarta - feira
		case 3: {
			dayStr = "Quarta - feira";
			break;
		}

		// Quinta - feira
		case 4: {
			dayStr = "Quinta - feira";
			break;
		}

		// Sexta - feira
		case 5: {
			dayStr = "Sexta - feira";
			break;
		}

		// Sábado - feira
		case 6: {
			dayStr = "Sábado";
			break;
		}
		}

		return dayStr;
	}

	/**
	 * Retorna um ítem randomico de um array principal
	 * 
	 * @param lengthArray
	 * @return item randomico
	 */
	public static int randomIndexArray(int lengthArray) {
		return new Random().nextInt(lengthArray);
	}

	/**
	 * Retorna um array de itens randomicos de um array principal
	 * 
	 * @param lengthArray
	 * @param qtdRandoms
	 *            tamanho do array gerado
	 * @return um array de itens randomicos
	 */
	public static int[] randomsIndexArray(int lengthArray, int qtdRandoms) {

		int[] rands = new int[qtdRandoms];

		for (int i = 0; i < rands.length; i++) {
			rands[i] = new Random().nextInt(lengthArray);
		}
		return rands;
	}

	/**
	 * Retorna o ultimo caractere de uma string
	 * 
	 * @param str
	 * @return
	 */
	public static String lastChar(String str) {
		if (str.length() > 0) {
			return str.substring((str.length() - 1), str.length());
		}
		return "";
	}

	public static String relativeUrlToAbsolut(String urlBase,
			String relativePath) {
		
		System.out.println("[relativeUrlToAbsolut]URLBASE: " + urlBase + "__relativePath: " + relativePath);

		String[] u = urlBase.split("://");

		urlBase = extractDomainName(urlBase);

		char c = relativePath.charAt(0);
		if ((c + "").equals("/")) {
			urlBase = u[0] + "://" + urlBase + relativePath;
		} else {
			urlBase = u[0] + "://" + urlBase + "/" + relativePath;
		}

		return urlBase;
	}
}
