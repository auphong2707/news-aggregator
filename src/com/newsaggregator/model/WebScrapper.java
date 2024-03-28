package com.newsaggregator.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public abstract class WebScrapper {
	protected String webSource;
    protected String type;
    protected String fileName = "data/";
    
    protected static final List<String> userAgent;
    
    static
    {
    	List<String> tmpList = new ArrayList<String>();
    	try {
			Scanner scanner = new Scanner(new File("user-agents.txt"));
			while (scanner.hasNext()){
			    tmpList.add(scanner.next());
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	finally {
    		userAgent = tmpList;
    	}
    }
    
    private List<ArticleData> listOfData = new ArrayList<ArticleData>();
    
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

 
    private ArticleData scrapeArticle(String articleLink) {
    	String summary="", title="", intro="", detailedContent="", tags="",
    			author="", category="", creationDate="";
    	try {
    		Random r = new Random();
        	Document document = connectWeb(articleLink, userAgent.get(r.nextInt(userAgent.size())));
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
    	
    	System.out.println("Collect data in link successfully");
    	return articleFeatures;
    }
    
    protected void scrapeAllData()
    {
    	List<String> allLinks = getAllLinks();
    
    	for (String link : allLinks) {
    		ArticleData unit = scrapeArticle(link);
    		
    		listOfData.add(unit);
    	}
    	
    	ModelTools.convertDataToJson(listOfData, fileName);
    }
}
