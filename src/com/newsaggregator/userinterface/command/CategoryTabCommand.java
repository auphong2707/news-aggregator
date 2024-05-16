package com.newsaggregator.userinterface.command;

import com.newsaggregator.userinterface.uienum.SceneType;

public final class CategoryTabCommand extends Command {

	String categoryName;
	
	public CategoryTabCommand(String categoryName) {
		super(SceneType.CATEGORY_TAB);
		this.categoryName = categoryName;
	}

	@Override
	public String getName() {
		return getKey().name() + ": " + categoryName;
	}

	@Override
	public String getValue() {
		return categoryName;
	}

}
