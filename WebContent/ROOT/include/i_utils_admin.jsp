<%@ page import = "java.util.*"%>
<%@ page import = "java.io.File"%>
<%@ page import = "utilsLib.*"%>
<%@ page import = "utilsLib.jsp.*"%>
<%@ page import = "utilsLib.util.*"%>
<%@ page import = "utilsLib.*"%>
<%@ page import = "system.*"%>
<%@ page import = "crawler.*"%>
<%
	JSPUtils jspUtils = new JSPUtils(request, response, out);

	Super superClass = (Super)session.getAttribute("base");
	if (superClass == null) {
		
		superClass = new Super();
		session.setAttribute("base", superClass);
	}

	Facade facade = superClass.getFacade();
%>

