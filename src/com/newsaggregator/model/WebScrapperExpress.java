package com.newsaggregator.model;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javafx.util.Pair;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

class WebScrapperExpress extends WebScrapper {
	WebScrapperExpress()
    {
    	webSource = "Financial Express";
    	type = "News Article";
    	htmlContentLocation = ".pcl-container";
    	fileName += "newsExpress.json";
    }
    
    @Override 
    List<Pair<String, String>> getAllLinksAndImages(){
    	List<Pair<String, String>> linkAndImage = new ArrayList<>();
    	
    	try {	
    		Document document = ModelTools.connectWeb("https://www.financialexpress.com/about/blockchain/");
            linkAndImage.addAll(getLinkAndImageInPage(document));
            Elements nextElements = document.select(".pagination");
            
            while (!nextElements.isEmpty()) {
            	Elements nextPageLink = document.select("a.next.page-numbers");
            	String completeLink = nextPageLink.attr("href");
                if (completeLink == null || completeLink.isEmpty()) break;
                document = ModelTools.connectWeb(completeLink);
                linkAndImage.addAll(getLinkAndImageInPage(document));   
                nextElements = document.select(".pagination");
            }   
        } catch (IOException e) {
            e.printStackTrace();
        } 
    	return linkAndImage;
    }
    
    @Override 
    List<Pair<String, String>> getLinkAndImageInPage(Document document) {
    	List<Pair<String, String>> linkAndImage = new ArrayList<>();
    	
    	Elements contents = document.getElementsByClass("tag-blockchain");
    	
    	for (Element content : contents) {
    		Element element = content.selectFirst(".post-thumbnail");

    		if (element != null) {
    			String link = element.select("a").attr("href");
    			String imageLink = element.select("img").attr("src");
    			Pair<String, String> tmp = new Pair<String, String>(link, imageLink);
        		linkAndImage.add(tmp);
    		}
        }
    	System.out.println("Collect links and images in page successfully");
    	return linkAndImage;
    }

    @Override 
    String getTitle(Document document) {
    	Elements content = document.select(".wp-block-post-title");
        String title = content.text();
        return title;
    }
    
    @Override 
    String getAuthor(Document document) {
    	Element content = document.selectFirst(".pcl-container");
        String author = content.text();
        if (author.contains("By ")) {
        	author = author.replace("By ", "");
        }
        return author;
    }

    
    @Override 
    String getDetailedContent(Document document) {
    	Elements contents = document.select(".pcl-container");
    	String text = new String();
    	for (Element content : contents) {
    		String paragraph = content.select("p").text();
    		text += paragraph;
    	}
    	return text;
    }

    
    @Override 
    String getCreationDate(Document document) {
    	Elements content = document.select(".ie-network-post-meta-date");
    	String timestamp = content.attr("datetime");
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
        LocalDate date = LocalDate.parse(timestamp, formatter);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateString = date.format(outputFormatter);
    	return dateString;
    }
      
    @Override
    String getTags(Document document) {
    	Elements contents = document.select(".wp-block-button.has-custom-font-size.has-small-font-size");
        String tags = "";
        for (Element content : contents.select("a[href]")) {
        	tags += content.text() + ", ";
        }
        return tags;
    }
}

