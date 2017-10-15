<%
	out.clearBuffer();
	response.setHeader("pragma", "no-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setHeader("Expires", "0");
%>	
<%
	if (session.getAttribute("link.admin") == null) {
		response.sendRedirect("index.jsp");
		return;
	}
%>
