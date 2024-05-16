package com.newsalligator.userinterface.command;

import com.newsalligator.model.ArticleData;
import com.newsalligator.userinterface.uienum.SceneType;

public final class ArticleTabCommand extends Command {
	private ArticleData articleData;
	
	public ArticleTabCommand(ArticleData articleData) {
		super(SceneType.ARTICLE_TAB);
		this.articleData = articleData;
	}

	@Override
	public String getName() {
		return getKey().name() + ": " + articleData.getTITLE();
	}

	@Override
	public ArticleData getValue() {
		return articleData;
	}

}
