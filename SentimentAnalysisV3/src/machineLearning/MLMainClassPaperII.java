package machineLearning;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import preProcessing.MLArticle;
import preProcessing.NewsArticleWithStemmedVersion;
import preProcessing.TextFileHandler;
import sun.java2d.pipe.SpanShapeRenderer.Simple;
import newsAPI.JsonHandler;

public class MLMainClassPaperII {

	public static String defaultSelector = "1111110000000";
	
	public static MLDataSet getMLDataSet(JsonHandler jh, int radius, String function, int sigma, String binaryIndicator) throws Exception {
		MLDataSet mldataset = new BasicMLDataSet();
		for (NewsArticleWithStemmedVersion nawsv : jh.stemmedArticles.getNawsv()) {
			MLArticle mla = new MLArticle(nawsv, radius, function, sigma);
			mldataset.add(mla.getMLDataPair(binaryIndicator));
		}
		return mldataset;
	}
	
	public static void main(String[] args) throws Exception {
		
//		writeArticlesToFiles();
//		computeAndPrintResultsForLatexTableProportion();
//		computeAndPrintResultsForLatexGraphsProportions();
		
//		computeAndPrintResultsForLatexTable();
//		computeAndPrintResultsForLatexGraphs();
		
		
		
		double maxPrecision = 0;
		String maxString = "";
		int counter = 0;
		for (int r = 2; r <= 8; r = r + 2) {
			for (String binarySelector : getFeatureSelectionString()) {
				String candidate = defaultSelector + binarySelector;
				double thisPrecision = NBClassifier.runNBC(readMLDataSetProportionSubsetFromFile(r, "mi", 0.5, candidate));
				if (thisPrecision > maxPrecision) {
					maxPrecision = thisPrecision;
					maxString = candidate;
				}
				if (counter % 500 == 0) {
					System.out.println(counter + ": " + maxPrecision);
				}
				counter++;
			}
			System.out.println(r + ": " + maxString + " --> " + maxPrecision);
		}
		
//		readMLDataSetFromFile(2, "chi", 4000);
//		JsonHandler jh = new JsonHandler("/ArticleSteps/4_StemmedArticles/MainDataSetStemmed.json", "stemmed");
////		MLDataSet mldataset = getMLDataSet(jh, 10, "chi", 4000, "111111111111111111111");
//		ArrayList<String> functions = new ArrayList<String>(Arrays.asList("tf", "idf", "tfidf", "mi", "chi"));
//		ArrayList<Integer> sigmas = new ArrayList<Integer>();
////		sigmas.add(new Integer(500));
//		sigmas.add(new Integer(1000));
//		sigmas.add(new Integer(2000));
//		sigmas.add(new Integer(4000));
//		Collections.sort(sigmas);
//		for (int r = 2; r <= 8; r = r + 2) {
//			for (String function : functions) {
//				for (int sigma : sigmas) {
//					MLDataSet mldataset = getMLDataSet(jh, r, function, sigma, "1111110000000111111001111");
////					mldataset;
//					System.out.print(String.format("[r=%s f=%s s=%s: ", r, function, sigma));
//					System.out.print("NB: " + RFClassifier.runRF(mldataset));
//					System.out.print(", RF: " + RFClassifier.runRF(mldataset));
//					System.out.print(", J48: " + J48Classifier.runJ48(mldataset) + "]\n");
//				}
//			}
//		}
////		MLDataSet mldataset = getMLDataSet(jh, 10, "chi", 4000, "111111000000000000000");
//		
////		System.out.println(NBClassifier.runNBC(mldataset));
	}
	
	public static void computeAndPrintResultsForLatexTable() throws Exception {
		ArrayList<String> functions = new ArrayList<String>(Arrays.asList("tf", "idf", "tfidf", "mi", "chi"));
		ArrayList<Integer> sigmas = new ArrayList<Integer>();
//		sigmas.add(new Integer(500));
		sigmas.add(new Integer(1000));
		sigmas.add(new Integer(2000));
		sigmas.add(new Integer(4000));
		Collections.sort(sigmas);
		
		System.out.print("\\multirow{5}{*}{NB} ");
		for (String function : functions) {
			if (function.equals("chi")) {
				System.out.print(" & $\\chi^2$");
			} else {
				System.out.print(" & \\textit{"+function+"}");
			}
			for (int sigma : sigmas) {
				for (int r = 2; r <= 8; r = r + 2) {
					double precision = NBClassifier.runNBC(readMLDataSetSubsetFromFile(r, function, sigma, "1111110000000111111111111")) * 100;
					System.out.print(String.format(" & %.3g", precision).replace(',', '.'));
				}
			}
			System.out.print(" \\\\\n");
		}	

		System.out.print("\\multirow{5}{*}{RF} ");
		for (String function : functions) {
			if (function.equals("chi")) {
				System.out.print(" & $\\chi^2$");
			} else {
				System.out.print(" & \\textit{"+function+"}");
			}
			for (int sigma : sigmas) {
				for (int r = 2; r <= 8; r = r + 2) {
					double precision = RFClassifier.runRF(readMLDataSetSubsetFromFile(r, function, sigma, "1111110000000111111111111")) * 100;
					System.out.print(String.format(" & %.3g", precision).replace(',', '.'));
				}
			}
			System.out.print(" \\\\\n");
		}	

		System.out.print("\\multirow{5}{*}{J48} ");
		for (String function : functions) {
			if (function.equals("chi")) {
				System.out.print(" & $\\chi^2$");
			} else {
				System.out.print(" & \\textit{"+function+"}");
			}
			for (int sigma : sigmas) {
				for (int r = 2; r <= 8; r = r + 2) {
					double precision = J48Classifier.runJ48(readMLDataSetSubsetFromFile(r, function, sigma, "1111110000000111111111111")) * 100;
					System.out.print(String.format(" & %.3g", precision).replace(',', '.'));
				}
			}
			System.out.print(" \\\\\n");
		}	
	}
	
	public static void computeAndPrintResultsForLatexTableProportion() throws Exception {
		String featureString = "1111110000000111111001111";
//		String featureString = "1111110000000000000000000";
		ArrayList<String> functions = new ArrayList<String>(Arrays.asList("tf", "idf", "tfidf", "mi", "chi"));
		ArrayList<Double> proportions = new ArrayList<Double>();
//		sigmas.add(new Integer(500));
		proportions.add(0.10);
		proportions.add(0.30);
		proportions.add(0.5);
		Collections.sort(proportions);
		HashMap<Integer, Double> radiusAverage = new HashMap<Integer, Double>();
		radiusAverage.put(2, 0.0);
		radiusAverage.put(4, 0.0);
		radiusAverage.put(6, 0.0);
		radiusAverage.put(8, 0.0);
		HashMap<Double, Double> proportionAverage = new HashMap<Double, Double>();
		proportionAverage.put(0.1, 0.0);
		proportionAverage.put(0.3, 0.0);
		proportionAverage.put(0.5, 0.0);
		HashMap<String, Double> rankingAverage = new HashMap<String, Double>();
		rankingAverage.put("tf", 0.0);
		rankingAverage.put("idf", 0.0);
		rankingAverage.put("tfidf", 0.0);
		rankingAverage.put("mi", 0.0);
		rankingAverage.put("chi", 0.0);
		HashMap<String, Double> mlAverage = new HashMap<String, Double>();
		mlAverage.put("nb", 0.0);
		mlAverage.put("rf", 0.0);
		mlAverage.put("j48", 0.0);
		
		System.out.print("\\multirow{5}{*}{NB} ");
		for (String function : functions) {
			if (function.equals("chi")) {
				System.out.print(" & $\\chi^2$");
			} else {
				System.out.print(" & \\textit{"+function+"}");
			}
			for (double proportion : proportions) {
				for (int r = 2; r <= 8; r = r + 2) {
					double precision = NBClassifier.runNBC(readMLDataSetProportionSubsetFromFile(r, function, proportion, featureString)) * 100;
					System.out.print(String.format(" & %.3g", precision).replace(',', '.'));
					radiusAverage.put(r, radiusAverage.get(r) + precision);
					proportionAverage.put(proportion, proportionAverage.get(proportion) + precision);
					rankingAverage.put(function, rankingAverage.get(function) + precision);
					mlAverage.put("nb", mlAverage.get("nb") + precision);
				}
			}
			System.out.print(" \\\\\n");
		}	
		System.out.print("\\hline \n");
		System.out.print("\\multirow{5}{*}{RF} ");
		for (String function : functions) {
			if (function.equals("chi")) {
				System.out.print(" & $\\chi^2$");
			} else {
				System.out.print(" & \\textit{"+function+"}");
			}
			for (double proportion : proportions) {
				for (int r = 2; r <= 8; r = r + 2) {
					double precision = RFClassifier.runRF(readMLDataSetProportionSubsetFromFile(r, function, proportion, featureString)) * 100;
					System.out.print(String.format(" & %.3g", precision).replace(',', '.'));
					radiusAverage.put(r, radiusAverage.get(r) + precision);
					proportionAverage.put(proportion, proportionAverage.get(proportion) + precision);
					rankingAverage.put(function, rankingAverage.get(function) + precision);
					mlAverage.put("rf", mlAverage.get("rf") + precision);
				}
			}
			System.out.print(" \\\\\n");
		}	
		System.out.print("\\hline \n");
		System.out.print("\\multirow{5}{*}{J48} ");
		for (String function : functions) {
			if (function.equals("chi")) {
				System.out.print(" & $\\chi^2$");
			} else {
				System.out.print(" & \\textit{"+function+"}");
			}
			for (double proportion : proportions) {
				for (int r = 2; r <= 8; r = r + 2) {
					double precision = J48Classifier.runJ48(readMLDataSetProportionSubsetFromFile(r, function, proportion, featureString)) * 100;
					System.out.print(String.format(" & %.3g", precision).replace(',', '.'));
					radiusAverage.put(r, radiusAverage.get(r) + precision);
					proportionAverage.put(proportion, proportionAverage.get(proportion) + precision);
					rankingAverage.put(function, rankingAverage.get(function) + precision);
					mlAverage.put("j48", mlAverage.get("j48") + precision);
				}
			}
			System.out.print(" \\\\ \n");
		}	
		System.out.print("\\hline \n\n");
		
		for (int key : radiusAverage.keySet()) {
			radiusAverage.put(key, radiusAverage.get(key) / 45.0);
		}
		for (double key : proportionAverage.keySet()) {
			proportionAverage.put(key, proportionAverage.get(key) / 60.0);
		}
		for (String key : rankingAverage.keySet()) {
			rankingAverage.put(key, rankingAverage.get(key) / 36.0);
			
		}
		for (String key : mlAverage.keySet()) {
			mlAverage.put(key, mlAverage.get(key) / 60.0);
			
		}
		
		
		System.out.println(radiusAverage);
		System.out.println(proportionAverage);
		System.out.println(rankingAverage);
		System.out.println(mlAverage);
	}
	
	

	public static void computeAndPrintResultsForLatexGraphsProportions() throws Exception {
		ArrayList<String> functions = new ArrayList<String>(Arrays.asList("tf", "idf", "tfidf", "mi", "chi"));
		ArrayList<Double> proportions = new ArrayList<Double>();
		proportions.add(0.10);
		proportions.add(0.30);
		proportions.add(0.5);
		Collections.sort(proportions);
		int selector = 1;
		
		System.out.println("%NB");
		for (double proportion : proportions) {
			if (selector == 1) {
				System.out.print("if (selector == "+ selector++ + ")\n");
			} else {
				System.out.print("elseif (selector == "+ selector++ + ")\n");
			}
				for (String function : functions) {
					System.out.print(function + " = [");
					for (int r = 2; r <= 8; r = r + 1) {
						double precision = NBClassifier.runNBC(readMLDataSetProportionSubsetFromFile(r, function, proportion, "1111110000000111111111111")) * 100;
						System.out.print(String.format("%.3g ", precision).replace(',', '.'));
					}
					System.out.print("];\n");
			}
		}	
			System.out.println("%RF");
			for (double proportion : proportions) {
				System.out.print("elseif (selector == "+ selector++ + ")\n");
				for (String function : functions) {
					System.out.print(function + " = [");
					for (int r = 2; r <= 8; r = r + 1) {
						double precision = RFClassifier.runRF(readMLDataSetProportionSubsetFromFile(r, function, proportion, "1111110000000111111111111")) * 100;
						System.out.print(String.format("%.3g ", precision).replace(',', '.'));
					}
					System.out.print("];\n");
				}
			}	
			System.out.println("%J48");
			for (double proportion : proportions) {
				System.out.print("elseif (selector == "+ selector++ + ")\n");
				for (String function : functions) {
					System.out.print(function + " = [");
					for (int r = 2; r <= 8; r = r + 1) {
						double precision = J48Classifier.runJ48(readMLDataSetProportionSubsetFromFile(r, function, proportion, "1111110000000111111111111")) * 100;
						System.out.print(String.format("%.3g ", precision).replace(',', '.'));
					}
					System.out.print("];\n");
				}
			}	
			System.out.println("end");
	}

	public static void computeAndPrintResultsForLatexGraphs() throws Exception {
		ArrayList<String> functions = new ArrayList<String>(Arrays.asList("tf", "idf", "tfidf", "mi", "chi"));
		ArrayList<Integer> sigmas = new ArrayList<Integer>();
//		sigmas.add(new Integer(500));
		sigmas.add(new Integer(1000));
		sigmas.add(new Integer(2000));
		sigmas.add(new Integer(4000));
		Collections.sort(sigmas);
				
		System.out.println("NB");
		for (int sigma : sigmas) {
			System.out.println("sigma = " + sigma);
			for (String function : functions) {
				System.out.print(function + " = [");
				for (int r = 2; r <= 8; r = r + 1) {
					double precision = NBClassifier.runNBC(readMLDataSetSubsetFromFile(r, function, sigma, "1111110000000111111111111")) * 100;
					System.out.print(String.format("%.3g ", precision).replace(',', '.'));
				}
				System.out.print("];\n");
			}
		}	
		System.out.println("RF");
		for (int sigma : sigmas) {
			System.out.println("sigma = " + sigma);
			for (String function : functions) {
				System.out.print(function + " = [");
				for (int r = 2; r <= 8; r = r + 1) {
					double precision = RFClassifier.runRF(readMLDataSetSubsetFromFile(r, function, sigma, "1111110000000111111111111")) * 100;
					System.out.print(String.format("%.3g ", precision).replace(',', '.'));
				}
				System.out.print("];\n");
			}
		}	
		System.out.println("J48");
		for (int sigma : sigmas) {
			System.out.println("sigma = " + sigma);
			for (String function : functions) {
				System.out.print(function + " = [");
				for (int r = 2; r <= 8; r = r + 1) {
					double precision = J48Classifier.runJ48(readMLDataSetSubsetFromFile(r, function, sigma, "1111110000000111111111111")) * 100;
					System.out.print(String.format("%.3g ", precision).replace(',', '.'));
				}
				System.out.print("];\n");
			}
		}	
	}
	
	

	public static void writeArticlesToFiles() throws Exception {
		JsonHandler jh = new JsonHandler("/ArticleSteps/4_StemmedArticles/MainDataSetStemmed.json", "stemmed");
		ArrayList<String> functions = new ArrayList<String>(Arrays.asList("tf", "idf", "tfidf", "mi", "chi"));
		ArrayList<Integer> sigmas = new ArrayList<Integer>();
		sigmas.add(new Integer(1000));
		sigmas.add(new Integer(2000));
		sigmas.add(new Integer(4000));
		Collections.sort(sigmas);
		HashMap<Integer, Integer> cotCountMap = new HashMap<Integer, Integer>();
		cotCountMap.put(2,2209);
		cotCountMap.put(3, 3240);
		cotCountMap.put(4, 4133);
		cotCountMap.put(5, 4959);
		cotCountMap.put(6, 5730);
		cotCountMap.put(7, 6426);
		cotCountMap.put(8, 7033);
		ArrayList<Double> proportions = new ArrayList<Double>();
		proportions.add(0.10);
		proportions.add(0.30);
		proportions.add(0.50);
		for (int radius = 2; radius <= 8; radius = radius + 1) {
			for (String function : functions) {
				for (double proportion: proportions) {
					int numberOfCotsToConsider =  (int) (proportion * cotCountMap.get(radius));
					MLDataSet mldataset = getMLDataSet(jh, radius, function, numberOfCotsToConsider, "1111110000000111111111111");
					writeMLDataSetToFile(mldataset, radius, function, proportion);
					System.out.println(String.format("Done with r=%s, f=%s and p=%s ", radius, function, proportion));
				}
			}
		}
	}

	public static void writeArticlesToFilesSigmas() throws Exception {
		JsonHandler jh = new JsonHandler("/ArticleSteps/4_StemmedArticles/MainDataSetStemmed.json", "stemmed");
		ArrayList<String> functions = new ArrayList<String>(Arrays.asList("tf", "idf", "tfidf", "mi", "chi"));
		ArrayList<Integer> sigmas = new ArrayList<Integer>();
		sigmas.add(new Integer(1000));
		sigmas.add(new Integer(2000));
		sigmas.add(new Integer(4000));
		Collections.sort(sigmas);
		for (int radius = 2; radius <= 8; radius = radius + 1) {
			for (String function : functions) {
				for (int sigma : sigmas) {
					MLDataSet mldataset = getMLDataSet(jh, radius, function, sigma, "1111110000000111111111111");
					writeMLDataSetToFile(mldataset, radius, function, sigma);
					System.out.println(String.format("Done with r=%s, f=%s and s=%s ", radius, function, sigma));
				}
			}
		}
	}
	
	public static void writeMLDataSetToFile(MLDataSet mldataset, int radius, String function, double proportion) throws Exception {
		JsonHandler jh = new JsonHandler("/ArticleSteps/4_StemmedArticles/MainDataSetStemmed.json", "stemmed");
		ArrayList<ArrayList<Double>> arrayList = MLArticle.wrapper(mldataset);
		Gson gson = new Gson();
		String text = gson.toJson(arrayList);
		MLMainClassPaperII mlmcpii = new MLMainClassPaperII();
		String path = String.format("%s/%s", System.getProperty("user.dir"), mlmcpii.getClass().getPackage().getName().replace(".", "/"));
	    path = path.substring(0, path.length()-16);
	    path = path + "/ArticleResources/MLDataSetProportion/";
		Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path+"r="+radius + "-f=" + function + "-p="+proportion+".json"), "UTF-8"));
		try {
			out.write(text);
		} finally {
			out.close();
		}
	}

	public static void writeMLDataSetToFileSigmas(MLDataSet mldataset, int radius, String function, int sigma) throws Exception {
		JsonHandler jh = new JsonHandler("/ArticleSteps/4_StemmedArticles/MainDataSetStemmed.json", "stemmed");
		ArrayList<ArrayList<Double>> arrayList = MLArticle.wrapper(mldataset);
		Gson gson = new Gson();
		String text = gson.toJson(arrayList);
		MLMainClassPaperII mlmcpii = new MLMainClassPaperII();
		String path = String.format("%s/%s", System.getProperty("user.dir"), mlmcpii.getClass().getPackage().getName().replace(".", "/"));
		path = path.substring(0, path.length()-16);
		path = path + "/ArticleResources/MLDataSet/";
		Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path+"r="+radius + "-f=" + function + "-s="+sigma+".json"), "UTF-8"));
		try {
			out.write(text);
		} finally {
			out.close();
		}
	}
	
	public static MLDataSet readMLDataSetFromFile(int radius, String function, int sigma) throws Exception {
		MLMainClassPaperII mlmcpii = new MLMainClassPaperII();
		Gson gson = new Gson();
		
		String path = String.format("%s/%s", System.getProperty("user.dir"), mlmcpii.getClass().getPackage().getName().replace(".", "/"));
	    path = path.substring(0, path.length()-16);
	    path = path + "/ArticleResources/MLDataSet/";
	    String filePath = path + "r="+radius + "-f=" + function + "-s="+sigma+".json";
	    TextFileHandler tfh = new TextFileHandler();
	    String file = tfh.getTextFileAsString(filePath, StandardCharsets.UTF_8);
	    Type arrayListType = new TypeToken<ArrayList<ArrayList<Double>>>(){}.getType();

	    ArrayList<ArrayList<Double>> arrayList = gson.fromJson(file, arrayListType);
//		System.out.println(mldataset);
		return MLArticle.wrapper(arrayList);
	    
//	    path = String.format("%s/%s", System.getProperty("user.dir"), mlmcpii.getClass().getPackage().getName().replace(".", "/"));
//	    path = path.split(mlmcpii.getClass().getPackage().getName())[0]+"/ArticleResources/";
//	    TextFileHandler tfh = new TextFileHandler();
//	    path = tfh.getTextFileAsString(path+"/CoTs/allannotations.json", StandardCharsets.UTF_8);
//	    HashMap<String, Integer> annotations = g.fromJson(path, stringIntegergMap);
//	    
//	    
//		return null;
	}
	
	public static MLDataSet readMLDataSetSubsetFromFile(int radius, String function, int sigma, String binarySelector) throws Exception {
		MLMainClassPaperII mlmcpii = new MLMainClassPaperII();
		Gson gson = new Gson();
		
		String path = String.format("%s/%s", System.getProperty("user.dir"), mlmcpii.getClass().getPackage().getName().replace(".", "/"));
	    path = path.substring(0, path.length()-16);
	    path = path + "/ArticleResources/MLDataSet/";
	    String filePath = path + "r="+radius + "-f=" + function + "-s="+sigma+".json";
	    TextFileHandler tfh = new TextFileHandler();
	    String file = tfh.getTextFileAsString(filePath, StandardCharsets.UTF_8);
	    Type arrayListType = new TypeToken<ArrayList<ArrayList<Double>>>(){}.getType();
	    ArrayList<ArrayList<Double>> arrayList = gson.fromJson(file, arrayListType);
	    for (ArrayList<Double> innerList : arrayList) {
	    	if (binarySelector.charAt(0) == '0') {
	    		innerList.remove(0+6);
	    	}
	    	if (binarySelector.charAt(1) == '0') {
	    		innerList.remove(1+6);
	    	}
	    	if (binarySelector.charAt(2) == '0') {
	    		innerList.remove(2+6);
	    	}
	    	if (binarySelector.charAt(3) == '0') {
	    		innerList.remove(2+6);
	    	}
	    	if (binarySelector.charAt(4) == '0') {
	    		innerList.remove(2+6);
	    	}
	    	if (binarySelector.charAt(5) == '0') {
	    		innerList.remove(2+6);
	    	}
	    	if (binarySelector.charAt(6) == '0') {
	    		innerList.remove(2+6);
	    	}
	    	if (binarySelector.charAt(7) == '0') {
	    		innerList.remove(2+6);
	    	}
	    	if (binarySelector.charAt(8) == '0') {
	    		innerList.remove(2+6);
	    	}
	    	if (binarySelector.charAt(9) == '0') {
	    		innerList.remove(2+6);
	    	}
	    	if (binarySelector.charAt(10) == '0') {
	    		innerList.remove(2+6);
	    	}
	    	if (binarySelector.charAt(11) == '0') {
	    		innerList.remove(2+6);
	    	}
	    }
	    	return MLArticle.wrapper(arrayList);
	}

	public static MLDataSet readMLDataSetProportionSubsetFromFile(int radius, String function, double proportion, String binarySelector) throws Exception {
		MLMainClassPaperII mlmcpii = new MLMainClassPaperII();
		Gson gson = new Gson();
		
		String path = String.format("%s/%s", System.getProperty("user.dir"), mlmcpii.getClass().getPackage().getName().replace(".", "/"));
		path = path.substring(0, path.length()-16);
		path = path + "/ArticleResources/MLDataSetProportion/";
		String filePath = path + "r="+radius + "-f=" + function + "-p="+proportion+".json";
		TextFileHandler tfh = new TextFileHandler();
		String file = tfh.getTextFileAsString(filePath, StandardCharsets.UTF_8);
		Type arrayListType = new TypeToken<ArrayList<ArrayList<Double>>>(){}.getType();
		ArrayList<ArrayList<Double>> arrayList = gson.fromJson(file, arrayListType);
		for (ArrayList<Double> innerList : arrayList) {
			if (binarySelector.charAt(0) == '0') {
				innerList.remove(0+6);
			}
			if (binarySelector.charAt(1) == '0') {
				innerList.remove(1+6);
			}
			if (binarySelector.charAt(2) == '0') {
				innerList.remove(2+6);
			}
			if (binarySelector.charAt(3) == '0') {
				innerList.remove(2+6);
			}
			if (binarySelector.charAt(4) == '0') {
				innerList.remove(2+6);
			}
			if (binarySelector.charAt(5) == '0') {
				innerList.remove(2+6);
			}
			if (binarySelector.charAt(6) == '0') {
				innerList.remove(2+6);
			}
			if (binarySelector.charAt(7) == '0') {
				innerList.remove(2+6);
			}
			if (binarySelector.charAt(8) == '0') {
				innerList.remove(2+6);
			}
			if (binarySelector.charAt(9) == '0') {
				innerList.remove(2+6);
			}
			if (binarySelector.charAt(10) == '0') {
				innerList.remove(2+6);
			}
			if (binarySelector.charAt(11) == '0') {
				innerList.remove(2+6);
			}
		}
		int counter = 0;
		ArrayList<ArrayList<Double>> larsArrayList = new ArrayList<ArrayList<Double>>();
		ArrayList<ArrayList<Double>> palArrayList = new ArrayList<ArrayList<Double>>();
		for (ArrayList<Double> innerList : arrayList) {
			if (0 <= counter && counter <= 480) {
				larsArrayList.add(innerList);
			} else if (492 <= counter && counter <= 992) {
				palArrayList.add(innerList);
			}
			counter++;
		}
		arrayList = larsArrayList;
		return MLArticle.wrapper(arrayList);
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
	
	public static String getArrayString(double[] array) {
		String str = "";
		for (int i = 0; i < array.length; i++) {
			str += array[i] + ", ";
		}
		return str;
	}
	
	
	
    public static ArrayList<String> getFeatureSelectionString() {
 		ArrayList<String> stringList = new ArrayList<String>();
 		for (int i = 1; i < 4096; i++) {
 			stringList.add(getBinaryStringLength12(i));
 		}
 		return stringList;
 	}
 	
 
	public static String getBinaryStringLength12(int i) {
 		String str = Integer.toBinaryString(i);
 		while (str.length() < 12) {
 			str = "0" + str;
 		}
 		return str;
 	}
}