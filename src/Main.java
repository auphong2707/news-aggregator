import java.io.IOException;

import com.newsaggregator.model.Model;
import com.newsaggregator.presenter.*;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.fxml.*;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO  Auto-generated method stub
    
    	Model.runLocalServer();
	    try {
	    	SceneManager.getInstance().setWindow(primaryStage);
	    	
	    	String directory = "/com/newsaggregator/presenter/";
	    	
	    	FXMLLoader searchtabLoader = new FXMLLoader(getClass().getResource(directory + "searchtab.fxml"));
	    	FXMLLoader homepageLoader = new FXMLLoader(getClass().getResource(directory + "homepage.fxml"));
	    	FXMLLoader articleLoader = new FXMLLoader(getClass().getResource(directory + "articleview.fxml"));
	    	
	        Parent root1 = homepageLoader.load();
	        Parent root2 = searchtabLoader.load();
	        Parent root3 = articleLoader.load();
	        
	        Scene homepage = new Scene(root1); 
	        Scene searchtab = new Scene(root2);
	        Scene article = new Scene(root3);
	        SceneManager.getInstance().setScenes(new Scene[] {
	        		homepage, 
	        		searchtab, 
	        		article
	        		});
	        
	        primaryStage.sceneProperty().addListener((obs, oldScene, newScene) -> {
	            if (newScene == searchtab) {
	                // Scene has changed, do something
	                searchtabLoader.<SearchTabPresenter>getController().sceneSwitchInitialize();
	            } else if (newScene == article) {
	            	articleLoader.<ArticleViewPresenter>getController().sceneSwitchInitialize();
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
    
}