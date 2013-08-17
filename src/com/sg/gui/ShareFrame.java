package com.sg.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.StringTokenizer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.sg.main.ClientLauncher;
import com.sg.main.Constants;

public class ShareFrame extends JFrame  {
	
	private int width;
	private int height;
	private JLabel bgImg;
	private JTextField textField;
	private JTextArea textArea;
	private Font inputFont;
	private JButton btn[];
	private ActionHandler handler;
	private JScrollPane scroll;
	
	// 회원 가입 frame
	public ShareFrame(int w, int h) {
		
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

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e){
				initialize();
			}
		});
		
		this.setLayout(null);
		
		// 배경이미지
		bgImg = new JLabel(new ImageIcon(Constants.BackgroudPath.sharingBG.getPath()));
		bgImg.setBounds(0,0,width,height);

		// font 설정
		inputFont = Constants.Font1;
		
		// User ID TextFiled
		textField = new JTextField();
		textField.setBounds(100,85,180,30);
		textField.setFont(inputFont);
		this.add(textField);
		
		// User List TextArea
		textArea = new JTextArea();
		textArea.setFont(inputFont);
		textArea.setEditable(false);
		
		scroll = new JScrollPane(textArea,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setBounds(50,130,300,80);
		
		this.add(scroll);
		
		handler = new ActionHandler();
		
		// 확인버튼 0 , 취소버튼 1
		
		btn = new JButton[3];
		
		btn[0] = new JButton(new ImageIcon(Constants.ButtonPath.confirmBtn1.getPath()));
		btn[0].setRolloverIcon(new ImageIcon(Constants.ButtonPath.confirmBtn2.getPath()));
		btn[0].setBounds(200,220,80,30);
		btn[0].addActionListener(handler);
		
		btn[1] = new JButton(new ImageIcon(Constants.ButtonPath.cancelBtn1.getPath()));
		btn[1].setRolloverIcon(new ImageIcon(Constants.ButtonPath.cancelBtn2.getPath()));
		btn[1].setBounds(280,220,80,30);
		btn[1].addActionListener(handler);
		
		btn[2] = new JButton(new ImageIcon(Constants.ButtonPath.addBtn2.getPath()));
		btn[2].setRolloverIcon(new ImageIcon(Constants.ButtonPath.addBtn1.getPath()));
		btn[2].setBounds(280,85,30,30);
		btn[2].addActionListener(handler);

		this.add(btn[0]);
		this.add(btn[1]);
		this.add(btn[2]);
		this.add(bgImg);
	}
	
	public JTextField getTextField() {
		return textField;
	}

	public void setTextField(JTextField textField) {
		this.textField = textField;
	}

	public JTextArea getTextArea() {
		return textArea;
	}

	public void setTextArea(JTextArea textArea) {
		this.textArea = textArea;
	}

	// form 초기화
	public void initialize(){
		textField.setText("");
		textArea.setText("");
	}
	
	// 버튼에 대한 이벤트
	private class ActionHandler implements ActionListener {
		private String id;

		@Override
		public void actionPerformed(ActionEvent event) {

			String userId = textField.getText();
			String userList = textArea.getText();
			
			// 확인 버튼을 눌렀을때
			if(event.getSource()==btn[0]){
							
				// 빈 Textfiled가 없도록 예외처리
				if(userList.equals("")){
					JOptionPane.showMessageDialog(null, "Add User");
				}
				
				// 공유 패킷 전송 data = userID +"\t"+dirIndex +"\t"+targetNumber+targetId1+targetId2+...
				else{
					StringTokenizer tokenizer = new StringTokenizer(userList,"\n");
					String targetId[] = new String[userList.length()];
					int i =0;
					
					// 공유 targetId를  저장
					while(tokenizer.hasMoreTokens()) {
						targetId[i] = tokenizer.nextToken();
						i++;
					}
					
					// target id가 몇명인지

					String data = ClientLauncher.getUser().getId() +"\t" + 
							ClientLauncher.getFrame().getDirectoryListPanel().getDirectoryID()+"\t"+i;
					
					// target ID만큼 shareRequest를 보냄
					int type = Constants.PacketType.ShareRequest.getType();
					for(int j=0;j<i;j++){
						data += "\t" + targetId[j];
					}
					int length = data.length();

					ClientLauncher.getConnector().sendPacket(type, 0, length, data);
				}
				textField.setText("");
			}
			
			// 취소 버튼을 눌렀을 때
			if(event.getSource()==btn[1]){
				dispose();
				initialize();
			}
			
			// add 버튼을 눌렀을 때
			if(event.getSource()==btn[2]){
				if(userId.equals("")) 
					JOptionPane.showMessageDialog(null, "Input user ID");
				else if(userId.equals(ClientLauncher.getUser().getId()))
					JOptionPane.showMessageDialog(null, "Can't add your ID");
					
				// 추가할 user id
				else{
					String data = userId;
					int type = Constants.PacketType.IdCheckRequest.getType();
					int length = data.length();
	
					ClientLauncher.getConnector().sendPacket(type, 0, length, data);
				}
				
			}
		}
	}
	

}
