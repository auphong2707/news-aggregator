package com.newsaggregator.presenter;


import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import com.newsaggregator.model.Model;

import com.newsaggregator.model.ArticleData;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class SearchTabPresenter extends Presenter {
	@FXML private ScrollPane scrollPane;
	
	@FXML private ImageView logo;
	@FXML private Label newsAlligatorLabel;
	@FXML private Label dateLabel;
	
	@FXML private Label pageLabel;
	@FXML private Button historyButton;
	@FXML private Button returnButton;
	@FXML private Button forwardButton;
	@FXML private Button nextPage;
	@FXML private Button previousPage;
	
	@FXML private TextField searchBar;
	
	@FXML private Group article1;
	@FXML private Group article2;
	@FXML private Group article3;
	@FXML private Group article4;
	@FXML private Group article5;
	
	private List<ArticleData> searchData;
	private int page;
	private Group[] articles;

	@FXML
	void initialize() throws IOException, InterruptedException {
		setDate();
		articles = new Group[] {article1, article2, article3, article4, article5};
	}
	
	private void setDate() {
		LocalDate currentDate = LocalDate.now();
		int day = currentDate.getDayOfMonth();
		int month = currentDate.getMonthValue();
		int year = currentDate.getYear();
		String dateAbbreviation = currentDate.getDayOfWeek().toString().substring(0, 3);
		dateLabel.setText(dateAbbreviation + ", " + day + "/" + month + "/" + year);
	}
	
	
	@FXML
	private void switchPage(ActionEvent event){
		if (event.getSource() == nextPage && page < 10) {
			setPage(page + 1);
		}
		else if (event.getSource() == previousPage && page > 1) {
			setPage(page - 1);
		}
		
		updateArticles();
	} 
	
	@FXML
	private void search(KeyEvent key) {
		if (key.getCode() == KeyCode.ENTER) {
			SceneManager.getInstance().moveScene(SceneType.SEARCHTAB, searchBar.getText());
		} 	
	}
	
	@FXML
	private void switchToHomepage() throws IOException {
		SceneManager.getInstance().moveScene(SceneType.HOMEPAGE, null);
	}
	
	private void updateArticles() {
		int first = (page - 1) * 5;
		int last = (page - 1) * 5 + 5;
		
		PresenterTools.setArrayArticleViews(articles, searchData.subList(first, last), ArticleSize.BIG);
	}
	
	private void setPage(int newPage) {
		page = newPage;
		pageLabel.setText("Page " + page);
	}
	
	@FXML
	private void switchToArticle(MouseEvent event) throws IOException {
		Node clickedObject = (Node) event.getSource();
		Group selectedGroup;
		if (clickedObject.getClass() == Group.class) {
			selectedGroup = (Group) clickedObject;
		}
		else selectedGroup = (Group) clickedObject.getParent();
		int index = (page - 1)*5 + Integer.parseInt(((Text)(selectedGroup.getChildren().get(5))).getText());
		
		ArticleData selectedData = searchData.get(index);
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
		scrollPane.setVvalue(0);
		
		searchBar.setText(SceneManager.getInstance().getSearchContent());
		
		searchData = Model.getInstance().search(SceneManager.getInstance().getSearchContent());
		setPage(1);

		updateArticles();
	}
}
