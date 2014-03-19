package divClasses;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpException;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.hp.hpl.jena.util.FileManager;



public class HtmlScraper {
	
	
	
	
	public HtmlScraper(){
		
		
	}
	
	
	public void readFileAndGetSynonyms() throws IOException{
		FileReader fr = new FileReader("C:/Users/Lars/Documents/GitHub/SentimentProject/SentimentAnalysis/src/xml/NSF-ordlisten-ren.txt");
		//BufferedReader br = new BufferedReader(fr);
		ArrayList<String> testOrd = new ArrayList<>();
		ArrayList<String> synonymResultater = new ArrayList<>();
		
		
		Scanner sc = new Scanner(fr);
		int counter = 0;
		while (sc.hasNext()) {
			testOrd.add(sc.next());
			//System.out.println(sc.next());
			counter++;
			
			if(counter == 100)
				break;
		}
		System.out.println(counter);
		
		
		
		FileWriter wr = new FileWriter("C:/Users/Lars/Documents/GitHub/SentimentProject/SentimentAnalysis/src/xml/ScrapedSynonyms.txt");
		
		
		for(int i = 0; i<testOrd.size(); i++){
			
			
			
			Connection.Response res = Jsoup.connect("http://www.synonymer.no")
				    .data("username", "lazerblade", "password", "8dfcd0")
				    .method(Method.POST)
				    .execute();

			Document doc = res.parse();

			
			Map<String,String> map = new HashMap<String,String>();
			map.put("SynonymerLogin", "r4blpn2gnm2n1fji2dud5mvcg6");
		
			
			String sessionId = res.cookie("SynonymerLogin");
			//System.out.println("Cookie session ID:  " + sessionId);
			
			//SET COOKIES 
			Map<String, String> loginCookies = res.cookies();
			loginCookies.put("SynonymerCookie", "lazerblade");
			//System.out.println("Res cookies: " + res.cookies());	
			
			//GET NEW DOCUMENT
			Document doc2 = Jsoup.connect("http://www.synonymer.no")
				    .cookies(map)
				    .data("synonym", testOrd.get(i))
				    .post();	
			
			String temp = doc2.getElementsByClass("container").text();
			if(temp.contains("Det var dessverre ingen treff på søkeordet:")){
				continue;
			}
			else{
				wr.write(doc2.getElementsByClass("container").text()+"\n");
				//synonymResultater.add(temp);
			}
			
		}
		wr.close();
		System.out.println("DONE");
		for(int i = 0; i<synonymResultater.size(); i++){
			System.out.println(synonymResultater.get(i));
		}
		
		
		
		
		
		
	}
	
	
	
	public Document jConnect() throws IOException{
//			Connection.Response loginForm = Jsoup.connect("http://www.synonymer.no")
//					  .data("username", "lazerblade")
//					  .data("password", "8dfcd0")
//					  .method(Method.POST)
//					  .execute();
//					//.data("username", "lazerblade", "password", "8dfcd0")
//					//.userAgent("Chrome")
//		            //.method(Connection.Method.GET)
//		            //.execute();
//				
//			
//			String token = loginForm.cookie("SynonymerLogin");
//		
//			System.out.println(loginForm.cookies());
//			System.out.println("TOKEN:" + token);
//			
//			Map<String,String> map = new HashMap<String,String>();
//			map.put("SynonymerLogin", "n5ok1k4kthdqbj0icqlvcltd40");
//			
//			
//			Document doc= Jsoup.connect("http://www.synonymer.no")
//			 .data("synonym", "klippe")
//			 .cookies(map)
//			 .timeout(3000)
//			 .post();
//			 return doc;
		
			//GET LOGIN COOKIE
			Connection.Response res = Jsoup.connect("http://www.synonymer.no")
				    .data("username", "lazerblade", "password", "8dfcd0")
				    .method(Method.POST)
				    .execute();

			Document doc = res.parse();

			
			
			String sessionId = res.cookie("SynonymerLogin");
			System.out.println("Cookie session ID:  " + sessionId);
			
			//SET COOKIES 
			Map<String, String> loginCookies = res.cookies();
			loginCookies.put("SynonymerCookie", "lazerblade");
			System.out.println("Res cookies: " + res.cookies());	
			
			//GET NEW DOCUMENT
			Document doc2 = Jsoup.connect("http://www.synonymer.no")
				    .cookies(loginCookies)
				    .data("synonym", "klippe")
				    .post();	
		
			return doc2;
	}
	
	public static void main(String args[]) throws Exception{
	  /* Connection.Response loginForm = Jsoup.connect("http://www.synonymer.no")
	            .method(Connection.Method.GET)
	            .execute();

	    Document doc = Jsoup.connect("http://www.synonymer.no")
	            .data("cookieexists", "false")
	            .data("name", "lazerblade")
	            .data("password", "8dfcd0")
	            .data("subbera", "Login")
	            .cookies(loginForm.cookies())
	            .post();
	    */
		
//
//		HtmlScraper scraper = new HtmlScraper();
//
//		//String title = document.title();
//
//		//scraper.jConnect().getElementsByClass("container");
//		System.out.println(scraper.jConnect().getElementsByClass("container").text());
		HtmlScraper scraper = new HtmlScraper();
		scraper.readFileAndGetSynonyms();
		
	}

}
