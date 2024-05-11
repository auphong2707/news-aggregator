import java.io.IOException;

import com.newsaggregator.model.Model;
import com.newsaggregator.presenter.*;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
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
	    	SceneManager.getInstance().initialize(primaryStage);
	    	
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
	        primaryStage.setScene(SceneManager.getInstance().getCurrentScene());
	        primaryStage.setMaximized(true);
	        primaryStage.show();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
    }
    
}