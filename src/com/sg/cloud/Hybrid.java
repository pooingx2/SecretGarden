package com.sg.cloud;

import java.io.FileOutputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;

import com.sg.controller.streamManager;
import com.sg.main.ClientLauncher;
import com.sg.main.Constants;
import com.sg.model.Files;

public class Hybrid {
	
	// 두 파일들이 전부 존재 하는지 확인후에 작업 수행...
	private HDFSClient hdfsModule;
	private AWSUpDown aWSModule;
	
	
	public Hybrid(){
		hdfsModule = new HDFSClient();
		aWSModule = new AWSUpDown();
	}
//	public String joinClient(String id) {
//		
//		
//		String accessKey;
//		int optionNum = 4;
//		Files request = new Files("none","none", optionNum, id);
//		
//		accessKey = hdfsModule.sendAccessKey2Serv(request);
//		
//		return accessKey;
//	}
//	
	
	public boolean auth(String type, String id, String port, String pw) {

		boolean isConnected = false;	// 1: connected		0 : disConnected
		
		Files request = new Files();
		int optionNum = 0;
		request.setAccessKey(pw);
		request.setOptionNum(optionNum);
		request.setUserId(ClientLauncher.getUser().getId());
		
		switch (type){

		case Constants.amazon : 		// aws s3 connect
			String AWSkeyId = "AKIAIUXPHCYBAHGUZGEQ";//id;			// 입력 받아야 함
			String AWSkey = "z3L3XdNwpWPx0R37bToPR+O85cmSoZTJrucfb4xE";//pw;			// 입력 받아야 함
			aWSModule.setKey(AWSkey);
			aWSModule.setKeyId(AWSkeyId);
			
			isConnected= aWSModule.auth(ClientLauncher.getUser().getId());
			break;

		case Constants.hadoop : 		// hdfs connect
			
			String hdfsIp = "211.189.127.91";//id;			// 입력 받아야 함
			int hdfsPort = 15000;//Integer.parseInt(port);		// 입력 받아야 함
			String hdfsPw = "aa";//pw;
			hdfsModule.setDestIp(hdfsIp);
			hdfsModule.setDestPort(hdfsPort);
			hdfsModule.setAccessKey(hdfsPw);
			
			try {
				isConnected = hdfsModule.auth(request);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if (isConnected == false) {
				System.out.println("connected fail");
				return isConnected;
			}
			break;
		}

		return isConnected;
	}
	public int disconnected() {
		hdfsModule.disconnected();
		return 0;
	}
	
	/*모든 경로를 제거하고 파일 이름만 얻는 함수*/
	private String getFileName(String path) {
		StringTokenizer token = new StringTokenizer(path,"/\\");
		String fileName = null;
		while(token.hasMoreElements()) {
			fileName = token.nextToken();
		}
		
		return fileName;
	}
	
	/*각 클라우드에 저장하기 위해 파일 이름을 받으면 /파일이름확장자/파일이름.확장자 형태로 변환 */
	public String makeFileName(String orginName) {
		StringTokenizer st = new StringTokenizer(orginName, ".");
		String fixedName = new String();

		while(st.hasMoreTokens()){
			fixedName += st.nextToken();
		}
		fixedName = fixedName + "/";

		return fixedName;
	}

	/*다운 로드 시 파일 이름은 제거하고 경로만 얻어주는 함수*/
	private String getDownloadDirPath(String originPath){
		String fixedPath = null;
		fixedPath = originPath.substring(1, originPath.lastIndexOf("/")+1);
		
		return fixedPath;
	}
	
	public int upload(String sourceFile, String destPath) throws IOException {
		//upload는 모두 transaction을 지켜야 하기 때문에 redo와 undo 처리가 필요
		int optionNum = 1;
		Files sendingFile = new Files();
		int privateRatio = 5; 						// private cloud에 들어갈 비율, 추후 para로 입력 받아야 함.
		File[] targetFile = new File[3];
		
		streamManager stream = new streamManager();
		
		String fileName = getFileName(sourceFile);			//입력 받아야 함 file을 잘라서 맨 마지막꺼
		
		String fixedDestPath = destPath.substring(1)+"/";						//유저가 만든 디랙토리 패스
		String fixedFileName =  makeFileName(fileName)+fileName;
		System.out.println(sourceFile);
		
		/* file divide */
		targetFile = stream.getSendFiles(sourceFile, privateRatio);
		
		
		//confirm exist or not
		
		

		//upload start
		System.out.println("upload start");
		System.out.println(targetFile);
		
		/*make request*/
		sendingFile = new Files(fileName, fixedDestPath, optionNum, ClientLauncher.getUser().getId());
		
		//aws s3 upload
		if (aWSModule.upload( sendingFile, targetFile[1]) == -1) {
			System.out.println("Sorry, aws file uploader encounters some problems. \nplease try again.");
			return -1;
			
		}

		//hdfs upload
		int isError = hdfsModule.upload(sendingFile, targetFile[2]);
		if ( isError == -1){
			System.out.println("Sorry, HDFS file uploader encounters some problems. \nplease try again.");
			return -1;
		} else if( isError == 2) {
			System.out.println("Sorry, HDFS session auth failed. \nplease try again.");
			return 2;
		}
		
		System.out.println("Upload Successfully");
		
		/* tmp내의 모든 파일들 삭제하는 method 삽입*/
		return 0;
	}
	
	

	/* 각 클라우드에서 다운로드. 
	 * sourcePath : 클라우드에 위치한 파일의 경로
	 * destPath   : 클라우드에서 다운받아 local에 저장할 경로
	 */
	public int download(String sourcePath, String destPath) throws IOException {
		
		int optionNum = 2;
		streamManager stream = new streamManager();
		
		/*sourcePath(selected Path에서 파일 이름을 추출해 각 Files객체에 넣어 각 다운로드에 넘겨준다.*/
		String fileName = getFileName(sourcePath);
		String fixedFileName = makeFileName(fileName) + fileName;
		String fixedSourcePath = getDownloadDirPath(sourcePath);
		
		String localDir = destPath + "/" + fixedSourcePath+ClientLauncher.getFileMgr().getSlash();
		
		System.out.println("\nlocalDir(in Hybrid download) : " + localDir);
		System.out.println("file name : " + fileName);
		
		Files request = new Files();
		request.setDirPath(fixedSourcePath);
		request.setFileName(fileName);
		request.setOptionNum(optionNum);
		request.setUserId(ClientLauncher.getUser().getId());
		

		File awsTmp = null;
		File hdfsTmp = null;
		//File downFile = null; //awsTmp + hdfsTmp
		
		BufferedOutputStream bos = null;
		
		//confirm exist or not
		//download start
		System.out.println("call download");
		//aws s3 download
		awsTmp = aWSModule.download(request, localDir);
		if (awsTmp == null) {
			System.out.println("다운로드 실패");
			return -1;
		}
		//hdfs download
		hdfsTmp = hdfsModule.download(request, localDir);
		if (hdfsTmp ==  null) {
			System.out.println("다운로드 실패");
			return -1;
		}
		
		//combination
		stream.restoreOrigFiles( localDir, fileName);
		
		//디렉토리에 동일 파일이 있는지 검사 필요
		/*합친 파일 쓰*/
//		downFile = new File( localDir );
//		
//		if(!downFile.exists()) 
//			downFile.mkdirs();
//		downFile = new File(localDir+fileName);
//		int fileCount = 1;
//		while (downFile.exists()) {
//			downFile = new File(localDir+fileAlreadyExists(fileName, fileCount));
//			fileCount++;
//		}
		
//		bos = new BufferedOutputStream(new FileOutputStream(downFile));
//		
//		
//		
//		if (hdfsReceiveFile.getOptionNum() == -1) {
//			System.out.println("hdfs download error...\ndelete " + fixedSourcePath+fixedFileName+" in s3 bucket");
//			return -1;
//		} 
//		bos.close();
		
		return 0;
	}
	
	public int delete(String targetPath) throws IOException {
		
		int optionNum = 3;

		String fileName = getFileName(targetPath);
		String fixedFileName = makeFileName(fileName) + fileName;
		String fixedSourcePath = getDownloadDirPath(targetPath);
		
		Files request = new Files();
		request.setDirPath(fixedSourcePath);
		request.setFileName(fileName);
		request.setOptionNum(optionNum);
		request.setUserId(ClientLauncher.getUser().getId());
		
		//confirm exist or not
		//Start delete
		boolean isSuccess;
		System.out.println("delete Start");
		//AWS delete
		isSuccess = aWSModule.delete(request);
		if (isSuccess == false) {
			System.out.println("삭제 실패");
			return -1;
		}
		//hdfs delete
		isSuccess = hdfsModule.delete(request);
		if (isSuccess ==  false) {
			System.out.println("삭제 실패");
			return -1;
		}
		return 0;
	}

	
	public HDFSClient getHdfsModule() {
		return hdfsModule;
	}

	public void setHdfsModule(HDFSClient hdfsModule) {
		this.hdfsModule = hdfsModule;
	}

	public AWSUpDown getAWSModule() {
		return aWSModule;
	}

	public void setAWSModule(AWSUpDown aWSModule) {
		this.aWSModule = aWSModule;
	}

	
}
