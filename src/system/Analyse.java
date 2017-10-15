package system;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

import crawler.Facade;
import utilsLib.util.FilterParam;
import utilsLib.util.FilterParam.COMPARE_TYPE;

public class Analyse {
	private int id;
	private String url;
	private Date dateCreate;

	public Analyse(int id, String url, Date dateCreate) {
		setId(id);
		setUrl(url);
		setDateCreate(dateCreate);
	}
	
	public Form[] getForms(){
		
		Super creator = null;
		try {
			creator = new Super();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Facade fac = creator.getFacade();
		
		FormSearchParams fparams = new FormSearchParams();
		fparams.addFilter(new FilterParam("idAnalyse", this.id, COMPARE_TYPE.EQUAL));
		
		return fac.getForms(fparams);
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return this.url;
	}

	public void setDateCreate(Date dateCreate) {
		this.dateCreate = dateCreate;
	}

	public Date getDateCreate() {
		return this.dateCreate;
	}

	@Override
	public String toString() {
		return "Analyse [id=" + id + ", url=" + url + ", dateCreate="
				+ dateCreate + "]";
	}
}