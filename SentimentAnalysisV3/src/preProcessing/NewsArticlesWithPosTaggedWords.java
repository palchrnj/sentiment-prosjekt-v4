package preProcessing;

import java.util.ArrayList;

public class NewsArticlesWithPosTaggedWords {
	
	public ArrayList<NewsArticleWithPosTaggedWords> nawpti;
	
	public NewsArticlesWithPosTaggedWords(){
		this.nawpti = new ArrayList<NewsArticleWithPosTaggedWords>();
		
	}

	public ArrayList<NewsArticleWithPosTaggedWords> getNawpti() {
		return nawpti;
	}

	public void setNawpti(ArrayList<NewsArticleWithPosTaggedWords> nawpti) {
		this.nawpti = nawpti;
	}

}
