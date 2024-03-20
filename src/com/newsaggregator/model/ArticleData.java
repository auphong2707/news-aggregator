package com.newsaggregator.model;

public class ArticleData {
	private final String LINK;
	private final String WEBSITE_SOURCE;
	private final String TYPE;
	private final String SUMMARY;
	private final String TITLE;
	private final String INTRO;
	private final String DETAILED_CONTENT;
	private final String TAGS;
	private final String AUTHOR_NAME;
	private final String CATEGORY;
	private final String CREATION_DATE;
	
	protected ArticleData(String link, String websiteSource,
			String type, String summary, String title, String intro,
			String detailedContent, String tags,
			String authorName, String category, String creationDate) {
		super();
		LINK = link;
		WEBSITE_SOURCE = websiteSource;
		TYPE = type;
		SUMMARY = summary;
		TITLE = title;
		INTRO = intro;
		DETAILED_CONTENT = detailedContent;
		TAGS = tags;
		AUTHOR_NAME = authorName;
		CATEGORY = category;
		CREATION_DATE = creationDate;
	}
	
	protected String getDataByType(DataType type) {
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
		case INTRO:
			return INTRO;
		case DETAILED_CONTENT:
			return DETAILED_CONTENT;
		case TAGS:
			return TAGS;
		case AUTHOR_NAME:
			return AUTHOR_NAME;
		case CATEGORY:
			return CATEGORY;
		case CREATION_DATE:
			return CREATION_DATE;
		default:
			return "";
		}
	}
}
