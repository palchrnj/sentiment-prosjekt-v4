package cot;

public class CoT {
	
	public String str1;
	public String str2;
	
	public CoT(String str1, String str2) {
		this.str1 = str1;
		this.str2 = str2;
	}
	
	public String toString() {
		return String.format("(%s, %s)", str1, str2);
	}
}
