package de.rohmio.util.filelabeling.gui;

import de.rohmio.util.filelabeling.model.ITag;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ToggleButton;
import javafx.scene.text.Font;

public class TagButton extends ToggleButton {
	
	private ITag tag;
	
	private String backgroundColor = "E3E3E3";
	
	private int size = 100;
	
	public TagButton(ITag tag) {
		this.tag = tag;

		setPrefHeight(size);
		setPrefWidth(size);
		setWrapText(true);
		setFont(Font.font(20));
		
		setText(tag.toString());
		
		selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				clearStyle();
				if(newValue) {
					addStyle("-fx-border-color: green; -fx-border-width: 5px;");
				}
				addStyle("-fx-background-color: "+backgroundColor+";");
			}
		});
		
		setBackgroundColor(backgroundColor);
	}
	
	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
		addStyle("-fx-background-color: "+backgroundColor+";");
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
	
	private void addStyle(String str) {
		setStyle(getStyle() + str);
	}
	
	private void clearStyle() {
		setStyle("");
	}

}
