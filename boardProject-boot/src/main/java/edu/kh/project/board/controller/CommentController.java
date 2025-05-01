package edu.kh.project.board.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.kh.project.board.model.dto.Comment;
import edu.kh.project.board.model.service.CommentService;


// 여기 들어오는건 다 비동기
// REST API 구축을 위해서 사용하는 컨트롤러용 어노테이션
// @Controller+ @Responsebody를 각 메서드마다 포함
// 요청과 응답 제어 역할 명시 + baen으로 등록
// ResponseBody => 응답 본문으로 응답 데이터 자체를 반환하는 비동기 요청에 대한 컨트롤러
@RestController
@RequestMapping("comments")
public class CommentController {
	
	// final + @RequiredArgsConstructor
	
	@Autowired
	private CommentService service;
	
	
	/** 댓글 목록의 조회
	 * @param boardNo
	 * @return
	 */
	@GetMapping("")
	public List<Comment> select(@RequestParam("boardNo") int boardNo){
	
		// JS는 List가 뭔지는 모르지만 알아서 JSON이라는 문자열들의 형태로 변환한다
		
		// HTTP MESSAGE CONVERTER가 그렇게 해 줌
		
		return service.select(boardNo);
	}
	
	
	// 댓글과 답글 모두 처리
	@PostMapping("")
	public int insert(@RequestBody Comment comment) {
	
		// 답글이라면 parentCommentNo까지 알아서 넘어옴. 다만 1레벨이면 0 
		
		return service.insert(comment);
		
	}
	
	
	
	/** 댓글 삭제
	 * @param commentNo
	 * @return
	 */
	@DeleteMapping("")
	public int delete(@RequestBody int commentNo) {	
		
		return service.delete(commentNo);
	}
	
	
	
	
	/** 댓글수정
	 * @param comment
	 * @return
	 */
	@PutMapping("")
	public int update(@RequestBody Comment comment) {
		
		return service.update(comment);
	}

}
