package edu.kh.todo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data 
public class Todo {
	
	private int todoNo;          // 할일 번호
	private String todoTitle;    // 할일 제목
	private String todoContent;  // 할일 내용
	private String complete;     // 할일 상태 (Y/N)
	private String regDate;      //  할일 등록일
}

	
