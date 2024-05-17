package com.newsalligator.userinterface.presenter;

public abstract class Presenter {
	public abstract void sceneSwitchInitialize();
	
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