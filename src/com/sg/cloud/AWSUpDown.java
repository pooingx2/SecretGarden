package com.sg.cloud;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JOptionPane;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.Region;
import com.amazonaws.services.s3.model.S3Object;
import com.sg.main.ClientLauncher;
import com.sg.model.Files;

public class AWSUpDown implements PublicUpDown{

	AmazonS3 s3;
	String keyId;
	String key;

	public AWSUpDown(){
		s3 = null;
	}
	

	public String getKeyId() {
		return keyId;
	}

	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	@Override
	public boolean auth(){
		return true;
	}
	@Override
	public boolean auth(String userId) {
		// TODO Auto-generated method stub
		//키 , 키아이디 입력
		s3 = new AmazonS3Client(new MyAWSCredentials(keyId, key));
		try {
			String bucketName = "secretgarden"+userId;
			if (s3.doesBucketExist(bucketName) != true) {
				System.out.println("create bucket : " + bucketName);
				System.out.println("This bucket is created when you connect AWS first by using this this program.");
				System.out.println("Don't delete this bucket arbitrary.");
				System.out.println("If you delete this bucket, you would lose your original data.");
				s3.createBucket(bucketName, Region.AP_Tokyo);
			}
		}catch (AmazonS3Exception e1){
			System.out.println("Check your AWS Access key and Secret key");
			JOptionPane.showMessageDialog(null, "Check your AWS Access key and Secret key");
			return false;
		}
		return true;
	}

	@Override
	public int upload(Files fileDescript, File targetFile) throws IOException {
		
		
		String bucketName = "secretgarden"+fileDescript.getUserId();			//입력 받아야 함. 키파일과 연결도 해야 함		
		String keyName = fileDescript.getDirPath() + fileDescript.getFileName();				 //입력 받아야 함. 버킷네임+고유id들 (디렉토리 패스 포함)
		
		
		try {
		
			System.out.println("Uploading a new object to S3 from a file\n");
			System.out.println(targetFile);

			s3.putObject(new PutObjectRequest(bucketName, keyName, targetFile));
			
			System.out.println("aws upload completed");

		} catch (AmazonServiceException ase) {
			System.out.println("Caught an AmazonServiceException, which means your request made it "
					+ "to Amazon S3, but was rejected with an error response for some reason.");
			System.out.println("Error Message:    " + ase.getMessage());
			System.out.println("HTTP Status Code: " + ase.getStatusCode());
			System.out.println("AWS Error Code:   " + ase.getErrorCode());
			System.out.println("Error Type:       " + ase.getErrorType());
			System.out.println("Request ID:       " + ase.getRequestId());
			return -1;
		} catch (AmazonClientException ace) {
			System.out.println("Caught an AmazonClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with S3, "
					+ "such as not being able to access the network.");
			System.out.println("Error Message: " + ace.getMessage());
			return -1;
		}

		return 0;

	}

	@Override
	public File download(Files request, String localPath) throws IOException {
		// TODO Auto-generated method stub
		//키파일 인증 포함
		
		
		String bucketName = "secretgarden" + request.getUserId();
		byte[] buf = new byte[10240];
		
		System.out.println(request.getDirPath()+request.getFileName());
		System.out.println("Downloading an object");
		
		/*tmp file들이 저장될 dir를 구하는 과정*/
		String streamPath;
		File workingDir = new File(".");
		if(!workingDir.exists()) 
			workingDir.mkdirs();
		String workingPath = workingDir.getCanonicalPath();
		streamPath = workingPath + "/tmp/" + "p/";
		File tmpFile = new File(streamPath + request.getFileName() + "._public");
		
//		File tmpDir = new File(localPath);
//		if(!tmpDir.exists()) 
//			tmpDir.mkdirs();
//		File tmpFile = new File(localPath+"fileAWS.tmp");
		
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(tmpFile));
		
		int totalBytesRead = 0;
		int bytesRead = 0;
		
		try {
			S3Object object = s3.getObject(new GetObjectRequest(bucketName, request.getDirPath()+request.getFileName()));
			System.out.println("Content-Type: "  + object.getObjectMetadata().getContentType());
			
			while (-1 != (bytesRead = object.getObjectContent().read(buf, 0, buf.length))) {
				totalBytesRead += bytesRead;
				System.out.println("rect AWS length : "+totalBytesRead);
				bos.write(buf, 0, bytesRead);
				ClientLauncher.getTaskMgr().getRunningTask().setCur(totalBytesRead);
				
			}
			System.out.println("AWS complete");
			bos.flush();
		} catch (IOException e) {
			e.printStackTrace();
			
		} catch(AmazonS3Exception a1){
			return null;
		}
		
		return tmpFile;
	}

	@Override
	public boolean delete(Files request) {
		//키파일 인증 후 메타데이터 변경 포함
		//디렉토리를 지
		String bucketName = "secretgarden" + request.getUserId();//입력 받아야 함. 키파일과 연결도 해야 함
//		String fileDir = getFileDir(request.getFileName());
		
		System.out.println("Deleting an object");
		System.out.println(request.getDirPath()+request.getFileName() + "\n");
		s3.deleteObject(bucketName, request.getDirPath()+request.getFileName());
				//+request.getFileName());

		
		return true;
	}
//	private String getFileDir(String fileName){
//		String fileDir;		
//		fileDir = fileName.substring(0, fileName.lastIndexOf("/")+1);
//		return fileDir;
//	}
	@Override
	public int upload() throws IOException {
		return 0;
	}
}
