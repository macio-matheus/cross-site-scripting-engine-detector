package crawler;

import java.util.ArrayList;

import system.Form;
import system.UrlFound;

/**
 * Generalização de elementos encontrados na resposta ou em urls
 * 
 * @author Matheus
 *
 */
public class ElementsFound {
	
	private Form form;
	private UrlFound url;
	private ArrayList<ElementsFromResponse> elements;

	public ElementsFound(UrlFound url, ArrayList<ElementsFromResponse> elements) {
		setUrl(url);
		setElements(elements);
	}
	
	public ElementsFound(Form form, ArrayList<ElementsFromResponse> elements) {
		setForm(form);
		setElements(elements);
	}

	public Form getForm() {
		return form;
	}

	public void setForm(Form form) {
		this.form = form;
	}

	public UrlFound getUrl() {
		return url;
	}

	public void setUrl(UrlFound url) {
		this.url = url;
	}

	public ArrayList<ElementsFromResponse> getElements() {
		return elements;
	}

	public void setElements(ArrayList<ElementsFromResponse> elements) {
		this.elements = elements;
	}
	
	@Override
	public String toString() {
		return "ElementsFromUrl [url=" + url + ", elements=" + elements + "]";
	}

}
