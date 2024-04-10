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
		List<Pair<String, String>> linkAndImage = new ArrayList<>();
    	try {	
            Random r = new Random();
            Document document = connectWeb("https://academy.moralis.io/blog/blockchain", userAgent.get(r.nextInt(userAgent.size())));
            Elements nextElements = document.select(".page-numbers.next");
            linkAndImage.addAll(getLinkAndImageInPage(document));
            
            while (!nextElements.isEmpty()) {
                Element nextPageLink = nextElements.first();
                Element linkElement = nextPageLink.getElementsByTag("a").first();
                if (linkElement == null || linkElement == null) break;
                String relativeLink = linkElement.attr("href");
                document = connectWeb(relativeLink, userAgent.get(r.nextInt(userAgent.size())));
                linkAndImage.addAll(getLinkAndImageInPage(document));;
                nextElements = document.select(".page-numbers.next");
            }   
        } catch (IOException e) {
            e.printStackTrace();
        }
    	
    	return linkAndImage;
    }

    @Override
    protected List<Pair<String, String>> getLinkAndImageInPage(Document document) {
    	List<String> articleLinks = new ArrayList<String>();
    	List<String> articleImages = new ArrayList<String>();
    	List<Pair<String, String>> linkAndImage = new ArrayList<>();
    	
    	Elements postContainers = document.getElementsByClass("elementor-post");
    	Elements contents = postContainers.select("article");

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
    protected String getTitle(Document document) {
    	Element titleElement = document.select(".headline-h2").first();
    	if (titleElement != null) {
    		String title = titleElement.text();
    		return title;
    	}
        return "";
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
    
    @Override
    protected String getIntro(Document document) {
    	String intro = document.select(".styles_description__QQdxm.body-14-regular").select("p").text();
		return intro;
    }
    
    @Override
    protected String getHtmlContent(Document document) {
    	Elements htmlContent = document.select(".styles_postInnerArticle__6XDZ9");
		if (htmlContent != null) {
			return htmlContent.html();
		}
		return "";
    }
}
