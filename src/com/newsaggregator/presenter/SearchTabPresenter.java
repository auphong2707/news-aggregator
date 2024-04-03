package com.newsaggregator.presenter;


import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import com.newsaggregator.model.Model;

import com.newsaggregator.model.ArticleData;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class SearchTabPresenter {
	private Model model = new Model();
	
	@FXML private ImageView logo;
	@FXML private Label newsAlligatorLabel;
	@FXML private Label dateLabel;
	
	@FXML private Label pageLabel;
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
	
	public void sceneSwitchInitialize() {
		searchBar.setText(SceneVariables.getInstance().searchContent);
		//System.out.println(SceneVariables.getInstance().searchContent);
		
		searchData = model.search(SceneVariables.getInstance().searchContent);
		page = 1;
		for (int i = 0; i < articles.length; i++)
		{
			int index = i + (page - 1) * 5;
			PresenterTools.setArticleView(articles[i], searchData.get(index), ArticleSize.BIG);
		}
	}
	
	@FXML
	private void switchPage(ActionEvent event){
		if (event.getSource() == nextPage && page < 10) {
			page++;
			pageLabel.setText("Page " + page);
			switchArticle();
		}
		else if (event.getSource() == previousPage && page > 1) {
			page--;
			pageLabel.setText("Page " + page);
			switchArticle();
		}
	} 
	
	private void switchArticle() {
		for (int i = 0; i < articles.length; i++)
		{
			PresenterTools.setArticleView(articles[i], searchData.get(i+(page-1)*5), ArticleSize.BIG);
		}
	}
	
	@FXML
	private void search(KeyEvent key) {
		if (key.getCode() == KeyCode.ENTER) {
			searchData = model.search(searchBar.getText());
			
			page = 1;
			pageLabel.setText("Page " + page);
			
			for (int i = 0; i < articles.length; i++)
			{
				int index = i + (page - 1) * 5;
				PresenterTools.setArticleView(articles[i], searchData.get(index), ArticleSize.BIG);
			}
		} 	
	}
	
	@FXML
	private void switchToHomepage(MouseEvent event) throws IOException {
		Test.window.setScene(Test.homepage);
        Test.window.setWidth(Screen.getPrimary().getVisualBounds().getWidth());
        Test.window.setHeight(Screen.getPrimary().getVisualBounds().getHeight());
	}
	
}
