package com.newsaggregator.presenter;

import java.io.IOException;
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
	
    String searchContent;
    ArticleData selectedArticleData;
    
	private Stage window;
	private Scene[] scenes;
	private Presenter[] presenters;
	private SceneType currentSceneType;
	
	Stack<Pair<SceneType, Object>> history = new Stack<Pair<SceneType, Object>>();
	
	public void initialize(Stage window) throws IOException {
		this.window = window;
		
		FXMLLoader searchtabLoader = new FXMLLoader(getClass().getResource("searchtab.fxml"));
    	FXMLLoader homepageLoader = new FXMLLoader(getClass().getResource("homepage.fxml"));
    	FXMLLoader articleLoader = new FXMLLoader(getClass().getResource("articleview.fxml"));
        
        Scene homepage = new Scene(homepageLoader.load()); 
        Scene searchtab = new Scene(searchtabLoader.load());
        Scene article = new Scene(articleLoader.load());
        
        
        scenes = new Scene[] {homepage, searchtab, article};
        presenters = new Presenter[] {
        	homepageLoader.<Presenter>getController(),
        	searchtabLoader.<Presenter>getController(),
        	articleLoader.<Presenter>getController()
        };
        
        currentSceneType = SceneType.HOMEPAGE;
	}
	
	public Scene getCurrentScene() {
		return scenes[currentSceneType.ordinal()];
	}

	private void switchScene(SceneType nextSceneType) {
		Scene nextScene = scenes[nextSceneType.ordinal()];
		Presenter nextPresenter = presenters[nextSceneType.ordinal()];
		
		nextPresenter.sceneSwitchInitialize();
		currentSceneType = nextSceneType;
		
		window.setScene(nextScene);
        window.setWidth(Screen.getPrimary().getVisualBounds().getWidth());
        window.setHeight(Screen.getPrimary().getVisualBounds().getHeight());
	}
	
	void moveScene(SceneType nextSceneType, Object object) {
		history.push(new Pair<SceneType, Object>(
		    currentSceneType,
		    switch (currentSceneType) {
		        case SEARCHTAB -> searchContent;
		        case ARTICLE_VIEW -> selectedArticleData;
		        default -> null; 
		    }
		));

		currentSceneType = nextSceneType;
		if (currentSceneType == SceneType.SEARCHTAB) {
			searchContent = (String) object;
		} else if (currentSceneType == SceneType.ARTICLE_VIEW) {
			selectedArticleData = (ArticleData) object;
		}
		
		switchScene(currentSceneType);
	}
		
	void returnScene() {
		if (history.size() > 0) {
			Pair<SceneType, Object> lastPage = history.pop();
			
			currentSceneType = lastPage.getKey();
			if (currentSceneType == SceneType.SEARCHTAB) {
				searchContent = (String) lastPage.getValue();
			} else if (currentSceneType == SceneType.ARTICLE_VIEW) {
				selectedArticleData = (ArticleData) lastPage.getValue();
			}
			
			switchScene(currentSceneType);
		}
	}	
}
