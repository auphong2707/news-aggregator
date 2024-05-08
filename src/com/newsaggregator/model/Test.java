package com.newsaggregator.model;

import java.io.IOException;

import org.jsoup.Jsoup;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.HttpStatusException;
import javafx.util.Pair;


public class Test {
	public static void main(String[] args) throws IOException, HttpStatusException{   	
		Model.getInstance();
		System.out.println("Latest:");
		for(ArticleData d : Model.getInstance().getLatest(5)) {
			System.out.println(d.getTITLE());
		}
		System.out.println("Random:");
		for(ArticleData d : Model.getInstance().getRandom(5)) {
			System.out.println(d.getTITLE());
		}
		System.out.println("Trending:");
		for(ArticleData d : Model.getInstance().getTrending(5)) {
			System.out.println(d.getTITLE());
		}
	}
}
        
     
