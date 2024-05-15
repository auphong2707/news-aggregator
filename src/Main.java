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
	    	SceneManager.getInstance().initialize(primaryStage);
	    	Runtime.getRuntime().addShutdownHook(new Thread() {
	    	    public void run() {
	    	    	Model.getInstance().terminateLocalServer();
	    	    }
	    	});
	        
	        primaryStage.setTitle("News Alligator");
	        primaryStage.getIcons().add(new Image("file:///" + System.getProperty("user.dir") + "/resources/images/alligator.png"));
	        primaryStage.setScene(SceneManager.getInstance().getCurrentScene());
	        primaryStage.setMaximized(true);
	        primaryStage.show();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
    }
    
}