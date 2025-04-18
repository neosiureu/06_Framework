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
import jakarta.servlet.RequestDispatcher;

@Controller // 요청 / 응답 제어 역할 명시 + Bean 등록
@RequestMapping("/todo") // "/todo" 로 시작하는 모든요청 매핑
public class TodoController {
	
	@Autowired // 의존성 주입 (같은 타입 + 상속관계 Bean을 의존성 주입(DI))
	private TodoService service;
	
	@PostMapping("/add") // "/todo/add" 요청 매핑
	public String addTodo(@RequestParam("todoTitle") String todoTitle,
						 @RequestParam("todoContent") String todoContent,
						 RedirectAttributes ra // 리다이렉트 시 request scope에 데이터 전달
			) {
		//RedirectAttributes : 리다이렉트 시 값을 1회성으로 전달하는 객체
		//RedirectAttributes.addFlashAttribute("key", value); 형식으로 세팅.
		//-> request scope -> session scope 로 잠시변환

		// 응답 전 : request scope
		// redirect 후 : session scope 로 이동 -> 사용 
		// 응답 까지 끝나고  난후 : request scope 복귀
	
		// 서비스 메서드 호출 후 결과 반환받기.
		int result = service.addTodo(todoTitle, todoContent);
		
		// 결과에 따라 message 값 지정
		String message = null;
		
		if(result > 0) message = "할일 등록 성공";			
		else          message = "할일 등록 실패";
		
		// 리다이렉트 후 1회성으로 사용할 데이터를 속성으로 추가(req-> session 
		 // req-> 복귀)
		
		ra.addFlashAttribute("message", message);
		
		
		return"redirect:/"; // 메인 페이지로 재요청"
	}


	@GetMapping("detail")
	public String todoDetail(@RequestParam("todoNo") int todoNo,
							 Model model,
							 RedirectAttributes ra) 
	   {
		Todo todo = service.todoDetail(todoNo);			
		
		String path = null;
		
		// 조회 결과가 있을경우 detail.html forward
		// 죄회 결과가 있을경우 메인 페이지 redirect
		
		if(todo != null) {
			
			// templates/todo/detail.html
			path = "todo/detail";
			model.addAttribute("todo",todo); //request scope 값
			
			
		}else {
			path ="redirect:/";
			ra.addFlashAttribute("message","해당 할일이 존재하지 않습니다.");
		}	
		
		return path;
		
	}
	
	// /todo/changeComplete?todoNo=4&complete=y
	/** 완료 여부 변경
	 * @param todo : 커맨드 객체(@ModelAttribute 생략)
	 *               - 파라미터의 key 와 Todo 객체의 필드명이 일치하면
	 *               - 일치하는 필드값이 파라미터의 value 값으로 세팅 된 상태
	 *               - 즉, todoNo와 complete 두 필드가 세팅완료된상태
	 * @return
	 */
	@GetMapping("changeComplete")
	public String changeComplete(Todo todo, RedirectAttributes ra) {
								//@ModelAttribute
		
		// 변경 서비스 호출
		
		int result = service.changeComplete(todo);
		
		// 변경 성공 시 : "변경 성공!"
		// 변경 실패 시 : "변경 실패 "
		
		String message = null;
		
		if(result > 0) message = "변경성공";
		else           message = "변경 실패..";
		
		ra.addFlashAttribute("message", message);
		
		
		// 상대경로 (현재 위치 중요 !!!)
		// 현재 주소 : /todo/changeComplete
		// 재요청 주소 : /todo/detail
		return"redirect:detail?todoNo=" + todo.getTodoNo();
		
		
	}
	
	/** 수정 화면 전환 요청
	 * @return
	 */
	@GetMapping("update")
	public String todoUpdate(@RequestParam("todoNo") int todoNo ,
			                               Model model) {
	
		// 상세 조회 서비스 호출 (재활용) -> 수정화면에 출력할 기존 내용 
		Todo todo = service.todoDetail(todoNo);
		
		model.addAttribute("todo",todo);
		
		//classpath:templates/todo/update.html 로 forward
		return "todo/update";
	
}

	@PostMapping("update")
	public String todoUpdate(Todo todo, RedirectAttributes ra) {
		
		// 수정 서비스 호출 후 결과 반환받기
		
		int result = service.todoUpdate(todo);
		
		String message = null;
		String path    = "redirect:";
		
		
		if(result >0) {
			// 상세조회로 리다이텍트
			message = "할일 수정성공";
			path += "/todo/detail?todoNo=" + todo.getTodoNo();
		}
		else         {
			message = "할일 수정 실패 ..";
			path += "/todo/update?todoNo=" + todo.getTodoNo();
		}
		
		ra.addFlashAttribute("message",message);
		
		return path;
	}

	@GetMapping("delete")
	public String todoDelete(Todo todo,RedirectAttributes ra) {
		
		int result = service.todoDelete(todo);
		
		String message = null;
		String path = "redirect:";
		
		
		if(result > 0) {
			message = "삭제성공";
			path +="/";
		}
		else {
			message="삭제실패";
			path +="todo/detail?todoNo=" + todo.getTodoNo();
		}
		
		ra.addFlashAttribute("message",message);
		
		return path;
	}
}
























