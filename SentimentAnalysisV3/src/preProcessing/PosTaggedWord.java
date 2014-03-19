package preProcessing;

public class PosTaggedWord {
	
	
	public String wordclass;
	public String stem;
	public String[] attr;
	public String input;
	
	
	public PosTaggedWord(){
		
	}


	public String getInput() {
		return input;
	}


	public void setInput(String input) {
		this.input = input;
	}


	public String getWordclass() {
		return wordclass;
	}


	public void setWordclass(String wordclass) {
		this.wordclass = wordclass;
	}


	public String getStem() {
		return stem;
	}


	public void setStem(String stem) {
		this.stem = stem;
	}


	public String[] getAttr() {
		return attr;
	}


	public void setAttr(String[] attr) {
		this.attr = attr;
	}
	

}
