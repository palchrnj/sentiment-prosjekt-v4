package paperIII;

public class DateArticleSentimentCounter {
	public int numberOfPositiveArticles;
	public int numberOfNegativeArticles;
	public int numberOfNeutralArticles;
	public int numberOfArticles;
	public double aggregateNegativeProb;
	public double aggregateNeutralProb;
	public double aggregatePositiveProb;
	
	
	public DateArticleSentimentCounter(){
		this.numberOfArticles=0;
		this.numberOfNegativeArticles=0;
		this.numberOfPositiveArticles=0;
		this.numberOfNeutralArticles=0;
		
	}

	
	
	public double getAggregateNegativeProb() {
		return aggregateNegativeProb;
	}



	public void setAggregateNegativeProb(double aggregateNegativeProb) {
		this.aggregateNegativeProb = aggregateNegativeProb;
	}



	public double getAggregateNeutralProb() {
		return aggregateNeutralProb;
	}



	public void setAggregateNeutralProb(double aggregateNeutralProb) {
		this.aggregateNeutralProb = aggregateNeutralProb;
	}



	public double getAggregatePositiveProb() {
		return aggregatePositiveProb;
	}



	public void setAggregatePositiveProb(double aggregatePositiveProb) {
		this.aggregatePositiveProb = aggregatePositiveProb;
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
