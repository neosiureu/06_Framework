package edu.kh.project.board.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.board.model.dto.Board;
import edu.kh.project.board.model.service.BoardService;
import edu.kh.project.board.model.service.EditBoardService;
import edu.kh.project.member.model.dto.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;

@RequestMapping("editBoard")
@Controller
@Slf4j
public class EditBoardController {

	@Autowired
	private EditBoardService service;
	
	@Autowired
	private BoardService boardService;

	
	
	
	
	
	
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
	
	/** 게시글 디테일에서 수정화면으로 전환하는 컨트롤러 메서드
	 * @param boardCode: 게시판 종류 번호
	 * @param boardNo: 게시글 번호
	 * @param logMember: 현재 로그인한 회원 객체 => 본인이 아니면 리다이렉트
	 * @param model
	 * @param ra
	 * @return
	 */
	@GetMapping("{boardCode:[0-9]+}/{boardNo:[0-9]+}/update")
	public String boardUpdate(@PathVariable("boardCode") int boardCode
			, @PathVariable("boardNo") int boardNo, 
			@SessionAttribute("loginMember") Member logMember,
			Model model, 
			RedirectAttributes ra
			) {
		
		 // 수정 화면에 출력할 제목 내용 이미지까지 조회
	    // 게시글 상세조회를 컨트롤러 서비스 매퍼 다 만들어뒀을 듯 
		// selectOne() => 재활용 (제목 내용 이미지리스트 댓글리스트)
		
		Map<String, Integer> map = new HashMap<>();

		map.put("boardCode", boardCode);
		map.put("boardNo", boardNo);
		
		
		// BoardSerice.selectOne(map)호출
		// Board가 반환 될듯
		
		 Board board  =boardService.selectOne(map);
		
		 
		 String message  =null;
		 String path = null;
		 
		 if(board == null) {
			 message = "해당 게시글이 존재하지 않습니다!";
			 // 저러한 boardCode에 저런 boardNo가 없다
			 path = "redirect:/"; // 메인 페이지로 리다이렉트
			 ra.addFlashAttribute("message",message);
			 // 리턴할 때 이렇게 안 하고 여기서 처리하는 이유는 
			 // redirect와 get이 모두 존재하기 때문
		 } 
		 
		 else if(board.getMemberNo() != logMember.getMemberNo()) {
			 message = "자신이 작성한 글만 수정 가능합니다!";
			 path = String.format("redirect:/board/%d/%d", boardCode, boardNo);  // 상세조회 페이지로 리다이렉트
			 ra.addFlashAttribute("message",message);
			 
		 }
		 	 
		 else {
			 path = "board/boardUpdate";
			 // src/main/resources/templates/board/boardUpdate.html로 포워딩
			 
			 model.addAttribute("board",board);
		 }
		 
		 
		 
		return path;
	}
	
	
	
	/** 게시글 수정
	 * @param boardCode: 게시판 종류 번호
	 * @param boardNo: 수정할 게시글의 번호
	 * @param inputBoard: 커맨드 객체 (세팅된 제목과 내용)
	 * @param images: 제출된 input type이 "file"이었던 모든 요소 = 이미지 파일
	 * @param logMember: 로그인한 회원의 번호를 이용
	 * @param ra
	 * @param deleteOrderList: 삭제된 이미지 순서가 기록된 문자열 배열 ("1,2,3"과 같은 형태)
	 * @param cp: 수정 성공 시 이전 파라미터를 유지하기 위함. 리다이렉트 페이지로 가려면 CP 필수
	 * @return
	 */
	@PostMapping("{boardCode:[0-9]+}/{boardNo:[0-9]+}/update")
	public String boardUpdate(@PathVariable("boardCode") int boardCode, 
			@PathVariable("boardNo") int boardNo,
			Board inputBoard,
			@RequestParam("images") List<MultipartFile> images,
			@SessionAttribute("loginMember") Member logMember,
			RedirectAttributes ra,
			@RequestParam(value="deleteOrderList", required=false) String deleteOrderList,
			@RequestParam(value="cp", required = false, defaultValue = "1") int cp
			) throws Exception{
		
		
		// 보드코드, 보드 숫자, 보드(수정한 제목, 수정한 내용) 
		
		
		
		// 1. inputBoard에 많은 것을 넣을 수 있을 듯. "boardCode" "boardNo" "loginMember"의 memberNo 세팅
		
		inputBoard.setBoardCode(boardCode);
		inputBoard.setBoardNo(boardNo);
		inputBoard.setMemberNo(logMember.getMemberNo());

		// 제목, 내용, boardCode, boardNo, memberNo가 전부 inputBoard에 들어감 
		// + deleteOrderList와 이미지들만  서비스로 넘기면 됨
		
		
		
		
		// 2. 게시글 수정 서비스 호출 후 결과를 반환받음
		
		int result = service.boardUpdate(inputBoard,images,deleteOrderList);
		
		
		
		// 0보다 큰 값이나 0이 들어있음
		
		// 3. 서비스 결과에 따른 화면 응답 제어
		
		String message = null;
		String path = null;
		
		
		if(result>0) {
			// 게시글 수정이 잘 된 경우
			message = "게시글이 수정되었습니다";
			// 게시글 상세조회 페이지로 리다이렉트
			// /board/1/2000?cp=3
			path = String.format("/board/%d/%d?cp=%d", boardCode, boardNo, cp);
			
	
//			log.debug("디버깅 결과"+images);
			
		}
		
		else {
			message = "수정 실패";
			path = "update"; 
			// 상대경로 editBoard/1/1994/update => POST방식으로 보냄
			// 게시글 수정 화면으로 다시 POST를 통해 전환하도록 요청하겠다.
			
		}
		
		ra.addFlashAttribute("message",message);
		
		
		
				
		return "redirect:"+path;
	}
}
