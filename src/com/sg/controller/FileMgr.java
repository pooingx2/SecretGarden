package com.sg.controller;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.sg.main.Constants;
import com.sg.model.FileInfo;

public class FileMgr {

	private String homePath;
	private String downloadPath;
	private String slash;
	private Vector<FileInfo> fileInfoList;
	private int maxDepth;		//fileInfoList의 maxDepth
	private String rootDirIndex;						//폴더들의 부모 디렉토리 인덱스.
														//디렉토리 내부의 폴더들의 루트 인덱스를 의미한다.
													    //디렉토리 내부에 아무런 폴더가없는 상태에서 폴더를 추가할시 참조한다.
	
	// file을 관리
	public FileMgr(){
		homePath = System.getProperty("user.home");
		setSlash();
		downloadPath = homePath+slash+"SecretGarden";
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
	
	public String getHomePath() {
		return homePath;
	}

	public void setHomePath(String homePath) {
		this.homePath = homePath;
	}

	public void setDownloadPath(String downloadPath) {
		this.downloadPath = downloadPath;
	}

	public String getDownloadPath() {
		return downloadPath;
	}
	

	public String getRootDirIndex() {
		return rootDirIndex;
	}

	public void setRootDirIndex(String rootDirIndex) {
		this.rootDirIndex = rootDirIndex;
	}
	
	public String getSlash() {
		return slash;
	}

	public void setSlash() {
		if(System.getProperty("os.name").contains("Window"))
			this.slash = "\\";
		else
			this.slash = "/";
	}

	public void init(){
		maxDepth = 0;
		fileInfoList.removeAllElements();
		fileInfoList.add(new FileInfo("folder","root"+rootDirIndex,"null","0",rootDirIndex));
	}
	
	// file을 저장하기 위한 함수
	public String saveKeyFile(String data){
		String path=null;
		
		JFileChooser fileDialog = new JFileChooser(new File(homePath));
		int isSelected = fileDialog.showSaveDialog(null);
		if(isSelected == JFileChooser.APPROVE_OPTION) {
			File file = fileDialog.getSelectedFile();
			path = file.getAbsolutePath()+".key";
			BufferedOutputStream out = null;

			if(file != null) {
				try{
					out = new BufferedOutputStream(new FileOutputStream(path));
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
		return path;
	}

	// key file을 load함 
	// return 	vector(0) : filePath
	//			vector(1) : key
	public Vector<String> loadKeyFile(){

		Vector<String> result = new Vector<String>();
		String path=null;
		String key="";
		String temp;
		JFileChooser fileDialog = new JFileChooser(new File(homePath));
		fileDialog.setFileFilter(new FileNameExtensionFilter("Key Files","key"));
		
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
	
	// upload할 file path를 return
	public File[] loadUploadFile(){
		File[] files = null;
		JFileChooser fileDialog = new JFileChooser(new File(homePath));
		fileDialog.setMultiSelectionEnabled(true);
		int isSelected = fileDialog.showOpenDialog(null);
		if(isSelected == JFileChooser.APPROVE_OPTION) {
			files = fileDialog.getSelectedFiles();
		}
		return files;
	}

	public void addFileInfo(String fileData) {
		
		// 수신한 데이터를 FileInfo List에 저장한다. (index,dirName \t index,dirName ...)
		// (type,name,parent,depth,index \t type,name,parent,depth,index ... )
		
		StringTokenizer tokenizer;
		String token[];
		FileInfo fileInfo;
		
		token= new String[100];
		fileInfo = new FileInfo();
		tokenizer = new StringTokenizer(fileData,",");
		
		int i = 0;
		while(tokenizer.hasMoreTokens()) {
			token[i] = tokenizer.nextToken();
			switch(i){
			case 0 : 
				fileInfo.setType(token[i]); 
				break;
			case 1 : 
				fileInfo.setName(token[i]); 
				break;
			case 2 : 
				fileInfo.setParent(token[i]); 
				break;
			case 3 : 
				fileInfo.setDepth(token[i]); 
				break;
			case 4 : 
				fileInfo.setIndex(token[i]); 
				break;
			case 5 : 
				fileInfo.setSize(token[i]); 
				break;
			default : 
				break;
			}
			i++;
		}
		fileInfoList.add(fileInfo);
	}

	// long 형의 size를 KB, MB, TB로 변환
	public String getSummarySize(long size) {
		long temp = size;
		String suffix = "";
		int count = 0;
		while(temp >= 1024){
			temp = temp/1024;
			count++;
		}
		switch(count){
			case 0 : 
				suffix = "Byte";
				break;
			case 1 : 
				suffix = "KB";
				break;
			case 2 : 
				suffix = "MB";
				break;
			case 3 : 
				suffix = "GB";
				break;
			case 4 : 
				suffix = "TB";
				break;
			default : 
				break;
		}
		return temp+suffix;
	}

}
