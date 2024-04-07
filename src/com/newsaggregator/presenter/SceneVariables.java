package com.newsaggregator.presenter;

import com.newsaggregator.model.ArticleData;

class SceneVariables {
	private static SceneVariables instance;
    private SceneVariables() { }

    public static SceneVariables getInstance() {
        if(instance == null) {
            instance = new SceneVariables();
        }
        return instance;
    }
    
    String searchContent;
    
    ArticleData selectedArticleData;
}
