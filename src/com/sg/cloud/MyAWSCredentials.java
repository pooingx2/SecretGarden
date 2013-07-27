package com.sg.cloud;

import com.amazonaws.auth.AWSCredentials;


public class MyAWSCredentials implements AWSCredentials {

	private String AWSAccessKeyId;
	private String AWSAccessKey;
	
	public MyAWSCredentials(String keyId, String key){
		this.AWSAccessKeyId = keyId;
		this.AWSAccessKey = key;
	}
	
	public MyAWSCredentials() {		
	}

	public void setAWSAccessKeyId(String aWSAccessKeyId) {
		AWSAccessKeyId = aWSAccessKeyId;
	}

	public void setAWSAccessKey(String aWSAccessKey) {
		AWSAccessKey = aWSAccessKey;
	}

	@Override
	public String getAWSAccessKeyId() {
		// TODO Auto-generated method stub
		return AWSAccessKeyId;
	}

	@Override
	public String getAWSSecretKey() {
		// TODO Auto-generated method stub
		return AWSAccessKey;
	}

}
