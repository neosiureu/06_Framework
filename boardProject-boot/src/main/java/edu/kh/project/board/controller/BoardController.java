package edu.kh.project.board.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.annotation.ApplicationScope;

import edu.kh.project.board.model.service.BoardService;
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
		
	

}
