package utils;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import com.google.gson.Gson;

import cot.CoTCounter;
import featureExctraction.NewsArticleWithFeatures;
import featureExctraction.NewsArticlesWithFeatures;
import preProcessing.HegnarTickerScraper;
import preProcessing.NewsArticleWithCots;
import preProcessing.NewsArticlesWithCots;
import preProcessing.NewsArticlesWithPosTaggedWords;
import preProcessing.NewsArticlesWithStemmedVersion;
import preProcessing.NewsArticlesWithTickers;
import preProcessing.PosTaggedJsonCreater;
import newsAPI.JsonHandler;
import newsAPI.NewsArticlesRaw;

public class ArticleTypeGenerator {
	
	
	public ArticleTypeGenerator(){
		
	};
	public void generateCleanRawArticles(String UntouchedArticlesFileSource, String newFileName) throws IOException{
		JsonHandler untouchedHandler = new JsonHandler(UntouchedArticlesFileSource, "raw");
		ArticleCleaner ac = new ArticleCleaner();
		NewsArticlesRaw rawArticles = ac.cleanAllArticles(untouchedHandler.getArticles());;
		Gson gson = new Gson();
		String rawArticlesAsJson = gson.toJson(rawArticles);
		this.writeToArticleFile(rawArticlesAsJson, this.getPath()+"ArticleSteps/1_RawArticles", newFileName);
		
	}
	public void generateCleanTickerArticles(String UntouchedArticlesFileSource, String newFileName) throws IOException{
		JsonHandler untouchedHandler = new JsonHandler(UntouchedArticlesFileSource, "ticker");
		ArticleCleaner ac = new ArticleCleaner();
		NewsArticlesWithTickers tickerArticles = ac.cleanAllTickersArticles(untouchedHandler.getTickerArticles());
		Gson gson = new Gson();
		String tickerArticlesAsJson = gson.toJson(tickerArticles);
		this.writeToArticleFile(tickerArticlesAsJson, this.getPath()+"ArticleSteps/1_RawArticles", newFileName);
		
	}
	public void generateTickerArticles(String RawArticlesFileSource, String newFileName) throws IOException{
		JsonHandler rawHandler = new JsonHandler(RawArticlesFileSource, "raw");
		System.out.println("Handler before ticker: " + rawHandler.getArticles().getArticles()[0].lead_text);
		HegnarTickerScraper hts = new HegnarTickerScraper();
		NewsArticlesWithTickers tickerArticles = hts.getArticlesWithTicker(rawHandler);
		System.out.println(tickerArticles.getNewsArticlesWithTickers().get(0).getText());
		Gson gson = new Gson();
		String tickerArticlesAsJson = gson.toJson(tickerArticles);
		this.writeToArticleFile(tickerArticlesAsJson, this.getPath()+"ArticleSteps/2_TickerArticles", newFileName);	
	}
	
	public void generatePOStaggedArticles(String TickerArticlesFileSource, String newFileName) throws IOException{
		JsonHandler tickerHandler = new JsonHandler(TickerArticlesFileSource, "ticker");
		PosTaggedJsonCreater ptjc = new PosTaggedJsonCreater();
		String posTaggedArticlesAsJson = ptjc.getAllArticlesAsJson(tickerHandler.getTickerArticles());
		this.writeToArticleFile(posTaggedArticlesAsJson, this.getPath()+"ArticleSteps/3_POStaggedArticles", newFileName);	
	}
	
	public void generateStemmedArticles(String PosTaggedArticleFileSource, String newFileName) throws IOException{
		JsonHandler posHandler = new JsonHandler(PosTaggedArticleFileSource, "pos");
		Stemmer s = new Stemmer();
		NewsArticlesWithStemmedVersion stemmedArticles = s.stemAllArticles(posHandler.getPosTaggedArticles());
		Gson gson = new Gson();
		String stemmedArticlesAsJson = gson.toJson(stemmedArticles);
		this.writeToArticleFile(stemmedArticlesAsJson, this.getPath()+"ArticleSteps/4_StemmedArticles", newFileName);	
	}
	
	public void generateCotsArticles(String StemmedArticleFileSource, String newFileName) throws IOException{
		CoTCounter cc = new CoTCounter(3);
		
		JsonHandler handler = new JsonHandler(StemmedArticleFileSource, "stemmed");
		
		NewsArticlesWithStemmedVersion nawsv = handler.stemmedArticles;
		System.out.println("Size: " + nawsv.getNawsv().size());
		
		NewsArticlesWithCots nawcs = new NewsArticlesWithCots();
		
		ArrayList<NewsArticleWithCots>  cotsList = new ArrayList<NewsArticleWithCots>();
		for(int i=0; i<nawsv.getNawsv().size(); i++){
				//System.out.println(nawsv.getNawsv().get(i).getId());
				System.out.println(cc.initiateCotsArticle(nawsv.getNawsv().get(i)));
				cotsList.add(cc.initiateCotsArticle(nawsv.getNawsv().get(i)));	
		}
		nawcs.setNawc(cotsList);
		
		Gson gson = new Gson();
		String cotsArticlesAsJson = gson.toJson(nawcs);
		this.writeToArticleFile(cotsArticlesAsJson, this.getPath()+"ArticleSteps/5_CotsArticles", newFileName);	
	}
	
	
	public void generateFeatureArticles(String StememdArticleFileSource, String newFileName) throws IOException{
		Stemmer s = new Stemmer();
		NewsArticlesWithFeatures newsArticlesWithFeatures = new NewsArticlesWithFeatures();
		newsArticlesWithFeatures.initiateNewsArticlesWithFeatures(StememdArticleFileSource);
		Gson gson = new Gson();
		String featureArticlesAsJson = gson.toJson(newsArticlesWithFeatures);
		this.writeToArticleFile(featureArticlesAsJson, this.getPath()+"ArticleSteps/5_FeatureArticles", newFileName);	
	}
	
	
	public void writeToArticleFile(String text, String path, String name) throws IOException{
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
	    return path.split(this.getClass().getPackage().getName())[0]+"/ArticleResources/";
	}
	
	public static void main(String[] args) throws IOException{
		ArticleTypeGenerator atg = new ArticleTypeGenerator();
		//atg.generateCleanRawArticles("ArticleSteps/0_UntouchedArticles/MainDataSet.txt", "MainDataSetClean");
		//atg.generateCleanTickerArticles("ArticleSteps/0_UntouchedArticles/MainDataSet.txt", "MainDataSetClean");
		
		//atg.generateTickerArticles("ArticleSteps/1_RawArticles/ArticleGeneratorTestClean.json", "ArticleGeneratorTestTicker");
		//atg.generatePOStaggedArticles("ArticleSteps/2_TickerArticles/MainDataSetClean.json", "MainDataSetPOS");
		//atg.generateStemmedArticles("ArticleSteps/3_POStaggedArticles/MainDataSetPOS.json", "MainDataSetStemmed");
		atg.generateCotsArticles("ArticleSteps/4_StemmedArticles/MainDataSetStemmed.json", "MainDataSetCOTS");
		
		//atg.generateFeatureArticles("ArticleSteps/4_StemmedArticles/ArticleGeneratorTestStemmed.json", "ArticleGeneratorTestFeatures");
		
	}
	

}
