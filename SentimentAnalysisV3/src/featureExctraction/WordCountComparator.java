package featureExctraction;

import java.util.Comparator;

public class WordCountComparator implements Comparator<WordCount> {

	@Override
	public int compare(WordCount wc1, WordCount wc2) {
		if(wc1.getCounter() > wc2.getCounter()){
			return -1;
		}
		else if(wc1.getCounter() < wc2.getCounter()){
			return 1;
		}
		else{
			return 0;
		}
	}

}
