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
			getProgressBar().setValue((int)percentage);

			ClientLauncher.getFrame().getFileListPanel().getFileMngPanel().getProgressFrame().repaint();
				// TODO Auto-generated catch block
		}
	}

}
