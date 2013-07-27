package com.sg.cloud;

import java.io.FileOutputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;

import com.sg.main.Constants;

public class Hybrid {

	private HDFSClient hdfsModule;
	private AWSUpDown aWSModule;

	public Hybrid(){
		hdfsModule = new HDFSClient();
		aWSModule = new AWSUpDown();
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

	public boolean auth(String type, String id, String pw) {

		boolean isConnected = false;	// 1: connected		0 : disConnected

		switch (type){

		case Constants.amazon : 		// aws s3 connect
			String keyId = id;			// 입력 받아야 함
			String key = pw;			// 입력 받아야 함
			aWSModule.setKey(key);
			aWSModule.setKeyId(keyId);
			isConnected= aWSModule.auth();
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
	
	private String getFileName(String path) {
		StringTokenizer token = new StringTokenizer(path,"\\");
		String fileName = null;
		while(token.hasMoreElements()) {
			fileName = token.nextToken();
		}
		System.out.println(fileName);
		return fileName;
	}
	public int upload(String file, String path) throws IOException {
		//upload는 모두 transaction을 지켜야 하기 때문에 redo와 undo 처리가 필요
		String fileName = getFileName(file);			//입력 받아야 함 file을 잘라서 맨 마지막꺼
		String dirPath = path;						//유저가 만든 디랙토리 패스
		System.out.println("\n\n\n\n\n!!!!!!TEST!!!!!!! : "+path);
		String userId = "sungjin";
		String realPos = makeFileName(fileName);
		String uploadName = realPos+fileName;
		File targetFile = new File(file);

		//aws s3 upload
		aWSModule.upload( uploadName, userId, targetFile, dirPath);		

		//hdfs upload
		hdfsModule.upload(fileName, userId, targetFile, dirPath+realPos);

		return 0;
	}
	public int download() throws IOException {
		String dirPath = "../";						//입력 필
		String sourcePath = "this/is/a/test/";		//입력 필요
		String fileName = "test.txt";					//입력 필요
		String userId = "sungjin";					//need input
		String sFileName = makeFileName(fileName) + fileName;
		byte[] awsBuf = null;
		byte[] hdfsBuf = null;
		File downFile = null;
		BufferedOutputStream bos = null;


		//aws s3 download
		awsBuf = aWSModule.download(sourcePath + sFileName, userId);
		System.out.println("AWSModule doesn't have problem");
		//hdfs download
		hdfsBuf = hdfsModule.download(sourcePath, userId, sFileName);

		//디렉토리에 동일 파일이 있는지 검사 필요
		downFile = new File( dirPath+fileName );

		bos = new BufferedOutputStream(new FileOutputStream(downFile));
		bos.write(hdfsBuf);
		bos.write(awsBuf);
		bos.close();

		return 0;
	}
	public int deleteFile() {
		return 0;
	}

	public String makeFileName(String orginName) {
		StringTokenizer st = new StringTokenizer(orginName, ".");
		String fixedName = new String();

		while(st.hasMoreTokens()){
			fixedName += st.nextToken();
		}
		fixedName = fixedName + "/";

		return fixedName;
	}
	//	
	//	public static void main(String[] args) {
	//		Hybrid test = new Hybrid();
	//		test.Auth();
	//		
	//		try {
	////			test.Upload();
	//			test.Download();
	//			System.out.println("here");
	//		} catch (IOException e) {
	//			e.printStackTrace();
	//		}
	//	}
}
