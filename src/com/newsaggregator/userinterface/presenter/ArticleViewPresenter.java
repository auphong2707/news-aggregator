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
import com.newsaggregator.userinterface.command.*;
import com.newsaggregator.userinterface.tools.ArticleSetter;
import com.newsaggregator.userinterface.uienum.ArticleSize;

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

public class ArticleViewPresenter extends Presenter {
	@FXML private ScrollPane scrollPane;
	
	@FXML private Button historyButton;
	@FXML private Button returnButton;
	@FXML private Button forwardButton;
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
	
	@FXML private Label newspaper1;
	@FXML private Label newspaper2;
	@FXML private Label newspaper3;
	@FXML private Label newspaper4;
	@FXML private Label newspaper5;
	@FXML private Label newspaper6;
	@FXML private Label newspaper7;
	@FXML private Label newspaper8;
	
	ArticleData selectedArticle;
	private List<ArticleData> latestData;
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
	
	@FXML
	void initialize() throws IOException, InterruptedException {
		setDate();
		setLatestArticle();
	}
	
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
		selectedArticle = (ArticleData) UIManager.getInstance().getCurrentCommandValue();
		
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
	
	private void setLatestArticle() {
		Group[] latestArticle = new Group[] {smallArticle1, smallArticle2, smallArticle3, 
											 smallArticle4, smallArticle5};
		
		latestData = Model.getInstance().getLatest(latestArticle.length);
		
		ArticleSetter.setArrayArticleViews(latestArticle, latestData, ArticleSize.SMALL);
	}
	
	private void setReadNextArticle() {
		Group[] reedNextArticle = new Group[] {bigArticle1, bigArticle2, bigArticle3};
		
		randomData = Model.getInstance().getRandom(reedNextArticle.length);
		
		ArticleSetter.setArrayArticleViews(reedNextArticle, randomData, ArticleSize.BIG);
	}
	
	@FXML
	private void switchToHomepage() throws IOException {
		UIManager.getInstance().addCommand(new HomepageCommand());
	}
	
	@FXML
	private void switchToLatestTab() {
		UIManager.getInstance().addCommand(new LatestTabCommand());
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
		
		List<ArticleData> selectedList = (indexCode.charAt(0) == 'L') ? latestData : randomData;
		ArticleData selectedData = selectedList.get(indexCode.charAt(1) - '0');

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
	
	@FXML
	private void openSummary() {
		contentToggleButton.setSelected(false);
		String summary = selectedArticle.getSUMMARY();
		webView.getEngine().loadContent(CSS + summary);
	}
	
	@FXML
	private void openContent() {
		summaryToggleButton.setSelected(false);
		String content = selectedArticle.getHTML_CONTENT();
		webView.getEngine().loadContent(CSS + content);
	}
}
