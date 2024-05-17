package com.newsalligator.userinterface.command;

import com.newsalligator.model.ArticleData;
import com.newsalligator.userinterface.uienum.SceneType;

/**
 * The {@code ArticleTabCommand} class represents a command to switch to the article tab scene.
 * @author Phong Au
 */
public final class ArticleTabCommand extends Command {
	private ArticleData articleData;
	
    /**
     * Constructs a new {@code ArticleTabCommand} with the article data.
     *
     * @param articleData the article data to be displayed in the article tab scene
     */
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
