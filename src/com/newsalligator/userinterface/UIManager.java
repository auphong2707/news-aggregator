package com.newsalligator.userinterface;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import com.newsalligator.model.Model;
import com.newsalligator.userinterface.command.Command;
import com.newsalligator.userinterface.command.HomepageCommand;
import com.newsalligator.userinterface.presenter.Presenter;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.File;

public class UIManager {
	private static final String VIEW_PATH = "resources/views/";
	private static UIManager instance;
	private static HistoryWindow historyPresenter;
    private UIManager() { }

    public static UIManager getInstance() {
        if(instance == null) {
        	historyPresenter = new HistoryWindow();
            instance = new UIManager();
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
		
		historyPresenter.initialize(webHistory);

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
        
        webHistory.offer(currentCommand);
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
		
		webHistory.offer(currentCommand);
		
		window.setScene(nextScene);
        window.setWidth(Screen.getPrimary().getVisualBounds().getWidth());
        window.setHeight(Screen.getPrimary().getVisualBounds().getHeight());
        
        nextPresenter.sceneSwitchInitialize();
        
        historyPresenter.closeWindow();
	}
	
	public void addCommand(Command command) {
		backHistory.push(currentCommand);
		forwardHistory.clear();

		updateSceneVariables(command);
		
		switchScene(command);
	}
		
	public void returnCommand() {
		if (backHistory.size() > 0) {
			forwardHistory.push(currentCommand);
			Command lastCommand = backHistory.pop();
			
			updateSceneVariables(lastCommand);
			
			switchScene(lastCommand);
		}
	}
	
	public void forwardScene() {
		if (forwardHistory.size() > 0) {
			Command nextCommand = forwardHistory.pop();
			backHistory.push(currentCommand);
			
			updateSceneVariables(nextCommand);
			
			switchScene(nextCommand);
		}
	}
	
	public void onApplicationShutdown() {
		Model.getInstance().terminateLocalServer();
	}
	
	public void openHistoryWindow() {
		historyPresenter.openWindow();
	}
}
