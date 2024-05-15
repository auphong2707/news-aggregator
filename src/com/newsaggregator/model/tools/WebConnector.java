package com.newsaggregator.model.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class WebConnector {

	private static final List<String> userAgent;
	
	static
    {
    	List<String> tmpList = new ArrayList<String>();
    	try {
			Scanner scanner = new Scanner(new File("user-agents.txt"));
			while (scanner.hasNext()){
			    tmpList.add(scanner.next());
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	finally {
    		userAgent = tmpList;
    	}
    }
	
	public static Document connectWeb(String url) throws IOException, HttpStatusException {
    	Document document = null;
    	
    	for(int tryTime = 1; tryTime <= 20; ++tryTime) {
    		try {	
        		Random random = new Random();
            	String randomUA = userAgent.get(random.nextInt(userAgent.size()));

            	Jsoup.newSession();
    			
            	document = Jsoup.connect(url)
                        .userAgent(randomUA)
                        .referrer("http://www.google.com")
                        .timeout(120000)
                        .get();
        	} catch (Exception e) {
        		System.out.println("Error connecting to URL: " + e.getMessage() + "(" + url + ")");
        		System.out.println("Tried " + tryTime + " times. Attempting to try again.");
        		
        		try {
					Thread.sleep(5000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        	}
    		if(document != null) break;
    	}
    	return document;
    }

}
