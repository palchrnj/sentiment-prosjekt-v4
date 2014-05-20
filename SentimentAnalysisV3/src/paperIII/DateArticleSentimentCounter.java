package paperIII;

public class DateArticleSentimentCounter {
	public int numberOfPositiveArticles;
	public int numberOfNegativeArticles;
	public int numberOfNeutralArticles;
	public int numberOfArticles;
	
	
	public DateArticleSentimentCounter(){
		this.numberOfArticles=0;
		this.numberOfNegativeArticles=0;
		this.numberOfPositiveArticles=0;
		this.numberOfNeutralArticles=0;
	}


	public int getNumberOfPositiveArticles() {
		return numberOfPositiveArticles;
	}


	public void setNumberOfPositiveArticles(int numberOfPositiveArticles) {
		this.numberOfPositiveArticles = numberOfPositiveArticles;
	}


	public int getNumberOfNegativeArticles() {
		return numberOfNegativeArticles;
	}


	public void setNumberOfNegativeArticles(int numberOfNegativeArticles) {
		this.numberOfNegativeArticles = numberOfNegativeArticles;
	}


	public int getNumberOfNeutralArticles() {
		return numberOfNeutralArticles;
	}


	public void setNumberOfNeutralArticles(int numberOfNeutralArticles) {
		this.numberOfNeutralArticles = numberOfNeutralArticles;
	}


	public int getNumberOfArticles() {
		return numberOfArticles;
	}


	public void setNumberOfArticles(int numberOfArticles) {
		this.numberOfArticles = numberOfArticles;
	}
	
	
	

}
