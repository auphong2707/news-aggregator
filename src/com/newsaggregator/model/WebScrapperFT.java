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

public class WebScrapperFT extends WebScrapper {
    public WebScrapperFT()
    {
    	webSource = "https://www.ft.com/blockchain";
    	type = "News Article";
    	fileName += "newsFT.csv";
    }
    
    @Override
    protected List<String> getAllLinks(){
    	List<String> allLinks = new ArrayList<String>();
    	try {	
            Random r = new Random();
            Document document = connectWeb(webSource, userAgent.get(r.nextInt(userAgent.size())));
            Elements nextElements = document.select(".stream__pagination.o-buttons-pagination");

            while (!nextElements.isEmpty()) {
                Element nextPageLink = document.selectFirst("a.o-buttons.o-buttons--secondary.o-buttons-icon.o-buttons-icon--arrow-right.o-buttons-icon--icon-only");
                String relativeLink = nextPageLink.attr("href");
                if (relativeLink == null || relativeLink.isEmpty()) break;
                String completeLink = webSource + relativeLink;

                document = connectWeb(completeLink, userAgent.get(r.nextInt(userAgent.size())));
                allLinks.addAll(getLinkInPage(document));
                
                nextElements = document.select(".stream__pagination.o-buttons-pagination");
            }   
        } catch (IOException e) {
            e.printStackTrace();
        }
    	
    	return allLinks;
    }
    
    @Override
    protected List<String> getLinkInPage(Document document) {
    	List<String> articleLinks = new ArrayList<String>();
    	
    	Elements contents = document.select(".o-teaser__heading");
        for (Element content : contents) {
            Element linkArticle = content.selectFirst("a.js-teaser-heading-link");
            String linkHref = "https://www.ft.com" + linkArticle.attr("href");
            articleLinks.add(linkHref);
        }
        System.out.println("Collect links in page successfully");
        return articleLinks;
    }
    
    
    @Override
    protected String getTitle(Document document) {
        Elements contents = document.select(".article-classifier__gap");
        for (Element content: contents) {
        	String articleTitle = content.text();
        	return articleTitle;
        }
        return "";
    }
    
    @Override
    protected String getAuthor(Document document) {
    	Elements contents = document.select(".article-info__time-byline");
        for (Element content : contents) {
            String authorArticle = content.select("a[href]").text();
            return authorArticle;
        }
        return "";
    }
    
    @Override
    protected String getDetailedContent(Document document) {
    	Elements contents = document.select(".n-content-body.js-article__content-body");
    	String text = new String();
    	for (Element content : contents) {
    		String paragraph = content.select("p").text();
    		text += paragraph;
    	}
    	return text;
    }
    
    @Override
    protected String getCreationDate(Document document) {
    	Elements contents = document.select(".article-info__timestamp.o-date");
    	for (Element content : contents) {
    		String creationDate = content.attr("datetime");
    		return creationDate;
    	}
    	return "";
    }
    
    @Override
    protected String getIntro(Document document) {
    	Elements contents = document.select(".o-topper__standfirst");
        for (Element content : contents) {
            String introArticle = content.text();
            return introArticle;
        }
        return "";
    }
}
