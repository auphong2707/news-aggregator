package com.newsalligator.model;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.newsalligator.model.tools.WebConnector;
/**
 * <h1> ArticleData </h1>
 * The {@code ArticleData} class is a class for storing and 
 * preprocessing data related to an article. 
 * @author Phong Au, Quan Tran
 */
public class ArticleData {
	private final String LINK;
	private final String WEBSITE_SOURCE;
	private final String IMAGE;
	private final String TYPE;
	private final String SUMMARY;
	private final String TITLE;
	private final String INTRO;
	private final String DETAILED_CONTENT;
	private final String TAGS;
	private final String AUTHOR_NAME;
	private final String CATEGORY;
	private final String CREATION_DATE;
	private final String HTML_CONTENT_LOCATION;
	
	/**
	 * Creates an {@code ArticleData} object with desired details from an article.
	 * @param link the link of the article
	 * @param websiteSource the source web of the article
	 * @param image the thumbnail of the article
	 * @param type the type of the article (blogs, article news, tweets,...)
	 * @param summary the summary of the article
	 * @param title the title of the article
	 * @param intro the introduction of the article
	 * @param detailedContent the detail content of the article 
	 * @param tags the tags of the article
	 * @param authorName the name of the author of the article
	 * @param category the category of the article
	 * @param creationDate the create date of the article
	 * @param htmlContentLocation the fetched HTML content location of the web
	 */
	public ArticleData(String link, String websiteSource, String image,
			String type, String summary, String title, String intro,
			String detailedContent, String tags,
			String authorName, String category, String creationDate, String htmlContentLocation) {
		super();
		LINK = link;
		WEBSITE_SOURCE = websiteSource;
		IMAGE = image;
		TYPE = type;
		SUMMARY = summary;
		TITLE = title;
		INTRO = intro;
		DETAILED_CONTENT = detailedContent;
		TAGS = tags;
		AUTHOR_NAME = authorName;
		CATEGORY = category;
		CREATION_DATE = creationDate;
		HTML_CONTENT_LOCATION = htmlContentLocation;
	}
	
	/**
	 * Extracts the link (URL) of the article.
	 * @return the link of the article
	 */
	public String getLINK() {
		return LINK;
	}
	
	/**
	 * Extracts the source web of the article.
	 * @return the source web of the article
	 */
	public String getWEBSITE_SOURCE() {
		return WEBSITE_SOURCE;
	}
	
	/**
	 * Extracts the thumbnail image of the article.
	 * @return the thumbnail image of the article
	 */
	public String getIMAGE() {
		return IMAGE;
	}
	
	/**
	 * Extracts the type (article, blog, tweets,...) of the article.
	 * @return the type of the article
	 */
	public String getTYPE() {
		return TYPE;
	}
	
	/**
	 * Extracts the summary the article.
	 * @return the summary of the article
	 */
	public String getSUMMARY() {
		return SUMMARY;
	}
	
	/**
	 * Extracts the title of the article.
	 * @return the title of the article
	 */
	public String getTITLE() {
		return TITLE;
	}
	
	/**
	 * Extracts the introduction of the article.
	 * @return the introduction of the article
	 */
	public String getINTRO() {
		return INTRO;
	}
	
	/**
	 * Extracts the detailed content of the article.
	 * @return the detailed content of the article
	 */
	public String getDETAILED_CONTENT() {
		return DETAILED_CONTENT;
	}
	
	/**
	 * Extracts the tags of the article.
	 * @return the tags of the article
	 */
	public String getTAGS() {
		return TAGS;
	}
	
	/**
	 * Extracts the author name of the article.
	 * @return the author name of the article
	 */
	public String getAUTHOR_NAME() {
		return AUTHOR_NAME;
	}
	
	/**
	 * Extracts the category of the article.
	 * @return the category of the article
	 */
	public String getCATEGORY() {
		return CATEGORY;
	}
	
	/**
	 * Extracts the creation date of the article.
	 * @return the creation date of the article
	 */
	public String getCREATION_DATE() {
		return CREATION_DATE;
	}
	
	/**
	 * Extracts the HTML of the article. The HTML is processed to meet required conditions. 
	 * @return the processed HTML of the article
	 */
	public String getHTML_CONTENT() {
		Document document = null;
		try {
			document = WebConnector.connectWeb(LINK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (document == null) return "";
		
		Elements contents = document.select(HTML_CONTENT_LOCATION);
    	if (contents == null) return "";
    	
    	if (WEBSITE_SOURCE.equals("The Fintech Times")) {
    		contents.select(".pp-multiple-authors-wrappermultiple-authors-target-the-content"
    				+ ".pp-multiple-authors-layout-boxed").remove();
    		contents.select(".wp-caption.alignleft").remove();
    		contents.select(".wp-caption.alignright").remove();
    		contents.select(".alignnone").remove();	
    	} else if (WEBSITE_SOURCE.equals("Financial Express")) {
    		contents.select(".parent_also_read").remove();
    		contents.select(".wp-block-newspack-blocks-wp-block-newspack-ads-blocks-ad-unit").remove();
    		contents.select(".wp-block-ie-network-blocks-also-read").remove();
    	} else if (WEBSITE_SOURCE.equals("Freight Wave")) {
    		contents.select(".essb_links_list").remove();
    	}
    		
    	String htmlContent = contents.html().replaceAll("<img ", 
    			"<img onerror=\"this.style.display='none'\" ");
    	
	    return htmlContent;
	}
}
