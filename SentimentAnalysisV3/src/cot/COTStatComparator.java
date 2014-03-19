package cot;

import java.util.Comparator;

public class COTStatComparator implements Comparator<COTStat>{
	
	String statisticOfComparison;
	
	public COTStatComparator(String statisticOfComparison) {
		this.statisticOfComparison = statisticOfComparison;
	}
	
	public int compare(COTStat o1, COTStat o2) {
		// TF
		if (statisticOfComparison.equals("tf")) {
			if (o1.tf > o2.tf) {
				return -1;
			} else if (o1.tf < o2.tf) {
				return 1;
			} else {
				return 0;
			}
		// IDF
		} else if (statisticOfComparison.equals("idf")) {
			if (o1.idf > o2.idf) {
				return -1;
			} else if (o1.idf < o2.idf) {
				return 1;
			} else {
				return 0;
			}
		// TFIDF
		} else if (statisticOfComparison.equals("tfidf")) {
			if (o1.tfidf > o2.tfidf) {
				return -1;
			} else if (o1.tfidf < o2.tfidf) {
				return 1;
			} else {
				return 0;
			}
		// MI
		} else if (statisticOfComparison.equals("mi")) {
			if (o1.mi > o2.mi) {
				return -1;
			} else if (o1.mi < o2.mi) {
				return 1;
			} else {
				return 0;
			}
		// CHI
		} else if (statisticOfComparison.equals("chi")) {
			if (o1.chi > o2.chi) {
				return -1;
			} else if (o1.chi < o2.chi) {
				return 1;
			} else {
				return 0;
			}
		} else {
			throw new IllegalArgumentException("Unsupported comparison statistic" + statisticOfComparison);
		}
	}
}
