package hegnarArticleScraper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import newsAPI.NewsArticlesRaw;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.joda.time.DateTime;

import com.google.gson.Gson;

import preProcessing.NewsArticleWithTickers;
import preProcessing.NewsArticlesWithTickers;
import preProcessing.TextFileHandler;
import utils.ExcelStockParser;

public class HegnarArticleOverview {
	
	
	public double getValueFromIndiceOfTicker(String ticker, DateTime dateOfInterest, int typeOfIndiceValue) throws IOException{
		double valueToReturn = 0.0;
		//GET WORKBOOK
		ExcelStockParser esp = new ExcelStockParser();
		String indiceSheet = "/Indices/"+ticker+".xls";
		InputStream myxls = new FileInputStream(esp.getPath()+indiceSheet);
		HSSFWorkbook wb = new HSSFWorkbook(myxls);
		
		HSSFSheet sheet = wb.getSheetAt(0);   //first sheet
		
		for(int i=2; i<sheet.getLastRowNum()+1; i++){
			String day = sheet.getRow(i).getCell(0).toString().split("-")[0];
			String month = sheet.getRow(i).getCell(0).toString().split("-")[1];
			String year = sheet.getRow(i).getCell(0).toString().split("-")[2];
			
			int dayNumber = Integer.parseInt(day);
			int monthNumber = 0;
			int yearNumber = Integer.parseInt(year);
			month = month.toLowerCase();
			switch (month) {
				case "jan":
					monthNumber =1;
					break;
				case "feb":
					monthNumber =2;
					break;
				case "mar":
					monthNumber =3;
					break;
				case "apr":
					monthNumber =4;
					break;
				case "mai":
					monthNumber =5;
					break;
				case "may":
					monthNumber = 5;
					break;
				case "jun":
					monthNumber =6;
					break;
				case "jul":
					monthNumber =7;
					break;
				case "aug":
					monthNumber =8;
					break;
				case "sep":
					monthNumber =9;
					break;
				case "okt":
					monthNumber =10;
					break;
				case "oct":
					monthNumber =10;
					break;
				case "nov":
					monthNumber =11;
					break;
				case "des":
					monthNumber =12;
					break;
				case "dec":
					monthNumber =12;
					break;
				default:
					System.out.println("Kunne ikke finne m�ned:" + month);
					break;
			}
			//System.out.println("DAG: " + dayNumber +" M�NED: " + monthNumber + " �R: " + yearNumber);
		
			//String indiceDateString = sheet.getRow(i).getCell(0).toString();
			DateTime indiceDate = new DateTime(yearNumber, monthNumber, dayNumber, 0, 0);
			if(indiceDate.equals(dateOfInterest)){
//				System.out.println("Samme dag");
				if(sheet.getRow(i).getCell(typeOfIndiceValue).toString().contains("E")){
					valueToReturn = Double.parseDouble(sheet.getRow(i).getCell(typeOfIndiceValue).toString().split("E")[0]);
				}
				else{
					valueToReturn = Double.parseDouble(sheet.getRow(i).getCell(typeOfIndiceValue).toString());
				}
//				System.out.println(valueToReturn);
			}
			
		}
		return valueToReturn;
	}
	
	public NewsArticlesWithTickers getArticlesFromTicker(String ticker) throws IOException{
			String path = this.getPath()+"/HegnarArticlesNew/HegnarArticlesWithTicker/NEW-HEGNAR-ARITCLE-WITH-TICKER-" + ticker+".json";
			String jsonSource = this.getJsonSource(path);
			Gson gson = new Gson();
			NewsArticlesWithTickers articles = gson.fromJson(jsonSource, NewsArticlesWithTickers.class);
			return articles;
	}
	public NewsArticlesWithTickers getArticlesFromTickerAnnotated(String ticker) throws IOException{
		String path = this.getPath()+"/HegnarArticlesNew/ArticlesAnnotated/NEW-ARTICLE-ANNOTATED-" + ticker+".json";
		String jsonSource = this.getJsonSource(path);
		Gson gson = new Gson();
		NewsArticlesWithTickers articles = gson.fromJson(jsonSource, NewsArticlesWithTickers.class);
		return articles;
}
	
	// RETURNS LIST WITH ALL DATES BETWEEN 1/1/2007 and 14/2/2014 
	public static ArrayList<DateTime> getAllDatesBetweenJanFirst2007AndFebFourteen2014() {
		ArrayList<DateTime> dateList = new ArrayList<DateTime>();
		DateTime yesterday = new DateTime(2007,1,1,0,0);
		dateList.add(yesterday);
		DateTime today = yesterday.plusDays(1);
		DateTime endDate = new DateTime(2014,2,15,0,0);
		while (today.isBefore(endDate)) {
			dateList.add(today);
			yesterday = today;
			today = yesterday.plusDays(1);
		}
		return dateList;
	}
	
	// RETURNS ARTICLE PUBLICATION COUNT IN ARTICLE LIST FOR GIVEN DATE
	public int getArticleCountForDate(ArrayList<NewsArticleWithTickers> articleList, DateTime date) {
		int count = 0;
		// LOOP THORUGH ARTICLES
		for(int i=0; i<articleList.size(); i++){
			
			// CHECK FOR GET PUBLISHED FIELD TO AVOID NULL POINTER
			String articleDateString = articleList.get(i).getpublished();
			if(articleDateString  != null){
				try {
					String articleYear = articleDateString.split("-")[0];
					String articleMonth = articleDateString.split("-")[1];
					String articleDay = articleDateString.split("-")[2].split("T")[0];
					int articleYearNumber = 2000 + Integer.parseInt(articleYear);
					int articleMonthNumber = Integer.parseInt(articleMonth);
					int articleDayNumber = Integer.parseInt(articleDay);
					DateTime articleDate = new DateTime(articleYearNumber, articleMonthNumber, articleDayNumber, 0, 0);
					if (articleDate.equals(date)) {
						count++;
					}
				} catch (Exception e) {
					System.out.println("Execption: " + e);
				}
			}
		}
		return count;
	}
	
	// 
	public static double getValueFromIndiceOfTickerStatic(String ticker, DateTime dateOfInterest, int typeOfIndiceValue) throws IOException{
		double valueToReturn = 0.0;
		//GET WORKBOOK
		ExcelStockParser esp = new ExcelStockParser();
		String indiceSheet = "/Indices/"+ticker+".xls";
		InputStream myxls = new FileInputStream(esp.getPath()+indiceSheet);
		HSSFWorkbook wb = new HSSFWorkbook(myxls);
		
		HSSFSheet sheet = wb.getSheetAt(0);   //first sheet
		
		for(int i=2; i<sheet.getLastRowNum()+1; i++){
			String day = sheet.getRow(i).getCell(0).toString().split("-")[0];
			String month = sheet.getRow(i).getCell(0).toString().split("-")[1];
			String year = sheet.getRow(i).getCell(0).toString().split("-")[2];
			int dayNumber = Integer.parseInt(day);
			int monthNumber = 0;
			int yearNumber = Integer.parseInt(year);
			month = month.toLowerCase();
			switch (month) {
			case "jan":
				monthNumber =1;
				break;
			case "feb":
				monthNumber =2;
				break;
			case "mar":
				monthNumber =3;
				break;
			case "apr":
				monthNumber =4;
				break;
			case "mai":
				monthNumber =5;
				break;
			case "may":
				monthNumber = 5;
				break;
			case "jun":
				monthNumber =6;
				break;
			case "jul":
				monthNumber =7;
				break;
			case "aug":
				monthNumber =8;
				break;
			case "sep":
				monthNumber =9;
				break;
			case "okt":
				monthNumber =10;
				break;
			case "oct":
				monthNumber =10;
				break;
			case "nov":
				monthNumber =11;
				break;
			case "des":
				monthNumber =12;
				break;
			case "dec":
				monthNumber =12;
				break;
				default:
					System.out.println("Kunne ikke finne m�ned:" + month);
					break;
			}
			//System.out.println("DAG: " + dayNumber +" M�NED: " + monthNumber + " �R: " + yearNumber);
		
			//String indiceDateString = sheet.getRow(i).getCell(0).toString();
			DateTime indiceDate = new DateTime(yearNumber, monthNumber, dayNumber, 0, 0);
			if (indiceDate.equals(dateOfInterest) || indiceDate.isAfter(dateOfInterest)) {
				if(sheet.getRow(i).getCell(typeOfIndiceValue).toString().contains("E")){
					valueToReturn = Double.parseDouble(sheet.getRow(i).getCell(typeOfIndiceValue).toString().split("E")[0]);
				}
				else{
					valueToReturn = Double.parseDouble(sheet.getRow(i).getCell(typeOfIndiceValue).toString());
				}
				return valueToReturn;
			} else {
				// Do nothing
			}
		}
		return -1.0;
	}
	
	public TickerResourceInfo getArticlesTickerDate(String ticker, int typeOfIndiceValue) throws IOException{
		
		NewsArticlesWithTickers nawt = this.getArticlesFromTicker(ticker);
		ArrayList<NewsArticleWithTickers> articleList = nawt.getNewsArticlesWithTickers();
		
		DateTime lastArticleDate = new DateTime();

		ArrayList<Double> articleIndiceValues = new ArrayList<Double>();
		ArrayList<String> articleDateValues = new ArrayList<String>();
		ArrayList<Integer> articleCountValues = new ArrayList<Integer>();
		
		int articleCount = 1;
		
		for(int i=0; i<articleList.size(); i++){
	
			
//			System.out.println(articleList.get(i).getpublished());
			if(articleList.get(i).getpublished() != null){
				
			
				try {
					String articleDateString = articleList.get(i).getpublished();
					
					//FIGURE OUT ARTICLE YEAR
					String articleYear = articleDateString.split("-")[0];

					String articleMonth = articleDateString.split("-")[1];
					String articleDay = articleDateString.split("-")[2].split("T")[0];
					
					int articleYearNumber = 2000 + Integer.parseInt(articleYear);
					int articleMonthNumber = Integer.parseInt(articleMonth);
					int articleDayNumber = Integer.parseInt(articleDay);
					
					DateTime articleDate = new DateTime(articleYearNumber, articleMonthNumber, articleDayNumber, 0, 0);
					
//					System.out.println("ARTICLE DATE: " + articleDateString);
//					System.out.println("DATETIME: " + articleDate.toString());
					
					try {
						if(lastArticleDate.equals(articleDate)){
							articleCount++;
//							System.out.println("Samme dato som forrige dag");
							continue;
						}
						else{
							articleCountValues.add(articleCount);
							articleDateValues.add(articleDateString);
							articleIndiceValues.add(getValueFromIndiceOfTicker(ticker, articleDate, typeOfIndiceValue));
							articleCount = 1;
						}
						
						lastArticleDate = articleDate;

					} catch (Exception e) {
						//CATCHE MED � HENTE NESTE VERDI
						boolean foundStockValue = false;
						DateTime nextActiveStockDate = new DateTime();

						while(!foundStockValue){
							try {
								articleIndiceValues.add(getValueFromIndiceOfTicker(ticker, nextActiveStockDate, typeOfIndiceValue));
								foundStockValue = true;
							} catch (Exception e2) {
								nextActiveStockDate = articleDate.plusDays(1);
								// TODO: handle exception
							}

						}
//						System.out.println("EXCEPTION: " + e);
					}
					
				} catch (Exception e) {
					continue;
				}
				
			
			}
			
		}
//		System.out.println("ArticleIndiceValuesSize: " + articleIndiceValues.size() +" ArticleDateValues: " + articleDateValues.size() + " ArticleCountValues: " + articleCountValues.size());
//		for(int i=0; i<articleIndiceValues.size(); i++){
//			System.out.print(articleIndiceValues.get(i)+" ");
//		}
//		System.out.println("\n----");
//		for(int i=0; i<articleDateValues.size(); i++){
//			System.out.print(articleDateValues.get(i)+" ");
//		}
//		System.out.println("\n----");
//		for(int i=0; i<articleCountValues.size(); i++){
//			System.out.print(articleCountValues.get(i)+" ");
//		}
		
		TickerResourceInfo tri = new TickerResourceInfo(articleIndiceValues, articleDateValues, articleCountValues);
		tri.ticker = ticker;
		tri.setTotalNumberOfArticles(articleList.size());
		
		
		//CALCULATE TOTAL TRADES AND VOLUME
		ExcelStockParser esp = new ExcelStockParser();
		String indiceSheet = "/Indices/"+ticker+".xls";
		InputStream myxls = new FileInputStream(esp.getPath()+indiceSheet);
		HSSFWorkbook wb = new HSSFWorkbook(myxls);
		
		HSSFSheet sheet = wb.getSheetAt(0);   //first sheet
		
		double totalVolume = 0;
		double totalTrades = 0;
		
		for(int i=2; i<sheet.getLastRowNum()+1; i++){
			try {
				double dayVolume = Double.parseDouble(sheet.getRow(i).getCell(6).toString());
				totalVolume+=dayVolume;
			} catch (Exception e) {
				double dayVolume = 0.0;
				totalVolume+=dayVolume;
			}
			
			try {
				double dayTrades = Double.parseDouble(sheet.getRow(i).getCell(9).toString());
				totalTrades+= dayTrades;
			} catch (Exception e) {
		
				double dayTrades = 0;
				totalTrades+= dayTrades;
			}
		}
		System.out.println("-------");
		System.out.println("TOTAL VOLUME FOR INDEX: " + totalVolume);
		System.out.println("TOTAL TRADES FOR INDEX: " + totalTrades);
		System.out.println("VOLUME/TRADES:  " + (totalVolume/totalTrades));
		double volumeTradesVariable = (totalVolume/totalTrades);
		double averageTrades = (totalTrades/sheet.getLastRowNum()+1);
		
		tri.setVolumeTradeVariable(volumeTradesVariable);
		tri.setAverageTotalTrade(averageTrades);

		//FIND COMPANY MARKET VALUE
		ExcelStockParser newEsp = new ExcelStockParser();
		String marketValueSheet = "/WordlistsOfImportance/CompanyMarketValues.xls";
		InputStream myxlsnew = new FileInputStream(newEsp.getPath()+marketValueSheet);
		HSSFWorkbook wbnew = new HSSFWorkbook(myxlsnew);
		
		HSSFSheet sheetnew = wbnew.getSheetAt(0);   //first sheet

		double companyMarketValue = 0;
		
		for(int i=2; i<sheetnew.getLastRowNum()+1; i++){
			if(sheetnew.getRow(i).getCell(0).toString().equals(ticker)){
				companyMarketValue = Double.parseDouble(sheetnew.getRow(i).getCell(1).toString().replace(" ","")); 
				tri.setCompanyMarketValue(companyMarketValue);
			}
	
		}
		
		return tri;

	}
	
	static String readFile(String path, Charset encoding) throws IOException 
	{
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return encoding.decode(ByteBuffer.wrap(encoded)).toString();
	}
	public String getJsonSource(String source) throws IOException{
		String jsonFile = readFile(source, StandardCharsets.UTF_8);
		return jsonFile;
	}
	public String getPath() {
	    String path = String.format("%s/%s", System.getProperty("user.dir"), this.getClass().getPackage().getName().replace(".", "/"));
	    return path.split(this.getClass().getPackage().getName())[0]+"/ArticleResources/";
	}
	
	
	public ArrayList<TickerResourceInfo> getTickersResourceInformation() throws IOException{
		ArrayList<TickerResourceInfo> tri = new ArrayList<TickerResourceInfo>();
		
		TextFileHandler tfh = new TextFileHandler();
		String tickerList = tfh.getTickerList();
		for(int i=0; i<tickerList.split("\\r?\\n").length; i++){
			System.out.println("TICKER " + i);
			tri.add(getArticlesTickerDate(tickerList.split("\\r?\\n")[i],6));
		}
		
		return tri;
	}
	public ArrayList<TickerResourceInfo> getTickersResourceInformationVolumeTradeSorted() throws IOException{
		ArrayList<TickerResourceInfo> tri = new ArrayList<TickerResourceInfo>();
		
		TextFileHandler tfh = new TextFileHandler();
		String tickerList = tfh.getTickerList();
		for(int i=0; i<tickerList.split("\\r?\\n").length; i++){
			System.out.println("TICKER " + i);
			TickerResourceInfo tickerResourceInfo = getArticlesTickerDate(tickerList.split("\\r?\\n")[i],6);
			if(tickerResourceInfo.getAverageArticlesPostedInADay() > 1.1 && tickerResourceInfo.getTotalNumberOfArticles()>200){
				tri.add(tickerResourceInfo);
			}
		}
		
		return tri;
	}
	
	
	public static void printArticleCountAndIndexValuesForMatlab(String ticker, int column) throws IOException {
		DateTime d = new DateTime(2013,10,31,0,0);
		HegnarArticleOverview hao = new HegnarArticleOverview();
//		hao.getValueFromIndiceOfTicker("STL", d, 3);
		NewsArticlesWithTickers nawt = hao.getArticlesFromTicker(ticker);
		ArrayList<NewsArticleWithTickers> articleList = nawt.getNewsArticlesWithTickers();
		ArrayList<DateTime> dateList = getAllDatesBetweenJanFirst2007AndFebFourteen2014();
		ArrayList<Integer> articleCounts = new ArrayList<Integer>();
		ArrayList<Double> indexValues = new ArrayList<Double>();
		for (DateTime date : dateList) {
			articleCounts.add(hao.getArticleCountForDate(articleList, date)); 
			indexValues.add(getValueFromIndiceOfTickerStatic(ticker, date, column));
		}
		System.out.println(ticker + "ArticleCounts = " + articleCounts + ";");
		System.out.println(ticker + "IndexValues = " + indexValues + ";");
	}
	
	public static void main(String args[]) throws IOException{
//		printArticleCountAndIndexValuesForMatlab("NAS", 6);
		
//		DateTime d = new DateTime(2014,2,27,0,0);
		HegnarArticleOverview hao = new HegnarArticleOverview();
//		hao.getValueFromIndiceOfTicker("STL", d, 3);
//		hao.getArticlesTickerDate("NAS", 6);
		ArrayList<TickerResourceInfo> allTickersOverview = hao.getTickersResourceInformationVolumeTradeSorted();
		Collections.sort(allTickersOverview);
		for(int i=0; i<allTickersOverview.size(); i++){
			System.out.println(allTickersOverview.get(i).toString());
		}
	}

}
