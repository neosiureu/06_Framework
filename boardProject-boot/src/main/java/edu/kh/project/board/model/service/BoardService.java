package edu.kh.project.board.model.service;

import java.util.List;
import java.util.Map;

import edu.kh.project.board.model.dto.Board;

public interface BoardService {

	/** 게시판 종류 조회 서비스 (Interceptor)
	 * @return
	 */
	List<Map<String, Object>> selectBoardTypeList();

	/** 특정게시판의 지정된 페이지 목록 조회 서비스
	 * @param boardCode
	 * @param cp
	 * @return
	 */
	Map<String, Object> selectBoardList(int boardCode, int cp);

	/** 게시글 상세조회
	 * @param map
	 * @return
	 */
	Board selectOne(Map<String, Integer> map);
	
	/** 게시글 좋아요 체크 / 해제
	 * @param map
	 * @return
	 */
	int boardLike(Map<String, Integer> map);

	/** 조회수 1 증가 서비스
	 * @param boardNo
	 * @return
	 */
	int updateReadCount(int boardNo);

	/** 검색 서비스
	 * @param paramMap
	 * @param cp
	 * @return
	 */
	Map<String, Object> searchList(Map<String, Object> paramMap, int cp);

	/** db이미지 파일명 목록 조회
	 * @return
	 */
	List<String> selectDbImageList();

}
