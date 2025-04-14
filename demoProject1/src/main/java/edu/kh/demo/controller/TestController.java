package edu.kh.demo.controller;


import edu.kh.demo.DemoProject1Application;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

// instance : 개발자가 직접 new 연산자를 통해 만든 객체, 관리하는 객체

// Bean : Spring Container가 만들고 , 관리하는 객체

// IOC (제어의 반전) : 객체의 생성 및 생명주기의 권한이 개발자가 아닌, 프레임 워크에게 있다.

@Controller // 요청/ 응답을 제어하는 역할인 컨트롤러임을 명시 + Bean 등록
public class TestController {

	
	// 기존 Servlet : 클래스 단위로 하나의 요청만 처리 가능
	// Spring : 메서드 단위로 요청 처리 가능
	
	// @RequestMapping("요청주소")
	// - 요청 주소를 처리할 메서드를 매핑하는 어노테이션
	
    
	// 1) 메서드에 작성 :
    // - 요청 주소와 해당 메서드를 매핑
    // - GET/POST 가리지 않고 매핑(속성을 통해서 지정 가능 or 다른 어노테이션 이용)
    
    //@RequestMapping(value="/test",method = RequestMethod.GET) // /test 요청 시 testMethod가 매핑하여 처리함
    @RequestMapping("/test")
	public String testMethod() {
		
    	System.out.println("/test 요청 받음");
    	
    	
    	/*
    	 * Controller 메서드의 반환형이 String 인 이유!
    	 * -> 메서드에서 반환되는 문자열이
    	 *    forward할 html 파일의 경로가 되기 때문!
    	 *    
    	 *Thymeleaf : JSP 대신 사용하는 템플릿 엔진 (html 형태) 
    	 * 
    	 * classpath : == src/main/resources
    	 * 접두사 : classpath:/templates/
    	 * 접미사: .html
    	 * 
    	 * */
    	
    	//src/main/resources/templates/test.html
		return "test"; // 접두사 + 반환값 + 접미사 경로의 html로 forward
		
		/* 접두사, 접미사, forward 설정은 View Resolver 객체가 담당*/
	}
	
	// 2) 클래스와 메서드에 함께 작성 :
    // - 공통 주소를 매핑 
    // ex) /todo/insert,  /todo/select , /todo/update .....
   
    /*
    @RequestMapping("/todo")
    public class TestController {
    @RequestMapping("/insert")
    public String 메서드명() {}

    
    @RequestMapping("/select")
    public String 메서드명() {}

    
    @RequestMapping("/update")
    public String 메서드명() {}

	*/
}
