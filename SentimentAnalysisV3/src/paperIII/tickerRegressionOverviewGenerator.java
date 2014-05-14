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
	
	public HSSFSheet getTickerExcelSheet(String ticker, String fileEnding) throws IOException{
		ExcelStockParser esp = new ExcelStockParser();
		String indiceSheet = "/ChosenIndices/"+ticker+"."+fileEnding;
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
			
			DateTime tickerDate = this.stringToDateTime(tickerExcelSheet.getRow(i).getCell(0).toString().replace(".", "").substring(0, 8));
			double close = Double.parseDouble(tickerExcelSheet.getRow(i).getCell(1).toString());
			double volume = Double.parseDouble(tickerExcelSheet.getRow(i).getCell(2).toString());
			double value = Double.parseDouble(tickerExcelSheet.getRow(i).getCell(3).toString());
			
			System.out.println(close + volume + value);
	
		}
		
		return indexHashmap;
		
	}
	//GET ARTICLES AND SENTIMENT STATS FOR DATE
	//GET COMPANY MARKET VALUE FOR DATE
	//GET OILPRICE FOR DATE
	//GET INFLATION FOR DATE
	//GET OSEAX FOR DATE
	//GET NIBOR FOR DATE
	//GET UNEMPLOYMNET RATE FOR DATE
	//GET EURO/NOK EXCHANGE RATE FOR DATE
	
	
	
	public static void main(String[] args) throws IOException{
		tickerRegressionOverviewGenerator trog = new tickerRegressionOverviewGenerator();

		//DATE TEST
		HSSFSheet testSheet = trog.getTickerExcelSheet("FUNCOM", "xls"); 
		System.out.println("SHEET DATE ROW:  " + testSheet.getRow(1).getCell(0).toString().replace(".", "").substring(0, 8));
		System.out.println(trog.stringToDateTime(testSheet.getRow(1).getCell(0).toString().replace(".", "").substring(0, 8)));
	}
	
	
	
	
	
	
	

}
