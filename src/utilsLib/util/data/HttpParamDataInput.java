package utilsLib.util.data;

import java.util.Iterator;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;

import utilsLib.util.Utils;
import utilsLib.util.data.RequestHttpIterator;

/**
 * Implementa a leitura parametrizada de dados de um request de HTTP, por meio
 * de {@link ParamDataInput}.
 * 
 * @version 1.0, 2002/02/06
 */
public class HttpParamDataInput extends ParamDataInput {
	private HttpServletRequest request; // request associado.

	/**
	 * Recebe o request de servlet para obtenção parametrizada de valores.
	 * Deve-se ter precaução em realação a se a instância do request continua
	 * válida, ja que ela é renovada a cada chamada de página.
	 * 
	 * @param request
	 *            Interface de request do Servlet.156
	 */
	public HttpParamDataInput(String extra, HttpServletRequest request) {
		super(extra);
		if (request == null) {
			throw new IllegalArgumentException("[DBParamDataInput] Request"
					+ " nulo.");
		}

		this.request = request;
	}

	public double getDouble(String paramName) {
		String value = getStr(paramName);

		if (value == null) {
			return Utils.UNDEFINED_NUMBER;
		}

		return Double.parseDouble(value);
	}

	public int getInt(String paramName) {
		Object o = request.getParameter(paramName);
		return Utils.parseInt(o + "", Utils.UNDEFINED_NUMBER);
	}

	public boolean getBoo(String paramName) {
		// Recupera o parametro.
		Object o = request.getParameter(paramName);
		String oValue = "";

		if (o != null) {
			oValue = o.toString();
		}

		return (oValue.equals("true") || oValue.equals("1") || oValue
				.equals("on")) ? true : false;
	}

	public String getStr(String paramName) {
		return request.getParameter(paramName);
	}

	public Object getObject(String paramName) {
		return request.getParameter(paramName);
	}

	public Date getDate(String paramName, String pattern) {
		return Utils.objToDate(request.getParameter(paramName), pattern);
	}

	public Iterator getParams() {
		return new RequestHttpIterator(request);
	}
}
