package com.newsaggregator.presenter;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import com.newsaggregator.model.ArticleData;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
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

	Stack<Pair<SceneType, Object>> backHistory = new Stack<Pair<SceneType, Object>>();
	Stack<Pair<SceneType, Object>> forwardHistory = new Stack<Pair<SceneType, Object>>();
	Queue<Pair<SceneType, Object>> webHistory = new LinkedList<Pair<SceneType, Object>>();
	
	public void initialize(Stage window) throws IOException {
		this.window = window;
		
		HistoryWindow.getInstance().initialize();

		FXMLLoader searchtabLoader = new FXMLLoader(getClass().getResource("searchtab.fxml"));
    	FXMLLoader homepageLoader = new FXMLLoader(getClass().getResource("homepage.fxml"));
    	FXMLLoader articleLoader = new FXMLLoader(getClass().getResource("articleview.fxml"));
    	FXMLLoader trendingLoader = new FXMLLoader(getClass().getResource("trendingtab.fxml"));
    	FXMLLoader latestLoader = new FXMLLoader(getClass().getResource("latesttab.fxml"));
        
        Scene homepage = new Scene(homepageLoader.load()); 
        Scene searchtab = new Scene(searchtabLoader.load());
        Scene article = new Scene(articleLoader.load());
        Scene trending = new Scene(trendingLoader.load());
        Scene latest = new Scene(latestLoader.load());
        
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
		
		window.setScene(nextScene);
        window.setWidth(Screen.getPrimary().getVisualBounds().getWidth());
        window.setHeight(Screen.getPrimary().getVisualBounds().getHeight());
        
        nextPresenter.sceneSwitchInitialize();
        
        HistoryWindow.getInstance().closeWindow();
	}
	
	public void moveScene(SceneType nextSceneType, Object information) {
		addHistory(backHistory);
		forwardHistory.clear();

		updateSceneVariables(nextSceneType, information);
		
		switchScene(currentSceneType);
	}
		
	public void returnScene() {
		if (backHistory.size() > 0) {
			addHistory(forwardHistory);
			Pair<SceneType, Object> lastPage = backHistory.pop();
			
			updateSceneVariables(lastPage.getKey(), lastPage.getValue());
			
			switchScene(currentSceneType);
		}
	}
	
	public void forwardScene() {
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
	
	private void addHistory(Queue<Pair<SceneType, Object>> history) {
		history.offer(new Pair<SceneType, Object>(
			    currentSceneType,
			    switch (currentSceneType) {
			        case SEARCHTAB -> searchContent;
			        case ARTICLE_VIEW -> selectedArticleData;
			        default -> null; 
			    }
			));
	}
}
