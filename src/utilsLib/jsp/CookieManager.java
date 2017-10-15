package utilsLib.jsp;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.*;

/**
 * Gerencia os cookies do JSP.
 * 
 * @version 1.0, 2005-11-17
 */
public class CookieManager {
	private HttpServletResponse response;
	private HttpServletRequest request;

	public CookieManager(HttpServletResponse response,
			HttpServletRequest request) {
		setResponse(response);
		setRequest(request);
	}

	/**
	 * 
	 * @param name
	 *            String
	 * @return Cookie pelo nome ou nulo se não encontra.
	 * @throws ServletException
	 * @throws IOException
	 */
	public Cookie getCookie(String name) throws ServletException, IOException {
		Cookie[] cookies = request.getCookies();
		Cookie c = null;

		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				if (cookies[i].getName().equals(name)) {
					c = cookies[i];
					break;
				}
			}
		}

		return c;
	}

	public void addCookie(Cookie c) {
		response.addCookie(c);
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
}
