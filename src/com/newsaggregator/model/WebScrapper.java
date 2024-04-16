package com.newsaggregator.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import javafx.util.Pair;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

abstract class WebScrapper {
	String webSource;
	String type;
	String fileName = "data/";
    
    private static final List<String> userAgent;
    
    static
    {
    	List<String> tmpList = new ArrayList<String>();
    	try {
			Scanner scanner = new Scanner(new File("user-agents.txt"));
			while (scanner.hasNext()){
			    tmpList.add(scanner.next());
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	finally {
    		userAgent = tmpList;
    	}
    }
    
    private List<ArticleData> listOfData = new ArrayList<ArticleData>();
    
    Document connectWeb(String url) throws IOException, HttpStatusException {
    	Document document = null;
    	while (document == null) {
    		try {	
        		Random random = new Random();
            	String randomUA = userAgent.get(random.nextInt(userAgent.size()));
            	document = Jsoup.connect(url)
                        .userAgent(randomUA)
                        .referrer("http://www.google.com")
                        .get();
                
        	} catch (HttpStatusException e) {
        		System.out.println("Error connecting to URL: " + e.getMessage());
        	}	
    	}
    	return document;
    }
    
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
    
    String getHTML(Document document) {
    	return "";
    }
    

 
    private ArticleData scrapeArticle(String articleLink, String imageLink) {
    	String summary="", title="", intro="", detailedContent="", tags="",
    			author="", category="", creationDate="";
    	try {
        	Document document = connectWeb(articleLink);
        	summary = getSummary(document);
            title = getTitle(document);
            intro = getIntro(document); 
            detailedContent = getDetailedContent(document);
            tags = getTags(document);
            author = getAuthor(document);
            category = getCategory(document);
            creationDate = getCreationDate(document);
    	} catch (IOException e) {
    		System.out.println("ERROR");
    	}
    	ArticleData articleFeatures = new ArticleData(articleLink, webSource, imageLink, type, summary,
    			title, intro, detailedContent, tags, author, category, creationDate);

    	System.out.println("Collect data in link successfully");

    	return articleFeatures;
    }
    
    void scrapeAllData()
    {
    	List<Pair<String, String>> allLinksImages = getAllLinksAndImages();
    
    	for (Pair<String, String> linkAndImage : allLinksImages) {

    		String link = linkAndImage.getKey();
    		String image = linkAndImage.getValue();
    		ArticleData unit = scrapeArticle(link, image);

    		listOfData.add(unit);
    	}
    	
    	ModelTools.convertDataToJson(listOfData, fileName);
    }
}
