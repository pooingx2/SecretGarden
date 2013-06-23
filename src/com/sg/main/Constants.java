package com.sg.main;

import java.awt.Color;
import java.awt.Font;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Constants {

	public static final int frameW = 800;
	public static final int frameH = 600;
	
	public static final String serverIP= "112.108.39.160";
	public static final int serverPort = 5551;
	
//	public static final String serverIP= "112.108.39.185";
//	public static final int serverPort = 13000;
	
	
	public static final Color backColor = Color.WHITE;
	public static final Font Font1 = new Font(null,Font.CENTER_BASELINE,15);
	public static final Font Font2 = new Font(null,Font.CENTER_BASELINE,20);
	
	public static enum BackgroudPath {
		
		loginBG("img/background/loginBG.jpg"), 
		barBG("img/background/barBG.jpg"), 
		connectionBG("img/background/connectionBG.jpg"),
		settingsBG("img/background/settingsBG.jpg"),
		privateBG1("img/background/privatePanelBG1.jpg"),
		privateBG2("img/background/privatePanelBG2.jpg"),
		publicBG1("img/background/publicPanelBG1.jpg"),
		publicBG2("img/background/publicPanelBG2.jpg"),
		directoryListBG("img/background/directoryListBG.jpg"),
		directoryMngBG1("img/background/directoryMngBG1.jpg"),
		directoryMngBG2("img/background/directoryMngBG2.jpg"),
		directoryMngBG3("img/background/directoryMngBG3.jpg"),
		directoryMngBG4("img/background/directoryMngBG4.jpg");
		
		final private String path;
		private BackgroudPath(String path) { this.path = path; }
		public String getPath() { return path; }
	}
	
	public static enum ButtonPath {

		loginBtn1("img/button/loginBtn1.jpg"), 
		loginBtn2("img/button/loginBtn2.jpg"), 
		logoutBtn1("img/button/logoutBtn1.jpg"), 
		logoutBtn2("img/button/logoutBtn2.jpg"),
		connectBtn1("img/button/connectBtn1.jpg"), 
		connectBtn2("img/button/connectBtn2.jpg"),
		privateBtn1("img/button/privateBtn1.jpg"),
		privateBtn2("img/button/privateBtn2.jpg"),
		publicBtn1("img/button/publicBtn1.jpg"),
		publicBtn2("img/button/publicBtn2.jpg"),
		confirmBtn1("img/button/confirmBtn1.jpg"),
		confirmBtn2("img/button/confirmBtn2.jpg"),
		cancelBtn1("img/button/cancelBtn1.jpg"), 
		cancelBtn2("img/button/cancelBtn2.jpg"), 
		createBtn1("img/button/createBtn1.jpg"), 
		createBtn2("img/button/createBtn2.jpg"), 
		deleteBtn1("img/button/deleteBtn1.jpg"), 
		deleteBtn2("img/button/deleteBtn2.jpg"),
		accessBtn1("img/button/accessBtn1.jpg"),
		accessBtn2("img/button/accessBtn2.jpg"),
		settingsBtn1("img/button/settingsBtn1.jpg"),
		settingsBtn2("img/button/settingsBtn2.jpg"),	
		uploadBtn1("img/button/uploadBtn1.jpg"),
		uploadBtn2("img/button/uploadBtn2.jpg"),
		downloadBtn1("img/button/downloadBtn1.jpg"),
		downloadBtn2("img/button/downloadBtn2.jpg"),
		loadKeyfileBtn1("img/button/loadKeyfileBtn1.jpg"),
		loadKeyfileBtn2("img/button/loadKeyfileBtn2.jpg");
		
		final private String path;
		private ButtonPath(String path) { this.path = path; }
		public String getPath() { return path; }
	}
	
	public static enum IconPath {
		
		privateIcon("img/icon/privateIcon.jpg"), 
		publicIcon("img/icon/publicIcon.jpg"); 
		
		final private String path;
		private IconPath(String path) { this.path = path; }
		public String getPath() { return path; }
	}
	
	public static enum PacketType {

		Error(0),
		LoginRequest(1), 
		LoginSuccess(2), 
		LoginFailure(3), 
		LogoutRequest(4), 
		LogoutResponse(5); 
		
		final private int type;
		private PacketType(int type) {

			this.type = type;
		}
		public int getType() { return type; }
	}
}
