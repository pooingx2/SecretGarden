package com.sg.model;

public class FileInfo {
	private String type;
	private String name;
	private String parent;
	private String depth;
	private String rootID;
	private String size;
	private String fileID;
	
	public FileInfo() {
		
	}
	
	public FileInfo(String type, String name, String parent, String depth, String rootID){
		this.type = type;
		this.name = name;
		this.parent = parent;
		this.depth = depth;
		this.rootID = rootID;
	}

	public String getRootID() {
		return rootID;
	}
	
	public void setRootID(String rootID) {
		this.rootID = rootID;
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
	
	public void setSize(String size) {
		this.size = size;
	}
	
	public String getSize() {
		return size;
	}
	
	public void setFileID(String fileID) {
		this.fileID = fileID;
	}
	
	public String getFileID() {
		return fileID;
	}
}
