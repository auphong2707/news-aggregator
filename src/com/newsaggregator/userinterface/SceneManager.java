package com.newsaggregator.userinterface;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import com.newsaggregator.model.ArticleData;
import com.newsaggregator.userinterface.command.Command;
import com.newsaggregator.userinterface.command.HomepageCommand;
import com.newsaggregator.userinterface.presenter.Presenter;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Pair;
import java.io.File;

public class SceneManager {
	private static final String VIEW_PATH = "resources/views/";
	private static SceneManager instance;
    private SceneManager() { }

    public static SceneManager getInstance() {
        if(instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }
    
    private Command currentCommand;
    
	private Stage window;
	private Scene[] scenes;
	private Presenter[] presenters;

	Stack<Command> backHistory = new Stack<Command>();
	Stack<Command> forwardHistory = new Stack<Command>();
	Queue<Command> webHistory = new LinkedList<Command>();
	
	public void initialize(Stage window) throws IOException {
		this.window = window;
		
		HistoryWindow.getInstance().initialize();

		FXMLLoader searchtabLoader = new FXMLLoader((new File(VIEW_PATH + "searchtab.fxml").toURI().toURL()));
    	FXMLLoader homepageLoader = new FXMLLoader(new File(VIEW_PATH + "homepage.fxml").toURI().toURL());
    	FXMLLoader articleLoader = new FXMLLoader(new File(VIEW_PATH + "articleview.fxml").toURI().toURL());
    	FXMLLoader trendingLoader = new FXMLLoader(new File(VIEW_PATH + "trendingtab.fxml").toURI().toURL());
    	FXMLLoader latestLoader = new FXMLLoader(new File(VIEW_PATH + "latesttab.fxml").toURI().toURL());
    	FXMLLoader categoryLoader = new FXMLLoader(new File(VIEW_PATH + "categorytab.fxml").toURI().toURL());
        
        Scene homepage = new Scene(homepageLoader.load()); 
        Scene searchtab = new Scene(searchtabLoader.load());
        Scene article = new Scene(articleLoader.load());
        Scene trending = new Scene(trendingLoader.load());
        Scene latest = new Scene(latestLoader.load());
        Scene category = new Scene(categoryLoader.load());
        
        scenes = new Scene[] {homepage, searchtab, article, trending, latest, category};
        presenters = new Presenter[] {
        	homepageLoader.<Presenter>getController(),
        	searchtabLoader.<Presenter>getController(),
        	articleLoader.<Presenter>getController(),
        	trendingLoader.<Presenter>getController(),
        	latestLoader.<Presenter>getController(),
        	categoryLoader.<Presenter>getController()
        };
        
        currentCommand = new HomepageCommand();
        
        addHistory(webHistory);
	}
	
	public Scene getCurrentScene() {
		return scenes[currentCommand.getKey().ordinal()];
	}
	
	public Object getCurrentCommandValue() {
		return currentCommand.getValue();
	}
	
	private void updateSceneVariables(Command newCommand) {
		currentCommand = newCommand;
	}

	private void switchScene(Command nextCommand) {
		System.gc();
		
		Scene nextScene = scenes[nextCommand.getKey().ordinal()];
		Presenter nextPresenter = presenters[nextCommand.getKey().ordinal()];
		
		currentCommand = nextCommand;
		
		addHistory(webHistory);
		
		window.setScene(nextScene);
        window.setWidth(Screen.getPrimary().getVisualBounds().getWidth());
        window.setHeight(Screen.getPrimary().getVisualBounds().getHeight());
        
        nextPresenter.sceneSwitchInitialize();
        
        HistoryWindow.getInstance().historyWindow.close();
	}
	
	public void addCommand(Command command) {
		addHistory(backHistory);
		forwardHistory.clear();

		updateSceneVariables(command);
		
		switchScene(command);
	}
		
	public void returnCommand() {
		if (backHistory.size() > 0) {
			addHistory(forwardHistory);
			Command lastCommand = backHistory.pop();
			
			updateSceneVariables(lastCommand);
			
			switchScene(lastCommand);
		}
	}
	
	public void forwardScene() {
		if (forwardHistory.size() > 0) {
			Command nextCommand = forwardHistory.pop();
			addHistory(backHistory);
			
			updateSceneVariables(nextCommand);
			
			switchScene(nextCommand);
		}
	}
	
	private void addHistory(Stack<Command> history) {
		history.push(currentCommand);
	}
	
	private void addHistory(Queue<Command> history) {
		history.offer(currentCommand);
	}
}
