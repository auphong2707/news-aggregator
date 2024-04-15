package com.newsaggregator.presenter;

import com.newsaggregator.model.ArticleData;

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
	
	public void setWindow(Stage window) {
		this.window = window;
	}
	
	public void setScenes(Scene[] scenes) {
		this.scenes = scenes;
	}

	void switchScene(SceneType scene) {
		Scene nextScene = scenes[scene.ordinal()];
		
		window.setScene(nextScene);
        window.setWidth(Screen.getPrimary().getVisualBounds().getWidth());
        window.setHeight(Screen.getPrimary().getVisualBounds().getHeight());
	}
}
