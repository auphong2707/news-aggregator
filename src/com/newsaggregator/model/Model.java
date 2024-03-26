package com.newsaggregator.model;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;

public class Model {
	private static final WebScrapper[] scrapers;
	private long processPid = -1;
	
	static
	{
		scrapers = new WebScrapper[] {
			new WebScrapperFT()
		};
	}
	
	public void runLocalServer() throws IOException, InterruptedException
	{
		if (processPid == -1)
		{
			String directory = System.getProperty("user.dir") + "\\src\\com\\newsaggregator\\model\\DataAnalyzer.py";
			String command = "python " + directory;
			
			Process server = Runtime.getRuntime().exec(command);
			server.waitFor(2000, TimeUnit.MILLISECONDS);
			
			processPid = server.pid();
		}
	}
	
	public void terminateLocalServer() throws IOException
	{
		if (processPid != -1)
		{
			String command = "taskkill /F /T /PID " + processPid;
			Runtime.getRuntime().exec(command);
		}
	}
	
	public void scrapeNewData()
	{
		for(WebScrapper scraper : scrapers)
		{
			scraper.scrapeAllData();
		}
		combineData();
	}
	
	public List<ArticleData> search(String inputContent)
	{
		HttpURLConnection conn = null;
        DataOutputStream os = null;
        
        try{
            URL url = new URL("http://127.0.0.1:5000/search/"); //important to add the trailing slash after add
            String input = "{\"content\": \"" + inputContent + "\"}";
            byte[] postData = input.getBytes(StandardCharsets.UTF_8);
            
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty( "charset", "utf-8");
            conn.setRequestProperty("Content-Length", Integer.toString(input.length()));
            
            os = new DataOutputStream(conn.getOutputStream());
            os.write(postData);
            os.flush();
            
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }
            
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
            String output = br.readLine();

            conn.disconnect();
            
            return ModelTools.convertJsonToData(output);
            
	    } 
        catch (MalformedURLException e) {
	        e.printStackTrace();
	    } 
        catch (IOException e){
	        e.printStackTrace();
	    } 
        finally {
            if(conn != null)
            {
                conn.disconnect();
            }
        }
		return null;
	}
	
	public void combineData() {
		String directory = "data/";
		String resultFileName = "newsAll.json";
		String[] arrayOfFileNames = new String[] {
			"newsFT.json", "newsCONV.json"	
		};
		
		try {
			List<ArticleData> listOfData = new ArrayList<ArticleData>();
			for(String fileName : arrayOfFileNames)
			{
				Scanner scanner = new Scanner(new File(directory + fileName));
				List<ArticleData> unitData = ModelTools.convertJsonToData(scanner.nextLine());
				
				listOfData.addAll(unitData);
			}
			ModelTools.convertDataToJson(listOfData, directory + resultFileName);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
