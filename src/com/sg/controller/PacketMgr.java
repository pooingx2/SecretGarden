package com.sg.controller;

import java.util.StringTokenizer;

import javax.swing.JOptionPane;

import com.sg.main.ClientLauncher;
import com.sg.main.Constants;
import com.sg.gui.*;

public class PacketMgr {
	private StringTokenizer tokenizer;
	private String token[];
	
	public PacketMgr(){
		
	}
	public void managePacket(int type, int desc ,int length, String data){
		tokenizer = new StringTokenizer(data,"\t");
		token = new String[length];
		int i =0;

		System.out.println("type : " + type);
		System.out.println("desc : " + desc);
		System.out.println("length : " + length);
		System.out.println("data : " + data);
		
		while(tokenizer.hasMoreTokens()) {
			token[i] = tokenizer.nextToken();
			i++;
		}
		
			
		if(type==Constants.PacketType.Error.getType()){
			JOptionPane.showMessageDialog(null, token[0]);
		}
		
		
		if(type==Constants.PacketType.LoginResponse.getType()){
			JOptionPane.showMessageDialog(null, "Welcome");
			ClientLauncher.getFrame().changePanel(ClientLauncher.getFrame().getConnectionPanel());
		}
		
		
		if(type==Constants.PacketType.LogoutResponse.getType()){
			ClientLauncher.getFrame().changePanel(ClientLauncher.getFrame().getLoginPanel());
		}
		
		
		if(type==Constants.PacketType.SignupResponse.getType()){
			JOptionPane.showMessageDialog(null, "Thank you");
			ClientLauncher.getFrame().getLoginPanel().getSigupFrame().initForm();
			ClientLauncher.getFrame().getLoginPanel().getSigupFrame().dispose();
		}
		
		
		if(type==Constants.PacketType.DirectoryListResponse.getType())
		{
			for(int tokenNum=0;tokenNum<i;tokenNum = tokenNum + 4){
				DirectoryListPanel.addList(token[tokenNum],token[tokenNum+1],token[tokenNum+2],token[tokenNum+3]);
			}
		}
	}
}
