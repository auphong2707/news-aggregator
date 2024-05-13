package com.newsaggregator.model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javafx.util.Pair;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Test {
	public static void main(String[] args) throws IOException {   	
		 WebScrapper wb = new WebScrapperAcademy();
		 wb.scrapeAllData();
	}		
}
     
