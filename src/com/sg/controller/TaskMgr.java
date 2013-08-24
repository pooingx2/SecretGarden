package com.sg.controller;

import java.util.Queue;
import java.util.Vector;

import com.sg.main.ClientLauncher;
import com.sg.task.Task;


public class TaskMgr {
//	private Queue<Task> taskList;
	private Vector<Task> taskList;
	private boolean running;
	private Task runningTask;
	private int runningIndex;
	
	public TaskMgr() {
		taskList = new Vector<Task>();
		runningTask = null;
		runningIndex = 0;
		running = false;
	}

	public Vector<Task> getTaskList() {
		return taskList;
	}

	public void setTaskList(Vector<Task> taskList) {
		this.taskList = taskList;
	}
	
	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
	
	public Task getRunningTask() {
		return runningTask;
	}

	public void setRunningTask(Task runningTask) {
		this.runningTask = runningTask;
	}
	
	public int getRunningIndex() {
		return runningIndex;
	}

	public void setRunningIndex(int runningIndex) {
		this.runningIndex = runningIndex;
	}

	public void stopRunningTask(){
		runningTask.setRunable(false);
	}
	
	public void remove(int i) {
		// 삭제하는 task가 running 중인경우 멈추고 thread 종료
		if(i == runningIndex) {
			stopRunningTask();
			taskList.get(i).setRunable(false);
			runningIndex--;
		}
		
		// 실행되는 task 리스트의 index 보다 작은 삭제시 runningIndex 1 감소하고 삭제
		else if(i <= runningIndex)
			runningIndex--;

		taskList.remove(i);
	}
	
	public void endTask() {
		runningIndex++;
		running = false;
		nextStart();
	}
	
	public void nextStart() {
		
		// 큐가 비어있으면 리턴 아무것도 안함
		if(taskList.size() == 0) {
			runningTask = null;
			runningIndex = 0;
			running = false;
			return;
		}
		// 큐가 비어있지 않은 경우
		else {
			// 실행중인 task가 없으면 큐의 first element를 수행 (critical section)
			if(running) {
				// 실행중인 task가 있으면 리턴
				return;
			}
			else {
//				runningTask = taskList.poll();
				if( runningIndex < taskList.size()){
					runningTask = taskList.get(runningIndex);
					runningTask.threadStart();
				}
			}
		}
	}
	
	public void addTask(Task task) {
		// Queue에 task를 추가
//		taskList.offer(task);
		taskList.add(task);
		// 실행 할 수 있는지 판단 후 실행
		nextStart();
	}
	
	
}
