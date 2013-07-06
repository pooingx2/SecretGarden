package com.sg.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import com.sg.main.ClientLauncher;
import com.sg.main.Constants;


public class DirectoryMngPanel extends JPanel {
	// Attribute
	private int width;
	private int height;
	
	/* 	status
	0 : Default		1 : Information		2 : Create	
	3 : Access			4: Delete	*/
	
	private int status;
	private File file;
	private String filePath;

	// Components
	private Font inputFont;
	private JLabel bgImg[];
	private JLabel label[];
	private JTextField textField[];
	private JButton btn[];
	private ActionHandler handler;

	public DirectoryMngPanel(int w, int h) {

		super();
		this.width = w;
		this.height = h;
		this.status = 0;
		this.file = null;
		this.filePath = null;
		this.setLayout(null);
		this.setBackground(Constants.backColor);
		this.setBorder(new LineBorder(Color.LIGHT_GRAY));

		bgImg = new JLabel[5];
		bgImg[0] = new JLabel();
		bgImg[1] = new JLabel(new ImageIcon(Constants.BackgroudPath.directoryMngBG1.getPath()));
		bgImg[2] = new JLabel(new ImageIcon(Constants.BackgroudPath.directoryMngBG2.getPath()));
		bgImg[3] = new JLabel(new ImageIcon(Constants.BackgroudPath.directoryMngBG3.getPath()));
		bgImg[4] = new JLabel(new ImageIcon(Constants.BackgroudPath.directoryMngBG4.getPath()));

		for(int i=0;i<5;i++) {
			bgImg[i].setBounds(1,1,width-2,height-2);
		}

		inputFont = Constants.Font1;

		label = new JLabel[2];
		textField = new JTextField[2];
		btn = new JButton[3];

		handler = new ActionHandler();

		label[0] = new JLabel("Drectory Name");
		label[0].setBounds(20,100,250,30);
		label[0].setFont(inputFont);

		label[1] = new JLabel();
		label[1].setBounds(20,20,300,30);
		label[1].setFont(Constants.Font2);

		textField[0] = new JTextField();		// directory name
		textField[0].setBounds(20,140,200,30);
		textField[0].setFont(inputFont);

		textField[1] = new JTextField();		// file path
		textField[1].setBounds(20,140,250,30);
		textField[1].setEditable(false);
		textField[1].setFont(inputFont);

		handler = new ActionHandler();

		btn[0] = new JButton(new ImageIcon(Constants.ButtonPath.loadKeyfileBtn1.getPath()));
		btn[0].setRolloverIcon(new ImageIcon(Constants.ButtonPath.loadKeyfileBtn2.getPath()));
		btn[0].setBounds(20,100,100,30);
		btn[0].addActionListener(handler);

		btn[1] = new JButton(new ImageIcon(Constants.ButtonPath.confirmBtn1.getPath()));
		btn[1].setRolloverIcon(new ImageIcon(Constants.ButtonPath.confirmBtn2.getPath()));
		btn[1].setBounds(100,200,80,30);
		btn[1].addActionListener(handler);

		btn[2] = new JButton(new ImageIcon(Constants.ButtonPath.cancelBtn1.getPath()));
		btn[2].setRolloverIcon(new ImageIcon(Constants.ButtonPath.cancelBtn2.getPath()));
		btn[2].setBounds(200,200,80,30);
		btn[2].addActionListener(handler);

		this.add(bgImg[1]);
	}


	public void initialize() { }

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setSelectedValue(String value){
		label[1].setText(value);
	}
	public void selectkeyFile(){
		JFileChooser fileDialog = new JFileChooser(new File("."));
		int isSelected = fileDialog.showOpenDialog(null);
		if(isSelected == JFileChooser.APPROVE_OPTION) {
			file = fileDialog.getSelectedFile();
			filePath = file.getAbsolutePath();
			if(file != null) {
				textField[1].setText(filePath);
			}
		}
	}

	public void changetPanel() {

		this.removeAll();
		switch(this.status){
		case 0 : 	// Default
			this.add(label[1]);
			break;
		case 1 : 	// Information
			break;
		case 2 : 	// Create 
			this.add(label[0]);
			this.add(textField[0]);
			this.add(btn[1]);
			this.add(btn[2]);
			break;
		case 3 : 	// Access
			this.add(textField[1]);
			this.add(btn[0]);
			this.add(btn[1]);
			this.add(btn[2]);
			break;
		case 4 : 	//	Delete
			this.add(btn[1]);
			this.add(btn[2]);
			break;
		default : 
			break;
		}
		this.add(bgImg[status]);
		this.repaint();
	}

	private class ActionHandler implements ActionListener {
		private String dirName;

		@Override
		public void actionPerformed(ActionEvent event) {

			if(event.getSource()==btn[0]){
				selectkeyFile();
			}

			if(event.getSource()==btn[1]){
				dirName=textField[0].getText();
				if(status == 2) {
					ClientLauncher.getFrame().getDirectoryListPanel().create(dirName);
				}
				else if(status == 3) {
					ClientLauncher.getFrame().getDirectoryListPanel().access();
				}
				else if(status == 4) {
					ClientLauncher.getFrame().getDirectoryListPanel().delete();
				}
				btn[2].doClick();
			}

			if(event.getSource()==btn[2]){
				textField[0].setText("");
				textField[1].setText("");
				if(ClientLauncher.getFrame().getDirectoryListPanel().getIsSelected()){
					setStatus(0);
				}
				else {
					label[1].setText("");
					setStatus(1);
				}
				changetPanel();
			}
		}
	}
}

