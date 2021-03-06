package com.sg.controller;

import java.io.FileNotFoundException;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JOptionPane;

import com.sg.main.ClientLauncher;
import com.sg.main.Constants;
import com.sg.model.FileInfo;
import com.sg.model.MetaData;
import com.sg.task.Task;

public class PacketMgr {
	private StringTokenizer tokenizer;
	private String token[];
	private FileMgr fileMgr;

	public PacketMgr() {
		fileMgr = ClientLauncher.getFileMgr();
	}

	// 패킷 정보를 tocken으로 쪼개고 각 type에 맞는 기능을 처리
	public void managePacket(int type, int desc, int length, String data)
			throws FileNotFoundException {

		// packet data를 쪼갬

		tokenizer = new StringTokenizer(data,"\t");
		token = new String[length];
		int i =0;

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
		else if(type==Constants.PacketType.LoginResponse.getType()){
			JOptionPane.showMessageDialog(null, "Welcome");

			ClientLauncher.getUser().setId(token[0]);
			ClientLauncher.getUser().setName(token[1]);
			ClientLauncher.getUser().setEmail(token[2]);

			ClientLauncher.getFileMgr().setDownloadPath(ClientLauncher.getFileMgr().getDownloadPath()+
					ClientLauncher.getFileMgr().getSlash()+token[0]);

			ClientLauncher.getFrame().changePanel(ClientLauncher.getFrame().getConnectionPanel());
		}

		// 로그아웃 응답에 대한 패킷을 처리 (현재 패널 -> LoginPanel)
		else if(type==Constants.PacketType.LogoutResponse.getType()){
			ClientLauncher.getFrame().changePanel(ClientLauncher.getFrame().getLoginPanel());
		}

		// 회원가입 응답에 대한 패킷을 처리 (회원가입 프레임을 초기화 하고 없앰)
		else if(type==Constants.PacketType.SignupResponse.getType()){
			JOptionPane.showMessageDialog(null, "Thank you");
			ClientLauncher.getFrame().getLoginPanel().getSigupFrame().initialize();
			ClientLauncher.getFrame().getLoginPanel().getSigupFrame().dispose();
		}

		// 디렉토리 리스트 요청 응답에 대한 패킷을 처리(디렉토리 정보들을 테이블에 저장)
		else if(type==Constants.PacketType.DirectoryListResponse.getType()) {

			StringTokenizer tokenizer2;
			String token2[];
			token2= new String[100];

			// Directory List를 갱신하기 위해 초기화 한다.
			ClientLauncher.getFrame().getDirectoryListPanel().initTable();

			// 수신한 데이터를 Table에 추가한다. (index,dirName,master,cloudRate,size)
			// 한 row를 vector형태로 취함
			for(int j=0 ; j<i ; j++){
				Vector<String> row = new Vector<String>();
				tokenizer2 = new StringTokenizer(token[j],",");
				int k = 0;

				while(tokenizer2.hasMoreTokens()) {
					token2[k] = tokenizer2.nextToken();
					
					if(k == 3) {
						token2[k] = token2[k] + " : " + (10 - Integer.parseInt(token2[k]));
					}
					row.add(token2[k]);
					k++;
				}

				ClientLauncher.getFrame().getDirectoryListPanel().addRow(row);
			}
		}

		// 디렉토리 생성에 따른 키 데이터를 수신하여 파일로 변환하는 과정
		else if (type == Constants.PacketType.DirectoryCreateResponse.getType()) {
			String test = fileMgr.saveKeyFile(token[0]);
			while(test==null){
				JOptionPane.showMessageDialog(null, "You have to save the key file");
				test = fileMgr.saveKeyFile(token[0]);
			}
		}

		// 디렉토리 액세스 리스판스(Key file 인증에 따른 디렉토리 접속 키 부여)
		else if (type == Constants.PacketType.DirectoryAccessResponse.getType()) {

			ClientLauncher.getFileMgr().init();

			// 수신한 데이터를 FileInfo List에 저장한다. (index,dirName \t index,dirName...)
			// (type,name,parent,depth,index \t type,name,parent,depth,index... )
			for (int j = 0; j < i; j++) {
				ClientLauncher.getFileMgr().addFileInfo(token[j]);
			}
			ClientLauncher.getFrame().changePanel(ClientLauncher.getFrame().getFileListPanel());
		}

		// 폴더 생성
		else if (type == Constants.PacketType.FolderCreateResponse.getType()) {
			ClientLauncher.getFileMgr().init();

			for (int j = 0; j < i; j++) {
				ClientLauncher.getFileMgr().addFileInfo(token[j]);
			}

			ClientLauncher.getFrame().getFileListPanel().initialize();
		}

		// 메타데이터 업로드
		else if (type == Constants.PacketType.FileUploadResponse.getType()) {
			ClientLauncher.getFileMgr().init();

			for (int j = 0; j < i; j++) {
				ClientLauncher.getFileMgr().addFileInfo(token[j]);
			}

			ClientLauncher.getFrame().getFileListPanel().initialize();
		}

		// 메타데이터 다운로드
		else if (type == Constants.PacketType.FileDownloadResponse.getType()) {
			StringTokenizer tokenizer2;
			String token2[];
			token2= new String[100];
			
			MetaData metaData = new MetaData();
			
			tokenizer2 = new StringTokenizer(token[0],"\n");
			int k = 0;

			while(tokenizer2.hasMoreTokens()) {
				token2[k] = tokenizer2.nextToken();
				k++;
				
				switch(k) {
					case 1 : metaData.setCloudTable(token2[k-1]); break;
					case 2 : metaData.setFilePath(token2[k-1]); break;
					case 3 : metaData.setFileName(token2[k-1]); break;
					case 4 : metaData.setFileType(token2[k-1]); break;
					case 5 : metaData.setFile_size(token2[k-1]); break;
					case 6 : metaData.setStream_size(token2[k-1]); break;
					case 7 : metaData.setLastStream_size(token2[k-1]); break;
					case 8 : metaData.setStream_count(token2[k-1]);	break;
					case 9 : metaData.setHash(token2[k-1]);	break;
					default : break;
				}
			}
			
			int count=0;
			Vector<Task> taskList = ClientLauncher.getTaskMgr().getTaskList();
			for (Task task : taskList){
				if(task.getType().equals("Download")) {
					if(task.getMetaData() == null) {
						ClientLauncher.getTaskMgr().getTaskList().get(count).setMetaData(metaData);
						break;
					}
					System.out.println("\t\t\t\t\t"+task.getMetaData());
				}
				count++;
			}

//			ClientLauncher.getStreamMgr().setMetaData(metaData);
			ClientLauncher.getTaskMgr().nextStart();
		}

		// 존재하는 아이디인지 확인
		else if (type == Constants.PacketType.IdCheckResponse.getType()) {
			String userId = token[0];
			ClientLauncher.getFrame().getDirectoryListPanel().getDirMngPanel().getShareFrame().getTextArea().append(userId+"\n");
		}
		
		// 공유가 완료 되었는지 확인
		else if (type == Constants.PacketType.ShareResponse.getType()) {
			ClientLauncher.getFrame().getDirectoryListPanel().getDirMngPanel().getShareFrame().dispose();
			ClientLauncher.getFrame().getDirectoryListPanel().getDirMngPanel().getShareFrame().initialize();
			JOptionPane.showMessageDialog(null, token[0]);
		}
		
		// 공유 목록 리스트 확인
		else if (type == Constants.PacketType.ShareListResponse.getType()) {
			
			StringTokenizer tokenizer2;
			String token2[];
			token2= new String[100];

			// share List를 갱신하기 위해 table을 초기화 한다.
			ClientLauncher.getFrame().getSharingPanel().initTable();

			// 수신한 데이터를 Table에 추가한다. (userId, status , ....)
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

				ClientLauncher.getFrame().getSharingPanel().addRow(row);
			}
			ClientLauncher.getFrame().getSharingPanel().initialize();
			ClientLauncher.getFrame().changePanel(ClientLauncher.getFrame().getSharingPanel());
		}

		
		// 프로그램 종료
		if (type == Constants.PacketType.PROGRAM_EXIT_RESPONSE.getType()) {

		}

	}
}



