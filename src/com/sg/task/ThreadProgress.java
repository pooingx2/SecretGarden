package com.sg.task;

import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

import com.sg.main.ClientLauncher;
import com.sg.model.MetaData;

public class ThreadProgress  implements Runnable{

	private JProgressBar progressBar;
	private boolean runable;
	private Thread thread;


	public ThreadProgress(){
		
		this.thread = new Thread(this);
		this.runable = true;
		this.progressBar = new JProgressBar(0,100);
		this.progressBar.setStringPainted(true);
		
	}

	public JProgressBar getProgressBar() {
		return progressBar;
	}

	public void setProgressBar(JProgressBar progressBar) {
		this.progressBar = progressBar;
	}

	public boolean getRunable() {
		return runable;
	}

	public void setRunable(boolean runable) {
		this.runable = runable;
	}

	public Thread getThread() {
		return thread;
	}

	public void setThread(Thread thread) {
		this.thread = thread;
	}

	@Override
	public void run() {
		ClientLauncher.getTaskMgr().setRunning(true);
		while(runable){	
			
			long max = ClientLauncher.getTaskMgr().getRunningTask().getMax();
			long cur = ClientLauncher.getTaskMgr().getRunningTask().getCur();
			double percentage = (double)cur / (double)max * 100;
			
			if(ClientLauncher.getTaskMgr().getRunningTask().getType().equals("Upload")) {
				if(progressBar.getValue() >= 50) 
					progressBar.setValue( 50 + (int)percentage);
			}
			else{
				progressBar.setValue((int)percentage);
			}
			
			ClientLauncher.getFrame().getFileListPanel().getFileMngPanel().getProgressFrame().repaint();
				// TODO Auto-generated catch block
		}
		progressBar.setValue(100);
		progressBar.setString("Success");
		ClientLauncher.getFrame().getFileListPanel().getFileMngPanel().getProgressFrame().repaint();
	}

}
