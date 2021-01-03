package de.rohmio.util.filelabeling.model;

import java.io.File;
import java.util.List;

public interface ITaggedFile {

	public int getId();
	public File getFile();
	public List<ITag> getTags();

}
