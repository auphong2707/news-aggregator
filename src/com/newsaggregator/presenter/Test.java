package com.newsaggregator.presenter;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.*;

public class Test extends Application {
	static Stage window;
	static Scene searchtab;
	static Scene homepage;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO  Auto-generated method stub


    try {
    	window = primaryStage; 
    	FXMLLoader searchtabLoader = new FXMLLoader(getClass().getResource("searchtab.fxml"));
        Parent root1 = FXMLLoader.load(getClass().getResource("homepage.fxml"));
        Parent root2 = searchtabLoader.load();
        homepage = new Scene(root1); 
        searchtab = new Scene(root2);
        primaryStage.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene == searchtab) {
                // Scene has changed, do something
                searchtabLoader.<SearchTabPresenter>getController().sceneSwitchInitialize();
            }
        });
        primaryStage.setScene(homepage);
        primaryStage.setMaximized(true);
        primaryStage.show();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
}