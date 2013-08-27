package com.sg.task;

import java.io.IOException;

import javax.swing.JOptionPane;

import com.sg.main.ClientLauncher;

public class DeleteTask extends Task{
	
	private String localPath;
	private String selectedPath;
	
	public DeleteTask(){
		super();
	}
	
	public DeleteTask(String selectedPath, String localPath, long fileSize){

		super();
		this.setType("Delete");
		this.localPath = localPath;
		this.selectedPath = selectedPath;
		this.setMax(fileSize);
	}

	@Override
	public void run() {

		System.out.println("Delete Task Start");
		
		ClientLauncher.getTaskMgr().setRunning(true);
		getThProgress().getThread().start();
		
		try {
			// return 0 = success failure = -1
			if (ClientLauncher.getHybrid().delete(selectedPath) == 0) {
				ClientLauncher.getFrame().getFileListPanel().delete();
			} else
				JOptionPane.showMessageDialog(null, "delete failure");
		} catch (IOException e) {
			e.printStackTrace();
		}

		ClientLauncher.getTaskMgr().endTask();
		System.out.println("Delete Task End");
	}
	
}
