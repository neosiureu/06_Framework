package edu.kh.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.kh.demo.model.dto.Student;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("example")
@Slf4j  //lombok 라이브러리가 제공하는 로그 객체 자동 생성 어노 테이션
public class ExampleController {
	
	/*
	 * Model(import org.springframework.ui.Model;)
	 * - Spring에서 데이터 전달 역할을 하는 객체
	 * 
	 * - 기본 scope : request
	 * 
	 * - @sessionAttribute 와 함께 사용시 session scope 변환
	 * 
	 * [기본 사용법]
	 * model.addAttribut ("key",value);
	 * 
	 * 
	 * */

	@GetMapping("ex1") // /example/ex1 GET 방식 요청 매핑
	public String ex1(HttpServletRequest req , Model model) {
		
		// servlet 내장객체 범위
		// page < request < session < application
		
		req.setAttribute("test1", "HttpServletRequest로 전달한 값");
		model.addAttribute("test2","model로 전달한 값");
		
		//단일 값 (숫자, 문자열) Model을 이용해서 html로 전달
		model.addAttribute("productName","종이컵");
		model.addAttribute("price",2000);
		
		// 복수 값 (배열, List) Model 이용해서 html로 전달
		List<String> fruitList = new ArrayList<>();
		fruitList.add("사과");
		fruitList.add("딸기");
		fruitList.add("바나나");
		
		model.addAttribute("fruitList",fruitList);
		
		//DTO 객체 model을 이용해서 html로 전달
		Student std = new Student();
		
		std.setStudentNo("12345");
		std.setName("홍길동");
		std.setAge(20);
		
		model.addAttribute("std",std); 
		
		//List<Student> 객체 Model 을 이용해서 html로 전달
		List<Student> stdList = new ArrayList<>();
		
		stdList.add(new Student("1111","김일번",20));
		stdList.add(new Student("2222","촤이번",30));
		stdList.add(new Student("3333","홍삼번",25));
		
		model.addAttribute("stdList",stdList);                           
		
		
		
		
		
		
		//src/main/resources/templates/example/ex1.html 로 forward
		return "example/ex1";
	}
	
	
	@PostMapping("ex2") // /example/ex2 Post 방식 요청 매핑
	public String ex2(Model model) {
		
		model.addAttribute("str","<h1>테스트중 &times;</h1>");
		
		return"example/ex2";
	}
	
	@GetMapping("ex3")
	public String ex3(Model model) {
		
		model.addAttribute("key","제목");
		model.addAttribute("query","검색어");
		model.addAttribute("boardNo",10);
		
		
		return"example/ex3";
	}
	
	@GetMapping("ex3/{path}")
	public String pathVariableTest(@PathVariable("path") int path ) {
     
	//controller에서 해야하는일 동일한 경우에
	 // exmaple/ex3/1 , example/ex3/2 , example/ex3/3 ..
	 // 주소중 {path} 부분의 값을 가져와서 매개변수로 저장
	 // Controller 단의 메서드에서 사용할 수 있또록 해줌
	 // + request scope 자동 세팅
		
				log.debug("path : " + path);
			
				return "example/testResult";
	}
	
	@GetMapping("ex4")
	public String pathVariableTest(Model model) {
		
		Student std = new Student("6879","잠만보",22);
		
		model.addAttribute("std",std);
		
		model.addAttribute("num",300);
		
		
		return"example/ex4";
	}
	
	@GetMapping("ex5")
	public String ex5(Model model) {
		
		model.addAttribute("message","타임리프+ javaScript 사용연습");
		model.addAttribute("num1",12345);
		
		Student std = new Student();
		std.setStudentNo("2222");
		model.addAttribute("std",std);
		
		return"example/ex5";
	}
	
}
