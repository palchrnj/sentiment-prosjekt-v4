package utils;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import preProcessing.TextFileHandler;
import sun.misc.Sort;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import cot.CotChiSquared;
import cot.CotCountTFDF;
import featureExctraction.WordCountList;

public class CotRankingHelper {
	
	
	//CHI SQUARED METHOD
	public ArrayList<CotChiSquared> generateChiSquaredList() throws JsonSyntaxException, IOException{
		
		ArrayList<CotChiSquared> chiSquaredList = new ArrayList<CotChiSquared>();
		
//		Co-occurring frequency mellom to termer (u og v) innenfor radius r: freq_r(u,v)
//		Frequency (antall title + lead term u forekommer) av term u: freq(u)
//		Frequency (antall title + lead term u forekommer) av term u: freq(v)
//		antall titles + lead: N
//
//		Kan da regne ut:
//		chi-sq = (freq_r(u,v) - N * freq(u) * freq(v)) ^2 / (N * freq(u) * freq(v))
		
		Gson gson = new Gson();

		//GET LIST OF ALL WORDS
		WordCountList allWordsTFDF = gson.fromJson(TextFileHandler.getWclList(), WordCountList.class);
		
		//CREATE TYPE FOR GSON JSON PARSING, because of hashmap
		Type stringIntegergMap = new TypeToken<HashMap<String, CotCountTFDF>>(){}.getType();
		HashMap<String, CotCountTFDF> cotsTFDF = gson.fromJson(TextFileHandler.getCotsTFDF(), stringIntegergMap);
		
		//GO THROUGH COTS
		for (String key : cotsTFDF.keySet()) {
				double ccTF = 1;
				int vTF = 1;
				int uTF = 1;
				int N = 1;
			   //System.out.println("Key = " + key + " - " + cotsTFDF.get(key));
				String v = "";
				String u = "";
					
				v = key.split(" ")[0];
			   
			   if(key.split(" ").length>1){
				   u = key.split(" ")[1];
			   }
			   
			   for(int i=0; i<allWordsTFDF.getWords().size(); i++){
				   if(allWordsTFDF.getWords().get(i).getWord().equals(v)){
					   //System.out.println("FANT V");
					   vTF = allWordsTFDF.getWords().get(i).getTermFrequency();
				   }
				   else if(allWordsTFDF.getWords().get(i).getWord().equals(u)){
					   //System.out.println("FANT U");
					   uTF = allWordsTFDF.getWords().get(i).getTermFrequency();
				   }
			   }
			   
			   ccTF = cotsTFDF.get(key).getTermFrequency();
			   N = allWordsTFDF.getTotalTitleCount()+allWordsTFDF.getTotalLeadTextCount();
			   
			   
			   //chi-sq = (freq_r(u,v) - N * freq(u) * freq(v)) ^2 / (N * freq(u) * freq(v))
			   N = allWordsTFDF.getTotalLeadTextCount() + allWordsTFDF.getTotalTitleCount();
			   
			   //CALCULATE CHI SQUARED
			   double chiSqr = Math.pow((ccTF-(N*uTF+vTF)), 2)/(N*uTF*vTF);
			   //System.out.println(chiSqr);
			   CotChiSquared ccs= new CotChiSquared();
			   ccs.setChiSquaredValue(chiSqr);
			   ccs.setCot(key);
			   chiSquaredList.add(ccs);
			   //System.out.println("U: " + u +" V: " + v);
			   //System.out.println("ALLE VERDIER: " + "CCTF: " + ccTF + " VTF: " + vTF + " UTF: " + uTF + " N: " + N);
		}
	
		//System.out.println("Cotswords" + allWordsTFDF.getWords().size() + " Hashmap size: " + cotsTFDF.size());
		return chiSquaredList;
		
	}
	
	//FILE HANDLER METHODS
	public void writeRankedListToFile(String text, String path, String name) throws IOException{
		Writer out = new BufferedWriter(new OutputStreamWriter(
			new FileOutputStream(path + "/"+name+".json"), "UTF-8"));
			try {
			    out.write(text);
			} finally {
			    out.close();
			}
	}
	public String getPath() {
	    String path = String.format("%s/%s", System.getProperty("user.dir"), this.getClass().getPackage().getName().replace(".", "/"));
	    return path.split(this.getClass().getPackage().getName())[0]+"/ArticleResources/";
	}
	
	//MAIN METHOD FOR GENERATING FILES
	public static void main(String[] args) throws JsonSyntaxException, IOException{
		CotRankingHelper crh = new CotRankingHelper();
		
		ArrayList<CotChiSquared> csl = crh.generateChiSquaredList();
		System.out.println(csl.size());
		
		//SORT SINCE ChiSquared CLASS IMPELEMNTS COMPARABLE
		Collections.sort(csl);
		Gson g = new Gson();
		String chiSqrtCotRanked = g.toJson(csl);
		crh.writeRankedListToFile(chiSqrtCotRanked, crh.getPath()+"/COTRanking/", "ChiSquaredRanked");
	}
}
