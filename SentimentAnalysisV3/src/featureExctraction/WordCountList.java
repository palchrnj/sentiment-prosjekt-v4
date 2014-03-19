package featureExctraction;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import preProcessing.NewsArticlesWithPosTaggedWords;
import preProcessing.PosTaggedWord;
import utils.ArticleSorter;
import newsAPI.JsonHandler;

import com.google.gson.Gson;

public class WordCountList {

	public ArrayList<WordCount> words = new ArrayList<WordCount>();
	public int totalTitleCount;
	public int totalLeadTextCount;
	
	public WordCountList(){
		this.totalTitleCount = 0;
		this.totalLeadTextCount = 0;
	}
	
	public void addWordTF(String word, String position){
		boolean alreadyExists = false;
		for(int i=0; i<words.size(); i++){
			if(words.get(i).getWord().equals(word)){
				words.get(i).termFrequency += 1;
				alreadyExists = true;
				if(position == "title"){
					words.get(i).titleCounter+=1;
				}
				else{
					words.get(i).leadCounter+=1;
				}
			}
		}
		if(!alreadyExists){
			WordCount newWord = new WordCount(word);
			if(position == "title"){
				newWord.titleCounter+=1;
			}
			else{
				newWord.leadCounter+=1;
			}
			words.add(newWord);
		}
	}
	public void addWordDF(String word, String position){
		for(int i=0; i<words.size(); i++){
			if(words.get(i).getWord().equals(word)){
				words.get(i).documentFrequency += 1;
			}
		}
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

	public ArrayList<WordCount> getWords() {
		return words;
	}
	public void setWords(ArrayList<WordCount> words) {
		this.words = words;
	}
	public void sortWords(){
		ArrayList<WordCount> sorted = this.getWords();
		Collections.sort(sorted, new WordCountComparator());
		this.setWords(sorted);
		
	}
	
	public int getTotalTitleCount() {
		return totalTitleCount;
	}

	public void setTotalTitleCount(int totalTitleCount) {
		this.totalTitleCount = totalTitleCount;
	}

	public int getTotalLeadTextCount() {
		return totalLeadTextCount;
	}

	public void setTotalLeadTextCount(int totalLeadTextCount) {
		this.totalLeadTextCount = totalLeadTextCount;
	}
	
	public static void main(String args[]) throws IOException{
		main2();
		
//		JsonHandler handler = new JsonHandler("ArticleSteps/3_POStaggedArticles/MainDataSetPOS.json", "pos");
//		Gson g = new Gson();
//		WordCountList wcl = new WordCountList();	
//		
//		NewsArticlesWithPosTaggedWords articleSource = handler.getPosTaggedArticles(); 
//		System.out.println(articleSource.getNawpti().size());
//		
//		for(int i=0; i< articleSource.getNawpti().size(); i++){
//			
//			ArrayList<String> wordsInTitle = new ArrayList<String>();
//			ArrayList<String> wordsInLead = new ArrayList<String>();
//			
//			if(articleSource.getNawpti().get(i).getPosTaggedTitle().getPosTaggedWords()!=null){
//			
//				wcl.totalTitleCount+=1;
//				for(int j=0; j<articleSource.getNawpti().get(i).getPosTaggedTitle().getPosTaggedWords().length; j++){
//					
//					String currentWord = articleSource.getNawpti().get(i).getPosTaggedTitle().getPosTaggedWords()[j].getInput();
//					
//					//ADD WORD TO TF
//					wcl.addWordTF(currentWord, "title");
//					
//					//CHECK IF TITLE HAS CURRENT WORD AND ADD TO DF IF NOT
//					if(!wordsInTitle.contains(currentWord)){
//						wcl.addWordDF(currentWord, "title");
//					}
//					
//					//ADD WORD TO TITLE WORDS OVERVIEW LIST
//					wordsInTitle.add(currentWord);
//					
//				}
//			}
//			if(articleSource.getNawpti().get(i).getPosTaggedLeadText().getPosTaggedWords()!=null){
//				wcl.totalLeadTextCount+=1;
//				for(int k=0; k<articleSource.getNawpti().get(i).getPosTaggedLeadText().getPosTaggedWords().length; k++){
//					
//					
//					String currentWord = articleSource.getNawpti().get(i).getPosTaggedLeadText().getPosTaggedWords()[k].getInput();
//					
//					//CHECK IF WORDS IN TITLE OR WORDS IN LEAD CONTAINS WORD
//					if(wordsInTitle.contains(currentWord)|| wordsInLead.contains(currentWord)){
//						wcl.addWordTF(articleSource.getNawpti().get(i).getPosTaggedLeadText().getPosTaggedWords()[k].getInput(), "lead");
//					}
//					else{
//						wcl.addWordTF(articleSource.getNawpti().get(i).getPosTaggedLeadText().getPosTaggedWords()[k].getInput(), "lead");
//						wcl.addWordDF(articleSource.getNawpti().get(i).getPosTaggedLeadText().getPosTaggedWords()[k].getInput(), "lead");
//					}
//					
//					wordsInLead.add(articleSource.getNawpti().get(i).getPosTaggedLeadText().getPosTaggedWords()[k].getInput());
//					
//				}
//			}
//		}
//
//		wcl.sortWords();
//		String wclNew = g.toJson(wcl);
//		wcl.writeToArticleFile(wclNew, wcl.getPath()+"WordlistsOfImportance/", "WCLCOTS");
	
	}
	

	
	public static void main2() throws IOException {
		HashMap<Integer, Integer> titleSentenceLength = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> leadSentenceLength = new HashMap<Integer, Integer>();
		HashMap<String, Integer> wordClassCount = new HashMap<String, Integer>();
		
		JsonHandler handler = new JsonHandler("ArticleSteps/3_POStaggedArticles/MainDataSetPOS.json", "pos");
		Gson g = new Gson();
		WordCountList wcl = new WordCountList();	
		
		NewsArticlesWithPosTaggedWords articleSource = handler.getPosTaggedArticles(); 
		System.out.println(articleSource.getNawpti().size());
		
		for(int i=0; i< articleSource.getNawpti().size(); i++){
			
			ArrayList<String> wordsInTitle = new ArrayList<String>();
			ArrayList<String> wordsInLead = new ArrayList<String>();
			// only keep nouns, adjective, verb and adverb!
			
			if(articleSource.getNawpti().get(i).getPosTaggedTitle().getPosTaggedWords()!=null){
			
				wcl.totalTitleCount+=1;
				ArrayList<Integer> titleLengthList = getNumberOfWords(articleSource.getNawpti().get(i).getPosTaggedTitle().getPosTaggedWords());
				for (Integer titleLength : titleLengthList) {
					if (! titleSentenceLength.containsKey(titleLength)) {
						titleSentenceLength.put(titleLength, 1);
					} else {
						int count = titleSentenceLength.get(titleLength);
						count++;
						titleSentenceLength.put(titleLength, count);
					}
				}
				wordClassCount = updateWordClassCount(wordClassCount, articleSource.getNawpti().get(i).getPosTaggedTitle().getPosTaggedWords());
				
//				System.out.print(getStringRepresentation(articleSource.getNawpti().get(i).getPosTaggedTitle().getPosTaggedWords()));
//				System.out.println(String.format("Title length=%s", articleSource.getNawpti().get(i).getPosTaggedTitle().getPosTaggedWords().length));
//				for(int j=0; j<articleSource.getNawpti().get(i).getPosTaggedTitle().getPosTaggedWords().length; j++){
//					
//					String currentWord = articleSource.getNawpti().get(i).getPosTaggedTitle().getPosTaggedWords()[j].getInput();
//					
//					//ADD WORD TO TF
//					wcl.addWordTF(currentWord, "title");
//					
//					//CHECK IF TITLE HAS CURRENT WORD AND ADD TO DF IF NOT
//					if(!wordsInTitle.contains(currentWord)){
//						wcl.addWordDF(currentWord, "title");
//					}
//					
//					//ADD WORD TO TITLE WORDS OVERVIEW LIST
//					wordsInTitle.add(currentWord);
					
//				}
			}
			if(articleSource.getNawpti().get(i).getPosTaggedLeadText().getPosTaggedWords()!=null){
				wcl.totalLeadTextCount+=1;
				ArrayList<Integer> leadLengthList = getNumberOfWords(articleSource.getNawpti().get(i).getPosTaggedLeadText().getPosTaggedWords());
//				if (leadLength >= 30) {
//					System.out.print(leadLength + ": " + getStringRepresentation(articleSource.getNawpti().get(i).getPosTaggedLeadText().getPosTaggedWords()));
//				}
				for (Integer leadLength : leadLengthList) {
					if (! leadSentenceLength.containsKey(leadLength)) {
						leadSentenceLength.put(leadLength, 1);
					} else {
						int count = leadSentenceLength.get(leadLength);
						count++;
						leadSentenceLength.put(leadLength, count);
					}
				}
				wordClassCount = updateWordClassCount(wordClassCount, articleSource.getNawpti().get(i).getPosTaggedLeadText().getPosTaggedWords());
//				System.out.println(String.format("Lead length=%s", articleSource.getNawpti().get(i).getPosTaggedLeadText().getPosTaggedWords().length));
//				for(int k=0; k<articleSource.getNawpti().get(i).getPosTaggedLeadText().getPosTaggedWords().length; k++){
//					
//					
//					String currentWord = articleSource.getNawpti().get(i).getPosTaggedLeadText().getPosTaggedWords()[k].getInput();
//					
//					//CHECK IF WORDS IN TITLE OR WORDS IN LEAD CONTAINS WORD
//					if(wordsInTitle.contains(currentWord)|| wordsInLead.contains(currentWord)){
//						wcl.addWordTF(articleSource.getNawpti().get(i).getPosTaggedLeadText().getPosTaggedWords()[k].getInput(), "lead");
//					}
//					else{
//						wcl.addWordTF(articleSource.getNawpti().get(i).getPosTaggedLeadText().getPosTaggedWords()[k].getInput(), "lead");
//						wcl.addWordDF(articleSource.getNawpti().get(i).getPosTaggedLeadText().getPosTaggedWords()[k].getInput(), "lead");
//					}
//					
//					wordsInLead.add(articleSource.getNawpti().get(i).getPosTaggedLeadText().getPosTaggedWords()[k].getInput());
//					
//				}
			}
		}
		System.out.println(titleSentenceLength);
		System.out.println(leadSentenceLength);
		System.out.println("Cumulative probability all: " + createMatlabFriendlyString(createCumulativeProbability(titleSentenceLength, leadSentenceLength)));
//		System.out.println(getCumulativeProbability(titleSentenceLength, 992));
//		System.out.println(getCumulativeProbability(leadSentenceLength, 992));
		System.out.println(wordClassCount);
//		wcl.sortWords();
//		String wclNew = g.toJson(wcl);
//		wcl.writeToArticleFile(wclNew, wcl.getPath()+"WordlistsOfImportance/", "WCLCOTS");
	}
	
	private static HashMap<Integer, Double> createCumulativeProbability(
			HashMap<Integer, Integer> titleSentenceLength,
			HashMap<Integer, Integer> leadSentenceLength) {
		HashMap<Integer, Double> retmap = new HashMap<Integer, Double>(); 
		int sentenceCounter = 0;
		for (int i = 1; i <= 29; i++) {
			int countT = titleSentenceLength.get(i) != null ? titleSentenceLength.get(i) : 0; 
			int countL = leadSentenceLength.get(i) != null ? leadSentenceLength.get(i) : 0;
			sentenceCounter += countT + countL;
			retmap.put(i, (double) (countT+countL));
		}
		double cumulativeCount = 0;
		for (Integer key : retmap.keySet()) {
			cumulativeCount += retmap.get(key);
			retmap.put(key, cumulativeCount / sentenceCounter);
		}
		return retmap;
	}
	
	private static String createMatlabFriendlyString(HashMap<Integer, Double> map) {
		String str = "[";
		for (Integer key : map.keySet()) {
			str += map.get(key) + " ";
		}
		return str.substring(0, str.length()-1) + "]";
	}

	private static HashMap<Integer, Double> getCumulativeProbability(
			HashMap<Integer, Integer> map, double length) {
		double sum = 0;
		HashMap<Integer, Double> retmap = new HashMap<Integer, Double>();
		for (Integer key : map.keySet()) {
			double count = map.get(key);
			sum += count;
			retmap.put(key, sum / length);
		}
		return retmap;
	}

	private static ArrayList<Integer> getNumberOfWords(PosTaggedWord[] posTaggedWords) {
//	Should be included: konj, prep, pron, interj, sbu, adv, subst, verb, inf-merke, adj, det
		ArrayList<String> wordclassList = new ArrayList<String>(Arrays.asList("konj", "prep", "pron", "interj", "sbu", "adv", "subst", "verb", "inf-merke", "adj", "det"));
		ArrayList<Integer> count = new ArrayList<Integer>();
		count.add(0);
		for (int i = 0; i < posTaggedWords.length;i++) {
			if (posTaggedWords[i].input.equals(".")) {
				PosTaggedWord[] words = selectFrom(posTaggedWords, i + 1);
				if (words != null) {
					count.addAll(getNumberOfWords(words));
				}
				return count;
			} else if (wordclassList.contains(posTaggedWords[i].wordclass)) {
				count.set(count.size()-1,count.get(count.size()-1) + 1);
//			} else {
//				System.out.println(posTaggedWords[i].wordclass);
			}
		}
		return count;
	}

	private static String getStringRepresentation(PosTaggedWord[] posTaggedWords) {
		String str1 = "";
		String str2 = "";
		for (int i = 0; i < posTaggedWords.length;i++) {
			str1 += posTaggedWords[i].getInput() + " (" + posTaggedWords[i].wordclass + ")";
//			str2 += posTaggedWords[i].wordclass + " ";
		}
		return str1 + "\n";// +  str2 + "\n";
	}
	
	public static HashMap<String, Integer> updateWordClassCount(HashMap<String, Integer> map, PosTaggedWord[] posTaggedWords) {
		for (int i = 0; i < posTaggedWords.length;i++) {
//			if (posTaggedWords[i].input.equals(".")) {
//				map = updateWordClassCount(map, selectFrom(posTaggedWords, i));
//			}
			String wordclass = posTaggedWords[i].wordclass;
			if (! map.containsKey(wordclass)) {
				map.put(wordclass, 1);
			} else {
				int count = map.get(wordclass);
				count++;
				map.put(wordclass, count);
			}
		}
		return map;
	}
	
	private static PosTaggedWord[] selectFrom(PosTaggedWord[] posTaggedWords, int index) {
		if (index >= posTaggedWords.length-1) {
			return null;
		} else {
			PosTaggedWord[] retwords = new PosTaggedWord[posTaggedWords.length-index];
			int counter = 0;
			for (int i = index; i < posTaggedWords.length; i++) {
				retwords[counter++] = posTaggedWords[i];
			}
			return retwords;
		}
	}
}
