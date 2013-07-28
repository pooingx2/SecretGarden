package com.sg.controller;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JFileChooser;

import com.sg.model.FileInfo;

public class FileMgr {

	private String homePath;
	private Vector<FileInfo> fileInfoList;
	private int maxDepth;		//fileInfoList의 maxDepth

	private String rootDirIndex;						//폴더들의 부모 디렉토리 인덱스.
														//디렉토리 내부의 폴더들의 루트 인덱스를 의미한다.
													    //디렉토리 내부에 아무런 폴더가없는 상태에서 폴더를 추가할시 참조한다.
	
	// file을 관리
	public FileMgr(){
		homePath = System.getProperty("user.home");
		fileInfoList = new Vector<FileInfo>();
		maxDepth = 0;
	}
	
	public Vector<FileInfo> getFileInfoList() {
		return fileInfoList;
	}

	public void setFileInfoList(Vector<FileInfo> fileInfoList) {
		this.fileInfoList = fileInfoList;
	}
	
	public int getMaxDepth() {
		return maxDepth;
	}

	public void setMaxDepth(int maxDepth) {
		this.maxDepth = maxDepth;
	}
	
	/* 최상위 루트 인덱스 */
	public String getRootDirID()
	{
		return rootDirIndex;
	}

	public void setRootDirID(String dirId) {
		this.rootDirIndex = dirId;
	}
	
	public void initFileInfo(){
		maxDepth = 0;
		fileInfoList.removeAllElements();
		fileInfoList.add(new FileInfo("folder","root"+rootDirIndex,"null","0",rootDirIndex));
		maxDepth = 0;
	}
	
	// file을 저장하기 위한 함수
	public void saveFile(String data){
		String path=null;
		JFileChooser fileDialog = new JFileChooser(new File(homePath));
		int isSelected = fileDialog.showSaveDialog(null);
		if(isSelected == JFileChooser.APPROVE_OPTION) {
			File file = fileDialog.getSelectedFile();
			path = file.getAbsolutePath();
			BufferedOutputStream out = null;
			if(file != null) {
				try{
					out = new BufferedOutputStream(new FileOutputStream(file));
					out.write(data.getBytes());
				}catch(FileNotFoundException e1){
					e1.printStackTrace();
				}catch(IOException e1){
					e1.printStackTrace();
				}finally{
					if(out!=null){
						try {
							out.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		}
	}

	// file을 load함 
	// return 	vector(0) : filePath
	//			vector(1) : key
	public Vector<String> loadKeyFile(){

		Vector<String> result = new Vector<String>();
		String path=null;
		String key="";
		String temp;
		JFileChooser fileDialog = new JFileChooser(new File(homePath));
		int isSelected = fileDialog.showOpenDialog(null);
		if(isSelected == JFileChooser.APPROVE_OPTION) {
			File file = fileDialog.getSelectedFile();
			BufferedReader in = null;
			if(file != null) {
				path = file.getAbsolutePath();
				try{
					in = new BufferedReader(new FileReader(file));
					while ((temp = in.readLine()) != null) {
						key += temp;
					}
				}catch(FileNotFoundException e1){
					e1.printStackTrace();
				}catch(IOException e1){
					e1.printStackTrace();
				}finally{
					if(in!=null){
						try {
							in.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		}
		result.add(path);
		result.add(key);
		
		return result;
	}
	
	public String loadUploadFile(){
		String path=null;
		JFileChooser fileDialog = new JFileChooser(new File(homePath));
		int isSelected = fileDialog.showOpenDialog(null);
		if(isSelected == JFileChooser.APPROVE_OPTION) {
			File file = fileDialog.getSelectedFile();
			BufferedReader in = null;
			if(file != null) {
				path = file.getAbsolutePath();
			}
		}
		return path;
	}
	
	
}
