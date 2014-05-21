package preProcessing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;



public class TextFileHandler {
	
	
	//HANDLES ALL RESOURCES THAT ARE SAVED AS TEXT FILES e.g WORDLISTS
	
	public TextFileHandler(){
		
	}
	
	public String getTextFileAsString(String path, Charset encoding) throws IOException 
	{
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return encoding.decode(ByteBuffer.wrap(encoded)).toString();
	}
	
	public String getPath() {
	    String path = String.format("%s/%s", System.getProperty("user.dir"), this.getClass().getPackage().getName().replace(".", "/"));
	    return path.split(this.getClass().getPackage().getName())[0]+"/ArticleResources/";
	}
	
	//GET TICKER LIST
	public String getTickerList() throws IOException{
		String tickerList = this.getTextFileAsString(this.getPath()+"/WordListsOfImportance/TickerOverview.txt", StandardCharsets.UTF_8);
		return tickerList;
	}
	//GET TICKER LIST
	public String getTickerListMissingTicker() throws IOException{
		String tickerList = this.getTextFileAsString(this.getPath()+"/WordListsOfImportance/TickerOverviewOnlyMissingTickers.txt", StandardCharsets.UTF_8);
		return tickerList;
	}
	
	
	
	//GET STOCK REPORT 

	public String getStockReport(String ticker, String yearInterval) throws IOException{
		String stockReportList = this.getTextFileAsString(this.getPath()+"/StockReports/"+ticker+"/STOCK-REPORT-"+yearInterval+"-"+ticker+".json", StandardCharsets.UTF_8);
		return stockReportList;
	}
	
	public String getCombinedStockReport(String ticker) throws IOException{
		String stockReportList = this.getTextFileAsString(this.getPath()+"/StockReports/STOCK-REPORT-COMBINED-"+ticker+".json", StandardCharsets.UTF_8);
		System.out.println(stockReportList);
		return stockReportList;
	}
	

	
	//GET ANALYTIC CLUES FROM FILE AS STRINGLIST
	public String[] getAnalyticalClues() throws IOException{
			String[] analyticArticleCluesList = null;
			String analyticArticleClues = this.getTextFileAsString(this.getPath()+"/WordListsOfImportance/AnalyticalArticleClues.txt", StandardCharsets.UTF_8);
			analyticArticleCluesList = analyticArticleClues.split(",");
			return analyticArticleCluesList;
	}
	
	//GET RECCOMENDATION CLUES FROM FILE AS STRINGLIST
	public String[] getReccomendationClues() throws IOException{
			String[] reccomenderClueList = null;
			String reccomenderClues = this.getTextFileAsString(this.getPath()+"/WordListsOfImportance/ReccomendationArticleClues.txt", StandardCharsets.UTF_8);
			reccomenderClueList = reccomenderClues.split(",");
			return reccomenderClueList;
	}
	
	//GET LIST OF VALENCE SHIFTERS FROM FILE AS STRINGLIST
	public String[] getValenceShifter() throws IOException{
		String[] valenceShifterList = null;
		String valenceShifters = this.getTextFileAsString(this.getPath()+"/WordListsOfImportance/ValenceShifters.txt", StandardCharsets.UTF_8);
		valenceShifterList = valenceShifters.split(",");
		return valenceShifterList;
	}
	
	//GET POSITIVE TITLE CLUES FROM FILE AS STRINGLIST
	public String[] getPositiveTitleClues() throws IOException{
		String[] positiveTitleCluesList = null;
		String positiveTitleClues = this.getTextFileAsString(this.getPath()+"/WordListsOfImportance/PositiveTitleClues.txt", StandardCharsets.UTF_8);
		positiveTitleCluesList = positiveTitleClues.split(",");
		return positiveTitleCluesList;
	}
	
	//GET NEGATIVE TITLE CLUES FROM FILE AS STRINGLIST
	public String[] getNegativeTitleClues() throws IOException{
		String[] negativeTitleCluesList = null;
		String positiveTitleClues = this.getTextFileAsString(this.getPath()+"/WordListsOfImportance/ReccomendationArticleClues.txt", StandardCharsets.UTF_8);
		negativeTitleCluesList = positiveTitleClues.split(",");
		return negativeTitleCluesList;
	}

	public static String getCotsAnnotatedString() throws IOException {
		TextFileHandler tfh = new TextFileHandler();
		return tfh.getTextFileAsString(tfh.getPath()+"/WordListsOfImportance/cotsannotated.json", StandardCharsets.UTF_8);
	}
	public static String getWclList() throws IOException {
		TextFileHandler tfh = new TextFileHandler();
		return tfh.getTextFileAsString(tfh.getPath()+"/WordListsOfImportance/WCLCOTS.json", StandardCharsets.UTF_8);
	}
	public static String getCots() throws IOException {
		TextFileHandler tfh = new TextFileHandler();
		return tfh.getTextFileAsString(tfh.getPath()+"/CoTs/CosRadius10ForTFDF.json", StandardCharsets.UTF_8);
	}
	
	public static String getCotsTFDF() throws IOException {
		TextFileHandler tfh = new TextFileHandler();
		return tfh.getTextFileAsString(tfh.getPath()+"/CoTs/cotsTFDF.json", StandardCharsets.UTF_8);
	}
	
	
	public static String getIdDateClassificationDistribution() throws IOException {
		TextFileHandler tfh = new TextFileHandler();
		return tfh.getTextFileAsString(tfh.getPath()+"/MLDataSetPaperIII/id_date_classification_distribution.json", StandardCharsets.UTF_8);
	}
	
	
	public static void main(String args[]) throws IOException {
		TextFileHandler tfh = new TextFileHandler();
		//PRINT ANALYTICAL CLUES
		String[] ac = tfh.getAnalyticalClues();
		System.out.println("Analytical Clues: ");
		for (int i=0; i<ac.length; i++){
			System.out.println(ac[i]);
		}
		
		//PRINT RECCOMENDER CLUES
		String[] rc = tfh.getReccomendationClues();
		System.out.println("Reccomender Clues: ");
		for (int i=0; i<rc.length; i++){
			System.out.println(rc[i]);
		}
		
		//PRINT POSITIVE TITLE CLUES
		String[] ptc = tfh.getPositiveTitleClues();
		System.out.println("Positive Title Clues: ");
		for (int i=0; i<ptc.length; i++){
			System.out.println(ptc[i]);
		}
		
		//PRINT NEGATIVE TITLE CLUES
		String[] ntc = tfh.getNegativeTitleClues();
		System.out.println("Negative Title Clues: ");
		for (int i=0; i<ntc.length; i++){
			System.out.println(ntc[i]);
		}
		
		//PRINT VALENCE SHIFTER
		String[] vs = tfh.getValenceShifter();
		System.out.println("Valence Shifters: ");
		for (int i=0; i<vs.length; i++){
			System.out.println(vs[i]);
		}
		
	}

}
