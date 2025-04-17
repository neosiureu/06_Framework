package edu.kh.todo.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.todo.model.dao.TodoDAO;
import edu.kh.todo.model.dto.Todo;
import edu.kh.todo.model.mapper.TodoMapper;

// @Transactional 
// - 트랜잭션 처리를 수행하라고 지시하는 어노테이션
// - 정상 코드 수행 시 COMMIT
// - 기본값 : Service 내부 코드 수행 중 RuntimeException 발생 시 rollback
// rollbackFor 속성 : 어떤 예외가 발생했을 때 rollback 할 지 지정하는 속성
// Exception.class == 모든 예외 발생 시 rollback 하겠다

@Transactional(rollbackFor = Exception.class)
@Service  // 비즈니스 로직(데이터가공, 트랜잭션 처리등) 역할 명시 + Bean 등록
public class TodoServiceImpl implements TodoService{

	@Autowired // TodoDAO와 같은 타입/상속관계 Bean 의존성 주입(DI)
	private TodoDAO dao;
	
	@Autowired
	private TodoMapper mapper;

	@Override
	public String testTitle() {
		return dao.testTitle();
	}

	@Override
	public Map<String, Object> selectAll() {
		
		// 1. 할 일 목록 조회
		List<Todo> todoList = mapper.selectAll();
		
		// 2. 완료된 할 일 개수 조회
		int completeCount = mapper.getCompleteCount();
		
		// 3. 위 두개 결과값을 Map으로 묶어서 반환
		Map<String, Object> map = new HashMap<>();
		
		map.put("todoList", todoList);
		map.put("completeCount", completeCount);
		
		return map;
	}

	@Override
	public int addTodo(String todoTitle, String todoContent) {
		
		// 마이바티스에서 SQL에 전달할 수 있는 파라미터 개수는 오직 1개!!!
		// -> todoTitle, todoContent 여러개인 파라미터를 전달하려면 
		//    Todo DTO로 묶어서 전달하면 된다!
		Todo todo = new Todo();
		todo.setTodoTitle(todoTitle);
		todo.setTodoContent(todoContent);
		
		return mapper.addTodo(todo);
	} 
	
	@Override
	public Todo todoDetail(int todoNo) {
		return mapper.todoDetail(todoNo);
	}
	
	
	
	
	
}