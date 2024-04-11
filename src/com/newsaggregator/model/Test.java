package com.newsaggregator.model;

import java.io.IOException;


public class Test {
	public static void main(String[] args) throws IOException, InterruptedException{
		Model model = new Model();
		Model.runLocalServer();
		
		System.out.println(model.getTrending(10));
		
		Model.terminateLocalServer();
	}
}
