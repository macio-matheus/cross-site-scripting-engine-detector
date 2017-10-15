

// função para esconder ou mostrar, sempre o inverso
function showHiden(obj,obj2) {
	if(document.getElementById(obj).style.display == 'block'){
		document.getElementById(obj).style.display = 'none';
		document.getElementById(obj2).innerHTML = 'mostrar  <img src="http://www.santander.com.br/document/wps/icoSetaBaixo.jpg" align="top"  />'
	}else{
		document.getElementById(obj).style.display = 'block';
		document.getElementById(obj2).innerHTML = 'esconder  <img src="http://www.santander.com.br/document/wps/icoSetaCima.jpg" align="top"  />'
	}
}

var urlIB_Banespa = "https://www.santandernet.com.br";
            function LogarNC()
            {
                if(document.frmnetbankingNC.txtDocto.value == "")
                {
                   alert("Por favor, preencha o campo CPF");
                   return false;
               }else{
                    var URL = urlIB_Banespa + "/defaultnc.asp?txtDocto=" + document.frmnetbankingNC.txtDocto.value  + '&oA=site';
                    window.open(URL,  "_blank");
                   return false;
                }
            }
            function sL(obj) 
           {
                if(document.getElementById)
                {
                    document.getElementById(obj).style.display = 'block';
                }
                if(document.all && !document.getElementById)
                {
                    document.all(obj).style.display = 'block';
                }
            }
            function hL(obj) 
            {
                if(document.getElementById)
                {
                    document.getElementById(obj).style.display = 'none';
                }
                if(document.all && !document.getElementById)
                {
                    document.all(obj).style.display = 'none';
                }
            }
			
			function dialogo(){
					if (document.getElementById("select_dialogo").value == ""){
						alert("Selecione um Canal de Diálogo");
						return false;
					}
					
					else {
						location.href = document.getElementById("select_dialogo").value;
						return false;
					}
				}
				
	$(document).ready(function(){
		//Definimos que todos as tags dd tero display:none menos o primeiro filho
		$('dd:not(:first)').hide();

		//Ao clicar no link, executamos a funcao
		var clicked = "";
		clicked = "dt1";

		$('dt a').click(function(){

			if (clicked == $(this).attr("id")){
				return false;
			}
			else{
				//$("#"+clicked).css('background-color', 'black');
				$("#"+clicked).removeClass('selected');
				$("#"+clicked).addClass('deselected');
				clicked = $(this).attr("id");
				//$(this).css('background-color', 'red');
				$(this).removeClass('deselected');
				$(this).addClass('selected');

			}
			//As tags dd's visveis agora ficam com display:none
			$("dd:visible").slideUp("fast");
			//Apos, a funcao  transferida para seu pai, que procura o proximo irmao no codigo o tonando visvel
			$(this).parent().next().slideDown("fast");
			return false;
		});
	});	


//---------------No Flash Link--------------
jQuery.fn.loadURLNoflash = function(_s){
var obj = $(this);
$.ajax({type: "GET",url: _s, dataType: "text",  error: function(xml){$(obj).attr("href","#");},success: trata});
function trata(xml){
try{
var _temp291010 = xml.match(/(enderecodosite=")([A-Za-z0-9:.-\\&_])+/gi); var _temp291020 = [];
$(obj).attr("href",_temp291010[0].replace(/enderecodosite=\"/,''));
}catch(e){$(obj).attr("href","#");}
}}

var bln_is_ie     = (document.all && navigator.plugins.length == 0 && navigator.userAgent.indexOf('Win') != -1) ? true : false;
var bln_is_ie5p     = (bln_is_ie && (navigator.userAgent.toLowerCase().indexOf('msie 5') != -1));
var bln_is_ie5      = (bln_is_ie && (navigator.userAgent.toLowerCase().indexOf('msie 5.0') != -1));
var bln_is_ie6      = (bln_is_ie && (navigator.userAgent.toLowerCase().indexOf('msie 6') != -1));
var bln_is_ie7      = (bln_is_ie && (navigator.userAgent.toLowerCase().indexOf('msie 7') != -1));
var bln_is_ie8      = (bln_is_ie && (navigator.userAgent.toLowerCase().indexOf('msie 8') != -1));
var str_ua        = navigator.userAgent.toLowerCase();
var bln_is_ns       = ((str_ua.indexOf('gecko') != -1) ? (str_ua.indexOf('netscape') != -1) : ((str_ua.indexOf('mozilla') != -1) && (str_ua.indexOf('spoofer') == -1) && (str_ua.indexOf('compatible') == -1) && (str_ua.indexOf('opera') == -1) && (str_ua.indexOf('webtv') == -1) && (str_ua.indexOf('hotjava') == -1)));
var bln_is_ns6      = ((bln_is_ns && !document.layers && !document.all && str_ua.indexOf('netscape6') !== -1) ? true : false);
var bln_is_op     = ((!bln_is_ie && document.all && str_ua.toLowerCase().indexOf('opera') !== -1) ? true : false);
var ie_box        = bln_is_ie && (document.compatMode == null || document.compatMode == 'BackCompat');
var bln_using_sr    = ((str_ua.toLowerCase().indexOf('mpvv') !== -1) ? true : false);
var int_mode      = 0; // 0 para HTML; 1 para XHTML

function fun_detect_flash(arg_required_major_version){
  if(navigator.plugins.length > 0){
    var str_flash_name = 'Shockwave Flash';
    for(int_count = 0; int_count <= parseInt(navigator.plugins.length - 1); int_count++){
      if(navigator.plugins[int_count].name.indexOf(str_flash_name) != -1){
        str_flash_desc = navigator.plugins[int_count].description;
        int_flash_major_version = ((str_flash_desc.match(/([0-9]{1,})\.([0-9]{1,})/)) ? str_flash_desc.match(/([0-9]{1,})\.([0-9]{1,})/)[1] : 0);
        if(int_flash_major_version >= arg_required_major_version) return true; else return false;
      }
    }
  }else{
    if(bln_is_ie){
      var obj_flash = null;
      try{
        obj_flash = new ActiveXObject('ShockwaveFlash.ShockwaveFlash.' + arg_required_major_version);
        return ((obj_flash) ? true : false);}
      catch(obj_err){
        try{
          obj_flash = new ActiveXObject('ShockwaveFlash.ShockwaveFlash');
          obj_flash.AllowScriptAccess = 'always';

          var mxd_tmp_version = obj_flash.GetVariable('$version');
          mxd_tmp_version = ((mxd_tmp_version.match(/\ ([0-9]{1,})\,([0-9]{1,})\,/)) ? mxd_tmp_version.match(/\ ([0-9]{1,})\,([0-9]{1,})\,/)[1] : 0)

          return ((!isNaN(mxd_tmp_version)) ? ((mxd_tmp_version >= arg_required_major_version) ? true : false) : false);
        }catch(obj_err){return false;}
      }
    }
  }
}

function fun_show_flash_obj(str_src,arr_fl_size,str_name,str_id,obj_flash_vars,obj_flash_param,str_src_param,str_style,obj_script,int_lower_player,str_classid,str_codebase){
  str_src = ((str_src.indexOf('?') != -1) ? str_src.substring(0, parseInt(str_src.indexOf('?'))) : str_src);
  str_id = str_id || '';
  str_name = str_name || str_id;
  str_src_param = str_src_param || '';
  str_style = str_style || '';
  int_lower_player = int_lower_player || 6;
  str_classid = str_classid || 'clsid:D27CDB6E-AE6D-11cf-96B8-444553540000';
  str_codebase = str_codebase || '//download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=' + int_lower_player + ',0,0,0';
  var int_tmp_width = arr_fl_size[0];
  var int_tmp_height = arr_fl_size[1];
  var str_tmp_fl_vars = '';
  var int_count = 0;
  for(obj_looper in obj_flash_vars){
    str_tmp_fl_vars += ((int_count != 0) ? '&' : '') + obj_looper + '=' + obj_flash_vars[obj_looper];
    int_count++;
  }
  delete obj_looper,int_count;
  var str_tmp_param = (str_tmp_fl_vars != '' && str_src_param != '') ? (str_tmp_fl_vars + '&' + str_src_param) : ((str_tmp_fl_vars != '') ? str_tmp_fl_vars : ((str_src_param != '') ? str_src_param : ''));
  obj_flash_param.movie = str_src + '?' + str_tmp_param;
  obj_flash_param.flashvars = str_tmp_fl_vars;
  var str_tmp_fl_params_for_obj = '';
  var str_tmp_fl_params_for_embed = '';
  var int_count = 0;
  for(obj_looper in obj_flash_param){
    str_tmp_fl_params_for_obj += ((int_count != 0) ? '\n' : '') + '<param name=\"' + obj_looper + '\" value=\"' + obj_flash_param[obj_looper] + '\"' + ((int_mode == 1) ? ' /' : '') + '>';
    str_tmp_fl_params_for_embed += ' ' + obj_looper + '=\"' + obj_flash_param[obj_looper] + '\"';
    int_count++;
  }
  delete obj_looper,int_count;
  var str_tmp_script = '';
  for(obj_looper in obj_script){
    str_tmp_script += ' ' + obj_looper + '=\"' + obj_script[obj_looper] + '\"';
  }
  delete obj_looper;
  document.writeln('<object classid=\"' + str_classid + '\" codebase=\"' + str_codebase + '\" width=\"' + int_tmp_width + '\" height=\"' + int_tmp_height + '\" id=\"' + str_id + '\"' + ((str_style != '') ? ' style=\"' + str_style + '\"' : '') + str_tmp_script + '>');
  document.writeln(str_tmp_fl_params_for_obj);
  document.writeln('<embed src=\"' + obj_flash_param.movie + '\" name=\"' + str_name + '\" width=\"' + int_tmp_width + '\" height=\"' + int_tmp_height + '\"' + str_tmp_fl_params_for_embed + ' pluginspage=\"//www.macromedia.com/go/getflashplayer\" type=\"application/x-shockwave-flash\"></embed>');
  document.writeln('</object>');
}

var ExternalContent = {
  Facebook: null,Twitter: null,MarketNews: null,Podcast: null,Assets: null,FinancialIndicators: null,Quotation: null,

  add: function(str_service, Content){this[str_service] = Content;}
};

var _gaq = _gaq || [];
  _gaq.push(['_setAccount', 'UA-17133846-1']);
  _gaq.push(['_trackPageview']);

  (function() {
    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
  })();