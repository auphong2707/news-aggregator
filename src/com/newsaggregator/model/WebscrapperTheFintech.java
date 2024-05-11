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

class WebScrapperTheFintech extends WebScrapper {
	WebScrapperTheFintech()
    {
    	webSource = "The Fintech Times";
    	type = "News Article";
    	htmlContentLocation = ".penci-entry-content.entry-content";
    	fileName += "newsTheFintech.json";
    }
    
    @Override 
    List<Pair<String, String>> getAllLinksAndImages(){
    	List<Pair<String, String>> linkAndImage = new ArrayList<>();
    	
    	try {	
    		Document document = ModelTools.connectWeb("https://thefintechtimes.com/category/news/blockchain/");
            linkAndImage.addAll(getLinkAndImageInPage(document));
            Elements nextElements = document.select(".nav-links");

            while (!nextElements.isEmpty()) {
                Element nextPageLink = document.selectFirst("a.next.page-numbers");
                if(nextPageLink == null) break;
                	
                 String completeLink = nextPageLink.attr("href");
                if (completeLink == null || completeLink.isEmpty()) break;
                document = ModelTools.connectWeb(completeLink);
                linkAndImage.addAll(getLinkAndImageInPage(document));   
                nextElements = document.select(".nav-links");
            }   
        } catch (IOException e) {
            e.printStackTrace();
        } 
    	return linkAndImage;
    }
    
    @Override 
    List<Pair<String, String>> getLinkAndImageInPage(Document document) {
    	List<Pair<String, String>> linkAndImage = new ArrayList<>();
    	
    	Elements contents = document.getElementsByClass("article_content penci_media_object");
    	
    	for (Element content : contents) {
    		Element linkElement = content.selectFirst(".entry-title");
    		Element imageElement = content.selectFirst(".penci-link-post.penci-image-holder.penci-disable-lazy");
    		
    		if (linkElement != null && imageElement != null) {
    			String link = linkElement.select("a").attr("href");
    			
    			String imageLink = imageElement.select("a").attr("style");
    			int start = imageLink.indexOf("url(") + 4;
    			int end = imageLink.indexOf(")", start);
    			String cleanImageLink = imageLink.substring(start, end);
    			
    			Pair<String, String> tmp = new Pair<String, String>(link, cleanImageLink);
        		linkAndImage.add(tmp);
    		}
        }
    	System.out.println("Collect links and images in page successfully");
    	return linkAndImage;
    }

    @Override 
    String getTitle(Document document) {
    	Elements content = document.select(".entry-title.penci-entry-title.penci-title-");
        String title = content.text();
        return title;
    }
    
    @Override 
    String getAuthor(Document document) {
    	Elements contents = document.select(".author.vcard");
		String allAuthor = "";
        for (Element content : contents) {
            String authorArticle = content.select("span").text();
            allAuthor += authorArticle + ", ";

        }
        return allAuthor;
    }

    
    @Override 
    String getDetailedContent(Document document) {
    	Elements contents = document.select(".penci-entry-content.entry-content");
		String text = new String();
        for (Element content : contents) {
        	String paragraph = content.select("p").text();
    		text += paragraph;
        }
    	return text;
    }

    @Override 
    String getCreationDate(Document document) {
    	Elements content = document.select(".entry-date.published");
    	String timestamp = content.attr("datetime");
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
        LocalDate date = LocalDate.parse(timestamp, formatter);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateString = date.format(outputFormatter);
    	return dateString;
    }
    
    @Override
    String getCategory(Document document) {
    	Elements contents = document.select(".penci-entry-categories");
        String categories = "";
        for (Element content : contents.select("a")) {
        	categories += content.text() + ", ";
        }
        return categories;
    }
    
    @Override 
    String getIntro(Document document) {
		Elements content = document.select(".penci-entry-content.entry-content");
        String intro = content.select("h4").text();
    	return intro;
    }
}

