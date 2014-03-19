package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

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
	
	
	public static void main(String[] args) throws IOException{
		ExcelStockParser esp = new ExcelStockParser();
		InputStream myxls = new FileInputStream(esp.getPath()+"/StockExcelSheets/data.xls");
		HSSFWorkbook wb = new HSSFWorkbook(myxls);
		
		HSSFSheet sheet = wb.getSheetAt(0);   //first sheet
		HSSFRow sheetRow = sheet.getRow(0);        //third row
		HSSFCell sheetCell = sheetRow.getCell(3);       //fourth cell
		
		HashMap<String, String> stockDate = new HashMap<String, String>();
		
		for(int i=2; i<sheet.getLastRowNum()+1; i++){
			stockDate.put(sheet.getRow(i).getCell(0).toString(), sheet.getRow(i).getCell(1).toString());
	
		}
		System.out.println(stockDate.toString());
		
		
		

	}
	
	

}
