package edu.kh.project.common.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/*
 
 Aspect Oriented Programming의 약자로 분산되어있는 관심사와 관점을 모듈화시키는 기법이다.
 
 - 주요 비즈니스 로직과 관련 없는 부가적 기능을 추가할 때 유용하다.
 
 ex) 코드 중간중간에 로그 또는 트랜잭션 처리 하고 싶을 때 등
 
 
 @Transactional(rollbackFor  = Exception.class)도 AOP 기술을 가지고 트랜잭션 처리를 하는 것. 
 개발자가 직접 하는 것이다.
 
 AOP의 주요 어노테이션
 
 1) @Aspect : Aspect를 정의하는데 사용되는 어노테이션으로, 클래스 상단에 작성한다. 공통된 관심사를 나타냄
 
 2) @Before(포인트 컷= 어드바이스 적용할 지점을 인자로): 대상 메서드, 즉 포인트컷의 실행 전에 advice를 실행하겠다.
  
 3) @After(포인트 컷= 어드바이스 적용할 지점을 인자로): 대상 메서드, 즉 포인트컷의 실행 후에 advice를 실행하겠다.

 4) @Around(포인트 컷= 어드바이스 적용할 지점을 인자로): 대상 메서드, 즉 포인트컷의 실행 전후에 advice를 실행하겠다.
 (@Before, @After)

 */

// 공통 관심사가 작성된 클래스임을 명시
//@Aspect
// 이걸 해제하면 다시
// Component와 Bean의 차이? 
// Component는 단순히 클래스를 객체화하여 빈으로 등록
// @Bean은 개발자가 수동으로 만든 객체를 빈으로 등록 (new로 만든 것 위에만)
@Component
@Slf4j
public class TestAspect {
	
	
	// Advice: 끼워넣을 코드 (메서드)
	// Pointcut: 실제로 Advice를 적용할 JoinPoint(지점)
	
	
	
	// <PointCut 작성 법>: 
	
	// execution( [접근제한자] 리턴타입 클래스명 메서드명 [파라미터]  * edu.kh.project..*Controller*.*(..))
	/* 
	
	클래스명은 패키지명부터 풀 네임으로 모두 작성할 것
	// execution: 메서드 실행 지점을 가리키는 키워드
	제일 앞의 별 하나 = 모든 리턴타입 * edu.kh.project..*Controller*.*(..)
	패키지명 * edu.kh.project
	.. : 0개 이상의 하위 패키지
	*Controller* 이름에 컨트롤러를 포함한 모든 클래스
	.*  = 모든 메서드
	(..) 그 메서드의 인자로 0개 이상이 들어갈 때
	
		
	컨트롤러라는 이름이 붙은 이 패키지 클래스의 모든 메서드
	*/
	
	@Before("execution(* edu.kh.project..*Controller*.*(..))") 
	// 어디에 찔러넣을지를 어떻게 넣어?
	
	public void testAdvice() {
		log.info("----------------testAdvice() 수행 됨-------------------");
		// 아직 어디에 찔러 넣어 어느 코드에 넣을 때 할지 정해야 함
	}
	
	
	@After("execution(* edu.kh.project..*Controller*.*(..))")
	public void controllerEnd(JoinPoint jp) { //AOP 기능이 적용된 대상
		log.info("----------------testAdvice() 수행 됨-------------------");
		// AOP가 적용된 클래스의 이름 얻어오기
		String className = jp.getTarget().getClass().getSimpleName();
		// 컨트롤러의 이름만 나온다. MainController, BoardController
		
		// 실행된 컨트롤러 메서드의 이름을 얻어온다.
		String methodName = jp.getSignature().getName();
		// ex) mainPage(), login()
		
		log.info("-------------------------{}.{} 수행 완료---------------", className, methodName);
		// 순서대로 {}에 들어감
	}
	

}
