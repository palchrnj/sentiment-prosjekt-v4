package hegnarArticleScraper;

import java.util.ArrayList;

public class TickerResourceInfo implements Comparable<TickerResourceInfo> {
	
	public String ticker;
	public ArrayList<Double> indiceValues;
	public ArrayList<String> dateValues;
	public ArrayList<Integer> articleCountValues;
	
	public TickerResourceInfo(ArrayList<Double> iv, ArrayList<String> dv, ArrayList<Integer> ac){
		this.indiceValues = iv;
		this.dateValues = dv;
		this.articleCountValues = ac;
	}
	
	public double getAverageArticlesPostedInADay(){
		double average;
		double totalArticles = 0;
		
		for(int i=0; i<this.articleCountValues.size(); i++){
			totalArticles+=this.articleCountValues.get(i);
		}
		
		average = totalArticles/this.articleCountValues.size();
		
		return average;
	}
	
	public int getTotalArticleCount(){
		return this.indiceValues.size();
	}

	@Override
	public int compareTo(TickerResourceInfo o) {
			Double firstDouble = new Double(this.getAverageArticlesPostedInADay());
			Double secondDouble = new Double(o.getAverageArticlesPostedInADay());
			int retval =  firstDouble.compareTo(secondDouble);
			return retval;

	}


	

}
