package com.newsalligator.userinterface.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The {@code SearchTabCommand} class represents a command to switch to the search tab scene.
 * @author Phong Au
 */
public final class SearchTabCommand extends Command{
	private String content, webSource, category;
	
    /**
     * Constructs a new {@code SearchTabCommand} with the search content, category, and web source.
     *
     * @param content   the search content
     * @param category  the category to search
     * @param webSource the web source to search
     */
	public SearchTabCommand(String content, String category, String webSource) {
		super(SceneType.SEARCH_TAB);
		
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
