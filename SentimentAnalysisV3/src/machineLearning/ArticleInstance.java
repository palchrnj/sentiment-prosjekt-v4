package machineLearning;

import org.encog.ml.data.MLData;

import preProcessing.NewsArticleWithStemmedVersion;

public class ArticleInstance {
	
	String date;
	MLData data;
	String sentiment;
	
	public ArticleInstance(NewsArticleWithStemmedVersion nawsv) {
		date = nawsv.getpublished();
		data = null;
		
		// Some (6) articles have classification in signature ... 
		if (nawsv.getSentimentValue().length() > 2) {
			sentiment = nawsv.getSentimentValue(); 
		} else if (nawsv.getSignature().length() > 2) {
			sentiment = nawsv.getSignature();
		} else {
			sentiment = null;
		}
	}
}
