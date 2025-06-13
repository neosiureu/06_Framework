package edu.kh.project.admin.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import edu.kh.project.admin.model.service.AdminService;
import edu.kh.project.board.model.dto.Board;
import edu.kh.project.member.model.dto.Member;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin(origins="http://localhost:5173" /*, allowCredentials = "true"*/) //리엑트에서의 서버와 통신할때 포트가 달라서이 오리진과도 통신하기위해서
@RequestMapping("admin")						// 클라이언트에 들어오는 쿠키를 허용하겠다
@Slf4j
@SessionAttributes({"loginMember"})
@RequiredArgsConstructor
public class AdminController {

	private final AdminService service;
	
	@PostMapping("login")
	public Member login(@RequestBody Member inputMember, Model model) {
		
		Member loginMember = service.login(inputMember);
		
		if(loginMember == null) {
			return null;
		}
			model.addAttribute(loginMember);
			return loginMember;
			
		}
	
	/** 관리자 로그아웃
	 * @return
	 */
	/**
	 * @param session
	 * @return
	 */
	@GetMapping("logout")
	public ResponseEntity<String> logout(HttpSession session) {
		 
		//ResponseEntity
		// Sprint 에서 제공하는 Http 응답 데이터를
		//커스터마이징 할수 있도록 지원하는 클래스
		// -> Http 상태코드 ,헤더, 응답 본문 (body)을 모두 설정 가능
		try {
			session.invalidate();
			return ResponseEntity.status(HttpStatus.OK)
					.body("로그아웃이 완료되었습니다."); // 200
			
		}catch(Exception e) {
			// 세션 무효화중 예외 발생한 경우
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("로그아웃중 예외 발생 : " + e.getMessage()); // 500 에러
		}
		
		// -----------------------통계 ----------------------------------
		
	
			
		}
		
		
	/** 최대 조회수
	 * @return
	 */
	@GetMapping("maxReadCount")
	public ResponseEntity<Object> maxReadCount(){
		
		try {
			Board board = service.maxReadCount();
			return ResponseEntity.status(HttpStatus.OK).body(board);
			
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
		
		
	}
		
	/** 최대 좋아요 
	 * @return
	 */
	@GetMapping("maxLikeCount")
	public ResponseEntity<Object> maxLikeCount(){
		
		try {
			Board board = service.maxLikeCount();
			return ResponseEntity.status(HttpStatus.OK).body(board);
			
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
		
		
	}
	
	/** 최대 댓글수 
	 * @return
	 */
	@GetMapping("maxCommentCount")
	public ResponseEntity<Object> maxCommentCount(){
		
		try {
			Board board = service.maxCommentCount();
			return ResponseEntity.status(HttpStatus.OK).body(board);
			
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
		
		
	}
	
	@GetMapping("getNewMember")
	public ResponseEntity<List<Member>> getNewMember(){
		
		try {
			List<Member> memberList = service.getNewMember();
			return ResponseEntity.status(HttpStatus.OK).body(memberList);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
		}
	}
	
	
	@GetMapping("withdrawnMemberList")
	public ResponseEntity<Object> selectWithdrawnMemberList(){
		// 성공시 List<Member> 반환 , 실패시 String 반환 -> Object 사용
		try {
			List<Member> memberList = service.selectWithdrawnMemberList();
			return ResponseEntity.status(HttpStatus.OK).body(memberList);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("탈퇴한 회원 목록 조회중 문제발생" + e.getMessage());
		}
		
		
	}
	
	@GetMapping("deleteBoardList")
	public ResponseEntity<Object> selectWithdrawnboardList(){
		// 성공시 List<Member> 반환 , 실패시 String 반환 -> Object 사용
		try {
			List<Board> boardList = service.selectWithdrawnboardList();
			return ResponseEntity.status(HttpStatus.OK).body(boardList);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("삭제한 게시글 목록 조회중 문제발생" + e.getMessage());
		}
		
		
	}
	@PutMapping("restoreMember")
	public ResponseEntity<String> restoreMember(@RequestBody Member member) {
		
		try {
		   int result =	service.restoreMember(member.getMemberNo());
		   
		   if(result > 0) {
			   
			   return ResponseEntity.status(HttpStatus.OK).body(member.getMemberNo() + "회원복구완료");
			   
		   }else {
			   //BAD_REQUEST : 400 -> 요청 구문이 잘못되었거나 유효하지 않음
			   return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					   .body("유효하지않은 memberNo" + member.getMemberNo());
		   }
			
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("탈퇴회원 복구중 에러발생");
		}
		
	}
	
	
	@PutMapping("restoreBoard")
	public ResponseEntity<String> restoreBoard(@RequestBody Board board) {
		
		try {
		   int result =	service.restoreBoard(board.getBoardNo());
		   
		   if(result > 0) {
			   
			   return ResponseEntity.status(HttpStatus.OK).body(board.getBoardNo() + "회원복구완료");
			   
		   }else {
			   //BAD_REQUEST : 400 -> 요청 구문이 잘못되었거나 유효하지 않음
			   return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					   .body("유효하지않은 BoardNo" + board.getBoardNo());
		   }
			
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("삭제된게시판 복구중 에러발생");
		}
		
	}
	
	/** 관리자 계정 발급
	 * @param member
	 * @return
	 */
	@PostMapping("createAdminAccount")
	public ResponseEntity<String> createAdminAccount(@RequestBody Member member) {
		
		try {
			//1 . 기존에있는 이메일인지 검사
			
			int checkEmail = service.checkEmail(member.getMemberEmail());
			
			if (checkEmail > 0) {
				// ResponseEntity.status(HttpStatus.CONFLICT (409) : 요청이 서버의 현재 상태와 출돌할떄 사용
				// == 이미 존재하는 리소스(email) 때문에 새로운 리소스를 만들수 없다.
				return ResponseEntity.status(HttpStatus.CONFLICT).body("중복된 이메일입니다."); // 이것도 리액트의 error에 잡힘
			}
			
			// 3. 없으면 새로 발급
			String accountPw = service.createAdminAccount(member);
			
			// HttpStatus.OK (200) : 요청이 정상적으로 처리되었으나 기존 리소스에 대한 단순처리
			// HttpStatus.CREATED (201) : 자원이 성공적으로 생성되었음을 나타냄 
			return ResponseEntity.status(HttpStatus.CREATED).body(accountPw);  
			
			
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)// 500
					.body("관리장 계정 생성중 문제 발생(서버확인 바람)");
			
		}
	}
	
	@GetMapping("getadminList")
	public ResponseEntity<List<Member>> getadminList() {
		
		try {
			List<Member> memberList = service.getadminList();
			
			if (memberList.isEmpty()) {
	            return ResponseEntity.noContent().build(); // 204 No Content
	        }
	        
	        return ResponseEntity.ok(memberList); // 200 OK
			
		}catch(Exception e) {
				
			return	ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
			
		}
			
			
		
	}
	
}
	

	

