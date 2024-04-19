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
		WebScrapper wp = new WebScrapperFreightWave();
		wp.scrapeAllData();
//		Document document = Jsoup.connect("https://www.freightwaves.com/blockchain").
//				userAgent("Mozilla").get();
//		List<Pair<String, String>> linkAndImage = new ArrayList<>();
//    	
//    	Elements contents = document.getElementsByClass("grid sm:grid-cols-2 gap-x-25 gap-y-10 sm:gap-25 mb-25");
//    	
//    	for (Element content : contents) {
//    		Element linkElement = content.selectFirst(".fw-block-post-item-blue");
//    		Element imageElement = content.selectFirst(".aspect-w-16.aspect-h-9.overflow-hidden");
//    		
//    		if (linkElement != null && imageElement != null) {
//    			String link = linkElement.select("a.fw-block-post-item-blue").attr("href");
//    			String imageLink = imageElement.select("img").attr("src");
//    			Pair<String, String> tmp = new Pair<String, String>(link, imageLink);
//        		linkAndImage.add(tmp);
//    		}
//        }
//    	for (Pair<String, String> e : linkAndImage) {
//    		System.out.println(e);
//    	}
    }
}
        
     
