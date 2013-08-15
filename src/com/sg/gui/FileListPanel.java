package com.sg.gui;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
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

import com.sg.main.ClientLauncher;
import com.sg.main.Constants;
import com.sg.model.FileInfo;
import com.sg.model.MetaData;

public class FileListPanel extends JPanel {

	// Attribute
	private int width;
	private int height;
	private boolean isSelected;
	private boolean editMode;
	
	// Components
	private JLabel bgImg;
	private JScrollPane scroll;
	private JPanel btnGroupPanel;
	private JButton btn[];
	private ActionHandler handler;
	private FileMngPanel fileMngPanel;
	private Vector<String> row;
	private JTree fileTree;
	private DefaultTreeModel model;
	private DefaultMutableTreeNode root;
	private Vector<FileInfo> fileInfoList;
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

		handler = new ActionHandler();

		// TreeModel 등록
		root = new DefaultMutableTreeNode("root");
		model = new DefaultTreeModel(root);
		
		// File view Tree 등록
		fileTree = new JTree(model);
		fileTree.setRowHeight(20);
		fileTree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {	
			@Override
			public void valueChanged(TreeSelectionEvent event) {
				selectedNode = getSelectedNode();
				fileMngPanel.setStatus(1);
				if (fileMngPanel.getStatus() == 1 && !editMode) {
					FileInfo fileInfo = getFileInfo(selectedNode);
					fileMngPanel.getLabel()[0].setText(fileInfo.getName());
					fileMngPanel.getLabel()[1].setText(fileInfo.getParent());
					fileMngPanel.getLabel()[2].setText(fileInfo.getDepth());
					fileMngPanel.getLabel()[3].setText(getSelectedPath());
					if((selectedNode.toString().indexOf('.'))==-1)
						fileMngPanel.getLabel()[4].setText(getFolderSize(selectedNode));
					else
						fileMngPanel.getLabel()[4].setText(
								ClientLauncher.getFileMgr().getSummarySize(Long.parseLong(fileInfo.getSize())));
					fileMngPanel.changePanel();
				}
			}
		});
		
		fileTree.setEditable(false);
		fileTree.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		fileTree.setCellRenderer(new MyTreeRenderer());
		
		// scroll 등록
		scroll = new JScrollPane(fileTree, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, 
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setBounds(50, 100, 250, 360);

		// File을 관리할 수 있는 패널 등록
		fileMngPanel = new FileMngPanel(350,250);
		fileMngPanel.setBounds(400,200,350,250);

		// 버튼
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
		
		this.add(scroll);
		this.add(btnGroupPanel);
		this.add(fileMngPanel);
		this.add(bgImg);
	}

	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
	}

	public void initialize() { 
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				editMode=true;
				makeTree();
				model.reload();
				fileMngPanel.initialize();
				changePanel();
				editMode=false;
			}
		});
	}

	// fileInfoList에 저장된 node를 통해 tree를 만듬
	public void makeTree() {
		Vector<FileInfo> fileInfoList;
		DefaultMutableTreeNode node;
		root.removeAllChildren();
		root.setUserObject("root"+ClientLauncher.getFileMgr().getRootDirID());
		fileTree.removeAll();
		
		fileInfoList = ClientLauncher.getFileMgr().getFileInfoList();
		
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
					}
					temp = temp.getNextNode();
				}
			}
		}
	}
	
	// 선택된 트리의 노드를 반환
	public DefaultMutableTreeNode getSelectedNode() {
		return (DefaultMutableTreeNode) fileTree.getLastSelectedPathComponent();
	}
	
	// 파일트리에서 해당 노드의 정보를 반환
	public FileInfo getFileInfo(DefaultMutableTreeNode temp){
		FileInfo fileInfo = null;
		fileInfoList = ClientLauncher.getFileMgr().getFileInfoList();
		
		if(temp.getLevel() == 0) {
			return fileInfoList.get(0);
		}
		
		String name = temp.toString();
		String parent = temp.getParent().toString();
		String level = temp.getLevel()+"";
		
		for(FileInfo item : fileInfoList){
			if(name.equals(item.getName()) 
					&& parent.equals(item.getParent()) && level.equals(item.getDepth())) {
				return item;
			}
		}
		return fileInfo;
	}
	
	// 폴더의 경우 폴더 내부의 파일들 전체의 크기를 구해야 함
	public String getFolderSize(DefaultMutableTreeNode temp){
		long totalSize = sumChildFile(temp);
		return ClientLauncher.getFileMgr().getSummarySize(totalSize);
	}
	
	// 해당 폴더 내부의 모든 자식들의 크기를 더함
	public long sumChildFile(DefaultMutableTreeNode temp){
		long totalSize = 0;
		Enumeration child = temp.children();
		while(child.hasMoreElements()){
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) child.nextElement();
			if(node.toString().indexOf(".") != -1) {
				totalSize += Long.parseLong(getFileInfo(node).getSize());
			}
			else
				totalSize += sumChildFile(node);
		}
		return totalSize;
	}
	
	public String getSelectedPath(){
		String path="";
		for(int i=0;i<(selectedNode.getPath().length);i++){
			path += "/" + selectedNode.getPath()[i];
		}
		return path;
	}
	
	// Component 추가 및 제거를 반영하기 위한 새로고침
	public void changePanel() { 
		this.removeAll();
		fileMngPanel.initialize();
		this.add(scroll);
		this.add(btnGroupPanel);
		this.add(fileMngPanel);
		this.add(bgImg);
		this.repaint();
	}

	// 추가할 TreeNode가 존재하는지를 파악
	public boolean isExistNode(String node){
		for(int i=0;i<selectedNode.getChildCount();i++){
			if(selectedNode.getChildAt(i).toString().equals(node))
				return true;
		}
		return false;
	}

	public void create(String folderName){
		if (selectedNode == null)
			JOptionPane.showMessageDialog(null, "Choose a parent directory");
		else if(isExistNode(folderName)) {
			JOptionPane.showMessageDialog(null, folderName + " is already exist");
		}
		else{
			int type = Constants.PacketType.FolderCreateRequest.getType();
			String data = folderName + "\t" + selectedNode.toString() + "\t" + 
					((selectedNode.getLevel()+1)+"") + "\t" + ClientLauncher.getFileMgr().getRootDirID();
			int length = data.length();
			
			ClientLauncher.getConnector().sendPacket(type, 0, length, data);
		}
	}
	
	public void upload(String filePath, Long fileSize, MetaData Object){
		if (selectedNode == null)
			JOptionPane.showMessageDialog(null, "Choose a parent directory");
		else {
			int type = Constants.PacketType.FileUploadRequest.getType();
			String data = filePath + "\t" + selectedNode.toString() + "\t" + 
					((selectedNode.getLevel()+1)+"") + "\t" + 
					ClientLauncher.getFileMgr().getRootDirID() +"\t"+fileSize+"\t"+"defaultMetadata";
			int length = data.length();
			
			ClientLauncher.getConnector().sendPacket(type, 0, length, data);
		}
	}
	
	
	public void download()
	{
		if (selectedNode == null)
			JOptionPane.showMessageDialog(null, "Choose a file");
		else
		{
			int type;		 // 패킷 타입
			int length;	 // 패킷 길이
			String data; 	 // 전송 데이터
			FileInfo fileInfo = getFileInfo(selectedNode);
			
			type = Constants.PacketType.FileDownloadRequest.getType();
			data = fileInfo.getName() + "\t" + fileInfo.getParent() + "\t" + 
					fileInfo.getDepth() + "\t" + fileInfo.getIndex(); 	// 차후에 메타데이터도 같이 전송되도록 할 예정
			length = data.length();
			
			ClientLauncher.getConnector().sendPacket(type, 0, length, data);
		}
	}
	
	public void delete(){
		if (selectedNode == null)
			JOptionPane.showMessageDialog(null, "Choose a file or directory");
		else if( selectedNode == root )
			JOptionPane.showMessageDialog(null, "Can't delete root directory");
		else {
			//((DefaultTreeModel) model).removeNodeFromParent(selectedNode);
			FileInfo fileInfo = getFileInfo(selectedNode);
			System.out.println("name " + fileInfo.getName());
			System.out.println("parent " + fileInfo.getParent());
			System.out.println("level " + fileInfo.getDepth());
			System.out.println("index " + fileInfo.getIndex());
		}
	}

	// 버튼 이벤트, 마우스 이벤트 리스너 등록
	private class ActionHandler implements ActionListener{


		/* 	status
		0 : Information		1 : Selected		2 : Create	
		3 : Upload			4 : Download		5 : Delete 
		*/
		
		@Override
		public void actionPerformed(ActionEvent event) {

			selectedNode = getSelectedNode();
			
			if(selectedNode == null){
				JOptionPane.showMessageDialog(null, "Not Selected");
			}
			
			// Create
			else if(event.getSource()==btn[0]){
				if(selectedNode.toString().indexOf('.') == -1){
					fileMngPanel.setStatus(2);
					fileMngPanel.changePanel();
				}
				else
					JOptionPane.showMessageDialog(null, "Select create directory path");
			}
			// Upload
			else if(event.getSource()==btn[1]){
				if(selectedNode.toString().indexOf('.') == -1){
					fileMngPanel.setStatus(3);
					fileMngPanel.changePanel();
				}
				else
					JOptionPane.showMessageDialog(null, "Select upload directory path");
				
			}
			// Download
			else if(event.getSource()==btn[2]){
				fileMngPanel.setStatus(4);
				fileMngPanel.changePanel();
			}

			// Delete
			else if(event.getSource()==btn[3]){
				fileMngPanel.setStatus(5);
				fileMngPanel.changePanel();
			}
			
		}
	}
}