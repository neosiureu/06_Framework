package edu.kh.todo.model.service;

import java.util.List;
import java.util.Map;

import edu.kh.todo.model.dto.Todo;




/**
 * 
 */
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

	int addTodo(String todoTitle, String todoContent);

	
	
	
	Todo todoDetail(int todoNo);

	

	/** 완료여부를 변경한다
	 * @return result
	 */
	int changeComplete(Todo todo);

	
	/** 할일을 수정한다
	 * @return result
	 */
	int todoUpdate(Todo todo);

	
	/** 할일을 삭제한다
	 * @return result
	 */
	int todoDelete(Todo todo);

	int getTotalCount();

	/** 완료된 할일 개수 조회
	 * @return completeCount
	 */
	int getCompleteCount();

	
	/** 완료된 할일
	 * @return completeCount
	 */
	List<Todo> selectList();

	int todoDelete(int todoNo);
	
}

