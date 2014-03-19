package newsAPI;

/*CLASS THAT HOLDS RAW NEWS ARTICLES FROM API*/
public class NewsArticlesRaw {
	
	
	public NewsArticleRaw[] articles;
	
	public NewsArticlesRaw(){

	}
	
	public NewsArticleRaw[] getArticles() {
		return articles;
	}

	public void setArticles(NewsArticleRaw[] articles) {
		this.articles = articles;
	}	
}
