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

}
