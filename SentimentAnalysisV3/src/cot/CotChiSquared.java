package cot;

public class CotChiSquared implements Comparable{
	
	public String cot;
	public double chiSquaredValue;
	
	public CotChiSquared(){
		
	}

	public String getCot() {
		return cot;
	}

	public void setCot(String cot) {
		this.cot = cot;
	}

	public double getChiSquaredValue() {
		return chiSquaredValue;
	}

	public void setChiSquaredValue(double chiSquaredValue) {
		this.chiSquaredValue = chiSquaredValue;
	}
	
	public int compareTo(Object o) {
        if (this.chiSquaredValue == ((CotChiSquared)o).chiSquaredValue)
            return 0;
        else if ((this.chiSquaredValue) > ((CotChiSquared) o).chiSquaredValue)
            return 1;
        else
            return -1;
    }
}