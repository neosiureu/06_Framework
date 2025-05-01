package edu.kh.project.board.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.project.board.model.dto.Comment;

@Mapper
public interface CommentMapper {

	
	
	/** 댓글 목록 조회
	 * @param boardNo
	 * @return
	 */
	List<Comment> select(int boardNo);

	
	
	/** 댓글이던 답글이던 전부 이 메서드를 타서 SQL문으로?
	 * @param comment
	 * @return
	 */
	int insert(Comment comment);




	
	/** 댓글 삭제
	 * @param commentNo
	 * @return
	 */
	int delete(int commentNo);



	int update(Comment comment);
	
	

	
}
