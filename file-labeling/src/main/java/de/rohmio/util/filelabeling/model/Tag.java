package de.rohmio.util.filelabeling.model;

public class Tag implements ITag {
	
	private int id;
	private String name;
	private String category;
	
	private Tag() {
		//n7den: access database via singleton, create tag in database, set id
	}
	
	public Tag(String name) {
		this();
		this.name = name;
	}
	
	public Tag(String name, String category) {
		this(name);
		this.category = category;
	}
	
	@Override
	public String toString() {
		return String.format("%s (%s)", name, category);
	}
	
	@Override
	public int getId() {
		return id;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public String getCategory() {
		return category;
	}

}
