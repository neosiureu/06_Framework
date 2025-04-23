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

import edu.kh.project.email.model.mapper.EmailMapper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
// 해당 클래스 내의 필드 중 final 필드에 자동으로 의존성을 주입한다
public class EmailServiceImpl implements EmailService {
	
	private final EmailMapper mapper;
	private final JavaMailSender mailSender;
	// 실제 메일의 발송을 담당하는 객체 (email설정이 적용)
	private final SpringTemplateEngine templateEngine;
	// 이미 bean이고 주입만 받으면 됨
	// 타임리프를 이용해서 html코드가 만들어져 있는걸 자바코드로 변환한다
	// 자바에서 html을 보내야하는 상황 => 그를 위해 html을 자바로 한번 변환할 필요가 있음
	

	@Override
	public String sendEmail(String htmlName, String email) {
		//  htmlName이 이름인 이유: 인증번호 발급하는 html을 만들기 때문
		
		// 1단계: 인증키를 생성 및 DB에 저장할 준비
		
		String authKey = createAuthKey();
		
		log.debug("authkey : "+authKey);
		
		// 2단계: authkey와 email을 통해 행을 추가
		Map<String, String> map = new HashMap<>();
		map.put("authKey" ,authKey);
		map.put("email", email);
		
		
		/* DB에 값을 저장하려는 시도 
		 => 실패시에는 해당 sendEmail 종료
		 DB에 값이 없는데 메일을 보내는 것도 이상함. 그래서 거르는 것
		*/
		
		if(!storeAuthkey(map)) { // 컨트롤러에서 null일 때 0을 보내겠다고 했으니
			return null;
		}
		
		
		
		// DB에 저장이 성공된 경우에만 메일을 발송 => 진짜 메일 보내는 기능
		
		
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		// 메일 발송 시 메시지를 담을만한 객체
		// mailSender는 위에서 의존성주입
		
		// 하지만 이 혼자서는 메일을 발송 못함 => 헬퍼
		
		// helper 클래스는 파일첨부나 템플릿을 설정할 수 있다
		
		try {
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,
					true, "UTF-8");
			
			/* 
			 
			 mimeMessage: 
			 MimeMessage 객체로 이메일 메시지의 내용을 담고 있음			 
			 즉 이메일의 본문, 제목, 발신 및 수신 정보 등은 이 안에 있음
			
			* true- 파일첨부가 사용될것인지,
			 내부 이미지도 삽입하여 보낼 수 있는지
			
			* UTF-8 - 이메일 내용을 UTF-8 인코딩을 통해 전송
			 
			*/
			
			helper.setTo(email); // 컨트롤러단에서 넘어온 수신자
			// 자바 메일 센더를 만들 때 이미 userName으로서 설정해놨으니 필요 없음
			// config.properties에 작성했던 나의 이메일 주소가 발신자가 된 셈
			
			helper.setSubject("[boardProject] 회원가입 인증 번호입니다."); // 메일의 제목
			
			helper.setText(loadHtml(authKey, htmlName), true); // 본문
			//loadHtml은 사용자정의 함수
			// 이메일의 본문으로 html 내용을 보내겠다
			// HTML여부가 맞다는 뜻으로 두번째 인자로 true 전달
			
			// 메일에는 웹사이트 관련 로고가 들어간다.
			helper.addInline("logo", new ClassPathResource("static/images/logo.jpg"));
			// src/ main/ resources하에
			// 이렇게 해놓으면 뷰 단에서 logo라는 이름으로 변수처럼 사용 가능
			
			mailSender.send(mimeMessage);
			// 실제 메일 발송
			
			return authKey;

			
		} catch (MessagingException e) {

			e.printStackTrace();
			return null;
		} 
		
		// 모든 작업이 성공했다면 인증키를 반환한다
		
	}
	
	
	// 인증키와 이메일을 세트로 DB에 저장하는 메서드
	
	
	private String loadHtml(String authKey, String htmlName) {
		/* setText의 첫 인자가 String이라서 알아서 이렇게 만들어줌
		 html템플릿에 데이터를 바인딩하여 최종 HTML을 생성하는 메서드
		 .html에다가 가진 데이터를 집어넣어 html코드를 만든다
		 타임리프에서 제공하는 context객체를 사용한다. 
		 자바 서버단임에도 불구하고 html파일이므로 타임리프의 영향을 받는다
		 */
		
		Context context = new Context(); // HTML 템플릿에 바인딩할 데이터를 담는 상자
		
		/* 타임리프에서 제공하는 HTML 템플릿에 데이터를 전달하기 위해 사용하는 클래스
		 authKey라는 자바코드로 만든 것을 HTML템플릿에 전달하고 싶다
		 그를 위해 Context라는 곳에 세팅하여 보내겠다 */
		
		context.setVariable("authKey", authKey); // 인증번호 6자리를 html형태로 보내기 위해
		// 템플릿에서 사용할 변수 authKey에 값을 설정한 것
		
		
		return templateEngine.process("email/"+htmlName, context /*변수가 담긴 상자*/);
		
		// templates/ 폴더 기준으로 email폴더 하위에 signup (컨트롤러에서 받은 문자열)
		// signup.html에 해당하는 내용이 본문으로써 사용된다
		// signup.html 안에서는 context가 들어가게 된다
	}


	@Transactional(rollbackFor = Exception.class) // 메서드 레벨에서도 Transactional 어노테이션 이용 가능 
	// (해당 메서드에서만 트랜잭션을 커밋하거나 롤백)
	private boolean storeAuthkey(Map<String, String> map) {
		// 인증키를 insert 또는 update할 때 DML 
		
		// update를 한다? 
		// 해당하는 인증키에 해당하는 값이 DB에 있다면 update, 없다면 insert 
		
		
		// 기존 이메일에 대한 인증키를 업데이트
		// 업데이트 수행했는데 0이라면?
		// 기존 데이터중 이 이메일을 가진 데이터가 없었다
		// 그럴 때는 삽입을 하면 된다. 즉 Update용 mapper와 Insert용 mapper를 모두 호출
		
		
		
		
		// 1) 기존 이메일에 대한 인증키를 업데이트
		
		int result = mapper.updateAuthKey(map);

		// WHERE절에 부합하는 이메일이 아직 없었다
		
		// 2)  업데이트 수행했는데 0이라면?
		// 처음 발급받는 이메일이라는 의미
		// 기존 데이터중 이 이메일을 가진 데이터가 없었다
		// 그럴 때는 삽입을 하면 된다. 
		// 즉 Update용 mapper와 Insert용 mapper를 모두 호출
		
		
		if (result==0){ // 위에서 업데이트를 실패했다면, 즉 없었다면
			result = mapper.insertAuthKey(map);
		}
		
		return result>0;
		
	}
	
	

	// 사이드 메서드

	// 인증번호 발급 메서드
	// UUID를 사용하여 인증키 생성
	
	private String createAuthKey() {
		// 랜덤한 키를 위한 UUID 메서드 
		// Universally Unique IDentifier => 전 세계에서 고유한 식별자를 생성하기 위한 표준 객체
		// 중복되는 식별자의 생성이 거의 없다
		// 주로 DB에서 기본 키를 자바단에서 만들어 보내거나
		// 고유한 식별자를 생성할 때 이를 쓴다
		return UUID.randomUUID().toString().substring(0,6);
	}


	
	
	// 함수를 호버링하면 자동으로 주석이 보임
	@Override
	public int checkAuthKey(Map<String, String> map) {
		
		return mapper.checkAuthKey(map);
	}
	
	
	
}
