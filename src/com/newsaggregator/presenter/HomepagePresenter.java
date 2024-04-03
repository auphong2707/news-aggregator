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
import javafx.stage.Screen;
import javafx.scene.Group;


public class HomepagePresenter {
	@FXML private Label dateLabel;
	
	@FXML private Group smallArticle1;
	@FXML private Group smallArticle2;
	@FXML private Group smallArticle3;
	@FXML private Group smallArticle4;
	
	@FXML private Group notSoBigArticle1;
	@FXML private Group notSoBigArticle2;
	@FXML private Group notSoBigArticle3;
	@FXML private Group notSoBigArticle4;
	@FXML private Group notSoBigArticle5;
	@FXML private Group notSoBigArticle6;
	
	@FXML private Group mediumArticle1;
	
	@FXML private Group bigArticle1;
	@FXML private Group bigArticle2;
	
	@FXML private TextField searchBar;
	@FXML private Button searchButton;
	
	private Model model = new Model();
	
	@FXML
	public void initialize() throws IOException, InterruptedException {
		setDate();
		setLatestArticle();
		setRandomArticle();
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
		Group[] latestArticle = new Group[] {bigArticle1, bigArticle2, smallArticle1, 
											 smallArticle2, smallArticle3, smallArticle4};
		
		List<ArticleData> latest = model.getLatestArticleData(latestArticle.length);
		
		for (int i = 0; i < latestArticle.length; i++) {
			if (i < 2) {
				PresenterTools.setArticleView(latestArticle[i], latest.get(i), ArticleSize.BIG);
			} else {
				PresenterTools.setArticleView(latestArticle[i], latest.get(i), ArticleSize.SMALL);
			}
		}
	} 
	
	private void setRandomArticle() {
		Group[] randomArticle = new Group[] {notSoBigArticle1, notSoBigArticle2, notSoBigArticle3,
											 notSoBigArticle4, notSoBigArticle5, notSoBigArticle6};
		
		List<ArticleData> random = model.getRandomArticleData(randomArticle.length);
		
		for (int i = 0; i < randomArticle.length; i++) {
			PresenterTools.setArticleView(randomArticle[i], random.get(i), ArticleSize.NOT_SO_BIG);
		}
	}
	
	@FXML
	private void searchByKey(KeyEvent key) throws IOException {
		if (key.getCode() == KeyCode.ENTER) {
			SceneVariables.getInstance().searchContent = searchBar.getText();
			switchToSearchtab();
		}
	}
	
	@FXML
	private void searchByButton() throws IOException {
		SceneVariables.getInstance().searchContent = searchBar.getText();
		switchToSearchtab();
	}
	
	private void switchToSearchtab() throws IOException {
		Test.window.setScene(Test.searchtab);
        Test.window.setWidth(Screen.getPrimary().getVisualBounds().getWidth());
        Test.window.setHeight(Screen.getPrimary().getVisualBounds().getHeight());
	}
}
