package com.newsaggregator.presenter;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import com.newsaggregator.model.ArticleData;
import com.newsaggregator.model.Model;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.scene.Group;
import javafx.scene.Node;


public class HomepagePresenter {
	@FXML private Label dateLabel;
	
	@FXML private Group smallArticle1;
	@FXML private Group smallArticle2;
	@FXML private Group smallArticle3;
	@FXML private Group smallArticle4;
	
	@FXML private Group notSoBigArticle1;
	@FXML private Group notSoBigArticle2;
	@FXML private Group notSoBigArticle3;
	@FXML private Group notSoBigArticle4;
	@FXML private Group notSoBigArticle5;
	@FXML private Group notSoBigArticle6;
	
	@FXML private Group mediumArticle1;
	
	@FXML private Group bigArticle1;
	@FXML private Group bigArticle2;
	
	@FXML private TextField searchBar;
	@FXML private Button searchButton;
	
	private Model model = new Model();
	
	private List<ArticleData> latestData;
	private List<ArticleData> randomData;
	
	@FXML
	public void initialize() throws IOException, InterruptedException {
		setDate();
		setLatestArticle();
		setRandomArticle();
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
		Group[] latestArticle = new Group[] {bigArticle1, bigArticle2, smallArticle1, 
											 smallArticle2, smallArticle3, smallArticle4};
		
		latestData = model.getLatestArticleData(latestArticle.length);
		
		for (int i = 0; i < latestArticle.length; i++) {
			int index = i;
			
			ArticleSize size = index < 2 ? ArticleSize.BIG : ArticleSize.SMALL;
			Thread thread = new Thread(() -> {
				try {
					Thread.sleep(0);
						Platform.runLater(() -> {
							PresenterTools.setArticleView(latestArticle[index], latestData.get(index), size);
						});
				} catch (InterruptedException ex) {
	                ex.printStackTrace();
	            }
			});
			
			thread.start();
		}
	} 
	
	private void setRandomArticle() {
		Group[] randomArticle = new Group[] {notSoBigArticle1, notSoBigArticle2, notSoBigArticle3,
											 notSoBigArticle4, notSoBigArticle5, notSoBigArticle6};
		
		randomData = model.getRandomArticleData(randomArticle.length);
		
		for (int i = 0; i < randomArticle.length; i++) {
			int index = i;
			Thread thread = new Thread(() -> {
				try {
					Thread.sleep(0);
						Platform.runLater(() -> {
							PresenterTools.setArticleView(randomArticle[index], randomData.get(index), ArticleSize.NOT_SO_BIG);
						});
				} catch (InterruptedException ex) {
	                ex.printStackTrace();
	            }
			});
			
			thread.start();
		}
	}
	
	@FXML
	private void searchByKey(KeyEvent key) throws IOException {
		if (key.getCode() == KeyCode.ENTER) {
			SceneVariables.getInstance().searchContent = searchBar.getText();
			switchToSearchtab();
		}
	}
	
	@FXML
	private void searchByButton() throws IOException {
		SceneVariables.getInstance().searchContent = searchBar.getText();
		switchToSearchtab();
	}
	
	private void switchToSearchtab() throws IOException {
		Test.window.setScene(Test.searchtab);
        Test.window.setWidth(Screen.getPrimary().getVisualBounds().getWidth());
        Test.window.setHeight(Screen.getPrimary().getVisualBounds().getHeight());
	}
	
	@FXML
	private void switchToArticle(MouseEvent event) throws IOException {
		Node clickedObject = (Node) event.getSource();
		Group selectedGroup;
		if (clickedObject.getClass() == Group.class) {
			selectedGroup = (Group) clickedObject;
		}
		else selectedGroup = (Group) clickedObject.getParent();
		String indexCode = ((Text)(selectedGroup.getChildren().getLast())).getText();
		
		List<ArticleData> selectedList = (indexCode.charAt(0) == 'L') ? latestData : randomData;
		SceneVariables.getInstance().selectedArticleData = selectedList.get(indexCode.charAt(1) - '0');
		
		
		Test.window.setScene(Test.article);
        Test.window.setWidth(Screen.getPrimary().getVisualBounds().getWidth());
        Test.window.setHeight(Screen.getPrimary().getVisualBounds().getHeight());
    }
}
