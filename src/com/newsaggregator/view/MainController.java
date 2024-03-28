package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;


public class MainController {
	@FXML
	private Button button;
	@FXML
	private TextField searchbar;
	public void searchBar(ActionEvent event) {
		button.setVisible(false);
		searchbar.setVisible(true);
}
	
}
