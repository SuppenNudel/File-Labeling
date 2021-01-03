package de.rohmio.util.filelabeling.gui;

import de.rohmio.util.filelabeling.model.ITag;
import javafx.beans.value.ObservableBooleanValue;
import javafx.scene.control.ToggleButton;
import javafx.scene.text.Font;

public class TagButton extends ToggleButton {
	
	private ITag tag;
	
	private int size = 100;
	
	public TagButton(ITag tag) {
		this.tag = tag;

		setPrefHeight(size);
		setPrefWidth(size);
		setWrapText(true);
		setFont(Font.font(20));
		
		setText(tag.toString());
	}
	
	public void hideOn(ObservableBooleanValue v) {
		if(v != null) {
			visibleProperty().bind(v);
			managedProperty().bind(v);
		}
	}
	
	public ITag getTag() {
		return tag;
	}

}
