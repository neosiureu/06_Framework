package edu.kh.todo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.todo.model.dto.Todo;
import edu.kh.todo.model.service.TodoService;

@Controller
@RequestMapping("todo") // "/todo" 로 시작하는 모든 요청 매핑
public class TodoController {
	
	@Autowired // 의존성 주입 (같은 타입 + 상속관계인 Bean을 의존성 주입:DI)
	private TodoService service;
	
	@PostMapping("add") //  /todo/add 로 Post 방식 요청 매핑
	public String addTodo(@RequestParam("todoTitle") String todoTitle, 
						  @RequestParam("todoContent") String todoContent,
						  RedirectAttributes ra) {
							// 사실 Model을 그냥 넣어버리면 그만임 @ModelAttribute Todo todo로 인자를 넣고 todo.으로 접근하는 것도 좋은 아이디어
		
		// RedirectAttributes : 리다이렉트 시 값을 1회성으로 전달하는 객체
		// RedirectAttributes.addFlashAttribute("key", value) 형식으로 세팅.
		// -> request scope -> session scope 로 잠시 변환
		
		// 응답 전 : request scope
		// redirect : session scope 로 이동 -> 사용
		// 응답까지 끝나고 난 후 : request scope 복귀 
		
		// 서비스 메서드 호출 후 결과 반환 받기
		int result = service.addTodo(todoTitle, todoContent);
	
		
		// 결과에 따라 message 값 지정
		String message = null;
		
		if(result > 0) message = "할 일 추가 성공!!!";
		else		   message = "할 일 추가 실패...";
		
		// 리다이렉트 후 1회성으로 사용할 데이터를 속성으로 추가
		// (req -> session -> req)
		ra.addFlashAttribute("message", message);
		
		return "redirect:/"; // 메인페이지로 재요청	
		
	}
	
	// /todo/detail?todoNo=1
	// /todo/detail?todoNo=2
	@GetMapping("detail") // /todo/detail GET 방식 요청 매핑
	public String todoDetail(@RequestParam("todoNo") int todoNo,
							Model model,
							RedirectAttributes ra) {
		
		Todo todo = service.todoDetail(todoNo);
		
		String path = null;
		
		// 조회결과가 있을 경우 detail.html forward
		if(todo != null) {
			
			// templates/todo/detail.html
			path = "todo/detail";
			
			model.addAttribute("todo", todo); // request scope 값 세팅
			
		} else {
			// 조회결과가 없을 경우 메인페이지로 redirect
			path = "redirect:/";
			
			ra.addFlashAttribute("message", "해당 할 일이 존재하지 않습니다");
		}
		
		return path;
	}
	
	
	
	
	
}