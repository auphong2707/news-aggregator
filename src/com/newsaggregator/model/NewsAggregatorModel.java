package com.newsaggregator.model;

public class NewsAggregatorModel {
	public static void main(String args[]) {
		WebScrapperFT scraperFT = new WebScrapperFT();
		// WebScrapperCONV scraperCONV = new WebScrapperCONV();
		scraperFT.scrapeAllData();
	}
}
