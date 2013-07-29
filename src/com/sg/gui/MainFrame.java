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

public class MainFrame extends JFrame  {
	
	private int width;
	private int height;
	private JPanel currentPanel;
	private LoginPanel loginPanel;
	private ActionBar actionBar;
	private SharingPanel sharingPanel;
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

		// public과 private에 연결과 인증을 위한 패널 생성
		connectionPanel = new ConnectionPanel(width, height-50);
		connectionPanel.setBounds(0, 50, width, height-50);

		// 최상위 디렉토리 목록을 보여주는 패널 생성
		directoryListPanel = new DirectoryListPanel(width, height-50);
		directoryListPanel.setBounds(0, 50, width, height-50);
		
		// 파일 목록을 Tree형태로 보여주는 패널 생성
		fileListPanel = new FileListPanel(width, height-50);
		fileListPanel.setBounds(0, 50, width, height-50);
		
		sharingPanel = new SharingPanel(width, height-50);
		sharingPanel.setBounds(0, 50, width, height-50);
		
		// 상단 액션바패널 생성
		actionBar = new ActionBar(width,50);
		actionBar.setBounds(0, 0, width, 50);
		
		// 처음 로그인 패널을 띄움
		currentPanel = loginPanel;
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
		System.out.println("Change : " + panel.getClass().getName());
		
		if(panel != loginPanel) {
			this.add(actionBar);
			if(panel == connectionPanel){
				connectionPanel.initialize();
			}
			else if(panel == directoryListPanel){
				directoryListPanel.initialize();
			}
			else if(panel == fileListPanel){
				fileListPanel.initialize();
			}
		}
		else {
			loginPanel.initialize();
			actionBar.initialize();
		}
		
		// 뒤로가기 버튼을 누른 경우가 아니고 페이지가 변했다면 backStack에 현재 페이지를 저장
		if(!actionBar.isBackBtnPress() && currentPanel != panel){
			actionBar.getBackStack().push(currentPanel);
		}
		// 뒤로가기 버튼을 누른고 페이지가 변했다면 forwardStack에 현재 페이지를 저장
		else if(actionBar.isBackBtnPress() && currentPanel != panel){
			actionBar.getForwardStack().push(currentPanel);
		}
		
		currentPanel = panel;
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

	public FileListPanel getFileListPanel() {
		return fileListPanel;
	}

	public void setFileListPanel(FileListPanel fileListPanel) {
		this.fileListPanel = fileListPanel;
	}

	public SharingPanel getSharingPanel() {
		return sharingPanel;
	}

	public void setSharingPanel(SharingPanel sharingPanel) {
		this.sharingPanel = sharingPanel;
	}
	

	public ActionBar getActionBar() {
		return actionBar;
	}

	public void setActionBar(ActionBar actionBar) {
		this.actionBar = actionBar;
	}

}
