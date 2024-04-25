package com.newsaggregator.presenter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.newsaggregator.model.ArticleData;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

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
	private Scene currentScene;
	
	List<SceneType> sceneHistory = new ArrayList<SceneType>();
	List<ArticleData> dataHistory = new ArrayList<ArticleData>();
	List<String> searchHistory = new ArrayList<String>();
	
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
        
        currentScene = homepage;
        sceneHistory.add(SceneType.HOMEPAGE);
	}
	
	public Scene getCurrentScene() {
		return currentScene;
	}

	void switchScene(SceneType scene) {
		Scene nextScene = scenes[scene.ordinal()];
		Presenter nextPresenter = presenters[scene.ordinal()];
		
		nextPresenter.sceneSwitchInitialize();
		currentScene = nextScene;
		sceneHistory.add(scene);
		System.out.println(sceneHistory);
		
		window.setScene(nextScene);
        window.setWidth(Screen.getPrimary().getVisualBounds().getWidth());
        window.setHeight(Screen.getPrimary().getVisualBounds().getHeight());
	}
		
	void returnScene() {
		if (sceneHistory.size() > 1) {
			sceneHistory.remove(sceneHistory.size() - 1);
			SceneType scene = sceneHistory.get(sceneHistory.size()-1);
			Scene nextScene = scenes[scene.ordinal()];
			Presenter nextPresenter = presenters[scene.ordinal()];
			
			nextPresenter.sceneReturnInitialize();
			currentScene = nextScene;
			System.out.println(sceneHistory);
			
			window.setScene(nextScene);
	        window.setWidth(Screen.getPrimary().getVisualBounds().getWidth());
	        window.setHeight(Screen.getPrimary().getVisualBounds().getHeight());
		}
	}
	
	@SuppressWarnings("rawtypes")
	void removeElement(List list) {
		if (list.size() > 1) {
			list.remove(list.get(list.size()-1));
		} 
	}
	
}
