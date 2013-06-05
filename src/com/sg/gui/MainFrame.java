package com.sg.gui;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.sg.main.Constants;

public class MainFrame extends JFrame  {
	
	private int width;
	private int height;

	
	public MainFrame() {
		
		super();
		
		this.width = Constants.frameW;
		this.height = Constants.frameH;
		
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		
		this.setSize(width,height);
		this.setLocation(screen.width/2-this.width/2, screen.height/2-this.height/2);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setLayout(null);
		
		LoginPanel loginpanel = new LoginPanel(width, height);
		loginpanel.setBounds(0, 0, width, height);


		this.add(loginpanel);
		
		// Initialize components functions
		this.setVisible(true);
	}

	public void initialize() {
		
		// Initialize associated functions

	}

}
