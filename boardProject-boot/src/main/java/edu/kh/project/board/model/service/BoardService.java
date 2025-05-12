package edu.kh.project.board.model.service;

import java.util.List;
import java.util.Map;

import edu.kh.project.board.model.dto.Board;

public interface BoardService {

	/** 게시판 종류 조회 서비스
	 * @return
	 */
	List<Map<String, Object>> selectBoardTypeList();

	/** 특정 게시판에 지정된 페이지 목록
	 * @param boardCode
	 * @param cp
	 * @return
	 */ 
	Map<String, Object> selectBoardList(int boardCode, int cp);

	/** 게시글 상세조회 서비스
	 * @param map
	 * @return
	 */
	Board selectOne(Map<String, Integer> map);

	/** 게시글 좋아요 체크 및 해제
	 * @param map
	 * @return
	 */
	int boardLike(Map<String, Integer> map);

	/** 조회수 1 증가 서비스
	 * @param boardNo
	 * @return
	 */
	int updateReadCount(int boardNo);

	
	// 게시글 완료/미완료 상태 수정 서비스를 위한 메소드 선언 추가
		/** 게시글 완료/미완료 상태 수정
		 * @param paramMap (boardNo, completionStatus)
		 * @return 업데이트된 행의 개수 (성공 시 1)
		 */
		int updateCompletion(Map<String, Object> paramMap);

		/** 검색 서비스
		 * @param paraMap
		 * @param cp
		 * @return
		 */
		Map<String, Object> searchList(Map<String, Object> paraMap, int cp);

		
		
		/** DB
		 * @return
		 */
		List<String> selectDBImageList(); 

}
