package edu.kh.project.common.aop;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import edu.kh.project.member.model.dto.Member;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@Aspect
//HttpHeader - 요청에 중요 정보
// HttpBody  - Http요청 데이터 내용

public class LogginAspect {
	
	/** 컨트롤러 수행 전 로그 출력(클래스/메서드/ip)
	 * @param jp
	 */
	@Before("PointcutBundle.controllerPointcut()")
	public void beforeController(JoinPoint jp) {
		
		// 클래스명 얻어오기
		String className = jp.getTarget().getClass().getSimpleName();
		
		String methodName = jp.getSignature().getName();
		
		// 요청한 클라이언트의 HttpServletRequest 객체 얻어오기
		
		HttpServletRequest req =
				((ServletRequestAttributes)RequestContextHolder
						.currentRequestAttributes()).getRequest();
		
		// 클라이언트 ip 얻어오기
		String ip = getRemoteAddr(req);
		
		StringBuilder sb = new StringBuilder();  // 가변성 
		
		sb.append(String.format("[%s.%s] 요청 / ip: %s", className, methodName,ip));
		// [MainController.mainPage] 요청 / ip : 159.30.23.19
		
		
		// 로그인 상태인 경우
		if(req.getSession().getAttribute("loginMember") != null) {
			
			String memberEmail = ((Member)req.getSession().getAttribute("loginMember"))
					.getMemberEmail();
			
			sb.append(String.format(", 요청회원 : %s", memberEmail));
			
		}
		
		log.info(sb.toString());
		
		
		
	}
	
	/**
	 * 접속자 IP 얻어오는 메서드
	 *
	 * @param request
	 * @return ip
	 */
	private String getRemoteAddr(HttpServletRequest request) {
		
		String ip = null;
		
		ip = request.getHeader("X-Forwarded-For");
		
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Real-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-RealIP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("REMOTE_ADDR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	//ProceedingJoinPoint
	// - JoinPoint 상속한 자식 객체
	// - @Around 에서 사용가능
	// proceed() 메서드 제공
	// - > proceed() 메서드 호출 전 / 후로
	//       Before/After 구분되어짐
	
	//* 주의할 점 *
	// 1) @Around 사용 시 반환형 Object 여야함
	// 2) @Around 메서드 종료 시 proceed() 반환값을 return 해야한다.
	
	
	@Around("PointcutBundle.serviceImplPointcut()")
	public Object aroundServiceImpl(ProceedingJoinPoint pjp) throws Throwable {
		// Throwable - 예외 처리의 최상위 클래스
		// Throwable 주요 자식으로 Exception(예외) / Error(오류)
		// Exception(예외) - 개발자가 처리할수 있는문제
		//                       error(오류) - 시스템 레벨의 심각한 문제
		
		//@Before
		// AOP가 적용된 클래스 이름 얻어오기
		String className = pjp.getTarget().getClass().getSimpleName();
		// ex) MainController , BoardController
		
		// 실행된 컨트롤러 메서드 이름을 얻어오기
		String methodName = pjp.getSignature().getName(); // ex) mainPage(), login..
		
		log.info("---------------------{}.{} 수행완료 ---------------------", className, methodName);
		
		//파라미터를 로그로 출력
		
		log.info("Parameter : {}", Arrays.toString(pjp.getArgs()));
		
		// 서비스 코드 실행 시 시간 기록
		long startMs = System.currentTimeMillis();
		
		Object obj = pjp.proceed(); // 전 / 후를 나누는 기준점
		
		// @After
		long endMs = System.currentTimeMillis();
		
		log.info("Running Time : {}ms" , endMs - startMs);
		
		log.info("==========================================================");
		
		
		return obj;
	}

	
	// ---------------------------------------------------------------------------------
	
	// 예외 발생 후 수행되는 코드
	
	//@AfterThrowing : 메서드가 예외를 던진 후에 실행되는 Advice를 정의
	@AfterThrowing(pointcut = "@annotation(org.springframework.transaction.annotation.Transactional)", 
			throwing = "ex")
	public void trasactionRollback(JoinPoint jp, Throwable ex) {
		log.info("***트랜잭션이 롤백됨{} ***", jp.getSignature().getName());
		log.error("[롤백원인] :{}", ex.getMessage());
	}
	
	
	
	//------------------------------------------------------------------------------------
	
	
	
}
