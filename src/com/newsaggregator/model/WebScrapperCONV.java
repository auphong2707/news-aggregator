package com.newsaggregator.model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WebScrapperCONV extends WebScrapper {
	public WebScrapperCONV()
	{
		webSource = "https://theconversation.com/us/topics/blockchain-11427/";
    	type = "News Article";
    	fileName += "newsCONV.json";
	}
	
	@Override
    protected List<String> getAllLinks(){
    	List<String> allLinks = new ArrayList<String>();
    	try {	
            Random r = new Random();
            Document document = connectWeb(webSource, userAgent.get(r.nextInt(userAgent.size())));
            Elements nextElements = document.select(".next");

            while (!nextElements.isEmpty()) {
                Element nextPageLink = nextElements.first();
                String relativeLink = nextPageLink.getElementsByTag("a").first().attr("href");
                if (relativeLink == null || relativeLink.isEmpty()) break;
                String completeLink = "https://theconversation.com" + relativeLink;

                document = connectWeb(completeLink, userAgent.get(r.nextInt(userAgent.size())));
                allLinks.addAll(getLinkInPage(document));
                
                nextElements = document.select(".next");
            }   
        } catch (IOException e) {
            e.printStackTrace();
        }
    	
    	return allLinks;
    }
    
    @Override
    protected List<String> getLinkInPage(Document document) {
    	List<String> articleLinks = new ArrayList<String>();
    	
    	Elements contents = document.select(".article--header");
        for (Element content : contents) {
            Element linkArticle = content.selectFirst("a");
            String linkHref = "https://theconversation.com" + linkArticle.attr("href");
            articleLinks.add(linkHref);
        }
        System.out.println("Collect links in page successfully");
        return articleLinks;
    }
    
    @Override
    protected String getTitle(Document document) {
        Elements contents = document.select(".legacy.entry-title.instapaper_title");
        for (Element content: contents) {
        	String articleTitle = content.text();
        	return articleTitle;
        }
        return "";
    }
    
    @Override
    protected String getAuthor(Document document) {
    	Elements contents = document.select(".fn.author-name");
    	String allAuthor = "";
        for (Element content : contents) {
            String authorArticle = content.select("span").text();
            allAuthor += authorArticle + " ";
            // return authorArticle;
        }
        return allAuthor;
    }
    
    @Override
    protected String getDetailedContent(Document document) {
    	Elements contents = document.select(".grid-ten.large-grid-nine.grid-last.content-body.content.entry-content.instapaper_body.inline-promos");
    	String text = new String();
    	for (Element content : contents) {
    		String paragraph = content.select("p").text();
    		text += paragraph;
    	}
    	return text;
    }
    
    @Override
    protected String getCreationDate(Document document) {
    	Elements contents = document.select(".timestamps");
    	for (Element content : contents) {
    		String creationDate = content.attr("datetime");
    		return creationDate;
    	}
    	return "";
    }
}
