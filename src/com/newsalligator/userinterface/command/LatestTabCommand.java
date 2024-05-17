package com.newsalligator.userinterface.command;

import com.newsalligator.userinterface.uienum.SceneType;

/**
 * The {@code LatestTabCommand} class represents a command to switch to the latest tab scene.
 * It extends the {@code NullCommand} class.
 */

public final class LatestTabCommand extends NullCommand {
    /**
     * Constructs a new {@code LatestTabCommand} to switch to the latest tab scene.
     */
	public LatestTabCommand() {
		super(SceneType.LATEST_TAB);
	}

}
