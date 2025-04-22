package edu.kh.project.email.model.service;

public interface EmailService {

	/** 이메일 보내기
	 * @param string: 어떤 목적으로 이메일을 발송할지 구분할 키로 쓰임 (회원가입/ 비밀번호재발급/ 광고)
	 현재 이메일 서비스가 꼭 회원가입에만 쓰인다는 보장은 없다는 것. 그를 구분하기 위한 키가 되는 인자
	 * @param email: 수신자의 정보를 담은 이메일
	 * @return authkey = 발급한 인증번호
	 */
	String sendEmail(String string, String email);
	// String authkey = service.sendEmail("signup",email);
	// 전달된 타입이 스트링이라는 뜻으로 그냥 아무거나 전달함


}
