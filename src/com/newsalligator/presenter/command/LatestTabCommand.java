package com.newsalligator.presenter.command;

/**
 * The {@code LatestTabCommand} class represents a command to switch to the latest tab scene.
 * It extends the {@code NullCommand} class.
 * @author Phong Au
 */

public final class LatestTabCommand extends NullCommand {
    /**
     * Constructs a new {@code LatestTabCommand} to switch to the latest tab scene.
     */
	public LatestTabCommand() {
		super(SceneType.LATEST_TAB);
	}

}
