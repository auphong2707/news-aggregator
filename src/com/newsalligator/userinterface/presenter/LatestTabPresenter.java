package com.newsalligator.userinterface.presenter;


import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;

import com.newsalligator.model.ArticleData;
import com.newsalligator.model.Model;
import com.newsalligator.userinterface.UIManager;
import com.newsalligator.userinterface.command.*;
import com.newsalligator.userinterface.tools.ArticleSetter;
import com.newsalligator.userinterface.uienum.ArticleSize;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class LatestTabPresenter extends Presenter {
	@FXML private ScrollPane scrollPane;
	
	@FXML private ImageView logo;
	@FXML private Label newsAlligatorLabel;
	@FXML private Label dateLabel;
	
	@FXML private Button historyButton;
	@FXML private Button returnButton;
	@FXML private Button forwardButton;
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
	
	private List<ArticleData> latestData;
	private int page;
	private Group[] articles;

	@FXML
	void initialize() {
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
	private void searchByKey(KeyEvent key) {
		if (key.getCode() == KeyCode.ENTER) {
			SearchTabCommand command = new SearchTabCommand(searchBar.getText(), "All", "All");
			
			UIManager.getInstance().executeCommand(command);
		}
	}
	
	@FXML
	private void searchByButton() {
		SearchTabCommand command = new SearchTabCommand(searchBar.getText(), "All", "All");
		
		UIManager.getInstance().executeCommand(command);
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
	private void switchToHomepage() {
		UIManager.getInstance().executeCommand(new HomepageCommand());
	}
	
	private void updateArticles() {
		int first = (page - 1) * 6;
		int last = (page - 1) * 6 + 6;
		
		ArticleSetter.setArrayArticleViews(articles, latestData.subList(first, last), ArticleSize.BIG);
	}
	
	private void setPage(int newPage) {
		page = newPage;
		pageLabel.setText("Page " + page);
	}
	
	@FXML
	private void switchToArticle(MouseEvent event) {
		Node clickedObject = (Node) event.getSource();
		Group selectedGroup;
		if (clickedObject.getClass() == Group.class) {
			selectedGroup = (Group) clickedObject;
		}
		else selectedGroup = (Group) clickedObject.getParent();
		int index = (page - 1)*6 + Integer.parseInt(((Text)(selectedGroup.getChildren().get(5))).getText());
		
		ArticleData selectedData = latestData.get(index);
		UIManager.getInstance().executeCommand(new ArticleTabCommand(selectedData));
    }
	
	@FXML
	private void returnScene() {
		UIManager.getInstance().returnCommand();
	}
	
	@FXML
	private void forwardScene() {
		UIManager.getInstance().forwardCommand();
	}
	
	@FXML 
	private void openHistory() {
		UIManager.getInstance().openHistoryWindow();
	}
	
	@FXML
	private void openWebsite(MouseEvent event) {
		Label clickedObject = (Label) event.getSource();
		String link = newsToLink(clickedObject.getText());
		
		try {
	        Desktop.getDesktop().browse(new URI(link));
	    } catch (IOException | URISyntaxException e) {
	        e.printStackTrace();
	    }
	}
	
	@Override
	public void sceneSwitchInitialize() {
		scrollPane.setVvalue(0);
		
		latestData = Model.getInstance().getLatest(60);
		
		searchBar.clear();
		
		setPage(1);

		updateArticles();
	}
}
