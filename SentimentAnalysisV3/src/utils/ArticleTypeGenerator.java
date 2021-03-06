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
	
	public void generatePOStaggedArticles(String TickerArticlesFileSource, String newFileName, int from, int to) throws IOException{
		JsonHandler tickerHandler = new JsonHandler(TickerArticlesFileSource, "ticker");
		PosTaggedJsonCreater ptjc = new PosTaggedJsonCreater();
		String posTaggedArticlesAsJson = ptjc.getAllArticlesAsJson(tickerHandler.getTickerArticles(), from, to);
		this.writeToArticleFile(posTaggedArticlesAsJson, this.getPath()+"ArticleSteps/3_POStaggedArticles", newFileName);	
	}
	
	//COMBINE POS TAGGED ARTICLES
	public void combinePosArticles() throws IOException{
		NewsArticlesWithPosTaggedWords allCombined1 = new NewsArticlesWithPosTaggedWords();
		NewsArticlesWithPosTaggedWords allCombined2 = new NewsArticlesWithPosTaggedWords();
		NewsArticlesWithPosTaggedWords allCombined3 = new NewsArticlesWithPosTaggedWords();
		NewsArticlesWithPosTaggedWords allCombined4 = new NewsArticlesWithPosTaggedWords();
		NewsArticlesWithPosTaggedWords allCombined5 = new NewsArticlesWithPosTaggedWords();
		
		JsonHandler j1 = new JsonHandler("ArticleSteps/3_POStaggedArticles/NEW-HEGNAR-ARTICLES-COMBINED-POS-0-1000.json", "pos");
		NewsArticlesWithPosTaggedWords nawptw1 = j1.getPosTaggedArticles();
		allCombined1.getNawpti().addAll(nawptw1.getNawpti());
		j1 = null;
		nawptw1 = null;
		
		JsonHandler j2 = new JsonHandler("ArticleSteps/3_POStaggedArticles/NEW-HEGNAR-ARTICLES-COMBINED-POS-1000-2000.json", "pos");
		NewsArticlesWithPosTaggedWords nawptw2 = j2.getPosTaggedArticles();
		allCombined1.getNawpti().addAll(nawptw2.getNawpti());
		j2 = null;
		
		nawptw2 = null;
		
		JsonHandler j3 = new JsonHandler("ArticleSteps/3_POStaggedArticles/NEW-HEGNAR-ARTICLES-COMBINED-POS-2000-3000.json", "pos");
		NewsArticlesWithPosTaggedWords nawptw3 = j3.getPosTaggedArticles();
		allCombined2.getNawpti().addAll(nawptw3.getNawpti());
		j3 = null;
		
		nawptw3 = null;
		JsonHandler j4 = new JsonHandler("ArticleSteps/3_POStaggedArticles/NEW-HEGNAR-ARTICLES-COMBINED-POS-3000-4000.json", "pos");
		NewsArticlesWithPosTaggedWords nawptw4 = j4.getPosTaggedArticles();
		allCombined2.getNawpti().addAll(nawptw4.getNawpti());
		j4 = null;
		
		nawptw4 = null;
		JsonHandler j5 = new JsonHandler("ArticleSteps/3_POStaggedArticles/NEW-HEGNAR-ARTICLES-COMBINED-POS-4000-4500.json", "pos");
		NewsArticlesWithPosTaggedWords nawptw5 = j5.getPosTaggedArticles();
		allCombined3.getNawpti().addAll(nawptw5.getNawpti());
		j5 = null;
		
		nawptw5 = null;
		JsonHandler j6 = new JsonHandler("ArticleSteps/3_POStaggedArticles/NEW-HEGNAR-ARTICLES-COMBINED-POS-4500-5000.json", "pos");
		NewsArticlesWithPosTaggedWords nawptw6 = j6.getPosTaggedArticles();
		allCombined3.getNawpti().addAll(nawptw6.getNawpti());
		j6 = null;
		
		nawptw6 = null;
		JsonHandler j7 = new JsonHandler("ArticleSteps/3_POStaggedArticles/NEW-HEGNAR-ARTICLES-COMBINED-POS-5000-6000.json", "pos");
		NewsArticlesWithPosTaggedWords nawptw7 = j7.getPosTaggedArticles();
		allCombined4.getNawpti().addAll(nawptw7.getNawpti());
		j7 = null;
		
		nawptw7 = null;
		JsonHandler j8 = new JsonHandler("ArticleSteps/3_POStaggedArticles/NEW-HEGNAR-ARTICLES-COMBINED-POS-6000-7000.json", "pos");
		NewsArticlesWithPosTaggedWords nawptw8 = j8.getPosTaggedArticles();
		allCombined4.getNawpti().addAll(nawptw8.getNawpti());
		j8 = null;
		
		nawptw8 = null;
		JsonHandler j9 = new JsonHandler("ArticleSteps/3_POStaggedArticles/NEW-HEGNAR-ARTICLES-COMBINED-POS-7000-8000.json", "pos");
		NewsArticlesWithPosTaggedWords nawptw9 = j9.getPosTaggedArticles();
		allCombined5.getNawpti().addAll(nawptw9.getNawpti());
		j9 = null;
		
		nawptw9 = null;
		JsonHandler j10 = new JsonHandler("ArticleSteps/3_POStaggedArticles/NEW-HEGNAR-ARTICLES-COMBINED-POS-8000-9000.json", "pos");
		NewsArticlesWithPosTaggedWords nawptw10 = j10.getPosTaggedArticles();
		allCombined5.getNawpti().addAll(nawptw10.getNawpti());
		j10 = null;
		
		nawptw10 = null;
		JsonHandler j11 = new JsonHandler("ArticleSteps/3_POStaggedArticles/NEW-HEGNAR-ARTICLES-COMBINED-POS-9000-9547.json", "pos");
		NewsArticlesWithPosTaggedWords nawptw11 = j11.getPosTaggedArticles();
		allCombined5.getNawpti().addAll(nawptw11.getNawpti());
		j11 = null;
		
		nawptw11 = null;

		
		Gson g = new Gson();
		
		this.writeToArticleFile(g.toJson(allCombined1), this.getPath()+"ArticleSteps/3_POStaggedArticles", "NEW-HEGNAR-ARTICLES-POS-ALL-1");	
		this.writeToArticleFile(g.toJson(allCombined2), this.getPath()+"ArticleSteps/3_POStaggedArticles", "NEW-HEGNAR-ARTICLES-POS-ALL-2");	
		this.writeToArticleFile(g.toJson(allCombined3), this.getPath()+"ArticleSteps/3_POStaggedArticles", "NEW-HEGNAR-ARTICLES-POS-ALL-3");
		this.writeToArticleFile(g.toJson(allCombined4), this.getPath()+"ArticleSteps/3_POStaggedArticles", "NEW-HEGNAR-ARTICLES-POS-ALL-4");	
		this.writeToArticleFile(g.toJson(allCombined5), this.getPath()+"ArticleSteps/3_POStaggedArticles", "NEW-HEGNAR-ARTICLES-POS-ALL-5");	

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
		System.out.println("STEMMED TITLE: " + nawsv.getNawsv().get(1).getStemmedTitle());
		
//		NewsArticlesWithCots nawcs = new NewsArticlesWithCots();
//		
//		ArrayList<NewsArticleWithCots>  cotsList = new ArrayList<NewsArticleWithCots>();
//		for(int i=0; i<nawsv.getNawsv().size(); i++){
//				//System.out.println(nawsv.getNawsv().get(i).getId());
//				System.out.println(cc.initiateCotsArticle(nawsv.getNawsv().get(i)));
//				cotsList.add(cc.initiateCotsArticle(nawsv.getNawsv().get(i)));	
//		}
//		nawcs.setNawc(cotsList);
//		
//		Gson gson = new Gson();
//		String cotsArticlesAsJson = gson.toJson(nawcs);
//		this.writeToArticleFile(cotsArticlesAsJson, this.getPath()+"ArticleSteps/5_CotsArticles", newFileName);	
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
	
//	public int getNumberOfArticles() throws IOException{
//		JsonHandler h = new JsonHandler("/ArticleSteps/3_POStaggedArticles/NEW-ARTICLE-TO-ANNOTATE-COMBINED-POS.json", "pos");
//		System.out.println(h.getTickerArticles());
//		return h.getTickerArticles().getNewsArticlesWithTickers().size();
//	}
	
	public static void main(String[] args) throws IOException{
		ArticleTypeGenerator atg = new ArticleTypeGenerator();
//		//atg.generateCleanRawArticles("ArticleSteps/0_UntouchedArticles/MainDataSet.txt", "MainDataSetClean");
//		//atg.generateCleanTickerArticles("ArticleSteps/0_UntouchedArticles/MainDataSet.txt", "MainDataSetClean");
//
//		int lower = 9000;
//		int upper = 9547;
//
//		//atg.generateTickerArticles("ArticleSteps/1_RawArticles/ArticleGeneratorTestClean.json", "ArticleGeneratorTestTicker");
//		atg.generatePOStaggedArticles("ArticleSteps/2_TickerArticles/NEW-HEGNAR-ARTICLES-COMBINED.json", "NEW-HEGNAR-ARTICLES-COMBINED-POS-" + lower + "-" + upper, lower, upper);
		atg.generateStemmedArticles("ArticleSteps/3_POStaggedArticles/NEW-HEGNAR-ARTICLES-POS-ALL-1.json", "NEW-HEGNAR-ARTICLES-STEMMED-1");
		atg.generateStemmedArticles("ArticleSteps/3_POStaggedArticles/NEW-HEGNAR-ARTICLES-POS-ALL-2.json", "NEW-HEGNAR-ARTICLES-STEMMED-2");
		atg.generateStemmedArticles("ArticleSteps/3_POStaggedArticles/NEW-HEGNAR-ARTICLES-POS-ALL-3.json", "NEW-HEGNAR-ARTICLES-STEMMED-3");
		atg.generateStemmedArticles("ArticleSteps/3_POStaggedArticles/NEW-HEGNAR-ARTICLES-POS-ALL-4.json", "NEW-HEGNAR-ARTICLES-STEMMED-4");
		atg.generateStemmedArticles("ArticleSteps/3_POStaggedArticles/NEW-HEGNAR-ARTICLES-POS-ALL-5.json", "NEW-HEGNAR-ARTICLES-STEMMED-5");
		//atg.generateCotsArticles("ArticleSteps/4_StemmedArticles/MainDataSetStemmed.json", "MainDataSetCOTS");
	
		//atg.generateFeatureArticles("ArticleSteps/4_StemmedArticles/ArticleGeneratorTestStemmed.json", "ArticleGeneratorTestFeatures");
		//atg.combinePosArticles();
	}
}
