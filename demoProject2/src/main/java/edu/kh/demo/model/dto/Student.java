package edu.kh.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Spring EL 같은 경우 DTO 객체 출력할 때 getter 가 필수 작성 되어있어야함.
// -> ${Student.name} == ${Student.getName()}

@NoArgsConstructor
@Data     // Getter + Setter + ToString
@AllArgsConstructor //모든 필드 초기화용 매개변수 생성자
public class Student {
	
	private String studentNo ; //학생번호
	private String name;       // 이름
	private int    age;        // 나이
}



