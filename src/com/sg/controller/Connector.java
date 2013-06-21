package com.sg.controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URLDecoder;
import java.net.UnknownHostException;

import com.sg.main.Constants;

public class Connector implements Runnable{
	Socket socket;
	DataInputStream dis;
	DataOutputStream dos;
	Thread client;
	boolean runable;
	PacketMgr pkMgr;

	public Connector(){

		try {
			socket = new Socket(Constants.serverIP, Constants.serverPort);
			dis = new DataInputStream(socket.getInputStream());
			dos = new DataOutputStream(socket.getOutputStream());
			
			pkMgr = new PacketMgr();
			runable = true;
			client = new Thread(this);
			client.start();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public DataInputStream getDis() {
		return dis;
	}

	public DataOutputStream getDos() {
		return dos;
	}
	
	public void disconnect(){
		runable = false;
		try {
			dis.close();
			dos.close();
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		String pk;
		
		while(runable) {
			try {
				//msg = dis.readUTF();
				byte[] temp = new byte[100];
				dis.read(temp);
				pk = new String(temp); 
				pk = pk.trim();
				
				if(!(pk.equals("")))
					System.out.println("receive from : " + pk);
				
				pkMgr.managePacket(pk);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
