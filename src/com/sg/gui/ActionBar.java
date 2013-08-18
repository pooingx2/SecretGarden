package com.sg.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.sg.main.ClientLauncher;
import com.sg.main.Constants;

public class ActionBar extends JMenuBar{
	
	// Attribute
	private int width;
	private int height;
	private Stack<JPanel> backStack;
	private Stack<JPanel> forwardStack;
	private boolean isBackBtnPress; 
	private boolean isForwardBtnPress;
	
	// Components
	private JLabel bgImg;
	private ActionHandler handler;
	private JButton homeBtn;
	private JButton backBtn;
	private JButton forwardBtn;
	private JButton sharingBtn;
	private JButton logoutBtn;
	
	public ActionBar(int w, int h) {
		super();
		this.width = w;
		this.height = h;
		this.setLayout(null);
		this.backStack = new Stack<JPanel>();
		this.forwardStack = new Stack<JPanel>();
		this.isBackBtnPress = false;
		this.isForwardBtnPress = false;
		
		// 배경이미지 등록
		bgImg = new JLabel(new ImageIcon(Constants.BackgroudPath.barBG.getPath()));
		bgImg.setBounds(0,0,width,height);
		
		// 이벤트 핸들러 등록
		handler = new ActionHandler();
		
		// home 버튼
		homeBtn = new JButton(new ImageIcon(Constants.ButtonPath.homeBtn1.getPath()));
		homeBtn.setRolloverIcon(new ImageIcon(Constants.ButtonPath.homeBtn2.getPath()));
		homeBtn.setBounds(20,5,40,40);
		homeBtn.addActionListener(handler);
		
		// 뒤로 버튼
		backBtn = new JButton(new ImageIcon(Constants.ButtonPath.backBtn1.getPath()));
		backBtn.setRolloverIcon(new ImageIcon(Constants.ButtonPath.backBtn2.getPath()));
		backBtn.setBounds(70,5,40,40);
		backBtn.addActionListener(handler);
		
		// 앞으로 버튼
		forwardBtn = new JButton(new ImageIcon(Constants.ButtonPath.forwardBtn1.getPath()));
		forwardBtn.setRolloverIcon(new ImageIcon(Constants.ButtonPath.forwardBtn2.getPath()));
		forwardBtn.setBounds(120,5,40,40);
		forwardBtn.addActionListener(handler);
		
		// 공유 버튼
		sharingBtn = new JButton(new ImageIcon(Constants.ButtonPath.sharingBtn1.getPath()));
		sharingBtn.setRolloverIcon(new ImageIcon(Constants.ButtonPath.sharingBtn2.getPath()));
		sharingBtn.setBounds(670,10,30,30);
		sharingBtn.addActionListener(handler);
		
		// 로그아웃 버튼
		logoutBtn = new JButton(new ImageIcon(Constants.ButtonPath.logoutBtn1.getPath()));
		logoutBtn.setRolloverIcon(new ImageIcon(Constants.ButtonPath.logoutBtn2.getPath()));
		logoutBtn.setBounds(720,5,40,40);
		logoutBtn.addActionListener(handler);
		
		this.add(homeBtn);
		this.add(backBtn);
		this.add(forwardBtn);
		this.add(sharingBtn);
		this.add(logoutBtn);
		this.add(bgImg);
	}
	
	public void initialize() { 
		backStack.clear();
		forwardStack.clear();
	}
	
	public Stack<JPanel> getBackStack() {
		return backStack;
	}

	public void setBackStack(Stack<JPanel> backStack) {
		this.backStack = backStack;
	}

	public Stack<JPanel> getForwardStack() {
		return forwardStack;
	}

	public void setForwardStack(Stack<JPanel> forwardStack) {
		this.forwardStack = forwardStack;
	}

	public boolean isBackBtnPress() {
		return isBackBtnPress;
	}

	public void setBackBtnPress(boolean isBackBtnPress) {
		this.isBackBtnPress = isBackBtnPress;
	}

	public boolean isForwardBtnPress() {
		return isForwardBtnPress;
	}

	public void setForwardBtnPress(boolean isForwardBtnPress) {
		this.isForwardBtnPress = isForwardBtnPress;
	}


	// 이벤트 핸들러 등록
	private class ActionHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			
			if(event.getSource()==homeBtn){
				ClientLauncher.getFrame().changePanel(ClientLauncher.getFrame().getConnectionPanel());
			}	
			// 뒤로가기 버튼을 눌렀을때 stack에 담긴 page에서 pop하고 현재 페이지를 forward stack에 push
			if(event.getSource()==backBtn){
				isBackBtnPress = true;
				if(!backStack.isEmpty()){
					JPanel item= backStack.pop();
					ClientLauncher.getFrame().changePanel(item);
				}
				else{
					JOptionPane.showMessageDialog(null, "Empty Stack");
				}
				isBackBtnPress = false;
			}	

			// 앞으로가기 버튼을 눌렀을때 stack에 담긴 page에서 pop하고 현재 페이지를 back stack에 push
			if(event.getSource()==forwardBtn){
				isForwardBtnPress = true;
				if(!forwardStack.isEmpty()){
					JPanel item= forwardStack.pop();
					ClientLauncher.getFrame().changePanel(item);
				}
				else{
					JOptionPane.showMessageDialog(null, "Empty Stack");
				}
				isForwardBtnPress = false;
			}	
			
			if(event.getSource()==sharingBtn){
				String data = ClientLauncher.getUser().getId();
				int type = Constants.PacketType.ShareListRequest.getType();
				int length = data.length();	
				
				// 공유 리스트 패킷을 전송
				ClientLauncher.getConnector().sendPacket(type, 0, length, data);
//				ClientLauncher.getFrame().changePanel(ClientLauncher.getFrame().getSharingPanel());
			}	
			
			if(event.getSource()==logoutBtn){
				String data = "logout reqeust";
				int type = Constants.PacketType.LogoutRequest.getType();
				int length = data.length();	
				
				// 로그아웃 요청 패킷 전송
				ClientLauncher.getConnector().sendPacket(type, 0, length, data);
				
			}
		}
	}

}
