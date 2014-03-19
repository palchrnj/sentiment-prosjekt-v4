package cot;

public class CotCountTFDF {
	int termFrequency;
	int documentFrequency;
	
	public CotCountTFDF(){
		this.termFrequency = 0;
		this.documentFrequency = 0;
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