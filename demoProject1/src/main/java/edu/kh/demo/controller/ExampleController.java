package edu.kh.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // IOC: 요청에 대한 응답, 제어 역할을 명시하면서 Bean에 등록까지 한다
public class ExampleController {	
//	@GetMapping("todo")
	@GetMapping("example") // 앞으로는 '/'를 맨 앞에 쓰지 마라 (배포 단계에서의 빌드 오류)
	public String exampleMethod() {
		
		/*
		 * 왜 "/example"로 하지않고 "example"로 했냐?
		 * 
		 * 1) Spring Boot에서는 요청 주소 앞에 "/"가 없어도 요청 처리에 문제가 없다
		 * 2) 프로젝트를 호스팅 서비스를 이용하여 배포할 때 빌드 과정에서 오류가 발생하기 때문 => AWS에 이런 것을 요청 가능
		 * 
		 * 다만 클라이언트쪽, 즉  뷰 단에서는 슬래시를 생략해서는 안 된다 (빼면 상대경로가 아닌 절대경로가 된다)
		 * 
		 * 
		 * */
		
		
		// view resolver가 제공하는 타임리프의 접두사, 접미사를 제외하고 작성
		// 접두사: claspath:/templates/ 까지가 접두사
		// 접미사: .html
		// classpath:/templates/example.html
		
		return "example";
		// 일종의 a옆 href form옆 action
		
		/*
		 * 안쓰긴 하지만) RequstMapping("주소") => 속성을 이용하면 get post를 명시할 수 있다
		 * 
		 * 1) GetMapping("주소") => Get(조회)방식의 요청을 매핑
		 * 2) PostMapping("주소") => Post (삽입)방식의 요청을 매핑
		 * 
		 * 기타) 
		 * @ PutMapping("주소") => Put (수정)방식의 요청을 매핑, 단 form a태그에서 요청할 수 없다. 오직 js에서의 비동기 요청시에만 사용
		 * @ DeleteMapping("주소")  => Delete (삭제)방식의 요청을 매핑
		 * */
	}

}
