package edu.kh.project.member.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//DTO(Data Transfer Object) : 데이터 전송 객체
//- 데이터 전달용 객체
//- DB에서 조회된 결과를 담을 용도 또는
// SQL 구문에 사용할 값을 전달하는 용도로
// 관련성 있는 데이터를 한번에 묶어서 다룸.

//VO(Value Object) : 값 객체

@NoArgsConstructor // 매개변수가 없는 기본 생성자 자동 생성
@AllArgsConstructor // 모든 필드를 매개변수로 받는 생성자 자동 생성
@Data // Lombok 라이브러리에서 제공하는 어노테이션으로, getter, setter, toString, equals, hashCode 메서드를 자동으로 생성
@Builder // Builder 패턴을 사용하여 객체를 생성할 수 있도록 하는 어노테이션
public class Member {
	
	private int memberNo;            //회원 번호	
	private String memberEmail;      //회원 이메일
	private String memberPw;         //회원 비밀번호
	private String memberNickname;;  //회원 닉네임
	private String memberTel;        //회원 전화번호
	private String memberAddress;    //회원 주소
	private String profileImg;       //회원 프로필 이미지
	private String enrollDate;       //회원 가입일
	private String memberDelFl;      //회원 탈퇴 여부
	private int authority;           //회원 권한
	
	
}
