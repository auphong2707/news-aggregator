package com.newsalligator.model;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.newsalligator.model.tools.Converter;
import com.newsalligator.model.webscraper.WebScraper;

public class Model {
	private final static String DIRECTORY = "data/";
	private final static String RESULT_FILE_NAME = "newsAll.json";
	private static WebScraper[] scrapers;
	
	private static long processPid = -1;
	
	private static Model instance;
    private Model() {
    	runLocalServer();
    	scrapers = WebScraper.getAllInstances();
    }

    public static Model getInstance() {
        if(instance == null) {
            instance = new Model();
        }
        return instance;
    }

	private void scrapeNewData()
	{
		ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	    
		for(WebScraper scraper : scrapers) {
    		executor.submit(() -> {
                scraper.scrapeAllData();
            });
    	}
    	
    	executor.shutdown();
    	while (!executor.isTerminated()) {
            // Waiting...
        }
    	
		combineData();
	}
	
	public List<ArticleData> search(String inputContent, String category, String webSource) {
		return search(inputContent, category, webSource, 50);
	}
	
	public List<ArticleData> search(String inputContent, String category, String webSource,  int count)
	{
		HttpURLConnection conn = null;
        DataOutputStream os = null;
        
        try{
            URL url = new URL("http://127.0.0.1:5000/search/");
            String input = "{\"content\": \"" + inputContent + "\","
			         	 + "\"category\": \"" + category + "\","
			         	 + "\"web_source\": \"" + webSource + "\","
			         	 + "\"num_relevant_results\": " + count + ""
			         	 + "}";
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
            
            return Converter.convertJsonStringToData(output);
            
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
	
	public List<ArticleData> getLatest(int count) {
		return getLatest(count, "All");
	}
	
	public List<ArticleData> getLatest(int count, String category) {
		try {
			URL url = new URL("http://127.0.0.1:5000/latest?number=" + count + "&category=" + category);
			String recievedJsonString = connectServerGET(url);
			
			return Converter.convertJsonStringToData(recievedJsonString);
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	public List<ArticleData> getRandom(int count) {
		try {
			URL url = new URL("http://127.0.0.1:5000/random?number=" + count);
			String recievedJsonString = connectServerGET(url);
			
			return Converter.convertJsonStringToData(recievedJsonString);
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;
	}
	
	public List<ArticleData> getTrending(int count) {
		try {
			URL url = new URL("http://127.0.0.1:5000/trending?number=" + count);
			String recievedJsonString = connectServerGET(url);
			
			return Converter.convertJsonStringToData(recievedJsonString);
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;
	}
	
	public void aggregateNewData() {
		// Phase 1: Scrape new data to newsAll.json
		scrapeNewData();
		
		// Phase 2: Process the scraped data
		String result = "";
		while(true) {
			try {
				URL url = new URL("http://127.0.0.1:5000/update");
				result = connectServerGET(url);
				System.out.println(result);
				if (result.equals("Data is already updated")) break;
				
				Thread.sleep(300000);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private String connectServerGET(URL url) {
		HttpURLConnection conn = null;
		try{
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(0);
            
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }
            
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
            String output = br.readLine();

            conn.disconnect();
            
            return output;
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
	
	private void combineData() {
		String[] arrayOfFileNames = new String[] {
			"newsFT.json", "newsCONV.json", "newsAcademy.json", "newsTheBlockchain.json"
			, "newsCoindesk.json", "newsFreightWave.json", "newsTheFintech.json", "newsExpress.json"	
		};
		
		try {
			// Combine all files to create newsAll.json
			List<ArticleData> listOfData = new ArrayList<ArticleData>();
			for(String fileName : arrayOfFileNames)
			{
				Scanner scanner = new Scanner(new File(DIRECTORY + fileName));
				List<ArticleData> unitData = Converter.convertJsonStringToData(scanner.nextLine());
				
				listOfData.addAll(unitData);
				
				scanner.close();
			}
			Converter.convertDataToJson(listOfData, DIRECTORY + RESULT_FILE_NAME);
			
			// Delete all the materials
			for(String fileName : arrayOfFileNames) {
				File file = new File(DIRECTORY + fileName);
				if (file.delete()) {
		            System.out.println("File " + fileName + " deleted successfully");
		        }
		        else {
		            System.out.println("Failed to delete the file " + fileName);
		        }
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void runLocalServer()
	{
		if (processPid == -1)
		{
			String directory = System.getProperty("user.dir") + "\\src\\com\\newsalligator\\model\\localserver\\LocalServer.py";
			String command = "python " + directory;
			
			Process server = null;
			try {
				server = Runtime.getRuntime().exec(command);
				server.waitFor(2500, TimeUnit.MILLISECONDS);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			processPid = server.pid();
		}
		System.out.println("Local server started!");
	}
	
	public void terminateLocalServer()
	{
		System.out.println("Local server is terminated!");
		if (processPid != -1)
		{
			String command = "taskkill /F /T /PID " + processPid;
			try {
				Runtime.getRuntime().exec(command);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
