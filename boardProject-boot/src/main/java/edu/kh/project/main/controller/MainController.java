package edu.kh.project.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;



@Controller // 요청과 응답을 제어하는 역할 + Bean으로 등록
public class MainController {
	
	
	@RequestMapping("/") // 최상위주소로 오면 이 메서드로 매핑하겠다
	public String mainPage() {
		
		// classpath:/templates/
		return "common/main";
		// .html
	}
	
	
	// 로그인 안 되어 있을 때 loginError로 리다이렉트하며 해당 코드를 실행
	// -> 이 컨트롤러의 역할은 메시지를 만들어 메인 페이지로 리다이렉트 시키는 것
	@GetMapping("loginError")
	public String loginError(RedirectAttributes ra) {
		
		ra.addFlashAttribute("message","로그인 후 이용 해주세요");
		
		return "redirect:/";
		
	}
	
	
	

}
