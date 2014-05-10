package paperIII;

import org.joda.time.DateTime;

public class tickerRegressionDate {
	
	
	public DateTime date;
	
	
	//Independent variables
	public int numberOfPublishedArticles;
	public int numberOfPositivePublishedArticles;
	public int numberOfNegativePublishedArticles;
	public double averageSentimentValue;
	
	public double oseaxVolatility;
	public double oseaxVolatilityPercentageChange;
	
	public double threeMonthNIBOR;
	public double monthlyInflation;
	
	public double oilPrice;
	public double euroNokExchangeRate;
	
	public boolean isRecession;
	
	//Dependent variables
	public double tradedVolume;
	public double tradedVolumeMarketValueVariable;
	public double tradedVolumeTotalTradedVolumeVariable;
	
	
	
	
	public tickerRegressionDate(){
		
	}
	
	

	
	
	
	


	//GETTERS & SETTERS
	public DateTime getDate() {
		return date;
	}

	public void setDate(DateTime date) {
		this.date = date;
	}

	public int getNumberOfPublishedArticles() {
		return numberOfPublishedArticles;
	}

	public void setNumberOfPublishedArticles(int numberOfPublishedArticles) {
		this.numberOfPublishedArticles = numberOfPublishedArticles;
	}

	public int getNumberOfPositivePublishedArticles() {
		return numberOfPositivePublishedArticles;
	}

	public void setNumberOfPositivePublishedArticles(
			int numberOfPositivePublishedArticles) {
		this.numberOfPositivePublishedArticles = numberOfPositivePublishedArticles;
	}




	public int getNumberOfNegativePublishedArticles() {
		return numberOfNegativePublishedArticles;
	}




	public void setNumberOfNegativePublishedArticles(
			int numberOfNegativePublishedArticles) {
		this.numberOfNegativePublishedArticles = numberOfNegativePublishedArticles;
	}




	public double getAverageSentimentValue() {
		return averageSentimentValue;
	}




	public void setAverageSentimentValue(double averageSentimentValue) {
		this.averageSentimentValue = averageSentimentValue;
	}




	public double getOseaxVolatility() {
		return oseaxVolatility;
	}




	public void setOseaxVolatility(double oseaxVolatility) {
		this.oseaxVolatility = oseaxVolatility;
	}




	public double getOseaxVolatilityPercentageChange() {
		return oseaxVolatilityPercentageChange;
	}




	public void setOseaxVolatilityPercentageChange(
			double oseaxVolatilityPercentageChange) {
		this.oseaxVolatilityPercentageChange = oseaxVolatilityPercentageChange;
	}




	public double getThreeMonthNIBOR() {
		return threeMonthNIBOR;
	}




	public void setThreeMonthNIBOR(double threeMonthNIBOR) {
		this.threeMonthNIBOR = threeMonthNIBOR;
	}




	public double getMonthlyInflation() {
		return monthlyInflation;
	}




	public void setMonthlyInflation(double monthlyInflation) {
		this.monthlyInflation = monthlyInflation;
	}




	public double getOilPrice() {
		return oilPrice;
	}




	public void setOilPrice(double oilPrice) {
		this.oilPrice = oilPrice;
	}




	public double getEuroNokExchangeRate() {
		return euroNokExchangeRate;
	}




	public void setEuroNokExchangeRate(double euroNokExchangeRate) {
		this.euroNokExchangeRate = euroNokExchangeRate;
	}




	public boolean isRecession() {
		return isRecession;
	}




	public void setRecession(boolean isRecession) {
		this.isRecession = isRecession;
	}




	public double getTradedVolume() {
		return tradedVolume;
	}




	public void setTradedVolume(double tradedVolume) {
		this.tradedVolume = tradedVolume;
	}




	public double getTradedVolumeMarketValueVariable() {
		return tradedVolumeMarketValueVariable;
	}




	public void setTradedVolumeMarketValueVariable(
			double tradedVolumeMarketValueVariable) {
		this.tradedVolumeMarketValueVariable = tradedVolumeMarketValueVariable;
	}




	public double getTradedVolumeTotalTradedVolumeVariable() {
		return tradedVolumeTotalTradedVolumeVariable;
	}




	public void setTradedVolumeTotalTradedVolumeVariable(
			double tradedVolumeTotalTradedVolumeVariable) {
		this.tradedVolumeTotalTradedVolumeVariable = tradedVolumeTotalTradedVolumeVariable;
	}
	
	
	
	

}
