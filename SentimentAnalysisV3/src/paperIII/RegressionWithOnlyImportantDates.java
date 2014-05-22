package paperIII;

import java.io.IOException;
import java.util.ArrayList;

import org.joda.time.DateTime;

public class RegressionWithOnlyImportantDates {
	
	
	DateTime startDate;
	DateTime endDate;
	String ticker;
	ArrayList<tickerRegressionDate> allTickerDates;
	
	
	
	public RegressionWithOnlyImportantDates(DateTime sd, DateTime ed, String ticker) throws IOException{
		this.startDate = sd;
		this.endDate = ed;
		this.ticker = ticker;
		
		tickerRegressionOverviewGenerator trog = new tickerRegressionOverviewGenerator();
		
		this.allTickerDates = trog.initiateTickerRegressionDateObjects(ticker, sd, ed);
	}
	
	//HELPER METHOD FOR DETERMINING SPECIAL DAYS
	public boolean isSpecialDay(DateTime dt){
		boolean isSpecialDay = false;
		
		DateManipulationHelper dmh = new DateManipulationHelper();
		ArrayList<DateTime> holidays = dmh.getListExtraordinaryOfClosedStockDays();
		
		if(dt.dayOfWeek().get()==6 || dt.dayOfWeek().get()==7){
			isSpecialDay = true;
		}
		if(holidays.contains(dt)){
			isSpecialDay = true;
		}
		return isSpecialDay;
	}
	
	
	public ArrayList<tickerRegressionDate> getAllImportantTickerRegressionDates(){
		
		ArrayList<tickerRegressionDate> importantTickerDates = new ArrayList<tickerRegressionDate>();
		
		int aggregateNumberOfStockReport = 0;
		int aggregateNumberOfFinancialStockReports = 0;
		int aggregateNumberOfTradeNotificationReports = 0;
		int aggregateNumberOfArticles = 0;
		int aggregateNumberOfPositiveArticles = 0;
		int aggregateNumberOfNegativeArticles = 0;
		double aggregatePosProb = 0.0;
		double aggregateNegProb = 0.0;
		double aggregateNeutralProb = 0.0;
		
		
		boolean aggregating = false;
		
		
		for(int i=0; i<allTickerDates.size(); i++){
			
			//CHECK TO SEE IF DATE IS SPECIAL
			if(this.isSpecialDay(allTickerDates.get(i).getDate())){
				tickerRegressionDate currentSpecialDate = allTickerDates.get(i);
				
				aggregateNumberOfStockReport += currentSpecialDate.getNumberOfStockReport();
				aggregateNumberOfFinancialStockReports += currentSpecialDate.getNumberOfFinancialStockReports();
				aggregateNumberOfTradeNotificationReports += currentSpecialDate.getNumberOfTradeNotificationReports();
				aggregateNumberOfArticles += currentSpecialDate.getNumberOfPublishedArticles();
				aggregateNumberOfPositiveArticles += currentSpecialDate.getNumberOfPositivePublishedArticles();
				aggregateNumberOfNegativeArticles += currentSpecialDate.getNumberOfNegativePublishedArticles();
				aggregatePosProb += currentSpecialDate.getAggregatePosProb();
				aggregateNegProb += currentSpecialDate.getAggregateNegProb();
				aggregateNeutralProb += currentSpecialDate.getAggregateNeutralProb();
				
				aggregating = true;
					
			}
			else{
				if(aggregating){
		
					allTickerDates.get(i).setNumberOfStockReport(allTickerDates.get(i).getNumberOfStockReport()+aggregateNumberOfStockReport);
					allTickerDates.get(i).setNumberOfFinancialStockReports(allTickerDates.get(i).getNumberOfFinancialStockReports()+aggregateNumberOfFinancialStockReports);
					allTickerDates.get(i).setNumberOfTradeNotificationReports(allTickerDates.get(i).getNumberOfTradeNotificationReports()+aggregateNumberOfTradeNotificationReports);
					allTickerDates.get(i).setNumberOfPublishedArticles(allTickerDates.get(i).getNumberOfPublishedArticles()+aggregateNumberOfArticles);
					allTickerDates.get(i).setNumberOfPositivePublishedArticles(allTickerDates.get(i).getNumberOfPositivePublishedArticles()+aggregateNumberOfPositiveArticles);
					allTickerDates.get(i).setNumberOfNegativePublishedArticles(allTickerDates.get(i).getNumberOfNegativePublishedArticles()+aggregateNumberOfNegativeArticles);
					allTickerDates.get(i).setAggregatePosProb(allTickerDates.get(i).getAggregatePosProb()+aggregatePosProb);
					allTickerDates.get(i).setAggregateNegProb(allTickerDates.get(i).getAggregateNegProb()+aggregateNegProb);
					allTickerDates.get(i).setAggregateNeutralProb(allTickerDates.get(i).getAggregateNeutralProb()+aggregateNeutralProb);
					
					//RESET AGGREGATE TO NULL
					aggregateNumberOfStockReport = 0;
					aggregateNumberOfFinancialStockReports = 0;
					aggregateNumberOfTradeNotificationReports = 0;
					aggregateNumberOfArticles = 0;
					aggregateNumberOfPositiveArticles = 0;
					aggregateNumberOfNegativeArticles = 0;
					aggregatePosProb = 0.0;
					aggregateNegProb = 0.0;
					aggregateNeutralProb = 0.0;
					
					importantTickerDates.add(allTickerDates.get(i));
					aggregating = false;
				}
				else{
					importantTickerDates.add(allTickerDates.get(i));
				}
			}
		}
		return importantTickerDates;
	}
	
	public ArrayList<tickerRegressionDate> getAllWeeks(){
		
		
		ArrayList<tickerRegressionDate> tickerWeeks = new ArrayList<tickerRegressionDate>();
		
		//ARTICLE AND STOCK REPORTS
		int aggregateNumberOfStockReport = 0;
		int aggregateNumberOfFinancialStockReports = 0;
		int aggregateNumberOfTradeNotificationReports = 0;
		int aggregateNumberOfArticles = 0;
		int aggregateNumberOfPositiveArticles = 0;
		int aggregateNumberOfNegativeArticles = 0;
		double aggregatePosProb = 0.0;
		double aggregateNegProb = 0.0;
		double aggregateNeutralProb = 0.0;
		
		//OTHER VALUES
		double aggregateIndiceValue = 0.0;
		double aggregateIndiceVolume = 0.0;
		double latestIndiceClose = 0.0;
		double latestOseaxClose = 0.0;
		double aggregateSTDDEV_CHANGE_LAST_THIRTY_DAYS = 0.0;
		double aggregateOSEAX_INTRADAY_RETURN = 0.0;
		double aggregateSTTDEV_RETURN_LAST_THIRTY_DAYS = 0.0;
		double aggregateOSEAX_TOTAL_TRADED = 0.0;
		double aggregateThreeMonthNIBOR = 0.0;
		double aggregateMonthlyInflation = 0.0;
		double aggregateUnemploymentRate = 0.0;
		double aggregateOilPrice = 0.0;
		double aggregateEuroNokExchangeRate = 0.0;
		boolean latestRecession = false;
		boolean latestBull = false;
		double latestMarketValue = 0.0;
		



		for(int i=7; i<allTickerDates.size()-7; i++){
			tickerRegressionDate lastWeeksDate = allTickerDates.get(i-7);
			tickerRegressionDate thisWeeksDate = allTickerDates.get(i);
			
			
			//AGGREGATE VALUES FROM LAST WEEK
			aggregateNumberOfStockReport += lastWeeksDate.getNumberOfStockReport();
			aggregateNumberOfFinancialStockReports += lastWeeksDate.getNumberOfFinancialStockReports();
			aggregateNumberOfTradeNotificationReports += lastWeeksDate.getNumberOfTradeNotificationReports();
			aggregateNumberOfArticles += lastWeeksDate.getNumberOfPublishedArticles();
			aggregateNumberOfPositiveArticles += lastWeeksDate.getNumberOfPositivePublishedArticles();
			aggregateNumberOfNegativeArticles += lastWeeksDate.getNumberOfNegativePublishedArticles();
			aggregatePosProb += lastWeeksDate.getAggregatePosProb();
			aggregateNegProb += lastWeeksDate.getAggregateNegProb();
			aggregateNeutralProb += lastWeeksDate.getAggregateNeutralProb();
			aggregateSTDDEV_CHANGE_LAST_THIRTY_DAYS += lastWeeksDate.getOseaxChangeInStddevLastThirtyDays();
			aggregateOSEAX_INTRADAY_RETURN += lastWeeksDate.getOseaxIntradayReturn();
			aggregateSTTDEV_RETURN_LAST_THIRTY_DAYS += lastWeeksDate.getOseaxStddevReturnLastThrityDays();
			aggregateThreeMonthNIBOR += lastWeeksDate.getThreeMonthNIBOR();
			aggregateMonthlyInflation += lastWeeksDate.getMonthlyInflation();
			aggregateUnemploymentRate += lastWeeksDate.getUnemploymentRate();
			aggregateOilPrice += lastWeeksDate.getOilPrice();
			aggregateEuroNokExchangeRate += lastWeeksDate.getEuroNokExchangeRate();
			
			//AGGREGATE VALUES FOR THIS WEEK
			aggregateIndiceValue += thisWeeksDate.getIndiceValue();
			aggregateIndiceVolume += thisWeeksDate.getIndiceVolume();
			aggregateOSEAX_TOTAL_TRADED += thisWeeksDate.getTradedVolumeTotalTradedVolumeVariable();
			if(this.isSpecialDay(thisWeeksDate.getDate())){
				latestIndiceClose = thisWeeksDate.getIndiceClose();
				latestOseaxClose = thisWeeksDate.getOseaxClose();
				latestRecession = thisWeeksDate.isRecession();
				latestBull = thisWeeksDate.isBull();
				latestMarketValue = thisWeeksDate.getTradedVolumeMarketValueVariable();
			}
			
			
			
			//CHECK TO SEE IF DATE IS SPECIAL
			if(allTickerDates.get(i).getDate().getDayOfWeek()==1){
				System.out.println("THIS HAPPENED " +i+" TIMES");
				//UPDATE THIS WEEKS OBJECT
				tickerRegressionDate weekObject = new tickerRegressionDate();
				weekObject.setDate(allTickerDates.get(i).getDate());
				weekObject.setNumberOfStockReport(aggregateNumberOfStockReport);
				weekObject.setNumberOfFinancialStockReports(aggregateNumberOfFinancialStockReports);
				weekObject.setNumberOfTradeNotificationReports(aggregateNumberOfTradeNotificationReports);
				weekObject.setNumberOfPublishedArticles(aggregateNumberOfArticles);
				weekObject.setNumberOfPositivePublishedArticles(aggregateNumberOfPositiveArticles);
				weekObject.setNumberOfNegativePublishedArticles(aggregateNumberOfNegativeArticles);
				weekObject.setAggregatePosProb(aggregatePosProb);
				weekObject.setAggregateNegProb(aggregateNegProb);
				weekObject.setAggregateNeutralProb(aggregateNeutralProb);
				
				weekObject.setIndiceValue(aggregateIndiceValue);
				weekObject.setIndiceVolume(aggregateIndiceVolume);
				weekObject.setIndiceClose(latestIndiceClose);
				weekObject.setOseaxClose(latestOseaxClose);
				weekObject.setOseaxChangeInStddevLastThirtyDays(aggregateSTDDEV_CHANGE_LAST_THIRTY_DAYS/7);
				weekObject.setOseaxIntradayReturn(aggregateOSEAX_INTRADAY_RETURN/7);
				weekObject.setOseaxStddevReturnLastThrityDays(aggregateSTTDEV_RETURN_LAST_THIRTY_DAYS/7);
				weekObject.setTradedVolumeTotalTradedVolumeVariable(aggregateOSEAX_TOTAL_TRADED);
				weekObject.setThreeMonthNIBOR(aggregateThreeMonthNIBOR/7);
				weekObject.setMonthlyInflation(aggregateMonthlyInflation/7);
				weekObject.setUnemploymentRate(aggregateUnemploymentRate/7);
				weekObject.setOilPrice(aggregateOilPrice/7);
				weekObject.setEuroNokExchangeRate(aggregateEuroNokExchangeRate/7);
				weekObject.setRecession(latestRecession);
				weekObject.setBull(latestBull);
				weekObject.setTradedVolumeMarketValueVariable(latestMarketValue);
				
					
				//RESET AGGREGATE TO NULL
				aggregateNumberOfStockReport = 0;
				aggregateNumberOfFinancialStockReports = 0;
				aggregateNumberOfTradeNotificationReports = 0;
				aggregateNumberOfArticles = 0;
				aggregateNumberOfPositiveArticles = 0;
				aggregateNumberOfNegativeArticles = 0;
				aggregatePosProb = 0.0;
				aggregateNegProb = 0.0;
				aggregateNeutralProb = 0.0;
				 aggregateIndiceValue = 0.0;
				 aggregateIndiceVolume = 0.0;
				 latestIndiceClose = 0.0;
				 latestOseaxClose = 0.0;
				 aggregateSTDDEV_CHANGE_LAST_THIRTY_DAYS = 0.0;
				 aggregateOSEAX_INTRADAY_RETURN = 0.0;
				 aggregateSTTDEV_RETURN_LAST_THIRTY_DAYS = 0.0;
				 aggregateOSEAX_TOTAL_TRADED = 0.0;
				 aggregateThreeMonthNIBOR = 0.0;
				 aggregateMonthlyInflation = 0.0;
				 aggregateUnemploymentRate = 0.0;
				 aggregateOilPrice = 0.0;
				 aggregateEuroNokExchangeRate = 0.0;
				 latestRecession = false;
				 latestBull = false;
				 latestMarketValue = 0.0;
	
				 tickerWeeks.add(weekObject);
			}

		}
		return tickerWeeks;
	}
	
	
	public static void main(String args[]) throws IOException{
			
		

		DateTime startDate = new DateTime(2007,1,1,0,0);
		DateTime endDate = new DateTime(2014,1,1,0,0);
		
		RegressionWithOnlyImportantDates rwoid = new RegressionWithOnlyImportantDates(startDate, endDate, "FUNCOM");
		
		tickerRegressionOverviewGenerator trog = new tickerRegressionOverviewGenerator();
		trog.generateExcelSheet(rwoid.getAllWeeks());

	}
	
	
	
	
	
	
	
	
	
	
	

}
