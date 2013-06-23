package com.sg.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.sg.main.ClientLauncher;
import com.sg.main.Constants;

public class MainFrame extends JFrame  {
	
	private int width;
	private int height;
	private LoginPanel loginPanel;
	private ActionBar actionBar;
	private ConnectionPanel connectionPanel;
	private DirectoryListPanel directoryListPanel;
	
	public MainFrame() {
		
		super();
		

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.updateComponentTreeUI(this);
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		this.width = Constants.frameW;
		this.height = Constants.frameH;
		
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		
		this.setSize(width,height);
		this.setResizable(false);
		this.setLocation(screen.width/2-this.width/2, screen.height/2-this.height/2);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e){
				ClientLauncher.getConnector().disconnect();
			}
		});
		this.setLayout(null);
		
		loginPanel = new LoginPanel(width, height);
		loginPanel.setBounds(0, 0, width, height);
		
		actionBar = new ActionBar(width,50);
		actionBar.setBounds(0, 0, width, 50);
		
		connectionPanel = new ConnectionPanel(width, height-50);
		connectionPanel.setBounds(0, 50, width, height-50);

		directoryListPanel = new DirectoryListPanel(width, height-50);
		directoryListPanel.setBounds(0, 50, width, height-50);
		
//		this.add(actionBar);
		this.add(loginPanel);
//		this.add(directoryListPanel);
		
		// Initialize components functions
		this.setVisible(true);
	}
	
	public void changePanel(JPanel panel) {
		this.getContentPane().removeAll();
//		panel.setFocusable(true);
		if(panel != loginPanel){
			this.add(actionBar);
		}
		this.add(panel);
		this.repaint();
	}
	
	public LoginPanel getLoginPanel() {
		return loginPanel;
	}

	public void setLoginPanel(LoginPanel loginPanel) {
		this.loginPanel = loginPanel;
	}

	public ConnectionPanel getConnectionPanel() {
		return connectionPanel;
	}

	public void setConnectionPanel(ConnectionPanel connectionPanel) {
		this.connectionPanel = connectionPanel;
	}

	public DirectoryListPanel getDirectoryListPanel() {
		return directoryListPanel;
	}

	public void setDirectoryListPanel(DirectoryListPanel directoryListPanel) {
		this.directoryListPanel = directoryListPanel;
	}
	
	

}
