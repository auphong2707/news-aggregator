package com.newsaggregator.presenter;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class SearchTabPresenter {
	@FXML
	private Button searchButton;
	@FXML
	private TextField searchBar;
	@FXML
	private Label category1;
	public void category(MouseEvent event) {
		System.out.print("Article");
	}
	public void searchbar(ActionEvent event) {
		searchButton.setVisible(false);
		searchBar.setVisible(true);
	}
	
}
