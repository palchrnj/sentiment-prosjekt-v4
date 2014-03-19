package machineLearning;

public class VectorAttributes {
	
	public boolean numberOfAdjectives;
	public boolean numberOfSubstantives;
	public boolean numberOfverbs;
	
	public boolean lengthOfTitle;
	public boolean lengthOfLeadText;
	public boolean lengthOfMainText;
	
	public boolean numberOfExclamationMarks;
	public boolean numberOfQuestionMarks;
	public boolean numberOfQuotes;
	
	public boolean averageLengthOfWords;

	public boolean analyticsMentioned;
	public boolean recommenderCluesMentioned;
	
	public boolean numberOfPositiveAdjectives;
	public boolean numberOfNegativeAdjectives;
	public boolean numberOfNeutralAdjectives;
	
	public boolean numberOfPositiveVerbs;
	public boolean numberOfNegativeVerbs;
	
	public boolean numberOfPositiveAdverbs;
	public boolean numberOfNegativeAdverbs;
	
	public boolean isPositiveTitle;
	public boolean isNegativeTitle;
	
	public boolean negativeTitleCount;
	public boolean positiveTitleCount;
	

	public boolean isBors;
	public boolean isAnalyse;
	public boolean isOkonomi;
	
	
	



	public VectorAttributes(){
		this.numberOfAdjectives=false;
		this.numberOfSubstantives=false;
		this.numberOfverbs=false;
		
		this.lengthOfTitle=false;
		this.lengthOfLeadText=false;
		this.lengthOfMainText=false;
		
		this.numberOfExclamationMarks=false;
		this.numberOfQuestionMarks=false;
		this.numberOfQuotes=false;
		
		this.averageLengthOfWords=false;

		this.analyticsMentioned=false;
		this.recommenderCluesMentioned=false;
		
		this.numberOfPositiveAdjectives=false;
		this.numberOfNegativeAdjectives=false;
		this.numberOfNeutralAdjectives=false;
		
		this.numberOfPositiveVerbs=false;
		this.numberOfNegativeVerbs=false;
		
		this.numberOfPositiveAdverbs=false;
		this.numberOfNegativeAdverbs=false;
		
		this.isBors=false;
		this.isAnalyse=false;
		this.isOkonomi=false;
		
		this.isNegativeTitle=false;
		this.isPositiveTitle=false;
		this.positiveTitleCount=false;
		this.negativeTitleCount=false;
	}
	
	public boolean isPositiveTitle() {
		return isPositiveTitle;
	}


	public void setPositiveTitle(boolean isPositiveTitle) {
		this.isPositiveTitle = isPositiveTitle;
	}


	public boolean isNegativeTitle() {
		return isNegativeTitle;
	}


	public void setNegativeTitle(boolean isNegativeTitle) {
		this.isNegativeTitle = isNegativeTitle;
	}


	public boolean isNegativeTitleCount() {
		return negativeTitleCount;
	}


	public void setNegativeTitleCount(boolean negativeTitleCount) {
		this.negativeTitleCount = negativeTitleCount;
	}


	public boolean isPositiveTitleCount() {
		return positiveTitleCount;
	}


	public void setPositiveTitleCount(boolean positiveTitleCount) {
		this.positiveTitleCount = positiveTitleCount;
	}
	public boolean isNumberOfAdjectives() {
		return numberOfAdjectives;
	}
	public void setNumberOfAdjectives(boolean numberOfAdjectives) {
		this.numberOfAdjectives = numberOfAdjectives;
	}
	public boolean isNumberOfSubstantives() {
		return numberOfSubstantives;
	}
	public void setNumberOfSubstantives(boolean numberOfSubstantives) {
		this.numberOfSubstantives = numberOfSubstantives;
	}
	public boolean isNumberOfverbs() {
		return numberOfverbs;
	}
	public void setNumberOfverbs(boolean numberOfverbs) {
		this.numberOfverbs = numberOfverbs;
	}
	public boolean isLengthOfTitle() {
		return lengthOfTitle;
	}
	public void setLengthOfTitle(boolean lengthOfTitle) {
		this.lengthOfTitle = lengthOfTitle;
	}
	public boolean isLengthOfLeadText() {
		return lengthOfLeadText;
	}
	public void setLengthOfLeadText(boolean lengthOfLeadText) {
		this.lengthOfLeadText = lengthOfLeadText;
	}
	public boolean isLengthOfMainText() {
		return lengthOfMainText;
	}
	public void setLengthOfMainText(boolean lengthOfMainText) {
		this.lengthOfMainText = lengthOfMainText;
	}
	public boolean isNumberOfExclamationMarks() {
		return numberOfExclamationMarks;
	}
	public void setNumberOfExclamationMarks(boolean numberOfExclamationMarks) {
		this.numberOfExclamationMarks = numberOfExclamationMarks;
	}
	public boolean isAverageLengthOfWords() {
		return averageLengthOfWords;
	}
	public void setAverageLengthOfWords(boolean averageLengthOfWords) {
		this.averageLengthOfWords = averageLengthOfWords;
	}
	public boolean isNumberOfQuestionMarks() {
		return numberOfQuestionMarks;
	}
	public void setNumberOfQuestionMarks(boolean numberOfQuestionMarks) {
		this.numberOfQuestionMarks = numberOfQuestionMarks;
	}
	public boolean isNumberOfQuotes() {
		return numberOfQuotes;
	}
	public void setNumberOfQuotes(boolean numberOfQuotes) {
		this.numberOfQuotes = numberOfQuotes;
	}
	public boolean isAnalyticsMentioned() {
		return analyticsMentioned;
	}
	public void setAnalyticsMentioned(boolean analyticsMentioned) {
		this.analyticsMentioned = analyticsMentioned;
	}
	public boolean isRecommenderCluesMentioned() {
		return recommenderCluesMentioned;
	}
	public void setRecommenderCluesMentioned(boolean recommenderCluesMentioned) {
		this.recommenderCluesMentioned = recommenderCluesMentioned;
	}
	public boolean isNumberOfPositiveAdjectives() {
		return numberOfPositiveAdjectives;
	}
	public void setNumberOfPositiveAdjectives(boolean numberOfPositiveAdjectives) {
		this.numberOfPositiveAdjectives = numberOfPositiveAdjectives;
	}
	public boolean isNumberOfNegativeAdjectives() {
		return numberOfNegativeAdjectives;
	}
	public void setNumberOfNegativeAdjectives(boolean numberOfNegativeAdjectives) {
		this.numberOfNegativeAdjectives = numberOfNegativeAdjectives;
	}
	public boolean isNumberOfNeutralAdjectives() {
		return numberOfNeutralAdjectives;
	}
	public void setNumberOfNeutralAdjectives(boolean numberOfNeutralAdjectives) {
		this.numberOfNeutralAdjectives = numberOfNeutralAdjectives;
	}
	public boolean isNumberOfPositiveVerbs() {
		return numberOfPositiveVerbs;
	}
	public void setNumberOfPositiveVerbs(boolean numberOfPositiveVerbs) {
		this.numberOfPositiveVerbs = numberOfPositiveVerbs;
	}
	public boolean isNumberOfNegativeVerbs() {
		return numberOfNegativeVerbs;
	}
	public void setNumberOfNegativeVerbs(boolean numberOfNegativeVerbs) {
		this.numberOfNegativeVerbs = numberOfNegativeVerbs;
	}
	public boolean isNumberOfPositiveAdverbs() {
		return numberOfPositiveAdverbs;
	}
	public void setNumberOfPositiveAdverbs(boolean numberOfPositiveAdverbs) {
		this.numberOfPositiveAdverbs = numberOfPositiveAdverbs;
	}
	public boolean isNumberOfNegativeAdverbs() {
		return numberOfNegativeAdverbs;
	}
	public void setNumberOfNegativeAdverbs(boolean numberOfNegativeAdverbs) {
		this.numberOfNegativeAdverbs = numberOfNegativeAdverbs;
	}
	public boolean isBors() {
		return isBors;
	}
	public void setBors(boolean isBors) {
		this.isBors = isBors;
	}
	public boolean isAnalyse() {
		return isAnalyse;
	}
	public void setAnalyse(boolean isAnalyse) {
		this.isAnalyse = isAnalyse;
	}
	public boolean isOkonomi() {
		return isOkonomi;
	}
	public void setOkonomi(boolean isOkonomi) {
		this.isOkonomi = isOkonomi;
	}
	

}
