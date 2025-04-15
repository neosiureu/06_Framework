package edu.kh.demo.model.dto;

import lombok.*;



@Data
@NoArgsConstructor
public class MemberDTO {
	private String memberId;
	private String memberPw;
	private String memberName;
	private int memberAge;
}
