package com.newsaggregator.model.tools;

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
import com.newsaggregator.model.ArticleData;

public class Converter {
	private static Gson gson = new Gson();
	
	public static List<ArticleData> convertJsonFileToData(String directory) {
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
	
	public static List<ArticleData> convertJsonStringToData(String jsonInput)
	{
		ArticleData[] arrayOfResult = gson.fromJson(jsonInput, ArticleData[].class);
        List<ArticleData> listOfResult = new ArrayList<>(Arrays.asList(arrayOfResult));
        
        return listOfResult;
	}
	
	public static void convertDataToJson(List<ArticleData> inputData, String fileDirectory)
	{
		FileWriter fileWriter = null;
   	 
        try {
        	File file = new File(fileDirectory);
        	file.createNewFile();
        	
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
}
