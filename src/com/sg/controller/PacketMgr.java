package com.sg.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
	
	// 패킷 정보를 tocken으로 쪼개고 각 type에 맞는 기능을 처리
	public void managePacket(int type, int desc ,int length, String data) throws FileNotFoundException{
		
		// packet data를 쪼갬
		tokenizer = new StringTokenizer(data,"\t");
		token = new String[length];
		int i =0;

		System.out.println("type : " + type);
		System.out.println("desc : " + desc);
		System.out.println("length : " + length);
		System.out.println("data : " + data);
		
		// token에 저장
		while(tokenizer.hasMoreTokens()) {
			token[i] = tokenizer.nextToken();
			i++;
		}
		
		// 에러 패킷을 처리
		if(type==Constants.PacketType.Error.getType()){
			JOptionPane.showMessageDialog(null, token[0]);
		}
		
		// 로그인 요청 응답에 대한 패킷을 처리 (LoginPanel -> ConnectionPanel)
		if(type==Constants.PacketType.LoginResponse.getType()){
			JOptionPane.showMessageDialog(null, "Welcome");
			ClientLauncher.getFrame().changePanel(ClientLauncher.getFrame().getConnectionPanel());
		}
		
		// 로그아웃 응답에 대한 패킷을 처리 (현재 패널 -> LoginPanel)
		if(type==Constants.PacketType.LogoutResponse.getType()){
			ClientLauncher.getFrame().changePanel(ClientLauncher.getFrame().getLoginPanel());
		}
		
		// 회원가입 응답에 대한 패킷을 처리 (회원가입 프레임을 초기화 하고 없앰)
		if(type==Constants.PacketType.SignupResponse.getType()){
			JOptionPane.showMessageDialog(null, "Thank you");
			ClientLauncher.getFrame().getLoginPanel().getSigupFrame().initForm();
			ClientLauncher.getFrame().getLoginPanel().getSigupFrame().dispose();
		}
		
		// 디렉토리 리스트 요청 응답에 대한 패킷을 처리(디렉토리 정보들을 테이블에 저장)
		if(type==Constants.PacketType.DirectoryListResponse.getType()) {
			
			// Directory List를 갱신하기 위해 초기화 한다.
			ClientLauncher.getFrame().getDirectoryListPanel().initTable();
			
			// 수신한 데이터를 Table에 추가한다. (index \t dirName \t index \t dirName ...)
			for(int j=0 ; j<i ; j =j+2){
				Vector<String> row = new Vector<String>();
				row.add(token[j]);
				row.add(token[j+1]);
				
				// table에 추가하기 위해 Vector(row)정보를 보냄
				ClientLauncher.getFrame().getDirectoryListPanel().addRow(row);
			}
		}

		// 디렉토리 생성에 따른 키 데이터를 수신하여 파일로 변환하는 과정
		if(type==Constants.PacketType.DirectoryCreateResponse.getType())
		{
			// 키 파일 생성
			// 어디다 저장할지 선택하면 좋을거같음...
			FileOutputStream combinedFO = new FileOutputStream("/home/keyFile.sg");	//통합될 파일
		
			byte out[];
			//data.trim();
			out = token[0].getBytes();

			
			try {
				combinedFO.write(out);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// 디렉토리 액세스 리스판스(Key file 인증) 
		if(type==Constants.PacketType.DirectoryAccessResponse.getType())
		{
			// 보여주기 위한 정보들 하부 폴더들 Name, parent, depth, root
					
		}
		
		// 폴더 생성
		
		// 파일 업로드
		
		// 파일 다운로드
		
		if(type==Constants.PacketType.PROGRAM_EXIT_RESPONSE.getType()){
		
			
		}
		
		
		
	}
}
