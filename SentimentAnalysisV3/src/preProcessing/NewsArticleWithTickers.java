package preProcessing;

import java.util.ArrayList;

import newsAPI.NewsArticleRaw;


public class NewsArticleWithTickers extends NewsArticleRaw{
	
	public ArrayList<String> tickerList;
	public String authorName;
	public ArrayList<String> keywordList; 
	
	
	public NewsArticleWithTickers(){
		
		
	}

	public ArrayList<String> getTickerList() {
		return tickerList;
	}

	public void setTickerList(ArrayList<String> tickerList) {
		this.tickerList = tickerList;
	}

	public ArrayList<String> getKeywordList() {
		return keywordList;
	}

	public void setKeywordList(ArrayList<String> keywordList) {
		this.keywordList = keywordList;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	
	
	

}
