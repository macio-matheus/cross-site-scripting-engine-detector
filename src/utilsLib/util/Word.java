package utilsLib.util;

public class Word implements Comparable<Word> {
	private String word;
	private String original;

	private String lowerWord;
	private String tonelessWord;

	private int firstPos;
	private boolean highlightedWord;
	private int count;

	private double points;
	private int currSentence;

	public Word(String word, String original) {
		this.word = word;
		this.original = original;
		
		this.lowerWord = word.toLowerCase();
		this.tonelessWord = Utils.removeTones(this.lowerWord);
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public int getFirstPos() {
		return firstPos;
	}

	public void setFirstPos(int firstPos) {
		this.firstPos = firstPos;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int compareTo(Word o) {
		double d = (o.points - this.points);
		
		if (d < 0) {
			return -1;
		} else if (d > 0) {
			return 1;
		} else {
			return 0;
		}
//		return (this.word.compareTo(o.word));
		/*
		 * int diff = o.count - this.count; if (diff == 0) { diff =
		 * this.firstPos - this.firstPos; }
		 * 
		 * return diff;
		 */
	}

	public String getLowerWord() {
		return lowerWord;
	}

	public void setLowerWord(String lowerWord) {
		this.lowerWord = lowerWord;
	}

	public String getTonelessWord() {
		return tonelessWord;
	}

	public void setTonelessWord(String tonelessWord) {
		this.tonelessWord = tonelessWord;
	}

	public double getPoints() {
		return points;
	}

	public void setPoints(double points) {
		this.points = points;
	}

	public int getCurrSentence() {
		return currSentence;
	}

	public void setCurrSentence(int currSentence) {
		this.currSentence = currSentence;
	}
	
	public Word clone() {
		Word w = new Word(word, original);

		w.setFirstPos(firstPos);
		w.setCount(count);
		w.setPoints(points);
		w.setCurrSentence(currSentence);

		return w;
	}

	public String getOriginal() {
		return original;
	}

	public void setOriginal(String original) {
		this.original = original;
	}

	public boolean isHighlightedWord() {
		return highlightedWord;
	}

	public void setHighlightedWord(boolean highlightedWord) {
		this.highlightedWord = highlightedWord;
	}
}
