package com.sg.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import com.sg.main.ClientLauncher;
import com.sg.main.Constants;

public class DirectoryMngPanel extends JPanel {
	// Attribute
	private int width;
	private int height;

	/* 	status
	0 : Default		1 : Information		2 : Create	
	3 : Access		4 : Delete			5 : Settings 
	*/
	private int status;
	private String key;

	
	// Components
	private Font inputFont;
	private JLabel bgImg[];
	private JLabel label[];
	private JTextField textField[];
	private JButton btn[];
	private ActionHandler handler;

	public DirectoryMngPanel(int w, int h) {

		super();
		this.width = w;
		this.height = h;
		this.status = 1;
		this.setLayout(null);
		this.setBackground(Constants.backColor);
		this.setBorder(new LineBorder(Color.LIGHT_GRAY));
		this.key=null;

		// 디렉토리를 관리하기 위한 배경 이미지 (Create, Access, Delete,  ...
		bgImg = new JLabel[6];
		bgImg[0] = new JLabel();
		bgImg[1] = new JLabel(new ImageIcon(Constants.BackgroudPath.directoryMngBG1.getPath()));
		bgImg[2] = new JLabel(new ImageIcon(Constants.BackgroudPath.directoryMngBG2.getPath()));
		bgImg[3] = new JLabel(new ImageIcon(Constants.BackgroudPath.directoryMngBG3.getPath()));
		bgImg[4] = new JLabel(new ImageIcon(Constants.BackgroudPath.directoryMngBG4.getPath()));
		bgImg[5] = new JLabel(new ImageIcon(Constants.BackgroudPath.directoryMngBG5.getPath()));
		
		for(int i=0;i<6;i++) {
			bgImg[i].setBounds(1,1,width-2,height-2);
		}

		// font 설정
		inputFont = Constants.Font1;

		label = new JLabel[5];
		for(int i=0;i<5;i++){
			label[i] = new JLabel();
			label[i].setBounds(30,50+(i*30),250,30);
			label[i].setFont(Constants.Font1);
		}
		
		textField = new JTextField[2];
		btn = new JButton[3];

		handler = new ActionHandler();

		textField[0] = new JTextField();		// directory name
		textField[0].setBounds(20,140,200,30);
		textField[0].setFont(inputFont);

		textField[1] = new JTextField();		// file path
		textField[1].setBounds(20,140,250,30);
		textField[1].setEditable(false);
		textField[1].setFont(inputFont);

		handler = new ActionHandler();
		
		// Accessfile load 버튼
		btn[0] = new JButton(new ImageIcon(Constants.ButtonPath.loadKeyfileBtn1.getPath()));
		btn[0].setRolloverIcon(new ImageIcon(Constants.ButtonPath.loadKeyfileBtn2.getPath()));
		btn[0].setBounds(250,100,30,30);
		btn[0].addActionListener(handler);

		// 확인버튼
		btn[1] = new JButton(new ImageIcon(Constants.ButtonPath.confirmBtn1.getPath()));
		btn[1].setRolloverIcon(new ImageIcon(Constants.ButtonPath.confirmBtn2.getPath()));
		btn[1].setBounds(100,200,80,30);
		btn[1].addActionListener(handler);

		// 취서버튼
		btn[2] = new JButton(new ImageIcon(Constants.ButtonPath.cancelBtn1.getPath()));
		btn[2].setRolloverIcon(new ImageIcon(Constants.ButtonPath.cancelBtn2.getPath()));
		btn[2].setBounds(200,200,80,30);
		btn[2].addActionListener(handler);

		this.add(bgImg[1]);
	}

	public JLabel[] getLabel() {
		return label;
	}

	public void setLabel(JLabel[] label) {
		this.label = label;
	}

	public void initialize() { 
		this.key=null;
		this.status=1;
		textField[0].setText("");
		textField[1].setText("");
		label[0].setText("");
		label[1].setText("");
		label[2].setText("");
		label[3].setText("");
		label[4].setText("");
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	// JFleChoose를 통한 파일 선택 함수
	public void loadFile() {
		Vector<String> fileInfo = ClientLauncher.getFileMgr().loadKeyFile();
		textField[1].setText(fileInfo.get(0));
		key = fileInfo.get(1);
	}

	// 각종 status에 따라  Directory관리 패널을 변경
	public void changePanel() {

		this.removeAll();
		switch(this.status){
		case 0 : 	// Default
			this.add(label[0]);
			this.add(label[1]);
			this.add(label[2]);
			this.add(label[3]);
			this.add(label[4]);
			break;
		case 1 : 	// Information
			break;
		case 2 : 	// Create 
			this.add(textField[0]);
			this.add(btn[1]);
			this.add(btn[2]);
			break;
		case 3 : 	// Access
			this.add(textField[1]);
			this.add(btn[0]);
			this.add(btn[1]);
			this.add(btn[2]);
			break;
		case 4 : 	//	Delete
			this.add(btn[1]);
			this.add(btn[2]);
			break;
		case 5 : 	//	Settings
			this.add(btn[1]);
			this.add(btn[2]);
		default : 
			break;
		}
		this.add(bgImg[status]);
		this.repaint();
	}

	// 버튼에 따른 이벤트 핸들러
	private class ActionHandler implements ActionListener {
		private String dirName;

		@Override
		public void actionPerformed(ActionEvent event) {

			// file load버튼을 누르면 유저 컴퓨터에서 file을 선택하도록 함
			if(event.getSource()==btn[0]){
				loadFile();
			}

			// 확인버튼을 누르면 해당 상태에 맞는 함수를 call 
			if(event.getSource()==btn[1]){
				dirName=textField[0].getText();
				if(status == 2) {
					ClientLauncher.getFrame().getDirectoryListPanel().create(dirName);
				}
				else if(status == 3) {
					key = getKey();
//					if(key!=null){
//						ClientLauncher.getFrame().getDirectoryListPanel().access(key);
//					}
//					else
//						JOptionPane.showMessageDialog(null, "Load Key file");
					ClientLauncher.getFrame().getDirectoryListPanel().access(key);

				}
				else if(status == 4) {
					ClientLauncher.getFrame().getDirectoryListPanel().delete();
				}
				else if(status == 5) {
					ClientLauncher.getFrame().getDirectoryListPanel().settings();
				}
				initialize();
			}

			// 취소버튼을 누르면 초기화
			if(event.getSource()==btn[2]){
				initialize();
				if(ClientLauncher.getFrame().getDirectoryListPanel().getIsSelected()){
					setStatus(0);
				}
				else {
					label[1].setText("");
					setStatus(1);
				}
				
				changePanel();
				
			}
		}
	}
}

