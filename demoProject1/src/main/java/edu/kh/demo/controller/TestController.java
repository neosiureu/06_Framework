package edu.kh.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;

// instance: 개발자가 직접 new로 만들고 관리하는 객체
// Bean: 사용자가 아닌 Spring Container가 만들고, 관리하는 객체 Container- core- bean space


// IOC (제어의 역전): 객체의 생성 및 생명주기 및 권한이 개발자가 아닌 프레임워크에 있는 것
// IOC + MVC와 관련된 것들 중 컨트롤러가 들어가는 클래스 =>  컨트롤러 어노테이션을 붙여야 한다

@Controller // 요청, 응답을 제어하는 역할이라는 것을 서버에 알려주고 빈으로 등록까지 한다
public class TestController {
	// 원래 서버는 실행될 때 new Testcontroller()로 실행됐었다.
	// 하지만 이제는 Spring이 이를 객체화하여 관리할 것

	// 컨테이너 코어에 이미 bean을 이 객체가 만들어져 쌓여 있다
	
	// 기존 서블릿 vs 이제 배울 스프링
	
	// 기존 서블릿: 클래스 단위로 하나의 요청만 처리 가능하다. 클래스 하나를 만들어 @WebServlet으로 하나의 요청만 가능했다
	// 이제는 하나의 클래스 단위에서 여러 요청 주소를 매핑할 수 있다
	
	// @RequestMapping ("요청주소")
	// 요청주소를 처리할 메서드를 매핑하는 어노테이션
	
	// 물론 메서드, 클래스에 대해 모두 가능
	
	// 1) 메서드에 @RequestMapping
	
	
	//@RequestMapping(value = "/test", method = RequestMethod.GET) // test 요청 시 testMethod가 따로 매핑하여 처리함
	// 이렇게 하면 요청 주소와 해당 메서드를 매핑하는데 get 과 post 가리지 않고 매핑한다 
	// => @RequestMapping중 속성을 통하여 지정하거나 다른 어노테이션이 있어 그를 통해 get과 post 매핑을 명시한다
	
	@RequestMapping("/test")
	public String testMethod() {
		System.out.println("/test에 대한 요청을 받았습니다. ");

	
		
		/*
		 * 컨트롤러는 항상 메서드로서 String타입을 반환한다
		 * 메서드에서 반환되는 문자열이 forward할 html파일의 경로가 된다 => 타입리프 엔진의 동작방식 때문이다.
		 * Thymeleaf는 JSP와 유사하지만 html의 형태로 뷰 단의 모습을 작성할 수 있는 템플릿 엔진이다.
		 * */

		
		return "test" ;
		// src/main/resources/templates/test.html을 찾아간 셈
		
		// 사실 "test"대신 실제로는 생략된 접두사, 접미사가 있음 
		// => classpath:/templates/가 접두사로서 생략. 
		// .html이 접미사로서 생략
		// classpath = src/main/resources		
		
		// 이 접두사와 접미사는 고정이며 정 바꾸고 싶다면 접두사, 접미사, forward하는 설정은 View Resolver 객체가 담당하므로 그를 조절해야 함
		
		
		// 1) 클라이언트의 get 요청 (test) 
		// 2) dispatcher Servleㅅ이 받아서 매핑하기 위해 Handler Mappung이 어느 컨트롤러로 보낼지 등록된 bean으로 통해 알라냄 => 결과값을 dispatcher servlet에 
		// 3) 컨트롤러로 보냄 => 뷰 리졸버를 통해 templates내의 html파일에 대한 뷰를 선택한다. 컨트롤러가 리턴 값에 담아서 보낸 (접두 접미 합친)것이 경로 +> 해당 뷰를 화면에 보여줌
		
	}
	

	
	
	
	// 2) 클래스와 메서드에 함께 @RequestMapping
	
	// 공통주소를 매핑할 때 둘 다 사용
	// 가령 /todo/insert라고 만들거나 /todo/select, /todo/update와 같이 만들었을 것이다
	
	// todo에 관련된 것들을 하나의 클래스로 관리하기 용이함
	
//	@RequestMapping("/insert")
//	public Sring insertTodo () {} 이것만 해도 같은 폴더 안에 모아놓을 수 있음
	
//	@RequestMapping("/select")
//	public Sring selectTodo () {} 이것만 해도 같은 폴더 안에 모아놓을 수 있음	
	
	
}
