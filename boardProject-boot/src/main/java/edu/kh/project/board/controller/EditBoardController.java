package edu.kh.project.board.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.board.model.dto.Board;
import edu.kh.project.board.model.service.EditBoardService;
import edu.kh.project.member.model.dto.Member;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("editBoard")
@Slf4j
public class EditBoardController {
	
	@Autowired
	private EditBoardService service;
	
	
	//게시글 작성 화면 전환
	@GetMapping("{boardCode:[0-9]+}/insert")
	public String boardInsert(@PathVariable("boardCode") int boardCode) {
		
		return "board/boardWrite";
	}
	
	/** 게시글 작성 
	 * @param boardCode : 어떤 게시판에 작성될 글인지 구분 (1 공지 2정보 3자유) 
	 * @param inputBoard : 입력된 값 (제목 , 내용)
	 * @param loginMember : 현재 로그인된 회원
	 * @param images      : 제출된 file 타입 input 태그가 전달한 데이터들(이미지 파일 ..)
	 * @param ra          
	 * @return
	 */
	@PostMapping("{boardCode:[0-9]+}/insert")
	public String boardInsert(@PathVariable("boardCode") int boardCode ,
			                  @ModelAttribute Board inputBoard ,
			                  @SessionAttribute("loginMember") Member loginMember,
			                  @RequestParam("images") List<MultipartFile> images, 
			                  RedirectAttributes ra)throws Exception {
		
		/* List<MultipartFile> images
		 * - 5개 모두 업로드 O -> List안에 0~4 번 인덱스에 파일 저장됨
		 * - 5개 모드 업로드 X -> List 안에 0~4 번 인덱스에 파일 저장X
		 * - 2번 인덱스만 업로드 -> 2번 인덱스만 파일 저장, 0/1/3/4번 인덱스는 저장 X
		 * 
		 * [문제점]
		 * - 파일이 선택되지 않은 input 태그도 제출되고있음
		 * (제출은 되어있는데 데이터는 없음)
		 * 
		 * -> 파일 선택이 안된 input 태그 값을 서버에 저장하려고 하면 오류발생 !
		 * 
		 * [해결방법]
		 * - 무작정 서버에 저장 X
		 * -> List의 각 인덱스에 들어있는 MultiPartFile에 실제로 
		 *   제출된 파일의 데이터가 있는지 확인하는 로직을 추가구성
		 *   
		 *   
		 *   + List 요소의 index번호 == IMG_ORDER 와 같음
		 *   
		 * */
		
		// 1. boardCode, 로그인한 회원번호 memberNo를 inputBoard에 세팅 
		inputBoard.setBoardCode(boardCode);
		inputBoard.setMemberNo(loginMember.getMemberNo());
		// -> inputBoard에는 총 네가지 세팅됨 (boardTitle, boardContent, boardCode, memberNo)
		
		//2. 서비스 메서드 호출 후 결과 반환받기
		// -> 성곳이 [상세조회]를 요청 할수 있또록
		// 삽입된 게시글의 번호를 반환받기

		int boardNo = service.boardInsert(inputBoard, images);
		
		
		//3. 서비스 결과에 따라 message, 리다이렉트 경로 지정
		String path = null;
		String message = null;
		
		if(boardNo > 0) {
			message = "게시글이 작성되었습니다";
			path = "/board/" + boardCode + "/" + boardNo;
			
		}else {
			
			path = "insert" ;
			message = "게시글 작성 실패";
			
		}
		
		ra.addFlashAttribute("message",message);
		return "redirect:" + path;
	}
	
	
}
