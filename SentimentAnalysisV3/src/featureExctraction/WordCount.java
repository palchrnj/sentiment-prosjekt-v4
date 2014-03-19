package featureExctraction;

public class WordCount {
	
	int termFrequency;
	int documentFrequency;
	int titleCounter;
	int leadCounter;
	String word;
	String sentimentValue;
	
	
	public WordCount(String word){
		this.word = word;
		this.termFrequency = 1;
		this.titleCounter = 0;
		this.leadCounter = 0;
		this.sentimentValue = "0";
	}


	public int getCounter() {
		return termFrequency;
	}


	public void setCounter(int counter) {
		this.termFrequency = counter;
	}


	public String getWord() {
		return word;
	}


	public void setWord(String word) {
		this.word = word;
	}

	public String getSentimentValue() {
		return sentimentValue;
	}


	public void setSentimentValue(String sentimentValue) {
		this.sentimentValue = sentimentValue;
	}
	public int getTitleCounter() {
		return titleCounter;
	}


	public void setTitleCounter(int titleCounter) {
		this.titleCounter = titleCounter;
	}


	public int getLeadCounter() {
		return leadCounter;
	}


	public void setLeadCounter(int leadCounter) {
		this.leadCounter = leadCounter;
	}
	public int getTermFrequency() {
		return termFrequency;
	}


	public void setTermFrequency(int termFrequency) {
		this.termFrequency = termFrequency;
	}


	public int getDocumentFrequency() {
		return documentFrequency;
	}


	public void setDocumentFrequency(int documentFrequency) {
		this.documentFrequency = documentFrequency;
	}

	
	

}
