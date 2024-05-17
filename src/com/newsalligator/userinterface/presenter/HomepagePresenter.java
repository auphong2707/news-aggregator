package com.newsalligator.userinterface.presenter;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.newsalligator.model.ArticleData;
import com.newsalligator.model.Model;
import com.newsalligator.userinterface.UIManager;
import com.newsalligator.userinterface.command.*;
import com.newsalligator.userinterface.tools.ArticleSetter;
import com.newsalligator.userinterface.uienum.ArticleSize;

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
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
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
	
	private List<ArticleData> latestData;
	private List<ArticleData> randomData;
	private List<ArticleData> trendingData;
	
	
	@FXML
	public void initialize() {
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
		
		ArticleSetter.setArrayArticleViews(latestSmallArticle, latestData, ArticleSize.SMALL);
	} 
	
	private void setRandomArticle() {
		Group[] randomArticle = new Group[] {notSoBigArticle1, notSoBigArticle2, notSoBigArticle3,
											 notSoBigArticle4, notSoBigArticle5, notSoBigArticle6};
		
		randomData = Model.getInstance().getRandom(randomArticle.length);
		
		ArticleSetter.setArrayArticleViews(randomArticle, randomData, ArticleSize.NOT_SO_BIG);
	}
	
	private void setTrendingArticle() {
		Group[] trendingBigArticle = new Group[] {bigArticle1, bigArticle2};
		Group[] trendingNotSoBigArticle = new Group[] {notSoBigArticle1t, notSoBigArticle2t,
													   notSoBigArticle3t, notSoBigArticle4t};
		
		trendingData = Model.getInstance().getTrending(6);
		
		ArticleSetter.setArrayArticleViews(trendingBigArticle, trendingData.subList(0, 2), ArticleSize.BIG);
		ArticleSetter.setArrayArticleViews(trendingNotSoBigArticle, trendingData.subList(2, 6), ArticleSize.NOT_SO_BIG);
	}
	
	@FXML
	private void switchToTrendingTab() {
		UIManager.getInstance().executeCommand(new TrendingTabCommand());
	}
	
	@FXML
	private void switchToLatestTab() {
		UIManager.getInstance().executeCommand(new LatestTabCommand());
	}
	
	@FXML
	private void switchTCategoryTab(MouseEvent event) {
		String category = ((Label) event.getSource()).getText();
		UIManager.getInstance().executeCommand(new CategoryTabCommand(category));
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
	private void switchToArticle(MouseEvent event) {
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
	
	@FXML
	private void refreshHomepage() {
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
            FXMLLoader loader = new FXMLLoader((new File("resources/views/loadingscreen.fxml").toURI().toURL()));
            Parent root = loader.load();
            Stage stage = new Stage();
            
            stage.setTitle("Aggregating data...");
            stage.getIcons().add(new Image("file:///" + System.getProperty("user.dir") + "/resources/images/alligator.png"));
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
	public void sceneSwitchInitialize() {
		searchBar.clear();
		
		scrollPane.setVvalue(0);
	}
}
