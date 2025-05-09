package edu.kh.project.board.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import edu.kh.project.board.model.dto.Board;



@Mapper
public interface BoardMapper {

	List<Map<String, Object>> selectBoardTypeList();

	/** 게시글 수 조회 (board code 에 따라)
	 * @param boardCode
	 * @return
	 */
	int getListCount(int boardCode);

	/** 특정 게시판의 지정된 페이지 목록 조회
	 * @param boardCode
	 * @param rowBounds
	 * @return
	 */
	List<Board> selectBoardList(int boardCode, RowBounds rowBounds);

	/**게시글 상세조회
	 * @param map
	 * @return
	 */
	Board selectOne(Map<String, Integer> map);

	/** 좋아요 체크해제
	 * @param map
	 * @return
	 */
	int deleteBoardLike(Map<String, Integer> map);

	/** 좋아요 체크하기
	 * @param map
	 * @return
	 */
	int insertBoardLike(Map<String, Integer> map);

	/** 게시글 좋아요 개수 조회
	 * @param integer
	 * @return
	 */
	int selectLikeCount(int boardNo);

	/**조회수 1증가
	 * @param boardNo
	 * @return
	 */
	int updateReadCount(int boardNo);

	/** 조회수 조회
	 * @param boardNo
	 * @return
	 */
	int selectReadCount(int boardNo);

	/** 검색 조건에 맞는 행의 갯수
	 * @param paramMap
	 * @return
	 */
	int getSearchCount(Map<String, Object> paramMap);

	/** 검색 결과 목록 조회
	 * @param paramMap
	 * @param rowBounds
	 * @return
	 */
	List<Board> selectSearchList(Map<String, Object> paramMap, RowBounds rowBounds);

}
