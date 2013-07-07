package com.sg.controller;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class KeyFileMgr {

	String homePath;

	public KeyFileMgr(){
		homePath =  System.getProperty ( "user.home" ); 
	}

	public void createKeyFile(String dir){
		// Create RSA keyPair
		KeyPairGenerator kpg;
		try {
			kpg = KeyPairGenerator.getInstance("RSA");
			kpg.initialize(1024);

			KeyPair keyPair = kpg.generateKeyPair();
			PublicKey publicKey = keyPair.getPublic();
			PrivateKey privateKey = keyPair.getPrivate();

//			System.out.println(publicKey);
//			System.out.println(privateKey);

			// Save Public Key:
			X509EncodedKeySpec x509ks = new X509EncodedKeySpec(publicKey.getEncoded());
			FileOutputStream fos1 = new FileOutputStream(homePath+"/"+dir+".pub");
			fos1.write(x509ks.getEncoded());

			// Save Private Key:
			PKCS8EncodedKeySpec pkcsKeySpec = new PKCS8EncodedKeySpec(privateKey.getEncoded());
			FileOutputStream fos2 = new FileOutputStream(homePath+"/"+dir+".pri");
			fos2.write(pkcsKeySpec.getEncoded());

			fos1.flush();
			fos1.close();
			fos2.flush();
			fos2.close();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
