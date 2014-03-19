package machineLearning;

import org.encog.ml.data.MLDataSet;

public class Fold {
	MLDataSet trainingSet;
	MLDataSet testSet;
	
	
	public Fold(MLDataSet trainingSet, MLDataSet testSet) {
		this.trainingSet = trainingSet;
		this.testSet = testSet;
	}
	
	public MLDataSet getTrainingSet() {
		return this.trainingSet;
	}
	
	public MLDataSet getTestSet() {
		return this.testSet;
	}
}