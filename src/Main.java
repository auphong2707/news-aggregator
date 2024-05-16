import com.newsaggregator.model.Model;
import com.newsaggregator.userinterface.*;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO  Auto-generated method stub
	    try {
	    	UIManager.getInstance().initialize(primaryStage);
	    	Runtime.getRuntime().addShutdownHook(new Thread() {
	    	    public void run() {
	    	    	UIManager.getInstance().onApplicationShutdown();
	    	    }
	    	});
	        
	        primaryStage.setTitle("News Alligator");
	        primaryStage.getIcons().add(new Image("file:///" + System.getProperty("user.dir") + "/resources/images/alligator.png"));
	        primaryStage.setScene(UIManager.getInstance().getCurrentScene());
	        primaryStage.setMaximized(true);
	        primaryStage.show();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
    }
    
}