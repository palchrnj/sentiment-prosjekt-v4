package utils;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

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
	
	public NewsArticlesWithTickers getNewsArticlesFromTickerNoInterval(String ticker) throws IOException{
		HegnarArticleOverview hao = new HegnarArticleOverview();
		
		NewsArticlesWithTickers allNewsArticles = hao.getArticlesFromTickerAnnotated(ticker);

		return allNewsArticles;
		
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
	
	public void writeCombinedArticlesToFile() throws IOException{
		Gson g = new Gson();
		String name = "NEW-ARTICLE-TO-ANNOTATE-COMBINED";
		NewsArticlesWithTickers combinedNawts = new NewsArticlesWithTickers();
		
		NewsArticlesWithTickers FUNCOMarticles = this.getNewsArticlesFromTickerNoInterval("FUNCOM");
		NewsArticlesWithTickers IOXarticles = this.getNewsArticlesFromTickerNoInterval("IOX");
		NewsArticlesWithTickers NAURarticles = this.getNewsArticlesFromTickerNoInterval("NAUR");
		NewsArticlesWithTickers NORarticles = this.getNewsArticlesFromTickerNoInterval("NOR");
		NewsArticlesWithTickers NSGarticles = this.getNewsArticlesFromTickerNoInterval("NSG");
		NewsArticlesWithTickers RCLarticles = this.getNewsArticlesFromTickerNoInterval("RCL");
		NewsArticlesWithTickers SDRLarticles = this.getNewsArticlesFromTickerNoInterval("SDRL");
		NewsArticlesWithTickers STLarticles = this.getNewsArticlesFromTickerNoInterval("STL");
		NewsArticlesWithTickers TELarticles = this.getNewsArticlesFromTickerNoInterval("TEL");
		NewsArticlesWithTickers YARarticles = this.getNewsArticlesFromTickerNoInterval("YAR");
		
		
		combinedNawts.getNewsArticlesWithTickers().addAll(FUNCOMarticles.getNewsArticlesWithTickers());
		combinedNawts.getNewsArticlesWithTickers().addAll(IOXarticles.getNewsArticlesWithTickers());
		combinedNawts.getNewsArticlesWithTickers().addAll(NAURarticles.getNewsArticlesWithTickers());
		combinedNawts.getNewsArticlesWithTickers().addAll(NORarticles.getNewsArticlesWithTickers());
		combinedNawts.getNewsArticlesWithTickers().addAll(NSGarticles.getNewsArticlesWithTickers());
		combinedNawts.getNewsArticlesWithTickers().addAll(RCLarticles.getNewsArticlesWithTickers());
		combinedNawts.getNewsArticlesWithTickers().addAll(SDRLarticles.getNewsArticlesWithTickers());
		combinedNawts.getNewsArticlesWithTickers().addAll(STLarticles.getNewsArticlesWithTickers());
		combinedNawts.getNewsArticlesWithTickers().addAll(TELarticles.getNewsArticlesWithTickers());
		combinedNawts.getNewsArticlesWithTickers().addAll(YARarticles.getNewsArticlesWithTickers());
		
		
		System.out.println(combinedNawts.getNewsArticlesWithTickers().size());
		
		
		this.writeLinksToFile(g.toJson(combinedNawts), this.getPath()+"/ArticlesAnnotated/", name);	
	}
	
	
	public static void main(String[] args) throws IOException{
		ArticleAnnotationExtractor aae = new ArticleAnnotationExtractor();
		aae.writeCombinedArticlesToFile();
	}
	
	
	
	

}
