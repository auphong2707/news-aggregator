package com.newsaggregator.userinterface.command;

import com.newsaggregator.model.ArticleData;
import com.newsaggregator.userinterface.uienum.SceneType;

public final class ArticleViewCommand extends Command {
	private ArticleData articleData;
	
	public ArticleViewCommand(ArticleData articleData) {
		super(SceneType.ARTICLE_VIEW);
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
