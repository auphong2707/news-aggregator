package com.newsaggregator.userinterface.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.newsaggregator.userinterface.uienum.SceneType;

public final class SearchTabCommand extends Command{
	private String content, webSource, category;
	
	public SearchTabCommand(String content, String category, String webSource) {
		super(SceneType.SEARCHTAB);
		
		this.content = content;
		this.category = category;
		this.webSource = webSource;
	}

	@Override
	public String getName() {
		return getKey().name() + ": " + content + " (" + category + ", " + webSource + ")";
	}

	@Override
	public List<String> getValue() {
		return new ArrayList<String>(Arrays.asList(content, category, webSource));
	}

}
