// função para alterar o tamanho da fonte do site
var tam = 10; // tamanho padrão
var tamMax = 13; // tamanho maximo
var tamMin = 9; // tamanho minimo

function alterarFonte(tipo) {
	switch (tipo) {
	    case '+':
	        if(tam < tamMax) tam = tam + 1;
	        break;
	    case '-':
			if(tam > tamMin) tam = tam - 1;
	        break;
		case 'normal':
			tam = 10;
	        break;
	}
	document.body.style.fontSize = tam + 'px';
}