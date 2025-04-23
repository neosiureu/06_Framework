package edu.kh.project.myPage.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.myPage.model.mapper.MyPageMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service

public class MyPageServiceImpl implements MyPageService {
	
	@Autowired
	private MyPageMapper mapper;
	
	public int updateInfo(Member inputMember, String[] memberAddress) {
		
		// 입력된 주소가 있을 경우와 없을 경우로 나눔
		
		if(!inputMember.getMemberAddress().equals(",,")) {
			// 주소에 입력된 값이 있을 때의 로직
			String address = String.join("^^^",memberAddress);
			inputMember.setMemberAddress(address);
		}
		
		else {
			inputMember.setMemberAddress(null);
		}
		
		return mapper.updateInfo(inputMember);
	}

}
