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

/**
 * <h1> UIMangager </h1>
 * The {@code UIManager} class is a class to manage to user interface,
 *  including switching scenes, maintaining history, and initializing the UI components.
 *  @author Khanh Nguyen, Phong Au
 */
public class UIManager {
	private static final String VIEW_PATH = "resources/views/";
	private static UIManager instance;
	private static HistoryWindow historyPresenter;
    private UIManager() { }
    
    /**
     * Returns the singleton instance of {@code UIManager}.
     * @return the singleton instance 
     */
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
	
	/**
	 * Initializes the UIManager with primary stage and loaded scenes.
	 * @param window the primary stage 
	 * @throws IOException if fail to load FXML files
	 */
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
	
	/**
	 * Returns the current scene being displayed
	 * @return the current scene being displayed
	 */
	public Scene getCurrentScene() {
		return scenes[currentCommand.getKey().ordinal()];
	}
	
	/**
	 * Returns the current value of command
	 * @return the current value of command
	 */
	public Object getCurrentCommandValue() {
		return currentCommand.getValue();
	}
	
	/**
	 * Updates the current command to a new command.
	 * @param newCommand the new command to be updated
	 */
	private void updateSceneVariables(Command newCommand) {
		currentCommand = newCommand;
	}

	/**
	 * Switches the scene to the scene related to the next command.
	 * @param nextCommand the command that includes the next scene to display
	 */
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
	
	/**
	 * Executes the command, updating the scene and command history.
	 * @param command the command to execute
	 */
	public void executeCommand(Command command) {
		backHistory.push(currentCommand);
		forwardHistory.clear();

		updateSceneVariables(command);
		
		switchScene(command);
	}
	
	/**
	 * Returns to the previous command, updating the scene and command history.
	 */
	public void returnCommand() {
		if (backHistory.size() > 0) {
			forwardHistory.push(currentCommand);
			Command lastCommand = backHistory.pop();
			
			updateSceneVariables(lastCommand);
			
			switchScene(lastCommand);
		}
	}
	
	/**
	 * Forwards to the next command, updating the scene and command history.
	 */
	public void forwardCommand() {
		if (forwardHistory.size() > 0) {
			Command nextCommand = forwardHistory.pop();
			backHistory.push(currentCommand);
			
			updateSceneVariables(nextCommand);
			
			switchScene(nextCommand);
		}
	}
	
	/**
	 *  Shuts down the application, terminates local servers.
	 */
	public void onApplicationShutdown() {
		Model.getInstance().terminateLocalServer();
	}
	
	/**
	 * Opens the history window.
	 */
	public void openHistoryWindow() {
		historyPresenter.openWindow();
	}
}
