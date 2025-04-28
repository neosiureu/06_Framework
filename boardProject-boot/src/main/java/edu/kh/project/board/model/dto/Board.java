package edu.kh.project.board.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Board {
	//Board 테이블 컬럼
	private int boardNo;
	private String boardTitle;
	private String boardContent;
	private String boardWriteDate;
	private String boardUpdateDate;
	private int readCount;
	private String boardDelFl;
	private int memberNo;
	private int boardCode;
	
	//MEMBER 테이블 조인
	private String memberNickname;
	
	//목록 조회시 상관쿼리
	private int commentCount; // 댓글 수
	private int likeCount;    // 좋아요 수
	
	//게시글 작성자 프로필 이미지
	private String profileImg;
	
	//게시글 목록 썸네일 이미지
	private String thumbnail;
	
	//특정 게시글 이미지 목록 리스트
	//private List<BoardImg> imageList;
	
	//특정 게시글 작성된 댓글 목록 리스트
	//private List<Commet> commentList;
	
	// 좋아요 여부 확인
	private int likeCheck;
	
}


















