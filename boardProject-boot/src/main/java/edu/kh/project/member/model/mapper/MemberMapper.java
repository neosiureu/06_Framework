package edu.kh.project.member.model.mapper;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.project.member.model.dto.Member;

@Mapper
public interface MemberMapper {

	
	/** loginSQL을 실행하는 메서드
	 * @param memberEmail
	 * @return
	 */
	Member login(String memberEmail);

	/** 이메일 중복검사 추상메서드 매퍼
	 * @param memberEmail
	 * @return
	 */
	int checkEmail(String memberEmail);

}
