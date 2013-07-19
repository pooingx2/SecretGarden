package com.sg.controller;

import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JOptionPane;

import com.sg.main.ClientLauncher;
import com.sg.main.Constants;

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
		
		
		if(type==Constants.PacketType.DirectoryListResponse.getType()) {
			
			// Directory List를 갱신하기 위해 초기화 한다.
			ClientLauncher.getFrame().getDirectoryListPanel().initTable();
			
			// 수신한 데이터를 Table에 추가한다. (index \t dirName \t index \t dirName ...)
			for(int j=0 ; j<i ; j =j+2){
				Vector<String> row = new Vector<String>();
				row.add(token[j]);
				row.add(token[j+1]);
				ClientLauncher.getFrame().getDirectoryListPanel().addRow(row);
			}
		}
		
		if(type==Constants.PacketType.PROGRAM_EXIT_RESPONSE.getType()){
		
			
		}
		
		
	}
}
