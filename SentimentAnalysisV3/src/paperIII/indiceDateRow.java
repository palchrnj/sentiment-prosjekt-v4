package paperIII;

public class indiceDateRow {
	
	public double close;
	public double volume;
	public double value;
	
	
	public indiceDateRow(double close, double volume, double value){
		this.close = close;
		this.volume = volume;
		this.value = value;
	}


	public double getClose() {
		return close;
	}


	public void setClose(double close) {
		this.close = close;
	}


	public double getVolume() {
		return volume;
	}


	public void setVolume(double volume) {
		this.volume = volume;
	}


	public double getValue() {
		return value;
	}


	public void setValue(double value) {
		this.value = value;
	}
	
	
	
	

}



