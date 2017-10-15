	/**
	*	Funções de edit template
	*	@OBS Necessário estar incluso a biblioteca JQuery (jquery-1.6.3.min.js) UP. 
	*		A função live está deprecade e precisa do jquery migrate
	*	@author Mácio Matheus	
	*	@dataInicial 12-09-2013
	*	@ultimaEdicao 15-01-2014
	*/

	// tudo que tem prioridade no carregamento e execução de funções esseciais.
	$( document ).ready(function(){

		$("#load").delay("200").fadeOut( "slow", function(){ $(this).css("display", "none"); });

		// Pega do template seus blocos editáveis
		createModelBloco( "#blocos" );
		//Função que vai dar todos os ids, tudo que for inserido dinamicamente. Processa tudo que é necessário para o funcionamento básico
		updateIds(".main", ".main_blocos");
		
		createColorArea(); // Cria a area de cores

		// Aqui faz o controle de sempre aparecer a div que contem os blocos editáveis pra sempre ficar visivel pro usuário
		$( window ).scroll(function(){ 
			if($(window).scrollTop() > 198){ $("#area-insert-bloco").addClass("fixed-area-bloco");} 
			else { $("#area-insert-bloco").removeClass("fixed-area-bloco");	}		
		});

		// Efetua o Upload sem dar refresh na pagina / #imagem é o id do input, ao alterar o conteudo do input executa a função baixo
		$('#img').live("change", function(){  
		
			$('#loading').html('<div id="load" style="font-size:12px; text-align:center;"><img src="/images/buscando.gif" alt="Buscando" /><br>Aguarde</div>');
			
			$("#botaoImage").css("display","none");
		
			$('#formUpload').ajaxForm({ 
				target: '#result', // o callback será no elemento com o id #visualizar 
				success: function(){
					$("#main").find("img").each(function () {
						var src = $(this).attr("src");
						$(this).attr("src", pseudoBaseHrefAbs(src));
					});
					$("#loading").find("#load").css("display", "none");  // Faz o gif "carregando" sumir
					$("#botaoImage").css("display","inline-block");
				}
			}).submit(); 
		});
		//faz a frescura do esmaecer a pagina
		$("#main").delay("120").fadeIn( "fast", function(){ $(this).css("display", "block"); });
		// Seleciona o texto do input do nome da pagina
		$("#namePage").select();
		// Aqui verifico se é uma edição, atraves do hidden passado no html
		if($("#copy").val() == "true"){
			alert('Cópia realizada com sucesso.');
		}

		// Desative o botão direito do mouse
		$(document).bind("contextmenu",function(e){ 
		    return false; 
		}); 
		// Bloqueia a abertura do console (chrome)
		$(document).keydown(function (e){
            var s = null;
		    if (e.ctrlKey) {s= 1;} else {s = 0;}	 
            if (e.shiftKey) {s = s+1;} else {s = s;}	
            if (e.which == 74 || e.witch == 106) {
				if(s == 2){e.preventDefault(); s=0; return false;}	
		    }    
        });

        setInterval("sessionRevalidate()", 600000);
		
		// Flag que detecta mudanças no template
		change = false;
	});
	// END ONLOAD

	function sessionRevalidate () {
		$.ajax({
			type: 'POST',
			url: "proc.jsp",
			data: {actionID: 150},
			success: function(data) {
				console.log(data);
			}
		});	
	}

	// Faz a operação de preferencia do balão
	function prefBal(classe) {
		$.ajax({
			type: 'POST',
			url: "proc.jsp",
			data: {actionID: 17, prefName: classe, prefVal: true},
			success: function(data) {
				if(data.trim() == "ok"){
					$(classe).delay("800").fadeOut( "slow", function(){
							$(this).css("display", "none");
					});
				}
			}
		});	
	}

	// fechar balão
	function closeBal(classe) {
		$(classe).delay("500").fadeOut( "slow" );
	}

	/**
	*	Primeira função a ser executada sempre, le o template que foi carregado e extrai deles os blocos editáveis e monta numa lista ordenada
	*	@param div: é o destino onde vai ser colocado o html formado por ela no final de sua execução
	*/
	function createModelBloco( div ){
		// Parametros que vem do html
		var edit = $("#edit").val(); // parametro de um campo hidden que indica se é um caso de edição
		var src = $("#src").val(); // caminho do original.html
		var container = "#aux"; // Lugar onde ele vai buscar Div auxiliar que uso pra carregar o bloco original
		var srcDir = $("#srcDir").val(); // Srcpra carregar o arquivo original no diretorio

		$.ajax({
			type: 'GET',
			url: srcDir,
			async: false,
			success: function(data) {
				$(container).html( data);
				console.clear();
				$("#aux").find("img").each(function () {
					var src = $(this).attr("src");
					$(this).attr("src", pseudoBaseHref(src));
				})
				$("#aux").find("a").each(function () {
					$(this).attr("href", "http://")
				})
			}
		});	

		// Aqui eu verifico se é um caso de edição de uma página criada, se for, ele vai extrair os blocos de 
		var content = "<div class='area-blocos' style='left: -275px;' id='area-insert-bloco'>"+
						  "<div class='tit-painel1'><h2>Inserir blocos</h2><span onClick='hideInsertBloco()'>Cancelar</span></div>"+
						  "<ul class='list-none menu-blocos' id='ul'>"+
						  "</ul>"+
					  "</div><!-- END area BLOCOS -->";
		$( div ).append(content);	
		
		// Aqui a mágica acontece, procuro todos os edit blocos, filtro pelo IF os que são Primários, ou seja, que tem uma descrição
		$(container).find(".edit-bloco").each(function(){
			var desc = $(this).attr("desc");
			var cssBloco = $(this).attr("style");
			if( typeof desc !== 'undefined' ){
				var blocoHtml = "<div class='edit-bloco' desc='" + desc + "' style='" + cssBloco + "' >"+$(this).html()+"</div>"; // Detalhe que pego o conteudo dela e adiciono na div origem, SEM O DESC
				var li = "<li>"+
							"<a href='javascript:void(0)'>"+desc+"</a>"+
							"<div class='exemplo-bloco'>"+
								"<div class='insert-edit-bloco'><!-- bloco - titulo / texto -->"+
									blocoHtml+
								"</div>"+
							"</div>"+
						 "</li>";
				$("#ul").append(li); // Jogo o LI, dentro da ul de id #ul ^^
			}		
		});  
	}
	// função que esconde a barra de inserir blocos 
	function hideInsertBloco() {
		var valueLeft = $("#area-insert-bloco").css("left");
		if( valueLeft == "0px"|| valueLeft == "0"){
			$("#area-insert-bloco").animate({"left": "-275px"});
			$(".exemplo-bloco").css("display", "none");
		}
	}

	/**
	*	Função que chama as outras duas funções que atribuem os ids dos elementos trabalhados no template e seus blocos
	*	@param main é a div que envolve todo o template carregado, esta localizado no edit-template.jsp
	*	@param mainBlocos é a div que envolve todo a área de blocos do template , esta localizado no edit-template.jsp
	*	@return lastId o ultimo id atribuido. Não é utilizado fora deste contexto.. retorna apenas por retornar
	*/
	function updateIds( main, mainBlocos){
		// Esse foreach remove as divs da classe .mark, que eu coloco afim de envolver os textos com divs em branco
		$(main).find(".mark").each(function () {
			var content = $(this).html();
			$(this).parent().html(content); // Pega o conteudo da div, sava na div de fora e depois remove a div mark
			$(this).html("");
			$(this).remove();
		});

		lastId = readTemplate(main);
		lastId = readEdt(lastId, mainBlocos);
		return lastId;
	}

	/**
	*	Função que atribui os ids, onclicks, inclusoes de elementos dinamicamente de todo o template
	*	Quando desejar incluir novo item basta adicionar um each com a classe a ação pretendida
	*	Os parametros são strings que podem ser classes ou ids de elementos do html
	*	Obs:Lembrando que a função segue um padrão, ou seja, a forma como o html foi feito, pois se baseia bem especificamente em determinadas tags e suas ordens de posição
	*	----------------------------------------------------------------------------------------
	*	Descrição das classes que a função manipula
	*	----------------------------------------------------------------------------------------
	*	@class .edit-bloco == blocos que podem ser editados
	*	@class .edit-text == Caixas de texto 
	*	@class .edit-img == Caixas de imagem
	*	@class .delete-item2 == botoes de delete item da div
	*	@class .inserir-bloco == botoes que inserem os blocos no template
	*	@class .delete-item == botoes de excluir caixas de texto, caixas de imagem
	*	@class .edit-link == classe semelhante ao edit-text, só que de link =S
	*	----------------------------------------------------------------------------------------
	*	@return retorna o ultimo id enumerado. Necessário pra outras funções
	*/
	function readTemplate( mn ){
				
		var main = mn; // Div main
		
		// divs e spans que serão inclusos no template puro
		var inserirBloco = "<span class='inserir-bloco' title='Inserir bloco'></span>";
		var moveUp =  "<span class='mover-cima'  title='Mover bloco para cima'></span>";
		var moveDown = "<span class='mover-baixo' title='Mover bloco para baixo'></span>";
		var deleteItem = "<span class='delete-item' title='Excluir item'></span>";
		var deleteItem2 = "<span class='delete-item2' title='Excluir bloco'></span>";
		
		// classes sempre iniciam com underline "_" (pelo menos nessa função aqui)
		var _editBloco = ".edit-bloco";	var _editImg = ".edit-img";
		var _editText = ".edit-text";	var _editLink = ".edit-link";
		var _moveUp = ".mover-cima";	var _moveDown = ".mover-baixo";
		var _deleteItem2 = ".delete-item2";	var _deleteItem = ".delete-item";
		var _insertBloco = ".inserir-bloco"; var _fixedBloco = ".fixed-bloco";
		
		// Aqui é se caso algum dos foreach não encontrar nada, evita que de pau xD Vou melhorar ainda; Gambiarra do bem
		var lastId1 = 100;	var lastId2 = 500;	var lastId3 = 900;	var lastId4 = 1400;
		var lastId5 = 2000;  var lastId6 = 2600;	var lastId7 = 3200;	var lastId8 = 4100;

		// Esse trecho garante que os appends feitos anteriormente sejam removidos, se nao forem, vao ficar duplicados a cada chamada de função
	
		$(main).find(".nav").each(function(){ $(this).remove(); });

		// Inserindo as divs fixed bloco no documento pra basear a navegação cima/ baixo
		if($(main).find(".fixed-bloco").length != 2){
			$(main).find( ".edit-bloco:first" ).before("<div class='fixed-bloco'></div>");
			$(main).find( ".edit-bloco:last" ).after("<div class='fixed-bloco'></div>");
		}

		// Enumera os blocos 
		$(main).find(_editBloco).each(function(i){
			i++;
			var idEditBloco = i;
			$(this).attr("id", idEditBloco);
			$(this).append("<span class='nav'>" + moveUp + moveDown  + deleteItem2 + inserirBloco + "</span>"); // Inclui os spans move cima, baixo, delete
			$(this).find(_deleteItem2).attr("onclick", "removeItem('#" + idEditBloco + "', '-1')");
			$(this).find(_moveDown).attr("onclick", "moveDown( '#" + idEditBloco + "' )");
			$(this).find(_moveUp).attr("onclick", "moveUp( '#" + idEditBloco + "' )");
			$(this).find(_insertBloco).attr("onclick", "sendId( '#" + idEditBloco + "' )");
			lastId1 = idEditBloco;
		});
		
		$(main).find(_editText).each(function(i){
			i++;
			var idEditText = i+lastId2;
			$(this).wrapInner("<div class='mark' onclick=showEditText('#"+ idEditText +"') ></div>"); // Envolve o texto com a div
			$(this).attr("id",idEditText);
			$(this).append(deleteItem);
			$(this).find(_deleteItem).attr("onclick", "removeItem('#"+idEditText+"')");
			lastId2 = idEditText;
		});
		// enumera os edit link
		$(main).find(_editLink).each(function(i){
			i++;
			var idEditLink = i+lastId3;
			$(this).attr("id",idEditLink);
			$(this).append( deleteItem );
			$(this).find('a').wrapInner("<div onclick=showEditLink('#"+ idEditLink +"') class='mark'></div>"); 
			$(this).find(_deleteItem).attr("onclick", "removeItem('#"+ idEditLink +"', '-2 ')"); // O -2 indica que é um link a ser excluido
			lastId3 = idEditLink;
		});
		// enumera as caixas de edit imagem e guarda todos em um array
		$(main).find(_editImg).each(function(i){
			i++;
			var idEditImg = i+lastId4;
			$(this).wrapInner("<div class='mark'></div>");
			$(this).attr("id",idEditImg);
			$(this).append(deleteItem);
			$(this).find(_deleteItem).attr("onclick", "removeItem('#"+idEditImg+"', '-1' )");
			lastId4 = idEditImg;
		});
		
		// enumera todos os "a"(links)
		$(main).find("a").each(function(i) {
			i++;
			var idA = i+lastId5;
			$(this).attr("id", idA);
			var thisHref = $(this).attr("href");
			$(this).attr("onclick", "return false;"); // Se não adicionar isso, os links vão disparar sem dó
			lastId5 = idA;
		});
		
		// enumera os img
		$(main).find("img").each(function(i){
			i++;
			var src = $(this).attr("src");
			$(this).attr("src", pseudoBaseHref(src));
			
			var idImg = i+lastId6;
			if ($(this).attr("class") != "meukit-prop") { // Verifica se é a img de propaganda
				
				$(this).attr("id",idImg);
				var alt = $(this).css("max-height");
				var larg = $(this).css("max-width"); 
				
				$(this).attr("onclick","showEditImage( '#" + idImg + "', '" + alt + "', '" + larg + "', '" + pseudoBaseHref(src) + "' )");	
			}
			lastId6 = idImg;
		});

		// Trata as imagens em background que aparecem na TD
		$("td").each(function(){ // Fiz ele tratar todas as TDS da pagina... 
			var bg = $(this).attr("background");
			if (typeof bg != 'undefined') {
				var src = $(this).attr("background");
				$(this).attr("background", pseudoBaseHref(src));
			}
		});

		//Só coloca o caminho absoluto da página no primeiro bloco "se a pg nao abrir clicque aqui"
		$(main).find(".link-fixo").each(function () {
			var src = $("#srcExterno").val();
			$(this).attr("target", "_blank");
			$(this).attr("href", src);
		});

		// Link de descadastramento
		$(main).find(".link-unregister").each(function () {
			var src = $("#unregister").val();
			$(this).attr("target", "_blank");
			$(this).attr("href", src);
		});

		return lastId6; // Ultimo id 
	}
	
	// Monta os src ou qualquer coisa que seja, simulando o base href
	function pseudoBaseHref (src) {
		if (src.search("http://") == -1) {
			var srcAbsolute = $("#srcExterno").val();	
			srcNew = srcAbsolute + src;
			return srcNew;
		} 
	}

	// Monta com o caminho absoluto
	function pseudoBaseHrefAbs (src) {
		if (src.search("http://") == -1) {
			var srcAbsolute = $("#srcAbsolut").val();	
			srcNew = srcAbsolute + src;
			return srcNew;
		} 
	}

	/** 
	*	Também enumera o edit template, onde ficam os blocos editáveis que podem ser inseridos
	*	@param ultimo id inserido 
	*	@param classe que contem os blocos
	*/
	function readEdt( lastId, mainBlocos ){
		var reference = mainBlocos;
		$(reference).find("a").each(function(i){
			i++;
			var idA = i+lastId;
			$(this).attr("id", idA);
			lastId = idA;
		});
		
		$(reference).find("li").each(function(i){
			i++;
			var idEditBloco2 = i+lastId;
			$(this).attr("id", idEditBloco2);
			lastId = idEditBloco2;
		});
		return lastId;
	}

	/**
	*	Essa função pega o ID do bloco do template que chamou e adiciona na parte dos blocos de edição dos templates 
	*	a função "addBloco", passando por paramentro o ID de quem chamou a sendID (no caso é a div "inserir-bloco") e o ID	
	*	do próprio link, que serve de referência para addBloco encontrar o novo bloco que será inserido.
	*	@param idOrigin: Id do "insert-bloco" que chamou a função
	*/
	function sendId( idOrigin ){
		//faz a animação pra aparecer
		$("#area-insert-bloco").animate({"left": 0});
		$(".exemplo-bloco").each( function(){ $(this).removeAttr("style"); }); // remove o atributo style pra que o elemento possa aparecer novamente
		$('.main_blocos').find('li').find('a:first').attr("onclick", "addBloco( '"+idOrigin+"', this.id )");
	}
	
	/**
	*	Essa função adiciona os blocos ao template, usando como referencia o id do link que serve para chegar apartir dele 
	*	no bloco que desejamos inserir no teplate imediatamente após o id de origem que foi passado pela senId
	*	obs: Uso o $(".main-blocos").find, pra garantir que sempre estarei buscando dentro da div que o jsp inclui a pagina html
	*	Chamo a função updateIds(), pois ela é responsável por atualizar a numeração dos ids de todos os ítens da pagina
	*/
	function addBloco( origin, idLinkA ){
		change = true; // Flag que indica que o usuario fez alguma modificação, definida no onload como false

		htmlCont = ("<div class='edit-bloco'>" + $(".main_blocos").find("#"+idLinkA).next().find('.edit-bloco').html() + "</div>");
		
		$(htmlCont).insertAfter( origin );
		$(origin).next(".edit-bloco").css({"display":"none"}); // oculta pra depois mostrar denovo com fade	

		// Aqui faz a adaptação pra o caso do template conter blocos 100%
		$(origin).next('.edit-bloco').delay(100).slideDown( "slow", function(){
			
			var cemporcento = $(".template").attr("cemporcento"); // atributo que identifica se o template tem blocos 100%
			var strCss = ""; // Recebe o que tem dentro do atributo 'cemporcento'
			if( typeof cemporcento !== 'undefined' ){
				if($(this).find("table:first").attr("width") != "100%"){ 
					$(this).attr("style", cemporcento); 	
				}
			} else {
				$(this).removeAttr("style");
			}	
		} );

		updateIds(".main", ".main_blocos"); // Sempre passar os parametros da update ids!!!
		hideInsertBloco(); // animação ocultar
	}
	
	/**
	*	Essa função vai limpar todo o template, retirando tudo que as outras funções incluiram anteriormente, deixando o código "puro" e em seguida
	*	"salva"(envia por ajax pro lado servidor) o template. 
	*/
	function saveTpl(){
		// Declaração das variáveis de parametros
		var fixedBloco = ".fixed-bloco";
		var editBlocos = ".edit-bloco";
		var editText = ".edit-text";
		var editImg = ".edit-img";
		var deleteItem = ".delete-item";
		var deleteItem2 = ".delete-item2";
		var insertBloco = ".inserir-bloco";
		var editLink = ".edit-link";
		var main = ".main"; // Div main
		var mark = ".mark"; // Div que inclui dinamicamente envolvendo todos os textos, tenho sempre que retirar
		
		// Parâmetros que vem dos campos hidden da pagina html
		var _codAcess = ""; // Esse é o codigo de acesso da pagina, preciso dele no proc é usada mais abaixo no AJAX
		var _idPage = ""; // Esse é o codigo da pagina que vai ser salva(na verdade ja esta salva, apenas vai sofrer uma reescrita), é usada mais abaixo no AJAX
		var _namePage = ""; // Nome da pagina
		var template = "";
		var templateModf = "";
	
		// Faz o gif aparecer novamente
		$("#load").delay("150").fadeIn( "fast" );

		// Abre o balaozinho do download e muda a classe do botão de download deixando ele verde ^^
		$("#down").removeClass("bt-down ");
		$("#down").addClass("bt-down-green");

		$(".downloadMesseger").delay("2000").fadeIn( "slow"	);
		
		$(main).find("a").each(function() {
			$(this).removeAttr("id");
			$(this).removeAttr("onClick");

			var href = $(this).attr("href");

			if (href == "http://" || href == "" || href == "javascript: void(0)" || href == "http://javascript:void(0)") {
				$(this).attr("href", "javascript: void(0)");
				$(this).removeAttr("target");
			} else {
				// se o usuario nao botar o http essa linha vai colocar
				if (href.search("http://") == -1) {
					href = "http://" + href; 
					$(this).attr("href", href);
				}	
				$(this).attr("target", "_blank"); 
			}	
		});
		
		$(main).find("img").each(function(){
			var src = $(this).attr("src");
			$(this).removeAttr("onClick");	
			$(this).removeAttr("id");
		});
		
		$(main).find(editImg).each(function(){
			$(this).removeAttr("id");
			$(this).find("div").removeAttr("onclick");
			$(this).find(deleteItem).remove();
		});
		
		$(main).find(".deleteItem2").each(function() { $(this).remove(); })

		$(main).find(editLink).each(function(){
			$(this).removeAttr("id");
			$(this).find(deleteItem).remove();
		});
		
		$(main).find(editText).each(function(){		
			$(this).removeAttr("id");
			$(this).find(deleteItem).remove();
		});
		
		$(main).find(editBlocos).each(function(i){
			$(this).removeAttr("id");
			$(this).find(".nav").remove(); // Remove as divs que foram colocadas envolvendo os spans de navegação e exclusão
		});

		// Remove os fixed bloco
		$(main).find(fixedBloco).each(function() { $(this).remove(); });

		// Essa função remove as divs da classe .mark, que eu coloco afim de envolver os textos com divs em branco
		$(main).find(mark).each(function () {

			var content = $(this).html();
			$(this).parent().html(content); // Pega o conteudo da div, sava na div de fora e depois remove a div mark
			$(this).html("");
			$(this).remove();
		});

		$("#main").css("display", "none");

		// Procura a tag de pageview e remove pra nao duplicar		
		$(main).find(".pgv").each(function(){ 
			$(this).remove(); 
			//console.log("Achou e removeu");
		});

		 template = "<div class='template'>" + $("#main").find(".template").html() + "</div>";
	
		_codAcess = $("#codAcess").val(); // Esse é o codigo de acesso da pagina, preciso dele no proc
		_idPage = $("#idPage").val(); // Esse é o codigo da pagina que vai ser salva(na verdade ja esta salva, apenas vai sofrer uma reescrita)
		_namePage = $("#namePage").val(); // Nome da página INPUT text
		
		if (_namePage == "") {	_namePage = "Nova mensagem"; }

		var newJson = createJson();	
		var aux = $("#aux").html();

		newJson = replaceAcents(newJson);
		aux = replaceAcents(aux);
		template = replaceAcents(template);

		$(main).find("a").each(function() {
			var href = $(this).attr("href");
			
			if (href.search("redirect") == -1) {
				if (href.search("javascript:") == -1 && href.search("/p/") == -1) {
					var linkVal = $(this).html();

					// Se for uma tag image o conteudo do link, pega apenas o src dela
					if (linkVal.search('<img') != -1) {
						linkVal = $(this).find("img").attr("src");
					}

					href = "http://www.meukit.com.br/redirect.jsp?l=" /**"http://192.168.0.108:8080/redirect.jsp?l="*/ + encodeURIComponent($(this).attr("href").trim()) + "&linkval=" + encodeURIComponent(linkVal) + "&id=[#mk#]";
					$(this).attr("href", href);
				}
			}
		});

		// Inclui a tag que conta o pageview Se nao existir a pageview inclui
		templateModf = "<div class='template'>" + $("#main").find(".template").html() + "<img class='pgv' src='http://www.meukit.com.br/redirect.jsp?id=[#mk#]'  height='1' width='1'>" + "</div>";
		
 		$.ajax({
			type: 'POST',
			dataType: 'HTML',
			url: '/proc.jsp',
			async: true, // Se der algum pau ou temora pra gravar, mudar pra TRUE,
			data: {actionID: 7, tpl: template, codAcess: _codAcess, idPage: _idPage, namePage: _namePage, json: newJson, original: aux.trim(), tplModf: templateModf},
			success: function(data) {
				
				alert("Mensagem salva com sucesso!");
				window.setTimeout('location.reload()', 2500); //reloads after 2,5 seconds
				change = false; // Flag que indica que o usuario fez alguma modificação, definida no onload como false
				
			}
		});
	}
	
	// Replace no template
	function replaceAcents(aux) {
		// Parser dos caracteres html
		aux = replaceAll(aux, "ç", "&ccedil;");	aux = replaceAll(aux, "Ç", "&Ccedil;");
		aux = replaceAll(aux, "ã", "&atilde;");	aux = replaceAll(aux, "Ã", "&Atilde;");
		aux = replaceAll(aux, "Á", "&Aacute;");	aux = replaceAll(aux, "á", "&aacute;");
		aux = replaceAll(aux, "Â", "&Acirc;");	aux = replaceAll(aux, "â", "&acirc;");
		aux = replaceAll(aux, "à", "&agrave;");	aux = replaceAll(aux, "À", "&Agrave;");
		aux = replaceAll(aux, "é", "&eacute;");	aux = replaceAll(aux, "É", "&Eacute;");
		aux = replaceAll(aux, "Ê", "&Ecirc;");	aux = replaceAll(aux, "ê", "&ecirc;");
		aux = replaceAll(aux, "í", "&iacute;");	aux = replaceAll(aux, "Í", "&Iacute;");
		aux = replaceAll(aux, "Ì", "&Igrave;");	aux = replaceAll(aux, "ì", "&igrave;");
		aux = replaceAll(aux, "õ", "&otilde;");	aux = replaceAll(aux, "Õ", "&Otilde;");
		aux = replaceAll(aux, "Ô", "&Ocirc;");	aux = replaceAll(aux, "ô", "&ocirc;");
		aux = replaceAll(aux, "ó", "&oacute;");	aux = replaceAll(aux, "Ó", "&Oacute;");
		aux = replaceAll(aux, "ú", "&uacute;");	aux = replaceAll(aux, "Ú", "&Uacute;");
		aux = replaceAll(aux, "û", "&ucirc;");	aux = replaceAll(aux, "Û", "&Ucirc;");
		aux = replaceAll(aux, "©", "&#169;");

		return aux;
	}

	// Função replace pra string
	function replaceAll(str, de, para){
		var pos = str.indexOf(de);
		while (pos > -1){
			str = str.replace(de, para);
			pos = str.indexOf(de);
		}
		return (str);
	}

	/** 
	*	Funções do editText 
	*	A seguir a manipulação de todos os eventos do editText; confirm; cancel; showEditText;
	*/
	function showEditText( idEdT ){
		change = true; // Flag que indica que o usuario fez alguma modificação, definida no onload como false

		var divContent = $(idEdT).find('div').html(); // Conteúdo html tratado
		
		$("#editText").css("display", "block");
		
		CKEDITOR.instances.textAreaText.setData( divContent );
		
		$("#textAreaText").val( divContent );
		$("#textAreaText").focus();
		$(".conf-text").attr("onclick", "confirmT( '"+idEdT+"')");
		dimension(); // Chamando essa função que dimensiona a caixa e evita um bug tosco do chrome
	}

	/** Fecha (deixa de exibir) a caixinha de editText  */
	function hideEditText(){
		$("#textAreaText").val("");
		$(".conf-text").attr("onclick", "");	
		$("#editText").css("display", "none");
		unDimension();
	}

	// Add na função show
	function confirmT(id){
		var textAreaVal = editor_data = CKEDITOR.instances.textAreaText.getData();
		
		// Uso o replace declarado acima pra remover os <p></P> que o ckeditor coloca
		textAreaVal = textAreaVal.replace("<p>", ""); // Remove só o primeiro p
		textAreaVal = replaceAll(textAreaVal, "<p>", "<br /><br />"); 
		textAreaVal = replaceAll(textAreaVal, "</p>", "");
		
		$(id).find('div').html(textAreaVal);
		$("#editText").css("display", "none");
		$("#textAreaText").val("");
		$(".conf-text").attr("onclick", "");	
		unDimension();
	}
	

	// É adicionada no proprio onclick do codigo html
	function cancelT(){
		$("#textAreaText").val("");
		$(".conf-text").attr("onclick", "");	
		$("#editText").css("display", "none");
		unDimension();
	}

	// Função que dribla um bug do google chrome que desativa o ckeditor diminui o tamanho em 1%
	function dimension (){
		var todos_elementos = document.getElementsByTagName('*');
		for (var i=0; i<todos_elementos.length; i++){
			var el = todos_elementos[i];
			if (el.className == 'cke_wysiwyg_frame cke_reset'){
				el.style.width="99%";
			}
		}
	}
	
	// Função que dribla um bug do google chrome que desativa o ckeditor aumenta o tamanho em 1%
	function unDimension (){
		var todos_elementos = document.getElementsByTagName('*');
		for (var i=0; i<todos_elementos.length; i++){
			var el = todos_elementos[i];
			if (el.className == 'cke_wysiwyg_frame cke_reset'){
				el.style.width="100%";
			}
		}
	}

	/** 
	*	Funções do editImg INCOMPLETA
	*	A seguir a manipulação de todos os eventos do edit-link; confirm; cancel; showEditText;
	*/
	function showEditImage ( idEdI, alt, larg, src ){
		change = true; // Flag que indica que o usuario fez alguma modificação, definida no onload como false
		var _editImg = "#editImage";
		var hrefSrc = $(idEdI).parent("a").attr("href"); // pego o href do link que ta envolvendo a img no momento do click 
		if(hrefSrc == "javascript: void(0)" || hrefSrc == "" || hrefSrc == "http://javascript:void(0)" || hrefSrc == "javascript:void(0)"){
			hrefSrc = "http://"; // Se for vazio boto http que é pro cara botar o resto
		}

		$("#link-image").val(hrefSrc);
		$(_editImg).find("#srcDelete").attr("value", src);
		$(_editImg).find("#larg").attr("value", larg);
		$(_editImg).find("#alt").attr("value", alt);
		$(_editImg).css("display", "block");
		$(_editImg).find("button").attr("onclick","confirmI('" + idEdI + "')");
		$("#link-image").select();
	}

	// Esconde a caixa de edit image
	function hideEditImg(){

		var _editImg = "#editImage";
		$("#result").html("");
		
		// Limpar o campo de imagem
		$('#img').replaceWith($('#img').clone());
		$("#img").val("");
		$(".conf-text").attr("onclick", "");	
		$(_editImg).find("#larg").attr("value", "");
		$(_editImg).find("#alt").attr("value", "");
		$(_editImg).css("display", "none");
	}

	// Add na função show
	function confirmI(id){
		var _editImg = "#editImage";
		var src = $(_editImg).find("#result").find("img").attr("src");
		$(id).attr("src", src);
		var linkImage = $("#link-image").val(); // Link opcional da imagem
		$(id).parent('a').attr("href", linkImage);
		// Limpar o campo de imagem
		$('#img').replaceWith($('#img').clone());
		$("#img").val("");
		$("#link-image").val("");
		$(_editImg).find("#larg").attr("value", "");
		$(_editImg).find("#alt").attr("value", "");
		$(_editImg).css("display", "none");
		$(_editImg).submit(function () { return false; });
		if( $("#img").val() == "" ){ 
			return false;  
		}
	}

	// É adicionada no proprio onclick do codigo html
	function cancelI(){
		var _editImg = "#editImage";
		$("#result").html("");
		// Limpar o campo de imagem
		$('#img').replaceWith($('#img').clone());
		$("#img").val("");
		$(".conf-text").attr("onclick", "");	
		$(_editImg).find("#larg").attr("value", "");
		$(_editImg).find("#alt").attr("value", "");
		$(_editImg).css("display", "none");
	}

	/** 
	*	Funções do editLink;  manipulação de todos os eventos do edit-link; confirm; cancel; showEditText;
	*/
	function showEditLink( idEdL ){
		
		change = true; // Flag que indica que o usuario fez alguma modificação, definida no onload como false

		var linkHrefContent = $(idEdL).find("a").attr("href");
		var linkTextContent = $(idEdL).find("a").text().trim();
		
		$("#editLink").css("display", "block");
		$(".campo-edit-link1").find("#textAreaLink").val(linkTextContent);
		$("#textAreaLink").select();
		if(linkHrefContent == "javascript: void(0)" || linkHrefContent == "" || linkHrefContent == "http://javascript:void(0)"){
			var hrefSrc = "http://"; // Se for vazio boto http que é pro cara botar o resto
			$("#inputLink").val(hrefSrc);
		}
		// Ta feio mas vai ser assim mesmo 
		if (linkHrefContent != "" && linkHrefContent != "#" && linkHrefContent != "javascript:void(0)" && linkHrefContent != "http://javascript:void(0)" && linkHrefContent != "javascript: void(0)") {
			$("#inputLink").val(linkHrefContent);
		}
		$(".conf-link").attr("onclick", "confirmL( '" + idEdL + "')");
	}

	function hideEditLink(){
		$("#textAreaLink").val("");
		$("#inputLink").val("http://");
		$(".conf-link").attr("onclick", "");	
		$("#editLink").css("display", "none");
	}

	function confirmL(id){
		var textAreaVal = $("#textAreaLink").val(); //texto do link
		var hrefLink = $(".campo-edit-link2").find("#inputLink").val();

		// Ficou mal feito, mas funciona.. ele verifica se a tag img existe, se existe ele nao altera o conteudo .txt() do link se nao ele muda o text 
		if($(id).find("a").find("img").length > 0){
			$(id).find("a").attr("href", hrefLink);	
			$("#editLink").css("display", "none");
			$("#textArea").val("");
			$(".conf-link").attr("onclick", "");	
		} else {
			$(id).find("a").find('.mark').text(textAreaVal);	
			$(id).find("a").attr("href", hrefLink);		
			$("#editLink").css("display", "none");
			$("#textArea").val("");
			$("#inputLink").val("http://");
			$(".conf-link").attr("onclick", "");
		}	
	}

	function cancelL(){
		$("#textAreaLink").val("");
		$("#inputLink").val("http://");
		$(".conf-link").attr("onclick", "");	
		$("#editLink").css("display", "none");
	}

	/** Funções de ações de blocos, como inserir, remover e mover */
	function removeItem( id, typeExclude){
		// Exclui os links
		if( typeExclude == -2 ) {
			$(id).fadeOut("fast", function(){
				$(this).parent('.edit-link').removeAttr("onclick");
				$(this).closest('table').remove(); // remove a td
				$(this).remove(); 
			});
		}
		// se for -1, indica a exclusão de um fixed bloco, e nao exclui a td acima, pq se não explode tudo xD	
		if (typeExclude == -1 || typeExclude == undefined || typeExclude == null ) {
			$(id).fadeOut("fast", function(){
				$(this).remove(); 
			});
		}
	}
	/**
	*	Funções de mover o bloco
	*/
	function moveDown( id ){
		
		var thisId = id; // Id de quem vai ser movido
		var _main = ".main";
		var _editBloco = ".edit-bloco";
		var desc = $(thisId).attr("desc");// Pego o atributo de descrição se houver
		var cssBloco = $(thisId).attr("style");
		
		// Verifica se a classe fixed-bloco está abaixo, se sim, retorna falso, pois indica que eh a limitação da area navegável
		if( $(thisId).next("div").attr("class") != "fixed-bloco" ){
		
			$(_main).find(".edit-bloco").find("span:empty").each(function () {
				if($(this).attr("class") == "nav"){ $(this).remove(); }
			})

			var temp = ((typeof desc === 'undefined') ? "<div class='edit-bloco' style='"+cssBloco+"'>" + $(thisId).html() + "</div>" : "<div desc='"+desc+"'class='edit-bloco' style='"+cssBloco+"'>"+$(thisId).html()+"</div>") ;
			var nextEditBlocoId = "#"+$(thisId).next(_editBloco).attr("id"); // pega o id do bloco onde será inserido o bloco a ser movido
			
			$(thisId).remove();
			$(_main).find(nextEditBlocoId).after(temp);

			$(_main).find(nextEditBlocoId).next(_editBloco).css("display", "none");
			
			// Faz a frescura de esmaecer na tela  ^^
			$(_main).find(nextEditBlocoId).next(_editBloco).delay(400).show( "slow"	);

			updateIds(".main", ".main_blocos");
		} else {
			return false;
		}	
	}
	/**
	*	Navega pra cima
	*/
	function moveUp( id ){
		// Id já vem com o # precedendo o numero.
		var thisId = id;
		var _main = ".main";
		var _inserirBloco = ".inserir-bloco";
		var _editBloco = ".edit-bloco";
		var desc = $(thisId).attr("desc"); // Pego o atributo de descrição se houver
		var cssBloco = $(thisId).attr("style"); 
		
		// Verifica se a classe fixed-bloco está acima, se sim, retorna falso, pois que eh a limitação da area navegável	
		if( $(thisId).prev("div").attr("class") != "fixed-bloco" ){

			// isso aqui precisa ser verificado comportamento
			$(_main).find(".edit-bloco").find("span:empty").each(function () {
				if($(this).attr("class") == "nav"){ $(this).remove(); }
			})

			var temp = ((typeof desc === 'undefined') ? "<div class='edit-bloco' style='"+cssBloco+"'>" + $(thisId).html() + "</div>" : "<div desc='"+desc+"'class='edit-bloco' style='"+cssBloco+"'>"+$(thisId).html()+"</div>") ;
			
			var prevEditBlocoId = "#"+$(thisId).prev(_editBloco).attr("id"); // pega o id do elemento acima do conteudo que vamos mover
			
			// Remove da tela o item salvo acima
			$(thisId).remove();
			
			// Pega o elemento acima(irmao) do elemento  e insere conteudo
			$(_main).find(prevEditBlocoId).before(temp); 
			
			$(_main).find(prevEditBlocoId).prev(_editBloco).css("display", "none");

			// Faz a frescura de esmaecer na tela  ^^
			$(_main).find(prevEditBlocoId).prev(_editBloco).delay(100).show( "slow"	);
			updateIds(".main", ".main_blocos");

		} else {
			return false;
		}	
	}

	/**
	*	Função que cria a parte de alteração de cores do template
	*	@autor Mácio Matheus
	*	Data: 22-10-2013
	*/
	function createColorArea() {
	    var srcJson =  $("#srcJson").val();
	   
	    $.getJSON( srcJson , function ( json ){
	      	$.each(json, function(arrayID, pallet) {
	      		var i = arrayID;
  				i++;
  				var id = 6000; // Numero aleatório, pro id não ficar proximo
  				id = id+i;
	      		$("#areaColor").append("<div class='pallet' style='white-space:nowrap;' id='" + id + "'></div>");
      			$.each( pallet.PARAMS, function  (paramsDesc, paramsData) {
      				//Gambiarra pro id não se repitir
      				idRow = id + 1 + paramsDesc;
					var attr = paramsData.ATTR;
					var classe = "."+paramsData.CLASS;
					$("#"+id).append( 
						"<div desc='." + paramsData.CLASS + "' pall='"+pallet.PALLET+"' att='"+ attr +"' id='"+ idRow +"' class='secao-cor'>"+
						  "<span class='subtit-painel'>" + paramsData.DESC + "&nbsp;:</span>&nbsp;"+
						  "<span class='exemplo-cor-config colorSpan' title='Alterar cor' style='background-color:" + paramsData.COLOR + "'>"+
						  "</span>&nbsp;"+
						  "<span>"+
						  	"<input type='text' disabled class='input colorInputText' style='text-transform:uppercase; width: 50px; padding: 6px 5px;' value='" + paramsData.COLOR + "' />"+
						  "</span>&nbsp;&nbsp;"+ 
						  "<a href='javascript:void(0);' class='colorAhref bt-padrao1 text-3 f-normal'>"+
						  	"<img src='/images/icon-7.png' style='vertical-align: middle; margin-bottom: 2px;' alt='Icon' title='Selecione a cor na paleta de cores' />"+
						  "</a>"+
						"</div>"
					);
					// Paleta de cores
					colorPallets( idRow, attr, classe );
      			});	
			});
	    });
	}

    /**
    *	Essa função cria o json a partir da estrutura de cores (paleta lateral)
    *	Sempre se ligar pra não errar a sintaxe do JSON... 
    */
	function createJson () {
		newJson = "[";
		$("#areaColor").find('.pallet').each(function(j){
            var PALLET = $(this).find('.secao-cor').attr('pall');
			if( j > 0 ){
				newJson += ",";
			}
			newJson += '{ "PALLET" :' + '"' + PALLET + '"' + "," + '"PARAMS":['; 
			$("#areaColor").find('.secao-cor').each(function ( i ){
				var CLASS = $(this).attr('desc');
				var DESC = $(this).find('.subtit-painel').text();
				var ATTR = $(this).attr('att');
				var COLOR = $(this).find('.input').val();
				if( i > 0 ){
					newJson += ",";
				}
				newJson += "{" + 
								'"DESC":' + '"' + DESC.replace(":","").trim() + '",' + 
								'"CLASS":' + '"' + CLASS.replace(".","") + '",' + 
								'"ATTR":' + '"' + ATTR + '",' + 
								'"COLOR":' + '"' + COLOR + '"'+
							"}";
			});
      	    newJson += "]";
			newJson += "}";
		});	
		newJson += "]";
		return newJson;
	}

	/** Esta aqui, somente redireciona pra pagina "minhas mensagens"  quando o usuario clica no botao voltar/cancelar */
	function backMsg ( id ) {
		if (change) {
			var t = "Você realizou alterações nessa mensagem! Deseja retornar sem salvar as alterações?";
			var question = confirm(t);
			if(question){ location.href = '/index-paginas.jsp'; } else { return false; }
		} else {
			location.href = '/index-paginas.jsp';
		}
	}

	/** Esta aqui, somente redireciona pra pagina "minhas mensagens"  quando o usuario clica no botao voltar/cancelar */
	function backEdit () {
		if (change) {
			var t = "Você realizou alterações nessa mensagem! Deseja retornar sem salvar as alteções?";
			var question = confirm(t);
			if(question){ return true; } else { return false; }
		} else {
			return true;
		}
	}

	/**
	*	Função que configura a paleta de cores do spectrum
	*	@param idSeccion Id da "seção" bloco do html
	*	@param att : É o atributo que vai ser mudado no css, vem do json 
	*	@param classe: É a clsse que vai ser alterada pela cor 
	*/
	function colorPallets( idSeccion, att, classe ){

		var colorSpan = "colorSpan";
		var colorInputText = "colorInputText";
		var colorAhref = "colorAhref";

		var cColorSpan = ".colorSpan";
		var cColorInputText = ".colorInputText";
		var cColorAhref = ".colorAhref";

		idSeccion = "#"+idSeccion;

		$(idSeccion).find(".colorSpan").each(function () {	$(this).attr("id", colorSpan); });

		$(idSeccion).find(".colorInputText").each(function () { $(this).attr("id", colorInputText);	});

		$(idSeccion).find(".colorAhref").each(function () {	$(this).attr("id", colorAhref);	});

		$(idSeccion).find( cColorAhref, cColorInputText ).spectrum({
			color: $( idSeccion ).find( cColorSpan ).css("backgroundColor"),
			showInput: true, className: "full-spectrum",
			showInitial: true, showPalette: true,
			showSelectionPalette: true,	maxPaletteSize: 10,
			preferredFormat: "hex",	cancelText: "Cancelar",
			chooseText: "Selecionar", localStorageKey: "spectrum.demo",
			move: function (color) {}, show: function () {},
			beforeShow: function () {},
			hide: function (color) {
				$(idSeccion).find( cColorInputText ).val(color);
				$(idSeccion).find( cColorSpan).css("backgroundColor", color);

				/** Aqui é modificada a cor de fato no css do template */ 
				$("#main").find(classe).each(function () {	$(this).css( att, color );	});
				/** Aqui é modificada a cor dos blocos também */ 
				$("#blocos").find(classe).each(function () { $(this).css( att, color );	});
				$("#aux").find(classe).each(function () { $(this).css( att, color ); });
			},
			change: function() {},
			palette: [
				["rgb(0, 0, 0)", "rgb(67, 67, 67)", "rgb(102, 102, 102)","rgb(204, 204, 204)", "rgb(217, 217, 217)","rgb(255, 255, 255)"],["rgb(152, 0, 0)", "rgb(255, 0, 0)", "rgb(255, 153, 0)", "rgb(255, 255, 0)", "rgb(0, 255, 0)",
				"rgb(0, 255, 255)", "rgb(74, 134, 232)","rgb(0, 0, 255)","rgb(153, 0, 255)", "rgb(255, 0, 255)"],["rgb(230, 184, 175)", "rgb(244, 204, 204)", "rgb(252, 229, 205)","rgb(255, 242, 204)", "rgb(217, 234, 211)","rgb(208, 224, 227)", 
				"rgb(201, 218, 248)", "rgb(207, 226, 243)", "rgb(217, 210, 233)","rgb(234, 209, 220)","rgb(221, 126, 107)", "rgb(234, 153, 153)", "rgb(249, 203, 156)", "rgb(255, 229, 153)", "rgb(182, 215, 168)","rgb(162, 196, 201)","rgb(164, 194, 244)", 
				"rgb(159, 197, 232)", "rgb(180, 167, 214)", "rgb(213, 166, 189)",	"rgb(204, 65, 37)", "rgb(224, 102, 102)", "rgb(246, 178, 107)", "rgb(255, 217, 102)", "rgb(147, 196, 125)",	"rgb(118, 165, 175)","rgb(109, 158, 235)","rgb(111, 168, 220)", 
				"rgb(142, 124, 195)", "rgb(194, 123, 160)","rgb(166, 28, 0)", "rgb(204, 0, 0)", "rgb(230, 145, 56)", "rgb(241, 194, 50)","rgb(106, 168, 79)","rgb(69, 129, 142)", "rgb(60, 120, 216)", "rgb(61, 133, 198)", "rgb(103, 78, 167)", "rgb(166, 77, 121)",
				"rgb(91, 15, 0)","rgb(102, 0, 0)", "rgb(120, 63, 4)", "rgb(127, 96, 0)", "rgb(39, 78, 19)","rgb(12, 52, 61)", "rgb(28, 69, 135)", "rgb(7, 55, 99)","rgb(32, 18, 77)", "rgb(76, 17, 48)"]
				]
		});
	}

	// Função que faz o preview do template
	function previewTemplate () {

	  	var main = $("#main").html();
	  	$("#preview_content").append(main);
	  	$(".geral-site").css("display","none");

	  	$("#preview").fadeIn( "slow" );
		// Tratando o conteudo, removendo as classes e id, pra uma exibição limpa pro usuário
		$("#preview_content").find(".edit-bloco").each(function () {
			$(this).removeClass("edit-bloco");
			$(this).removeAttr("id");
		});
		
		$("#preview_content").find(".inserir-bloco").each(function () {
			$(this).removeClass("inserir-bloco");
			$(this).removeAttr("id");
		});
		
		$("#preview_content").find(".edit-link").each(function () {
			$(this).removeClass("edit-link");
			$(this).removeAttr("onClick");
			$(this).removeAttr("id");
		});
		
		$("#preview_content").find(".edit-img").each(function () {
			$(this).removeClass("edit-img");
			$(this).removeAttr("onClick");
			$(this).removeAttr("id");
		});

		$("#preview_content").find(".edit-text").each(function () {
			$(this).removeClass("edit-text");
			$(this).removeAttr("onClick");
			$(this).removeAttr("id");
		});

		$("#preview_content").find(".mark").each(function () {
			$(this).removeClass("mark");
			$(this).removeAttr("onClick");
			$(this).removeAttr("id");
		});

		$("#preview_content").find("img").each(function () {
			$(this).removeAttr("onClick");
			$(this).removeAttr("id");
		});

		$("#preview_content").find("a").each(function () {
			$(this).removeAttr("onClick");
			if ($(this).attr("href") != "javascript:void(0)") {
				$(this).attr("target", "_blank");
			}
		})

		$("#preview_content").fadeIn( "slow" );
	}
	/** 
	 *	Serve pra quando o usuário clicar no botão voltar da div de preview, ocultar desfazer o processo que for feito 
	 *	na função Preview template
	 */
	function previewBack () {
		$("#preview_content").css("display", "none");
		$("#preview").css("display", "none");
		$("#preview_content").html("");
		$(".geral-site").fadeIn( "fast" );
	}

	/**
	*	Função que inicia o processo de duplicação de mensagem
	*/
	function duplicateMesseger () {
		if (change) { alert("Antes de duplicar, salve as alterações atuais"); } 
		else {
			$("#duplicate").css("display", "block");
			$("#input-duplica").select();
		}
	}

	function okDuplicate (codAcess, templateOriginal) {
		$("#duplicate").css("display", "none");
		var name = $("#input-duplica").val();
		location.href="/proc.jsp" + 
						"?actionID=9&codAcess=" + codAcess + 
								"&idTemplateOriginal=" + templateOriginal + 
									"&namePage=" + name;
	}
	// Chamada quando o usuario quiser cancelar a operação de duplicação da mensagem
	function cancelDuplicate () { $("#duplicate").css("display", "none"); }

	// download da mensagem
	function downloadMesseger () {
		if (change) { alert("Antes de fazer download, salve as alterações atuais");	return false; } 
		else { return true; }
	}