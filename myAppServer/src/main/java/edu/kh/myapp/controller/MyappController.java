package edu.kh.myapp.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


/*
 CrossOrigin: 스프링에서 제공하는 어노테이션에 해당하며 CORS 설정을 위해 사용한다.
 CORS (Cross-Origin Resource Sharing)
 클라이언트와 서버가 서로 다른 출처 (origin)에서 요청을 주고받을 때 발생하는 보안정책
 브라우저에서는 기본적으로 다른출처 (도메인, 프로토콜, 포트 중 하나라도) 다르면 요청을 기본적으로 차단함
*/


@CrossOrigin(origins = "http://localhost:5173")
@RestController
public class MyappController {
	
	@GetMapping("getPortNumber")
	public List<String> getPortNumber(){
		
		return Arrays.asList("서버포트는 80", "클라이언트 포트는 5173");
		// 배열을 리스트로 또는 인자들을 리스트로
	}
	
	
	
	@PostMapping("getUserInfo")
	public String getUserInfo(@RequestBody Map<String, Object> map) {
		// 메시지 자체를 리턴
		// 만약 요청데이터 중 name: "홍길동", age:20이라는 가정에 "홍길동 님은 20세입니다 리턴"
		// 같지 않으면 데이터가 없다고 뜨게 한다.
		
		System.out.println(map.get("name"));
		System.out.println(map.get("age"));

		String message = "데이터가 없습니다";
		
		if(map.get("name").equals("홍길동") && map.get("age").equals(20)) {
			message = "홍길동님은 20세입니다";
		}
		
		return message;
	}
	

}
