package edu.kh.project.board.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.project.board.model.dto.Board;
import edu.kh.project.board.model.dto.BoardImg;

@Mapper
public interface EditBoardMapper {

	/** 게시글 작성 
	 * @param inputBoard
	 * @return boardNo 해당 게시글 번호
	 * mybatis기능으로 DML을 하는데도 행의 개수가 아닌 진짜 보드 넘버가 넘어옴
	 */
	int boardInsert(Board inputBoard);

	/** 게시글 이미지를 모두 삽입하는 메서드
	 * @param uploadList
	 * @return
	 */
	int insertUploadList(List<BoardImg> uploadList);
 
	/** 게시글 부분수정 (제목, 내용)
	 * @param inputBoard
	 * @return
	 */
	int boardUpdate(Board inputBoard);

	
	
	/** 게시글 이미지 삭제 요청
	 * @param map
	 * @return
	 */
	int deleteImage(Map<String, Object> map);

	
	
	/** 게시글 이미지 수정
	 * @param img
	 * @return
	 */
	int updateImage(BoardImg img);

	
	
	/** 위와 세트로 게시글 이미지를 삽입
	 * @param img
	 * @return
	 */
	int insertImg(BoardImg img);

	int boardDelete(Map<String, Integer> map);
	
	

}
