package com.newsaggregator.model;
import java.util.ArrayList;
import java.util.List;

public class Test {
	public static void main(String[] args) {
		List<String> arr1 = new ArrayList<String>();
		List<String> arr2 = new ArrayList<String>();
		arr1.add("phong");
		arr2.add("nononon");
		arr2.addAll(arr1);
		System.out.println(arr2);
		
		
	}
}
