package com.newsaggregator.presenter;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import com.newsaggregator.model.ArticleData;
import com.newsaggregator.model.DataType;
import com.newsaggregator.model.Model;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class ArticleViewPresenter {
	@FXML private Label dateLabel;
	
	@FXML private Label titleLabel; 
	@FXML private Label introLabel; 
	@FXML private WebView webView;
	@FXML private Label authorLabel;
	@FXML private Label publishDateLabel; 
	@FXML private Label websiteLabel; 
	
	@FXML private Group smallArticle1;
	@FXML private Group smallArticle2;
	@FXML private Group smallArticle3;
	@FXML private Group smallArticle4;
	@FXML private Group smallArticle5;
	
	@FXML private Group bigArticle1;
	@FXML private Group bigArticle2;
	@FXML private Group bigArticle3;
	
	private Model model = new Model();
	
	@FXML
	void initialize() throws IOException, InterruptedException {
		setDate();
		setLatestArticle();
		setReedNextArticle();
		sceneSwitchInitialize();
	}
	
	private void setDate() {
		LocalDate currentDate = LocalDate.now();
		int day = currentDate.getDayOfMonth();
		int month = currentDate.getMonthValue();
		int year = currentDate.getYear();
		String dateAbbreviation = currentDate.getDayOfWeek().toString().substring(0, 3);
		dateLabel.setText(dateAbbreviation + ", " + day + "/" + month + "/" + year);
	}
	
	public void sceneSwitchInitialize() {
		ArticleData selected = SceneVariables.getInstance().selectedArticleData;
		
		String title = selected.getDataByType(DataType.TITLE);
		String intro = selected.getDataByType(DataType.INTRO);
		String author = selected.getDataByType(DataType.AUTHOR_NAME);
		String htmlContent = selected.getDataByType(DataType.HTML_CONTENT);
		String publishDate = selected.getDataByType(DataType.CREATION_DATE);
		String website = selected.getDataByType(DataType.WEBSITE_SOURCE);
		
		titleLabel.setText(title);
		introLabel.setText(intro);
		webView.getEngine().loadContent(htmlContent);
		authorLabel.setText(author);
		publishDateLabel.setText(publishDate);
		websiteLabel.setText(website);
		
	}
	
	private void setLatestArticle() {
		Group[] latestArticle = new Group[] {smallArticle1, smallArticle2, smallArticle3, 
											 smallArticle4, smallArticle5};
		
		List<ArticleData> latest = model.getLatestArticleData(latestArticle.length);
		for (int i = 0; i < latestArticle.length; i++) {
			int index = i;
			
			Thread thread = new Thread(() -> {
				try {
					Thread.sleep(0);
						Platform.runLater(() -> {
							PresenterTools.setArticleView(latestArticle[index], latest.get(index), ArticleSize.SMALL);
						});
				} catch (InterruptedException ex) {
	                ex.printStackTrace();
	            }
			});
			
			thread.start();
		}
	}
	
	private void setReedNextArticle() {
		Group[] reedNextArticle = new Group[] {bigArticle1, bigArticle2, bigArticle3};
		
		List<ArticleData> random = model.getRandomArticleData(reedNextArticle.length);
		
		for (int i = 0; i < reedNextArticle.length; i++) {
			int index = i;
			Thread thread = new Thread(() -> {
				try {
					Thread.sleep(0);
						Platform.runLater(() -> {
							PresenterTools.setArticleView(reedNextArticle[index], random.get(index), ArticleSize.BIG);
						});
				} catch (InterruptedException ex) {
	                ex.printStackTrace();
	            }
			});
			
			thread.start();
		}
	}
	
}
