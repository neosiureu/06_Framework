package edu.kh.demo.controller;



import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Controller // 요청 / 응답 제어 역할 명시 + Bean 등록
@RequestMapping("param")  // /param으로 시작하는 요청을 현재 컨트롤러로 매핑
@Slf4j // log를 이용한 메시지 콘솔창에 출력할 때 사용 (lombok 제공)
public class ParameterController {
	
	
	
	
	@GetMapping("main") //      /param/main GET 방식 요청 매핑
	public String paramMain() {
		
		//src/main/resources/templates/param/param-main.html 로 forward
				                  //접두사 는 templates 까지 
		                          // 접미사는 .html
		
		return"param/param-main";
	}
	
	/*
	 * 1. HttpServletRequest : 
	 * - 요청 클라이언트의 정보 , 제출된 파라미터 등을 저장한 객체
	 * - 클라이언트 요청 시 생성
	 * 
	 * Spring 의 Controller 메서드 작성시
	 * 매개변수에 원하는 객체를 작성하면
	 * 존재하는 객체를 바인딩 또는 없으면 생성해서 바인딩
	 * 
	 * --> ArgumentResolver(전달 인자 해결사) 
	 * */
	
	@PostMapping("test1") // /param/test1 의 post 방식 요청 매핑
	public String paramTest1(HttpServletRequest req) { // -< bean으로 등록된건 아님 .
		
		
	String inputName = req.getParameter("inputName");
	String inputAddress = req.getParameter("inputAddress");
	int inputAge = Integer.parseInt(req.getParameter("inputAge"));
	
	log.debug("inputName : "+inputName);
	log.debug("inputAddres : "+inputAddress);
	log.debug("inputAge : "+inputAge);
	
	/*
	 * Spring에서 Redirect(재요청) 하는 방법!
	 * 
	 * - Controller 메서드 반환값에
	 *  "redirect:요청주소"; 작성
	 * 
	 * */
	
		return "redirect:/param/main";
	}




}

