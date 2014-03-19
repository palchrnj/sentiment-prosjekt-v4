package machineLearning;

import java.util.ArrayList;
import java.util.Collections;

import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.ml.svm.KernelType;
import org.encog.ml.svm.SVM;
import org.encog.ml.svm.SVMType;
import org.encog.ml.svm.training.SVMTrain;

import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.supervised.attribute.Discretize;

public class SVMClassifier {
	
	 public static double runSVM(MLDataSet data) throws Exception{
			double totalError = 0;
			double averagePrecision = 0;
	     	ArrayList<Fold> folds = new ArrayList<Fold>();
	     	
	     	for(int i = 0; i < 5; i++){
	     		data = randomShufftle(data);
	     		folds = genereateNFold(data, 5);
	     		
	     		for(Fold f : folds){
//	     			Instances trainingSet = InstancesMLDataSetWrapper.convert(f.getTrainingSet());
//	     			Instances testSet = InstancesMLDataSetWrapper.convert(f.getTestSet());
	     	     	
	     			MLDataSet trainingSet = f.getTrainingSet();
	     			MLDataSet testSet = f.getTestSet();
	     			
	     			SVM svm = new SVM(trainingSet.size(),SVMType.EpsilonSupportVectorRegression,KernelType.RadialBasisFunction);
    				SVMTrain trainer = new SVMTrain(svm, trainingSet);
    				trainer.iteration();
    				double thisError = svm.calculateError(testSet);
    				System.out.println(thisError);
    				totalError += thisError;
	     		}
	     	}
	     	averagePrecision = 1 - totalError/ ( folds.size() * 5);
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
