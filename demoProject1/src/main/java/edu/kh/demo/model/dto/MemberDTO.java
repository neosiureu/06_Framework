package edu.kh.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class MemberDTO {
	private String memberId;
	private String memberPw;
	private String memberName;
	private int memberAge;
}
