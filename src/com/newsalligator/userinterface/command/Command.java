package com.newsalligator.userinterface.command;

import com.newsalligator.userinterface.uienum.SceneType;

/**
 * <h1> Command </h1>
 * The {@code Command} is an abstract class for commands that can be used to change UI state. 
 * Each command is associated with a scene type
 * @author Phong Au
 */
public abstract class Command {
	private SceneType key;
	
	/**
	 * Constructs a new {@code Command} with a scene type
	 * @param key the scene type associated with this command
	 */
	public Command(SceneType key) {
		this.key = key;
	}
	
	/**
	 * Returns the scene type associated with this command
	 * @return the scene type associated with this command
	 */
	public SceneType getKey() {
		return key;
	}
	
	/**
     * Returns the name of this command.
     *
     * @return the name of this command
     */
	public abstract String getName();

	/**
	 * Returns the value associated with this command.
	 * @return the value associated with this command
	 */
	public abstract Object getValue();
}
