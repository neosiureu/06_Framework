package edu.kh.project.board.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.board.model.dto.Board;
import edu.kh.project.board.model.dto.BoardImg;
import edu.kh.project.board.model.service.BoardService;
import edu.kh.project.member.model.dto.Member;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("board")
@Slf4j
public class BoardController {
	
	@Autowired
	private BoardService service;
	
	/**
	 * 
	 * @param boardCode : 게시판 종류 구분 번호 (1/2/3/ ...)
	 * @param cp : 현재 조회를 요청한 페이지 번호 (없으면 1)
	 * /board/1, /board/2, /board/3 
	 *
	 *[0-9] : 한칸에 0~ 9 사이 숫자 하나만 입력 가능
	 *[0-9] + : 모든 숫자
	 *
	 * /board 이하 1레벨 자리에 숫자로된 요청 주소가 작성되어있을때만 해당 메서드 매핑
	 *  -> 정규식 이용했기 때문에
	 * @return
	 */
	@GetMapping("{boardCode:[0-9]+}")   //  /board/xxx  -> /board 이하 1레벨 자리에 어떤 주소값이 
	//                                           들어오든 모두 이메서드에 매핑
	public String selectBoardList(@PathVariable("boardCode") int boardCode , 
			                      @RequestParam(value="cp" , required=false, 
			                       defaultValue = "1") int cp ,
			                        Model model) {
	
		// 조회 서비스 호출 후 결과 반한 받기.
		
		Map<String, Object> map = null;
		
		// 조건에 따라 서비스 메서드 분기처리 하기 위해 map 은 선언만 함.
		
		
		// 검색이 아닌경우
		
		// 게시글 목록 조회 서비스 호출
		map = service.selectBoardList(boardCode, cp);
		
		
		// 검색인 경우
 		
		//model에 반환 받은 값 등록
		model.addAttribute("pagination",map.get("pagination"));
		model.addAttribute("boardList",map.get("boardList"));
		
		// forward: src/main/resources/templates/board/boardList.html
		return "board/boardList";
	}
	
	//상세 조회 요청 주소
	// /board/1/1994?cp=1
	
	/** 게시글 상세조회
	 * @param boardCode : 주소에 포함된 게시판 종류 1 , 2 ,3 
	 * @param boardNo : 게시글 번호
	 *              (boardcode,boardNo Request scope에 저장되어있음)
	 *              왜 ? @PathVariable 어노테이션 이용시 변수값이 request scope에 저장
	 *              되기 때문에
	 * 
	 * @param model     
	 * @param loginMember : 로그인 여부와 관련없이 상세 조회는 할수 있어야함으로
	 *                      required = false 로함
	 * @param ra
	 * @return
	 */
	@GetMapping("{boardCode:[0-9]+}/{boardNo:[0-9]+}")
	public String boardDetail(@PathVariable("boardCode") int boardCode,
			                  @PathVariable("boardNo") int boardNo,
			                  Model model ,
    @SessionAttribute(value="loginMember" , required = false) Member loginMember,
			                  RedirectAttributes ra ) {
			 
		// 게시글 상세조회 서비스 호출
		
		// 1) Map으로 전달할 파라미터 묶기
		
		Map<String, Integer> map = new HashMap<>();
		map.put("boardCode", boardCode);
		map.put("boardNo", boardNo);
		
		// 로그인 상태인 경우에만 memberNo 추가
		if(loginMember !=null) {
			map.put("memberNo", loginMember.getMemberNo());
		}
		
		//2  서비스호출
		
		Board board = service.selectOne(map); //한개의 게시글을 조회
		
		String path = null;
		
		if(board == null) {
			
			path ="redirect:/board/" + boardCode; // 목록 재요청
			ra.addFlashAttribute("message","게시글이 존재하지 않습니다.");
			
		}else {
			
			// 조회 결과가 있는 경우
			path =  "board/boardDetail"; // boardDetail.html로 forward
			
			// board - 게시글 일반 내용 + imageList + commentList
			model.addAttribute("board", board);
			
			// 조회된 이미지 목록이 있을 경우
			if(!board.getImageList().isEmpty()) {
				
				BoardImg thumbnail = null;
				
				//imageList의 0번 인덱스 == 가장 빠른 순서 (imgOrder)
				//만약 이미지 목록의 첫번째 행의 imgOrder가 0 == 썸네일인 경우
				
				if(board.getImageList().get(0).getImgOrder() == 0) {
					
					thumbnail = board.getImageList().get(0);
					
				}
				model.addAttribute("thumbnail",thumbnail);
				model.addAttribute("start", thumbnail != null ? 1 : 0);
				// 썸네일이 null 이 아니면 1 , null이면 0
				                //start : 썸네일이 있다면1 ,없다면 0 을저장
				
			}
			
			
		}

		return path;
		
		}

}
