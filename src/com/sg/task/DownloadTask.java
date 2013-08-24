package com.sg.task;

import com.sg.main.ClientLauncher;

public class DownloadTask extends Task{
	
	private String localPath;
	private String selectedPath;
		
	public DownloadTask(){
		super();
	}
	
	public DownloadTask(String selectedPath, String localPath, long fileSize){
		
		super();
		this.setType("Download");
		this.localPath = localPath;
		this.selectedPath = selectedPath;
		this.setMax(fileSize);
	}

	@Override
	public void run() {
		ClientLauncher.getTaskMgr().setRunning(true);
		System.out.println("Download Task Start");
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

		System.out.println("Download Task End");
		ClientLauncher.getTaskMgr().endTask();
	}
	
}
