package divClasses;

import java.io.InputStream;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;

public class JenaWordnetHandler {
	
	
	
	
	
	public String inputFileName = "C:/Users/Lars/Documents/GitHub/SentimentProject/SentimentAnalysis/ordnett_nob_1.1.0/rdf/words.rdf";
	
	public JenaWordnetHandler(){
		
	}
	
	
	
	public String getInputFileName() {
		return inputFileName;
	}
	public void setInputFileName(String inputFileName) {
		this.inputFileName = inputFileName;
	}
	
	public void fixJena(){
	
	 Model model = ModelFactory.createDefaultModel();

	 // use the FileManager to find the input file
	 InputStream in = FileManager.get().open(this.getInputFileName());
		if (in == null) {
		    throw new IllegalArgumentException(
		                                 "File: " + inputFileName + " not found");
		}

	// read the RDF/XML file
	model.read(in, null);

	// write it to standard out
	model.write(System.out);
	}
	
	public static void main(String[] args){
		JenaWordnetHandler test = new JenaWordnetHandler();
		test.fixJena();
	}
	
}
