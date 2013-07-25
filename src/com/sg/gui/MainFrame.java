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
	private FileListPanel fileListPanel;
	
	// main frame
	public MainFrame(int w, int h) {
		
		super();
		
		// lookAndFeel을 통해 OS에 맞는 UI제공
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
		
		// 화면 크기를 구함
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		
		this.setSize(width,height);
		this.setResizable(false);
		
		// 화면 중앙에 frame이 오도록 설정
		this.setLocation(screen.width/2-this.width/2, screen.height/2-this.height/2);
		
		// frame x버튼시 event 리스너
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e){
				ClientLauncher.getConnector().disconnect();
			}
		});
		this.setLayout(null);
		
		// 로그인 패널 생성
		loginPanel = new LoginPanel(width, height);
		loginPanel.setBounds(0, 0, width, height);
		
		// 상단 액션바패널 생성
		actionBar = new ActionBar(width,50);
		actionBar.setBounds(0, 0, width, 50);
		
		// public과 private에 연결과 인증을 위한 패널 생성
		connectionPanel = new ConnectionPanel(width, height-50);
		connectionPanel.setBounds(0, 50, width, height-50);

		// 최상위 디렉토리 목록을 보여주는 패널 생성
		directoryListPanel = new DirectoryListPanel(width, height-50);
		directoryListPanel.setBounds(0, 50, width, height-50);
		
		// 파일 목록을 Tree형태로 보여주는 패널 생성
		fileListPanel = new FileListPanel(width, height-50);
		fileListPanel.setBounds(0, 50, width, height-50);
		
		// 처음 로그인 패널을 띄움
		this.add(loginPanel);
		
//		this.add(actionBar);
//		this.add(fileListPanel);
//		this.add(directoryListPanel);
		
		// Initialize components functions
		this.setVisible(true);
	}
	
	// main frame 화면 전환을 위한 패널
	public void changePanel(JPanel panel) {
		this.getContentPane().removeAll();
		if(panel != loginPanel){
			this.add(actionBar);
		}
		this.add(panel);
		this.repaint();
	}
	
	//getter setter
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

	public FileListPanel getFileViewPanel() {
		return fileListPanel;
	}

	public void setFileListPanel(FileListPanel fileListPanel) {
		this.fileListPanel = fileListPanel;
	}

}
