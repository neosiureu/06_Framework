package edu.kh.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;


// Spring Security의 자동 설정 중 로그인 페이지를 이용하지 않기 위해
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class BoardProjectBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoardProjectBootApplication.class, args);
		
	}

}
