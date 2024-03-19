package com.newsaggregator.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public abstract class WebScrapper {
	protected String webSource;
    protected String type;
    
    protected final String[] userAgent = {
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.5112.79 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36 Edge/18.19582",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:101.0) Gecko/20100101 Firefox/101.0",
            "Mozilla/5.0 (Windows; U; Windows NT 6.1; rv:2.2) Gecko/20110201",
            "Opera/9.80 (X11; Linux i686; Ubuntu/14.10) Presto/2.12.388 Version/12.16.2"
    };
    
    private List<ArticleData> listOfData = new ArrayList<>();
    
    protected Document connectWeb(String url, String userAgent) throws IOException {
        return Jsoup.connect(url)
                .userAgent(userAgent)
                .referrer("http://www.google.com")
                .timeout(12000)
                .get();
    }
    
    protected abstract List<String> getLinkInPage(Document document);
    
    protected abstract List<String> getAllLinks();
    
    protected String getSummary(Document document) {
    	return "";
    }
    
    protected String getTitle(Document document) {
    	return "";
    }
    
    protected String getIntro(Document document) {
    	return "";
    }
    
    protected String getDetailedContent(Document document) {
    	return "";
    }
    
    protected String getTags(Document document) {
    	return "";
    }
    
    protected String getAuthor(Document document) {
    	return "";
    }
    
    protected String getCategory(Document document) {
    	return "";
    }
    
    protected String getCreationDate(Document document) {
    	return "";
    }

 
    public ArticleData scrapeArticle(String articleLink) {
    	String summary="", title="", intro="", detailedContent="", tags="",
    			author="", category="", creationDate="";
    	try {
    		Random r = new Random();
        	Document document = connectWeb(articleLink, userAgent[r.nextInt(userAgent.length)]);
        	summary = getSummary(document);
            title = getTitle(document);
            intro = getIntro(document);
            detailedContent = getDetailedContent(document);
            tags = getTags(document);
            author = getAuthor(document);
            category = getCategory(document);
            creationDate = getCreationDate(document);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    	ArticleData articleFeatures = new ArticleData(articleLink, webSource, type, summary,
    			title, intro, detailedContent, tags, author, category, creationDate);
    	
    	return articleFeatures;
    }
}
