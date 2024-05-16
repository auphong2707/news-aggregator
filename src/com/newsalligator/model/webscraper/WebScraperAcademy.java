package com.newsalligator.model.webscraper;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.newsalligator.model.tools.WebConnector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

final class WebScraperAcademy extends WebScraper {
	WebScraperAcademy()
	{
		webSource = "Academy Moralis";
    	type = "Blog";
    	htmlContentLocation = ".styles_postInnerArticle__6XDZ9";
    	fileName += "newsAcademy.json";
	}
	
	@Override 
	List<Pair<String, String>> getAllLinksAndImages(){
		List<Pair<String, String>> linkAndImage = new ArrayList<>();
		
        Document document = WebConnector.connectWeb("https://academy.moralis.io/blog/blockchain");
        Elements nextElements = document.select(".page-numbers.next");
        linkAndImage.addAll(getLinkAndImageInPage(document));
        
        while (!nextElements.isEmpty()) {
            Element nextPageLink = nextElements.first();
            Element linkElement = nextPageLink.getElementsByTag("a").first();
            if (linkElement == null || linkElement == null) break;
            String relativeLink = linkElement.attr("href");
            document = WebConnector.connectWeb(relativeLink);
            linkAndImage.addAll(getLinkAndImageInPage(document));;
            nextElements = document.select(".page-numbers.next");
        }   
    	
    	return linkAndImage;
    }

    @Override 
    List<Pair<String, String>> getLinkAndImageInPage(Document document) {
    	List<Pair<String, String>> linkAndImage = new ArrayList<>();
    	
    	Elements postContainers = document.getElementsByClass("elementor-post");
    	for (Element content : postContainers) {
    		Element linkElement = content.selectFirst(".elementor-post__thumbnail__link");
    		Element imageElement = content.selectFirst(".elementor-post__thumbnail");
    		if (linkElement != null && imageElement != null) {
    			String link = content.select("a").attr("href");
    			String imageLink = content.select("img").attr("src");
    			Pair<String, String> tmp = new Pair<String, String>(link, imageLink);
        		linkAndImage.add(tmp);
    		}
		}
    	System.out.println("Collect links and images in page successfully");
    	return linkAndImage;
    }

    @Override 
    String getTitle(Document document) {
    	Element titleElement = document.select(".headline-h2").first();
    	String title = "";
    	if (titleElement != null) {
    		title = titleElement.text();
    		return title;
    	}
    	else {
    		titleElement = document.select(".elementor-element.elementor-element-4c72f30e.elementor-widget__width-initial.elementor-widget-tablet__width-inherit.elementor-widget.elementor-widget-theme-post-title.elementor-page-title.elementor-widget-heading").first();
    		title = titleElement.text();
    		return title;
    	}
    }
    
    @Override 
    String getAuthor(Document document) {
    	String allAuthor = "";
    	Elements contents = document.select(".typography_subtitle2__HAAtd.styles_postArticleAuthorInfo__XNgVX");
    	if (contents.isEmpty() != true) {
    		Elements author = contents.select("span");
        	if (author.text().contains("WRITTEN BY ")){
        		allAuthor = author.text().replace("WRITTEN BY ", "");
        	}
        	return allAuthor;
    	}
    	else {
    		contents = document.select(".elementor-element.elementor-element-b3d7692.elementor-widget.elementor-widget-heading");
    		allAuthor = contents.text();
    		if (allAuthor.contains("Written by ")){
        		allAuthor = allAuthor.replace("Written by ", "");
    		}
    		if (allAuthor.isEmpty() == true) {
    			allAuthor = "Anonymous";
    		}
    		return allAuthor;
    	}
    	
    }
  
    @Override 
    String getDetailedContent(Document document) {
    	Elements contents = document.select(".styles_postArticle__Hbggq");
		String text = new String();
		if (contents.isEmpty() == false) {
			for (Element content : contents) {
	    		String paragraph = content.select("p").text();
	    		text += paragraph;
	    	}
	    	return text;
		}
		else {
			contents = document.select(".elementor-element.elementor-element-22101c7c.elementor-widget-tablet__width-inherit.postContent.elementor-widget.elementor-widget-theme-post-content");
			for (Element content : contents) {
	    		String paragraph = content.select("p").text();
	    		text += paragraph;
	    	}
			return text;
		}	
    }
    
    @Override 
    String getCreationDate(Document document) {
    	Elements contents = document.select(".styles_lastUpdated__pOFs8.caption-12-capitalize");
    	String date = "";
    	if (contents.isEmpty() != true) {
    		for (Element content : contents) {
    			date = content.select("p").text();
    			date = date.replace("Updated ", "");
    			date = date.replaceAll("(?<=\\d)(st|nd|rd|th)", "");
    	        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy 'at' HH:mm");   
    	        LocalDateTime dateTime = LocalDateTime.parse(date, dateFormatter);
    	        
    	        DateTimeFormatter dateToString = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    	        String dateString = dateTime.format(dateToString);
    			return dateString;
    		}
    	}
    	else {
    		contents = document.select(".elementor-element.elementor-element-773cabf.elementor-widget.elementor-widget-heading");
    		date = contents.text();
    		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
    		LocalDate datetime = LocalDate.parse(date, dateFormatter);
    
            DateTimeFormatter dateToString = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            String dateString = datetime.format(dateToString);
            return dateString;
    	}
    	return "";
    }
    
    @Override 
    String getIntro(Document document) {
    	Elements contents = document.select(".styles_description__QQdxm.body-14-regular");
    	String intro = "";
    	if (contents.isEmpty() != true) {
    		intro = contents.select("p").text();
    		return intro;
    	}
    	else {
    		contents = document.select(".elementor-element.elementor-element-2b30972.elementor-widget.elementor-widget-heading");
    		intro = contents.select("p").text();
    		return intro;
    	}
    }
}
