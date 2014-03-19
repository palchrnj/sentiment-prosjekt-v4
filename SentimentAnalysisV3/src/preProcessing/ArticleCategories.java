package preProcessing;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;



import newsAPI.NewsArticleRaw;
import annotationStatistics.AnnotationValidator;

import com.google.gson.Gson;

public class ArticleCategories {
	
	
	public ArrayList<ArticleCategory> categories;
	
	
	public ArticleCategories() {
		this.categories = new ArrayList<ArticleCategory>();
	}
	
	
	/* ADDS A CATEGORY OF A GIVEN ARTICLE TO A LIST */
	public void addCategory(String category, NewsArticleRaw article) throws IOException{
		
		boolean alreadyExists = false;
		AnnotationValidator val = new AnnotationValidator();
		
		for(int i=0; i<categories.size(); i++){
			if(categories.get(i).getCategory().equals(category)){
				if(val.parseSentimentValue(article.getSentimentValue()).get(3).equals(1)){
					categories.get(i).positive+=1;
				}
				else if(val.parseSentimentValue(article.getSentimentValue()).get(3).equals(-1)){
					categories.get(i).negative+=1;
				}
				else{
					categories.get(i).neutral+=1;
				}
				categories.get(i).count += 1;
				alreadyExists = true;
			}
		}
		if(!alreadyExists){
			ArticleCategory newCategory = new ArticleCategory(category);
			categories.add(newCategory);
		}
	}
	
	/* WRITES AN OVERVIEW OF THE CATEGORIES TO FILE */
	public void writeToFileAsJson() throws IOException{
			Gson gson = new Gson();
			String json = gson.toJson(this);
			Writer out = new BufferedWriter(new OutputStreamWriter(
			    new FileOutputStream("C:/Users/Lars/Desktop/SentimentProject/SentimentAnalysis/src/xml/ArticleCategories.txt"), "UTF-8"));
			try {
			    out.write(json);
			} finally {
			    out.close();
			}
	}

	/* SORTS LIST OF ARTICLES */
	public void sortWords(){
		ArrayList<ArticleCategory> sorted = this.getCategories();
		Collections.sort(sorted, new ArticleCategoryComparator());
		this.setCategories(sorted);	
	}
	
	
	public ArrayList<ArticleCategory> getCategories() {
		return categories;
	}

	public void setCategories(ArrayList<ArticleCategory> categories) {
		this.categories = categories;
	}
	
}
