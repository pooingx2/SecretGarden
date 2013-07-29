package com.sg.cloud;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;

import com.sg.main.Constants;
import com.sg.model.Files;

public class Hybrid {

	private HDFSClient hdfsModule;
	private AWSUpDown aWSModule;
	private String userId;
	
	public Hybrid(){
		hdfsModule = new HDFSClient();
		aWSModule = new AWSUpDown();
		this.userId = null;
	}

	public boolean auth(String type, String id, String pw) {

		boolean isConnected = false;	// 1: connected		0 : disConnected

		switch (type){

		case Constants.amazon : 		// aws s3 connect
			String keyId = id;			// 입력 받아야 함
			String key = pw;			// 입력 받아야 함
			aWSModule.setKey(key);
			aWSModule.setKeyId(keyId);
			
			isConnected= aWSModule.auth(userId);
			break;

		case Constants.hadoop : 		// hdfs connect
			String hdfsIp = id;			// 입력 받아야 함
			int hdfsPort = 15000;		// 입력 받아야 함
			hdfsModule.setDestIp(hdfsIp);
			hdfsModule.setDestPort(hdfsPort);
			
			isConnected = hdfsModule.auth();
			break;
		}

		return isConnected;
	}
	public int disconnected() {
		// 제거 모듈 만들어 줘야 함
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
		sendingFile = new Files(fixedFileName, fixedDestPath, 1, this.userId);
		//aws s3 upload
		if (aWSModule.upload( sendingFile, targetFile) == -1) {
			System.out.println("Sorry, aws file uploader encounters some problems. \nplease try again.");
			return -1;
			
		}

		//hdfs upload
		hdfsModule.upload(sendingFile, targetFile);

		return 0;
	}
	


	/* 각 클라우드에서 다운로드. 
	 * sourcePath : 클라우드에 위치한 파일의 경로
	 * destPath   : 클라우드에서 다운받아 local에 저장할 경로
	 */
	public int download(String sourcePath, String destPath) throws IOException {
		
		/*sourcePath에서 파일 이름을 추출해 각 Files객체에 넣어 각 다운로드에 넘겨준다.*/
		Files request = new Files();
		String fileName = getFileName(sourcePath);		///root/asd/filename		
		
		String fixedSourcePath = getDownloadDirPath(sourcePath);
		String fixedFileName = makeFileName(fileName) + fileName;
		
		request.setDirPath(fixedSourcePath);
		request.setFileName(fixedFileName);
		request.setOptionNum(2);
		request.setUserId(userId);
		

		byte[] awsBuf = null;
		byte[] hdfsBuf = null;
		File downFile = null;
		BufferedOutputStream bos = null;
		Files awsReceiveFile = null;
		Files hdfsReceiveFile = null;
		
		//aws s3 download
		awsReceiveFile = aWSModule.download(request);
		System.out.println("optnum : " + awsReceiveFile.getOptionNum());
		if (awsReceiveFile.getOptionNum() == -1) {
			System.out.println("aws download error...");
			return -1;
		}else{
			
			awsBuf = awsReceiveFile.getFileBuf();
		}
		//hdfs download
		hdfsReceiveFile = hdfsModule.download(request);
		
		if (hdfsReceiveFile.getOptionNum() == -1) {
			System.out.println("hdfs download error...\ndelete " + fixedSourcePath+fixedFileName+" in s3 bucket");
			return -1;
		} else{
			hdfsBuf = hdfsReceiveFile.getFileBuf();
		}

		//디렉토리에 동일 파일이 있는지 검사 필요
		downFile = new File( destPath+fileName );
		bos = new BufferedOutputStream(new FileOutputStream(downFile));
		
		bos.write(hdfsBuf);
		
		try{
			bos.write(awsBuf);
		} catch (NullPointerException es){
			System.out.println("파일을 열기 위한 경로 : " + destPath+fileName);
		}
		bos.close();

		return 0;
	}
	
	public int deleteFile() {
		return 0;
	}

	
	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
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
