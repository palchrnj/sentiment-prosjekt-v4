package preProcessing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import newsAPI.JsonHandler;
import newsAPI.NewsArticlesRaw;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.google.gson.Gson;




public class HegnarTickerScraper {
	
	public ArrayList<String> TickerList; 
	public String AuthorName;
	public ArrayList<String> keywordsList;
	
	public HegnarTickerScraper() {
		
	}
	
	public HegnarTickerScraper(String url) throws IOException{
		//Adds string of tickers to object
		this.TickerList = new ArrayList<String>();
		this.keywordsList = new ArrayList<String>();
		
		Document doc;
		
		doc = Jsoup.connect(url).get();
	
		doc.outputSettings().charset("utf-8");
		
		Elements newsHeadlines = doc.select(".ticker");
		
		if(newsHeadlines.size() > 0){
			String htmlText = newsHeadlines.get(0).text();
			String[] htmlTickersTextArray = htmlText.split("Se aksjeticker:");
			String[] tickers = htmlTickersTextArray[1].split(" ");
			for(int i=1; i<tickers.length; i++){
				this.TickerList.add(tickers[i]);
			}
		}

		//Adds author name to object
		Elements articleAuthor = doc.select(".ArtikkelForfatter");
		
		if(articleAuthor.size() > 0){
			String articleAuthorText = articleAuthor.get(0).text();
			this.AuthorName = articleAuthorText;
		}
		//Adds Keywords to object
		if(doc.select("meta[name=keywords]").size() > 0){
			String keywordsRaw = doc.select("meta[name=keywords]").get(0).attr("content");
			String[] keywordArray = keywordsRaw.split(",");
			for (int i = 0; i < keywordArray.length; i++) {
				if(keywordArray[i] != " "){
					this.keywordsList.add(keywordArray[i]);
				}	
			}
		}	
	}
	//GET ARTICLES FROM API
	public String getArticlesFromAPI(String publisher, String rows) throws IOException{
		String urlParams = "q=publisher%3A" + URLEncoder.encode(publisher, "UTF-8") + "&wt=json&rows=" + URLEncoder.encode(rows, "UTF-8");
		System.out.println(urlParams);
		URL url;
	    HttpURLConnection connection = null;  
	    try {
	      //Create connection
	      url = new URL("http://vm-6120.idi.ntnu.no:8080/solr/news/select?");
	      connection = (HttpURLConnection)url.openConnection();
	      connection.setRequestMethod("POST");
	
	      connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
	      connection.setRequestProperty("Accept-Encoding", "gzip,deflate,sdch");
	      connection.setRequestProperty("Accept-Language", "en-US,en;q=0.8,nb;q=0.6");  
	      connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");

				
	      connection.setUseCaches (false);
	      connection.setDoInput(true);
	      connection.setDoOutput(true);

	      //Send request
	      DataOutputStream wr = new DataOutputStream (
	      connection.getOutputStream ());
	      wr.writeBytes (urlParams);
	      wr.flush ();
	      wr.close ();

	      //Get Response	
	      InputStream is = connection.getInputStream();
	      BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
	      
	      String line;
	      
	      StringBuffer response = new StringBuffer(); 

	     
	      while((line = rd.readLine()) != null) {
	        response.append(line);
	        //response.append('\r');
	      }
	      rd.close();
	      
	
	      return response.toString();
	 
	    } catch (Exception e) {

	      e.printStackTrace();
	      return null;

	    } finally {

	      if(connection != null) {
	        connection.disconnect(); 
	        
	      }
	      
	    }
	}
	// WRITE ARTICLES TO FILE
	public void writeToFile(String text) throws IOException{
		Writer out = new BufferedWriter(new OutputStreamWriter(
			new FileOutputStream(this.getPath()+"/PreProcessedArticles/AllArticles.txt"), "UTF-8"));
			try {
			    out.write(text);
			} finally {
			    out.close();
			}
	}
	public void writeDesiredArticlesToFile(String text) throws IOException{
		Writer out = new BufferedWriter(new OutputStreamWriter(
			new FileOutputStream(this.getPath()+"/PreProcessedArticles/AllArticles.txt"), "UTF-8"));
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
	public  NewsArticlesWithTickers getArticlesWithTicker(JsonHandler h) throws IOException{
		//System.out.println(h.getArticles().getArticles()[0].getText());
		NewsArticlesWithTickers tickerArticles = new NewsArticlesWithTickers();
		NewsArticlesRaw na = h.getArticles();
	
		
		for(int i=0; i<na.getArticles().length; i++){
			NewsArticleWithTickers nawt = new NewsArticleWithTickers();
			nawt.setcat(na.getArticles()[i].getcat());
			nawt.setId(na.getArticles()[i].getId());
			nawt.setImageUrl(na.getArticles()[i].getImageUrl());
			nawt.setlast_modified(na.getArticles()[i].getlast_modified());
			
			System.out.println("LEAD TEXT" + na.getArticles()[i].getlead_text());
			
			
			nawt.setlead_text(na.getArticles()[i].getlead_text());
			nawt.setLinks(na.getArticles()[i].getLinks());
			nawt.setpublished(na.getArticles()[i].getpublished());
			nawt.setPublisher(na.getArticles()[i].getPublisher());
			nawt.setSentimentValue(na.getArticles()[i].getSentimentValue());
			nawt.setSignature(na.getArticles()[i].getSignature());
			nawt.setTags(na.getArticles()[i].getTags());
			nawt.setText(na.getArticles()[i].getText());
			nawt.setTitle(na.getArticles()[i].getTitle());
			nawt.setversion(na.getArticles()[i].getversion());
		
			try {
				HegnarTickerScraper hts = new HegnarTickerScraper(na.getArticles()[i].links[0]);
				String authorFromArticle = hts.AuthorName;
				ArrayList<String> tickerListFromArticle = hts.TickerList;
				ArrayList<String> keywordListFromArticle = hts.keywordsList;
				
				nawt.setAuthorName(authorFromArticle);
				nawt.setTickerList(tickerListFromArticle);
				nawt.setKeywordList(keywordListFromArticle);
				
				if(nawt.getTickerList().size()>0){
					tickerArticles.getNewsArticlesWithTickers().add(nawt);
				}
			} catch (Exception e) {
				System.out.println("Skipping article " + i);
				continue;
			}
			//System.out.println(hts.AuthorName);
			//System.out.println(hts.keywordsList);
			//System.out.println(hts.TickerList);	
		}
		//System.out.println(tickerArticles.getNewsArticlesWithTickers().get(0).authorName);
		return tickerArticles;
	}
	
	public ArrayList<String> getTickerList() {
		return TickerList;
	}
	public void setTickerList(ArrayList<String> tickerList) {
		TickerList = tickerList;
	}
	
	public static void main(String[] args) throws IOException{
		/*Gson gson = new Gson();
		HegnarTickerScraper hts = new HegnarTickerScraper("http://www.hegnar.no/bors/article754445.ece");
		String splitString = "docs";
		String tempJsonString = hts.getArticlesFromAPI("Hegnar","5000").split(splitString)[1];
		String finalJsonString = "{\"articles" + tempJsonString.substring(0, tempJsonString.length()-1);
		hts.writeToFile(finalJsonString);*/
	
		//hts.writeDesiredArticlesToFile(gson.toJson(getArticlesWithTicker()));
		
		
		HegnarTickerScraper hts = new HegnarTickerScraper();
		hts.writeDesiredArticlesToFile(hts.getArticlesFromAPI("Hegnar", "15000"));
	}
}
