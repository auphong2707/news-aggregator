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
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO  Auto-generated method stub


    try {
        Parent root = FXMLLoader.load(getClass().getResource("searchtab.fxml"));
        Scene scene = new Scene(root);

        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
//    	System.out.println(getClass().getResource("homepage.fxml"));
    } catch (Exception e) {
        e.printStackTrace();
    }
}
}