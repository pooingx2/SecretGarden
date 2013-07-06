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

import com.sg.main.ClientLauncher;
import com.sg.main.Constants;

public class ActionBar extends JMenuBar{
	
	// Attribute
	private int width;
	private int height;
	
	// Components
	private JLabel bgImg;
	private ActionHandler handler;
	private JButton logoutBtn;
	
	public ActionBar(int w, int h) {
		super();
		this.width = w;
		this.height = h;
		this.setLayout(null);
		
		bgImg = new JLabel(new ImageIcon(Constants.BackgroudPath.barBG.getPath()));
		bgImg.setBounds(0,0,width,height);
		
		handler = new ActionHandler();
		logoutBtn = new JButton(new ImageIcon(Constants.ButtonPath.logoutBtn1.getPath()));
		logoutBtn.setRolloverIcon(new ImageIcon(Constants.ButtonPath.logoutBtn2.getPath()));
		logoutBtn.setBounds(730,5,40,40);
		logoutBtn.addActionListener(handler);
		
		this.add(logoutBtn);
		this.add(bgImg);
	}
	
	

	private class ActionHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			
			if(event.getSource()==logoutBtn){
				String data = "logout reqeust";
				int type = Constants.PacketType.LogoutRequest.getType();
				int length = data.length();	
				
				ClientLauncher.getConnector().sendPacket(type, 0, length, data);
				
			}	
		}
	}

}
