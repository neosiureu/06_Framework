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

}
