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
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Screen;

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
	private List<ArticleData> latestData;
	private List<ArticleData> randomData;
	
	@FXML
	void initialize() throws IOException, InterruptedException {
		setDate();
		setLatestArticle();
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
		
		String title = selected.getTITLE();
		String intro = selected.getINTRO();
		String author = selected.getAUTHOR_NAME();
		String htmlContent = selected.getHTML_CONTENT();
		String publishDate = selected.getCREATION_DATE();
		String website = selected.getWEBSITE_SOURCE();
		
		titleLabel.setText(title);
		introLabel.setText(intro);
		webView.getEngine().loadContent(htmlContent);
		authorLabel.setText(author);
		publishDateLabel.setText(publishDate);
		websiteLabel.setText(website);
		
		setReadNextArticle();
	}
	
	private void setLatestArticle() {
		Group[] latestArticle = new Group[] {smallArticle1, smallArticle2, smallArticle3, 
											 smallArticle4, smallArticle5};
		
		latestData = model.getLatestArticleData(latestArticle.length);
		for (int i = 0; i < latestArticle.length; i++) {
			int index = i;
			
			Thread thread = new Thread(() -> {
				try {
					Thread.sleep(0);
						Platform.runLater(() -> {
							PresenterTools.setArticleView(latestArticle[index], latestData.get(index), ArticleSize.SMALL);
						});
				} catch (InterruptedException ex) {
	                ex.printStackTrace();
	            }
			});
			
			thread.start();
		}
	}
	
	private void setReadNextArticle() {
		Group[] reedNextArticle = new Group[] {bigArticle1, bigArticle2, bigArticle3};
		
		randomData = model.getRandomArticleData(reedNextArticle.length);
		
		for (int i = 0; i < reedNextArticle.length; i++) {
			int index = i;
			Thread thread = new Thread(() -> {
				try {
					Thread.sleep(0);
						Platform.runLater(() -> {
							PresenterTools.setArticleView(reedNextArticle[index], randomData.get(index), ArticleSize.BIG);
						});
				} catch (InterruptedException ex) {
	                ex.printStackTrace();
	            }
			});
			
			thread.start();
		}
	}
	
	@FXML
	private void switchToArticle(MouseEvent event) throws IOException {
		Node clickedObject = (Node) event.getSource();
		Group selectedGroup;
		if (clickedObject.getClass() == Group.class) {
			selectedGroup = (Group) clickedObject;
		}
		else selectedGroup = (Group) clickedObject.getParent();
		
		List<Node> groupChildren = selectedGroup.getChildren();
		String indexCode = ((Text)(groupChildren.get(groupChildren.size() - 1))).getText();
		
		List<ArticleData> selectedList = (indexCode.charAt(0) == 'L') ? latestData : randomData;
		SceneVariables.getInstance().selectedArticleData = selectedList.get(indexCode.charAt(1) - '0');
		
		sceneSwitchInitialize();
    }
	
}
