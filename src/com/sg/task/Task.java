package com.sg.task;

import javax.swing.JProgressBar;

public class Task implements Runnable{
	
	private String type;
	private String fileName;
	private JProgressBar progressBar;
	private long max;
	private long cur;
	private boolean runable;
	
	private Thread task;
	
	public Task(){
		task = new Thread(this);
		progressBar = new JProgressBar(0,100);
		progressBar.setStringPainted(true);
		runable = true;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public long getMax() {
		return max;
	}

	public void setMax(long max) {
		this.max = max;
	}

	public long getCur() {
		return cur;
	}

	public void setCur(long cur) {
		this.cur = cur;
	}

	public Thread getTask() {
		return task;
	}


	public void setTask(Thread task) {
		this.task = task;
	}

	public void threadStart(){
		this.getTask().start();
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

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Task");
	}
	
}
