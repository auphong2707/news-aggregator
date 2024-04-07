package com.newsaggregator.model;

import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Test {
	public static void main(String[] args) throws IOException, InterruptedException{
		Model model = new Model();
		model.runLocalServer();
		WebScrapper scraper = new WebScrapperCONV();
		scraper.scrapeAllData();
		
		model.terminateLocalServer();
	

	}
}
