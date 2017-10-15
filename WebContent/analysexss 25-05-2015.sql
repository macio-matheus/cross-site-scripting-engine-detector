# Host: localhost  (Version: 5.1.33-community)
# Date: 2015-05-25 10:27:54
# Generator: MySQL-Front 5.3  (Build 4.214)

/*!40101 SET NAMES latin1 */;

#
# Structure for table "a_analise"
#

DROP TABLE IF EXISTS `a_analise`;
CREATE TABLE `a_analise` (
  `ID_ANALISE` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `DOMINIO_OU_URL` varchar(255) DEFAULT NULL,
  `DATA_INICIO` datetime DEFAULT NULL,
  `MODO_CRAWLER` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`ID_ANALISE`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

#
# Data for table "a_analise"
#

INSERT INTO `a_analise` VALUES (4,'http://localhost:8080/index-xss-locator.html','2015-05-19 13:41:11',1);

#
# Structure for table "a_campo"
#

DROP TABLE IF EXISTS `a_campo`;
CREATE TABLE `a_campo` (
  `ID_CAMPO` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ID_CODIGO_XSS` int(10) unsigned NOT NULL,
  `ID_FORMULARIO` int(10) unsigned NOT NULL,
  `NOME` varchar(255) DEFAULT NULL,
  `TIPO` varchar(15) DEFAULT NULL,
  `VALOR` text,
  `ATTR_ID` varchar(255) DEFAULT NULL,
  `TIPO_SUGERIDO` tinyint(2) unsigned DEFAULT NULL,
  PRIMARY KEY (`ID_CAMPO`),
  KEY `A_CAMPO_FKIndex1` (`ID_FORMULARIO`),
  KEY `A_CAMPO_FKIndex2` (`ID_CODIGO_XSS`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

#
# Data for table "a_campo"
#

INSERT INTO `a_campo` VALUES (1,0,1,'seu_email','text','','',3),(2,0,1,'sua_mensagem','textarea','','',0),(3,0,2,'seu_email','text','','',3),(4,0,2,'sua_mensagem','textarea','','',0);

#
# Structure for table "a_codigo_xss"
#

DROP TABLE IF EXISTS `a_codigo_xss`;
CREATE TABLE `a_codigo_xss` (
  `ID_CODIGO_XSS` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `NOME` varchar(255) DEFAULT NULL,
  `CODIGO` text,
  `SAIDA` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID_CODIGO_XSS`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

#
# Data for table "a_codigo_xss"
#

INSERT INTO `a_codigo_xss` VALUES (1,'XSS Locator','\';alert(String.fromCharCode(88,83,83))//\';alert(String.fromCharCode(88,83,83))//\";\r\nalert(String.fromCharCode(88,83,83))//\";alert(String.fromCharCode(88,83,83))//--\r\n></SCRIPT>\">\'><SCRIPT>alert(String.fromCharCode(88,83,83))</SCRIPT>','XSS'),(2,'XSS Locator 2','\'\';!--\"<XSS>=&{()}','<XSS verses &lt;XSS'),(3,'Image XSS using the JavaScript directive','<IMG SRC=\"javascript:alert(\'XSS\' + (1+1));\">','XSS2'),(4,'No quotes and no semicolon','<IMG SRC=javascript:alert(\'XSS\' + (2*3))>','XSS6');

#
# Structure for table "a_formulario"
#

DROP TABLE IF EXISTS `a_formulario`;
CREATE TABLE `a_formulario` (
  `ID_FORMULARIO` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ID_ANALISE` int(10) unsigned NOT NULL,
  `ACTION_FORM` varchar(255) DEFAULT NULL,
  `METODO` varchar(4) DEFAULT NULL,
  `URL` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID_FORMULARIO`),
  KEY `A_FORMULARIO_FKIndex1` (`ID_ANALISE`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

#
# Data for table "a_formulario"
#

INSERT INTO `a_formulario` VALUES (1,4,'http://192.168.25.4:8080/index-xss-locator.jsp','post',''),(2,4,'http://192.168.25.4:8080/index-xss-locator.jsp','post','');

#
# Structure for table "a_resposta_formulario"
#

DROP TABLE IF EXISTS `a_resposta_formulario`;
CREATE TABLE `a_resposta_formulario` (
  `ID_RESPOSTA` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ID_CAMPO` int(10) unsigned NOT NULL DEFAULT '0',
  `ID_CODIGO_XSS` int(10) unsigned NOT NULL,
  `ID_FORMULARIO` int(10) unsigned NOT NULL,
  `RESPOSTA` text,
  `STATUS_SERVIDOR` varchar(10) DEFAULT NULL,
  `RESULTADO` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`ID_RESPOSTA`),
  KEY `A_RESPOSTA_FORMULARIO_FKIndex1` (`ID_FORMULARIO`),
  KEY `A_RESPOSTA_FORMULARIO_FKIndex2` (`ID_CAMPO`),
  KEY `A_RESPOSTA_FORMULARIO_FKIndex3` (`ID_CODIGO_XSS`),
  KEY `A_RESPOSTA_FORMULARIO_FKIndex4` (`ID_CAMPO`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

#
# Data for table "a_resposta_formulario"
#

INSERT INTO `a_resposta_formulario` VALUES (1,3,2,2,'\r\n\r\n<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\r\n<html xmlns=\"http://www.w3.org/1999/xhtml\">\r\n<head>\r\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\r\n<title>XSS</title>\r\n</head>\r\n\r\n<body>\r\n<div align=\"center\">\r\n  <h1> Lista de coment&aacute;rios</h1>\t\t\r\n  <p>Ola, suponha que isso &eacute; uma mensagem num post de um f&oacute;rum de tecnologia!</p>\r\n  <p>-------------------------------------------------------------------------------------------------</p>\r\n  <p>15068</p>\r\n  <p>-------------------------------------------------------------------------------------------------</p>\r\n  <p>Ola, suponha que isso &eacute; uma mensagem num post de um f&oacute;rum de tecnologia!</p>\r\n  <p>-------------------------------------------------------------------------------------------------</p>\r\n  \r\n</div>\r\n\r\n</body>\r\n</html>\r\n','200',0);

#
# Structure for table "a_script_detectado"
#

DROP TABLE IF EXISTS `a_script_detectado`;
CREATE TABLE `a_script_detectado` (
  `ID_SCRIPT_DETECTADO` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ID_RESPOSTA` int(10) unsigned NOT NULL,
  `TIPO` tinyint(3) unsigned DEFAULT NULL,
  PRIMARY KEY (`ID_SCRIPT_DETECTADO`),
  KEY `A_SCRIPT_DETECTADO_FKIndex1` (`ID_RESPOSTA`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

#
# Data for table "a_script_detectado"
#


#
# Structure for table "a_url"
#

DROP TABLE IF EXISTS `a_url`;
CREATE TABLE `a_url` (
  `ID_URL` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ID_RESPOSTA` int(10) unsigned DEFAULT NULL,
  `ID_FORMULARIO` int(10) unsigned DEFAULT NULL,
  `NOVA_URL` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID_URL`),
  KEY `A_URLS_FKIndex1` (`ID_FORMULARIO`),
  KEY `A_URL_FKIndex2` (`ID_RESPOSTA`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

#
# Data for table "a_url"
#

