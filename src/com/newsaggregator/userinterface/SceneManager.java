package com.newsaggregator.userinterface;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import com.newsaggregator.model.ArticleData;
import com.newsaggregator.userinterface.presenter.Presenter;
import com.newsaggregator.userinterface.uienum.SceneType;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Pair;
import java.io.File;

public class SceneManager {
	private static final String VIEW_PATH = "resources/views/";
	private static SceneManager instance;
    private SceneManager() { }

    public static SceneManager getInstance() {
        if(instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }
	
    private List<String> searchContent;
    private String categoryName;
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

		FXMLLoader searchtabLoader = new FXMLLoader((new File(VIEW_PATH + "searchtab.fxml").toURI().toURL()));
    	FXMLLoader homepageLoader = new FXMLLoader(new File(VIEW_PATH + "homepage.fxml").toURI().toURL());
    	FXMLLoader articleLoader = new FXMLLoader(new File(VIEW_PATH + "articleview.fxml").toURI().toURL());
    	FXMLLoader trendingLoader = new FXMLLoader(new File(VIEW_PATH + "trendingtab.fxml").toURI().toURL());
    	FXMLLoader latestLoader = new FXMLLoader(new File(VIEW_PATH + "latesttab.fxml").toURI().toURL());
    	FXMLLoader categoryLoader = new FXMLLoader(new File(VIEW_PATH + "categorytab.fxml").toURI().toURL());
        
        Scene homepage = new Scene(homepageLoader.load()); 
        Scene searchtab = new Scene(searchtabLoader.load());
        Scene article = new Scene(articleLoader.load());
        Scene trending = new Scene(trendingLoader.load());
        Scene latest = new Scene(latestLoader.load());
        Scene category = new Scene(categoryLoader.load());
        
        scenes = new Scene[] {homepage, searchtab, article, trending, latest, category};
        presenters = new Presenter[] {
        	homepageLoader.<Presenter>getController(),
        	searchtabLoader.<Presenter>getController(),
        	articleLoader.<Presenter>getController(),
        	trendingLoader.<Presenter>getController(),
        	latestLoader.<Presenter>getController(),
        	categoryLoader.<Presenter>getController()
        };
        
        currentSceneType = SceneType.HOMEPAGE;
        
        addHistory(webHistory);
	}
	
	public Scene getCurrentScene() {
		return scenes[currentSceneType.ordinal()];
	}
	
	public List<String> getSearchContent() {
		return searchContent;
	}

	public ArticleData getSelectedArticleData() {
		return selectedArticleData;
	}
	

	public String getCategoryName() {
		return categoryName;
	}
	
  @SuppressWarnings("unchecked")    
	private void updateSceneVariables(SceneType sceneType, Object information) {
		currentSceneType = sceneType;
		if (currentSceneType == SceneType.SEARCHTAB) {
			searchContent = (List<String>) information;
		} else if (currentSceneType == SceneType.CATEGORYTAB) {
			categoryName = (String) information;
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
        
        HistoryWindow.getInstance().historyWindow.close();
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
			        case CATEGORYTAB -> categoryName;
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
			        case CATEGORYTAB -> categoryName;
			        case ARTICLE_VIEW -> selectedArticleData;
			        default -> null; 
			    }
			));
	}
}
