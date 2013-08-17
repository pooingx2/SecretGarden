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
	private JScrollPane scroll;
	private ActionHandler handler;
	private JButton btn[];
	private DefaultTableModel tableModel;
	private JTable table;
	private JTableHeader header;
	private DefaultTableCellRenderer renderer;
	private Vector<String> row;

	public SharingPanel(int w, int h) {

		super();
		this.width = w;
		this.height = h;
		this.isSelected = false;
		this.setBackground(Constants.backColor);
		this.setLayout(null);

		// 배경이미지 등록
		bgImg = new JLabel(new ImageIcon(Constants.BackgroudPath.sharingBG.getPath()));
		bgImg.setBounds(0, 0, width, height);

		// 이벤트 핸들러 등록
		handler = new ActionHandler();

		table = new JTable();
		scroll = new JScrollPane();
		tableModel = new DefaultTableModel(){


			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		table = new JTable(); 
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // row를 하나만
		// 택하도록
		// 설정
		table.setRowHeight(30); // row 높이 설정
		table.setFont(Constants.Font1); // table font 설정
		table.setModel(tableModel); // table model 설정

		// row를 선택 리스너
		table.getSelectionModel().addListSelectionListener(
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
		header = table.getTableHeader();
		header.setFont(Constants.Font2);
		header.setEnabled(false);

		// table cell 정렬
		renderer = new DefaultTableCellRenderer();
		renderer.setHorizontalAlignment(JLabel.CENTER);
		renderer.setFont(getFont().deriveFont(80f));

		// scroll 등록
		scroll = new JScrollPane(table,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setBounds(260, 170, 300, 250);

		// table 헤더 설정
		tableModel.setColumnIdentifiers(new String[] { "    User ID ","      status" });

		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getColumnModel().getColumn(0).setPreferredWidth(140);
		table.getColumnModel().getColumn(1).setPreferredWidth(140);
		table.getColumnModel().getColumn(0).setCellRenderer(renderer);
		table.getColumnModel().getColumn(1).setCellRenderer(renderer);

		btn = new JButton[4];

		btn[0] = new JButton(new ImageIcon(Constants.ButtonPath.confirmBtn1.getPath()));
		btn[0].setRolloverIcon(new ImageIcon(Constants.ButtonPath.confirmBtn2.getPath()));
		// 버튼 생성 (Create, Access, Delte)

		btn[1] = new JButton(new ImageIcon(Constants.ButtonPath.cancelBtn1.getPath()));
		btn[1].setRolloverIcon(new ImageIcon(Constants.ButtonPath.cancelBtn2.getPath()));

		for (int i = 0; i < 2; i++) {
			btn[i].setBounds(380 + 90 * i, 430, 80, 30);
			btn[i].addActionListener(handler);
//			this.add(btn[i]);
		}

		this.add(scroll);
		this.add(bgImg);
	}



	public void initialize() {
		this.remove(btn[0]);
		this.remove(btn[1]);
		this.repaint();
	}

	// 리스트 초기화 함수
	public void initTable() {
		for (int i = tableModel.getRowCount() - 1; i > -1; i--) {
			tableModel.removeRow(i);
		}
	}

	// table row를 추가하는 함수
	public void addRow(Vector<String> row) {
		tableModel.addRow(row);
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
		this.add(scroll);
		this.add(btn[0]);
		this.add(btn[1]);
		this.add(bgImg);
		this.repaint();
	}

	// 버튼 이벤트, 마우스 이벤트 리스너 등록
	private class ActionHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent event) {

			if (event.getSource() == btn[0]) {
				
			}

			if (event.getSource() == btn[1]) {

			}
		}
	}
}