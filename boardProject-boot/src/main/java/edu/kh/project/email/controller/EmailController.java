package edu.kh.project.email.controller;

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

}
