urlBase="http://www.linkws.com";var linkIndicID=13;function resizeWindow(width,height,centralized){window.resizeTo(width,height);if(centralized!=false){var top=(screen.height/2)-(height/2);var left=(screen.width/2)-(width/2);}window.moveTo(left,top);}function openWindow(url,name,width,height,centralized,params){var position="";if(params==null){params="";}else {params=","+params;}if(centralized!=false){var top=(screen.height/2)-(height/2);var left=(screen.width/2)-(width/2);position="top="+top+",left="+left;}params=position+",width="+width+",height="+height+params;return window.open(url,name,params);}function putAnItem(theList,name,value,index){var option=new Option(name,value);if(index==null) index=theList.length;theList.options[index]=option;}function multiSelect(select,i){for (var j=0;j<i.length;j++)select.options[j].selected=i[j];}function swap(s1,from,to){var option1=s1.options[from];var option2=s1.options[to];putAnItem(s1,option1.value,option1.value,to);putAnItem(s1,option2.value,option2.value,from);}function sync(select1,select2){var i=new Array(select1.options.length);for (var j=0;j<i.length;j++)i[j]=select1.options[j].selected;multiSelect(select1,i);multiSelect(select2,i);}function selItem(select,i){var selIndex=new Array(select.options.length);selIndex[i]=true;multiSelect(select,selIndex);}function deleteAnItem(theList,itemNo){theList.options[itemNo]=null}function insertAnItem(theList,item){var option=new Option(item,item);theList.options[theList.length]=option;}function isCheckGroup(element){return (element.checked+""=="undefined" && element.length+""!="undefined");}function showLinkIndic_1(id){var LI=window.open(urlBase+'/webservices/li/link_indic.jsp?userProductID='+id+"&url="+escape(location.href),'link_Indic','height=340,width=402,scrolling=no');}function showLinkComunic(){openWindow(urlBase+'/webservices/lc/teste.htm','link_Comunic',600,350);}function checkAll(checksInput,isToCheck){if(checksInput.checked==null){for (var i=0;i<checksInput.length;i++){checksInput[i].checked=isToCheck;}}else {checksInput.checked=isToCheck;}}function updateChecks(checksInput,allCheckInput){var allChecked=true;if(checksInput.checked==null){for(var i=0;i<checksInput.length;i++){if(!checksInput[i].checked){allChecked=false;break;}}}else {allChecked=checksInput.checked;}if(allCheckInput!=null){allCheckInput.checked=allChecked;}return allChecked;}function selectAll(element){if(element.options==null){element.select();}else {for (var j=0;j<element.options.length;j++)element.options[j].selected=true;}}function Validator(preMsg){this.firstErrorField=null;if(preMsg!=null){this.msg=preMsg;}else {this.msg="Há informações erradas ou incompletas no formulário. Erros:\n\n";}this.validateSimpleTextField=validateSimpleTextField;this.validateVariableField=validateVariableField;this.validateNumberTextField=validateNumberTextField;this.validateUrlTextField=validateUrlTextField;this.validateEmailField=validateEmailField;this.validateComboField=validateSimpleTextField;this.validateCheckGroup=validateCheckGroup;this.doLog=doLog;this.processLog=processLog;}


function trim(str){return str.replace(/^\s+|\s+$/g,"");}

function isValidURL(url){
  var urlregex = new RegExp("^(http|https|ftp)\\://[a-zA-Z0-9_\\-\\.]+\\.[a-zA-Z]{2,3}(:[a-zA-Z0-9]*)?/?([a-zA-Z0-9()*@:!\\-\\._\\?\\,\\'/\\\\\\+&amp;%\\$#\\=~])*$");
  return urlregex.test(url.toLowerCase());
} 

function validateSimpleTextField(element,errorMsg){if(trim(element.value+"")==""){this.doLog(element,errorMsg);return false;}return true;}function validateVariableField(element,errorMsg){var str=element.value;if(str==""||!isNaN(str.charAt(0))){this.doLog(element,errorMsg);return false;}for (var i=0;i<str.length;i++){if(!((str.charAt(i) >= 'a'&&str.charAt(i) <= 'z')||(str.charAt(i) >= 'A'&&str.charAt(i) <= 'Z') ||(str.charAt(i) >= '0'&&str.charAt(i) <= '9') ||(str.charAt(i)=='_'))){this.doLog(element,errorMsg);return false;}}return true;}function validateNumberTextField(element,maxValue,minValue,errorMsg){if(element.value!=""&&(isNaN(element.value)||(maxValue!=null&&element.value>maxValue)||(minValue!=null&&element.value<minValue))){this.doLog(element,errorMsg);return false;}return true;}function validateUrlTextField(element,errorMsg){var httpUrl=element.value;if(httpUrl==""){this.doLog(element,errorMsg);return false;}if(httpUrl.length<"http://".length){this.doLog(element,"URL inválida. Ela provavelmente não possui http://");return false;}if(httpUrl.substring(0,"http://".length)!="http://" ){this.doLog(element,"URL inválida. Ela deve conter http://");return false;}if(httpUrl.length<"http://".length+1){this.doLog(element,"URL inválida. Ela tem que conter algum dado além de http://");return false;}var dotPos=httpUrl.indexOf(".");if(dotPos==-1){this.doLog(element,"URL inválida. Ela tem que conter um ponto.");return false;}if(!(dotPos>"http://".length&&dotPos<httpUrl.length-1)){this.doLog(element,"URL inválida. O caminho não existe ou está incorreto.");return false;}return true;}

function validateEmailField(element,errorMsg) {
	if (!isValidEmail(element.value)) {
		this.doLog(element,errorMsg);return false;
		return false;
	}

	return true;
}

function validateCheckGroup(element,errorMsg){var ok=false;if(isCheckGroup(element)){for (var i=0;i<element.length;i++){if( element[i].checked){ok=true;}}}else {ok=element.checked;}if(!ok){this.doLog(element,errorMsg);return false;}return true;}function doLog(element,newMsg){if(this.firstErrorField==null){this.firstErrorField=element;}else {this.msg += "\n";}this.msg += "- "+newMsg;}function processLog(){if(this.firstErrorField!=null){alert(this.msg);if(!isCheckGroup(this.firstErrorField)){this.firstErrorField.focus();this.firstErrorField.select();}else{this.firstErrorField[0].focus()}}return (this.firstErrorField==null);}function strReplace(text,searchFor,replaceStr){var re=new RegExp(searchFor,'gi');text=text.replace(re,replaceStr);return text;}function limitTextArea(mf,cf,m){if(mf.value.length>m){mf.value=mf.value.substring(0,m);}else{if(cf!=null){cf.value=m-mf.value.length;}}}

// Se é um logiu válido: não pode ter espaços, acentos, cedilha, espaços e deve ter mais de 2 caracteres.
function isValidLogin(login) {
	var originalLogin = login;
	login = (login).toLowerCase();
	var deniedChars = "áàéèíìóòúùç \t";
	var denied = false;
	
	for (var i = 0; i < deniedChars.length; i++) {
		if (login.indexOf(deniedChars.charAt(i)) != -1) {
			denied = true;
			break;
		}
	}
	
	if (denied || login != originalLogin || login.length < 3) {
		return false;
	}
	
	return true;
}

//---------------
// 2013-04-23 De Isaac.

// Colocar o cookie. O temmpo é em segundos. Se for 0 é de sessão, ou seja, fechou o navegado, já era.
function setCookie(name,value,expire) {
	expDate=new Date();
	expDate.setTime(expDate.getTime()+(expire!=0?expire:0));
	document.cookie=name+"="+escape(value)+";path=/"+(expire?";expires="+expDate.toGMTString()+";":"");
}

// Pega o cookie
function getCookie(name) {
	cV=document.cookie;
	keyPos=cV.indexOf(name+"=");
	if(keyPos==-1) return "";
	cV=cV.substring(keyPos+(name+"=").length,cV.length);
	cD=cV.indexOf(";");
	
	if(cD==-1) cD=cV.length;
	return cV.substring(0,cD);
}

function number_format( number, decimals, dec_point, thousands_sep) {
	// % nota 1: Para 1000.55 retorna com precisão 1 no FF/Opera é 1,000.5, mas no IE é 1,000.6
	// * exemplo 1: number_format(1234.56);
	// * retorno 1: '1,235'
	// * exemplo 2: number_format(1234.56, 2, ',', ' ');
	// * retorno 2: '1 234,56'
	// * exemplo 3: number_format(1234.5678, 2, '.', '');
	// * retorno 3: '1234.57'
	// * exemplo 4: number_format(67, 2, ',', '.');
	// * retorno 4: '67,00'
	// * exemplo 5: number_format(1000);
	// * retorno 5: '1,000'
	// * exemplo 6: number_format(67.311, 2);
	// * retorno 6: '67.31'

	var n = number, prec = decimals;
	n = !isFinite(+n) ? 0 : +n;
	prec = !isFinite(+prec) ? 0 : Math.abs(prec);
	var sep = (typeof thousands_sep == "undefined") ? ',' : thousands_sep;
	var dec = (typeof dec_point == "undefined") ? '.' : dec_point;
	
	var s = (prec > 0) ? n.toFixed(prec) : Math.round(n).toFixed(prec); //fix for IE parseFloat(0.55).toFixed(0) = 0;
	
	var abs = Math.abs(n).toFixed(prec);
	var _, i;
	
	if (abs >= 1000) {
		_ = abs.split(/\D/);
		i = _[0].length % 3 || 3;
		
		_[0] = s.slice(0,i + (n < 0)) +
		_[0].slice(i).replace(/(\d{3})/g, sep+'$1');
		
		s = _.join(dec);
	} else {
		s = s.replace('.', dec);
	}

	return s;
}

function moeda2float(moeda){
   moeda = moeda.replace(".","");
   moeda = moeda.replace(",",".");
   return parseFloat(moeda);
}


function isValidEmail(email) {
  var re = new RegExp("^([a-zA-Z0-9_\\-])([a-zA-Z0-9_\\-\\.]*)@(\\[((25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9][0-9]|[0-9])\\.){3}|((([a-zA-Z0-9\\-]+)\\.)+))([a-zA-Z]{2,}|(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9][0-9]|[0-9])\\])$");
  return re.test(email.toLowerCase());
}

// Valida se o evento é uma digitação de número
// Só colocar no input: onKeyDown="return validateOnlyNumber(event)"
// Lembrando que esta validação é falha, então, se for necessário que seja número mesmo, tem que validar no jsp também.
function validateOnlyNumber(evt) {
    var e = evt || window.event;
    var key = e.keyCode || e.which;
	var isNumberValid = true;
	
	// console.log(key);
	
    if (!e.shiftKey && !e.altKey && !e.ctrlKey &&
		// numbers   
		key >= 48 && key <= 57 ||
		
		// Numeric keypad
		key >= 96 && key <= 105 ||
		
		// Backspace and Tab and Enter
		key == 8 || key == 9 || key == 13 ||
		
		// Home and End
		key == 35 || key == 36 ||
		
		// left and right arrows
		key == 37 || key == 39 ||
		
		// Del and Ins
		key == 46 || key == 45 ||
		
		// outros - control + f5
		key == 116) {
    } else {
        e.returnValue = false;
        isNumberValid = false;
	    if (e.preventDefault) e.preventDefault();
    }
	
	return isNumberValid;
}