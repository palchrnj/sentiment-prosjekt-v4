package utils;

import java.io.IOException;

import preProcessing.NewsArticleWithStemmedVersion;
import preProcessing.NewsArticlesWithPosTaggedWords;
import preProcessing.NewsArticlesWithStemmedVersion;
import newsAPI.JsonHandler;

public class Stemmer extends norwegianStemmer {
	
	
	
	public Stemmer(){
		
	}
	
	public static String stemSimple(String str) {
		Stemmer stemmer = new Stemmer();
		stemmer.setCurrent(str);
		stemmer.stem();
		return stemmer.getCurrent();	
	}
	
	public static String stemText(String text){
		Stemmer stemmer = new Stemmer();
		//THE FINAL STEMEMD VERSION OF THE TEXT
		String newText = "";
		
		//CHECK IF TEXT IS NULL
		if(text!=null){
			//CREATE ARRAY OF TEXT
			String[] oldTextArray = text.split(" ");
//			for(int i=0; i<oldTextArray.length; i++){
//				System.out.println(oldTextArray[i]);
//				
//			}
			
			for(int i=0; i<oldTextArray.length; i++){
				
				if(oldTextArray[i]!=null && oldTextArray[i]!="" && oldTextArray[i].length()>0){
					//System.out.println(oldTextArray[i]);
					
					if(oldTextArray[i].substring(oldTextArray[i].length() - 1) != null){	
					//	System.out.println("THE CHAR:  " + (oldTextArray[i].substring(oldTextArray[i].length() - 1)));
						
						if(Character.isLetter(oldTextArray[i].substring(oldTextArray[i].length() - 1).charAt(0))){
							stemmer.setCurrent(oldTextArray[i]);
							stemmer.stem();
							newText+= " " + stemmer.getCurrent();	
						}
						
						else{
							if(oldTextArray[i].length()>1){
								stemmer.setCurrent(oldTextArray[i].substring(0,oldTextArray[i].length()-2));
								stemmer.stem();
								newText+= " " + stemmer.getCurrent() + oldTextArray[i].charAt(oldTextArray[i].length()-1);
							}
							else{
								newText+=oldTextArray[i].charAt(oldTextArray[i].length()-1); 
							}
						}
					}
				}
			}
		}	
		return newText;
	}
	
	public NewsArticlesWithStemmedVersion stemAllArticles(NewsArticlesWithPosTaggedWords nawptw){
		NewsArticlesWithStemmedVersion nawsv = new NewsArticlesWithStemmedVersion();
		
		for(int i=0; i<nawptw.getNawpti().size(); i++){
			NewsArticleWithStemmedVersion newArticle = new NewsArticleWithStemmedVersion();
			
			newArticle.setcat(nawptw.getNawpti().get(i).getcat());
			newArticle.setId(nawptw.getNawpti().get(i).getId());
			newArticle.setImageUrl(nawptw.getNawpti().get(i).getImageUrl());
			newArticle.setlast_modified(nawptw.getNawpti().get(i).getlast_modified());
			newArticle.setlead_text(nawptw.getNawpti().get(i).getlead_text());
			newArticle.setLinks(nawptw.getNawpti().get(i).getLinks());
			newArticle.setpublished(nawptw.getNawpti().get(i).getpublished());
			newArticle.setPublisher(nawptw.getNawpti().get(i).getPublisher());
			newArticle.setSentimentValue(nawptw.getNawpti().get(i).getSentimentValue());
			newArticle.setSignature(nawptw.getNawpti().get(i).getSignature());
			newArticle.setTags(nawptw.getNawpti().get(i).getTags());
			newArticle.setText(nawptw.getNawpti().get(i).getText());
			newArticle.setTitle(nawptw.getNawpti().get(i).getTitle());
			newArticle.setversion(nawptw.getNawpti().get(i).getversion());
			newArticle.setTickerList(nawptw.getNawpti().get(i).getTickerList());
			newArticle.setKeywordList(nawptw.getNawpti().get(i).getKeywordList());
			newArticle.setAuthorName(nawptw.getNawpti().get(i).getAuthorName());
			
			newArticle.setPosTaggedTitle(nawptw.getNawpti().get(i).getPosTaggedTitle());
			newArticle.setPosTaggedLeadText(nawptw.getNawpti().get(i).getPosTaggedLeadText());
			newArticle.setPosTaggedMainText(nawptw.getNawpti().get(i).getPosTaggedMainText());
			
			newArticle.setStemmedTitle(stemText(nawptw.getNawpti().get(i).getTitle()));
			newArticle.setStemmedLeadText(stemText(nawptw.getNawpti().get(i).getlead_text()));
			newArticle.setStemmedText(stemText(nawptw.getNawpti().get(i).getText()));
			
			nawsv.getNawsv().add(newArticle);
		}
		
		
		return nawsv;
	}
	
	public static void main(String[] args) throws IOException{
		JsonHandler handler = new JsonHandler("/ArticleSteps/3_POStaggedArticles/StemmingTestArticles.txt", "pos");
		System.out.println(stemText(handler.getPosTaggedArticles().nawpti.get(0).text));
	}
}
