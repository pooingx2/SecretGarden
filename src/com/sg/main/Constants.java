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
	public static final String serverIP= "112.108.39.153";
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
		
//		loginBG("img/background/loginBG.jpg"),
		loginBG("img/background/loginBG.jpg"),
		signupBG("img/background/signupBG.jpg"),
		barBG("img/background/barBG.jpg"), 
		connectionBG("img/background/connectionBG.jpg"),
		settingsBG("img/background/settingsBG.jpg"),
		sharingBG("img/background/sharingBG.jpg"),
		shareListBG("img/background/shareListBG.jpg"),
		privateBG1("img/background/privatePanelBG1.jpg"),
		privateBG2("img/background/privatePanelBG2.jpg"),
		publicBG1("img/background/publicPanelBG1.jpg"),
		publicBG2("img/background/publicPanelBG2.jpg"),
		directoryListBG("img/background/directoryListBG.jpg"),
		directoryMngBG0("img/background/directoryMngBG0.jpg"),
		directoryMngBG1("img/background/directoryMngBG1.jpg"),
		directoryMngBG2("img/background/directoryMngBG2.jpg"),
		directoryMngBG3("img/background/directoryMngBG3.jpg"),
		directoryMngBG4("img/background/directoryMngBG4.jpg"),
		directoryMngBG5("img/background/directoryMngBG5.jpg"),
		fileListBG("img/background/fileListBG.jpg"),
		fileMngBG0("img/background/fileMngBG0.jpg"),
		fileMngBG1("img/background/fileMngBG1.jpg"),
		fileMngBG2("img/background/fileMngBG2.jpg"),
		fileMngBG3("img/background/fileMngBG3.jpg"),
		fileMngBG4("img/background/fileMngBG4.jpg"),
		fileMngBG5("img/background/fileMngBG5.jpg");
		
		final private String path;
		private BackgroudPath(String path) { this.path = path; }
		public String getPath() { return path; }
	}
	
	// 버튼 관련 이미지 경로
	public static enum ButtonPath {
		
		loginBtn1("img/button/loginBtn1.jpg"), 
		loginBtn2("img/button/loginBtn2.jpg"), 
		signupBtn1("img/button/signupBtn1.jpg"), 
		signupBtn2("img/button/signupBtn2.jpg"), 
		logoutBtn1("img/button/logoutBtn1.jpg"), 
		logoutBtn2("img/button/logoutBtn2.jpg"),
		homeBtn1("img/button/homeBtn1.jpg"),
		homeBtn2("img/button/homeBtn2.jpg"),
		backBtn1("img/button/backBtn1.jpg"),
		backBtn2("img/button/backBtn2.jpg"),
		forwardBtn1("img/button/forwardBtn1.jpg"),
		forwardBtn2("img/button/forwardBtn2.jpg"),
		shareBtn1("img/button/shareBtn1.jpg"),
		shareBtn2("img/button/shareBtn2.jpg"),
		sharingBtn1("img/button/sharingBtn1.jpg"),
		sharingBtn2("img/button/sharingBtn2.jpg"),
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
		addBtn1("img/button/addBtn1.jpg"), 
		addBtn2("img/button/addBtn2.jpg"), 
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
		loadKeyfileBtn2("img/button/loadKeyfileBtn2.jpg"),
		fileOpenBtn1("img/button/fileOpenBtn1.jpg"),
		fileOpenBtn2("img/button/fileOpenBtn2.jpg"),		
		downloadPathBtn1("img/button/downloadPathBtn1.jpg"),
		downloadPathBtn2("img/button/downloadPathBtn2.jpg");
		
		final private String path;
		private ButtonPath(String path) { this.path = path; }
		public String getPath() { return path; }
	}
	
	// icon 관련 이미지 경로
	public static enum IconPath {
		
		privateIcon("img/icon/privateIcon.jpg"), 
		publicIcon("img/icon/publicIcon.jpg"),
		forderIcon("img/icon/forderIcon.jpg"),
		fileIcon("img/icon/fileIcon.jpg"),
		xlsxFileIcon("img/icon/xlsxFileIcon.jpg"),
		exeFileIcon("img/icon/exeFileIcon.jpg"),
		psdFileIcon("img/icon/psdFileIcon.jpg"),
		flashFileIcon("img/icon/flashFileIcon.jpg"),
		mp3FileIcon("img/icon/mp3FileIcon.jpg"),
		pdfFileIcon("img/icon/pdfFileIcon.jpg"),
		txtFileIcon("img/icon/txtFileIcon.jpg"),
		docFileIcon("img/icon/docFileIcon.jpg"),
		aviFileIcon("img/icon/aviFileIcon.jpg"),
		mediaFileIcon("img/icon/mediaFileIcon.png"),
		bmpFileIcon("img/icon/bmpFileIcon.png"),
		jpgFileIcon("img/icon/jpgFileIcon.jpg"),
		gifFileIcon("img/icon/gifFileIcon.png"),
		pngFileIcon("img/icon/pngFileIcon.jpg"),
		pptFileIcon("img/icon/pptFileIcon.jpg"),
		hwpFileIcon("img/icon/hwpFileIcon.jpg"),
		jarFileIcon("img/icon/jarFileIcon.jpg"),
		gulFileIcon("img/icon/gulFileIcon.jpg"),
		smiFileIcon("img/icon/smiFileIcon.png"),
		xmlFileIcon("img/icon/xmlFileIcon.png"),
		htmlFileIcon("img/icon/htmlFileIcon.jpg"),
		zipFileIcon("img/icon/zipFileIcon.png"),
		keyFileIcon("img/icon/keyFileIcon.jpg"),
		shareFromIcon1("img/icon/shareFromState1.jpg"),
		shareFromIcon2("img/icon/shareFromState2.jpg"),
		shareFromIcon3("img/icon/shareFromState3.jpg"),
		shareToIcon1("img/icon/shareToState1.jpg"),
		shareToIcon2("img/icon/shareToState2.jpg"),
		shareToIcon3("img/icon/shareToState3.jpg");
		
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
		IdCheckRequest(19),
		IdCheckResponse(20),
		ShareRequest(21),
		ShareResponse(22),
		ShareListRequest(23),
		ShareListResponse(24),
		SharingConfirmRequest(25),
		SharingRefuseRequest(26),
		SharingCancelRequest(27),
		DirectoryDeleteRequest(28),
		FileDeleteRequest(29),
//		ShareID
		
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
	
	public static enum HybridError{
	
		NoFile(1);
		
		final private int error;
		private HybridError(int error) {this.error = error;}
		public int getError() { return error; }
	}
}
