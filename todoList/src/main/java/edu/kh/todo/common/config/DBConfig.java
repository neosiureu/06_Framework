package edu.kh.todo.common.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;


/*
 * @Configuration
 * - 스프링 설정용 클래스임을 명시 (스프링이 해당클래스 설정정보로 인식하고 사용
 * + 객체로 생성해서 (Bean) 내부 코드를 서버 실행시 모두 바로실행
 * 
 *  @PropertySource("경로") : properties 파일의 내용을 이용하겠다는 어노테이션
    다른 properties도 추가하고 싶으면 어노테이션을 계속 추가
    여러개도 사용가능!
    
 * 
 * */

@Configuration
   
@PropertySource("classpath:/config.properties")
    public class DBConfig {
		
		// 필드
	
    	@Autowired //(DI, 의존성 주입)
	    private ApplicationContext applicationContext; // application scope 객체 : 즉, 현재프로젝트
 
    //@Bean
    //- 개발자가 수동으로 bean을 등록하는 어노테이션
    //- @Bean 어노테이션이 작성된 메서드에서 반환된 객체는
    //Spring Container가 관리함(IOC)
	@Bean
    //@ConfigurationProperties(prefix = "spring.datasource.hikari")
    //properties 파일의 내용을 이용해서 생성되는 bean을 설정하는 어노테이션
    //prefix를 지정하여 spring.datasource.hikari으로 시작하는 설정을 모두 적용
	@ConfigurationProperties(prefix = "spring.datasource.hikari")
	public HikariConfig hikariConfig() {
		return new HikariConfig();
	}

	@Bean
	public DataSource dataSource(HikariConfig config) {
		DataSource dataSource = new HikariDataSource(config);
		return dataSource;
	}

////////////////////////////Mybatis 설정 추가 ////////////////////////////
//SqlSessionFactory : SqlSession을 만드는 객체
@Bean
public SqlSessionFactory sessionFactory(DataSource dataSource) throws Exception{

	SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();

	sessionFactoryBean.setDataSource(dataSource);
    //매퍼 파일이 모여있는 경로 지정

	sessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:/mapper"));



    //별칭을 지정해야하는 DTO가 모여있는 패키지 지정
    //-> 해당 패키지에 있는 모든 클래스가 클래스명으로 별칭이 지정됨

	sessionFactoryBean.setTypeAliasesPackage("edu.kh.project.member.model.dto");
    //마이바티스 설정 파일 경로 지정

	sessionFactoryBean.setConfigLocation(applicationContext.getResource("classpath:mybatis-c"));
    //SqlSession 객체 반환

	return sessionFactoryBean.getObject();

  }

    
    //SqlSessionTemplate : 기본 SQL 실행 + 트랜잭션 처리
	@Bean
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sessionFactory) {
		
		return new SqlSessionTemplate(sessionFactory);
	}


	//DataSourceTransactionManager : 트랜잭션 매니저
	@Bean
	public DataSourceTransactionManager dataSourceTransactionManager(DataSource dataSource) {
		
		return new DataSourceTransactionManager(dataSource);
		
	}
	
	
}
