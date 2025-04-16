package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
	private String studentNo; // 학생 번호
	private String name; 
	private int age;
	// 왜 getter가 필수적?
	// Spring EL같은 경우 DTO 객체를 출력할 때 getter가 필수 작성 되어 있어야
	// -> ${Student.name} == 내부적으로는 Student.getName()과 동일
	
}
