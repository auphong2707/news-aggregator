package com.newsalligator.presenter.command;

/**
 * The {@code CategoryTabCommand} class represents a command to switch to the category tab scene.
 * @author Phong Au
 */
public final class CategoryTabCommand extends Command {

	private String categoryName;
	
    /**
     * Constructs a new {@code CategoryTabCommand} with the category name.
     * @param categoryName the name of the category to be displayed in the category tab scene
     */
	public CategoryTabCommand(String categoryName) {
		super(SceneType.CATEGORY_TAB);
		this.categoryName = categoryName;
	}

	@Override
	public String getName() {
		return getKey().name() + ": " + categoryName;
	}

	@Override
	public String getValue() {
		return categoryName;
	}

}
