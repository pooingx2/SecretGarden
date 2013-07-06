package com.sg.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.sg.main.ClientLauncher;
import com.sg.main.Constants;

public class SignupFrame extends JFrame  {
	
	private int width;
	private int height;
	private JLabel bgImg;
	private JTextField[] textField;
	private JPasswordField[] pwdField;
	private Font inputFont;
	private JButton btn[];
	private ActionHandler handler;
	
	public SignupFrame(int w, int h) {
		
		super();
		

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.updateComponentTreeUI(this);
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		this.width = w;
		this.height = h;
		
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		
		this.setSize(width,height);
		this.setResizable(false);
		this.setLocation(screen.width/2-this.width/2, screen.height/2-this.height/2);

		this.setLayout(null);
//		this.setVisible(true);
		
		bgImg = new JLabel(new ImageIcon(Constants.BackgroudPath.signupBG.getPath()));
		bgImg.setBounds(0,0,width,height);

		inputFont = Constants.Font1;
		
		textField = new JTextField[3];
		pwdField = new JPasswordField[2];
		
		textField[0] = new JTextField();
		textField[0].setBounds(100,105,250,30);
		
		pwdField[0] = new JPasswordField();
		pwdField[0].setBounds(100,167,250,30);
		
		pwdField[1] = new JPasswordField();
		pwdField[1].setBounds(100,230,250,30);
		
		textField[1] = new JTextField();
		textField[1].setBounds(100,295,250,30);
		
		textField[2] = new JTextField();
		textField[2].setBounds(100,357,250,30);
		
		for(int i=0;i<3;i++){
			textField[i].setFont(inputFont);
			this.add(textField[i]);
		}
		
		for(int i=0;i<2;i++){
			pwdField[i].setFont(inputFont);
			this.add(pwdField[i]);
		}
		
		handler = new ActionHandler();
		
		btn = new JButton[2];
		
		btn[0] = new JButton(new ImageIcon(Constants.ButtonPath.confirmBtn1.getPath()));
		btn[0].setRolloverIcon(new ImageIcon(Constants.ButtonPath.confirmBtn2.getPath()));
		btn[0].setBounds(200,420,80,30);
		btn[0].addActionListener(handler);
		
		btn[1] = new JButton(new ImageIcon(Constants.ButtonPath.cancelBtn1.getPath()));
		btn[1].setRolloverIcon(new ImageIcon(Constants.ButtonPath.cancelBtn2.getPath()));
		btn[1].setBounds(280,420,80,30);
		btn[1].addActionListener(handler);

		this.add(btn[0]);
		this.add(btn[1]);
		this.add(bgImg);
	}
	
	public void initForm(){
		textField[0].setText("");
		textField[1].setText("");
		textField[2].setText("");
		pwdField[0].setText("");
		pwdField[1].setText("");
	}
	
	private class ActionHandler implements ActionListener {
		private String id;
		private String pwd1;
		private String pwd2;
		private String name;
		private String email;

		@Override
		public void actionPerformed(ActionEvent event) {

			if(event.getSource()==btn[0]){
				id=textField[0].getText();
				name=textField[1].getText();
				email=textField[2].getText();
				pwd1=pwdField[0].getText();
				pwd2=pwdField[1].getText();
				
				if(id.equals("") || name.equals("") || email.equals("")
									|| pwd1.equals("") || pwd2.equals("")){
					JOptionPane.showMessageDialog(null, "Fill out the form");
				}
				else if(!(pwd1.equals(pwd2))){
					JOptionPane.showMessageDialog(null, "Password doesn't match the confirmation");
				}
				else{
					String data = id + "\t"+pwd1+"\t"+name+"\t"+email;
					int type = Constants.PacketType.SignupRequest.getType();
					int length = data.length();
					
					//ClientLauncher.getConnector().sendHeader(type, length);
					ClientLauncher.getConnector().sendPacket(type, 0, length, data);
					//ClientLauncher.getConnector().sendData(data);
				}
			}
			
			if(event.getSource()==btn[1]){
				dispose();
				initForm();
			}
		}
	}
	

}
