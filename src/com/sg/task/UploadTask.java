package com.sg.task;

import java.io.IOException;

import javax.swing.JOptionPane;

import com.sg.main.ClientLauncher;
import com.sg.model.MetaData;

public class UploadTask extends Task{
	
	private String localPath;
	private String selectedPath;
	
	public UploadTask(){
		super();
	}
	
	public UploadTask(String localPath, String selectedPath, long fileSize){

		super();
		this.setType("Upload");
		this.localPath = localPath;
		this.selectedPath = selectedPath;
		this.setMax(fileSize);
	}

	@Override
	public void run() {
		ClientLauncher.getTaskMgr().setRunning(true);
		
		System.out.println("Upload Task Start");
		
		try {	
			if(ClientLauncher.getHybrid().upload(localPath, selectedPath) == 0){
				MetaData m_data = new MetaData();
				ClientLauncher.getFrame().getFileListPanel().upload(localPath, getMax(), m_data);
			}
			else
				JOptionPane.showMessageDialog(null, "upload failure");

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		int i = getProgressBar().getValue();
		while(i < 100 && getRunable()){
			try {
				int value = getProgressBar().getValue();
				getProgressBar().setValue(value + 1);
				ClientLauncher.getFrame().getFileListPanel().getFileMngPanel().getProgressFrame().repaint();
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			i = getProgressBar().getValue();
		}

		System.out.println("Upload Task End");
		ClientLauncher.getTaskMgr().endTask();
	}
	
}
