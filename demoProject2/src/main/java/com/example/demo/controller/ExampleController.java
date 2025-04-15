package com.example.demo.controller;

import java.util.*;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


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
		
		
		return "example/ex1";
	}
	
	

}
