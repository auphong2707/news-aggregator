package com.newsaggregator.presenter;

import java.io.IOException;
import java.util.Stack;

import com.newsaggregator.model.ArticleData;

import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Pair;

public class SceneManager {
	private static SceneManager instance;
    private SceneManager() { }

    public static SceneManager getInstance() {
        if(instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }
	
    private String searchContent;
    private ArticleData selectedArticleData;
    
	private Stage window;
	private Scene[] scenes;
	private Presenter[] presenters;
	private SceneType currentSceneType;
	
	Stage historyWindow = new Stage();
	//private Scene history;
	VBox box = new VBox();

	Stack<Pair<SceneType, Object>> backHistory = new Stack<Pair<SceneType, Object>>();
	Stack<Pair<SceneType, Object>> forwardHistory = new Stack<Pair<SceneType, Object>>();
	Stack<Pair<SceneType, Object>> webHistory = new Stack<Pair<SceneType, Object>>();
	
	public void initialize(Stage window) throws IOException {
		this.window = window;
		
		historyWindow.initStyle(StageStyle.UNDECORATED);
		historyWindow.setX(10);
		historyWindow.setY(100);

		FXMLLoader searchtabLoader = new FXMLLoader(getClass().getResource("searchtab.fxml"));
    	FXMLLoader homepageLoader = new FXMLLoader(getClass().getResource("homepage.fxml"));
    	FXMLLoader articleLoader = new FXMLLoader(getClass().getResource("articleview.fxml"));
    	FXMLLoader trendingLoader = new FXMLLoader(getClass().getResource("trendingtab.fxml"));
    	FXMLLoader latestLoader = new FXMLLoader(getClass().getResource("latesttab.fxml"));
    	//FXMLLoader historyLoader = new FXMLLoader(getClass().getResource("historytab.fxml"));
        
        Scene homepage = new Scene(homepageLoader.load()); 
        Scene searchtab = new Scene(searchtabLoader.load());
        Scene article = new Scene(articleLoader.load());
        Scene trending = new Scene(trendingLoader.load());
        Scene latest = new Scene(latestLoader.load());
        //history = new Scene(historyLoader.load());
        
        scenes = new Scene[] {homepage, searchtab, article, trending, latest};
        presenters = new Presenter[] {
        	homepageLoader.<Presenter>getController(),
        	searchtabLoader.<Presenter>getController(),
        	articleLoader.<Presenter>getController(),
        	trendingLoader.<Presenter>getController(),
        	latestLoader.<Presenter>getController()
        };
        
        currentSceneType = SceneType.HOMEPAGE;
        
        addHistory(webHistory);
        box.getChildren().add(visitedWeb());
	}
	
	public Scene getCurrentScene() {
		return scenes[currentSceneType.ordinal()];
	}
	
	public String getSearchContent() {
		return searchContent;
	}

	public ArticleData getSelectedArticleData() {
		return selectedArticleData;
	}
	
	private void updateSceneVariables(SceneType sceneType, Object information) {
		currentSceneType = sceneType;
		if (currentSceneType == SceneType.SEARCHTAB) {
			searchContent = (String) information;
		} else if (currentSceneType == SceneType.ARTICLE_VIEW) {
			selectedArticleData = (ArticleData) information;
		}
	}

	private void switchScene(SceneType nextSceneType) {
		System.gc();
		
		Scene nextScene = scenes[nextSceneType.ordinal()];
		Presenter nextPresenter = presenters[nextSceneType.ordinal()];
		
		currentSceneType = nextSceneType;
		
		addHistory(webHistory);
		box.getChildren().add(0, visitedWeb());
		
		window.setScene(nextScene);
        window.setWidth(Screen.getPrimary().getVisualBounds().getWidth());
        window.setHeight(Screen.getPrimary().getVisualBounds().getHeight());
        
        nextPresenter.sceneSwitchInitialize();
	}
	
	void moveScene(SceneType nextSceneType, Object information) {
		addHistory(backHistory);
		forwardHistory.clear();

		updateSceneVariables(nextSceneType, information);
		
		switchScene(currentSceneType);
	}
		
	void returnScene() {
		if (backHistory.size() > 0) {
			addHistory(forwardHistory);
			Pair<SceneType, Object> lastPage = backHistory.pop();
			
			updateSceneVariables(lastPage.getKey(), lastPage.getValue());
			
			switchScene(currentSceneType);
		}
	}
	
	void forwardScene() {
		if (forwardHistory.size() > 0) {
			Pair<SceneType, Object> nextPage = forwardHistory.pop();
			addHistory(backHistory);
			
			updateSceneVariables(nextPage.getKey(), nextPage.getValue());
			
			switchScene(currentSceneType);
		}
	}
	
	private void addHistory(Stack<Pair<SceneType, Object>> history) {
		history.push(new Pair<SceneType, Object>(
			    currentSceneType,
			    switch (currentSceneType) {
			        case SEARCHTAB -> searchContent;
			        case ARTICLE_VIEW -> selectedArticleData;
			        default -> null; 
			    }
			));
	}
	
	Label visitedWeb() {
		Pair<SceneType, Object> currentWeb = webHistory.pop();
		String webContent = new String();
		if (currentWeb.getKey()== SceneType.ARTICLE_VIEW) {
			ArticleData information = (ArticleData) currentWeb.getValue();
			webContent = " : " + information.getTITLE();  
		} else if (currentWeb.getKey()== SceneType.SEARCHTAB) {
			webContent = " : " + currentWeb.getValue();
		} else {
			webContent = "";
		}
		
		Label web = new Label(currentWeb.getKey() + webContent);
		web.setCursor(Cursor.HAND);
		web.setOnMouseClicked(e -> {
            moveScene(currentWeb.getKey(), currentWeb.getValue());
        });
		return web;
	}
	
	void openHistory() {
		BorderPane root = new BorderPane();
        root.setPrefSize(300, 500);

        StackPane centerPane = new StackPane();
        root.setCenter(centerPane);

        AnchorPane anchorPane = new AnchorPane();
        centerPane.getChildren().add(anchorPane);
        StackPane.setAlignment(anchorPane, javafx.geometry.Pos.CENTER);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefSize(300, 450);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        anchorPane.getChildren().add(scrollPane);
        AnchorPane.setTopAnchor(scrollPane, 50.0);

        AnchorPane innerAnchorPane = new AnchorPane();
        scrollPane.setContent(innerAnchorPane);

        box.setPrefSize(270, 993);
        innerAnchorPane.getChildren().add(box);
        AnchorPane.setLeftAnchor(box, 7.0);
        AnchorPane.setTopAnchor(box, 1.0);

        Label label = new Label("WEB BROWSING HISTORY");
        label.setFont(new Font("System Bold", 18.0));
        anchorPane.getChildren().add(label);
        AnchorPane.setLeftAnchor(label, 45.0);
        AnchorPane.setTopAnchor(label, 13.0);

        Button closeButton = new Button("X");
        closeButton.setPrefSize(25, 26);
        closeButton.setOnAction(e -> {
        	historyWindow.close();
        });
        anchorPane.getChildren().add(closeButton);
        AnchorPane.setTopAnchor(closeButton, -2.0);

        Scene scene = new Scene(root);
        historyWindow.setScene(scene);
        historyWindow.show();
    }

}
