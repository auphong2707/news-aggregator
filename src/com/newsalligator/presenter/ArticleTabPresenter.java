package com.newsalligator.presenter;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;

import com.newsalligator.model.ArticleData;
import com.newsalligator.model.Model;
import com.newsalligator.presenter.command.*;
import com.newsalligator.presenter.tools.ArticleSetter;
import com.newsalligator.presenter.tools.ArticleSize;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;

/**
 * The {@code ArticleTabPresenter} to display article tab view.
 * @author Khanh Nguyen, Quan Tran, Phong Au
 */
public class ArticleTabPresenter extends Presenter {
	@FXML private ScrollPane scrollPane;
	@FXML private Label dateLabel;
	
	@FXML private ToggleButton contentToggleButton;
	@FXML private ToggleButton summaryToggleButton;
	
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
	
	/**
	 * The {@code ArticleData} object of an selected article
	 */
	private ArticleData selectedArticle;
	
	/**
	 * List of {@code ArticleData} objects representing latest articles.
	 */
	private List<ArticleData> latestData;
	
	/**
	 * List of {@code ArticleData} objects representing random articles.
	 */
	private List<ArticleData> randomData;
	
	private final String CSS = "<style>"
			+ "img {max-width: 100%; height: auto; "
			+ "		display: block; "
			+ "		margin: 0 auto; }"
			+ "figcaption {text-align: center; }"
			+ "body {width: 97%; height: 100%; margin: 0; "
			+ "		 font-family: verdana, arial, sans-serif; "
			+ "		 font-size: 20px; "
			+ "		 align-items: center;"
			+ "		 overflow-x: hidden; "
			+ "		 text-align: justify; "
			+ "		 background-color: #f4f4f4;}"
			+ "aside { display: none !important; }"
			+ "button { display: none !important; }"
			+ "iframe { display: none !important; }"
			+ "a { text-decoration: none; color: inherit; cursor: text; pointer-events: none;}"
			+ "</style>";
	
	/**
	 * Initializes the article tab view.
	 * @throws IOException if there is error loading data
	 * @throws InterruptedException if the process is interrupted
	 */
	@FXML
	void initialize() throws IOException, InterruptedException {
		setDate();
		setLatestArticle();
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
	

	@Override
	public void sceneSwitchInitialize() {
		selectedArticle = (ArticleData) PresenterManager.getInstance().getCurrentCommandValue();
		
		String title = selectedArticle.getTITLE();
		String intro = selectedArticle.getINTRO();
		String author = selectedArticle.getAUTHOR_NAME();
		String htmlContent = selectedArticle.getHTML_CONTENT();
		String publishDate = selectedArticle.getCREATION_DATE();
		String website = selectedArticle.getWEBSITE_SOURCE();

		titleLabel.autosize();
		introLabel.autosize();
		titleLabel.setText(title);
		introLabel.setText(intro);
		webView.getEngine().loadContent(CSS + htmlContent);
		authorLabel.setText(author);
		publishDateLabel.setText(publishDate);
		websiteLabel.setText(website);
		scrollPane.setVvalue(0);
		
		setReadNextArticle();
	}
	
    /**
     * Sets the latest articles in the small article views.
     */
	private void setLatestArticle() {
		Group[] latestArticle = new Group[] {smallArticle1, smallArticle2, smallArticle3, 
											 smallArticle4, smallArticle5};
		
		latestData = Model.getInstance().getLatest(latestArticle.length);
		
		ArticleSetter.setArrayArticleViews(latestArticle, latestData, ArticleSize.SMALL);
	}
	
    /**
     * Sets the "Read Next" articles in the big article views.
     */
	private void setReadNextArticle() {
		Group[] reedNextArticle = new Group[] {bigArticle1, bigArticle2, bigArticle3};
		
		randomData = Model.getInstance().getRandom(reedNextArticle.length);
		
		ArticleSetter.setArrayArticleViews(reedNextArticle, randomData, ArticleSize.BIG);
	}
	
    /**
     * Switches to the homepage.
     */
	@FXML
	private void switchToHomepage() {
		PresenterManager.getInstance().executeCommand(new HomepageCommand());
	}
	
    /**
     * Switches to the latest articles tab.
     */
	@FXML
	private void switchToLatestTab() {
		PresenterManager.getInstance().executeCommand(new LatestTabCommand());
	}
	
    /**
     * Switches to the selected article view based on click.
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
		
		List<Node> groupChildren = selectedGroup.getChildren();
		String indexCode = ((Text)(groupChildren.get(groupChildren.size() - 1))).getText();
		
		List<ArticleData> selectedList = (indexCode.charAt(0) == 'L') ? latestData : randomData;
		ArticleData selectedData = selectedList.get(indexCode.charAt(1) - '0');

		PresenterManager.getInstance().executeCommand(new ArticleTabCommand(selectedData));
    }
	
    /**
     * Returns to the previous scene.
     */
	@FXML
	private void returnScene() {
		PresenterManager.getInstance().returnCommand();
	}
	
    /**
     * Forwards to the next scene.
     */
	@FXML
	private void forwardScene() {
		PresenterManager.getInstance().forwardCommand();
	}
	
    /**
     * Opens the history window.
     */
	@FXML 
	private void openHistory() {
		PresenterManager.getInstance().openHistoryWindow();
	}
	
    /**
     * Opens the website.
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
	
    /**
     * Displays the summary of the article in the WebView.
     */
	@FXML
	private void openSummary() {
		contentToggleButton.setSelected(false);
		String summary = selectedArticle.getSUMMARY();
		webView.getEngine().loadContent(CSS + summary);
	}
	
    /**
     * Displays the full content of the article in the WebView.
     */
	@FXML
	private void openContent() {
		summaryToggleButton.setSelected(false);
		String content = selectedArticle.getHTML_CONTENT();
		webView.getEngine().loadContent(CSS + content);
	}
}
