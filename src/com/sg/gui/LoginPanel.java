package com.sg.gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
   String id;
	
	// Components
	private JLabel bgImg;
	private Font inputFont;
	private ActionHandler handler;
	private JLabel idLabel;
	private JLabel pwdLabel;
	private JButton loginBtn;
	private JButton signupBtn;
	private JTextField inputID;
	private JPasswordField inputPwd;
	private SignupFrame sigupFrame;
	
	// 로그인을 위한 패널
	public LoginPanel(int w, int h) {
		
		super();
		this.width = w;
		this.height = h;
		this.setLayout(null);
		
		// 배경이미지 등록
		bgImg = new JLabel(new ImageIcon(Constants.BackgroudPath.loginBG.getPath()));
		bgImg.setBounds(0,0,width,height);

		// 폰트 설정
		inputFont = Constants.Font1;
		
		// ID를 입력하기 위한 form
		idLabel = new JLabel("ID");
		idLabel.setFont(inputFont);
		idLabel.setBounds(550,250,200,30);
		
		inputID = new JTextField();
		inputID.setBounds(550,280,200,30);
		inputID.setFont(inputFont);
		
		// password를 입력하기 위한 form
		pwdLabel = new JLabel("Password");
		pwdLabel.setFont(inputFont);
		pwdLabel.setBounds(550,320,200,30);
		
		inputPwd = new JPasswordField();
		inputPwd.setBounds(550,350,200,30);
		inputPwd.setFont(inputFont);

		// 버튼 이벤트 리스너 등록
		handler = new ActionHandler();
		loginBtn = new JButton(new ImageIcon(Constants.ButtonPath.loginBtn1.getPath()));
		loginBtn.setRolloverIcon(new ImageIcon(Constants.ButtonPath.loginBtn2.getPath()));
		loginBtn.setBounds(550,400,100,40);
		loginBtn.addActionListener(handler);
		
		signupBtn = new JButton(new ImageIcon(Constants.ButtonPath.signupBtn1.getPath()));
		signupBtn.setRolloverIcon(new ImageIcon(Constants.ButtonPath.signupBtn2.getPath()));
		signupBtn.setBounds(650,400,100,40);
		signupBtn.addActionListener(handler);
		
		sigupFrame = new SignupFrame(400,500);

		this.add(idLabel);
		this.add(inputID);
		this.add(pwdLabel);
		this.add(inputPwd);
		this.add(loginBtn);
		this.add(signupBtn);
		this.add(bgImg);
			
	}

	public void initialize() { 
		inputID.setText("");
		inputPwd.setText("");
	}
	
	
	public SignupFrame getSigupFrame() {
		return sigupFrame;
	}

	public void setSigupFrame(SignupFrame sigupFrame) {
		this.sigupFrame = sigupFrame;
	}



	// 로그인 버튼과 취소 버튼을 위한 이벤트
	private class ActionHandler implements ActionListener {
		
		private String id;
		private String pwd;
		
		@Override
		public void actionPerformed(ActionEvent event) {
			id=inputID.getText();
			pwd=inputPwd.getText();
			
			setId(id);
			
			if(event.getSource()==loginBtn){

				String data = id + "\t"+pwd;
				int type = Constants.PacketType.LoginRequest.getType();
				int length = data.length();
				
				//id와 password 패킷 전송
				ClientLauncher.getHybrid().setUserId(id);
				ClientLauncher.getConnector().sendPacket(type, 0, length, data);

			}
			if(event.getSource()==signupBtn){

				getSigupFrame().setVisible(true);
				
			}
		}
	}
	
	public void setId(String id)
	{
		this.id = id;
	}
	
	public String getId()
	{
		return this.id;
	}
}

