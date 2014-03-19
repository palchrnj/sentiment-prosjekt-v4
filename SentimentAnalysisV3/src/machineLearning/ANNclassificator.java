package machineLearning;

import java.io.IOException;

import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.back.Backpropagation;

import annotationStatistics.AnnotationValidator;

public class ANNclassificator {

	BasicNetwork network;
	Backpropagation train;
	double learningRate;
	double momentum;
	double error = 0.001;
	
	public ANNclassificator(int numberOfInputFeatures) {
		this(numberOfInputFeatures, 0.01, 0.2);
	}
	
	// learningRate = 0.0001 and momentum = 0.5 worked well in AI assignments
	public ANNclassificator(int numberOfInputFeatures, double learningRate, double momentum) {
		this.learningRate = learningRate;
		this.momentum = momentum;
		network = new BasicNetwork();
		// Input layer 
		network.addLayer(new BasicLayer(null,true, numberOfInputFeatures));
		// Hidden layer
		network.addLayer(new BasicLayer(new ActivationSigmoid(),true, 2*numberOfInputFeatures));
		// Second hidden layer
		network.addLayer(new BasicLayer(new ActivationSigmoid(),true, 2*numberOfInputFeatures));
		// Output layer 
		network.addLayer(new BasicLayer(new ActivationSigmoid(),true, 1));
		// Dunno why this is done...
		network.getStructure().finalizeStructure();				
		network.reset();
	}
	
	public boolean trainANNclassificator(MLDataSet trainingSet) {
		train = new Backpropagation(network, trainingSet, learningRate, momentum);
		train.fixFlatSpot(true);
		int epoch = 1;
		do {
			train.iteration();
			if (epoch % 100 == 0) {
//				System.out
//				.println("Epoch #" + epoch + " Error:" + train.getError());    				
			}
			epoch++;
		} while(train.getError() > error && epoch < 500);
		
		// test the neural network
//		System.out.println("Neural Network Results:");
//		for (MLDataPair pair: trainingSet ) {
//			final MLData output = network.compute(pair.getInput());
//			System.out.println(pair.getInput().getData(0) + "," + pair.getInput().getData(1) + ", " + pair.getInput().getData(2)
//					+ ", actual=" + output.getData(0) + ",ideal=" + pair.getIdeal().getData(0));
//		}
		return true;
	}
	
	public double calculateError(MLDataSet testSet) {
		return network.calculateError(testSet);
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
		AnnotationValidator a1 = new AnnotationValidator();
		AnnotationValidator a2 = new AnnotationValidator();
		
//		a1.initalizeAnnotationObject("/Users/paulus/Dropbox/Data5/Prosjektoppgave/lars_annotated_1000_1426_19_13.json");
//		a2.initalizeAnnotationObject("/Users/paulus/Dropbox/Data5/Prosjektoppgave/pal_annotated_1000_1501_19_13.json");
		
//		double[][] matrix = PredictingAggregateClassification.getMatrixRepresentationOfFullyAgreedArticles(a1, a2);
//		double[][] matrixWithInput = PredictingAggregateClassification.returnThreeColumns(matrix, 0, 1, 2);
//		double[] matrixWithOutput = PredictingAggregateClassification.returnOneColumn(matrix, 3);
//		
//		System.out.println("Input lenght: " + matrixWithInput.length);
//		System.out.println("Input[0] length: " + matrixWithInput[0].length);
//		System.out.println("Output length: " + matrixWithOutput.length);
		
//		PredictingAggregateClassification.printMatrix(matrixWithInput);
//		System.out.println(" ");
//		PredictingAggregateClassification.printMatrix(matrixWithOutput);
		
		ANNclassificator annc = new ANNclassificator(3);
		int cutoff = 400;
		
//		MLDataSet trainingData = createMLDataSetFromMatrix(MatrixOperations.getFirstNEntries(matrixWithInput, cutoff), MatrixOperations.getFirstNEntries(matrixWithOutput, cutoff));
//		MLDataSet testData = createMLDataSetFromMatrix(MatrixOperations.getLastNEntries(matrixWithInput, matrixWithInput[0].length-cutoff), MatrixOperations.getLastNEntries(matrixWithOutput, matrixWithOutput.length-cutoff));
//
//		annc.trainANNclassificator(trainingData);
//		double error = annc.calculateError(testData);
//		System.out.println(error);
	}
}