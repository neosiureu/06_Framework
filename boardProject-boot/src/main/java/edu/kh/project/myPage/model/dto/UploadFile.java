package edu.kh.project.myPage.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadFile {
	private int fileNo;
	private String filePath;
	private String fileOriginalName;
	private String fileRename;
	private String fileUploadDate;
	private int memberNo;
	
	
	
	private String memberNickname; //테이블에 없는 것 => 다른 테이블과 조인하여 닉네임도 같이 가져오고 싶어서
	// DTO만들 때 관련된 테이블 컬럼과 반드시 동일하게 만들어야 하는 것은 아님 
	// => 필요에 따라 JOIN하거나 필드를 늘리거나 줄여도 좋다: 자바단에서 굳이 사용하지 않을 데이터는 신경쓰지 않는다.
	
	
}
