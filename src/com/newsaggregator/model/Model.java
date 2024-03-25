package com.newsaggregator.model;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;

public class Model {
	private static WebScrapper[] scrapers;
	static
	{
		scrapers = new WebScrapper[] {
			new WebScrapperFT()
		};
	}
	
	public void scrapeNewData()
	{
		for(WebScrapper scraper : scrapers)
		{
			scraper.scrapeAllData();
		}
		// Combine all JSON files into one: Incomplete
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
            
            Gson gson = new Gson();
            ArticleData[] arrayOfResult = gson.fromJson(output, ArticleData[].class);
            List<ArticleData> listOfResult = new ArrayList<>(Arrays.asList(arrayOfResult));
            
            return listOfResult;
            
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
}
