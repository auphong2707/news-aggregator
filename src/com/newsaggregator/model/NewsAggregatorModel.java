package com.newsaggregator.model;

public class NewsAggregatorModel {
	public static void main(String args[]) {
		WebScraper scraper = new WebScraper();
		ArticleData test = scraper.scrapeArticle("https://www.ft.com/content/2daf0c89-a2b5-4828-bb76-18b1e5aa99ef");
		for (DataType feature : DataType.values()) {
			System.out.println(test.getDataByType(feature));
		}
	}	
}
