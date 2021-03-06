package hegnarArticleScraper;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.google.gson.Gson;

import preProcessing.NewsArticleWithTickers;
import preProcessing.NewsArticlesWithTickers;
import preProcessing.TextFileHandler;
import newsAPI.NewsArticleRaw;

public class HegnarArticleExtractor {

	public HegnarArticleExtractor() {

	}

	public NewsArticleWithTickers generateNewsArticleRawFromUrl(String url)
			throws IOException {
		NewsArticleWithTickers nawt = new NewsArticleWithTickers();

		String[] urlList = new String[1];
		urlList[0] = url;
		String[] catList = new String[1];
		urlList[0] = "ECONOMY";

		String authorName = "";

		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(1000000);
		String id = "" + randomInt;

		// System.out.println(id);

		Document hegnarDoc;
		hegnarDoc = Jsoup.connect(url).get();
		hegnarDoc.outputSettings().charset("utf-8");
		// GET TITLE FROM HEGNAR
		Elements title = new Elements();
		try {
			title = hegnarDoc.select(".storyContent h2");
		} catch (Exception e) {
			System.out.println("Could not set title");
		}
		// GET INGRESS FROM HEGNAR
		Elements ingress = new Elements();
		try {
			ingress = hegnarDoc.select(".storyContent h5");
		} catch (Exception e) {
			System.out.println("Could not set ingress");
		}
		// GET MAIN TEXT FROM HEGNAR
		Elements mainText = new Elements();
		try {
			mainText = hegnarDoc.select(".wrappingContent");
		} catch (Exception e) {
			System.out.println("Could not set maint text");
		}
		// GET PUBLISH DATE FROM HEGNAR
		String publishedString = "UNDEFINED";
		Elements published = new Elements();
		try {
			published = hegnarDoc.select(".byline");

			// GET PUBLSIHED DATE
			String date = published.text().split("HegnarOnline - ")[1]
					.split("\\.")[0];
			String month = published.text().split("HegnarOnline - ")[1]
					.split("\\.")[1];
			if (month.length() < 2) {
				month = "0" + month;
			}
			String year = published.text().split("HegnarOnline - ")[1]
					.split("\\.")[2].split(" ")[0];
			String hourOfDay = published.text().split("HegnarOnline - ")[1]
					.split(" ")[1].split(":")[0];
			String minute = published.text().split("HegnarOnline - ")[1]
					.split(" ")[1].split(":")[1].substring(0, 2);

			publishedString = year + "-" + month + "-" + date + "T" + hourOfDay
					+ ":" + minute + ":00Z";
		} catch (Exception e) {
			System.out.println("Could not set publish date text");
		}
		// GET FEED IMG URL DATE FROM HEGNAR
		Elements feedImageUrl = new Elements();
		try {
			feedImageUrl = hegnarDoc.select(".activeitem");
		} catch (Exception e) {
			System.out.println("Could not set feed img url date text");
		}

		ArrayList<String> tickerList = new ArrayList<String>();
		ArrayList<String> keywordList = new ArrayList<String>();

		Elements newsHeadlines = new Elements();
		try {
			newsHeadlines = hegnarDoc.select(".ticker");
			if (newsHeadlines.size() > 0) {
				String htmlText = newsHeadlines.get(0).text();
				String[] htmlTickersTextArray = htmlText
						.split("Se aksjeticker:");
				String[] tickers = htmlTickersTextArray[1].split(" ");
				for (int i = 1; i < tickers.length; i++) {
					tickerList.add(tickers[i]);
				}
			}
		} catch (Exception e) {
			System.out.println("COULD NOT FIND TICKERS");
		}

		// Adds author name to object
		Elements articleAuthor = new Elements();
		try {
			articleAuthor = hegnarDoc.select(".ArtikkelForfatter");
			if (articleAuthor.size() > 0) {
				String articleAuthorText = articleAuthor.get(0).text();
				authorName = articleAuthorText;
			}

		} catch (Exception e) {
			System.out.println("COULD NOT FIND AUTHOR");
		}

		Elements keywords = new Elements();
		try {
			keywords = hegnarDoc.select("meta[name=keywords]");
			// Adds Keywords to object
			if (keywords.size() > 0) {
				String keywordsRaw = keywords.get(0).attr("content");
				String[] keywordArray = keywordsRaw.split(",");
				for (int i = 0; i < keywordArray.length; i++) {
					if (keywordArray[i] != " ") {
						keywordList.add(keywordArray[i]);
					}
				}
			}
		} catch (Exception e) {
			System.out.println("COULD NOT FIND KEYWORDS");
		}

		// System.out.println("TITLE - " + title.text());
		// System.out.println("INGRESS - " + ingress.text());
		// System.out.println("MAIN TEXT - " + mainText.text());
		// System.out.println("PUBLISHED - " + publishedString);
		// System.out.println("Feed-Image-Url - " +
		// feedImageUrl.toString().split("\"")[3]);

		nawt.setTitle(title.text());
		nawt.setlead_text(ingress.text());
		nawt.setText(mainText.text());
		nawt.setpublished(publishedString);
		nawt.setPublisher("Hegnar");
		nawt.setImageUrl(" ");
		nawt.setlast_modified(publishedString);
		nawt.setId(id);
		nawt.setSignature(" ");
		nawt.setTickerList(tickerList);
		nawt.setKeywordList(keywordList);
		nawt.setAuthorName(authorName);
		nawt.setLinks(urlList);
		nawt.setcat(catList);
		nawt.setversion(" ");
		nawt.setSentimentValue(" ");

		System.out.println(title.text());

		return nawt;
	}

	public NewsArticlesWithTickers generateNewsArticlesFromHegnar(String ticker)
			throws IOException {
		NewsArticlesWithTickers nawt = new NewsArticlesWithTickers();
		ArrayList<NewsArticleWithTickers> articleList = new ArrayList<NewsArticleWithTickers>();
		ArrayList<String> linkList = new ArrayList<String>();

		Gson g = new Gson();
		String jsonFile = readFile(this.getPath()
				+ "/ArticleTickerLinks/ARTICLE-LINKS-" + ticker + ".json",
				StandardCharsets.UTF_8);

		linkList = g.fromJson(jsonFile, ArrayList.class);

		for (int i = 0; i < linkList.size(); i++) {
			try {
				System.out
						.println("LINKLIST " + i + " URL: " + linkList.get(i));
				articleList.add(this.generateNewsArticleRawFromUrl(linkList
						.get(i)));
			} catch (Exception e) {
				System.out.println("Exception bla: " + e);

			}
		}

		nawt.setNewsArticlesWithTickers(articleList);

		return nawt;
	}

	// FILE HANDLERS
	static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return encoding.decode(ByteBuffer.wrap(encoded)).toString();
	}

	public String getPath() {
		String path = String.format("%s/%s", System.getProperty("user.dir"),
				this.getClass().getPackage().getName().replace(".", "/"));
		return path.split(this.getClass().getPackage().getName())[0]
				+ "/ArticleResources/HegnarArticlesNew/";
	}

	public void writeLinksToFile(String text, String path, String name)
			throws IOException {
		Writer out = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(path + "/" + name + ".json"), "UTF-8"));
		try {
			out.write(text);
		} finally {
			out.close();
		}
	}

	public void writeArticlesToFile(String ticker) throws IOException {
		HegnarArticleExtractor hae = new HegnarArticleExtractor();
		Gson g = new Gson();
		String name = "NEW-HEGNAR-ARITCLE-WITH-TICKER-" + ticker;
		NewsArticlesWithTickers nawt = this
				.generateNewsArticlesFromHegnar(ticker);
		hae.writeLinksToFile(g.toJson(nawt), hae.getPath()
				+ "/HegnarArticlesWithTicker/", name);
	}

	public void createAllhegnarTickerArticles() throws IOException {
		TextFileHandler tfh = new TextFileHandler();
		String tickerList = tfh.getTickerListMissingTicker();

		System.out.println(tickerList.split("\\r?\\n").length);
		for (int i = 0; i < tickerList.split("\\r?\\n").length; i++) {
			System.out.println(tickerList.split("\\r?\\n")[i]);
			this.writeArticlesToFile(tickerList.split("\\r?\\n")[i]);
		}
		// this.writeArticlesToFile("TOM");

	}

	public static void main(String[] args) throws IOException {
		HegnarArticleExtractor hae = new HegnarArticleExtractor();
		// System.out.println(hae.getPath());
		hae.createAllhegnarTickerArticles();
	}

}
