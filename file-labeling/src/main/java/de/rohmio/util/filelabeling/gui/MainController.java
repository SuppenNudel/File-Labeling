package de.rohmio.util.filelabeling.gui;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

public class MainController implements Initializable {

	@FXML
	private Label currentFileNameLabel;
	@FXML
	private ProgressBar imageLoadingProgress;
	@FXML
	private Button previousButton;
	@FXML
	private Button nextButton;
	
	@FXML
	private ImageView previewImageView;
	
	@FXML
	private Label leftStatus;
	
	private ObservableList<File> loadedFiles = FXCollections.observableArrayList();
	private IntegerProperty idx = new SimpleIntegerProperty(0);
	
//	private Image[] imageCache = new Image[3];
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		previousButton.disableProperty().bind(idx.lessThanOrEqualTo(0));
		
		IntegerBinding sizeProperty = Bindings.size(loadedFiles).add(-1);
		nextButton.disableProperty().bind(idx.greaterThanOrEqualTo(sizeProperty));
		
		idx.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				loadImage(loadedFiles.get(newValue.intValue()));
			}
		});
	}
	
	@FXML
	private void nextImage() {
		if(idx.get() + 1 >= loadedFiles.size()) {
			return;
		}
		idx.set(idx.get() + 1);
	}
	
	@FXML
	private void prevImage() {
		if(idx.get() - 1 < 0) {
			return;
		}
		idx.set(idx.get() - 1);
	}
	
	public void loadImage(File file) {
		currentFileNameLabel.setText(file.getName());
		try {
			Image image = new Image(file.toURI().toURL().toString(), true);
			imageLoadingProgress.progressProperty().bind(image.progressProperty());
			previewImageView.setImage(image);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
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
        	leftStatus.setText(db.getFiles().toString());
        	
        	List<File> allFiles = new ArrayList<>();
        	for(File file : db.getFiles()) {
        		allFiles.addAll(getFilesRecurively(file));
        	}
        	loadedFiles.setAll(allFiles);
        	idx.set(0);
        	loadImage(loadedFiles.get(0));
            success = true;
        }
        event.setDropCompleted(success);
        event.consume();
	}
	
	private List<File> getFilesRecurively(File directory) {
		List<File> files = new ArrayList<>();
		if(directory.isFile()) {
			files.add(directory);
			return files;
		}
		for(File file : directory.listFiles()) {
			if(file.isDirectory()) {
				files.addAll(getFilesRecurively(file));
			} else {
				files.add(file);
			}
		}
		return files;
	}

}
