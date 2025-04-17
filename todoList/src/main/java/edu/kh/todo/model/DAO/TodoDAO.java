package edu.kh.todo.model.DAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import edu.kh.todo.model.mapper.TodoMapper;

@Repository // DAO 계층 역할 명시 + Bean 등록
public class TodoDAO {
	
	@Autowired // 의존성주입 (DI) -> 같은타입 + 상속관계 Bean을 의존성 주입(DI)
	private TodoMapper mapper; // mapper에는 TodoMapper의 구현체가 의존성 주입 됨
	                      // 그 구현체가 sqlSessionTemplate 이용
	// 마이바티스
	// @Mapper 만 사용 가장흔함
	// @Mapper + @Repository -> 자주 사용하진 않으나 가능!
	// @Repository -> 불가능

	public String testTitle() {
		
		// 결과 저장용변수 선언
		// Sql 작성
		// pstmt/ResultSet 등 객체 생성 / 세팅 /사용
		// 결과값 얻어온것 가공
		
		return mapper.testTitle();
	}
}
