package newsAPI;

import com.google.gson.*;

import featureExctraction.NewsArticlesWithFeatures;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import preProcessing.NewsArticleWithStemmedVersion;
import preProcessing.NewsArticlesWithPosTaggedWords;
import preProcessing.NewsArticlesWithStemmedVersion;
import preProcessing.NewsArticlesWithTickers;


public class JsonHandler {
	
	
	public NewsArticlesRaw articles;
	public NewsArticlesWithPosTaggedWords posTaggedArticles;
	public NewsArticlesWithTickers tickerArticles;
	public NewsArticlesWithStemmedVersion stemmedArticles;
	public NewsArticlesWithFeatures featureArticles;



	public String jsonSource;
	


	public JsonHandler() throws IOException{
		
	}
	
	public JsonHandler(String articleSource, String type) throws IOException{
		this.jsonSource = getJsonSource(this.getPath()+articleSource);
		if(type == "raw"){
			this.articles = newsArticles(this.jsonSource);
		}
		else if(type == "pos"){
			this.posTaggedArticles = posTaggedNewsArticles(this.jsonSource);
		}
		else if(type == "ticker"){
			this.tickerArticles = newsArticlesWithTickers(this.jsonSource);
		}
		else if(type == "stemmed"){
			this.stemmedArticles = newsArticlesWithStemmedVersion(this.jsonSource);
		}
		else if(type == "features"){
			this.featureArticles = newsArticlesWithFeatures(this.jsonSource);
		}
	}
	



	
	
	/*FIELD NAMING STRATEGY FOR GSON*/
	public class MyFieldNamingStrategy implements FieldNamingStrategy {
	    public String translateName(Field arg0) {
	        String result = "";
	        String name = arg0.getName();
	        for(char ch : name.toCharArray()){
	        	if(ch == '-'){
	        		ch = '_';
	        	}
	        	else if(ch == '_'){
	        		ch = '-';
	        	}
	            result += ch;
	        }
	        System.out.println(result);
	        return result;
	    }
	}
	
	/* READS FILE */
	static String readFile(String path, Charset encoding) throws IOException 
	{
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return encoding.decode(ByteBuffer.wrap(encoded)).toString();
	}
	
	/* GET THE JSON SOURCE OF FILE */
	public String getJsonSource(String source) throws IOException{
		String jsonFile = readFile(source, StandardCharsets.UTF_8);
		return jsonFile;
	}

	/* CREATES NewsArticlesRaw object from JSON source */
	public NewsArticlesRaw newsArticles(String jsonSource){
		//MyFieldNamingStrategy strategy = new MyFieldNamingStrategy();
		Gson gson = new Gson();
		NewsArticlesRaw articles = gson.fromJson(jsonSource, NewsArticlesRaw.class);
	//	System.out.println("Handler raw articles elad text: " + articles.getArticles()[0].getTitle());
	//	System.out.println("Handler raw articles elad text: " + articles.getArticles()[0].getlead_text());
		return articles;
	}
	
	public NewsArticlesWithTickers newsArticlesWithTickers (String jsonSource){
		Gson gson = new Gson();
		NewsArticlesWithTickers articles = gson.fromJson(jsonSource, NewsArticlesWithTickers.class);
		return articles;
	}
	
	public NewsArticlesWithPosTaggedWords posTaggedNewsArticles(String jsonSource){
		Gson gson = new Gson();
		NewsArticlesWithPosTaggedWords articles = gson.fromJson(jsonSource, NewsArticlesWithPosTaggedWords.class);
		return articles;
	}

	public NewsArticlesWithStemmedVersion newsArticlesWithStemmedVersion (String jsonSource){
		Gson gson = new Gson();
		NewsArticlesWithStemmedVersion articles = gson.fromJson(jsonSource, NewsArticlesWithStemmedVersion.class);
		return articles;
	}
	
	public NewsArticlesWithFeatures newsArticlesWithFeatures (String jsonSource){
		Gson gson = new Gson();
		NewsArticlesWithFeatures articles = gson.fromJson(jsonSource, NewsArticlesWithFeatures.class);
		return articles;
	}
	
	/* METHOD FOR DETERMINING RELATIVE PATHS */
	public String getPath() {
	    String path = String.format("%s/%s", System.getProperty("user.dir"), this.getClass().getPackage().getName().replace(".", "/"));
	    return path.split(this.getClass().getPackage().getName())[0]+"/ArticleResources/";
	}
	
	/* GETTERS AND SETTERS */
	public NewsArticlesRaw getArticles() {
		return articles;
	}
	public void setArticles(NewsArticlesRaw articles) {
		this.articles = articles;
	}
	public String getJsonSource() {
		return jsonSource;
	}

	public void setJsonSource(String jsonSource) {
		this.jsonSource = jsonSource;
	}
	public NewsArticlesWithPosTaggedWords getPosTaggedArticles() {
		return posTaggedArticles;
	}

	public void setPosTaggedArticles(
			NewsArticlesWithPosTaggedWords posTaggedArticles) {
		this.posTaggedArticles = posTaggedArticles;
	}
	public NewsArticlesWithTickers getTickerArticles() {
		return tickerArticles;
	}

	public void setTickerArticles(NewsArticlesWithTickers tickerArticles) {
		this.tickerArticles = tickerArticles;
	}
	/* TEST METHOD */
	public static void main(String[] args) throws IOException{
		JsonHandler h = new JsonHandler("ArticleSteps/4_StemmedArticles/ArticleGeneratorTestStemmed.json", "stemmed");	
	}
}
