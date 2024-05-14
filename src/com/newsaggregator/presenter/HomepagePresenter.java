package com.newsaggregator.presenter;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.newsaggregator.model.ArticleData;
import com.newsaggregator.model.Model;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class HomepagePresenter extends Presenter {
	@FXML private ScrollPane scrollPane;
	
	@FXML private Label dateLabel;
	@FXML private Label trendingLabel;
	@FXML private Label latestLabel;
	
	@FXML private Group smallArticle1;
	@FXML private Group smallArticle2;
	@FXML private Group smallArticle3;
	@FXML private Group smallArticle4;
	@FXML private Group smallArticle5;
	@FXML private Group smallArticle6;
	
	@FXML private Group notSoBigArticle1;
	@FXML private Group notSoBigArticle2;
	@FXML private Group notSoBigArticle3;
	@FXML private Group notSoBigArticle4;
	@FXML private Group notSoBigArticle5;
	@FXML private Group notSoBigArticle6;
	
	@FXML private Group notSoBigArticle1t;
	@FXML private Group notSoBigArticle2t;
	@FXML private Group notSoBigArticle3t;
	@FXML private Group notSoBigArticle4t;
	
	@FXML private Group mediumArticle1;
	
	@FXML private Group bigArticle1;
	@FXML private Group bigArticle2;
	
	@FXML private TextField searchBar;
	@FXML private Button searchButton;
	@FXML private Button returnButton;
	@FXML private Button forwardButton;
	@FXML private Button historyButton;
	
	@FXML private Label newspaper1;
	@FXML private Label newspaper2;
	@FXML private Label newspaper3;
	@FXML private Label newspaper4;
	@FXML private Label newspaper5;
	@FXML private Label newspaper6;
	@FXML private Label newspaper7;
	@FXML private Label newspaper8;
	
	private List<ArticleData> latestData;
	private List<ArticleData> randomData;
	private List<ArticleData> trendingData;
	
	
	@FXML
	public void initialize() throws IOException, InterruptedException {
		setDate();
		setLatestArticle();
		setRandomArticle();
		setTrendingArticle();
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
		latestData = Model.getInstance().getLatest(6);
		
		Group[] latestSmallArticle = new Group[] {smallArticle1, smallArticle2, smallArticle3,
				 								  smallArticle4, smallArticle5, smallArticle6};
		
		PresenterTools.setArrayArticleViews(latestSmallArticle, latestData, ArticleSize.SMALL);
	} 
	
	private void setRandomArticle() {
		Group[] randomArticle = new Group[] {notSoBigArticle1, notSoBigArticle2, notSoBigArticle3,
											 notSoBigArticle4, notSoBigArticle5, notSoBigArticle6};
		
		randomData = Model.getInstance().getRandom(randomArticle.length);
		
		PresenterTools.setArrayArticleViews(randomArticle, randomData, ArticleSize.NOT_SO_BIG);
	}
	
	private void setTrendingArticle() {
		Group[] trendingBigArticle = new Group[] {bigArticle1, bigArticle2};
		Group[] trendingNotSoBigArticle = new Group[] {notSoBigArticle1t, notSoBigArticle2t,
													   notSoBigArticle3t, notSoBigArticle4t};
		
		trendingData = Model.getInstance().getTrending(6);
		
		PresenterTools.setArrayArticleViews(trendingBigArticle, trendingData.subList(0, 2), ArticleSize.BIG);
		PresenterTools.setArrayArticleViews(trendingNotSoBigArticle, trendingData.subList(2, 6), ArticleSize.NOT_SO_BIG);
	}
	
	@FXML
	private void switchToTrendingTab() {
		SceneManager.getInstance().moveScene(SceneType.TRENDINGTAB, null);
	}
	
	@FXML
	private void switchToLatestTab() {
		SceneManager.getInstance().moveScene(SceneType.LATESTTAB, null);
	}
	
	@FXML
	private void searchByKey(KeyEvent key) throws IOException {
		if (key.getCode() == KeyCode.ENTER) {
			List<String> searchContent = Arrays.asList(searchBar.getText(), "All", "All");
			SceneManager.getInstance().moveScene(SceneType.SEARCHTAB, searchContent);
		}
	}
	
	@FXML
	private void searchByButton() throws IOException {
		List<String> searchContent = Arrays.asList(searchBar.getText(), "All", "All");
		SceneManager.getInstance().moveScene(SceneType.SEARCHTAB, searchContent);
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
		
		List<ArticleData> selectedList = null;
		if (indexCode.charAt(0) == 'L')
			selectedList = latestData;
		else if (indexCode.charAt(0) == 'R')
			selectedList = randomData;
		else if (indexCode.charAt(0) == 'T')
			selectedList = trendingData;
		
		ArticleData selectedData = selectedList.get(indexCode.charAt(1) - '0');
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
	
	@FXML
	public void refreshHomepage() {
		setRandomArticle();
		setTrendingArticle();
	}
	
	@FXML
	private void showWarningDialog(ActionEvent event) {
	    Alert alert = new Alert(Alert.AlertType.WARNING);
	    alert.setTitle("News Alligator");
	    alert.setHeaderText("Warning");
	    alert.setContentText("This will reset everything! Would you like to continue?");
	    alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
	    Optional<ButtonType> result = alert.showAndWait();

	    if (result.isPresent() && result.get() == ButtonType.OK) {
	    	showLoadingScreen();
	    	
	        Task<Void> task = new Task<Void>() {
	            @Override
	            protected Void call() throws Exception {
	                Model.getInstance().aggregateNewData();
	            	return null;
	            }
	        };
	        
	        task.setOnSucceeded(e -> hideLoadingScreen());

	        new Thread(task).start();
	    }
	}
	
	  private void showLoadingScreen() {
	        try {
	            FXMLLoader loader = new FXMLLoader(getClass().getResource("loadingscreen.fxml"));
	            Parent root = loader.load();
	            Stage stage = new Stage();
	            stage.initModality(Modality.APPLICATION_MODAL);
	            stage.setScene(new Scene(root));
	            stage.show();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

	    private void hideLoadingScreen() {
	    	System.exit(0);
	    }
	    
	@Override
	void sceneSwitchInitialize() {
		searchBar.clear();
		
		scrollPane.setVvalue(0);
	}
}
