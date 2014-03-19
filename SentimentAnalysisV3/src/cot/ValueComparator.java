package cot;

import java.util.Comparator;
import java.util.Map;

public class ValueComparator implements Comparator<String> {

	Map<String, Integer> map;
	
	public ValueComparator(Map<String, Integer> map) {
		this.map = map;
	}
	
	public int compare(String o1, String o2) {
		int diff = map.get(o1) - map.get(o2);
		if (diff > 0) {
			return 1;
		} else if (diff < 0) {
			return -1;
		} else {
			return 0;
		}
	}
}