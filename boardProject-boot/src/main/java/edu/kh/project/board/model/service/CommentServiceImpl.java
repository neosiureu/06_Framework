package edu.kh.project.board.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.board.model.dto.Comment;
import edu.kh.project.board.model.mapper.CommentMapper;

@Transactional(rollbackFor = Exception.class)
@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private CommentMapper mapper;
	
	
	// 댓글 목록 조회
	@Override
	public List<Comment> select(int boardNo) {
		return mapper.select(boardNo);
	}


	
	
	
	/**
	 * 댓글 및 답글 등록 서비스
	 */
	@Override
	public int insert(Comment comment) {
		// TODO Auto-generated method stub
		
		return mapper.insert(comment);
	}




	/** 댓글 삭제
	 * @param commentNo
	 * @return
	 */

	@Override
	public int delete(int commentNo) {
		// TODO Auto-generated method stub
		return mapper.delete(commentNo);
	}





	/**
	 * 댓글 수정
	 */
	@Override
	public int update(Comment comment) {

		return mapper.update(comment);
	}
	

}
