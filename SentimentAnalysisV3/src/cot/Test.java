package cot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Test {
	
	public static void main(String[] args) {
		String str1 = "abc";
		String str2 = "abc";
		Set<String> allcotstobeannotated = new HashSet<String>();
		allcotstobeannotated.add(str1);
		System.out.println(allcotstobeannotated.contains(str2));
		ArrayList<Double> list = new ArrayList<Double>();
		list.add(0, 1.0);
		list.add(1, 2.0);
		list.add(2, 3.0);
		list.add(3, 4.0);
		list.add(4, 5.0);
		list.add(5, 6.0);
		System.out.println(list);
		list.remove(2);
		System.out.println(list);
	}
}
