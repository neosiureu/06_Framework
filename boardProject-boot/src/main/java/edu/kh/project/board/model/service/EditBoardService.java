package edu.kh.project.board.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.board.model.dto.Board;
import lombok.Value;

public interface EditBoardService {



	/**
	 * 게시글 작성 서비스
	 * 
	 * @param inputBoard
	 * @param images
	 * @return
	 */
	
	/*
	 * # 게시글 이미지 요청주소 my.board.resource-handler=/images/board/** #웹상에서 <img
	 * src=/images/board/abc.png>로 요청을 보내면
	
	
	 * # 게시글 이미지 요청 시 연결할 서버 폴더 경로
	 * my.board.resource-location=file:///C:/uploadFiles/board/
	 */

	int boardInsert(Board inputBoard, List<MultipartFile> images) throws Exception ;

	
	/** 게시글 수정 서비스
	 * @param inputBoard
	 * @param images
	 * @param deleteOrderList
	 * @return
	 */
	int boardUpdate(Board inputBoard, List<MultipartFile> images, String deleteOrderList)
	throws Exception;


	/** 게시글 삭제 서비스
	 * @param map
	 * @return
	 */
	int boardDelete(Map<String, Integer> map);

	
}
