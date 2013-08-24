package com.sg.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.sg.main.ClientLauncher;
import com.sg.main.Constants;

public class SharingPanel extends JPanel {

	// Attribute
	private int width;
	private int height;
	private boolean isSelected;
	private boolean editMode;
	// 0 : Default	1 : Requester	2 : Target
	private String selectedInfo;
	

	// Components
	private JLabel bgImg;
	private JScrollPane scroll;
	private ActionHandler handler;
	private JButton btn[];
	private DefaultTableModel tableModel;
	private JTable table;
	private JTableHeader header;
	private DefaultTableCellRenderer renderer;
	private Vector<String> row;
	private ImageIcon shareTo[];
	private ImageIcon shareFrom[];

	public SharingPanel(int w, int h) {

		super();
		this.width = w;
		this.height = h;
		this.isSelected = false;
		this.setBackground(Constants.backColor);
		this.setLayout(null);
		this.selectedInfo = "";

		// 배경이미지 등록
		bgImg = new JLabel(new ImageIcon(Constants.BackgroudPath.shareListBG.getPath()));
		bgImg.setBounds(0, 0, width, height);

		// Sharing status icon
        shareTo = new ImageIcon[3];
        shareTo[0] = new ImageIcon(Constants.IconPath.shareToIcon1.getPath());	// waiting
        shareTo[1] = new ImageIcon(Constants.IconPath.shareToIcon2.getPath());	// refused
        shareTo[2] = new ImageIcon(Constants.IconPath.shareToIcon3.getPath());	// sharing
        
        shareFrom = new ImageIcon[3];
        shareFrom[0] = new ImageIcon(Constants.IconPath.shareFromIcon1.getPath());
        shareFrom[1] = new ImageIcon(Constants.IconPath.shareFromIcon2.getPath());
        shareFrom[2] = new ImageIcon(Constants.IconPath.shareFromIcon3.getPath());
        

		// 이벤트 핸들러 등록
		handler = new ActionHandler();

		tableModel = new DefaultTableModel(){
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		// table 헤더 설정 (UserID, dirName,  Status, Requester OR Target, ShareId, dirID)
		tableModel.setColumnIdentifiers(new String[] {"    User ID", "    Directory", "  Status","", "",""});
		
//		table = new JTable();
		table = new JTable(){
            //  Returning the Class of each column will allow different
            //  renderers to be used based on Class
            public Class getColumnClass(int column){
                return getValueAt(0, column).getClass();
            }
        };
        
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // row를 하나만 택하도록 설정
		table.setRowHeight(30); // row 높이 설정
		table.setFont(Constants.Font1); // table font 설정
		table.setModel(tableModel); // table model 설정

		// columnSize 지정
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getColumnModel().getColumn(0).setPreferredWidth(130);
		table.getColumnModel().getColumn(1).setPreferredWidth(150);
		table.getColumnModel().getColumn(2).setPreferredWidth(100);
		
		// hide column
		for (int i=3 ;i<6;i++){
			table.getColumnModel().getColumn(i).setPreferredWidth(0);
			table.getColumnModel().getColumn(i).setMinWidth(0);
			table.getColumnModel().getColumn(i).setMaxWidth(0);
		}
		
		// row를 선택 리스너
		table.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent event) {
						if (!event.getValueIsAdjusting() && !editMode) {
							isSelected = true;
							selectedInfo = "";
							if(table.getValueAt(table.getSelectedRow(), 2).equals(shareTo[0])
									|| table.getValueAt(table.getSelectedRow(), 2).equals(shareFrom[0])){
								initialize();
								if(table.getValueAt(table.getSelectedRow(), 3).toString().equals("Target")){
									selectedInfo = "Target";
								}
								else if(table.getValueAt(table.getSelectedRow(), 3).toString().equals("Requester")){
									selectedInfo = "Requester";
								}
							}
							changePanel();
						}
					}
				});

		// header 관련 설정
		header = table.getTableHeader();
		header.setFont(Constants.Font2);
		header.setEnabled(false);

		// table cell 정렬
		renderer = new DefaultTableCellRenderer();
		renderer.setHorizontalAlignment(JLabel.CENTER);
		renderer.setFont(getFont().deriveFont(80f));
		table.getColumnModel().getColumn(0).setCellRenderer(renderer);
		table.getColumnModel().getColumn(1).setCellRenderer(renderer);

		// scroll 등록
		scroll = new JScrollPane(table,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setBounds(200, 200, 400, 250);
      
		btn = new JButton[4];

		btn[0] = new JButton(new ImageIcon(Constants.ButtonPath.confirmBtn1.getPath()));
		btn[0].setRolloverIcon(new ImageIcon(Constants.ButtonPath.confirmBtn2.getPath()));
		

		btn[1] = new JButton(new ImageIcon(Constants.ButtonPath.cancelBtn1.getPath()));
		btn[1].setRolloverIcon(new ImageIcon(Constants.ButtonPath.cancelBtn2.getPath()));

		for (int i = 0; i < 2; i++) {
			btn[i].setBounds(420 +(i*100), 160, 80, 30);
			btn[i].addActionListener(handler);
		}

		this.add(scroll);
		this.add(bgImg);
	}

	public void initialize() {
		this.editMode = true;
		this.isSelected = false;
		this.selectedInfo = "";
//		this.remove(btn[0]);
//		this.remove(btn[1]);
//		this.repaint();
		changePanel();
		this.editMode = false;
	}

	// 리스트 초기화 함수
	public void initTable() {
		this.editMode = true;
		for (int i = tableModel.getRowCount() - 1; i > -1; i--) {
			tableModel.removeRow(i);
		}
		this.editMode = false;
	}

	// table row를 추가하는 함수
	public void addRow(Vector<String> row) {
		
		this.editMode = true;
		Object[] rowData = new Object[6];
		rowData[0] = row.get(2);	// User ID
		rowData[1] = row.get(5);	// DirName
		rowData[2] = row.get(3);	// Status
		rowData[3] = row.get(0);	// Target or Requester
		rowData[4] = row.get(1);	// Share ID
		rowData[5] = row.get(4);	// Directory ID
		
		if(rowData[3].equals("Target")) {
			if(rowData[2].equals("waiting"))
				rowData[2] = shareFrom[0];
			else if(rowData[2].equals("refused"))
				rowData[2] = shareFrom[1];
			else if(rowData[2].equals("sharing"))
				rowData[2] = shareFrom[2];
		}
		
		if(rowData[3].equals("Requester")) {
			if(rowData[2].equals("waiting"))
				rowData[2] = shareTo[0];
			else if(rowData[2].equals("refused"))
				rowData[2] = shareTo[1];
			else if(rowData[2].equals("sharing"))
				rowData[2] = shareTo[2];
		}
		
		tableModel.addRow(rowData);
		this.editMode = false;
	}

	// row가 등록 되었는지 상태를 나타냄
	public boolean getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	// Component 추가 및 제거를 반영하기 위한 새로고침
	public void changePanel() {
		this.removeAll();
		this.add(scroll);
		if(selectedInfo.equals("Requester")){
			this.add(btn[1]);
		}
		else if(selectedInfo.equals("Target")){
			this.add(btn[0]);
			this.add(btn[1]);
		}
		this.add(bgImg);
		this.repaint();
	}

	// 버튼 이벤트, 마우스 이벤트 리스너 등록
	private class ActionHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent event) {

			String shareId = table.getValueAt(table.getSelectedRow(),4).toString();
			// 공유를 수락한 경우
			if (event.getSource() == btn[0]) {
				String data = shareId +"\t" + ClientLauncher.getUser().getId();
				
				// share ID와 User ID를 보냄
				int type = Constants.PacketType.SharingConfirmRequest.getType();
				int length = data.length();

				ClientLauncher.getConnector().sendPacket(type, 0, length, data);
			}

			if (event.getSource() == btn[1]) {
				// 공유를 거절한 경우
				if(selectedInfo.equals("Target")){
					String data = shareId +"\t" + ClientLauncher.getUser().getId();
					
					// share ID와 User ID를 보냄
					int type = Constants.PacketType.SharingRefuseRequest.getType();
					int length = data.length();

					ClientLauncher.getConnector().sendPacket(type, 0, length, data);

				}
				// 공유를 취소한 경우
				else if(selectedInfo.equals("Requester")){
					String data = shareId +"\t" + ClientLauncher.getUser().getId();
					
					// share ID와 User ID를 보냄
					int type = Constants.PacketType.SharingCancelRequest.getType();
					int length = data.length();

					ClientLauncher.getConnector().sendPacket(type, 0, length, data);
				}
			}
		}
	}
}