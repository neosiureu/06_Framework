package edu.kh.project.board.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import edu.kh.project.board.model.dto.Board;

@Mapper
public interface BoardMapper {

	List<Map<String, Object>> selectBoardTypeList();

	/** 게시판 종류에 따라 게시글 수를 조회
	 * @param boardCode
	 * @return
	 */
	int getListCount(int boardCode);

	/** 특정 게시판의 지정된 페이지의 목록을 조회한다
	 * @param boardCode
	 * @param rowBounds
	 * @return
	 */
	List<Board> selectBoardList(int boardCode, RowBounds rowBounds);

	
	
	/** 게시글 상세 조회
	 * @param map
	 * @return 
	 */
	Board selectOne(Map<String, Integer> map);

	/** 좋아요 해제
	 * @param map
	 * @return
	 */
	int deleteBoardLike(Map<String, Integer> map);

	
	
	/** 좋아요 체크
	 * @param map
	 * @return
	 */
	int insertBoardLike(Map<String, Integer> map);

	/** 게시글 좋아요 개수 조회
	 * @param integer
	 * @return
	 */
	int selectLikeCount(int boardNo);
	
	
	
	
	

	/** 조회수 바꾸기
	 * @param boardNo
	 * @return
	 */

	int updateReadCount(int boardNo);

	/** 조회수 알아내기
	 * @param boardNo
	 * @return
	 */
	int selectReadCount(int boardNo);

	
	
	/** 나눔 완료 여부 알아내기
	 * @param paramMap
	 * @return
	 */
	int updateCompletion(Map<String, Object> paramMap);

	
	
	/** 검색 조건이 맞는 게시글 수의 조회
	 * @param paraMap
	 * @return
	 */
	int getSearchCount(Map<String, Object> paraMap);

	
	
	/** 검색 결과 목록 조회
	 * @param paraMap
	 * @param rowBounds
	 * @return
	 */
	List<Board> selectSearchList(Map<String, Object> paraMap, RowBounds rowBounds);

	
	
	
	/** DB 이미지 파일명 
	 * @return
	 */
	List<String> selectDBImageList();

	
	
	
	
	
	

}
