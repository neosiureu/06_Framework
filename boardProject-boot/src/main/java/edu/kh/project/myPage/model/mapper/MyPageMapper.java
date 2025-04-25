package edu.kh.project.myPage.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.myPage.model.dto.UploadFile;





@Mapper
@Transactional(rollbackFor = Exception.class)

public interface MyPageMapper {

	/** 회원정보 수정
	 * @param inputMember
	 * @return
	 */
	int updateInfo(Member inputMember);

	
	/** 비밀번호를 바꿀 회원의 현재 비밀번호를 일단 조회한다
	 * @param memberNo
	 * @return
	 */
	String selectPw(int memberNo);


	/** 확인된 회원의 비밀번호를 변경한다.
	 * @param paramMap 
	 * @return
	 */
	int changePw(Map<String, String> paramMap);


	/** 회원 탈퇴
	 * @param memberNo
	 * @return
	 */
	int secession(int memberNo);


	/** 파일정보를 DB에 삽입
	 * @param uf
	 * @return
	 */
	int insertUploadFile(UploadFile uf);


	List<UploadFile> fileList(int memberNo);

	
	
}
