package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import weka.filters.unsupervised.attribute.Discretize;
import featureExctraction.FeaturesVektor;

public class Binning {
	
	Discretize discretize;
	
	
	// Only considers featureVector.get(0)
	public static ArrayList<FeaturesVektor> computeChiStatisticBinning(ArrayList<FeaturesVektor> list) {
		// Find best binning
		double maximumCHI = 0.0;
		ArrayList<FeaturesVektor> bestList = new ArrayList<FeaturesVektor>();
		for (int i = 1; i <= 12; i++) {
			ArrayList<FeaturesVektor> tempList = createBinsOfEqualFrequencyAscendingFeatures(list, i);
			double candidiateMaximumCHI = computeChiStatistic(tempList, i);
			if (candidiateMaximumCHI > maximumCHI) {
				maximumCHI = candidiateMaximumCHI;
				bestList = tempList;
			}
		}
		return bestList;		
	}
	
	// Only considers featureVector.get(0)
	public static ArrayList<FeaturesVektor> computeMutualInformationBinning(ArrayList<FeaturesVektor> list) {
		// Find best binning
		double maximumMI = 0.0;
		ArrayList<FeaturesVektor> bestList = new ArrayList<FeaturesVektor>();
		for (int i = 1; i <= 12; i++) {
			ArrayList<FeaturesVektor> tempList = createBinsOfEqualFrequencyAscendingFeatures(list, i);
			double candidiateMaximumMI = computeMutualInformationBinnedFeature(tempList, i);
			if (candidiateMaximumMI > maximumMI) {
				maximumMI = candidiateMaximumMI;
				bestList = tempList;
			}
		}
		return bestList;		
	}
	
	
	
	
	
	
	
	
	public static double computeChiStatisticDiscreteOrContinuousFeature(ArrayList<FeaturesVektor> list) {
		// Find best binning
		double maximumCHI = 0.0;
		for (int i = 1; i <= 12; i++) {
			ArrayList<FeaturesVektor> tempList = createBinsOfEqualFrequencyAscendingFeatures(list, i);
			double candidiateMaximumCHI = computeChiStatistic(tempList, i);
			if (candidiateMaximumCHI > maximumCHI) {
				maximumCHI = candidiateMaximumCHI;
			}
		}
		return maximumCHI;		
	}
	
	public static double computeMutualInformationDiscreteOrContinuousFeature(ArrayList<FeaturesVektor> list) {
		// Find best binning
		double maximumMI = 0.0;
		for (int i = 1; i <= 12; i++) {
			ArrayList<FeaturesVektor> tempList = createBinsOfEqualFrequencyAscendingFeatures(list, i);
			double candidiateMaximumMI = computeMutualInformationBinnedFeature(tempList, i);
			if (candidiateMaximumMI > maximumMI) {
				maximumMI = candidiateMaximumMI;
			}
		}
		return maximumMI;		
	}
	
	public static double computeChiStatistic(ArrayList<FeaturesVektor> list, int numberOfBins) {
		double chistat = 0.0;
		int bigN = list.size();
		int[][] combinationMatrix = new int[numberOfBins][3];
		for (FeaturesVektor fv : list) {
			combinationMatrix[fv.getFeatureVector().get(0).intValue()][(int) fv.getSentimentVector().get(0).intValue()+1]++;
		}
		for (int r = 0; r < combinationMatrix.length; r++) {
			for (int c = 0; c < combinationMatrix[r].length; c++) {
				double eRC = bigN * ((getColSum(combinationMatrix, c)) / ((double) bigN )) * ((getRowSum(combinationMatrix, r)) / ((double) bigN ));
				double nRC = combinationMatrix[r][c];
				chistat += ( (nRC - eRC) * (nRC - eRC) ) / eRC; 
			}
		}
		return chistat;
	}
	
	public static double computeMutualInformationBinnedFeature(ArrayList<FeaturesVektor> list, int numberOfBins) {
		double mi = 0.0;
		int featureLength = list.size();
		int[][] combinationMatrix = new int[numberOfBins][3];
		for (FeaturesVektor fv : list) {
			combinationMatrix[fv.getFeatureVector().get(0).intValue()][(int) fv.getSentimentVector().get(0).intValue()+1]++;
		}
		for (int r = 0; r < combinationMatrix.length; r++) {
			for (int c = 0; c < combinationMatrix[r].length; c++) {
				mi += (combinationMatrix[r][c] / ((double ) featureLength)) * logBase2( (combinationMatrix[r][c] * featureLength) / ((double) ( getRowSum(combinationMatrix, r) *  getColSum(combinationMatrix, c))) ); 
			}

		}
		return mi;
	}
	
	public static double computeMutualInformationBinaryFeature(ArrayList<FeaturesVektor> list) {
		double mi = 0.0;
		int featureLength = list.size();
		int[][] combinationMatrix = new int[2][3];
		for (FeaturesVektor fv : list) {
			combinationMatrix[fv.getFeatureVector().get(0).intValue()][(int) fv.getSentimentVector().get(0).intValue()+1]++;
		}
		for (int r = 0; r < combinationMatrix.length; r++) {
			for (int c = 0; c < combinationMatrix[r].length; c++) {
				mi += (combinationMatrix[r][c] / ((double ) featureLength)) * logBase2( (combinationMatrix[r][c] * featureLength) / ((double) ( getRowSum(combinationMatrix, r) *  getColSum(combinationMatrix, c))) ); 
			}

		}
		return mi;
	}
	
	public static double logBase2(double input) {
		return Math.log(input) / Math.log(2);
	}
	
	public static int getRowSum(int[][] matrix, int row) {
		int count = 0;
		for (int col = 0; col < matrix[row].length; col++) {
			count += matrix[row][col];
		}
		return count;
	}

	public static int getColSum(int[][] matrix, int col) {
		int count = 0;
		for (int row = 0; row < matrix.length; row++) {
			count += matrix[row][col];
		}
		return count;
	}
	
	public static int getUniqueInputValueCount(ArrayList<FeaturesVektor> list) {
		int count = 0;
		ArrayList<Double> seenValues = new ArrayList<Double>();
		for (FeaturesVektor fv : list) {
			Double d = fv.getFeatureVector().get(0);
			if (! seenValues.contains(d)) {
				count++;
				seenValues.add(d);
			}
		}
		return count;
	}
	
	public static ArrayList<ArrayList<FeaturesVektor>> setBinsToMedian(ArrayList<ArrayList<FeaturesVektor>> list) {
		for (ArrayList<FeaturesVektor> innerList : list) {
			double d;
			if (innerList.size() % 2 == 0) {
				// Even numbered list
				d = 0.5 * (innerList.get(innerList.size() / 2).getFeatureVector().get(0) + innerList.get(innerList.size() / 2 - 1).getFeatureVector().get(0));
			} else {
				// Odd numbered list
				d = innerList.get((int) Math.floor(innerList.size() / 2.0) ).getFeatureVector().get(0);
			}
			innerList = setAllFeatureVectorsTo(innerList, d);
		}
		return list;
	}
	
	public static ArrayList<ArrayList<FeaturesVektor>> setBinsToAscendingIntegers(ArrayList<ArrayList<FeaturesVektor>> list) {
		double d = 0.0;
		for (ArrayList<FeaturesVektor> innerList : list) {
			innerList = setAllFeatureVectorsTo(innerList, d);
			d++;
		}
		return list;
	}
	
	private static ArrayList<FeaturesVektor> setAllFeatureVectorsTo(ArrayList<FeaturesVektor> list, double d) {
		for (FeaturesVektor fv : list) {
			ArrayList<Double> tempList = new ArrayList<Double>();
			tempList.add(d);
			fv.setFeatureVector(tempList);
		}
		return list;
	}
	
	public static ArrayList<FeaturesVektor> createBinsOfEqualFrequencyAscendingFeatures(ArrayList<FeaturesVektor> list, int numberOfBins) {
		ArrayList<ArrayList<FeaturesVektor>> listInList = createBinsOfEqualFrequency(list, numberOfBins);
		listInList = setBinsToAscendingIntegers(listInList);
		ArrayList<FeaturesVektor> returnList = new ArrayList<FeaturesVektor>();
		for (ArrayList<FeaturesVektor> innerList : listInList) {
			returnList.addAll(innerList);
		}
		return returnList;
	}
	
	public static ArrayList<ArrayList<FeaturesVektor>> createBinsOfEqualFrequency(ArrayList<FeaturesVektor> list, int numberOfBins) {
		Collections.sort(list);
		ArrayList<ArrayList<FeaturesVektor>> binnedList = new ArrayList<ArrayList<FeaturesVektor>>();
		int startIndex = 0;
		ArrayList<FeaturesVektor> tempList = new ArrayList<FeaturesVektor>();
		for (int i = 1; i <= numberOfBins; i++) {
			int stopIndex = (int) ( (i* list.size()) / ((double) numberOfBins));
			binnedList.add(new ArrayList<FeaturesVektor>(list.subList(startIndex, stopIndex)));
//			System.out.println("Start: " + startIndex + ", stop: " + stopIndex + ", length of list: " + list.subList(startIndex, stopIndex).size());
			startIndex = stopIndex;
		}
		return binnedList;
	}
	
	public static void main(String[] args) {
		FeaturesVektor fv1 = new FeaturesVektor(new ArrayList<Double>(Arrays.asList(0.1, 0.2, 0.3, 0.4, 0.5, 0.6)), new ArrayList<Double>(Arrays.asList(0.0)));
		FeaturesVektor fv2 = new FeaturesVektor(new ArrayList<Double>(Arrays.asList(0.1, 0.2, 0.3, 0.4, 0.5, 0.6)), new ArrayList<Double>(Arrays.asList(0.0)));
		FeaturesVektor fv3 = new FeaturesVektor(new ArrayList<Double>(Arrays.asList(1.1, 1.2, 1.3, 1.4, 1.5, 1.6)), new ArrayList<Double>(Arrays.asList(1.0)));
		FeaturesVektor fv4 = new FeaturesVektor(new ArrayList<Double>(Arrays.asList(1.1, 1.2, 1.3, 1.4, 1.5, 1.6)), new ArrayList<Double>(Arrays.asList(1.0)));
		FeaturesVektor fv5 = new FeaturesVektor(new ArrayList<Double>(Arrays.asList(1.1, 1.2, 1.3, 1.4, 1.5, 1.6)), new ArrayList<Double>(Arrays.asList(1.0)));
		ArrayList<FeaturesVektor> fvList = new ArrayList<FeaturesVektor>(Arrays.asList(fv1, fv2, fv3, fv4, fv5));
		System.out.println(fvList);
		
	}
	
}
