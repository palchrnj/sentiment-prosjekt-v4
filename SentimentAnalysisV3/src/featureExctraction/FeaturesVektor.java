package featureExctraction;

import java.util.ArrayList;

public class FeaturesVektor implements Comparable {
	

	public ArrayList<Double> featureVector;
	public ArrayList<Double> sentimentVector;
	
	
	public FeaturesVektor(){
		this.featureVector = new ArrayList<Double>();
		this.sentimentVector = new ArrayList<Double>();
	}

	public FeaturesVektor(ArrayList<Double> featureVector, ArrayList<Double> sentimentVector){
		this.featureVector = featureVector;
		this.sentimentVector = sentimentVector;
	}


	public ArrayList<Double> getFeatureVector() {
		return featureVector;
	}

	/* RETURNS THE FEATURE VECTOR OF AN ARTICLE AS ARRAY */
	public double[] getFeatureVectorAsArray() {
		double[] array = new double[this.featureVector.size()];
		int i = 0;
		for (Double d : this.featureVector) {
			array[i] = d;
			i++;
		}
		return array;
		
	}
	
	/* RETURNS SENTIMENT VECTOR AS ARRAY */
	public double[] getSentimentAsArray() {
		return new double[]{this.sentimentVector.get(0)};
		
	}

	
	public void setFeatureVector(ArrayList<Double> featureVector) {
		this.featureVector = featureVector;
	}
	public ArrayList<Double> getSentimentVector() {
		return sentimentVector;
	}
	public void setSentimentVector(ArrayList<Double> sentimentVector) {
		this.sentimentVector = sentimentVector;
	}
	
//	public boolean isContinious() {
//		
//	}
	
	public String toString() {
		return featureVector.toString() + " | " + sentimentVector.toString();
	}

	@Override
	public int compareTo(Object other) {
		if (this.getFeatureVector().get(0) > ((FeaturesVektor) other).getFeatureVector().get(0)) {
			return 1;
		} else if (this.getFeatureVector().get(0) < ((FeaturesVektor) other).getFeatureVector().get(0)) {
			return -1;
		} else {
			return 0;
		}
	}
}
