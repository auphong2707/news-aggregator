package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}
		@Override
	public void start(Stage primaryStage) throws Exception {
			// TODO  Auto-generated method stub

	
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/application/homepage.fxml"));
			Scene scene = new Scene(root);
		
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setMaximized(true);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}