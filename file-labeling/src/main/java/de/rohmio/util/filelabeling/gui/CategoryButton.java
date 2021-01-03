package de.rohmio.util.filelabeling.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ToggleButton;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class CategoryButton extends ToggleButton {

	private String category;
	private String backgroundColor = "E3E3E3";

	private int size = 100;

	public CategoryButton(String category) {
		this.category = category;

		setPrefHeight(size);
		setPrefWidth(size);
		setWrapText(true);
		setFont(Font.font(20));

		setText(category);

		selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				clearStyle();
				if(newValue) {
					addStyle("-fx-border-color: green; -fx-border-width: 5px;");
				}
				addStyle("-fx-background-color: #" + backgroundColor+";");
				addStyle("-fx-stroke-width: 5;");
				addStyle("-fx-stroke: WHITE;");
			}
		});

		setTextFill(Color.BLACK);
//		getShape().setStrokeWidth(3.0);
//		getShape().setStroke(Color.WHITE);
		
		setSelected(true);
	}
	
	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
		addStyle("-fx-background-color: #" + backgroundColor+";");
	}

	public String getBackgroundColor() {
		return backgroundColor;
	}

	public String getCategory() {
		return category;
	}

	private void addStyle(String str) {
		setStyle(getStyle() + str);
	}

	private void clearStyle() {
		setStyle("");
	}

}
