package de.rohmio.util.filelabeling;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Util {
	
	public static List<File> getFilesRecurively(File directory) {
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
