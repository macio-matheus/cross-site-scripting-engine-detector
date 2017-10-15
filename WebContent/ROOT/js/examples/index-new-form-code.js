//< % @ include file="/include/i_utils.jsp" %>
//<%@ include file="/include/i_logon_control.jsp" %>
//<%@ page contentType="text/html; charset=utf-8" language="java" %>
//<%
//	int ID_FORM_CONTACT = jspUtils.getInt("i", Utils.UNDEFINED_NUMBER);
//    boolean IS_EDITION = jspUtils.getBool("edt", true);
//%>
/**
*	Js da pagina index-new-form-code.jsp
*	@author: Mácio Matheus
*/ 
$(function (){	

	// Constants
	//ID_FORM_CONTACT = <%=ID_FORM_CONTACT%> // id form contact
	JSON_DEFAULT = ""; // Json padrão
	JSON_FORM_CONTACT = ""; // Json do formulário corrente
	//IS_EDITION = <%=IS_EDITION%>

	// Carrega os jsons
	loadDefaultJson ();
	loadFormContactJson ();

	// Função que alerta caso usuario pressione F5
	window.onkeydown = function (e) {
		if (true) { return true; }
		if (e.keyCode === 116) {
			var r = confirm("Ao atualizar esta página, todas as configurações não salvas, serão perdidas. Deseja continuar?");
			if (r == false) {
				// e.keyCode = 0; e.returnValue = false;
				return false;
			}
		}
	}
	// end function f5

	// Preenche os textarea com os codigos
	getIframe ();
	getPopup ();
	getCodeFull ();

	// Reseta sempre as configurações do esquema de obter codigo
	$("#iframe").prop("checked", true);
	$("#popUp").prop("checked", false);
	$("#codefull").prop("checked", false);

	var iframe = $("#codeFormIframe").text();
	var popup = $("#codeFormPopUp").text();
	var full = $("#codeFormFull").text();

	$("#codeFormIframe").text(iframe.trim());
	$("#codeFormPopUp").text(popup.trim());
	$("#codeFormFull").text(full.trim());

	$("#codeFormIframe").css("display", "block");
	$("#codeFormPopUp").css("display", "none");
	$("#codeFormFull").css("display", "none");
	// ---------------------------------------------------------

	// Se os json forem iguais, indica que é a primeira vez que o formulário
	// é aberto, então seta o botão prata
	if (compareJsonEquals(JSON_FORM_CONTACT, JSON_DEFAULT)) {
		console.log("entrou no if");

		// reseta padrão PRATA definido por francisco
	    //resetButtonDefault (false);
	}
});



function loadDefaultJson () {
	$.getJSON("../style_default_form.json", function (json) {
      	JSON_DEFAULT = json;
	});
}

function loadFormContactJson () {
	$.ajax({
		type: 'POST',
		dataType: 'JSON',
		url: '/proc.jsp',
		data: {actionID: 19, idFormContact: ID_FORM_CONTACT},
		success: function(jsonStyle) {
			JSON_FORM_CONTACT = jsonStyle;
		}
	});	
}

// Exibe div de configuração do campo
function hideBoxConfig (idDiv) {
	$(idDiv).find('.modal').slideUp(function () {
		$(idDiv).hide();
	});
}

// Form -------------------------------------------------

// Exibe div de configuração do campo
function showBoxConfigForm (idDiv) {

	$.each(JSON_FORM_CONTACT, function (count, currClass) {

		if (currClass.NAME_CLASS == "l_formulario") {

			// Padding
			$("#f_padding").val(currClass.PROPS[1].padding_bottom.replace("px", ""));
			
			// cor de fundo do form
			var background_color = currClass.PROPS[0].background_color;
			background_color = background_color.replace(" !important", "");
			background_color = background_color.replace("#", "");
			$("#f_color").val(background_color);
			$("#f_color").css("background", "#" + background_color);

			// Largura do formulário, faz o calculo inverso do paranauê
			var width_total = calculateWidthFormInverse (currClass.PROPS[9].width, currClass.PROPS[1].padding_bottom, currClass.PROPS[5].border);
			$("#f_width").val(width_total);

			// Define a borda como reta/continua
			if (currClass.PROPS[8].border_radius == "0px" && currClass.PROPS[5].border != "0px") {
				
				$("#bordaform").css("display", "block");
				$("#f_border_type_cont").prop("checked", true);

			// Define como abaulada
			} else if (currClass.PROPS[8].border_radius != "0px" && currClass.PROPS[5].border != "0px") {

				$("#bordaform").css("display", "block");
				$("#f_border_type_abau").prop("checked", true);
			
			// Define como nenhuma
			} else if (currClass.PROPS[5].border == "0px") {

				$("#f_border_type_nenh").prop("checked", true);
				$("#bordaform").css("display", "none");
			}

			if (currClass.PROPS[5].border != "0px") {

				$("#f_border").val(currClass.PROPS[5].border.replace("px", ""));
				$("#f_border_color").val(currClass.PROPS[7].border_color.replace("#", ""));
				$("#f_border_color").css("background", currClass.PROPS[7].border_color);

				$("#f_border_style").val(currClass.PROPS[6].border_style);
			}
		}
	});

	$(idDiv).show();
	$(idDiv).find('.modal').slideDown();
}

// Reseta a classe form na forma default
function resetFormDefault () {
	var PROPS = "";
	$.each(JSON_DEFAULT, function (count, currClass) {	
		if (currClass.NAME_CLASS == "l_formulario") {

			PROPS = currClass.PROPS;
		}
	});

	$.each(JSON_FORM_CONTACT, function (count, currClass) {	
		if (currClass.NAME_CLASS == "l_formulario") {
			currClass.PROPS = PROPS;
		}
	});

	saveJson("#modalForm");

}

// Seta as configurações da classe de formulário
function setFormJson () {

	var BG_COLOR_DEFAULT = "";
	var WIDTH_DEFAULT = "";
	var PADDING_DEFAULT = "";
	var BORDER_RADIUS_DEFAULT = "10px";
	var BORDER_COLOR_DEFAULT = "";
	var BORDER_DEFAULT = "1px";
	var BORDER_STYLE_DEFAULT = "";
	var BORDER_TYPE_DEFAULT = "";

	var padding = $("#f_padding").val();
	var bg_color = $("#f_color").val();
	var width = $("#f_width").val();
	var border_type = $("#f_border_type").val();
	var border_color = $("#f_border_color").val();
	var border = $("#f_border").val() + "px";
	var border_style = $("#f_border_style").val();

	// Proteção para caso algum valor venha vazio
	padding = ((padding == "")? PADDING_DEFAULT : padding);
	bg_color = ((bg_color == "")? BG_COLOR_DEFAULT : bg_color);
	width = ((width == "")? WIDTH_DEFAULT : width);
	border_type = ((border_type == "")? BORDER_TYPE_DEFAULT : border_type);
	border_color = ((border_color == "")? BORDER_COLOR_DEFAULT : border_color);
	border = ((border == "px")? BORDER_DEFAULT : border);
	border_style = ((border_style == "")? BORDER_STYLE_DEFAULT : border_style);

	$.each(JSON_FORM_CONTACT, function (count, currClass) {
		if (currClass.NAME_CLASS == "l_formulario") {
			
			// definindo os estilos, caso tenha sido escolhida abaulada ou reta
			if (border_type != "none") {

				if (border_type == "solid") {
					currClass.PROPS[8].border_radius = "0px";
				} else if (border_type == "radius") {
					currClass.PROPS[8].border_radius = BORDER_RADIUS_DEFAULT;
				}

				currClass.PROPS[1].padding_bottom = padding + "px";
				currClass.PROPS[2].padding_left = padding + "px";
				currClass.PROPS[4].padding_top = padding + "px";

				// ***
				var padding_form = "";
				$.each(JSON_FORM_CONTACT, function (count, ccClass) {	
					if (ccClass.NAME_CLASS == "l_formulario") {
						padding_form = ccClass.PROPS[1].padding_bottom;
					}
				});

				// Retirar o tamanho do titulo e deixar a margem do tamanho da
				// margem do formulário
				$.each(JSON_FORM_CONTACT, function (count, ccClass) {	
					if (ccClass.NAME_CLASS == "l_div_conteudo") {
						ccClass.PROPS[1].margin_right = padding_form;
					}
				}); 

				// Retirar o tamanho do titulo e deixar a margem do tamanho da
				// margem do formulário
				$.each(JSON_FORM_CONTACT, function (count, ccClass) {	
					if (ccClass.NAME_CLASS == "l_div_botao") {
						ccClass.PROPS[1].margin_right = padding_form;
					}
				}); 

				currClass.PROPS[6].border_style = border_style;

				currClass.PROPS[0].background_color =  "#" + bg_color;

				currClass.PROPS[7].border_color = "#" + border_color;
				currClass.PROPS[5].border = border;

				// Calculando a largura do form, subtraindo o padding
				currClass.PROPS[9].width = calculateWidthForm (width, padding_form, border);

				// Retirar o tamanho do titulo e deixar a margem do tamanho da
				// margem do formulário

				// pega o padding do input
				var input_padding = "";
				var input_border = "";
				$.each(JSON_FORM_CONTACT, function (count, ccClass) {	
					if (ccClass.NAME_CLASS == "l_campo") {
						input_padding = ccClass.PROPS[4].padding_bottom;
						input_border = ccClass.PROPS[8].border;
					}
				}); 
			
			// Se foi escolhida a opção NENHUMA	
			}  else if (border_type == "none") {
				currClass.PROPS[5].border = "0px";
			}
		}
	});

	saveJson ("#modalForm");
}


// Calcula o width subtraindo o padding,
// ex: width 10 e padding 3 então width real == 7
function calculateWidthForm (width, padding, border) {
	
	if (padding == "" || padding == null) {
		padding = "0";
	}
	
	// parse int
	width = parseInt(width);
	padding = parseInt(padding);
	border = parseInt(border);

	return (width - padding - (border * 2)) + "px";

}

// Calcula o width subtraindo o padding,
// ex: width 10 e padding 3 então width real == 7
function calculateWidthFormInverse (width, padding, border) {
	
	if (padding == "" || padding == null) {
		padding = "0";
	}
	
	// parse int
	width = width.replace("px", "");
	padding = padding.replace("px", "");
	border = border.replace("px", "");

	// parse int
	width = width.replace(" !important", "");
	padding = padding.replace(" !important", "");
	border = border.replace(" !important", "");

	// parse int
	width = parseInt(width);
	padding = parseInt(padding);
	border = parseInt(border);

	return (width + padding + (border * 2));
}


// Seta o tipo da borda
function setFormTypeBorder (element) {

	var type = $(element).val();
	
	if (type == "solid" || type == "radius" || type == "none") {
		
		// Oculta ou mostra as configurações dependendo o 
		// tipo escolhido
		if (type == "none") {
			$("#bordaform").css("display", "none");
		} else {
			$("#bordaform").css("display", "block");
		}

		$("#f_border_type").val(type);

	} else {
		$("#f_border_type").val("solid");
	}
}
// ---------------------------------------------

// TITULO --------------------------------------

// Exibe div de configuração do titulo
function showBoxConfigTitle (idDiv) {

	var FONT_SIZE_P = "12px !important";
	var FONT_SIZE_M = "14px !important";
	var FONT_SIZE_G = "16px !important";
 		
	$.each(JSON_FORM_CONTACT, function (count, currClass) {

		if (currClass.NAME_CLASS == "l_titulo") {

			$(".t_btn_size").each(function () {
				$(this).addClass("btn-default");
			});

			// tamanho da fonte
			if (currClass.PROPS[0].font_size == FONT_SIZE_P) {
				$("#t_p").removeClass("btn-default");
			} else if (currClass.PROPS[0].font_size == FONT_SIZE_M) {
				$("#t_m").removeClass("btn-default");
			} else if (currClass.PROPS[0].font_size == FONT_SIZE_G) {
				$("#t_g").removeClass("btn-default");
			}

			$("#t_font_size").val(currClass.PROPS[0].font_size);
			// -------------------------------------------------

			// Indica se é negrito ou não
			if (currClass.PROPS[2].font_weight == "bold") {
				$("#t_btnIsBold").removeClass("btn-default");
				$("#t_isBold").val(true);	
			} else {
				$("#t_btnIsBold").addClass("btn-default");
				$("#t_isBold").val(false);
			}

			// Indica se é italico ou não
			if (currClass.PROPS[3].font_style == "italic") {
				$("#t_btnIsItalic").removeClass("btn-default");
				$("#t_isItalic").val(true);
			} else {
				$("#t_btnIsItalic").addClass("btn-default");
				$("#t_isItalic").val(false);
			}

			$("#t_font_family").val(currClass.PROPS[1].font_family);

			var font_color = currClass.PROPS[5].color;
			font_color = font_color.replace(" !important", "");
			font_color = font_color.replace("#", "");
			$("#t_color").val(font_color);
			$("#t_color").css("background", "#" + font_color);
		}
	});
			
	$(idDiv).show();
	$(idDiv).find('.modal').slideDown();
}

// Reseta o titulo na forma default
function resetTitleDefault () {
	var PROPS = "";
	$.each(JSON_DEFAULT, function (count, currClass) {	
		if (currClass.NAME_CLASS == "l_titulo") {

			PROPS = currClass.PROPS;
		}
	});

	$.each(JSON_FORM_CONTACT, function (count, currClass) {	
		if (currClass.NAME_CLASS == "l_titulo") {
			currClass.PROPS = PROPS;
		}
	});

	saveJson("#modalTitle");

}

// Seta a configuração do titulo do css
function setTitleJson () {
	var FONT_SIZE_DEFAULT = "";
	var FONT_WEIGHT_DEFAULT = "";
	var FONT_STYLE_DEFAULT = "";
	var FONT_FAMILY_DEFAULT = "";
	var COLOR_DEFAULT = "";

	var font_size = $("#t_font_size").val();
	var font_weight = $("#t_isBold").val();
	var font_style = $("#t_isItalic").val();
	var font_family = $("#t_font_family").val();
	var color = $("#t_color").val();

	font_size = ((font_size == "")? FONT_SIZE_DEFAULT : font_size);
	font_weight = ((font_weight == "")? FONT_WEIGHT_DEFAULT : font_weight);;
	font_style = ((font_style == "")? FONT_STYLE_DEFAULT : font_style);
	font_family = ((font_family == "")? FONT_FAMILY_DEFAULT : font_family);
	color = ((color == "")? COLOR_DEFAULT : color);

	$.each(JSON_FORM_CONTACT, function (count, currClass) {	
		if (currClass.NAME_CLASS == "l_titulo") {

			currClass.PROPS[1].font_family = font_family;

	  		if (font_size > "0") {
	  			currClass.PROPS[0].font_size = font_size;
	  		}

	  		// negrito
	  		if (font_weight == "true") {
	  			currClass.PROPS[2].font_weight = "bold";
	  		} else {
	  			currClass.PROPS[2].font_weight = "initial";
	  		}

	  		// Italico
	  		if (font_style == "true") {
	  			currClass.PROPS[3].font_style = "italic";
	  		} else {
	  			currClass.PROPS[3].font_style = "initial";
	  		}

	  		currClass.PROPS[5].color = "#" + color + " !important";
  		}
	});

	saveJson ("#modalTitle");
}

// Seta o tamanho da fonte
function setTitleFontSize (element) {
	
	$(".t_btn_font_size").each(function () {
		$(this).addClass("btn-default");
	});

	$(element).removeClass("btn-default");

	$("#t_font_size").val($(element).attr("propval"));
}

// Seta como negrito
function setTitleFontBold () {
	
	if ($("#t_isBold").val() == "true") {

		$("#t_btnIsBold").addClass("btn-default");
		$("#t_isBold").val(false);
	} else {

		$("#t_btnIsBold").removeClass("btn-default");
		$("#t_isBold").val(true);
	}
}

// Seta como italico
function setTitleFontItalic () {
	
	if ($("#t_isItalic").val() == "true") {

		$("#t_btnIsItalic").addClass("btn-default");
		$("#t_isItalic").val(false);
	} else {

		$("#t_btnIsItalic").removeClass("btn-default");
		$("#t_isItalic").val(true);
	}
}

// ------------------------------------------------------

// CAMPOS -----------------------------------------------

// Exibe div de configuração do campo
function showBoxConfigInput (idDiv) {

	var FONT_SIZE_P = "12px !important";
	var FONT_SIZE_M = "14px !important";
	var FONT_SIZE_G = "16px !important";
 		
	$.each(JSON_FORM_CONTACT, function (count, currClass) {

		if (currClass.NAME_CLASS == "l_campo") {

			$(".i_btn_size").each(function () {
				$(this).addClass("btn-default");
			});

			// tamanho da fonte
			if (currClass.PROPS[1].font_size == FONT_SIZE_P) {
				$("#i_p").removeClass("btn-default");
			} else if (currClass.PROPS[1].font_size == FONT_SIZE_M) {
				$("#i_m").removeClass("btn-default");
			} else if (currClass.PROPS[1].font_size == FONT_SIZE_G) {
				$("#i_g").removeClass("btn-default");
			}

			// Tamanho da fonte
			$("#i_font_size").val(currClass.PROPS[1].font_size);

			// Cor da fonte
			var font_color = currClass.PROPS[2].color;

			font_color = font_color.replace("#", "");
			$("#i_color").val(font_color);
			$("#i_color").css("background", "#" + font_color);

			// Indica se é negrito ou não
			if (currClass.PROPS[12].font_weight == "bold") {
				$("#i_btnIsBold").removeClass("btn-default");
				$("#i_isBold").val(true);	
			} else {
				$("#i_btnIsBold").addClass("btn-default");
				$("#i_isBold").val(false);
			}

			// Indica se é italico ou não
			if (currClass.PROPS[13].font_style == "italic") {
				$("#i_btnIsItalic").removeClass("btn-default");
				$("#i_isItalic").val(true);
			} else {
				$("#i_btnIsItalic").addClass("btn-default");
				$("#i_isItalic").val(false);
			}
			
			// estilo da fonte
			$("#i_font_family").val(currClass.PROPS[0].font_family);

			// Define a borda como reta/continua
			if (currClass.PROPS[11].border_radius == "0px !important") {
				
				$("#i_border_type_cont").prop("checked", true);

			// Define como abaulada
			} else if (currClass.PROPS[11].border_radius != "0px !important" && currClass.PROPS[8].border != "0px") {

				$("#i_border_type_abau").prop("checked", true);
			}

			var bg_color = currClass.PROPS[3].background; 
			bg_color = bg_color.replace(" !important", "");
			bg_color = bg_color.replace("#", "");
			$("#i_bgcolor").val(bg_color);
			$("#i_bgcolor").css("background", "#" + bg_color);

			// Estilos da borda
			var border_color = currClass.PROPS[9].border_color.replace("#", "");
			$("#i_border").val(currClass.PROPS[8].border.replace("px", ""));
			border_color = border_color.replace(" !important", "");
			$("#i_border_color").val(border_color);
			$("#i_border_color").css("background", "#" + border_color);
			
			$("#i_padding").val(currClass.PROPS[4].padding_bottom.replace("px", ""))
			
			// Seta a margem bottom, pegando de outra classe
			$.each(JSON_FORM_CONTACT, function(count, cClass) {	
				if (cClass.NAME_CLASS == "l_div_conteudo") {
					$("#i_margin_bottom").val(cClass.PROPS[0].padding_bottom.replace("px", ""));
				}
			});	

			// Alinhamento
			$.each(JSON_FORM_CONTACT, function (count, cClass) {	
				if (cClass.NAME_CLASS == "l_formulario p") {
					
					if (cClass.PROPS[0].float == "none") {
						$("#i_align_float").val("vert");
						$("#i_vert").prop("checked", true);
						$("#i_hori").prop("checked", false);

						// Regra das colunas, vai na classe div conteudo e 
						// seta como float			
						$.each(JSON_FORM_CONTACT, function (count, ccClass) {	
							if (ccClass.NAME_CLASS == "l_div_conteudo") {
				
								// Escolheu opção de coluna			
								if (ccClass.PROPS[2].float == "left") {
									$("#col").prop("checked", true);
									$("#notCol").prop("checked", false);
									$("#isColumn").val(true);
									$("#divCheckBoxCemPorcent").css("display", "none");
								
								// se nao escolheu coluna
								} else {

									$("#col").prop("checked", false);
									$("#notCol").prop("checked", true);
									$("#isColumn").val(false);
								
								}
							}
						});	

						// Seta os campos como 100%, caso o usuário tenha escolhido 
						// a opção de usar.
						$.each(JSON_FORM_CONTACT, function (count, ccClass) {	
							if (ccClass.NAME_CLASS == "l_campo_cem_porcento") {
								if (ccClass.PROPS[0].width == "100% !important") {
									$("#inputCemPorcent").prop("checked", true);
								} else {
									$("#inputCemPorcent").prop("checked", false);
								}
							}
						});

						// Mostra e oculta as divs de configurações
						$("#i_conf_vert").show();
						$("#i_conf_hori").hide();

					} else {

						$("#i_align_float").val("hori");
						$("#i_hori").prop("checked", true);
						$("#i_vert").prop("checked", false);

						// Seta a largura do titulo
						$.each(JSON_FORM_CONTACT, function (count, ccClass) {	
							if (ccClass.NAME_CLASS == "l_titulo") {

								var wdt = ccClass.PROPS[7].width;
					
								if (wdt != "initial" && wdt != "120px !important") {
									wdt = wdt.replace("px", "");
									wdt = wdt.replace(" !important", "");
								} else {
									wdt = "120";
								}	
		
								$("#i_width_title").val(parseInt(wdt));
							}
						});	

						// Mostra e ocul5a as divs de configurações
						$("#i_conf_hori").show();
						$("#i_conf_vert").hide();
					}
				}
			});	
		}
	});

	$(idDiv).css("display", "block");
	$(idDiv).find('.modal').slideDown();
}

// Reseta a classe botão para default
function resetInputDefault () {
	var PROPS = "";
	$.each(JSON_DEFAULT, function (count, cClass) {	
		if (cClass.NAME_CLASS == "l_campo") {

			PROPS = cClass.PROPS;
		}
	});

	$.each(JSON_FORM_CONTACT, function (count, currClass) {	
		if (currClass.NAME_CLASS == "l_campo") {
			currClass.PROPS = PROPS;
		}
	});

	$.each(JSON_DEFAULT, function (count, currClass) {	
		if (currClass.NAME_CLASS == "l_formulario p") {

			PROPS = currClass.PROPS;
		}
	});

	$.each(JSON_FORM_CONTACT, function (count, currClass) {	
		if (currClass.NAME_CLASS == "l_formulario p") {
			currClass.PROPS = PROPS;
		}
	});

	$.each(JSON_DEFAULT, function (count, currClass) {	
		if (currClass.NAME_CLASS == "l_div_conteudo") {

			PROPS = currClass.PROPS;
		}
	});

	$.each(JSON_FORM_CONTACT, function (count, currClass) {	
		if (currClass.NAME_CLASS == "l_div_conteudo") {
			currClass.PROPS = PROPS;
		}
	});

	$.each(JSON_DEFAULT, function (count, currClass) {	
		if (currClass.NAME_CLASS == "l_campo_cem_porcento") {

			PROPS = currClass.PROPS;
		}
	});

	$.each(JSON_FORM_CONTACT, function (count, currClass) {	
		if (currClass.NAME_CLASS == "l_campo_cem_porcento") {
			currClass.PROPS = PROPS;
		}
	});

	saveJson("#modalInput");
}


// Seta a configuração dos campos
function setInputJson () {
	
	var FONT_SIZE_DEFAULT = "";
	var FONT_FAMILY_DEFAULT = "";
	var COLOR_DEFAULT = "";
	var BORDER_TYPE_DEFAULT = "";
	var PADDING_BOTTOM_DEFAULT = "";
	var BORDER_DEFAULT = "1px !important";
	var BORDER_COLOR_DEFAULT = "#CCC !important";
	var BORDER_STYLE_DEFAULT = "solid !important";
	var PADDING_DEFAULT = "4px";
	var ALIGN_DEFAULT = ""; // vert ou hori
	var BORDER_RADIUS_DEFAULT = "6px !important";
	var WIDTH_TITLE_DEFAULT = "100px !important";
	var FONT_WEIGHT_DEFAULT = "initial";
	var FONT_STYLE_DEFAULT = "initial";
	var BACKGROUND_DEFAULT = "#FFF";

	var font_size = $("#i_font_size").val();
	var font_family = $("#i_font_family").val();
	var color = $("#i_color").val();
	var border_type = $("#i_border_type").val();
	var padding_bottom = $("#i_margin_bottom").val();
	var border_style = $("#i_border_style").val();
	var padding = $("#i_padding").val(); 
	var align = $("#i_align_float").val();
	var border_color = $("#i_border_color").val();
	var column  = $("#isColumn").val();
	var isCemPorcent = $("#inputCemPorcent").prop("checked");
	var widthTitle = $("#i_width_title").val();
	var font_weight = $("#i_isBold").val();
	var font_style = $("#i_isItalic").val();
	var background = $("#i_bgcolor").val();

	font_size = ((font_size == "")? FONT_SIZE_DEFAULT : font_size);
	font_family = ((font_family == "")? FONT_FAMILY_DEFAULT : font_family);
	color = ((color == "")? COLOR_DEFAULT : color);
	border_type = ((border_type == "")? BORDER_TYPE_DEFAULT : border_type);
	padding_bottom = ((padding_bottom == "")? PADDING_BOTTOM_DEFAULT : padding_bottom);
	padding = ((padding == "")? PADDING_DEFAULT : padding);
	align = ((align == "")? ALIGN_DEFAULT : align);
	border_color = ((border_color == "")? BORDER_COLOR_DEFAULT : border_color);
	widthTitle = ((widthTitle == "")? WIDTH_TITLE_DEFAULT : widthTitle);
	font_weight = ((font_weight == "")? FONT_WEIGHT_DEFAULT : font_weight);;
	font_style = ((font_style == "")? FONT_STYLE_DEFAULT : font_style);
	background = ((background == "")? BACKGROUND_DEFAULT : background);

	$.each(JSON_FORM_CONTACT, function (count, currClass) {	
		if (currClass.NAME_CLASS == "l_campo") {
	  		
	  		// Setando as regras da fonte
  	  		currClass.PROPS[0].font_family = font_family;
	  		currClass.PROPS[1].font_size = font_size;
	  		currClass.PROPS[2].color = "#" + color; // + " !important";

	  		// negrito
	  		if (font_weight == "true") {
	  			currClass.PROPS[12].font_weight = "bold";
	  		} else {
	  			currClass.PROPS[12].font_weight = "initial";
	  		}

	  		// Italico
	  		if (font_style == "true") {
	  			currClass.PROPS[13].font_style = "italic";
	  		} else {
	  			currClass.PROPS[13].font_style = "initial";
	  		}

	  		// define os paddings
	  		currClass.PROPS[4].padding_bottom = padding + "px";
	  		currClass.PROPS[5].padding_left = padding + "px";
	  		currClass.PROPS[6].padding_right = padding + "px";
	  		currClass.PROPS[7].padding_top = padding + "px";

	  		currClass.PROPS[9].border_color = "#" + border_color + " !important";

	  		// Seta a margem, que fica em uma outra classe
	  		$.each(JSON_FORM_CONTACT, function (count, cClass) {	
				if (cClass.NAME_CLASS == "l_div_conteudo") {
					cClass.PROPS[0].padding_bottom = padding_bottom + "px";
				}
			});			

	  		// Configura a borda
	  		if (border_type == "solid") {
				currClass.PROPS[11].border_radius = "0px !important";
			} else if (border_type == "radius") {
				currClass.PROPS[11].border_radius = BORDER_RADIUS_DEFAULT;
			}

			currClass.PROPS[3].background = "#" + background + " !important"; 

			// Seta o alinhamento e se é em colunas ou não
			setInputAlignFields(align, column, isCemPorcent, widthTitle);
  		}
	});

	saveJson("#modalInput");
}


// Seta o tamanho da fontedo input
function setInputFontSize (element) {
	$(".i_btn_font_size").each(function () {
		$(this).addClass("btn-default");
	});

	$(element).removeClass("btn-default");

	$("#i_font_size").val($(element).attr("propval"));
}

// Seta o tipo da borda
function setInputTypeBorder (element) {
	var type = $(element).val();
	$("#i_border_type").val(type);	
}

// seta a posição de alinhamento
function setInputAlign (element) {
	$("#i_align_float").val($(element).val());

	if ($(element).val() == "vert") {
		$("#i_conf_vert").show();
		$("#i_conf_hori").hide();
	} else {
		$("#i_conf_hori").show();
		$("#i_conf_vert").hide();
	}
}

// Seta como negrito
function setInputFontBold () {
	
	if ($("#i_isBold").val() == "true") {

		$("#i_btnIsBold").addClass("btn-default");
		$("#i_isBold").val(false);
	} else {

		$("#i_btnIsBold").removeClass("btn-default");
		$("#i_isBold").val(true);
	}
}

// Seta como italico
function setInputFontItalic () {
	
	if ($("#i_isItalic").val() == "true") {

		$("#i_btnIsItalic").addClass("btn-default");
		$("#i_isItalic").val(false);
	} else {

		$("#i_btnIsItalic").removeClass("btn-default");
		$("#i_isItalic").val(true);
	}
}


// Seta a escolha do usuário. Se quer
// o formulário horizontal, em colunas ou não
function setInputIsColumn (element) {
	
	if ($(element).val() == "true") {
		$("#isColumn").val($(element).val());	
		$("#inputCemPorcent").prop("checked", false);
		$("#divCheckBoxCemPorcent").css("display", "none");
		
	} else {
		$("#isColumn").val($(element).val());
		$("#inputCemPorcent").prop("disable", false);
		$("#divCheckBoxCemPorcent").css("display", "block");
	}
}

// Seta o align dos campos em relação ao titulo do input
// isColumn: indica se é coluna; isCemPorcent: inidica se é 100% os inputs
function setInputAlignFields (align, isColumn, isCemPorcent, widthTitle) {

	var WIDTH_TITLE_DEFAULT = "initial";

	// Alinhamento dos campos na 
	// modalidade VERTICAL
	if (align == "vert") {
		align = "none";

		// Regra das colunas, vai na classe div conteudo e 
		// seta como float			
		$.each(JSON_FORM_CONTACT, function (count, cClass) {	
			if (cClass.NAME_CLASS == "l_div_conteudo") {
				
				// Escolheu opção de coluna			
				if (isColumn == "true") {
					cClass.PROPS[2].float = "left";
					
					// coloca o clear NONE none na classe quebra linha,
					// assim as colunas se formam
					$.each(JSON_FORM_CONTACT, function (count, ccClass) {	
						if (ccClass.NAME_CLASS == "l_quebra_linha") {
							ccClass.PROPS[0].clear = "none";
						}
					});	

					// Seta os campos como como width INITIAL
					$.each(JSON_FORM_CONTACT, function (count, ccClass) {	
						if (ccClass.NAME_CLASS == "l_campo_cem_porcento") {
							ccClass.PROPS[0].width = "initial";
						}
					});		
		
					// Retirar o tamanho do titulo e deixar a margem do tamanho da
					// margem do formulário
					$.each(JSON_FORM_CONTACT, function (count, ccClass) {	
						if (ccClass.NAME_CLASS == "l_titulo") {
							ccClass.PROPS[7].width = "initial";
						}
					});

				// Caso seja um campo por linha
				} else {

					// float
					cClass.PROPS[2].float = "none";
					
					// coloca o clear both none na classe quebra linha
					$.each(JSON_FORM_CONTACT, function (count, ccClass) {	
						if (ccClass.NAME_CLASS == "l_quebra_linha") {
							ccClass.PROPS[0].clear = "both";
						}
					});	

					// Seta os campos como 100%, caso o usuário tenha escolhido 
					// a opção de usar.
					$.each(JSON_FORM_CONTACT, function (count, ccClass) {	
						if (ccClass.NAME_CLASS == "l_campo_cem_porcento") {
							if (isCemPorcent) {
								ccClass.PROPS[0].width = "100% !important";
							} else {
								ccClass.PROPS[0].width = "initial";
							}
						}
					});
				} // end else
			} // end if	
		});

		// Seta a largura do titulo como initial
		$.each(JSON_FORM_CONTACT, function (count, ccClass) {	
			if (ccClass.NAME_CLASS == "l_titulo") {
				ccClass.PROPS[7].width = WIDTH_TITLE_DEFAULT;
			}
		});		
	
	// Alinhamento dos campos na
	// modalidade HORIZONTAL //
	} else if (align == "hori") {

		// Regra das colunas, vai na classe div conteudo e 
		// seta como float NONE		
		$.each(JSON_FORM_CONTACT, function (count, cClass) {	
			if (cClass.NAME_CLASS == "l_div_conteudo") {
				cClass.PROPS[2].float = "none";
			}
		});

		align = "left";

		// coloca o clear both none na classe quebra linha,
		// assim as colunas se formam
		$.each(JSON_FORM_CONTACT, function (count, ccClass) {	
			if (ccClass.NAME_CLASS == "l_quebra_linha") {
				ccClass.PROPS[0].clear = "both";
			}
		});

		// Seta os campos como como width INITIAL
		$.each(JSON_FORM_CONTACT, function (count, ccClass) {	
			if (ccClass.NAME_CLASS == "l_campo_cem_porcento") {
				ccClass.PROPS[0].width = "initial";
			}
		});

		// Seta a largura do titulo
		$.each(JSON_FORM_CONTACT, function (count, ccClass) {	
			if (ccClass.NAME_CLASS == "l_titulo") {
				ccClass.PROPS[7].width = widthTitle + "px !important";
			}
		});	

		// seta automaticamente a largura do formulário ***
		var widthInput = 0;
		var max = 0;

		// pegao maior campo do formulário
		$('#iframePreview').contents().find('.l_campo').each(function (i) {
			
			widthInput = $(this).outerWidth(); // pega o tamanho que está sendo renderizado, nao o setado

			if (i == 0) {
				max = widthInput;
			} else if (max < widthInput) {
				max = widthInput;
			}
		});

		$.each(JSON_FORM_CONTACT, function (count, ccClass) {	

			if (ccClass.NAME_CLASS == "l_formulario") {

				var border = ccClass.PROPS[5].border.replace("px", "");
				var padding_left = ccClass.PROPS[2].padding_left.replace("px", "");

				ccClass.PROPS[9].width = (parseInt(max) + parseInt(padding_left) + (parseInt(border) * 2) + parseInt(widthTitle)) + "px"; 
			}
		});
	
	}
	// END seta automaticamente a largura do formulário ***

	// define o alinhamento do formulário p
	$.each(JSON_FORM_CONTACT, function (count, currClass) {	
		if (currClass.NAME_CLASS == "l_formulario p") {
			currClass.PROPS[0].float = align;
		}
	});		
}


// Calcula a margem direita de acordo com a formula aplicada
function calculateMarginRight (paddingForm, paddingInput, input_border) {

	paddingForm = paddingForm.replace("px", "");
	paddingForm = paddingForm.replace(" !important", "");

	paddingInput = paddingInput.replace("px", "");
	paddingInput = paddingInput.replace(" !important", "");

	input_border = input_border.replace("px", "");
	input_border = input_border.replace(" !important", "");


	paddingForm = parseInt(paddingForm);
	paddingInput = parseInt(paddingInput);
	input_border = parseInt(input_border);	

	return (paddingForm + ((2 * paddingInput) + (2 * input_border))) + "px";
}


// ------------------------------------------------------

// BUTTON -----------------------------------------------

// Exibe div de configuração do campo
function showBoxConfigButton (idDiv) {

	var FONT_SIZE_P = "12px !important";
	var FONT_SIZE_M = "14px !important";
	var FONT_SIZE_G = "16px !important";
 		
	$.each(JSON_FORM_CONTACT, function (count, currClass) {

		if (currClass.NAME_CLASS == "l_botao_enviar") {

			$("#b_text_value").val(currClass.PROPS[0].text_value);

			// Botao visivel da classe botao cancelar
			$.each (JSON_FORM_CONTACT, function (count, cClass) {	
				if (cClass.NAME_CLASS == "l_botao_cancelar") {
					
					$("#b_text_value_cancel").val(cClass.PROPS[0].text_value);

					if (cClass.PROPS[1].display == "block") {
						$("#b_checkbox_visible").prop("checked", true);
						$("#b_button_cancel_visible").val("block");
					} else {

						$("#b_checkbox_visible").prop("checked", false);
						$("#b_button_cancel_visible").val("none");
					}
				}
			});

			// Se for padrão 
			if (currClass.PROPS[12].opacity == "1" && currClass.PROPS[13].padding_bottom == "3px" && currClass.PROPS[7].border_color == "#A1A1A1") {

				$("#b_person_or_default").val("default");
				$("#b_default").prop("checked", true);
				$("#b_person").prop("checked", false);

				$("#b_configs").css("display", "none");


			// Se for personalizado	
			} else {

				$("#b_person_or_default").val("person");
				$("#b_default").prop("checked", false);
				$("#b_person").prop("checked", true);				
				
				$("#b_configs").css("display", "block");
			
				$(".b_btn_size").each(function () {
					$(this).addClass("btn-default");
				});

				if (false) { console.log("1");}else {

					// tamanho da fonte
					if (currClass.PROPS[1].font_size == FONT_SIZE_P) {
						$("#b_p").removeClass("btn-default");
					} else if (currClass.PROPS[1].font_size == FONT_SIZE_M) {
						$("#b_m").removeClass("btn-default");
					} else if (currClass.PROPS[1].font_size == FONT_SIZE_G) {
						$("#b_g").removeClass("btn-default");
					}

					$("#b_font_size").val(currClass.PROPS[1].font_size);

					// Cor da fonte
					var font_color = currClass.PROPS[3].color;
					font_color = font_color.replace(" !important", "");
					font_color = font_color.replace("#", "");
					$("#b_color").val(font_color);
					$("#b_color").css("background", "#" + font_color);

					// tamanho da fonte
					var font_size = currClass.PROPS[1].font_size;
					$("#b_font_size").val(font_size);
					
					
					// estilo da fonte
					$("#b_font_family").val(currClass.PROPS[2].font_family);

					// Define a borda como reta/continua
					if (currClass.PROPS[8].border_radius == "0px") {
						$("#b_border_type").val("solid");
						$("#b_border_type_cont").prop("checked", true);

					// Define como abaulada
					} else {
						$("#b_border_type").val("radius");
						$("#b_border_type_abau").prop("checked", true);
					}

					var bg_color = currClass.PROPS[4].background.replace("#", "");
					bg_color = bg_color.replace(" !important", "");
					$("#b_background_color").val(bg_color);
					$("#b_background_color").css("background", "#" + bg_color);

				}
			}	
		}
	});
	
	$(idDiv).css("display", "block");
	$(idDiv).find('.modal').slideDown();
}

// Reseta a classe botão para o padrão cinza
function resetButtonDefault (isHideModal) {

	var	PROPS = "";
	$.each(JSON_DEFAULT, function (count, currClass) {	
		if (currClass.NAME_CLASS == "l_botao_enviar_default") {
			PROPS = currClass.PROPS;
		}
	});

	$.each(JSON_FORM_CONTACT, function (count, currClass) {	
		if (currClass.NAME_CLASS == "l_botao_enviar") {
			currClass.PROPS = PROPS;
		}
	});

	$.each(JSON_DEFAULT, function (count, currClass) {	
		if (currClass.NAME_CLASS == "l_botao_cancelar") {
			PROPS = currClass.PROPS;
		}
	});

	$.each(JSON_FORM_CONTACT, function (count, currClass) {	
		if (currClass.NAME_CLASS == "l_botao_cancelar") {
			currClass.PROPS = PROPS;
		}
	});

	// Padding do form
	var padding_form = "";
	$.each(JSON_FORM_CONTACT, function (count, ccClass) {	
		if (ccClass.NAME_CLASS == "l_formulario") {
			padding_form = ccClass.PROPS[1].padding_bottom;
		}
	});

	// Seta no botão o padding do formulário
	$.each (JSON_FORM_CONTACT, function (count, currClass) {	
		if (currClass.NAME_CLASS == "l_div_botao") {
			currClass.PROPS[1].margin_right = padding_form;
		}
	});

	if (isHideModal) {
		saveJson("#modalButton");
	} else {
		saveJson();	
	}
	
}

// Seta a configuração do botao enviar
function setButtonJson () {
	
	var BUTTON_SEND_DEFAULT = "";
	var BUTTON_CANCEL_DEFAULT = "";
	var BUTTON_CANCEL_VISIBLE_DEFAULT = "";
	var STYLE_PERSON_OR_DEFAULT = "default";
	var FONT_SIZE_DEFAULT = "14px";
	var COLOR_DEFAULT = "000";
	var FONT_FAMILY_DEFAULT = "Tahoma";
	var BACKGROUND_DEFAULT = "F8F8F8";
	var BORDER_TYPE_DEFAULT = "radius";
	var BORDER_RADIUS_DEFAULT = "6px";
	var BORDER_DEFAULT = "1px";
	var BORDER_RADIUS_DEFAULT = "6px";

	var button_send_text = $("#b_text_value").val();
	var button_cancel_text = $("#b_text_value_cancel").val();
	var button_cancel_visible = $("#b_button_cancel_visible").val();
	var style_person_or_default = $("#b_person_or_default").val();
	var font_size = $("#b_font_size").val();
	var color = $("#b_color").val();
	var font_family = $("#b_font_family").val(); 
	var background = $("#b_background_color").val();
	var border_type = $("#b_border_type").val();
	
	button_send_text = ((button_send_text == "")? BUTTON_SEND_DEFAULT : button_send_text);
	button_cancel_text = ((button_cancel_text == "")? BUTTON_CANCEL_DEFAULT : button_cancel_text);
	button_cancel_visible = ((button_cancel_visible == "")? BUTTON_CANCEL_VISIBLE_DEFAULT : button_cancel_visible);
	style_person_or_default = ((style_person_or_default == "")? STYLE_PERSON_OR_DEFAULT : style_person_or_default);
	font_size = ((font_size == "")? FONT_SIZE_DEFAULT : font_size);
	color = ((color == "")? COLOR_DEFAULT : color);
	font_family = ((font_family == "")? FONT_FAMILY_DEFAULT : font_family);
	background = ((background == "")? BACKGROUND_DEFAULT : background);
	border_type = ((border_type == "")? BORDER_TYPE_DEFAULT : border_type);
	
	// reseta pra tirar o lixo
	resetButtonDefault ();

	$.each(JSON_FORM_CONTACT, function (count, currClass) {	
		if (currClass.NAME_CLASS == "l_botao_enviar") {
	  			  	
	  		currClass.PROPS[0].text_value = button_send_text;

			// Seta o texto do botao send e do cancel, assim como a visibilidade do segundo.
			setButtonCancelValueAndVisible (button_cancel_visible, button_cancel_text);
			
			// Seta o botão cancelar e o botão enviar no banco de dados
			addValueBtnSendAndCancel (button_send_text, button_cancel_text);

			// Se for personalizado
	  		if (style_person_or_default == "person") {

	  			// Configuração da fonte
	  			currClass.PROPS[1].font_size = font_size;
	  			currClass.PROPS[2].font_family = font_family;
	  			currClass.PROPS[3].color = "#" + color + " !important";

	  			// cor de fundo
	  			currClass.PROPS[4].background = "#" + background + " !important";

	  			// Configura a borda
		  		if (border_type == "solid") {
					currClass.PROPS[8].border_radius = "0px";
				} else if (border_type == "radius") {
					currClass.PROPS[8].border_radius = BORDER_RADIUS_DEFAULT;
				}

			// se nao for personalizado, volta as configurações padrão
	  		} else {
	  		
	  			var PROPS = "";
		  		$.each (JSON_DEFAULT, function (count, currClass) {	
					if (currClass.NAME_CLASS == "l_botao_enviar_default") {
						PROPS = currClass.PROPS;
					}
				});

				$.each (JSON_FORM_CONTACT, function (count, currClass) {	
					if (currClass.NAME_CLASS == "l_botao_enviar") {
						currClass.PROPS = PROPS;
					}
				});	

				// Padding do form
				$.each(JSON_FORM_CONTACT, function (count, ccClass) {	
					if (ccClass.NAME_CLASS == "l_formulario") {
						padding_form = ccClass.PROPS[1].padding_bottom;
					}
				});

				$.each (JSON_FORM_CONTACT, function (count, currClass) {	
					if (currClass.NAME_CLASS == "l_div_botao") {
						currClass.PROPS[1].margin_right = padding_form;
					}
				});
	  		}
  		}
	});

	saveJson ("#modalButton");
}

// Seta se o botao limpar, vai estar visivel ou nao
function setBtnCancelVisible (element) {

	if ($(element).prop("checked")) {
		$("#b_button_cancel_visible").val("block");
	} else {
		$("#b_button_cancel_visible").val("none");
	}
}


// Seta o padrão do botao
function setButtonPersonOrDefault (element) {

	if ($(element).val() == "person") {

		$("#b_person_or_default").val("person");
		$(".porcsessenta").css("display", "block");

		// Se for um novo formulário, carrega o padrão DIFERENTE do 
		// "cinza" das bordas quadradas.. No inicio do projeto era cinza.
		if (JSON.stringify(JSON_FORM_CONTACT) === JSON.stringify(JSON_DEFAULT)) {
			var FONT_SIZE_P = "12px !important";
			var FONT_SIZE_M = "14px !important";
			var FONT_SIZE_G = "16px !important";

			// Botao visivel da classe botao cancelar
			$.each (JSON_DEFAULT, function (count, currClass) {	

				if (currClass.NAME_CLASS == "l_botao_enviar") {
				
					// tamanho da fonte
					if (currClass.PROPS[1].font_size == FONT_SIZE_P) {
						$("#b_p").removeClass("btn-default");
					} else if (currClass.PROPS[1].font_size == FONT_SIZE_M) {
						$("#b_m").removeClass("btn-default");
					} else if (currClass.PROPS[1].font_size == FONT_SIZE_G) {
						$("#b_g").removeClass("btn-default");
					}

					$("#b_font_size").val(currClass.PROPS[1].font_size);

					// Cor da fonte
					var font_color = currClass.PROPS[3].color;
					font_color = font_color.replace(" !important", "");
					font_color = font_color.replace("#", "");
					$("#b_color").val(font_color);
					$("#b_color").css("background", "#" + font_color);

					// tamanho da fonte
					var font_size = currClass.PROPS[1].font_size;
					$("#b_font_size").val(font_size);
					
					
					// estilo da fonte
					$("#b_font_family").val(currClass.PROPS[2].font_family);

					// Define a borda como reta/continua
					if (currClass.PROPS[8].border_radius == "0px") {
						$("#b_border_type").val("solid");
						$("#b_border_type_cont").prop("checked", true);

					// Define como abaulada
					} else {
						$("#b_border_type").val("radius");
						$("#b_border_type_abau").prop("checked", true);
					}

					var bg_color = currClass.PROPS[4].background.replace("#", "");
					bg_color = bg_color.replace(" !important", "");
					$("#b_background_color").val(bg_color);
					$("#b_background_color").css("background", "#" + bg_color);

				}
			});
		} 

	} else if ($(element).val() == "default") {

		$("#b_person_or_default").val("default");
		$(".porcsessenta").css("display", "none");
	}
}

// Seta o espaçamento entre campos
function setMarginBottom (margin) {

	$.each (JSON_FORM_CONTACT, function (count, currClass) {	
		if (currClass.NAME_CLASS == "l_div_conteudo") {
			currClass.PROPS[0].margin_bottom = margin;
		}
	});		
}

// Seta o tamanho da fontedo input
function setButtonFontSize (element) {
	
	$(".b_btn_size").each(function () {
		$(this).addClass("btn-default");
	});

	$(element).removeClass("btn-default");

	$("#b_font_size").val($(element).attr("propval"));
}

// Seta o tipo da borda se é solida ou abaulada
function setButtonTypeBorder (element) {
	var type = $(element).val();
	$("#b_border_type").val(type);
}

// Seta o texto do botão cancel e sua visibilidade
function setButtonCancelValueAndVisible (visible, text) {
	$.each (JSON_FORM_CONTACT, function (count, currClass) {	
		if (currClass.NAME_CLASS == "l_botao_cancelar") {
			currClass.PROPS[0].text_value = text;
			currClass.PROPS[1].display = visible;
		}
	});		
}

// ------------------------------------------------------

// Salva o json do formulário
function saveJson (idModal) {

	// GARANTE que a margem direita vai estar sempre calculada 
	// de forma correta
	var padding_form = "";
	$.each(JSON_FORM_CONTACT, function (count, ccClass) {	
		if (ccClass.NAME_CLASS == "l_formulario") {
			padding_form = ccClass.PROPS[1].padding_bottom;
		}
	});

	// Seta os campos como como width INITIAL
	var input_border = "";
	var paddingInput = "";
	$.each(JSON_FORM_CONTACT, function (count, ccClass) {	
		if (ccClass.NAME_CLASS == "l_campo") {
			input_border = ccClass.PROPS[8].border.replace("px", "");
			input_border = input_border.replace(" !important", "");
			paddingInput = ccClass.PROPS[4].padding_bottom.replace("px", "");
		}
	});

	// Retirar o tamanho do titulo e deixar a margem do tamanho da
	// margem do formulário, isso depois de fazer o calculo geral
	$.each(JSON_FORM_CONTACT, function (count, ccClass) {	
		if (ccClass.NAME_CLASS == "l_div_botao") {
			ccClass.PROPS[1].margin_right =  padding_form;
		}
	}); 

	// Retirar o tamanho do titulo e deixar a margem do tamanho da
	// margem do formulário
	$.each(JSON_FORM_CONTACT, function (count, ccClass) {	
		if (ccClass.NAME_CLASS == "l_campo_cem_porcento") {
			if (ccClass.PROPS[0].width == "100% !important") {
				
				// Retirar o tamanho do titulo e deixar a margem do tamanho da
				// margem do formulário
				$.each(JSON_FORM_CONTACT, function (count, ccClass) {	
					if (ccClass.NAME_CLASS == "l_div_conteudo") {
						ccClass.PROPS[1].margin_right = calculateMarginRight (padding_form, paddingInput, input_border); //padding_form;
					}
				});

			} else {

				// Retirar o tamanho do titulo e deixar a margem do tamanho da
				// margem do formulário
				$.each(JSON_FORM_CONTACT, function (count, ccClass) {	
					if (ccClass.NAME_CLASS == "l_div_conteudo") {
						ccClass.PROPS[1].margin_right = padding_form;
					}
				});

			}
		}
	}); 

	

	// força a margem do campo select, pois ele nao tem
	// padding
	$.each(JSON_FORM_CONTACT, function (count, ccClass) {	
		if (ccClass.NAME_CLASS == "l_combobox") {
			ccClass.PROPS[0].margin_right = padding_form + " !important";
		}
	}); 
	// END forma correta calculo margem

	// Atualiza a largura e altura do formulário
	// addWidthHeight ();

	// loadPreview ();

	$.ajax({
		type: 'POST',
		dataType: 'HTML',
		url: '/proc.jsp',
		async: false,
		data: {actionID: 20, idFormContact: ID_FORM_CONTACT, json_form_contact: JSON.stringify(JSON_FORM_CONTACT)},
		success: function(response) {
		
			if (response.trim().length == 2) {
				// Oculta o modal
				$(idModal).find('.modal').slideUp(function () {
					$(idModal).hide();
				});

				getIframe ();
				getPopup ();
				getCodeFull ();
			} else {
				// Oculta o modal
				$(idModal).find('.modal').slideUp(function () {
					$(idModal).hide();
				});
			}
		}
	});	
}

// Função que carrega e recarrega o preview
function loadPreview () {
	$('#iframePreview').each(function() {
 		this.contentWindow.location.reload(true);
	});
}

// Add value btn Cancel AND SEND
function addValueBtnSendAndCancel (btnSend, btnCancel) {
	
	$.ajax({
		type: 'POST',
		dataType: 'HTML',
		async: false,
		url: '/proc.jsp',
		data: {actionID: 21, idFormContact: ID_FORM_CONTACT, _btnSend: btnSend, _btnCancel: btnCancel},
		success: function(response) {
			if (response.trim().length == 2) {
				console.log("OK");
			} else {
				console.log("ERRO");
			}
		}
	});	
}

function addWidthHeight () {
	var width = $('#iframePreview').contents().find('.l_formulario').outerWidth();
	var height = $('#iframePreview').contents().find('.l_formulario').outerHeight();

	$.ajax({
		type: 'POST',
		dataType: 'HTML',
		async: false,
		url: '/proc.jsp',
		data: {actionID: 22, idFormContact: ID_FORM_CONTACT, _width: (width), _height: (height)},
		success: function(response) {
			if (response.trim().length == 2) {
				console.log("OK");
			} else {
				console.log("ERRO");
			}
		}
	});	
}

function setTypeCodeShow (element) {
	
	var type = $(element).val();

	if (type == "iframe") {
	
		$("#codeFormIframe").css("display", "block");
		$("#codeFormPopUp").css("display", "none");
		$("#codeFormFull").css("display", "none");
	
	} else 	if (type == "popup") {
	
		$("#codeFormIframe").css("display", "none");
		$("#codeFormPopUp").css("display", "block");
		$("#codeFormFull").css("display", "none");
	
	} else 	if (type == "codefull") {
	
		$("#codeFormIframe").css("display", "none");
		$("#codeFormPopUp").css("display", "none");
		$("#codeFormFull").css("display", "block");
	
	}
}

// pega o codigo iframe
function getIframe () {
	
	$.ajax({
		type: 'POST',
		dataType: 'HTML',
		async: false,
		url: '/proc.jsp',
		data: {actionID: 23, idForm: ID_FORM_CONTACT},
		success: function(response) {
			$("#codeFormIframe").text(response.trim());
		}
	});	
}


// pega o codigo do popup
function getPopup () {
	
	$.ajax({
		type: 'POST',
		dataType: 'HTML',
		async: false,
		url: '/proc.jsp',
		data: {actionID: 24, idForm: ID_FORM_CONTACT},
		success: function(response) {
			$("#codeFormPopUp").text(response.trim());
		}
	});	
}

// pega o codigo bruto
function getCodeFull () {
	
	$.ajax({
		type: 'POST',
		dataType: 'HTML',
		async: false,
		url: '/proc.jsp',
		data: {actionID: 25, idForm: ID_FORM_CONTACT},
		success: function(response) {
			$("#codeFormFull").text(response.trim());
		}
	});	
}

// Comparação de dois jsonStyle
function compareJsonEquals(objA, objB) {
    return compareTreeJson(objA, objB, "root");
}

function compareTreeJson(a, b, name ) {
    var typeA = typeofReal(a);
    var typeB = typeofReal(b);

    var isEquals = true;

    var aString = (typeA === "object" || typeA === "array") ? "" : String(a) + " ";
    var bString = (typeB === "object" || typeB === "array") ? "" : String(b) + " ";


    if (a === undefined) {
        isEquals = false;
    }
    else if (b === undefined) {
        isEquals = false;
    }
    else if (typeA !== typeB || (typeA !== "object" && typeA !== "array" && a !== b)) {
        isEquals = false;
    }
   
    if (typeA === "object" || typeA === "array" || typeB === "object" || typeB === "array") {
        var keys = [];
        for (var i in a) {
            if (a.hasOwnProperty(i)) {
                keys.push(i);
            }
        }
        for (var i in b) {
            if (b.hasOwnProperty(i)) {
                keys.push(i);
            }
        }
        keys.sort();

        for (var i = 0; i < keys.length; i++) {
            if (keys[i] === keys[i-1]) {
                continue;
            } else {
            	if (a[keys[i]] != b[keys[i]]) {
            		isEquals = false;
            	}
            }

            compareTreeJson(a && a[keys[i]], b && b[keys[i]], keys[i]);
        }
    }

    return isEquals;
}

function typeofReal(value) {
    return isArray(value) ? "array" : typeof value;
}

function isArray(value) {
    return value && typeof value === "object" && value.constructor === Array;
}