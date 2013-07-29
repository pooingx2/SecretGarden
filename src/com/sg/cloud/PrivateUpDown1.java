package com.sg.cloud;
import java.io.File;
import java.io.IOException;


interface PrivateUpDown1 {
	public boolean auth();
	public int upload() throws IOException;
	public byte[] download(String sourcePath, String userId, String fileName) throws IOException;
	public int deleteFile();
	public int upload(String fileName, String userId, File targetFile, String dirPath) throws IOException;
	public int download() throws IOException;
}
