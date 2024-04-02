package com.newsaggregator.presenter;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import com.newsaggregator.model.ArticleData;
import com.newsaggregator.model.Model;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.Group;
import javafx.scene.Node;

public class HomepagePresenter {
	@FXML private Label dateLabel;
	@FXML private Group bigArticle1;
	@FXML private Group bigArticle2;
	@FXML private Group smallArticle1;
	@FXML private Group smallArticle2;
	@FXML private Group smallArticle3;
	@FXML private Group smallArticle4;
	@FXML private Group notSoBigArticle1;
	@FXML private Group notSoBigArticle2;
	@FXML private Group notSoBigArticle3;
	@FXML private Group notSoBigArticle4;
	@FXML private Group notSoBigArticle5;
	@FXML private Group notSoBigArticle6;
	@FXML private Group mediumArticle1;

	private Model model = new Model();
	
	@FXML
	public void initialize() throws IOException, InterruptedException {
		setDate();
		model.runLocalServer();
		setLatestArticle();
		List<ArticleData> random = model.search("");
		Group[] randomArticle = new Group[] {notSoBigArticle1, notSoBigArticle2, notSoBigArticle3,
											 notSoBigArticle4, notSoBigArticle5, notSoBigArticle6};
		for (int i = 0; i < randomArticle.length; i++) {
			PresenterTools.setArticleView(randomArticle[i], random.get(i), ArticleSize.NOT_SO_BIG);
		}
	}
	
	private void setDate() {
		LocalDate currentDate = LocalDate.now();
		int day = currentDate.getDayOfMonth();
		int month = currentDate.getMonthValue();
		int year = currentDate.getYear();
		String dateAbbreviation = currentDate.getDayOfWeek().toString().substring(0, 3);
		dateLabel.setText(dateAbbreviation + ", " + day + "/" + month + "/" + year);
	}
	
	private void setLatestArticle() {
		List<ArticleData> latest = model.search("");
		Group[] latestArticle = new Group[] {bigArticle1, bigArticle2, smallArticle1, 
											 smallArticle2, smallArticle3, smallArticle4};
		for (int i = 0; i < latestArticle.length; i++) {
			if (i < 2) {
				PresenterTools.setArticleView(latestArticle[i], latest.get(i), ArticleSize.BIG);
			} else {
				PresenterTools.setArticleView(latestArticle[i], latest.get(i), ArticleSize.SMALL);
			}
		}
	} 
	public void finalize() throws IOException {
		model.terminateLocalServer();
	}
}
