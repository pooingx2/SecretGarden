package com.sg.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.LineBorder;

import com.sg.main.Constants;


public class DirectoryListPanel extends JPanel {
	// Attribute
	private int width;
	private int height;

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
		this.setBackground(Constants.backColor);
		this.setLayout(null);

		bgImg = new JLabel(new ImageIcon(Constants.BackgroudPath.directoryListBG.getPath()));
		bgImg.setBounds(0,0,width,height);
		
		data = new Vector<>();
		
		for(int i=0;i<30;i++){
			data.addElement("Directory Name "+i);
		}

		list = new JList<String>(data);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setFixedCellHeight(30);
		list.setFont(Constants.Font2);
		


		scroll = new JScrollPane(list,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setBounds(50, 100, 300, 360);

		btnGroupPanel = new JPanel();
		btnGroupPanel.setBounds(450,80,300,80);
		btnGroupPanel.setBackground(Constants.backColor);
		btnGroupPanel.setLayout(null);
		
		btn = new JButton[3];
		btn[0] = new JButton(new ImageIcon(Constants.ButtonPath.createBtn1.getPath()));
		btn[0].setRolloverIcon(new ImageIcon(Constants.ButtonPath.createBtn2.getPath()));

		btn[1] = new JButton(new ImageIcon(Constants.ButtonPath.accessBtn1.getPath()));
		btn[1].setRolloverIcon(new ImageIcon(Constants.ButtonPath.accessBtn2.getPath()));

		btn[2] = new JButton(new ImageIcon(Constants.ButtonPath.deleteBtn1.getPath()));
		btn[2].setRolloverIcon(new ImageIcon(Constants.ButtonPath.deleteBtn2.getPath()));
		
		handler = new ActionHandler();
		
		for(int i=0;i<3;i++) {
			btn[i].setBounds(15+100*i,5,70,70);
			btn[i].addActionListener(handler);
			btnGroupPanel.add(btn[i]);
		}
		
		dirMngPanel = new DirectoryMngPanel(300,300);
		dirMngPanel.setBounds(450,180,300,300);
		
		this.add(scroll);
		this.add(btnGroupPanel);
		this.add(dirMngPanel);
		this.add(bgImg);
	}

	public void initialize() { }
	
	public void changePanel() { 
		this.remove(bgImg);
		dirMngPanel.changetPanel();
		this.add(dirMngPanel);
		this.add(bgImg);
		this.repaint();
	}

	private class ActionHandler implements ActionListener {
		private String id;
		private String pwd;

		@Override
		public void actionPerformed(ActionEvent event) {

			if(event.getSource()==btn[0]){
				dirMngPanel.setStatus(2);
				changePanel();
			}

			if(event.getSource()==btn[1]){
				dirMngPanel.setStatus(3);
				changePanel();
			}

			if(event.getSource()==btn[2]){
				dirMngPanel.setStatus(4);
				changePanel();
			}
		}
	}
}

