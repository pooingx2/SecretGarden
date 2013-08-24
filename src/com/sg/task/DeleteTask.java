package com.sg.task;

import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import com.sg.main.ClientLauncher;

public class DeleteTask extends Task{
	
	private String localPath;
	private String selectedPath;
	
	public DeleteTask(){
		super();
	}
	
	public DeleteTask(String localPath, String selectedPath, long fileSize){

		super();
		this.setType("Delete");
		this.localPath = localPath;
		this.selectedPath = selectedPath;
		this.setMax(fileSize);
	}

	@Override
	public void run() {
		ClientLauncher.getTaskMgr().setRunning(true);
		System.out.println("Delete Task Start");
		int i = getProgressBar().getValue();
		while(i < 100){
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

		System.out.println("Delete Task End");
		ClientLauncher.getTaskMgr().endTask();
	}
	
}
