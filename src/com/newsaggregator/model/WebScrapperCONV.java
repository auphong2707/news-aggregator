package com.newsaggregator.model;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javafx.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class WebScrapperCONV extends WebScrapper {
	WebScrapperCONV()
	{
		webSource = "The Conversation";
    	type = "News Article";
    	htmlContentLocation = ".grid-ten.large-grid-nine.grid-last.content-body.content.entry-content.instapaper_body";
    	fileName += "newsCONV.json";
	}
	
	@Override 
	List<Pair<String, String>> getAllLinksAndImages(){
		List<Pair<String, String>> linkAndImage = new ArrayList<>();
		
    	try {	
            Document document = ModelTools.connectWeb("https://theconversation.com/us/topics/blockchain-11427/");
            linkAndImage.addAll(getLinkAndImageInPage(document));
            Elements nextElements = document.select(".next");

            while (!nextElements.isEmpty()) {
                Element nextPageLink = nextElements.first();
                String relativeLink = nextPageLink.getElementsByTag("a").first().attr("href");
                if (relativeLink == null || relativeLink.isEmpty()) break;
                String completeLink = "https://theconversation.com" + relativeLink;

                document = ModelTools.connectWeb(completeLink);
                linkAndImage.addAll(getLinkAndImageInPage(document));
                
                nextElements = document.select(".next");
            }   
        } catch (IOException e) {
            e.printStackTrace();
        }

    	return linkAndImage;
    }
    
    @Override 
    List<Pair<String, String>> getLinkAndImageInPage(Document document) {
    	List<Pair<String, String>> linkAndImageList = new ArrayList<>();
    	
    	Elements contents = document.getElementsByClass("clearfix placed analysis published");
    	
    	for (Element content : contents) {
    		Element element = content.selectFirst(".article-link");
    		if (element != null) {
    			String link = "https://theconversation.com" + element.attr("href");
    			String imageLink = element.select("img").attr("data-src");
    			Pair<String, String> tmp = new Pair<String, String>(link, imageLink);
        		linkAndImageList.add(tmp);
    		}
		}
    	System.out.println("Collect links and images in page successfully");
    	return linkAndImageList;
    }
    
    @Override 
    String getTitle(Document document) {
        Elements contents = document.select(".legacy.entry-title.instapaper_title");
        for (Element content: contents) {
        	String articleTitle = content.text();
        	return articleTitle;
        }
        return "";
    }
    
    @Override 
    String getAuthor(Document document) {
    	Elements contents = document.select(".fn.author-name");
    	String allAuthor = "";
        for (Element content : contents) {
            String authorArticle = content.select("span").text();
            allAuthor += authorArticle + ", ";

        }
        return allAuthor;
    }
    
    @Override 
    String getDetailedContent(Document document) {
    	Elements contents = document.select(".grid-ten.large-grid-nine.grid-last.content-body.content.entry-content.instapaper_body.inline-promos");
    	String text = new String();
    	for (Element content : contents) {
    		String paragraph = content.select("p").text();
    		text += paragraph;
    	}
    	return text;
    }
    
    @Override 
    String getCreationDate(Document document) {
    	Elements contents = document.select(".timestamps time[datetime]");
    	for (Element content : contents) {
    		String creationDate = content.attr("datetime");
    		return creationDate;
    	}
    	return "";
    }
    
    @Override 
    String getIntro(Document document) {
    	Elements contents = document.select(".styles_description__QQdxm.body-14-regular");
    	String intro = contents.select("p").text();
    	return intro;
    }
}
