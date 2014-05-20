package paperIII;

public class stockReportCounter {
	public int numberOfStockReport;
	public int numberOfFinancialReports;
	public int numberOfTradeNotificationReports;
	
	
	public stockReportCounter(){
		this.numberOfFinancialReports = 0;
		this.numberOfStockReport = 0;
		this.numberOfTradeNotificationReports = 0;
	}


	public int getNumberOfStockReport() {
		return numberOfStockReport;
	}


	public void setNumberOfStockReport(int numberOfStockReport) {
		this.numberOfStockReport = numberOfStockReport;
	}


	public int getNumberOfFinancialReports() {
		return numberOfFinancialReports;
	}


	public void setNumberOfFinancialReports(int numberOfFinancialReports) {
		this.numberOfFinancialReports = numberOfFinancialReports;
	}


	public int getNumberOfTradeNotificationReports() {
		return numberOfTradeNotificationReports;
	}


	public void setNumberOfTradeNotificationReports(
			int numberOfTradeNotificationReports) {
		this.numberOfTradeNotificationReports = numberOfTradeNotificationReports;
	}
	
	
}
