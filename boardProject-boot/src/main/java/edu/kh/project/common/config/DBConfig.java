package edu.kh.project.common.config;

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

//@PropertySource (경로) : properties 파일의 내용을 이용하겠다는 어노테이션
//스프링 설정용 클래스임을 명시하는 어노테이션
// 객체로 생성하여 안에 있는 내부 코드를 서버 실행시 모두 바로 실행
// 다른 properties도 추가하고 싶으면 어노테이션을 계속 추가

// 서버가 켜지면 서버가 설정용 클래스임을 인식 => 저 내용은 객체로 미리 생성해서 관리하고 실행
@Configuration

// 다음 파일 내용을 전부 읽어 이용하자
@PropertySource("classpath:/config.properties")

public class DBConfig {
	@Autowired
	// 스프링이 컨테이너에 빈으로서 관리하고 있는 applicationContext를 이 자리에 넣어주자. 만약 없으면 만들어주자
	// private ApplicationContext applicationContext = new ApplicationContext();를
	// 해야하는데 스프링이 한걸 그냥 받는 것
	private ApplicationContext applicationContext;

	// application scope 객체 => 현재 프로젝트 자체
	// 의존성 주입 = DI <= @Autowired (프레임워크한테서 객체가 필요한 곳에 갖다 넣어주라고 한다)

	// 현재 프로젝트의 전반적인 DB설정과 DB에 관한 Bean 관리에 접근할 수 있도록 함 => 그러한 객체는 서버 내내 유지되어야 하니
	// application Scope여야지
	// 따라서 의존성 주입받는 것

// @Bean
// - 개발자가 수동으로 bean을 등록하는 어노테이션
// - @Bean 어노테이션이 작성된 메서드에서 반환된 객체는
// Spring Container가 관리함(IOC)

// @ConfigurationProperties(prefix = "spring.datasource.hikari")
// properties 파일의 내용을 이용해서 생성되는 bean을 설정하는 어노테이션
// prefix를 지정하여 spring.datasource.hikari으로 시작하는 설정을 모두 적용

	@Bean
	@ConfigurationProperties(prefix = "spring.datasource.hikari")
	public HikariConfig hikariConfig() {

		return new HikariConfig();
	}

	// HikariCP라는 커넥션 수저통을 잘 다뤄주는 모듈을 다루기 위해 "개발자가" 객체를 생성
	// @Bean을 붙여서 개발자가 스프링에게 Bean을 등록

	// @ConfigurationProperties의 의미: classpath:/config.properties로부터
	// spring.datasource.hikari로 시작하는(접두사) 설정의 내용을 읽어오자
	// 그 설정들을 수저통관리자를 만드는데 사용하여 적용하겠다. 자동으로 HikariConfig라는 객체에 알맞은 필드에 세팅 => 객체화 된다.
	// HikariConfig의 필드에 선언된 내용들이 사실 config.properties 내에서 내가 지정한 것이었다

	@Bean
	public DataSource dataSource(HikariConfig config) {
		// 수저통관리자를 의존성 주입. 이미 Bean으로 관리되고 있으므로 argument resolver는 Bean container에서 알아서
		// 매개변수로 넣어 줌

		// 매개변수 Hikari config config
		// 등록된 Bean중 HIkariConfig 타입의 Bean을 자동으로 주입
		// -> HikariConfig 객체를 받아 DataSource객체를 생성하는데 사용

		DataSource dataSource = new HikariDataSource(config); // 상위호환의 존재를 대비하기 위한 다형성과 인터페이스의 예시

		// 애플리케이션이 데이터베이스에 연결할 때 사용하는 설정 (HikariConfig는 정보만 가짐 => 진짜 설정 객체를 가지는 것은
		// DataSource)

		// 1) DB연결정보 제공 (url userName password)
		// 2) Connection Pool 관리 -> 즉 수저통에 대한 관리
		// 3) commit rollback 관리
		return dataSource;
	}

	// 위 클래스도 빈으로 등록 => 반환하는 객체인 dataSource를 빈으로 등록하고 Spring이 관리

	/********************
	 * sessionFactoryBean이라는 공장에 MyBatis를 이용하기 위한 각종 세팅을 함
	 **************/

// SqlSessionFactory : SqlSession을 만드는 객체
	@Bean
	public SqlSessionFactory sessionFactory(DataSource dataSource) throws Exception {
		// 매개변수로 dataSource를 받아와 DB연결정보로 사용할 수 있도록 함

		// MyBatis에 SqlSession을 생성하는 역할을 할 객체를 생성
		SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();

		// mybatis에 대한 설명
		// 자바 어플리케이션에서 SQL을 더 쉽게 사용할 수 있도록 도와주는 영속성 프레임워크
		// => 영속성 프레임워크는 애플리케이션의 데이터를 데이터베이스와 같은 저장소에 영구적으로 저장하고 이를 쉽게 CRUD할 수 있도록 도와주는
		// 프레임워크

		sessionFactoryBean.setDataSource(dataSource); // 매퍼 파일이 모여있는 경로 지정

		// [세팅1] mapper.xml(SQL 작성해둘 파일) 파일이 모이는 경로를 지정한다
		// 매퍼 파일이 모여있는 경로 지정
		sessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:/mappers/**.xml"));
		// 현재 프로젝트에서 소스정보를 얻어온다 => classpath하위의 mapper폴더를 다 읽어와 관리하게 하자.
		// 폴더 이름을 mappers로 지어서 폴더를 만들었었으므로 이렇게 지어야. 그 폴더 안에 xml 파일들을 전부 읽어오겠다.
		// *이 아닌 **를 써야한다는 특징이 있음

// 별칭을 지정해야하는 DTO가 모여있는 패키지 지정
// -> 해당 패키지에 있는 모든 클래스가 클래스명으로 별칭이 지정됨

		/*
		 * [세팅2] 해당 패키지에 있는 모든 클래스에 대해 별칭이 지정됨 마이 바티스는 특정 클래스 지정 시 패키지명.클래스명이라는 긴 이름을
		 * 사용해야 함 긴 이름을 짧게 부를 수 있도록 별칭을 설정하는 방법
		 */

		sessionFactoryBean.setTypeAliasesPackage("edu.kh.project");
		// 마이바티스 설정 파일 경로 지정 => 세팅 시 패키지 하위에 있는 모든 클래스가 별칭으로 지정 됨
		// edu.kh.project라고 세팅 시 패키지 하위에 있는 모든 클래스가 클래스명으로 별칭이 지정 됨
		// ex) edu.kh.project.model.dto.Todo 대신 그냥 Todo라고 할 수 있음

		sessionFactoryBean.setConfigLocation(applicationContext.getResource("classpath:mybatis-config.xml"));
		// 현재 프로젝트에서 소스정보를 얻어온다 => classpath하위의 mapper폴더를 다 읽어와 관리하게 하자.
		// 파일 이름을 mybatis-config.xml

		// [세팅3] mybatis-config.xml의 내용을 읽어들임 => <setting name="jdbcTypeForNull"
		// value="NULL"/> <setting name="mapUnderscoreToCamelCase" value="true"/>

		// SqlSession 객체 반환
		return sessionFactoryBean.getObject();

	}
	

// 지금까지는 팩토리 자체 

// SqlSessionTemplate : 기본 SQL 실행 + 트랜잭션 처리
// Connection + DBCP (Data Base Connection Pool) + Mybatis + 커밋 롤백	
	@Bean
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sessionFactory) {
		return new SqlSessionTemplate(sessionFactory);
	}

// DataSourceTransactionManager : 트랜잭션 매니저 => 로그, 트랜잭션관리, 보안 등 코드 중간중간에 끼어들어 공통된 관심사를 처리하는 AOP의 일종
	@Bean
	public DataSourceTransactionManager dataSourceTransactionManager(DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}
}
