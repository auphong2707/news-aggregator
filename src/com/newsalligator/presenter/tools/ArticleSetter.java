package com.newsalligator.presenter.tools;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.newsalligator.model.ArticleData;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
/**
 * <h1> ArticleSetter </h1>
 * The {@code ArticleSetter} class is a class to provide set up article view in user interface.
 * @author Quan Tran
 */
public class ArticleSetter {	
	/**
	 * Sets up an array of article views parallelly
	 * @param views the array of article views to set up
	 * @param data the list of {@code ArticleData} 
	 * @param size the size of article view
	 */
	public static void setArrayArticleViews(Group[] views, List<ArticleData> data, ArticleSize size) {
		ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        for (int i = 0; i < views.length; i++) {
            int index = i;

            executorService.submit(() -> {
                try {
                    // Simulating delay if necessary
                    Thread.sleep(0);
                    
                    Platform.runLater(() -> {
                        ArticleSetter.setArticleView(views[index], data.get(index), size);
                    });
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                    Thread.currentThread().interrupt(); // Restore interrupt status
                }
            });
        }

        // Optionally shut down the executor service if you don't need it for other tasks
        executorService.shutdown();
	}

    /**
     * Sets up a article view.
     * 
     * @param view the group representing the article view
     * @param data the {@code ArticleData} object
     * @param size the size of the article view
     */
	static void setArticleView(Group view, ArticleData data, ArticleSize size) {
		switch(size) {
		case BIG:
			setBigArticleView(view, data);
			return;
		case NOT_SO_BIG:
			setNotBigArticleView(view, data);
			return;
		case MEDIUM:
			setMediumArticleView(view, data);
			return;
		case SMALL:
			setSmallArticleView(view, data);
			return;
		default:
			return;
		}
	}
	
    /**
     * Sets up a small size article view.
     * 
     * @param view the group representing the article
     * @param data the {@code ArticleData} object
     */
	private static void setSmallArticleView(Group view, ArticleData data) {
		view.setVisible(data != null);
		
		List<Node> elements = view.getChildren();
		
		setWebsite(elements, data, 0);
		setTitle(elements, data, 1);
		setImage(elements, data, 2);
	}

    /**
     * Sets up a not big size article view.
     * 
     * @param view the group representing the article
     * @param data the {@code ArticleData} object
     */
	private static void setNotBigArticleView(Group view, ArticleData data) {
		view.setVisible(data != null);
		if(data == null) return;
		
		List<Node> elements = view.getChildren();
		
		setWebsite(elements, data, 0);
		setTitle(elements, data, 1);
		setIntro(elements, data, 2);
		setImage(elements, data, 3);
	}
	
    /**
     * Sets up a medium size article view.
     * 
     * @param view the group representing the article
     * @param data the {@code ArticleData} object
     */
	private static void setMediumArticleView(Group view, ArticleData data) {
		view.setVisible(data != null);
		if(data == null) return;
		
		List<Node> elements = view.getChildren();
		
		setWebsite(elements, data, 0);
		setTitle(elements, data, 1);
		setIntro(elements, data, 2);
	}

    /**
     * Sets up a big size article view.
     * 
     * @param view the group representing the article
     * @param data the {@code ArticleData} object
     */
	private static void setBigArticleView(Group view, ArticleData data) {
		view.setVisible(data != null);
		if(data == null) return;
		
		List<Node> elements = view.getChildren();
		
		setWebsite(elements, data, 0);
		setTitle(elements, data, 1);
		setIntro(elements, data, 2);
		setAuthor(elements, data, 3);
		setImage(elements, data, 4);
	}
	
    /**
     * Sets the website label in the article view.
     * 
     * @param elements the list of nodes representing elements in the article view
     * @param data the article data to extract the website from
     * @param index the index of the website label in the elements list
     */
	private static void setWebsite(List<Node> elements, ArticleData data, int index) {
		Label website = (Label) elements.get(index);
		website.setText(data.getWEBSITE_SOURCE());
	}
	
    /**
     * Sets the title label in the article view.
     * 
     * @param elements The list of nodes representing elements in the article view.
     * @param data The article data to extract the website from.
     * @param index The index of the website label in the elements list.
     */
	private static void setTitle(List<Node> elements, ArticleData data, int index) {
		Label title = (Label) elements.get(index);
		title.setText(data.getTITLE());
	}
	
    /**
     * Sets the intro label in the article view.
     * 
     * @param elements The list of nodes representing elements in the article view.
     * @param data The article data to extract the website from.
     * @param index The index of the website label in the elements list.
     */
	private static void setIntro(List<Node> elements, ArticleData data, int index) {
		Label intro = (Label) elements.get(index);
		intro.setText(data.getINTRO());
	}
	
    /**
     * Sets the author label in the article view.
     * 
     * @param elements The list of nodes representing elements in the article view.
     * @param data The article data to extract the website from.
     * @param index The index of the website label in the elements list.
     */
	private static void setAuthor(List<Node> elements, ArticleData data, int index) {
		Label author = (Label) elements.get(index);
		author.setText(data.getAUTHOR_NAME());
	}
	
    /**
     * Sets the image label in the article view.
     * 
     * @param elements The list of nodes representing elements in the article view.
     * @param data The article data to extract the website from.
     * @param index The index of the website label in the elements list.
     */
	private static void setImage(List<Node> elements, ArticleData data, int index) {
		ImageView imageView = (ImageView) elements.get(index);
		
		String imgURL = data.getIMAGE();
		if (imgURL != null && !imgURL.equals("")) {
			Image image = new Image(imgURL, 150, 100, false, true);
			System.out.println("Finish");
			imageView.setImage(image);
		}
		else {
			String blankDirectory = "file:///" + System.getProperty("user.dir") + "/images/blank_rectangle.png";
			imageView.setImage(new Image(blankDirectory));
		}
	}
}
