package machineLearning;

import java.io.IOException;
import java.util.ArrayList;

import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataSet;

import divClasses.InstanceTriplet;

public class NBclassificator {

	NBCBuilder nbc = new NBCBuilder();
	
	public NBclassificator() {
		
	}
	
	public boolean trainNBclassificator(MLDataSet trainingSet) {
		DataSet trainingDataSet = mlDataSetToDataSet(trainingSet);
		nbc.train(trainingDataSet);
		return true;
	}
	
	public double calculateError(MLDataSet testSet) {
		DataSet testDataSet = mlDataSetToDataSet(testSet);
		return nbc.testSet(testDataSet);
	}
	
	public static DataSet mlDataSetToDataSet(MLDataSet mldataset) {
		ArrayList<InstanceTriplet> set = new ArrayList<InstanceTriplet>();
		for (MLDataPair pair : mldataset) {
			ArrayList<Double> inst = new ArrayList<Double>();
			for (Double d : pair.getInputArray() ) {
				inst.add(d);
			}
			inst.add(pair.getIdealArray()[0]);
			set.add(new InstanceTriplet(inst));
		}
		return new DataSet(set, true);
	}
	
	public static MLDataSet createMLDataSetFromMatrix(double[][] input, double[] output) {
		MLDataSet dataset = new BasicMLDataSet();
		MLData inputdata;
		MLData outputdata;
		for (int c = 0; c < input[0].length; c++) {
			double[] inputcolumn = new double[input.length];
			for (int r = 0; r < input.length; r++) {
				inputcolumn[r] = input[r][c];
			}
			inputdata = new BasicMLData(inputcolumn);
			outputdata = new BasicMLData(new double[]{output[c]});
			dataset.add(inputdata, outputdata);
		}
		return dataset;
	}
	
	public static void main(String[] args) throws IOException {
//		AnnotationValidator a1 = new AnnotationValidator();
//		AnnotationValidator a2 = new AnnotationValidator();
//		
//		a1.initalizeAnnotationObject("/Users/paulus/Dropbox/Data5/Prosjektoppgave/lars_annotated_1000_1426_19_13.json");
//		a2.initalizeAnnotationObject("/Users/paulus/Dropbox/Data5/Prosjektoppgave/pal_annotated_1000_1501_19_13.json");
//		
////		double[][] matrix = PredictingAggregateClassification.getMatrixRepresentationOfFullyAgreedArticles(a1, a2);
//		double[][] matrixWithInput = PredictingAggregateClassification.returnThreeColumns(matrix, 0, 1, 2);
//		double[] matrixWithOutput = PredictingAggregateClassification.returnOneColumn(matrix, 3);
//		
//		System.out.println("Input lenght: " + matrixWithInput.length);
//		System.out.println("Input[0] length: " + matrixWithInput[0].length);
//		System.out.println("Output length: " + matrixWithOutput.length);
		
//		PredictingAggregateClassification.printMatrix(matrixWithInput);
//		System.out.println(" ");
//		PredictingAggregateClassification.printMatrix(matrixWithOutput);
		
		int cutoff = 300;
		NBclassificator nbc = new NBclassificator();
//		MLDataSet trainingData = createMLDataSetFromMatrix(MatrixOperations.getFirstNEntries(matrixWithInput, cutoff), MatrixOperations.getFirstNEntries(matrixWithOutput, cutoff));
//		MLDataSet testData = createMLDataSetFromMatrix(MatrixOperations.getLastNEntries(matrixWithInput, matrixWithInput[0].length-cutoff), MatrixOperations.getLastNEntries(matrixWithOutput, matrixWithOutput.length-cutoff));

//		nbc.trainNBclassificator(trainingData);
//		double error = nbc.calculateError(testData);
//		System.out.println(error);
	}
}