package com.sg.main;

import java.awt.Color;
import java.awt.Font;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Constants {

	
	
	// main framesize
	public static final int frameW = 800;
	public static final int frameH = 600;
	
	// msg passing server ip, port
	public static final String serverIP= "112.108.39.217";
	public static final int serverPort = 12600;

	// 기본 바탕색
	public static final Color backColor = Color.WHITE;
	
	// font 설정
	public static final Font Font1 = new Font(null,Font.CENTER_BASELINE,15);
	public static final Font Font2 = new Font(null,Font.CENTER_BASELINE,20);

	// cloud Constants
	public static final String hadoop = "Hadoop";
	public static final String amazon = "Amazon";
	
	// 각종 패널 배경이미지 경로
	public static enum BackgroudPath {
		
		loginBG("/images/background/loginBG.jpg"),
		signupBG("/images/background/signupBG.jpg"),
		barBG("/images/background/barBG.jpg"), 
		connectionBG("/images/background/connectionBG.jpg"),
		settingsBG("/images/background/settingsBG.jpg"),
		sharingBG("/images/background/sharingBG.jpg"),
		privateBG1("/images/background/privatePanelBG1.jpg"),
		privateBG2("/images/background/privatePanelBG2.jpg"),
		publicBG1("/images/background/publicPanelBG1.jpg"),
		publicBG2("/images/background/publicPanelBG2.jpg"),
		directoryListBG("/images/background/directoryListBG.jpg"),
		directoryMngBG0("/images/background/directoryMngBG0.jpg"),
		directoryMngBG1("/images/background/directoryMngBG1.jpg"),
		directoryMngBG2("/images/background/directoryMngBG2.jpg"),
		directoryMngBG3("/images/background/directoryMngBG3.jpg"),
		directoryMngBG4("/images/background/directoryMngBG4.jpg"),
		directoryMngBG5("/images/background/directoryMngBG5.jpg"),
		fileListBG("/images/background/fileListBG.jpg"),
		fileMngBG0("/images/background/fileMngBG0.jpg"),
		fileMngBG1("/images/background/fileMngBG1.jpg"),
		fileMngBG2("/images/background/fileMngBG2.jpg"),
		fileMngBG3("/images/background/fileMngBG3.jpg"),
		fileMngBG4("/images/background/fileMngBG4.jpg"),
		fileMngBG5("/images/background/fileMngBG5.jpg");
		
		final private String path;
		private BackgroudPath(String path) { this.path = path; }
		public String getPath() { return path; }
	}
	
	// 버튼 관련 이미지 경로
	public static enum ButtonPath {
		
		loginBtn1("/images/button/loginBtn1.jpg"), 
		loginBtn2("/images/button/loginBtn2.jpg"), 
		signupBtn1("/images/button/signupBtn1.jpg"), 
		signupBtn2("/images/button/signupBtn2.jpg"), 
		logoutBtn1("/images/button/logoutBtn1.jpg"), 
		logoutBtn2("/images/button/logoutBtn2.jpg"),
		homeBtn1("/images/button/homeBtn1.jpg"),
		homeBtn2("/images/button/homeBtn2.jpg"),
		backBtn1("/images/button/backBtn1.jpg"),
		backBtn2("/images/button/backBtn2.jpg"),
		forwardBtn1("/images/button/forwardBtn1.jpg"),
		forwardBtn2("/images/button/forwardBtn2.jpg"),
		shareBtn1("/images/button/shareBtn1.jpg"),
		shareBtn2("/images/button/shareBtn2.jpg"),
		connectBtn1("/images/button/connectBtn1.jpg"), 
		connectBtn2("/images/button/connectBtn2.jpg"),
		privateBtn1("/images/button/privateBtn1.jpg"),
		privateBtn2("/images/button/privateBtn2.jpg"),
		publicBtn1("/images/button/publicBtn1.jpg"),
		publicBtn2("/images/button/publicBtn2.jpg"),
		confirmBtn1("/images/button/confirmBtn1.jpg"),
		confirmBtn2("/images/button/confirmBtn2.jpg"),
		cancelBtn1("/images/button/cancelBtn1.jpg"), 
		cancelBtn2("/images/button/cancelBtn2.jpg"), 
		createBtn1("/images/button/createBtn1.jpg"), 
		createBtn2("/images/button/createBtn2.jpg"), 
		deleteBtn1("/images/button/deleteBtn1.jpg"), 
		deleteBtn2("/images/button/deleteBtn2.jpg"),
		accessBtn1("/images/button/accessBtn1.jpg"),
		accessBtn2("/images/button/accessBtn2.jpg"),
		settingsBtn1("/images/button/settingsBtn1.jpg"),
		settingsBtn2("/images/button/settingsBtn2.jpg"),	
		uploadBtn1("/images/button/uploadBtn1.jpg"),
		uploadBtn2("/images/button/uploadBtn2.jpg"),
		downloadBtn1("/images/button/downloadBtn1.jpg"),
		downloadBtn2("/images/button/downloadBtn2.jpg"),
		loadKeyfileBtn1("/images/button/loadKeyfileBtn1.jpg"),
		loadKeyfileBtn2("/images/button/loadKeyfileBtn2.jpg"),
		fileOpenBtn1("/images/button/fileOpenBtn1.jpg"),
		fileOpenBtn2("/images/button/fileOpenBtn2.jpg"),		
		downloadPathBtn1("/images/button/downloadPathBtn1.jpg"),
		downloadPathBtn2("/images/button/downloadPathBtn2.jpg");
		
		final private String path;
		private ButtonPath(String path) { this.path = path; }
		public String getPath() { return path; }
	}
	
	// icon 관련 이미지 경로
	public static enum IconPath {
		
		privateIcon("/images/icon/privateIcon.jpg"), 
		publicIcon("/images/icon/publicIcon.jpg"),
		forderIcon("/images/icon/forderIcon.jpg"),
		fileIcon("/images/icon/fileIcon.jpg"),
		keyFileIcon("/images/icon/keyFileIcon.jpg"),
		shareFromIcon1("/images/icon/shareFromState1.jpg"),
		shareFromIcon2("/images/icon/shareFromState2.jpg"),
		shareFromIcon3("/images/icon/shareFromState3.jpg"),
		shareToIcon1("/images/icon/shareToState1.jpg"),
		shareToIcon2("/images/icon/shareToState2.jpg"),
		shareToIcon3("/images/icon/shareToState3.jpg");
		
		final private String path;
		private IconPath(String path) { this.path = path; }
		public String getPath() { return path; }
	}
	
	// 프로토콜 패킷 타입
	public static enum PacketType {

		Error(0),
		LoginRequest(1), 
		LoginResponse(2), 
		LogoutRequest(3), 
		LogoutResponse(4),
		SignupRequest(5), 
		SignupResponse(6),
		DirectoryListRequest(7), 
		DirectoryListResponse(8),
		DirectoryCreateRequest(9), 
		DirectoryCreateResponse(10),
		DirectoryAccessRequest(11), 
		DirectoryAccessResponse(12),
		FolderCreateRequest(13), 
		FolderCreateResponse(14),
		FileUploadRequest(15), 
		FileUploadResponse(16),
		FileDownloadRequest(17),
		FileDownloadResponse(18),
		SettingRequset(21), 
		SettingResponse(22),
		
		// 파일 업로드, 파일 다운로드, 디렉토리 서버 전송, 바인딩 프로토콜
		FROM_DIR_TO_HDFS_FOR_UPLOAD_METADATA(30),
		FROM_HDFS_TO_DIR_FOR_MODIFY_MEATAPATH(31),
		FROM_DIR_TO_HDFS_FOR_DOWNLOADLOAD_METADATA(32),
		FROM_HDFS_TO_DIR_FOR_SEND_METADATA(33),

		PROGRAM_EXIT_REQUEST(50), 
		PROGRAM_EXIT_RESPONSE(51); 
		
		final private int type;
		private PacketType(int type) {

			this.type = type;
		}
		public int getType() { return type; }
	}
}
