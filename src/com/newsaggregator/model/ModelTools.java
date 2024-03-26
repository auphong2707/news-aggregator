package com.newsaggregator.model;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;

public class ModelTools {
	private static Gson gson = new Gson();
	
	protected static List<ArticleData> convertJsonToData(String jsonInput)
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
}
