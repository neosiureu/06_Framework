package edu.kh.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // 스프링부트 어플리케이션에 필요한 최소한의 필수 어노테이션과 설정을 모아둔 @를 의미 (ComponentScan)
public class DemoProject1Application {
	
	
	/*
	 * 아무것도 안 만들어도 이러한 클래스가 존재
	 * Spring boot 프로젝트로 만든 어플리케이션의 실행을 담당하는 클래스
	 * Spring Application을 최소 설정으로 간단하고 빠르게 실행할 수 있게 해준다
	 * 마치 자바 파일을 실행하듯 run을 누르면 배포가 시작된다.
	 * 
	 * */

	public static void main(String[] args) {
		SpringApplication.run(DemoProject1Application.class, args);
		
	}

}
