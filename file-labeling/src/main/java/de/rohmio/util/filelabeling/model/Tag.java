package de.rohmio.util.filelabeling.model;

public class Tag {
	
	private int id;
	private String name;
	private String category;
	
	private Tag() {}
	
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
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getCategory() {
		return category;
	}

}
