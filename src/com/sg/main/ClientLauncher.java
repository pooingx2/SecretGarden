package com.sg.main;

import java.io.IOException;

import com.sg.controller.Connector;
import com.sg.gui.MainFrame;


public class ClientLauncher{

	private static Connector connector;
	private static MainFrame frame;
	
	public static void main(String[] args) {
		//connector = new Connector();
		//if(connector.getSocket()!=null) {
			frame = new MainFrame();
			frame.initialize();
		//}
	}
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
	
}
