package com.newsaggregator.presenter;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import com.newsaggregator.model.ArticleData;
import com.newsaggregator.model.Model;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Box;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Node;


public class HomepagePresenter extends Presenter {
	@FXML private Label dateLabel;
	@FXML private Label trendingLabel;
	@FXML private Label latestLabel;
	
	@FXML private Group smallArticle1;
	@FXML private Group smallArticle2;
	@FXML private Group smallArticle3;
	@FXML private Group smallArticle4;
	@FXML private Group smallArticle5;
	@FXML private Group smallArticle6;
	
	@FXML private Group notSoBigArticle1;
	@FXML private Group notSoBigArticle2;
	@FXML private Group notSoBigArticle3;
	@FXML private Group notSoBigArticle4;
	@FXML private Group notSoBigArticle5;
	@FXML private Group notSoBigArticle6;
	
	@FXML private Group notSoBigArticle1t;
	@FXML private Group notSoBigArticle2t;
	@FXML private Group notSoBigArticle3t;
	@FXML private Group notSoBigArticle4t;
	
	@FXML private Group mediumArticle1;
	
	@FXML private Group bigArticle1;
	@FXML private Group bigArticle2;
	
	@FXML private TextField searchBar;
	@FXML private Button searchButton;
	@FXML private Button returnButton;
	@FXML private Button forwardButton;
	@FXML private Button historyButton;
	
	private Model model = new Model();
	
	private List<ArticleData> latestData;
	private List<ArticleData> randomData;
	private List<ArticleData> trendingData;
	
	@FXML
	public void initialize() throws IOException, InterruptedException {
		setDate();
		setLatestArticle();
		setRandomArticle();
		setTrendingArticle();
	}
	
	private void setDate() {
		LocalDate currentDate = LocalDate.now();
		int day = currentDate.getDayOfMonth();
		int month = currentDate.getMonthValue();
		int year = currentDate.getYear();
		String dateAbbreviation = currentDate.getDayOfWeek().toString().substring(0, 3);
		dateLabel.setText(dateAbbreviation + ", " + day + "/" + month + "/" + year);
	}
	
	private void setLatestArticle() {
		latestData = model.getLatestArticleData(6);
		
		Group[] latestSmallArticle = new Group[] {smallArticle1, smallArticle2, smallArticle3,
				 								  smallArticle4, smallArticle5, smallArticle6};
		
		PresenterTools.setArrayArticleViews(latestSmallArticle, latestData, ArticleSize.SMALL);
	} 
	
	private void setRandomArticle() {
		Group[] randomArticle = new Group[] {notSoBigArticle1, notSoBigArticle2, notSoBigArticle3,
											 notSoBigArticle4, notSoBigArticle5, notSoBigArticle6};
		
		randomData = model.getRandomArticleData(randomArticle.length);
		
		PresenterTools.setArrayArticleViews(randomArticle, randomData, ArticleSize.NOT_SO_BIG);
	}
	
	private void setTrendingArticle() {
		Group[] trendingBigArticle = new Group[] {bigArticle1, bigArticle2};
		Group[] trendingNotSoBigArticle = new Group[] {notSoBigArticle1t, notSoBigArticle2t,
													   notSoBigArticle3t, notSoBigArticle4t};
		
		trendingData = model.getTrending(6);
		
		PresenterTools.setArrayArticleViews(trendingBigArticle, trendingData.subList(0, 2), ArticleSize.BIG);
		PresenterTools.setArrayArticleViews(trendingNotSoBigArticle, trendingData.subList(2, 6), ArticleSize.NOT_SO_BIG);
	}
	
	@FXML
	private void switchToTrendingTab() {
		SceneManager.getInstance().moveScene(SceneType.TRENDINGTAB, null);
	}
	
	@FXML
	private void switchToLatestTab() {
		SceneManager.getInstance().moveScene(SceneType.LATESTTAB, null);
	}
	
	@FXML
	private void searchByKey(KeyEvent key) throws IOException {
		if (key.getCode() == KeyCode.ENTER) {
			String searchContent = searchBar.getText();
			SceneManager.getInstance().moveScene(SceneType.SEARCHTAB, searchContent);
		}
	}
	
	@FXML
	private void searchByButton() throws IOException {
		String searchContent = searchBar.getText();
		SceneManager.getInstance().moveScene(SceneType.SEARCHTAB, searchContent);
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
		
		List<ArticleData> selectedList = null;
		if (indexCode.charAt(0) == 'L')
			selectedList = latestData;
		else if (indexCode.charAt(0) == 'R')
			selectedList = randomData;
		else if (indexCode.charAt(0) == 'T')
			selectedList = trendingData;
		
		ArticleData selectedData = selectedList.get(indexCode.charAt(1) - '0');
		SceneManager.getInstance().moveScene(SceneType.ARTICLE_VIEW, selectedData);
    }
	
	@FXML
	private void returnScene() {
		SceneManager.getInstance().returnScene();
	}
	
	@FXML
	private void forwardScene() {
		SceneManager.getInstance().forwardScene();
	}
	
	@FXML 
	private void openHistory() {
		HistoryWindow.getInstance().switchWindow();
	}
	
	@Override
	void sceneSwitchInitialize() {
		searchBar.clear();
	}
}
