package edu.kh.todo.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.todo.model.dto.Todo;



/*
 * @Mapper 
 * Mybatis에서 제공하는 어노테이션
 * Mybatis에서 SQL과 Java 메서드를 연결해주는 인터페이스, 즉 Mapper의
 * 구현체를 스프링의 bean으로 등록할 수 있게 해주는 어노테이션
 * */

/* 아직 인터페이스지만 @Mapper를 이용하면 구현체가 생성되어 Bean으로 알아서 등록한다.
	 이 어노테이션을 붙이면 Spring이 Mapper 인터페이스를 인식하고 자동으로 구현체를 생성해준다.
	 이 구현체는 bean으로 등록된다.
	 
	 - 해당 어노테이션이 작성된 인터페이스는 
	 namespace에 해당 인터페이스가 작성된 mapper.xml파일과 연결되어 sql의 호출 및 수행 및 결과반환이 가능하다
	
	xml 파일에서 처음에 이렇게 만들라고
	<mapper namespace=" edu.kh.todo.model.mapper.TodoMapper">
 */


@Mapper
public interface TodoMapper {

	String testTitle();
	// Mapper의 메서드명이 곧 mapper.xml파일 내 태그의 id가 된다
	// 메서드명과 sql구문 중 id가 같은 태그가 서로 연결된다.

	List<Todo> selectAll();

	int getCompleteCount();

	
} // 여기에서 maper.xml로
