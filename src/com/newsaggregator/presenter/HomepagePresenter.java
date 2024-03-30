package com.newsaggregator.presenter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.newsaggregator.model.ArticleData;
import com.newsaggregator.model.Model;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.Group;
import javafx.scene.Node;

public class HomepagePresenter {
	@FXML
	private Label dateLabel;
	
	@FXML
	private Group bigArticle1;
	
	@FXML
	private Group bigArticle2;
	    
	@FXML
	private Group smallArticle1;

	@FXML
	private Group smallArticle2;
	
	@FXML
	private Group smallArticle3;
	
	@FXML
	private Group smallArticle4;
	    
	@FXML
	private Group mediumArticle1;
	    
	@FXML
	private Group article5;
	
	private Model model = new Model();
	
	@FXML
	public void initialize() throws IOException, InterruptedException {
		setDate();
		model.runLocalServer();
		List<ArticleData> bigArticleData = model.search("TFT");
		PresenterTools.setArticleView(bigArticle1, bigArticleData.get(0), ArticleSize.BIG);
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
}
