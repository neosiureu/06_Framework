package edu.kh.project.member.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.member.model.mapper.MemberMapper;
import lombok.extern.slf4j.Slf4j;

@Transactional(rollbackFor = Exception.class) // 메서드에 @Transactional을 붙이면 해당 메서드에서 발생하는 모든 DB작업을 하나의 트랜잭션으로 묶어줌
@Service // 비즈니스로직 처리 역할 명시 + Bean 등록
@Slf4j // 로그를 남기기 위한 어노테이션
public class MemberServiceImpl implements MemberService {
	
	//등록 된 Bean 중에서 MemberMapper와 같은 타입or 상속관계인 Bean을 찾아서 주입
	//의존성 주입 (DI)
	
	@Autowired // Spring에서 제공하는 의존성 주입 어노테이션
	private MemberMapper mapper;

	
	// Bcrypt 암호화 객체 의존성 주입(DI) - SecurityConfig.java에서 @Bean으로 등록한 객체를 주입
	@Autowired 
	private BCryptPasswordEncoder bcrypt;
	
	
	@Override
	public Member login(Member inputMember) {
		
		// 암호화 진행
		// bcrypt.encode(문자열) : 문자열을 암호화하여 반환	
		
		//String bcryptPassword = bcrypt.encode(inputMember.getMemberPw());
		//log.debug("bcryptpassword : " +bcryptPassword);
		
		//bcrypt.mathes(평문,암호화) : 평문과 암호화가 일치하면 true, false 반환
		
		// 1. 이메일이 일치하면서 탈퇴하지 않은 회원 조회
		Member loginMember = mapper.login(inputMember.getMemberEmail());
		
		//2 . 만약에 일치하는 이메일이 없어서 조회결과가 null인경우
		
		if(loginMember == null) return null;
		
		//3 . 입력 받은 비밀번호 (평문 : inputMember.getMemberPw()) 와
		//    암호화된 비밀번호 (loginMember.getMemberPw())
		//    두 비밀번호가 일치하는지 확인 (bcrypt.mathes(평문,암호화))
		
		if (!bcrypt.matches(inputMember.getMemberPw(), loginMember.getMemberPw())) {
			return null;
		}
			
		
		// 로그인 결과에서 비밀번호 제거
		loginMember.setMemberPw(null);
	
		return loginMember;
	}


	//이메일 중복 검사 서비스
	@Override
	public int checkEmail(String memberEmail) {
		
		return mapper.checkEmail(memberEmail);
	}
	
	



}




