package machineLearning;

import org.encog.ml.data.MLDataSet;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class InstancesMLDataSetWrapper {
	
	public static Instances convert(MLDataSet mldataset) {
		int numberOfAttributes = mldataset.getInputSize();
		FastVector attributes = new FastVector(numberOfAttributes + 1);
		for (int i = 0; i < numberOfAttributes; i++) {
			attributes.addElement(new Attribute("" + i));
		}
		FastVector fvClassVal = new FastVector(3);
		fvClassVal.addElement("-1");
		fvClassVal.addElement("0");
		fvClassVal.addElement("1");
		Attribute ClassAttribute = new Attribute("class", fvClassVal);
		attributes.addElement(ClassAttribute);
		int numberOfInstances = (int) mldataset.getRecordCount();
        Instances instances = new Instances("Rel", attributes, numberOfInstances);
        instances.setClassIndex(numberOfAttributes); // set last attribute to the one being classified
        
		for (int i = 0; i < numberOfInstances; i++) {
			Instance instance = new Instance(numberOfAttributes+1);
			double[] inputData = mldataset.get(i).getInput().getData();
			for (int j = 0; j < inputData.length; j++) {
				instance.setValue((Attribute) attributes.elementAt(j), inputData[j]);
			}
			double[] idealData = mldataset.get(i).getIdeal().getData();
			instance.setValue((Attribute) attributes.elementAt(numberOfAttributes), "" + (int) idealData[0]);
			instances.add(instance);
		}
		return instances;
	}
	
	public static String getDoubleArrayString(double[] d) {
		String str = "[";
		for (int i = 0; i < d.length; i++) {
			str += d[i] + ", ";
		}
		return str.substring(0, str.length()-2) + "]";
	}
}