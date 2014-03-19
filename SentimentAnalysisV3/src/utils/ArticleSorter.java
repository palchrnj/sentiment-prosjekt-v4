package utils;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;

import newsAPI.JsonHandler;
import preProcessing.NewsArticlesWithTickers;

public class ArticleSorter {
	
	public ArticleSorter(){
			
	}
	
	public NewsArticlesWithTickers getArticlesWithTicker(String ticker, NewsArticlesWithTickers inputArticles){
		NewsArticlesWithTickers newsArticlesWithGivenTicker = new NewsArticlesWithTickers();
		
		for(int i=0; i<inputArticles.getNewsArticlesWithTickers().size(); i++){
			for(int j=0; j<inputArticles.getNewsArticlesWithTickers().get(i).getTickerList().size(); j++){
				//System.out.println("TICKER: " + inputArticles.getNewsArticlesWithTickers().get(i).getTickerList().get(j));
				if(inputArticles.getNewsArticlesWithTickers().get(i).getTickerList().get(j).equals(ticker)){
					System.out.println("DSANT");
					newsArticlesWithGivenTicker.getNewsArticlesWithTickers().add(inputArticles.getNewsArticlesWithTickers().get(i));
				}
			}
		}
		return newsArticlesWithGivenTicker;
	}
	
	public void writeToFile(String text) throws IOException{
		Writer out = new BufferedWriter(new OutputStreamWriter(
			new FileOutputStream(this.getPath()+"/PreProcessedArticles/InterestingTickersOnlyOne.txt"), "UTF-8"));
			try {
			    out.write(text);
			} finally {
			    out.close();
			}
	}

	public String getPath() {
	    String path = String.format("%s/%s", System.getProperty("user.dir"), this.getClass().getPackage().getName().replace(".", "/"));
	    return path.split("/")[0]+"/ArticleResources/";
	}
	
	
	public static void writeArticlesWithDesiredTickersToFile(String[] tickerList) throws IOException{
		ArticleSorter a = new ArticleSorter();
		JsonHandler h = new JsonHandler("PreProcessedArticles/DesiredArticles","ticker");
		
		NewsArticlesWithTickers articlesWithDesiredTickers = new NewsArticlesWithTickers();
		
		for(int i=0; i<tickerList.length; i++){
			NewsArticlesWithTickers CurrentArticles = a.getArticlesWithTicker(tickerList[i], h.tickerArticles);
			for(int j=0; j<CurrentArticles.getNewsArticlesWithTickers().size(); j++){
				if(CurrentArticles.getNewsArticlesWithTickers().get(j).getTickerList().size()<=1 && !articlesWithDesiredTickers.getNewsArticlesWithTickers().contains(CurrentArticles.getNewsArticlesWithTickers().get(j))){
					articlesWithDesiredTickers.getNewsArticlesWithTickers().add(CurrentArticles.getNewsArticlesWithTickers().get(j));
				}
			}
		}
		Gson g = new Gson();
	    a.writeToFile(g.toJson(articlesWithDesiredTickers));  

	}
	public static void writeArticlesWithAnyTickerToFileOnlyOneThough() throws IOException{
		ArticleSorter a = new ArticleSorter();
		JsonHandler h = new JsonHandler("PreProcessedArticles/DesiredArticles","ticker");
		
		NewsArticlesWithTickers articlesWithDesiredTickers = new NewsArticlesWithTickers();
		NewsArticlesWithTickers allTickerArticles = h.getTickerArticles();
		
		for(int j=0; j<allTickerArticles.getNewsArticlesWithTickers().size(); j++){
			if(allTickerArticles.getNewsArticlesWithTickers().get(j).getTickerList().size()<=1 && !articlesWithDesiredTickers.getNewsArticlesWithTickers().contains(allTickerArticles.getNewsArticlesWithTickers().get(j))){
				articlesWithDesiredTickers.getNewsArticlesWithTickers().add(allTickerArticles.getNewsArticlesWithTickers().get(j));
			}
		}
		
		Gson g = new Gson();
	    a.writeToFile(g.toJson(articlesWithDesiredTickers));  

	}
	
	public static void writeTickerOverviewToFile() throws IOException{
		ArticleSorter a = new ArticleSorter();
		JsonHandler h = new JsonHandler("PreProcessedArticles/DesiredArticles","ticker");
		
		NewsArticlesWithTickers allArticles = h.getTickerArticles();
		
		HashMap<String, Integer> tickerOverviewMap = new HashMap<String, Integer>();
		
		
		for(int i=0; i<allArticles.getNewsArticlesWithTickers().size(); i++){

			for(int j=0; j<allArticles.getNewsArticlesWithTickers().get(i).getTickerList().size(); j++){
					String currentTicker = allArticles.getNewsArticlesWithTickers().get(i).getTickerList().get(j);
					
					if ( tickerOverviewMap.containsKey(currentTicker)) {
						int count = tickerOverviewMap.get(currentTicker);
						tickerOverviewMap.put(currentTicker, ++count);
					} else {
						tickerOverviewMap.put(currentTicker, 1);
					}
					
			}

		}
		
		List<Map.Entry<String, Integer>> entries = new ArrayList<Map.Entry<String, Integer>>(tickerOverviewMap.entrySet());
		Collections.sort(entries, new Comparator<Map.Entry<String, Integer>>() {
		  public int compare(
		      Map.Entry<String, Integer> entry1, Map.Entry<String, Integer> entry2) {
		    return entry1.getValue().compareTo(entry2.getValue());
		  }
		});
		
		String stringArray = "{";
		for (Entry<String, Integer> e : entries) {
			if(e.getValue()>=4){
				stringArray+='"'+e.getKey()+'"'+",";
				
			}
			
		}
		stringArray+="}";
		System.out.println(entries.toString());
		System.out.println(stringArray);
		
		
	}
	
	
	public static void main(String args[]) throws IOException{
//		ArticleSorter a = new ArticleSorter();
//		JsonHandler h = new JsonHandler("PreProcessedArticles/DesiredArticles","ticker");
//		NewsArticlesWithTickers norwegianTickerArticles = a.getArticlesWithTicker("STL", h.tickerArticles);
//		Gson g = new Gson();
//	    a.writeToFile(g.toJson(norwegianTickerArticles)); 
		String[] array = new String[] {"ROM","NOM","MSFT","INFRA","NONG","ALNG","NADL","OLT","NIO","PDR","SAGA","DD","SCI","CECON","COP","TELIO","REM","HRG","NOF","C","ODF","EKO","AFG","RGT","IGE","SRBANK","DOF","GOOG","MMM","PHO","BIRD","REPANT","SOFF","BON","HAVI","PEN","SNI","FB","NORTH","KOG","AKS","ECHEM","DOCK","ITE","BIONOR","ELT","BEL","BWG","SPU","ATEA","BERGEN","NOD","IOX","AUSS","BRG","AOD","BRIDGE","SDSD","IDEX","SIOFF","SEVDR","EVRY","PRS","CELL","ORK","DESSC","VIZ","GJF","JIN","VEI","TSU","NPRO","MORPOL","KOA","GRR","KVAER","NAUR","WWASA","QEC","TOM","NSG","ITX","PRON","HLNG","GOGL","STB","ALGETA","ARCHER","COD","NOR","SBX","PROS","AKER","OPERA","BAKKA","PLCS","SCH","NEC","FRO","NRS","FOE","BWO","SAS","LSG","AAPL","FUNCOM","NHY","GSF","CLAVIS","TGS","SEVAN","DOLP","CEQ","SALM","DNB","SASNOK","SUBC","EMGS","AKSO","REC","YAR","RCL","NAS","DETNOR","SAS-NOK","PGS","DNO","SDRL","TEL","SONG","MHG","STL"};

		writeArticlesWithDesiredTickersToFile(array);
//		writeArticlesWithAnyTickerToFileOnlyOneThough();
//		writeTickerOverviewToFile();
	}
	
	
	

}
