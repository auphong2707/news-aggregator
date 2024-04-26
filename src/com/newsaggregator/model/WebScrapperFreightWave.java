package com.newsaggregator.model;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javafx.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class WebScrapperFreightWave extends WebScrapper {
	WebScrapperFreightWave()
    {
    	webSource = "Freight Wave";
    	type = "News Article";
    	fileName += "newsFreightWave.json";
    }
    
    @Override 
    List<Pair<String, String>> getAllLinksAndImages(){
    	List<Pair<String, String>> linkAndImage = new ArrayList<>();
    	
    	try {	
            Document document = ModelTools.connectWeb("https://www.freightwaves.com/blockchain");
            linkAndImage.addAll(getLinkAndImageInPage(document));
            Elements nextElements = document.select(".fw-pagination");

            while (nextElements != null) {
            	Element linkElement = null;
            	for (Element element : nextElements.select(".fw-pagination-item")) {
                	if ("»".equals(element.text())) {
                		linkElement = element;
                	}
                }
            	if (linkElement == null) break;
            	Element nextPageLink = linkElement.select("a").first();
            	if (nextPageLink == null) break;
            	String link = nextPageLink.attr("href");
                if (link == null || link.isEmpty()) break;
                
                document = ModelTools.connectWeb(link);
                linkAndImage.addAll(getLinkAndImageInPage(document));
                
                nextElements = document.select(".fw-pagination");
            }   
        } catch (IOException e) {
            e.printStackTrace();
        } 
    	return linkAndImage;
    }
    
    @Override 
    List<Pair<String, String>> getLinkAndImageInPage(Document document) {
    	List<Pair<String, String>> linkAndImage = new ArrayList<>();
    	
    	Elements contents = document.getElementsByClass("grid sm:grid-cols-2 gap-x-25 gap-y-10 sm:gap-25 mb-25");
    	
    	for (Element content : contents) {
    		Element linkElement = content.selectFirst(".fw-block-post-item-blue");
    		Element imageElement = content.selectFirst(".aspect-w-16.aspect-h-9.overflow-hidden");
    		
    		if (linkElement != null && imageElement != null) {
    			String link = linkElement.select("a.fw-block-post-item-blue").attr("href");
    			String imageLink = imageElement.select("img").attr("src");
    			Pair<String, String> tmp = new Pair<String, String>(link, imageLink);
        		linkAndImage.add(tmp);
    		}
        }
    	System.out.println("Collect links and images in page successfully");
    	return linkAndImage;
    }

    @Override 
    String getTitle(Document document) {
    	Elements content = document.select(".entry-title.post-title.text-28.mb-15");
        String title = content.text();
        return title;
    }
    
    @Override 
    String getAuthor(Document document) {
    	Elements content = document.select(".meta-author.inline-block.mr-5");
        String author = content.select("a[href]").text();
        return author;
    }

    
    @Override 
    String getDetailedContent(Document document) {
    	Elements contents = document.select(".entry-content.mb-30");
    	String text = new String();
    	for (Element content : contents) {
    		String paragraph = content.select("p").text();
    		text += paragraph;
    	}
    	return text;
    }

    
    @Override 
    String getCreationDate(Document document) {
    	Elements content = document.select(".entry-date.published.updated");
    	String creationDate = content.attr("datetime");
    	return creationDate;
    }
    
    @Override
    String getCategory(Document document) {
    	Elements contents = document.select(".entry-category-cloud");
        String categories = "";
        for (Element content : contents.select("span")) {
        	categories += content.text() + ", ";
        }
        return categories;
    }
    
    @Override
    String getTags(Document document) {
    	Elements contents = document.select(".entry-tag-cloud.text-center");
        String tags = "";
        for (Element content : contents.select("a[href]")) {
        	tags += content.text() + ", ";
        }
        return tags;
    }
    
    @Override
    String getHtmlContent(Document document) {
    	Elements contents = document.select(".entry-content.mb-30");
    	if (contents != null) {
    		return contents.html();
    	}
	    return "";
    }
    
    @Override 
    String getIntro(Document document) {
    	Elements contents = document.select(".entry-sub-title.font-sans.text-gray-400.font-normal.text-18.leading-23.mb-15");
    	String intro = contents.text();
    	return intro;
    }
}

