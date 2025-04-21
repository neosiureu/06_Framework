package edu.kh.project.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller // 요청과 응답을 제어하는 역할 + Bean으로 등록
public class MainController {
	
	
	@RequestMapping("/") // 최상위주소로 오면 이 메서드로 매핑하겠다
	public String mainPage() {
		
		// classpath:/templates/
		return "common/main";
		// .html
		
	}
	
	
	

}
