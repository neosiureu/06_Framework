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
	    private ApplicationContext applicationContext; // application scope 객체 : 
    	//즉, 현재프로젝트
    	// -> 스프링이 관리하고 있는 ApplicationConext 객체를 의존성 주입받는다.
    	// - > 현재 프로젝트의 전반적인 DB 설정과 Bean 관리에 접근할 수 있도록 해줌.
 
    	//메서드
    	
    ///////////////////////////// HikariCP 설정 //////////////////////////////////	
    
    	//@Bean
    //- 개발자가 수동으로 bean을 등록하는 어노테이션
    //- @Bean 어노테이션이 작성된 메서드에서 반환된 객체는
    //Spring Container가 관리함(IOC)
	
    //@ConfigurationProperties(prefix = "spring.datasource.hikari")
    //properties 파일의 내용을 이용해서 생성되는 bean을 설정하는 어노테이션
    //prefix를 지정하여 spring.datasource.hikari으로 시작하는 설정을 모두 적용
    
    	@Bean
	@ConfigurationProperties(prefix = "spring.datasource.hikari")
	public HikariConfig hikariConfig() {
		
		//-> config.properties 파일에서 읽어온
		// spring.datasource.hikari로 시작하는 모든 값이
		// 자동으로 HikariConfig 라는 객체의 알맞은 필드에 셑잉이되어
		// 객체화 됨.
		return new HikariConfig(); // new 연산자를 통해 개발자가 만듬
	}

	@Bean                         //
	public DataSource dataSource(HikariConfig config) {
		//매개변수 HikariConfig config
		//-> 등록된 Bean 중 HikariConfig 타입의 Bean을 자동으로 주입
		//-> HikariConfig 객체를 받아, DataSource 객체를 생성하는데 사용.
		
		
		
		DataSource dataSource = new HikariDataSource(config);
		                               
		// DataSource:
		// 애플리케이션이 데이터베이스에 연결할 때 사용하는 설정.
		// 1) DB 연결 정보제공 (url, username, password)
		// 2) Connection pool관리 (Connection 생성/생명주기 관리)
		// 3) 트랜잭션 관리 
		return dataSource;
	}

////////////////////////////Mybatis 설정 추가 ////////////////////////////
    
	// mybatis : Java 애플리케이션에서 SQL 을 더 쉽게 사용 할 수 있도록 도와주는
	//           영속성 프레임 워크.
	// 영속성 프레임워크(persistence Framework)는 애플리케이션의 데이터를
	// 데이터베이스와 같은 저장소에 영구적으로 저장하고 ,
	// 이를 쉽게 CRUD 할 수 있도록 도와주는 프레임워크.
	

	//SqlSessionFactory : SqlSession을 만드는 객체
   @Bean
   public SqlSessionFactory sessionFactory(DataSource dataSource) throws Exception{
	                        //매개변수로 DataSource를 받아와 DB 연결 정보를 사용할수 있도록함.
	   						
    //myBatis의 SqlSession을 생성하는 역할을 할 객체를 생성
	SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
	
	// sessionFactoryBean이름의 공장에 MyBatis를 이용하기위한 각종 세팅을 함..
	
	sessionFactoryBean.setDataSource(dataSource);

	// 세팅1. mapper.xml(SQL 작성해둘 파일) 파일이 모이는 경로를 지정
	// -> Mybatis 코드 수행시 XXX-mapper.xml 을 읽을수 있음.
	//매퍼 파일이 모여있는 경로 지정
	sessionFactoryBean.setMapperLocations(applicationContext.
			getResources("classpath:/mappers/**.xml"));
	                                    //mappers 하위에있는 모든 xml을 읽어옴
	                                     // sql파일이 여러개일수도잇음
	
 	//현재 프로젝트.자원을 얻겠다.src/main/resources/하위의 모든 xml
	
	 // 세팅2. 해당 패키지 내 모든 클래스의 별칭을 등록
	//-> 별칭을 지정하지 않으면 클래스명으로 사용해야함.
	// -> 긴 이름을 짧게 부를수있또록 별칭을 지정해줌.
	



    //별칭을 지정해야하는 DTO가 모여있는 패키지 지정
    //-> 해당 패키지에 있는 모든 클래스가 클래스명으로 별칭이 지정됨

	sessionFactoryBean.setTypeAliasesPackage("edu.kh.todo");
	                          //edu.kh.todo 하위에있는 클래스들은 클래스명으로만
	                          // 쓸수잇게해줌
	// ->edu.kh.todo 하위에 있는 모든 클래스는 별칭으로 지정됨.
	// -> ex) edu.kh.todo.model.dto.Todo -> Todo(별칭 등록)

	//마이바티스 설정 파일 경로 지정
	sessionFactoryBean.setConfigLocation(applicationContext.
			getResource("classpath:mybatis-config.xml"));
	// mybatis=config.xml 파일을 읽어들임
	// -> jdbcTypeForNull / mapUnderscoreToCamlcase를 적용함
	
	//SqlSession 객체 반환
	return sessionFactoryBean.getObject();

  }

    
    //SqlSessionTemplate : 기본 SQL 실행 + 트랜잭션 처리
    // Connection + DBCP + mybatis + 트랜잭션 처리 
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
