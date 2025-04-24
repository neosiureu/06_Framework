package edu.kh.project.myPage.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UploadFile {
	private int fileNo;
	private String filePath;
	private String fileOriginalName;
	private String fileRename;
	private String fileUploadDate;
	private int memberNo;
	// DTO 만들떄 관련된 테이블 컬럼과 반드시 동일하게 만들어야하는건 아니다!
	// 필요에 따라 (JOIN) 필드를 더 늘리거나, 줄여도 된다!
	private String memberNickname;
}

