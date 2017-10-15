<%@ include file="i_utils.jsp"%>
<%
	out.clearBuffer();
	response.setHeader("pragma", "no-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setHeader("Expires", "0");
%>	
<%
	if (/*fac.getLoggedUser() == null*/ true) {
		jspUtils.printAndGo("Você não está logado.", "/index.jsp");
		return;
	}
%>
