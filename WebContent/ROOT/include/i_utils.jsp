<%@ page import = "java.util.*"%>
<%@ page import = "java.io.IOException"%>
<%@ page import = "utilsLib.jsp.*"%>
<%@ page import = "utilsLib.util.Utils"%>
<%@ page import = "utilsLib.*"%>
<%@ page import = "system.*"%>
<%@ page import = "crawler.*"%>
<%@ page import = "com.oreilly.servlet.multipart.Part" %>
<%@ page import = "com.oreilly.servlet.multipart.FilePart" %>
<%@ page import = "com.oreilly.servlet.multipart.ParamPart" %>
<%@ page import = "com.oreilly.servlet.multipart.MultipartParser" %>
<%@ page import = "java.io.*" %>
<%@page import="org.jsoup.safety.Whitelist"%>
<%@page import="org.jsoup.Jsoup"%>
<%
	// correção malassombrada do charset
	//request.setCharacterEncoding("UTF-8");

	JSPUtils jspUtils = new JSPUtils(request, response, out);

	Super superClass = (Super)session.getAttribute("base");
	if (superClass == null) {
		
		superClass = new Super();
		session.setAttribute("base", superClass);
	}

	Facade fac = superClass.getFacade();
%>