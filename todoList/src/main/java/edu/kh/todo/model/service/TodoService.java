package edu.kh.todo.model.service;

import java.util.Map;

public interface TodoService {

	
	
	/** (Test) todoNo가 1인 할일의 제목을 조회한다. 
	 * @return title
	 */
	String testTitle();

	/** 할일의 목록과 완료된 할일의 개수를 조회한다
	 * 그 둘을 묶어맵으로 리턴한다
	 * @return
	 */
	Map<String, Object> selectAll();
	

}
