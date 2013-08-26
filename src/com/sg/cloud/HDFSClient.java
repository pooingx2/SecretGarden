package com.sg.cloud;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

import com.sg.main.ClientLauncher;
import com.sg.model.Files;

public class HDFSClient implements PrivateUpDown1 {

	private String destIp;
	private int destPort;
	private Socket sock;
	
	private String accessKey;
	private String sessionKey; 
	
	private InputStream reader = null;
	private OutputStream writer = null;
	private ObjectInputStream objInput = null;
	private ObjectOutputStream objOutput = null;
	private Files recievFile = null;
	
	BufferedInputStream readFile = null;
	BufferedOutputStream writeFile = null;
	

	
//	public String sendAccessKey2Serv(Files request) {
//		
//		
//		/*sending Request*/
//		System.out.println("sending Request");
//		try {
//			String accessKey = "aa";	//generateRandomKey();
//			request.setAccessKey(accessKey);
//			objOutput.writeObject(request);
//			objOutput.flush();
//			
//		/*결과를 얻어온다*/
//			Files recvFile = new Files();
//			recvFile = (Files) objInput.readObject();
//			this.accessKey = recvFile.getAccessKey();
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		System.out.println(accessKey);
//		
//		return accessKey;
//		
//	}
	private boolean connectToHDFS() {
		try {

			sock = new Socket(destIp, destPort);
			reader = sock.getInputStream();
			writer = sock.getOutputStream();
			objOutput = new ObjectOutputStream(writer);
			objInput = new ObjectInputStream(reader);
			
			
		} catch (UnknownHostException e) {
			System.out.println("connect Error");
			return false;
		} catch (IOException e) {
			System.out.println("IOException");
			return false;
		}
		System.out.println("networking established");
		return true;
	}

	
	/*accessKey를 통해 인증 후 sessionKey 받아*/
	/*login시*/
	@Override
	public boolean auth(Files request) throws IOException {
		boolean isConnected;
		isConnected = connectToHDFS();
		System.out.println("sending Request");
		
		int optionNum = 0; 
		
		/*accesskey로 인증*/
		objOutput.writeObject(request);
		objOutput.flush();

		/*session키를 받아옴*/
		try {
			recievFile = (Files) objInput.readObject();
		
		} catch (ClassNotFoundException e) {
			System.out.println("클래스를 찾지 못했습니다. 왜???");
			return false;
		}
		
		System.out.println("recv option : " + recievFile.getOptionNum());
		System.out.println("recv sessionKey : " + recievFile.getSessionKey());
		//access key 인증(sessionkey 받아오기)
		if (recievFile.getOptionNum() == 0) {
			this.sessionKey = recievFile.getSessionKey();
		}else if (recievFile.getOptionNum() == -1) {
			return false;
		}else if (recievFile.getOptionNum() == 2) {
			System.out.println("인증 실패");
			return false;
		}
		
		System.out.println("here");
		return isConnected;
	}

	@Override
	public int upload(Files fileDescript, File targetFile) throws IOException {
		/*
		 * 디렉토리 중복 여부 확인 필요
		 */		
		int optionNum = 1;
		int bytesRead = 0;
		byte[] buf = new byte[10240];
		readFile = new BufferedInputStream(new FileInputStream(targetFile));

		/*인증 후 재 연결*/
		connectToHDFS();
		fileDescript.setSessionKey(this.sessionKey);

		System.out.println("hdfs upload Start!");
		objOutput.writeObject(fileDescript);
		objOutput.flush();
		
		try {
			recievFile = (Files) objInput.readObject();
		
		} catch (ClassNotFoundException e) {
			System.out.println("클래스를 찾지 못했습니다. 왜???");

		}
		if (recievFile.getOptionNum() == -1) {
			System.out.println("업로드 실패");
			return -1;
		}
		if (recievFile.getOptionNum() == 2) {
			System.out.println("session 인증 실패");
			return 2;
		}
		//test
		int totalBytesRead = 0;
		while (-1 != (bytesRead = readFile.read(buf, 0, buf.length))) {
			
			totalBytesRead += bytesRead;
			System.out.println("sending bytes : " + totalBytesRead);
			//ClientLauncher.getTaskMgr().getRunningTask().setCur(totalBytesRead);
			writer.write(buf, 0, bytesRead);
		}
		writer.flush();
		System.out.println("complete...");
		writer.close();
		
		
		sock.close();
		return 0;
	}

	@Override
	public File download(Files request, String localPath) throws IOException {

		int bytesRead = 0;
		byte[] buf=new byte[10240];
		
		connectToHDFS();
		request.setSessionKey(this.sessionKey);
		
		/*tmp file들이 저장될 dir를 구하는 과정*/
		String streamPath;
		File workingDir = new File(".");
		if(!workingDir.exists()) 
			workingDir.mkdirs();
		String workingPath = workingDir.getCanonicalPath();
		streamPath = workingPath + "/tmp/" + "p/";
		File tmpFile = new File(streamPath + request.getFileName() + "._private");
		
//		File tmpDir = new File(localPath);
//		if(!tmpDir.exists()) 
//			tmpDir.mkdirs();
//		File tmpFile = new File(localPath+"fileH.tmp");
//		
		
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(tmpFile)) ;
		
		// request
		System.out.println("sending Request");
		objOutput.writeObject(request);
		objOutput.flush();
		
		
		try {
			recievFile = (Files) objInput.readObject();
		
		} catch (ClassNotFoundException e) {
			System.out.println("클래스를 찾지 못했습니다. 왜???");

		}
		if (recievFile.getOptionNum() == -1) {
			System.out.println("HDFS에 파일이 존재하지 않음");
			return null;
		}else if (recievFile.getOptionNum() == 2) {
			System.out.println("session 인증 실패");
			return null;
		}
		
		
		// download
		
		System.out.println("download Start " );
		int totalByteRead = 0;
		while(-1 != (bytesRead = reader.read(buf, 0,buf.length))) {
			totalByteRead += bytesRead;
			System.out.println("recv HDFS length :"+totalByteRead);
			bos.write(buf, 0, bytesRead);
		}
		
		System.out.println("HDFS complete");
		bos.flush();
		
		return tmpFile;
	}

	public void disconnected() {
		try {
			sock.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			
		}
	}

	@Override
	public boolean delete(Files request) throws IOException {

		connectToHDFS();
		request.setSessionKey(this.sessionKey);
		
		objOutput.writeObject(request);
		objOutput.flush();
		
		
		try {
			recievFile = (Files) objInput.readObject();
		} catch (IOException e) {
			System.out.println("object 수신 실패");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("클래스를 찾지 못했습니다. 왜???");

		}
		if (recievFile.getOptionNum() == 1) {
			System.out.println("HDFS에 파일이 존재하지 않음");
			return false;
		} else if (recievFile.getOptionNum() == 2) {
			System.out.println("session 인증 실패");
			return false;
		} 
		return true;
	}
	

	private String generateRandomKey() {
		
		/*디비에 저장*/
		
		char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < 20; i++) {
		    char c = chars[random.nextInt(chars.length)];
		    sb.append(c);
		}
		String output = sb.toString();
		System.out.println(output);
		return output;
	}
	
	
	public String getDestIp() {
		return destIp;
	}

	public void setDestIp(String destIp) {
		this.destIp = destIp;
	}

	public int getDestPort() {
		return destPort;
	}

	public void setDestPort(int destPort) {
		this.destPort = destPort;
	}

	@Override
	public int upload() throws IOException {

		return 0;
	}

	@Override
	public int download() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}


	public String getAccessKey() {
		return accessKey;
	}


	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

}
