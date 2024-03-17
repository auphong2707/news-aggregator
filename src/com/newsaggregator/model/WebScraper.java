package com.newsaggregator.model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WebScraper {
    private final String articleLink = "https://www.ft.com/blockchain";
    private final String[] userAgent = {
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.5112.79 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36 Edge/18.19582",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:101.0) Gecko/20100101 Firefox/101.0",
            "Mozilla/5.0 (Windows; U; Windows NT 6.1; rv:2.2) Gecko/20110201",
            "Opera/9.80 (X11; Linux i686; Ubuntu/14.10) Presto/2.12.388 Version/12.16.2"
    };
    
    private Document webConnector(String url, String userAgent) throws IOException {
        return Jsoup.connect(url)
                .userAgent(userAgent)
                .referrer("http://www.google.com")
                .timeout(12000)
                .get();
    }
    
    private void getTitle(Document document, List<String> articleTitles) {
        Elements contents = document.select(".o-teaser__heading");
        for (Element content : contents) {
            String titleArticle = content.select("a[href]").text();
            articleTitles.add(titleArticle);
        }
    }
    
    private void getLink(Document document, List<String> articleLinks) {
    	Elements contents = document.select(".o-teaser__heading");
        for (Element content : contents) {
            Element linkArticle = content.selectFirst("a.js-teaser-heading-link");
            String linkHref = "https://www.ft.com" + linkArticle.attr("href");
            articleLinks.add(linkHref);
        }
    }
    
    private void getIntro(Document document, List<String> articleIntros) {
    	Elements contents = document.select(".o-teaser__standfirst");
        for (Element content : contents) {
            String introArticle = content.select("a[href]").text();
            articleIntros.add(introArticle);
        }
    }
    public void scrapeArticles() {
        try {
            List<String> articleLinks = new ArrayList<>();
            List<String> articleTitles = new ArrayList<>();
            List<String> articleIntros = new ArrayList<>();
            
            Random r = new Random();
            Document document = webConnector(articleLink, userAgent[r.nextInt(userAgent.length)]);
            Elements nextElements = document.select(".stream__pagination.o-buttons-pagination");

            while (!nextElements.isEmpty()) {
                Element nextPageLink = document.selectFirst("a.o-buttons.o-buttons--secondary.o-buttons-icon.o-buttons-icon--arrow-right.o-buttons-icon--icon-only");
                String relativeLink = nextPageLink.attr("href");
                if (relativeLink == null || relativeLink.isEmpty()) break;
                String completeLink = articleLink + relativeLink;

                document = webConnector(completeLink, userAgent[r.nextInt(userAgent.length)]);
                getTitle(document, articleTitles);
                getLink(document, articleLinks);
                getIntro(document, articleIntros);

                nextElements = document.select(".stream__pagination.o-buttons-pagination");
            }

            System.out.println(articleTitles);
            System.out.println(articleLinks);
            System.out.println(articleIntros);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
