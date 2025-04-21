package edu.kh.project.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


// 해당 클래스가 설정용 클래스임을 명시 -> 객체로 생성하여 서버 실행시 해당 코드를 모두 실행
@Configuration
public class SecurityConfig {
	//bcrypt 객체를 만들기 위함
	
	//스프링에게 본인이 새로 생성한 객체를 위임하는 어노테이션
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		
		return new BCryptPasswordEncoder();
	}

}
