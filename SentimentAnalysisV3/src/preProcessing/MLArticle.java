package preProcessing;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataPair;
import org.encog.ml.data.basic.BasicMLDataSet;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import machineLearning.MLMainClassPaperII;
import newsAPI.JsonHandler;

public class MLArticle {
	
	public double positiveCotsTitle;
	public double negativeCotsTitle;
	public double neutralCotsTitle;

	public double positiveCotsLead;
	public double negativeCotsLead;
	public double neutralCotsLead;
	
	public double isMonday; //unused
	public double isTuesday; //unused
	public double isWednesday; //unused
	public double isThursday; //unused
	public double isFriday; //unused
	public double isSaturday; //unused
	public double isSunday; //unused
	
	public double isWeekday;
	public double isWeekend;
	
	public double isStockExchangeOpen;
	
	public double isBors;
	public double isAnalyse;
	public double isOkonomi;
	
	public double lengthOfTitle;
	public double lengthOfLead;
	
	public double positiveTitleClueCount;
	public double negativeTitleClueCount;
	public double hasPositiveTitleClue;
	public double hasNegativeTitleClue;
	
	public double sentimentClassification;
	
	public MLArticle() {
		
	}

	public MLArticle(NewsArticleWithStemmedVersion nawsv, int radius, String function, int sigma) throws Exception {
		ArrayList<Double> titleList = getPosNegNeuCotCount(cotCountArticlesTitleNounAdjectiveVerbAdverb(nawsv, radius), radius, function, sigma);
		positiveCotsTitle = titleList.get(0);
		negativeCotsTitle = titleList.get(1);
		neutralCotsTitle = titleList.get(2);

		ArrayList<Double> leadList = getPosNegNeuCotCount(cotCountArticlesTitleNounAdjectiveVerbAdverb(nawsv, radius), radius, function, sigma);
		positiveCotsLead = leadList.get(0);
		negativeCotsLead = leadList.get(1);
		neutralCotsLead = leadList.get(2);
		
		setDate(nawsv.published);
		
		isBors = getBors(nawsv);
		isAnalyse = getAnalyse(nawsv);
		isOkonomi = getOkonomi(nawsv);
		
		setLengthOfTitleAndLead(nawsv);
		
		setUpTitleClues(nawsv);
		
		sentimentClassification = extractAggregateSentimentFromString(nawsv.getSentimentValue());
	}
	
	private void setDate(String dateStr) {
		String yearStr = dateStr.substring(0,4);
		String monthStr = dateStr.substring(5,7);
		String dayStr = dateStr.substring(8, 10);
		String hourStr = dateStr.substring(11,13);
		String minStr = dateStr.substring(14,16);
		
		int year = Integer.parseInt(yearStr) - 1900;
		int month = Integer.parseInt(monthStr) - 1;
		int day = Integer.parseInt(dayStr) - 1;
		int hour = Integer.parseInt(hourStr);
		int min = Integer.parseInt(minStr);
		
		Date date = new Date(year, month, day);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		isMonday = 0.0;
		isTuesday = 0.0;
		isWednesday = 0.0;
		isThursday = 0.0;
		isFriday = 0.0;
		isSaturday = 0.0;
		isSunday  = 0.0;
		if (dayOfWeek == 1) {
			isMonday = 1.0;
		} else if (dayOfWeek == 2) {
			isTuesday = 1.0;
		} else if (dayOfWeek == 3) {
			isWednesday = 1.0;
		} else if (dayOfWeek == 4) {
			isThursday = 1.0;
		} else if (dayOfWeek == 5) {
			isFriday = 1.0;
		} else if (dayOfWeek == 6) {
			isSaturday = 1.0;
		} else if (dayOfWeek == 7) {
			isSunday  = 1.0;
		} else {
			throw new IllegalStateException("Illegal week day!");
		}
		
		
		if (isSaturday == 1.0 || isSunday == 1.0) {
			isWeekend = 1.0;
		} else if (isMonday == 1.0 || isTuesday == 1.0 || isWednesday == 1.0 || isThursday == 1.0 || isFriday == 1.0) {
			isWeekday = 1.0;
		} else {
			throw new IllegalStateException("Neither weekend nor weekday!");
		}
		// OSX open between 0900 and 1620 weekdays
		if (isWeekday == 1.0 && 9 <= hour && hour <= 16) {
			if (hour == 16) {
				if (min <= 20) {
					isStockExchangeOpen = 1.0;
				} else {
					isStockExchangeOpen = 0.0;
				}
			} else {
				isStockExchangeOpen = 1.0;
			}
		} else {
			isStockExchangeOpen = 0.0;
		}
	}
	
	public static void main(String[] args) throws Exception {

		JsonHandler jh = new JsonHandler("/ArticleSteps/4_StemmedArticles/MainDataSetStemmed.json", "stemmed");
		int nullcounter = 0;
		for (NewsArticleWithStemmedVersion nawsv : jh.stemmedArticles.getNawsv()) {
			MLArticle mla = new MLArticle(nawsv, 2, "chi", 4000);
//			ArrayList<Double> list = getPosNegNeuCotCount(cotCountArticlesTitleNounAdjectiveVerbAdverb(nawsv, 6), 6, "chi", 4000);
//			ArrayList<Double> list2 = getPosNegNeuCotCount(cotCountArticlesLeadNounAdjectiveVerbAdverb(nawsv, 6), 6, "chi", 4000);
//			if (list.get(0) == 0.0 && list.get(1) == 0.0 && list.get(2) == 0.0 && list2.get(0) == 0.0 && list2.get(1) == 0.0 && list2.get(2) == 0.0) {
//				nullcounter++;
//			}
//			System.out.println("Title:" + cotCountArticlesTitleNounAdjectiveVerbAdverb(nawsv, 2));
//			System.out.println("Lead:" + cotCountArticlesLeadNounAdjectiveVerbAdverb(nawsv, 2));
		}
//		System.out.println(nullcounter);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(nullcounter, nullcounter, nullcounter));
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
	}
	
	public static ArrayList<Double> getPosNegNeuCotCount(Map<String, Integer> map, int radius, String function, int sigma) throws Exception {
		ArrayList<Double> cotCount = new ArrayList<Double>();
		double posCount = 0.0;
		double negCount = 0.0;
		double neuCount = 0.0;
		HashMap<String, Integer> annotations = loadAllCotAnnotations();
		HashMap<String, Integer> legalannotations = loadLegalCotAnnotations(radius, function, sigma);
		legalannotations = mergeAnnotations(annotations, legalannotations);
		for (String key : map.keySet()) {
			if (legalannotations.get(key) != null) {
				int classification = legalannotations.get(key);
				if (classification == 1) {
					posCount++;
				} else if (classification == -1) {
					negCount++;
				} else if (classification == 0) {
					neuCount++;
				} else {
					System.err.println("Unknown classification: " + classification);
				}
				
			}
		}
		
//		System.out.println(annotations);
//		int poscount = 0;
//		int negcount = 0;
//		int neucount = 0;
//		for (String key : annotations.keySet()) {
//			int classification = annotations.get(key);
//			if (classification == 1) {
//				poscount++;
//			} else if (classification == -1) {
//				negcount++;
//			} else if (classification == 0) {
//				neucount++;
//			} else {
//				System.out.println("None of the above:-s");
//			}
//		}
//		System.out.println("Pos = " + poscount);
//		System.out.println("Neg = " + negcount);
//		System.out.println("Neu = " + neucount);
//		System.out.println(legalannotations);
//		System.out.println(legalannotations.size());
		cotCount.add(posCount); // positive count
		cotCount.add(negCount); // negative count
		cotCount.add(neuCount); // neutral count
		return cotCount;
	}
	
	public static HashMap<String, Integer> mergeAnnotations(HashMap<String, Integer> annotations, HashMap<String, Integer> legalannotations) {
		HashMap<String, Integer> retannotations = new HashMap<String, Integer>();
		for (String key : legalannotations.keySet()) {
			retannotations.put(key, annotations.get(key));
		}
		return retannotations;
	}
	
	public static HashMap<String, Integer> loadAllCotAnnotations() throws Exception {
		Gson g = new Gson();
		Type stringIntegergMap = new TypeToken<HashMap<String, Integer>>(){}.getType();
		MLArticle mla = new MLArticle();
		String path = String.format("%s/%s", System.getProperty("user.dir"), mla.getClass().getPackage().getName().replace(".", "/"));
	    path = path.split(mla.getClass().getPackage().getName())[0]+"/ArticleResources/";
	    TextFileHandler tfh = new TextFileHandler();
	    path = tfh.getTextFileAsString(path+"/CoTs/allannotations.json", StandardCharsets.UTF_8);
	    HashMap<String, Integer> annotations = g.fromJson(path, stringIntegergMap);
		return annotations;
	}

	public static HashMap<String, Integer> loadLegalCotAnnotations(int radius, String function, int sigma) throws Exception {
		Gson g = new Gson();
		Type stringIntegergMap = new TypeToken<HashMap<String, Integer>>(){}.getType();
		MLArticle mla = new MLArticle();
		String path = String.format("%s/%s", System.getProperty("user.dir"), mla.getClass().getPackage().getName().replace(".", "/"));
		path = path.split(mla.getClass().getPackage().getName())[0]+"/ArticleResources/";
		TextFileHandler tfh = new TextFileHandler();
		path = tfh.getTextFileAsString(path+"/CoTs/r=" + radius + "/" + function + ".json", StandardCharsets.UTF_8);
		HashMap<String, Integer> allSigmaAnnotations = g.fromJson(path, stringIntegergMap);
		HashMap<String, Integer> legalannotations = new HashMap<String, Integer>();
		for (String key : allSigmaAnnotations.keySet()) {
			if (allSigmaAnnotations.get(key) <= sigma) {
				legalannotations.put(key, allSigmaAnnotations.get(key));
			}
		}
		return legalannotations;
	}
	
	public static Map<String, Integer> cotCountArticlesLeadNounAdjectiveVerbAdverb(NewsArticleWithStemmedVersion nawsv, int radius) {
		
		// Only create cots from worclasses noun, adjective, verb and adverb only
		
		Map<String, Integer> map = new HashMap<String, Integer>();
		ArrayList<PosTaggedWord> ptwList = new ArrayList<PosTaggedWord>();
		
		if(nawsv.getPosTaggedLeadText().getPosTaggedWords()!= null){
			for (PosTaggedWord posTaggedWord : nawsv.getPosTaggedLeadText().getPosTaggedWords()) {
				ptwList.add(posTaggedWord);
			}
		}	
		for (int i = 0; i < ptwList.size();i++) {
			ArrayList<String> permittedWordclasses = new ArrayList<String>(Arrays.asList("subst", "adj", "verb", "adv"));
			String wordclass = ptwList.get(i).wordclass;
			if (permittedWordclasses.contains(wordclass)) {
//					System.out.println("Contains: " + wordclass);
				ArrayList<String> cots = getCoTsFromIndexNounAdjectiveVerbAdverb(ptwList, i, radius);
				for (String key : cots) {
					if (map.containsKey(key)) {
						int count = map.get(key);
						count++;
						map.put(key, count);
					} else {
						map.put(key, 1);
					}
				}
			} 
		}
		return map;
	}
	
	public static Map<String, Integer> cotCountArticlesTitleNounAdjectiveVerbAdverb(NewsArticleWithStemmedVersion nawsv, int radius) {
		
		// Only create cots from worclasses noun, adjective, verb and adverb only
		
		Map<String, Integer> map = new HashMap<String, Integer>();
		ArrayList<PosTaggedWord> ptwList = new ArrayList<PosTaggedWord>();
		
		if(nawsv.getPosTaggedTitle().getPosTaggedWords()!= null){
			for (PosTaggedWord posTaggedWord : nawsv.getPosTaggedTitle().getPosTaggedWords()) {
				ptwList.add(posTaggedWord);
			}
		}	
		for (int i = 0; i < ptwList.size();i++) {
			ArrayList<String> permittedWordclasses = new ArrayList<String>(Arrays.asList("subst", "adj", "verb", "adv"));
			String wordclass = ptwList.get(i).wordclass;
			if (permittedWordclasses.contains(wordclass)) {
//					System.out.println("Contains: " + wordclass);
				ArrayList<String> cots = getCoTsFromIndexNounAdjectiveVerbAdverb(ptwList, i, radius);
				for (String key : cots) {
					if (map.containsKey(key)) {
						int count = map.get(key);
						count++;
						map.put(key, count);
					} else {
						map.put(key, 1);
					}
				}
			} 
		}
		return map;
	}
	
	public static ArrayList<String> getCoTsFromIndexNounAdjectiveVerbAdverb(ArrayList<PosTaggedWord> allPosTaggedWords, int index, int radius) {
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
	
	public static int extractAggregateSentimentFromString(String string) {
		// Format: '0000', '-1-1-1-1', '1111' etc.
		if (string.charAt(string.length()-2) == '0') {
			return 0;
		} else {
			if (string.charAt(string.length()-3) == '-') {
				return -1;
			} else if (string.charAt(string.length()-2) == '1' && string.charAt(string.length()-3) != '-') {
				return 1;
			} else {
				throw new IllegalStateException("Unknown sentiment classification: " + string);
			}
		}
	}
	
	//RETURNS TRUE IF IN BORS CATEGORY
	public static double getBors(NewsArticleWithPosTaggedWords nawpti){
		double bors = 0.0;
		ArticleCategoryGenerator ag = new ArticleCategoryGenerator();
		String article = ag.extractCategory(nawpti);
		
		if(article.equals(bors) || article.split("/")[0].equals("bors")){
			bors = 1.0;
		}
		return bors;
	}
		
	//RETURNS TRUE IF IN Okonomi CATEGORY
	public static double getOkonomi(NewsArticleWithPosTaggedWords nawpti){
		double okonomi = 0.0;
		ArticleCategoryGenerator ag = new ArticleCategoryGenerator();
		String article = ag.extractCategory(nawpti);
		
		if(article.equals("okonomi") || article.split("/")[0].equals("okonomi")){
			okonomi = 1.0;
		}
		return okonomi;
	}
		
	//RETURNS TRUE IF IN Analyser CATEGORY
	public static double getAnalyse(NewsArticleWithPosTaggedWords nawpti){
		double analyser = 0.0;
		ArticleCategoryGenerator ag = new ArticleCategoryGenerator();
		String article = ag.extractCategory(nawpti);
		
		if(article.equals("analyser") || article.split("/")[0].equals("analyser")){
			analyser = 1.0;
		}
		return analyser;
	}
	
	public void setLengthOfTitleAndLead(NewsArticleWithPosTaggedWords nawsv) {
		if(nawsv.getPosTaggedTitle().getPosTaggedWords()!= null){
			double counter = 0.0;
			for (PosTaggedWord posTaggedWord : nawsv.getPosTaggedTitle().getPosTaggedWords()) {
				counter++;
			}	
			lengthOfTitle = counter;
		} else {
			lengthOfTitle = 0.0;
		}
		if(nawsv.getPosTaggedLeadText().getPosTaggedWords()!= null){
			double counter = 0.0;
			for (PosTaggedWord posTaggedWord : nawsv.getPosTaggedLeadText().getPosTaggedWords()) {
				counter++;
			}	
			lengthOfLead = counter;
		} else {
			lengthOfLead = 0.0;
		}
	}
	
	public MLDataPair getMLDataPair() {
		ArrayList<Double> inputList = new ArrayList<Double>();
		inputList.add(positiveCotsTitle);
		inputList.add(negativeCotsTitle);
		inputList.add(neutralCotsTitle);

		inputList.add(positiveCotsLead);
		inputList.add(negativeCotsLead);
		inputList.add(neutralCotsLead);
		
		inputList.add(isMonday);
		inputList.add(isTuesday);
		inputList.add(isWednesday);
		inputList.add(isThursday);
		inputList.add(isFriday);
		inputList.add(isSaturday);
		inputList.add(isSunday);
		
		inputList.add(isWeekday);
		inputList.add(isWeekend);
		
		inputList.add(isStockExchangeOpen);
		
		inputList.add(isBors);
		inputList.add(isAnalyse);
		inputList.add(isOkonomi);
		
		inputList.add(lengthOfTitle);
		inputList.add(lengthOfLead);
		
		inputList.add(positiveTitleClueCount);
		inputList.add(negativeTitleClueCount);
		inputList.add(hasPositiveTitleClue);
		inputList.add(hasNegativeTitleClue);
		
		ArrayList<Double> idealList = new ArrayList<Double>();
		idealList.add(sentimentClassification);
		
		MLData inputData = new BasicMLData(getDoubleArray(inputList));
		MLData idealData = new BasicMLData(getDoubleArray(idealList));
		MLDataPair mldatapair = new BasicMLDataPair(inputData, idealData);
		return mldatapair;
	}

	public MLDataPair getMLDataPair(String binaryIndicator) {
		if (binaryIndicator.length() != 25) {
			throw new IllegalArgumentException("String must be of length 25, not of length " + binaryIndicator.length());
		}
		ArrayList<Double> inputList = new ArrayList<Double>();
		
		if (binaryIndicator.charAt(0) == '1') {
			inputList.add(positiveCotsTitle);
		}
		if (binaryIndicator.charAt(1) == '1') {
			inputList.add(negativeCotsTitle);
		}
		if (binaryIndicator.charAt(2) == '1') {
			inputList.add(neutralCotsTitle);
		}
		if (binaryIndicator.charAt(3) == '1') {
			inputList.add(positiveCotsLead);
		}
		if (binaryIndicator.charAt(4) == '1') {
			inputList.add(negativeCotsLead);
		}
		if (binaryIndicator.charAt(5) == '1') {
			inputList.add(neutralCotsLead);
		}
		if (binaryIndicator.charAt(6) == '1') {
			inputList.add(isMonday);
		}
		if (binaryIndicator.charAt(7) == '1') {
			inputList.add(isTuesday);
		}
		if (binaryIndicator.charAt(8) == '1') {
			inputList.add(isWednesday);
		}
		if (binaryIndicator.charAt(9) == '1') {
			inputList.add(isThursday);
		}
		if (binaryIndicator.charAt(10) == '1') {
			inputList.add(isFriday);
		}
		if (binaryIndicator.charAt(11) == '1') {
			inputList.add(isSaturday);
		}
		if (binaryIndicator.charAt(12) == '1') {
			inputList.add(isSunday);
		}
		if (binaryIndicator.charAt(13) == '1') {
			inputList.add(isWeekday);
		}
		if (binaryIndicator.charAt(14) == '1') {
			inputList.add(isWeekend);
		}
		if (binaryIndicator.charAt(15) == '1') {
			inputList.add(isStockExchangeOpen);
		}
		if (binaryIndicator.charAt(16) == '1') {
			inputList.add(isBors);
		}
		if (binaryIndicator.charAt(17) == '1') {
			inputList.add(isAnalyse);
		}
		if (binaryIndicator.charAt(18) == '1') {
			inputList.add(isOkonomi);
		}
		if (binaryIndicator.charAt(19) == '1') {
			inputList.add(lengthOfTitle);
		}
		if (binaryIndicator.charAt(20) == '1') {
			inputList.add(lengthOfLead);
		}
		if (binaryIndicator.charAt(21) == '1') {
			inputList.add(positiveTitleClueCount);
		}
		if (binaryIndicator.charAt(22) == '1') {
			inputList.add(negativeTitleClueCount);
		}
		if (binaryIndicator.charAt(23) == '1') {
			inputList.add(hasPositiveTitleClue);
		}
		if (binaryIndicator.charAt(24) == '1') {
			inputList.add(hasNegativeTitleClue);
		}

		ArrayList<Double> idealList = new ArrayList<Double>();
		idealList.add(sentimentClassification);
		MLData inputData = new BasicMLData(getDoubleArray(inputList));
		MLData idealData = new BasicMLData(getDoubleArray(idealList));
		MLDataPair mldatapair = new BasicMLDataPair(inputData, idealData);
		return mldatapair;
	}
	
	public static double[] getDoubleArray(List<Double> list) {
		double[] doubleArray = new double[list.size()];
		for (int i = 0; i < list.size(); i++) {
			doubleArray[i] = list.get(i);
		}
		return doubleArray;
	}

	public void setUpTitleClues(NewsArticleWithStemmedVersion nawsv) throws Exception {
		positiveTitleClueCount = getPositiveWordTitleCount(nawsv);
//		System.out.println("Positive clue count: " + positiveTitleClueCount);
		negativeTitleClueCount = getNegativeWordTitleCount(nawsv);
//		System.out.println("Negative clue count: " + negativeTitleClueCount);
		hasPositiveTitleClue = positiveTitleClueCount > 0 ? 1.0 : 0.0;
		hasNegativeTitleClue = negativeTitleClueCount > 0 ? 1.0 : 0.0;
	}
	
	public double getPositiveWordTitleCount(NewsArticleWithStemmedVersion nawsv) throws Exception{
		TextFileHandler tfh = new TextFileHandler();
		String[] clues = tfh.getPositiveTitleClues();
		double counter = 0.0;
		for (String string : clues) {
			if (nawsv.getTitle().contains(string)) {
				counter++;
			}
		}
//		System.out.println("Positive title count: " + counter);
		return counter;
	}
	
	public double getNegativeWordTitleCount(NewsArticleWithStemmedVersion nawsv) throws Exception{
		TextFileHandler tfh = new TextFileHandler();
		String[] clues = tfh.getNegativeTitleClues();
		double counter = 0.0;
		for (String string : clues) {
			if (nawsv.getTitle().contains(string)) {
				counter++;
			}
		}
//		System.out.println("Negative title count: " + counter);
		return counter;
	}
	
	public static ArrayList<ArrayList<Double>> wrapper(MLDataSet mldataset) {
		ArrayList<ArrayList<Double>> list = new ArrayList<ArrayList<Double>>();
		for (MLDataPair mlDataPair : mldataset) {
			list.add(wrapperInner(mlDataPair));
		}
		return list;
	}

	public static MLDataSet wrapper(ArrayList<ArrayList<Double>> outerList) {
		MLDataSet mlDataSet = new BasicMLDataSet();
		for (ArrayList<Double> innerList : outerList) {
			mlDataSet.add(wrapperInner(innerList));
		}
		return mlDataSet;
	}
	
	public static ArrayList<Double> wrapperInner(MLDataPair mldatapair) {
		ArrayList<Double> arrayList = new ArrayList<Double>();
		double[] input = mldatapair.getInputArray();
		for (int i = 0; i < input.length; i++) {
			arrayList.add(i, input[i]);
		}
		double[] ideal = mldatapair.getIdealArray();
		arrayList.add(arrayList.size(), ideal[0]);
		return arrayList;
	}
	
	public static MLDataPair wrapperInner(ArrayList<Double> arrayList) {
		double[] input = new double[arrayList.size()-1];
		for (int i = 0; i < arrayList.size()-1; i++) {
			input[i] = arrayList.get(i);
		}
		double[] ideal = new double[1];
		ideal[0] = arrayList.get(arrayList.size()-1);
		MLData mlDataInput = new BasicMLData(input);
		MLData mlDataIdeal = new BasicMLData(ideal);
		MLDataPair mldatapair = new BasicMLDataPair(mlDataInput, mlDataIdeal);
		return mldatapair;
	}
}
