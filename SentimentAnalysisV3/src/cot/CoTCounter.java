package cot;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import featureExctraction.WordCountList;
import preProcessing.NewsArticleWithCots;
import preProcessing.NewsArticleWithStemmedVersion;
import preProcessing.NewsArticlesWithStemmedVersion;
import preProcessing.PosTaggedWord;
import preProcessing.TextFileHandler;
import sun.print.CUPSPrinter;
import newsAPI.JsonHandler;

public class CoTCounter {
	
	/*
	 * This class counts the number of CoTs within distance r in a news artile
	 */
	
	public HashMap<String, Integer> map;
	public HashMap<String, Integer> mapDocFreq;
	public HashMap<String, Integer> termFrequency;
	public HashMap<String, Integer> docFrequency;
	public int radius;
	public String filename;
	public String folder = "";
	
	public CoTCounter(String filename, int radius) {
		this.map = new HashMap<String, Integer>();
		this.mapDocFreq = new HashMap<String, Integer>();
		this.filename = filename;
		this.radius = radius;
	}
	public CoTCounter(int radius) {
		this.map = new HashMap<String, Integer>();
		this.mapDocFreq = new HashMap<String, Integer>();
		this.radius = radius;
	}
	
	public ArrayList<String> getCoTsFromIndex(ArrayList<PosTaggedWord> allPosTaggedWords, int index) {
		ArrayList<String> cots = new ArrayList<String>();
		String current = allPosTaggedWords.get(index).stem;
		if (current.length() >= 1 && current.charAt(0) == '$') {
			return cots;
		}
		int steps = 1;
		for (int i = index + 1 ; i < allPosTaggedWords.size(); i++) {
			if (steps > radius) {
				return cots;
			}
			String candidate = allPosTaggedWords.get(i).stem;
			if (candidate.length() >= 1 && candidate.charAt(0) == '$') {
				// some kind of sign - could be comma or stop sign
				if (candidate.length() >= 2 && ".!?".contains(candidate.charAt(1)+"")) {
					return cots;
				} else {
					// Some other sign ',', ';' etc, --> skip
				}
			} else if (candidate.length() < 1){
				// skip Strings of length 0
			} else {
				// word - add CoT
				cots.add(current + " " + candidate);
				steps++;
			}
		}
		return cots;
	}

	public static ArrayList<String> getCoTsFromIndex(ArrayList<PosTaggedWord> allPosTaggedWords, int index, int radius) {
		ArrayList<String> cots = new ArrayList<String>();
		String current = allPosTaggedWords.get(index).stem;
		if (current.length() >= 1 && current.charAt(0) == '$') {
			return cots;
		}
		int steps = 1;
		for (int i = index + 1 ; i < allPosTaggedWords.size(); i++) {
			if (steps > radius) {
				return cots;
			}
			String candidate = allPosTaggedWords.get(i).stem;
			if (candidate.length() >= 1 && candidate.charAt(0) == '$') {
				// some kind of sign - could be comma or stop sign
				if (candidate.length() >= 2 && ".!?".contains(candidate.charAt(1)+"")) {
					return cots;
				} else {
					// Some other sign ',', ';' etc, --> skip
				}
			} else if (candidate.length() < 1){
				// skip Strings of length 0
			} else {
				// word - add CoT
				cots.add(current + " " + candidate);
				steps++;
			}
		}
		return cots;
	}
	

	public ArrayList<String> getCoTsFromIndexNounAdjectiveVerbAdverb(ArrayList<PosTaggedWord> allPosTaggedWords, int index) {
		
		// Only create cots from worclasses noun, adjective, verb and adverb only
		
		ArrayList<String> permittedWordclasses = new ArrayList<String>(Arrays.asList("subst", "adj", "verb", "adv"));

		
		ArrayList<String> cots = new ArrayList<String>();
		String current = allPosTaggedWords.get(index).stem;
		if (current.length() >= 1 && current.charAt(0) == '$') {
			return cots;
		}
		int steps = 1;
		for (int i = index + 1 ; i < allPosTaggedWords.size(); i++) {
			if (steps > radius) {
				return cots;
			}
			String candidate = allPosTaggedWords.get(i).stem;
			String wordclassOfCandidate = allPosTaggedWords.get(i).wordclass;
			if (candidate.length() >= 1 && candidate.charAt(0) == '$') {
				// some kind of sign - could be comma or stop sign
				if (candidate.length() >= 2 && ".!?".contains(candidate.charAt(1)+"")) {
					return cots;
				} else {
					// Some other sign ',', ';' etc, --> skip
				}
			} else if (candidate.length() < 1){
				// skip Strings of length 0
			} else if (current.equals(candidate)) {
				// same strings - skip
				steps++;
			} else {
				// word - add CoT
				if (permittedWordclasses.contains(wordclassOfCandidate)) {
					// Only add cot IF second part is of wordclass noun, adjective, verb and adverb
					cots.add(current + " " + candidate);
				}
				// Update steps regardless
				steps++;
			}
		}
		return cots;
	}

	public void cotCountArticles(JsonHandler jh) {
		map = new HashMap<String, Integer>();
		for (NewsArticleWithStemmedVersion nawsv : jh.stemmedArticles.getNawsv()) {
//			for (int i = 0; i < nawsv.getAllPosTaggedWords().size();i++) {
//				System.out.print(nawsv.getAllPosTaggedWords().get(i).stem + " ");
//			}
			for (int i = 0; i < nawsv.getAllPosTaggedWords().size();i++) {
				ArrayList<String> cots = getCoTsFromIndex(nawsv.getAllPosTaggedWords(), i);
				addCoTsToMap(cots);
			}
		}
	}
	
	public void cotCountArticlesNotMainText(JsonHandler jh) {
		map = new HashMap<String, Integer>();
		for (NewsArticleWithStemmedVersion nawsv : jh.stemmedArticles.getNawsv()) {
//			for (int i = 0; i < nawsv.getAllPosTaggedWords().size();i++) {
//				System.out.print(nawsv.getAllPosTaggedWords().get(i).stem + " ");
//			}
			ArrayList<PosTaggedWord> ptwList = new ArrayList<PosTaggedWord>();
			
			if(nawsv.getPosTaggedTitle().getPosTaggedWords()!= null){
				for (PosTaggedWord posTaggedWord : nawsv.getPosTaggedTitle().getPosTaggedWords()) {
					ptwList.add(posTaggedWord);
				}
			}	
			if(nawsv.getPosTaggedLeadText().getPosTaggedWords()!= null){
				for (PosTaggedWord posTaggedWord : nawsv.getPosTaggedLeadText().getPosTaggedWords()) {
					ptwList.add(posTaggedWord);
				}
			}
			
			for (int i = 0; i < ptwList.size();i++) {
				ArrayList<String> cots = getCoTsFromIndex(ptwList, i);
				addCoTsToMap(cots);
			}
		}
	}

	public void cotCountArticlesNotMainTextNounAdjectiveVerbAdverb(JsonHandler jh) {
		
		// Only create cots from worclasses noun, adjective, verb and adverb only
		
		map = new HashMap<String, Integer>();
		for (NewsArticleWithStemmedVersion nawsv : jh.stemmedArticles.getNawsv()) {
//			for (int i = 0; i < nawsv.getAllPosTaggedWords().size();i++) {
//				System.out.print(nawsv.getAllPosTaggedWords().get(i).stem + " ");
//			}
			ArrayList<PosTaggedWord> ptwList = new ArrayList<PosTaggedWord>();
			
			if(nawsv.getPosTaggedTitle().getPosTaggedWords()!= null){
				for (PosTaggedWord posTaggedWord : nawsv.getPosTaggedTitle().getPosTaggedWords()) {
					ptwList.add(posTaggedWord);
				}
			}	
			if(nawsv.getPosTaggedLeadText().getPosTaggedWords()!= null){
				for (PosTaggedWord posTaggedWord : nawsv.getPosTaggedLeadText().getPosTaggedWords()) {
					ptwList.add(posTaggedWord);
				}
			}
			Set<String> wordset = new TreeSet<String>();
			for (int i = 0; i < ptwList.size();i++) {
				ArrayList<String> permittedWordclasses = new ArrayList<String>(Arrays.asList("subst", "adj", "verb", "adv"));
				String wordclass = ptwList.get(i).wordclass;
				if (permittedWordclasses.contains(wordclass)) {
//					System.out.println("Contains: " + wordclass);
					ArrayList<String> cots = getCoTsFromIndexNounAdjectiveVerbAdverb(ptwList, i);
					wordset.addAll(cots);
					addCoTsToMap(cots);					
				} //else {
//					System.out.println("Does not contain: " + wordclass);
//				}
			}
			for (String str : wordset) {
				if (mapDocFreq.containsKey(str)) {
					int count = mapDocFreq.get(str);
					count++;
					mapDocFreq.put(str, count);
				} else {
					mapDocFreq.put(str, 1);
				}
			}
		}
	}

	public HashMap<String, Integer> getTermDistribution(JsonHandler jh) {
		
		// Only create cots from worclasses noun, adjective, verb and adverb only
		
		map = new HashMap<String, Integer>();
		for (NewsArticleWithStemmedVersion nawsv : jh.stemmedArticles.getNawsv()) {
//			for (int i = 0; i < nawsv.getAllPosTaggedWords().size();i++) {
//				System.out.print(nawsv.getAllPosTaggedWords().get(i).stem + " ");
//			}
			ArrayList<PosTaggedWord> ptwList = new ArrayList<PosTaggedWord>();
			
			if(nawsv.getPosTaggedTitle().getPosTaggedWords()!= null){
				for (PosTaggedWord posTaggedWord : nawsv.getPosTaggedTitle().getPosTaggedWords()) {
					ptwList.add(posTaggedWord);
				}
			}	
			if(nawsv.getPosTaggedLeadText().getPosTaggedWords()!= null){
				for (PosTaggedWord posTaggedWord : nawsv.getPosTaggedLeadText().getPosTaggedWords()) {
					ptwList.add(posTaggedWord);
				}
			}
			Set<String> wordset = new TreeSet<String>();
			for (int i = 0; i < ptwList.size();i++) {
				ArrayList<String> permittedWordclasses = new ArrayList<String>(Arrays.asList("subst", "adj", "verb", "adv"));
				String wordclass = ptwList.get(i).wordclass;
				if (permittedWordclasses.contains(wordclass)) {
//					System.out.println("Contains: " + wordclass);
					ArrayList<String> cots = getCoTsFromIndexNounAdjectiveVerbAdverb(ptwList, i);
					wordset.addAll(cots);
					addCoTsToMap(cots);					
				} //else {
//					System.out.println("Does not contain: " + wordclass);
//				}
			}
			for (String str : wordset) {
				if (mapDocFreq.containsKey(str)) {
					int count = mapDocFreq.get(str);
					count++;
					mapDocFreq.put(str, count);
				} else {
					mapDocFreq.put(str, 1);
				}
			}
		}
	}

	public void termCountArticlesNotMainTextNounAdjectiveVerbAdverb(JsonHandler jh) {
		
		// Only create terms from worclasses noun, adjective, verb and adverb only
		
		termFrequency = new HashMap<String, Integer>();
		docFrequency = new HashMap<String, Integer>();
		for (NewsArticleWithStemmedVersion nawsv : jh.stemmedArticles.getNawsv()) {
//			for (int i = 0; i < nawsv.getAllPosTaggedWords().size();i++) {
//				System.out.print(nawsv.getAllPosTaggedWords().get(i).stem + " ");
//			}
			ArrayList<PosTaggedWord> ptwList = new ArrayList<PosTaggedWord>();
			
			if(nawsv.getPosTaggedTitle().getPosTaggedWords()!= null){
				for (PosTaggedWord posTaggedWord : nawsv.getPosTaggedTitle().getPosTaggedWords()) {
					ptwList.add(posTaggedWord);
				}
			}	
			if(nawsv.getPosTaggedLeadText().getPosTaggedWords()!= null){
				for (PosTaggedWord posTaggedWord : nawsv.getPosTaggedLeadText().getPosTaggedWords()) {
					ptwList.add(posTaggedWord);
				}
			}
			Set<String> wordset = new TreeSet<String>();
			for (int i = 0; i < ptwList.size();i++) {
				ArrayList<String> permittedWordclasses = new ArrayList<String>(Arrays.asList("subst", "adj", "verb", "adv"));
				String wordclass = ptwList.get(i).wordclass;
				if (permittedWordclasses.contains(wordclass)) {
					String str = ptwList.get(i).stem;
					if (termFrequency.containsKey(str)) {
						int count = termFrequency.get(str);
						count++;
						termFrequency.put(str, count);
					} else {
						termFrequency.put(str, 1);
					}
//					System.out.println("Contains: " + wordclass);
					wordset.add(str);
				} //else {
//					System.out.println("Does not contain: " + wordclass);
//				}
			}
			// Keep track of set so that no duplicates within same article are counted
			for (String str : wordset) {
				if (docFrequency.containsKey(str)) {
					int count = docFrequency.get(str);
					count++;
					docFrequency.put(str, count);
				} else {
					docFrequency.put(str, 1);
				}
			}
		}
	}
	
	public HashMap<String, Integer> getCotsForArticle(NewsArticleWithStemmedVersion stemmedArticle){
		HashMap<String, Integer> articleHashmap = new HashMap<String, Integer>();

		for (int i = 0; i < stemmedArticle.getAllPosTaggedWords().size(); i++) {
			//System.out.println("Stemmed article cots: " + stemmedArticle.getAllPosTaggedWords().size());
			ArrayList<String> cots = getCoTsFromIndex(stemmedArticle.getAllPosTaggedWords(), i);
	
			addCoTsToMap(articleHashmap, cots);
		}
//		System.out.println("Cots: " + articleHashmap.toString());
		return articleHashmap;
		
	}
	public NewsArticleWithCots initiateCotsArticle(NewsArticleWithStemmedVersion stemmedArticle){
		NewsArticleWithCots nawc = new NewsArticleWithCots();
		
		
		nawc.setcat(stemmedArticle.getcat());
		nawc.setId(stemmedArticle.getId());
		nawc.setImageUrl(stemmedArticle.getImageUrl());
		nawc.setlast_modified(stemmedArticle.getlast_modified());
		nawc.setlead_text(stemmedArticle.getlead_text());
		nawc.setLinks(stemmedArticle.getLinks());
		nawc.setpublished(stemmedArticle.getpublished());
		nawc.setPublisher(stemmedArticle.getPublisher());
		nawc.setSentimentValue(stemmedArticle.getSentimentValue());
		nawc.setSignature(stemmedArticle.getSignature());
		nawc.setTags(stemmedArticle.getTags());
		nawc.setText(stemmedArticle.getText());
		nawc.setTitle(stemmedArticle.getTitle());
		nawc.setversion(stemmedArticle.getversion());
		nawc.setTickerList(stemmedArticle.getTickerList());
		nawc.setKeywordList(stemmedArticle.getKeywordList());
		nawc.setAuthorName(stemmedArticle.getAuthorName());
		
		nawc.setPosTaggedTitle(stemmedArticle.getPosTaggedTitle());
		nawc.setPosTaggedLeadText(stemmedArticle.getPosTaggedLeadText());
		nawc.setPosTaggedMainText(stemmedArticle.getPosTaggedMainText());
		
		nawc.setStemmedTitle(stemmedArticle.getStemmedTitle());
		nawc.setStemmedLeadText(stemmedArticle.getStemmedLeadText());
		nawc.setStemmedText(stemmedArticle.getStemmedText());
		
		nawc.setCots(this.getCotsForArticle(stemmedArticle));
		
		//System.out.println("Initiating article" + nawc.getCots().toString());
		
		
		return nawc;
		
	}
	

	public static HashMap<String, Integer> cotCountArticles(NewsArticleWithStemmedVersion nawsv, int radius) {
		HashMap<String, Integer> hashmap = new HashMap<String, Integer>();
			for (int i = 0; i < nawsv.getAllPosTaggedWords().size();i++) {
				ArrayList<String> cots = getCoTsFromIndex(nawsv.getAllPosTaggedWords(), i, radius);
				hashmap = addCoTsToMap(hashmap, cots);
			}
		return hashmap;
	}

	public static HashMap<String, Integer> addCoTsToMap(HashMap<String, Integer> hashmap, ArrayList<String> cots) {
		for (String cot : cots) {
			if ( hashmap.containsKey(cot)) {
				int count = hashmap.get(cot);
				hashmap.put(cot, ++count);
			} else {
				hashmap.put(cot, 1);
			}
		}
		return hashmap;
	}


	public void addCoTsToMap(ArrayList<String> cots) {
		for (String cot : cots) {
			if ( map.containsKey(cot)) {
				int count = map.get(cot);
				map.put(cot, ++count);
			} else {
				map.put(cot, 1);
			}
		}
	}
	
	public void writeMapToFile() throws IOException {
		Gson gson = new Gson();
		writeToFile(gson.toJson(this));
	}
	public void writeSortedMapToFile(HashMap<String, Integer> sortedMap) throws IOException {
		Gson gson = new Gson();
		writeToFile(gson.toJson(sortedMap));
	}

	public void writeDocFreqMapToFile(HashMap<String, Integer> sortedMap) throws IOException {
		Gson gson = new Gson();
		writeDocFreqToFile(gson.toJson(sortedMap));
	}

	public void writeTermFreqMapToFile(HashMap<String, Integer> sortedMap) throws IOException {
		Gson gson = new Gson();
		writeTermFreqToFile(gson.toJson(sortedMap));
	}
	
	public void writeTermDocFreqMapToFile(HashMap<String, Integer> sortedMap) throws IOException {
		Gson gson = new Gson();
		writeTermDocFreqToFile(gson.toJson(sortedMap));
	}
	
	public void writeMapToFile(Map<String, Integer> map, String thefilepath) throws IOException {
		Gson gson = new Gson();
		String text = gson.toJson(map);
		Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(getPath()+thefilepath), "UTF-8"));
		try {
			out.write(text);
		} finally {
			out.close();
		}
	}

	public void writeListToFile(List<String> list, String thefilepath) throws IOException {
		Gson gson = new Gson();
		String text = gson.toJson(list);
		Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(getPath()+thefilepath), "UTF-8"));
		try {
			out.write(text);
		} finally {
			out.close();
		}
	}

	public void writeSetToFile(Set<String> list, String thefilepath) throws IOException {
		Gson gson = new Gson();
		String text = gson.toJson(list);
		Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(getPath()+thefilepath), "UTF-8"));
		try {
			out.write(text);
		} finally {
			out.close();
		}
	}

	public void writeToFile(String text) throws IOException{
		Writer out = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(getPath()+folder + filename), "UTF-8"));
		try {
			out.write(text);
		} finally {
			out.close();
		}
	}
	
	public void writeDocFreqToFile(String text) throws IOException{
		Writer out = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(getPath()+folder + "allNounAdjectiveVervAdverbCotsDocFreq" + radius + ".json"), "UTF-8"));
		try {
			out.write(text);
		} finally {
			out.close();
		}
	}

	public void writeTermFreqToFile(String text) throws IOException{
		Writer out = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(getPath()+folder + "allNounAdjectiveVervAdverbCotsTermFreq" + radius + ".json"), "UTF-8"));
		try {
			out.write(text);
		} finally {
			out.close();
		}
	}
	
	public void writeTermDocFreqToFile(String text) throws IOException{
		Writer out = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(getPath()+folder + "allNounAdjectiveVervAdverbCotsTermDocFreq" + radius + ".json"), "UTF-8"));
		try {
			out.write(text);
		} finally {
			out.close();
		}
	}
	
	public void writeTFDFhashmaptofile(HashMap<String, CotCountTFDF> tfdfmap) throws IOException {
		Gson gson = new Gson();
		writeToFile(gson.toJson(tfdfmap));
	}
	
	public String getPath() {
	    String path = String.format("%s/%s", System.getProperty("user.dir"), this.getClass().getPackage().getName().replace(".", "/"));
	    path = path.substring(0, path.length()-4);
	    return path + "/ArticleResources/CoTs/";
	}

	public static String getStaticPath() {
		CoTCounter cc = new CoTCounter("cotsannotated.json", 0);
		String path = String.format("%s/%s", System.getProperty("user.dir"), cc.getClass().getPackage().getName().replace(".", "/"));
		path = path.substring(0, path.length()-4);
		return path + "/ArticleResources/CoTs/";
	}
	
	// Return the cots in a NewsArticleWithStemmedVersion object
	public HashMap<String, Integer> getCoTsWithCountForArticles(NewsArticleWithStemmedVersion nawsv) {
		HashMap<String, Integer> cotsMap = new HashMap<String, Integer>();
		for (int i = 0; i < nawsv.getAllPosTaggedWords().size();i++) {
			ArrayList<String> cots = getCoTsFromIndex(nawsv.getAllPosTaggedWords(), i);
			addCoTsToMap(cots);
		}
		return cotsMap;
	}
	
	public static int getNumberOfMatchingCoTsForArticles(NewsArticleWithStemmedVersion nawsv, int value, int radius) throws Exception {
		int counter = 0;
		Gson gson = new Gson();
		CoTCounter cc = new CoTCounter("cotsannotated.json", 0);
		cc = gson.fromJson(TextFileHandler.getCotsAnnotatedString(), cc.getClass());
		HashMap<String, Integer> annotatedMap = cc.map;
//		System.out.println(annotatedMap);
		
		HashMap<String, Integer> articleMap = cotCountArticles(nawsv, radius);
//		System.out.println(articleMap);
		for (String key : articleMap.keySet()) {
			if (annotatedMap.containsKey(key) && annotatedMap.get(key) == value) {
				counter++;
			}
		}
		return counter;
	}

	public static int getNumberOfPositiveCoTsForArticles(NewsArticleWithStemmedVersion nawsv, int radius) throws Exception {
		return getNumberOfMatchingCoTsForArticles(nawsv, 1, radius);
	}
	
	public static int getNumberOfNegativeCoTsForArticles(NewsArticleWithStemmedVersion nawsv, int radius) throws Exception {
		return getNumberOfMatchingCoTsForArticles(nawsv, -1, radius);
	}
	
	public static int getNumberOfNeutralCoTsForArticles(NewsArticleWithStemmedVersion nawsv, int radius) throws Exception {
		return getNumberOfMatchingCoTsForArticles(nawsv, 0, radius);
	}
	
	public String toString() {
		return map.toString();
	}
	
	public LinkedHashMap sortHashMapByValuesD(HashMap passedMap) {
		   List mapKeys = new ArrayList(passedMap.keySet());
		   List mapValues = new ArrayList(passedMap.values());
		   Collections.sort(mapValues);
		   Collections.sort(mapKeys);

		   LinkedHashMap sortedMap = new LinkedHashMap();

		   Iterator valueIt = mapValues.iterator();
		   while (valueIt.hasNext()) {
		       Object val = valueIt.next();
		       Iterator keyIt = mapKeys.iterator();

		       while (keyIt.hasNext()) {
		           Object key = keyIt.next();
		           String comp1 = passedMap.get(key).toString();
		           String comp2 = val.toString();

		           if (comp1.equals(comp2)){
		               passedMap.remove(key);
		               mapKeys.remove(key);
		               sortedMap.put((String)key, (Integer)val);
		               break;
		           }

		       }

		   }
		   return sortedMap;
	}
	
	//DOCUMENT FREQUENCY COT FINDER
	public HashMap<String, CotCountTFDF> generateDocumentFrequencyForCots(HashMap<String, Integer> tfCots) throws IOException{
		
		HashMap<String, CotCountTFDF> dfCots = new HashMap<String, CotCountTFDF>();
		HashMap<String, CotCountTFDF> newTFDFCots = new HashMap<String, CotCountTFDF>();
		
		JsonHandler jh = new JsonHandler("/ArticleSteps/4_StemmedArticles/MainDataSetStemmed.json", "stemmed");
		
		
//		System.out.println("HENTER TFCOT " + tfCots.get("i i"));
//		System.out.println("Lengde på tfcots " + tfCots.size());
	
		
		
		for (String key : tfCots.keySet()) {
		   //System.out.println("Key = " + key + " - " + tfCots.get(key));
		    CotCountTFDF cctfdf = new CotCountTFDF();
		    cctfdf.setTermFrequency(tfCots.get(key));
		    //System.out.println("TERM FREQUENCY :" + cctfdf.getTermFrequency());
		    dfCots.put(key, cctfdf);
		}
	

		System.out.println("HENTER dfCot " + dfCots.get("i i").getTermFrequency());
		
		for (NewsArticleWithStemmedVersion nawsv : jh.stemmedArticles.getNawsv()) {
			
			ArrayList<PosTaggedWord> ptwList = new ArrayList<PosTaggedWord>();
		
			ArrayList<String> currentArticleCots = new ArrayList<String>();
			
			if(nawsv.getPosTaggedTitle().getPosTaggedWords()!= null){
				for (PosTaggedWord posTaggedWord : nawsv.getPosTaggedTitle().getPosTaggedWords()) {
					ptwList.add(posTaggedWord);
				}
			}
			
			if(nawsv.getPosTaggedLeadText().getPosTaggedWords()!= null){
				for (PosTaggedWord posTaggedWord : nawsv.getPosTaggedLeadText().getPosTaggedWords()) {
					ptwList.add(posTaggedWord);
				}
			}
			
			//System.out.println("Størrelse " + ptwList.size());
			
			for (int i = 0; i < ptwList.size(); i++) {
				ArrayList<String> cots = getCoTsFromIndex(ptwList, i);

				for(int k = 0; k < cots.size(); k++){
					
					String currentCot = cots.get(k);
					
					if(!currentArticleCots.contains(currentCot)){
						
						//System.out.println("Den er ikke i denne artikkelens cots");
						//System.out.println("COT SOM GJELDER " + currentCot);
						//GET OBJECT FROM MAP
						CotCountTFDF oldcctfdf = dfCots.get(currentCot);
				
						
						CotCountTFDF newcctfdf = new CotCountTFDF();
						newcctfdf.termFrequency = oldcctfdf.getTermFrequency();
						int newDF = oldcctfdf.getDocumentFrequency()+1;
						newcctfdf.documentFrequency = newDF;
						//System.out.println("TF: " + newcctfdf.termFrequency + " DF: " + newcctfdf.documentFrequency);
						
						
							
						dfCots.put(currentCot, newcctfdf);
						
					}
					//ADD CURRENT COT TO ARTICLE COTS
					currentArticleCots.add(currentCot);
				}
	
			}
		}
		return dfCots;
	}
	
	private static String createMatlabFriendlyString(Map<Integer, Double> map) {
		String str = "[";
		for (Integer key : map.keySet()) {
			str += map.get(key) + " ";
		}
		return str.substring(0, str.length()-1) + "]";
	}
	
	public static void main(String[] args) throws Exception {
		
		main4();
		
//		main3();
		
//		CoTCounter cc = new CoTCounter(10);
//		cc.generateChiSquaredCots(10);
		
//		JsonHandler jh = new JsonHandler("/ArticleSteps/4_StemmedArticles/MainDataSetStemmed.json", "stemmed");
//		
//		HashMap<Integer, Integer> cotRcount = new HashMap<Integer, Integer>(); 
//		
//		for (int i = 2; i <= 29; i++) {
//			CoTCounter cc = new CoTCounter("cotsTFDF" + i + "test.json", i);
//			cc.cotCountArticlesNotMainText(jh);
////		
//			HashMap<String, Integer> sortedHashmap = new HashMap<String, Integer>();
//			Iterator it = cc.map.entrySet().iterator();
//			
//			
//			while (it.hasNext()) {
//				Map.Entry pairs = (Map.Entry)it.next();
////		        System.out.println(pairs.getKey() + " = " + pairs.getValue());
//				if(Integer.parseInt(pairs.getValue().toString()) > 0){
////		        	System.out.println("DSANT");
//					sortedHashmap.put(pairs.getKey().toString(), Integer.parseInt(pairs.getValue().toString()));	
//				}
//				it.remove(); // avoids a ConcurrentModificationException
//			}
//			
//			Gson g = new Gson();
//			
//			Type stringIntegergMap = new TypeToken<HashMap<String, Integer>>(){}.getType();
//			HashMap<String, Integer> tfcots = g.fromJson(TextFileHandler.getCots(), stringIntegergMap);
//			//System.out.println(tfcots.get("i i"));
//			
//			
////			cc.writeTFDFhashmaptofile(cc.generateDocumentFrequencyForCots(tfcots));
////			System.out.println(sortedHashmap);
//			System.out.println(String.format("[i=%s, count=%s]", i, sortedHashmap.size()));
//			cotRcount.put(i, sortedHashmap.size());
//			
////			String maxKey = ""; 
////			int maxCount = 0;
////			for (String key : sortedHashmap.keySet()) {
////				if (sortedHashmap.get(key) > maxCount) {
////					maxCount = sortedHashmap.get(key);
////					maxKey = key;
////				}
////			}
////			System.out.println(String.format("[cot=%s, count=%s ]", maxKey, maxCount));
//			
//			cc.writeSortedMapToFile(sortedHashmap);
//		}
//		
//		System.out.println(cotRcount);
//		Map<Integer, Double> cumulativeMap = createCumulativeMap(cotRcount);
//		System.out.println(cumulativeMap);
//		System.out.println(createMatlabFriendlyString(cumulativeMap));
		
		
//		cc.writeMapToFile();
		//System.out.println(getNumberOfPositiveCoTsForArticles(nawsv, 2));
		//System.out.println(getNumberOfNeutralCoTsForArticles(nawsv, 2));
		//System.out.println(getNumberOfNegativeCoTsForArticles(nawsv, 2));
	}
	
	public static void main4() throws Exception {
		JsonHandler jh = new JsonHandler("/ArticleSteps/4_StemmedArticles/MainDataSetStemmed.json", "stemmed");
		for (int i = 2; i <= 30; i++) {
			CoTCounter cc = new CoTCounter("allNounAdjectiveVervAdverbCotsTremFreq" + i + ".json", i);
			cc.cotCountArticlesNotMainTextNounAdjectiveVerbAdverb(jh);
			int counter = 0;
			for (String str : cc.map.keySet()) {
//				System.out.println(str);
				String term1 = str.split(" ")[0];
				String term2 = str.split(" ")[1];
//				System.out.println(term1);
//				System.out.println(term2);
				int dFcot = cc.mapDocFreq.get(str);
				if (dFcot > 1) {
					counter++;
				}
			}
			System.out.println("r=" + i + " --> " + counter);
		}
	}
	
	public static void main3() throws Exception {
//		CoTCounter cc = new CoTCounter(10);
//		cc.generateChiSquaredCots(10);
		
		JsonHandler jh = new JsonHandler("/ArticleSteps/4_StemmedArticles/MainDataSetStemmed.json", "stemmed");
		
		HashMap<Integer, Integer> cotRcount = new HashMap<Integer, Integer>(); 
		CoTCounter c2 = new CoTCounter(1);
		Set<String> allcotstobeannotated = new HashSet<String>();
		Set<String> allcotstobeannotated500 = new HashSet<String>();
		Set<String> allcotstobeannotated1000 = new HashSet<String>();
		Set<String> allcotstobeannotated2000 = new HashSet<String>();
		Set<String> allcotstobeannotated4000 = new HashSet<String>();
		for (int i = 2; i <= 10; i++) {
			System.out.println("r=" + i);
			CoTCounter cc = new CoTCounter("allNounAdjectiveVervAdverbCotsTremFreq" + i + ".json", i);
			cc.cotCountArticlesNotMainTextNounAdjectiveVerbAdverb(jh);
			cc.folder = "r="+i+"/";
//			cc.writeDocFreqMapToFile(cc.mapDocFreq);
//			cc.writeSortedMapToFile(cc.map);
			cc.termCountArticlesNotMainTextNounAdjectiveVerbAdverb(jh);
//		System.out.println("Term freq: " + cc.termFrequency);
//		System.out.println("Term doc freq: " + cc.docFrequency);
//			cc.writeTermFreqMapToFile(cc.termFrequency);
//			cc.writeTermDocFreqMapToFile(cc.docFrequency);
			int excludedcounter = 0;
			ArrayList<COTStat> cotstatlist = new ArrayList<COTStat>();
			for (String str : cc.map.keySet()) {
//				System.out.println(str);
				String term1 = str.split(" ")[0];
				String term2 = str.split(" ")[1];
//				System.out.println(term1);
//				System.out.println(term2);
				int tFcot = cc.map.get(str);
				int dFcot = cc.mapDocFreq.get(str);
				int tFterm1 = cc.termFrequency.get(term1) != null ? cc.termFrequency.get(term1) : 0;
				int dFterm1 = cc.docFrequency.get(term1) != null ? cc.docFrequency.get(term1) : 0;
				int tFterm2 = cc.termFrequency.get(term2) != null ? cc.termFrequency.get(term2) : 0;
				int dFterm2 = cc.docFrequency.get(term2) != null ? cc.docFrequency.get(term2) : 0;
//			System.out.println(String.format("%s, %s , %s, %s %s, %s , %s, %s", term1, term2, tFcot, dFcot, tFterm1, dFterm1, tFterm2, dFterm2));
				if ( (! (tFterm1 == 0 || dFterm1 == 0 || tFterm2 == 0 || dFterm2 == 0))) {
					if (dFcot > 1) {
						cotstatlist.add(new COTStat(term1, term2, tFcot, dFcot, tFterm1, dFterm1, tFterm2, dFterm2));
					} else {
						excludedcounter++;
					}
				} 
				System.out.println("Excluded counter=" + excludedcounter);
			}
			int N = cotstatlist.size();
			for (COTStat cotstat : cotstatlist) {
				cotstat.computeAllStats(N);
			}
			ArrayList<COTStat> tflist = copy(cotstatlist);
			ArrayList<COTStat> idflist = copy(cotstatlist);
			ArrayList<COTStat> tfidflist = copy(cotstatlist);
			ArrayList<COTStat> milist = copy(cotstatlist);
			ArrayList<COTStat> chilist = copy(cotstatlist);
			Collections.sort(tflist, new COTStatComparator("tf"));
			HashMap<Integer, Integer> freqCount = new HashMap<Integer, Integer>();
			for (COTStat cotStat : tflist) {
				if (freqCount.containsKey(cotStat.TFcot)) {
					freqCount.put(cotStat.TFcot, freqCount.get(cotStat.TFcot)+1);
				} else {
					freqCount.put(cotStat.TFcot, 1);
				}
			}
//			System.out.println(freqCount);
			Collections.sort(idflist, new COTStatComparator("idf"));
			Collections.sort(tfidflist, new COTStatComparator("tfidf"));
			Collections.sort(milist, new COTStatComparator("mi"));
			Collections.sort(chilist, new COTStatComparator("chi"));
//			for (COTStat cotstat : cotstatlist) {
//				System.out.println(cotstat.getStatString());
//			}
			Map<String, Integer> tfmap = new HashMap<String, Integer>();
			ValueComparator vcTF =  new ValueComparator(tfmap);
			TreeMap<String,Integer> tfmapSorted = new TreeMap<String,Integer>(vcTF);
			
			Map<String, Integer> idfmap = new HashMap<String, Integer>();
			ValueComparator vcIDF =  new ValueComparator(idfmap);
			TreeMap<String,Integer> idfmapSorted = new TreeMap<String,Integer>(vcIDF);
			
			Map<String, Integer> tfidfmap = new HashMap<String, Integer>();
			ValueComparator vcTFIDF =  new ValueComparator(tfidfmap);
			TreeMap<String,Integer> tfidfmapSorted = new TreeMap<String,Integer>(vcTFIDF);
			
			Map<String, Integer> mimap = new HashMap<String, Integer>();
			ValueComparator vcMI =  new ValueComparator(mimap);
			TreeMap<String,Integer> mimapSorted = new TreeMap<String,Integer>(vcMI);
			
			Map<String, Integer> chimap = new HashMap<String, Integer>();
			ValueComparator vcCHI =  new ValueComparator(chimap);
			TreeMap<String,Integer> chimapSorted = new TreeMap<String,Integer>(vcCHI);
			
			for (int j = 0; j < 4000; j++) {
				if (j >= tflist.size() || j >= idflist.size() || j >= tfidflist.size() || j >= milist.size() || j >= chilist.size()) {
					break;
				}
				System.out.println(j);
				String tfcot = tflist.get(j).term1+" "     +tflist.get(j).term2;
				String idfcot = idflist.get(j).term1+" "    +idflist.get(j).term2;
				String tfidfcot = tfidflist.get(j).term1+ " " +tfidflist.get(j).term2;
				String micot = milist.get(j).term1+" "     +milist.get(j).term2;
				String chicot = chilist.get(j).term1+" "    +chilist.get(j).term2;
				if (0 <= j && j < 500) {
					allcotstobeannotated500.add(tfcot);
					allcotstobeannotated500.add(idfcot);
					allcotstobeannotated500.add(tfidfcot);
					allcotstobeannotated500.add(micot);
					allcotstobeannotated500.add(chicot);
				} else if (500 <= j && j < 1000) {
						allcotstobeannotated1000.add(tfcot);
						allcotstobeannotated1000.add(idfcot);
						allcotstobeannotated1000.add(tfidfcot);
						allcotstobeannotated1000.add(micot);
						allcotstobeannotated1000.add(chicot);
				} else if (1000 <= j && j < 2000) {
						allcotstobeannotated2000.add(tfcot);
						allcotstobeannotated2000.add(idfcot);
						allcotstobeannotated2000.add(tfidfcot);
						allcotstobeannotated2000.add(micot);
						allcotstobeannotated2000.add(chicot);
				} else if (2000 <= j && j < 4000) {
					allcotstobeannotated4000.add(tfcot);
					allcotstobeannotated4000.add(idfcot);
					allcotstobeannotated4000.add(tfidfcot);
					allcotstobeannotated4000.add(micot);
					allcotstobeannotated4000.add(chicot);
				}
				allcotstobeannotated.add(tfcot);
				allcotstobeannotated.add(idfcot);
				allcotstobeannotated.add(tfidfcot);
				allcotstobeannotated.add(micot);
				allcotstobeannotated.add(chicot);
				tfmap.put(tfcot, j+1);
				idfmap.put(idfcot, j+1);
				tfidfmap.put(tfidfcot, j+1);
				mimap.put(micot, j+1);
				chimap.put(chicot, j+1);
			}
			// put all into sorted maps
			tfmapSorted.putAll(tfmap);
			idfmapSorted.putAll(idfmap);
			tfidfmapSorted.putAll(tfidfmap);
			mimapSorted.putAll(mimap);
			chimapSorted.putAll(chimap);
			
//			// write all lists to file
			cc.writeMapToFile(tfmapSorted, "r="+i+"/tf.json");
			cc.writeMapToFile(idfmapSorted, "r="+i+"/idf.json");
			cc.writeMapToFile(tfidfmapSorted, "r="+i+"/tfidf.json");
			cc.writeMapToFile(mimapSorted, "r="+i+"/mi.json");
			cc.writeMapToFile(chimapSorted, "r="+i+"/chi.json");
		}
		// Remove duplicates:
		// 500s present in 1000 and 2000
		for (String key : allcotstobeannotated500) {
			if (allcotstobeannotated1000.contains(key)) {
				allcotstobeannotated1000.remove(key);
			}
			if (allcotstobeannotated2000.contains(key)) {
				allcotstobeannotated2000.remove(key);
			}
			if (allcotstobeannotated4000.contains(key)) {
				allcotstobeannotated4000.remove(key);
			}
		}
		
		for (String key : allcotstobeannotated1000) {
			if (allcotstobeannotated2000.contains(key)) {
				allcotstobeannotated2000.remove(key);
			}
			if (allcotstobeannotated4000.contains(key)) {
				allcotstobeannotated4000.remove(key);
			}
		}
		
		for (String key : allcotstobeannotated2000) {
			if (allcotstobeannotated4000.contains(key)) {
				allcotstobeannotated4000.remove(key);
			}
		}
		Map<String, Integer> allcotstobeannotatedMap500 = new HashMap<String, Integer>();
		Map<String, Integer> allcotstobeannotatedMap1000 = new HashMap<String, Integer>();
		Map<String, Integer> allcotstobeannotatedMap2000 = new HashMap<String, Integer>();
		Map<String, Integer> allcotstobeannotatedMap4000 = new HashMap<String, Integer>();
		Map<String, Integer> allcotstobeannotatedMap = new HashMap<String, Integer>();
		for (String str : allcotstobeannotated500) {
			allcotstobeannotatedMap500.put(str, 0);
		}
		
		for (String str : allcotstobeannotated1000) {
			allcotstobeannotatedMap1000.put(str, 0);
		}
		
		for (String str : allcotstobeannotated2000) {
			allcotstobeannotatedMap2000.put(str, 0);
		}

		for (String str : allcotstobeannotated4000) {
			allcotstobeannotatedMap4000.put(str, 0);
		}

		for (String str : allcotstobeannotated) {
			allcotstobeannotatedMap.put(str, 0);
		}
		
		int duplicates = 0;
		for (String key : allcotstobeannotated500) {
			if (allcotstobeannotated1000.contains(key)) {
				duplicates++;
			}
			if (allcotstobeannotated2000.contains(key)) {
				duplicates++;
			}
			if (allcotstobeannotated4000.contains(key)) {
				duplicates++;
			}
		}
		for (String key : allcotstobeannotated1000) {
			if (allcotstobeannotated500.contains(key)) {
				duplicates++;
			}
			if (allcotstobeannotated2000.contains(key)) {
				duplicates++;
			}
			if (allcotstobeannotated4000.contains(key)) {
				duplicates++;
			}
		}
		for (String key : allcotstobeannotated2000) {
			if (allcotstobeannotated500.contains(key)) {
				duplicates++;
			}
			if (allcotstobeannotated1000.contains(key)) {
				duplicates++;
			}
			if (allcotstobeannotated4000.contains(key)) {
				duplicates++;
			}
		}
		for (String key : allcotstobeannotated4000) {
			if (allcotstobeannotated500.contains(key)) {
				duplicates++;
			}
			if (allcotstobeannotated1000.contains(key)) {
				duplicates++;
			}
			if (allcotstobeannotated2000.contains(key)) {
				duplicates++;
			}
		}
		System.out.println("Duplicates="+duplicates);
		
		c2.writeMapToFile(allcotstobeannotatedMap500, "allcotstobeannotated500.json");
		c2.writeMapToFile(allcotstobeannotatedMap1000, "allcotstobeannotated1000.json");
		c2.writeMapToFile(allcotstobeannotatedMap2000, "allcotstobeannotated2000.json");
		c2.writeMapToFile(allcotstobeannotatedMap4000, "allcotstobeannotated4000.json");
		c2.writeMapToFile(allcotstobeannotatedMap, "allcotstobeannotated.json");
		
		System.out.println("All done!");
	}
	
	private static ArrayList<COTStat> copy(ArrayList<COTStat> inlist) {
		ArrayList<COTStat> retlist = new ArrayList<COTStat>();
		for (COTStat cotStat : inlist) {
			retlist.add(cotStat);
		}
		return retlist;
	}
	
	public static void main2() throws Exception {
//		CoTCounter cc = new CoTCounter(10);
//		cc.generateChiSquaredCots(10);
		
		JsonHandler jh = new JsonHandler("/ArticleSteps/4_StemmedArticles/MainDataSetStemmed.json", "stemmed");
		
		HashMap<Integer, Integer> cotRcount = new HashMap<Integer, Integer>(); 
		
//		for (int i = 2; i <= 10; i++) {
			int i = 2;
			CoTCounter cc = new CoTCounter("allNounAdjectiveVervAdverbCotsTremFreq" + i + ".json", i);
			cc.cotCountArticlesNotMainTextNounAdjectiveVerbAdverb(jh);
			cc.folder = "r="+i+"/";
			cc.writeDocFreqMapToFile(cc.mapDocFreq);
			cc.writeSortedMapToFile(cc.map);
			cc.termCountArticlesNotMainTextNounAdjectiveVerbAdverb(jh);
			System.out.println("Term freq: " + cc.termFrequency);
			System.out.println("Term doc freq: " + cc.docFrequency);
			cc.writeTermFreqMapToFile(cc.termFrequency);
			cc.writeTermDocFreqMapToFile(cc.docFrequency);
			ArrayList<COTStat> cotstatlist = new ArrayList<COTStat>();
//			for (String str : cc.map.keySet()) {
//				String term1 = str.split(" ")[0];
//				String term2 = str.split(" ")[1];
//				int tFcot = cc.map.get(str);
//				int dFcot = cc.mapDocFreq.get(str);
//				int tFterm1 = cc.termFrequency.get(term1);
//				int dFterm1 = cc.docFrequency.get(term1);
//				int tFterm2 = cc.termFrequency.get(term2);
//				int dFterm2 = cc.docFrequency.get(term2);
//				cotstatlist.add(new COTStat(term1, term2, tFcot, dFcot, tFterm1, dFterm1, tFterm2, dFterm2));
//			}
			
			System.out.println(cc.map.size() + ": " + cc.map);
			System.out.println(cc.mapDocFreq.size() + ": " + cc.mapDocFreq);
			int countMapGreater = 0;
			int countEqual = 0;
			for (String key : cc.map.keySet()) {
				Integer mapInt = cc.map.get(key);
				Integer mapDocFrqInt = cc.mapDocFreq.get(key);
				if (mapInt > mapDocFrqInt) {
					countMapGreater++;
				} else if (mapInt == mapDocFrqInt) {
					countEqual++;
				} else {
					System.out.println(String.format("Error: %s has count %s in map and %s in mapdocfeq", key, mapInt, mapDocFrqInt));
				}
			}
			System.out.println(String.format("countMapGreater=%s, countEqual=%s", countMapGreater, countEqual));
			HashMap<String, Integer> sortedHashmap = new HashMap<String, Integer>();
//			Iterator it = cc.map.entrySet().iterator();
//			
//			
//			while (it.hasNext()) {
//				Map.Entry pairs = (Map.Entry)it.next();
////		        System.out.println(pairs.getKey() + " = " + pairs.getValue());
//				if(Integer.parseInt(pairs.getValue().toString()) > 0){
////		        	System.out.println("DSANT");
//					sortedHashmap.put(pairs.getKey().toString(), Integer.parseInt(pairs.getValue().toString()));	
//				}
//				it.remove(); // avoids a ConcurrentModificationException
//			}
			
			Gson g = new Gson();
			
			Type stringIntegergMap = new TypeToken<HashMap<String, Integer>>(){}.getType();
			HashMap<String, Integer> tfcots = g.fromJson(TextFileHandler.getCots(), stringIntegergMap);
			//System.out.println(tfcots.get("i i"));
			
			
//			cc.writeTFDFhashmaptofile(cc.generateDocumentFrequencyForCots(tfcots));
//			System.out.println(sortedHashmap);
			System.out.println(String.format("[i=%s, count=%s]", i, sortedHashmap.size()));
			cotRcount.put(i, sortedHashmap.size());
			
//			String maxKey = ""; 
//			int maxCount = 0;
//			for (String key : sortedHashmap.keySet()) {
//				if (sortedHashmap.get(key) > maxCount) {
//					maxCount = sortedHashmap.get(key);
//					maxKey = key;
//				}
//			}
//			System.out.println(String.format("[cot=%s, count=%s ]", maxKey, maxCount));

//		}
		
//		System.out.println(cotRcount);
//		Map<Integer, Double> cumulativeMap = createCumulativeMap(cotRcount);
//		System.out.println(cumulativeMap);
//		System.out.println(createMatlabFriendlyString(cumulativeMap));
		
		
//		cc.writeMapToFile();
		//System.out.println(getNumberOfPositiveCoTsForArticles(nawsv, 2));
		//System.out.println(getNumberOfNeutralCoTsForArticles(nawsv, 2));
		//System.out.println(getNumberOfNegativeCoTsForArticles(nawsv, 2));
	}
	private static Map<Integer, Double> createCumulativeMap(
			HashMap<Integer, Integer> cotRcount) {
		Map<Integer, Double> retmap = new TreeMap<Integer, Double>();
		int maxKey = 0;
		double maxCount = 0;
		for (Integer key : cotRcount.keySet()) {
			if (key > maxKey) {
				maxKey = key;
				maxCount = cotRcount.get(key);
			}
		}
		for (Integer key : cotRcount.keySet()) {
			retmap.put(key, cotRcount.get(key) / maxCount);
		}
		return retmap;
	}
}