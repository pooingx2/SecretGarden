package com.sg.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.sg.main.ClientLauncher;
import com.sg.main.Constants;
import com.sg.task.Task;

public class ProgressFrame extends JFrame  {
	
	private int width;
	private int height;
	private boolean isSelected;
	private boolean editMode;
	
	private JLabel bgImg;
	private Font inputFont;
	private JScrollPane scroll;
	private DefaultTableModel tableModel;
	private JTable table;
	private JTableHeader header;
	private DefaultTableCellRenderer renderer;
	private JTextField textField;
	
	// 회원 가입 frame
	public ProgressFrame(int w, int h) {
		
		super();
		
		// lookAndFeel을 통해 OS에 맞는 UI제공
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.updateComponentTreeUI(this);
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		this.width = w;
		this.height = h;
		this.isSelected = false;
		
		// 화면 크기를 구함
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		
		this.setSize(width,height);
		this.setResizable(false);
		
		// 화면 중앙에 frame이 오도록 설정
		this.setLocation(screen.width/2-this.width/2, screen.height/2-this.height/2);

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e){
				initialize();
			}
		});
		
		this.setLayout(null);
		
		// 배경이미지
		bgImg = new JLabel(new ImageIcon(Constants.BackgroudPath.taskProgressBG.getPath()));
		bgImg.setBounds(0,0,width,height);

		// font 설정
		inputFont = Constants.Font1;
		
		// User ID TextFiled
		textField = new JTextField();
		textField.setBounds(180,70,300,30);
		textField.setFont(inputFont);
		textField.setText(ClientLauncher.getFileMgr().getDownloadPath());
		textField.setEditable(false);
		this.add(textField);
		
		tableModel = new DefaultTableModel(){
			@Override
			public boolean isCellEditable(int row, int column) {
				if(column == 3) return true;				
				else return false;
			}
		};
		


		// table 헤더 설정
		tableModel.setColumnIdentifiers(new String[] { " Task", "      File Name", "  Progress bar",""});

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
		table.getColumnModel().getColumn(0).setPreferredWidth(70);
		table.getColumnModel().getColumn(1).setPreferredWidth(170);
		table.getColumnModel().getColumn(2).setPreferredWidth(160);
		table.getColumnModel().getColumn(3).setPreferredWidth(40);

		
		// row를 선택 리스너
		table.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent event) {
						if (!event.getValueIsAdjusting() && !editMode) {
							isSelected = true;
							/*
							if(table.getValueAt(table.getSelectedRow(), 2).equals(shareTo[0])
									|| table.getValueAt(table.getSelectedRow(), 2).equals(shareFrom[0])){
								initialize();

							}
							*/
						}
					}
				});

		// header 관련 설정
		header = table.getTableHeader();
		header.setFont(Constants.Font2);
		header.setEnabled(false);
		
		// table cell 정렬
		renderer = new DefaultTableCellRenderer();
		ButtonRenderer btnRenderer = new ButtonRenderer(table, tableModel);
		renderer.setHorizontalAlignment(JLabel.CENTER);
		table.getColumnModel().getColumn(0).setCellRenderer(renderer);
		table.getColumnModel().getColumn(1).setCellRenderer(renderer);
		table.getColumnModel().getColumn(2).setCellRenderer(new ProgressRenderer());
		table.getColumnModel().getColumn(3).setCellRenderer(btnRenderer);
		table.getColumnModel().getColumn(3).setCellEditor(btnRenderer);
		scroll = new JScrollPane(table,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setBounds(20,110,460,300);
		
		this.add(scroll);
		this.add(bgImg);
	}
	
	public JTextField getTextField() {
		return textField;
	}

	public void setTextField(JTextField textField) {
		this.textField = textField;
	}
	
	public DefaultTableModel getTableModel() {
		return tableModel;
	}

	public void setTableModel(DefaultTableModel tableModel) {
		this.tableModel = tableModel;
	}
	
	// form 초기화
	public void initialize(){
		this.editMode = true;
		this.isSelected = false;
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
	
	// 해당 row를 지우는 함수
	public void removeRow(int row){
		this.editMode = true;
		this.tableModel.removeRow(row);
		this.editMode = false;
	}
	
	// 선택된 row를 반환하는 함수
	public int getSelectedRow(){
		return this.table.getSelectedRow();
	}

	// table row를 추가하는 함수
	public void addRow(Task task) {
		
		this.editMode = true;
		
		Object[] rowData = new Object[4];
		rowData[0] = task.getType();
		rowData[1] = task.getFileName();
		rowData[2] = task.getThProgress().getProgressBar();
		rowData[3] = "";
		
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

}
