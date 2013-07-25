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

public class FileMgr {

	private String homePath;
	// file을 관리
	public FileMgr(){
		homePath = System.getProperty("user.home");
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
			path = file.getAbsolutePath();
			BufferedReader in = null;
			if(file != null) {
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
}
