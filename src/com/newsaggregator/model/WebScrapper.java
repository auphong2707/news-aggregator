package com.newsaggregator.model;

import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;

import org.jsoup.nodes.Document;

abstract class WebScrapper {
	String webSource;
	String type;
	String htmlContentLocation;
	String fileName = "data/";
    
    private List<ArticleData> listOfData = new ArrayList<ArticleData>();
    
    abstract List<Pair<String, String>> getLinkAndImageInPage(Document document);
    
    abstract List<Pair<String, String>> getAllLinksAndImages();
    
    
    String getSummary(Document document) {
    	return "";
    }
    
    String getTitle(Document document) {
    	return "";
    }
    
    String getIntro(Document document) {
    	return "";
    }
    
    String getDetailedContent(Document document) {
    	return "";
    }
    
    String getTags(Document document) {
    	return "";
    }
    
    String getAuthor(Document document) {
    	return "";
    }
    
    String getCategory(Document document) {
    	return "";
    }
    
    String getCreationDate(Document document) {
    	return "";
    }
    

 
    private ArticleData scrapeArticle(String articleLink, String imageLink) {
    	String summary="", title="", intro="", detailedContent="", tags="",
    			author="", category="", creationDate="";
    	
    	try {
        	Document document = ModelTools.connectWeb(articleLink);
        	if (document == null) return null;
        	
        	summary = getSummary(document);
            title = getTitle(document);
            intro = getIntro(document); 
            detailedContent = getDetailedContent(document);
            tags = getTags(document);
            author = getAuthor(document);
            category = getCategory(document);
            creationDate = getCreationDate(document);
            
            ArticleData articleFeatures = new ArticleData(articleLink, webSource, imageLink, type, summary,
        			title, intro, detailedContent, tags, author, category, creationDate, htmlContentLocation);
            
            System.out.println("Collect data in link successfully");
            
            return articleFeatures;
    	} catch (Exception e) {
    		System.out.println("Scrape article error: " + e.getMessage());
    	}
    	
    	System.out.println("Skip article!");

    	return null;
    }
    
    void scrapeAllData()
    {
    	List<Pair<String, String>> allLinksImages = getAllLinksAndImages();
    
    	for (Pair<String, String> linkAndImage : allLinksImages) {

    		String link = linkAndImage.getKey();
    		String image = linkAndImage.getValue();
    		ArticleData unit = scrapeArticle(link, image);

    		if (unit != null) listOfData.add(unit);
    	}
    	
    	ModelTools.convertDataToJson(listOfData, fileName);
    }
}
