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

@Controller // 요청 / 응답 제어 역할 명시 + Bean 등록
@Slf4j // Lombok을 이용한 로그 객체 생성
public class MainController {

	@Autowired 
	private TodoService service;
	
	
	@RequestMapping("/")
	public String mainPage(Model model) {
		
		log.debug("service : "+ service);
		
		// todoNo 가 1인 todo의 제목 조회하여 request scope 추가

		String testTitle = service.testTitle();
		
		model.addAttribute("testTitle", testTitle);
		
		//--------------------------------------------------
		
		// TB_TODO 테이블에 저장된 전체 할일 목록 조회하기
		// + 완료된 할일 개수
		
		// Service 메서드 호출 후 결과 반환 받기
		Map<String, Object> map = service.selectAll();
		
		// map에 담긴 내용 추출
		List<Todo> todoList = (List<Todo>)map.get("todoList");
		
		int completeCount = (int)map.get("completeCount");
		
		model.addAttribute("todoList",todoList);
		model.addAttribute("completeCount",completeCount);
		
		
		
		// Modle을 이용해서 조회 결과 request scope에 추가
		
	
		//src/main/resources/templates/common/main.html
		return "common/main";
	}
	
}

