package com.newsalligator.userinterface.command;

import com.newsalligator.userinterface.uienum.SceneType;
/**
 * The {@code NullCommand} class is an abstract implementation of the {@code Command} class
 * that represents a command with no specific value.
 * @author Phong Au
 */
abstract class NullCommand extends Command{
    /**
     * Constructs a new {@code NullCommand} with the scene type.
     *
     * @param key the scene type associated with this command
     */
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
