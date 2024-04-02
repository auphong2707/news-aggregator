package com.newsaggregator.presenter;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
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
<<<<<<< Updated upstream
=======
<<<<<<< HEAD
    	window = primaryStage; 
        Parent root1 = FXMLLoader.load(getClass().getResource("homepage.fxml"));
        Parent root2 = FXMLLoader.load(getClass().getResource("searchtab.fxml"));
        homepage = new Scene(root1); 
        searchtab = new Scene(root2);
        primaryStage.setScene(homepage);
=======
>>>>>>> Stashed changes
        Parent root = FXMLLoader.load(getClass().getResource("searchtab.fxml"));
        Scene scene = new Scene(root);

        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        primaryStage.setScene(scene);
>>>>>>> 18bbfbc421abf60c38e93e919369d667826ba488
        primaryStage.setMaximized(true);
        primaryStage.show();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
}