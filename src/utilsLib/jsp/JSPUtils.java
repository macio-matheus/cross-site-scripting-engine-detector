package utilsLib.jsp;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspWriter;

import utilsLib.util.Utils;
import utilsLib.util.data.Field;
import utilsLib.util.data.ParamsReader;

/**
 * Classe de definicao de mensagens padroes. Servem para comunicacao codificada
 * entre objetos (numeros) e posterior obtencao de valores de String
 * equivalentes.
 */
public class JSPUtils {
	private ParamsReader params;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private JspWriter out;

	public static boolean LOGGING = false;

	public JSPUtils(HttpServletRequest request, HttpServletResponse response,
			JspWriter out) {
		params = new ParamsReader(request);
		this.request = request;
		this.response = response;
		this.out = out;

		if (LOGGING) {
			logAccess();
		}
	}

	public String getAccessLog() {
		// hora
		String hora = Utils.dateToStr(new Date(), "HH:mm");

		// nome da página?
		String pageName = request.getRequestURL().toString();

		// ip
		String ip = request.getRemoteAddr();

		// headers
		String headersName = null;
		for (Enumeration<String> e = request.getHeaderNames(); e.hasMoreElements();) {
			if (headersName == null) {
				headersName = "HEADER: ";
			} else {
				headersName = headersName + " | ";
			}

			String name = (String) e.nextElement();
			headersName += name + " [" + request.getHeader(name) + "]";
		}

		return hora + "," + ip + "," + pageName + ","
				+ request.getQueryString() + ","
				+ Utils.formatIfNull(headersName) + "\n";
	}

	public void logAccess() {
		// Data
		String filePath = JSPUtils.SITE_REAL_PATH + "/WI/logs/jsp/jsp_"
				+ Utils.dateToStr(new Date()) + ".txt";

		try {
			Utils.writeStrFile(filePath, this.getAccessLog(), true);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public static final int REDIRECT_METHOD_DEFAULT = -1;
	public static final int REDIRECT_METHOD_SEND = 0;
	public static final int REDIRECT_METHOD_SCRIPT = 1;
	public static final int REDIRECT_METHOD_DISPATCH = 2;

	// public static String SITE_URL = "http://meusite.com.br:8080";
	// public static String SITE_REAL_PATH = "/home/down/www";

	public static String SITE_URL = "http://localhost:8080";
	public static String SITE_REAL_PATH = "C:\\PROJETOS\\analysexss\\WebContent\\ROOT";

	/**
	 * Redireciona para destinUrl a página atual, usando o melhor método para
	 * isto. Após a chamada deste método, deve-se usar "return" para que o
	 * processamento posterior dentro da página/servlet não seja executado.
	 * 
	 */
	public static void redirect(String destinUrl, HttpServletRequest request,
			HttpServletResponse response, int redirectMethod)
			throws ServletException, IOException {
		if (destinUrl == null) {
			throw new IllegalArgumentException("Param: destinUrl");
		}

		if (request == null) {
			throw new IllegalArgumentException("Param: request");
		}

		if (response == null) {
			throw new IllegalArgumentException("Param: response");
		}

		if (redirectMethod == REDIRECT_METHOD_DEFAULT) {
			if (destinUrl.startsWith("http://")) {
				if (destinUrl.startsWith(JSPUtils.SITE_URL)) {
					// System.out.println("1");
					JSPUtils.log("[JSPUtils.redirect()] Teve que redirecionar por javascript: "
							+ destinUrl);
					response.getWriter().print(
							"<script>location.replace('"
									+ formatScriptInStr(destinUrl)
									+ "');</script>");
				} else {
					response.sendRedirect(destinUrl);
				}
			} else {
				response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
				response.setHeader("Location", destinUrl);
				request.getRequestDispatcher(destinUrl).forward(request,
						response);
			}
		} else if (redirectMethod == REDIRECT_METHOD_SCRIPT) {
			response.getWriter().print(
					"<script>location.replace('" + formatScriptInStr(destinUrl)
							+ "');</script>");
		} else if (redirectMethod == REDIRECT_METHOD_DISPATCH) {
			request.getRequestDispatcher(destinUrl).forward(request, response);
		} else {
			response.sendRedirect(destinUrl);
		}
	}

	public static void redirect(String destinUrl, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		redirect(destinUrl, request, response, REDIRECT_METHOD_DEFAULT);
	}

	public static String formatScriptStr(String code) {
		if (code == null) {
			throw new IllegalArgumentException();
		}

		code = Utils.replace(code, "\\", "\\\\", true);
		code = Utils.replace(code, "\"", "\\\"", true);
		code = Utils.replace(code, "\'", "\\\'", true);

		return code.trim();
	}

	public static String formatToScriptOutput(String text) {
		if (text == null) {
			throw new IllegalArgumentException();
		}

		return "document.write(\"" + formatScriptStr(text) + "\");";
	}

	public static String formatParamsToHiddenForm(Iterator<?> request) {
		if (request == null) {
			throw new IllegalArgumentException();
		}

		String htmlCode = "";

		while (request.hasNext()) {
			Field param = (Field) request.next();
			htmlCode += "<input type=\"hidden\"" + " name=\"" + param.getName()
					+ "\"" + " value=\"" + param.getValue() + "\"" + ">";
		}

		return htmlCode;
	}

	/**
	 * Obtém o cookie, se existir, de um dado nome. São passados o array dos
	 * cookies, que são obtidos, por exemplo, pela função
	 * <code>request.getCookies();</code>, e o nome do cookie.
	 * 
	 * @param cookies
	 *            Array contendo os cookies entre os quais serão pesquisados.
	 * @param cookieName
	 *            Nome do cookie a ser procurado.
	 * @return Retorna <code>null</code> caso o cookie não esteja no array ou o
	 *         cookie encontrado.
	 */
	public static Cookie getCookieByName(Cookie[] cookies, String cookieName) {
		if (cookieName == null) {
			throw new IllegalArgumentException();
		}

		if (cookies == null) {
			return null;
		}

		Cookie cookie = null;
		int numCookies = cookies.length;

		// Looping de Procura
		for (int i = 0; i < numCookies; ++i) {
			if (cookies[i].getName().equals(cookieName)) {
				cookie = cookies[i];
				break;
			}
		}

		return cookie;
	}

	public void jspLog(String content) {
		log("jsplog", "(referer: " + request.getHeader("referer") + " | "
				+ request.getQueryString() + ") " + content);
	}

	public static void logError(String content, Exception error) {
		JSPUtils.log(content + Utils.getStackTrace(error, false));
	}

	public static void logError(Object obj, String methodName,
			String description, Exception error) {
		String className = "";

		if (obj != null) {
			className = obj.getClass().getName();
		}

		JSPUtils.logError("[" + className + "." + methodName + "] "
				+ description + " | Erro: ", error);
	}

	public static void logError(Object obj, String content, Exception error) {
		String className = "";

		if (obj != null) {
			className = obj.getClass().getName();
		}

		JSPUtils.logError("[" + className + "] " + content, error);
	}

	/**
	 * 
	 * @param prefix
	 *            Prefixo do arquivo a ser "logado".
	 * @param content
	 *            String
	 */
	public static void log(String prefix, String content) {
		if (content == null) {
			throw new IllegalArgumentException("Parametro inválido: content");
		}

		try {
			GregorianCalendar gc = Utils.getGregorianCalendarInstance();
			int index = gc.get(Calendar.HOUR_OF_DAY) / 6;

			String strDate = Utils.dateToStr(gc.getTime());
			String strDateTime = Utils.dateTimeToStr(gc.getTime());
			String filePath = SITE_REAL_PATH + "/WI/logs/" + prefix + "_"
					+ strDate + "_" + index + ".txt";

			Utils.writeStrFile(filePath, "[" + strDateTime + "] " + content
					+ Utils.getBreakLine(), true);
		} catch (IOException error) {
		} catch (Exception ex) {
			throw new RuntimeException("Erro ao realizar o log: "
					+ ex.getMessage());
		}
	}

	public static void log(String content) {
		log("log", content);
	}

	/**
	 * Formata uma string que fica dentro de uma string de um script. Retira
	 * barras, aspas, etc.
	 * 
	 * @param str
	 *            String a ser foramtada
	 * @return Código formatad.
	 */
	public static String formatScriptInStr(String str) {
		if (str == null) {
			return "";
		}

		str = Utils.format(str, new String[] { "\\\"", "\\\'", "\\n", "" },
				new String[] { "\"", "\'", "\n", "\r" }, true);

		// sobram se tratar as barras. Entao, faz...
		if (str.length() > 0 && str.indexOf("\\") != -1) {
			StringBuffer sb = new StringBuffer(str);

			// para todos os caracteres...
			for (int i = 0; i < sb.length(); i++) {

				if (sb.charAt(i) == '\\'
						&& (i == sb.length() - 1 || "n'\"".indexOf(sb
								.charAt(i + 1)) == -1)) {
					sb.insert(i, '\\');
					i++;
				}
			}

			str = sb.toString();

		}

		return str;
	}

	public static String formatPreformatted(String text) {
		if (text == null) {
			throw new IllegalArgumentException("Param: text");
		}

		text = Utils.format(text, new String[] { "<br>", "", " &nbsp;" },
				new String[] { "\n", "\r", "  " });

		return text;
	}

	public void back() throws Exception {
		JSPUtils.back(out);
	}

	public static void back(JspWriter out) throws Exception {
		out.print("<script>history.go(-1);</script>");
	}

	public static void printAndBack(String msg, JspWriter out) throws Exception {
		if (msg != null) {
			printCharset(out);
			out.print("<script>alert('" + formatScriptInStr(msg)
					+ "');history.go(-1);</script>");
		}
	}

	public static void printCharset(JspWriter out) throws Exception {
		out.print("<meta http-equiv=\"Content-Type\" content=\"text/html; charset="
				+ Utils.URL_ENCODE_ENC + "\" />");
	}

	public static void printAndGo(String msg, String nextUrl, JspWriter out)
			throws Exception {
		if (msg != null) {
			printCharset(out);
			out.print("<script>alert('" + formatScriptInStr(msg)
					+ "');location.href='" + nextUrl + "';</script>");
		}
	}

	public static void printAndClose(String msg, JspWriter out)
			throws Exception {
		if (msg != null) {
			printCharset(out);
			out.print("<script>alert('" + formatScriptInStr(msg)
					+ "');window.close();</script>");
		}
	}

	public double getDouble(String paramName, double errorValue) {
		return getParams().getDouble(paramName, errorValue);
	}

	public double getDouble(String paramName) {
		return getParams().getDouble(paramName);
	}

	public int getInt(String paramName, int errorValue) {
		return getParams().getInt(paramName, errorValue);
	}

	public int getInt(String paramName) {
		return getParams().getInt(paramName);
	}

	public boolean getBool(String paramName, boolean defaultValue) {
		return Utils.toBoo(getParams().getStr(paramName), defaultValue);
	}

	public boolean getBool(String paramName) {
		return getParams().getBool(paramName);
	}

	public String getStr(String paramName) {
		return getParams().getStr(paramName);
	}

	public String getStr(String paramName, String alternative) {
		return getParams().getStr(paramName, alternative);
	}

	public Date getDate(String paramName, String pattern) {
		return getParams().getDate(paramName, pattern);
	}

	public Date getDate(String paramName) {
		return getParams().getDate(paramName);
	}

	public Date getDate(String paramName, Date alternative) {
		return getParams().getDate(paramName, alternative);
	}

	public Date getDateTime(String paramName, Date alternative) {
		return getParams().getDateTime(paramName, alternative);
	}

	public Date getDateTime(String paramName) {
		return getParams().getDateTime(paramName);
	}

	public String[] getStrs(String paramName) {
		return getParams().getStrs(paramName);
	}

	public String[] getStrs(String paramName, String[] alternative) {
		return getParams().getStrs(paramName, alternative);
	}

	public void redirect(String destinUrl) throws ServletException, IOException {
		JSPUtils.redirect(destinUrl, request, response);

	}

	public void printAndBack(String msg) throws Exception {
		JSPUtils.printAndBack(msg, out);
	}

	public void printAndGo(String msg, String nextUrl) throws Exception {
		JSPUtils.printAndGo(msg, nextUrl, out);
	}

	public void printAndClose(String msg) throws Exception {
		JSPUtils.printAndClose(msg, out);
	}

	public ParamsReader getParams() {
		return params;
	}

	public void setParams(ParamsReader params) {
		this.params = params;
	}

}
