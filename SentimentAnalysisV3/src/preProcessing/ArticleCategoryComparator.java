package preProcessing;

import java.util.Comparator;

public class ArticleCategoryComparator implements Comparator<ArticleCategory> {

	/* COMPARES 2 ARTICLE CATEGORIES */
	@Override
	public int compare(ArticleCategory wc1, ArticleCategory wc2) {
		if(wc1.getCount() > wc2.getCount()){
			return -1;
		}
		else if(wc1.getCount() < wc2.getCount()){
			return 1;
		}
		else{
			return 0;
		}
	}

}
