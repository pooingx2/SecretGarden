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
//			objOutput.writeObject(request);
//			objOutput.flush();
//			
//		/*寃곌낵瑜��살뼱�⑤떎*/
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

	
	/*accessKey auth sessionKey receive*/
	/*login request*/
	@Override
	public boolean auth(Files request) throws IOException {
		boolean isConnected;
		isConnected = connectToHDFS();
		System.out.println("sending Request");
		
		int optionNum = 0; 
		
		/*accesskey send*/
		objOutput.writeObject(request);
		objOutput.flush();

		/*sessionkey receive*/
		try {
			recievFile = (Files) objInput.readObject();
		
		} catch (ClassNotFoundException e) {
			System.out.println("what's the proplem??");
			return false;
		}
		
		System.out.println("recv option : " + recievFile.getOptionNum());
		System.out.println("recv sessionKey : " + recievFile.getSessionKey());
		//access key auth(sessionkey receive)
		if (recievFile.getOptionNum() == 0) {
			this.sessionKey = recievFile.getSessionKey();
		}else if (recievFile.getOptionNum() == -1) {
			return false;
		}else if (recievFile.getOptionNum() == 2) {
			System.out.println("accesskey auth fail");
			return false;
		}
		
		System.out.println("here");
		return isConnected;
	}

	@Override
	public int upload(Files fileDescript, File targetFile) throws IOException {
			
		int optionNum = 1;
		int bytesRead = 0;
		byte[] buf = new byte[10240];
		readFile = new BufferedInputStream(new FileInputStream(targetFile));

		
		connectToHDFS();
		fileDescript.setSessionKey(this.sessionKey);

		System.out.println("hdfs upload Start!");
		objOutput.writeObject(fileDescript);
		objOutput.flush();
		
		try {
			recievFile = (Files) objInput.readObject();
		
		} catch (ClassNotFoundException e) {
			System.out.println("WTF??");

		}
		if (recievFile.getOptionNum() == -1) {
			System.out.println("upload fail");
			return -1;
		}
		if (recievFile.getOptionNum() == 2) {
			System.out.println("session auth fail");
			return 2;
		}
		//test
		int totalBytesRead = 0;
		while (-1 != (bytesRead = readFile.read(buf, 0, buf.length))) {
			
			totalBytesRead += bytesRead;
//			System.out.println("sending bytes : " + totalBytesRead);
			ClientLauncher.getTaskMgr().getRunningTask().setCur(totalBytesRead);
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
		
		/*check working dir exists*/
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
			System.out.println("WTF??");

		}
		if (recievFile.getOptionNum() == -1) {
			System.out.println("HDFS download fail");
			return null;
		}else if (recievFile.getOptionNum() == 2) {
			System.out.println("session auth fail");
			return null;
		}
		
		
		// download
		
		System.out.println("download Start " );
		int totalByteRead = 0;
		while(-1 != (bytesRead = reader.read(buf, 0,buf.length))) {
			totalByteRead += bytesRead;
			//System.out.println("recv HDFS length :"+totalByteRead);
			bos.write(buf, 0, bytesRead);
			ClientLauncher.getTaskMgr().getRunningTask().setCur(ClientLauncher.getTaskMgr().getRunningTask().getCur()+totalByteRead);
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
			System.out.println("file cannot find.");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("classNotFound??");

		}
		if (recievFile.getOptionNum() == 1) {
			System.out.println("HDFS delete fail");
			return false;
		} else if (recievFile.getOptionNum() == 2) {
			System.out.println("session auth failed");
			return false;
		} 
		return true;
	}
	

	private String generateRandomKey() {
		
		/*�붾퉬����옣*/
		
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
