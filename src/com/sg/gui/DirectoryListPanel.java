package com.sg.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.sg.main.ClientLauncher;
import com.sg.main.Constants;




public class DirectoryListPanel extends JPanel {
	
	// Attribute
	private int width;
	private int height;
	private boolean isSelected;
	private boolean flag;
	
	// Components
	private JLabel bgImg;
	private JList<String> list;
	private JScrollPane scroll;
	
	private JPanel btnGroupPanel;
	private JButton btn[];
	private ActionHandler handler;
	private DirectoryMngPanel dirMngPanel;

	
	/* 디렉토리 리스트 관련 변수 */ 
	// PacketMgr에서 받아온 파일 및 폴더 정보를 저장하기 위한 변수 
	static Vector<String> dirList;
	// 파일 및 폴더 추가시 사용할 부모 및 루트 정보를 보관한다
	static Map<String, String> nameTo_ParentandRoot;
	
	
	public DirectoryListPanel(int w, int h) {

		super();
		this.width = w;
		this.height = h;
		this.isSelected = false;
		this.flag = true;
		this.setBackground(Constants.backColor);
		this.setLayout(null);
		
		bgImg = new JLabel(new ImageIcon(Constants.BackgroudPath.directoryListBG.getPath()));
		bgImg.setBounds(0,0,width,height);

		dirList = new Vector<>();
		nameTo_ParentandRoot = new HashMap<String, String>();
		
		list = new JList<String>(dirList);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setFixedCellHeight(30);
		list.setFont(Constants.Font2);
		handler = new ActionHandler();
		list.addListSelectionListener(handler);
		list.addMouseListener(handler);
		
		scroll = new JScrollPane(list,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setBounds(50, 100, 300, 360);

		btnGroupPanel = new JPanel();
		btnGroupPanel.setBounds(450,100,300,80);
		btnGroupPanel.setBackground(Constants.backColor);
		btnGroupPanel.setLayout(null);

		btn = new JButton[3];
		
		btn[0] = new JButton(new ImageIcon(Constants.ButtonPath.createBtn1.getPath()));
		btn[0].setRolloverIcon(new ImageIcon(Constants.ButtonPath.createBtn2.getPath()));

		btn[1] = new JButton(new ImageIcon(Constants.ButtonPath.accessBtn1.getPath()));
		btn[1].setRolloverIcon(new ImageIcon(Constants.ButtonPath.accessBtn2.getPath()));

		btn[2] = new JButton(new ImageIcon(Constants.ButtonPath.deleteBtn1.getPath()));
		btn[2].setRolloverIcon(new ImageIcon(Constants.ButtonPath.deleteBtn2.getPath()));



		for(int i=0;i<3;i++) {
			btn[i].setBounds(15+100*i,5,70,70);
			btn[i].addActionListener(handler);
			btnGroupPanel.add(btn[i]);
		}

		dirMngPanel = new DirectoryMngPanel(300,250);
		dirMngPanel.setBounds(450,200,300,250);

		this.add(scroll);
		this.add(btnGroupPanel);
		this.add(dirMngPanel);
		this.add(bgImg);
		
		//초기 로드시 디렉토리 조회 패킷 전송
		String data = "directory list request" + "\t" + "endif";
		int type = Constants.PacketType.DirectoryListRequset.getType();
		int length = data.length();
		
		ClientLauncher.getConnector().sendPacket(type, 0, length, data);
		
	}

	public void initialize() { }

	public boolean getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	// Component 추가 및 제거를 반영하기 위한 새로고침
	public void changePanel() { 
		this.remove(bgImg);
		dirMngPanel.changetPanel();
		this.add(dirMngPanel);
		this.add(bgImg);
		this.repaint();
	}
	
	
	//디렉토리 추가 버튼 클릭시 발생하는 이벤트
	public void create(String dir){
		
		// 디렉토리 생성을 반영하기 위한 패널 새로고침
		changePanel();
		
		// 디렉토리 추가 패킷 전송
		// 초기 로드시 디렉토리 조회 패킷 전송
		// 부분마다 추가를 위해 parent, root를 해쉬맵에서 조회하는 부분이 필요하다 
		String data = "NULL" + "\t" + "folder" + "\t" + dir + "\t" + "1" + "\t" + "mydir" + "\t" + "1";
		int type = Constants.PacketType.DirectoryCreateRequset.getType();
		int length = data.length();
			
		ClientLauncher.getConnector().sendPacket(type, 0, length, data);
		
		// 추가된 Component를 반영하기 위해 조회 패킷 전송
		data = "directory list request" + "\t" + "endif";
		type = Constants.PacketType.DirectoryListRequset.getType();
		length = data.length();
	
		ClientLauncher.getConnector().sendPacket(type, 0, length, data);
		
	}
	
	public void access(){
		isSelected = false;
	}
	
	public void delete(){
		dirList.removeElementAt(list.getSelectedIndex());
		flag = false;
		list.setListData(dirList);
		list.repaint();
		flag = true;
		isSelected = false;
	}

	private class ActionHandler implements ActionListener, ListSelectionListener, MouseListener{

		private String id;
		private String pwd;

		@Override
		public void actionPerformed(ActionEvent event) {

			if(event.getSource()==btn[0]){
				dirMngPanel.setStatus(2);
				changePanel();
			}

			if(event.getSource()==btn[1]){
				if(isSelected) {
					dirMngPanel.setStatus(3);
					changePanel();
				}
				else{
					dirMngPanel.setStatus(0);
					changePanel();
					JOptionPane.showMessageDialog(null, "Choose a directory");
				}
			}

			if(event.getSource()==btn[2]){
				
				if(isSelected) {
					dirMngPanel.setStatus(4);
					changePanel();
				}
				
				else {
					dirMngPanel.setStatus(0);
					changePanel();
					JOptionPane.showMessageDialog(null, "Choose a directory");
				}
			}
			
		}

		@Override
		public void valueChanged(ListSelectionEvent event) {
			if (!event.getValueIsAdjusting() && flag) {
				isSelected = true;
				dirMngPanel.setSelectedValue(list.getSelectedValue().toString());
				dirMngPanel.setStatus(0);
				changePanel();
			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			if(e.getButton()==MouseEvent.BUTTON1) {
				if(e.getClickCount()==2) {
					btn[1].doClick();
				}
			}
		}
		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
		}
	}
	
	// 외부 클래스(PacketMgr)에서 리스트에 디렉토리 목록을 갱신하기 위한 함수
	public static void addList(String type, String name, String parent, String rootInt){
			
			dirList.addElement(type+ "  " + name);
			nameTo_ParentandRoot.put(name, parent);
			nameTo_ParentandRoot.put(name, rootInt);	
			
	}
	
	// 리스트 초기화 함수 
	// 디렉토리 삽입,삭제,파일 업/다운로드시에 리스트를 갱신해야한다
	public static void initList()
	{
		dirList.clear();
		nameTo_ParentandRoot.clear();
	}
}

