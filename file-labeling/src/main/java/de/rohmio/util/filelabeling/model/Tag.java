package de.rohmio.util.filelabeling.model;

import de.rohmio.util.filelabeling.database.ConnectorAccessor;

public class Tag implements ITag {
	
	private int id;
	private String name;
	private String category;
	
	public Tag(String name) {
		this(name, null);
	}
	
	public Tag(String name, String category) {
		this.name = name;
		this.category = category;
		this.id = ConnectorAccessor.Instance().Connector().createTagAndGetId(name, category);
	}
	
	/**
	 * This constructor exists only for the SqlConnector
	 */
	@SuppressWarnings("unused")
	private Tag(int id, String name, String category) {
		this.id = id;
		this.name = name;
		this.category = category;
	}
	
	@Override
	public String toString() {
		if(category == null) {
			return name;
		} else {
			return String.format("%s (%s)", name, category);
		}
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
