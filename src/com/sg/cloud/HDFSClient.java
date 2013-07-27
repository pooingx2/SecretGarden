package com.sg.cloud;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

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
	
	public HDFSClient(){
		
	}
	
	private boolean connectToHDFS() {
		try {
			sock = new Socket(destIp, destPort);
			reader = sock.getInputStream();
			writer = sock.getOutputStream();
			System.out.println(writer);
		} catch (UnknownHostException e) {
			System.out.println("connect Error");
			return false;
		} catch (IOException e) {
			System.out.println("IOException");
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
	public int upload(String fileName, String userId, File targetFile, String dirPath) throws IOException {
		/*
		  디렉토리 중복 여부 확인 필요
		 */	
		int optionNum = 1;
		byte[] buf = new byte[1048576];
		BufferedInputStream readFile = new BufferedInputStream(new FileInputStream(targetFile));
		readFile.read(buf, 0, 1048576);

		Files sendingFile = new Files(fileName,"secretgarden"+userId+dirPath+"/", optionNum, buf, userId);
		objOutput = new ObjectOutputStream(writer);
		objOutput.writeObject(sendingFile);
		objOutput.flush();
		System.out.println("일단은 보냇음...");
		
		objOutput.close();
		readFile.close();
		return 0;
	}
	

	@Override
	public byte[] download(String sourcePath, String userId, String fileName) throws IOException {
		//BufferedOutputStream bos = null;
		int optionNum = 2;
		
		Files request = new Files(fileName, sourcePath, optionNum, userId);
		
		//request
		objOutput = new ObjectOutputStream(writer);
		objOutput.writeObject(request);
		objOutput.flush();
		
		//download
		try {
			objInput = new ObjectInputStream(reader);
			recievFile = (Files)objInput.readObject();
		} catch (IOException e) {
			System.out.println("object 수신 실");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		objInput.close();
		objOutput.close();
		
		return recievFile.getFileBuf();
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
	
