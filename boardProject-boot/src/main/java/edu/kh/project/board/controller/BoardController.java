package edu.kh.project.board.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.kh.project.board.model.service.BoardService;
import lombok.extern.slf4j.Slf4j;


@RequestMapping("board")
@Controller
@Slf4j
public class BoardController {
	
	@Autowired
	private BoardService service;
	
	/*
	 * @param boardCode: 게시판종류에 따라 (1/2/3)
	 * @param cp: 현재 조회를 요청한 페이지 번호를 가짐 (없으면 1페이지를 요청한 것과 같다)
	  
	 /board/1 /board/2 /board/3
	 요청 주소에서 쿼리스트링 뒷 부분을 주소상 변수로서 사용할 수 있는 방법
	 pathVariable로 매핑한다
	 이는 정규식이 들어간다
	 
	 
	 정규식을 이용했기에 /boadrd이하 1레벨 자리에 
	 숫자로 된 요청 주소가 작성되어있을 때에 한해서 이 메서드로 매핑한다
	 	 
	 */
	@GetMapping("{boardCode:[0-9]+}") // +가 없으면 한 칸에 한자리수의 숫자가 들어갈 수 있다.
	public String selectBoardList(@PathVariable("boardCode") int boardCode
			/* 페이지네이션을 위해 cp값을 받아오기로 한다. 현재 페이지가 몇 페이지인지 받아오기 위해
			 * */
			,@RequestParam(value="cp",required = false, defaultValue = "1") int cp, Model model
			) {
		// board이하 1레벨 하위에 어떤 숫자 주소 값이 들어오더라도 매핑하겠다
		
		// 조회 서비스 호출 후 결과를 맵으로 반환
		
		
		Map<String,Object> map = null;
		// 조건에 따라 어떤 서비스의 메서드를 호출할지 가름. 
		// 다만 반환되는 것을 Map으로
		
		// 맨 밑에서 하는 검색인 경우와 검색이 아닌 경우를 따진다
		// board ?key=t & query = 1930; => key는 검색으로 t또는 c 또는 tc 또는 w로 key가 설정될 수 있다
		
		// 검색 역시 게시판의 목록 조회와 똑같으므로 맵으로 넘어온다
		
		
		
		
		
		// 게시글 목록 조회 서비스 호출하기
		
		map = service.selectBoardList(boardCode,cp);
		//	어떤 게시판 종류인지, 어떤 페이지를 요청했는지
		
		
		model.addAttribute("pagination",map.get("pagination"));
		model.addAttribute("boardList",map.get("boardList"));
		
		
		return "board/boardList";
		// src/main/resources/templates/board/boardList.html
		
	}
	
	// boadr/1?cp=3이라고 보내서 1페이지에서 3페이지로 넘어가게 한다
	// 다만 페이지네이션을 눌렀을 때만 있다. 따라서 Requestparam으로 받되
	// required = false, defaultValue = "1"
	
	
	

}
