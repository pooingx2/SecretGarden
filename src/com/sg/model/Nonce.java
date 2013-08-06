package com.sg.model;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;

public class Nonce {
	
	private Random random;
	private String nonce;
	private Date date;
	private Timestamp stamp;

	
	public Nonce() {
		// 64 bit 정수형 랜덤 난수 절대값 생성
		random = new Random();
		nonce = Long.toString(Math.abs(random.nextLong()));
		
		// timestamp를 찍음
		date= new Date();
		stamp = new Timestamp(date.getTime());
	}

	public Random getRandom() {
		return random;
	}

	public void setRandom(Random random) {
		this.random = random;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Timestamp getStamp() {
		return stamp;
	}

	public void setStamp(Timestamp stamp) {
		this.stamp = stamp;
	}

	public String getNonce() {
		return nonce;
	}

	public void setNonce(String nonce) {
		this.nonce = nonce;
	}
	
	
}