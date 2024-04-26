package com.newsaggregator.presenter;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import com.newsaggregator.model.ArticleData;
import com.newsaggregator.model.Model;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;

public class ArticleViewPresenter extends Presenter {
	@FXML private ScrollPane scrollPane;
	
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
	
	@Override
	void sceneSwitchInitialize() {
		ArticleData selected = SceneManager.getInstance().selectedArticleData;
		
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
		scrollPane.setVvalue(0);
		
		setReadNextArticle();
	}
	
	private void setLatestArticle() {
		Group[] latestArticle = new Group[] {smallArticle1, smallArticle2, smallArticle3, 
											 smallArticle4, smallArticle5};
		
		latestData = model.getLatestArticleData(latestArticle.length);
		
		PresenterTools.setArrayArticleViews(latestArticle, latestData, ArticleSize.SMALL);
	}
	
	private void setReadNextArticle() {
		Group[] reedNextArticle = new Group[] {bigArticle1, bigArticle2, bigArticle3};
		
		randomData = model.getRandomArticleData(reedNextArticle.length);
		
		PresenterTools.setArrayArticleViews(reedNextArticle, randomData, ArticleSize.BIG);
	}
	
	@FXML
	private void switchToHomepage() throws IOException {
		SceneManager.getInstance().switchScene(SceneType.HOMEPAGE);
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
		SceneManager.getInstance().selectedArticleData = selectedList.get(indexCode.charAt(1) - '0');
		
		sceneSwitchInitialize();
    }
	
}
