package cot;

public class COTStat {
	
	public String term1;
	public String term2;
	
	public int TFcot;
	public int DFcot;
	
	public int TFterm1;
	public int DFterm1;

	public int TFterm2;
	public int DFterm2;
	
	public double tf;
	public double idf;
	public double tfidf;
	public double mi;
	public double chi;
	
	public COTStat(String term1, String term2) {
		this.term1 = term1;
		this.term2 = term2;
		
		TFcot = 0;
		DFcot = 0;
		
		TFterm1 = 0;
		DFterm1 = 0;

		TFterm2 = 0;
		DFterm2 = 0;
		
		tf = 0.0;
		idf = 0.0;
		tfidf = 0.0;
		mi = 0.0;
		chi = 0.0;
	}

	public COTStat(String term1, String term2, int TFcot) {
		this(term1, term2);
		this.TFcot = TFcot;
	}

	public COTStat(String term1, String term2, int TFcot, int DFcot) {
		this(term1, term2);
		this.TFcot = TFcot;
		this.DFcot = DFcot;
	}
	
	public COTStat(String term1, String term2, int tFcot, int dFcot,
			int tFterm1, int dFterm1, int tFterm2, int dFterm2) {
		this.term1 = term1;
		this.term2 = term2;
		TFcot = tFcot;
		DFcot = dFcot;
		TFterm1 = tFterm1;
		DFterm1 = dFterm1;
		TFterm2 = tFterm2;
		DFterm2 = dFterm2;
	}

//	public String toString() {
//		return term1 + " " + term2;
//	}
	
	public void computeAllStats(int N) {
		// tf
		tf = TFcot;
		// idf
		idf = ((double) N) / ((double) DFcot);
		// tfidf
		tfidf = ((double) tf) * Math.log(idf); 
		// mi
		mi = ( ((double) DFcot) / ((double) N) ) * Math.log( ((double) N * DFcot) / ((double) DFterm1 * DFterm2) );
		// chi
		chi = Math.pow(((double) DFcot) - ((double) N * DFterm1 * DFterm2), 2) / ((double) N * DFterm1 * DFterm2);
	}
	
	public String toString() {
		return String.format("[%s %s, TFcot=%s, DFcot=%s, TFterm1=%s, DFterm1=%s, TFterm2=%s, DFterm2=%s]", term1, term2, TFcot, DFcot, TFterm1, DFterm1, TFterm2, DFterm2);
	}

	public String getStatString() {
		return String.format("[%s %s, tf=%s, idf=%s, tfidf=%s, mi=%s, chi=%s]", term1, term2, tf, idf, tfidf, mi, chi);
	}
	
	
}
