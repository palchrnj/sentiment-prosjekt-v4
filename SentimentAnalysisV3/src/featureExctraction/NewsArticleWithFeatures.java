package featureExctraction;

import java.beans.Encoder;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import machineLearning.VectorAttributes;

//import org.hsqldb.lib.InOutUtil;

import preProcessing.ArticleCategoryGenerator;
import preProcessing.NewsArticleWithPosTaggedWords;
import preProcessing.NewsArticleWithStemmedVersion;
import preProcessing.TextFileHandler;
import newsAPI.JsonHandler.MyFieldNamingStrategy;
import annotationStatistics.AnnotationValidator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

public class NewsArticleWithFeatures extends NewsArticleWithStemmedVersion {

	
	public String[] analyticClues; 
	public String[] recommenderClues;
	
	public  String[] positiveTitleWordsClues;
	public  String[] negativeTitleWordsClues;
	
	public String[] valenceShifters;
	
	
	public int numberOfAdjectives;
	public int numberOfSubstantives;
	public int numberOfverbs;
	
	public int lengthOfTitle;
	public int lengthOfLeadText;
	public int lengthOfMainText;
	
	
	public int numberOfExclamationMarks;
	public int numberOfQuestionMarks;
	public int numberOfQuotes;
	
	public double averageLengthOfWords;

	public boolean analyticsMentioned;
	public boolean recommenderCluesMentioned;
	
	public boolean hasPositiveTitle;
	public boolean hasNegativeTitle;
	
	public int negativeTitleCount;
	public int positiveTitleCount;
	
	public int numberOfPositiveAdjectives;
	public int numberOfNegativeAdjectives;
	public int numberOfNeutralAdjectives;
	
	public int numberOfPositiveVerbs;
	public int numberOfNegativeVerbs;
	
	public int numberOfPositiveAdverbs;
	public int numberOfNegativeAdverbs;
	
	public boolean isBors;
	public boolean isAnalyse;
	public boolean isOkonomi;
	

	public NewsArticleWithFeatures() throws IOException{
		TextFileHandler tfh = new TextFileHandler();
		this.analyticClues = tfh.getAnalyticalClues();
		this.recommenderClues = tfh.getReccomendationClues();
		this.positiveTitleWordsClues = tfh.getPositiveTitleClues();
		this.negativeTitleWordsClues = tfh.getNegativeTitleClues();
		this.valenceShifters = tfh.getValenceShifter();
		
	}
	
	
	//GET NUMBER OF EXCLAMATION MARKS
	public int getNumberOfExclamationMarks(NewsArticleWithPosTaggedWords nawpti){
		int exclamationMarks = 0;
		
		if(nawpti.getPosTaggedTitle().getPosTaggedWords()!=null){
			for(int i = 0; i<nawpti.getPosTaggedTitle().getPosTaggedWords().length; i++){
				if(nawpti.getPosTaggedTitle().getPosTaggedWords()[i].input.equals("!")){
					exclamationMarks++;
				}
			}
		}
		if(nawpti.getPosTaggedLeadText().getPosTaggedWords()!=null){
			for(int i = 0; i<nawpti.getPosTaggedLeadText().getPosTaggedWords().length; i++){
				if(nawpti.getPosTaggedLeadText().getPosTaggedWords()[i].input.equals("!")){
					exclamationMarks++;
				}
			}
		}
		if(nawpti.getPosTaggedMainText().getPosTaggedWords()!=null){
			for(int i = 0; i<nawpti.getPosTaggedMainText().getPosTaggedWords().length; i++){
				if(nawpti.getPosTaggedMainText().getPosTaggedWords()[i].input.equals("!")){
					exclamationMarks++;
				}
			}
			
		}
		return exclamationMarks;
	}
	//GET NUMBER OF QUESTION MARKS 
	public int getNumberOfQuestionMarks(NewsArticleWithPosTaggedWords nawpti){
			int questionMarks = 0;
			
			if(nawpti.getPosTaggedTitle().getPosTaggedWords()!=null){
				for(int i = 0; i<nawpti.getPosTaggedTitle().getPosTaggedWords().length; i++){
					if(nawpti.getPosTaggedTitle().getPosTaggedWords()[i].input.equals("?")){
						questionMarks++;
					}
				}
				
			}
			if(nawpti.getPosTaggedLeadText().getPosTaggedWords()!=null){
				for(int i = 0; i<nawpti.getPosTaggedLeadText().getPosTaggedWords().length; i++){
					if(nawpti.getPosTaggedLeadText().getPosTaggedWords()[i].input.equals("?")){
						questionMarks++;
					}
				}
						
					}
			if(nawpti.getPosTaggedMainText().getPosTaggedWords()!=null){
				for(int i = 0; i<nawpti.getPosTaggedMainText().getPosTaggedWords().length; i++){
					if(nawpti.getPosTaggedMainText().getPosTaggedWords()[i].input.equals("?")){
						questionMarks++;
					}
				}
				
			}
			return questionMarks;
		}
	//RETURNS NUMBER OF QUOTES IN TEXT
		public int getNumberOfQuotes(NewsArticleWithPosTaggedWords nawpti){
			int quotes = 0;
			
			if(nawpti.getPosTaggedTitle().getPosTaggedWords()!=null){
				for(int i = 0; i<nawpti.getPosTaggedTitle().getPosTaggedWords().length; i++){
					if(nawpti.getPosTaggedTitle().getPosTaggedWords()[i].input.equals(" -")){
						quotes++;
					}
				}
				
			}
			if(nawpti.getPosTaggedLeadText().getPosTaggedWords()!=null){
				for(int i = 0; i<nawpti.getPosTaggedLeadText().getPosTaggedWords().length; i++){
					if(nawpti.getPosTaggedLeadText().getPosTaggedWords()[i].input.equals(" -")){
						quotes++;
					}
				}
						
					}
			if(nawpti.getPosTaggedMainText().getPosTaggedWords()!=null){
				for(int i = 0; i<nawpti.getPosTaggedMainText().getPosTaggedWords().length; i++){
					if(nawpti.getPosTaggedMainText().getPosTaggedWords()[i].input.equals(" -")){
						quotes++;
					}
				}
				
			}
			
			return quotes;
		}
	
	//RETURNS WHETHER OR NOT A ANALYTIC COMPANY IS REFERED IN TEXT
	public boolean getAnalyticsMentioned(NewsArticleWithPosTaggedWords nawpti, ArrayList<String> analyticWords){
		boolean analyticsMentioned = false;
		
		if(nawpti.getPosTaggedTitle().getPosTaggedWords()!=null){
			for(int i = 0; i<nawpti.getPosTaggedTitle().getPosTaggedWords().length; i++){
				for(int j=0; j<analyticWords.size(); j++){
					if(analyticWords.get(j).equalsIgnoreCase(nawpti.getPosTaggedTitle().getPosTaggedWords()[i].input)){
						analyticsMentioned = true;
					}
				}
			}
		}
		if(nawpti.getPosTaggedLeadText().getPosTaggedWords()!=null){
			for(int i = 0; i<nawpti.getPosTaggedLeadText().getPosTaggedWords().length; i++){
				for(int j=0; j<analyticWords.size(); j++){
					if(analyticWords.get(j).equalsIgnoreCase(nawpti.getPosTaggedLeadText().getPosTaggedWords()[i].input)){
						analyticsMentioned = true;
					}
				}	
			}	
		}
		if(nawpti.getPosTaggedMainText().getPosTaggedWords()!=null){
			for(int i = 0; i<nawpti.getPosTaggedMainText().getPosTaggedWords().length; i++){
				for(int j=0; j<analyticWords.size(); j++){
					if(analyticWords.get(j).equalsIgnoreCase(nawpti.getPosTaggedMainText().getPosTaggedWords()[i].input)){
						analyticsMentioned = true;
					}
					
				}
			}
		}
		return analyticsMentioned;
	}

	//RETURNS NUMBER OF ADJECTIVES IN TEXT
	public int getNumberOfAdjectives(NewsArticleWithPosTaggedWords nawpti){
		int numberOfAdj = 0;
		//System.out.println(ptw.getPosTaggedWords().length);
		if(nawpti.getPosTaggedTitle().getPosTaggedWords() != null){
			for(int i = 0; i<nawpti.getPosTaggedTitle().getPosTaggedWords().length; i++){
				if(nawpti.getPosTaggedTitle().getPosTaggedWords()[i].wordclass.equals("adj")){
					numberOfAdj++;
				}
			}
			
		}
		if(nawpti.getPosTaggedLeadText().getPosTaggedWords() != null){
			for(int i = 0; i<nawpti.getPosTaggedLeadText().getPosTaggedWords().length; i++){
				
				if(nawpti.getPosTaggedLeadText().getPosTaggedWords()[i].wordclass.equals("adj")){
					numberOfAdj++;
				}
			}
		}
	
		if(nawpti.getPosTaggedMainText().getPosTaggedWords()!= null){
			for(int i = 0; i<nawpti.getPosTaggedMainText().getPosTaggedWords().length; i++){
				if(nawpti.getPosTaggedMainText().getPosTaggedWords()[i].wordclass.equals("adj")){
					numberOfAdj++;
				}
			}
			
		}
	
		return numberOfAdj;
	}
	
	//RETURNS NUMBER OF SUBSTANTIVES IN TEXT
	public int getNumberOfSubstantives(NewsArticleWithPosTaggedWords nawpti){
		int numberOfSubst = 0;
		
		if(nawpti.getPosTaggedTitle().getPosTaggedWords() != null){
			for(int i = 0; i<nawpti.getPosTaggedTitle().getPosTaggedWords().length; i++){
				if(nawpti.getPosTaggedTitle().getPosTaggedWords()[i].wordclass.equals("subst")){
					numberOfSubst++;
				}
			}
			
		}
		if(nawpti.getPosTaggedLeadText().getPosTaggedWords() != null){
			for(int i = 0; i<nawpti.getPosTaggedLeadText().getPosTaggedWords().length; i++){
				if(nawpti.getPosTaggedLeadText().getPosTaggedWords()[i].wordclass.equals("subst")){
					numberOfSubst++;
				}
			}
			
		}
		if(nawpti.getPosTaggedMainText().getPosTaggedWords() != null){
			for(int i = 0; i<nawpti.getPosTaggedMainText().getPosTaggedWords().length; i++){
				if(nawpti.getPosTaggedMainText().getPosTaggedWords()[i].wordclass.equals("subst")){
					numberOfSubst++;
				}
			}
			
		}
		return numberOfSubst;
	}
	
	//RETURNS NUMBER OF VERBS
	public int getNumberOfVerbs(NewsArticleWithPosTaggedWords nawpti){
		int numberOfVerbs = 0;
		if(nawpti.getPosTaggedTitle().getPosTaggedWords()!=null){
			for(int i = 0; i<nawpti.getPosTaggedTitle().getPosTaggedWords().length; i++){
				
				if(nawpti.getPosTaggedTitle().getPosTaggedWords()[i].wordclass.equals("verb")){
					numberOfVerbs++;
				}
			}	
		}
		if(nawpti.getPosTaggedLeadText().getPosTaggedWords()!=null){
			for(int i = 0; i<nawpti.getPosTaggedLeadText().getPosTaggedWords().length; i++){
				//System.out.println("Ordklasse " + ptw.getPosTaggedWords()[i].wordclass);
				if(nawpti.getPosTaggedLeadText().getPosTaggedWords()[i].wordclass.equals("verb")){
					numberOfVerbs++;
				}
			}
			
		}
		if(nawpti.getPosTaggedMainText().getPosTaggedWords()!=null){
			for(int i = 0; i<nawpti.getPosTaggedMainText().getPosTaggedWords().length; i++){
				//System.out.println("Ordklasse " + ptw.getPosTaggedWords()[i].wordclass);
				if(nawpti.getPosTaggedMainText().getPosTaggedWords()[i].wordclass.equals("verb")){
					numberOfVerbs++;
				}
			}
			
		}
		
		return numberOfVerbs;
	}
	
	//RETURNS TRUE IF IN BORS CATEGORY
	public boolean getBors(NewsArticleWithPosTaggedWords nawpti){
		boolean bors = false;
		ArticleCategoryGenerator ag = new ArticleCategoryGenerator();
		String article = ag.extractCategory(nawpti);
		
		if(article.equals(bors) || article.split("/")[0].equals("bors")){
			bors = true;
		}
		return bors;
	}
	
	//RETURNS TRUE IF IN Okonomi CATEGORY
	public boolean getOkonomi(NewsArticleWithPosTaggedWords nawpti){
		boolean okonomi = false;
		ArticleCategoryGenerator ag = new ArticleCategoryGenerator();
		String article = ag.extractCategory(nawpti);
		
		if(article.equals("okonomi") || article.split("/")[0].equals("okonomi")){
			okonomi = true;
		}
		return okonomi;
	}
	
	//RETURNS TRUE IF IN Analyser CATEGORY
	public boolean getAnalyse(NewsArticleWithPosTaggedWords nawpti){
		boolean analyser = false;
		ArticleCategoryGenerator ag = new ArticleCategoryGenerator();
		String article = ag.extractCategory(nawpti);
		
		if(article.equals("analyser") || article.split("/")[0].equals("analyser")){
			analyser = true;
		}
		return analyser;
	}
	
	public boolean getAnalyticsMentioned(NewsArticleWithPosTaggedWords nawpti){
		boolean am = false;
		String[] analytics = this.analyticClues;
		if(nawpti.getPosTaggedTitle().getPosTaggedWords()!=null){
			for(int i = 0; i<nawpti.getPosTaggedTitle().getPosTaggedWords().length; i++){
				for(int j=0; j<analytics.length; j++){
					if(nawpti.getPosTaggedTitle().getPosTaggedWords()[i].getInput().equals(analytics[j])){
						am = true;
					}
				}
			}
			
		}
	if(nawpti.getPosTaggedLeadText().getPosTaggedWords()!=null){
		for(int i = 0; i<nawpti.getPosTaggedLeadText().getPosTaggedWords().length; i++){
			for(int j=0; j<analytics.length; j++){
				if(nawpti.getPosTaggedLeadText().getPosTaggedWords()[i].getInput().equals(analytics[j])){
					am = true;
				}
			}
		}
			
		}
	if(nawpti.getPosTaggedMainText().getPosTaggedWords()!=null){		
		for(int i = 0; i<nawpti.getPosTaggedMainText().getPosTaggedWords().length; i++){
			for(int j=0; j<analytics.length; j++){
				if(nawpti.getPosTaggedMainText().getPosTaggedWords()[i].getInput().equals(analytics[j])){
					am = true;
				}
			}
		}
		
	}
	
		
		return am;
	}
	
	public boolean getRecommenderMentioned(NewsArticleWithPosTaggedWords nawpti){
		
		boolean rm = false;
		String[] analytics = this.recommenderClues;
		
		if(nawpti.getPosTaggedTitle().getPosTaggedWords()!=null){
			for(int i = 0; i<nawpti.getPosTaggedTitle().getPosTaggedWords().length; i++){
				for(int j=0; j<analytics.length; j++){
					if(nawpti.getPosTaggedTitle().getPosTaggedWords()[i].getInput().equalsIgnoreCase(analytics[j])){
						rm = true;
					}
				}
			}
			
		}
		if(nawpti.getPosTaggedLeadText().getPosTaggedWords()!=null){
			for(int i = 0; i<nawpti.getPosTaggedLeadText().getPosTaggedWords().length; i++){
				for(int j=0; j<analytics.length; j++){
					if(nawpti.getPosTaggedLeadText().getPosTaggedWords()[i].getInput().equalsIgnoreCase(analytics[j])){
						rm = true;
					}
				}
			}
					
				}
		if(nawpti.getPosTaggedMainText().getPosTaggedWords()!=null){
			for(int i = 0; i<nawpti.getPosTaggedMainText().getPosTaggedWords().length; i++){
				for(int j=0; j<analytics.length; j++){
					if(nawpti.getPosTaggedMainText().getPosTaggedWords()[i].getInput().equalsIgnoreCase(analytics[j])){
						rm = true;
					}
				}
			}
			
		}
		
		
		return rm;
	}
	
	public boolean isPositiveWordTitle(NewsArticleWithPosTaggedWords nawpti){
		if(getPositiveWordTitleCount(nawpti) > 0){
			System.out.println("Positiv in title: " + nawpti.getTitle());
		}
		return getPositiveWordTitleCount(nawpti) > 0;
	}

	public boolean isNegativeWordTitle(NewsArticleWithPosTaggedWords nawpti){
		if(getNegativeWordTitleCount(nawpti) > 0){
			System.out.println("Negativ in title" + nawpti.getTitle());
		}	
		return getNegativeWordTitleCount(nawpti) > 0;
	}
	
	public int getPositiveWordTitleCount(NewsArticleWithPosTaggedWords nawpti){
		String[] analytics = this.positiveTitleWordsClues;
		int counter = 0;
		for (String string : analytics) {
			if (nawpti.getTitle().contains(string)) {
				counter++;
			}
		}
		System.out.println("Positive title count: " + counter);
		return counter;
	}
	
	public int getNegativeWordTitleCount(NewsArticleWithPosTaggedWords nawpti){
		String[] analytics = negativeTitleWordsClues;
		int counter = 0;
		for (String string : analytics) {
			if (nawpti.getTitle().contains(string)) {
				counter++;
			}
		}
		System.out.println("Negative title count: " + counter);
		return counter;
	}
	
	
	//GENERATES VECTOR BASED ON ARTICLE FEATURES
	public FeaturesVektor getVector(VectorAttributes va, int sentimentTypeToClassify) throws IOException{
		FeaturesVektor vector = new FeaturesVektor();
		
		if(va.isNumberOfAdjectives()){
			vector.getFeatureVector().add((double)this.getNumberOfAdjectives());
		}
		if(va.isNumberOfSubstantives()){
			vector.getFeatureVector().add((double)this.getNumberOfSubstantives());
		}
		if(va.isNumberOfverbs()){
			vector.getFeatureVector().add((double)this.getNumberOfverbs());
		}
		if(va.isLengthOfTitle()){
			vector.getFeatureVector().add((double)this.getLengthOfTitle());
		}
		if(va.isLengthOfLeadText()){
			vector.getFeatureVector().add((double)this.getLengthOfLeadText());
		}
		if(va.isLengthOfMainText()){
			vector.getFeatureVector().add((double)this.getLengthOfMainText());
		}
		if(va.isNumberOfExclamationMarks()){
			vector.getFeatureVector().add((double)this.getNumberOfExclamationMarks());
		}
		if(va.isNumberOfQuestionMarks()){
			vector.getFeatureVector().add((double)this.getNumberOfQuestionMarks());
		}
		if(va.isNumberOfQuotes()){
			vector.getFeatureVector().add((double)this.getNumberOfQuotes());
		}
		if(va.isAverageLengthOfWords()){
			vector.getFeatureVector().add((double)this.getAverageLengthOfWords());
		}
	
		if(va.isAnalyticsMentioned()){
			if(this.isAnalyticsMentioned()){
				vector.getFeatureVector().add(1.0);
			}
			else{
				vector.getFeatureVector().add(0.0);
			}
		}
		if(va.isRecommenderCluesMentioned()){
			if(this.isRecommenderCluesMentioned()){
				vector.getFeatureVector().add(1.0);
			}
			else{
				vector.getFeatureVector().add(0.0);
			}
		}
		if(va.isNumberOfPositiveAdjectives()){
			vector.getFeatureVector().add((double)((this.getNumberOfPositiveAdjectives())/13.0));
		}
		if(va.isNumberOfNegativeAdjectives()){
			vector.getFeatureVector().add((double)((this.getNumberOfNegativeAdjectives())/27.0));
		}
		if(va.isNumberOfNeutralAdjectives()){
			vector.getFeatureVector().add((double)this.getNumberOfNeutralAdjectives());
		}
		if(va.isNumberOfPositiveVerbs()){
			vector.getFeatureVector().add((double)this.getNumberOfPositiveVerbs());
		}
		if(va.isNumberOfNegativeVerbs()){
			vector.getFeatureVector().add((double)this.getNumberOfNegativeVerbs());
		}
		if(va.isNumberOfPositiveAdverbs()){
			vector.getFeatureVector().add((double)this.getNumberOfPositiveAdverbs());
		}
		if(va.isNumberOfNegativeAdverbs()){
			vector.getFeatureVector().add((double)this.getNumberOfNegativeAdverbs());
		}
		
		if(va.isAnalyse()){
			if(this.isAnalyse()){
				vector.getFeatureVector().add(1.0);
			}
			else{
				vector.getFeatureVector().add(0.0);
			}
		}
	
		if(va.isBors()){
			if(this.isBors()){
				vector.getFeatureVector().add(1.0);
			}
			else{
				vector.getFeatureVector().add(0.0);
			}
		}
		if(va.isOkonomi()){
			if(this.isOkonomi()){
				vector.getFeatureVector().add(1.0);
			}
			else{
				vector.getFeatureVector().add(0.0);
			}
		}
		if(va.isPositiveTitle()){
			if(this.isPositiveTitle()){
				vector.getFeatureVector().add(1.0);
			}
			else{
				vector.getFeatureVector().add(0.0);
			}
		}
		if(va.isNegativeTitle()){
			if(this.isNegativeTitle()){
				vector.getFeatureVector().add(1.0);
			}
			else{
				vector.getFeatureVector().add(0.0);
			}
		}
		if(va.isNegativeTitleCount()){
			vector.getFeatureVector().add((double)this.getNegativeTitleCount());
		}
		if(va.isPositiveTitleCount()){
			vector.getFeatureVector().add((double)this.getPositiveTitleCount());
		}

		AnnotationValidator v = new AnnotationValidator();
		
		int overallSentiment = v.parseSentimentValue(this.getSentimentValue()).get(sentimentTypeToClassify);
		vector.getSentimentVector().add((double)overallSentiment);
		
		return vector;
		
	}
	
	
	//RETURNS AVERAGE LENGTH OF WORDS IN ARTICLE
	public double getAverageLengthOfWords(NewsArticleWithPosTaggedWords nawpti){
		String totalText = nawpti.getTitle() + nawpti.getlead_text() + nawpti.getText();
		
		double averageLength = 0;
		double totalLength = 0;
		int wordCounter = 0;
		
		String[] words = totalText.split(" ");

		for(int i=0; i<words.length; i++){
			totalLength+=words[i].length();
			wordCounter++;
			//System.out.println(totalLength);
		}

		averageLength = (double)totalLength/wordCounter;
		//System.out.println(averageLength);
		return averageLength;
	}
	
	//RETURNS NUMBER OF POSITIVE ADJECTIVES
	public int getNumberOfPositiveAdjectives(NewsArticleWithPosTaggedWords nawpti) throws JsonSyntaxException, IOException{
		int numberOfPositiveAdjectives = 0;
		SentimentWordExtractor sex = new SentimentWordExtractor();
		numberOfPositiveAdjectives = sex.getNumberOfSentimentBearingWords(nawpti, "adj", "1");
		//System.out.println(numberOfPositiveAdjectives);
		return numberOfPositiveAdjectives;
	}
	//GET NUBMER OF NEGATIVE ADJECTIVES
	public int getNumberOfNegativeAdjectives(NewsArticleWithPosTaggedWords nawpti) throws JsonSyntaxException, IOException{
		int numberOfNegativeAdjectives = 0;
		SentimentWordExtractor sex = new SentimentWordExtractor();
		numberOfPositiveAdjectives = sex.getNumberOfSentimentBearingWords(nawpti, "adj", "-1");
		//System.out.println(numberOfNegativeAdjectives);
		return numberOfNegativeAdjectives;
	}
	
	//GET NUBMER OF NEUTRAL ADJECTIVES
	public int getNumberOfNeutralAdjectives(NewsArticleWithPosTaggedWords nawpti) throws JsonSyntaxException, IOException{
		int numberOfNeutralAdjectives = 0;
		SentimentWordExtractor sex = new SentimentWordExtractor();
		numberOfPositiveAdjectives = sex.getNumberOfSentimentBearingWords(nawpti, "adj", "0");
		return numberOfNeutralAdjectives;
	}
	
	public int[] getNumberOfPositiveAdjectivesWithValenceShifting(NewsArticleWithPosTaggedWords nawpti) throws JsonSyntaxException, IOException{
		int [] valenceshiftPositive;
		
		SentimentWordExtractor sex = new SentimentWordExtractor();
		valenceshiftPositive = sex.getNumberOfSentimentBearingWordsWithValenceShifting(nawpti, this.getValenceShifters(), "adj", "1");
		
		return valenceshiftPositive;
	}	
	public int[] getNumberOfNegativeAdjectivesWithValenceShifting(NewsArticleWithPosTaggedWords nawpti) throws JsonSyntaxException, IOException{
	int [] valenceShiftNegative;
		
		SentimentWordExtractor sex = new SentimentWordExtractor();
		valenceShiftNegative = sex.getNumberOfSentimentBearingWordsWithValenceShifting(nawpti, this.getValenceShifters(), "adj", "-1");
		
		return valenceShiftNegative;
	}
	
	
	/*GET NUMBER OF POSITIVE VERBS */
	public int [] getNumberOfPositiveVerbs(NewsArticleWithPosTaggedWords nawpti) throws JsonSyntaxException, IOException{
		int [] numberOfPositiveVerbs;
		SentimentWordExtractor sex = new SentimentWordExtractor();
		numberOfPositiveVerbs = sex.getNumberOfSentimentBearingWordsWithValenceShifting(nawpti,this.getValenceShifters(), "verb", "1");
		//System.out.println(numberOfPositiveAdjectives);
		return numberOfPositiveVerbs;
	}
	/*GET NUMBER OF Negative VERBS */
	public int [] getNumberOfNegativeVerbs(NewsArticleWithPosTaggedWords nawpti) throws JsonSyntaxException, IOException{
		int [] numberOfNegativeVerbs;
		SentimentWordExtractor sex = new SentimentWordExtractor();
		numberOfNegativeVerbs = sex.getNumberOfSentimentBearingWordsWithValenceShifting(nawpti,this.getValenceShifters(), "verb", "-1");
		//System.out.println(numberOfPositiveAdjectives);
		return numberOfNegativeVerbs;
	}
	
	/*GET NUMBER OF POSITIVE AdVERBS */
	public int [] getNumberOfPositiveAdVerbs(NewsArticleWithPosTaggedWords nawpti) throws JsonSyntaxException, IOException{
		int [] numberOfPositiveAdVerbss;
		SentimentWordExtractor sex = new SentimentWordExtractor();
		numberOfPositiveAdVerbss = sex.getNumberOfSentimentBearingWordsWithValenceShifting(nawpti,this.getValenceShifters(), "adv", "1");
		//System.out.println(numberOfPositiveAdjectives);
		return numberOfPositiveAdVerbss;
	}
	/*GET NUMBER OF Negative ADVERBS */
	public int [] getNumberOfNegativeAdVerbs(NewsArticleWithPosTaggedWords nawpti) throws JsonSyntaxException, IOException{
		int [] numberOfNegativeAdVerbs;
		SentimentWordExtractor sex = new SentimentWordExtractor();
		numberOfNegativeAdVerbs = sex.getNumberOfSentimentBearingWordsWithValenceShifting(nawpti,this.getValenceShifters(), "adv", "-1");
		//System.out.println(numberOfPositiveAdjectives);
		return numberOfNegativeAdVerbs;
	}
	
	
	

	/*INITIALIZES NEWS ARTICLE WITH FEATURES*/
	public NewsArticleWithFeatures createFeatureArticle(NewsArticleWithPosTaggedWords inputArticle) throws JsonSyntaxException, IOException{
		NewsArticleWithFeatures nawf = new NewsArticleWithFeatures();
		
		
		nawf.setTitle(inputArticle.getTitle());
		nawf.setlead_text(inputArticle.getlead_text());
		nawf.setText(inputArticle.getText());
		
		nawf.setSentimentValue(inputArticle.getSentimentValue());
		
		nawf.setNumberOfAdjectives(getNumberOfAdjectives(inputArticle));
		nawf.setNumberOfSubstantives(getNumberOfSubstantives(inputArticle));
		nawf.setNumberOfverbs(getNumberOfVerbs(inputArticle));

		nawf.setLengthOfTitle(inputArticle.getTitle().length());
		nawf.setLengthOfLeadText(inputArticle.getlead_text().length());
		nawf.setLengthOfMainText(inputArticle.getText().length());
		
		nawf.setNumberOfExclamationMarks(getNumberOfExclamationMarks(inputArticle));
		nawf.setNumberOfQuestionMarks(getNumberOfQuestionMarks(inputArticle));
		nawf.setNumberOfQuotes(getNumberOfQuotes(inputArticle));
		
		nawf.setAverageLengthOfWords(getAverageLengthOfWords(inputArticle));
		
		nawf.setAnalyticsMentioned(getAnalyticsMentioned(inputArticle));
		nawf.setRecommenderCluesMentioned(getRecommenderMentioned(inputArticle));
		
		/* WITH VALENCE SHIFTING BETA */
		
		/* ADJEKTIV */
		int totalNumberOfPositiveAdjectives = nawf.getNumberOfPositiveAdjectivesWithValenceShifting(inputArticle)[0] + nawf.getNumberOfNegativeAdjectivesWithValenceShifting(inputArticle)[1];
		int totalNumberofNegativeAdjectives = nawf.getNumberOfNegativeAdjectivesWithValenceShifting(inputArticle)[0] + nawf.getNumberOfPositiveAdjectivesWithValenceShifting(inputArticle)[1];
		
		nawf.setNumberOfPositiveAdjectives(totalNumberOfPositiveAdjectives);
		nawf.setNumberOfNegativeAdjectives(totalNumberofNegativeAdjectives);
		
		/* VERB */
		int totalNumberOfPositiveVerbs = nawf.getNumberOfPositiveVerbs(inputArticle)[0] + nawf.getNumberOfNegativeVerbs(inputArticle)[1];
		int totalNumberofNegativeVerbs = nawf.getNumberOfNegativeVerbs(inputArticle)[0] + nawf.getNumberOfPositiveVerbs(inputArticle)[1];
		
		nawf.setNumberOfPositiveVerbs(totalNumberOfPositiveVerbs);
		nawf.setNumberOfNegativeVerbs(totalNumberofNegativeVerbs);
		
		
		/* ADVERB */
		int totalNumberOfPositiveAdverbs = nawf.getNumberOfPositiveAdVerbs(inputArticle)[0] + nawf.getNumberOfNegativeAdVerbs(inputArticle)[1];
		int totalNumberOfNegativeAdverbs = nawf.getNumberOfNegativeAdVerbs(inputArticle)[0] + nawf.getNumberOfPositiveAdVerbs(inputArticle)[1];
		
		nawf.setNumberOfPositiveAdverbs(totalNumberOfPositiveAdverbs);
		nawf.setNumberOfNegativeAdverbs(totalNumberOfNegativeAdverbs);
		
		/* WITHOUT VALENCE SHIFTING */
//		nawf.setNumberOfPositiveAdjectives(getNumberOfPositiveAdjectives(inputArticle));
//		nawf.setNumberOfNegativeAdjectives(getNumberOfNegativeAdjectives(inputArticle));
		nawf.setNumberOfNeutralAdjectives(getNumberOfNeutralAdjectives(inputArticle));
		
		nawf.setBors(getBors(inputArticle));
		nawf.setAnalyse(getAnalyse(inputArticle));
		nawf.setOkonomi(getOkonomi(inputArticle));
		
		
		nawf.setNegativeTitle(nawf.isPositiveWordTitle(inputArticle));
		nawf.setPositiveTitle(nawf.isNegativeWordTitle(inputArticle));
		nawf.setPositiveTitleCount(nawf.getPositiveWordTitleCount(inputArticle));
		nawf.setNegativeTitleCount(nawf.getNegativeWordTitleCount(inputArticle));
		
		return nawf;
	}
	
/* GETTERS AND SETTERS */ 
	public int getLengthOfText(String text){
		return text.length();
	}
	public boolean isPositiveTitle() {
		return hasPositiveTitle;
	}

	public void setPositiveTitle(boolean isPositiveTitle) {
		this.hasPositiveTitle = isPositiveTitle;
	}

	public boolean isNegativeTitle() {
		return hasNegativeTitle;
	}

	public void setNegativeTitle(boolean hasNegativeTitle) {
		this.hasNegativeTitle = hasNegativeTitle;
	}
	public int getNumberOfPositiveVerbs() {
		return numberOfPositiveVerbs;
	}

	public void setNumberOfPositiveVerbs(int numberOfPositiveVerbs) {
		this.numberOfPositiveVerbs = numberOfPositiveVerbs;
	}

	public int getNumberOfNegativeVerbs() {
		return numberOfNegativeVerbs;
	}

	public void setNumberOfNegativeVerbs(int numberOfNegativeVerbs) {
		this.numberOfNegativeVerbs = numberOfNegativeVerbs;
	}

	public int getNumberOfPositiveAdverbs() {
		return numberOfPositiveAdverbs;
	}

	public void setNumberOfPositiveAdverbs(int numberOfPositiveAdverbs) {
		this.numberOfPositiveAdverbs = numberOfPositiveAdverbs;
	}

	public int getNumberOfNegativeAdverbs() {
		return numberOfNegativeAdverbs;
	}

	public void setNumberOfNegativeAdverbs(int numberOfNegativeAdverbs) {
		this.numberOfNegativeAdverbs = numberOfNegativeAdverbs;
	}
	public int getNumberOfAdjectives() {
		return numberOfAdjectives;
	}

	public void setNumberOfAdjectives(int numberOfAdjectives) {
		this.numberOfAdjectives = numberOfAdjectives;
	}

	public int getNumberOfSubstantives() {
		return numberOfSubstantives;
	}

	public void setNumberOfSubstantives(int numberOfSubstantives) {
		this.numberOfSubstantives = numberOfSubstantives;
	}

	public int getNumberOfverbs() {
		return numberOfverbs;
	}

	public void setNumberOfverbs(int numberOfverbs) {
		this.numberOfverbs = numberOfverbs;
	}

	public int getLengthOfTitle() {
		return lengthOfTitle;
	}

	public void setLengthOfTitle(int lengthOfTitle) {
		this.lengthOfTitle = lengthOfTitle;
	}

	public int getLengthOfLeadText() {
		return lengthOfLeadText;
	}

	public void setLengthOfLeadText(int lengthOfLeadText) {
		this.lengthOfLeadText = lengthOfLeadText;
	}

	public int getLengthOfMainText() {
		return lengthOfMainText;
	}

	public void setLengthOfMainText(int lengthOfMainText) {
		this.lengthOfMainText = lengthOfMainText;
	}

	public int getNumberOfExclamationMarks() {
		return numberOfExclamationMarks;
	}

	public void setNumberOfExclamationMarks(int numberOfExclamationMarks) {
		this.numberOfExclamationMarks = numberOfExclamationMarks;
	}

	public int getNumberOfQuestionMarks() {
		return numberOfQuestionMarks;
	}

	public void setNumberOfQuestionMarks(int numberOfQuestionMarks) {
		this.numberOfQuestionMarks = numberOfQuestionMarks;
	}

	public int getNumberOfQuotes() {
		return numberOfQuotes;
	}

	public void setNumberOfQuotes(int numberOfQuotes) {
		this.numberOfQuotes = numberOfQuotes;
	}

	public boolean isAnalyticsMentioned() {
		return analyticsMentioned;
	}

	public void setAnalyticsMentioned(boolean analyticsMentioned) {
		this.analyticsMentioned = analyticsMentioned;
	}

	public double getAverageLengthOfWords() {
		return averageLengthOfWords;
	}

	public void setAverageLengthOfWords(double averageLengthOfWords) {
		this.averageLengthOfWords = averageLengthOfWords;
	}

	public boolean isRecommenderCluesMentioned() {
		return recommenderCluesMentioned;
	}

	public void setRecommenderCluesMentioned(boolean recommenderCluesMentioned) {
		this.recommenderCluesMentioned = recommenderCluesMentioned;
	}

	public int getNumberOfPositiveAdjectives() {
		return numberOfPositiveAdjectives;
	}

	public void setNumberOfPositiveAdjectives(int numberOfPositiveAdjectives) {
		this.numberOfPositiveAdjectives = numberOfPositiveAdjectives;
	}

	public int getNumberOfNegativeAdjectives() {
		return numberOfNegativeAdjectives;
	}

	public void setNumberOfNegativeAdjectives(int numberOfNegativeAdjectives) {
		this.numberOfNegativeAdjectives = numberOfNegativeAdjectives;
	}

	public int getNumberOfNeutralAdjectives() {
		return numberOfNeutralAdjectives;
	}

	public void setNumberOfNeutralAdjectives(int numberOfNeutralAdjectives) {
		this.numberOfNeutralAdjectives = numberOfNeutralAdjectives;
	}

	public boolean isBors() {
		return isBors;
	}

	public void setBors(boolean isBors) {
		this.isBors = isBors;
	}
	public int getNegativeTitleCount() {
		return negativeTitleCount;
	}

	public void setNegativeTitleCount(int negativeTitleCount) {
		this.negativeTitleCount = negativeTitleCount;
	}

	public int getPositiveTitleCount() {
		return positiveTitleCount;
	}

	public void setPositiveTitleCount(int positiveTitleCount) {
		this.positiveTitleCount = positiveTitleCount;
	}
	public boolean isAnalyse() {
		return isAnalyse;
	}

	public void setAnalyse(boolean isAnalyse) {
		this.isAnalyse = isAnalyse;
	}

	public boolean isOkonomi() {
		return isOkonomi;
	}

	public void setOkonomi(boolean isOkonomi) {
		this.isOkonomi = isOkonomi;
	}
	public String[] getAnalyticClues() {
		return analyticClues;
	}
	public void setAnalyticClues(String[] analyticClues) {
		this.analyticClues = analyticClues;
	}
	public String[] getRecommenderClues() {
		return recommenderClues;
	}
	public void setRecommenderClues(String[] recommenderClues) {
		this.recommenderClues = recommenderClues;
	}
	public String[] getPositiveTitleWordsClues() {
		return positiveTitleWordsClues;
	}
	public void setPositiveTitleWordsClues(String[] positiveTitleWordsClues) {
		this.positiveTitleWordsClues = positiveTitleWordsClues;
	}
	public String[] getNegativeTitleWordsClues() {
		return negativeTitleWordsClues;
	}


	public void setNegativeTitleWordsClues(String[] negativeTitleWordsClues) {
		this.negativeTitleWordsClues = negativeTitleWordsClues;
	}


	public String[] getValenceShifters() {
		return valenceShifters;
	}


	public void setValenceShifters(String[] valenceShifters) {
		this.valenceShifters = valenceShifters;
	}

	
	
	
	public static void main(String[] args){
		

        		
	}
	
	
	
	
}
