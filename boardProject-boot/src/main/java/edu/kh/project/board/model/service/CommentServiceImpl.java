package edu.kh.project.board.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import edu.kh.project.board.mapper.CommentMapper;
import edu.kh.project.board.model.dto.Comment;

@Service
@Transactional (rollbackFor = Exception.class)
public class CommentServiceImpl implements CommentService {

	@Autowired
	private CommentMapper mapper;
	
	// 댓글 목록조회
	@Override
	public List<Comment> select(int boardNo) {
		// List<comment> (java의 자료형 List)
		// HTTPMessageConverter 가
		// List -> JSON(문자열)로 변환해서 응답 -> JS
		return mapper.select(boardNo);
		
	}

	//댓글 /답글 등록 서비스
	@Override
	public int insert(Comment comment) {
		
		return mapper.insert(comment);
	}
	@Override
	public int delete(int commentNo) {
		
		return mapper.delete(commentNo);
	}
	@Override
	public int update(Comment comment) {
		
		return mapper.update(comment);
	}
	
}
















