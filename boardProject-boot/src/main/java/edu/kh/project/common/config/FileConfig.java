package edu.kh.project.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.servlet.MultipartConfigElement;



@Configuration
@PropertySource("classpath:config.properties") // config.properties파일을 읽어준다
public class FileConfig implements WebMvcConfigurer {
	// WebMvcConfigurer = Spring MVC 프레임워크에서 제공하는 인터페이스 중 하나로 
	// 스프링 구성을 커스터마이징하고 확장하기 위한 메서드를 제공한다
	// 주로 웹 어플리케이션의 설정을 조정하거나 추가할 때 사용한다
	
	// addResourceHandlers(“경로”); 랑 addResourceLocation(“조금 이상한 경로명”);를 위한 implements
	
	
	// emailCofig 시 썼었던 @Value어노테이션
	
	
	@Value("${spring.servlet.multipart.file-size-threshold}")
	private long fileSizeThreshold;
	
	@Value("${spring.servlet.multipart.max-request-size}")
	private long maxRequestSize;

	@Value("${spring.servlet.multipart.max-file-size}")
	private long maxFileSize;
			
	@Value("${spring.servlet.multipart.location}")
	private String location;
	
	// 프로필 이미지 관련 경로
	
	
	@Value("${my.profile.resource-handler}")
	private String profileResourceHandler;
	
	
	@Value("${my.profile.resource-location}")
	private String profileResourceLocation;

	
	// 요청 주소에 따라 서버 컴퓨터 중 어떤 경로에 접근할지 설정
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// ResourceHandlerRegistry:
		// Spring MVC서 정적 리소스 (css js 이미지 파일 등)의 요청을 처리하기 위해 사용하는 클래스
		// URL 요청 패턴을 서버의 실제 파일 경로와 연결하여 클라이언트가 특정 경로로 정적파일에 접근할 수 있도록 설정
		
		registry.addResourceHandler("/myPage/file/**").
		addResourceLocations("file:///C:/uploadFiles/test/"); // 꽉찼을 때 사용하는 temp와는 다른 경로로 의도적 경로
		// 클라이언트의 요청을 연결해서 처리해 줄 서버의 실제 폴더 경로
		
		// 클라이언트가 /myPage/file/** 패턴으로 이미지를 요청할 때 
		// => 서버 폴더 경로 중 C:uploadFiles/test로 일단 연결하겠다 => 여기서 이미지를 찾겠다
		
		// 클라이언트의 요청주소 패턴		
		
		
		
		
		
		// 서버의 서비스에서 다룰 수 있게 config.properties에 있는 것을 변수로 쓴 것
		
		registry.addResourceHandler(profileResourceHandler).addResourceLocations(profileResourceLocation);
		
		
		
		
	}

	// multipartResolver의 설정
	
	/*
	 
	  <form action="/myPage/file/test2"
            method="POST"
            enctype="multipart/form-data" >를 참고
            
      => form데이터에서 문자와 숫자 파일등이 섞여서 넘어오는데 파일은 이진데이터로 넘어옴
      이를 MultipartResolver를 이용하여 섞여있는 파라미터를 분리
      
      문자열과 숫자는 String으로, 파일은 MultipartFile로 분리 <-multipartResolver가 그 역할을 해줌
      
      
	*/
	
	
	
	// 위 메서드를 통해 MultipartConfigElement 를 생성
	
	@Bean
	// resolver 설정용 메서드
	public MultipartConfigElement configElement () {
		// 파일업로드 시 사용되는 MultipartConfigElement를 구성하고 반환
		// 파일업로드를 위한 구성 옵션을 설정하는데 사용되는 객체
		
		// 업로드 파일의 최대크기 또는 메모리 임시 저장경로 등을 여기서 설정
		// 필드에 있는 네 가지 변수들을 여기에서 설정
		
		
		
		MultipartConfigFactory factory = new MultipartConfigFactory();
		
		// 파일 업로드 임계 값
		factory.setFileSizeThreshold(DataSize.ofBytes(fileSizeThreshold));
		// DataSize ofBytes(long) 
		
		// HTTP 요청당 파일 최대 크기
		
		factory.setMaxRequestSize(DataSize.ofBytes(maxRequestSize));
		
		
		// 개별 파일당 최대 크기
		
		factory.setMaxFileSize(DataSize.ofBytes(maxFileSize));
		
		// 임계값 초과시 임시저장 폴더 경로
		
		factory.setLocation(location);
	
		
		// 객체 자체를 만드는 fatory의 메서드
		return factory.createMultipartConfig();
		
		// 설정
		
	}
	
	
	
	// MultipartConfigElement 를 통해 MultipartResolver를 마지막으로 설정
	
	
	@Bean 
	// Bean으로 등록하면 Element 객체를 자동으로 이용할 수 있음
	public MultipartResolver multipartResolver() {
//		 MultipartResolver: MultipartFile을 처리해주는 해결사로
//		 MultipartResolver는 클라이언트로 받은 multipart 요청을 처리하고 ( enctype="multipart/form-data"  
//		 폼 형식이 섞여있는걸 처리하게 함)
//		 이 중에서 업로드된 파일을 MultipartFile객체로 제공하는 역할
		StandardServletMultipartResolver multipartResolver = new StandardServletMultipartResolver();
		
		return multipartResolver;
	}
	

}
