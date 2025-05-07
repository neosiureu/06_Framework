package edu.kh.project.member.model.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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

   /** 닉네임 중복검사
    * @param memberNickname
    * @return
    */
   int checkNickname(String memberNickname);

   /** 회원가입
    * @param inputMember
    * @return
    */
   int signup(Member inputMember);


   String selectID(Member member);

   
   Member selectMemberByNicknameEmailTel(Member member); 
   
   
   int updatePasswordByMemberNo(@Param("memberNo") int memberNo, @Param("memberPw") String memberPw);

Member selectMemberByNo(int memberNo);
}
