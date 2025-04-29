package edu.kh.project.board.model.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class BoardImg {
	
	
	private int imgNo;
	
	
	private String imgPath;
	private String imgOriginalName;
	private String imgRename;
	
	private int imgOrder;
	private int boardNo;

	
	
	// 게시글 이미지 삽입 및 수정 시 사용하는 필드를 미리 만들어 놓는다
	private MultipartFile uploadFile;
	
}
