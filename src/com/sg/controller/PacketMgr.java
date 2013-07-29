package com.sg.controller;

import java.io.FileNotFoundException;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JOptionPane;

import com.sg.main.ClientLauncher;
import com.sg.main.Constants;
import com.sg.model.FileInfo;

public class PacketMgr {
	private StringTokenizer tokenizer;
	private String token[];
	private FileMgr fileMgr;
	
	public PacketMgr(){
		fileMgr=ClientLauncher.getFileMgr();
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
			ClientLauncher.getFrame().getLoginPanel().getSigupFrame().initialize();
			ClientLauncher.getFrame().getLoginPanel().getSigupFrame().dispose();
		}
		
		// 디렉토리 리스트 요청 응답에 대한 패킷을 처리(디렉토리 정보들을 테이블에 저장)
		if(type==Constants.PacketType.DirectoryListResponse.getType()) {
			
			StringTokenizer tokenizer2;
			String token2[];
			token2= new String[100];
			
			// Directory List를 갱신하기 위해 초기화 한다.
			ClientLauncher.getFrame().getDirectoryListPanel().initTable();
			
			// 수신한 데이터를 Table에 추가한다. (index,dirName \t index,dirName ...)
			// 한 row를 vector형태로 취함
			for(int j=0 ; j<i ; j++){
				Vector<String> row = new Vector<String>();
				tokenizer2 = new StringTokenizer(token[j],",");
				int k = 0;
				
				while(tokenizer2.hasMoreTokens()) {
					token2[k] = tokenizer2.nextToken();
					row.add(token2[k]);
					k++;
				}
				//table에 추가하기 위해 Vector(row)정보를 보냄
				ClientLauncher.getFrame().getDirectoryListPanel().addRow(row);
			}
		}

		// 디렉토리 생성에 따른 키 데이터를 수신하여 파일로 변환하는 과정
		if(type==Constants.PacketType.DirectoryCreateResponse.getType()) {
			fileMgr.saveFile(token[0]);
		}
		
		// 디렉토리 액세스 리스판스(Key file 인증에 따른 디렉토리 접속 키 부여) 
		if(type==Constants.PacketType.DirectoryAccessResponse.getType()) {

			StringTokenizer tokenizer2;
			String token2[];
			token2= new String[100];
			Vector<FileInfo> fileInfoList;
			FileInfo fileInfo;
			
			ClientLauncher.getFileMgr().initFileInfo();
			fileInfoList = ClientLauncher.getFileMgr().getFileInfoList();
			// 수신한 데이터를 FileInfo List에 저장한다. (index,dirName \t index,dirName ...)
			// (type,name,parent,depth,index \t type,name,parent,depth,index ... )
			for(int j=0 ; j<i ; j++){
				fileInfo = new FileInfo();
				tokenizer2 = new StringTokenizer(token[j],",");
				int k = 0;
				while(tokenizer2.hasMoreTokens()) {
					token2[k] = tokenizer2.nextToken();
					
					switch(k){
					case 0 : 
						fileInfo.setType(token2[k]); 
						break;
					case 1 : 
						fileInfo.setName(token2[k]); 
						break;
					case 2 : 
						fileInfo.setParent(token2[k]); 
						break;
					case 3 : 
						fileInfo.setDepth(token2[k]); 
						if(ClientLauncher.getFileMgr().getMaxDepth() < Integer.parseInt(fileInfo.getDepth())) 
							ClientLauncher.getFileMgr().setMaxDepth(Integer.parseInt(fileInfo.getDepth()));
						break;
					case 4 : 
						fileInfo.setIndex(token2[k]); 
//						ClientLauncher.getFileMgr().setRootDirID(token2[k]);
						break;
					default : 
						break;
					}
					k++;
				}
				fileInfoList.add(fileInfo);
			}
			ClientLauncher.getFrame().changePanel(ClientLauncher.getFrame().getFileListPanel());
		}
		
		// 폴더 생성
		if(type==Constants.PacketType.FolderCreateResponse.getType()) 
		{	
			StringTokenizer tokenizer2;
			String token2[];
			token2= new String[100];
			Vector<FileInfo> fileInfoList;
			FileInfo fileInfo;
			
			ClientLauncher.getFileMgr().initFileInfo();
			fileInfoList = ClientLauncher.getFileMgr().getFileInfoList();
			// 수신한 데이터를 FileInfo List에 저장한다. (index,dirName \t index,dirName ...)
			// (type,name,parent,depth,index \t type,name,parent,depth,index ... )
			for(int j=0 ; j<i ; j++){
				fileInfo = new FileInfo();
				tokenizer2 = new StringTokenizer(token[j],",");
				int k = 0;
				while(tokenizer2.hasMoreTokens()) {
					token2[k] = tokenizer2.nextToken();
					
					switch(k){
					case 0 : 
						fileInfo.setType(token2[k]); 
						break;
					case 1 : 
						fileInfo.setName(token2[k]); 
						break;
					case 2 : 
						fileInfo.setParent(token2[k]); 
						break;
					case 3 : 
						fileInfo.setDepth(token2[k]); 
						if(ClientLauncher.getFileMgr().getMaxDepth() < Integer.parseInt(fileInfo.getDepth())) 
							ClientLauncher.getFileMgr().setMaxDepth(Integer.parseInt(fileInfo.getDepth()));
						break;
					case 4 : 
						fileInfo.setIndex(token2[k]); 
						break;
					default : 
						break;
					}
					k++;
				}
				fileInfoList.add(fileInfo);
			}
			ClientLauncher.getFrame().getFileListPanel().initialize();
		}
		
		
		// 폴더 및 디렉토리 셋팅(공유와 관련된)패킷 수신
		if(type==Constants.PacketType.SettingResponse.getType())
		{
				
					
		}
		
		
		// 프로그램 종료
		if(type==Constants.PacketType.PROGRAM_EXIT_RESPONSE.getType())
		{
		
			
		}
		
		
		
	}
}
