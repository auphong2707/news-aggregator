package com.newsaggregator.model;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Model {
	private final static String DIRECTORY = "data/";
	private final static String RESULT_FILE_NAME = "newsAll.json";
	private final static WebScrapper[] scrapers;
	
	private static long processPid = -1;
	private static List<ArticleData> modelData;
	
	static
	{
		modelData = ModelTools.convertJsonToData(DIRECTORY + RESULT_FILE_NAME);
		modelData.sort(
			(ArticleData a1, ArticleData a2)
			-> a2.getCREATION_DATE().compareTo(a1.getCREATION_DATE())
		);
		scrapers = new WebScrapper[] {
			new WebScrapperFT(),
			new WebScrapperCONV(),
			new WebScrapperAcademy(),
			new WebScrapperTheBlockchain(),
			new WebScrapperCoindesk(),
			new WebScrapperFreightWave(),
			new WebScrapperTheFintech(),
			new WebScrapperExpress()
		};
	}
	
	public static void runLocalServer() throws IOException, InterruptedException
	{
		if (processPid == -1)
		{
			String directory = System.getProperty("user.dir") + "\\src\\com\\newsaggregator\\model\\DataAnalyzer.py";
			String command = "python " + directory;
			
			Process server = Runtime.getRuntime().exec(command);
			server.waitFor(5, TimeUnit.SECONDS);
			
			processPid = server.pid();
		}
	}
	
	public static void terminateLocalServer() throws IOException
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
            
            return ModelTools.convertJsonStringToData(output);
            
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
	
	public List<ArticleData> getLatestArticleData(int count) {
		return modelData.subList(0, count);
	}
	
	public List<ArticleData> getRandomArticleData(int count) {
		return ModelTools.randomSubList(modelData, count);
	}
	
	public List<ArticleData> getTrending(int count) {
		HttpURLConnection conn = null;
        DataOutputStream os = null;
        
        try{
            URL url = new URL("http://127.0.0.1:5000/trending"); //important to add the trailing slash after add
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");
            
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }
            
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
            String output = br.readLine();

            conn.disconnect();
            
            List<ArticleData> trendingData = ModelTools.convertJsonStringToData(output);
            return ModelTools.randomSubList(trendingData, count);
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
	
	private void combineData() {
		String[] arrayOfFileNames = new String[] {
			"newsFT.json", "newsCONV.json", "newsAcademy.json", "newsTheBlockchain.json"
			, "newsCoindesk.json", "newsFreightWave.json", "newsTheFintech.json", "newsExpress"	
		};
		
		try {
			List<ArticleData> listOfData = new ArrayList<ArticleData>();
			for(String fileName : arrayOfFileNames)
			{
				Scanner scanner = new Scanner(new File(DIRECTORY + fileName));
				List<ArticleData> unitData = ModelTools.convertJsonStringToData(scanner.nextLine());
				
				listOfData.addAll(unitData);
				
				scanner.close();
			}
			ModelTools.convertDataToJson(listOfData, DIRECTORY + RESULT_FILE_NAME);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
