package paperIII;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.joda.time.DateTime;

import utils.ExcelStockParser;

public class tickerRegressionOverviewGenerator {
	
	
	public ArrayList<tickerRegressionDate> tickerRegressionDates;
	
	public tickerRegressionOverviewGenerator(){
		
		
	}
	
	//DATESTRING TO DATETIME
	public DateTime stringToDateTime(String date){
		
		String year = date.substring(0,4);
		String month = date.substring(4,6);
		String day = date.substring(6,8);
		
		System.out.println(year + " " + month + " " + day);
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
		System.out.println(marketValueExcelSheet.getLastRowNum());
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
			System.out.println(oseaxExcelSheet.getRow(i).getCell(0).toString());
			
			Double dateDouble = Double.parseDouble(oseaxExcelSheet.getRow(i).getCell(0).toString());
			String doubleString = String.format(".%f", dateDouble);
			//System.out.println("DOUBLE STRING: " + doubleString);
			
			
			DateTime oseaxDate = this.stringToDateTime(doubleString.replace(".", "").substring(0, 8));
			

			double close = Double.parseDouble(oseaxExcelSheet.getRow(i).getCell(1).toString());
			double ir = Double.parseDouble(oseaxExcelSheet.getRow(i).getCell(2).toString());
			double srltd = Double.parseDouble(oseaxExcelSheet.getRow(i).getCell(3).toString());
			double pcisltd = Double.parseDouble(oseaxExcelSheet.getRow(i).getCell(4).toString());
			int r = Integer.parseInt(oseaxExcelSheet.getRow(i).getCell(5).toString());
			int b = Integer.parseInt(oseaxExcelSheet.getRow(i).getCell(6).toString());
			
			boolean bull = false;
			boolean recession = false;
			
			if(r == 1){
				recession = true;
			}
			if(b == 1){
				bull = true;
			}
			
			oseaxValueHolderDateObject ovhdo = new oseaxValueHolderDateObject(close, ir, srltd, pcisltd, recession, bull);
			//System.out.println("DATE: " + inflationDate.toString() + " VALUE: " + inflationValueForDate);
			
			oseaxDateHashmap.put(oseaxDate, ovhdo);
		}
		return oseaxDateHashmap;
	}
	//GET NIBOR FOR DATE
	//GET UNEMPLOYMNET RATE FOR DATE
	//GET EURO/NOK EXCHANGE RATE FOR DATE
	
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
	
	public static void main(String[] args) throws IOException{
		tickerRegressionOverviewGenerator trog = new tickerRegressionOverviewGenerator();

		//DATE TEST
//		HSSFSheet testSheet = trog.getTickerExcelSheet("FUNCOM", "xls"); 
//		System.out.println("SHEET DATE ROW:  " + testSheet.getRow(1).getCell(0).toString().replace(".", "").substring(0, 8));
//		System.out.println(trog.stringToDateTime(testSheet.getRow(1).getCell(0).toString().replace(".", "").substring(0, 8)));
		trog.getOseaxForDate();
	}
	
	
	
	
	
	
	

}
