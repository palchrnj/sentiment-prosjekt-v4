package hegnarArticleScraper;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import preProcessing.TextFileHandler;

import com.google.gson.Gson;

public class NetfondsArticleUrlExtractor {
	
	
	
	
	public NetfondsArticleUrlExtractor(){
		
		
	}
	
	
	//Get correct URL for netfonds
	public String getNetfondsUrlTickerQuery(String ticker, int page){
		return "http://norma.netfonds.no/releases.php?layout=horizontal&distributor=Hegnar&days=&year=2014&month=4&day=1&exchange=OSE&paper="+ticker+"&search=&page_size=500&page="+page;	
	}
	public int getTotalNumberOfNetfondsArticlePages(String ticker, int page) throws IOException{
		String url = getNetfondsUrlTickerQuery(ticker, page);
		int counter = 1;
		boolean articlesExist = true;
		
		//Goes through netfonds article overview and determines how many pages of articles there are
		while(articlesExist){
			Document netfondsDoc;
			netfondsDoc = Jsoup.connect(url).get();
			netfondsDoc.outputSettings().charset("utf-8");
			Elements netfondsResults = netfondsDoc.select(".qbox");
			
			if(netfondsResults.html().length()<500){
				articlesExist = false;
			}
			else{
				counter++;
				url=getNetfondsUrlTickerQuery(ticker, counter);
			}
		}
		//System.out.println(counter);
		return counter;
	}
	public ArrayList<String> getHegnarArticleNetfondsLinks(String ticker) throws IOException{
		int totalNumberOfPages = getTotalNumberOfNetfondsArticlePages(ticker, 1);
		ArrayList<String> hegnarArticleNetfondsLinks = new ArrayList<String>();
		
		for(int i=1; i<totalNumberOfPages; i++){
			String url = getNetfondsUrlTickerQuery(ticker, i);
			
			Document netfondsDoc;
			netfondsDoc = Jsoup.connect(url).get();
			netfondsDoc.outputSettings().charset("utf-8");
			Elements netfondsResults = netfondsDoc.select(".qbox tr a[href*=id]");
			//System.out.println("Size of netfondsElements " + netfondsResults.size());
			Iterator netfondsIterator = netfondsResults.iterator();
			
			while(netfondsIterator.hasNext()){
				hegnarArticleNetfondsLinks.add("http://norma.netfonds.no/" + netfondsIterator.next().toString().split("\"")[1].split("\"")[0]);
			}

		}
		System.out.println("Hegnar articles current ticker size: " + hegnarArticleNetfondsLinks.size());
//		for(int j=0; j<hegnarArticleLinks.size(); j++){
//			System.out.println(hegnarArticleLinks.get(j));
//		}

		return hegnarArticleNetfondsLinks;
	}
	public ArrayList<String> getHegnarArticleLinks(String ticker) throws IOException{
		ArrayList<String> hegnarArticleLinks = new ArrayList<String>();
		ArrayList<String> hegnarArticleNetfondsLinks = getHegnarArticleNetfondsLinks(ticker);
	
		for(int i=0; i<hegnarArticleNetfondsLinks.size(); i++){
			System.out.println(i);
			Document netfondsDoc;
			netfondsDoc = Jsoup.connect(hegnarArticleNetfondsLinks.get(i)).get();
			netfondsDoc.outputSettings().charset("utf-8");
			Element netfondsResult = netfondsDoc.select("p a[href*=www.hegnar.no]").first();
			if(!netfondsResult.toString().split("\"")[3].contains("newsdet")){
				hegnarArticleLinks.add(netfondsResult.toString().split("\"")[3]);
			}
			
			//System.out.println(netfondsResult.toString().split("\"")[3]);
		}
		return hegnarArticleLinks;
	}
	
	//FILE HANDLERS
	public String getPath() {
	    String path = String.format("%s/%s", System.getProperty("user.dir"), this.getClass().getPackage().getName().replace(".", "/"));
	    return path.split(this.getClass().getPackage().getName())[0]+"/ArticleResources/HegnarArticlesNew/ArticleTickerLinks/";
	}
	public void writeLinksToFile(String text, String path, String name) throws IOException{
		Writer out = new BufferedWriter(new OutputStreamWriter(
	    new FileOutputStream(path + "/"+name+".json"), "UTF-8"));
		try {
		    out.write(text);
		} finally {
		    out.close();
		}
	}
	
	public void writeGivenTickerLinksToFile(String ticker) throws IOException{
		NetfondsArticleUrlExtractor nae = new NetfondsArticleUrlExtractor();
		Gson g = new Gson();
		String name = "ARTICLE-LINKS-" + ticker;
		nae.writeLinksToFile(g.toJson(nae.getHegnarArticleLinks(ticker)), nae.getPath(), name);	
	}
	
	public void createAllTickerLinkLists() throws IOException{
		TextFileHandler tfh = new TextFileHandler();
		String tickerList = tfh.getTickerList();
		for(int i=0; i<tickerList.split("\\r?\\n").length; i++){
			System.out.println(tickerList.split("\\r?\\n")[i]);
			this.writeGivenTickerLinksToFile(tickerList.split("\\r?\\n")[i]);
		}
		
	}
	
	
	
	
	public static void main(String[] args) throws IOException{
		NetfondsArticleUrlExtractor nae = new NetfondsArticleUrlExtractor();
		nae.createAllTickerLinkLists();
		
		
	}
	

}
