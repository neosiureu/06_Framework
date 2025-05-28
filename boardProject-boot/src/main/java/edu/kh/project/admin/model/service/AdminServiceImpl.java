package edu.kh.project.admin.model.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.member.model.dto.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor

class AdminServiceImpl implements AdminService {
	
	
	
	private final edu.kh.project.admin.model.mapper.AdminMapper mapper;
	
	private final BCryptPasswordEncoder bcrypt;
	
	
	
	@Override
	public Member login(Member inputMember) {
		
		String encodedPw = bcrypt.encode("pass01!");
	    log.debug("BCrypt 인코딩 결과 (pass01!): {}", encodedPw);
	    
		
		Member loginMember = mapper.login(inputMember.getMemberEmail());
		
		
		log.debug("입력 PW: {}", inputMember.getMemberPw());
		log.debug("DB PW: {}", loginMember.getMemberPw());
		log.debug("비교 결과: {}", bcrypt.matches(inputMember.getMemberPw(), loginMember.getMemberPw()));

		
		if(loginMember == null) {
			return null;
		}
		
		if(!bcrypt.matches(inputMember.getMemberPw(), loginMember.getMemberPw()))
		{
			return null;
		}
			
		loginMember.setMemberPw(null);
		
		return loginMember;
		
	}

	
}
