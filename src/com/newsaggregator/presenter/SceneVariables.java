package com.newsaggregator.presenter;

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
}
