package de.rohmio.util.filelabeling;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeView;

public class MainController implements Initializable {
	
	@FXML
	private TreeView<File> fileView;
	
	@FXML
	private void showDetails() {
		/*
		 * Implement dialog to be prompted when users asks for
		 * details.
		 */
	}
	
	@FXML
	private void sayHelloWorld() {
		System.out.println("Hello World!");
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		fileView.setRoot(new SimpleFileTreeItem(new File("D:\\")));
	}

}
