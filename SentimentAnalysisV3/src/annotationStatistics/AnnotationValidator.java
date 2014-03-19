package annotationStatistics;



import java.io.*;

import java.util.ArrayList;

import newsAPI.JsonHandler;
import newsAPI.NewsArticlesRaw;

public class AnnotationValidator {
	

	public NewsArticlesRaw articles;
	
	public AnnotationValidator() throws IOException{
		
	}
	
	
	/* GETS SETNIMENT VALUE OF GIVEN ARTICLE */
	public String getSentimentValue(int articlePos){
		return this.articles.getArticles()[articlePos].sentiment_value;
	}
	
	/* PARSE SENTIMENT STRING TO INTEGER LIST */
	public ArrayList<Integer> parseSentimentValue(String sentimentValue){
		ArrayList<Integer> array = new ArrayList<Integer>(4);
		int counter = 0;
		while(counter < sentimentValue.length()) {
			char ch = sentimentValue.charAt(counter);
			if (ch == '0') {
				array.add(0);
				counter++;
			} else if (ch == '1') {
				array.add(1);
				counter++;
			} else if (ch == '-') {
				array.add(-1);
				counter++;
				counter++;
			} else {
				counter++;
			}
		}
		return array;
	}
	
	/* COMPARES ANNOTATED SENTIMENT VALUES */
	public ArrayList<Integer> compareSentimentValue(ArrayList<Integer> array1, ArrayList<Integer> array2){
		ArrayList<Integer> array = new ArrayList<Integer>(4);
		for (int i = 0; i < array1.size(); i++) {
			if (array1.get(i) == array2.get(i)) {
				array.add(1);
			} else {
				array.add(0);
			}
		}
		return array;
	}
	
	/* COMPAREs COMPLETE ANNOTATION SET*/
	public ArrayList<ArrayList<Integer>> compareResults(AnnotationValidator a1 , AnnotationValidator a2){
		ArrayList<ArrayList<Integer>> totalResults = new ArrayList<ArrayList<Integer>>(a1.getArticles().getArticles().length);
		
		for(int i = 0; i<a1.getArticles().getArticles().length; i++){
			ArrayList<Integer> compareValue = compareSentimentValue(a1.parseSentimentValue(a1.getSentimentValue(i)), a2.parseSentimentValue(a2.getSentimentValue(i)));
			totalResults.add(compareValue);
		}
		
		return totalResults;
		
	}
	
	
	public double hitRatiocompareResults(AnnotationValidator a1 , AnnotationValidator a2){
		ArrayList<ArrayList<Integer>> totalResults = compareResults(a1, a2);
		int sum = 0;
		for (ArrayList<Integer> sublist : totalResults) {
			for (Integer i : sublist) {
				sum += i;
			}
		}
		return (double) sum / (double) (4 * totalResults.size());
	}
	
	public double hitRatiocompareResultsThreeParts(AnnotationValidator a1 , AnnotationValidator a2){
		ArrayList<ArrayList<Integer>> totalResults = compareResults(a1, a2);
		int sum = 0;
		for (ArrayList<Integer> sublist : totalResults) {
			for (int i = 0; i < sublist.size()-1; i++) {
				sum += sublist.get(i);
			}
		}
		return (double) sum / (double) (3 * totalResults.size());
	}
	
	public double hitRatioCompareIndexOnly(AnnotationValidator a1 , AnnotationValidator a2, int index){
		ArrayList<ArrayList<Integer>> totalResults = compareResults(a1, a2);
		int sum = 0;
		for (ArrayList<Integer> sublist : totalResults) {
			sum += sublist.get(index);			
		}
		
		return (double) sum / ((double) totalResults.size());
	}
	
	public double hitRatioEqualOnAll(AnnotationValidator a1 , AnnotationValidator a2){
		ArrayList<ArrayList<Integer>> totalResults = compareResults(a1, a2);
		int sum = 0;
		for (ArrayList<Integer> sublist : totalResults) {
			int sublistsum = 0;
			for (Integer i : sublist) {
				sublistsum += i;
			}
			if (sublistsum == 4) {
				sum++;
			}
		}
		return (double) sum / (double) (totalResults.size());
	}
	
	public double kappaStatistic(AnnotationValidator a1 , AnnotationValidator a2){
		ArrayList<ArrayList<Integer>> annotations1= new ArrayList<ArrayList<Integer>>();
		ArrayList<ArrayList<Integer>> annotations2= new ArrayList<ArrayList<Integer>>();
		for (int i = 0; i < a1.getArticles().getArticles().length; i++) {
			annotations1.add(parseSentimentValue(a1.getSentimentValue(i)));
			annotations2.add(parseSentimentValue(a2.getSentimentValue(i)));
		}
		return calculateProbability(annotations1, -1) * calculateProbability(annotations2, -1) + 
				calculateProbability(annotations1, 0) * calculateProbability(annotations2, 0) +
				calculateProbability(annotations1, 1) * calculateProbability(annotations2, 1);
	}
	
	public double calculateProbability(ArrayList<ArrayList<Integer>> array, int value) {
		int hits = 0;
		int totalPossibleHits = array.size() * array.get(0).size();
		for (ArrayList<Integer> innerlist : array) {
			for (int element : innerlist) {
				if (element == value) {
					hits++;
				}
			}
		}
		return hits / (double) totalPossibleHits;
	}
	

	public NewsArticlesRaw getArticles() {
		return articles;
	}

	public void setArticles(NewsArticlesRaw articles) {
		this.articles = articles;
	}

	
	public static void main(String[] args) throws IOException{
		
		JsonHandler handler = new JsonHandler("/Misc/lars_annotated_1000_1426_19_13.json");
		JsonHandler handler2 = new JsonHandler("/Misc/pal_annotated_1000_1501_19_13.json");
		
		AnnotationValidator annotationValidator1 = new AnnotationValidator();
		AnnotationValidator annotationValidator2 = new AnnotationValidator();

		annotationValidator1.setArticles(handler.getArticles());
		annotationValidator2.setArticles(handler2.getArticles());
		
		//System.out.println(annotationValidator1.getSentimentValue(1));
		//System.out.println(annotationValidator2.getSentimentValue(1));
		//System.out.println(handler.getArticleObjects().get(0).getCategories());
		//System.out.println(annotationValidator1.compareResults(annotationValidator1, annotationValidator2));
		//double hitRatiocompareResults = annotationValidator1.hitRatiocompareResults(annotationValidator1, annotationValidator2);
		//System.out.println("Overall agreement: " + hitRatiocompareResults);
		//double kappaStatistic = annotationValidator1.kappaStatistic(annotationValidator1, annotationValidator2);
		//System.out.println("prE: " + kappaStatistic);
		
		int counter = 0;
		
		for(ArrayList<Integer> result : annotationValidator1.compareResults(annotationValidator1, annotationValidator2)){
			if(result.get(0) == 0 && result.get(1) == 0 && result.get(2) == 0 && result.get(3) == 0){
				System.out.println(counter + ":" + result);
				System.out.println(annotationValidator1.getArticles().getArticles()[counter].title);
			}
			
			counter++;
		}
		//System.out.println(annotationValidator1.compareResults(annotationValidator1, annotationValidator1));
		//System.out.println(annotationValidator1.compareResults(annotationValidator2, annotationValidator2));
		
		//System.out.println("Size1 " + annotationValidator1.getArticles().getArticles().length + " Size2 " + annotationValidator2.getArticles().getArticles().length);
		
		//System.out.println("Only three parts: " + annotationValidator1.hitRatiocompareResultsThreeParts(annotationValidator1, annotationValidator2));
		//System.out.println("Compare: " + annotationValidator1.compareResults(annotationValidator1, annotationValidator2));
		//System.out.println("Kappa Statistic: " + (hitRatiocompareResults-kappaStatistic) / (1-kappaStatistic));
		//System.out.println("Title agreement: " + annotationValidator1.hitRatioCompareIndexOnly(annotationValidator1, annotationValidator2, 0));
		//System.out.println("Lead agreement: " + annotationValidator1.hitRatioCompareIndexOnly(annotationValidator1, annotationValidator2, 1));
		//System.out.println("Main agreement: " + annotationValidator1.hitRatioCompareIndexOnly(annotationValidator1, annotationValidator2, 2));
		//System.out.println("Total agreement: " + annotationValidator1.hitRatioCompareIndexOnly(annotationValidator1, annotationValidator2, 3));
		//System.out.println("Agreement on all: " + annotationValidator1.hitRatioEqualOnAll(annotationValidator1, annotationValidator2));
	}

}
