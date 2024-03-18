package com.newsaggregator.model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class ArticleData {
	private final String LINK;
	private final String WEBSITE_SOURCE;
	private final String TYPE;
	private final String SUMMARY;
	private final String TITLE;
	private final String DETAILED_CONTENT;
	private final String TAGS;
	private final String AUTHOR_NAME;
	private final String CATEGORY;
	
	public ArticleData(String link, String websiteSource,
			String type, String summary, String title,
			String detailedContent, String tags,
			String authorName, String category) {
		super();
		LINK = link;
		WEBSITE_SOURCE = websiteSource;
		TYPE = type;
		SUMMARY = summary;
		TITLE = title;
		DETAILED_CONTENT = detailedContent;
		TAGS = tags;
		AUTHOR_NAME = authorName;
		CATEGORY = category;
	}
	
	protected String getDataByType(DataType type) throws Exception {
		switch(type) {
		case LINK:
			return LINK;
		case WEBSITE_SOURCE:
			return WEBSITE_SOURCE;
		case TYPE:
			return TYPE;
		case SUMMARY:
			return SUMMARY;
		case TITLE:
			return TITLE;
		case DETAILED_CONTENT:
			return DETAILED_CONTENT;
		case TAGS:
			return TAGS;
		case AUTHOR_NAME:
			return AUTHOR_NAME;
		case CATEGORY:
			return CATEGORY;
		default:
			throw new Exception("Invalid Type!");
		}
	}
	
}
