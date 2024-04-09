package com.newsaggregator.model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.util.Pair;

public class WebScrapperAcademy extends WebScrapper {
	public WebScrapperAcademy()
	{
		webSource = "Academy Moralis";
    	type = "Blog";
    	fileName += "newsAcademy.json";
	}
	
	@Override
    protected List<Pair<String, String>> getAllLinksAndImages(){
    	List<String> allLinks = new ArrayList<String>();
    	List<String> allImages = new ArrayList<String>();
    	try {	
            Random r = new Random();
            Document document = connectWeb("https://academy.moralis.io/blog/blockchain", userAgent.get(r.nextInt(userAgent.size())));
            Elements nextElements = document.select(".page-numbers.next");
            allLinks.addAll(getLinkInPage(document));
            allImages.addAll(getImageInPage(document));
            
            while (!nextElements.isEmpty()) {
                Element nextPageLink = nextElements.first();
                Element linkElement = nextPageLink.getElementsByTag("a").first();
                if (linkElement == null || linkElement == null) break;
                String relativeLink = linkElement.attr("href");
                document = connectWeb(relativeLink, userAgent.get(r.nextInt(userAgent.size())));
                allLinks.addAll(getLinkInPage(document));
                allImages.addAll(getImageInPage(document));
                nextElements = document.select(".page-numbers.next");
            }   
        } catch (IOException e) {
            e.printStackTrace();
        }
    	List<Pair<String, String>> linkAndImage = new ArrayList<>();
    	for (int i = 0; i < allLinks.size(); i++) {
    		Pair<String, String> tmp = new Pair<String, String>(allImages.get(i), allLinks.get(i));
    		linkAndImage.add(tmp);
    	}
    	return linkAndImage;
    }
	
    
    @Override
    protected List<String> getLinkInPage(Document document) {
    	List<String> articleLinks = new ArrayList<String>();
    	
    	Elements contents = document.select(".elementor-post__card");
        for (Element content : contents) {
            Element linkArticle = content.selectFirst("a.elementor-post__thumbnail__link");
            if (linkArticle != null) {
            	String linkHref = linkArticle.attr("href");
                articleLinks.add(linkHref);
            }
        }
        System.out.println("Collect links in page successfully");
        return articleLinks;
    }
    
    @Override
    protected List<String> getImageInPage(Document document) {
    	List<String> articleImage = new ArrayList<String>();
    	
    	Elements images = document.select(".elementor-post__thumbnail");
		for (Element image : images) {
			String imageLink = image.select("img").first().attr("src");
			articleImage.add(imageLink);
		}
        return articleImage;
    }

    @Override
    protected String getTitle(Document document) {
    	String title = document.select(".headline-h2").first().text();
        return title;
    }
    
    @Override
    protected String getAuthor(Document document) {
    	Elements contents = document.select(".typography_subtitle2__HAAtd.styles_postArticleAuthorInfo__XNgVX");
    	Elements author = contents.select("span");
    	String allAuthor = "";
    	if (author.text().contains("WRITTEN BY ")){
    		allAuthor = author.text().replace("WRITTEN BY ", "");
       }
        return allAuthor;
    }
  
    @Override
    protected String getDetailedContent(Document document) {
    	Elements contents = document.select(".styles_postArticle__Hbggq");
		String text = new String();
    	for (Element content : contents) {
    		String paragraph = content.select("p").text();
    		text += paragraph;
    	}
    	return text;
    }
    
    @Override
    protected String getCreationDate(Document document) {
    	Elements contents = document.select(".styles_lastUpdated__pOFs8.caption-12-capitalize");
		for (Element content : contents) {
			String date = content.select("p").text();
			if (date.contains("Updated ")){
				date.replace("Updated ", "");
			}
			return date;
		}
    	return "";
    }
}
