package com.newsaggregator.model;

import java.util.List;

public class Test {
	public static void main(String[] args) {
		Model model = new Model();
		
		List<ArticleData> result = model.search("Facebook Libra: the");
		
		for(ArticleData article : result)
		{
			System.out.println(article.getDataByType(DataType.TITLE));
		}
	}
}
