package edu.kh.project.common.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@PropertySource("classpath:/config.properties")
public class EmailConfig {

	// config.properties에 작성된 내용 중 키가 일치하는 value값을 얻어와
	// 필드인 userName password에 대입하는 어노테이션 @Value

	@Value("${spring.mail.username}")
	private String userName; // config.properties에 있는 이메일주소

	@Value("${spring.mail.password}")
	private String password; // config.properties에 있는 앱 비번

	// DI를 통해 유저 이름과 비밀번호에 대한 객체를 생성

	// 진짜 메일을 보내기 위한 구성설정

	@Bean
	public JavaMailSender /* 일종의 인터페이스 */ javaMailSender() {
		/*
		 * 스프링에서 JavaMailSender를 구성하는 Bean을 정의하기 위한 메서드
		 * 
		 * JavaMailSender는 이메일을 보내는데 사용되는 인터페이스로 JavaMailSenderImpl 클래스를 통해 구현된다.
		 * 
		 * SMTP 서버를 사용하여 이메일을 보내기 위한 구성을 제공한다.
		 */

		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

		// mailSender에 대한 구성설정
		 

		Properties prop = new Properties();
		prop.setProperty("mail.transport.protocol", "smtp"); // 전송 프로토콜을 설정. 여기서는 SMTP를 사용
		prop.setProperty("mail.smtp.auth", "true"); // SMTP 서버 인증을 사용할지 여부를 설정함.
													// true로 설정되어 있으므로 인증이 사용됨
													// SMTP 서버를 사용하여 이메일을 보내려면 보안 상의 이유로 인증이 필요.
													// (사용자이름(이메일)과 비밀번호(앱비밀번호) 확인)
		prop.setProperty("mail.smtp.starttls.enable", "true"); // STARTTLS를 사용하여 안전한 연결을 활성화할지 여부를 설정
		prop.setProperty("mail.debug", "true"); // 디버그 모드를 설정
		prop.setProperty("mail.smtp.ssl.trust", "smtp.gmail.com"); // 신뢰할 수 있는 SMTP 서버 호스트를 지정
		prop.setProperty("mail.smtp.ssl.protocols", "TLSv1.2"); // SSL 프로토콜을 설정. 여기서는 TLSv1.2를 사용

		mailSender.setUsername(userName); // 이메일 계정의 사용자
		mailSender.setPassword(password); // 이메일 계정의 비밀번호
		mailSender.setHost("smtp.gmail.com"); // SMTP 서버 호스트를 설정. Gmail의 SMTP 서버인 "smtp.gmail.com"을 사용
		mailSender.setPort(587); // SMTP 서버의 포트 587로 설정
		mailSender.setDefaultEncoding("UTF-8"); // 기본 인코딩을 설정
		mailSender.setJavaMailProperties(prop); // JavaMail의 속성을 설정(앞서 정의해둔 prop 있는 설정들을 여기에 추가)

		
		// 위처럼 각종 설정이 적용된 JavaMailSender를 Bean으로 등록하여 스프링 어플리케이션에서 이메일을 보내기 위한 구성을 제공한다
		
		// 필요한 곳에서 javaMailSender()를 호출할 수 있다.
		
		
		return mailSender;
		
		
	}

	/*
	 * 
	 * 1) 이메일 테이블 구현
	 * 
	 * 2) 구글 SMTP를 위한 비밀번호 발급
	 * 
	 * 3) 구글 SMTP를 나의 자바 어플리케이션에서 이용하기 위해 config를 설정한다
	 * 
	 * SMTP란? Simple Mail Transfer Protocol로 email을 전송하는데 사용하는 표준 통신 프로토콜
	 * 
	 * 1. spring boot 어플리케이션에서 내가 이메일을 보낸다
	 * 
	 * 2. Google SMTP 서버로 전송한다 => Google 서버가 중간 배달자 역할을 한다.
	 * 
	 * 3. Google SMTP 서버가 이메일 주소에 맞는 수신자에게 구글이 만든 이메일서버(gamil, kh.or.kr, naver)등을 통해
	 * 발송한다
	 * 
	 * 4. 최종 수신자가 이메일을 받는다.
	 * 
	 * 기본적으로 SMTP 포트 번호는 25
	 * 
	 */

}