package hadoop;

import java.util.Random;

public class fileManager {

	String path = " default path ";
	String data = " default data ";
	int index = 77;
	
	
	public String save_mdata_to_HDFS(String meta_data) {
		/* 메타 데이터 [경로]를 저장할 변수 */
		String m_path;
		
		/* 메타 데이터를 저장할 위치를 지정하는 랜덤 스트링 생성 */
		StringBuffer buffer = new StringBuffer();
		Random random = new Random();
		 
		String chars[] = 
		  "a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z".split(",");
		 
		for (int i=0 ; i<120 ; i++)
		{
		    buffer.append(chars[random.nextInt(chars.length)]);
		}
		
		m_path = buffer.toString();
		
		
		/* 해당 위치에 메타 데이터 저장 */
		
		
		
		return m_path;
		
	}

	
	public String get_mdata_from_Mpath(String m_path) {
		
		/* 메타 [데이터] 를 저장할 변수 */
		String m_data;
		
		/* m_path에서 메타 데이터를 가져온다 */
		
		
		
		return data;
		
	}





	
}
