package com.sg.main;

import java.io.IOException;

import com.sg.cloud.Hybrid;
import com.sg.controller.Connector;
import com.sg.controller.FileMgr;
import com.sg.controller.PacketMgr;
import com.sg.gui.MainFrame;


public class ClientLauncher{

	private static Connector connector;
	private static MainFrame frame;
	private static PacketMgr pkMgr;
	private static FileMgr fileMgr;
	private static Hybrid hybrid;
	
	public static void main(String[] args) {

		// file을 load 및 save
		fileMgr = new FileMgr();
		// 받은 패킷을 처리 하는 함수
		pkMgr = new PacketMgr();
		// 통신담당 모듈로 소켓을 연결하고 프로토콜에 맞게 통신을 지원
		connector = new Connector();
		// cloud Controller module 실행
		hybrid = new Hybrid();
		
		// 정상 연결시 frame을 띄움
		if(connector.getSocket()!=null) {
			frame = new MainFrame(Constants.frameW,Constants.frameH);
		}
	}
	
	// 프로그램 종료시 리소스 반환
	public static void exit()
	{
		try {
			connector.getDis().close();
			connector.getDos().close();
			connector.getSocket().close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	public static Connector getConnector() {
		return connector;
	}

	public static void setConnector(Connector connector) {
		ClientLauncher.connector = connector;
	}

	public static MainFrame getFrame() {
		return frame;
	}

	public static void setFrame(MainFrame frame) {
		ClientLauncher.frame = frame;
	}

	public static PacketMgr getPkMgr() {
		return pkMgr;
	}

	public static void setPkMgr(PacketMgr pkMgr) {
		ClientLauncher.pkMgr = pkMgr;
	}

	public static FileMgr getFileMgr() {
		return fileMgr;
	}

	public static void setFileMgr(FileMgr fileMgr) {
		ClientLauncher.fileMgr = fileMgr;
	}

	public static Hybrid getHybrid() {
		return hybrid;
	}

	public static void setHybrid(Hybrid hybrid) {
		ClientLauncher.hybrid = hybrid;
	}
	
	
	
}
