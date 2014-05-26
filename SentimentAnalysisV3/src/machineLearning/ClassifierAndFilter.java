package machineLearning;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.supervised.attribute.Discretize;

public class ClassifierAndFilter {
	
	public Classifier classifier;
	public Filter filter;
	public Evaluation evaluation;
	
}
