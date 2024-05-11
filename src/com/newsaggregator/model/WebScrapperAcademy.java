package com.newsaggregator.model;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class WebScrapperAcademy extends WebScrapper {
	WebScrapperAcademy()
	{
		webSource = "Academy Moralis";
    	type = "Blog";
    	htmlContentLocation = ".styles_postInnerArticle__6XDZ9";
    	fileName += "newsAcademy.json";
	}
	
	@Override 
	List<Pair<String, String>> getAllLinksAndImages(){
		List<Pair<String, String>> linkAndImage = new ArrayList<>();
		
    	try {	
            Document document = ModelTools.connectWeb("https://academy.moralis.io/blog/blockchain");
            Elements nextElements = document.select(".page-numbers.next");
            linkAndImage.addAll(getLinkAndImageInPage(document));
            
            while (!nextElements.isEmpty()) {
                Element nextPageLink = nextElements.first();
                Element linkElement = nextPageLink.getElementsByTag("a").first();
                if (linkElement == null || linkElement == null) break;
                String relativeLink = linkElement.attr("href");
                document = ModelTools.connectWeb(relativeLink);
                linkAndImage.addAll(getLinkAndImageInPage(document));;
                nextElements = document.select(".page-numbers.next");
            }   
        } catch (IOException e) {
            e.printStackTrace();
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
    	if (titleElement != null) {
    		String title = titleElement.text();
    		return title;
    	}
        return "";
    }
    
    @Override 
    String getAuthor(Document document) {
    	Elements contents = document.select(".typography_subtitle2__HAAtd.styles_postArticleAuthorInfo__XNgVX");
    	Elements author = contents.select("span");
    	String allAuthor = "";
    	if (author.text().contains("WRITTEN BY ")){
    		allAuthor = author.text().replace("WRITTEN BY ", "");
       }
        return allAuthor;
    }
  
    @Override 
    String getDetailedContent(Document document) {
    	Elements contents = document.select(".styles_postArticle__Hbggq");
		String text = new String();
    	for (Element content : contents) {
    		String paragraph = content.select("p").text();
    		text += paragraph;
    	}
    	return text;
    }
    
    @Override 
    String getCreationDate(Document document) {
    	Elements contents = document.select(".styles_lastUpdated__pOFs8.caption-12-capitalize");
		for (Element content : contents) {
			String date = content.select("p").text();
			date = date.replace("Updated ", "");
			date = date.replaceAll("(?<=\\d)(st|nd|rd|th)", "");
	        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy 'at' HH:mm");   
	        LocalDateTime dateTime = LocalDateTime.parse(date, dateFormatter);
	        
	        DateTimeFormatter dateToString = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	        String dateString = dateTime.format(dateToString);
			return dateString;
		}
    	return "";
    }
    
    @Override 
    String getIntro(Document document) {
    	String intro = document.select(".styles_description__QQdxm.body-14-regular").select("p").text();
		return intro;
    }
}
