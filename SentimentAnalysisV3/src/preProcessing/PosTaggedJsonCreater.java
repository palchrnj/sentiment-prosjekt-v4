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




import utils.ArticleCleaner;
import newsAPI.JsonHandler;
import newsAPI.NewsArticleRaw;

import com.google.gson.Gson;



public class PosTaggedJsonCreater {
	

	public NewsArticleWithPosTaggedWords getNiceArticle(NewsArticleWithTickers article) throws IOException{
		NewsArticleWithPosTaggedWords napt = new NewsArticleWithPosTaggedWords();
		napt.setcat(article.getcat());
		napt.setId(article.getId());
		napt.setImageUrl(article.getImageUrl());
		napt.setlast_modified(article.getlast_modified());
		napt.setlead_text(article.getlead_text());
		napt.setLinks(article.getLinks());
		napt.setpublished(article.getpublished());
		napt.setPublisher(article.getPublisher());
		napt.setSentimentValue(article.getSentimentValue());
		napt.setSignature(article.getSignature());
		napt.setTags(article.getTags());
		napt.setText(article.getText());
		napt.setTitle(article.getTitle());
		napt.setversion(article.getversion());
		napt.setAuthorName(article.getAuthorName());
		napt.setKeywordList(article.getKeywordList());
		napt.setTickerList(article.getTickerList());
		
		napt.setPosTaggedTitle(getPostTaggedWords(getPosTaggedList(article.title)));
		napt.setPosTaggedLeadText(getPostTaggedWords(getPosTaggedList(article.lead_text)));
		napt.setPosTaggedMainText(getPostTaggedWords(getPosTaggedList(article.text)));
		
		return napt;
	}
	
	
	
	public NewsArticleRaw cleanArticle(NewsArticleRaw article) throws IOException{
		ArticleCleaner cleaner = new ArticleCleaner();
		cleaner.cleanJsonObject(article);
		return article;
	}
	
	
	public NewsArticleRaw[] getAllArticles() throws IOException{
		JsonHandler handler = new JsonHandler();
		return handler.getArticles().getArticles();
	}
	
	public String posTaggedArticleToJson(NewsArticleWithPosTaggedWords article){
		Gson gson = new Gson();
		return gson.toJson(article, NewsArticleWithPosTaggedWords.class);
	}
	
	
	public PosTaggedWords getPostTaggedWords(String jsonSource){
		String tempJsonSource = ((char) 123)+"\"posTaggedWords\":" + jsonSource; 
		String newJsonSource = tempJsonSource + ((char) 125); 
		Gson gson = new Gson();
		PosTaggedWords postw = gson.fromJson(newJsonSource, PosTaggedWords.class);
		
		return postw;
	}

	@SuppressWarnings("deprecation")
	public String getPosTaggedList(String text) throws IOException{
		//System.out.println(text);
		if(text == null){
			text = "UDEFINERT TEKST";
			
		}
		String urlParams = "json=y&text=" + URLEncoder.encode(text, "UTF-8");
		
		//String urlParameters = "json=y&text="+new String (text.getBytes ("UTF-8"), "UTF-8");
		//String encodedParameters = URLEncoder.encode(urlParameters, "UTF-8");
		
			
		URL url;
	    HttpURLConnection connection = null;  
	    try {
	      //Create connection
	      url = new URL("http://classy.nokvero.com:5000/obt/");
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
	        response.append(URLDecoder.decode(line));
	        response.append('\r');
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
	
	public String getAllArticlesAsJson(NewsArticlesWithTickers articles) throws IOException{
	
		NewsArticlesWithPosTaggedWords nawpti = new NewsArticlesWithPosTaggedWords();
		//System.out.println("Length of articles: " + articles.getNewsArticlesWithTickers().size());
		
		for(int i=0; i<articles.getNewsArticlesWithTickers().size(); i++){
			System.out.println("Counter: " + i);
			nawpti.getNawpti().add(this.getNiceArticle(articles.getNewsArticlesWithTickers().get(i)));
		}
		Gson g = new Gson();
		return g.toJson(nawpti, NewsArticlesWithPosTaggedWords.class);
	}


	
	public void writeToFile(String text) throws IOException{
		Writer out = new BufferedWriter(new OutputStreamWriter(
			new FileOutputStream("C:/Users/Lars/Desktop/SentimentProject/SentimentAnalysis/src/xml/test2.txt"), "UTF-8"));
			try {
			    out.write(text);
			} finally {
			    out.close();
			}
	}

	public static void main(String[] args) throws IOException{
		PosTaggedJsonCreater creator = new PosTaggedJsonCreater();
//		creator.writeToFile(creator.getAllArticlesAsJson(creator.getAllArticles()));
	}
	
	
	
	
	
	
	
	

}
