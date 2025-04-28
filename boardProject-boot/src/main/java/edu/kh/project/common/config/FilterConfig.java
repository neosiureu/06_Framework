package edu.kh.project.common.config;

import java.util.Arrays;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import edu.kh.project.common.filter.LoginFilter;



@Configuration 
// 서버가 켜지자마자 해당 클래스 내 모든 내용이 실행되도록 하는 어노테이션

public class FilterConfig {
	// 만들어놓은 로그인 필터 클래스가 언제 적용될지 설정한다
	
	
	@Bean
	// 반환된 객체를 지금부터 빈으로 등록하여 IOC가 일어나게 한다
	// 반환된 객체 = LoginFilter타입만 들어갈 수 있는 객체
	public FilterRegistrationBean<LoginFilter> loginFilter(){
		// FilterRegistrationBean => 필터를 Bean으로 등록하는 객체
		
		FilterRegistrationBean<LoginFilter> filter = new FilterRegistrationBean<>();
		
		filter.setFilter(new LoginFilter());
		// 새로 로그인필터를 객체화시키고 그를 FilterRegistrationBean으로 등록
		
		
		/* 필터가 동작할 URL을 세팅
		 /myPage/* 
		이라고 하면 /myPage로 시작하는 모든 요청에서 이 필터가 동작할 것이라는 말
		*/
		
		String[] filteringURL = {"/myPage/*", "/editBoard/*", "/chatting/*" };
		// 						마이페이지 진입 시 게시판 수정 진입 시 채팅창 진입 시
		
		//String들의 배열을 리스트로 변환해야 함 => filter.setUrlPatterns(null)의 인자가 리스트
		
		
		
		filter.setUrlPatterns(Arrays.asList(filteringURL)); // 배열 자체가 리스트로 들어감
		
		// 필터 이름 지정
		filter.setName("loginFilter");
		
		// 필터 순서 지정 (순서는 1부터)
		filter.setOrder(1);
		
		return filter;
		// 이 필터 자체가 빈으로 등록
	}

}
