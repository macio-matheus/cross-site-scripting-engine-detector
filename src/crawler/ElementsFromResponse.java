package crawler;

import java.util.ArrayList;

import system.Form;
import system.UrlFound;

/**
 * Elementos encontrados na resposta do formulário
 * 
 * @author Matheus
 *
 */
public class ElementsFromResponse {
	private ArrayList<Form> forms;
	private ArrayList<UrlFound> urls;

	public ElementsFromResponse(ArrayList<Form> forms, ArrayList<UrlFound> urls) {
		setForms(forms);
		setUrls(urls);
	}

	public ArrayList<Form> getForms() {
		return forms;
	}

	public void setForms(ArrayList<Form> forms) {
		this.forms = forms;
	}

	public ArrayList<UrlFound> getUrls() {
		return urls;
	}

	public void setUrls(ArrayList<UrlFound> urls) {
		this.urls = urls;
	}

	@Override
	public String toString() {
		return "ElementsFromResponse [forms=" + forms + ", urls=" + urls + "]";
	}

}
