package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import preProcessing.NewsArticleWithCots;
import preProcessing.NewsArticlesWithCots;

public class ExcelStockParser {
	public ExcelStockParser(){
		
	}
	
	public String getPath() {
	    String path = String.format("%s/%s", System.getProperty("user.dir"), this.getClass().getPackage().getName().replace(".", "/"));
	    return path.split(this.getClass().getPackage().getName())[0]+"/ArticleResources/";
	}
	
	public HashMap<String, String> getGraph() throws IOException{
		ExcelStockParser esp = new ExcelStockParser();
		InputStream myxls = new FileInputStream(esp.getPath()+"/StockExcelSheets/data.xls");
		HSSFWorkbook wb = new HSSFWorkbook(myxls);
		
		HSSFSheet sheet = wb.getSheetAt(0);   //first sheet

		HashMap<String, String> stockDate = new HashMap<String, String>();
		
		for(int i=2; i<sheet.getLastRowNum()+1; i++){
			stockDate.put(sheet.getRow(i).getCell(0).toString(), sheet.getRow(i).getCell(1).toString());
		}
		
		return stockDate;
	}
	
	public double getPercentileChangeInStock(String ticker, Calendar c, int days) throws IOException{
		
		//Variables
		double percentileChange = 0;
		String pathToIndex = "/Indices/"+ticker+".xls";
		
		//Setup .xls parser
		ExcelStockParser esp = new ExcelStockParser();
		InputStream myxls = new FileInputStream(esp.getPath()+pathToIndex);
		HSSFWorkbook wb = new HSSFWorkbook(myxls);
		
		HSSFSheet sheet = wb.getSheetAt(0);

		HashMap<String, String> stockOverview = new HashMap<String, String>();
		
		for(int i=2; i<sheet.getLastRowNum()+1; i++){
			stockOverview.put(sheet.getRow(i).getCell(0).toString(), sheet.getRow(i).getCell(1).toString());
		}
		
		Date date = c.getTime();
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(c.getTime());
		cal.add(Calendar.DATE, -days);
		Date fromDate = cal.getTime();
		
		
		System.out.println("TODAYS DATE " + date);
		System.out.println("PREVIOUS DATE " + fromDate);

		System.out.println(stockOverview.toString());
		double todaysValue = 0;
		double previousValue = 0;
		
		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MMM-yyyy");
	    String todayFormated = DATE_FORMAT.format(date);
	    String fromDayFormated = DATE_FORMAT.format(fromDate);
	    
		System.out.println("TODAYS DATE FORMATED " + todayFormated);
		System.out.println("PREVIOUS DATE FORMATED " + fromDayFormated); 
	    
		
		//INCREASE OR DECREASE DATE TO FIND A VALUE
		while(todaysValue == 0){
			try {
				todaysValue = Double.parseDouble(stockOverview.get(todayFormated));
			} catch (Exception e) {
				c.add(Calendar.DATE, 1);
				date = c.getTime();
			    todayFormated = DATE_FORMAT.format(date);
	
			}
		}
		while(previousValue == 0){
			try {
				previousValue = Double.parseDouble(stockOverview.get(fromDayFormated));
			} catch (Exception e) {
				cal.add(Calendar.DATE, -1);
				fromDate = cal.getTime();
			    fromDayFormated = DATE_FORMAT.format(fromDate);
			}
		}
		
		
		System.out.println("TODAYS VALUE " + todaysValue);
		System.out.println("PREVIOUS VALUE " + previousValue);
		return percentileChange;
		
	}
	
	
	
	
	public static void main(String[] args) throws IOException{
		ExcelStockParser esp = new ExcelStockParser();
//		InputStream myxls = new FileInputStream(esp.getPath()+"/StockExcelSheets/data.xls");
//		HSSFWorkbook wb = new HSSFWorkbook(myxls);
//		
//		HSSFSheet sheet = wb.getSheetAt(0);   //first sheet
//		HSSFRow sheetRow = sheet.getRow(0);        //third row
//		HSSFCell sheetCell = sheetRow.getCell(3);       //fourth cell
//		
//		HashMap<String, String> stockDate = new HashMap<String, String>();
//		
//		for(int i=2; i<sheet.getLastRowNum()+1; i++){
//			stockDate.put(sheet.getRow(i).getCell(0).toString(), sheet.getRow(i).getCell(1).toString());
//	
//		}
//		System.out.println(stockDate.toString());
		Calendar c = Calendar.getInstance();
		c.set(2012, 10, 1);
		esp.getPercentileChangeInStock("ROM", c, 1);

	}
	
	

}
