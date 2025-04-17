package edu.kh.todo.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.todo.model.dto.Todo;


/*
 * @Mapper
 * - Mybatis 제공하는 어노테이션
 * - Mybatis에서 SQL과 Java 메서드를 연결해주는 인터페이스(Mapper)의 
 *   구현체를 스프링의 Bean으로 등록할 수 있게주는 어노테이션.
 * 
 * 이 어노테이션을 붙이면 Spring이 Mapper인터페이스를 인식하고, 자동으로 구현체를 생성해줌.
 * -> 이 구현체가 Bean 으로 등록됨!
 * 
 * - 해당 어노테이션이 작성된 인터페이스는
 *   namespace에 해당 인터페이스가 작성된 mapper.xml 파일과 연결되어
 *   SQL 호출/수행/결과 반환 가능
 * 
 * */


@Mapper
public interface TodoMapper {

	// Mapper의 메서드명 == mapper.xml 파일 내 태그의 id
	// -> 메서드명과 sql구문중 id가 같은 태그가 서로 연결된다!
	
	String testTitle();

	List<Todo> selectAll();

	int getCompleteCount();

	int addTodo(Todo todo);

	Todo todoDetail(int todoNo);
	
	
	
	
	
	
	
	

}