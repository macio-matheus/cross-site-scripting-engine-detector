package crawler;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import system.Analyse;
import system.AnalyseBusiness;
import system.AnalyseSearchParams;
import system.CodeXSS;
import system.CodeXSSBusiness;
import system.CodeXSSSearchParams;
import system.Form;
import system.FormBusiness;
import system.FormSearchParams;
import system.Input;
import system.InputBusiness;
import system.InputSearchParams;
import system.ResponseForm;
import system.ResponseFormBusiness;
import system.ResponseFormSearchParams;
import system.ScriptDetected;
import system.ScriptDetectedBusiness;
import system.ScriptDetectedSearchParams;
import system.Super;
import system.UrlFound;
import system.UrlFoundBusiness;
import system.UrlFoundSearchParams;
import utilsLib.util.FilterParam;
import utilsLib.util.PagingInfo;
import utilsLib.util.Utils;

public class Facade {

	private Super creator;

	private CodeXSSBusiness codeXSSBus;
	private AnalyseBusiness analyseBus;
	private InputBusiness inputBus;
	private FormBusiness formBus;
	private ResponseFormBusiness responseFormBus;
	private UrlFoundBusiness urlFoundBus;
	private ScriptDetectedBusiness scriptDetectedBus;

	/**
	 * Constants
	 */
	public static final int QTD_RANDOMS = 10;
	public static final int MAX_DEPTH = 5;	// Profundidade do scaneamento

	public static enum ScriptiDetected_Type {
		REFLECTED, PERSISTENT
	}; // REFLECTED_AND_PERSISTENT

	public Facade(Super creator, CodeXSSBusiness codeXSSBus,
			AnalyseBusiness analyseBus, InputBusiness inputBus,
			FormBusiness formBus, ResponseFormBusiness responseFormBus,
			UrlFoundBusiness urlFoundBus,
			ScriptDetectedBusiness scriptDetectedBus) {
		this.creator = creator;
		this.codeXSSBus = codeXSSBus;
		this.analyseBus = analyseBus;
		this.inputBus = inputBus;
		this.formBus = formBus;
		this.responseFormBus = responseFormBus;
		this.urlFoundBus = urlFoundBus;
		this.scriptDetectedBus = scriptDetectedBus;

		init();
	}

	/**
	 * Métodos de scripts detectados
	 */
	public void addScriptDetected(ScriptDetected scriptDetected) {
		scriptDetectedBus.add(scriptDetected);
	}

	public void updateScriptDetected(ScriptDetected scriptDetected) {
		scriptDetectedBus.update(scriptDetected);
	}

	public void removeScriptDetected(int id) {
		scriptDetectedBus.remove(id);
	}

	public ScriptDetected[] getScriptDetecteds(ScriptDetectedSearchParams params) {
		return scriptDetectedBus.getScriptDetecteds(params);
	}

	public ScriptDetected searchScriptDetected(int id) {
		ScriptDetectedSearchParams params = new ScriptDetectedSearchParams();
		params.addFilter(new FilterParam("id", id,
				FilterParam.COMPARE_TYPE.EQUAL));

		params.setPi(new PagingInfo(1));

		return (ScriptDetected) Utils.getArrayItem(
				this.getScriptDetecteds(params), 0, null);
	}

	/**
	 * Métodos de formulario
	 */
	public void addForm(Form form) {
		formBus.add(form);
	}

	public void updateForm(Form form) {
		formBus.update(form);
	}

	public void removeForm(int id) {
		formBus.remove(id);
	}

	public Form[] getForms(FormSearchParams params) {
		return formBus.getForms(params);
	}

	public Form searchForm(int id) {
		FormSearchParams params = new FormSearchParams();
		params.addFilter(new FilterParam("id", id,
				FilterParam.COMPARE_TYPE.EQUAL));

		params.setPi(new PagingInfo(1));

		return (Form) Utils.getArrayItem(this.getForms(params), 0, null);
	}

	/**
	 * Métodos de URL
	 */

	public void addUrlFound(UrlFound urlFound) {
		urlFoundBus.add(urlFound);
	}

	public void updateUrlFound(UrlFound urlFound) {
		urlFoundBus.update(urlFound);
	}

	public void removeUrlFound(int id) {
		urlFoundBus.remove(id);
	}

	public UrlFound[] getUrlFounds(UrlFoundSearchParams params) {
		return urlFoundBus.getUrlFounds(params);
	}

	public UrlFound searchUrlFound(int id) {
		UrlFoundSearchParams params = new UrlFoundSearchParams();
		params.addFilter(new FilterParam("id", id,
				FilterParam.COMPARE_TYPE.EQUAL));

		params.setPi(new PagingInfo(1));

		return (UrlFound) Utils
				.getArrayItem(this.getUrlFounds(params), 0, null);
	}

	/**
	 * Métodos de input
	 */

	public void addInput(Input input) {
		inputBus.add(input);
	}

	public void updateInput(Input input) {
		inputBus.update(input);
	}

	public void removeInput(int id) {
		inputBus.remove(id);
	}

	public Input[] getInputs(InputSearchParams params) {
		return inputBus.getInputs(params);
	}

	public Input searchInput(int id) {
		InputSearchParams params = new InputSearchParams();
		params.addFilter(new FilterParam("id", id,
				FilterParam.COMPARE_TYPE.EQUAL));

		params.setPi(new PagingInfo(1));

		return (Input) Utils.getArrayItem(this.getInputs(params), 0, null);
	}

	/**
	 * Métodos de codeXSS
	 * 
	 * @return
	 */
	public void addCodeXSS(CodeXSS codeXSS) {
		codeXSSBus.add(codeXSS);
	}

	public void updateCodeXSS(CodeXSS codeXSS) {
		codeXSSBus.update(codeXSS);
	}

	public void removeCodeXSS(int id) {
		codeXSSBus.remove(id);
	}

	public CodeXSS[] getCodeXSSs(CodeXSSSearchParams params) {
		return codeXSSBus.getCodeXSSs(params);
	}

	public CodeXSS searchCodeXSS(int id) {
		CodeXSSSearchParams params = new CodeXSSSearchParams();
		params.addFilter(new FilterParam("id", id,
				FilterParam.COMPARE_TYPE.EQUAL));

		params.setPi(new PagingInfo(1));

		return (CodeXSS) Utils.getArrayItem(this.getCodeXSSs(params), 0, null);
	}

	/**
	 * Métodos de Analise
	 */
	public void addAnalyse(Analyse analyse) {
		analyseBus.add(analyse);
	}

	public void updateAnalyse(Analyse analyse) {
		analyseBus.update(analyse);
	}

	public void removeAnalyse(int id) {
		analyseBus.remove(id);
	}

	public Analyse[] getAnalyses(AnalyseSearchParams params) {
		return analyseBus.getAnalyses(params);
	}

	public Analyse searchAnalyse(int id) {
		AnalyseSearchParams params = new AnalyseSearchParams();
		params.addFilter(new FilterParam("id", id,
				FilterParam.COMPARE_TYPE.EQUAL));

		params.setPi(new PagingInfo(1));

		return (Analyse) Utils.getArrayItem(this.getAnalyses(params), 0, null);
	}

	/**
	 * Response forms
	 */

	public void addResponseForm(ResponseForm responseForm) {
		responseFormBus.add(responseForm);
	}

	public void updateResponseForm(ResponseForm responseForm) {
		responseFormBus.update(responseForm);
	}

	public void removeResponseForm(int id) {
		responseFormBus.remove(id);
	}

	public ResponseForm[] getResponseForms(ResponseFormSearchParams params) {
		return responseFormBus.getResponseForms(params);
	}

	public ResponseForm searchResponseForm(int id) {
		ResponseFormSearchParams params = new ResponseFormSearchParams();
		params.addFilter(new FilterParam("id", id,
				FilterParam.COMPARE_TYPE.EQUAL));

		params.setPi(new PagingInfo(1));

		return (ResponseForm) Utils.getArrayItem(this.getResponseForms(params),
				0, null);
	}

	/**
	 * Inicio da analise
	 * 
	 */
	public void beginAnalyse(Analyse[] analyse) {

		ThreadPoolExecutor executor = null;

		for (int i = 0; i < analyse.length; i++) {

			WebCrawler crawler = new WebCrawler(analyse[i].getId());

			executor = ExecutorHelper.getInstance();
			executor.execute(crawler);
		}

		try {
			while (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
				Thread.sleep(1000);

				System.out.println("Tarefas em execução: "
						+ executor.getTaskCount());
				System.out.println("Tarefas completas: "
						+ executor.getCompletedTaskCount());
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Finished all threads");
		executor.shutdown();
	}
	
	public void beginAnalyse(Analyse analyse) {
		
		Analyse[] aux = new Analyse[1];// analyse;
		aux[0] = analyse;

		this.beginAnalyse(aux);
	}

	// End métodos gerais
	public Super getCreator() {
		return creator;
	}

	public void init() {

	}
}