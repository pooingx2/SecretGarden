package com.sg.gui;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.sg.main.Constants;


public class ConnectionPanel extends JPanel {
	// Attribute
	private int width;
	private int height;
	
	// Components
	private JLabel bgImg;
	private ConnectionPrivatePanel privatePanel;
	private ConnectionPublicPanel publicPanel;

	public ConnectionPanel(int w, int h) {
		
		super();
		this.width = w;
		this.height = h;
		this.setLayout(null);
		
		bgImg = new JLabel(new ImageIcon(Constants.BackgroudPath.connectionBG.getPath()));
		bgImg.setBounds(0,0,width,height);
		
		privatePanel = new ConnectionPrivatePanel(250,300);
		privatePanel.setBounds(50,135,250,300);
		
		publicPanel = new ConnectionPublicPanel(250,300);
		publicPanel.setBounds(500,135,250,300);
		
		this.add(privatePanel);
		this.add(publicPanel);
		this.add(bgImg);
	}
	
	public void initialize() { }
	
	public ConnectionPrivatePanel getPrivatePanel() {
		return privatePanel;
	}

	public void setPrivatePanel(ConnectionPrivatePanel privatePanel) {
		this.privatePanel = privatePanel;
	}

	public ConnectionPublicPanel getPublicPanel() {
		return publicPanel;
	}

	public void setPublicPanel(ConnectionPublicPanel publicPanel) {
		this.publicPanel = publicPanel;
	}
}

