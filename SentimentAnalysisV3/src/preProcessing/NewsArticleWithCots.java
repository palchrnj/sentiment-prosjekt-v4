package preProcessing;

import java.util.HashMap;

public class NewsArticleWithCots extends NewsArticleWithStemmedVersion{
	
	public HashMap<String, Integer> cots;
	
	
	public NewsArticleWithCots() {

	}
	
	
	
	public HashMap<String, Integer> getCots() {
		return cots;
	}


	public void setCots(HashMap<String, Integer> cots) {
		this.cots = cots;
	}

}
