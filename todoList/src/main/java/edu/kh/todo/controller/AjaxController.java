package edu.kh.todo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.kh.todo.model.dto.Todo;
import edu.kh.todo.model.service.TodoService;
import lombok.extern.slf4j.Slf4j;

/*
 * @ResponseBody
 * - 컨트롤러 메서드의 반환값을
 *    HTTP 응답 본문에 직접 바인딩하는 역할임을 명시
 *    
 *    
 * -> 컨트롤러 메서드의 반환값을  
 *    비동기 요청했떤 HTML/JS 파일 부분에 값을 그대로 돌려 보낼 것이다. 를 명시
 *    
 * -> 해당 어노테이션이 붙은 컨트롤러의 메서드는 return 값이 forward/redirect 로 
 *     인식되지 않음
 * 
 * @RequestBody
 * - 비동기 요청시 전달되는 데이터 중 body 부분에 포함된 요청 데이터를
 *   알맞은 java 객체 타입으로 바인딩하는 어노테이션
 *   
 * - 기본적으로 JSON 형식을 기대함.  
 * 
 * [HttpMessageConverter]
 * Spring 에서 비동기 통신시 
 * - 전달받은 데이터의 자료형 
 * - 응답하는 데이터의 자료형
 * 위 두가지를 알맞은 형태로 가공(변환)해주는 객체
 * 
 *   java                                Js
 *  문자열, 숫자 <--------------------> TEXT 
 * 	  Map       <-> JSON             <-> JS object
 *    DTO       <-> JSON             <-> JS object
 *    List<DTO>  <-> JSON Array       <-> JS Array ? ?  ..
 *   
 *  (참고)
 *  Spring 에서 HttpMessageConverter 자동하기 위해서는
 *  Jackson-data-bind 라이브러리 필요
 *  spring-boot-starter-web 에 포함되어있음  
 *   
 *   
 * */




@Controller //요청 /응답 제어하는 역할 명시 + bean 등록
@RequestMapping("ajax") // 요청 주소 시작이 "ajax"인 요청을 매핑
@Slf4j
public class AjaxController {
	
	//@Autowired
	// - 등록된 Bean 중 같은 타입 또는 상속관계인 Bean을 찾아서
	// 해당 필드에 의존성 주입
	
	@Autowired // 의존성 주입(DI)
	private TodoService service;
	
	@GetMapping("main")
	public String ajaxMain() {
		
		return"ajax/main";
		
	}
	
	// 전체 Todo 개수 조회
	// -> forward / redirect 를 원하는게 아님!
	// -> "전체 Todo 개수" 라는 데이터를 비동기 요청 보낸 
	// 클라이언트(브라우저) 반환되는 것을 원함.
	// -> 반환 되어야하는 결과값의 자료형을 반환형에 써야함!
	       // 반환값을 HTTP 응답 본문으로 직접전송 
	               // (값 그대로 돌려보낼거야!!)
	@ResponseBody
	@GetMapping("totalCount")
	public int getTotalCount() {
		
		// 전체 할일 개수 조회 서비스 호출 결과 반환 받기
		int totalCount =  service.getTotalCount();
		
		// 이자리에 결과 작성하기.
		
		
		return totalCount;
		
	}
	@ResponseBody
	@GetMapping("completeCount")
	public int getCompleteCount() {
		
		
		
		
		return service.getCompleteCount();
		
	}
	
	//할일 추가
	@ResponseBody
	@PostMapping("add")
	public int addTodo(@RequestBody Todo todo) { //요청 Body에 담긴 값을 Todo DTO에 저장
		// @RequestParam은 일반적으로 쿼리파라미터나 URL 파라미터에 사용
		log.debug("todo : "+ todo);
		
		// 할일 추가 서비스 호출후 응답
		int result = service.addTodo(todo.getTodoTitle(), todo.getTodoContent());
		
		
		
		return result;
	}
	
	@ResponseBody
	@GetMapping("selectList")
	public List<Todo> selectList(){
		
		List<Todo> todoList = service.selectList();
		
		return todoList;
		
		// List(Java 전용 타입)을 반환
		// -> JS가 인식할 수 없기 때문에 JSON으로 변환 필요!
		// -> HttpMessageConverter가 JSON 형태로 자동변환 하여 반환
		
	}
	@ResponseBody
	@GetMapping("detail")
	public Todo todoDetail(@RequestParam("todoNo") int todoNo) {
		
		Todo todo = service.todoDetail(todoNo);
		
		return todo;
		
	}
	
	
	
}
