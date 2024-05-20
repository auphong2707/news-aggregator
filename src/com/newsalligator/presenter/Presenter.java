package com.newsalligator.presenter;

import com.newsalligator.model.Model;

/**
 * <h1> Presenter </h1>
 * The {@code Presenter} is an abstract class to initialize scenes when switching and map
 * news title to their corresponding links.
 * @author Phong Au
 */
public abstract class Presenter {
	static Model model = new Model();
	
	/**
	 * Handles initialization when the scene is switched.
	 */
	public abstract void sceneSwitchInitialize();
	
	/**
	 * Maps a news title to its corresponding links.
	 * @param newsTitle the title of the news
	 * @return the URL of the news, or null if the title is not detected.
	 */
	String newsToLink(String newsTitle) {
		switch(newsTitle) {
		case "Academy Moralis": 
			return "https://academy.moralis.io/";
		case "Coindesk": 
			return "https://www.coindesk.com/";
		case "Financial Express": 
			return "https://www.financialexpress.com/";
		case "Financial Time":
			return "https://www.ft.com/";
		case "Frieght Wave":
			return "https://www.freightwaves.com/";
		case "The Blockchain":
			return "https://www.the-blockchain.com/";
		case "The Conversation":
			return "https://theconversation.com/global";
		case "The Fintech Times":
			return "https://thefintechtimes.com/";
		default:
			return null;
		}
	}
}