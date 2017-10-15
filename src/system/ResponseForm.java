package system;

import java.io.IOException;
import java.sql.SQLException;

import utilsLib.util.FilterParam;
import utilsLib.util.FilterParam.COMPARE_TYPE;
import utilsLib.util.FilterParam.OPERATOR;
import crawler.Facade;

public class ResponseForm {
	private int id;
	private int idInput;
	private int idForm;
	private String responseText;
	private String codeStatus;
	private int idCodeXSS;
	private boolean result;
	private int typeXSS;

	public int getTypeXSS() {
		return typeXSS;
	}

	public void setTypeXSS(int typeXSS) {
		this.typeXSS = typeXSS;
	}

	public ResponseForm(int id, int idInput, int idForm, String responseText,
			String codeStatus, int idCodeXSS, boolean result, int typeXSS) {
		setId(id);
		setIdInput(idInput);
		setIdForm(idForm);
		setResponseText(responseText);
		setCodeStatus(codeStatus);
		setIdCodeXSS(idCodeXSS);
		setResult(result);
		// setTypeXSS(typeXSS);
	}

	public ResponseForm(int id, int idInput, int idForm, String responseText,
			String codeStatus, int idCodeXSS, boolean result) {
		setId(id);
		setIdInput(idInput);
		setIdForm(idForm);
		setResponseText(responseText);
		setCodeStatus(codeStatus);
		setIdCodeXSS(idCodeXSS);
		setResult(result);
	}

	public UrlFound[] getUrls() {

		Super creator = null;
		try {
			creator = new Super();
		} catch (ClassNotFoundException | SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		UrlFoundSearchParams uparams = new UrlFoundSearchParams();
		uparams.addFilter(new FilterParam("idResponseForm", this.id, COMPARE_TYPE.EQUAL));

		return creator.getFacade().getUrlFounds(uparams);
	}

	public ScriptDetected[] getScriptDetecteds() {

		Super creator = null;
		try {
			creator = new Super();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Facade fac = creator.getFacade();

		ScriptDetectedSearchParams sparams = new ScriptDetectedSearchParams();
		sparams.addFilter(new FilterParam("idResponseForm", this.id,
				COMPARE_TYPE.EQUAL));

		return fac.getScriptDetecteds(sparams);
	}

	public Analyse getAnalyse() {

		Super creator = null;
		try {
			creator = new Super();
		} catch (ClassNotFoundException | SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Facade fac = creator.getFacade();

		return fac.searchAnalyse(fac.searchForm(this.idForm).getIdAnalyse());
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public int getIdCodeXSS() {
		return idCodeXSS;
	}

	public void setIdCodeXSS(int idCodeXSS) {
		this.idCodeXSS = idCodeXSS;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public void setIdInput(int idInput) {
		this.idInput = idInput;
	}

	public int getIdInput() {
		return this.idInput;
	}

	public void setIdForm(int idForm) {
		this.idForm = idForm;
	}

	public int getIdForm() {
		return this.idForm;
	}

	public void setResponseText(String responseText) {
		this.responseText = responseText;
	}

	public String getResponseText() {
		return this.responseText;
	}

	public void setCodeStatus(String codeStatus) {
		this.codeStatus = codeStatus;
	}

	public String getCodeStatus() {
		return this.codeStatus;
	}

	@Override
	public String toString() {
		return "ResponseForm [id=" + id + ", idInput=" + idInput + ", idForm="
				+ idForm + ", responseText=" + responseText + ", codeStatus="
				+ codeStatus + ", idCodeXSS=" + idCodeXSS + ", result="
				+ result + "]";
	}
}