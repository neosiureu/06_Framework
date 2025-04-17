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


// @Transactional => 트랜잭션 처리를 수행하라고 지시하는 어노테이션. 이제 커밋과 롤백을 하지 않고 Spring에게 위임하는 어노테이션
// 에러가 안 났으면 알아서 커밋, 에러가 나면 롤백
// 만약 인자로 아무것도 안 쓰는 경우 기본값: Service 내부 코드를 수행 중 Runtime Exception 발생 시 rollback
// 따라서 최상위 예외인 Exception을 처리하여 rollback함 => 어떤 예외가 발생했을 때 rollback할지 지정하는 속성이다.
// Exception.class == 모든 예외 발생 시 rollback 하겠다


@Transactional(rollbackFor = Exception.class)  
@Service // (비즈니스 로직, 즉 데이터 가공이나 트랜잭션 처리 등을 명시함과 동시에 Bean에 등록한다. 즉 @Bean안해도 됨)
public class TodoServiceImpl implements TodoService {
	
	@Autowired // todoDAO와 같은 타입이거나 상속관계인 빈을 SPC에서 찾아서 넣어준다 (DI한다)
	private TodoDAO dao;
	
	@Autowired
	private TodoMapper mapper;

	@Override
	public String testTitle() {
		// TODO Auto-generated method stub
		return dao.testTitle();
	}
	// 커넥션 생성 + 커넥션 반납 (수저통 관리자가 알아서 관리함)
	// 데이터가공
	// 트랜잭션 관리 @Transactional(rollbackFor = Exception.class)  

	@Override
	public Map<String, Object> selectAll() {
		// TODO Auto-generated method stub
		
		// 할일의 목록을 조회하고 
		// 완료된 할일 개수를 조회하며
		// 이 둘의 값을 맵으로 묶어 반환한다
		
		
		
		// 할일의 목록을 조회하고 
		List <Todo> todoList = mapper.selectAll();
		// 완료된 할일 개수를 조회하며
		int completeCount = mapper.getCompleteCount();
		
				// 이 둘의 값을 맵으로 묶어 반환한다
		
		
		Map<String, Object> map = new HashMap<>();
		
		map.put("todoList", todoList);
		map.put("completeCount", completeCount);
		
		return map;
	}
	

}
