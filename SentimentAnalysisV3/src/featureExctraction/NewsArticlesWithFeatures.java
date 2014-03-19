package featureExctraction;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import preProcessing.NewsArticlesWithPosTaggedWords;
import preProcessing.NewsArticlesWithStemmedVersion;
import newsAPI.JsonHandler;
import newsAPI.NewsArticlesRaw;
import annotationStatistics.AnnotationValidator;

import com.google.gson.Gson;

public class NewsArticlesWithFeatures {
	
	public ArrayList<NewsArticleWithFeatures> articles;
	
	
	public NewsArticlesWithFeatures(){
		this.articles = new ArrayList<NewsArticleWithFeatures>();
	}
	
	
	/* SETS ARTICLES TO OBJECT */
	public void initiateNewsArticlesWithFeatures(String pathToStemmedSource) throws IOException{
		Gson g = new Gson();
		JsonHandler handler = new JsonHandler(pathToStemmedSource, "stemmed");
		NewsArticlesWithStemmedVersion inputArticles = g.fromJson(handler.getJsonSource(), NewsArticlesWithStemmedVersion.class);
	
		for(int i=0; i<inputArticles.getNawsv().size(); i++){
			System.out.println("COUNTER: " + i);
			NewsArticleWithFeatures n = new NewsArticleWithFeatures();
			NewsArticleWithFeatures b = n.createFeatureArticle(inputArticles.getNawsv().get(i));
			this.getArticles().add(b);
			//System.out.println(b.getVector().getFeatureVector());
		}
	}
	
	public void initiatePaalCheckArticles(String pathToStemmedSource) throws IOException{
		AnnotationValidator av = new AnnotationValidator();
		Gson g = new Gson();
		JsonHandler handler = new JsonHandler(pathToStemmedSource, "stemmed");
		NewsArticlesWithStemmedVersion inputArticles = g.fromJson(handler.getJsonSource(), NewsArticlesWithStemmedVersion.class);
		
		NewsArticlesRaw sentimentCheckArticles = handler.getArticles();
	
    	// ADD POSITIVE ARTICLES
		for(int i=0; i<inputArticles.getNawsv().size(); i++){
			System.out.println("COUNTER: " + i);
			if((inputArticles.getNawsv().get(i).getSentimentValue()!=null) && (sentimentCheckArticles.getArticles()[i].getSentimentValue() != null)){
				System.out.println("Sentiment is not null");
        		if(av.parseSentimentValue(inputArticles.getNawsv().get(i).getSentimentValue()).equals(av.parseSentimentValue(sentimentCheckArticles.getArticles()[i].getSentimentValue()))){
        			System.out.println("Sentiment is alike");
        			if(av.parseSentimentValue(inputArticles.getNawsv().get(i).getSentimentValue()).get(3).equals(1)){
        				System.out.println("Sentiment is positive");
        				NewsArticleWithFeatures n = new NewsArticleWithFeatures();
    					NewsArticleWithFeatures b = n.createFeatureArticle(inputArticles.getNawsv().get(i));
    					this.getArticles().add(b);
        			}
				
        		}
			}	
		}
		// ADD NEGATIVE ARTICLES
		for(int i=0; i<inputArticles.getNawsv().size(); i++){
			System.out.println("COUNTER: " + i);
			if((inputArticles.getNawsv().get(i).getSentimentValue()!=null) && (sentimentCheckArticles.getArticles()[i].getSentimentValue() != null)){
				System.out.println("Sentiment is not null");
        		if(av.parseSentimentValue(inputArticles.getNawsv().get(i).getSentimentValue()).equals(av.parseSentimentValue(sentimentCheckArticles.getArticles()[i].getSentimentValue()))){
        			System.out.println("Sentiment is alike");
        			if(av.parseSentimentValue(inputArticles.getNawsv().get(i).getSentimentValue()).get(3).equals(-1)){
        				System.out.println("Sentiment is positive");
        				NewsArticleWithFeatures n = new NewsArticleWithFeatures();
    					NewsArticleWithFeatures b = n.createFeatureArticle(inputArticles.getNawsv().get(i));
    					this.getArticles().add(b);
        			}
				
        		}
			}	
		}
		
		
	}


	public void writeToFileAsJson() throws IOException{
		Gson gson = new Gson();
		String json = gson.toJson(this);
		Writer out = new BufferedWriter(new OutputStreamWriter(
		    new FileOutputStream("C:/Users/Lars/Desktop/SentimentProject/SentimentAnalysis/src/xml/NewsArticlesWithFeaturesTitlePositiveNegativeCount.txt"), "UTF-8"));
		try {
		    out.write(json);
		} finally {
		    out.close();
		}
	}
	
	
	
	
	
	public ArrayList<NewsArticleWithFeatures> getArticles() {
		return articles;
	}
	public void setArticles(ArrayList<NewsArticleWithFeatures> articles) {
		this.articles = articles;
	}

	
	public static void main(String[] args) throws IOException{
	
	}

}
