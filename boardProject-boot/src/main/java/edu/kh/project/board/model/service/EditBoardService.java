package edu.kh.project.board.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.board.model.dto.Board;

public interface EditBoardService {

	/** 게시글 작성 서비스
	 * @param inputBoard
	 * @param images
	 * @return
	 */
	int boardInsert(Board inputBoard, List<MultipartFile> images)throws Exception;

	/** 게시글 수정 서비스
	 * @param inputBoard
	 * @param images
	 * @param deletedOrdeList
	 * @return
	 */
	int boardUpdate(Board inputBoard, List<MultipartFile> images, String deleteOrdeList)throws Exception;

	/** 게시글 삭제 서비스
	 * @param map
	 * @return
	 */
	int boardDelete(Map<String, Integer> map);

}
