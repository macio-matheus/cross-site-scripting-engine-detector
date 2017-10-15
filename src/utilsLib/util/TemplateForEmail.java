package utilsLib.util;

import java.io.IOException;
import java.util.StringTokenizer;

/**
 * L� um modelo de e-mail em HTML ou texto. Se for HTML, tem que ter extens�o
 * .htm ou .html; texto, qualquer outra.
 * 
 * Se for html, o t�tulo do e-mail � definido pela tag <title>TITULO</title>. Se
 * for texto, o t�tulo � definido pela primeira linha, o restante � o conte�do.
 * 
 * A vers�o antiga, EmailTemplate, continua. Esta � a vers�o atual de leitor de
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

		// extra��o do t�tulo
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
