package utilsLib.util;

import java.io.IOException;
import java.util.StringTokenizer;

/**
 * Lê um modelo de e-mail em HTML ou texto. Se for HTML, tem que ter extensão
 * .htm ou .html; texto, qualquer outra.
 * 
 * Se for html, o título do e-mail é definido pela tag <title>TITULO</title>. Se
 * for texto, o título é definido pela primeira linha, o restante é o conteúdo.
 * 
 * A versão antiga, EmailTemplate, continua. Esta é a versão atual de leitor de
 * templates.
 */
public class TemplateForEmail {
	private boolean html;
	private String title;
	private String text;

	public TemplateForEmail(String path) throws IOException {
		if (path.endsWith(".htm") || path.endsWith(".html")) {
			html = true;
		}

		text = Utils.readStrFile(path);

		// extração do título
		title = "";

		String lowerText = text.toLowerCase();

		int openTitle = lowerText.indexOf("<title>");
		int closeTitle = lowerText.indexOf("</title>");

		if (openTitle != -1 && closeTitle != -1 && closeTitle > openTitle) {
			title = text.substring(openTitle + "<title>".length(), closeTitle);
		}
	}

	public String getText() {
		return text;
	}

	public String getTitle() {
		return title;
	}

	public boolean isHtml() {
		return html;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setHtml(boolean html) {
		this.html = html;
	}

	public void format(String[] values, String[] ids) {
		text = Utils.format(text, values, ids);
		title = Utils.format(title, values, ids);
	}
}
