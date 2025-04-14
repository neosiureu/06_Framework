package edu.kh.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller// 요청/ 응답제어 역할명시 + Bean 등록
public class ExampleController {

	// 요청주소 
	// todo/test/update , todo/test/put  , todo/delete 
	
	  // 하게되면 아래에 test/update 만 써도됨
	
	
	/* 1) RequestMapping("주소")
	 * 
	 * 2) @GetMapping("주소")    : Get(조회) 방식 요청 매핑 
	 * 	  @PostMapping("주소")   : post(삽입) 방식 요청 매핑
	 * 	  @PutMapping("주소")    : put (수정) 방식 요청 매핑   (form, a 태그 요청불가)
	 * 	  @DeleteMapping("주소") : Delete(삭제) 방식 요청 매핑 (form, a 태그 요청불가)
	 * 
	 *   
	 * 
	 * */
	
	/* "/" 를 앞에 안붙이는 이유
	 * 
	 * 1)Spring Boot 에서는 요청 주소 앞에 "/" 가 없어도
	 *  요청 처리가 잘된다! (오류 발생 X)
	 * 
	 * 2)프로젝트를 AWS 같은 호스팅 서비스를 이용하여 베포시 
	 *   Build 과정에서 리눅스 os를 이용하면 오류 발생. ..
 	 * 
	 * */
	
	
	@GetMapping("example") //  /example GET 방식 요청 매핑 
	                         // 웹사이트 베포할때 / 붙여야 AWS 에서 제공해주는 
	                         // 무료 Lynux를 이용할때 오류가 안남 ..
	public String examplemethod() {
		
		// forward 하려는 html 파일 경로 return 작성
		// 단, ViewResolver가 제공하는
		// 타임리프의 접두사, 접미사 제외하고 작성
		
		
		//접두사 : classpath:/templates/
		//접미사 : .html
		return "example";
		//classpath:/templates/example.html
	}
	
}
