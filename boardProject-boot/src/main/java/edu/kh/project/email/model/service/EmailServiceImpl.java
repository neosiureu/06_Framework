package edu.kh.project.email.model.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import edu.kh.project.email.model.mapper.EmaillMapper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j

@RequiredArgsConstructor //final 필드에 자동으로 의존성 주입 == @AutoWired 랑 똑같음 둘중에 쓰고싶은걸로 ㄱㄱ
public class EmailServiceImpl implements EmailServcie {
	
	private final EmaillMapper mapper ;
	private final JavaMailSender mailSender;// JavaMailSender : 실제 메일 발송을 담당하는 객체 (EmailConfig 설정이 적용된 객체) 
	private final SpringTemplateEngine templateEngine;
	// SpringTemplateEngine : 타임리프를 이용해서 html 코드 -> java 코드 변환
	
	
	
	

	@Override
	public String sendEmail(String htmlName, String email) {
		
		// 1. 인증키 생성 및 DB 저장 준비
		String authKey = createAuthKey();
		log.debug("authKey :" + authKey);
		
		Map<String, String> map = new HashMap<>();
		map.put("authKey", authKey);
		map.put("email", email);
		
		// DB 저장 시도 - 실패 시 해당 메서드 종료
		if(!storeAuthKey(map)) {
			return null;
		}
		
		//2. DB 저장이 성공된 경우에만 메일 발송 시도
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		// 메일발송시 사용하는 객체
		
		// 메일 발송을 도와주는 Helper 클래스 (파일첨부, 템플릿 설정 등 쉽게 처리)
		try {
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true , "UTF-8");
			// - mimeMessage : MimeMessage 객체로 , 이메일 메시지의 내용을 담고 있음
			//                이메일의 본문 ,제목 , 발송자 정보, 수신자 정보 등 포함
			
			
			// - true : 파일 첨부를 사용할 것인지 여부 지정 (파일 첨부 및 내부 이미지 삽입 가능) 
			// - UTF-8 : 이메일 내용을 "UTF-8" 인코딩으로 전송
			
			// 메일 기존 정보설정
			helper.setTo(email); // 받는 사람 (수신자)
			helper.setSubject("[boardProject] 회원 가입 인증번호입니다."); //제목
			helper.setText(loadHtml(authKey, htmlName) , true); // 내용 (본문)
							// 이메일의 본문으로 html 내용을 보냄
			
			//메일에 이미지 첨부
			helper.addInline("logo", new ClassPathResource("static/images/logo.jpg"));
			
			// 실제 메일 발송
			mailSender.send(mimeMessage);
			
			return authKey; // 모든 작업 성공 시 인증키 반환
			
			
		
		} catch (MessagingException e) {
			e.printStackTrace();
			return null; // 메일 발송 실패시 null 반환
			
		}
		
	}
		
		
		
	// HTML 템플릿에 데이터를 바인딩하여 최종 HTML 생성 메서드
	private String loadHtml(String authKey, String htmlName) {
		// Context : 티임리프에서 제공하는 HTMl 템플릿에 데이터를 
		//           전달하기 위해 사용하는 클래스
	
		Context context = new Context(); // 템플릿에 바인딩할 데이터를 담는 상자
		context.setVariable("authKey", authKey); // 템플릿에서 사용할 변수 authKey에 값 설정
		
		
		
		return templateEngine.process("email/" + htmlName, context);
		// templates/email/signup.html
	}




	// 인증키와 이메일을 DB에 저장하는 메서드
	@Transactional(rollbackFor = Exception.class) // 메서드 레벨에서도 이용 가능 (해당 메서드에서만 트랜잭션 커밋/ 롤백)
	private boolean storeAuthKey(Map<String, String> map) {
		
		// 인증키 INSERT / UPDATE 수행 DML
		
		// 1 .기존 이메일에 대한 인증키 UPDATE 수행
		
		// 2. UPDATE 수행 시 0이 반환됨 == UPDATE 안됨 == INSERT 된 내용이 없엇다
		//      INSERT 수행하면 된다.
		int result = mapper.updateAuthKey(map);
		
		if(result == 0 ){
			
		result  = mapper.insertAuthKey(map);
		}
		
		return result > 0; // 성공 여부 반환(true/false)
		
		
	}
	
	// 인증번호 발급 메서드
	// UUID를 사용하여 인증키 생성
	// (Universally unique IDentifier) : 전 세계에서 고유한 식별자를 생성하기위한 표준 객체
	// 매우 낮은 확률로 중복되는 식별자를 생성
	// 주로 데이터베이스 기본키, 고유한 식별자를 생성
	private String createAuthKey() {
		
		return UUID.randomUUID().toString().substring(0,6);
	}
}
