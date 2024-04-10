package com.newsaggregator.model;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javafx.util.Pair;


public class Test {
	public static void main(String[] args) throws IOException, InterruptedException{
		Model model = new Model();
		model.scrapeNewData();
//		WebScrapper wb = new WebScrapperAcademy();
//		wb.scrapeAllData();

	}
}
