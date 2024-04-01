package com.newsaggregator.presenter;


import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import com.newsaggregator.model.Model;

import com.newsaggregator.model.ArticleData;
import com.newsaggregator.model.DataType;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class SearchTabPresenter {
	private Model model = new Model();
	
	@FXML private Label dateLabel;
	@FXML private Button searchButton;
	@FXML private TextField searchBar;
	@FXML private Group article1;
	@FXML private Group article2;
	@FXML private Group article3;
	@FXML private Group article4;
	@FXML private Group article5;

	@FXML
	void initialize() throws IOException, InterruptedException {
		setDate();
		model.runLocalServer();
	}
	
	private void setDate() {
		LocalDate currentDate = LocalDate.now();
		int day = currentDate.getDayOfMonth();
		int month = currentDate.getMonthValue();
		int year = currentDate.getYear();
		String dateAbbreviation = currentDate.getDayOfWeek().toString().substring(0, 3);
		dateLabel.setText(dateAbbreviation + ", " + day + "/" + month + "/" + year);
	}
	
	public void finalize() throws IOException {
		model.terminateLocalServer();
	}
	
	
	
	public void search(KeyEvent key) {
		if (key.getCode() == KeyCode.ENTER) {
			List<ArticleData> searchData = model.search(searchBar.getText());
			List<Group> article = Arrays.asList(article1, article2, article3, article4, article5);
			for (int i=0; i < 5; i++)
			{
				PresenterTools.setArticleView(article.get(i), searchData.get(i), ArticleSize.BIG);
			}
		}
	}
	
}
