package edu.kh.project.board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.project.board.model.dto.Board;
import edu.kh.project.board.model.dto.BoardImg;

@Mapper
public interface EditBoardMapper {

	/** 게시글 작성 (제목, 콘텐트만)
	 * @param inputBoard
	 * @return 게시글의 번호 boardNo
	 */
	int boardInsert(Board inputBoard);

	/** 게시글의 이미지 모두 삽입
	 * @param uploadList
	 * @return
	 */
	int insertUploadList(List<BoardImg> uploadList);

	
}
