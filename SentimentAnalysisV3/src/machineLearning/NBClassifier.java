package machineLearning;

import java.util.ArrayList;
import java.util.Collections;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;
import weka.core.*;
import weka.filters.Filter;
import weka.filters.supervised.attribute.Discretize;



public class NBClassifier {
	
	 public static double runNBC(MLDataSet data) throws Exception{
		double totalPrecision = 0;
	    double averagePrecision = 0;
     	ArrayList<Fold> folds = new ArrayList<Fold>();
     	
     	for(int i = 0; i < 5; i++){
     		data = randomShufftle(data);
     		folds = genereateNFold(data, 5);
     		
     		for(Fold f : folds){
     			Instances trainingSet = InstancesMLDataSetWrapper.convert(f.getTrainingSet());
     			Instances testSet = InstancesMLDataSetWrapper.convert(f.getTestSet());
     	     	
     			// Discretizing
     			Discretize filter = new Discretize();
     		    filter.setInputFormat(trainingSet);
     		    trainingSet = Filter.useFilter(trainingSet, filter);
     		    testSet  = Filter.useFilter(testSet,  filter);
     			
     			Classifier cModel = (Classifier) new NaiveBayes();
//     			J48 tree = new J48();
     			cModel.buildClassifier(trainingSet);
     			
     			// Test the model
     			Evaluation eTest = new Evaluation(testSet);
     			eTest.evaluateModel(cModel, testSet);
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
}
