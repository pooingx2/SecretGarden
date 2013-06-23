package com.sg.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.sg.controller.Connector;
import com.sg.main.ClientLauncher;
import com.sg.main.Constants;


public class LoginPanel extends JPanel {
	// Attribute
	private int width;
	private int height;
	
	// Components
	private JLabel bgImg;
	private Font inputFont;
	private ActionHandler handler;
	private JLabel idLabel;
	private JLabel pwdLabel;
	private JButton loginBtn;
	private JTextField inputID;
	private JPasswordField inputPwd;
	
	public LoginPanel(int w, int h) {
		
		super();
		this.width = w;
		this.height = h;
		this.setLayout(null);
		
		bgImg = new JLabel(new ImageIcon(Constants.BackgroudPath.loginBG.getPath()));
		bgImg.setBounds(0,0,width,height);

		inputFont = Constants.Font1;
		
		idLabel = new JLabel("ID");
		idLabel.setFont(inputFont);
		idLabel.setBounds(520,250,200,30);
		
		inputID = new JTextField();
		inputID.setBounds(520,280,200,30);
		inputID.setFont(inputFont);
		
		pwdLabel = new JLabel("Password");
		pwdLabel.setFont(inputFont);
		pwdLabel.setBounds(520,320,200,30);
		
		inputPwd = new JPasswordField();
		inputPwd.setBounds(520,350,200,30);
		inputPwd.setFont(inputFont);

		handler = new ActionHandler();
		loginBtn = new JButton(new ImageIcon(Constants.ButtonPath.loginBtn1.getPath()));
		loginBtn.setRolloverIcon(new ImageIcon(Constants.ButtonPath.loginBtn2.getPath()));
		loginBtn.setBounds(630,400,100,45);
		loginBtn.addActionListener(handler);

		this.add(idLabel);
		this.add(inputID);
		this.add(pwdLabel);
		this.add(inputPwd);
		this.add(loginBtn);
		this.add(bgImg);
			
	}

	public void initialize() { }
	
	private class ActionHandler implements ActionListener {
		
		private String id;
		private String pwd;
		
		@Override
		public void actionPerformed(ActionEvent event) {
			id=inputID.getText();
			pwd=inputPwd.getText();
			
			if(event.getSource()==loginBtn){

				String data = id + "\t"+pwd;
				int type = Constants.PacketType.LoginRequest.getType();
				int length = data.length();
				ClientLauncher.getConnector().sendHeader(type, length);
				ClientLauncher.getConnector().sendData(data);
				ClientLauncher.getConnector().receiveHeader();
				ClientLauncher.getConnector().receiveData();
				inputID.setText("");
				inputPwd.setText("");

			}
		}

	}
}

