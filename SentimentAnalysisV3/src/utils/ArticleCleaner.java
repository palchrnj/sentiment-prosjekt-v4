package utils;

import java.io.IOException;








import preProcessing.NewsArticleWithTickers;
import preProcessing.NewsArticlesWithTickers;
import newsAPI.JsonHandler;
import newsAPI.NewsArticleRaw;
import newsAPI.NewsArticlesRaw;





public class ArticleCleaner {
	
	public NewsArticlesRaw articles;
	public String jsonArticle;


	public ArticleCleaner() throws IOException{
		
	}
	

	
	

	public NewsArticlesRaw getArticles() {
		return articles;
	}
	public void setArticles(NewsArticlesRaw articles) {
		this.articles = articles;
	}

	public String getJsonArticle() {
		return jsonArticle;
	}
	public void setJsonArticle(String jsonArticle) {
		this.jsonArticle = jsonArticle;
	}
	
	
	/* REMOVES BAD CHARACTERS FROM TEXT */
	public String removeBadChars(String fixString){
		String fixedString = "";
		fixedString = fixString.replaceAll("\\s+"," ");
		return fixedString;
	}

	
	/* REMOVES TITLE FROM MAIN TEXT */
	public String removeTitleFromMainText(NewsArticleRaw article){
		String fixedText = article.text;
		if(article.text.contains(article.title)){
		//	System.out.println("Title funnet i main text");
			fixedText = fixedText.replace(article.title,"");
		}	
		return fixedText; 
		
	}
	
	/* REMOVES LEAD TEXT FROM MAIN TEXT */
	public String removeLeadTextFromMainText(NewsArticleRaw article){
		String fixedText = article.text;
		String compareLead = article.lead_text;
		
		if(article.lead_text.charAt(0) == ' '){
			compareLead = article.lead_text.substring(1);
			
		}
		
		if(article.text.contains(compareLead)){
		//	System.out.println("Lead text funnet i main text");
			fixedText = fixedText.replace(compareLead,"");
		}
		return fixedText; 
	}
	
	/* ADDS SPACE TO "ARTIKKEL AV" */
	public String fixArticleBySpace(NewsArticleRaw article){
		String fixedText = article.text;

		if(article.text.contains("Artikkel av")){
			fixedText = fixedText.replace("Artikkel av"," Artikkel av");
			fixedText = fixedText.replace(",",", ");
		}
		return fixedText; 
	}
	
	
	/* CLEANS AN ARTICLE */
	public NewsArticleRaw cleanJsonObject(NewsArticleRaw article){
		NewsArticleRaw cleanedArticle = article;

		cleanedArticle.title = removeBadChars(article.title);
		cleanedArticle.lead_text = removeBadChars(article.lead_text);
		cleanedArticle.text = removeBadChars(article.text);
		
		cleanedArticle.text = removeTitleFromMainText(cleanedArticle);
		cleanedArticle.text = removeLeadTextFromMainText(cleanedArticle);
		
		return cleanedArticle;

	}
	/* CLEANS AN ARTICLE */
	public NewsArticleWithTickers cleanTickerObject(NewsArticleWithTickers article){
		NewsArticleWithTickers cleanedArticle = article;

		cleanedArticle.title = removeBadChars(article.title);
		cleanedArticle.lead_text = removeBadChars(article.lead_text);
		cleanedArticle.text = removeBadChars(article.text);
		
		cleanedArticle.text = removeTitleFromMainText(cleanedArticle);
		cleanedArticle.text = removeLeadTextFromMainText(cleanedArticle);
		
		return cleanedArticle;

	}
	
	public NewsArticlesRaw cleanAllArticles(NewsArticlesRaw articles){
		NewsArticlesRaw cleanArticles = new NewsArticlesRaw();
		NewsArticleRaw[] tempCleanList = new NewsArticleRaw[articles.getArticles().length];
		
		for(int i=0; i<articles.getArticles().length; i++){
			tempCleanList[i]= this.cleanJsonObject(articles.getArticles()[i]);	
		}
		cleanArticles.setArticles(tempCleanList);
		return cleanArticles;
	}
	

	
	public NewsArticlesWithTickers cleanAllTickersArticles(NewsArticlesWithTickers articles){
		NewsArticlesWithTickers cleanArticles = new NewsArticlesWithTickers();
		NewsArticlesWithTickers tempCleanList = new NewsArticlesWithTickers();
		
		for(int i=0; i<articles.getNewsArticlesWithTickers().size(); i++){
			tempCleanList.getNewsArticlesWithTickers().add(this.cleanTickerObject(articles.getNewsArticlesWithTickers().get(i)));	
		}
		
		cleanArticles.setNewsArticlesWithTickers(tempCleanList.getNewsArticlesWithTickers());
		return cleanArticles;
	}
	
	/* TEST METHOD */
	public static void main(String[] args) throws IOException{
		JsonHandler handler = new JsonHandler("/Misc/lars_annotated_1000_1426_19_13.json", "raw");
		
		ArticleCleaner annotationCleaner1 = new ArticleCleaner();

			
		System.out.println("SKITTEN VERSJON");
		System.out.println(handler.getArticles().getArticles()[22].title.toString());
		System.out.println(handler.getArticles().getArticles()[22].lead_text.toString());
		System.out.println(handler.getArticles().getArticles()[22].text.toString());
		
		System.out.println("REN VERSJON");
		NewsArticleRaw fresh = annotationCleaner1.cleanJsonObject(handler.getArticles().getArticles()[22]);
		System.out.println(fresh.title);
		System.out.println(fresh.lead_text);
		System.out.println(fresh.text);

	}
	
	
	
	
	
	
	
	

}
