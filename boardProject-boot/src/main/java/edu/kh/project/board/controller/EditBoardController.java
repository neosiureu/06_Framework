package edu.kh.project.board.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.board.model.dto.Board;
import edu.kh.project.board.model.service.EditBoardService;
import edu.kh.project.member.model.dto.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequestMapping("editBoard")
@Controller
@Slf4j
public class EditBoardController {

	@Autowired
	private EditBoardService service;

	@GetMapping("{boardCode:[0-9]+}/insert")
	public String BoardInsert(@PathVariable("boardCode") int boardCode) {

		return "board/boardWrite";
	}

	/**
	 * 게시글 작성
	 * 
	 * @param boardCode   어떤 게시판에 작성될 글일지 구분
	 * @param intputBoard 입력된 값들 중 제목과 내용만 세팅되어 있음. 즉 커멘드 객체 상태
	 * @param loginMember 현재 로그인한 회원의 번호를 얻어오는 용도
	 * @param images      제출된 파일타입의 input태그가 전달하는 중인 데이터
	 * @param ra
	 * @return
	 */
	@PostMapping("{boardCode:[0-9]+}/insert")
	public String BoardInsert(@PathVariable("boardCode") int boardCode, @ModelAttribute Board inputBoard,
			@SessionAttribute("loginMember") Member loginMember, @RequestParam("images") List<MultipartFile> images,
			RedirectAttributes ra) throws Exception {

		// imageList에는 할일이 있다. 이름바꿔주고 서버에 직접 올리는 등의 작업을 따로 해야하기에
		// 보드에 직접 삽입하지 않는다. 따로 image 들을 설정해야 한다

		// 파라미터 중 List<MultipartFile>인 images가 있다

		// ex) 만일 다섯 개 모두 이미지를 업로드했다면 List내 0~4번 인덱스에 실제로 파일이 저장 됨

		// ex) 만일 아무것도 안 넘어올 때 0~4번 인덱스에 실제 파일이 없을 것

		// ex) 만일 2번인덱스에만 채워져있고 0134번은 비어 있다면? => 문제점 발생

		/*
		 * 파일이 선택되지 않은 input태그 역시 제출되는 중 (제출은 되어있으나 MultipartFile이 비어있음)
		 * 
		 * [해결법]: List의 각 인덱스에 들어있는 MultipartFile이 비어있는지 NPE를 방지 서비스단에서 해야 적절
		 * 
		 * 주의!! List요소의 index번호는 DB에서 boardImg 테이블의 IMG_ORDER번호와 동일하다
		 * 
		 * 
		 */

		
		
		
		// 1) boardDTO에는 memberNo boardCode의 필드가 있으니 @ModelAttribute Board intputBoard에 다 넣어버리지?
		//
		inputBoard.setBoardCode(boardCode);
		inputBoard.setMemberNo(loginMember.getMemberNo());
		//inputBoard는  총 네가지 필드를 가지게 된다. boardTitle, boardContent, boardCode, member
		
		// 2) 서비스 메서드 호출 후 결과 반환 페이지? 성공 시에는 상세 조회를 요청할 수 있도록 
		// 현재 삽입된 게시글의 번호를 반환받기
		
		int boardNo = service.boardInsert(inputBoard, images);
		
		
		// 3. 서비스 결과에 따른 메시지 작성 및 리다이렉트 경로 지정
		
		String path = null;
		String message = null;
		
		if(boardNo>0) {
			message = "게시글이 잘 작성되었습니다";		
			// 게시글 잘 작성
			path = "/board/"+boardCode + "/" + boardNo;
		}
		
		else {
			path = "insert";
			// editBoard/1/insert
			message = "게시글 작성 실패";
		}
		
		
		ra.addFlashAttribute("message",message);
		return "redirect:"+path;

	}
}
