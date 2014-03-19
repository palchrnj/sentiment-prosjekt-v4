package preProcessing;

import java.util.ArrayList;

public class NewsArticlesWithStemmedVersion {
	public ArrayList<NewsArticleWithStemmedVersion> nawsv;
	
	
	public NewsArticlesWithStemmedVersion(){
		this.nawsv = new ArrayList<NewsArticleWithStemmedVersion>();
	}
	
	
	public ArrayList<NewsArticleWithStemmedVersion> getNawsv() {
		return nawsv;
	}
	public void setNawsv(ArrayList<NewsArticleWithStemmedVersion> nawsv) {
		this.nawsv = nawsv;
	}
	
}
