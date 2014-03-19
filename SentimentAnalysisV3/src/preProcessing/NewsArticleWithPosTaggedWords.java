package preProcessing;

import java.util.ArrayList;


public class NewsArticleWithPosTaggedWords extends NewsArticleWithTickers {

	
	public PosTaggedWords posTaggedTitle;
	public PosTaggedWords posTaggedLeadText;
	public PosTaggedWords posTaggedMainText;
	
	
	public NewsArticleWithPosTaggedWords() {

	}


	public PosTaggedWords getPosTaggedTitle() {
		return posTaggedTitle;
	}


	public void setPosTaggedTitle(PosTaggedWords posTaggedTitle) {
		this.posTaggedTitle = posTaggedTitle;
	}


	public PosTaggedWords getPosTaggedLeadText() {
		return posTaggedLeadText;
	}


	public void setPosTaggedLeadText(PosTaggedWords posTaggedLeadText) {
		this.posTaggedLeadText = posTaggedLeadText;
	}


	public PosTaggedWords getPosTaggedMainText() {
		return posTaggedMainText;
	}


	public void setPosTaggedMainText(PosTaggedWords posTaggedMainText) {
		this.posTaggedMainText = posTaggedMainText;
	}
	
	public ArrayList<PosTaggedWord> getAllPosTaggedWords(){
		ArrayList<PosTaggedWord> completePosTaggedList = new ArrayList<PosTaggedWord>();

		if(this.getPosTaggedTitle().getPosTaggedWords()!=null){
			for(int i=0; i<this.getPosTaggedTitle().getPosTaggedWords().length; i++){
				completePosTaggedList.add(this.getPosTaggedTitle().getPosTaggedWords()[i]);
			}
		}
		if(this.getPosTaggedLeadText().getPosTaggedWords()!=null){
			for(int i=0; i<this.getPosTaggedLeadText().getPosTaggedWords().length; i++){
				completePosTaggedList.add(this.getPosTaggedLeadText().getPosTaggedWords()[i]);
			}
		}	
		if(this.getPosTaggedMainText().getPosTaggedWords()!=null){
			for(int i=0; i<this.getPosTaggedMainText().getPosTaggedWords().length; i++){
				completePosTaggedList.add(this.getPosTaggedMainText().getPosTaggedWords()[i]);
			}
		}	
		
		return completePosTaggedList;
	}

}
