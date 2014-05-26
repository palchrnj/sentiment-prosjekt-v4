package paperIII;

import hegnarArticleScraper.NewswebStockReportDateValue;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import machineLearning.MLArticlePaperIII;
import newsAPI.JsonHandler;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.joda.time.DateTime;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.sun.org.apache.bcel.internal.generic.Type;

import preProcessing.NewsArticleWithStemmedVersion;
import preProcessing.NewsArticlesWithStemmedVersion;
import preProcessing.TextFileHandler;
import utils.ExcelStockParser;
import utils.StockGrapher;

public class tickerRegressionOverviewGenerator {
	
	
	public ArrayList<tickerRegressionDate> tickerRegressionDates;
	
	public tickerRegressionOverviewGenerator(){
		
		
	}
	
	//DATESTRING TO DATETIME
	public DateTime stringToDateTime(String date){
		
		String year = date.substring(0,4);
		String month = date.substring(4,6);
		String day = date.substring(6,8);
		
	//	System.out.println(year + " " + month + " " + day);
		int yearAsInteger = Integer.parseInt(year);
		int monthAsInteger = Integer.parseInt(month);
		int dayAsInteger = Integer.parseInt(day);
		
		DateTime newDateTime = new DateTime(yearAsInteger, monthAsInteger, dayAsInteger, 0, 0);

		return newDateTime;
	}
	
	
	public DateTime marketValueDateStringToDateTime(String date){
//		System.out.println(date.split("-")[0]);
//		System.out.println(date.split("-")[1]);
//		System.out.println(date.split("-")[2]);
//		
		String year = date.split("-")[2];
		String day = date.split("-")[0];
		

		int yearAsInteger = Integer.parseInt(year);
		int monthAsInteger = this.getMonthFromString(date.split("-")[1]);
		int dayAsInteger = Integer.parseInt(day);
		
//		System.out.println(year + " " + monthAsInteger + " " + day);
		
		DateTime newDateTime = new DateTime(yearAsInteger, monthAsInteger, dayAsInteger, 0, 0);

		return newDateTime;
	}
	public DateTime inflationDateStringToDateTime(String date){
//		System.out.println(date.split("-")[0]);
//		System.out.println(date.split("-")[1]);
//		System.out.println(date.split("-")[2]);
//		
		String year = date.split("M")[0];
		String month = date.split("M")[1];
		

		int yearAsInteger = Integer.parseInt(year);
		int monthAsInteger = Integer.parseInt(month);

		
//		System.out.println(year + " " + monthAsInteger + " " + day);
		
		DateTime newDateTime = new DateTime(yearAsInteger, monthAsInteger, 1, 0, 0);

		return newDateTime;
	}
	
	//-------------------------//
	
	public HSSFSheet getTickerExcelSheet(String ticker, String fileEnding) throws IOException{
		ExcelStockParser esp = new ExcelStockParser();
		String indiceSheet = "/ChosenIndices/"+ticker+"."+fileEnding;
		InputStream myxls = new FileInputStream(esp.getPath()+indiceSheet);
		HSSFWorkbook wb = new HSSFWorkbook(myxls);
		
		HSSFSheet sheet = wb.getSheetAt(0);   //first sheet
		
		return sheet;
	}
	public HSSFSheet getMarketValueExcelSheet(String ticker, String fileEnding) throws IOException{
		ExcelStockParser esp = new ExcelStockParser();
		String indiceSheet = "/MarketValue/"+ticker+"."+fileEnding;
		InputStream myxls = new FileInputStream(esp.getPath()+indiceSheet);
		HSSFWorkbook wb = new HSSFWorkbook(myxls);
		
		HSSFSheet sheet = wb.getSheetAt(0);   //first sheet
		
		return sheet;
	}
	public HSSFSheet getIndependantVariableExcelSheet(String name, String fileEnding) throws IOException{
		ExcelStockParser esp = new ExcelStockParser();
		String indiceSheet = "/IndependentVariables/"+name+"."+fileEnding;
		InputStream myxls = new FileInputStream(esp.getPath()+indiceSheet);
		HSSFWorkbook wb = new HSSFWorkbook(myxls);
		
		HSSFSheet sheet = wb.getSheetAt(0);   //first sheet
		
		return sheet;
	}
	
	//GENERATE DateTime values HashMap for indices
	public HashMap<DateTime, indiceDateRow> getIndexHashmap(String ticker) throws IOException{
		HashMap<DateTime, indiceDateRow> indexHashmap = new HashMap<>();
		
		HSSFSheet tickerExcelSheet = this.getTickerExcelSheet(ticker, "xls");
		for(int i=2; i<tickerExcelSheet.getLastRowNum()+1; i++){
			//System.out.println("DATE: " + tickerExcelSheet.getRow(i).getCell(0).toString());
			Double dateDouble = Double.parseDouble(tickerExcelSheet.getRow(i).getCell(0).toString());
			String doubleString = String.format(".%f", dateDouble);
			//System.out.println("DOUBLE STRING: " + doubleString);
			
			
			DateTime tickerDate = this.stringToDateTime(doubleString.replace(".", "").substring(0, 8));
			
			double close = Double.parseDouble(tickerExcelSheet.getRow(i).getCell(1).toString());
			double volume = Double.parseDouble(tickerExcelSheet.getRow(i).getCell(2).toString());
			double value = Double.parseDouble(tickerExcelSheet.getRow(i).getCell(3).toString());
			
			indiceDateRow idr = new indiceDateRow(close, volume, value);
			
			indexHashmap.put(tickerDate, idr);
			
			//System.out.println(i +"CLOSE: " + close + " VOLUME: " + volume + "VALUE: " +value);
	
		}
		return indexHashmap;
	}
	//GET ARTICLES AND SENTIMENT STATS FOR DATE
	//GET COMPANY MARKET VALUE FOR DATE
	public HashMap<DateTime, Double> getMarketValueForDate(String ticker) throws IOException{
		HashMap<DateTime, Double> dateMarketValueHashmap = new HashMap<>();
		
		HSSFSheet marketValueExcelSheet = this.getMarketValueExcelSheet(ticker, "xls");
		//System.out.println(marketValueExcelSheet.getLastRowNum());
		for(int i=0; i<marketValueExcelSheet.getLastRowNum()+1; i++){

			DateTime marketValueDate = this.marketValueDateStringToDateTime(marketValueExcelSheet.getRow(i).getCell(0).toString());
			double marketValueForDate = Double.parseDouble(marketValueExcelSheet.getRow(i).getCell(1).toString());
			dateMarketValueHashmap.put(marketValueDate, marketValueForDate);

		}
		return dateMarketValueHashmap;
	}
	//GET OILPRICE FOR DATE
	public HashMap<DateTime, Double> getOilPriceForDate() throws IOException{
		HashMap<DateTime, Double> dateOilPriceHashMap = new HashMap<>();
		
		HSSFSheet oilPriceExcelSheet = this.getIndependantVariableExcelSheet("oilprice", "xls");

		
		for(int i=1; i<oilPriceExcelSheet.getLastRowNum(); i++){
			
			DateTime oilPriceDate = this.marketValueDateStringToDateTime(oilPriceExcelSheet.getRow(i).getCell(0).toString());
			double oilPriceValueDate = Double.parseDouble(oilPriceExcelSheet.getRow(i).getCell(1).toString());
			
			//System.out.println("DATE: " + oilPriceDate.toString() + " VALUE: " + oilPriceValueDate);
			
			dateOilPriceHashMap.put(oilPriceDate, oilPriceValueDate);

		}
		
		return dateOilPriceHashMap;
	}
	//GET INFLATION FOR DATE
	public HashMap<DateTime, Double> getInflationForDate() throws IOException{
		HashMap<DateTime, Double> inflationDateHashMap = new HashMap<>();
		HSSFSheet inflationExcelSheet = this.getIndependantVariableExcelSheet("inflation", "xls");
		
		
		for(int i=1; i<inflationExcelSheet.getLastRowNum(); i++){
			//System.out.println(inflationExcelSheet.getRow(i).getCell(0).toString());
			DateTime inflationDate = this.inflationDateStringToDateTime(inflationExcelSheet.getRow(i).getCell(0).toString());
			double inflationValueForDate = Double.parseDouble(inflationExcelSheet.getRow(i).getCell(1).toString());
			
			//System.out.println("DATE: " + inflationDate.toString() + " VALUE: " + inflationValueForDate);
			
			inflationDateHashMap.put(inflationDate, inflationValueForDate);
		}
		return inflationDateHashMap;
	}
	//GET OSEAX FOR DATE
	public HashMap<DateTime, oseaxValueHolderDateObject> getOseaxForDate() throws IOException{
		HashMap<DateTime, oseaxValueHolderDateObject> oseaxDateHashmap = new HashMap<>();
		HSSFSheet oseaxExcelSheet = this.getIndependantVariableExcelSheet("oseax", "xls");
		
		
		for(int i=1; i<oseaxExcelSheet.getLastRowNum(); i++){
			//System.out.println(oseaxExcelSheet.getRow(i).getCell(0).toString());
			
			Double dateDouble = Double.parseDouble(oseaxExcelSheet.getRow(i).getCell(0).toString());
			String doubleString = String.format(".%f", dateDouble);
			//System.out.println("DOUBLE STRING: " + doubleString);
			
			
			DateTime oseaxDate = this.stringToDateTime(doubleString.replace(".", "").substring(0, 8));
			

			double close = Double.parseDouble(oseaxExcelSheet.getRow(i).getCell(1).toString());
			double ir = Double.parseDouble(oseaxExcelSheet.getRow(i).getCell(2).toString());
			double srltd = Double.parseDouble(oseaxExcelSheet.getRow(i).getCell(3).toString());
			double pcisltd = Double.parseDouble(oseaxExcelSheet.getRow(i).getCell(4).toString());
			double totalTraded = Double.parseDouble(oseaxExcelSheet.getRow(i).getCell(7).toString());
			double r = Double.parseDouble(oseaxExcelSheet.getRow(i).getCell(5).toString());
			double b = Double.parseDouble(oseaxExcelSheet.getRow(i).getCell(6).toString());
			
			boolean bull = false;
			boolean recession = false;
			
			if(r == 1){
				recession = true;
			}
			if(b == 1){
				bull = true;
			}
			
			

			oseaxValueHolderDateObject ovhdo = new oseaxValueHolderDateObject(close, ir, srltd, pcisltd, recession, bull,totalTraded);
//			System.out.println("DATE: " + oseaxDate.toString() + " VALUE: " + ovhdo.getClose() + " IR: " + ovhdo.getIntradayReturn() + " SRLTD:  " + ovhdo.getStddevReturnLastThirtyDays() + 
//					" PCILSTD: " + ovhdo.getChangeInStddevLastThirtyDays() + " RECESSION: " + ovhdo.recession + " BULL: " + ovhdo.bull);
			
			oseaxDateHashmap.put(oseaxDate, ovhdo);
		}
		return oseaxDateHashmap;
	}
	//GET NIBOR FOR DATE
	public HashMap<DateTime, Double> getNiborForDate() throws IOException{
		
		HashMap<DateTime, Double> niborDateHashMap = new HashMap<>();
		HSSFSheet niborExcelSheet = this.getIndependantVariableExcelSheet("niborNew", "xls");
		
	//	System.out.println(niborExcelSheet.getLastRowNum());
		for(int i=1; i<niborExcelSheet.getLastRowNum() && niborExcelSheet.getRow(i).getCell(0).toString().length() > 0; i++){
	//		System.out.println(niborExcelSheet.getRow(i).getCell(0).toString());
			DateTime niborDate = this.marketValueDateStringToDateTime(niborExcelSheet.getRow(i).getCell(0).toString());
			double niborForValueForDate = Double.parseDouble(niborExcelSheet.getRow(i).getCell(1).toString());
			
			//System.out.println("DATE: " + niborDate.toString() + " VALUE: " + niborForValueForDate);
			
			niborDateHashMap.put(niborDate, niborForValueForDate);

		}
		
		return niborDateHashMap;
	}
	
	//GET UNEMPLOYMNET RATE FOR DATE
	public HashMap<DateTime, Double> getUnemploymentForDate() throws IOException{
		
		HashMap<DateTime, Double> unemploymentDateHashMap = new HashMap<>();
		HSSFSheet unemploymentExcelSheet = this.getIndependantVariableExcelSheet("unemployment", "xls");
		
		//System.out.println(unemploymentExcelSheet.getLastRowNum());
		for(int i=0; i<unemploymentExcelSheet.getLastRowNum()+1 && unemploymentExcelSheet.getRow(i).getCell(0).toString().length() > 0; i++){
			
			//System.out.println(unemploymentExcelSheet.getRow(i).getCell(0).toString());
			DateTime unemploymentDate = this.inflationDateStringToDateTime(unemploymentExcelSheet.getRow(i).getCell(0).toString());
			double unemploymentValueForDate = Double.parseDouble(unemploymentExcelSheet.getRow(i).getCell(1).toString());
			
			//System.out.println("DATE: " + unemploymentDate.toString() + " VALUE: " + unemploymentValueForDate);
			
			unemploymentDateHashMap.put(unemploymentDate, unemploymentValueForDate);
	
		}
		
		return unemploymentDateHashMap;
	}
	
	//GET EURO/NOK EXCHANGE RATE FOR DATE
	public HashMap<DateTime, Double> getEuroNokExchangeRateForDate() throws IOException{
		
		HashMap<DateTime, Double> exchangeRateHashMap = new HashMap<>();
		HSSFSheet exchangeRateExcelSheet = this.getIndependantVariableExcelSheet("eurnokNew", "xls");
		
		double previousDateExchangeRateValue = 0.0;
		
		//System.out.println(exchangeRateExcelSheet.getLastRowNum());
		for(int i=3; i<exchangeRateExcelSheet.getLastRowNum()+1 && exchangeRateExcelSheet.getRow(i).getCell(0).toString().length() > 0; i++){
			
		//	System.out.println(exchangeRateExcelSheet.getRow(i).getCell(0).toString());
			
			DateTime exchangeRateDate = this.marketValueDateStringToDateTime(exchangeRateExcelSheet.getRow(i).getCell(0).toString());
		//	System.out.println("value" + exchangeRateExcelSheet.getRow(i).getCell(1));
			
			double exchangeRateValueForDate = 0.0;
			if(exchangeRateExcelSheet.getRow(i).getCell(1).toString().length()>0){
				exchangeRateValueForDate = Double.parseDouble(exchangeRateExcelSheet.getRow(i).getCell(1).toString());
				previousDateExchangeRateValue = Double.parseDouble(exchangeRateExcelSheet.getRow(i).getCell(1).toString());
			}
			else{
				exchangeRateValueForDate = previousDateExchangeRateValue;
			}
			
//			System.out.println("DATE: " + exchangeRateDate.toString() + " VALUE: " + exchangeRateValueForDate);
			
			exchangeRateHashMap.put(exchangeRateDate, exchangeRateValueForDate);
			
		}
		
		return exchangeRateHashMap;
	}
	
	//GET STOCK REPORT HASHMAP FOR TICKER
	public HashMap<DateTime, stockReportCounter> getStockReportDateHashMapForTicker(String ticker) throws JsonSyntaxException, IOException{
		HashMap<DateTime, stockReportCounter> stockReportDateValueHashMap = new HashMap<>();
		
		TextFileHandler tfh = new TextFileHandler();
		Gson g = new Gson();
		
		ArrayList<NewswebStockReportDateValue> stockReportFromTicker = new ArrayList<>();
		
		
		stockReportFromTicker = g.fromJson(tfh.getCombinedStockReport(ticker), new TypeToken<List<NewswebStockReportDateValue>>(){}.getType());
		
		
		stockReportCounter src = new stockReportCounter();
		DateTime surveyorDate = new DateTime();
		
		
		for(int i=0; i<stockReportFromTicker.size(); i++){
		
			NewswebStockReportDateValue stockReport = stockReportFromTicker.get(i);

			String[] dateArray = stockReport.getFormatedDate().split("\\.");

			DateTime date = new DateTime(Integer.parseInt(dateArray[2]), Integer.parseInt(dateArray[1]), Integer.parseInt(dateArray[0]), 0,0);
			
			if(surveyorDate.equals(date)){
				//System.out.println("OPPDATERER SRC: " + src.getNumberOfStockReport() +" " + src.getNumberOfFinancialReports() + " "  +src.getNumberOfTradeNotificationReports());
				
				src.setNumberOfStockReport(src.getNumberOfStockReport()+1);
				if(stockReport.getCategory().equals("MELDEPLIKTIG HANDEL")){
					src.setNumberOfTradeNotificationReports(src.getNumberOfTradeNotificationReports()+1);
				}
				if(stockReport.getCategory().equals("FINANSIELL RAPPORTERING")){
					src.setNumberOfFinancialReports(src.getNumberOfFinancialReports()+1);
				}
				
				//System.out.println("OPPDATERERT --SRC: " + src.getNumberOfStockReport() +" " + src.getNumberOfFinancialReports() + " "  +src.getNumberOfTradeNotificationReports());
			}
			else{
				//System.out.println("PUTTER: DATO-" + surveyorDate.toString() + " OG VERDI: " + src.getNumberOfStockReport() + " " + src.getNumberOfFinancialReports() + " " + src.getNumberOfTradeNotificationReports());
				stockReportDateValueHashMap.put(surveyorDate, src);
				src = new stockReportCounter();
				surveyorDate = date;
				
				src.setNumberOfStockReport(src.getNumberOfStockReport()+1);
				if(stockReport.getCategory().equals("MELDEPLIKTIG HANDEL")){
					src.setNumberOfTradeNotificationReports(src.getNumberOfTradeNotificationReports()+1);
				}
				if(stockReport.getCategory().equals("FINANSIELL RAPPORTERING")){
					src.setNumberOfFinancialReports(src.getNumberOfFinancialReports()+1);
				}
			}
			
			
			
		}
		
		return stockReportDateValueHashMap;
		
	}
	
	//SENTIMENT
	public HashMap<DateTime, DateArticleSentimentCounter> getSentimentHashMap(String ticker) throws IOException{
		JsonHandler stemmedHandler1 = new JsonHandler("ArticleSteps/4_StemmedArticles/NEW-HEGNAR-ARTICLES-STEMMED-1.json", "stemmed");
		NewsArticlesWithStemmedVersion stemmedArticles1 = stemmedHandler1.getStemmedArticles();
		
		JsonHandler stemmedHandler2 = new JsonHandler("ArticleSteps/4_StemmedArticles/NEW-HEGNAR-ARTICLES-STEMMED-2.json", "stemmed");
		NewsArticlesWithStemmedVersion stemmedArticles2 = stemmedHandler2.getStemmedArticles();
		
		JsonHandler stemmedHandler3 = new JsonHandler("ArticleSteps/4_StemmedArticles/NEW-HEGNAR-ARTICLES-STEMMED-3.json", "stemmed");
		NewsArticlesWithStemmedVersion stemmedArticles3 = stemmedHandler3.getStemmedArticles();
		
		JsonHandler stemmedHandler4 = new JsonHandler("ArticleSteps/4_StemmedArticles/NEW-HEGNAR-ARTICLES-STEMMED-4.json", "stemmed");
		NewsArticlesWithStemmedVersion stemmedArticles4 = stemmedHandler4.getStemmedArticles();
		
		JsonHandler stemmedHandler5 = new JsonHandler("ArticleSteps/4_StemmedArticles/NEW-HEGNAR-ARTICLES-STEMMED-5.json", "stemmed");
		NewsArticlesWithStemmedVersion stemmedArticles5 = stemmedHandler5.getStemmedArticles();
		
		
		NewsArticlesWithStemmedVersion allStemmedArticlesWithSentimentValue = new NewsArticlesWithStemmedVersion();
		allStemmedArticlesWithSentimentValue.getNawsv().addAll(stemmedArticles1.getNawsv());
		allStemmedArticlesWithSentimentValue.getNawsv().addAll(stemmedArticles2.getNawsv());
		allStemmedArticlesWithSentimentValue.getNawsv().addAll(stemmedArticles3.getNawsv());
		allStemmedArticlesWithSentimentValue.getNawsv().addAll(stemmedArticles4.getNawsv());
		allStemmedArticlesWithSentimentValue.getNawsv().addAll(stemmedArticles5.getNawsv());
		
		//TESTING PURPOSES
		System.out.println("SIZE: " + allStemmedArticlesWithSentimentValue.getNawsv().size());
		
		TextFileHandler tfh = new TextFileHandler();
		Gson g = new Gson();
		
		ArrayList<ArrayList<String>> idDateClassificationDistribution = g.fromJson(tfh.getIdDateClassificationDistribution(), ArrayList.class); 
		
		HashMap<DateTime, DateArticleSentimentCounter> sentimentHashMap = new HashMap<>();
		
		for(int i=0; i<allStemmedArticlesWithSentimentValue.getNawsv().size(); i++){
			if(allStemmedArticlesWithSentimentValue.getNawsv().get(i).getTickerList().contains(ticker)){
			
				NewsArticleWithStemmedVersion nawsv = allStemmedArticlesWithSentimentValue.getNawsv().get(i);
				
				DateTime dt = new DateTime();
				try {
					String dateOfArticle = nawsv.getpublished().split("T")[0];
					int year = 2000+Integer.parseInt(dateOfArticle.split("-")[0]);
					int month = Integer.parseInt(dateOfArticle.split("-")[1]);
					int day = Integer.parseInt(dateOfArticle.split("-")[2]);
					
					 dt = new DateTime(year,month,day,0,0);
					
				} catch (Exception e) {
					 dt = new DateTime(2014,1,1,0,0);
				}

				DateArticleSentimentCounter dasc = new DateArticleSentimentCounter();
				
				//IF ROW ALREADY EXISTS
				if(sentimentHashMap.get(dt)==null){
					for(int j=0; j<idDateClassificationDistribution.size(); j++){
						String idDateClassification = idDateClassificationDistribution.get(j).get(0);
						if(nawsv.getId().equals(idDateClassification)){
							dasc.setNumberOfArticles(1);
//							System.out.println("Den er lik");
						
							String idDateClassificationSentiment = idDateClassificationDistribution.get(j).get(2);
							String idDateClassificationNegProb = idDateClassificationDistribution.get(j).get(3);
							String idDateClassificationNeutralProb = idDateClassificationDistribution.get(j).get(4);
							String idDateClassificationSentPosProb = idDateClassificationDistribution.get(j).get(5);
							
							
							int sentimentValue = Integer.parseInt(idDateClassificationSentiment);
							double posProb = Double.parseDouble(idDateClassificationSentPosProb);
							double negProb = Double.parseDouble(idDateClassificationNegProb);
							double neutralProb = Double.parseDouble(idDateClassificationNeutralProb);

							//GET SENTIMENT FROM DATE CLASSIFICATION DISTRIBUTION
							if(sentimentValue == 1){
								dasc.setNumberOfPositiveArticles(1);
							}
							else if(sentimentValue == -1){
								dasc.setNumberOfNegativeArticles(1);
							}
							else{
								dasc.setNumberOfNeutralArticles(1);
							}
							dasc.setAggregateNegativeProb(negProb);
							dasc.setAggregateNeutralProb(neutralProb);
							dasc.setAggregatePositiveProb(posProb);
						}
					}
					
					sentimentHashMap.put(dt, dasc);
					//System.out.println("STRING LEGNTH:" + nawsv.getSentimentValue().length() + "  SENTIMENT: " +sentimentValue + " SENTIMENT STRING:" +  nawsv.getSentimentValue().replaceAll("\\s+",""));
					//System.out.println("PUTTER: DATO: " + dt.toString("YYYY.MM.dd") + " ANTALL ARTIKLER " + dasc.getNumberOfArticles() + "  ANTALL POSITIVE ARTIKLER " + dasc.getNumberOfPositiveArticles() + " ANTALL NEGATIVE ARTIILER: " + dasc.getNumberOfNegativeArticles());
				}
				else{
					int currentNumberOfArticles = sentimentHashMap.get(dt).getNumberOfArticles();
					int currentNumberOfPositiveArticles = sentimentHashMap.get(dt).getNumberOfPositiveArticles();
					int currentNumberOfNegativeArticles = sentimentHashMap.get(dt).getNumberOfNegativeArticles();
					int currentNumberOfNeutralArticles = sentimentHashMap.get(dt).getNumberOfNeutralArticles();
					double currentAggregateNegProb = sentimentHashMap.get(dt).getAggregateNegativeProb();
					double currentAggregatePosProb = sentimentHashMap.get(dt).getAggregatePositiveProb();
					double currentAggregateNeutralProb = sentimentHashMap.get(dt).getAggregateNeutralProb();
					
					dasc.setNumberOfArticles(currentNumberOfArticles+1);
					
					for(int j=0; j<idDateClassificationDistribution.size(); j++){
						String idDateClassification = idDateClassificationDistribution.get(j).get(0);
						
						if(nawsv.getId().equals(idDateClassification)){
			
//							System.out.println("Den er lik");
						
							String idDateClassificationSentiment = idDateClassificationDistribution.get(j).get(2);
							String idDateClassificationNegProb = idDateClassificationDistribution.get(j).get(3);
							String idDateClassificationNeutralProb = idDateClassificationDistribution.get(j).get(4);
							String idDateClassificationSentPosProb = idDateClassificationDistribution.get(j).get(5);
							
							
							int sentimentValue = Integer.parseInt(idDateClassificationSentiment);
							double posProb = Double.parseDouble(idDateClassificationSentPosProb);
							double negProb = Double.parseDouble(idDateClassificationNegProb);
							double neutralProb = Double.parseDouble(idDateClassificationNeutralProb);

							//GET SENTIMENT FROM DATE CLASSIFICATION DISTRIBUTION
							if(sentimentValue == 1){
								dasc.setNumberOfPositiveArticles(currentNumberOfPositiveArticles+1);
							}
							else if(sentimentValue == -1){
								dasc.setNumberOfNegativeArticles(currentNumberOfNegativeArticles+1);
							}
							else{
								dasc.setNumberOfNeutralArticles(currentNumberOfNeutralArticles+1);
							}
							dasc.setAggregateNegativeProb(currentAggregateNegProb+negProb);
							dasc.setAggregateNeutralProb(currentAggregatePosProb+posProb);
							dasc.setAggregatePositiveProb(currentAggregateNeutralProb+neutralProb);
						}
					}
					
					sentimentHashMap.put(dt, dasc);
					//System.out.println("OPPDATERER: DATO: " + dt.toString("YYYY.MM.dd") + " ANTALL ARTIKLER " + dasc.getNumberOfArticles() + "  ANTALL POSITIVE ARTIKLER " + dasc.getNumberOfPositiveArticles() + " ANTALL NEGATIVE ARTIILER: " + dasc.getNumberOfNegativeArticles());
					
				}
			}
			
			
			
		}
		return sentimentHashMap;
	}
	
	
	
	
	//INITIATE ALL VARIABLES
	public ArrayList<tickerRegressionDate> initiateTickerRegressionDateObjects(String ticker, DateTime startDate, DateTime endDate) throws IOException{
		
		System.out.println("INITIATING VARIABLES");
		HashMap<DateTime, Double> oilPriceDateValueHashMap = this.getOilPriceForDate();
		HashMap<DateTime, Double> eurNokExchangeRateDateValueHashMap = this.getEuroNokExchangeRateForDate();
		HashMap<DateTime, Double> inflationDateValueHashMap = this.getInflationForDate();
		HashMap<DateTime, Double> marketValueHashMap = this.getMarketValueForDate(ticker);
		HashMap<DateTime, Double> niborDateValueHashMap = this.getNiborForDate();
		HashMap<DateTime, Double> unemploymentDateValueHashMap = this.getUnemploymentForDate();
		HashMap<DateTime, oseaxValueHolderDateObject> oseaxHashmap = this.getOseaxForDate();
		HashMap<DateTime, indiceDateRow> indiceOverview = this.getIndexHashmap(ticker);
		HashMap<DateTime, stockReportCounter> stockReportOverview = this.getStockReportDateHashMapForTicker(ticker);
		HashMap<DateTime, DateArticleSentimentCounter> sentimentOverview = this.getSentimentHashMap(ticker);
		
		int counter = 0;
		
		
		DateTime currentDate = startDate;
		ArrayList<tickerRegressionDate> tickerRegressionDates = new ArrayList<>();
		
		while(!currentDate.equals(endDate)){
			//System.out.println("CURRENT DATE: " + currentDate.toString());
			
			tickerRegressionDate currentDateRegressionObject = new tickerRegressionDate();
			//INITIATE DATES
			currentDateRegressionObject.setDate(currentDate);
			//OILPRICE
			double oilPrice = 0.0;
			DateTime changedDateOilPrice = currentDate;
			while(oilPrice == 0.0){
				try {
					//System.out.println("OILPRICE DENNE ER EVIG: " + oilPrice + " DATE " + changedDateOilPrice.toString());
					oilPrice = oilPriceDateValueHashMap.get(changedDateOilPrice);
					currentDateRegressionObject.setOilPrice(oilPrice);
				} catch (Exception e) {
					changedDateOilPrice = changedDateOilPrice.minusDays(1);
				}
			}
			
			//EURNOK EXCHANGE RATE
			double eurNok = 0.0;
			DateTime changedDateEURNOK = currentDate;
			while(eurNok == 0.0){
				try {
				//	System.out.println("EXCHANGERATE DENNE ER EVIG: " + eurNok);
					eurNok = eurNokExchangeRateDateValueHashMap.get(changedDateEURNOK);
					currentDateRegressionObject.setEuroNokExchangeRate(eurNok);
				} catch (Exception e) {
					changedDateEURNOK = changedDateEURNOK.minusDays(1);
				}
			}
			//INFLATION
			double inflation = -9999999999999999999999.0;
			DateTime changedDateINFLATION = currentDate;
			while(inflation == -9999999999999999999999.0){
				try {
				//	System.out.println("INFLATION DENNE ER EVIG: " + inflation + " INFALTEIONM DATE " + changedDateINFLATION.toString("YYYY.MM.dd"));
					inflation = inflationDateValueHashMap.get(changedDateINFLATION);
					currentDateRegressionObject.setMonthlyInflation(inflation);
				} catch (Exception e) {
					changedDateINFLATION = changedDateINFLATION.minusDays(1);
				}
			}
			//MARKET VALUE
			double marketValue = -999999999999.0;
			DateTime changedDateMARKETVALUE = currentDate;
			while(marketValue == -999999999999.0){
				try {
					//System.out.println("MARKETVALUE DENNE ER EVIG: " + marketValue);
					marketValue = marketValueHashMap.get(changedDateMARKETVALUE);
					currentDateRegressionObject.setTradedVolumeMarketValueVariable(marketValue);
				} catch (Exception e) {
					changedDateMARKETVALUE = changedDateMARKETVALUE.minusDays(1);
				}
			}
			//NIBOR
			double threeMonthNibor = 0.0;
			DateTime changedDateNIBOR = currentDate;
			while(threeMonthNibor == 0.0){
				try {
					//System.out.println("NIBOR DENNE ER EVIG: " + threeMonthNibor);
					threeMonthNibor = niborDateValueHashMap.get(changedDateNIBOR);
					currentDateRegressionObject.setThreeMonthNIBOR(threeMonthNibor);
				} catch (Exception e) {
					changedDateNIBOR = changedDateNIBOR.minusDays(1);
				}
			}
			//RECESSION
			double recessionDouble = 0.0;
			DateTime changedDateRecession = currentDate;
			while(recessionDouble == 0.0){
				try {
					boolean recession = oseaxHashmap.get(changedDateRecession).isRecession();
					//System.out.println("RECESSION DENNE ER EVIG: " + recession);
					currentDateRegressionObject.setRecession(recession);
					recessionDouble = 1.0;
				} catch (Exception e) {
					changedDateRecession = changedDateRecession.minusDays(1);
				}
			}
			//BULL
			double bullDouble = 0.0;
			DateTime changedDateBULL = currentDate;
			while(bullDouble == 0.0){
				try {
					boolean bull = oseaxHashmap.get(changedDateBULL).isBull();
				//	System.out.println("BULL DENNE ER EVIG: " + bull);
					currentDateRegressionObject.setBull(bull);
					bullDouble = 1.0;
				} catch (Exception e) {
					changedDateBULL = changedDateBULL.minusDays(1);
				}
			}
		
			//OSEAX STDDEV
			double STDDEV = 0.0;
			DateTime changedDateSTDDEV = currentDate;
			while(STDDEV == 0.0){
				try {
				//	System.out.println("STDDEV DENNE ER EVIG: " + threeMonthNibor);
					STDDEV = oseaxHashmap.get(changedDateSTDDEV).getStddevReturnLastThirtyDays();
					currentDateRegressionObject.setOseaxStddevReturnLastThrityDays(STDDEV);
				} catch (Exception e) {
					changedDateSTDDEV = changedDateSTDDEV.minusDays(1);
				}
			}
			//OSEAX CLOSE
			double oseaxClose = 0.0;
			DateTime changedDateOseaxClose = currentDate;
			while(oseaxClose == 0.0){
				try {
				//	System.out.println("OSEAX CLOSE DENNE ER EVIG: " + oseaxClose);
					oseaxClose = oseaxHashmap.get(changedDateOseaxClose).getClose();
					currentDateRegressionObject.setOseaxClose(oseaxClose);
				} catch (Exception e) {
					changedDateOseaxClose = changedDateOseaxClose.minusDays(1);
				}
			}
			//OSEAX INTRADAY RETUNR
			double oseaxIntradayReturn = -999999999999999999999999.0;
			DateTime changedDateOseaxINTRDAYRETURN = currentDate;
			while(oseaxIntradayReturn == -999999999999999999999999.0){
				try {
				//	System.out.println("OSEAX INTRADAY DENNE ER EVIG: " + oseaxIntradayReturn);
					oseaxIntradayReturn = oseaxHashmap.get(changedDateOseaxINTRDAYRETURN).getIntradayReturn();
					currentDateRegressionObject.setOseaxIntradayReturn(oseaxIntradayReturn);
				} catch (Exception e) {
					changedDateOseaxINTRDAYRETURN = changedDateOseaxINTRDAYRETURN.minusDays(1);
				}
			}

			//OSEAX STDDEV CHANGE RETUNR
			double oseaxSTDDEVCHANGE = -999999999999999999999999.0;
			DateTime changeDateOseaxSTDDEVCHANGE = currentDate;
			while(oseaxSTDDEVCHANGE == -999999999999999999999999.0){
				try {
				//	System.out.println("OSEAX STDDEV CHANGE DENNE ER EVIG: " + oseaxSTDDEVCHANGE);
					oseaxSTDDEVCHANGE = oseaxHashmap.get(changeDateOseaxSTDDEVCHANGE).getChangeInStddevLastThirtyDays();
					currentDateRegressionObject.setOseaxChangeInStddevLastThirtyDays(oseaxSTDDEVCHANGE);
				} catch (Exception e) {
					changeDateOseaxSTDDEVCHANGE = changeDateOseaxSTDDEVCHANGE.minusDays(1);
				}
			}
			//OSEAX TOTAL TRADED
			double oseaxTotalTraded = -999999999999999999999999.0;
			DateTime changeDateOseaxTOTALTRADED = currentDate;
			while(oseaxTotalTraded == -999999999999999999999999.0){
				try {
				//	System.out.println("OSEAX TOTAL TRADED DENNE ER EVIG: " + oseaxTotalTraded);
					oseaxTotalTraded = oseaxHashmap.get(changeDateOseaxTOTALTRADED).getTotalTraded();
					currentDateRegressionObject.setTradedVolumeTotalTradedVolumeVariable(oseaxTotalTraded);
				} catch (Exception e) {
					changeDateOseaxTOTALTRADED = changeDateOseaxTOTALTRADED.minusDays(1);
				}
			}
			//UNEMPLOYMENT RATE
			double unemploymentRate = -999999999999999999999999.0;
			DateTime changeDateUnemployment = currentDate;
			while(unemploymentRate == -999999999999999999999999.0){
				try {
				//	System.out.println("UNEMPLOYEMENT ER EVIG: " + oseaxSTDDEVCHANGE);
					unemploymentRate = unemploymentDateValueHashMap.get(changeDateUnemployment);
					currentDateRegressionObject.setUnemploymentRate(unemploymentRate);
				} catch (Exception e) {
					changeDateUnemployment = changeDateUnemployment.minusDays(1);
				}
			}
			
			//INDICE VALUE
			double indiceValue = -999999999999999999999999.0;
			DateTime changeDateIndiceValue = currentDate;
			while(indiceValue == -999999999999999999999999.0){
				try {
				//	System.out.println("INDICE VALUE DENNE ER EVIG: " + oseaxSTDDEVCHANGE);
					indiceValue = indiceOverview.get(changeDateIndiceValue).getValue();
					currentDateRegressionObject.setIndiceValue(indiceValue);
				} catch (Exception e) {
					changeDateIndiceValue = changeDateIndiceValue.minusDays(1);
				}
			}
			//INDICE VOLUME
			double indiceVolume = -999999999999999999999999.0;
			DateTime changeDateIndiceVolume = currentDate;
			while(indiceVolume == -999999999999999999999999.0){
				try {
				//	System.out.println("INDICE VOLUME DENNE ER EVIG: " + oseaxSTDDEVCHANGE);
					indiceVolume = indiceOverview.get(changeDateIndiceVolume).getVolume();
					currentDateRegressionObject.setIndiceVolume(indiceVolume);
				} catch (Exception e) {
					changeDateIndiceVolume = changeDateIndiceVolume.minusDays(1);
				}
			}
			//INDICE CLOSE
			double indiceClose = -999999999999999999999999.0;
			DateTime changeDateIndiceClose = currentDate;
			while(indiceClose == -999999999999999999999999.0){
				try {
				//	System.out.println("INDICE CLOSE DENNE ER EVIG: " + oseaxSTDDEVCHANGE);
					indiceClose = indiceOverview.get(changeDateIndiceClose).getClose();
					currentDateRegressionObject.setIndiceClose(indiceClose);
				} catch (Exception e) {
					changeDateIndiceClose = changeDateIndiceClose.minusDays(1);
				}
			}
			
			//STOCK REPORTS
			if(stockReportOverview.get(currentDate)!=null){
				currentDateRegressionObject.setNumberOfStockReport(stockReportOverview.get(currentDate).getNumberOfStockReport());
				currentDateRegressionObject.setNumberOfFinancialStockReports(stockReportOverview.get(currentDate).getNumberOfFinancialReports());
				currentDateRegressionObject.setNumberOfTradeNotificationReports(stockReportOverview.get(currentDate).getNumberOfTradeNotificationReports());
			}
			else{
				currentDateRegressionObject.setNumberOfStockReport(0);
				currentDateRegressionObject.setNumberOfFinancialStockReports(0);
				currentDateRegressionObject.setNumberOfTradeNotificationReports(0);
			}
			
			//SENTIMENT
			//STOCK REPORTS
			if(sentimentOverview.get(currentDate)!=null){
				currentDateRegressionObject.setNumberOfPublishedArticles(sentimentOverview.get(currentDate).getNumberOfArticles());
				currentDateRegressionObject.setNumberOfPositivePublishedArticles(sentimentOverview.get(currentDate).getNumberOfPositiveArticles());
				currentDateRegressionObject.setNumberOfNegativePublishedArticles(sentimentOverview.get(currentDate).getNumberOfNegativeArticles());
				currentDateRegressionObject.setAggregateNegProb(sentimentOverview.get(currentDate).getAggregateNegativeProb());
				currentDateRegressionObject.setAggregatePosProb(sentimentOverview.get(currentDate).getAggregatePositiveProb());
				currentDateRegressionObject.setAggregateNeutralProb(sentimentOverview.get(currentDate).getAggregateNeutralProb());
			}
			else{
				currentDateRegressionObject.setNumberOfPublishedArticles(0);
				currentDateRegressionObject.setNumberOfPositivePublishedArticles(0);
				currentDateRegressionObject.setNumberOfNegativePublishedArticles(0);
				currentDateRegressionObject.setAggregateNegProb(0.0);
				currentDateRegressionObject.setAggregatePosProb(0.0);
				currentDateRegressionObject.setAggregateNeutralProb(0.0);
			
			}
			
			
			currentDateRegressionObject.setTicker(ticker);
			tickerRegressionDates.add(currentDateRegressionObject);
			System.out.println(counter + "  " + currentDateRegressionObject.toString());
			counter++;
			currentDate = currentDate.plusDays(1);
		}
		return tickerRegressionDates;
		
		
		
		
	}
	
	
	//SUPPORT METHODS
	public int getMonthFromString(String month){
		int monthNumber = 0;
		switch (month) {
			case "jan":
				monthNumber =1;
				break;
			case "Jan":
				monthNumber =1;
				break;
			case "feb":
				monthNumber =2;
				break;
			case "Feb":
				monthNumber =2;
				break;
			case "mar":
				monthNumber =3;
				break;
			case "Mar":
				monthNumber =3;
				break;
			case "apr":
				monthNumber =4;
				break;
			case "Apr":
				monthNumber =4;
				break;
			case "mai":
				monthNumber =5;
				break;
			case "Mai":
				monthNumber =5;
				break;
			case "may":
				monthNumber = 5;
				break;
			case "May":
				monthNumber = 5;
				break;
			case "jun":
				monthNumber =6;
				break;
			case "Jun":
				monthNumber =6;
				break;
			case "jul":
				monthNumber =7;
				break;
			case "Jul":
				monthNumber =7;
				break;
			case "aug":
				monthNumber =8;
				break;
			case "Aug":
				monthNumber =8;
				break;
			case "sep":
				monthNumber =9;
				break;
			case "Sep":
				monthNumber =9;
				break;
			case "okt":
				monthNumber =10;
				break;
			case "Okt":
				monthNumber =10;
				break;
			case "oct":
				monthNumber =10;
				break;
			case "Oct":
				monthNumber =10;
				break;
			case "nov":
				monthNumber =11;
				break;
			case "Nov":
				monthNumber =11;
				break;
			case "des":
				monthNumber =12;
				break;
			case "Des":
				monthNumber =12;
				break;
			case "dec":
				monthNumber =12;
				break;
			case "Dec":
				monthNumber =12;
				break;
			default:
				System.out.println("Kunne ikke finne m�ned:" + month);
				break;
			}
		return monthNumber;
	}
	
	public void generateExcelSheet(ArrayList<tickerRegressionDate> listToExcel, String ticker) throws IOException{
		Workbook wb = new HSSFWorkbook(); 
		Sheet tickerSheet = wb.createSheet("EXCEL");
		
		   CellStyle style = wb.createCellStyle();
		    Font font = wb.createFont();
		    font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		    style.setFont(font);
	       
		
		   Row row = tickerSheet.createRow((short)0);
		   Cell cellHeader1 = row.createCell(0);
		   cellHeader1.setCellValue("DATE");
		   cellHeader1.setCellStyle(style);
		   
		   Cell cellHeader2 = row.createCell(1);
		   cellHeader2.setCellValue("INDICE VALUE");
		   cellHeader2.setCellStyle(style);
		   
		   Cell cellHeader3 = row.createCell(2);
		   cellHeader3.setCellValue("INDICE VOLUME");
		   cellHeader3.setCellStyle(style);
		   
		   Cell cellHeader4 = row.createCell(3);
		   cellHeader4.setCellValue("INDICE CLOSE");
		   cellHeader4.setCellStyle(style);
		   
		   Cell cellHeader5 = row.createCell(4);
		   cellHeader5.setCellValue("OSEAX CLOSE");
		   cellHeader5.setCellStyle(style);
		   
		   Cell cellHeader6 = row.createCell(5);
		   cellHeader6.setCellValue("OSEAX STDDEV CHANGE LAST THIRTY DAYS");
		   cellHeader6.setCellStyle(style);
		   
		   Cell cellHeader7 = row.createCell(6);
		   cellHeader7.setCellValue("OSEAX OSEAX INTRADAY RETURN");
		   cellHeader7.setCellStyle(style);
		   
		   Cell cellHeader8 = row.createCell(7);
		   cellHeader8.setCellValue("OSEAX STDDEV RETURN LAST THIRTY DAYS");
		   cellHeader8.setCellStyle(style);
		   
		   Cell cellHeader9 = row.createCell(8);
		   cellHeader9.setCellValue("OSEAX TOTAL TRADED");
		   cellHeader9.setCellStyle(style);
		   
		   Cell cellHeader10 = row.createCell(9);
		   cellHeader10.setCellValue("THREE MONTH NIBOR");
		   cellHeader10.setCellStyle(style);
		   
		   Cell cellHeader11 = row.createCell(10);
		   cellHeader11.setCellValue("MONTHY INFLATION");
		   cellHeader11.setCellStyle(style);
		   
		   Cell cellHeader12 = row.createCell(11);
		   cellHeader12.setCellValue("UNEMPLOYMENT RATE");
		   cellHeader12.setCellStyle(style);
		   
		   Cell cellHeader13 = row.createCell(12);
		   cellHeader13.setCellValue("OIL PRICE");
		   cellHeader13.setCellStyle(style);
		   
		   Cell cellHeader14 = row.createCell(13);
		   cellHeader14.setCellValue("EURO NOK EXCHANGE RATE");
		   cellHeader14.setCellStyle(style);
		   
		   Cell cellHeader15 = row.createCell(14);
		   cellHeader15.setCellValue("RECESSION");
		   cellHeader15.setCellStyle(style);
		   
		   Cell cellHeader16 = row.createCell(15);
		   cellHeader16.setCellValue("BULL");
		   cellHeader16.setCellStyle(style);
		   
		   Cell cellHeader17 = row.createCell(16);
		   cellHeader17.setCellValue("MARKET VALUE");
		   cellHeader17.setCellStyle(style);
		   
		   Cell cellHeader18 = row.createCell(17);
		   cellHeader18.setCellValue("STOCK REPORT");
		   cellHeader18.setCellStyle(style);
		   
		   Cell cellHeader19 = row.createCell(18);
		   cellHeader19.setCellValue("FINANCIAL STOCK REPORTS");
		   cellHeader19.setCellStyle(style);
		   
		   Cell cellHeader20 = row.createCell(19);
		   cellHeader20.setCellValue("TRADE NOTIFICATION STOCK REPORTS");
		   cellHeader20.setCellStyle(style);
		   
		   Cell cellHeader21 = row.createCell(20);
		   cellHeader21.setCellValue("NUMBER OF ARTICLES");
		   cellHeader21.setCellStyle(style);
		   
		   Cell cellHeader22 = row.createCell(21);
		   cellHeader22.setCellValue("NUMBER OF POSITIVE ARTICLES");
		   cellHeader22.setCellStyle(style);
		   
		   Cell cellHeader23 = row.createCell(22);
		   cellHeader23.setCellValue("NUMBER OF NEGATIVE ARTICLES");
		   cellHeader23.setCellStyle(style);
		   
		   Cell cellHeader24 = row.createCell(23);
		   cellHeader24.setCellValue("AGGREGATE POS PROB");
		   cellHeader24.setCellStyle(style);
		   
		   Cell cellHeader25 = row.createCell(24);
		   cellHeader25.setCellValue("AGGREGATE NEG PROB");
		   cellHeader25.setCellStyle(style);
		   
		   Cell cellHeader26 = row.createCell(25);
		   cellHeader26.setCellValue("AGGREGATE NEUTRAL PROB");
		   cellHeader26.setCellStyle(style);
		   
		
		
		for(int i=1; i<listToExcel.size(); i++){
			//CREATE ROW
		    Row dateRow = tickerSheet.createRow((short)i);
		   
		    
		    dateRow.createCell(0).setCellValue(listToExcel.get(i).getDate().toString("dd.MM.YYYY"));
		    dateRow.createCell(1).setCellValue(listToExcel.get(i).getIndiceValue());
		    dateRow.createCell(2).setCellValue(listToExcel.get(i).getIndiceVolume());
		    dateRow.createCell(3).setCellValue(listToExcel.get(i).getIndiceClose());
		    dateRow.createCell(4).setCellValue(listToExcel.get(i).getOseaxClose());
		    dateRow.createCell(5).setCellValue(listToExcel.get(i).getOseaxChangeInStddevLastThirtyDays());
		    dateRow.createCell(6).setCellValue(listToExcel.get(i).getOseaxIntradayReturn());
		    dateRow.createCell(7).setCellValue(listToExcel.get(i).getOseaxStddevReturnLastThrityDays());
		    dateRow.createCell(8).setCellValue(listToExcel.get(i).getTradedVolumeTotalTradedVolumeVariable());
		    dateRow.createCell(9).setCellValue(listToExcel.get(i).getThreeMonthNIBOR());
		    dateRow.createCell(10).setCellValue(listToExcel.get(i).getMonthlyInflation());
		    dateRow.createCell(11).setCellValue(listToExcel.get(i).getUnemploymentRate());
		    dateRow.createCell(12).setCellValue(listToExcel.get(i).getOilPrice());
		    dateRow.createCell(13).setCellValue(listToExcel.get(i).getEuroNokExchangeRate());
		    if (listToExcel.get(i).isRecession())
		    	dateRow.createCell(14).setCellValue(1.0);
		    else{
		    	dateRow.createCell(14).setCellValue(0.0);
		    }
		    if (listToExcel.get(i).isBull())
			    dateRow.createCell(15).setCellValue(1.0);
		    else{
			    dateRow.createCell(15).setCellValue(0.0);
		    }
		    dateRow.createCell(16).setCellValue(listToExcel.get(i).getTradedVolumeMarketValueVariable());
		    dateRow.createCell(17).setCellValue(listToExcel.get(i).getNumberOfStockReport());
		    dateRow.createCell(18).setCellValue(listToExcel.get(i).getNumberOfFinancialStockReports());
		    dateRow.createCell(19).setCellValue(listToExcel.get(i).getNumberOfTradeNotificationReports());
		    dateRow.createCell(20).setCellValue(listToExcel.get(i).getNumberOfPublishedArticles());
		    dateRow.createCell(21).setCellValue(listToExcel.get(i).getNumberOfPositivePublishedArticles());
		    dateRow.createCell(22).setCellValue(listToExcel.get(i).getNumberOfNegativePublishedArticles());
		    dateRow.createCell(23).setCellValue(listToExcel.get(i).getAggregatePosProb());
		    dateRow.createCell(24).setCellValue(listToExcel.get(i).getAggregateNegProb());
		    dateRow.createCell(25).setCellValue(listToExcel.get(i).getAggregateNeutralProb());
		    
		}
		
		 tickerSheet.autoSizeColumn((short)0);
		 tickerSheet.autoSizeColumn((short)1);
		 tickerSheet.autoSizeColumn((short)2);
		 tickerSheet.autoSizeColumn((short)3);
		 tickerSheet.autoSizeColumn((short)4);
		 tickerSheet.autoSizeColumn((short)5);
		 tickerSheet.autoSizeColumn((short)6);
		 tickerSheet.autoSizeColumn((short)7);
		 tickerSheet.autoSizeColumn((short)8);
		 tickerSheet.autoSizeColumn((short)9);
		 tickerSheet.autoSizeColumn((short)10);
		 tickerSheet.autoSizeColumn((short)11);
		 tickerSheet.autoSizeColumn((short)12);
		 tickerSheet.autoSizeColumn((short)13);
		 tickerSheet.autoSizeColumn((short)14);
		 tickerSheet.autoSizeColumn((short)15);
		 tickerSheet.autoSizeColumn((short)16);
		 tickerSheet.autoSizeColumn((short)17);
		 tickerSheet.autoSizeColumn((short)18);
		 tickerSheet.autoSizeColumn((short)19);
		 tickerSheet.autoSizeColumn((short)20);
		 tickerSheet.autoSizeColumn((short)21);
		 tickerSheet.autoSizeColumn((short)22);
		 tickerSheet.autoSizeColumn((short)23);
		 tickerSheet.autoSizeColumn((short)24);
		 tickerSheet.autoSizeColumn((short)25);
		 
		 
<<<<<<< HEAD
		 FileOutputStream fileOut = new FileOutputStream(this.getPath()+"/TickerRegressionGeneratedExcelSheets/EXCEL-" + ticker + "-IMPORTANT-DATES.xls");
=======
		 FileOutputStream fileOut = new FileOutputStream(this.getPath()+"/TickerRegressionGeneratedExcelSheets/Monthly/EXCEL-YAR-MONTHLY-2008-2014.xls");
>>>>>>> 6042190964907157ffd8d3dbd2935c71a98c3235
		 wb.write(fileOut);
		 fileOut.close();
		
	}
	
//	public void generateExcelSheetWithMultipleTickers(ArrayList<ArrayList<tickerRegressionDate>> listsToExcel) throws IOException{
//		
//		Workbook wb = new HSSFWorkbook(); 
//		Sheet tickerSheet = wb.createSheet("EXCEL");
//		
//		   CellStyle style = wb.createCellStyle();
//		    Font font = wb.createFont();
//		    font.setBoldweight(Font.BOLDWEIGHT_BOLD);
//		    style.setFont(font);
//	       
//		   Row row = tickerSheet.createRow((short)0);
//		   Cell cellHeader1 = row.createCell(0);
//		   cellHeader1.setCellValue("TICKER");
//		   cellHeader1.setCellStyle(style);
//		   
//		   Cell cellHeader2 = row.createCell(1);
//		   cellHeader2.setCellValue("BLUECHIP");
//		   cellHeader2.setCellStyle(style);
//		    
//		   Cell cellHeader3 = row.createCell(2);
//		   cellHeader3.setCellValue("DATE");
//		   cellHeader3.setCellStyle(style);
//		   
//		   Cell cellHeader4 = row.createCell(3);
//		   cellHeader4.setCellValue("INDICE VALUE");
//		   cellHeader4.setCellStyle(style);
//		   
//		   Cell cellHeader5 = row.createCell(4);
//		   cellHeader5.setCellValue("INDICE VOLUME");
//		   cellHeader5.setCellStyle(style);
//		   
//		   Cell cellHeader6 = row.createCell(5);
//		   cellHeader6.setCellValue("INDICE CLOSE");
//		   cellHeader6.setCellStyle(style);
//		   
//		   Cell cellHeader7 = row.createCell(6);
//		   cellHeader7.setCellValue("OSEAX CLOSE");
//		   cellHeader7.setCellStyle(style);
//		   
//		   Cell cellHeader8 = row.createCell(7);
//		   cellHeader8.setCellValue("OSEAX STDDEV CHANGE LAST THIRTY DAYS");
//		   cellHeader8.setCellStyle(style);
//		   
//		   Cell cellHeader9 = row.createCell(8);
//		   cellHeader9.setCellValue("OSEAX OSEAX INTRADAY RETURN");
//		   cellHeader9.setCellStyle(style);
//		   
//		   Cell cellHeader10 = row.createCell(9);
//		   cellHeader10.setCellValue("OSEAX STDDEV RETURN LAST THIRTY DAYS");
//		   cellHeader10.setCellStyle(style);
//		   
//		   Cell cellHeader11 = row.createCell(10);
//		   cellHeader11.setCellValue("OSEAX TOTAL TRADED");
//		   cellHeader11.setCellStyle(style);
//		   
//		   Cell cellHeader12 = row.createCell(11);
//		   cellHeader12.setCellValue("THREE MONTH NIBOR");
//		   cellHeader12.setCellStyle(style);
//		   
//		   Cell cellHeader13 = row.createCell(12);
//		   cellHeader13.setCellValue("MONTHY INFLATION");
//		   cellHeader13.setCellStyle(style);
//		   
//		   Cell cellHeader14 = row.createCell(13);
//		   cellHeader14.setCellValue("UNEMPLOYMENT RATE");
//		   cellHeader14.setCellStyle(style);
//		   
//		   Cell cellHeader15 = row.createCell(14);
//		   cellHeader15.setCellValue("OIL PRICE");
//		   cellHeader15.setCellStyle(style);
//		   
//		   Cell cellHeader16 = row.createCell(15);
//		   cellHeader16.setCellValue("EURO NOK EXCHANGE RATE");
//		   cellHeader16.setCellStyle(style);
//		   
//		   Cell cellHeader17 = row.createCell(16);
//		   cellHeader17.setCellValue("RECESSION");
//		   cellHeader17.setCellStyle(style);
//		   
//		   Cell cellHeader18 = row.createCell(17);
//		   cellHeader18.setCellValue("BULL");
//		   cellHeader18.setCellStyle(style);
//		   
//		   Cell cellHeader19 = row.createCell(18);
//		   cellHeader19.setCellValue("MARKET VALUE");
//		   cellHeader19.setCellStyle(style);
//		   
//		   Cell cellHeader20 = row.createCell(19);
//		   cellHeader20.setCellValue("STOCK REPORT");
//		   cellHeader20.setCellStyle(style);
//		   
//		   Cell cellHeader21 = row.createCell(20);
//		   cellHeader21.setCellValue("FINANCIAL STOCK REPORTS");
//		   cellHeader21.setCellStyle(style);
//		   
//		   Cell cellHeader22 = row.createCell(21);
//		   cellHeader22.setCellValue("TRADE NOTIFICATION STOCK REPORTS");
//		   cellHeader22.setCellStyle(style);
//		   
//		   Cell cellHeader23 = row.createCell(22);
//		   cellHeader23.setCellValue("NUMBER OF ARTICLES");
//		   cellHeader23.setCellStyle(style);
//		   
//		   Cell cellHeader24 = row.createCell(23);
//		   cellHeader24.setCellValue("NUMBER OF POSITIVE ARTICLES");
//		   cellHeader24.setCellStyle(style);
//		   
//		   Cell cellHeader25 = row.createCell(24);
//		   cellHeader25.setCellValue("NUMBER OF NEGATIVE ARTICLES");
//		   cellHeader25.setCellStyle(style);
//		   
//		   Cell cellHeader26 = row.createCell(25);
//		   cellHeader26.setCellValue("AGGREGATE POS PROB");
//		   cellHeader26.setCellStyle(style);
//		   
//		   Cell cellHeader27 = row.createCell(26);
//		   cellHeader27.setCellValue("AGGREGATE NEG PROB");
//		   cellHeader27.setCellStyle(style);
//		   
//		   Cell cellHeader28 = row.createCell(27);
//		   cellHeader28.setCellValue("AGGREGATE NEUTRAL PROB");
//		   cellHeader28.setCellStyle(style);
//		
//		for(int tickerCounter=0; tickerCounter<listsToExcel.size(); tickerCounter++){
//			ArrayList<tickerRegressionDate> listToExcel = listsToExcel.get(tickerCounter);
//		
//			
//			for(int i=0; i<listToExcel.size(); i++){
//				int rowCounter = i;
//				
//				if(tickerCounter>0){
//					rowCounter = listsToExcel.get(i-1).size()+i;
//				}
//				  
//				//CREATE ROW
//			    Row dateRow = tickerSheet.createRow((short)rowCounter);
//			    
//			    dateRow.createCell(0).setCellValue("TICKER" + i);
//			    dateRow.createCell(1).setCellValue("BIG");
//			    dateRow.createCell(2).setCellValue(listToExcel.get(i).getDate().toString("dd.MM.YYYY"));
//			    dateRow.createCell(3).setCellValue(listToExcel.get(i).getIndiceValue());
//			    dateRow.createCell(4).setCellValue(listToExcel.get(i).getIndiceVolume());
//			    dateRow.createCell(5).setCellValue(listToExcel.get(i).getIndiceClose());
//			    dateRow.createCell(6).setCellValue(listToExcel.get(i).getOseaxClose());
//			    dateRow.createCell(7).setCellValue(listToExcel.get(i).getOseaxChangeInStddevLastThirtyDays());
//			    dateRow.createCell(8).setCellValue(listToExcel.get(i).getOseaxIntradayReturn());
//			    dateRow.createCell(9).setCellValue(listToExcel.get(i).getOseaxStddevReturnLastThrityDays());
//			    dateRow.createCell(10).setCellValue(listToExcel.get(i).getTradedVolumeTotalTradedVolumeVariable());
//			    dateRow.createCell(11).setCellValue(listToExcel.get(i).getThreeMonthNIBOR());
//			    dateRow.createCell(12).setCellValue(listToExcel.get(i).getMonthlyInflation());
//			    dateRow.createCell(13).setCellValue(listToExcel.get(i).getUnemploymentRate());
//			    dateRow.createCell(14).setCellValue(listToExcel.get(i).getOilPrice());
//			    dateRow.createCell(15).setCellValue(listToExcel.get(i).getEuroNokExchangeRate());
//			    if (listToExcel.get(i).isRecession())
//			    	dateRow.createCell(16).setCellValue(1.0);
//			    else{
//			    	dateRow.createCell(16).setCellValue(0.0);
//			    }
//			    if (listToExcel.get(i).isBull())
//				    dateRow.createCell(17).setCellValue(1.0);
//			    else{
//				    dateRow.createCell(17).setCellValue(0.0);
//			    }
//			    dateRow.createCell(18).setCellValue(listToExcel.get(i).getTradedVolumeMarketValueVariable());
//			    dateRow.createCell(19).setCellValue(listToExcel.get(i).getNumberOfStockReport());
//			    dateRow.createCell(20).setCellValue(listToExcel.get(i).getNumberOfFinancialStockReports());
//			    dateRow.createCell(21).setCellValue(listToExcel.get(i).getNumberOfTradeNotificationReports());
//			    dateRow.createCell(22).setCellValue(listToExcel.get(i).getNumberOfPublishedArticles());
//			    dateRow.createCell(23).setCellValue(listToExcel.get(i).getNumberOfPositivePublishedArticles());
//			    dateRow.createCell(24).setCellValue(listToExcel.get(i).getNumberOfNegativePublishedArticles());
//			    dateRow.createCell(25).setCellValue(listToExcel.get(i).getAggregatePosProb());
//			    dateRow.createCell(26).setCellValue(listToExcel.get(i).getAggregateNegProb());
//			    dateRow.createCell(27).setCellValue(listToExcel.get(i).getAggregateNeutralProb());
//			    
//			}
//			
//			 tickerSheet.autoSizeColumn((short)0);
//			 tickerSheet.autoSizeColumn((short)1);
//			 tickerSheet.autoSizeColumn((short)2);
//			 tickerSheet.autoSizeColumn((short)3);
//			 tickerSheet.autoSizeColumn((short)4);
//			 tickerSheet.autoSizeColumn((short)5);
//			 tickerSheet.autoSizeColumn((short)6);
//			 tickerSheet.autoSizeColumn((short)7);
//			 tickerSheet.autoSizeColumn((short)8);
//			 tickerSheet.autoSizeColumn((short)9);
//			 tickerSheet.autoSizeColumn((short)10);
//			 tickerSheet.autoSizeColumn((short)11);
//			 tickerSheet.autoSizeColumn((short)12);
//			 tickerSheet.autoSizeColumn((short)13);
//			 tickerSheet.autoSizeColumn((short)14);
//			 tickerSheet.autoSizeColumn((short)15);
//			 tickerSheet.autoSizeColumn((short)16);
//			 tickerSheet.autoSizeColumn((short)17);
//			 tickerSheet.autoSizeColumn((short)18);
//			 tickerSheet.autoSizeColumn((short)19);
//			 tickerSheet.autoSizeColumn((short)20);
//			 tickerSheet.autoSizeColumn((short)21);
//			 tickerSheet.autoSizeColumn((short)22);
//			 tickerSheet.autoSizeColumn((short)23);
//			 tickerSheet.autoSizeColumn((short)24);
//			 tickerSheet.autoSizeColumn((short)25);
//			 tickerSheet.autoSizeColumn((short)26);
//			 tickerSheet.autoSizeColumn((short)27);
//			 
//			 
//			 FileOutputStream fileOut = new FileOutputStream(this.getPath()+"/TickerRegressionGeneratedExcelSheets/FUNCOM-DAILY-2007-2014.xls");
//			 wb.write(fileOut);
//			 fileOut.close();
//		}
//		
//	}
	
	
	public String getPath() {
	    String path = String.format("%s/%s", System.getProperty("user.dir"), this.getClass().getPackage().getName().replace(".", "/"));
	    return path.split(this.getClass().getPackage().getName())[0]+"/ArticleResources/";
	}
	
	public static void main(String[] args) throws IOException{
		tickerRegressionOverviewGenerator trog = new tickerRegressionOverviewGenerator();

//		DATE TEST
//		HSSFSheet testSheet = trog.getTickerExcelSheet("FUNCOM", "xls"); 
//		System.out.println("SHEET DATE ROW:  " + testSheet.getRow(1).getCell(0).toString().replace(".", "").substring(0, 8));
//		System.out.println(trog.stringToDateTime(testSheet.getRow(1).getCell(0).toString().replace(".", "").substring(0, 8)));
		trog.getEuroNokExchangeRateForDate();
		
		DateTime startDate = new DateTime(2011,1,1,0,0);
		DateTime endDate = new DateTime(2012,1,1,0,0);
		
		
		
		ArrayList<tickerRegressionDate> list = trog.initiateTickerRegressionDateObjects("FUNCOM", startDate, endDate);
		trog.generateExcelSheet(list);
//		System.out.println("LIST SIZE: " +  list.size());
//		for(int i=0; i<list.size(); i++){
//			System.out.println(list.get(i).toString());
//		}
	}
	
	

}
