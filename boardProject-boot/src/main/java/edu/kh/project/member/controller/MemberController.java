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

@SessionAttributes({"loginMember"}) // 여러개를 session에다가 실을수있어서 {} 안에서 쓰면됨.
@Controller
@RequestMapping("member")
@Slf4j
public class MemberController {
	
	@Autowired // Spring에서 제공하는 의존성 주입 어노테이션
	private MemberService service;
	
	/* [로그인]
	 * -특정 사이트에 아이디/ 비밀번호 입력 후
	 *  해당 정보가 DB 에 있으면 조회/ 서비스 이용
	 *  
	 *  - 로그인 한 회원 정보는 session에 기록하여 
	 *  로그아웃 또는 브라우저 종료 시 까지 
	 *  해당 정보를 계속 이용할 수 잇게 함.
	 * 
	 * */
	
	
	
	
	/** 로그인
	 * @param inputMember : 커맨드 객체 (@ModelAttribute 생략)
	 *                      memberEmail, memberPw 세팅된 상태
	 * 
	 * @param ra : 리다이렉트시 request scope -> session scope -> request 로 데이터 전달 
	 * @param model : 데이터 전달용 객체 (기본 request scope)
	 *					/ (@SessionAttributes 어노테이션과 함께 사용시 session scope 이동)
	 * @return 
	 */
	
	@PostMapping("login")
	public String login(Member inputMember , RedirectAttributes ra , Model model) {
		
		// 로그인 서비스 호출
		Member loginMember = service.login(inputMember);
		
		// 로그인 실패시
		
		if (loginMember == null) {
			ra.addFlashAttribute("message" , "아이디 또는 비밀번호가 일치하지 않습니다.");
			
			
		}else {// 로그인 성공시
			
			//session scope에 loginMember 추가
			
			model.addAttribute("loginMember",loginMember);
			// 1단계 : model을 이용하여 request scope에 세팅됨
			// 2단계 : 클래스위에 @SessionAttributes() 어노테이션 작성하여
			//                    session scopel loginMember를 이동
			
			
		}
		
		return "redirect:/"; // 메인 페이지로 리다이렉트
	}

}
