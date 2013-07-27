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
import com.sg.model.MetaData;

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
	Vector<FileInfo> fileInfoList;
	
	/*******************************************************/
	/* JTree에 폴더 추가를 반영하기 위해 선택된 부모노드를 의미한다 */
	/* 추가되는 폴더 및 파일은 selectedNode의 자식으로 추가된다. */
	private DefaultMutableTreeNode selectedNode;
	String selected_node_Level;
	String selected_node_Name;
	String selected_node_root;
	/*******************************************************/
	
	/*******************************************************/
	/* JTree 에 추가되는 자식노드를 의미한다 */ 
	DefaultMutableTreeNode node;
	/*******************************************************/
	
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
		fileTree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener()
		{	
			@Override
			public void valueChanged(TreeSelectionEvent event) 
			{
				/* 노드 선택시 선택된 노드가 어떠한 노드인지 저장 */
				/* 폴더 펼쳤다 접었다 하는경우 오류 방지 및 폴더 생성시 관련 정보 참조를 위함 */
				//if(event.)
				if(getSelectedNode().equals(new DefaultMutableTreeNode()))
				{
					selectedNode = getSelectedNode();
				}
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
		root.removeAllChildren();
		fileTree.removeAll();
		changePanel();
		int maxDepth;
		
		fileInfoList = ClientLauncher.getFileMgr().getFileInfoList();
		maxDepth     = ClientLauncher.getFileMgr().getMaxDepth();
		
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
						System.out.println("add child " + node.toString() +" to " + temp.getParent() + " : "+ temp);
					}
					temp = temp.getNextNode();
				}
			}
		}
		changePanel();
	}
	
	public DefaultMutableTreeNode getSelectedNode()
	{
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

	public void create(String folderName){
		if (selectedNode == null)
			JOptionPane.showMessageDialog(null, "Choose a path");
		else{
			System.out.println("create");
			/* 폴더 생성 패킷 */
			
			// 변수 선언
			int type;		 // 패킷 타입
			int length;	 // 패킷 길이
			String data; 	 // 전송 데이터
			
			String name;
			String parent;
			String depth;
			String root;
			DefaultMutableTreeNode node;
			
			Vector<FileInfo> fileInfoList;
			FileInfo fileInfo =  new FileInfo();
			
			
			/* 이전에 선택된 폴더의 정보, 파일 및 폴더 추가를 위해 구성하였음 */
			/************************************************/
			System.out.println("selected node");
			
			selected_node_Level = Integer.toString(getSelectedNode().getLevel());
			selected_node_Name  = getSelectedNode().toString();
			selected_node_root  = ClientLauncher.getFileMgr().get_root_dir_index();
			
			System.out.println(
					  " level : " + selected_node_Level
					+ ", name : " + selected_node_Name 
					+ ", root dir index : " + selected_node_root );
			/************************************************/
			
			// 전송 데이터 셋팅
			name = folderName;
			parent = selected_node_Name;
			depth  = Integer.toString((Integer.parseInt(selected_node_Level) + 1));
			root   = selected_node_root ;
		
			type = Constants.PacketType.FolderCreateRequset.getType();
			data = name + "\t" + parent + "\t" + depth + "\t" + root;
			length = data.length();
			
			fileInfoList = ClientLauncher.getFileMgr().getFileInfoList();
			
			
			/************************************************/
			// 선택한 노드 밑에다 달아준다(테스트 용 함수) - 서버와 같이 수정한후 Packet Manager 위치로 옴길예정
			node = new DefaultMutableTreeNode(name);
			selectedNode.add(node);
			
			// 생성된 폴더를 fileInfo리스트에 추가한다.
			// 차후에 폴더설정의 변경시 참조해야 하는 데이터
			System.out.println(fileInfoList.size());
			fileInfo.setType("folder");
			fileInfo.setName(name);
			fileInfo.setParent(parent);
			fileInfo.setDepth(depth);
			fileInfo.setIndex(root);
			
			fileInfoList.add(fileInfo);
			System.out.println(fileInfoList.size());
			/************************************************/
			
			// 폴더 생성 요청 패킷을 전송
			ClientLauncher.getConnector().sendPacket(type, 0, length, data);
			changePanel();
		}
	}
	
	public void upload(String fileName, MetaData Object){
		if (selectedNode == null)
			JOptionPane.showMessageDialog(null, "Choose a path");
		else
		{
			System.out.println("upload");
			
			/* !!!파일전송 및 메타데이터 생성이 정상적으로 수행될경우에 메타데이터를 업로드 한다!!! */
			
			// 변수 선언
			int type;		 // 패킷 타입
			int length;	 // 패킷 길이
			String data; 	 // 전송 데이터
			
			String name;
			String parent;
			String depth;
			String root;
			DefaultMutableTreeNode node;
			
			Vector<FileInfo> fileInfoList;
			FileInfo fileInfo =  new FileInfo();
			
			/* 노드 선택시 발생하는 이벤트, 파일 및 폴더 추가를 위해 구성하였음 */
			/************************************************/
			System.out.print("selected node : ");
			
			selected_node_Level = Integer.toString(getSelectedNode().getLevel());
			selected_node_Name  = getSelectedNode().toString();
			selected_node_root  = ClientLauncher.getFileMgr().get_root_dir_index();
			
			System.out.println(
					  "level : " + selected_node_Level
					+ ", name : " + selected_node_Name 
					+ ", root dir index : " + selected_node_root );
			
			

			fileInfoList = ClientLauncher.getFileMgr().getFileInfoList();
			/************************************************/
			
			
			// 전송 데이터 셋팅
			name 	= fileName;
			parent = selected_node_Name;
			depth  = Integer.toString((Integer.parseInt(selected_node_Level) + 1));
			root   = selected_node_root ;
		
			type = Constants.PacketType.FileCreateRequset.getType();
			data = name + "\t" + parent + "\t" + depth + "\t" + root + "\t" + Object.getdata() ; // 차후에 메타데이터도 같이 전송되도록 할 예정
			length = data.length();
			
			
			/************************************************/
			// 선택한 노드 밑에다 달아준다(테스트 용 함수) - 서버와 같이 수정한후 Packet Manager 위치로 옴길예정
			node = new DefaultMutableTreeNode(name);
			selectedNode.add(node);
			
			// 생성된 폴더를 fileInfo리스트에 추가한다.
			// 차후에 폴더설정의 변경시 참조해야 하는 데이터
			System.out.println(fileInfoList.size());
			fileInfo.setType("file");
			fileInfo.setName(name);
			fileInfo.setParent(parent);
			fileInfo.setDepth(depth);
			fileInfo.setIndex(root);
			
			fileInfoList.add(fileInfo);
			System.out.println(fileInfoList.size());
			/************************************************/
			
			// 파일 생성 요청 패킷을 전송
			ClientLauncher.getConnector().sendPacket(type, 0, length, data);
			changePanel();
		}
	}
	
	
	public void download()
	{
		if (selectedNode == null)
			JOptionPane.showMessageDialog(null, "Choose a file");
		else
		{
			System.out.println("download");
			// 무엇을 어디서 다운로드 할 것인가?ㄴ
		}
	}
	
	public void delete(){
		if (selectedNode == null)
			JOptionPane.showMessageDialog(null, "Choose a file or directory");
			
		else if( selectedNode == root )
			JOptionPane.showMessageDialog(null, "Can't delete root directory");
		else
			((DefaultTreeModel) model).removeNodeFromParent(selectedNode);
	}
	
	public void file_to_cloud()
	{
		
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