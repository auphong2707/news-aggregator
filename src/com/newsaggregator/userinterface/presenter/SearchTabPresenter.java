package com.newsaggregator.userinterface.presenter;


import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import com.newsaggregator.model.Model;
import com.newsaggregator.userinterface.HistoryWindow;
import com.newsaggregator.userinterface.SceneManager;
import com.newsaggregator.userinterface.tools.ArticleSetter;
import com.newsaggregator.userinterface.uienum.ArticleSize;
import com.newsaggregator.userinterface.uienum.SceneType;
import com.newsaggregator.model.ArticleData;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
	
	@FXML private ComboBox<String> categoryBox;
	@FXML private ComboBox<String> webBox;
	
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
	
	@FXML private Label newspaper1;
	@FXML private Label newspaper2;
	@FXML private Label newspaper3;
	@FXML private Label newspaper4;
	@FXML private Label newspaper5;
	@FXML private Label newspaper6;
	@FXML private Label newspaper7;
	@FXML private Label newspaper8;
	
	private List<ArticleData> searchData;
	private int page;
	private Group[] articles;

	@FXML
	void initialize() throws IOException, InterruptedException {
		setDate();
		articles = new Group[] {article1, article2, article3, article4, article5};
		
		categoryBox.getItems().addAll("All", "Blockchain", "Crypto", "Others");
		webBox.getItems().addAll("All", "Academy Moralis", "Coindesk", "The Conversation", 
				"Financial Express", "Freight Wave", "Financial Times", "The Blockchain", "The Fintech Times");
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
		
		scrollPane.setVvalue(0);
		
		updateArticles();
	} 
	
	@FXML
	private void search(KeyEvent key) {
		if (key.getCode() == KeyCode.ENTER) {
			List<String> searchContent = Arrays.asList(searchBar.getText(), categoryBox.getValue(), webBox.getValue());
			SceneManager.getInstance().moveScene(SceneType.SEARCHTAB, searchContent);
		} 	
	}
	
	@FXML
	private void searchByButton() throws IOException {
		List<String> searchContent = Arrays.asList(searchBar.getText(), categoryBox.getValue(), webBox.getValue());
		SceneManager.getInstance().moveScene(SceneType.SEARCHTAB, searchContent);
	}
	
	@FXML
	private void clearSearch() {
		searchBar.clear();
		categoryBox.setValue("All");
		webBox.setValue("All");	
	}
	
	@FXML
	private void switchToHomepage() throws IOException {
		SceneManager.getInstance().moveScene(SceneType.HOMEPAGE, null);
	}
	
	private void updateArticles() {
		int first = (page - 1) * 5;
		int last = (page - 1) * 5 + 5;
		
		ArticleSetter.setArrayArticleViews(articles, searchData.subList(first, last), ArticleSize.BIG);
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
	
	@FXML
	private void openWebsite(MouseEvent event) {
	    String link = "";
		Label clickedObject = (Label) event.getSource();
		
		if (clickedObject == newspaper1) {
			link = "https://academy.moralis.io/";
		} else if (clickedObject == newspaper2) {
			link = "https://www.coindesk.com/";
		} else if (clickedObject == newspaper3) {
			link = "https://www.financialexpress.com/";
		} else if (clickedObject == newspaper4) {
			link = "https://www.ft.com/";
		} else if (clickedObject == newspaper5) {
			link = "https://www.freightwaves.com/";
		} else if (clickedObject == newspaper6) {
			link = "https://www.the-blockchain.com/";
		} else if (clickedObject == newspaper7) {
			link = "https://theconversation.com/global";
		} else if (clickedObject == newspaper8) {
			link = "https://thefintechtimes.com/";
		}
		
		try {
	        Desktop.getDesktop().browse(new URI(link));
	    } catch (IOException | URISyntaxException e) {
	        e.printStackTrace();
	    }
	}
	
	@Override
	public void sceneSwitchInitialize() {
		scrollPane.setVvalue(0);
		
		List<String> searchContent = SceneManager.getInstance().getSearchContent();		
		
		searchBar.setText(searchContent.get(0));
		categoryBox.setValue(searchContent.get(1));
		webBox.setValue(searchContent.get(2));
		
		searchData = Model.getInstance().search(searchContent.get(0), searchContent.get(1), searchContent.get(2));
		
		setPage(1);

		updateArticles();
	}
}
