package com.newsaggregator.model;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javafx.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class WebScrapperFT extends WebScrapper {
    WebScrapperFT()
    {
    	webSource = "Financial Times";
    	type = "News Article";
    	fileName += "newsFT.json";
    }
    
    @Override 
    List<Pair<String, String>> getAllLinksAndImages(){
    	List<Pair<String, String>> linkAndImage = new ArrayList<>();
    	
    	try {	
            Document document = connectWeb("https://www.ft.com/blockchain");
            linkAndImage.addAll(getLinkAndImageInPage(document));
            Elements nextElements = document.select(".stream__pagination.o-buttons-pagination");

            while (!nextElements.isEmpty()) {
                Element nextPageLink = document.selectFirst("a.o-buttons.o-buttons--secondary.o-buttons-icon.o-buttons-icon--arrow-right.o-buttons-icon--icon-only");
                String relativeLink = nextPageLink.attr("href");
                if (relativeLink == null || relativeLink.isEmpty()) break;
                String completeLink = "https://www.ft.com/blockchain" + relativeLink;

                document = connectWeb(completeLink);
                linkAndImage.addAll(getLinkAndImageInPage(document));
                
                nextElements = document.select(".stream__pagination.o-buttons-pagination");
            }   
        } catch (IOException e) {
            e.printStackTrace();
        } 
    	
    	return linkAndImage;
    }
    
    @Override 
    List<Pair<String, String>> getLinkAndImageInPage(Document document) {
    	List<Pair<String, String>> linkAndImage = new ArrayList<>();
    	
    	Elements contents = document.getElementsByClass("o-teaser-collection__item o-grid-row");
    	
    	for (Element content : contents) {
    		Element linkElement = content.selectFirst(".o-teaser__heading");
    		Element imageElement = content.selectFirst(".o-teaser__image-placeholder");
    		
    		if (linkElement != null && imageElement != null) {
    			String link = "https://www.ft.com" + linkElement.select("a").attr("href");
    			String imageLink = imageElement.select("img").attr("data-src");
    			Pair<String, String> tmp = new Pair<String, String>(link, imageLink);
        		linkAndImage.add(tmp);
    		}
		}
    	System.out.println("Collect links and images in page successfully");
    	return linkAndImage;
    }
    
    @Override 
    String getTitle(Document document) {
        Elements contents = document.select(".article-classifier__gap");
        for (Element content: contents) {
        	String articleTitle = content.text();
        	return articleTitle;
        }
        return "";
    }
    
    @Override 
    String getAuthor(Document document) {
    	Elements contents = document.select(".article-info__time-byline");
        for (Element content : contents) {
            String authorArticle = content.select("a[href]").text();
            return authorArticle;
        }
        return "";
    }
    
    @Override 
    String getDetailedContent(Document document) {
    	Elements contents = document.select(".n-content-body.js-article__content-body");
    	String text = new String();
    	for (Element content : contents) {
    		String paragraph = content.select("p").text();
    		text += paragraph;
    	}
    	return text;
    }
    
    @Override 
    String getCreationDate(Document document) {
    	Elements contents = document.select(".article-info__timestamp.o-date");
    	for (Element content : contents) {
    		String creationDate = content.attr("datetime");
    		return creationDate;
    	}
    	return "";
    }
    
    @Override 
    String getIntro(Document document) {
    	Elements contents = document.select(".o-topper__standfirst");
        for (Element content : contents) {
            String introArticle = content.text();
            return introArticle;
        }
        return "";
    }
}
