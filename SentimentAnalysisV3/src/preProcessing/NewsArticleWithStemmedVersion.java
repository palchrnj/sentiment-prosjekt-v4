package preProcessing;

public class NewsArticleWithStemmedVersion extends NewsArticleWithPosTaggedWords{
	public String stemmedTitle;
	public String stemmedLeadText;
	public String stemmedText;
	
	public NewsArticleWithStemmedVersion() {

	}

	public String getStemmedTitle() {
		return stemmedTitle;
	}

	public void setStemmedTitle(String stemmedTitle) {
		this.stemmedTitle = stemmedTitle;
	}

	public String getStemmedLeadText() {
		return stemmedLeadText;
	}

	public void setStemmedLeadText(String stemmedLeadText) {
		this.stemmedLeadText = stemmedLeadText;
	}

	public String getStemmedText() {
		return stemmedText;
	}

	public void setStemmedText(String stemmedText) {
		this.stemmedText = stemmedText;
	}
	
	public static void main(String[] args){
		  
	}
	
	
}
