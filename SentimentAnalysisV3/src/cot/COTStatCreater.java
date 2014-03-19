package cot;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import preProcessing.NewsArticleWithStemmedVersion;
import preProcessing.PosTaggedWord;
import preProcessing.TextFileHandler;
import utils.Stemmer;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import featureExctraction.WordCountList;
import newsAPI.JsonHandler;

public class COTStatCreater {
	
//	public String term1;
//	public String term2;
//	
//	public int TFcot;
//	public int DFcot;
//	
//	public int TFterm1;
//	public int DFterm1;
//
//	public int TFterm2;
//	public int DFterm2;
//	
//	public double tf;
//	public double idf;
//	public double tfidf;
//	public double mi;
//	public double chi;
	
	int radius;
	ArrayList<COTStat> fullcotlist = new ArrayList<COTStat>(); 
	JsonHandler jsonhandler;

	public COTStatCreater(int radius) throws Exception {
		this.radius = radius;
		jsonhandler = new JsonHandler("/ArticleSteps/4_StemmedArticles/MainDataSetStemmed.json", "stemmed");
	}
	
	public String getPath(int r) {
	    String path = String.format("%s/%s", System.getProperty("user.dir"), this.getClass().getPackage().getName().replace(".", "/"));
	    return path.split(this.getClass().getPackage().getName())[0]+"ArticleResources/CoTs/r=" + r + "/";
	}
	
	public String getCotListFilePath(int r) {
		return getPath(r) + "allNounAdjectiveVervAdverbCotsTremFreq" + r + ".json";
	}

	public String getCotListFilePathDocFreq(int r) {
		return getPath(r) + "allNounAdjectiveVervAdverbCotsDocFreq" + r + ".json";
	}
	
	public void writeCotListToFile() throws IOException {
		Gson gson = new Gson();
		writeToFile(gson.toJson(fullcotlist));
	}
	
	public void writeToFile(String text) throws IOException{
		Writer out = new BufferedWriter(new OutputStreamWriter(
			new FileOutputStream(getPath(radius) + "cotstatfull.json"), "UTF-8"));
			try {
			    out.write(text);
			} finally {
			    out.close();
			}
	}
	
	public void initializeAll() throws Exception {
//		initializeTFcots(radius);

//		initializeDFcots();
		initializeTFterms();
		initializeDFterms();
		
//		initializeTFterm1s(radius);
//		initializeDFterm1s(radius);
//		initializeTFterm2s(radius);
//		initializeDFterm2s(r);
	}
	
	public void initializeTFcots() throws Exception {
		// Do nothing
		System.out.println("TF");
		int counter = 1;
		int found = 0;
		int notfound = 0;
		for (COTStat cot : fullcotlist) {
			if (counter % 1000 == 0) {
				System.out.println("   " + counter);
			}
			counter++;
			for (NewsArticleWithStemmedVersion nawsv : jsonhandler.stemmedArticles.getNawsv()) {
				ArrayList<String> articleCotList = getCotsForArticle(nawsv);
				for (String articleCot : articleCotList) {
					if ((cot.term1 + " " + cot.term2).equals(articleCot)) {
						cot.TFcot++;
					}
				}
			}
			if (cot.TFcot > 0.0) {
				found++;
			} else {
				notfound++;
			}
		}
		System.out.println(String.format("Found=%s, Not found=%s", found, notfound));
	}

	public void initializeTFcots2() throws Exception {
		// Do nothing
		System.out.println("DF");
		int counter = 1;
		int found = 0;
		int notfound = 0;
		for (COTStat cot : fullcotlist) {
			if (counter % 1000 == 0) {
				System.out.println("   " + counter);
			}
			counter++;
			for (NewsArticleWithStemmedVersion nawsv : jsonhandler.stemmedArticles.getNawsv()) {
				ArrayList<String> articleCotList = getCotsForArticle(nawsv);
				for (String articleCot : articleCotList) {
					if ((cot.term1 + " " + cot.term2).equals(articleCot)) {
						cot.DFcot++;
						break;
					}
				}
			}
			if (cot.DFcot > 0.0) {
				found++;
			} else {
				notfound++;
			}
		}
		System.out.println(String.format("Found=%s, Not found=%s", found, notfound));
	}

	public void initializeDFcots() throws Exception {
		// go through docs and count
		System.out.println("DF");
		int counter = 1;
		int found = 0;
		int notfound = 0;
		for (COTStat cot : fullcotlist) {
			if (counter % 1000 == 0) {
				System.out.println("COT: " + counter);
			}
			counter++;
			for (NewsArticleWithStemmedVersion nawsv : jsonhandler.stemmedArticles.getNawsv()) {
				ArrayList<String> articleCotList = getCotsForArticle(nawsv);
				int i = 0;
				boolean flag = false;
				while (i < articleCotList.size() && flag == false) {
					String articleCot = articleCotList.get(i);
					if ((cot.term1 + " " + cot.term2).equals(articleCot)) {
						cot.DFcot++;
						flag = true;
					}
					i++;
//				if (articleCotList.contains((cot.term1 + " " + cot.term2))) {
//					cot.DFcot++;
//					System.out.println("JADA");
//				}
				}
			}
			if (cot.DFcot > 0.0) {
				found++;
			} else {
				notfound++;
			}
		}
		System.out.println(String.format("Found=%s, Not found=%s", found, notfound));
	}
	
	public void initializeTFterms() throws Exception {
		// go through docs and count
		System.out.println("TFterms");
		int counter = 1;
		for (COTStat cot : fullcotlist) {
			if (counter % 1000 == 0) {
				System.out.println("COT: " + counter);
			}
			counter++;
			for (NewsArticleWithStemmedVersion nawsv : jsonhandler.stemmedArticles.getNawsv()) {
			ArrayList<String> articleTermList = getTermsForArticle(nawsv);
				for (String articleTerm : articleTermList) {
					if (cot.term1.equals(articleTerm)) {
						cot.TFterm1++;
//						System.out.println("JADA");
					}
					if (cot.term2.equals(articleTerm)) {
						cot.TFterm2++;
					}
				}
			}
		}
	}
	
	public void initializeDFterms() throws Exception {
		// go through docs and count
		System.out.println("DFterm1");
		int counter = 1;
		for (COTStat cot : fullcotlist) {
			if (counter % 100 == 0) {
				System.out.println("COT: " + counter);
			}
			counter++;
			for (NewsArticleWithStemmedVersion nawsv : jsonhandler.stemmedArticles.getNawsv()) {
				ArrayList<String> articleTermList = getTermsForArticle(nawsv);
				if (articleTermList.contains(cot.term1)) {
					cot.DFterm1++;
//					System.out.println("JADA");
				}
				if (articleTermList.contains(cot.term2)) {
					cot.DFterm2++;
//					System.out.println("JADA");
				}
				
			}
		}
//		for (COTStat cot : fullcotlist) {
//			if (cot.DFterm1 == 0 || cot.DFterm2 == 0) {
//				System.out.println(cot.term1 + " and " + cot.term2);
//			}
//		}
//		System.out.println("Finished!");
	}
	
	public void initializeTFterm1s() throws Exception {
		// go through docs and count
		System.out.println("TFterm1");
		int counter = 1;
		for (NewsArticleWithStemmedVersion nawsv : jsonhandler.stemmedArticles.getNawsv()) {
			if (counter % 100 == 0) {
				System.out.println("Article: " + counter);
			}
			counter++;
			ArrayList<String> articleTermList = getTermsForArticle(nawsv);
			for (COTStat cot : fullcotlist) {
				for (String articleTerm : articleTermList) {
					if (cot.term1.equals(articleTerm)) {
						cot.TFterm1++;
//						System.out.println("JADA");
					}
				}
			}
		}
	}
	
	public void initializeDFterm1s() throws Exception {
		// go through docs and count
		System.out.println("DFterm1");
		int counter = 1;
		for (NewsArticleWithStemmedVersion nawsv : jsonhandler.stemmedArticles.getNawsv()) {
			if (counter % 100 == 0) {
				System.out.println("Article: " + counter);
			}
			counter++;
			ArrayList<String> articleTermList = getTermsForArticle(nawsv);
			for (COTStat cot : fullcotlist) {
				if (articleTermList.contains(cot.term1)) {
					cot.DFterm1++;
//					System.out.println("JADA");
				}
			}
		}
	}

	public void initializeTFterm2s() throws Exception {
		// go through docs and count
		System.out.println("TFterm2");
		int counter = 1;
		for (NewsArticleWithStemmedVersion nawsv : jsonhandler.stemmedArticles.getNawsv()) {
			if (counter % 100 == 0) {
				System.out.println("Article: " + counter);
			}
			counter++;
			ArrayList<String> articleTermList = getTermsForArticle(nawsv);
			for (COTStat cot : fullcotlist) {
				if (articleTermList.contains(cot.term1)) {
					cot.DFterm1++;
//					System.out.println("JADA");
				}
			}
		}
	}
	
	public void initializeDFterm2s() throws Exception {
		// go through docs and count
		System.out.println("DFterm2");
		int counter = 1;
		for (NewsArticleWithStemmedVersion nawsv : jsonhandler.stemmedArticles.getNawsv()) {
			if (counter % 100 == 0) {
				System.out.println("Article: " + counter);
			}
			counter++;
			ArrayList<String> articleTermList = getTermsForArticle(nawsv);
			for (COTStat cot : fullcotlist) {
				if (articleTermList.contains(cot.term1)) {
					cot.DFterm1++;
//					System.out.println("JADA");
				}
			}
		}
	}
	
	public HashMap<String, Integer> loadCotFileIntoCotList() throws Exception {
		Gson gson = new Gson();
		Type cotHashMap = new TypeToken<HashMap<String, Integer>>(){}.getType();
		TextFileHandler tfh = new TextFileHandler();
		String jsonFilRefStr = tfh.getTextFileAsString(getCotListFilePath(radius), StandardCharsets.UTF_8);
		HashMap<String, Integer> cotList = gson.fromJson(jsonFilRefStr, cotHashMap);
		return cotList;
	}
	
	public HashMap<String, Integer> loadCotFileIntoCotListDF() throws Exception {
		Gson gson = new Gson();
		Type cotHashMap = new TypeToken<HashMap<String, Integer>>(){}.getType();
		TextFileHandler tfh = new TextFileHandler();
		String jsonFilRefStr = tfh.getTextFileAsString(getCotListFilePathDocFreq(radius), StandardCharsets.UTF_8);
		HashMap<String, Integer> cotList = gson.fromJson(jsonFilRefStr, cotHashMap);
		return cotList;
	}
	
	
	
	// Includes term frequency
	public void initializeCotList(HashMap<String, Integer> cotList) {
		for (String key : cotList.keySet()) {
			String[] terms = key.split(" ");
			String term1 = Stemmer.stemSimple(terms[0]);
			String term2 = Stemmer.stemSimple(terms[1]);
			fullcotlist.add(new COTStat(term1, term2, cotList.get(key)));
		}
	}

	// Includes term frequency and document frequency
	public void initializeCotList(HashMap<String, Integer> termFreqList, HashMap<String, Integer> docFreqList) {
		for (String key : termFreqList.keySet()) {
			String[] terms = key.split(" ");
			String term1 = Stemmer.stemSimple(terms[0]);
			String term2 = Stemmer.stemSimple(terms[1]);
			fullcotlist.add(new COTStat(term1, term2, termFreqList.get(key), docFreqList.get(key)));
		}
	}
	
	// Extract all cots from both title and lead of article
	public ArrayList<String> getCotsForArticle(NewsArticleWithStemmedVersion nawsv) {
		ArrayList<String> articleCotList = new ArrayList<String>();
		
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
		
		ArrayList<String> permittedWordclasses = new ArrayList<String>(Arrays.asList("subst", "adj", "verb", "adv"));
		for (int i = 0; i < ptwList.size();i++) {
			String wordclass = ptwList.get(i).wordclass;
			if (permittedWordclasses.contains(wordclass)) {
//				System.out.println("Contains: " + wordclass);
				articleCotList.addAll(getCoTsFromIndexNounAdjectiveVerbAdverb(ptwList, i));
			} //else {
//				System.out.println("Does not contain: " + wordclass);
//			}
		}
		return articleCotList;
	}
	
	// Extract all cots from index i within radius r of list
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

	// Extract all terms from both title and lead of article
	public ArrayList<String> getTermsForArticle(NewsArticleWithStemmedVersion nawsv) {
		ArrayList<String> articleCotList = new ArrayList<String>();
		
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
		
		ArrayList<String> permittedWordclasses = new ArrayList<String>(Arrays.asList("subst", "adj", "verb", "adv"));
		for (int i = 0; i < ptwList.size();i++) {
			String wordclass = ptwList.get(i).wordclass;
			if (permittedWordclasses.contains(wordclass)) {
//				System.out.println("Contains: " + wordclass);
				articleCotList.add(ptwList.get(i).stem);
			} //else {
//				System.out.println("Does not contain: " + wordclass);
//			}
		}
		return articleCotList;
	}
	
	public static void computeAndWriteAllFiles() throws Exception{
		for (int i = 2; i <= 10;i++) {
			COTStatCreater csc = new COTStatCreater(i);
			HashMap<String, Integer> map = csc.loadCotFileIntoCotList();
			csc.initializeCotList(map);
			csc.initializeAll();
			csc.writeCotListToFile();
		}
	}
	
	public static void main(String[] args) throws Exception {
		COTStatCreater csc = new COTStatCreater(2);
		HashMap<String, Integer> map = csc.loadCotFileIntoCotList();
		HashMap<String, Integer> mapDocFreq = csc.loadCotFileIntoCotListDF();
		csc.initializeCotList(map, mapDocFreq);
		csc.initializeAll();
		csc.writeCotListToFile();
		
//		csc.initializeTFcots(2);
	}
}