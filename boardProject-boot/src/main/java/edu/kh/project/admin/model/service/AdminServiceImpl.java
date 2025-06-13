package edu.kh.project.admin.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.admin.model.mapper.AdminMapper;
import edu.kh.project.board.model.dto.Board;
import edu.kh.project.common.utill.Utility;
import edu.kh.project.member.model.dto.Member;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class AdminServiceImpl implements AdminService{

	@Autowired
	private AdminMapper mapper;
	
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	
	@Override
	public Member login(Member inputMember) {
		
		Member loginMember = mapper.login(inputMember.getMemberEmail());
		
		System.out.println(loginMember);
		if(loginMember ==null) {
			return null;
		}
		
		if(!bcrypt.matches(inputMember.getMemberPw(), loginMember.getMemberPw())) {
			return null;
		}
		
		loginMember.setMemberPw(null);
		
		return loginMember;
		
		
	}
	
	@Override
	public Board maxCommentCount() {
		
		return mapper.maxCommentCount();
	}
	@Override
	public Board maxLikeCount() {
		
		return mapper.maxLikeCount();
	}
	@Override
	public Board maxReadCount() {
		
		return mapper.maxReadCount();
	}
	
	@Override
	public List<Member> getNewMember() {
		
		return mapper.getNewMember();
	}
	@Override
	public List<Member> selectWithdrawnMemberList() {
		
		return mapper.selectWithdrawnMemberList();
	}
	@Override
	public List<Board> selectWithdrawnboardList() {
	
		return mapper.selectWithdrawnboardList();
	}

	@Override
	public int restoreMember(int memberNo) {
		
		return mapper.restoreMember(memberNo);
	}
	
	@Override
	public int restoreBoard(int boardNo) {
		
		return mapper.restoreBoard(boardNo);
	}
	
	
	@Override
	public int checkEmail(String memberEmail) {
		
		return mapper.checkEmail(memberEmail);
	}
	@Override
	public String createAdminAccount(Member member) {
		
		
		// 1. 영어(대소문자), 숫자도 포함 6 자리 난수로 만든 비밀번호를 암호화 한 값 구하기
		String rawPw = Utility.generatePw(); // 평문 비번
		
		
		// 2. 평문 비밀번호를 암호화하여 저장
		String encPw = bcrypt.encode(rawPw);
		
		// 3. member에 암호화된 비밀번호 세팅
		member.setMemberPw(encPw);
		
		// 4. DB에 암호화된 비밀번호가 세팅 된 member를 전달하여
		//    계정 발급
		int result = mapper.createAdminAccount(member);
		
		// 5. 계정 발급 정상처리 되었다면 , 발급된(평문) 비밀번호 리턴
		
		if(result > 0 ) {
			return rawPw; //평문 비밀번호를 보내야되징 ?
			
		}else {
			return null;
		}
		
	
	}
	
	@Override
	public List<Member> getadminList() {
		
		return mapper.getadminList();
	}
}
