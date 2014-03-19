package preProcessing;

import java.util.ArrayList;


public class NewsArticlesWithTickers {
	
	public ArrayList<NewsArticleWithTickers> newsArticlesWithTickers;
	
	
	public NewsArticlesWithTickers(){
		this.newsArticlesWithTickers = new ArrayList<NewsArticleWithTickers>();
	}
	public ArrayList<NewsArticleWithTickers> getNewsArticlesWithTickers() {
		return newsArticlesWithTickers;
	}

	public void setNewsArticlesWithTickers(
			ArrayList<NewsArticleWithTickers> newsArticlesWithTickers) {
		this.newsArticlesWithTickers = newsArticlesWithTickers;
	}
	
}
