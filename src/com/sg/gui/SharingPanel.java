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

	// Components
	private JLabel bgImg;
	private JScrollPane scroll[];
	private ActionHandler handler;
	private JButton btn[];
	private DefaultTableModel tableModel[];
	private JTable table[];
	private JTableHeader header;
	private DefaultTableCellRenderer renderer;
	private Vector<String> row;

	public SharingPanel(int w, int h) {

		super();
		boolean isAccessed = false;
		this.width = w;
		this.height = h;
		this.isSelected = false;
		this.setBackground(Constants.backColor);
		this.setLayout(null);

		// 배경이미지 등록
		bgImg = new JLabel(new ImageIcon(
				Constants.BackgroudPath.shareBG.getPath()));
		bgImg.setBounds(0, 0, width, height);

		// 이벤트 핸들러 등록
		handler = new ActionHandler();

		table = new JTable[2];
		scroll = new JScrollPane[2];
		tableModel = new DefaultTableModel[2];
		
		for(int i=0;i<2;i++){
			tableModel[i] = new DefaultTableModel() {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
			
			table[i] = new JTable(); 
			table[i].setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // row를 하나만
																			// 택하도록
																			// 설정
			table[i].setRowHeight(30); // row 높이 설정
			table[i].setFont(Constants.Font1); // table font 설정
			table[i].setModel(tableModel[i]); // table model 설정
	
			// columnSize 지정
//			table[i].setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//			table[i].getColumnModel().getColumn(0).setPreferredWidth(140);
//			table[i].getColumnModel().getColumn(1).setPreferredWidth(140);
	
			// row를 선택 리스너
			table[i].getSelectionModel().addListSelectionListener(
					new ListSelectionListener() {
						public void valueChanged(ListSelectionEvent event) {
							if (!event.getValueIsAdjusting()) {
								isSelected = true;
	//							table.getValueAt(table.getSelectedRow(), 0).toString();
								changePanel();
							}
						}
					});
	
			// header 관련 설정
			header = table[i].getTableHeader();
			header.setFont(Constants.Font2);
			header.setEnabled(false);
	
			// table cell 정렬
			renderer = new DefaultTableCellRenderer();
			renderer.setHorizontalAlignment(JLabel.CENTER);
			renderer.setFont(getFont().deriveFont(80f));
//			table[i].getColumnModel().getColumn(0).setCellRenderer(renderer);
//			table[i].getColumnModel().getColumn(1).setCellRenderer(renderer);
	
			// scroll 등록
			scroll[i] = new JScrollPane(table[i],
					ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			scroll[i].setBounds(50+i*400, 150, 300, 300);

			this.add(scroll[i]);
		}
		

		// table 헤더 설정
		tableModel[0].setColumnIdentifiers(new String[] { "    target ID ","      status" });
		tableModel[1].setColumnIdentifiers(new String[] { " requester ID","      status" });

		table[0].setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table[0].getColumnModel().getColumn(0).setPreferredWidth(140);
		table[0].getColumnModel().getColumn(1).setPreferredWidth(140);
		table[0].getColumnModel().getColumn(0).setCellRenderer(renderer);
		table[0].getColumnModel().getColumn(1).setCellRenderer(renderer);

		
		btn = new JButton[4];

		btn[0] = new JButton(new ImageIcon(
				Constants.ButtonPath.createBtn1.getPath()));
		btn[0].setRolloverIcon(new ImageIcon(Constants.ButtonPath.createBtn2
				.getPath()));
		// 버튼 생성 (Create, Access, Delte)

		btn[1] = new JButton(new ImageIcon(
				Constants.ButtonPath.accessBtn1.getPath()));
		btn[1].setRolloverIcon(new ImageIcon(Constants.ButtonPath.accessBtn2
				.getPath()));

		btn[2] = new JButton(new ImageIcon(
				Constants.ButtonPath.deleteBtn1.getPath()));
		btn[2].setRolloverIcon(new ImageIcon(Constants.ButtonPath.deleteBtn2
				.getPath()));

		btn[3] = new JButton(new ImageIcon(
				Constants.ButtonPath.settingsBtn1.getPath()));
		btn[3].setRolloverIcon(new ImageIcon(Constants.ButtonPath.settingsBtn2
				.getPath()));

		for (int i = 0; i < 4; i++) {
			btn[i].setBounds(11 + 70 * i, 5, 70, 70);
			btn[i].addActionListener(handler);
//			this.add(btn[i]);
		}

		this.add(bgImg);
	}

	public void initialize() {
	}

	// 리스트 초기화 함수
	// 디렉토리 삽입,삭제,파일 업로드,다운로드시에 리스트를 갱신해야한다
	public void initTable() {
		for (int i = tableModel[0].getRowCount() - 1; i > -1; i--) {
			tableModel[0].removeRow(i);
		}
		for (int i = tableModel[1].getRowCount() - 1; i > -1; i--) {
			tableModel[1].removeRow(i);
		}
	}

	// table row를 추가하는 함수
	public void addRow(Vector<String> row) {
		tableModel[0].addRow(row);
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
		this.add(scroll[0]);
		this.add(scroll[1]);
		this.add(bgImg);
		this.repaint();
	}

	// 버튼 이벤트, 마우스 이벤트 리스너 등록
	private class ActionHandler implements ActionListener, MouseListener {

		private String id;
		private String pwd;

		@Override
		public void actionPerformed(ActionEvent event) {

			if (event.getSource() == btn[0]) {
				changePanel();
			}

			if (event.getSource() == btn[1]) {
				
			}

			if (event.getSource() == btn[2]) {
				
			}

			if (event.getSource() == btn[3]) {

			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				if (e.getClickCount() == 2) {
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