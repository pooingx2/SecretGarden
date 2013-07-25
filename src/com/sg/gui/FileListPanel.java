package com.sg.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import com.sg.main.ClientLauncher;
import com.sg.main.Constants;
import com.sg.model.FileInfo;

public class FileListPanel extends JPanel {

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
	private FileMngPanel fileMngPanel;
	private Vector<String> row;
	private JTree fileTree;
	private TreeModel model;
	private DefaultMutableTreeNode root;
	private DefaultMutableTreeNode selectedNode;


	public FileListPanel(int w, int h) {

		super();
		this.width = w;
		this.height = h;
		this.isSelected = false;
		this.setBackground(Constants.backColor);
		this.setLayout(null);

		// 배경이미지 등록
		bgImg = new JLabel(new ImageIcon(Constants.BackgroudPath.fileListBG.getPath()));
		bgImg.setBounds(0,0,width,height);

		// 이벤트 핸들러 등록
		handler = new ActionHandler();

		// TreeModel 등록
		root = new DefaultMutableTreeNode("root");

//		System.out.println("\n\n root.getDepth()" + root.getLevel());
//		
//		for (int j = 0; j < 5; j++) {
//			DefaultMutableTreeNode node1 = new DefaultMutableTreeNode("Main :" + j);
//			root.add(node1);
//			System.out.println("\n\n node1.getDepth()" + node1.getLevel());
//			System.out.println("\n\n node1.getParent()" + node1.getParent());
//			for (int i = 0; i < 4; i++) {
//				DefaultMutableTreeNode tmpnode = new DefaultMutableTreeNode(i);
//				node1.add(tmpnode);
//				System.out.println("\n\n tmpnode.getDepth()" + tmpnode.getLevel());
//				System.out.println("\n\n tmpnode.getParent()" + tmpnode.getParent());
//			}
//		}
		
		model= new DefaultTreeModel(root);
		

		// File view Tree 등록
		fileTree = new JTree();
		fileTree.setRowHeight(20);
		fileTree.setModel(model);
		fileTree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent event) {
				// TODO Auto-generated method stub
				System.out.println("event");
			}
		});
		fileTree.setEditable(true);
		fileTree.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

		// scroll 등록
		scroll = new JScrollPane(fileTree, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, 
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setBounds(50, 100, 250, 360);

		// 버튼 그룹 패널 생성
		btnGroupPanel = new JPanel();
		btnGroupPanel.setBounds(400,100,350,80);
		btnGroupPanel.setBackground(Constants.backColor);
		btnGroupPanel.setLayout(null);

		// 버튼 생성  (Create, Access, Delte)
		btn = new JButton[4];

		btn[0] = new JButton(new ImageIcon(Constants.ButtonPath.createBtn1.getPath()));
		btn[0].setRolloverIcon(new ImageIcon(Constants.ButtonPath.createBtn2.getPath()));

		btn[1] = new JButton(new ImageIcon(Constants.ButtonPath.uploadBtn1.getPath()));
		btn[1].setRolloverIcon(new ImageIcon(Constants.ButtonPath.uploadBtn2.getPath()));

		btn[2] = new JButton(new ImageIcon(Constants.ButtonPath.downloadBtn1.getPath()));
		btn[2].setRolloverIcon(new ImageIcon(Constants.ButtonPath.downloadBtn2.getPath()));
		
		btn[3] = new JButton(new ImageIcon(Constants.ButtonPath.deleteBtn1.getPath()));
		btn[3].setRolloverIcon(new ImageIcon(Constants.ButtonPath.deleteBtn2.getPath()));

		for(int i=0;i<4;i++) {
			btn[i].setBounds(20+80*i,5,70,70);
			btn[i].addActionListener(handler);
			btnGroupPanel.add(btn[i]);
		}

		// File을 관리할 수 있는 패널 등록
		fileMngPanel = new FileMngPanel(350,250);
		fileMngPanel.setBounds(400,200,350,250);

		this.add(scroll);
		this.add(btnGroupPanel);
		this.add(fileMngPanel);
		this.add(bgImg);
	}


	public void initialize() { 
		scroll = new JScrollPane(fileTree, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, 
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setBounds(50, 100, 250, 360);
		this.removeAll();
		this.add(scroll);
		this.add(btnGroupPanel);
		this.add(fileMngPanel);
		this.add(bgImg);
	}

	// fileInfoList에 저장된 node를 통해 tree를 만듬
	public void makeTree() {
		Vector<FileInfo> fileInfoList;
		DefaultMutableTreeNode node;
		int maxDepth;
		
		fileInfoList = ClientLauncher.getFileMgr().getFileInfoList();
		maxDepth = ClientLauncher.getFileMgr().getMaxDepth();
		
		for(FileInfo fileInfo : fileInfoList){
			node = new DefaultMutableTreeNode(fileInfo.getName());
			if(fileInfo.getDepth().equals("1")){
				root.add(node);
			}
			else{
				DefaultMutableTreeNode temp;
				temp = root;
				while( temp != null){
					if(temp.toString().equals(fileInfo.getParent()) && 
							temp.getLevel() == (Integer.parseInt(fileInfo.getDepth())-1)){
						temp.add(node);
						System.out.println("add " + temp.getParent() + " : "+ temp);
					}
					temp = temp.getNextNode();
				}
			}
		}

	}
	
	public DefaultMutableTreeNode getSelectedNode() {
		return (DefaultMutableTreeNode) fileTree.getLastSelectedPathComponent();
		
	}


	// Component 추가 및 제거를 반영하기 위한 새로고침
	public void changePanel() { 
		this.remove(bgImg);
		fileMngPanel.changePanel();
		this.add(scroll);
		this.add(fileMngPanel);
		this.add(bgImg);
		this.repaint();
	}

	public void create(String dirName){
		if (selectedNode == null)
			JOptionPane.showMessageDialog(null, "Choose a path");
		else
			System.out.println("create");
	}
	
	public void upload(){
		if (selectedNode == null)
			JOptionPane.showMessageDialog(null, "Choose a path");
		else
			System.out.println("upload");
	}
	
	public void download(){
		if (selectedNode == null)
			JOptionPane.showMessageDialog(null, "Choose a file");
		else
			System.out.println("download");
	}
	public void delete(){
		if (selectedNode == null)
			JOptionPane.showMessageDialog(null, "Choose a file or directory");
			
		else if( selectedNode == root )
			JOptionPane.showMessageDialog(null, "Can't delete root directory");
		else
			((DefaultTreeModel) model).removeNodeFromParent(selectedNode);
	}

	// 버튼 이벤트, 마우스 이벤트 리스너 등록
	private class ActionHandler implements ActionListener, MouseListener{


		/* 	status
		0 : Default		1 : Information		2 : Create	
		3 : Upload		4 : Download		5 : Delete 
		*/
		
		@Override
		public void actionPerformed(ActionEvent event) {

			selectedNode = getSelectedNode();
			
			// Create
			if(event.getSource()==btn[0]){
				fileMngPanel.setStatus(2);
				fileMngPanel.changePanel();
			}
			// Upload
			if(event.getSource()==btn[1]){
				fileMngPanel.setStatus(3);
				fileMngPanel.changePanel();
			}
			// Download
			if(event.getSource()==btn[2]){
				fileMngPanel.setStatus(4);
				fileMngPanel.changePanel();
			}

			// Delete
			if(event.getSource()==btn[3]){
				fileMngPanel.setStatus(5);
				fileMngPanel.changePanel();
				delete();
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