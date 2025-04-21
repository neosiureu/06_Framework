package edu.kh.project.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.member.model.service.MemberService;
import lombok.extern.slf4j.Slf4j;

@SessionAttributes({"loginMember"})
@Controller
@RequestMapping("member")
@Slf4j
public class MemberController {
	
	@Autowired
	private MemberService service;
	
	/*
	 * [로그인]
	 * 정의: 특정 사이트에 아이디/ 비밀번호 등을 입력하여 해당 정보가 DB에 있으면 조회하거나 서비스를 이용가능하게 함
	 * 로그인한 회원의 정보는 session객체에 기록하여 
	 * 로그아웃 (세션 만료) 또는 브라우저 종료 시 까지 해당 정보를 계속 이용할 수 있게 해야 한다 
	 * */
	
	
	
	
	/** login method: command 객체 
	 * (@ModelAttribute로 불러온 놈 => memberEmail, memberPw가 이미 세팅된 상태)
	 * @param inputMember 
	 * @param ra: 리다이렉트 시 일시적으로 세션 스코프로 바꾸지만 리다이렉트가 끝나면 사라지는 정보
	 * @param model: 데이터 전달용 객체로 기본적으로 request 범위의 객체이지만 
	 * SessionAttribute라는 어노테이션과 함께 사용 시 session scope에 객체를 담을 수 있다.
	 * @return 
	 */
	
	@PostMapping("login")
	// 동기식 요청
	public String login(Member inputMember, RedirectAttributes ra, Model model) {
		
		// 로그인 서비스 호출
		Member loginMember = service.login(inputMember);
		
		// 로그인 실패시
		
		if(loginMember ==null) {
			ra.addFlashAttribute("message", "아이디 또는 비밀번호가 일치하지 않습니다");
			
		}
		
	
		// 위 조건을 통과한 성공 시 
		
		else {
			// 원래 model.이면 request이지만 
			model.addAttribute("loginMember",loginMember);
			// 이름을 html에서 이렇게 해놨기 때문에 
			//  <th:block th:if="${session.loginMember==null}">

			// 여기까지가 1단계로 model을 이용하여 request scope에 세팅
			
			
			// 2단계: 클래스 위에 @SessionAttributes() 어노테이션을 사용하여 loginMember를
			// 리퀘스트에서 세션 스코프로 바꿈
			

		}
		
		return "redirect:/"; //메인페이지 재요청
	}

}
