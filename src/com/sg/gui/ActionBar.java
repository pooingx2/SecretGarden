package com.sg.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import com.sg.main.ClientLauncher;
import com.sg.main.Constants;

public class ActionBar extends JMenuBar{
	
	// Attribute
	private int width;
	private int height;
	/* status
	0 : default		1 : connection		2 : DirectoryList		3 : fileList
	 */
	private int status;
	
	// Components
	private JLabel bgImg;
	private ActionHandler handler;
	private JButton homeBtn;
	private JButton backBtn;
	private JButton forwardBtn;
	private JButton logoutBtn;
	
	public ActionBar(int w, int h) {
		super();
		this.width = w;
		this.height = h;
		this.setLayout(null);
		
		// 배경이미지 등록
		bgImg = new JLabel(new ImageIcon(Constants.BackgroudPath.barBG.getPath()));
		bgImg.setBounds(0,0,width,height);
		
		// 이벤트 핸들러 등록
		handler = new ActionHandler();
		
		// home 버튼
		homeBtn = new JButton(new ImageIcon(Constants.ButtonPath.homeBtn1.getPath()));
		homeBtn.setRolloverIcon(new ImageIcon(Constants.ButtonPath.homeBtn2.getPath()));
		homeBtn.setBounds(20,5,40,40);
		homeBtn.addActionListener(handler);
		
		// 뒤로 버튼
		backBtn = new JButton(new ImageIcon(Constants.ButtonPath.backBtn1.getPath()));
		backBtn.setRolloverIcon(new ImageIcon(Constants.ButtonPath.backBtn2.getPath()));
		backBtn.setBounds(70,5,40,40);
		backBtn.addActionListener(handler);
		
		// 앞으로 버튼
		forwardBtn = new JButton(new ImageIcon(Constants.ButtonPath.forwardBtn1.getPath()));
		forwardBtn.setRolloverIcon(new ImageIcon(Constants.ButtonPath.forwardBtn2.getPath()));
		forwardBtn.setBounds(120,5,40,40);
		forwardBtn.addActionListener(handler);
		
		// 로그아웃 버튼
		logoutBtn = new JButton(new ImageIcon(Constants.ButtonPath.logoutBtn1.getPath()));
		logoutBtn.setRolloverIcon(new ImageIcon(Constants.ButtonPath.logoutBtn2.getPath()));
		logoutBtn.setBounds(730,5,40,40);
		logoutBtn.addActionListener(handler);
		
		this.add(homeBtn);
		this.add(backBtn);
		this.add(forwardBtn);
		this.add(logoutBtn);
		this.add(bgImg);
	}
	
	public void initialize() { 
	}
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	// 이벤트 핸들러 등록
	private class ActionHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			
			if(event.getSource()==homeBtn){
				ClientLauncher.getFrame().changePanel(ClientLauncher.getFrame().getConnectionPanel());
			}	
			
			if(event.getSource()==backBtn){
				switch(status){
				case 1 : ClientLauncher.getFrame().changePanel(ClientLauncher.getFrame().getLoginPanel());
					break;
				case 2 : ClientLauncher.getFrame().changePanel(ClientLauncher.getFrame().getConnectionPanel());
					break;
				case 3 : ClientLauncher.getFrame().changePanel(ClientLauncher.getFrame().getDirectoryListPanel());
					break;
				default : 
					break;
				}
			}	
			
			if(event.getSource()==forwardBtn){
				System.out.println("forwardBtn");
			}	
			
			if(event.getSource()==logoutBtn){
				String data = "logout reqeust";
				int type = Constants.PacketType.LogoutRequest.getType();
				int length = data.length();	
				
				// 로그아웃 요청 패킷 전송
				ClientLauncher.getConnector().sendPacket(type, 0, length, data);
				
			}
		}
	}

}
