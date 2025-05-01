package edu.kh.project.member.model.service;

import edu.kh.project.member.model.dto.Member;

public interface MemberService {

   
   /** 로그인 서비스
    * @param inputMember
    * @return loginMember(Member)
    */
   Member login(Member inputMember);

   /** 이메일 중복검사 서비스
    * @param memberEmail
    * @return 일치하는 이메일 개수
    */
   int checkEmail(String memberEmail);

   
   
   /** 닉네임 중복검사 서비스
    * @param memberNickname
    * @return
    */
   int checkNickname(String memberNickname);

   // 회원가입 서비스
   int signup(Member inputMember, String[] memberAddres);


   String selectID(Member member);
   
   String processPasswordFind(Member member);
   
   

}
