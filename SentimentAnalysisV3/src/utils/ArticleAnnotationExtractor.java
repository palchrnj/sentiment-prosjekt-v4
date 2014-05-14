package utils;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import com.google.gson.Gson;

import hegnarArticleScraper.HegnarArticleExtractor;
import hegnarArticleScraper.HegnarArticleOverview;
import newsAPI.JsonHandler;
import preProcessing.NewsArticlesWithTickers;

public class ArticleAnnotationExtractor {
	
	public ArticleAnnotationExtractor(){
		
	}
	
	
	
	public NewsArticlesWithTickers getNewsArticlesFromTicker(String ticker, int interval) throws IOException{
		HegnarArticleOverview hao = new HegnarArticleOverview();
		
		NewsArticlesWithTickers allNewsArticles = hao.getArticlesFromTicker(ticker);
		NewsArticlesWithTickers articlesToBeAnnotated = new NewsArticlesWithTickers();
		
		for(int i=0; i<allNewsArticles.getNewsArticlesWithTickers().size(); i++){
			if(i%interval == 0){
				articlesToBeAnnotated.getNewsArticlesWithTickers().add(allNewsArticles.getNewsArticlesWithTickers().get(i));
			}
		}
		
		return articlesToBeAnnotated;
		
	}
	
	
	public void writeLinksToFile(String text, String path, String name) throws IOException{
		Writer out = new BufferedWriter(new OutputStreamWriter(
	    new FileOutputStream(path + "/"+name+".json"), "UTF-8"));
		try {
		    out.write(text);
		} finally {
		    out.close();
		}
	}
	public String getPath() {
	    String path = String.format("%s/%s", System.getProperty("user.dir"), this.getClass().getPackage().getName().replace(".", "/"));
	    return path.split(this.getClass().getPackage().getName())[0]+"/ArticleResources/HegnarArticlesNew/";
	}
	public void writeArticlesToFile(String ticker, int interval) throws IOException{
		Gson g = new Gson();
		String name = "NEW-ARTICLE-TO-ANNOTATE-"+ticker;
		NewsArticlesWithTickers nawt = this.getNewsArticlesFromTicker(ticker, interval);
		this.writeLinksToFile(g.toJson(nawt), this.getPath()+"/ArticlesToBeAnnotated/", name);	
	}
	
	public static void main(String[] args) throws IOException{
		ArticleAnnotationExtractor aae = new ArticleAnnotationExtractor();
		aae.writeArticlesToFile("STL", 10);
	}
	
	
	
	

}
