package com.newsaggregator.userinterface.command;

import com.newsaggregator.userinterface.uienum.SceneType;

abstract class NullCommand extends Command{

	public NullCommand(SceneType key) {
		super(key);
	}

	@Override
	public String getName() {
		return getKey().name();
	}

	@Override
	public Object getValue() {
		return null;
	}
}
