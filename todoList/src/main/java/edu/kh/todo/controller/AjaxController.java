package edu.kh.todo.controller;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import edu.kh.todo.model.dto.Todo;
import edu.kh.todo.model.service.TodoService;
import lombok.extern.slf4j.Slf4j;




//@ResponseBody란?

/*
 * 컨트롤러 메서드의 반환 값을 http응답 본문에 직접 바인딩하는 역할임을 명시
 * 
 * 컨트롤러 메서드의 반환 값을 비동기로 요청했던 html이나 js파일 부분에 값을 그대로 돌려보낼 것이다.
 * 
 * 해당 어노테이션이 붙은 컨트롤러의 메서드는 리턴에 작성된 값이 포워드, 리다이렉트와 전혀 무관
 * 
 * */


@Slf4j
@RequestMapping("ajax") // 요청주소 시작이 ajax로 시작하는 요청을 매핑한다.
@Controller // 요청과 응답을 제어하는 컨트롤러 단임을 스프링에게 알려주고 자동으로 @Bean
public class AjaxController {
	
	
	
	@Autowired
	private TodoService service;
	// 등록된 bean중에 같은 타입이나 상속관계인 빈을 찾아 해당 필드에 의존성 주입해준다.
	
	

	@GetMapping("main")
	public String ajaxMain() {
		
		
		
		
		
		return "ajax/main";
	}
	

	/*
	 전체 todo의 개수 조회
	 동기 요청시에 redirect forward를 해야했으므로 String이었지만
	 전체 todo개수라는 데이터를 비동기 요청 보낸 클라이언트, 즉 브라우저쪽으로 반환되기를 바람
	 반환되어야하는 결과 값의 자료형을 반환형에 써야 함
	*/
	
	@ResponseBody // 반환 값을 http의 응답 본문으로 값 그대로 돌려보낸다.
	@GetMapping("totalCount") // 물론 get매핑 보냈으니 그에 대응하는 매핑 어노테이션도 있어야 함
	public int getTotalCount() {
		
		// 전체 할일 개수 조회하는 서비스를 호출하고 결과를 반환받은 뒤 리턴 자리에 결과 작성
		
		int totalCount = service.getTotalCount();
		
		return totalCount;
	}
	
	
	@ResponseBody
	@GetMapping("completeCount")
	public int getCompleteCount() {
	  return service.getCompleteCount();
	}
	
	// 할일 추가  
	@ResponseBody
	@PostMapping("addTodo")
	// parma이 일단 서버로 오고 있음
	public int addTodo(@RequestBody Todo todo) {
		/*requestParam은 일반적으로 쿼리 파라미터 혹은 URL 파라미터에서 사용
		 * 요청 중 바디에 담긴 값을 받으려면 RequestBody
		 * Todo타입으로 받을 수 있는 이유: JS에서 준 값이 dto에 있는 필드의 값과 똑같기 때문에
		 * 자동으로 세팅해주고 Todo객체까지 만들어준다.
		 * */
		
		log.debug("todo"+todo);
		
		int result = service.addTodo(todo.getTodoTitle(), todo.getTodoContent());
	
		
		return result;
		
		/* @RequestBody
		 * 비동기 요청 시 전달되는 데이터 중 body부분에 포함된 요청 데이터를 
		 * 알맞은 Java객체 타입으로 바인딩하는 어노테이션
		 * 기본적으로 요청받은 데이터가 JSON 형식일 것으로 예상함
		 * JS는 Java로 제출할 경우 처음에는 
		 * @RequestBody는 출발지가 JSON이기를 기대한다. 알아서 TODO와 같은 형식으로 바꿔준다.
		 * 그 주체는 HTTPMessageConvertor
		 * Spring에서 비동기 통신 시 전달받은 데이터의 자료형, 응답하는 데이터의 자료형을
		 * 알맞은 형태로 가공하고 변환해주는 객체이다.
		 * 
		 * Java     <----------------------------> JS
		 * 문자열, 숫자                              Text
		 * Map                 JSON               JS Object
		 * DTO                 JSON               JS Object	
		 *
		 *
		 *
		 *Spiring에서 HTTPMessageConvertor가 작동하기 위해서는 jackson-data0bind 라이브러리가 필요한데
		 *스프링부트에는 내장 되어 있음.
		 *
		 */
	}
	
	
	@ResponseBody
	@GetMapping("selectList")
	public List<Todo> selectList(){
		List<Todo> todoList = service.selectList();
		return todoList;
		// List는 자바 전용 타입 => JSON으로 바꿔야  js가 인식할 수 있다
		
	}
	
	
	
}
