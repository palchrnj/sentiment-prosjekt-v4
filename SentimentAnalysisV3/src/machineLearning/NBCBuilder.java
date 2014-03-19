package machineLearning;

import java.util.ArrayList;
import java.util.HashMap;

import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataSet;

import divClasses.InstanceTriplet;

public class NBCBuilder {

	private DataSet trainingSet;
	private HashMap<Integer, Double> aPrioriClassProb;
	private HashMap<AttributeValueClassTriplet, Double> conditionalAttrProb;
 	
	public NBCBuilder(){
	}
	
	public void train(DataSet data){
		this.trainingSet = data;
		
		this.aPrioriClassProb = new HashMap<Integer, Double>();
		this.conditionalAttrProb = new HashMap<AttributeValueClassTriplet, Double>();

		for (int i = 0; i < this.trainingSet.getClasses().length; i++){
			int classification = this.trainingSet.getClasses()[i];
			int counter = 0;
			double nevner = 0.0;
			ArrayList<InstanceTriplet> temp = new ArrayList<InstanceTriplet>();
			
			for(InstanceTriplet it : this.trainingSet.getInstances()){
				if(it.instance.get(it.instance.size()-1) == classification){
					counter++;
					temp.add(it);
					nevner += it.weight;

				}
			}
			
			this.aPrioriClassProb.put(classification, (double)counter/this.trainingSet.getInstances().size());
			
			for(int j = 0; j < this.trainingSet.getAttrNumberOfValues().length; j++){
				int attr = j;
				for(int k = 0; k < this.trainingSet.getAttrNumberOfValues()[attr].length ; k++){
					int val = this.trainingSet.getAttrNumberOfValues()[attr][k];
					
					AttributeValueClassTriplet attrValClass = new AttributeValueClassTriplet(attr, (double)val, classification);

					double teller = 0.0;
					for(InstanceTriplet it : temp){
						if(it.instance.get(j) == attrValClass.value){
							teller += it.weight;
							
						}
					}
					this.conditionalAttrProb.put(attrValClass, (double)teller/nevner);
				}
			}
		}
	}
	
	public double testSet(DataSet set){
		int error = 0;
		for(InstanceTriplet it : set.getInstances()){
			double max = 0.0;
			for(int i = 0; i < set.getClasses().length; i++){
				double hmap = 1.0;
				for(int attr = 0; attr < set.getAttrNumberOfValues().length; attr++){
					try{
//						
						hmap = hmap*((double) conditionalAttrProb.get(new AttributeValueClassTriplet(attr, it.instance.get(attr), set.getClasses()[i])));
					} catch (NullPointerException e) {
//						System.err.println(conditionalAttrProb.get(new AttributeValueClassTriplet(attr, it.instance.get(attr), set.getClasses()[i])));
//						System.err.println(hmap);
					}
				}
				
				hmap = hmap*this.aPrioriClassProb.get(set.getClasses()[i]);
				if(max < hmap){
					it.setClassification(set.getClasses()[i]);
					max = hmap;
				}
			}
			if(it.getClassification() != it.instance.get(it.instance.size()-1).intValue()){
				error++;
			}
		}
		
		return (double)error/(double)set.getInstances().size();
	}
	
	public HashMap<Integer, Double> getaPrioriClassProb() {
		return aPrioriClassProb;
	}

	public void setaPrioriClassProb(HashMap<Integer, Double> aPrioriClassProb) {
		this.aPrioriClassProb = aPrioriClassProb;
	}

	public HashMap<AttributeValueClassTriplet, Double> getaPrioriAttrProb() {
		return conditionalAttrProb;
	}

	public void setaPrioriAttrProb(HashMap<AttributeValueClassTriplet, Double> aPrioriAttrProb) {
		this.conditionalAttrProb = aPrioriAttrProb;
	}
	
	public DataSet getTrainingSet() {
		return trainingSet;
	}
	public void setTrainingSet(DataSet dataSet) {
		this.trainingSet = dataSet;
	}

	public class AttributeValueClassTriplet{
		
		public int attribute;
		public double value;
		public int classification;
		
		public AttributeValueClassTriplet(int a, double v, int c){
			this.attribute = a;
			this.value = v;
			this.classification = c;
		}
		
		@Override
		public int hashCode(){
			return (this.attribute * (int)value) ^ classification;
		}
		
		@Override
		public boolean equals(Object obj){
			if(obj instanceof AttributeValueClassTriplet) {
				AttributeValueClassTriplet AttrValClass = (AttributeValueClassTriplet) obj;
				return (this.attribute == AttrValClass.attribute && this.value == AttrValClass.value && this.classification == AttrValClass.classification);
			}
			else
				return false;
		}

	}
	
public static DataSet mlDataSetToDataSet(MLDataSet mldataset) {
		
		DataSet dataset;
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
	
	public static void main(String[] args) {
		DataSet adataset;
		if (true) {
			double[] d1 = new double[]{1,0,1,0,1,1,0,1};
			double[] d2 = new double[]{1,0,1,0,1,1,0,1};
			double[] d3 = new double[]{1,0,1,0,1,1,0,1};
			double[] d4 = new double[]{1,0,1,0,1,1,0,1};
			double[] d5 = new double[]{1,0,1,0,1,1,0,1};
			double[] d6 = new double[]{1,0,1,1,1,1,0,0};
			double[] d7 = new double[]{1,0,1,0,1,1,0,1};
			double[] d8 = new double[]{1,0,0,0,1,1,0,1};
			double[] d9 = new double[]{1,0,1,0,1,1,0,0};
			double[] d10 = new double[]{1,0,1,0,1,1,0,1};
			double[][] d = new double[][]{d1, d2, d3, d4, d5, d6, d7, d8, d9, d10};
			MLDataSet dataset = new BasicMLDataSet();
			for (int i = 0; i < d.length; i++) {
				MLData data = new BasicMLData(d[i]);
				if (i == 0) {
					dataset.add(data, new BasicMLData(new double[]{0.0}));
					
				} else {
					
					dataset.add(data, new BasicMLData(new double[]{1.0}));
				}
			}
			adataset = mlDataSetToDataSet(dataset);
		}
		NBCBuilder nb = new NBCBuilder();
		nb.train(adataset);
		
		DataSet bdataset;
		if (true) {
			double[] d1 = new double[]{1,0,1,0,1,1,0,1};
			double[] d2 = new double[]{1,0,1,0,1,1,0,1};
			double[] d3 = new double[]{1,0,1,0,1,1,0,1};
			double[] d4 = new double[]{1,0,1,0,1,1,0,1};
			double[] d5 = new double[]{1,0,1,0,1,1,0,1};
			double[] d6 = new double[]{1,0,1,1,1,1,0,0};
			double[] d7 = new double[]{1,0,1,0,1,1,0,1};
			double[] d8 = new double[]{1,0,0,0,1,1,0,1};
			double[] d9 = new double[]{1,0,1,0,1,1,0,0};
			double[] d10 = new double[]{1,0,1,0,1,1,0,1};
			double[][] d = new double[][]{d1, d2, d3, d4, d5, d6, d7, d8, d9, d10};
			MLDataSet dataset = new BasicMLDataSet();
			for (int i = 0; i < d.length; i++) {
				MLData data = new BasicMLData(d[i]);
				if (i == 0) {
					dataset.add(data, new BasicMLData(new double[]{1.0}));
					
				} else {
					
					dataset.add(data, new BasicMLData(new double[]{0.0}));
				}			}
			bdataset = mlDataSetToDataSet(dataset);
		}
		
		
		
		System.out.println(nb.testSet(bdataset));
		
		
	}
}