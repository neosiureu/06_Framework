package edu.kh.project.board.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Board {
	// Board 테이블 관련 필드
	
	private int boardNo;
	private String boardTitle;
	private String boarContent;
	private String boardWriteDate;
	private String boardUpdateDate;

	private int readCount; //조회수
	private String boardFl;
	
	private int memberNo;
	private int boardCode;

	
	// 멤버테이블에만 있는 요소 => 조인할 것임
	private String memberNickname;
	private int commentCount; // 좋아요수
	private int likeCount;// 댓글수
	
	// 게시글 작성자의 프로필 이미지
	private String profileImg; // 상세 페이지에서
	
	// 게시글 목록에서의 썸네일 이미지
	private String thumbnail; // 각자 해라
	
	// 특정 게시글의 이미지 목록 리스트
	// 보드 이미지
	// private List<BoardImg> imageList;
	
	// 특정 게시글 작성된 댓글 목록 리스트
	// private List<Comment> commentList;
	
	// 좋아요 여부 확인
	private int likeCheck; // 상세 페이지에서
	

}
