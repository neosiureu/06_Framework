package edu.kh.todo.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.todo.model.DAO.TodoDAO;
import edu.kh.todo.model.dto.Todo;
import edu.kh.todo.model.mapper.TodoMapper;

// MVC + IOC

//@Transactional
// - 트랜잭션 처리를 수행하라고 지시하는 어노테이션
// - 정상 코드 수행시 COMMIT
// - 기본 값 : Service 내부 코드 수행중 RuntimeException 발생 시 Rollback
// rollbackFor 속성 : 어떤 예외가 발생했을때 rollback 할 지 지정하는 속성
// Exception.class == 모든 예외 발생시 rollback 하겠다

@Transactional(rollbackFor = Exception.class)
@Service // 비즈니스 로직 (데이터 가공, 트랜잭션 처리등) 역할명시 + Bean 등록
public class TodoServiceImpl implements TodoService {
	
	@Autowired // TodoDAO와 같은 타입/ 상속관계 Bean 의존성 주입(DI)
	private TodoDAO dao;
	
	@Autowired
	private TodoMapper mapper;

	@Override
	public String testTitle() { 
		
		return dao.testTitle();
	
	}

	@Override
	public Map<String, Object> selectAll() {
	   
		Map<String, Object> map = new HashMap<>();

	    // "todoList"에 List<Todo> 객체 저장
	    List<Todo> todoList = mapper.selectAll(); // 올바른 DAO 호출
	    map.put("todoList", todoList);

	    // "completeCount"에 완료된 할 일 개수 저장
	    int completeCount = mapper.getCompleteCount(); // 올바른 DAO 호출
	    map.put("completeCount", completeCount);

	    return map;
	}

	@Override
	public int addTodo(String todoTitle, String todoContent) {
		
		// 마이바티스에서 SQL에 전달할 수 있는 파라미터 개수는 오직 1개!!
		// -> todoTitle, todoContent 여러개인 파라미터를 전달하려면
		//    묶어야 한다.!
		// Todo DTO로 묶어서 전달하면 된다!
		Todo todo = new Todo();
		todo.setTodoTitle(todoTitle);
		todo.setTodoContent(todoContent);
		
		return mapper.addTodo(todo);
	}

	@Override
	public Todo todoDetail(int todoNo) {
		
		return mapper.todoDetail(todoNo);
	}

	@Override
	public int changeComplete(Todo todo) {
		
		
		return mapper.changeComplete(todo);
	}

	@Override
	public int todoUpdate(Todo todo) {
		
		
		return mapper.todoUpdate(todo);
	}

	@Override
	public int todoDelete(int todoNo) {
		
		return mapper.todoDelete(todoNo);
	}

	@Override
	public int getTotalCount() {
		
		return mapper.getTotalCount();
	}

	@Override
	public int getCompleteCount() {
		
		return mapper.getCompleteCount();
	}

	@Override
	public List<Todo> selectList() {
		
		return mapper.selectAll();
	}

	
	
	
	

}

