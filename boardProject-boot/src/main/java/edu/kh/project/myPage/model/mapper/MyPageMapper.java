package edu.kh.project.myPage.model.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.member.model.dto.Member;





@Mapper
@Transactional(rollbackFor = Exception.class)

public interface MyPageMapper {

	/** 회원정보 수정
	 * @param inputMember
	 * @return
	 */
	int updateInfo(Member inputMember);

}
