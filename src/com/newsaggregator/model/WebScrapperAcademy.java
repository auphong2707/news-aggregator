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

public class WebScrapperAcademy extends WebScrapper {
	public WebScrapperAcademy()
	{
		webSource = "Academy Moralis";
    	type = "Blog";
    	fileName += "newsAcademy.json";
	}
	
	@Override
    protected List<String> getAllLinks(){
    	List<String> allLinks = new ArrayList<String>();
    	try {	
            Random r = new Random();
            Document document = connectWeb("https://academy.moralis.io/blog/blockchain", userAgent.get(r.nextInt(userAgent.size())));
            Elements nextElements = document.select(".page-numbers.next");

            while (!nextElements.isEmpty()) {
                Element nextPageLink = nextElements.first();
                String relativeLink = nextPageLink.getElementsByTag("a").first().attr("href");
                if (relativeLink == null || relativeLink.isEmpty()) break;
                String completeLink = "https://academy.moralis.io/blog/blockchain" + relativeLink;
                System.out.println(completeLink);
                document = connectWeb(completeLink, userAgent.get(r.nextInt(userAgent.size())));
                allLinks.addAll(getLinkInPage(document));
                
                nextElements = document.select(".page-numbers.next");
            }   
        } catch (IOException e) {
            e.printStackTrace();
        }
    	
    	return allLinks;
    }
    
    @Override
    protected List<String> getLinkInPage(Document document) {
    	List<String> articleLinks = new ArrayList<String>();
    	
    	Elements contents = document.select(".elementor-post*");
        for (Element content : contents) {
            Element linkArticle = content.selectFirst("a.elementor-post__thumbnail__link");
            String linkHref = "https://academy.moralis.io/blog" + linkArticle.attr("href");
            articleLinks.add(linkHref);
        }
        System.out.println("Collect links in page successfully");
        return articleLinks;
    }
    
    @Override
    protected String getTitle(Document document) {
        Elements contents = document.select("");
        for (Element content: contents) {
        	String articleTitle = content.text();
        	return articleTitle;
        }
        return "";
    }
    
    @Override
    protected String getAuthor(Document document) {
    	Elements contents = document.select("");
    	String allAuthor = "";
        for (Element content : contents) {
            String authorArticle = content.select("").text();
            allAuthor += authorArticle + ", ";
            // return authorArticle;
        }
        return allAuthor;
    }
//    
    @Override
    protected String getDetailedContent(Document document) {
    	Elements contents = document.select("");
    	String text = new String();
    	for (Element content : contents) {
    		String paragraph = content.select("p").text();
    		text += paragraph;
    	}
    	return text;
    }
    
    @Override
    protected String getCreationDate(Document document) {
    	Elements contents = document.select("");
    	for (Element content : contents) {
    		String creationDate = content.attr("");
    		return creationDate;
    	}
    	return "";
    }
    
    @Override
    protected String getTags(Document document) {
    	Elements contents = document.select("");
    	String allTags = "";
        for (Element content : contents) {
            String tagArticle = content.select("").text();
            allTags += tagArticle + ", ";
            // return authorArticle;
        }
        return allTags;
    }
    
    @Override
    protected String getImage(Document document) {
    	Elements contents = document.select("");
        for (Element content : contents) {
        	String imageLink = content.select("").first().attr("");
            return imageLink;
        }
        return "";
    }
}
