package com.sg.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.sg.main.ClientLauncher;
import com.sg.main.Constants;

public class DirectoryListPanel extends JPanel {
	
	// Attribute
	private int width;
	private int height;
	private boolean isSelected;
	
	// Components
	private JLabel bgImg;
	private JScrollPane scroll;
	private JPanel btnGroupPanel;
	private JButton btn[];
	private ActionHandler handler;
	private DirectoryMngPanel dirMngPanel;
	private DefaultTableModel tableModel;
	private JTable table;
	private JTableHeader header;
	
	/* 디렉토리 리스트 관련 변수 */ 
	// PacketMgr에서 받아온 파일 및 폴더 정보를 저장하기 위한 변수 
	private Vector<String> row;
	// 파일 및 폴더 추가시 사용할 부모 및 루트 정보를 보관한다
	//private Map<String, String> nameTo_ParentandRoot;
	
	
	public DirectoryListPanel(int w, int h) {

		super();
		this.width = w;
		this.height = h;
		this.isSelected = false;
		this.setBackground(Constants.backColor);
		this.setLayout(null);

		bgImg = new JLabel(new ImageIcon(Constants.BackgroudPath.directoryListBG.getPath()));
		bgImg.setBounds(0,0,width,height);

		handler = new ActionHandler();
		
		tableModel = new DefaultTableModel();
		tableModel.setColumnIdentifiers( new String[] {"ID","Directory Name"} );
		
		table = new JTable( );
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setRowHeight(30);
        table.setFont(Constants.Font1);
        table.setModel(tableModel);
        
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
    			if (!event.getValueIsAdjusting()) {
    				isSelected = true;
    				dirMngPanel.setSelectedValue(table.getValueAt(table.getSelectedRow(), 0).toString());
    				dirMngPanel.setStatus(0);
    				changePanel();
    			}
            }
        });

		header = table.getTableHeader();
		header.setFont(Constants.Font2);
        header.setEnabled( false );
        
        scroll = new JScrollPane( table );
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
	}
	
	public void initialize() { }
	
	// 리스트 초기화 함수 
	// 디렉토리 삽입,삭제,파일 업로드,다운로드시에 리스트를 갱신해야한다
	public void initTable() {
		for(int i=tableModel.getRowCount()-1;i>=0;i--){
			tableModel.removeRow(i);
		}
	}
	
	public void addRow(Vector<String> row){
		tableModel.addRow(row);
	}

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
		this.add(scroll);
		this.add(dirMngPanel);
		this.add(bgImg);
		this.repaint();
	}
	
	//디렉토리 추가 버튼 클릭시 실행
	public void create(String dir){
		String data;
		int type;
		int length;
		
		System.out.println("Asdf!!!!!");
		// 디렉토리 생성을 반영하기 위한 패널 새로고침
		changePanel();
	
		// 디렉토리 추가 패킷 전송
		// 초기 로드시 디렉토리 조회 패킷 전송
		// 부분마다 추가를 위해 parent, root를 해쉬맵에서 조회하는 부분이 필요하다 
		
		data = dir;
		type = Constants.PacketType.DirectoryCreateRequset.getType();
		length = data.length();
		ClientLauncher.getConnector().sendPacket(type, 0, length, data);
		
		// 추가된 Component를 반영하기 위해 조회 패킷 전송
		data = "";
		type = Constants.PacketType.DirectoryListRequset.getType();
		length = data.length();
		ClientLauncher.getConnector().sendPacket(type, 0, length, data);
	}
	
	public void access(){
		isSelected = false;
	}
	
	public void delete(){
		isSelected = false;
	}

	private class ActionHandler implements ActionListener, MouseListener{

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
}
