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

public class DirectoryListPanel extends JPanel {

	// Attribute
	private int width;
	private int height;
	private boolean isSelected;
	private boolean isAccessed;
	private boolean editMode;

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
	private DefaultTableCellRenderer renderer;
	private Vector<String> row;

	public DirectoryListPanel(int w, int h) {

		super();
		this.width = w;
		this.height = h;
		this.isSelected = false;
		this.isAccessed = false;
		this.setBackground(Constants.backColor);
		this.setLayout(null);

		// 배경이미지 등록
		bgImg = new JLabel(new ImageIcon(this.getClass().getResource(
				Constants.BackgroudPath.directoryListBG.getPath())));
		bgImg.setBounds(0, 0, width, height);

		// 이벤트 핸들러 등록
		handler = new ActionHandler();

		// 해당셀을 수정할 수 없도록 설정
		tableModel = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		// table 헤더 설정
		tableModel.setColumnIdentifiers(new String[] { "   ID",
				"     Directory Name" });

		table = new JTable(); // directory list를 위한 table을 만듬
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // row를 하나만
																		// 택하도록
																		// 설정
		table.setRowHeight(30); // row 높이 설정
		table.setFont(Constants.Font1); // table font 설정
		table.setModel(tableModel); // table model 설정

		// columnSize 지정
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getColumnModel().getColumn(0).setPreferredWidth(60);
		table.getColumnModel().getColumn(1).setPreferredWidth(220);

		// row를 선택 리스너
		table.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent event) {
						if (!event.getValueIsAdjusting() && !editMode) {
							isSelected = true;
							dirMngPanel.setStatus(1);
							if (dirMngPanel.getStatus() == 1) {
								dirMngPanel.getLabel()[0].setText(table.getValueAt(table.getSelectedRow(), 0).toString());
								dirMngPanel.getLabel()[1].setText(table.getValueAt(table.getSelectedRow(), 1).toString());
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
		scroll.setBounds(50, 100, 300, 360);

		// 버튼 그룹 패널 생성
		btnGroupPanel = new JPanel();
		btnGroupPanel.setBounds(450, 100, 300, 80);
		btnGroupPanel.setBackground(Constants.backColor);
		btnGroupPanel.setLayout(null);

		// 버튼 생성 (Create, Access, Delte)
		btn = new JButton[4];

		btn[0] = new JButton(new ImageIcon(this.getClass().getResource(Constants.ButtonPath.createBtn1.getPath())));
		btn[0].setRolloverIcon(new ImageIcon(this.getClass().getResource(Constants.ButtonPath.createBtn2.getPath())));

		btn[1] = new JButton(new ImageIcon(this.getClass().getResource(Constants.ButtonPath.accessBtn1.getPath())));
		btn[1].setRolloverIcon(new ImageIcon(this.getClass().getResource(Constants.ButtonPath.accessBtn2.getPath())));

		btn[2] = new JButton(new ImageIcon(this.getClass().getResource(Constants.ButtonPath.deleteBtn1.getPath())));
		btn[2].setRolloverIcon(new ImageIcon(this.getClass().getResource(Constants.ButtonPath.deleteBtn2.getPath())));

		btn[3] = new JButton(new ImageIcon(this.getClass().getResource(Constants.ButtonPath.settingsBtn1.getPath())));
		btn[3].setRolloverIcon(new ImageIcon(this.getClass().getResource(Constants.ButtonPath.settingsBtn2.getPath())));

		for (int i = 0; i < 4; i++) {
			btn[i].setBounds(11 + 70 * i, 5, 70, 70);
			btn[i].addActionListener(handler);
			btnGroupPanel.add(btn[i]);
		}

		// Directory를 관리할 수 있는 패널 등록
		dirMngPanel = new DirectoryMngPanel(300, 250);
		dirMngPanel.setBounds(450, 200, 300, 250);

		this.add(scroll);
		this.add(btnGroupPanel);
		this.add(dirMngPanel);
		this.add(bgImg);
	}

	public void initialize() {
		this.editMode = true;
		this.isSelected = false;
		this.isAccessed = false;
		dirMngPanel.initialize();
		table.clearSelection();
		this.editMode = false;
	}

	// 리스트 초기화 함수
	// 디렉토리 삽입,삭제,파일 업로드,다운로드시에 리스트를 갱신해야한다
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
		tableModel.addRow(row);
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
		this.remove(bgImg);
		dirMngPanel.changePanel();
		this.add(scroll);
		this.add(dirMngPanel);
		this.add(bgImg);
		this.repaint();
	}

	// 생성 클릭시 실행
	public void create(String dir) {
		
		if (isAccessed == false) {
			int type; // 패킷 타입
			int length; // 패킷 길이
			String data; // 전송 데이터
			String id; // 디렉토리 indext
			String private_cloud; // 클라우드 연결 정보
			String public_cloud;

			private_cloud = ClientLauncher.getFrame().getConnectionPanel()
					.getPrivate();
			public_cloud = ClientLauncher.getFrame().getConnectionPanel()
					.getPublic();
			id = ClientLauncher.getFrame().getLoginPanel().getId();

			data = dir + "\t" + private_cloud + "\t" + public_cloud + "\t" + id;
			type = Constants.PacketType.DirectoryCreateRequest.getType();
			length = data.length();

			// 디렉토리 생성 요청 패킷을 전송
			ClientLauncher.getConnector().sendPacket(type, 0, length, data);

			// 추가된 Directory를 포함한 디렉토리 정보를 반영하기 위해 조회 패킷 전송
			data = id + "\t" + private_cloud + "\t" + public_cloud;
			type = Constants.PacketType.DirectoryListRequest.getType();
			length = data.length();

			// 디렉토리 리시트 조회 요청 패킷 전송
			ClientLauncher.getConnector().sendPacket(type, 0, length, data);

			// 디렉토리 생성을 반영하기 위한 패널 새로고침
			changePanel();
		}
	}

	// 디렉토리 접근 최종 확인 버튼 클릭시 실행
	public void access(String key) {
		// 액세스시 폴더 리스트를 가져와야 한다
		isAccessed = true;
		
		// 하부 폴더에 접근하기 위하여 Keyfile을 서버로 전송한다 
		String id   = ClientLauncher.getFrame().getDirectoryListPanel().getDirectoryID();
		String data = id + "\t" + "BCFA3232E97F";
		
		int type = Constants.PacketType.DirectoryAccessRequest.getType();
		int length = data.length();
		
		// 인증 및 폴더 리스트 요청
		ClientLauncher.getFileMgr().setRootDirID(id);
		ClientLauncher.getConnector().sendPacket(type, 0, length, data);
	}

	// 디렉토리 삭제 최종 확인 클릭시 실행
	public void delete() {
	}

	public void settings() {
	}

	public String getDirectoryID() {
		return table.getValueAt(table.getSelectedRow(), 0).toString();
	}

	// 버튼 이벤트, 마우스 이벤트 리스너 등록
	private class ActionHandler implements ActionListener {

		private String id;
		private String pwd;

		@Override
		public void actionPerformed(ActionEvent event) {

			if (event.getSource() == btn[0]) {
				dirMngPanel.setStatus(2);
				changePanel();
			}

			if (event.getSource() == btn[1]) {
				if (isSelected) {
					dirMngPanel.setStatus(3);
					changePanel();
				} else {
					JOptionPane.showMessageDialog(null,
							"For Access : Choose a directory");
					initialize();
				}
			}

			if (event.getSource() == btn[2]) {

				if (isSelected) {
					dirMngPanel.setStatus(4);
					changePanel();
				}

				else {
					JOptionPane.showMessageDialog(null,
							"For Delete : Choose a directory");
					initialize();
				}
			}

			if (event.getSource() == btn[3]) {
				if (isSelected) {
					dirMngPanel.setStatus(5);
					changePanel();
				}

				else {
					JOptionPane.showMessageDialog(null,
							"For Setting : Choose a directory");
					initialize();
				}
			}
		}
	}
}