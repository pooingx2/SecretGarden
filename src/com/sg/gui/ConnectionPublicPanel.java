package com.sg.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;

import javax.sound.sampled.Line;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import com.sg.main.ClientLauncher;
import com.sg.main.Constants;


public class ConnectionPublicPanel extends JPanel {
	// Attribute
	private int width;
	private int height;
	private boolean connection;
	private String id;
	private String pwd;

	// Components
	private Font inputFont;
	private JLabel bgImg[];
	private JLabel label[];
	private JTextField textField;
	private JPasswordField pwdField;
	private JButton btn[];
	private ActionHandler handler;

	public ConnectionPublicPanel(int w, int h) {

		super();
		this.width = w;
		this.height = h;
		this.connection = false;
		this.setLayout(null);
		this.setBackground(Constants.backColor);
		this.setBorder(new LineBorder(Color.LIGHT_GRAY));

		bgImg = new JLabel[2];
		bgImg[0] = new JLabel(new ImageIcon(Constants.BackgroudPath.publicBG1.getPath()));
		bgImg[0].setBounds(0,0,width,height);
		bgImg[1] = new JLabel(new ImageIcon(Constants.BackgroudPath.publicBG2.getPath()));
		bgImg[1].setBounds(0,0,width,height);

		inputFont = Constants.Font1;

		label = new JLabel[3];
		textField = new JTextField();
		pwdField = new JPasswordField();
		btn = new JButton[3];

		handler = new ActionHandler();

		label[0] = new JLabel(new ImageIcon(Constants.IconPath.publicIcon.getPath()));
		label[0].setBounds(5,5,230,55);

		label[1] = new JLabel("AWS S3 ID");
		label[1].setBounds(20,80,200,30);
		label[1].setFont(inputFont);

		textField = new JTextField();
		textField.setBounds(20,110,200,30);
		textField.setFont(inputFont);

		label[2] = new JLabel("AWS S3 Secret key");
		label[2].setBounds(20,150,200,30);
		label[2].setFont(inputFont);

		pwdField = new JPasswordField();
		pwdField.setBounds(20,180,200,30);
		pwdField.setFont(inputFont);

		handler = new ActionHandler();

		btn[0] = new JButton(new ImageIcon(Constants.ButtonPath.publicBtn1.getPath()));
		btn[0].setRolloverIcon(new ImageIcon(Constants.ButtonPath.publicBtn2.getPath()));
		btn[0].setBounds(0,30,250,150);
		btn[0].addActionListener(handler);
		
		btn[1] = new JButton(new ImageIcon(Constants.ButtonPath.confirmBtn1.getPath()));
		btn[1].setRolloverIcon(new ImageIcon(Constants.ButtonPath.confirmBtn2.getPath()));
		btn[1].setBounds(30,240,80,30);
		btn[1].addActionListener(handler);
		
		btn[2] = new JButton(new ImageIcon(Constants.ButtonPath.cancelBtn1.getPath()));
		btn[2].setRolloverIcon(new ImageIcon(Constants.ButtonPath.cancelBtn2.getPath()));
		btn[2].setBounds(130,240,80,30);
		btn[2].addActionListener(handler);


		this.add(btn[0]);
		this.add(bgImg[0]);
	}


	public void initialize() { }
	
	public String getId() {
		return id;
	}

	public String getPwd() {
		return pwd;
	}

	public boolean isConnection() {
		return connection;
	}

	public void setConnection(boolean connection) {
		this.connection = connection;
	}

	public void changeSettingPanel() {
		this.removeAll();
		this.add(label[0]);
		this.add(label[1]);
		this.add(label[2]);
		this.add(textField);
		this.add(pwdField);
		this.add(btn[1]);
		this.add(btn[2]);
		this.repaint();
	}

	public void changeStatusPanel() {
		this.removeAll();
		this.add(btn[0]);
		if(this.connection) {
			this.add(bgImg[1]);
		}
		else{
			this.add(bgImg[0]);
		}
		this.repaint();
	}

	private class ActionHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {

			if(event.getSource()==btn[0]){
				changeSettingPanel();
			}
			
			if(event.getSource()==btn[1]){
				
				id = textField.getText();
				pwd = pwdField.getText();
				
				textField.setText("");
				pwdField.setText("");

				connection = true;
				changeStatusPanel();
				
				if(isConnection() && ClientLauncher.getFrame().
						getConnectionPanel().getPrivatePanel().isConnection()){

					String data = "";
					int type = Constants.PacketType.DirectoryListRequset.getType();
					int length = data.length();
			
					ClientLauncher.getFrame().getDirectoryListPanel().initialize();
					ClientLauncher.getFrame().changePanel(ClientLauncher.getFrame().getDirectoryListPanel());
					//ClientLauncher.getConnector().sendPacket(type, 0, length, data);
	
				}
			}
			
			if(event.getSource()==btn[2]){
				changeStatusPanel();
			}
		}
	}

}

