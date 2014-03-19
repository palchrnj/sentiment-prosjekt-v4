package preProcessing;

public class ArticleCategory {
	
	
	String category;
	int count;
	int negative;
	int positive;
	int neutral;
	
	
	public ArticleCategory(String category){
		this.negative = 0;
		this.positive = 0;
		this.neutral = 0;
		this.count = 0;
		this.category = category; 
	}

	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getNegative() {
		return negative;
	}

	public void setNegative(int negative) {
		this.negative = negative;
	}

	public int getPositive() {
		return positive;
	}

	public void setPositive(int positive) {
		this.positive = positive;
	}

	public int getNeutral() {
		return neutral;
	}

	public void setNeutral(int neutral) {
		this.neutral = neutral;
	}

	
}
