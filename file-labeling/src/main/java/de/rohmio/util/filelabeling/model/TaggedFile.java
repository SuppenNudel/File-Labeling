package de.rohmio.util.filelabeling.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TaggedFile implements ITaggedFile {
	
	private int id;
	private File file;
	private List<ITag> tags;
	
	private TaggedFile() {}
	
	public TaggedFile(File file) {
		this();
		this.file = file;
		this.tags = new ArrayList<>();
	}
	
	public void addTag(Tag tag) {
		tags.add(tag);
	}
	
	@Override
	public int getId() {
		return id;
	}

	@Override
	public File getFile() {
		return file;
	}

	@Override
	public List<ITag> getTags() {
		return tags;
	}

}
