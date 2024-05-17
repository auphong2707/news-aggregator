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
import com.newsalligator.userinterface.command.ArticleTabCommand;
import com.newsalligator.userinterface.command.HomepageCommand;
import com.newsalligator.userinterface.command.SearchTabCommand;
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
/**
 * The {@code TrendingTabPresenter} class is a class to manage the trending tab view.
 * @author Khanh Nguyen, Quan Tran, Phong Au
 */
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
	
	/**
	 * List of {@code ArticleData} objects representing trending articles.
	 */
	private List<ArticleData> trendingData;
	/**
	 * Page number.
	 */
	private int page;
	/**
	 * Group of articles.
	 */
	private Group[] articles;

    /**
     * Initializes the Trending tab view.
     */
	@FXML
	void initialize() {
		setDate();
		articles = new Group[] {article1, article2, article3, article4, article5, article6};
	}
	
    /**
     * Sets the current date in the date label.
     */
	private void setDate() {
		LocalDate currentDate = LocalDate.now();
		int day = currentDate.getDayOfMonth();
		int month = currentDate.getMonthValue();
		int year = currentDate.getYear();
		String dateAbbreviation = currentDate.getDayOfWeek().toString().substring(0, 3);
		dateLabel.setText(dateAbbreviation + ", " + day + "/" + month + "/" + year);
	}
	
    /**
     * Performs a search when the Enter key is pressed.
     * 
     * @param key the key event
     */
	@FXML
	private void searchByKey(KeyEvent key) {
		if (key.getCode() == KeyCode.ENTER) {
			SearchTabCommand command = new SearchTabCommand(searchBar.getText(), "All", "All");
			
			UIManager.getInstance().executeCommand(command);
		}
	}
	
    /**
     * Initiates a search when the search button is clicked.
     */
	@FXML
	private void searchByButton() {
		SearchTabCommand command = new SearchTabCommand(searchBar.getText(), "All", "All");
		
		UIManager.getInstance().executeCommand(command);
	}
	
    /**
     * Switches to the next or previous page of trending articles.
     * 
     * @param event the action event
     */
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
	
    /**
     * Switches to the Homepage tab.
     */
	@FXML
	private void switchToHomepage() {
		UIManager.getInstance().executeCommand(new HomepageCommand());
	}
	
    /**
     * Updates the displayed trending articles based on the current page.
     */
	private void updateArticles() {
		scrollPane.setVvalue(0);
		
		int first = (page - 1) * 6;
		int last = (page - 1) * 6 + 6;
		
		ArticleSetter.setArrayArticleViews(articles, trendingData.subList(first, last), ArticleSize.BIG);
	}
	
    /**
     * Sets the current page and updates the page label.
     * 
     * @param newPage the new page number
     */
	private void setPage(int newPage) {
		page = newPage;
		pageLabel.setText("Page " + page);
	}
	
    /**
     * Switches to the article view for the selected trending article.
     * 
     * @param event the mouse click event
     */
	@FXML
	private void switchToArticle(MouseEvent event) {
		Node clickedObject = (Node) event.getSource();
		Group selectedGroup;
		if (clickedObject.getClass() == Group.class) {
			selectedGroup = (Group) clickedObject;
		}
		else selectedGroup = (Group) clickedObject.getParent();
		int index = (page - 1)*6 + Integer.parseInt(((Text)(selectedGroup.getChildren().get(5))).getText());
		
		ArticleData selectedData = trendingData.get(index);
		UIManager.getInstance().executeCommand(new ArticleTabCommand(selectedData));
    }
	
    /**
     * Returns to the previous scene.
     */
	@FXML
	private void returnScene() {
		UIManager.getInstance().returnCommand();
	}
	
    /**
     * Forwards to the next scene.
     */
	@FXML
	private void forwardScene() {
		UIManager.getInstance().forwardCommand();
	}
	
    /**
     * Opens the history window.
     */
	@FXML 
	private void openHistory() {
		UIManager.getInstance().openHistoryWindow();
	}
	
    /**
     * Opens the website for the clicked link in the web browser.
     * 
     * @param event the mouse click event
     */
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
		trendingData = Model.getInstance().getTrending(60);
		
		searchBar.clear();
		
		setPage(1);

		updateArticles();
	}
}
