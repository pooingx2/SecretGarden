package com.sg.cloud;

import java.io.FileOutputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;

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

	public boolean auth(String type, String id, String port, String pw) {

		boolean isConnected = false;	// 1: connected		0 : disConnected
		
			
		
		switch (type){

		case Constants.amazon : 		// aws s3 connect
			String keyId = "AKIAIUXPHCYBAHGUZGEQ";//id;			// 입력 받아야 함
			String key = "z3L3XdNwpWPx0R37bToPR+O85cmSoZTJrucfb4xE";//pw;			// 입력 받아야 함
			aWSModule.setKey(key);
			aWSModule.setKeyId(keyId);
			
			isConnected= aWSModule.auth(ClientLauncher.getUser().getId());
			break;

		case Constants.hadoop : 		// hdfs connect
			String hdfsIp = "211.189.127.91";//id;			// 입력 받아야 함
			int hdfsPort = 15000;//Integer.parseInt(port);		// 입력 받아야 함
			hdfsModule.setDestIp(hdfsIp);
			hdfsModule.setDestPort(hdfsPort);
			
			isConnected = hdfsModule.auth();
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
		
		String fileName = getFileName(sourceFile);			//입력 받아야 함 file을 잘라서 맨 마지막꺼
		
		String fixedDestPath = destPath.substring(1)+"/";						//유저가 만든 디랙토리 패스
		String fixedFileName =  makeFileName(fileName)+fileName;
	
		
		File targetFile = new File(sourceFile);
		sendingFile = new Files(fixedFileName, fixedDestPath, optionNum, ClientLauncher.getUser().getId());
		//confirm exist or not
		//upload start
		System.out.println("upload start");
		//aws s3 upload
		if (aWSModule.upload( sendingFile, targetFile) == -1) {
		System.out.println("Sorry, aws file uploader encounters some problems. \nplease try again.");
			return -1;
			
		}

		//hdfs upload
		if (hdfsModule.upload(sendingFile, targetFile) == -1){
			System.out.println("Sorry, HDFS file uploader encounters some problems. \nplease try again.");
			return -1;
		}
		
		System.out.println("Upload Successfully");
		
		return 0;
	}
	
	public String fileAlreadyExists(String fileName, int count) {
		int i = 0;
		
		String fixedName;
		StringTokenizer st = new StringTokenizer(fileName, ".");
		String[] token = new String[fileName.length()/2];
		//fileName.substring(fileName.lastIndexOf("."), fileName.length())
		while(st.hasMoreElements()) {			
			token[i] = st.nextToken();
			i++;
		}
		fixedName = token[0] + "(" + count + ")." + token[1];
		
		return fixedName;
	}

	/* 각 클라우드에서 다운로드. 
	 * sourcePath : 클라우드에 위치한 파일의 경로
	 * destPath   : 클라우드에서 다운받아 local에 저장할 경로
	 */
	public int download(String sourcePath, String destPath) throws IOException {
		
		/*sourcePath(selected Path에서 파일 이름을 추출해 각 Files객체에 넣어 각 다운로드에 넘겨준다.*/
		String fileName = getFileName(sourcePath);
		String fixedFileName = makeFileName(fileName) + fileName;
		String fixedSourcePath = getDownloadDirPath(sourcePath);
		
		String localDir = fixedSourcePath+ClientLauncher.getFileMgr().getSlash();
		
		Files request = new Files();
		request.setDirPath(fixedSourcePath);
		request.setFileName(fixedFileName);
		request.setOptionNum(2);
		request.setUserId(ClientLauncher.getUser().getId());
		

		File awsTmp = null;
		File hdfsTmp = null;
		File downFile = null;
		
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
	
	public int delete(String targetPath) {
		
		String fileName = getFileName(targetPath);
		String fixedFileName = makeFileName(fileName) + fileName;
		String fixedSourcePath = getDownloadDirPath(targetPath);
		
		Files request = new Files();
		request.setDirPath(fixedSourcePath);
		request.setFileName(fixedFileName);
		request.setOptionNum(2);
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
