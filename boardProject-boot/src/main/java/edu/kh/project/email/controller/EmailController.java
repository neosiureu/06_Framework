package edu.kh.project.email.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.kh.project.email.model.service.EmailService;
import lombok.RequiredArgsConstructor;

@RequestMapping("email")
@Controller
@RequiredArgsConstructor
public class EmailController {

	private final EmailService service;

	@PostMapping("signup")
	@ResponseBody
	public int signup(@RequestBody String email) {
		/*
		 * fetch("/email/signup",
		 * { method: "post", 
		 * headers: {"Content-Type":"application/json"}, 
		 * body : memberEmail.value })
		 */
		String authKey = service.sendEmail("signup",email);
		
		if(authKey !=null) {
			// 인증번호 발급 성공 && 이메일 보내기도 성공했다
			return 1;
		}
		
		
		return 0;
	}
	
	
	
	/** 입력받은 이메일과 인증번호가 DB상에 있는지 조회
	 * @param map (email, authKey)
	 * @return 1또는 0 => 1은 이메일과 인증번호 일치, 0은 일치하는 것이 없을 떄
	 */
	@ResponseBody
	@PostMapping("checkAuthKey")
	public int checkAuthKey(@RequestBody Map<String, String> map) {
		//	Map으로 가져오겠다 하면  알아서 json안에 있는 것이 그대로 넘어옴
		// {"email": "user01@..." ,  "authKey" : "6자리 인증번호" }
		
		
		
		return service.checkAuthKey(map);
	}

}
