package hegnarArticleScraper;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import preProcessing.TextFileHandler;

import com.google.gson.Gson;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

public class NewswebStockReportScraper {
	
	
	public NewswebStockReportScraper(){
		
	}
	
	
	
	
	public Document pageDocument(String url) throws IOException{
		Document newswebPage;
		newswebPage = Jsoup.connect(url).get();
		return newswebPage;
	}
	
	public ArrayList<NewswebStockReportDateValue> getReportsFromPage(Document stockReportDocument){
		ArrayList<NewswebStockReportDateValue> currentPageStockReportList = new ArrayList<>();
		
		Elements newsWebReportTable = stockReportDocument.select(".messageTable tr");
		
		for(int i=1; i<newsWebReportTable.size(); i++){
			int day = Integer.parseInt(newsWebReportTable.get(i).select("td").get(0).text().substring(0,2));
			int month = Integer.parseInt(newsWebReportTable.get(i).select("td").get(0).text().substring(3,5));
			int year = Integer.parseInt(newsWebReportTable.get(i).select("td").get(0).text().substring(6,10));
			
			String message = newsWebReportTable.get(i).select("td").get(4).text();
			String category = newsWebReportTable.get(i).select("td").get(6).text();
			
			System.out.println("DAY: " + day + " MONTH: " + month + " YEAR: " + year);
			System.out.println("MESSAGE: " + newsWebReportTable.get(i).select("td").get(4).text());
			System.out.println("CATEGORY : " + newsWebReportTable.get(i).select("td").get(6).text());
			
			DateTime dateOfStockReport = new DateTime(year, month, day, 0, 0);
			
			NewswebStockReportDateValue newswebStockReportDate = new NewswebStockReportDateValue(dateOfStockReport, message, category);
			currentPageStockReportList.add(newswebStockReportDate);
						
			//System.out.println(newsWebReportTable.get(i).select("td").get(0).text());

		}

		return currentPageStockReportList;
	}

	
	
	public int getNumberOfPages(Document d){
		int numberOfPages = d.select(".navigator a").size();
		return numberOfPages;
	}

	
	public String newsWebStockReportUrlStringBuilder(String issuerId, String fromDate, String toDate, String page){
		String completeString = "";
		String first = "http://www.newsweb.no/newsweb/search.do?headerSearch=&selectedPagenumber="+page+"&searchSubmitType=searchtype&searchtype=full&searchCriteria.issuerId="+issuerId;
		String second = "&searchCriteria.issuerSign=&searchCriteria.instrumentShortName=&searchCriteria.categoryId=0&searchCriteria.fromDate="+fromDate;
		String third = "&searchCriteria.toDate="+toDate+"&_searchCriteria.infoRequiredOnly=&_searchCriteria.oamMandatoryOnly=&_searchCriteria.currentVersionOnly=&_searchCriteria.activeIssuersOnly=&searchCriteria.activeIssuersOnly=true&_searchCriteria.osloMarketOnly=";
		
		completeString = first+second+third;
		
		System.out.println(completeString);
		return completeString;
	}
	
	public ArrayList<String> getCompleteStringList(String issuerId, String fromDate, String toDate) throws IOException{
		ArrayList<String> stringList = new ArrayList<>();
		
		Document currentDoc = this.pageDocument(this.newsWebStockReportUrlStringBuilder(issuerId, fromDate, toDate, "1"));
		int totalPages = this.getNumberOfPages(currentDoc);
		
		for(int i=1; i<=totalPages+1; i++){
			stringList.add(this.newsWebStockReportUrlStringBuilder(issuerId, fromDate, toDate, ""+i));
		}
		
		return stringList;
	}
	
	public ArrayList<NewswebStockReportDateValue> getCompleteObjectArrayList(String issuerId, String fromDate, String toDate) throws IOException{
		
		ArrayList<String> completeUrlStringList = this.getCompleteStringList(issuerId, fromDate, toDate);
		ArrayList<NewswebStockReportDateValue> completeNewswebStockReportDateValueList = new ArrayList<>();
		
		System.out.println(completeUrlStringList.toString());
		
		for(int i=0; i<completeUrlStringList.size(); i++){
			Document currentDoc = this.pageDocument(completeUrlStringList.get(i));
			completeNewswebStockReportDateValueList.addAll(this.getReportsFromPage(currentDoc));
		}
		
		return completeNewswebStockReportDateValueList;
		
	}
	
	//FILE HANDLERS
	public String getPath() {
	    String path = String.format("%s/%s", System.getProperty("user.dir"), this.getClass().getPackage().getName().replace(".", "/"));
	    return path.split(this.getClass().getPackage().getName())[0]+"/ArticleResources/StockReports/";
	}
	public void writeStockReportsToFile(String text, String path, String name) throws IOException{
		Writer out = new BufferedWriter(new OutputStreamWriter(
	    new FileOutputStream(path + "/"+name+".json"), "UTF-8"));
		try {
		    out.write(text);
		} finally {
		    out.close();
		}
	}
	public void writeStockReportFromPeriodToFile(ArrayList<NewswebStockReportDateValue> stockReport, String ticker) throws IOException{
		Gson g = new Gson();
		String name = "STOCK-REPORT-COMBINED-" + ticker;
		this.writeStockReportsToFile(g.toJson(stockReport), this.getPath(), name);	
	}
	
	public static void mergeStockReports() throws IOException{
		String tickers = "FUNCOM,IOX,NAUR,NOR,NSG,RCL,SDRL,STL,TEL,YAR";
		String[] tickersAsArray = tickers.split(",");
		NewswebStockReportScraper nwsrs = new NewswebStockReportScraper();
		
		Gson g = new Gson();
		TextFileHandler tfh = new TextFileHandler();
		
		for(int i=0; i<tickersAsArray.length; i++){
			//System.out.println(tickersAsArray[i]);
			ArrayList<NewswebStockReportDateValue> combined = new ArrayList<>();
			ArrayList<NewswebStockReportDateValue> stockReport1 = g.fromJson(tfh.getStockReport(tickersAsArray[i], "2007-2009"), ArrayList.class);
			ArrayList<NewswebStockReportDateValue> stockReport2 = g.fromJson(tfh.getStockReport(tickersAsArray[i], "2009-2011"), ArrayList.class);
			ArrayList<NewswebStockReportDateValue> stockReport3 = g.fromJson(tfh.getStockReport(tickersAsArray[i], "2011-2013"), ArrayList.class);
			ArrayList<NewswebStockReportDateValue> stockReport4 = g.fromJson(tfh.getStockReport(tickersAsArray[i], "2013-2014"), ArrayList.class);
			
			combined.addAll(stockReport1);
			combined.addAll(stockReport2);
			combined.addAll(stockReport3);
			combined.addAll(stockReport4);
			
			nwsrs.writeStockReportFromPeriodToFile(combined, tickersAsArray[i]);
		}
	}
	
	
	
	
	public static void main(String[] args) throws IOException{
//		String startString = "http://www.newsweb.no/newsweb/search.do?headerSearch=&selectedPagenumber=1&searchSubmitType=searchtype&searchtype=full&searchCriteria.issuerId=1309&searchCriteria.issuerSign=&searchCriteria.instrumentShortName=&searchCriteria.categoryId=0&searchCriteria.fromDate=01.01.2010&searchCriteria.toDate=01.01.2012&_searchCriteria.infoRequiredOnly=&_searchCriteria.oamMandatoryOnly=&_searchCriteria.currentVersionOnly=&_searchCriteria.activeIssuersOnly=&searchCriteria.activeIssuersOnly=true&_searchCriteria.osloMarketOnly=";
		
		NewswebStockReportScraper nwsrs = new NewswebStockReportScraper();
//		Document tempDoc = nwsrs.pageDocument(startString);
//		nwsrs.getReportsFromPage(tempDoc);
//		nwsrs.newsWebStockReportUrlStringBuilder("1309", "01.01.2010", "01.01.2013", "1");
//		ArrayList<NewswebStockReportDateValue> completeList = nwsrs.getCompleteObjectArrayList("7760", "01.01.2013", "19.05.2014");
//		nwsrs.writeStockReportFromPeriodToFile(completeList, "YAR");
		nwsrs.mergeStockReports();
	}
	
	

}
