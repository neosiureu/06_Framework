package edu.kh.project.myPage.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.myPage.model.dto.UploadFile;

@Mapper
public interface MyPageMapper {

	int updateInfo(Member inputMember);

	/** 현재 로그인한 사람의 비밀번호를 가져옴
	 * @param memberNo
	 * @return
	 */
	String selectPw(int memberNo);

	/** 회원의 비밀번호를 변경
	 * @param paramMap
	 * @return
	 */
	int changePw(Map<String, String> paramMap);

	/** 회원 탈퇴
	 * @param memberNo
	 * @return
	 */
	int secession(int memberNo);

	/** 전달받은 파일을 DB에 insert
	 * @param uf
	 * @return
	 */
	int insertUploadFile(UploadFile uf);

	/** 파일 목록 조회
	 * @param memberNo
	 * @return
	 */
	List<UploadFile> fileList(int memberNo);

	
}
