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
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

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
	private Vector<String> data;
	private JPanel btnGroupPanel;
	private JButton btn[];
	private ActionHandler handler;
	private DirectoryMngPanel dirMngPanel;

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

		data = new Vector<>();

//		for(int i=0;i<30;i++){
//			data.addElement("Directory Name "+i);
//		}

		list = new JList<String>(data);
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
	}

	public void initialize() { }

	public boolean getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public void changePanel() { 
		this.remove(bgImg);
		dirMngPanel.changetPanel();
		this.add(dirMngPanel);
		this.add(bgImg);
		this.repaint();
	}
	
	public void create(String dir){
		data.addElement(dir);
		list.setSelectedIndex(data.size()-1);
	}
	
	public void access(){
		isSelected = false;
	}
	
	public void delete(){
		data.removeElementAt(list.getSelectedIndex());
		flag = false;
		list.setListData(data);
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
}

