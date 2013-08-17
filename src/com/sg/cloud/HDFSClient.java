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

import com.sg.main.ClientLauncher;
import com.sg.model.Files;

public class HDFSClient implements PrivateUpDown1 {

	private String destIp;
	private int destPort;
	private Socket sock;
	private InputStream reader = null;
	private OutputStream writer = null;
	private ObjectInputStream objInput = null;
	private ObjectOutputStream objOutput = null;
	private Files recievFile = null;
	BufferedInputStream readFile = null;
	BufferedOutputStream writeFile = null;

	public HDFSClient() {

	}

	private boolean connectToHDFS() {
		try {

			sock = new Socket(destIp, destPort);
			reader = sock.getInputStream();
			writer = sock.getOutputStream();
			objOutput = new ObjectOutputStream(writer);
			objInput = new ObjectInputStream(reader);

			System.out.println("here");
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

	@Override
	public boolean auth() {
		boolean isConnected;
		isConnected = connectToHDFS();
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

		System.out.println("hdfs upload Start!");

		objOutput.writeObject(fileDescript);
		objOutput.flush();
		
		
		//test
		int totalBytesRead = 0;
		while (-1 != (bytesRead = readFile.read(buf, 0, buf.length))) {
			
			totalBytesRead += bytesRead;
			System.out.println("sending bytes : " + totalBytesRead);
			writer.write(buf, 0, bytesRead);
		}
		writer.flush();
		System.out.println("complete...");
		writer.close();
		// sock.close();
		return 0;
	}

	@Override
	public File download(Files request, String localPath) throws IOException {

		int bytesRead = 0;
		byte[] buf=new byte[10240];
		File tmpDir = new File(localPath);
		if(!tmpDir.exists()) 
			tmpDir.mkdirs();
		File tmpFile = new File(localPath+"fileH.tmp");
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(tmpFile)) ;
		// request
		System.out.println("sending Request");
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
		if (recievFile.getOptionNum() == -1) {
			System.out.println("HDFS에 파일이 존재하지 않음");
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
	public int deleteFile() {
		// TODO Auto-generated method stub
		return 0;
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

}
