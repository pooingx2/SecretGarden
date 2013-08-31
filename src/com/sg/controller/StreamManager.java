package com.sg.controller;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.StringTokenizer;

import com.sg.main.ClientLauncher;
import com.sg.model.MetaData;


public class streamManager {

	/*split 정보*/
	public final static int maxSpliteSize = 10;
	public final static int sizeKB = 1024 * 1;
	//public static int streamSize  = 100 * sizeKB;
//	public int streamSize;
//	
//	/*source file*/
//	public String sourceFilePath = "d:/test/";
//	public String sourceFileName = "png.png";
//	
//	/*destination stream*/
//	public String projDir = "d:/test/";
//	public String streamPath;
////	public static String streamPath = projDir + "stream/";	//setting
//	public String streamName = sourceFileName;		//temp
//	
//	public String saveDir = "d:/";		//temp
//	public String outStreamDir = "out/";				//setting
//	public String inStreamDir = "in/";				//setting
//	public String privatePublicDir = "p/";			//setting
	public int streamSize;
	
	/*source file*/
	private String sourceFilePath;
	private String sourceFileName;
	
	/*destination stream*/
	private String projDir;
	private String streamPath;
//	public static String streamPath = projDir + "stream/";	//setting
	private String streamName;		//temp
	
	private String saveDir;		//temp
	private String outStreamDir;				//setting
	private String inStreamDir;				//setting
	private String privatePublicDir;		
	
	/*metadata*/
	private String cloudTable;
	private String filePath;
	private String fileName;
	private String fileType;
	private String file_size;
	private String stream_size;
	private String lastStream_size;
	
	private MetaData metaData;
	
	public streamManager() throws IOException {
		projDir = "";
		outStreamDir = "out/";
		inStreamDir = "in/";
		privatePublicDir = "p/";
		

		metaData = new MetaData();
		/*추후에 static으로 박아놓으면 편함*/
		/*현재 dir를 구하는 과정*/
		
//		File workingDir = new File(".");
//		if(!workingDir.exists()) 
//			workingDir.mkdirs();
//		String workingPath = workingDir.getCanonicalPath();
//		streamPath = workingPath + "/tmp/" ;
		streamPath = checkDir(".") + "/tmp/";
		
	}
	
	public MetaData getMetaData() {
		return metaData;
	}

	public void setMetaData(MetaData metaData) {
		this.metaData = metaData;
	}

	/*폴더가 존재하는지 확인후 없으면 폴더 생성*/
	private String checkDir(String dirPath) throws IOException {
		File checkDir = new File(dirPath);
		if(!checkDir.exists()) 
			checkDir.mkdirs();
		return checkDir.getCanonicalPath();
	}
	
	
	/*main*/
//	public static void main(String[] args) {
//		/*================================ Send to server ================================*/
//		getSendFiles(sourceFilePath+sourceFileName, 5);
//		System.out.println(cloudTable);
//		
//		/*================================ Receive from server ================================*/
//		
//		restoreOrigFiles(saveDir, sourceFileName);
//
//
//	}

	public File[] getSendFiles(String origFilePath, int privateRatio){
		FileInputStream origFI = null;
		
		/*File*/
		File[] output = new File[3];
		File origFile  = new File(origFilePath);

//		String streamPath = "./tmp/";//origFilePath.substring(0, origFilePath.lastIndexOf('/')) + "/stream/";
		String streamName = origFilePath.substring(origFilePath.lastIndexOf('\\') + 1, origFilePath.length());
		
		//System.out.println("path : " + streamPath);
		//System.out.println("path : " + streamName);
		
		try {
			origFI = new FileInputStream(origFilePath);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(origFile.length() > 1024)
			streamSize = (int) ((origFile.length()/10)/1024+1)*1024;
		else
			streamSize = 1024;
		System.out.println(streamSize);
		
		/*stream 생성하여 파일 분할*/
		splitFile(streamPath, streamName, origFI, origFile, privateRatio);
		
		try {
			combineTwoFile(streamPath, streamName);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		output[0] = new File(streamPath + streamName + "._metadata");
		output[1] = new File(streamPath + privatePublicDir + streamName + "._public");
		output[2] = new File(streamPath + privatePublicDir + streamName + "._private");
		
		return output;
	}
	
	public boolean restoreOrigFiles(String savePath, String targetFileName){
		
		int fileSize;
		getMetadata(streamPath, targetFileName);
		
		fileSize = Integer.parseInt(file_size);
		
		if(fileSize > 1024)
			streamSize = (int) ((fileSize/10)/1024+1)*1024;
		else
			streamSize = 1024;
		
		splitTwoFile(streamPath, targetFileName);	//두 파일을 10개로 분할
		
		/*stream들을 하나의 파일로 통합*/
		try {
			combineFile(targetFileName, streamPath, savePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		deleteTmpMetaFiles();
		
		return true;
	}
	
	private static String getUserID(){
		String id = "";
		return id;
	}

	private static String getType(String fileName){
		String typeStart = ".";
		String typeName = fileName.substring(fileName.lastIndexOf(typeStart)+1);

		return typeName;
	}
	
	/*============================= metadata =============================*/
	private void mkMetadata(String path, String name, int streamCount, File origFile, int privateRatio){
		BufferedWriter out = null;
		int i;
		int j0 = 0, j1 = 0, k = 0;

//		try {
//			out = new BufferedWriter(new FileWriter(path + name + "._metadata"));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	    
		/*meta data 초기화*/
		//cloud mapping table
		cloudTable = "";
		for(i = 0; i<streamCount+1; i++){
			k = (int) (Math.random()*10000) % 2;
			if(j0 == privateRatio)
				k = 1;
			else if(j1 == privateRatio)
				k = 0;
			cloudTable += Integer.toString(k);
			if(k == 0)
				j0++;
			else
				j1++;
		}
		/*
		String filePath = path;
		String fileName = name;
		String fileType = getType(name);
		String file_size = Long.toString(origFile.length());
		String stream_size = Integer.toString(streamSize);
		String lastStream_size = Long.toString(origFile.length() % (long)streamSize);
		String stream_count = Integer.toString(streamCount + 1);
		*/
		

		filePath = path;
		fileName = name;
		fileType = getType(name);
		file_size = Long.toString(origFile.length());
		stream_size = Integer.toString(streamSize);
		lastStream_size = Long.toString(origFile.length() % (long)streamSize);
		stream_count = Integer.toString(streamCount + 1);

		
		getMetaData().setCloudTable(cloudTable);
		getMetaData().setFilePath(path);
		getMetaData().setFileName(name);
		getMetaData().setFileType(getType(name));
		getMetaData().setFile_size(Long.toString(origFile.length()));
		getMetaData().setStream_size(Integer.toString(streamSize));
		getMetaData().setLastStream_size(Long.toString(origFile.length() % (long)streamSize));
		getMetaData().setStream_count(Integer.toString(streamCount + 1));
				
		/*account info*/
		String account_id = getUserID();
		
		//System.out.println(stream_count);
		
		/*
		
	    try {
	    	out.write(cloudTable);		//1. cloud mapping table
	    	out.newLine();				//token
	    	out.write(filePath);		//2. file path
	    	out.newLine();				//token
	    	out.write(fileName);		//3. file name
	    	out.newLine();				//token
	    	out.write(fileType);		//4. file type
	    	out.newLine();				//token
	    	out.write(file_size);		//5. file size
	    	out.newLine();				//token
	    	out.write(stream_size);		//6. stream size
	    	out.newLine();				//token
	    	out.write(lastStream_size);	//7. last stream size
	    	out.newLine();				//token
	    	out.write(stream_count);	//8. stream count
	    	out.newLine();				//token
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
	    
	    try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		*/
	
	}
	
	private void getMetadata(String path, String name){

		BufferedReader in = null;

		try {
		    in = new BufferedReader(new FileReader(path + name + "._metadata"));

		    cloudTable = in.readLine();
		    filePath = in.readLine();
			fileName = in.readLine();
			fileType = in.readLine();
			file_size = in.readLine();
			stream_size = in.readLine();
			lastStream_size = in.readLine();
			stream_count = in.readLine();
			
		    in.close();
		}
		catch (IOException e) {
			System.err.println(e);
		    System.exit(1);
	    }		
	}
	
	/*============================= splite/combine file =============================*/
	/*InputStream을 받아 매개변수로 받은 경로에 stream 파일 생성*/
	private void splitFile(String streamPath, String streamName, InputStream origFI, File origFile, int privateRatio) {
		int readCnt = 0;
		int totCnt = 0;
		int streamIndex = 0;			//stream 이름을 위한 인덱스
		byte[] readBuffer = new byte[1024];		//최소 stream size는 1KB
				
		String hashString = null;		//hash key  
		String MD5;						//md5 hash value
		
		ClientLauncher.getTaskMgr().getRunningTask().getThProgress().getProgressBar().setString("Splitting File...");
		ClientLauncher.getTaskMgr().getRunningTask().getThProgress().getProgressBar().setValue(10);
		
		try {
			/*file I/O stream*/
			BufferedInputStream bufferedFI = new BufferedInputStream(origFI);						//원본 파일
			System.out.println(streamPath + outStreamDir + streamName + streamIndex + "._outStream");
			File streamFile = new File(streamPath + outStreamDir + streamName + streamIndex + "._outStream");	//stream 파일
			FileOutputStream streamFO = new FileOutputStream(streamFile);
			
			do {
				/*원본 파일 읽기*/
				readCnt = bufferedFI.read(readBuffer);
				if (readCnt == -1) {
					break;
				}
				
				/*stream파일에 write*/
				streamFO.write(readBuffer, 0, readCnt);
				totCnt += readCnt;
				
				//get hash for each stream
				hashString = readBuffer.toString();
				MD5 = getHashMD5(hashString);
				//System.out.println(hashString);
				//System.out.println(MD5);
				
				/*다음 스트림*/
				if (totCnt % streamSize == 0) {
					streamFO.flush();
					streamFO.close();
					streamFile = new File(streamPath + outStreamDir + streamName + (++streamIndex) + "._outStream");
					streamFO = new FileOutputStream(streamFile);
				}
			} while (true);
			
			/*metadata*/
			mkMetadata(streamPath, streamName, streamIndex, origFile, privateRatio);
			
			origFI.close();
			streamFO.flush();
			streamFO.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("Split success!");
	}
	
	/*매개변수로 받은 경로에 있는 stream 파일들을 한 파일로 통합*/
	private void combineFile(String origFileName, String streamPath, String savePath) throws FileNotFoundException, IOException {
		
		
		
		int readCnt = 0;
		byte[] buf = null;
		
		/*file I/O stream*/
		File streamFiles = new File(streamPath + inStreamDir );
		String[] files = streamFiles.list();				//stream 파일 리스트
		checkDir(savePath);
		
		/*파일 중복시 처리*/
		int fileCount = 1;
		File downFile = new File(savePath + origFileName);
		while (downFile.exists()) {
			downFile = new File(savePath+fileAlreadyExists(origFileName, fileCount));
			fileCount++;
		}
		
		/*File write*/
		FileOutputStream combinedFO = new FileOutputStream(downFile);
		FileInputStream streamFI = null;

		for (int i = 0; i < files.length; i++) {
			if(!files[i].matches(origFileName+"."+"._inStream.")){
				continue;
			}
	
			streamFI = new FileInputStream(streamPath + inStreamDir + files[i]);
			buf = new byte[1024];
			readCnt = 0;
			
			while ((readCnt = streamFI.read(buf)) > -1) {
				combinedFO.write(buf, 0, readCnt);
			}
			streamFI.close();
		}

		combinedFO.flush();
		combinedFO.close();
		
		//in 디렉토리 tmp 파일들 삭제
		deleteTmpInFiles();
		System.out.println("Combine success!");
	}
	
	/*다운로드시 localpath에 중복된 파일이 있을경우 처리*/
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
	
	/*============================= splite/combine 2 files =============================*/
	
	/*InputStream을 받아 매개변수로 받은 경로에 stream 파일 생성*/
	private void splitTwoFile(String streamPath,  String streamName) {
		int readCnt = 0;
		int totCnt = 0;
		int streamIndex = 0;			//stream 이름을 위한 인덱스
		byte[] readBuffer = new byte[1024];		//최소 stream size는 1KB
		int public_ok = 0, private_ok = 0;
		int i = 0;
	
		String hashString = null;		//hash key  
		String MD5;						//md5 hash value

		
		try {
			
			/*File*/
			File privateFile  = new File(streamPath + privatePublicDir  + streamName + "._private");
			//System.out.println(streamPath + streamName + "._private");
			FileInputStream privateFI = new FileInputStream(privateFile);
			
			File publicFile  = new File(streamPath + privatePublicDir + streamName + "._public");
			//System.out.println(streamPath + streamName + "._public");
			FileInputStream publicFI = new FileInputStream(publicFile);
			
			/*first file I/O stream*/
			BufferedInputStream bufferedFI0 = new BufferedInputStream(privateFI);
			File streamFile0 = null;
			FileOutputStream streamFO0 = null;
			
			/*second file I/O stream*/
			BufferedInputStream bufferedFI1 = new BufferedInputStream(publicFI);
			File streamFile1 = null;
			FileOutputStream streamFO1 = null;
			
			do {
				if(cloudTable.substring(i,i+1).equals("0")){
					//System.out.printf("###0");
					streamFile0 = new File(streamPath + inStreamDir + streamName + (streamIndex++) + "._inStream0");
					streamFO0 = new FileOutputStream(streamFile0);
					
					/*원본 파일 읽기*/
					while(true){
						readCnt = bufferedFI0.read(readBuffer);
						if (readCnt == -1) {
							private_ok = 1;
							//System.out.println("private ok!");
							break;
						}
						if(private_ok == 1)
							System.out.println("private error!");
				
						/*stream write*/
						streamFO0.write(readBuffer, 0, readCnt);
						totCnt += readCnt;
						
						//get hash for each stream
						hashString = readBuffer.toString();					
						MD5 = getHashMD5(hashString);
						//System.out.println(hashString);
						//System.out.println(MD5);
						
						/*다음 스트림*/
						if (totCnt % streamSize == 0) {
							streamFO0.flush();
							streamFO0.close();
							break;
						}
					}
				}
				else{
					//System.out.printf("###1");
					streamFile1 = new File(streamPath + inStreamDir + streamName + (streamIndex++) + "._inStream1");
					streamFO1 = new FileOutputStream(streamFile1);
					
					/*원본 파일 읽기*/
					while(true){
						readCnt = bufferedFI1.read(readBuffer);
						if (readCnt == -1) {
							public_ok = 1;
							//System.out.println("public ok!");
							break;
						}
						if(public_ok == 1)
							System.out.println("public error!");
					
						/*stream write*/
						streamFO1.write(readBuffer, 0, readCnt);
						totCnt += readCnt;

						//get hash for each stream
						hashString = readBuffer.toString();					
						MD5 = getHashMD5(hashString);
						//System.out.println(hashString);
						//System.out.println(MD5);
						
						/*다음 스트림*/
						if (totCnt % streamSize == 0) {
							streamFO1.flush();
							streamFO1.close();			
							break;
						}	
					}
				}
				
				if(i<Integer.parseInt(stream_count)-1)
					i++;
				else
					break;
				
			} while (public_ok == 0 || private_ok == 0);

			
			privateFI.close();
			if(streamFO0 != null){
				streamFO0.flush();
				streamFO0.close();
			}
			
			publicFI.close();
			if(streamFO1 != null) {
				streamFO1.flush();
				streamFO1.close();
			}
			
			//temp private, public 파일 삭제
			deletePFile();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	
		System.out.println("Split two file success!");
	}
	
	/*매개변수로 받은 경로에 있는 stream 파일들을 한 파일로 통합*/
	private void combineTwoFile(String  streamPath, String origFileName) throws FileNotFoundException, IOException {
		
		ClientLauncher.getTaskMgr().getRunningTask().getThProgress().getProgressBar().setString("Mixing file...");
		ClientLauncher.getTaskMgr().getRunningTask().getThProgress().getProgressBar().setValue(20);
		
		int readCnt = 0;
		byte[] buf = null;
		int j = 0;
		
		File streamFiles = new File(streamPath + outStreamDir );
		String[] files = streamFiles.list();				//stream파일 리스트
		
		/*first file I/O stream*/
		FileOutputStream combinedFO0 = new FileOutputStream(streamPath + privatePublicDir + origFileName + "._private");	//���յ� ����
		FileInputStream streamFI0 = null;
		
		/*second file I/O stream*/
		FileOutputStream combinedFO1 = new FileOutputStream(streamPath + privatePublicDir  + origFileName + "._public");	//���յ� ����
		FileInputStream streamFI1 = null;
	
		
		for (int i = 0; i < files.length; i++) {
			if(!files[i].matches(origFileName+"."+"._outStream")){
				continue;
			}
			
			//if mapping info is first file
			if(cloudTable.substring(j,j+1).equals("0")){
				//System.out.println("###0");
				streamFI0 = new FileInputStream(streamPath + outStreamDir + files[i]);
				buf = new byte[1024];
				readCnt = 0;
				/*통합될 파일에  write*/
				while ((readCnt = streamFI0.read(buf)) > -1) {
					combinedFO0.write(buf, 0, readCnt);
				}
				streamFI0.close();
			}
			//if mapping info is second file
			else{
				//System.out.println("###1");
				/*각 stream 파일의 InputStream*/
				streamFI1 = new FileInputStream(streamPath + outStreamDir  + files[i]);
				buf = new byte[1024];
				readCnt = 0;
				/*통합될 파일에  write*/
				while ((readCnt = streamFI1.read(buf)) > -1) {
					combinedFO1.write(buf, 0, readCnt);
				}
				streamFI1.close();
			}
			j++;
			// System.out.println("##########");
		}
		// System.out.println("1");
		
		combinedFO0.flush();
		combinedFO0.close();
		combinedFO1.flush();
		combinedFO1.close();
		
		deleteTmpOutFiles();
		System.out.println("Combine two file success!");	
	}
	
	/*============================= delete files =============================*/
	private void _deleteTmpFiles(String filePath){
		File target = new File(filePath);
		if(target.exists()){
			if(target.delete())
				System.out.println("Delete seccess!");
			else
				System.out.println("Delete fail!");
		}else
			System.out.println("no file!");
	}
	
	
	public void deleteTmpOutFiles(){
		/*out*/
		File streamFiles = new File(streamPath + outStreamDir );
		
		String[] files = streamFiles.list();				//stream파일 리스트
				
		for (int i = 0; i < files.length; i++) {
			System.out.println(streamPath + outStreamDir +files[i]);
			_deleteTmpFiles(streamPath + outStreamDir + files[i]);
		}
	}
	
	public void deleteTmpInFiles(){
		/*in*/
		File streamFiles = new File(streamPath + inStreamDir);
		String[] files = streamFiles.list();				//stream파일 리스트

		for (int i = 0; i < files.length; i++) {
			System.out.println(streamPath + inStreamDir +files[i]);
			_deleteTmpFiles(streamPath + inStreamDir + files[i]);
		}
	}

	public void deletePFile(){
		File streamFiles = new File(streamPath + privatePublicDir);
		String[] files = streamFiles.list();
		
		for (int i = 0; i < files.length; i++) {
			if(files[i].matches(".*._private")){
				_deleteTmpFiles(streamPath + privatePublicDir + files[i]);
			}
			if(files[i].matches(".*._public")){
				_deleteTmpFiles(streamPath + privatePublicDir + files[i]);
			}
		}
	}
	
	public void deleteTmpMetaFiles(){
		File streamFiles = new File(streamPath);
		String[] files = streamFiles.list();
		
		for (int i = 0; i < files.length; i++) {
			if(files[i].matches(".*._metadata")){
				_deleteTmpFiles(streamPath + files[i]);
			}
		}
	}
	
	/*============================= md5 hash =============================*/
	
	public String getHashMD5(String str){
		String hashMD5 = ""; 

		try{
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			byte byteData[] = md.digest();
			StringBuffer sb = new StringBuffer();
			
			for(int i = 0 ; i < byteData.length ; i++){
				sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
			}

			hashMD5 = sb.toString();			

		}catch(NoSuchAlgorithmException e){
			e.printStackTrace(); 
			hashMD5 = null; 

		}

		return hashMD5;
	}
	
	public int getStreamSize() {
		return streamSize;
	}


	public void setStreamSize(int streamSize) {
		this.streamSize = streamSize;
	}


	public String getSourceFilePath() {
		return sourceFilePath;
	}


	public void setSourceFilePath(String sourceFilePath) {
		this.sourceFilePath = sourceFilePath;
	}


	public String getSourceFileName() {
		return sourceFileName;
	}


	public void setSourceFileName(String sourceFileName) {
		this.sourceFileName = sourceFileName;
	}


	public String getProjDir() {
		return projDir;
	}


	public void setProjDir(String projDir) {
		this.projDir = projDir;
	}


	public String getStreamPath() {
		return streamPath;
	}


	public void setStreamPath(String streamPath) {
		this.streamPath = streamPath;
	}


	public String getStreamName() {
		return streamName;
	}


	public void setStreamName(String streamName) {
		this.streamName = streamName;
	}


	public String getSaveDir() {
		return saveDir;
	}


	public void setSaveDir(String saveDir) {
		this.saveDir = saveDir;
	}


	public String getOutStreamDir() {
		return outStreamDir;
	}


	public void setOutStreamDir(String outStreamDir) {
		this.outStreamDir = outStreamDir;
	}


	public String getInStreamDir() {
		return inStreamDir;
	}


	public void setInStreamDir(String inStreamDir) {
		this.inStreamDir = inStreamDir;
	}


	public String getPrivatePublicDir() {
		return privatePublicDir;
	}


	public void setPrivatePublicDir(String privatePublicDir) {
		this.privatePublicDir = privatePublicDir;
	}


	public String getCloudTable() {
		return cloudTable;
	}


	public void setCloudTable(String cloudTable) {
		this.cloudTable = cloudTable;
	}


	public String getFilePath() {
		return filePath;
	}


	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}


	public String getFileName() {
		return fileName;
	}


	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	public String getFileType() {
		return fileType;
	}


	public void setFileType(String fileType) {
		this.fileType = fileType;
	}


	public String getFile_size() {
		return file_size;
	}


	public void setFile_size(String file_size) {
		this.file_size = file_size;
	}


	public String getStream_size() {
		return stream_size;
	}


	public void setStream_size(String stream_size) {
		this.stream_size = stream_size;
	}


	public String getLastStream_size() {
		return lastStream_size;
	}


	public void setLastStream_size(String lastStream_size) {
		this.lastStream_size = lastStream_size;
	}


	public String getStream_count() {
		return stream_count;
	}


	public void setStream_count(String stream_count) {
		this.stream_count = stream_count;
	}


	public static int getMaxsplitesize() {
		return maxSpliteSize;
	}


	public static int getSizekb() {
		return sizeKB;
	}


	private String stream_count;
	
	streamManager(String path){
		streamPath = "./tmp/";
		
		
	}
	
}