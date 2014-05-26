package machineLearning;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.io.ObjectInputStream.GetField;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import newsAPI.JsonHandler;

import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataPair;
import org.encog.ml.data.basic.BasicMLDataSet;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import preProcessing.MLArticle;
import preProcessing.NewsArticleWithStemmedVersion;
import preProcessing.TextFileHandler;
import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.supervised.attribute.Discretize;

public class ArticleClassifierPaperIII {
	
	public static ClassifierAndFilter getClassifier() throws Exception {
		MLDataSet mldataset = MLMainClassPaperIII.readMLDataSetSubsetFromFileForTuning(6, "111111001001");
		ArrayList<String> options = new ArrayList<String>();
		//[-C 0.10, -M 2, -N 2, -R, -S, -J]
		options.add("-C 0.10"); // // 0.05, 0.10, 0.15, 0.20, 0.25, 0.30, 0.35, 0.40, 0.45, 0.5
		options.add("-M 2"); // 1, 2, 3, 4, 5
		options.add("-N 2"); // 1, 2, 3, 4
		options.add("-R");
		options.add("-S");
		options.add("-J");
		return J48Classifier.getClassifier(mldataset, options);
	}
	
	public static HashMap<String, String> getAnnotationMap() throws Exception {
		HashMap<String, String> map = new HashMap<String, String>();
		JsonHandler jh = new JsonHandler("/ArticleSteps/4_StemmedArticles/NEW-ARTICLE-TO-ANNOTATE-COMBINED-STEMMED-CORRECTED.json", "stemmed");
		System.out.println(jh.stemmedArticles.getNawsv().size());
		for (NewsArticleWithStemmedVersion nawsv : jh.stemmedArticles.getNawsv()) {
			System.out.println("THis far");
			System.out.println("Id=" + nawsv.id + ", sentiment=" + nawsv.getSentimentValue());
		}
		System.out.println(map.size());
		return null;
	}
	
	public static void main(String[] args) throws Exception {
//		getAnnotationMap();
//		copyAnnotatations();
//		correctWronglyEnteredSentimentClassification();
//		Classifier classifier = getClassifier();
//		writeAllArticlesToOneFile();
//		System.out.println(getAllIdsDatesAndSentiments(true));
		
		classifyAndWriteToFile();
		
//		getAllIdsDatesAndSentiments(true);
	}
	
	public static void classifyAndWriteToFile() throws Exception {
		ArrayList<ArrayList<String>> arrayListAll = getAllIdsDatesAndSentiments(false);
		MLDataSet mldataset = MLMainClassPaperIII.readMLDataSetSubsetFromFileForTuning(6, "111111001001", "allMldataset");
		ClassifierAndFilter classifierAndFilter = getClassifier();
		Instances dataSet = InstancesMLDataSetWrapper.convert(mldataset);
	    dataSet = Filter.useFilter(dataSet, classifierAndFilter.filter);
	    System.out.println("Number of classes=" + dataSet.numClasses());
	    if (arrayListAll.size() != dataSet.numInstances()) {
	    	throw new IllegalStateException("Size of arrayList=" + arrayListAll.size() + ", size of dataset = " + dataSet.numInstances());
	    }
	    int positiveCount = 0;
	    int negativeCount = 0;
		for (int index = 0; index < dataSet.numInstances(); index++) {
			double predIndex = classifierAndFilter.classifier.classifyInstance(dataSet.instance(index));
			String predString = dataSet.classAttribute().value((int) predIndex);
			int predInt = Integer.parseInt(predString);
//			if (pred == 1.0) {
//				positiveCount++;
//			} 
//			if (pred == -1.0) {
//				negativeCount++;
//			}
			double distribution[] = classifierAndFilter.classifier.distributionForInstance(dataSet.instance(index));
			String probNeg = distribution[0] + "";
			String probNeu = distribution[1] + "";
			String probPos = distribution[2] + "";
			ArrayList<String> row = arrayListAll.get(index);
			if (row.get(2).equals("n/a") && row.get(3).equals("n/a") && row.get(4).equals("n/a") && row.get(5).equals("n/a")) {
				row.set(2, predString);
				row.set(3, probNeg);
				row.set(4, probNeu);
				row.set(5, probPos);
			} else if (row.get(2).equals("n/a")) {
		    	throw new IllegalStateException("row.get(2).equals(n/a)==true");
			} else if (row.get(3).equals("n/a")) {
				throw new IllegalStateException("row.get(3).equals(n/a)==true");
			} else if (row.get(4).equals("n/a")) {
				throw new IllegalStateException("row.get(4).equals(n/a)==true");
			} else if (row.get(5).equals("n/a")) {
				throw new IllegalStateException("row.get(5).equals(n/a)==true");
			}
//			System.out.println(pred);
//			printMatrix(distribution);
		}
		writeArrayListToFile(arrayListAll, "id_date_classification_distribution");
		System.out.println("Positve count=" + positiveCount + ", negative count = " + negativeCount);
		System.out.println("Total = " + dataSet.numInstances());
		System.out.println("Done.");
	}
	
//	public static void updateAnnotated() throws Exception {
//		MLMainClassPaperII mlmcpii = new MLMainClassPaperII();
//		Gson gson = new Gson();
//		String path = String.format("%s/%s", System.getProperty("user.dir"), mlmcpii.getClass().getPackage().getName().replace(".", "/"));
//		path = path.substring(0, path.length()-16);
//		path = path + "/ArticleResources/MLDataSetPaperIII/";
//		String filePath = path + "mldataset.json";
//		TextFileHandler tfh = new TextFileHandler();
//		String file = tfh.getTextFileAsString(filePath, StandardCharsets.UTF_8);
//		Type arrayListType = new TypeToken<ArrayList<ArrayList<Double>>>(){}.getType();
//		ArrayList<ArrayList<Double>> inputList = gson.fromJson(file, arrayListType);
//
//		
//		
//		return MLArticle.wrapper(outputList);
//	}
	
	public static ArrayList<ArrayList<String>> getAllIdsDatesAndSentiments(boolean flag) throws Exception {

		ArrayList<ArrayList<String>> arrayListAnnotations = new ArrayList<ArrayList<String>>();
//		for (int i = 1; i <= 5; i++) {
//			JsonHandler jh = new JsonHandler("/ArticleSteps/4_StemmedArticles/NEW-HEGNAR-ARTICLES-STEMMED-" + i + ".json", "stemmed");
			JsonHandler jh = new JsonHandler("/ArticleSteps/4_StemmedArticles/NEW-ARTICLE-TO-ANNOTATE-COMBINED-STEMMED.json", "stemmed");
			int radius = 6;
			int positiveCount = 0;
			int negativeCount = 0;
			int neutralCount = 0;
			for (NewsArticleWithStemmedVersion nawsv : jh.stemmedArticles.getNawsv()) {
				String sentiment = "";
				if (nawsv.getSentimentValue().trim().length() != 0) {
					sentiment = MLArticlePaperIII.extractAggregateSentimentFromString(nawsv.getSentimentValue().trim()) + "";
				} else if (nawsv.getSignature().trim().length() != 0) {
					sentiment = MLArticlePaperIII.extractAggregateSentimentFromString(nawsv.getSignature().trim()) + "";
				} else {
					sentiment = "n/a";
				}
				String probNeg = "0.0";
				String probNeu = "0.0";
				String probPos = "0.0";
				if (Integer.parseInt(sentiment) == -1) {
					probNeg = "1.0";
				} else if (Integer.parseInt(sentiment) == 0) {
					probNeu = "1.0";
				} else if (Integer.parseInt(sentiment) == 1) {
					probPos = "1.0";
				} else {
					throw new IllegalStateException("Neither -1, 0 nor 1 but: " + sentiment);
				}
				arrayListAnnotations.add(new ArrayList<String>(Arrays.asList(nawsv.id, nawsv.published, sentiment, probNeg, probNeu, probPos)));
				int sentimentInt = Integer.parseInt(sentiment);
				if (sentimentInt == 1) {
					positiveCount++;
				} else if (sentimentInt == -1) {
					negativeCount++;
				} else if (sentimentInt == 0) {
					neutralCount++;
				} else {
					System.out.println("An error must have occurred: " + sentimentInt);
				}
//				arrayList.add(new ArrayList<String>());
//				arrayList.get(arrayList.size()-1).add(nawsv.id);
//				arrayList.get(arrayList.size()-1).add(nawsv.published);
//			}
//			MLDataSet mldataset = MLMainClassPaperIII.getMLDataSet(jh, radius, "111111111000000011100011111100");
//			MLMainClassPaperIII.writeMLDataSetToFileUnannotated(mldataset, radius, "mldataset-"+i);
//			System.out.println("Done with " + i);
		}
		
//		System.out.println("Positive count=" + positiveCount);
//		System.out.println("Negative count=" + negativeCount);
//		System.out.println("Neutral count=" + neutralCount);
//			
//		System.out.println(arrayListAnnotations.size());
//		System.out.println(arrayListAnnotations);
		
		ArrayList<ArrayList<String>> arrayListAll = new ArrayList<ArrayList<String>>();
		int counter = 0;
		for (int i = 1; i <= 5; i++) {
			jh = new JsonHandler("/ArticleSteps/4_StemmedArticles/NEW-HEGNAR-ARTICLES-STEMMED-" + i + ".json", "stemmed");
//		JsonHandler jh = new JsonHandler("/ArticleSteps/4_StemmedArticles/NEW-ARTICLE-TO-ANNOTATE-COMBINED-STEMMED.json", "stemmed");
			for (NewsArticleWithStemmedVersion nawsv : jh.stemmedArticles.getNawsv()) {
				String id = nawsv.id;
				String date = nawsv.getpublished();
				String sentiment = "n/a";
				String probNeg = "n/a";
				String probNeu = "n/a";
				String probPos = "n/a";
				for (ArrayList<String> list : arrayListAnnotations) {
					if (list.get(0).equals(id)) {
						sentiment = list.get(2);
						probNeg = list.get(3);
						probNeu = list.get(4);
						probPos = list.get(5);
					}
				}
				arrayListAll.add(new ArrayList<String>(Arrays.asList(id, date, sentiment, probNeg, probNeu, probPos)));
//				arrayList.add(new ArrayList<String>());
//				arrayList.get(arrayList.size()-1).add(nawsv.id);
//				arrayList.get(arrayList.size()-1).add(nawsv.published);
//				if (counter++ % 500 == 0) 
//					System.out.println(counter);
			}
//			MLDataSet mldataset = MLMainClassPaperIII.getMLDataSet(jh, radius, "111111111000000011100011111100");
//			MLMainClassPaperIII.writeMLDataSetToFileUnannotated(mldataset, radius, "mldataset-"+i);
//			System.out.println("Done with " + i);
		}
//		System.out.println(arrayListAll.size());
//		System.out.println(arrayListAll);
//		for (String key : hashMapAnnotations.keySet()) {
//			hashMapAll.get(key).set(1, hashMapAnnotations.get(key).get(1));
//		}
//		return hashMapAll;
		if (flag) {
			writeArrayListToFile(arrayListAll, "allMldatasetMetadata");
		}
		return arrayListAll;
	}
	
	public static void writeArrayListToFile(ArrayList<ArrayList<String>> arrayList, String filename) throws Exception {
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
	
	public static void writeAllArticlesToFiles() throws Exception {
		for (int i = 1; i <= 5; i++) {
			JsonHandler jh = new JsonHandler("/ArticleSteps/4_StemmedArticles/NEW-HEGNAR-ARTICLES-STEMMED-" + i + ".json", "stemmed");
			int radius = 6;
			MLDataSet mldataset = MLMainClassPaperIII.getMLDataSet(jh, radius, "111111111000000011100011111100");
			MLMainClassPaperIII.writeMLDataSetToFileUnannotated(mldataset, radius, "mldataset-"+i);
			System.out.println("Done with " + i);
		}
	}

	public static void writeAllArticlesToOneFile() throws Exception {
		MLDataSet alldata = new BasicMLDataSet();
		for (int i = 1; i <= 5; i++) {
			JsonHandler jh = new JsonHandler("/ArticleSteps/4_StemmedArticles/NEW-HEGNAR-ARTICLES-STEMMED-" + i + ".json", "stemmed");
			int radius = 6;
			MLDataSet mldataset = MLMainClassPaperIII.getMLDataSet(jh, radius, "111111111000000011100011111100");
			for (MLDataPair mlDataPair : mldataset) {
				alldata.add(mlDataPair);
			}
			System.out.println("Done with " + i);
		}
		MLMainClassPaperIII.writeMLDataSetToFileUnannotated(alldata, 6, "allMldataset");
		System.out.println("Done with everythin'");
	}
	
	public static void correctWronglyEnteredSentimentClassification() throws Exception {
		JsonHandler jh2 = new JsonHandler("/ArticleSteps/4_StemmedArticles/NEW-ARTICLE-TO-ANNOTATE-COMBINED-STEMMED.json", "stemmed");
		int counter = 0;
		for (NewsArticleWithStemmedVersion nawsv : jh2.stemmedArticles.getNawsv()) {
			String sentiment = nawsv.getSentimentValue();
			if (sentiment.length() < 2) {
				System.out.println("<" + sentiment + "> id=" + nawsv.id + ", signature=" + nawsv.getSignature());
//				nawsv.setSentimentValue(nawsv.getSignature());
				System.out.println("Counter =" + counter);
			}
			counter++;
		}
//		Gson g = new Gson();
//		ArticleClassifierPaperIII acpIII = new ArticleClassifierPaperIII();
//		String path = acpIII.getPath();
//		writeToArticleFile(g.toJson(jh2), path+"ArticleSteps/4_StemmedArticles/", "NEW-ARTICLE-TO-ANNOTATE-COMBINED-STEMMED-CORRECTED");
//		System.out.println("Done");
	}
	
	public static void copyAnnotatations() throws Exception {
		String filename = "/ArticleSteps/4_StemmedArticles/NEW-HEGNAR-ARTICLES-STEMMED-1.json";
		JsonHandler jh1 = new JsonHandler(filename, "stemmed");
		JsonHandler jh2 = new JsonHandler("/ArticleSteps/4_StemmedArticles/NEW-ARTICLE-TO-ANNOTATE-COMBINED-STEMMED-CORRECTED.json", "stemmed");
		for (NewsArticleWithStemmedVersion nawsvUnannotated : jh1.stemmedArticles.getNawsv()) {
			for (NewsArticleWithStemmedVersion nawsvAnnotated : jh2.stemmedArticles.getNawsv()) {
				System.out.println("hre");
				System.out.println(nawsvUnannotated.id + " =? " + nawsvAnnotated.id);
			}
		}
		System.out.println("Done.");
	}

	public static void writeToArticleFile(String text, String path, String name) throws IOException{
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
	
	public static void writeArticlesToFiles() throws Exception {
		JsonHandler jh = new JsonHandler("/ArticleSteps/4_StemmedArticles/NEW-ARTICLE-TO-ANNOTATE-COMBINED-STEMMED.json", "stemmed");
		int radius = 6;
		MLDataSet mldataset = getMLDataSet(jh, radius, "111111111000000011100011111100");
		writeMLDataSetToFile(mldataset, radius);
		System.out.println("Done");
	}
	
	public static MLDataSet getMLDataSet(JsonHandler jh, int radius, String binaryIndicator) throws Exception {
		MLDataSet mldataset = new BasicMLDataSet();
		int counter = 0;
		for (NewsArticleWithStemmedVersion nawsv : jh.stemmedArticles.getNawsv()) {
			MLArticlePaperIII mla = new MLArticlePaperIII(nawsv, radius);
			mldataset.add(mla.getMLDataPair(binaryIndicator));
//			System.out.println("Done with article article number " + counter++);
		}
		return mldataset;
	}
	
	private static void printMatrix(double[] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			System.out.print(matrix[i] + " ");
		}
		System.out.print("\n");
	}
}
