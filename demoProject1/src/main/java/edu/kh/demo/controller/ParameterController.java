package edu.kh.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Controller // 요청 응답 제어 역할 명시 + Bean (객체)로서 이 클래스를 등록하겠다
@RequestMapping("param") // 슬래시 안 붙임!!
// "param"으로 시작하는 요청을 현재 컨트롤러로 매핑

@Slf4j //롬복에서 제공하는 어노테이션. 이는 log를 이용한 메시지를 콘솔창에 출력할 때 사용
public class ParameterController {
	
	// html을 보니 @main인 메서드를 따로 만들겠군
	
	@GetMapping("main") // param/main으로 온 GET 방식 요청을 매핑한다
	public String  paramName() {
		
	return "param/param-main"; // 이는 다음 경로와 똑같음
	// (src/main/resources/templates/) param /param-main (.html)	
		
	}
	

	
	/* 1) HttpServletRequest는 요청된 클라이언트의 정보, 제출된 파라미터 등을 저장한 객체
	 * 클라이언트 요청 시 생성되며 원래는 extends받았었음
	 * 다만 @Controller과 같이 Spring의 컨트롤러에 대한 어노테이션이 달린 메서드 작성 시 
	 * 매개변수에 원하는 객체를 생성하면 
	 * 존재하는 객체를 바인딩한다. 또는 해당 객체가 없으면 스스로 생성하여 바인딩한다
	 * 이를 ArgumentResolver라 한다
	 * */
	
	// param/test1 Post방식 요청을 매핑했다는 의미
	
	@PostMapping("test1") 
	public String paramTest1(HttpServletRequest req) { // 이러한 객체는 기본적으로 bean으로 등록되어있지 않지만 알아서 만들어 줌
		// 주입을 받아서 get을 하게 된다. => 그냥 인자로 필요한 객체를 막 쓰면 된다. (by argument resolver)
		String inputName = req.getParameter("inputName");
		String inputAddress = req.getParameter("inputName");
		int inputAge = Integer.parseInt(req.getParameter("inputAge")); 
		
		log.debug("inputName:" + inputName );
		log.debug("inputAddress:" + inputAddress );
		log.debug("inputAge:" + inputAge);
		// 아까 application.properties에서 설정한 것
		
		//Spring에서 redirect하는 방법 logging.level.edu.kh.demo=debug
		// 쌍따옴표 안에 가장 먼저 redirect: 이라고 알려줘야 한다
		
		return "redirect:/param/main";
	}
	
	// 컨트롤러에 의헤  이 메서드가 매핑 됨
	

}
