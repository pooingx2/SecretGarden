package test_division;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class streamManager {
  /*split 정보*/
	public final static int maxSpliteSize = 10;
	public final static int sizeKB = 1024 * 1;
	public static int streamSize  = 3 * 1024 * sizeKB;
	 
	/*source file*/
	public static String sourceFilePath = "d:/test/";
	public static String sourceFileName = "wmv.wmv";
	
	/*destination stream*/
	public static String streamPath = "d:/test/stream/";
	public static String streamName = "streamfile0";

	/*main*/
	public static void main(String[] args) {
		try {
			/*원본 파일 InputStream*/
			File origFile = new File(sourceFilePath + sourceFileName);
			FileInputStream origFI = new FileInputStream(origFile);
			
			/*최대 분할 개수 초과 확인*/
			if(origFile.length() / streamSize >= maxSpliteSize){
				System.out.println("10개 이상 분할할 수 없습니다.(현재 stream 개수 : " + origFile.length()/streamSize + ")");
				System.exit(-1);
			}
			
			/*stream 생성하여 파일 분할*/
			splitFile(streamPath, streamName, origFI);

			/*stream을 통합하여 파일 복원*/
			combineFile(sourceFileName, streamPath);

		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	/*InputStream을 받아 매개변수로 받은 경로에 stream 파일 생성*/
	private static void splitFile(String streamPath, String streamName, InputStream origFI) {
		try {
			int readCnt = 0;
			int totCnt = 0;
			int streamIndex = 0;			//stream 이름을 위한 인덱스
			byte[] readBuffer = new byte[1024];		//최소 stream size는 1KB
			
			/*file I/O stream*/
			BufferedInputStream bufferedFI = new BufferedInputStream(origFI);						//원본 파일
			File streamFile = new File(streamPath + streamName + streamIndex + "._secretgarden");	//stream 파일
			FileOutputStream streamFO = new FileOutputStream(streamFile);
			
			do {
				/*원본 파일 읽기*/
				readCnt = bufferedFI.read(readBuffer);
				if (readCnt == -1) {
					break;
				}
				
				/*stream 파일에 write*/
				streamFO.write(readBuffer, 0, readCnt);
				totCnt += readCnt;

				/*다음 스트림*/
				if (totCnt % streamSize == 0) {
					streamFO.flush();
					streamFO.close();
					streamFile = new File(streamPath + streamName + (++streamIndex) + "._secretgarden");
					streamFO = new FileOutputStream(streamFile);
				}
			} while (true);

			origFI.close();
			streamFO.flush();
			streamFO.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Split success!");
	}
	
	/*매개변수로 받은 경로에 있는 stream 파일들을 한 파일로 통합*/
	private static void combineFile(String origFileName, String streamPath) throws FileNotFoundException, IOException {
		int readCnt = 0;
		byte[] buf = null;
		
		/*file I/O stream*/
		File streamFiles = new File(streamPath);
		String[] files = streamFiles.list();				//stream 파일 리스트
		FileOutputStream combinedFO = new FileOutputStream(streamPath + origFileName);	//통합될 파일
		FileInputStream streamFI = null;
		
		for (int i = 0; i < files.length; i++) {
			/*각 stream 파일의 InputStream*/
			streamFI = new FileInputStream(streamPath + files[i]);
			buf = new byte[1024];
			readCnt = 0;
			
			/*통합될 파일에 write*/
			while ((readCnt = streamFI.read(buf)) > -1) {
				combinedFO.write(buf, 0, readCnt);
			}
			// System.out.println("##########");
		}
		// System.out.println("1");
		
		combinedFO.flush();
		combinedFO.close();
		System.out.println("Combine seccess!");	
	}
}
