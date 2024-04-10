package com.newsaggregator.presenter;

import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class SceneManager {
	private static Stage window;
	
	private static Scene[] scenes;
	
	public static void setWindow(Stage window) {
		SceneManager.window = window;
	}
	
	public static void setScenes(Scene[] scenes) {
		SceneManager.scenes = scenes;
	}

	static void switchScene(SceneType scene) {
		Scene nextScene = scenes[scene.ordinal()];
		
		window.setScene(nextScene);
        window.setWidth(Screen.getPrimary().getVisualBounds().getWidth());
        window.setHeight(Screen.getPrimary().getVisualBounds().getHeight());
	}
}
