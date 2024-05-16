package com.newsaggregator.userinterface;

import java.util.Queue;

import com.newsaggregator.userinterface.command.Command;
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
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
    Queue<Command> webHistory = SceneManager.getInstance().webHistory;
    private VBox vbox = new VBox();
	
	public void initialize() {
		historyWindow.initStyle(StageStyle.UNDECORATED);
	      
		historyWindow.setX(Screen.getPrimary().getVisualBounds().getMaxX() - 400);
		historyWindow.setY(Screen.getPrimary().getVisualBounds().getMinY() + 100);
		
        historyWindow.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (! isNowFocused) {
                historyWindow.close();
            }
        });
	}
	
	private Label visitedWeb() {
		Command currentWebCommand = webHistory.poll();
		
		
		Label web = new Label("  <> " + currentWebCommand.getName());
		web.setCursor(Cursor.HAND);
		web.setWrapText(true);
		web.setOnMouseEntered(e -> {
			web.setStyle("-fx-font-weight: bold; -fx-underline: true;");
		});
		web.setOnMouseExited(e -> {
			web.setStyle("");
		});
		web.setOnMouseClicked(e -> {
            SceneManager.getInstance().addCommand(currentWebCommand);
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
		
		historyWindow.show();
	}
}
