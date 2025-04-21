package edu.kh.project.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller // 해당 클래스가 Controller임을 명시 + Bean으로 등록
// 해당 클래스의 모든 메서드에 /가 붙음
public class MainController {
	
	@RequestMapping("/") // "/" 요청 매핑
	public String mainPage() {
		
		//foward 하겟다
		//경로 : src/main/resources/templates/common/main.html
		return "common/main";
	}
}
