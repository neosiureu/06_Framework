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
	 요청 주소에서 쿼리스트링이 아닌 하위 주소를 주소상 변수로서 사용할 수 있는 방법
	 pathVariable로 매핑한다
	 이는 정규식이 들어간다
	 
	 
	 정규식을 이용했기에 /boadrd이하 1레벨 자리에 
	 숫자로 된 요청 주소가 작성되어있을 때에 한해서 이 메서드로 매핑한다
	 	 
	 */
	
	
	// @PathVariable은  "boardCode"에 대한 값을 requestScope에 실어준다 + 매핑까지 해준다.
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
		// board ?key=t & query = 1930; => key는 검색어에 해당하며 t또는 c 또는 tc 또는 w로 key가 설정될 수 있다
		
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
	
	
	
	
	
	
	//   /board/1/1997?cp=1과 같이 넘어옴

	// 1/1997을 pathVarialbe로 뒤에 cp는 requestParam으로
	
	
	
	/* 게시물 상세 조회
	 * @param boardCode: 주소에 포함된 게시판의 종류 번호  ( ex) 1=공지 )
	 * @param boardNo: 주소에 포함된 게시글의 번호
	 위 둘은 항상 request scope에 실려있다. @PathVariable을 이용 시 
	 항상 변수 값이 request scope에 저장 
	 * @param model
	 * @param loginMemeber: 로그인 여부와 관련 없이 일단 상세 조회는 할 수 있음 => required false
	 * @param ra
	 * @return
	 */
	@GetMapping("{boardCode:[0-9]+}/{boardNo:[0-9]+}")
	public String boardDetail(@PathVariable("boardCode") int boardCode, 
							@PathVariable("boardNo") int boardNo,
							Model model,
							@SessionAttribute(value = "loginMember", required = false) Member loginMemeber,
							RedirectAttributes ra )
	{
		
		// 게시글 상세 조회 서비스 호출
		
		/*
		 SQL문까지 boardCode boardNo를 전달해야 하나만 저장할 수 있다. 따라서  맵으로 묶으면 된다
		 */
		

		Map<String, Integer> map = new HashMap<>();
		
		map.put("boardCode", boardCode);
		map.put("boardNo", boardNo);
		
		// 로그인 상태일 때 한정으로만 memberNo를 꺼내서 추가하자
		
		if(loginMemeber!=null) {
			map.put("memberNo", loginMemeber.getMemberNo());
		}
		
		
		Board board  = service.selectOne(map); // 게시글 상세조회를 하려는데 board를 받아야해?
		// 한 행의 데이터가 보드로 담겨야하기 때문
		
		
		String path = null;
		
		
		if(board==null) {
			
			path = "redircet:/board/"; 
			// 해당 게시판의 목록으로 재요청 가령 자유게시판 목록으로
			ra.addFlashAttribute("message","게시글이 존재하지 않습니다");
			return "path" ;
		
		}
		
		
		else {
			
			path = "board/boardDetail";
			
			// 게시글의 일반 내용과 imageList + commentList
			model.addAttribute("board",board);
			// src/main/resources/board/boardDetail"
			
			
			// 조회된 이미지 목록이 있을 경우 
			if(!board.getImageList().isEmpty()) {
				BoardImg thumbnail = null;
				// imageList의 0번 인덱스 == 가장 빠른 순서. 
				// 실제 DB에서 IMG_ORDER가 작은것부터 나오게 됨
				// 만약 이미지 목록의 첫번째 행의 image order가 0인경우라면 썸네일이라는 뜻
				
				if(board.getImageList().get(0).getImgOrder()==0) {
					// 썸네일의 조건
					
					thumbnail = board.getImageList().get(0);
				}
				
				model.addAttribute("thumbnail", thumbnail);
				model.addAttribute("start",thumbnail !=null ? 1:0);
				// start라는 값은 썸네일이 있다면 1을 저장, 없으면 0을 저장
			}
			
			return path;
			
		}
		
		
	}
	

}
