package edu.kh.project.member.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.member.model.mapper.MemberMapper;
import lombok.extern.slf4j.Slf4j;

@Service // 비즈니스 로직처리인 서비스 역할을 한다는 것을 프레임워크에 알리고 빈으로 등록
@Transactional(rollbackFor  = Exception.class)
@Slf4j
public class MemberServiceImpl implements MemberService {
	
	// 등록된 bean중 MemberMapper와 같은 타입이거나 상속관계인 Bean을 찾아서 주입해주겠다
	
	@Autowired
	private MemberMapper mapper;
	
	// bcrypt 암호화를 위한 객체를 의존성 주입받자 => config/SecurityConfig.java를 참고한다
	@Autowired
	private BCryptPasswordEncoder bcrypt;

	
	
	
	// 로그인 서비스
	@Override
	public Member login(Member inputMember) {
		
		// 비밀번호에 대한 암호화를 진행한다.
		// 암호화 진행에는 bcrypt라는 이름의 암호화 함수를 사용한다
		
		// bcrypt.encode(문자열) => 비크립트 형식에 따라 암호화된 문자열이 반환 됨
		
		
		
		//	String bcryptPassword = bcrypt.encode(inputMember.getMemberPw());
		
		//	log.debug("bcryptPassword로 암호화된 비밀번호:"+bcryptPassword);
		
		
		
		
		// 비교를 위해 bcrpyt.matches(평문, 암호화): 일치 여부에 따라 true false
		
//$2a$10$82haJV8j1wPHa/JNEywzSOMuYdnFjBdSJvhLPB6ahNHnc/ujwHbUC
		

		// 1. 이메일이 일치하면서 탈퇴하지 않은 회원을 조회한다
		
		Member loginMember = mapper.login(inputMember.getMemberEmail());
		
		
		// 2. 만약 일치하는 이메일이 없어 조회 결과가 null인 경우
		
		if(loginMember == null) return null;
		
		// 3. 입력받은 비밀번호, 평문은  서비스단에서 inputMemeber로 내려오고 있다
		// 암호문은 암호화된 loginMember.getMemberPw()로 DB에서 올라오고 있다. 
		// 두 비밀번호가 일치하는지 확인
		// 일치하지 않으면 그냥 null
		if (!bcrypt.matches(inputMember.getMemberPw(), loginMember.getMemberPw()))
		{
			return null;
		}
		
		
		// 로그인 결과에서 비밀번호를 제거한다 => 비밀번호는 여기서만 신경쓰고 세션에 싣지 않는다
		loginMember.setMemberPw(null); // 객체 중 비밀번호 부분만 null인 상태
		
		return loginMember;
	}

	
	

}
