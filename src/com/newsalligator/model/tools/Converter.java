package com.newsalligator.model.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;
import com.newsalligator.model.ArticleData;

/**
 * <h1> Converter </h1>
 * The {@code Converter} class is a class to convert JSON data file to 
 * {@code ArticleData} object and vice versa.
 * @author Phong Au
 */
public class Converter {
	private static Gson gson = new Gson();
	
	/**
	 * Converts JSON file in a directory into a list of {@code ArticleData} objects.
	 * @param directory the directory of the JSON file
	 * @return a list of {@code ArticleData} objects
	 */
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
	
	/**
	 * Converts JSON string to a list of {@code ArticleData} objects.
	 * @param jsonInput the JSON string to convert
	 * @return a list of {@code ArticleData} objects 
	 */
	public static List<ArticleData> convertJsonStringToData(String jsonInput)
	{
		ArticleData[] arrayOfResult = gson.fromJson(jsonInput, ArticleData[].class);
        List<ArticleData> listOfResult = new ArrayList<>(Arrays.asList(arrayOfResult));
        
        return listOfResult;
	}
	
	/**
	 * Converts a list of {@code ArticleData} objects to JSON file.
	 * @param inputData the list of {@code ArticleData} objects to convert
	 * @param fileDirectory the directory the JSON file is saved
	 */
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
