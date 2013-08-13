package main;

import hadoop.fileManager;

import java.io.IOException;

import controller.Connector;
import controller.PacketMgr;


public class ClientLauncher{

	private static Connector connector;
	private static PacketMgr pkMgr;
	private static fileManager fileMgr;
	
	public static void main(String[] args) {
		
		// 통신담당 모듈로 소켓을 연결하고 프로토콜에 맞게 통신을 지원
		fileMgr   = new fileManager();
		pkMgr     = new PacketMgr();
		connector = new Connector();
		
		String data = "HDFS Serv Binding Request";
		int type = 103;
		connector.sendPacket(type, 0, data.length(), data);
	
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

	public static PacketMgr getPkMgr() {
		return pkMgr;
	}

	public static void setPkMgr(PacketMgr pkMgr) {
		ClientLauncher.pkMgr = pkMgr;
	}

	public static void setConnector(Connector connector) {
		ClientLauncher.connector = connector;
	}

	public static Connector getConnector() {
		return connector;
	}
	
	public static void setfileManager(fileManager fileMgr)
	{
		ClientLauncher.fileMgr = fileMgr;
	}
	
	public static fileManager getfileManager()
	{
		return fileMgr;
	}
	
	
	
}
