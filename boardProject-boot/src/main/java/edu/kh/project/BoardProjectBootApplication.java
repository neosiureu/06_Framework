package edu.kh.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling // 해당 프로젝트 서버 실행 시 스케줄러를 활성화 하여 이용하겠다!
// SpringSecurity 관련 자동 설정을 중 로그인 페이지 이용안함
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
// 스프링부트에 제공하는 보안 관련자동 설정을 제외 
public class BoardProjectBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoardProjectBootApplication.class, args);
	}

}
