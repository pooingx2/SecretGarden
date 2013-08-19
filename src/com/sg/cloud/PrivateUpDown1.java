package com.sg.cloud;
import java.io.File;
import java.io.IOException;

import com.sg.model.Files;


interface PrivateUpDown1 {
	public boolean auth();
	public int upload() throws IOException;
	public File download(Files request, String localPath) throws IOException;
	public boolean delete(Files request);
	public int upload(Files fileDescript, File targetFile) throws IOException;
	public int download() throws IOException;
}
