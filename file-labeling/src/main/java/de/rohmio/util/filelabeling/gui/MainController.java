package de.rohmio.util.filelabeling.gui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import de.rohmio.util.filelabeling.Util;
import de.rohmio.util.filelabeling.model.ITag;
import de.rohmio.util.filelabeling.model.Tag;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

public class MainController implements Initializable {

	@FXML
	private BorderPane mainPane; 
	
	@FXML
	private FlowPane categoriesPane;
	@FXML
	private FlowPane tagsPane;
	
	private Map<String, CategoryButton> categoryButtons = new HashMap<>();
	
	private ImagePreviewController imagePreviewController;
	
	private List<String> availableColors = new LinkedList<String>(Arrays.asList("ffff00", "ffa500", "e06f1f", "ff0000", "ff00ff", "8a2be2", "0000ff", "00ff00"));
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadImagePreviewController();
		
		List<ITag> tags = Arrays.asList(
				new Tag("Ashe", "LoL"),
				new Tag("Hat"),
				new Tag("Tryndamere", "LoL"),
				new Tag("Ashe", "Overwatch"),
				new Tag("Sylvari", "Guild Wars 2"),
				new Tag("Liliana", "Magic: the Gathering"),
				new Tag("Garen", "LoL"));
		tags.sort(new Comparator<ITag>() {
			@Override
			public int compare(ITag o1, ITag o2) {
				if(o2.getCategory() == null && o1.getCategory() != null) {
					return -1;
				} else if(o1.getCategory() == null && o2.getCategory() != null) {
					return 1;
				} else {
					return o1.getCategory().compareTo(o2.getCategory());
				}
			}
		});
		tags.stream().map(ITag::getCategory).distinct().forEach(cat -> {
			if(cat == null || cat.isEmpty()) {
				return;
			}
			CategoryButton button = new CategoryButton(cat);
			String color = availableColors.remove(0);
			button.setBackgroundColor(color);
			categoryButtons.put(cat, button);
			categoriesPane.getChildren().add(button);
		});
		tags.stream().forEach(tag -> {
			TagButton button = new TagButton(tag);
			if(tag.getCategory() == null) {
				button.hideOn(null);
			} else {
				CategoryButton categoryButton = categoryButtons.get(tag.getCategory());
				button.setBackgroundColor(categoryButton.getBackgroundColor());
				button.hideOn(categoryButton.selectedProperty());
			}
			tagsPane.getChildren().add(button);
		});
	}
	
	private void loadImagePreviewController() {
		FXMLLoader loader = new FXMLLoader(ImagePreviewController.class.getResource("imagePreview.fxml"));
		try {
			Node imagePreview = loader.load();
			mainPane.setRight(imagePreview);
		} catch (IOException e) {
			e.printStackTrace();
		}
		imagePreviewController = loader.getController();
	}
	
	@FXML
	private void nextImage() {
		imagePreviewController.nextImage();
	}
	
	@FXML
	private void prevImage() {
		imagePreviewController.prevImage();
	}
	
	@FXML
	private void onDragOver(DragEvent event) {
		if (event.getDragboard().hasFiles()) {
			event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
		}
		event.consume();
	}
	
	@FXML
	private void onDragDropped(DragEvent event) {
		Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasFiles()) {
        	List<File> allFiles = new ArrayList<>();
        	for(File file : db.getFiles()) {
        		allFiles.addAll(Util.getFilesRecurively(file));
        	}
        	imagePreviewController.setFiles(allFiles);
            success = true;
        }
        event.setDropCompleted(success);
        event.consume();
	}

}
