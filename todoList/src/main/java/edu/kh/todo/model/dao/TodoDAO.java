package edu.kh.todo.model.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import edu.kh.todo.model.mapper.TodoMapper;

@Repository // DAO 계층 역할 명시 + @Bean으로 등록해 줌
public class TodoDAO {
	// 마이바티스를 사용할 때는 DAO만으로는 DB에 연결이 안 되며 그냥 @Mapper만을 이용하는 경우가 대부분 (Mapper+Repository를 둘 다 사용할 수도 있다)
	// Repository만 있으면 불가능  => Repositoy 어노테이션은 SQL을 해석하지 못하기 때문
	
	
	@Autowired // (의존성 주입해주는 어노테이션)
	private TodoMapper mapper;
	// 스프링은 SPC에 등록되어있는 빈들 중 TodoMapper와 같은 타입이거나 상속관계인 빈을 찾아 넣어준다 (의존성 주입한다 = DI한다)

	public String testTitle() {
		// TODO Auto-generated method stub
		return mapper.testTitle();
	}
	// 결과저장용 변수 선언 
	// SQL 작성 (mapper가 해줌)
	// pstmt, resultSet 등의 JDBC 객체 생성 및 사용 (mapper가 해줌)
	// 결과 값의 가공 (mapper가 해줌)
	
	// 위 내용이 아무것도 필요 없어
	
	
	// mapper에는 TodoMapper의 구현체가 의존성 주입되며
	// 그 구현체는 sqlSessionTemplate을 이용한다. (DBConfig에서 엄청 길게 만든 것의 결론이 sqlSessionTemplate)
	

}
