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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.board.model.dto.Board;
import edu.kh.project.board.model.service.BoardService;
import edu.kh.project.board.model.service.EditBoardService;
import edu.kh.project.member.model.dto.Member;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("editBoard")

@Slf4j
public class EditBoardController {
	
	@Autowired
	private EditBoardService service;
	
	@Autowired
	private BoardService boardService;
	
	
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
	
	/** 게시글 수정 화면 전환
	 * @param boardCode : 게시판 종류 번호
	 * @param boardNo   : 게시글 번호
	 * @param loginMember : 현재 로그인한 회원 객체 (로그인한 회원이 작성한 글이 맞는지 검사하는 용도)
	 * @param model  : 전달용
	 * @param ra
	 * @return
	 */
	@GetMapping("{boardCode:[0-9]+}/{boardNo:[0-9]+}/update")
	public String boardUpdate(@PathVariable("boardCode") int boardCode ,
			                   @PathVariable("boardNo") int boardNo,
			                   @SessionAttribute("loginMember") Member loginMember,
			                    Model model,
			                    RedirectAttributes ra) {
		
		// 수정 화면에 출력할 기존의 내용 / 제목/ 이미지 조회
		// -> 게시글 상세 조회
		Map<String, Integer> map = new HashMap<>();
		map.put("boardCode", boardCode);
		map.put("boardNo", boardNo);
		
		//BoardService.selectOne(map) 호출
		
		Board board = boardService.selectOne(map);
		
		String message = null;
		String path = null;
		
		if(board == null) {
			
			message = "해당게시글이 존재하지 않습니다";
			path = "redirect:/";  //메인페이지로 리다이렉트
		
			ra.addFlashAttribute("message",message);
			
		} else if(board.getMemberNo() != loginMember.getMemberNo()) {
			message = "자신이 작성한 글만 수정 가능합니다.";
			path = String.format("redirect:/board/%d/%d", boardCode, boardNo);
			
			ra.addFlashAttribute("message",message);
			
		}else {
			
			path = "board/boardUpdate"; //template/board/boardUpdate.html로 forward 
			model.addAttribute("board", board);
		}
		
		
		return path;
		
	}
	
	/** 게시글 수정
	 * @param boardCode : 게시판 종류번호
	 * @param boardNo   : 수정할 게시글의 번호
	 * @param inputBoard : 커맨드 객체 (제목, 내용)
	 * @param images      :  제출된 이미지파일 input type = "file" 모든 요소 (이미지 파일)
	 * @param loginMember : 현재 로그인한 회원의 번호를 이용하려고함
	 * @param ra
 	 * @param deledOrdeList :삭제된 이미지 순서가기록된 문자열 ("1","2","3")
	 * @param cp             : 수정성공시 이전 파라미터 유지
	 * @return
	 */
	@PostMapping("{boardCode:[0-9]+}/{boardNo:[0-9]+}/update")
	public String boardUpdate(@PathVariable("boardCode") int boardCode ,
                   @PathVariable("boardNo") int boardNo ,
                   Board inputBoard, 
                   @RequestParam("images") List<MultipartFile> images,
                   @SessionAttribute("loginMember") Member loginMember ,
                   RedirectAttributes ra ,
                   @RequestParam(value = "deleteOrderList" , required = false) String deleteOrdeList 
                   ,@RequestParam(value ="cp", required = false, defaultValue = "1") int cp) throws Exception{
	
		// 1. 커맨드 객체 (inputBoard)에 boardCode, boardNo, memberNo 세팅
		inputBoard.setBoardCode(boardCode);
		inputBoard.setBoardNo(boardNo);
		inputBoard.setMemberNo(loginMember.getMemberNo());
		// inputBoard -> 제목, 내용 , 코드 , 번호 , 회원번호
		
		//2. 게시글 수정 서비스 호출 후 결과 반한 받기
		
		int result = service.boardUpdate(inputBoard,images, deleteOrdeList);
		
		//3. 서비스 결과에 따라 응답 제어
		String message = null;
		String path = null;
		
		if(result > 0) {
			message = "게시글이 수정 되었습니다";
			//게시글 상세조회 페이지로 redirect
			// / board/1/2000?cp=3
			path = String.format("/board/%d/%d?cp=%d", boardCode, boardNo , cp);
			
			
			
		}else {
			message = "수정실패";
			path = "update"; // 상대경로 
			// 현재 : editBoard/1/1994/update?cp = 1 (GET)
			
			
		}
		ra.addFlashAttribute("message", message);
		
		return "redirect:" + path;
	}
	
	
	@RequestMapping(value = "{boardCode:[0-9]+}/{boardNo:[0-9]+}/delete", 
			              method = {RequestMethod.GET, RequestMethod.POST})
	public String boardDelete(@PathVariable("boardCode") int boardCode ,
			                  @PathVariable("boardNo") int boardNo ,
			                  @RequestParam(value = "cp", required = false , defaultValue ="1") int cp,
			                  RedirectAttributes ra ,
			                  @SessionAttribute("loginMember") Member loginMember) {
			      
		 Map<String , Integer> map = new HashMap<>();
		 map.put("boardCode", boardCode);
		 map.put("boardNo", boardNo);
		 map.put("memberNo", loginMember.getMemberNo());
		 
		 int result = service.boardDelete(map);
		
		 String path = null;
		 String message = null;
		 
		 if(result > 0 ) {
			 message = "삭제가 완료되었습니다";
			 path = String.format("/board/%d?cp=%d", boardCode,cp); //게시글 상세조회
			 // /bard/1?cp=7
		 }else {
			 message = "삭제 실패";
			 path =  String.format("/board/%d/%d?cp=%d", boardCode,boardNo,cp); //게시글 상세조회
			//board/1/2005?cp=1		 
		 }
		ra.addFlashAttribute("message" , message);
		return "redirect:" + path;
	}
	
}
















