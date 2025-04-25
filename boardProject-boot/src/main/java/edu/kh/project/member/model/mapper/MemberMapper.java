package edu.kh.project.member.model.mapper;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.project.member.model.dto.Member;

@Mapper // MyBatis가 해당 인터페이스를 Mapper로 인식하도록 하는 어노테이션
public interface MemberMapper {

	/** 로그인 SQL 실행
	 * @param string
	 * @return
	 */
	Member login(String memberEmail);

	/** 이메일 중복검사
	 * @param memberEmail
	 * @return
	 */
	int checkEmail(String memberEmail);

	/** 아이디 중복검사 
	 * @param memberNickname
	 * @return
	 */
	int checkNickname(String memberNickname);

	/** 전화번호 중복검사
	 * @param memberTel
	 * @return
	 */
	int checkTel(String memberTel);

	/** 회원가입하는 메서드
	 * @param inputMember
	 * @return
	 */
	int signup(Member inputMember);

	/** 아이디 찾기 메서드
	 * @param inputMember
	 * @return
	 */
	Member findMember(Member inputMember);

	/** 비밀번호 재설정 전 아이디가 있는지
	 * @param inputMember
	 * @return
	 */
	Member findPw(Member inputMember);

	/**로그인 안된상태에서 비밀번호 재설정
	 * @param inputMember
	 * @param memberNo 
	 * @return
	 */
	int findPwConfirm(Member inputMember, int memberNo);

	

}
