package edu.kh.project.board.model.service;

import java.util.List;

import edu.kh.project.board.model.dto.Comment;

public interface CommentService {

	/**댓글 목록 조회하기
	 * @param boardNo
	 * @return
	 */
	List<Comment> select(int boardNo);

	/** 댓글/답글 작성 서비스 
	 * @param comment
	 * @return
	 */
	int insert(Comment comment);

	/**댓글 삭제 서비스
	 * @param commentNo
	 * @return
	 */
	int delete(int commentNo);

	/**댓글 수정
	 * @param comment
	 * @return
	 */
	int update(Comment comment);

}
