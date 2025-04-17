package edu.kh.todo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.kh.todo.model.dto.Todo;
import edu.kh.todo.model.service.TodoService;
import lombok.extern.slf4j.Slf4j;

// 참고로 이제 DAO 대신 Mapper라는 이름을 씀

@Slf4j 
@Controller // (요청과 응답에 대한 제어를 명시함과 동시에 Bean에 등록한다. 즉 @Bean안해도 됨)
public class MainController {
	
	@Autowired
	private TodoService service;
	// new TodoServiceInpl()
	
	
	
	@RequestMapping("/") //get post상관없이 /로 오면 내가 처리해줄게
	public String mainPage(Model model) {
		
		log.debug("service의 객체에 대한 주소 값: " + service);
		
		// todoNo가 1인 todo의 제목을 조회하여 requestScope에 추가해보자
		String testTitle  =  service.testTitle();
		model.addAttribute("testTitle",testTitle);
		
		// 리퀘스트 스코프에 넣으려면 model이 필요
		
		
		
	
		
		// TB_TODO 테이블에 저장된 전체 할일 조회하기 
		// 완료된 할 일의 개수
		Map<String, Object> map = service.selectAll();
		
		// 일단 Map으로 올라온 내용을 추출
		
		List<Todo> todoList = (List<Todo>) map.get("todoList");
		int completeCount = (int) map.get("completeCount");
		
		
		
		model.addAttribute("todoList",todoList);
		model.addAttribute("completeCount",completeCount);
		
		
		// src/main/resouces/templates/
		return "common/main";
		// .html
	}
	

}
