package com.newsaggregator.presenter;

import java.util.List;

import com.newsaggregator.model.ArticleData;
import com.newsaggregator.model.DataType;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;

public class PresenterTools {
	
	protected static void setArticleView(Group view, ArticleData data, ArticleSize size) {
		switch(size) {
		case BIG:
			setBigArticleView(view, data);
			return;
		case NOT_SO_BIG:
			setNotBigArticleView(view, data);
			return;
		case MEDIUM:
			setMediumArticleView(view, data);
			return;
		case SMALL:
			setSmallArticleView(view, data);
			return;
		default:
			return;
		}
	}

	private static void setSmallArticleView(Group view, ArticleData data) {
		// TODO Auto-generated method stub
		List<Node> elements = view.getChildren();
		
		Label website = (Label) elements.get(0);
		Label title = (Label) elements.get(1);
		
		website.setText(data.getDataByType(DataType.WEBSITE_SOURCE));
		title.setText(data.getDataByType(DataType.TITLE));
	}

	private static void setMediumArticleView(Group view, ArticleData data) {
		// TODO Auto-generated method stub
		List<Node> elements = view.getChildren();
		
		Label website = (Label) elements.get(0);
		Label summary = (Label) elements.get(1);
		Label title = (Label) elements.get(2);
		
		website.setText(data.getDataByType(DataType.WEBSITE_SOURCE));
		title.setText(data.getDataByType(DataType.TITLE));
		summary.setText(data.getDataByType(DataType.SUMMARY));
	}

	private static void setNotBigArticleView(Group view, ArticleData data) {
		// TODO Auto-generated method stub
		List<Node> elements = view.getChildren();
		
		Label website = (Label) elements.get(0);
		Label title = (Label) elements.get(1);
		Label summary = (Label) elements.get(2);
		
		website.setText(data.getDataByType(DataType.WEBSITE_SOURCE));
		title.setText(data.getDataByType(DataType.TITLE));
		summary.setText(data.getDataByType(DataType.SUMMARY));
	}

	private static void setBigArticleView(Group view, ArticleData data) {
		// TODO Auto-generated method stub
		List<Node> elements = view.getChildren();
		
		Label website = (Label) elements.get(0);
		Label title = (Label) elements.get(1);
		Label summary = (Label) elements.get(2);
		Label author = (Label) elements.get(3);
		
		website.setText(data.getDataByType(DataType.WEBSITE_SOURCE));
		title.setText(data.getDataByType(DataType.TITLE));
		summary.setText(data.getDataByType(DataType.SUMMARY));
		author.setText(data.getDataByType(DataType.AUTHOR_NAME));
		
	}
}
