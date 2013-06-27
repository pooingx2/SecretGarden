package com.sg.controller;

import java.util.StringTokenizer;

import javax.swing.JOptionPane;

import com.sg.main.ClientLauncher;
import com.sg.main.Constants;

public class PacketMgr {
	private StringTokenizer tokenizer;
	private String token[];
	
	public PacketMgr(){
		token = new String[100];
	}
	public void managePacket(String pk){
		tokenizer = new StringTokenizer(pk,"\t");
		int i =0;

		while(tokenizer.hasMoreTokens()) {
			token[i] = tokenizer.nextToken();
			System.out.println("teken : "+token[i]);
			i++;
		}

		if(token[0].equals(Constants.PacketType.LoginFailure.getType())){
			JOptionPane.showMessageDialog(null, token[1]);
		}
		
		if(token[0].equals(Constants.PacketType.LoginSuccess.getType())){
			JOptionPane.showMessageDialog(null, token[1]+" 님 환영합니다.");
			ClientLauncher.getFrame().changePanel(ClientLauncher.getFrame().getConnectionPanel());
		}
		
		if(token[0].equals(Constants.PacketType.LogoutResponse.getType())){
			ClientLauncher.getFrame().changePanel(ClientLauncher.getFrame().getLoginPanel());
		}
	}
}
