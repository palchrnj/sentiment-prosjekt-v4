package paperIII;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.joda.time.DateTime;

import utils.ExcelStockParser;

public class tickerRegressionOverviewGenerator {
	
	
	public ArrayList<tickerRegressionDate> tickerRegressionDates;
	
	public tickerRegressionOverviewGenerator(){
		
		
	}
	
	
	public DateTime stringToDateTime(String date){
		
		String year = date.substring(0,3);
		String month = date.substring(3,5);
		String day = date.substring(5,7);
		
		System.out.println(year + " " + month + " " + day);
//		int yearAsInteger = Integer.parseInt(year);
//		int monthAsInteger = Integer.parseInt(month);
//		int dayAsInteger = Integer.parseInt(day);
		
//		DateTime newDateTime = new DateTime(yearAsInteger, monthAsInteger, dayAsInteger, 0, 0);

		return new DateTime();
	}
	
	public HSSFSheet getTickerExcelSheet(String ticker) throws IOException{
		ExcelStockParser esp = new ExcelStockParser();
		String indiceSheet = "/ChosenIndices/"+ticker+".xls";
		InputStream myxls = new FileInputStream(esp.getPath()+indiceSheet);
		HSSFWorkbook wb = new HSSFWorkbook(myxls);
		
		HSSFSheet sheet = wb.getSheetAt(0);   //first sheet
		
		return sheet;
	}
	
	
	public static void main(String[] args) throws IOException{
		tickerRegressionOverviewGenerator trog = new tickerRegressionOverviewGenerator();
		
		HSSFSheet testSheet = trog.getTickerExcelSheet("FUNCOM"); 
		System.out.println(testSheet.getRow(1).getCell(0).toString());
		System.out.println(trog.stringToDateTime(testSheet.getRow(1).getCell(0).toString()));
		
		
	}
	
	
	
	
	
	
	

}
