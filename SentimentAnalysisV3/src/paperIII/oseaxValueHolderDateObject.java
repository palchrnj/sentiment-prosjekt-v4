package paperIII;

public class oseaxValueHolderDateObject {
	
	double close;
	double intradayReturn;
	double stddevReturnLastThirtyDays;
	double changeInStddevLastThirtyDays;
	boolean recession;
	boolean bull;
	
	public oseaxValueHolderDateObject(double c, double ir, double srltd, double cisltd, boolean r, boolean b){
		this.close = c;
		this.intradayReturn = ir;
		this.stddevReturnLastThirtyDays = srltd;
		this.changeInStddevLastThirtyDays = cisltd;
		this.recession = r;
		this.bull = b;
	}

	public double getClose() {
		return close;
	}

	public void setClose(double close) {
		this.close = close;
	}

	public double getIntradayReturn() {
		return intradayReturn;
	}

	public void setIntradayReturn(double intradayReturn) {
		this.intradayReturn = intradayReturn;
	}

	public double getStddevReturnLastThirtyDays() {
		return stddevReturnLastThirtyDays;
	}

	public void setStddevReturnLastThirtyDays(double stddevReturnLastThirtyDays) {
		this.stddevReturnLastThirtyDays = stddevReturnLastThirtyDays;
	}

	public double getChangeInStddevLastThirtyDays() {
		return changeInStddevLastThirtyDays;
	}

	public void setChangeInStddevLastThirtyDays(double changeInStddevLastThirtyDays) {
		this.changeInStddevLastThirtyDays = changeInStddevLastThirtyDays;
	}

	public boolean isRecession() {
		return recession;
	}

	public void setRecession(boolean recession) {
		this.recession = recession;
	}

	public boolean isBull() {
		return bull;
	}

	public void setBull(boolean bull) {
		this.bull = bull;
	}
	
	
	

}
