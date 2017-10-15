<%@ page import = "java.util.*" %>
<%
	Cookie cookie = new Cookie("email", request.getParameter("seu_email"));
    // ...
    response.addCookie(cookie);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>XSS</title>
</head>

<body>
<div align="center">
  <h1> Lista de coment&aacute;rios</h1>		
  <p>Ola, suponha que isso &eacute; uma mensagem num post de um f&oacute;rum de tecnologia!</p>
  <p>-------------------------------------------------------------------------------------------------</p>
  <p><%=request.getParameter("sua_mensagem") %></p>
  <p>-------------------------------------------------------------------------------------------------</p>
  <p>Ola, suponha que isso &eacute; uma mensagem num post de um f&oacute;rum de tecnologia!</p>
  <p>-------------------------------------------------------------------------------------------------</p>
  
</div>

</body>
</html>
