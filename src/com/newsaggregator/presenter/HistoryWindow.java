package com.newsaggregator.presenter;

import java.util.Queue;

import com.newsaggregator.model.ArticleData;

import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Pair;

public class HistoryWindow {
	private static HistoryWindow instance;
    private HistoryWindow() { }

    public static HistoryWindow getInstance() {
        if(instance == null) {
            instance = new HistoryWindow();
        }
        return instance;
    }
	
	public Stage historyWindow = new Stage();
	private boolean isOpening = false;
    Queue<Pair<SceneType, Object>> webHistory = SceneManager.getInstance().webHistory;
    private VBox vbox = new VBox();
	
	public void initialize() {
		historyWindow.initStyle(StageStyle.UNDECORATED);
		historyWindow.setX(1200);
		historyWindow.setY(100);
	}
	
	private Label visitedWeb() {
		Pair<SceneType, Object> currentWeb = webHistory.poll();
		String webContent = new String();
		if (currentWeb.getKey()== SceneType.ARTICLE_VIEW) {
			ArticleData information = (ArticleData) currentWeb.getValue();
			webContent = " : " + information.getTITLE();  
		} else if (currentWeb.getKey()== SceneType.SEARCHTAB) {
			webContent = " : " + currentWeb.getValue();
		} else {
			webContent = "";
		}
		
		Label web = new Label("  <> " + currentWeb.getKey() + webContent);
		web.setCursor(Cursor.HAND);
		web.setWrapText(true);
		web.setOnMouseEntered(e -> {
			web.setStyle("-fx-font-weight: bold; -fx-underline: true;");
		});
		web.setOnMouseExited(e -> {
			web.setStyle("");
		});
		web.setOnMouseClicked(e -> {
            SceneManager.getInstance().moveScene(currentWeb.getKey(), currentWeb.getValue());
        });
		
		return web;
	}
	
	private void constructWindow() {
		BorderPane root = new BorderPane();
        root.setPrefSize(300, 400);

        StackPane centerPane = new StackPane();
        root.setCenter(centerPane);

        AnchorPane anchorPane = new AnchorPane();
        centerPane.getChildren().add(anchorPane);
        StackPane.setAlignment(anchorPane, javafx.geometry.Pos.CENTER);
        
        Line line = new Line(0, 0, 300, 0);
        line.setStyle("-fx-stroke: black; -fx-stroke-width: 4;");
        Line line1 = new Line(1, 0, 1, 50);
        line.setStyle("-fx-stroke: black; -fx-stroke-width: 4;");
        Line line2 = new Line(302, 0, 302, 50);
        line.setStyle("-fx-stroke: black; -fx-stroke-width: 4;");
        Label label = new Label("WEB BROWSING HISTORY");
        label.setFont(new Font("System Bold", 18.0));
        anchorPane.getChildren().addAll(label, line, line1, line2);
        AnchorPane.setLeftAnchor(label, 45.0);
        AnchorPane.setTopAnchor(label, 13.0);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefSize(300, 350);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        anchorPane.getChildren().add(scrollPane);
        AnchorPane.setTopAnchor(scrollPane, 50.0);

        AnchorPane innerAnchorPane = new AnchorPane();        
        innerAnchorPane.autosize();
        scrollPane.setContent(innerAnchorPane);
        
        while (!webHistory.isEmpty()) {
        	vbox.getChildren().add(0, visitedWeb());
        }
        vbox.setPrefWidth(270);
        vbox.autosize();
        innerAnchorPane.getChildren().add(vbox);
        AnchorPane.setLeftAnchor(vbox, 10.0);
        AnchorPane.setTopAnchor(vbox, 1.0);

        Scene scene = new Scene(root);
        historyWindow.setScene(scene);
	}
	
	public void switchWindow() {
		constructWindow();
		
		if (isOpening == false) {
			historyWindow.show();
			isOpening = true;
		} else {
			closeWindow();
		}
	}
	
	public void closeWindow() {
		historyWindow.close();
		isOpening = false;
	}
}
