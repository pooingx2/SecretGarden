package com.sg.controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.sg.main.Constants;

public class Connector implements Runnable{
	Socket socket;
	DataInputStream dis;
	DataOutputStream dos;

	public Connector(){

		try {
			socket = new Socket(Constants.serverIP, Constants.serverPort);
			dis = new DataInputStream(socket.getInputStream());
			dos = new DataOutputStream(socket.getOutputStream());
			
			Thread client;
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

	@Override
	public void run() {

		while(true) {
			 
			/*
			byte[] out = new byte[100];
			String s = "someStr"; 
			out = s.getBytes();
			dos.write(out); 
			dos.flush();
			*/
			String input;
			
			try {
				byte[] temp = new byte[100]; 
				dis.read(temp);
				input = new String(temp); 
				input = input.trim();
				
				if(!(input.equals("")))
					System.out.println(input);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}	
}
