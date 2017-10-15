package system;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import utilsLib.util.FilterParam;
import utilsLib.util.FilterParam.COMPARE_TYPE;
import utilsLib.util.FilterParam.OPERATOR;
import utilsLib.util.PagingInfo;
import utilsLib.util.Utils;
import crawler.Facade;
import crawler.WebCrawler;

public class Form {
	private int id;
	private int idAnalyse;
	private String action;
	private String method;
	private String url;

	public Form(int id, int idAnalyse, String action, String method, String url) {
		setId(id);
		setIdAnalyse(idAnalyse);
		setAction(action);
		setMethod(method);
		setUrl(url);

	}

	public ResponseForm[] getResponses() {
		
		Super creator = null;
		try {
			creator = new Super();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Facade fac = creator.getFacade(); 
		
		ResponseFormSearchParams rparams = new ResponseFormSearchParams();
		rparams.addFilter(new FilterParam("idForm", this.id, COMPARE_TYPE.EQUAL));
		
		return fac.getResponseForms(rparams);
	}
	
	public UrlFound[] getUrls(){
		
		Super creator = null;
		try {
			creator = new Super();
		} catch (ClassNotFoundException | SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		UrlFoundSearchParams uparams = new UrlFoundSearchParams();
		uparams.addFilter(new FilterParam("idForm", this.id, COMPARE_TYPE.EQUAL));
		
		return creator.getFacade().getUrlFounds(uparams);
	}
	
	public Input[] getInputs() {
		
		Super creator = null;
		try {
			creator = new Super();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Facade fac = creator.getFacade(); 
		
		InputSearchParams iparams = new InputSearchParams();
		iparams.addFilter(new FilterParam("idForm", this.id, COMPARE_TYPE.EQUAL));
		
		return fac.getInputs(iparams);
	}

	public Response submit(HashMap<String, String> mapData) {
		Method method = (this.getMethod().toUpperCase().equals("POST") ? Method.POST
				: Method.GET);
		String userAgent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21";

		Response res = null;

		System.out.println(">Tentou submeter o action: " + this.getAction());

		try {
			res = Jsoup.connect(this.getAction()).ignoreHttpErrors(true)
					.data(mapData).userAgent(userAgent).timeout(5000)
					.method(method).execute();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// /e.printStackTrace();
			System.out.println("Erro ao submeter formulário ID: "
					+ this.getId());
		}

		return res;
	}

	public int qtdFields() {

		Super creator = null;
		try {
			creator = new Super();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		InputSearchParams iparams = new InputSearchParams();
		iparams.addFilter(new FilterParam("idForm", this.id,
				FilterParam.COMPARE_TYPE.EQUAL));
		iparams.setPi(new PagingInfo(100));

		return (creator.getFacade().getInputs(iparams)).length;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public void setIdAnalyse(int idAnalyse) {
		this.idAnalyse = idAnalyse;
	}

	public int getIdAnalyse() {
		return this.idAnalyse;
	}

	private void setAction(String action) {

		// Se nao for válida, tenta tratar e juntar com o dominio
		if (!Utils.isValidUrl(action) && !action.equals("")) {
			Analyse analyse = WebCrawler.getFacade().searchAnalyse(
					this.idAnalyse);

			String[] u = analyse.getUrl().split("://");

			Utils.lastChar(Utils.extractDomainName(analyse.getUrl()));
			char c = action.charAt(0);
			if ((c + "").equals("/")) {
				action = u[0] + "://"
						+ Utils.extractDomainName(analyse.getUrl()) + action;
			} else {
				action = u[0] + "://"
						+ Utils.extractDomainName(analyse.getUrl()) + "/"
						+ action;
			}
		}

		this.action = action;
	}

	public String getAction() {
		return this.action;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getMethod() {
		return this.method;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return this.url;
	}

	public String toString() {
		return "id: " + id + " | idAnalyse: " + idAnalyse + " | action: "
				+ action + " | method: " + method + " | url: " + url;
	}
}