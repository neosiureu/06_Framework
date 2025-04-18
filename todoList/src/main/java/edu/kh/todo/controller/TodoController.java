package edu.kh.todo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
	
	//  /todo/changeComplete?todoNo=4&complete=Y와 같은 꼴로 뷰단에서 데이터를 가진채로 이 컨트롤러에 신호가 들어온다.
	
	
	/**
	 * @param todo: 
	 * 커맨드 객체 (파라미터의 키와 함께 사용된 Todo객체의 필드명이 일치하면 
	 * 일치하는 필드 값이 파라미터의 value값으로 알아서 세팅)
	 * todoNo와 complete변수는 이미 인자로 들어온 todo에 세팅 완료된 상태
	 * @return 
	 */
	@GetMapping("changeComplete")
	public String changeComplete(@ModelAttribute Todo todo, RedirectAttributes ra) {
		
		// 서비스의 호출 => DML의 일종을 하고 싶음
		
		int result = service.changeComplete(todo);
		
		// 변경 성공 시 : "변경성공 !!"
		// 변경 실패 시 : "변경실패 !!"
		
		String message = null;
		
		if(result>0) message = "변경 성공!";
		else message = "변경 실패!";
		
		
		ra.addFlashAttribute("message", message);
		// 일시적 세션 객체
		
		return "redirect:detail?todoNo="+todo.getTodoNo();
		
		/*
		 상대경로는 최하단 주소를 detail로 갈아끼운다
		 현재주소는 todo/changeCompelte를
		 재요청주소는 todo/detail (갈아끼운 것)
		 하지만 단순히 주소뿐 아니라 
		 쿼리리스트링까지 연이어 연결해야 함
		 */
		
	}
	
	// modelAttribute는 뷰단에서 넘어온 변수명이 dto의 필드의 이름 (Todo클래스의 필드값)과 일치하면 
	// 둘이 알아서 매핑되어 각 필드에 알아서 세팅해준다. 
	
	@GetMapping("update")
	public String todoUpdate(@RequestParam("todoNo") int todoNo, Model model
			) {
	
		// 일단 update 페이지에 들어가자 마자
		// detail 페이지에서 나오는 내용이 기존 내용 그대로 나와야 함
		// 이때 Todo객체 하나를 얻어왔어야 한다
		
		Todo todo = service.todoDetail(todoNo);
		// 이미 수행되어 기존 값이 mapper단까지 가서 되돌아 나옴
		
		model.addAttribute("todo",todo);
		
		
		
		return "todo/update";
		// 수정 페이지 자체가 있는 페이지가 있어야 하므로 화면전환만 하면 됨
		// 따라서 redirect가 아닌 forward가 더 적절
		
		
	}
	
	
	@PostMapping("update")
	public String todoUpdate(@ModelAttribute Todo todo, RedirectAttributes ra ) {
		
		int result = service.todoUpdate(todo);
		
		String path;
		String message = null;
		
		if(result>0) {
			// 상세 조회로 redirect, 즉 detail로 
			path = "/todo/detail?todoNo="+todo.getTodoNo();
			message = "수정 성공";
		}
	
		else {
			// 다시 현재 수정하는 화면으로 리다이렉트
			path = "/todo/update?todoNo="+todo.getTodoNo();
			message= "수정실패";
			
		}
		
		ra.addFlashAttribute("message",message);
		
		
		return path;
	}
	
	

	
	@GetMapping("delete")
	public String todoDelete(@ModelAttribute Todo todo, RedirectAttributes ra ) {
		
		int result = service.todoDelete(todo);
		
		String path = null;
		String message = null;
		
		if(result>0) {
		
			path = "/";
			message = "삭제 성공!";
		}
	
		else {
			// 다시 현재 수정하는 화면으로 리다이렉트
			path += "/todo/detail?todoNo="+todo.getTodoNo();
			message= "삭제 실패..";
			
		}
		
		ra.addFlashAttribute("message",message);
		
		
		return  "redirect:" +  path;
	}
	
	
	
	
	
}