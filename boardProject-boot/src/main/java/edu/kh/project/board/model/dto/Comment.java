package edu.kh.project.board.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Comment {
	
	private int commentNo;
	private String commentContent;
	private String commentWriteDate;
	private String commentDelFl;
	private int boardNo;
	private int memberNo;
	private int parentCommentNo;

	
	// 댓글 조회 시 회원의 프로필과 닉네임
	
	private String memberNickname;
	private String profileImg;
	

}
