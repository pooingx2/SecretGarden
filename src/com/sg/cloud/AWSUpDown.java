package com.sg.cloud;

import java.io.File;
import java.io.IOException;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

public class AWSUpDown implements PublicUpDown{

	AmazonS3 s3;
	String keyId;
	String key;
	String userId;

	public AWSUpDown(){
		s3 = null;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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
	public boolean auth() {
		// TODO Auto-generated method stub
		//키 , 키아이디 입력
		s3 = new AmazonS3Client(new MyAWSCredentials(keyId, key));
				
		return true;
	}

	@Override
	public int upload(String fileName, String userId, File targetFile, String dirPath) throws IOException {
		// TODO Auto-generated method stub
		//버킷 만들고 키파일 설정 포함
		//파일 업로드 ㄱㄱ...
		String bucketName = "secretgarden"+userId;//입력 받아야 함. 키파일과 연결도 해야 함		
		String keyName = dirPath + fileName;					 //입력 받아야 함. 버킷네임+고유id들 (디렉토리 패스 포함)

		//System.out.println("Creating bucket " + bucketName + "\n");
		try {
			if (s3.doesBucketExist(bucketName) != true) {
				System.out.println("create bucket : " + bucketName);
				System.out.println("This bucket is created when you first use this program.");
				System.out.println("Don't delete this bucket arbitrary.");
				System.out.println("If you delete this bucket, you would lose your original data.");
				s3.createBucket(bucketName);
			}
			System.out.println("Uploading a new object to S3 from a file\n");
			s3.putObject(new PutObjectRequest(bucketName, keyName, targetFile));

		} catch (AmazonServiceException ase) {
			System.out.println("Caught an AmazonServiceException, which means your request made it "
					+ "to Amazon S3, but was rejected with an error response for some reason.");
			System.out.println("Error Message:    " + ase.getMessage());
			System.out.println("HTTP Status Code: " + ase.getStatusCode());
			System.out.println("AWS Error Code:   " + ase.getErrorCode());
			System.out.println("Error Type:       " + ase.getErrorType());
			System.out.println("Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			System.out.println("Caught an AmazonClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with S3, "
					+ "such as not being able to access the network.");
			System.out.println("Error Message: " + ace.getMessage());
		}

		return 0;

	}

	@Override
	public byte[] download(String sourceFile, String userId) {
		// TODO Auto-generated method stub
		//키파일 인증 포함
		String bucketName = "secretgarden" + userId;
		byte[] downBuf = new byte[10];

		System.out.println("Downloading an object");
		S3Object object = s3.getObject(new GetObjectRequest(bucketName, sourceFile));
		System.out.println("Content-Type: "  + object.getObjectMetadata().getContentType());
		//버퍼 저장 후 file로 저장
		try {
			object.getObjectContent().read(downBuf);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return downBuf;
	}

	@Override
	public int deleteFile() {
		// TODO Auto-generated method stub움
		//키파일 인증 후 메타데이터 변경 포함
		//디렉토리를 지
		String bucketName = "jqpglnzgno";//입력 받아야 함. 키파일과 연결도 해야 함
		String key = "jqpglnzgnofile"; //입력 받아야 함. 버킷네임+고유id들

		System.out.println("Deleting an object\n");
		s3.deleteObject(bucketName, key);

		System.out.println("Deleting bucket " + bucketName + "\n");
		s3.deleteBucket(bucketName);
		return 0;
	}

	@Override
	public int upload() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}
}
