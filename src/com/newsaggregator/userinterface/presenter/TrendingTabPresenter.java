package com.newsaggregator.userinterface.presenter;


import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;

import com.newsaggregator.model.ArticleData;
import com.newsaggregator.model.Model;
import com.newsaggregator.userinterface.UIManager;
import com.newsaggregator.userinterface.command.ArticleViewCommand;
import com.newsaggregator.userinterface.command.HomepageCommand;
import com.newsaggregator.userinterface.command.SearchTabCommand;
import com.newsaggregator.userinterface.tools.ArticleSetter;
import com.newsaggregator.userinterface.uienum.ArticleSize;

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

public class TrendingTabPresenter extends Presenter {
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
	
	@FXML private Label newspaper1;
	@FXML private Label newspaper2;
	@FXML private Label newspaper3;
	@FXML private Label newspaper4;
	@FXML private Label newspaper5;
	@FXML private Label newspaper6;
	@FXML private Label newspaper7;
	@FXML private Label newspaper8;
	
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
			SearchTabCommand command = new SearchTabCommand(searchBar.getText(), "All", "All");
			
			UIManager.getInstance().addCommand(command);
		}
	}
	
	@FXML
	private void searchByButton() throws IOException {
		SearchTabCommand command = new SearchTabCommand(searchBar.getText(), "All", "All");
		
		UIManager.getInstance().addCommand(command);
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
		UIManager.getInstance().addCommand(new HomepageCommand());
	}
	
	private void updateArticles() {
		int first = (page - 1) * 6;
		int last = (page - 1) * 6 + 6;
		
		ArticleSetter.setArrayArticleViews(articles, trendingData.subList(first, last), ArticleSize.BIG);
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
		int index = (page - 1)*6 + Integer.parseInt(((Text)(selectedGroup.getChildren().get(5))).getText());
		
		ArticleData selectedData = trendingData.get(index);
		UIManager.getInstance().addCommand(new ArticleViewCommand(selectedData));
    }
	
	@FXML
	private void returnScene() {
		UIManager.getInstance().returnCommand();
	}
	
	@FXML
	private void forwardScene() {
		UIManager.getInstance().forwardScene();
	}
	
	@FXML 
	private void openHistory() {
		UIManager.getInstance().openHistoryWindow();
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
		
		trendingData = Model.getInstance().getTrending(60);
		
		searchBar.clear();
		
		setPage(1);

		updateArticles();
	}
}