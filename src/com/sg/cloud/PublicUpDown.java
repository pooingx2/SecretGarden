package com.sg.cloud;

import java.io.File;
import java.io.IOException;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;


interface PublicUpDown {
	
	public boolean auth();
	public int upload() throws IOException;
	public byte[] download( String sourceFile, String userId);
	public int deleteFile();
	public int upload(String fileName, String userId, File targetFile, String dirPath) throws IOException;

}
