package de.rohmio.util.filelabeling.gui;

import javafx.scene.control.ToggleButton;
import javafx.scene.text.Font;

public class CategoryButton extends ToggleButton {
	
	private String category;
	
	private int size = 100;
	
	public CategoryButton(String category) {
		this.category = category;

		setPrefHeight(size);
		setPrefWidth(size);
		setWrapText(true);
		setFont(Font.font(20));
		
		setText(category);
	}
	
	public String getCategory() {
		return category;
	}

}
