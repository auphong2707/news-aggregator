package com.newsalligator.userinterface.command;

/**
 * The {@code TrendingTabCommand} class represents a command to switch to the trending tab scene.
 * It extends the {@code NullCommand} class.
 * @author Phong Au
 */
public final class TrendingTabCommand extends NullCommand {

	public TrendingTabCommand() {
		super(SceneType.TRENDING_TAB);
	}

}
