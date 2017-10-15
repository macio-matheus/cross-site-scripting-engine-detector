package utilsLib.util;

import utilsLib.util.data.KeywordRecognizer;

/**
 * Informação sobre paginação de conjunto de dados.
 */
public class PagingInfo {
	private int currPage;
	private int pageSize;
	private int regsCount;
	private boolean simplified;
	private int orderedLevel;
	private KeywordRecognizer keywordRecognizer;

	public PagingInfo(int pageSize) {
		this();
		setPageSize(pageSize);
	}

	public PagingInfo() {
		setCurrPage(1);
		setRegsCount(Utils.UNDEFINED_NUMBER);
		setSimplified(false);
	}

	public int getCurrPage() {
		return currPage;
	}

	public int getCurrPageSize() {
		int currPageSize = 0;

		if (this.getLastRegPos() <= this.getRegsCount()) {
			if (this.getFirstRegPos() < this.getRegsCount()
					- (this.getPageSize() - 1)) {
				currPageSize = this.getPageSize();
			} else {
				currPageSize = (this.getRegsCount() - this.getFirstRegPos()) + 1;
			}
		}
		return currPageSize;
	}

	public void setCurrPage(int currPage) {
		if (currPage < 1) {
			throw new IllegalArgumentException("Parametro inválido: currPage");
		}
		this.currPage = currPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		// 2012-08-02 Se for 0, dá aquele negócio de pegar apenas o count.
		if (pageSize < 0) {
			throw new IllegalArgumentException(
					"Parametro inválido: pageSize. Ele deve ser maior ou igual a 0.");
		}

		this.pageSize = pageSize;
	}

	public int getRegsCount() {
		return regsCount;
	}

	/**
	 * Armazena a quantidade de registros e automaticamente atualiza a
	 * quantidade de páginas.
	 * 
	 * @param regsCount
	 */
	public void setRegsCount(int regsCount) {
		this.regsCount = regsCount;
	}

	public int getPagesCount() {
		int pagesCount = 0;

		// Se já definiu regsCount e o tamanho da página é válido.
		if (regsCount > 0 && regsCount != Utils.UNDEFINED_NUMBER
				&& getPageSize() > 0) {

			int diff = regsCount % getPageSize();

			if (diff == 0) {
				pagesCount = regsCount / getPageSize();
			} else {
				pagesCount = ((regsCount - diff) / getPageSize()) + 1;
			}
		}

		return pagesCount;
	}

	/**
	 * Recupera a posição do primeiro registro a ser exibido da paginação. Para
	 * isto, deve haver qtd de registros gravado.
	 * 
	 * @return O posição do primeiro elemento, começando por 1.
	 */
	public int getFirstRegPos() {
		return ((getCurrPage() - 1) * getPageSize()) + 1;
	}

	/**
	 * Recupera a posição do primeiro registro a ser exibido da paginação.
	 * 
	 * @return O posição do primeiro elemento, começando por 1.
	 */
	public int getLastRegPos() {
		int lastRegPos = getCurrPage() * this.getPageSize();

		if (this.getCurrPage() == this.getPagesCount()) {
			int diff = this.getRegsCount() % this.getPageSize();
			if (diff != 0) {
				lastRegPos -= this.getPageSize() - diff;
			}
		} else if (this.getCurrPage() > this.getPagesCount()) {
			lastRegPos = 0;
		}

		return lastRegPos;
	}

	public boolean isSimplified() {
		return simplified;
	}

	public void setSimplified(boolean simplified) {
		this.simplified = simplified;
	}

	public KeywordRecognizer getKeywordRecognizer() {
		return keywordRecognizer;
	}

	public void setKeywordRecognizer(KeywordRecognizer keywordRecognizer) {
		this.keywordRecognizer = keywordRecognizer;
	}

	public Object clone() {
		PagingInfo tempPi = new PagingInfo();
		tempPi.setCurrPage(this.getCurrPage());
		tempPi.setPageSize(this.getPageSize());
		tempPi.setRegsCount(this.getRegsCount());
		tempPi.setSimplified(this.isSimplified());
		tempPi.setKeywordRecognizer(this.getKeywordRecognizer());

		return tempPi;
	}

	public String toString() {
		return "currPage: "
				+ currPage + " | pageSize: " + pageSize + 
				" | regsCount: " + regsCount
				+ " simplified: " + simplified;
	}

	public String toSimpleString() {
		return "currPage: " + currPage + " | pageSize: " + pageSize;
	}

	public int getOrderedLevel() {
		return orderedLevel;
	}

	public void setOrderedLevel(int orderedLevel) {
		this.orderedLevel = orderedLevel;
	}

	public boolean equals(Object obj) {
		PagingInfo cpi = (PagingInfo) obj;

		return this.currPage == cpi.currPage && this.pageSize == cpi.pageSize
				&& this.regsCount == cpi.regsCount
				&& this.simplified == cpi.simplified
				&& this.orderedLevel == cpi.orderedLevel;
	}

	public void copy(PagingInfo cpi) {
		if (cpi != null) {
			this.currPage = cpi.currPage;
			this.pageSize = cpi.pageSize;
			this.regsCount = cpi.regsCount;
			this.simplified = cpi.simplified;
			this.orderedLevel = cpi.orderedLevel;
		}
	}
}
