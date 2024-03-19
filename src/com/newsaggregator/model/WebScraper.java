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

public class WebScraper {
    private final String webSource = "https://www.ft.com/blockchain";
    private List<ArticleData> listOfData = new ArrayList<>();
    private final String type = "News Article";
    private final String[] userAgent = {
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.5112.79 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36 Edge/18.19582",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:101.0) Gecko/20100101 Firefox/101.0",
            "Mozilla/5.0 (Windows; U; Windows NT 6.1; rv:2.2) Gecko/20110201",
            "Opera/9.80 (X11; Linux i686; Ubuntu/14.10) Presto/2.12.388 Version/12.16.2"
    };
    
    private Document connectWeb(String url, String userAgent) throws IOException {
        return Jsoup.connect(url)
                .userAgent(userAgent)
                .referrer("http://www.google.com")
                .timeout(12000)
                .get();
    }
    
    private String getTitle(Document document) {
        Elements contents = document.select(".article-classifier__gap");
        for (Element content: contents) {
        	String articleTitle = content.text();
        	return articleTitle;
        }
        return "";
    }
    
    private String getAuthor(Document document) {
    	Elements contents = document.select(".article-info__time-byline");
        for (Element content : contents) {
            String authorArticle = content.select("a[href]").text();
            return authorArticle;
        }
        return "";
    }
    
    private String getDetailedContent(Document document) {
    	Elements contents = document.select(".n-content-body.js-article__content-body");
    	String text = new String();
    	for (Element content : contents) {
    		String paragraph = content.select("p").text();
    		text += paragraph;
    	}
    	return text;
    }
    
    private String getCreationDate(Document document) {
    	Elements contents = document.select(".article-info__timestamp.o-date");
    	for (Element content : contents) {
    		String creationDate = content.attr("datetime");
    		return creationDate;
    	}
    	return "";
    }
    
    private List<String> getLinkInPage(Document document) {
    	List<String> articleLinks = new ArrayList<String>();
    	
    	Elements contents = document.select(".o-teaser__heading");
        for (Element content : contents) {
            Element linkArticle = content.selectFirst("a.js-teaser-heading-link");
            String linkHref = "https://www.ft.com" + linkArticle.attr("href");
            articleLinks.add(linkHref);
        }
        return articleLinks;
    }
    
    private String getIntro(Document document) {
    	Elements contents = document.select(".o-topper__standfirst");
        for (Element content : contents) {
            String introArticle = content.text();
            return introArticle;
        }
        return "";
    }
    
    private List<String> getAllLinks(){
    	List<String> allLinks = new ArrayList<String>();
    	try {	
            Random r = new Random();
            Document document = connectWeb(webSource, userAgent[r.nextInt(userAgent.length)]);
            Elements nextElements = document.select(".stream__pagination.o-buttons-pagination");

            while (!nextElements.isEmpty()) {
                Element nextPageLink = document.selectFirst("a.o-buttons.o-buttons--secondary.o-buttons-icon.o-buttons-icon--arrow-right.o-buttons-icon--icon-only");
                String relativeLink = nextPageLink.attr("href");
                if (relativeLink == null || relativeLink.isEmpty()) break;
                String completeLink = webSource + relativeLink;

                document = connectWeb(completeLink, userAgent[r.nextInt(userAgent.length)]);
                allLinks.addAll(getLinkInPage(document));
                
                nextElements = document.select(".stream__pagination.o-buttons-pagination");
            }   
        } catch (IOException e) {
            e.printStackTrace();
        }
    	
    	return allLinks;
    }
    

    
    public ArticleData scrapeArticle(String articleLink) {
    	String title="", author="", date="", detailContent="", intro="";
    	try {
    		Random r = new Random();
        	Document document = connectWeb(articleLink, userAgent[r.nextInt(userAgent.length)]);
            title = getTitle(document);
            author = getAuthor(document);
            date = getCreationDate(document);
            detailContent = getDetailedContent(document);
            intro = getIntro(document);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    	ArticleData articleFeatures = new ArticleData(articleLink, webSource, type, "",
    			title, intro, detailContent, "", author, "", date);
    	
    	return articleFeatures;
    }
}
