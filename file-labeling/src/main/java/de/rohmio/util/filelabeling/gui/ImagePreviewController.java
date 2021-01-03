package de.rohmio.util.filelabeling.gui;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImagePreviewController implements Initializable {

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
	
	private ObservableList<File> loadedFiles = FXCollections.observableArrayList();
	private IntegerProperty idx = new SimpleIntegerProperty(0);
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		previousButton.disableProperty().bind(idx.lessThanOrEqualTo(0));
		
		IntegerBinding sizeProperty = Bindings.size(loadedFiles).add(-1);
		nextButton.disableProperty().bind(idx.greaterThanOrEqualTo(sizeProperty));
		
		idx.addListener((obs, old, val) -> loadImage(loadedFiles.get(val.intValue())));
	}
	
	@FXML
	public void nextImage() {
		if(idx.get() + 1 >= loadedFiles.size()) {
			return;
		}
		idx.set(idx.get() + 1);
	}
	
	@FXML
	public void prevImage() {
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
	
	
	public void setFiles(List<File> files) {
    	loadedFiles.setAll(files);
    	idx.set(0);
    	loadImage(loadedFiles.get(0));
	}

}
