package com.newsaggregator.userinterface.tools;

import java.util.List;

import com.newsaggregator.model.ArticleData;
import com.newsaggregator.userinterface.uienum.ArticleSize;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ArticleSetter {	
	public static void setArrayArticleViews(Group[] views, List<ArticleData> data, ArticleSize size) {
		for (int i = 0; i < views.length; i++) {
			int index = i;
			
			Thread thread = new Thread(() -> {
				try {
					Thread.sleep(500);
						Platform.runLater(() -> {
							ArticleSetter.setArticleView(views[index], data.get(index), size);
						});
				} catch (InterruptedException ex) {
	                ex.printStackTrace();
	            }
			});
			
			thread.setDaemon(true);
			thread.start();
		}
	}
	
	static void setArticleView(Group view, ArticleData data, ArticleSize size) {
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
		view.setVisible(data != null);
		
		List<Node> elements = view.getChildren();
		
		setWebsite(elements, data, 0);
		setTitle(elements, data, 1);
		setImage(elements, data, 2);
	}

	private static void setNotBigArticleView(Group view, ArticleData data) {
		view.setVisible(data != null);
		if(data == null) return;
		
		List<Node> elements = view.getChildren();
		
		setWebsite(elements, data, 0);
		setTitle(elements, data, 1);
		setIntro(elements, data, 2);
		setImage(elements, data, 3);
	}

	private static void setMediumArticleView(Group view, ArticleData data) {
		view.setVisible(data != null);
		if(data == null) return;
		
		List<Node> elements = view.getChildren();
		
		setWebsite(elements, data, 0);
		setTitle(elements, data, 1);
		setIntro(elements, data, 2);
	}

	private static void setBigArticleView(Group view, ArticleData data) {
		view.setVisible(data != null);
		if(data == null) return;
		
		List<Node> elements = view.getChildren();
		
		setWebsite(elements, data, 0);
		setTitle(elements, data, 1);
		setIntro(elements, data, 2);
		setAuthor(elements, data, 3);
		setImage(elements, data, 4);
	}
	
	private static void setWebsite(List<Node> elements, ArticleData data, int index) {
		Label website = (Label) elements.get(index);
		website.setText(data.getWEBSITE_SOURCE());
	}
	
	private static void setTitle(List<Node> elements, ArticleData data, int index) {
		Label title = (Label) elements.get(index);
		title.setText(data.getTITLE());
	}
	
	private static void setIntro(List<Node> elements, ArticleData data, int index) {
		Label intro = (Label) elements.get(index);
		intro.setText(data.getINTRO());
	}
	
	private static void setAuthor(List<Node> elements, ArticleData data, int index) {
		Label author = (Label) elements.get(index);
		author.setText(data.getAUTHOR_NAME());
	}
	
	private static void setImage(List<Node> elements, ArticleData data, int index) {
		ImageView imageView = (ImageView) elements.get(index);
		
		String imgURL = data.getIMAGE();
		if (imgURL != null && !imgURL.equals("")) {
			imageView.setImage(new Image(imgURL, 300, 200, false, false));
		}
		else {
			String blankDirectory = "file:///" + System.getProperty("user.dir") + "/images/blank_rectangle.png";
			imageView.setImage(new Image(blankDirectory));
		}
	}
}
