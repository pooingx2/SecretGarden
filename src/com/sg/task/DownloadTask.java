package com.sg.task;

import java.io.IOException;

import javax.swing.JOptionPane;

import com.sg.main.ClientLauncher;
import com.sg.model.FileInfo;

public class DownloadTask extends Task{
	
	private String localPath;
	private String selectedPath;
	private FileInfo fileInfo;
		
	public DownloadTask(){
		super();
	}
	
	public DownloadTask(String selectedPath, String localPath, long fileSize, FileInfo fileInfo){
		
		super();
		this.setType("Download");
		this.localPath = localPath;
		this.selectedPath = selectedPath;
		this.setMax(fileSize);
		this.fileInfo = fileInfo;
	}


	@Override
	public void run() {

		System.out.println("Download Task Start");
		ClientLauncher.getFrame().getFileListPanel().download(fileInfo);
		ClientLauncher.getTaskMgr().setRunning(true);
		getThProgress().getThread().start();
		
		try {
			// return 0 = success	failure = -1
			if(ClientLauncher.getHybrid().download(selectedPath, localPath) == 0){
				
			}
			else
				JOptionPane.showMessageDialog(null, "download failure");
		} catch (IOException e) {
			e.printStackTrace();
		}

		ClientLauncher.getTaskMgr().endTask();
		System.out.println("Download Task End");
	}
	
}
