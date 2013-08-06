package com.sg.model;

public class FileInfo {
	private String type;
	private String name;
	private String parent;
	private String depth;
	private String index;
	
	public FileInfo() {
		
	}
	
	public FileInfo(String type, String name, String parent, String depth, String index){
		this.type = type;
		this.name = name;
		this.parent = parent;
		this.depth = depth;
		this.index = index;
	}

	public String getIndex() {
		return index;
	}
	
	public void setIndex(String index) {
		this.index = index;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getDepth() {
		return depth;
	}

	public void setDepth(String depth) {
		this.depth = depth;
	}
	
}
