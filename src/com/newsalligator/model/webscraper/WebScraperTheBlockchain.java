package com.newsalligator.model.webscraper;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.newsalligator.model.tools.WebConnector;

import javafx.util.Pair;

import java.util.HashSet;
import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

final class WebScraperTheBlockchain extends WebScraper {
    WebScraperTheBlockchain()
    {
    	webSource = "The Blockchain";
    	type = "News Article";
    	htmlContentLocation = ".td-pb-span8.td-main-content";
    	fileName += "newsTheBlockchain.json";
    }
    
    @Override 
    List<Pair<String, String>> getAllLinksAndImages(){
    	List<Pair<String, String>> linkAndImage = new ArrayList<>();
    	try {	
            Document document = WebConnector.connectWeb("https://the-blockchain.com/");
            linkAndImage.addAll(getLinkAndImageInPage(document));
            Elements nextElements = document.select(".page-nav.td-pb-padding-side");

            while (!nextElements.isEmpty()) {
            	Element nextPageLink = nextElements.select("a[aria-label=next-page]").first();
            	if (nextPageLink == null) break;
            	String relativeLink = nextPageLink.attr("href");
                if (relativeLink == null || relativeLink.isEmpty()) break;
                document = WebConnector.connectWeb(relativeLink);
                linkAndImage.addAll(getLinkAndImageInPage(document));
                
                nextElements = document.select(".page-nav.td-pb-padding-side");
            }   
        } catch (Exception e) {
            e.printStackTrace();
        } 
    	Set<Pair<String, String>> set = new HashSet<>(linkAndImage);
    	linkAndImage.clear();
    	linkAndImage.addAll(set);
    	return linkAndImage;
    }
    
    @Override 
    List<Pair<String, String>> getLinkAndImageInPage(Document document) {
    	List<Pair<String, String>> linkAndImage = new ArrayList<>();
    	
    	Elements contents = document.getElementsByClass("tdb_module_loop_2 td_module_wrap td-animation-stack td-cpt-post");
    	
    	for (Element content : contents) {
    		Element linkElement = content.selectFirst(".entry-title.td-module-title");
    		Element imageElement = content.selectFirst(".td-module-thumb");
    		
    		if (linkElement != null && imageElement != null) {
    			String link = linkElement.select("a").attr("href");
    			String imageLink = imageElement.select("span").attr("data-img-url");
    			Pair<String, String> tmp = new Pair<String, String>(link, imageLink);
        		linkAndImage.add(tmp);
    		}
		}
    	System.out.println("Collect links and images in page successfully");
    	return linkAndImage;
    }

    
    @Override 
    String getTitle(Document document) {
        Element content = document.selectFirst(".entry-title");
        String title = content.text();
        return title;
    }
    
    @Override 
    String getAuthor(Document document) {
    	Elements content = document.select(".td-post-author-name");
        String author = content.select("a").text();
        return author;
    }

    
    @Override 
    String getDetailedContent(Document document) {
    	Elements contents = document.select(".td-post-content.tagdiv-type");
    	String text = new String();
    	for (Element content : contents) {
    		String paragraph = content.select("p").text();
    		text += paragraph;
    	}
    	return text;
    }

    
    @Override 
    String getCreationDate(Document document) {
    	Element content = document.selectFirst(".td-post-date");
    	String timestamp = content.select(".entry-date.updated.td-module-date").attr("datetime");
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
        LocalDate date = LocalDate.parse(timestamp, formatter);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateString = date.format(outputFormatter);
    	return dateString;
    }
    
    @Override
    String getTags(Document document) {
    	Elements contents = document.select(".td-tags.td-post-small-box.clearfix");
        String tags = "";
        for (Element content : contents.select("a")) {
        	tags += content.text() + ", ";
        }
        return tags;
    }
    
    @Override
    String getCategory(Document document) {
    	Elements contents = document.select(".td-category");
        String categories = "";
        for (Element content : contents.select("a")) {
        	categories += content.text() + ", ";
        }
        return categories;
    }
}
