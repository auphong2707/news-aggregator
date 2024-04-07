package com.newsaggregator.presenter;

import java.io.IOException;

import com.newsaggregator.model.Model;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.fxml.*;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;

public class Test extends Application {
	static Stage window;
	static Scene searchtab;
	static Scene homepage;
    public static void main(String[] args) {
        launch(args);
    }
    
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		
	    try {
	    	window = primaryStage;
	    	
	    	FXMLLoader articleviewLoader = new FXMLLoader(getClass().getResource("articleview.fxml"));

	    	Parent root = articleviewLoader.load();
	        
	        Scene articleview = new Scene(root);
	        
	        
	        primaryStage.setTitle("News Alligator");
	        primaryStage.getIcons().add(new Image("file:///" + System.getProperty("user.dir") + "/images/alligator.png"));
	        primaryStage.setScene(articleview);
	        primaryStage.setMaximized(true);
	        primaryStage.show();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
    
    /*
    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO  Auto-generated method stub
    
    	Model.runLocalServer();

	    try {
	    	window = primaryStage;
	    	
	    	FXMLLoader searchtabLoader = new FXMLLoader(getClass().getResource("searchtab.fxml"));
	    	FXMLLoader homepageLoader = new FXMLLoader(getClass().getResource("homepage.fxml"));
	    	
	        Parent root1 = homepageLoader.load();
	        Parent root2 = searchtabLoader.load();
	        
	        homepage = new Scene(root1); 
	        searchtab = new Scene(root2);
	        
	        primaryStage.sceneProperty().addListener((obs, oldScene, newScene) -> {
	            if (newScene == searchtab) {
	                // Scene has changed, do something
	                searchtabLoader.<SearchTabPresenter>getController().sceneSwitchInitialize();
	            }
	        });
	        
	        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
	            @Override
	            public void handle(WindowEvent event) {
	            	System.out.println("Server's terminated");
	        		try {
						Model.terminateLocalServer();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	                System.out.println("Closing application...");
	            }
	        });
	        
	        primaryStage.setTitle("News Alligator");
	        primaryStage.getIcons().add(new Image("file:///" + System.getProperty("user.dir") + "/images/alligator.png"));
	        primaryStage.setScene(homepage);
	        primaryStage.setMaximized(true);
	        primaryStage.show();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
    }
    */
}