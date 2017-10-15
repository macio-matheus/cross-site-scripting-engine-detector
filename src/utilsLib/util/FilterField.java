package utilsLib.util;


/**
 * Esta classe serve para filtros. A princ�pio, para filtrar coisas com o mesmo
 * campo.
 * 
 * OBS: por enquanto, s� serve para opera��es diretamente com o campo.
 */
public class FilterField {
	private String relatedName;

	public FilterField(String relatedName) {
		this.relatedName = relatedName;
	}

	public String getRelatedName() {
		return relatedName;
	}

	public void setRelatedName(String relatedName) {
		this.relatedName = relatedName;
	}
}
