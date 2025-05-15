package edu.kh.project.board.controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.board.model.dto.Board;
import edu.kh.project.board.model.dto.BoardImg;
import edu.kh.project.board.model.service.BoardService;
import edu.kh.project.member.model.dto.Member;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@RequestMapping("board")
@Controller
@Slf4j
public class BoardController {

	@Autowired
	private BoardService service;

	
	
	/*
	 * @param boardCode: 게시판종류에 따라 (1/2/3)
	 * 
	 * @param cp: 현재 조회를 요청한 페이지 번호를 가짐 (없으면 1페이지를 요청한 것과 같다)
	 * 
	 * /board/1 /board/2 /board/3 요청 주소에서 쿼리스트링이 아닌 하위 주소를 주소상 변수로서 사용할 수 있는 방법
	 * pathVariable로 매핑한다 이는 정규식이 들어간다
	 * 
	 * 정규식을 이용했기에 /boadrd이하 1레벨 자리에 숫자로 된 요청 주소가 작성되어있을 때에 한해서 이 메서드로 매핑한다
	 */

	// @PathVariable은 "boardCode"에 대한 값을 requestScope에 실어준다 + 매핑까지 해준다.
	@GetMapping("{boardCode:[0-9]+}") // +가 없으면 한 칸에 한자리수의 숫자가 들어갈 수 있다.
	public String selectBoardList(@PathVariable("boardCode") int boardCode
			, @RequestParam(value = "cp", required = false, defaultValue = "1") int cp, Model model
	/* ====================== 추가적으로 키와 쿼리를 얻어옴 ====================== */
	,@RequestParam Map<String, Object> paraMap)
	/* === paramMap안에는{"query" = "짱구", "key"="tc"} === */

	{
		// board이하 1레벨 하위에 어떤 숫자 주소 값이 들어오더라도 매핑하겠다
		// 조회 서비스 호출 후 결과를 맵으로 반환

		Map<String, Object> map = null;
		
		
		
		
		
		/* ====================== 검색이 아닐 때  ====================== */

		
		/* ========= 검색이 아니라면 paramMap은 {}라는 빈 맵 상태 ================ */

		if(paraMap.get("key") == null){
			

			/* 조건에 따라 어떤 서비스의 메서드를 호출할지 가름.
			 다만 반환되는 것을 Map으로

			 맨 밑에서 하는 검색인 경우와 검색이 아닌 경우를 따진다
			 board ?key=t & query = 1930; => key는 검색어에 해당하며 t또는 c 또는 tc 또는 w로 key가 설정될 수
			 있다

			 검색 역시 게시판의 목록 조회와 똑같으므로 맵으로 넘어온다

			 게시글 목록 조회 서비스 호출하기 */
			

			map = service.selectBoardList(boardCode, cp);
			// 어떤 게시판 종류인지, 어떤 페이지를 요청했는지
			
			
		}
		
		
		
		
		else {
			
			/* ====================== 검색일 때 ====================== */
			
			

			// 검색이 아닐 때는 서비스단으로 넘겨줄 때 boardCode, cp만 넘겨줬었음 paramMap까지 넘겨줘야 하니까 애초에 paramMap에 boardCode를 넣어버려
			
			// boardCode를 paramMap에 추가
			paraMap.put("boardCode", boardCode);
			//paraMap = {"query"="짱구", "key"="tc", "boardCode"=1 }
			
			// cp는 따로 보내도 된다. 페이지네이션은 유지되어야 하기때문
			// cp로 검색서비스에서 페이지네이션을 만든다.
			
			// 검색 서비스 호출
			
			map = service.searchList(paraMap,cp);
			//selectBoardList(boardCode, cp);가 아니라
			//searchList(paraMap,cp)
			
			
			
		}

		
		
		
		

		model.addAttribute("pagination", map.get("pagination"));
		model.addAttribute("boardList", map.get("boardList"));

		return "board/boardList";
		// src/main/resources/templates/board/boardList.html

	}

	// boadr/1?cp=3이라고 보내서 1페이지에서 3페이지로 넘어가게 한다
	// 다만 페이지네이션을 눌렀을 때만 있다. 따라서 Requestparam으로 받되
	// required = false, defaultValue = "1"

	// /board/1/1997?cp=1과 같이 넘어옴

	// 1/1997을 pathVarialbe로 뒤에 cp는 requestParam으로

	/*
	 * 게시물 상세 조회
	 * 
	 * @param boardCode: 주소에 포함된 게시판의 종류 번호 ( ex) 1=공지 )
	 * 
	 * @param boardNo: 주소에 포함된 게시글의 번호 위 둘은 항상 request scope에 실려있다. @PathVariable을
	 * 이용 시 항상 변수 값이 request scope에 저장
	 * 
	 * @param model
	 * 
	 * @param loginMemeber: 로그인 여부와 관련 없이 일단 상세 조회는 할 수 있음 => required false
	 * 
	 * @param ra
	 * 
	 * @return
	 */
	
	
	@GetMapping("{boardCode:[0-9]+}/{boardNo:[0-9]+}")
	public String boardDetail(@PathVariable("boardCode") int boardCode, @PathVariable("boardNo") int boardNo,
			Model model, @SessionAttribute(value = "loginMember", required = false) Member loginMember,
			RedirectAttributes ra, HttpServletRequest req /* 쿠키 얻어오려고 */
			, HttpServletResponse resp /* 새로운 쿠키를 구워 클라이언트로 보낼 때 */) {

		// 게시글 상세 조회 서비스 호출

		/*
		 * SQL문까지 boardCode boardNo를 전달해야 하나만 저장할 수 있다. 따라서 맵으로 묶으면 된다
		 */

		Map<String, Integer> map = new HashMap<>();

		map.put("boardCode", boardCode);
		map.put("boardNo", boardNo);

		// 로그인 상태일 때 한정으로만 memberNo를 꺼내서 추가하자

		if (loginMember != null) {
			map.put("memberNo", loginMember.getMemberNo());
		}

		Board board = service.selectOne(map); // 게시글 상세조회를 하려는데 board를 받아야해?
		// 한 행의 데이터가 보드로 담겨야하기 때문

		String path = null;

		if (board == null) {

			path = "redirect:/board/";
			// 해당 게시판의 목록으로 재요청 가령 자유게시판 목록으로
			ra.addFlashAttribute("message", "게시글이 존재하지 않습니다");
			return path;

		}

		else {

			
			
			/* ====================== 조회수 증가 처리 (쿠키 기반) ====================== */

			// 로그인하지 않았거나, 로그인했더라도 작성자가 아닌 경우만 조회수 증가
			if (loginMember == null || loginMember.getMemberNo() != board.getMemberNo()) {

				// 요청에 포함된 모든 쿠키 가져오기
				Cookie[] cookies = req.getCookies();

				// 게시글 조회 여부를 기록하는 쿠키 변수
				Cookie c = null;

				// 쿠키 배열에서 "readBoardNo"라는 이름의 쿠키를 찾기
				if (cookies != null) {
					for (Cookie temp : cookies) {
						if (temp.getName().equals("readBoardNo")) {
							c = temp;
							break; // 찾으면 더 이상 순회하지 않음
						}
					}
				}

				int result = 0; // 실제로 DB에 조회수를 반영한 결과

				// "readBoardNo" 쿠키가 없는 경우 → 처음 읽는 글
				if (c == null) {
					// 쿠키를 새로 생성하고, 현재 게시글 번호만 기록
					c = new Cookie("readBoardNo", "[" + boardNo + "]");

					// 조회수 1 증가
					result = service.updateReadCount(boardNo);

				} else {
					// "readBoardNo" 쿠키는 있지만, 현재 글 번호가 없으면
					if (c.getValue().indexOf("[" + boardNo + "]") == -1) {
						// 이전에 읽지 않았던 글 → 쿠키에 번호 추가하고 조회수 증가
						
						/*
				         * indexOf(): 쿠키 값에는 [2][5][30] 처럼 이미 조회한 글 번호들이 문자열로 저장되어 있음
				            : 이 중에 현재 글 번호([boardNo])가 포함되어 있는지 검사
				         indexOf("[1982]") == -1 =>  포함되어 있지 않음 => 처음 보는 글임
				         indexOf(...) >= 0      =>  이미 본 글 => 조회수 증가하지 않음
				         */
						c.setValue(c.getValue() + "[" + boardNo + "]");
						result = service.updateReadCount(boardNo);
					}
				}

				// 이후 result > 0일 경우 → 실제 쿠키 설정 및 클라이언트로 전송 등 수행

				// 조회수 증가 처리 후, board 객체의 조회수 필드도 업데이트

				if (result > 0) {

					/* ---------- 조회수 반영 ---------- */
					// DB에서 READ_COUNT를 1 증가시켰으므로,
					// 메모리에 이미 올라와 있는 board 객체에도 동일한 값을 반영해 준다.
					board.setReadCount(result);

					/* ---------- 쿠키 설정 ---------- */
					// (1) 쿠키 전송 범위 : 사이트 전역 “/”
					// → 이후 모든 요청에 이 쿠키가 자동 포함되어 다시 조회수 중복 증가를 막음
					c.setPath("/");

					// (2) 쿠키 만료 시각 : **내일 자정(00:00)**
					// → 같은 날에는 조회수가 한 번만 올라가고,
					// 자정이 지나면 쿠키가 사라져 새로운 날에 다시 1회 증가 가능
					LocalDateTime now = LocalDateTime.now();

					// 다음날 자정까지를 쿠키의 만료 시간으로 지정하자

					LocalDateTime nextDatMidNigh = now.plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
					// 다음날 자정 0시 0분 0초

					// 현재시간부터 다음날 자정까지의 남은 시간을 계산하여 초단위로 표현

					// duration.between

					long seconds = Duration.between(now, nextDatMidNigh).getSeconds(); // 차이를 초로 환산

					// 쿠키수명 설정 시 넣어 주자

					c.setMaxAge((int) seconds);

					resp.addCookie(c);
					// 응답객체가 이미 있으므로 바로 클라이언트에 쿠키 전달 가능

				}

			}

			/* 쿠키를 이용한 조회수 증가 끝 */

			path = "board/boardDetail";

			// 게시글의 일반 내용과 imageList + commentList
			model.addAttribute("board", board);
			// src/main/resources/board/boardDetail"

			// 조회된 이미지 목록이 있을 경우
			if (!board.getImageList().isEmpty()) {
				BoardImg thumbnail = null;
				// imageList의 0번 인덱스 == 가장 빠른 순서.
				// 실제 DB에서 IMG_ORDER가 작은것부터 나오게 됨
				// 만약 이미지 목록의 첫번째 행의 image order가 0인경우라면 썸네일이라는 뜻

				if (board.getImageList().get(0).getImgOrder() == 0) {
					// 썸네일의 조건

					thumbnail = board.getImageList().get(0);
				}

				model.addAttribute("thumbnail", thumbnail);
				model.addAttribute("start", thumbnail != null ? 1 : 0);
				// start라는 값은 썸네일이 있다면 1을 저장, 없으면 0을 저장
			}

			return path;

		}

	}

	/**
	 * 게시글 좋아요 설정 또는 해제
	 * 
	 * @param map
	 * @return
	 */
	@PostMapping("like")
	@ResponseBody
	public int boardLike(@RequestBody Map<String, Integer> map) {
//		log.debug("map" + map);
		return service.boardLike(map);
	}

	
	@GetMapping("updateCompletionSync") // GET 요청으로 
	public String updateCompletionStatusSync(
	        Board board, // boardCode, boardNo, completionStatus가 자동으로 바인딩 이유: DTO 필드 이름 일치
	        @RequestParam(value = "cp", defaultValue = "1") int cp, // cp는 DTO에 없어서
	        @SessionAttribute(value = "loginMember", required = false) Member loginMember,
	        RedirectAttributes ra
	) {
	   

	    // 로그인 체크 (필요하다면)
	    if (loginMember == null) {
	        ra.addFlashAttribute("message", "로그인 후 이용해주세요.");
	        // DTO에서 boardCode와 boardNo 값을 가져와 리다이렉트 URL 생성
	        return "redirect:/board/" + board.getBoardCode() + "/" + board.getBoardNo() + "?cp=" + cp;
	    }

	    // 서비스 메소드에 전달할 Map 생성
	    // DTO에서 필요한 값들을 Map에 담아 서비스에 전달
	    Map<String, Object> paramMap = new HashMap<>();
	    paramMap.put("boardNo", board.getBoardNo());
	    paramMap.put("completionStatus", board.getCompletionStatus()); // DTO에서 상태 값 가져옴

	    // 서비스 호출 (기존 서비스 메소드 updateCompletion 사용)
	    int result = service.updateCompletion(paramMap);

	    String message = null;
	    if (result > 0) {
	        message = "게시글 상태가 성공적으로 변경되었습니다.";
	    } else {
	        message = "게시글 상태 변경 실패.";
	    }

	    ra.addFlashAttribute("message", message);

	    return "redirect:/board/" + board.getBoardCode() + "/" + board.getBoardNo() + "?cp=" + cp;
	}

}
