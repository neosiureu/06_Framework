package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

// index.html을 첫 화면에 보여주기 싫어서 보여줄 화면을 설정

@Controller // 요청과 응답을 제어하는 역할을 명시하면서 bean으로 등록한다

public class MainController {
	// "/" 주소로 요청이 왔을 때 main.html파일로 forward하는 것
	
	@RequestMapping("/") //원래 잘 안쓰지만 여기서만 쓴다
	public String mainPage() {
		// forward 즉 html등으로 요청 위임
		// 그런데 그 html을 thymeleaf를 이용하여 진행한다.
		
		// .html 확장자 파일을 똑같이 사용하되 스프링 부트에서 사용하는 엔진을 사용하는 것
		// 타입리프를 이용한 html파일로 포워딩할 시 사용되는 접두사, 접미사
		
		
		// 접두사: classpath:/templates/
		// 접미사: .html
		
		
		//	목표 주소는 classpath:/templates/common/main.html
		
		return "common/main";
		
		// 그러면 이렇게 주소를 써라
	}

}

