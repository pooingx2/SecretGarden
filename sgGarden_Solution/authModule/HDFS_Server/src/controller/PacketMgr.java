package controller;

import java.io.FileNotFoundException;
import java.util.StringTokenizer;
import java.util.Vector;



import main.ClientLauncher;
import main.Constants;


public class PacketMgr {
	private StringTokenizer tokenizer;
	private String token[];
	
	public PacketMgr()
	{
		
	}
	
	// 패킷 정보를 tocken으로 쪼개고 각 type에 맞는 기능을 처리
	public void managePacket(int type, int desc ,int length, String data) throws FileNotFoundException{
		
		// packet data를 쪼갬
		System.out.println("패킷 관리자 생성");
		tokenizer = new StringTokenizer(data,",");
		token 	   = new String[length];
		int i =0;

		System.out.println("type : " + type);
		System.out.println("desc : " + desc);
		System.out.println("length : " + length);
		System.out.println("data : " + data);
		
		// token에 저장
		while(tokenizer.hasMoreTokens()) 
		{
			token[i] = tokenizer.nextToken();
			i++;
		}
		
		/* 
		 * 
		 * token[0] : 메타 데이터
		 * token[1] : 디렉토리 id
		 * token[2] : 폴더 패쓰
		 * token[3] : 클라이언트 소켓
		 * 
		 */
		
		// 메타데이터 업로드 처리
		if(type==Constants.PacketType.FROM_DIR_TO_HDFS_FOR_UPLOAD_METADATA.getType())
		{
			int index;
			String responseString;
			
			/* 전송 받은 메타데이터를 fileManager를 이용하여 하둡 파일시스템에 저장 */
			responseString = token[1] + "\t" + token[3] + "\t" + ClientLauncher.getfileManager().save_mdata_to_HDFS(token[0], token[2]);
		
			/* 메타데이터 패쓰를 디렉토리 서버에 전송한다 */
			ClientLauncher.getConnector().sendPacket(31, 
					desc, 
					responseString.length(),
					responseString);
			
		}
		
		/* 
		 * 
		 * token[0] : 메타 데이터 패쓰
		 * token[1] : 클라이언트 소켓
		 * 
		 */
		
		// 메타데이터 다운로드 처리
		if(type==Constants.PacketType.FROM_DIR_TO_HDFS_FOR_DOWNLOADLOAD_METADATA.getType())
		{
			String responseString;
			
			/* 요청받은 패쓰의 자료를 가져온다, 최종 수신할 클라이언트 */
			responseString = token[1] + "\t" + ClientLauncher.getfileManager().get_mdata_from_Mpath(token[0]);
			
			
			/* 사용자에게 메타 데이터를 전송한다 */
			ClientLauncher.getConnector().sendPacket(33, 
					desc, 
					responseString.length(),
					responseString);
		}

		
	}
}
