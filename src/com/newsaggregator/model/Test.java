package com.newsaggregator.model;

import java.io.IOException;
import java.util.List;

public class Test {
	public static void main(String[] args) throws IOException, InterruptedException{
		Model model = new Model();
		model.runLocalServer();
		
		List<ArticleData> result = model.search("Facebook Libra: the");
		
		for(ArticleData article : result)
		{
			System.out.println(article.getDataByType(DataType.TITLE));
		}
		
		model.terminateLocalServer();
	}
}
