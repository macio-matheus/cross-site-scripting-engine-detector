<%@ include file="/include/i_utils.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><!-- InstanceBegin template="/Templates/template.dwt" codeOutsideHTMLIsLocked="false" -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<!-- InstanceBeginEditable name="doctitle" -->
<title>AnalyseXSS</title>
<!-- InstanceEndEditable -->
<link href="css/style.css" rel="stylesheet" type="text/css" />
<!-- InstanceBeginEditable name="head" -->
<script type="text/javascript" src="js/jquery-2.1.1.min.js"></script>
<script type="text/javascript" src="js/indexJs.jsp"></script>
<!-- InstanceEndEditable -->
</head>

<body>
<span id="top"></span>
<div id="templatemo_body_wrapper">
  <div id="templatemo_wrapper">
    <div id="templatemo_header">
      <div id="site_title">
       
      </div>
      <div id="templatemo_menu">
        <ul>
          <li><a href="index.jsp">Home</a></li>
          <li class="last"><a href="index-abaut.jsp">Sobre</a></li>
        </ul>
      </div>
      <!-- end of templatemo_menu --> 
    </div>
    <!-- end of header -->
    
    <div id="templatemo_main">
      <div id="home" class="content_top"></div>
      <div class="content_box">
        <div class="content_title content_ctf">
          <h2 align="center">Analyse XSS <br><i style="font-size:12px;   margin-left: 147px;">spider web</i></h2>
        </div>
        <div class="content"> 
		<!-- InstanceBeginEditable name="corpo" --> 
        <div class="container_input">
        	<input type="url" placeholder="Informe o dom&iacute;nio que deseja scanear. Ex: http://www.dominio.com.br" id="urlScan" />
             <br />
            <br />
            <a href="#" id="#btn_start" onclick="btClick()" class="btn btn-default">Escanear</a>
            <br /><br />
            <table width="60%" style="display:none;" class="table table-bordered">
        <thead>
          <tr>
            <th>Qtd. campos</th>
            <th>Qtd. Forms</th>
            <th>Qtd. Responses</th>
            <th>Qtd. Urls</th>
          </tr>
        </thead>
        <tbody id="alltrs">
          
        </tbody>
      </table>
        </div>
		<!-- InstanceEndEditable -->
          <div class="cleaner"></div>
        </div>
        <div class="content_bottom content_cbf"><a href="#templatemo_header" class="gototop">Go To Top</a>
        </div>
      </div>
    </div>
    <div id="templatemo_footer"> Copyright © 2015 Your Company Name
      <div class="cleaner"></div>
    </div>
  </div>
  <!-- end of warpper --> 
</div>
<!-- end of body wrapper -->
</body>
<!-- InstanceEnd --></html>
