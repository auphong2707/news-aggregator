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
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class TrendingTabPresenter extends Presenter {
	private Model model = new Model();
	
	@FXML private ImageView logo;
	@FXML private Label newsAlligatorLabel;
	@FXML private Label dateLabel;
	
	@FXML private Button returnButton;
	@FXML private TextField searchBar;
	@FXML private Button searchButton;
	@FXML private Label pageLabel;
	@FXML private Button nextPage;
	@FXML private Button previousPage;
	
	@FXML private Group article1;
	@FXML private Group article2;
	@FXML private Group article3;
	@FXML private Group article4;
	@FXML private Group article5;
	@FXML private Group article6;
	
	private List<ArticleData> trendingData;
	private int page;
	private Group[] articles;

	@FXML
	void initialize() throws IOException, InterruptedException {
		setDate();
		articles = new Group[] {article1, article2, article3, article4, article5, article6};
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
	private void switchToHomepage() throws IOException {
		SceneManager.getInstance().moveScene(SceneType.HOMEPAGE, null);
	}
	
	private void updateArticles() {
		int first = (page - 1) * 6;
		int last = (page - 1) * 6 + 6;
		
		PresenterTools.setArrayArticleViews(articles, trendingData.subList(first, last), ArticleSize.BIG);
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
		int index = (page - 1)*6 + Integer.parseInt(((Text)(selectedGroup.getChildren().get(6))).getText());
		
		ArticleData selectedData = trendingData.get(index);
		SceneManager.getInstance().moveScene(SceneType.ARTICLE_VIEW, selectedData);
    }
	
	@FXML
	void returnScene() {
		SceneManager.getInstance().returnScene();
	}
	
	@Override
	void sceneSwitchInitialize() {
		trendingData = model.getTrending(60);
		
		searchBar.clear();
		
		setPage(1);

		updateArticles();
	}
}
