package edu.kh.project.myPage.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.myPage.model.dto.UploadFile;

public interface MyPageService {

	/** 회원정보 수정 서비스
	 * @param inputMember
	 * @return
	 */
	int updateInfo(Member inputMember, String[] memberAddress);

	/** 비밀번호 변경 서비스
	 * @param memberNo
	 * @param paramMap
	 * @return
	 */
	int changPw(int memberNo, Map<String, String> paramMap);

	/** 회원 탈퇴 (del 를 y로 바꿀꺼임)
	 * @param memberNo
	 * @param memberPw
	 * @return
	 */
	int secession(int memberNo, String memberPw);

	/** 파입 업로드 테스트1
	 * @param uploadFile
	 * @return path
	 */
	String fileUpload1(MultipartFile uploadFile)throws Exception;

	/** 파일 업로드 테스트 2 
	 * @param memberNo
	 * @param uploadFile
	 * @return
	 */
	int fileUpload2(int memberNo, MultipartFile uploadFile)throws Exception;

	/**파일 목록 조회
	 * @param memberNo
	 * @return
	 */
	List<UploadFile> fileList(int memberNo);

	

}
