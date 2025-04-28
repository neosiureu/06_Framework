package edu.kh.project.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller // 해당 클래스가 Controller임을 명시 + Bean으로 등록
// 해당 클래스의 모든 메서드에 /가 붙음
public class MainController {
	
	@RequestMapping("/") // "/" 요청 매핑
	public String mainPage() {
		
		//foward 하겟다
		//경로 : src/main/resources/templates/common/main.html
		return "common/main";
	}
	
	// LoginFilter -> 로그인 안되어있을때 loginError 리다이렉트
	//-> message 만들어서 메인페이지로 리다이렉트
	@GetMapping("loginError")
	public String loginError(RedirectAttributes ra) {
		
		ra.addFlashAttribute("message","로그인 후 이용해주세요.");
		
		return"redirect:/";
	}
	
	
	
	
	
	
}
