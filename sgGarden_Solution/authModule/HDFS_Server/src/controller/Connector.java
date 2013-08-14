package controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import main.ClientLauncher;
import main.Constants;

public class Connector implements Runnable {
	Socket socket;
	DataInputStream dis;
	DataOutputStream dos;
	Thread client;
	boolean runable;
	PacketMgr pkMgr;

	// 서버와 연결을 담당
	public Connector() {
		pkMgr = ClientLauncher.getPkMgr();
		System.out.println("pkMgr Create");

		try {
			socket = new Socket(Constants.serverIP, Constants.serverPort);
			dis = new DataInputStream(socket.getInputStream());
			dos = new DataOutputStream(socket.getOutputStream());
			
			runable = true;
			
			// 패킷을 받기 위한 스레드
			client = new Thread(this);
			client.start();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public DataInputStream getDis() {
		return dis;
	}

	public DataOutputStream getDos() {
		return dos;
	}

	// 패킷을 서버로 전송하는 함수 (C 서버와 통신을 위해 byte 변환)
	public void sendPacket(int type, int redesc, int length, String data) {
		byte[] sendbuf = new byte[length + 12];

		byte[] bytes = new byte[] { 
				(byte) (type >>> 24), (byte) (type >>> 16),
				(byte) (type >>> 8), (byte) (type >>> 0),
				
				(byte) (redesc >>> 24), (byte) (redesc >>> 16),
				(byte) (redesc >>> 8), (byte) (redesc >>> 0),

				(byte) (length >>> 24), (byte) (length >>> 16),
				(byte) (length >>> 8), (byte) (length >>> 0) };

		byte out[];
		data.trim();
		out = data.getBytes();

		int i = 0;
		for (i = 0; i < 12; i++) {
			sendbuf[i] = bytes[i];
		}

		for (i = 12; i < length + 12; i++) {
			sendbuf[i] = out[i - 12];
		}

		try {
			dos.write(sendbuf, 0, length + 12);
			System.out.println("전송 데이터 길이 : " + i);
			System.out.println("전송 데이터 타입 : " + type);
			System.out.println("전송 데이터 디스크립터 : " + redesc);
			System.out.println("전송 데이터 : " + data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 서버로부터 받은 패킷을 재정의하여 처리 하는 함수 (byte를 원하는 정보 형태로 가공)
	public void recvPacket(byte[] buf) {
		byte[] headerBuf = new byte[12];

		int n1, n2, n3, n4;
		int h_length = 0;
		int type = 0;
		int desc = 0;
		int length = 0;
		try {
			h_length = dis.read(headerBuf, 0, 12);
			
			if(h_length == 0){
				System.out.println("수신데이터 제로 ");
				return;
			}
			
			if (h_length != 0) {
				n1 = (headerBuf[0] & (int) 0xFF) << 24;
				n2 = (headerBuf[1] & (int) 0xFF) << 16;
				n3 = (headerBuf[2] & (int) 0xFF) << 8;
				n4 = (headerBuf[3] & (int) 0xFF);

				type = n1 + n2 + n3 + n4;

				n1 = (headerBuf[4] & (int) 0xFF) << 24;
				n2 = (headerBuf[5] & (int) 0xFF) << 16;
				n3 = (headerBuf[6] & (int) 0xFF) << 8;
				n4 = (headerBuf[7] & (int) 0xFF);

				desc = n1 + n2 + n3 + n4;

				n1 = (headerBuf[8] & (int) 0xFF) << 24;
				n2 = (headerBuf[9] & (int) 0xFF) << 16;
				n3 = (headerBuf[10] & (int) 0xFF) << 8;
				n4 = (headerBuf[11] & (int) 0xFF);

				length = n1 + n2 + n3 + n4;
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// return null;

		System.out.println("수신 데이터 타입 : " + type);
		System.out.println("수신 데이터 디스크립터 : " + desc);
		System.out.println("수신 데이터 길이: " + length);

		byte[] dataBuf = new byte[length];
		try {

			if (length != 0)
			{
				dis.read(dataBuf);
				String data = new String(dataBuf);
				System.out.println("수신 데이터 : " + data);
				pkMgr.managePacket(type, desc, length, data);
			} 
			else
			{
				String data = new String();
				System.out.println("수신 데이터 : NULL ");
				pkMgr.managePacket(type, desc, length, "");
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 서버로 패킷 헤더를 전송하는 함수 (C 서버와 통신을 위해 byte 변환)
	public void sendHeader(int type, int redesc, int length) {

		byte[] bytes = new byte[] { (byte) (type >>> 24), (byte) (type >>> 16),
				(byte) (type >>> 8), (byte) (type >>> 0),
				(byte) (redesc >>> 24), (byte) (redesc >>> 16),
				(byte) (redesc >>> 8), (byte) (redesc >>> 0),
				(byte) (length >>> 24), (byte) (length >>> 16),
				(byte) (length >>> 8), (byte) (length >>> 0) };

		try {
			// dos.write(bytes);
			dos.write(bytes, 0, 12);
			dos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendHeader(int type, int length) {
		byte[] bytes = new byte[] { (byte) (type >>> 24), (byte) (type >>> 16),
				(byte) (type >>> 8), (byte) (type >>> 0),
				(byte) (length >>> 24), (byte) (length >>> 16),
				(byte) (length >>> 8), (byte) (length >>> 0) };
		try {
			dos.write(bytes);
			dos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 서버로 패킷 데이터를 전송하는 함수 (C 서버와 통신을 위해 byte 변환)
	public void sendData(String data) {
		byte out[];
		data.trim();
		out = data.getBytes();

		try {
			dos.write(out);
			dos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// 서버로부터 받은 헤더 패킷을 재정의하는 함수(byte를 원하는 정보 형태로 가공)
	public int[] receiveHeader() {
		byte[] headerBuf = new byte[8];
		int n1, n2, n3, n4;
		int type;
		int length;
		try {
			dis.read(headerBuf);

			n1 = (headerBuf[0] & (int) 0xFF) << 24;
			n2 = (headerBuf[1] & (int) 0xFF) << 16;
			n3 = (headerBuf[2] & (int) 0xFF) << 8;
			n4 = (headerBuf[3] & (int) 0xFF);

			type = n1 + n2 + n3 + n4;

			n1 = (headerBuf[4] & (int) 0xFF) << 24;
			n2 = (headerBuf[5] & (int) 0xFF) << 16;
			n3 = (headerBuf[6] & (int) 0xFF) << 8;
			n4 = (headerBuf[7] & (int) 0xFF);

			length = n1 + n2 + n3 + n4;

			return new int[] { type, length };

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	// 서버로부터 받은 패킷 데이터를 재정의하여 처리 하는 함수 (byte를 원하는 정보 형태로 가공)
	public void receiveData(int type, int length) {
		byte[] dataBuf = new byte[length];
		try {
			dis.read(dataBuf);
			String data = new String(dataBuf);
			pkMgr.managePacket(type, 0, length, data);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void receiveData(String data) {

	}

	// 프로그램 종료 패킷이 들어오면 수행
	public void disconnect() {
		runable = false;
		String data = "EXIT";
		int type = Constants.PacketType.PROGRAM_EXIT_REQUEST.getType();
		int length = data.length();
			
		ClientLauncher.getConnector().sendPacket(type, 0, length, data);
	}

	// 패킷 정보를 받기 위한 스레드
	@Override
	public void run() {

		System.out.println("Test");
		while (runable) {
			byte[] header = null;

			System.out.println("recv before");
			recvPacket(header);
			System.out.println("recv after");
				
		}
		
		System.out.println("while end");
	}
	
}
