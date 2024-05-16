package com.newsalligator.userinterface.command;

import com.newsalligator.userinterface.uienum.SceneType;

public abstract class Command {
	private SceneType key;
	
	public Command(SceneType key) {
		this.key = key;
	}

	public SceneType getKey() {
		return key;
	}
	
	public abstract String getName();

	public abstract Object getValue();
}
