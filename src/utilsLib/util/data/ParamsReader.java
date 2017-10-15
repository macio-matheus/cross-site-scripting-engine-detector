package utilsLib.util.data;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;

import utilsLib.jsp.JSPUtils;
import utilsLib.util.Utils;




/**
 * Leitura parametrizada de dados de um request de HTTP. Dispara exceções na não
 * correção dos parametros.
 *
 * Geralmente, para cada tipo de dado, existe um método que chama apenas
 * passando o nome e outro com o valor padrão. O que apenas chama, caso seja
 * inexistente ou haja algum erro na conversão do dado, dispara exceção
 * IllegalArgumentException, colocarn do ERROR_PATTERN.
 *
 * @author Leonardo Rodrigues
 * @version 1.0, 2004/10/11
 */
public class ParamsReader {
    public static String ERROR_PATTERN = "Param: ";

    private HttpServletRequest request; // request associado.

    public ParamsReader(HttpServletRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("[ParamsReader] Request"
                                               + " nulo.");
        }

        this.request = request;
    }

    public double getDouble(String paramName, double errorValue) {
        return Utils.parseDouble(request.getParameter(paramName), errorValue);
    }

    public double getDouble(String paramName) {
        return Utils.parseDouble(request.getParameter(paramName));
    }

    public int getInt(String paramName, int errorValue) {
        return Utils.parseInt(request.getParameter(paramName), errorValue);
    }

    public int getInt(String paramName) {
        return Utils.parseInt(request.getParameter(paramName));
    }

    public boolean getBool(String paramName) {
        return Utils.toBoo(request.getParameter(paramName));
    }

    public String getStr(String paramName) {
        return request.getParameter(paramName);
    }

    public String getStr(String paramName, String alternative) {
        return Utils.formatIfNull(request.getParameter(paramName), alternative);
    }

    public Date getDate(String paramName, String pattern) {
        return Utils.strToDate(request.getParameter(paramName), pattern);
    }

    public Date getDate(String paramName) {
        return Utils.strToDate(request.getParameter(paramName));
    }

    public Date getDate(String paramName, Date alternative) {
    	Date date = null;
    	
        try {
            date = Utils.strToDate(request.getParameter(paramName));
        } catch (Exception e) {
            date = alternative;
        }
        
        // qd é nulo, não dá erro o Utils.strToDate, mas retorna nulo. Então isso aq resolve este problema para alternative.
        if (date == null) {
			date = alternative;
		}
        
        return date;
    }

    public Date getDateTime(String paramName, Date alternative) {
        try {
            return Utils.strToDateTime(request.getParameter(paramName));
        } catch (Exception e) {
            return alternative;
        }
    }

    public Date getDateTime(String paramName) {
        return Utils.strToDateTime(request.getParameter(paramName));
    }

    public String[] getStrs(String paramName) {
        return request.getParameterValues(paramName);
    }

    public String[] getStrs(String paramName, String[] alternative) {
        String[] strs = request.getParameterValues(paramName);

        if (strs == null) {
            strs = alternative;
        }

        return strs;
    }
}
