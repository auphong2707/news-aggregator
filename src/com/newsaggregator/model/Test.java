package com.newsaggregator.model;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Test {
	public static void main(String[] args) {
		FileWriter fileWriter = null;
		String fileName = "data/newsFT.csv";
		List<String> listOfData = new ArrayList<String>();
		listOfData.add("1234");
		listOfData.add("4155");
		
        try {
            fileWriter = new FileWriter(fileName);
 
            // Write the CSV file header
            fileWriter.append(ArticleData.HEADER);
 
            // Write a new ArticleData object list to the CSV file
            for (String unit : listOfData) {
            	fileWriter.append(unit);
            }
 
            System.out.println("CSV file was created successfully !!!");
 
        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
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
