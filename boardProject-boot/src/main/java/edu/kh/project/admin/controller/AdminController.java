package edu.kh.project.admin.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import edu.kh.project.admin.model.service.AdminService;
import edu.kh.project.member.model.dto.Member;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@SessionAttributes({"loginMember"})
@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("admin")
@Slf4j
@RequiredArgsConstructor
public class AdminController {
	//CORS error => 오리진이 다를 때 발생하는 오류 => url 호스트, 포트가 같으면 발생
	
	
	private final AdminService service;
	 
	
	@PostMapping("login")
	public Member login(@RequestBody Member inputMember, Model model) {
		// email과 pw를 requstbody를 json형태로 넘기게 됨
		// model은 보통 포워딩 하지만 여기서는 그럴일이 없음. 
		// 세션에 올리기 위해 사용
		
		Member logMember = service.login(inputMember);
		
		
		if(logMember==null) {
			
			return null;
		}
		
		model.addAttribute(logMember);
		
		return logMember;
	}
	

	
	/** 관리자 로그아웃
	 * @return
	 */
	@GetMapping("logout")
	public ResponseEntity<String> logout(HttpSession session){
		// Spring에서 제공하는 
		// Http응답 데이터를 커스터마이징 할 수 있도록 지원한다
		// 가령 HTTP 상태코드: 200, 404, 500 등을 자기 마음대로 보냄
		// 응답 헤더나 본문을 모두 설정 가능하다
		
		try {
			session.invalidate(); // 세션 무효화 처리
			// 이상이 없다면 이 뒤를 따름
			return ResponseEntity.status(HttpStatus.OK)
					.body("로그아웃이 완료 되었습니다");
			// OK는 200번

		} catch (Exception e) {
			// 세션 만료시키다가 문제가 생긴 경우
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("로그아웃 중 예외 발생 : "+ e.getMessage());
			

		}
		
	}
	
	
}
