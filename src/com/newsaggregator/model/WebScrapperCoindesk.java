package com.newsaggregator.model;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javafx.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class WebScrapperCoindesk extends WebScrapper {
	WebScrapperCoindesk()
    {
    	webSource = "Coindesk";
    	type = "News Article";
    	htmlContentLocation = ".at-body";
    	fileName += "newsCoindesk.json";
    }
    
    @Override 
    List<Pair<String, String>> getAllLinksAndImages(){
    	List<Pair<String, String>> linkAndImage = new ArrayList<>();
    	
    	try {	
            Document document = ModelTools.connectWeb("https://www.coindesk.com/tag/blockchain-tech/");
            linkAndImage.addAll(getLinkAndImageInPage(document));
            Elements nextElements = document.select(".page-link");

            while (!nextElements.isEmpty()) {
            	Element nextPageLink = nextElements.select("a[aria-label=Next page]").first();
            	if (nextPageLink == null) break;
            	String relativeLink = nextPageLink.attr("href");
           
                if (relativeLink == null || relativeLink.isEmpty()) break;
                String completeLink = "https://www.coindesk.com/" + relativeLink;
                document = ModelTools.connectWeb(completeLink);
                linkAndImage.addAll(getLinkAndImageInPage(document));
                
                nextElements = document.select(".page-link");
            }   
        } catch (IOException e) {
            e.printStackTrace();
        } 
    	return linkAndImage;
    }
    
    @Override 
    List<Pair<String, String>> getLinkAndImageInPage(Document document) {
    	List<Pair<String, String>> linkAndImage = new ArrayList<>();
    	
    	Elements contents = document.getElementsByClass("article-cardstyles__StyledWrapper-sc-q1x8lc-0 eJFoEa article-card default");
    	
    	for (Element content : contents) {
    		Element linkElement = content.selectFirst(".typography__StyledTypography-sc-owin6q-0.bhrWMt");
    		Element imageElement = content.selectFirst(".responsive-picturestyles__ResponsivePictureWrapper-sc-1urqrom-0.iLCXlQ");
    		
    		if (linkElement != null && imageElement != null) {
    			String link = "https://www.coindesk.com" + linkElement.select("a").attr("href");
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
    	Elements content = document.select(".at-headline");
        String title = content.text();
        return title;
    }
    
    @Override 
    String getAuthor(Document document) {
    	Elements content = document.select(".at-authors");
        String author = content.select("a[href]").text();
        return author;
    }

    
    @Override 
    String getDetailedContent(Document document) {
    	Elements contents = document.select(".typography__StyledTypography-sc-owin6q-0.eycWal.at-text");
    	String text = new String();
    	for (Element content : contents) {
    		String paragraph = content.select("p").text();
    		text += paragraph;
    	}
    	return text;
    }

    
    @Override 
    String getCreationDate(Document document) {
    	Elements content = document.select(".at-created.label-with-icon");
    	String creationDate = content.text();
    	return creationDate;
    }
    
    @Override
    String getTags(Document document) {
    	Elements contents = document.select(".article-tagsstyles__TagPill-sc-17t0gri-0.eJTFpe.light");
        String tags = "";
        for (Element content : contents.select("span")) {
        	tags += content.text() + ", ";
        }
        return tags;
    }
    
    @Override
    String getCategory(Document document) {
    	Elements content = document.select(".typography__StyledTypography-sc-owin6q-0.kRnPCl");
        String category = content.select("span").text();
        return category;
    }
    
    @Override 
    String getIntro(Document document) {
    	Elements contents = document.select(".typography__StyledTypography-sc-owin6q-0.sVcXY");
    	String intro = contents.text();
    	return intro;
    }
}

