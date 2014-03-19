package featureExctraction;

import java.io.IOException;
import java.util.ArrayList;

import preProcessing.NewsArticleWithPosTaggedWords;
import preProcessing.PosTaggedWord;
import newsAPI.JsonHandler;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class SentimentWordExtractor {

	
	public SentimentWordExtractor(){
		
		
	}
	
	/* RETURNS NUMBER OF SENTIMENT BEARING WORDS FROM ARTICLE */
	public int[] getNumberOfSentimentBearingWordsWithValenceShifting(NewsArticleWithPosTaggedWords nawpti, String[] valenceShifters, String wordclass, String value) throws JsonSyntaxException, IOException{
		int numberOfPositiveAdjectives = 0;
		int numberOfNegativeAdjectives = 0;
		
		JsonHandler handler = new JsonHandler("/Misc/AnnotatedSentimentWords.json", "ticker");
		Gson g = new Gson();
		WordCountList wcl = g.fromJson(handler.getJsonSource(), WordCountList.class);
		
		if(wordclass == "adj"){
			 wcl = g.fromJson(handler.getJsonSource(), WordCountList.class);
		}
		if(wordclass == "verbs"){
			 wcl = g.fromJson(handler.getJsonSource(), WordCountList.class);
			}
		if(wordclass == "adv"){
			 wcl = g.fromJson(handler.getJsonSource(), WordCountList.class);
		}
		
		ArrayList<String> valenceShifterList = new ArrayList<String>();
		for(int i=0; i<valenceShifters.length; i++){
			valenceShifterList.add(valenceShifters[i]);
		}
		
		if(nawpti.getAllPosTaggedWords() != null){
			for(int i=0; i<nawpti.getAllPosTaggedWords().size(); i++){
				
				if(nawpti.getAllPosTaggedWords().get(i).getWordclass().equals(wordclass)){
					
					for(int j=0; j<wcl.getWords().size(); j++){
						
						if(wcl.getWords().get(j).getWord().equals(nawpti.getAllPosTaggedWords().get(i).getInput())){
							
							if(wcl.getWords().get(j).getSentimentValue().equals(value)){
								if(!identifyValenceShifter(wcl.getWords().get(j).getWord(), valenceShifterList, nawpti.getAllPosTaggedWords())){
									numberOfPositiveAdjectives++;
								}
								else{
									numberOfNegativeAdjectives++;
								}
							}
							
						}
						
					}
					
				}
				
			}
		}

		int[] results = new int[2];
		results[0] = numberOfPositiveAdjectives;
		results[1] = numberOfNegativeAdjectives;
		return results;
	}	
	
	/* RETURNS NUMBER OF SENTIMENT BEARING WORDS FROM ARTICLE */
	public int getNumberOfSentimentBearingWords(NewsArticleWithPosTaggedWords nawpti, String wordclass, String value) throws JsonSyntaxException, IOException{
		int numberOfSentimentBearingWords = 0;
		JsonHandler handler = new JsonHandler("/Misc/AnnotatedSentimentWords.json", "ticker");
		Gson g = new Gson();
		WordCountList wcl = g.fromJson(handler.getJsonSource(), WordCountList.class);
		
		if(wordclass == "adj"){
			 wcl = g.fromJson(handler.getJsonSource(), WordCountList.class);
		}
		if(wordclass == "verbs"){
			 wcl = g.fromJson(handler.getJsonSource(), WordCountList.class);
			}
		if(wordclass == "adv"){
			 wcl = g.fromJson(handler.getJsonSource(), WordCountList.class);
		}
		
		if(nawpti.getAllPosTaggedWords() != null){
				for(int i=0; i<nawpti.getAllPosTaggedWords().size(); i++){
					
					if(nawpti.getAllPosTaggedWords().get(i).getWordclass().equals(wordclass)){
						
						for(int j=0; j<wcl.getWords().size(); j++){
							
							if(wcl.getWords().get(j).getWord().equals(nawpti.getAllPosTaggedWords().get(i).getInput())){
								
								if(wcl.getWords().get(j).getSentimentValue().equals(value)){
									numberOfSentimentBearingWords++;
								}
								
							}
							
						}
						
					}
					
				}
		}
		return numberOfSentimentBearingWords;
	}
	
	
		//NOT YET USED OR IMPLEMENTED PROPERLY
		public boolean identifyValenceShifter(String sentimentWord, ArrayList<String> valenceShifterList, ArrayList<PosTaggedWord> ptw){
			boolean valenceShifter = false;
			int sentimentWordIndex = 0;
			
			for(int i = 0; i<ptw.size(); i++){
				if(ptw.get(i).input.equals(sentimentWord)){
					sentimentWordIndex = i;
				}
			}
			
			for(int i = sentimentWordIndex; i<ptw.size() - i; i++){
				for(int j=0; j<valenceShifterList.size(); j++){
					if(valenceShifterList.get(j).split(" ").length > 1){
						if(valenceShifterList.get(j).split(" ")[0].equals((ptw.get(i).input))){
							boolean isStillTrue = true;
							for(int k=0; k<valenceShifterList.get(j).split(" ").length; k++){
								if(ptw.get(i+valenceShifterList.get(j).split(" ").length) != null){
									if(!valenceShifterList.get(j).split(" ")[k].equals(ptw.get(i+k))){
										isStillTrue = false;
									}
								}
							}
							if(isStillTrue){
								valenceShifter = true;
								break;
							}
						}
					
					}
					else{
						if(valenceShifterList.get(j).equals((ptw.get(i).input))){
							valenceShifter = true;
							break;
						}
						if(ptw.get(i).input == "."){
							break;
						}
					}
				
				}
			
			}
			for(int i = sentimentWordIndex; i!=0; i--){
				if(valenceShifterList.contains(ptw.get(i).input)){
					valenceShifter = true;
					break;
				}
				if(ptw.get(i).input == "."){
					break;
				}
			}
			return valenceShifter;
		}
}
