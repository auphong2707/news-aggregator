package com.newsalligator.userinterface.command;

import com.newsalligator.userinterface.uienum.SceneType;

/**
 * The {@code HomepageCommand} class represents a command to switch to the homepage scene.
 * It extends the {@code NullCommand} class.
 * @author Phong Au
 */
public final class HomepageCommand extends NullCommand {
    /**
     * Constructs a new {@code HomepageCommand} to switch to the homepage scene.
     */
	public HomepageCommand() {
		super(SceneType.HOMEPAGE);
	}

}
