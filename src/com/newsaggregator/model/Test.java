package com.newsaggregator.model;

import java.io.IOException;

import org.jsoup.Jsoup;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.HttpStatusException;
import javafx.util.Pair;


public class Test {
	public static void main(String[] args) throws IOException, HttpStatusException{   	
		WebScrapper wp = new WebScrapperCoindesk();
		wp.scrapeAllData();
//		Document document = Jsoup.connect("https://www.coindesk.com/tech/2024/03/20/protocol-village/").get();

    }
}

        
     
