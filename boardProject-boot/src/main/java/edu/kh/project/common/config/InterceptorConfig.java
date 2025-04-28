package edu.kh.project.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import edu.kh.project.common.interceptor.BoardTypeInterceptor;

// 인터셉터가 어떤 요청을 가로챌지를 설정한다
@Configuration
public class InterceptorConfig implements WebMvcConfigurer{
	// fileConfig에서 리소스들간의 핸들링
	
	
	// BoardTypeInterceptor를 자기가 객체로 만들어 Bean으로 등록
	@Bean
	public BoardTypeInterceptor boardTypeInterceptor () {
		return new BoardTypeInterceptor();
	}
	
	
	
	// 동작할 인터셉트 객체를 추가해준다
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		
		// Bean으로 등록 된 BoardTypeInterCeptor를 얻어와 매개변수로 전달한다
		registry.addInterceptor( boardTypeInterceptor ()).addPathPatterns("/**")
		// 가로챌 요청 주소를 지정 => /** (메인 이하 모든 주소에서)
		.excludePathPatterns("/css/**","/css/**","images/**","/favicon.ico");
		// 제외할 요청 주소를 지정, 즉 가로채지 않을 주소를 지정
		// 정적 리소스는 가로채지 않을 것이다.
		
		
	}
	
	

}
