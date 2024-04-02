package com.newsaggregator.model;

import java.io.IOException;
import java.util.List;

public class Test {
	public static void main(String[] args) throws IOException, InterruptedException{
		Model model = new Model();
		model.runLocalServer();
		List<ArticleData> result = model.getRandomArticleData(5);
		for(ArticleData data : result) {
			System.out.println(data.getDataByType(DataType.TITLE));
		}
		
		model.terminateLocalServer();
	}
}
