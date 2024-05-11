package com.newsaggregator.model;

import java.io.IOException;

import org.jsoup.HttpStatusException;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

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
	
	ArticleData(String link, String websiteSource, String image,
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
	
	public String getLINK() {
		return LINK;
	}

	public String getWEBSITE_SOURCE() {
		return WEBSITE_SOURCE;
	}

	public String getIMAGE() {
		return IMAGE;
	}

	public String getTYPE() {
		return TYPE;
	}

	public String getSUMMARY() {
		return SUMMARY;
	}

	public String getTITLE() {
		return TITLE;
	}

	public String getINTRO() {
		return INTRO;
	}

	public String getDETAILED_CONTENT() {
		return DETAILED_CONTENT;
	}

	public String getTAGS() {
		return TAGS;
	}

	public String getAUTHOR_NAME() {
		return AUTHOR_NAME;
	}

	public String getCATEGORY() {
		return CATEGORY;
	}

	public String getCREATION_DATE() {
		return CREATION_DATE;
	}
	
	public String getHTML_CONTENT() {
		Document document = null;
		try {
			document = ModelTools.connectWeb(LINK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (document == null) return "";
		
		Elements contents = document.select(HTML_CONTENT_LOCATION);
    	if (contents == null) return "";
	    
    	String htmlContent = contents.html().replaceAll("<img ", 
    			"<img onerror=\"this.style.display='none'\" ");
    	
	    return htmlContent;
	}
}
