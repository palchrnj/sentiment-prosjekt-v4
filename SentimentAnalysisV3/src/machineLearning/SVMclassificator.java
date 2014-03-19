package machineLearning;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import newsAPI.JsonHandler;
import newsAPI.NewsArticlesRaw;

import org.encog.mathutil.randomize.Randomizer;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.ml.svm.KernelType;
import org.encog.ml.svm.SVM;
import org.encog.ml.svm.SVMType;
import org.encog.ml.svm.training.SVMSearchTrain;
import org.encog.ml.svm.training.SVMTrain;

import annotationStatistics.AnnotationValidator;

import com.google.gson.Gson;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import featureExctraction.FeaturesVektor;
import featureExctraction.NewsArticlesWithFeatures;

public class SVMclassificator {

        SVM svm;

        public SVMclassificator() {
            svm = new SVM(1,SVMType.EpsilonSupportVectorRegression,KernelType.RadialBasisFunction);
        	//svm = new SVM(678, true);
        }

        public static ArrayList<FeaturesVektor> getAgreedUponVectors(int i) throws IOException{
        	Gson g = new Gson();
        	JsonHandler handler = new JsonHandler("/Misc/NewsArticlesWithFeaturesCompleteWithVS.txt");
        	AnnotationValidator av = new AnnotationValidator();

        	ArrayList<FeaturesVektor> vectors = new ArrayList<FeaturesVektor>();
        	
        	
        	/* SET VECTOR ATTRIBUTES */
        	VectorAttributes va = new VectorAttributes();
        	
        	/* KATEGORI */
        	va.setAnalyse(true);
//        	va.setOkonomi(true);
//        	va.setBors(true);
        	
        	/* CLUES */
        	va.setAnalyticsMentioned(true);
        	va.setRecommenderCluesMentioned(true);
        	
        	/* LENGTH OF TEXT */
        	va.setAverageLengthOfWords(true);
        	va.setLengthOfLeadText(true);
        	va.setLengthOfMainText(true);
//        	va.setLengthOfTitle(true);
        	
        	/* NUMBER OF WORDSCLASSES */ 
//            va.setNumberOfSubstantives(true);
//        	va.setNumberOfverbs(true);
        	va.setNumberOfAdjectives(true);
        	
        	/* SENTIMENT BEARING WORDS */
        	va.setNumberOfNegativeAdjectives(true);
        	va.setNumberOfNegativeAdverbs(true);
        	va.setNumberOfNegativeVerbs(true);
        	va.setNumberOfNeutralAdjectives(true);
        	va.setNumberOfPositiveAdjectives(true);
        	va.setNumberOfPositiveAdverbs(true);
        	va.setNumberOfPositiveVerbs(true);
        	
        	/* SPECIAL CHARACTERS */
//        	va.setNumberOfExclamationMarks(true);
//        	va.setNumberOfQuestionMarks(true);
        	va.setNumberOfQuotes(true);
     
        
        	
        	
        	//NewsArticlesWithFeatures featureArticles = g.fromJson(handler.getNewsArticlesWithFeaturesSource(), NewsArticlesWithFeatures.class);
        	NewsArticlesWithFeatures featureArticles =g.fromJson(handler.getJsonSource(), NewsArticlesWithFeatures.class); 
        	//NewsArticles sentimentCheckArticles = g.fromJson(handler.getCheckArticles(), NewsArticles.class);
//        	av.initalizeAnnotationObject("C:/Users/Lars/Desktop/SentimentProject/SentimentAnalysis/src/xml/pal_annotated_1000_1501_19_13.json");
        	NewsArticlesRaw sentimentCheckArticles = av.getArticles();
        	
        	for(int j=0; j<featureArticles.getArticles().size(); j++){
//        		System.out.println("COUNTER: " + j);
//        		System.out.println("SENTIMENT FETURE: " + featureArticles.getArticles().get(j).getSentimentValue());
//        		System.out.println("SENTIMENT CHECK: " + sentimentCheckArticles.getArticles()[j].getSentimentValue());
        		if((featureArticles.getArticles().get(j).getSentimentValue()!=null) && (sentimentCheckArticles.getArticles()[j].getSentimentValue() != null)){
	        		if(av.parseSentimentValue(featureArticles.getArticles().get(j).getSentimentValue()).get(i).equals(av.parseSentimentValue(sentimentCheckArticles.getArticles()[j].getSentimentValue()).get(i))){
	        			vectors.add(featureArticles.getArticles().get(j).getVector(va, 3));
	        		
	        		} 
        		}

        	}
//        	System.out.println("Lengde på vector samling: " + vectors.size());
        	
//        	for(int k=0; k<vectors.size(); k++){
//        		System.out.println("Vecotr: " + k + " " + vectors.get(k).getFeatureVector() + "SENTIMENT TABLE: " + vectors.get(k).getSentimentVector());
//        	}
      
        	return vectors;
        }
        public static ArrayList<FeaturesVektor> getPerfectAgreedUponVectors() throws IOException{
        	Gson g = new Gson();
        	JsonHandler handler = new JsonHandler("/Misc/NewsArticlesWithFeaturesTitlePositiveNegativeCount.txt");
        	AnnotationValidator av = new AnnotationValidator();

        	ArrayList<FeaturesVektor> vectors = new ArrayList<FeaturesVektor>();
        	
        	/* SET VECTOR ATTRIBUTES */
        	VectorAttributes va = new VectorAttributes();
        	
        	
    

        	
        	/* KATEGORI */
//        	va.setAnalyse(true);
//        	va.setOkonomi(true);
//        	va.setBors(true);
//        	
//        	va.setNegativeTitle(true);
        	va.setPositiveTitle(true);
////      	
        	va.setPositiveTitleCount(true);
//        	va.setNegativeTitleCount(true);
////      	
        	/* CLUES */
//        	va.setAnalyticsMentioned(true);
        	va.setRecommenderCluesMentioned(true);
        	
        	/* LENGTH OF TEXT */
//        	va.setAverageLengthOfWords(true);
//        	va.setLengthOfLeadText(true);
//        	va.setLengthOfMainText(true);
//        	va.setLengthOfTitle(true);
//        	
        	/* NUMBER OF WORDSCLASSES */ 
//        	va.setNumberOfSubstantives(true);
//        	va.setNumberOfverbs(true);
//        	va.setNumberOfAdjectives(true);
        	
        	/* SENTIMENT BEARING WORDS */
        	va.setNumberOfNegativeAdjectives(true);
//        	va.setNumberOfNegativeAdverbs(true);
//        	va.setNumberOfNegativeVerbs(true);
//        	va.setNumberOfNeutralAdjectives(true);
        	va.setNumberOfPositiveAdjectives(true);
//        	va.setNumberOfPositiveAdverbs(true);
//        	va.setNumberOfPositiveVerbs(true);
        	
//        	/* SPECIAL CHARACTERS */
//        	va.setNumberOfExclamationMarks(true);
        	va.setNumberOfQuestionMarks(true);
//        	va.setNumberOfQuotes(true);
//     
        	
        	NewsArticlesWithFeatures featureArticles = g.fromJson(handler.getJsonSource(), NewsArticlesWithFeatures.class);
        	//NewsArticles sentimentCheckArticles = g.fromJson(handler.getCheckArticles(), NewsArticles.class);
//        	av.initalizeAnnotationObject("C:/Users/Lars/Desktop/SentimentProject/SentimentAnalysis/src/xml/pal_annotated_1000_1501_19_13.json");
        	NewsArticlesRaw sentimentCheckArticles = av.getArticles();
        	
        	for(int j=0; j<featureArticles.getArticles().size(); j++){
//        		System.out.println("COUNTER: " + j);
//        		System.out.println("SENTIMENT FETURE: " + featureArticles.getArticles().get(j).getSentimentValue());
//        		System.out.println("SENTIMENT CHECK: " + sentimentCheckArticles.getArticles()[j].getSentimentValue());
        		if((featureArticles.getArticles().get(j).getSentimentValue()!=null) && (sentimentCheckArticles.getArticles()[j].getSentimentValue() != null)){
	        		if(av.parseSentimentValue(featureArticles.getArticles().get(j).getSentimentValue()).equals(av.parseSentimentValue(sentimentCheckArticles.getArticles()[j].getSentimentValue()))){
//	        			System.out.println(featureArticles.getArticles().get(j).getVector(va,3).getFeatureVectorAsArray());
//	        			System.out.println(featureArticles.getArticles().get(j).getVector(va,3).getSentimentAsArray());
	        			vectors.add(featureArticles.getArticles().get(j).getVector(va, 3));
	        			System.out.println(j);
	        		
	        		} 
        		}

        	}
//        	System.out.println("Lengde på vector samling: " + vectors.size());
        	
//        	for(int k=0; k<vectors.size(); k++){
//        		System.out.println("Vecotr: " + k + " " + vectors.get(k).getFeatureVector() + "SENTIMENT TABLE: " + vectors.get(k).getSentimentVector());
//        	}
       
        	return vectors;
        }
        
        public static ArrayList<String> getFeatureSelectionString() {
    		ArrayList<String> stringList = new ArrayList<String>();
    		for (int i = 1; i < 4096; i++) {
    			stringList.add(getBinaryStringLength12(i));
    		}
    		return stringList;
    	}
    	
    	public static String getBinaryStringLength12(int i) {
    		String str = Integer.toBinaryString(i);
    		while (str.length() < 12) {
    			str = "0" + str;
    		}
    		return str;
    	}
    	
        public static ArrayList<FeaturesVektor> CreateVectorPermutation(String binaryInitiator) throws IOException{
        	Gson g = new Gson();
        	JsonHandler handler = new JsonHandler("/Misc/NewsArticlesWithFeaturesTitlePositiveNegativeCount.txt");
        	AnnotationValidator av = new AnnotationValidator();

        	ArrayList<FeaturesVektor> vectors = new ArrayList<FeaturesVektor>();
        	/* SET VECTOR ATTRIBUTES */
        	VectorAttributes va = new VectorAttributes();
        	


        	//000110011101

        	/* KATEGORI */
        	if(binaryInitiator.charAt(0)=='1'){
        		va.setAnalyse(true);
        	}
        	if(binaryInitiator.charAt(1)=='1'){
        		va.setBors(true);	 
        	}
        	if(binaryInitiator.charAt(2)=='1'){
        	 	va.setNegativeTitle(true);
        	}
        	if(binaryInitiator.charAt(3)=='1'){
        	 	va.setPositiveTitle(true);
        	}
        	if(binaryInitiator.charAt(4)=='1'){
        		va.setPositiveTitleCount(true);
        	}
        	if(binaryInitiator.charAt(5)=='1'){
        		va.setNegativeTitleCount(true);
        	}
        	if(binaryInitiator.charAt(6)=='1'){
        		va.setAnalyticsMentioned(true);
        	}
        	if(binaryInitiator.charAt(7)=='1'){
        		va.setRecommenderCluesMentioned(true);
        	}
        	if(binaryInitiator.charAt(8)=='1'){
        		va.setNumberOfNegativeAdjectives(true);
        	}
        	if(binaryInitiator.charAt(9)=='1'){
        	 	va.setNumberOfPositiveAdjectives(true);
        	}
        	if(binaryInitiator.charAt(10)=='1'){
        		va.setNumberOfExclamationMarks(true);
        	}
        	if(binaryInitiator.charAt(11)=='1'){
        		va.setNumberOfQuestionMarks(true);
        	}

        	NewsArticlesWithFeatures featureArticles = g.fromJson(handler.getJsonSource(), NewsArticlesWithFeatures.class);
        	//NewsArticles sentimentCheckArticles = g.fromJson(handler.getCheckArticles(), NewsArticles.class);
//        	av.initalizeAnnotationObject("C:/Users/Lars/Desktop/SentimentProject/SentimentAnalysis/src/xml/pal_annotated_1000_1501_19_13.json");
        	NewsArticlesRaw sentimentCheckArticles = av.getArticles();
        	
        	for(int j=0; j<featureArticles.getArticles().size(); j++){
//        		System.out.println("COUNTER: " + j);
//        		System.out.println("SENTIMENT FETURE: " + featureArticles.getArticles().get(j).getSentimentValue());
//        		System.out.println("SENTIMENT CHECK: " + sentimentCheckArticles.getArticles()[j].getSentimentValue());
        		if((featureArticles.getArticles().get(j).getSentimentValue()!=null) && (sentimentCheckArticles.getArticles()[j].getSentimentValue() != null)){
	        		if(av.parseSentimentValue(featureArticles.getArticles().get(j).getSentimentValue()).equals(av.parseSentimentValue(sentimentCheckArticles.getArticles()[j].getSentimentValue()))){
//	        			System.out.println(featureArticles.getArticles().get(j).getVector(va,3).getFeatureVectorAsArray());
//	        			System.out.println(featureArticles.getArticles().get(j).getVector(va,3).getSentimentAsArray());
	        			vectors.add(featureArticles.getArticles().get(j).getVector(va, 3));
	        		
	        		} 
        		}

        	}
//        	System.out.println("Lengde på vector samling: " + vectors.size());
        	
//        	for(int k=0; k<vectors.size(); k++){
//        		System.out.println("Vecotr: " + k + " " + vectors.get(k).getFeatureVector() + "SENTIMENT TABLE: " + vectors.get(k).getSentimentVector());
//        	}
       
        	return vectors;
        }
        
        public static void findBestFeatures() throws IOException{
        	double currentResult = 0.0;
        	String currentVector = "";
        	double bestResult = 10000;
        	String bestVector = "";
        	ArrayList<String> under35Vectors = new ArrayList<String>(); 
        	
        	int counter = 0;
        	
        	
        	for(String str : getFeatureSelectionString()){
        		currentVector = str;
        		currentResult = runNBCfindBestFeatures(str);
        		
        		System.out.println("Result: " + currentResult + " Vector used : " + currentVector + " COUNTER: " + counter);
        		if(currentResult < 0.35){
        			under35Vectors.add(currentVector);
        		}
        		if(currentResult < bestResult){
        			bestResult = currentResult;
        			bestVector = currentVector;
        		}
        		counter++;
        	}
        	System.out.println("Beste result: "  + bestResult);
        	System.out.println("BEst vector: " + bestVector);
        	System.out.println("List of under 35 vectors " + under35Vectors.toString());
        	
        }
        
        public void writeToWekaDataSet() throws IOException{
        	ArrayList<FeaturesVektor> vectors = this.getAgreedUponVectors(3);
        	
        	String wekaString = "numberOfAdjectives,numberOfSubstantives,numberOfverbs,lengthOfTitle,lengthOfLeadText,lengthOfMaext,numberOfExclamationMarks,numberOfQuestionMarks,numberOfQuotes,averageLengthOfWords,analyticsMentioned,recommenderCluesMentioned,numberOfPositiveAdjectives,numberOfNegativeAdjectives,numberOfNeutralAdjectives,isBors,isAnalyse,isOkonomi,sentimentClassification\n";
        	
        	for(FeaturesVektor v : vectors){
        		for(Double d : v.getFeatureVector()){
        			wekaString += d+",";
        		}
        		wekaString = wekaString + v.getSentimentVector().get(0) + "\n";
        	}
        	
        	
        	System.out.println(wekaString);
        	
//			Writer out = new BufferedWriter(new OutputStreamWriter(
//			    new FileOutputStream("C:/Users/Lars/Desktop/SentimentProject/SentimentAnalysis/src/xml/wekaDataSet.txt"), "UTF-8"));
//			try {
//			    out.write(wekaString);
//			} finally {
//			    out.close();
//			}

        }

        public static MLDataSet createMlDataSetForVectors(ArrayList<FeaturesVektor> v){
        	MLDataSet dataset = new BasicMLDataSet();
//        	System.out.println(v.size());
//        	double max = 0.0;
        	for(FeaturesVektor fv : v){
      
//        		if(fv.getFeatureVector().get(0) > max){
//        			System.out.println(fv.getFeatureVector().get(0));
//        			max = fv.getFeatureVector().get(0);
//        		}
        		MLData input = new BasicMLData(fv.getFeatureVectorAsArray());
        		MLData output = new BasicMLData(fv.getSentimentAsArray());
        		dataset.add(input, output);
        	}
//        	System.out.println("MAX NEGATIVE ADJEKTIV ER : " + max);
        	return dataset;
        }
        
        public boolean trainSVMclassificator(MLDataSet dataset) {
        	// Create datasets

        	SVMTrain trainer = new SVMTrain(svm, dataset);
        	trainer.iteration();
//        	System.out.println("ER TRENING FERDIG: " + trainer.isTrainingDone());

        	int counter = 1;
        	while (! trainer.isTrainingDone()) {
        		System.out.println("Training step: " + counter++);
        		trainer.iteration();
        	}
        	return true;
        	// train dataset
        }
        public boolean train2SVMclassificator(MLDataSet dataset){
        	SVMTrain trainer2 = new SVMTrain(svm, dataset);
     
      
        	trainer2.iteration();
        	System.out.println("ER TRENING FERDIG: " + trainer2.isTrainingDone());

        	int counter = 1;
        	while (! trainer2.isTrainingDone()) {
        		System.out.println("Training step: " + counter++);
        		trainer2.iteration();
        	}
           	trainer2.setFold(3);
           	//trainer2.iteration();
        	return true;
        	
        }

        public double calculateError(MLDataSet dataset) {
        	return svm.calculateError(dataset);
        }
        
        // ArrayList.get(0) training dat
        // ArrayList.get(1) test dat
        public static ArrayList<MLDataSet> partitionDataSet(MLDataSet inputset, int trainingSetLength) {
        	if (trainingSetLength >= inputset.size()) {
        		throw new RuntimeException("You need to reserve som data for testing idiot!");
        	}
        	ArrayList<MLDataSet> returnset = new ArrayList<MLDataSet>();
        	returnset.add(new BasicMLDataSet());
        	returnset.add(new BasicMLDataSet());
        	int counter = 0;
        	for (MLDataPair datapair : inputset) {
        		if (counter < trainingSetLength) {
        			returnset.get(0).add(datapair);
        		} else {
        			returnset.get(1).add(datapair);
        		}
        		counter++;
        	}
        	return returnset;
        }

        public static ArrayList<MLDataSet> partitionDataSetRandom(MLDataSet inputset, int trainingSetLength) {
        	int testSetLength = inputset.size() - trainingSetLength;
        	if (trainingSetLength >= inputset.size()) {
        		throw new RuntimeException("You need to reserve som data for testing idiot!");
        	}
        	ArrayList<MLDataSet> returnset = new ArrayList<MLDataSet>();
        	returnset.add(new BasicMLDataSet());
        	returnset.add(new BasicMLDataSet());
        	int counter = 0;
        	ArrayList<Integer> selectedNumbers = new ArrayList<Integer>();
        	while (counter < testSetLength) {
        		Random r = new Random();
        		int nextInt = r.nextInt(inputset.size());
        		if (! selectedNumbers.contains(nextInt)) {
        			selectedNumbers.add(nextInt);
        			returnset.get(1).add(inputset.get(nextInt));
        			counter++;
        		}
        	}
        	
        	for (MLDataPair datapair : inputset) {
        		if (! returnset.contains(datapair)) {
        			returnset.get(0).add(datapair);
        		}
        	}
        	return returnset;
        }
        
        
        
        public String calculateTableForNB(){
        	String table = ""; 
        	return table;

        }
        
    	public static MLDataSet randomShufftle(MLDataSet unshuffeledDataSet) {
            MLDataSet shuffeledDataSet = new BasicMLDataSet();
   		ArrayList<MLDataPair> pairList = new ArrayList<MLDataPair>();
   		for (MLDataPair pair : unshuffeledDataSet) {
           	 pairList.add(pair);
            }
   		Collections.shuffle(pairList);
   		for (MLDataPair pair : pairList) {
   			shuffeledDataSet.add(pair);
   		}
   		return shuffeledDataSet;
       }

        
        public static ArrayList<Fold> genereateNFold(MLDataSet dataset, int n) {
        	ArrayList<Fold> foldSet = new ArrayList<Fold>();
        	double delta = (((double) dataset.size()) / ((double) n));
        	int startIndex = 0;
        	int stopIndex = (int) delta;
        	for (double foldNum = 0; foldNum < n; foldNum++) {
        		MLDataSet trainingSet = new BasicMLDataSet();
        		MLDataSet testSet = new BasicMLDataSet();
        		for (int index = 0; index < dataset.size(); index++) {
        			if (startIndex <= index && index < stopIndex) {
        				testSet.add(dataset.get(index));
        			} else {
        				trainingSet.add(dataset.get(index));
        			}
        		}
        		foldSet.add(new Fold(trainingSet, testSet));
        		startIndex = (int) ((foldNum + 1.0) * delta);
        		stopIndex = (int) ((foldNum + 2.0) * delta);
        	}
        	return foldSet;
        }
        
            
        
        public static double runSVMGrowingDataSet() throws IOException{
        	
        	ArrayList<Fold> folds = new ArrayList<Fold>();
        	MLDataSet data = createMlDataSetForVectors(getPerfectAgreedUponVectors());
        	int dataSize = data.size();
        	double averageError = 100;
        	double totalError = 0;
        	System.out.println(data.size());
        	for (int j = 50; j <= dataSize; j++) {
        		data = createMlDataSetForVectors(getPerfectAgreedUponVectors());
        		averageError = 100;
        		totalError = 0;
        		data = partitionDataSet(data, j).get(0);
        		for(int i=0; i<5; i++){
        			data = randomShufftle(data);
        			folds = genereateNFold(data, 5);
        			for(Fold f : folds){
        				SVM svm = new SVM(f.trainingSet.size(),SVMType.EpsilonSupportVectorRegression,KernelType.RadialBasisFunction);
        				SVMTrain trainer = new SVMTrain(svm, f.getTrainingSet());
        				trainer.iteration();
        				double thisError = svm.calculateError(f.getTestSet());
        				totalError += thisError;
        			}
        		}
        		averageError = totalError / (folds.size()*5);
        		System.out.println(j + " " + averageError + ";");
        	}        	
        	return averageError;
        		
        }
        
        
        
   public static double runANNGrowingDataSet() throws IOException{
        	
        	ArrayList<Fold> folds = new ArrayList<Fold>();
        	MLDataSet data = createMlDataSetForVectors(getPerfectAgreedUponVectors());
        	int dataSize = data.size();
        	double averageError = 100;
        	double totalError = 0;
        	System.out.println(data.size());
        	for (int j = 50; j <= dataSize; j++) {
        		data = createMlDataSetForVectors(getPerfectAgreedUponVectors());
        		averageError = 100;
        		totalError = 0;
        		data = partitionDataSet(data, j).get(0);
        		for(int i=0; i<5; i++){
        			data = randomShufftle(data);
        			folds = genereateNFold(data, 5);
        			for(Fold f : folds){
        				ANNclassificator a = new ANNclassificator(f.getTrainingSet().getInputSize());
            			a.trainANNclassificator(f.getTrainingSet());
    	            	double thisError = a.calculateError(f.getTestSet());
//    	            	System.out.println(thisError);
    	        		totalError += thisError;
        			}
        		}
        		averageError = totalError / (folds.size()*5);
        		System.out.println(j + " " + averageError + ";");
        	}        	
        	return averageError;
        		
        }
        
        
        
        
        
   public static double runSVMFulLDataSet() throws IOException{
        	
        	ArrayList<Fold> folds = new ArrayList<Fold>();
        	MLDataSet data = createMlDataSetForVectors(getPerfectAgreedUponVectors());
        	int dataSize = data.size();
        	double averageError = 100;
        	double totalError = 0;
        	System.out.println(data.size());
        
        		averageError = 100;
        		totalError = 0;
        		for(int i=0; i<5; i++){
        			data = randomShufftle(data);
        			folds = genereateNFold(data, 5);
        			for(Fold f : folds){
        				SVM svm = new SVM(f.trainingSet.size(),SVMType.EpsilonSupportVectorRegression,KernelType.RadialBasisFunction);
        				SVMTrain trainer = new SVMTrain(svm, f.getTrainingSet());
        				trainer.iteration();
        				double thisError = svm.calculateError(f.getTestSet());
        				totalError += thisError;
        			}
        		}
        		averageError = totalError / (folds.size()*5);
        		System.out.println(averageError + ";");
        	     	
        	return averageError;
        		
        }
        
     public static double runSVMfindBestFeatures(String binaryInitiator) throws IOException{
        	
        	ArrayList<Fold> folds = new ArrayList<Fold>();
        	MLDataSet data = createMlDataSetForVectors(CreateVectorPermutation(binaryInitiator));
        	int dataSize = data.size();
        	double averageError = 100;
        	double totalError = 0;
        	System.out.println(data.size());
        
        		averageError = 100;
        		totalError = 0;
        		for(int i=0; i<5; i++){
        			data = randomShufftle(data);
        			folds = genereateNFold(data, 5);
        			for(Fold f : folds){
        				SVM svm = new SVM(f.trainingSet.size(),SVMType.EpsilonSupportVectorRegression,KernelType.RadialBasisFunction);
        				SVMTrain trainer = new SVMTrain(svm, f.getTrainingSet());
        				trainer.iteration();
        				double thisError = svm.calculateError(f.getTestSet());
        				totalError += thisError;
        			}
        		}
        		averageError = totalError / (folds.size()*5);
        		System.out.println(averageError + ";");
        	     	
        	return averageError;
        		
        }

     public static double runNBCfindBestFeatures(String binaryInitiator) throws IOException{
     	
     	ArrayList<Fold> folds = new ArrayList<Fold>();
     	MLDataSet data = createMlDataSetForVectors(CreateVectorPermutation(binaryInitiator));
     	int dataSize = data.size();
     	double averageError = 100;
     	double totalError = 0;
     	System.out.println(data.size());
     
     		averageError = 100;
     		totalError = 0;
     		for(int i=0; i<5; i++){
     			data = randomShufftle(data);
     			folds = genereateNFold(data, 5);
     			for(Fold f : folds){
     				NBclassificator nb = new NBclassificator();
        			nb.trainNBclassificator(f.getTrainingSet());	
	            	double thisError = nb.calculateError(f.getTestSet());
	            //	System.out.println(thisError);
	        		totalError += thisError;
     			}
     		}
     		averageError = totalError / (folds.size()*5);
     		System.out.println(averageError + ";");
     	     	
     	return averageError;
     		
     }
     
     public static double runANNfindBestFeatures(String binaryInitiator) throws IOException{
      	
      	ArrayList<Fold> folds = new ArrayList<Fold>();
      	MLDataSet data = createMlDataSetForVectors(CreateVectorPermutation(binaryInitiator));
      	int dataSize = data.size();
      	double averageError = 100;
      	double totalError = 0;
      	System.out.println(data.size());
      
      		averageError = 100;
      		totalError = 0;
      		for(int i=0; i<5; i++){
      			data = randomShufftle(data);
      			folds = genereateNFold(data, 5);
      			for(Fold f : folds){
      				ANNclassificator a = new ANNclassificator(f.getTrainingSet().getInputSize());
        			a.trainANNclassificator(f.getTrainingSet());
	            	double thisError = a.calculateError(f.getTestSet());
//	            	System.out.println(thisError);
	        		totalError += thisError;
      			}
      		}
      		averageError = totalError / (folds.size()*5);
      		System.out.println(averageError + ";");
      	     	
      	return averageError;
      		
      }
        
        public static double runANN() throws IOException{
        	double averageError = 100;
        	double totalError = 0;
        	ArrayList<Fold> folds = new ArrayList<Fold>();

    		
        	MLDataSet data = createMlDataSetForVectors(getPerfectAgreedUponVectors());
        	for(int i=0; i<5; i++){
        		data = randomShufftle(data);
        		folds = genereateNFold(data, 5);
        		
        		for(Fold f : folds){
        			ANNclassificator a = new ANNclassificator(f.getTrainingSet().getInputSize());
        			a.trainANNclassificator(f.getTrainingSet());
	            	double thisError = a.calculateError(f.getTestSet());
//	            	System.out.println(thisError);
	        		totalError += thisError;
        		}
        	}
        	
        	averageError = totalError/(folds.size()*5);
        	System.out.println(averageError);
        	return averageError;

        }
       
        
        public static double runNBC() throws IOException{
        	double averageError = 100;
        	double totalError = 0;
        	ArrayList<Fold> folds = new ArrayList<Fold>();

   
        	MLDataSet data = createMlDataSetForVectors(getPerfectAgreedUponVectors());
        	
        	
        	for(int i=0; i<5; i++){
        		data = randomShufftle(data);
        		folds = genereateNFold(data, 5);
        		
        		for(Fold f : folds){
        	     	NBclassificator nb = new NBclassificator();
        			nb.trainNBclassificator(f.getTrainingSet());	
	            	double thisError = nb.calculateError(f.getTestSet());
	            //	System.out.println(thisError);
	        		totalError += thisError;
        		}
        	}
        	
        	averageError = totalError/ ( folds.size() * 5);
        	System.out.println(averageError);
        	return averageError;

        }
        
        
        
        public static void main(String[] args) throws IOException {
        
        	runANNGrowingDataSet();
//        	System.out.println("Starter ANN");
//        	runSVMFulLDataSet();
//          findBestFeatures();
        	
        	
        	
//    		MLDataSet data = createMlDataSetForVectors(getPerfectAgreedUponVectors());
//    		data = randomShufftle(data);
//    		System.out.println("herejdfds");
//    		genereateNFold(data, 5);
        	
//                AnnotationValidator a1 = new AnnotationValidator();
//                AnnotationValidator a2 = new AnnotationValidator();

//                a1.initalizeAnnotationObject("/Users/paulus/Dropbox/Data5/Prosjektoppgave/lars_annotated_1000_1426_19_13.json");
//                a2.initalizeAnnotationObject("/Users/paulus/Dropbox/Data5/Prosjektoppgave/pal_annotated_1000_1501_19_13.json");

//                double[][] matrix = PredictingAggregateClassification.getMatrixRepresentationOfFullyAgreedArticles(a1, a2);
//                double[][] matrixWithInput = PredictingAggregateClassification.returnThreeColumns(matrix, 0, 1, 2);
//                double[] matrixWithOutput = PredictingAggregateClassification.returnOneColumn(matrix, 3);

//                System.out.println("Input lenght: " + matrixWithInput.length);
//                System.out.println("Input[0] length: " + matrixWithInput[0].length);
//                System.out.println("Output length: " + matrixWithOutput.length);

//              PredictingAggregateClassification.printMatrix(matrixWithInput);
//              System.out.println(" ");
////              PredictingAggregateClassification.printMatrix(matrixWithOutput);
//
//                SVMclassificator svmc = new SVMclassificator();
////                int cutoff = 400;
////                svmc.trainSVMclassificator(MatrixOperations.getFirstNEntries(matrixWithInput, cutoff), MatrixOperations.getFirstNEntries(matrixWithOutput, cutoff));
////                double error = svmc.calculateError(MatrixOperations.getLastNEntries(matrixWithInput, matrixWithInput[0].length-cutoff), MatrixOperations.getLastNEntries(matrixWithOutput, matrixWithOutput.length-cutoff));
//                System.out.println(error);
//                MLData inputinstance = new BasicMLData(new double[]{1.0,1.0,1.0});
//                System.out.println(svmc.svm.classify(inputinstance));
//                System.out.println(error * (matrixWithOutput.length-cutoff));
//              MLData mldata = new BasicMLData(null);
//              MLDataSet input;
//              MLDataSet output;
        	
//        		for (int i = 0; i < 332; i++) {
//        			SVMclassificator c = new SVMclassificator();
//        			MLDataSet data = createMlDataSetForVectors(getPerfectAgreedUponVectors());
//        			data = randomShufftle(data);
//        			ArrayList<MLDataSet> dataset = partitionDataSet(data, i);
//        			c.trainSVMclassificator(dataset.get(0));
//        			System.out.println(c.calculateError(partitionDataSet(dataset.get(1), 100).get(0)));
//				}
//        		
//        	    SVMclassificator c = new SVMclassificator();
//        		MLDataSet data = createMlDataSetForVectors(getPerfectAgreedUponVectors());
//        		ArrayList<MLDataSet> dataset = partitionDataSet(data, (int) (data.size()*0.20));
//        		c.trainSVMclassificator(dataset.get(0));
//        		System.out.println(c.calculateError(dataset.get(1)));
        		
        		/* TRAIN ON BEST DATA */
        	
//        			SVMclassificator c = new SVMclassificator();
//        			MLDataSet data = createMlDataSetForVectors(getPerfectAgreedUponVectors());
//        			System.out.println(getPerfectAgreedUponVectors().size());
//        			int length = (int) (data.size() * 0.80);
//        			ArrayList<MLDataSet> dataset = partitionDataSet(data, length);
////        			c.trainSVMclassificator(dataset.get(0));
//        			c.train2SVMclassificator(dataset.get(0));
//        			System.out.println(c.calculateError(dataset.get(1)));
        			
        			
//        			for(int i = 0; i<getPerfectAgreedUponVectors().size(); i++){
//        				getPerfectAgreedUponVectors().get(i).getFeatureVector();
//        			}
//        		for (int i = 1; i < 300; i++) {
//        			ANNclassificator a = new ANNclassificator(18);
//        			MLDataSet data = createMlDataSetForVectors(getPerfectAgreedUponVectors());
//        			ArrayList<MLDataSet> dataset = partitionDataSet(data, i);
//        			a.trainANNclassificator(dataset.get(0));
//        			System.out.println(i + " " + a.calculateError(partitionDataSet(dataset.get(1), 100).get(0)) + ";");
//        		}
        		/* WRITE DATASET */
//        		SVMclassificator c = new SVMclassificator();
//        		MLDataSet data = createMlDataSetForVectors(getPerfectAgreedUponVectors());
//        		c.writeToWekaDataSet();
//        		

//        		SVMclassificator c = new SVMclassificator();
//        		MLDataSet data = createMlDataSetForVectors(c.getPerfectAgreedUponVectors());
//        		c.trainSVMclassificator(partitionDataSet(data, 400).get(0));
//        		System.out.println(c.calculateError(partitionDataSet(data, 400).get(1)));
        		
        	
//        		NBclassificator nb = new NBclassificator();
//        		MLDataSet data = createMlDataSetForVectors(getPerfectAgreedUponVectors());
//    			System.out.println(getPerfectAgreedUponVectors().size());
//    			int length = (int) (data.size() * 0.80);
//    			ArrayList<MLDataSet> dataset = partitionDataSet(data, length);
//        		nb.trainNBclassificator(dataset.get(0));	
//        		System.out.println("The error is: " + nb.calculateError(dataset.get(1)));
        		
        	
        	
//        		System.out.println("[");
//        		for (int testSetSize = 1; testSetSize < 432; testSetSize++) {
//        			double avg = 0.0;
//        			double averageruns = 10.0;
//        			for (int i = 1; i < averageruns; i++) {
//        				SVMclassificator c1 = new SVMclassificator();
//        				MLDataSet data1 = createMlDataSetForVectors(c1.getPerfectAgreedUponVectors());
//        				ArrayList<MLDataSet> randomData = partitionDataSetRandom(data1, testSetSize);
//        				c1.trainSVMclassificator(randomData.get(0));
//        				avg +=c1.calculateError(randomData.get(1));
////        				System.out.println(c1.calculateError(randomData.get(1)));
//        			}
//        			System.out.println(testSetSize + " "+ avg / averageruns + "; ");
//        		}
        	
        }
}