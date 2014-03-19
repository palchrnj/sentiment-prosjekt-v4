package cot;

import java.util.ArrayList;

public class COTRankingCreater {
	
	int radius;
	ArrayList<COTStat> fullcotlist = new ArrayList<COTStat>();
	
	public String getPath(int r) {
	    String path = String.format("%s/%s", System.getProperty("user.dir"), this.getClass().getPackage().getName().replace(".", "/"));
	    return path.split(this.getClass().getPackage().getName())[0]+"ArticleResources/CoTs/r="+r;
	}
	
	
	
	public static void main(String[] args) {
		COTRankingCreater ctc = new COTRankingCreater();
		System.out.println(ctc.getPath(2));
	}
	
}
