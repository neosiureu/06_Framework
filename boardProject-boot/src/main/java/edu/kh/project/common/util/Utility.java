package edu.kh.project.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;


// 프로그램 전체적으로 사용 될 유용한 기능 모음

public class Utility {
	// UUID나 전달받은 파일 => 객체화 되지 않고 전체적으로 접근 가능해야 함 => static 메서드
	
	public static int seqNum =1;
	// 1부터 99999까지 반복하게 한다
	
	/** 매개변수로 전달받은 원본명을 받아 파일의 변경명을 반환하는 메서드
	 * @return
	 */
	public static String fileRename(String originalFileName) {
		// 주로 20250424030330_000002.jpg와 같은 식으로 날짜와 시간 정보 + 순서가 숫자로 오게 됨
		
		// simpleDateFormat : 시간을 원하는 형태의 문자열로 간단히 변경할 수 있다
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		
		// java.util.Date를 반환해야 함 (현재시간 저장 자바 객체)
		
		String date = sdf.format(new Date());
		
		String number = String.format("%05d", seqNum);
		// 0을 다섯개 채워두고 가장 오른쪽에 숫자가 들어가게 됨 
		seqNum++;
		
		if(seqNum==100000) {
			seqNum=1;
		}
		
		
		// 문자열에서 사용하는 lastIndexOf('.')을 전달 => 해당 문자열에서 마지막 .의 위치를 반환
		
		String ext = originalFileName.substring(originalFileName.lastIndexOf(".")); 
		//문자열을 인덱스부터 끝까지 잘라낸 결과를 반환한다
		
		
		return date+"_"+number+ext;
		
	}
	

}
