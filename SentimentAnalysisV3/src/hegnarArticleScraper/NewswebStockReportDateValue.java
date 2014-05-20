package hegnarArticleScraper;

import org.joda.time.DateTime;

public class NewswebStockReportDateValue {
	
	//DateTime stockReportDate;
	String message;
	String category;
	String formatedDate;
	
	public NewswebStockReportDateValue(DateTime dt, String m, String c){
	//	this.stockReportDate = dt;
		this.message = m;
		this.category = c;
		this.formatedDate = dt.toString("dd.MM.YYYY");
		
	}
//
//	public DateTime getStockReportDate() {
//		return stockReportDate;
//	}
//
//	public void setStockReportDate(DateTime stockReportDate) {
//		this.stockReportDate = stockReportDate;
//	}

	public String getMessage() {
		return message;
	}

	public String getFormatedDate() {
		return formatedDate;
	}

	public void setFormatedDate(String formatedDate) {
		this.formatedDate = formatedDate;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	
	
	

}
