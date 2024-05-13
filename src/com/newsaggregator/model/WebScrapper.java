package com.newsaggregator.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Collections;
import java.util.List;
import javafx.util.Pair;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jsoup.nodes.Document;

/**
 * <h1> WebScrapper </h1>
 * The WebScrapper class is an abstract class which represents a web scraper that extracts useful
 * data from the news article/blog page. The derived class should implement the detailed process to get
 * specific data from a web page about blockchain.
 * @author Dat Dao, Phong Au
 * @since 2024-03-09
 */
abstract class WebScrapper {
	/**
	 * The name of the web being scraped. 
	 */
	String webSource;
	
	/**
	 * The type of the web (e.g News Article/ Blog/ Tweet/ Facebook Post). 
	 */
	String type;
	
	/**
	 * The location of HTML code of the news content.
	 */
	String htmlContentLocation;
	
	/**
	 * The directory where the data is saved.
	 */
	String fileName = "data/";
    
	/**
	 * The abstract operation to get the article link and 
	 * the thumbnail image URL of a news container of a page. 
	 * @param document
	 * @return A list of pair containing article links and thumbnail image URLs found in a page
	 */
	abstract List<Pair<String, String>> getLinkAndImageInPage(Document document);
    
	/**
	 * The abstract operation to iterate and use getLinkAndImageInPage operation in every page in a web.
	 * @return A list of pair containing article links and thumbnail image URLs in all pages of a web.
	 */
    abstract List<Pair<String, String>> getAllLinksAndImages();
    
    /**
     * Extracts the summary of an article.
     *
     * @param document The document object representing the web page.
     * @return The summary of the article.
     */
    String getSummary(Document document) {
        return "";
    }

    /**
     * Extracts the title of an article.
     *
     * @param document The document object representing the web page.
     * @return The title of the article.
     */
    String getTitle(Document document) {
        return "";
    }

    /**
     * Extracts the introduction/standfirst of an article.
     *
     * @param document The document object representing the web page.
     * @return The introduction of the article.
     */
    String getIntro(Document document) {
        return "";
    }

    /**
     * Extracts the detailed content of an article.
     *
     * @param document The document object representing the web page.
     * @return The detailed content of the article.
     */
    String getDetailedContent(Document document) {
        return "";
    }

    /**
     * Extracts the tags associated with an article.
     *
     * @param document The document object representing the web page.
     * @return The tags associated with the article.
     */
    String getTags(Document document) {
        return "";
    }

    /**
     * Extracts the author of an article.
     *
     * @param document The document object representing the web page.
     * @return The author of the article.
     */
    String getAuthor(Document document) {
        return "";
    }

    /**
     * Extracts the category of an article.
     *
     * @param document The document object representing the web page.
     * @return The category of the article.
     */
    String getCategory(Document document) {
        return "";
    }

    /**
     * Extracts the creation date of an article and format it to YY/MM/DD.
     *
     * @param document The document object representing the web page.
     * @return The creation date of the article.
     */
    String getCreationDate(Document document) {
        return "";
    }

    /**
     * The private method to scrape all the details of an article (e.g article link, image URLs,...) and
     * return them in ArticleData data structure.
     * @param articleLink The link of the article. 
     * @param imageLink The link of the thumbnail image.
     * @return All the details of an article.
     */
    private ArticleData scrapeArticle(String articleLink, String imageLink) {
    	String summary="", title="", intro="", detailedContent="", tags="",
    			author="", category="", creationDate="";
    	
    	try {
        	Document document = ModelTools.connectWeb(articleLink);
        	if (document == null) return null;
        	
        	summary = getSummary(document);
            title = getTitle(document);
            intro = getIntro(document); 
            detailedContent = getDetailedContent(document);
            tags = getTags(document);
            author = getAuthor(document);
            category = getCategory(document);
            creationDate = getCreationDate(document);
            
            ArticleData articleFeatures = new ArticleData(articleLink, webSource, imageLink, type, summary,
        			title, intro, detailedContent, tags, author, category, creationDate, htmlContentLocation);
            
            System.out.println("Collect data in link successfully");
            
            return articleFeatures;
    	} catch (Exception e) {
    		System.out.println("Scrape article error: " + e.getMessage());
    	}
    	
    	System.out.println("Skip article!");

    	return null;
    }
    
    /**
     * The package method to scrape all the details of an article from a list of all links and thumbnail
     * image URLs in a web and convert them to JSON file.
     */
    void scrapeAllData()
    {
    	List<Pair<String, String>> allLinksImages = getAllLinksAndImages();
    	List<ArticleData> listOfData = Collections.synchronizedList(new ArrayList<>());
    	 
    	ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    
    	for (Pair<String, String> linkAndImage : allLinksImages) {

    		String link = linkAndImage.getKey();
    		String image = linkAndImage.getValue();
    		
    		executor.submit(() -> {
                ArticleData unit = scrapeArticle(link, image);
                if (unit != null) {
                    listOfData.add(unit);
                }
            });
    	}
    	
    	executor.shutdown();
    	while (!executor.isTerminated()) {
            // Waiting...
        }
    	
    	ModelTools.convertDataToJson(listOfData, fileName);
    }
}
