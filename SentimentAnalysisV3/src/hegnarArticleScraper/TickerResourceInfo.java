package hegnarArticleScraper;

import java.util.ArrayList;

public class TickerResourceInfo implements Comparable<TickerResourceInfo> {
	
	public String ticker;
	public double CompanyMarketValue;
	public double volumeTradeVariable;
	public double averageTotalTrade;
	public int totalNumberOfArticles;
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
			Double firstDouble = new Double(this.getVolumeTradeVariable());
			Double secondDouble = new Double(o.getVolumeTradeVariable());
			int retval =  firstDouble.compareTo(secondDouble);
			return retval;

	}
	public double getCompanyMarketValue() {
		return CompanyMarketValue;
	}

	public void setCompanyMarketValue(double companyMarketValue) {
		CompanyMarketValue = companyMarketValue;
	}

	public double getVolumeTradeVariable() {
		return volumeTradeVariable;
	}

	public void setVolumeTradeVariable(double valumeTradeVariable) {
		this.volumeTradeVariable = valumeTradeVariable;
	}

	public double getAverageTotalTrade() {
		return averageTotalTrade;
	}

	public void setAverageTotalTrade(double averageTotalTrade) {
		this.averageTotalTrade = averageTotalTrade;
	}

	public int getTotalNumberOfArticles() {
		return totalNumberOfArticles;
	}

	public void setTotalNumberOfArticles(int totalNumberOfArticles) {
		this.totalNumberOfArticles = totalNumberOfArticles;
	}
	
	public String toString(){
		return "Ticker: " + this.ticker + " || " +  
				"Volume divided by trades: " + this.getVolumeTradeVariable() + " || " +
				"Total number of articles: " + this.getTotalNumberOfArticles() + " || " + 
				"Avg articles posted per day: " + this.getAverageArticlesPostedInADay() + " || " +  
				"Market value: " + this.getCompanyMarketValue() + " || " +
				"Average number of trades: " + this.getAverageTotalTrade();  
	}


	

}
