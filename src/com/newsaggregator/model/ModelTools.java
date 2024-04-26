package com.newsaggregator.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.google.gson.Gson;

class ModelTools {
	private static Gson gson = new Gson();
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
	
	static Document connectWeb(String url) throws IOException, HttpStatusException {
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
	
	static List<ArticleData> convertJsonToData(String directory) {
		try {
		      File jsonFile = new File(directory);

		      String jsonData = "";
		      
		      Scanner sc = new Scanner(jsonFile);
		      while (sc.hasNextLine()) {
		        String line = sc.nextLine();
		        jsonData += line;
		      }
		      sc.close();
		      
		      return convertJsonStringToData(jsonData);
		      
	    } catch (FileNotFoundException e) {
	      System.out.println("An error occurred.");
	      e.printStackTrace();
	    }
		return null;
	}
	
	static List<ArticleData> convertJsonStringToData(String jsonInput)
	{
		ArticleData[] arrayOfResult = gson.fromJson(jsonInput, ArticleData[].class);
        List<ArticleData> listOfResult = new ArrayList<>(Arrays.asList(arrayOfResult));
        
        return listOfResult;
	}
	
	protected static void convertDataToJson(List<ArticleData> inputData, String fileDirectory)
	{
		FileWriter fileWriter = null;
   	 
        try {
            fileWriter = new FileWriter(fileDirectory);
 
            Gson gson = new Gson();
            gson.toJson(inputData, fileWriter);
 
            System.out.println("JSON file was created successfully !!!");
 
        } catch (Exception e) {
            System.out.println("Error in JSON File Writer !!!");
            e.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
            }
        }
	}
	
	static <T> List<T> randomSubList(List<T> list, int newSize) {
	    list = new ArrayList<>(list);
	    Collections.shuffle(list);
	    return list.subList(0, newSize);
	}
}
