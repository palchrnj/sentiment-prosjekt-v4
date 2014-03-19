package preProcessing;

import java.io.IOException;

import newsAPI.JsonHandler;
import newsAPI.NewsArticleRaw;

public class ArticleCategoryGenerator {
	
	
	public ArticleCategoryGenerator(){
		
		
	}
	
	/* GETS CATEGORY STRING FROM URL */
	public String extractCategory(NewsArticleRaw article){
		String category = "";
		String articleString = article.getLinks()[0];
		
		category = articleString.split("http://www.hegnar.no/")[1].split("/article")[0];
		
		//System.out.println(category);
		return category;	
	}
	
	
	
	public static void main(String[] args) throws IOException{
		ArticleCategoryGenerator generator = new ArticleCategoryGenerator();
		ArticleCategories ac = new ArticleCategories();
		
		
		JsonHandler handler = new JsonHandler();
		
		for(int i=0; i<handler.getArticles().getArticles().length; i++){
			generator.extractCategory(handler.getArticles().getArticles()[i]);
			ac.addCategory(generator.extractCategory(handler.getArticles().getArticles()[i]), handler.getArticles().getArticles()[i]);
		}
		
		ac.writeToFileAsJson();
	}
	

	
	
	
	
	
	
	

}
