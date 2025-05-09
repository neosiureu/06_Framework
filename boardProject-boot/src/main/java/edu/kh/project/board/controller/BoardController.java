package edu.kh.project.board.controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
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
			                        Model model ,
			                      @RequestParam Map<String , Object> paramMap) {
	
		// paramMap = {"query" = "짱구" , "key" = "tc"..}
		
		// 조회 서비스 호출 후 결과 반한 받기.
		
		Map<String, Object> map = null;
		
		// 조건에 따라 서비스 메서드 분기처리 하기 위해 map 은 선언만 함.
		
		
		// 검색이 아닌경우
		if(paramMap.get("key") == null) {
			// 게시글 목록 조회 서비스 호출
			map = service.selectBoardList(boardCode, cp);
			
		
		}else {// 검색인 경우
			
			//boardCode를 paramMap에 추가
			paramMap.put("boardCode", boardCode);
			
			//검색 서비스 호출
			map = service.searchList(paramMap,cp);
			
			
			
		}
		
	
		
		
		
 		
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
			                  RedirectAttributes ra,
			                  HttpServletRequest req, //요청에 담긴 쿠키 얻어오기
			                  HttpServletResponse resp // 새로운 쿠키 만들어서 응답하기
			                                          ) {
			 
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
			
			/*--------------쿠키를 이용한 조회수 증가--------------------*/
			
			//비회원 또는 로그인한 회원의 글이 아닌경우 (== 글쓴이를 뺀 다른 사람)

			if(loginMember == null || loginMember.getMemberNo() != board.getMemberNo()) {
				
				//요청에 담겨있는 모든 쿠키 얻어오기
				Cookie[] cookies = req.getCookies();
				
				Cookie c = null;
				
				for(Cookie temp : cookies) {
					
					//요청에 담긴 쿠키에 "readBoardNo"가 존재할때
					
					if(temp.getName().equals("readBoardNo")) {
						c = temp;
						break;
					}
				}
				
				int result = 0; // 조회수 증가 결과를 저장할 변수
				
				// "readBoardNo" 가 쿠키에 없응ㄹ떄
				if(c == null) {
					
					// 새쿠키 생성"readBoardNo",[게시글번호])
					c = new Cookie("readBoardNo", "[" + boardNo + "]");
					result = service.updateReadCount(boardNo);
					
				}else {
					// "readBoardNo"가 쿠키에 있을때
					// "readBoardNo" : [2] [30][400]
					
					// 현재 게시글을 처음 읽는 경우
					if(c.getValue().indexOf( "[" + boardNo + "]") == -1) {
						
						
						// 해당 글 번호를 쿠키에 누적 + 서비스 호출
						c.setValue(c.getValue() + "[" + boardNo + "]");
						result = service.updateReadCount(boardNo);
					}
					
				}
				
				//조회 수 증가 성공/ 조회 성공 시 
				
				if(result > 0) {
					
					//먼저 조회된 board의 readCount 값을 
					//result 값으로 다시세팅
					board.setReadCount(result);
					
					// 쿠키 적용 경록 설정
					 c.setPath("/");    // "/" 이하 경로 요청 시 쿠키 서버로 전달
					 
					 // 쿠키 수명 지정
					 // 현재 시간을 얻어오기
					 LocalDateTime now = LocalDateTime.now();
					 
					 // 다음날 자정 지정
					 LocalDateTime nextDayMidnight = now.plusDays(1).withHour(0).withMinute(0)
							 .withSecond(0).withNano(0);
					 
					 //현재 시간부터 다음날 자정까지 남은 시간 계산 (초단위)
					long seconds = Duration.between(now, nextDayMidnight).getSeconds();
					 
					// 쿠키 수명 설정
					c.setMaxAge((int)seconds);
					
					resp.addCookie(c); // 응답 객체를 이용해서 클라이언트에게 쿠키 전달
					
				}
				
				
			}
			
			/*--------------쿠키를 이용한 조회수 끝--------------------*/
			
			
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

	
	


	/** 게시글 좋아요 체크 /해제
	 * @param map
	 * @return
	 */
	@ResponseBody
	@PostMapping("like")
	public int boardLike(@RequestBody Map<String, Integer> map) {
		
		return service.boardLike(map);
	}
	
	
}
