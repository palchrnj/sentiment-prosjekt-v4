package paperIII;

import org.joda.time.DateTime;

public class tickerRegressionDate {
	
	
	public DateTime date;
	
	public String ticker;
	
	//Independent variables
	public int numberOfPublishedArticles;
	public int numberOfPositivePublishedArticles;
	public int numberOfNegativePublishedArticles;
	
	public double aggregatePosProb;
	public double aggregateNegProb;
	public double aggregateNeutralProb;
	public double averageSentimentValue;
	
	public double oseaxClose;
	public double oseaxChangeInStddevLastThirtyDays;
	public double oseaxIntradayReturn;
	public double oseaxStddevReturnLastThrityDays;
	
	public double indiceClose;
	public double indiceValue;
	public double indiceVolume;
	
	public double threeMonthNIBOR;
	public double monthlyInflation;
	public double unemploymentRate;
	
	public double oilPrice;
	public double euroNokExchangeRate;
	
	public boolean isRecession;
	public boolean isBull;
	
	public int numberOfStockReport;
	public int numberOfFinancialStockReports;
	public int numberOfTradeNotificationReports;
	
	//Dependent variables
	public double tradedVolume;
	public double tradedVolumeMarketValueVariable;
	public double tradedVolumeTotalTradedVolumeVariable;
	
	
	
	
	
	
	public tickerRegressionDate(){
		
	}
	
	public String toString(){
		return 
		"TICKER: "+ ticker + 
		" || DATE: "+ date.toString("YYYY.MM.DD") + 
		" || INDICE VALUE: " + indiceValue +
		" || INDICE VOLUME: " + indiceVolume +
		" || INDICE CLOSE: " + indiceClose +
		" || OSEAX CLOSE: " + oseaxClose +
		" || OSEAX STDDEV CHANGE LAST THIRTY DAYS: " + oseaxChangeInStddevLastThirtyDays + 
		" || OSEAX INTRADAY RETURN: " + oseaxIntradayReturn + 
		" || OSEAX STDDEV RETURN LAST THIRTY DAYS: " + oseaxStddevReturnLastThrityDays+
		" || OSEAX TOTAL TRADED: " + tradedVolumeTotalTradedVolumeVariable+
		" || THREE MONTH NIBOR: " + threeMonthNIBOR+
		" || MONTHY INFLATION: " + monthlyInflation+
		" || UNEMPLOYMENT RATE: " + unemploymentRate+
		" || OIL PRICE: " + oilPrice+
		" || EURO NOK EXCHANGE RATE: " + euroNokExchangeRate+
		" || RECESSION: "+isRecession+
		" || MARKET VALUE: "+tradedVolumeMarketValueVariable+
		" || BULL: " + isBull;
		
	}
	
	
	
	

	//GETTERS & SETTERS
	
	
	public DateTime getDate() {
		return date;
	}

	public double getAggregatePosProb() {
		return aggregatePosProb;
	}

	public void setAggregatePosProb(double aggregatePosProb) {
		this.aggregatePosProb = aggregatePosProb;
	}

	public double getAggregateNegProb() {
		return aggregateNegProb;
	}

	public void setAggregateNegProb(double aggregateNegProb) {
		this.aggregateNegProb = aggregateNegProb;
	}

	public double getAggregateNeutralProb() {
		return aggregateNeutralProb;
	}

	public void setAggregateNeutralProb(double aggregateNeutralProb) {
		this.aggregateNeutralProb = aggregateNeutralProb;
	}

	public int getNumberOfStockReport() {
		return numberOfStockReport;
	}

	public void setNumberOfStockReport(int numberOfStockReport) {
		this.numberOfStockReport = numberOfStockReport;
	}

	public int getNumberOfFinancialStockReports() {
		return numberOfFinancialStockReports;
	}

	public void setNumberOfFinancialStockReports(int numberOfFinancialStockReports) {
		this.numberOfFinancialStockReports = numberOfFinancialStockReports;
	}

	public int getNumberOfTradeNotificationReports() {
		return numberOfTradeNotificationReports;
	}

	public void setNumberOfTradeNotificationReports(
			int numberOfTradeNotificationReports) {
		this.numberOfTradeNotificationReports = numberOfTradeNotificationReports;
	}

	public String getTicker() {
		return ticker;
	}

	public void setTicker(String ticker) {
		this.ticker = ticker;
	}

	public double getIndiceClose() {
		return indiceClose;
	}

	public void setIndiceClose(double indiceClose) {
		this.indiceClose = indiceClose;
	}

	public double getIndiceValue() {
		return indiceValue;
	}

	public void setIndiceValue(double indiceValue) {
		this.indiceValue = indiceValue;
	}

	public double getIndiceVolume() {
		return indiceVolume;
	}

	public void setIndiceVolume(double indiceVolume) {
		this.indiceVolume = indiceVolume;
	}

	public boolean isBull() {
		return isBull;
	}



	public void setBull(boolean isBull) {
		this.isBull = isBull;
	}



	public double getOseaxClose() {
		return oseaxClose;
	}



	public void setOseaxClose(double oseaxClose) {
		this.oseaxClose = oseaxClose;
	}



	public double getOseaxChangeInStddevLastThirtyDays() {
		return oseaxChangeInStddevLastThirtyDays;
	}



	public void setOseaxChangeInStddevLastThirtyDays(
			double oseaxChangeInStddevLastThirtyDays) {
		this.oseaxChangeInStddevLastThirtyDays = oseaxChangeInStddevLastThirtyDays;
	}



	public double getOseaxIntradayReturn() {
		return oseaxIntradayReturn;
	}



	public void setOseaxIntradayReturn(double oseaxIntradayReturn) {
		this.oseaxIntradayReturn = oseaxIntradayReturn;
	}



	public double getOseaxStddevReturnLastThrityDays() {
		return oseaxStddevReturnLastThrityDays;
	}



	public void setOseaxStddevReturnLastThrityDays(
			double oseaxStddevReturnLastThrityDays) {
		this.oseaxStddevReturnLastThrityDays = oseaxStddevReturnLastThrityDays;
	}



	public double getUnemploymentRate() {
		return unemploymentRate;
	}



	public void setUnemploymentRate(double unemploymentRate) {
		this.unemploymentRate = unemploymentRate;
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
		return oseaxClose;
	}




	public void setOseaxVolatility(double oseaxVolatility) {
		this.oseaxClose = oseaxVolatility;
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
