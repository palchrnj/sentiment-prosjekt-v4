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
import newsAPI.JsonHandler;

public class MLMainClassPaperIII {

	public static String defaultSelector = "1111110000000";
	
	public static MLDataSet getMLDataSet(JsonHandler jh, int radius, String binaryIndicator) throws Exception {
		MLDataSet mldataset = new BasicMLDataSet();
		int counter = 0;
		for (NewsArticleWithStemmedVersion nawsv : jh.stemmedArticles.getNawsv()) {
			MLArticlePaperIII mla = new MLArticlePaperIII(nawsv, radius, true);
			mldataset.add(mla.getMLDataPair(binaryIndicator));
//			System.out.println("Done with article article number " + counter++);
		}
		return mldataset;
	}
	
	public static void main(String[] args) throws Exception {
		
//		writeArticlesToFiles();
//		exhaustiveSearch();
//		J48Classifier.tuneJ48(readMLDataSetSubsetFromFileForTuning(6, "111111001001"));
		System.out.println(J48Classifier.runJ48(readMLDataSetSubsetFromFileForTuning(6, "111111001001")));
//		J48Classifier.tuneJ48(readMLDataSetSubsetFromFile(6, "111111111000000011100011111100"));
//		System.out.print(String.format("%.3g", precision).replace(',', '.'));
//		writeArticlesToFilesSigmas();
//		computeAndPrintResultsForLatexTableProportion();
//		computeAndPrintResultsForLatexGraphsProportions();
		
//		computeAndPrintResultsForLatexTable();
//		computeAndPrintResultsForLatexGraphs();
		
		
		
//		double maxPrecision = 0;
//		String maxString = "";
//		int counter = 0;
//		for (int r = 8; r <= 8; r = r + 2) {
//			for (String binarySelector : getFeatureSelectionString()) {
//				String candidate = defaultSelector + binarySelector;
//				double thisPrecision = NBClassifier.runNBC(readMLDataSetProportionSubsetFromFile(r, "mi", 0.5, candidate));
//				if (thisPrecision > maxPrecision) {
//					maxPrecision = thisPrecision;
//					maxString = candidate;
//				}
//				if (counter % 500 == 0) {
//					System.out.println(counter + ": " + maxPrecision);
//				}
//				counter++;
//			}
//			System.out.println(r + ": " + maxString + " --> " + maxPrecision);
//		}
		
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
		String featureString = "111111000000011111100111111";
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
					double precision = NBClassifier.runNBC(readMLDataSetSubsetFromFile(r, function, sigma, featureString)) * 100;
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
					double precision = RFClassifier.runRF(readMLDataSetSubsetFromFile(r, function, sigma, featureString)) * 100;
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
					double precision = J48Classifier.runJ48(readMLDataSetSubsetFromFile(r, function, sigma, featureString)) * 100;
					System.out.print(String.format(" & %.3g", precision).replace(',', '.'));
				}
			}
			System.out.print(" \\\\\n");
		}	
	}
	
	public static void computeAndPrintResultsForLatexTableProportion() throws Exception {
		String featureString = "111111000000011111100111111";
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
		JsonHandler jh = new JsonHandler("/ArticleSteps/4_StemmedArticles/NEW-ARTICLE-TO-ANNOTATE-COMBINED-STEMMED.json", "stemmed");
		int radius = 6;
		MLDataSet mldataset = getMLDataSet(jh, radius, "111111111000000011100011111100");
		writeMLDataSetToFile(mldataset, radius);
		System.out.println("Done");
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
					MLDataSet mldataset = getMLDataSet(jh, radius, function, sigma, "111111000000011111111111111");
					writeMLDataSetToFileSigmas(mldataset, radius, function, sigma);
					System.out.println(String.format("Done with r=%s, f=%s and s=%s ", radius, function, sigma));
				}
			}
		}
	}
	
	public static void writeMLDataSetToFile(MLDataSet mldataset, int radius) throws Exception {
		JsonHandler jh = new JsonHandler("/ArticleSteps/4_StemmedArticles/NEW-ARTICLE-TO-ANNOTATE-COMBINED-STEMMED.json", "stemmed");
		ArrayList<ArrayList<Double>> arrayList = MLArticle.wrapper(mldataset);
		Gson gson = new Gson();
		String text = gson.toJson(arrayList);
		MLMainClassPaperII mlmcpii = new MLMainClassPaperII();
		String path = String.format("%s/%s", System.getProperty("user.dir"), mlmcpii.getClass().getPackage().getName().replace(".", "/"));
	    path = path.substring(0, path.length()-16);
	    path = path + "/ArticleResources/MLDataSetPaperIII/";
		Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path + "mldataset.json"), "UTF-8"));
		try {
			out.write(text);
		} catch (Exception e) {
			System.out.println("Something failed when trying to write!");
		} finally {
			out.close();
		}
	}

	public static void writeMLDataSetToFileUnannotated(MLDataSet mldataset, int radius, String filename) throws Exception {
		JsonHandler jh = new JsonHandler("/ArticleSteps/4_StemmedArticles/NEW-ARTICLE-TO-ANNOTATE-COMBINED-STEMMED.json", "stemmed");
		ArrayList<ArrayList<Double>> arrayList = MLArticle.wrapper(mldataset);
		Gson gson = new Gson();
		String text = gson.toJson(arrayList);
		MLMainClassPaperII mlmcpii = new MLMainClassPaperII();
		String path = String.format("%s/%s", System.getProperty("user.dir"), mlmcpii.getClass().getPackage().getName().replace(".", "/"));
		path = path.substring(0, path.length()-16);
		path = path + "/ArticleResources/MLDataSetPaperIII/";
		Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path + filename + ".json"), "UTF-8"));
		try {
			out.write(text);
		} catch (Exception e) {
			System.out.println("Something failed when trying to write!");
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

	public static MLDataSet readMLDataSetSubsetFromFile(int radius, String binarySelector) throws Exception {
		MLMainClassPaperII mlmcpii = new MLMainClassPaperII();
		Gson gson = new Gson();
		String path = String.format("%s/%s", System.getProperty("user.dir"), mlmcpii.getClass().getPackage().getName().replace(".", "/"));
		path = path.substring(0, path.length()-16);
		path = path + "/ArticleResources/MLDataSetPaperIII/";
		String filePath = path + "mldataset.json";
		TextFileHandler tfh = new TextFileHandler();
		String file = tfh.getTextFileAsString(filePath, StandardCharsets.UTF_8);
		Type arrayListType = new TypeToken<ArrayList<ArrayList<Double>>>(){}.getType();
		ArrayList<ArrayList<Double>> arrayList = gson.fromJson(file, arrayListType);
//		for (ArrayList<Double> innerList : arrayList) {
//			if (binarySelector.charAt(0) == '0') {
//				innerList.remove(0+6);
//			}
//			if (binarySelector.charAt(1) == '0') {
//				innerList.remove(1+6);
//			}
//			if (binarySelector.charAt(2) == '0') {
//				innerList.remove(2+6);
//			}
//			if (binarySelector.charAt(3) == '0') {
//				innerList.remove(2+6);
//			}
//			if (binarySelector.charAt(4) == '0') {
//				innerList.remove(2+6);
//			}
//			if (binarySelector.charAt(5) == '0') {
//				innerList.remove(2+6);
//			}
//			if (binarySelector.charAt(6) == '0') {
//				innerList.remove(2+6);
//			}
//			if (binarySelector.charAt(7) == '0') {
//				innerList.remove(2+6);
//			}
//			if (binarySelector.charAt(8) == '0') {
//				innerList.remove(2+6);
//			}
//			if (binarySelector.charAt(9) == '0') {
//				innerList.remove(2+6);
//			}
//			if (binarySelector.charAt(10) == '0') {
//				innerList.remove(2+6);
//			}
//			if (binarySelector.charAt(11) == '0') {
//				innerList.remove(2+6);
//			}
//		}
		return MLArticle.wrapper(arrayList);
	}
	
	public static void exhaustiveSearch() throws Exception {
		double maxPrecision = 0;
		String bestSelector = null;
		int counter = 0;
		for (String selector : getFeatureSelectionString()) {
//			String selector = "111111111111"; 
			selector = "111" + selector;
			MLDataSet mldataset = readMLDataSetSubsetFromFileForTuning(6, selector);
			double precision = J48Classifier.runJ48(mldataset) * 100;
			if (precision > maxPrecision) {
				maxPrecision = precision;
				bestSelector = selector;
				System.out.println(String.format("Precision=%s, selector=%s", maxPrecision, bestSelector));
			}
			if (counter++ % 128 == 0) {
				System.out.println("Counter=" + counter);
			}
		}
		System.out.println("Done.");
	}

	public static MLDataSet readMLDataSetSubsetFromFileForTuning(int radius, String binarySelector) throws Exception {
		MLMainClassPaperII mlmcpii = new MLMainClassPaperII();
		Gson gson = new Gson();
		String path = String.format("%s/%s", System.getProperty("user.dir"), mlmcpii.getClass().getPackage().getName().replace(".", "/"));
		path = path.substring(0, path.length()-16);
		path = path + "/ArticleResources/MLDataSetPaperIII/";
		String filePath = path + "mldataset.json";
		TextFileHandler tfh = new TextFileHandler();
		String file = tfh.getTextFileAsString(filePath, StandardCharsets.UTF_8);
		Type arrayListType = new TypeToken<ArrayList<ArrayList<Double>>>(){}.getType();
		ArrayList<ArrayList<Double>> inputList = gson.fromJson(file, arrayListType);
		ArrayList<ArrayList<Double>> outputList = new ArrayList<ArrayList<Double>>();
		for (ArrayList<Double> innerInputList : inputList) {
			ArrayList<Double> innerOutputList = new ArrayList<Double>(); 
			if (binarySelector.charAt(0) == '1') {
				// Title COTS
				innerOutputList.add(innerInputList.get(0));
				innerOutputList.add(innerInputList.get(1));
				innerOutputList.add(innerInputList.get(2));
			}
			if (binarySelector.charAt(1) == '1') {
				// Lead COTS
				innerOutputList.add(innerInputList.get(3));
				innerOutputList.add(innerInputList.get(4));
				innerOutputList.add(innerInputList.get(5));
			}
			if (binarySelector.charAt(2) == '1') {
				// Main COTS
				innerOutputList.add(innerInputList.get(6));
				innerOutputList.add(innerInputList.get(7));
				innerOutputList.add(innerInputList.get(8));
			}
			if (binarySelector.charAt(3) == '1') {
				innerOutputList.add(innerInputList.get(9));
			}
			if (binarySelector.charAt(4) == '1') {
				innerOutputList.add(innerInputList.get(10));
			}
			if (binarySelector.charAt(5) == '1') {
				innerOutputList.add(innerInputList.get(11));
			}
			if (binarySelector.charAt(6) == '1') {
				innerOutputList.add(innerInputList.get(12));
			}
			if (binarySelector.charAt(7) == '1') {
				innerOutputList.add(innerInputList.get(13));
			}
			if (binarySelector.charAt(8) == '1') {
				innerOutputList.add(innerInputList.get(14));
			}
			if (binarySelector.charAt(9) == '1') {
				innerOutputList.add(innerInputList.get(15));
			}
			if (binarySelector.charAt(10) == '1') {
				innerOutputList.add(innerInputList.get(16));
			}
			if (binarySelector.charAt(11) == '1') {
				innerOutputList.add(innerInputList.get(17));
			}
			innerOutputList.add(innerInputList.get(innerInputList.size()-1));
			outputList.add(innerOutputList);
		}
//		System.out.println(outputList);
		return MLArticle.wrapper(outputList);
	}

	public static MLDataSet readMLDataSetSubsetFromFileForTuning(int radius, String binarySelector, String filename) throws Exception {
		MLMainClassPaperII mlmcpii = new MLMainClassPaperII();
		Gson gson = new Gson();
		String path = String.format("%s/%s", System.getProperty("user.dir"), mlmcpii.getClass().getPackage().getName().replace(".", "/"));
		path = path.substring(0, path.length()-16);
		path = path + "/ArticleResources/MLDataSetPaperIII/";
		String filePath = path + filename + ".json";
		TextFileHandler tfh = new TextFileHandler();
		String file = tfh.getTextFileAsString(filePath, StandardCharsets.UTF_8);
		Type arrayListType = new TypeToken<ArrayList<ArrayList<Double>>>(){}.getType();
		ArrayList<ArrayList<Double>> inputList = gson.fromJson(file, arrayListType);
		ArrayList<ArrayList<Double>> outputList = new ArrayList<ArrayList<Double>>();
		for (ArrayList<Double> innerInputList : inputList) {
			ArrayList<Double> innerOutputList = new ArrayList<Double>(); 
			if (binarySelector.charAt(0) == '1') {
				// Title COTS
				innerOutputList.add(innerInputList.get(0));
				innerOutputList.add(innerInputList.get(1));
				innerOutputList.add(innerInputList.get(2));
			}
			if (binarySelector.charAt(1) == '1') {
				// Lead COTS
				innerOutputList.add(innerInputList.get(3));
				innerOutputList.add(innerInputList.get(4));
				innerOutputList.add(innerInputList.get(5));
			}
			if (binarySelector.charAt(2) == '1') {
				// Main COTS
				innerOutputList.add(innerInputList.get(6));
				innerOutputList.add(innerInputList.get(7));
				innerOutputList.add(innerInputList.get(8));
			}
			if (binarySelector.charAt(3) == '1') {
				innerOutputList.add(innerInputList.get(9));
			}
			if (binarySelector.charAt(4) == '1') {
				innerOutputList.add(innerInputList.get(10));
			}
			if (binarySelector.charAt(5) == '1') {
				innerOutputList.add(innerInputList.get(11));
			}
			if (binarySelector.charAt(6) == '1') {
				innerOutputList.add(innerInputList.get(12));
			}
			if (binarySelector.charAt(7) == '1') {
				innerOutputList.add(innerInputList.get(13));
			}
			if (binarySelector.charAt(8) == '1') {
				innerOutputList.add(innerInputList.get(14));
			}
			if (binarySelector.charAt(9) == '1') {
				innerOutputList.add(innerInputList.get(15));
			}
			if (binarySelector.charAt(10) == '1') {
				innerOutputList.add(innerInputList.get(16));
			}
			if (binarySelector.charAt(11) == '1') {
				innerOutputList.add(innerInputList.get(17));
			}
			innerOutputList.add(innerInputList.get(innerInputList.size()-1));
			outputList.add(innerOutputList);
		}
//		System.out.println(outputList);
		return MLArticle.wrapper(outputList);
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
 		for (int i = 1; i < 512; i++) {
 			stringList.add(getBinaryStringLength9(i));
 		}
 		return stringList;
 	}
 	
	public static String getBinaryStringLength9(int i) {
 		String str = Integer.toBinaryString(i);
 		while (str.length() < 9) {
 			str = "0" + str;
 		}
 		return str;
 	}

	public static String getBinaryStringLength12(int i) {
		String str = Integer.toBinaryString(i);
		while (str.length() < 12) {
			str = "0" + str;
		}
		return str;
	}
}