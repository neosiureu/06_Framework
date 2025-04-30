package edu.kh.project.board.model.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.board.model.dto.Board;
import lombok.Value;

public interface EditBoardService {

	int boardInsert(Board inputBoard, List<MultipartFile> images) throws Exception ;

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

	
	
}
