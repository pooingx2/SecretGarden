package com.sg.main;

import java.awt.Color;

public class Constants {

	public static final int frameW = 800;
	public static final int frameH = 600;
	
	public static final String serverIP= "112.108.39.171";
	public static final int serverPort = 5556;
	
	public static final Color backColor = Color.WHITE;
	public static final String loginImgPath = "img/login.jpg";
	public static final String backImgPath = "img/background.jpg";
	
	
	public static enum frameSize {
		width(800), height(600);
		
		final private int size;
		private frameSize(int size) { this.size = size; }
		public int getFrameSize() { return size; }
	}

}
