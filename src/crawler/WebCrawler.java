package crawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import system.Analyse;
import system.CodeXSS;
import system.CodeXSSSearchParams;
import system.Form;
import system.FormSearchParams;
import system.Input;
import system.InputSearchParams;
import system.ResponseForm;
import system.ScriptDetected;
import system.Super;
import system.UrlFound;
import utilsLib.jsp.JSPUtils;
import utilsLib.util.FilterParam;
import utilsLib.util.PagingInfo;
import utilsLib.util.Utils;

/**
 * 
 * @author M�cio Matheus
 */
public class WebCrawler implements Runnable {

	private int idAnalyse;
	
	private static ArrayList<String> urlsScanneds = new ArrayList<String>();
	public static ArrayList<String> actionsSubmiteds = new ArrayList<String>();
	
	private int DEPTH = 0;

	public WebCrawler(int idAnalyse) {
		this.idAnalyse = idAnalyse;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		// Processa em busca de formul�rios e url
		Analyse analyse = getFacade().searchAnalyse(idAnalyse);
				
		System.out.println(">Come�ou a an�lise da URL: " + analyse.getUrl());
		JSPUtils.log("BEGIN ANALYSEXSS", "Come�ou a analise da URL: " + analyse.getUrl());

		ArrayList<UrlFound> urls = new ArrayList<UrlFound>();
		ArrayList<Form> forms = new ArrayList<Form>();

		Document document = getDocument(analyse.getUrl());

		// retorna caso nao consiga conectar
		if (document == null) {
			System.out.println(">Terminou a analise document == null " + analyse.getUrl());
			return;
		}

		Elements formElements = document.select("form");

		Form[] formsEntity = new Form[formElements.size()];

		for (int j = 0; j < formElements.size(); j++) {

			// Caso n�o possua action, descarta
			if (formElements.get(j).equals("")) {
				System.out.println(">entrou aqui");
				continue;
			}

			System.out.println(">Achou um form.. itera��o_" + j);
			formsEntity[j] = insertFormAndFields(analyse,	formElements.get(j), analyse.getUrl());
			
			if (formsEntity[j] != null) {
				forms.add(formsEntity[j]);
			}
		}

		Elements aElements = document.getElementsByTag("a");
		UrlFound urlFound = null;
		for (Element aLinks : aElements) {
			if (!aLinks.attr("href").equals("") && !aLinks.attr("href").equals("#")) {
				
				if (Utils.isValidUrl(aLinks.attr("href"))) {
					
					String domainAnalyse = Utils.extractDomainName(analyse.getUrl());
					String domainElement = Utils.extractDomainName(aLinks.attr("href"));
	
					if (domainAnalyse.indexOf(domainElement) > -1) {
						urlFound = new UrlFound(Utils.UNDEFINED_NUMBER,	Utils.UNDEFINED_NUMBER,	Utils.UNDEFINED_NUMBER,	aLinks.attr("href"));
						getFacade().addUrlFound(urlFound);
						urls.add(urlFound);
					}
				} else {
	
					String u = Utils.relativeUrlToAbsolut(analyse.getUrl(),	aLinks.attr("href"));
					if (Utils.isValidUrl(u)) {
						urlFound = new UrlFound(Utils.UNDEFINED_NUMBER,	Utils.UNDEFINED_NUMBER,	Utils.UNDEFINED_NUMBER,	aLinks.attr("href"));
						getFacade().addUrlFound(urlFound);
						urls.add(urlFound);
					}
				}
			}
	
			System.out.println(">Formul�rios antes de submeter: " + forms.size());	
			System.out.println(">Urls encontradas na detec��o inicial: " + urls.size());
			analyseLevelN(forms, urls);
		}
	}
	
	/**
	 * M�todo recursivo que faz a an�lise em profundidade ilimitada
	 * 
	 * @param formsFound
	 * @param urlsFound
	 */
	public void analyseLevelN(ArrayList<Form> formsFound, ArrayList<UrlFound> urlsFound) {
		
		synchronized (Super.SYNCH_INIT) {
			if (DEPTH >= Facade.MAX_DEPTH) {
				System.out.println(">[analyseLevelN]Analise terminou! Profundidade m�xima atingida.");
				JSPUtils.log("BEGIN ANALYSEXSS", "Analise terminou! Profundidade m�xima atingida.");
				return;
			}
		}

		System.out.println(">[analyseLevelN][INICIO]");

		ArrayList<ElementsFound> newElementsFound = new ArrayList<ElementsFound>();

		ArrayList<UrlFound> urlsAux = new ArrayList<UrlFound>();
		ArrayList<Form> formsAux = new ArrayList<Form>();

		// Processa e obtem as respostas
		if (urlsFound.size() > 0 && formsFound.size() > 0) {
			System.out.println(">[analyseLevelN] IF 1");
			newElementsFound.addAll(findElementsOnListUrl(urlsFound));
			newElementsFound.addAll(findElementsOnListForm(formsFound));
		} else if (urlsFound.size() > 0) {
			System.out.println(">[analyseLevelN] ELSE IF 1");
			newElementsFound.addAll(findElementsOnListUrl(urlsFound));
		} else if (formsFound.size() > 0) {
			System.out.println(">[analyseLevelN] ELSE IF 2");
			newElementsFound.addAll(findElementsOnListUrl(urlsFound));
		}

		for (ElementsFound elementsFound : newElementsFound) {
			for (ElementsFromResponse elResp : elementsFound.getElements()) {
				// Urls detectadas somadas as urls da resposta do form
				urlsAux.addAll(elResp.getUrls());
				formsAux.addAll(elResp.getForms());
			}
		}
		
		// Atualiza a profundidade
		DEPTH = DEPTH + 1;

		// Se nao existirem formul�rios ou urls, terminou
		if (urlsAux.size() == 0 && formsAux.size() == 0) {
			System.out.println(">[analyseLevelN]Analise terminou! Urls e Forms vazios.");
			JSPUtils.log("BEGIN ANALYSEXSS", "Analise terminou! Urls e Forms vazios.");
			return;
		} else {
			System.out.println(">[analyseLevelN] RECURSIVO");
			analyseLevelN(formsAux, urlsAux);
		}
	}

	/**
	 * Processa uma lista de urls e tras uma lista contendo elementos
	 * encontrados (forms e urls)
	 * 
	 * @param urls
	 * @return
	 */
	private ArrayList<ElementsFound> findElementsOnListUrl(
			ArrayList<UrlFound> urls) {
		// TODO Auto-generated method stub

		System.out.println(">[processUrl] INICIO");

		ArrayList<ElementsFound> listElementsFound = new ArrayList<ElementsFound>();
		ArrayList<Form> forms = new ArrayList<Form>();
		ArrayList<ElementsFound> elements = new ArrayList<ElementsFound>();

		// Processa em busca de formul�rios e urls
		Analyse analyse = getFacade().searchAnalyse(idAnalyse);

		Document document = WebCrawler.getDocument(analyse.getUrl());

		// retorna caso nao consiga conectar
		if (document == null) {
			System.out.println(">[CrawlerAnalyseXSS] document == null");
			return listElementsFound;
		}

		for (UrlFound url : urls) {

			// Forms elementos html
			Elements formElements = document.select("form");

			// Forms entidade
			Form[] formsEntity = new Form[formElements.size()];

			int i = 0;
			for (Element htmlForm : formElements) {

				System.out.println(">[processUrl]  FOREACH");
				System.out.println(">[processUrl]"
						+ htmlForm.toString().length());

				// Caso n�o possua action, descarta
				if (htmlForm.attr("action").equals("")) {
					continue;
				}
				// Recupera o form ap�s realizar a detec��o e adicionar no banco
				// seus campos
				formsEntity[i] = insertFormAndFields(analyse, htmlForm, null);
				
				if (formsEntity[i] != null) {
					forms.add(formsEntity[i]);
				}
				
				i = i + 1;
			}

			// Chama o algoritmo de submiss�o que retorna um formul�rio e uma
			// lista de urls encontradas
			elements = findElementsOnListForm(forms);

			for (ElementsFound el : elements) {
				listElementsFound.add(new ElementsFound(url, el.getElements()));
			}
		}

		return listElementsFound;
	}

	/**
	 * Processa uma lista de formul�rios e tras uma lista para cada formul�rio,
	 * contendo elementos encontrados (forms e urls)
	 * 
	 * @param forms
	 * @return
	 */
	public static ArrayList<ElementsFound> findElementsOnListForm(
			ArrayList<Form> forms) {

		System.out.println(">[findElementsOnListForm] INICIO dalista de forms: "
				+ forms.size());

		ArrayList<ElementsFound> listElementsFound = new ArrayList<ElementsFound>();

		ArrayList<ElementsFromResponse> listElementsAux = new ArrayList<ElementsFromResponse>();

		ArrayList<ResponseForm> allResponses = new ArrayList<ResponseForm>();

		for (Form form : forms) {

			System.out.println(">[findElementsOnListForm] foreach");

			// Submete o formul�rio
			allResponses.addAll(submitForm(form));

			// Retorna uma lista de elementos encontrados em sua resposta, n�o
			// analisadas
			listElementsAux = findElementsOnListResponse(allResponses);

			listElementsFound.add(new ElementsFound(form, listElementsAux));
		}

		// Lista de elementos, dadas as respotas dos formul�rios
		return listElementsFound;
	}

	/**
	 * Recebe novos elementos (formul�rios e urls) encontrados na lista de
	 * respostas passadas por parametro
	 * 
	 * @param responses
	 * @return
	 */
	private static ArrayList<ElementsFromResponse> findElementsOnListResponse(
			ArrayList<ResponseForm> responses) {
		// TODO Auto-generated method stub

		ArrayList<ElementsFromResponse> elements = new ArrayList<ElementsFromResponse>();

		Analyse analyse = null;

		ArrayList<Form> forms = new ArrayList<Form>();
		ArrayList<UrlFound> urls = new ArrayList<UrlFound>();

		for (ResponseForm responseForm : responses) {

			analyse = responseForm.getAnalyse();

			Document doc = Jsoup.parse(responseForm.getResponseText());

			// Pega os forms se houverem
			Elements tagsForm = doc.select("form");
			Form formAux = null;
			
			for (Element htmlForm : tagsForm) {

				// Caso n�o possua action, descarta
				if (htmlForm.attr("action").equals("")) {
					continue;
				}
			
				// Chama o m�todo que extrai formul�rios, passa o action do form
				// que gerou a resposta
				formAux = insertFormAndFields(analyse, htmlForm, (getFacade().searchForm(responseForm.getIdForm()).getAction()));
				if (formAux != null) {
					forms.add(formAux);
				}
			}

			// Procura os elementos de link e guarda na memoria
			// Tem que pertencer ao mesmo dominio
			Elements aElements = doc.select("a");
			UrlFound urlFound = null;
			for (Element aLinks : aElements) {
				if (!aLinks.attr("href").equals("")	&& !aLinks.attr("href").equals("#")) {
					if (Utils.isValidUrl(aLinks.attr("href"))) {

						String domainAnalyse = Utils.extractDomainName(analyse.getUrl());
						String domainElement = Utils.extractDomainName(aLinks.attr("href"));

						if (domainAnalyse.indexOf(domainElement) > -1) {
							urlFound = new UrlFound(Utils.UNDEFINED_NUMBER,	Utils.UNDEFINED_NUMBER,	responseForm.getId(), aLinks.attr("href"));
							getFacade().addUrlFound(urlFound);
							urls.add(urlFound);
						}
					} else {

						if (aLinks.attr("href").substring(0, 1).equals("/")) {
							String u = Utils.extractDomainName(analyse.getUrl()) + aLinks.attr("href");

							if (Utils.isValidUrl(u)) {
								urlFound = new UrlFound(Utils.UNDEFINED_NUMBER,	Utils.UNDEFINED_NUMBER,	responseForm.getId(), aLinks.attr("href"));
								getFacade().addUrlFound(urlFound);
								urls.add(urlFound);
							}

						}
					}
				}
			}

			elements.add(new ElementsFromResponse(forms, urls));
		}
		return elements;
	}

	/**
	 * Submete um formul�rio e retorna uma lista de respostas
	 * 
	 * @param form
	 * @return
	 */
	public static ArrayList<ResponseForm> submitForm(Form form) {
		// TODO Auto-generated method stub

		System.out.println(">[submitForm] INICIO " + form.toString());

		ArrayList<ResponseForm> listResponsesClassified = new ArrayList<ResponseForm>();

		Connection.Response res = null;

		// Pegar os campos do formul�rio
		InputSearchParams iparams = new InputSearchParams();
		iparams.addFilter(new FilterParam("idForm", form.getId(), FilterParam.COMPARE_TYPE.EQUAL));

		Input[] inputs = getFacade().getInputs(iparams);

		CodeXSSSearchParams cparams = new CodeXSSSearchParams();
		CodeXSS[] allCodesXSS = getFacade().getCodeXSSs(cparams);

		// Dados que ser�o enviados
		HashMap<String, String> mapData = new HashMap<String, String>();

		// Lista de hasmap de dados que ser�o enviados
		ArrayList<HashMap<String, String>> listMapData = new ArrayList<HashMap<String, String>>();
		ArrayList<ResponseForm> listAllResponse = new ArrayList<ResponseForm>();

		/*
		 * Guarda as respostas esperadas de todos os xss usados no m�todo;
		 * 
		 * Caso o analisador encontre uma ou mais respostas desse arrayList,
		 * indica que � do tipo refletida. Caso contr�rio pode ser persistente.
		 */
		ArrayList<String> listOutputsXSSExpected = new ArrayList<String>();

		// Para cada campo, v�o ser criadas X quantidades de submiss�es,
		// que s�o definidas pela quantidade de c�digos testados, que
		// diretamente afeta o tempo de processamento.
		for (int j = 0; j < inputs.length; j++) {


			int[] indexCodesXSS = Utils.randomsIndexArray(allCodesXSS.length, Facade.QTD_RANDOMS);

			// cria novas submiss�es para cada c�digo a ser testado
			for (int k = 0; k < indexCodesXSS.length; k++) {

				mapData.put(inputs[j].getName(), allCodesXSS[indexCodesXSS[k]].getCode());

				// guarda na lista que vai identificar os tipos refletidos
				listOutputsXSSExpected.add(allCodesXSS[indexCodesXSS[k]].getOutput());

				// Preenche com os valores esperados
				for (int i = 0; i < inputs.length; i++) {
					if (j != i) {
						String value = "";

						// Se o campo tem um valor padr�o, utiliza o mesmo
						if (!inputs[j].getValue().equals("")) {
							value = inputs[j].getValue();

							// Se n�o, gera um tipo de acordo com o tipo
							// sugerido
						} else {
							value = inputs[j].generateDataToType();
						}

						// adiciona um valor "esperado" (sem XSS)
						mapData.put(inputs[j].getName(), value);
					}
				}
				// Guarda na lista que ser� processada ao final do loop
				listAllResponse.add(new ResponseForm(Utils.UNDEFINED_NUMBER, inputs[j].getId(), form.getId(), "", "", 
						allCodesXSS[indexCodesXSS[0]].getId(), false));
				listMapData.add(mapData);
			}
		}

		int i = 0;
		ResponseForm responseForm = null;
		for (HashMap<String, String> map : listMapData) {
			// Faz a submissao do formul�rio
			res = form.submit(map);

			if (res != null) {

				responseForm = listAllResponse.get(i);
				responseForm.setResponseText(res.body());
				responseForm.setCodeStatus(res.statusCode() + "");

				// S� analisa resposta o status foi 200 e a resposta n�o veio
				// vazia.
				if (!responseForm.getResponseText().equals("")) {

					// Adiciona a resposta, pois vai utilizar o id da mesma
					getFacade().addResponseForm(responseForm);

					boolean isVulnerable = false;
					ScriptDetected scriptDetected = null;

					// Classifica a resposta
					for (int j = 0; j < allCodesXSS.length; j++) {
						if (responseForm.getResponseText().contains(allCodesXSS[j].getOutput())) {

							scriptDetected = new ScriptDetected(Utils.UNDEFINED_NUMBER,	responseForm.getId(), Facade.ScriptiDetected_Type.REFLECTED
											.ordinal());

							// Seta a flag como vulner�vel
							if (!isVulnerable) {
								isVulnerable = true;
							}

							if (!listOutputsXSSExpected.contains(allCodesXSS[j].getOutput())) {
								scriptDetected.setTypeXSS(Facade.ScriptiDetected_Type.PERSISTENT.ordinal());
							}
							// Adiciona nova informa��o
							getFacade().addScriptDetected(scriptDetected);
							System.out.println(">[submitForm] Adicionou um novo Script Detectado: "	+ scriptDetected.toString());
						}
					}
					// Seta com a flag definida true ou false
					responseForm.setResult(isVulnerable);

					// Atualiza em banco de dados
					getFacade().updateResponseForm(responseForm);

					// Grava no arraylist todas as respostas do formul�rio
					listResponsesClassified.add(responseForm);
				}
			}

			i = i + 1;
		}

		return listResponsesClassified;
	}

	/**
	 * Dada uma "tag" de formul�rio, transforma em entidade e insere em banco,
	 * procura seus campos e insere da mesma forma. Retorna uma ENTIDADE
	 * formul�rio
	 * 
	 * @param analyse
	 * @param tagForm
	 * @param urlOrigin
	 * @return
	 */
	private static Form insertFormAndFields(Analyse analyse, Element tagForm,
			String urlOrigin) {

		Form formEntity = null;

		if (tagForm == null || tagForm.equals("")) {
			System.out.println(">[insertFormAndFields]Tag vazia");
			return formEntity;
		}

		// Adiciona o formul�rio
		formEntity = new Form(Utils.UNDEFINED_NUMBER, analyse.getId(),	tagForm.attr("action"), tagForm.attr("method"), urlOrigin);
		
		// s� insere formul�rios v�lidos
		try {
			if (formExists(formEntity) || formEntity.getAction().equals("") || formEntity.getMethod().equals("")) {
				return (Form) null;
			}
		} catch (Exception e) {
			return (Form) null;
		}
		
		getFacade().addForm(formEntity);

		System.out.println(">[insertFormAndFields] Formul�rio inserido: " + formEntity.toString());
		
		ArrayList<Elements> inputsAllElements = new ArrayList<Elements>();
		
		inputsAllElements.add(tagForm.select("input[type=text]"));
		inputsAllElements.add(tagForm.getElementsByTag("textarea"));
		inputsAllElements.add(tagForm.select("input[type=hidden]"));
		inputsAllElements.add(tagForm.select("input[type=date]"));
		inputsAllElements.add(tagForm.select("input[type=url]"));
		inputsAllElements.add(tagForm.select("input[type=search]"));
		inputsAllElements.add(tagForm.select("input[type=number]"));
		inputsAllElements.add(tagForm.getElementsByTag("select"));
		inputsAllElements.add(tagForm.select("input[type=email]"));
				
		Input input = null;
		for (Elements inputElements : inputsAllElements) {
			for (Element fields : inputElements) {
				input = new Input(Utils.UNDEFINED_NUMBER, fields.attr("name"),
						fields.attr("type"), fields.attr("value"),
						fields.attr("id"), Utils.UNDEFINED_NUMBER,
						formEntity.getId());
				
				input.setTypeSuggestedValue();
				
				if (!input.getName().equals("")) {
					
					// Adiciona input
					getFacade().addInput(input);
					System.out.println(">[insertFormAndFields] Inseriu um campo: " + input.toString());
				}
			}
		}
		
		return formEntity;
	}

	private static boolean formExists(Form formEntity) {
		
		FormSearchParams fparams = new FormSearchParams();
		fparams.addFilter(new FilterParam("idAnalyse", formEntity.getIdAnalyse(), FilterParam.COMPARE_TYPE.EQUAL));
		fparams.addFilter(new FilterParam("action", formEntity.getAction(), FilterParam.COMPARE_TYPE.EQUAL));
		fparams.addFilter(new FilterParam("url", formEntity.getUrl(), FilterParam.COMPARE_TYPE.EQUAL));
		fparams.addFilter(new FilterParam("method", formEntity.getMethod(), FilterParam.COMPARE_TYPE.EQUAL));
		fparams.setPi(new PagingInfo(1));
		
		if (getFacade().getForms(fparams).length == 0)
			return false;
		else
			return true;
	}

	/**
	 * Retorna a facade
	 * 
	 * @return
	 */
	public static Facade getFacade() {
		Super creator = null;
		try {
			creator = new Super();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return creator.getFacade();
	}

	public static Document getDocument(String url) {

		Document document = null;
		boolean error = false;
		int retryCount = 0;
		
		synchronized (Super.SYNCH_INIT) {
			if (urlsScanneds.contains(url)) {
				System.out.println(">...:::Url ja scaneada:::...");
				return document;
			}
		}

		if (!url.contains("http:") && !url.contains("https:")) {
			System.out.println(url);
			url = "http://" + url;
		}

		// Se termina com .exe ou .zip
		if ((url.endsWith(".exe") || url.endsWith(".zip")
				|| url.endsWith(".msi") || url.endsWith(".bin"))) {
			String strLog = "[WebCrawler][getDocument] Url inv�lida. Ext. maliciosa:"
					+ url;
			JSPUtils.log(strLog);
			System.out.println(strLog);
		} else {
			do {
				try {
					document = Jsoup.connect(url).get();
				} catch (Exception ex) {
					try {
						document = Jsoup.connect(url.replace("http://", "https://")).get();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						error = true;
						retryCount++;
					}
				}
			} while (error && retryCount < 5);
			if (error && retryCount >= 5) {
				System.out.println(">Abandonando URL: " + url + " , m�ximo de tentativas atingido.");
			}
		}
		
		synchronized (Super.SYNCH_INIT) {
			urlsScanneds.add(url);
			System.out.println(">...:::Url adicionada na lista de scaneadas:::...");
		}		

		return document;
	}
}
