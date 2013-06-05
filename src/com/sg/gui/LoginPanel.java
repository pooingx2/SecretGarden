package com.sg.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.sg.main.ClientLauncher;
import com.sg.main.Constants;


public class LoginPanel extends JPanel {
	// Attribute
	private int width;
	private int height;
	
	// Components
	private ActionHandler handler;
	private JLabel idLabel;
	private JLabel pwdLabel;
	private JButton loginBtn;
	private JTextField inputID;
	private JPasswordField inputPwd;
	private Font inputFont;

	private JLabel backImg;
	
	public LoginPanel(int w, int h) {
		super();
		
		this.width = w;
		this.height = h;
		handler = new ActionHandler();
		
		this.setBackground(Color.WHITE);
		this.setLayout(null);
		
		inputFont = new Font(null,Font.CENTER_BASELINE,15);
		
		idLabel = new JLabel("ID");
		idLabel.setFont(inputFont);
		idLabel.setBounds(520,220,200,30);
		
		inputID = new JTextField();
		inputID.setBounds(520,250,200,30);
		inputID.setFont(inputFont);
		
		pwdLabel = new JLabel("Password");
		pwdLabel.setFont(inputFont);
		pwdLabel.setBounds(520,300,200,30);
		
		inputPwd = new JPasswordField();
		inputPwd.setBounds(520,330,200,30);
		inputPwd.setFont(inputFont);
		
		loginBtn = new JButton();
		loginBtn.setBounds(570,400,150,50);
		loginBtn.setIcon(new ImageIcon(Constants.loginImgPath));
		loginBtn.addActionListener(handler);
		//loginBtn.setActionCommand(eToolBarButton.getClassName());

		backImg = new JLabel(new ImageIcon(Constants.backImgPath));
		backImg.setBounds(0,0,width,height);

		this.add(idLabel);
		this.add(inputID);
		this.add(pwdLabel);
		this.add(inputPwd);
		this.add(loginBtn);
		
		this.add(backImg);
			
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
				try {
					System.out.println("send");
					byte[] out = new byte[100];
					String temp = "id : "+ id + ", pwd : "+pwd;
					temp.trim();
					out = temp.getBytes();
					ClientLauncher.getConnector().getDos().write(out); 
					ClientLauncher.getConnector().getDos().flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
}

