package machineLearning;

import java.util.ArrayList;
import java.util.Collections;

import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.supervised.attribute.Discretize;

public class J48Classifier {
	
	 public static double runJ48(MLDataSet data) throws Exception{
		double totalPrecision = 0;
		double averagePrecision = 0;
     	ArrayList<Fold> folds = new ArrayList<Fold>();
     	
     	for(int i = 0; i < 5; i++){
     		data = randomShufftle(data);
     		folds = genereateNFold(data, 5);
     		
     		for(Fold f : folds){
     			Instances trainingSet = InstancesMLDataSetWrapper.convert(f.getTrainingSet());
     			Instances testSet = InstancesMLDataSetWrapper.convert(f.getTestSet());
     	     	
     			Discretize filter = new Discretize();
     		    filter.setInputFormat(trainingSet);
     		    trainingSet = Filter.useFilter(trainingSet, filter);
     		    testSet  = Filter.useFilter(testSet,  filter);

     		    Classifier tree = new J48();
     			ArrayList<String> options = new ArrayList<String>();
     			//[-C 0.10, -M 2, -N 2, -R, -S, -J]
     			options.add("-C 0.10"); // // 0.05, 0.10, 0.15, 0.20, 0.25, 0.30, 0.35, 0.40, 0.45, 0.5
     			options.add("-M 2"); // 1, 2, 3, 4, 5
     			options.add("-N 2"); // 1, 2, 3, 4
     			options.add("-R");
     			options.add("-S");
     			options.add("-J");
     			String optionArray[]=options.toArray(new String[options.size()]);
     			tree.setOptions(optionArray);
     			tree.buildClassifier(trainingSet);

     			// Test the model
     			Evaluation eTest = new Evaluation(testSet);
     			eTest.evaluateModel(tree, testSet);
     			double thisPrecision = eTest.correct() / testSet.numInstances();
//     			for (int j = 0; j < testSet.numInstances(); j++) {
//     				printMatrix(tree.distributionForInstance(testSet.instance(j)));
//				}
     			
     			// Print the result à la Weka explorer:
//     			String strSummary = eTest.toSummaryString();
//     			System.out.println(strSummary);
     			
//     			nb.trainNBclassificator(f.getTrainingSet());
//     			double thisError = nb.calculateError(f.getTestSet());
     			totalPrecision += thisPrecision;
     		}
     	}
     	averagePrecision = totalPrecision/ ( folds.size() * 5);
     	return averagePrecision;
    }

	 public static double runJ48WithOptions(MLDataSet data, String[] optionArray) throws Exception{
		 double totalPrecision = 0;
		 double averagePrecision = 0;
		 ArrayList<Fold> folds = new ArrayList<Fold>();
		 
		 for(int i = 0; i < 5; i++){
			 data = randomShufftle(data);
			 folds = genereateNFold(data, 5);
			 
			 for(Fold f : folds){
				 Instances trainingSet = InstancesMLDataSetWrapper.convert(f.getTrainingSet());
				 Instances testSet = InstancesMLDataSetWrapper.convert(f.getTestSet());
				 
				 Discretize filter = new Discretize();
				 filter.setInputFormat(trainingSet);
				 trainingSet = Filter.useFilter(trainingSet, filter);
				 testSet  = Filter.useFilter(testSet,  filter);
				 
				 J48 tree = new J48();
				 tree.setOptions(optionArray);
				 tree.buildClassifier(trainingSet);
				 
// Test the model
				 Evaluation eTest = new Evaluation(testSet);
				 eTest.evaluateModel(tree, testSet);
				 double thisPrecision = eTest.correct() / testSet.numInstances();
				 
// Print the result à la Weka explorer:
//     			String strSummary = eTest.toSummaryString();
//     			System.out.println(strSummary);
				 
//     			nb.trainNBclassificator(f.getTrainingSet());
//     			double thisError = nb.calculateError(f.getTestSet());
				 totalPrecision += thisPrecision;
			 }
		 }
		 averagePrecision = totalPrecision/ ( folds.size() * 5);
		 return averagePrecision;
	 }
	 
	public static double tuneJ48(MLDataSet data) throws Exception{
		ArrayList<String> optionStrings= getFeatureSelectionString();
		String[] optionArray = null;
		double maxPrecision = 0.0;
		ArrayList<String> bestOptions = null;
		int counter = 0;
		for (String optionString : optionStrings) {
			ArrayList<String> options = new ArrayList<String>();
			if (optionString.charAt(0) == '1') {
				if (optionString.charAt(1) == '1') {
					options.add("-C 0.10"); // /0.10, 0.20, 0.30, 0.40, 0.5
				} else {
					options.add("-C 0.20"); // /0.10, 0.20, 0.30, 0.40, 0.5
				}
			} else {
				if (optionString.charAt(1) == '1') {
					options.add("-C 0.30"); // /0.10, 0.20, 0.30, 0.40, 0.5
				} else {
					options.add("-C 0.40"); // /0.10, 0.20, 0.30, 0.40, 0.5
				}
				
			}
			if (optionString.charAt(2) == '1') {
				if (optionString.charAt(3) == '1') {
					options.add("-M 1"); // 1, 2, 3, 4
				} else {
					options.add("-M 2"); // 1, 2, 3, 4
				}
			} else {
				if (optionString.charAt(3) == '1') {
					options.add("-M 3"); // 1, 2, 3, 4
				} else {
					options.add("-M 4"); // 1, 2, 3, 4
				}
			}
			if (optionString.charAt(4) == '1') {
				if (optionString.charAt(5) == '1') {
					options.add("-N 2"); // 1, 2, 3, 4
				} else {
					options.add("-N 3"); // 1, 2, 3, 4
				}
			} else {
				if (optionString.charAt(5) == '1') {
					options.add("-N 4"); // 1, 2, 3, 4
				} else {
					options.add("-N 5"); // 1, 2, 3, 4
				}
			}
			if (optionString.charAt(6) == '1') {
				options.add("-R");
			}
			if (optionString.charAt(7) == '1') {
				options.add("-B");
			}
			if (optionString.charAt(8) == '1') {
				options.add("-S");
			}
			if (optionString.charAt(9) == '1') {
				options.add("-A");
			}
			if (optionString.charAt(10) == '1') {
				options.add("-J");
			}
			if (optionString.charAt(11) == '1') {
				options.add("-doNotMakeSplitPointActualValue");
			}
			optionArray = options.toArray(new String[options.size()]);
//			if (counter % 100 == 0) {
//				System.out.println(counter);
//			}
			counter++;
			try {
				double precision = runJ48WithOptions(data, optionArray) * 100;
				if (precision > maxPrecision) {
					maxPrecision = precision;
					bestOptions = options;
					System.out.println(maxPrecision + " @ " + bestOptions);
				}
				
			} catch (Exception e) {
				System.out.println(e + ". Option string=" + options);
			}
		}
//		setBinarySplits(boolean v)
//		setCollapseTree(boolean v)
//		setDoNotMakeSplitPointActualValue(boolean m_doNotMakeSplitPointActualValue)
//		setReducedErrorPruning(boolean v)
//		setSaveInstanceData(boolean v)
//		setSubtreeRaising(boolean v)
//		setUnpruned(boolean v)
//		setUseLaplace(boolean newuseLaplace)
//		setUseMDLcorrection(boolean newuseMDLcorrection)
//
//		setConfidenceFactor(float v) // 0.05, 0.10, 0.15, 0.20, 0.25, 0.30, 0.35, 0.40, 0.45, 0.5
//		setMinNumObj(int v)  
//		setNumFolds(int v) // 3
//		setSeed(int newSeed) // 
		return maxPrecision;
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

	public static ClassifierAndFilter getClassifier(MLDataSet data, ArrayList<String> options) throws Exception {
		double totalPrecision = 0;
		double averagePrecision = 0;
     	ArrayList<Fold> folds = new ArrayList<Fold>();
     	ClassifierAndFilter bestClassifierAndFilter = new ClassifierAndFilter();
     	double bestPrecision = 0;
     	for(int i = 0; i < 5; i++){
     		data = randomShufftle(data);
     		folds = genereateNFold(data, 5);
     		
     		for(Fold f : folds){
     			Instances trainingSet = InstancesMLDataSetWrapper.convert(f.getTrainingSet());
     			Instances testSet = InstancesMLDataSetWrapper.convert(f.getTestSet());
     	     	
     			Discretize filter = new Discretize();
     		    filter.setInputFormat(trainingSet);
     		    trainingSet = Filter.useFilter(trainingSet, filter);
     		    testSet  = Filter.useFilter(testSet,  filter);
     			
     			Classifier tree = new J48();
     			
     			String optionArray[]=options.toArray(new String[options.size()]);
     			tree.setOptions(optionArray);
     			tree.buildClassifier(trainingSet);

     			// Test the model
     			Evaluation eTest = new Evaluation(testSet);
     			eTest.evaluateModel(tree, testSet);
     			double thisPrecision = eTest.correct() / testSet.numInstances();
     			if (thisPrecision > bestPrecision) {
     				bestPrecision = thisPrecision;
     				bestClassifierAndFilter.classifier = tree;
     				bestClassifierAndFilter.filter = filter;
     				bestClassifierAndFilter.evaluation = eTest;
     			}
     			// Print the result à la Weka explorer:
//     			String strSummary = eTest.toSummaryString();
//     			System.out.println(strSummary);
     			
//     			nb.trainNBclassificator(f.getTrainingSet());
//     			double thisError = nb.calculateError(f.getTestSet());
     			totalPrecision += thisPrecision;
     		}
     	}
     	averagePrecision = totalPrecision/ ( folds.size() * 5);
     	System.out.println("Average precision = " + averagePrecision + ", best precision=" + bestPrecision);
     	System.out.println(bestClassifierAndFilter.evaluation.toSummaryString());
     	printConfusionMatrix(bestClassifierAndFilter.evaluation.confusionMatrix());
     	return bestClassifierAndFilter;
	}
	
	private static void printMatrix(double[] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			System.out.print(matrix[i] + " ");
		}
		System.out.print("\n");
	}

	private static void printConfusionMatrix(double[][] confusionMatrix) {
		for (int r = 0; r < confusionMatrix.length; r++) {
			for (int c = 0; c < confusionMatrix[r].length; c++) {
				System.out.print(confusionMatrix[r][c] + " ");
			}
			System.out.print("\n");
		}
	}
}