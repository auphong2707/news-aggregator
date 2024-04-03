package com.newsaggregator.presenter;

public class SceneVariables {
	private static SceneVariables instance;
    private SceneVariables() {
    }

    public static SceneVariables getInstance() {
        if(instance == null) {
            instance = new SceneVariables();
        }
        return instance;
    }
    public String searchContent;
}
