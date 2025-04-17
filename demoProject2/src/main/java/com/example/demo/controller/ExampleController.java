package com.example.demo.controller;

import java.util.*;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.dto.Student;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("example") // /example로 시작하는 주소를 해당 컨트롤러가 담당할 수 있도록 매핑하겠다
@Slf4j // log.console()을 이용하기 위해 필요하다 (로그 객체 자동 생성을 위함)
public class ExampleController {
	
	@RequestMapping("ex1")
	public String ex1(HttpServletRequest req, Model model) {
		
		// 서블릿 => page < request < session < application
		
		// import org.springframework.ui.Model;
		/*
		 * 스프링에서 데이터 전달 역할을 하는 객체로 기본 스코프가 req임
		 * 
		 * @SessionAttribute와 함계 사용 시 session Scope로 변환한다
		 * 
		 * model.addAttribute("key",value); 
		 * model!= modelAttribute
		 * 자바 단의 메서드에서 만든 변수를 html로 전달하게
		 * */
		
		req.setAttribute("test1", "HttpServletRequest를 이용해 전달한 값");
		model.addAttribute("test2", "model을 이용해 전달한 값");
		// 위 둘은 동일한 동작을 한다. 다만 장점은 세션 범위로 변할 수 있다는 점
		
		// --------------------------------------------------------------------//
		
		// 단일 값(숫자 또는 문자열만) Model을 이용해서 html로 전달
		
		model.addAttribute("productName","종이컵");
		model.addAttribute("price",2000);
				
		// 컬렉션 역시 Model을 이용해서 html로 전달

		List<String> fruitList = new ArrayList<>(); // 원래 개발자가 이런걸 함부로 만들면 안되지만 지금 만들고 쓰면 끝날거라서
		fruitList.add("사과");
		fruitList.add("딸기");
		fruitList.add("바나나");
		
		model.addAttribute("fruitList" , fruitList);
		
		Student std = new Student();
		std.setAge(10);
		std.setName("홍길동");
		std.setStudentNo("2021113420");
		
		model.addAttribute("std",std); // example/ex1.html로 전달 완료
		
		
		// Student제네릭 된 리스트를 Model을 이용하여 html쪽으로 전달한다
		
		List<Student> stdList = new ArrayList<>();
		
		stdList.add(new Student("11111","김일번",20));
		stdList.add(new Student("22222","최이번",23));
		stdList.add(new Student("33333","홍삼번",22));
		
		
		model.addAttribute("stdList",stdList);// example/ex1.html로 전달 완료
		
		return "example/ex1";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//DTO 객체를 model에 실어서 html로 전달
	
	

	@PostMapping("ex2")
	public String ex2(Model model) {
		
		model.addAttribute("str","<h1>테스트 중 &times;</h1>");
		// 이게 html로 텍스트로 전달되면 코드로 해석될까?
		
		return "example/ex2";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//ex3/1은 여기서 몾찾음 ex3/2 ex3/3도 모름

	@GetMapping("ex3")
	public String ex3(Model model) {
		model.addAttribute("key", "제목");
		model.addAttribute("qurey","검색어");
		model.addAttribute("boardNo",10);
		
		return "example/ex3";
	}
	
	@GetMapping("ex3/{path:.+}") // <- 여기들어간 것은 변수명
	public String pathVariableTest(@PathVariable("path") String path) {
		// 컨트롤러에서 해야하는일이 동일한 경우
		// 게시판 같은 경우 항상 응답하는 페이지는 다 똑같지만 example/ex3/1 example/ex3/2 example/ex3/3
		// 주소 중 {path}부분의 값을 가져와 매개변수로 저장함
	
		// 컨트롤러 단의 메서드에서 사용할 수 있게 해줄 뿐 아니라 req 스코프의 변수로 path를 세팅까지 해줌
		
		// 즉 model.addAttribute를 따로 할 필요 없이 갈 경로에서 그냥 써버릴 수 있다
		
		log.debug("path: " + path);
	
		
		return "/example/testResult";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@GetMapping("ex4")
	public String ex4(Model model) {
		
		Student std = new Student("2021", "잠만보",22);
		model.addAttribute("std",std);
		model.addAttribute("num",100);
		
		
		return "/example/ex4";
				
		
	}
	
	
	
	
	
	
	
	
	
	
	
	@GetMapping("ex5")
	public String ex5(Model model) {
		model.addAttribute("message","타입리프에 자바스크립트를 추가로 사용하겠다");
		model.addAttribute("num1",12);
		
		Student std  =new Student();
		std.setStudentNo("22222");
		model.addAttribute("std",std);
			
		return "example/ex5";
		
	}

	
}
