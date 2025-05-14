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
@Aspect
@Slf4j

// 클래스와 메서드, ip까지 얻어온다
public class LoggingAspect {
	@Before("PointcutBundle.controllerPointcut()")
	public void beforeController(JoinPoint jp) {
		// 클래스명 얻어오기
		String className  = jp.getTarget().getClass().getSimpleName();
		
		// 메서드명 얻어오기
		String methodName = jp.getSignature().getName();
		
		// 클라이언트 IP 얻어오기
		// 요청한 클라이언트의 HttpServletRequest 객체를 얻어온다.
		HttpServletRequest req =
				((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		
		String ip = getRemoteAddr(req);
		
		StringBuilder sb = new StringBuilder(); // 문자열의 변경사항이 많을 때 가변 문자열을 사용한다
		sb.append(String.format("[%s.%s] 요청 / ip:%s ", className , methodName , ip));
		
		// 로그인 상태인 경우
		if(req.getSession().getAttribute("loginMember")!=null) {
			String memberEmail = ((Member)  req.getSession().getAttribute("loginMember")).getMemberEmail();
			sb.append(String.format(", 요청회원: %s",memberEmail));
		}
		
		log.info(sb.toString()); // sb는 그대로 로그 찍을 수 없다
	}
	
	/**
	 * 접속자 IP 얻어오는 메서드
	 */
	private String getRemoteAddr(HttpServletRequest request) {
		String ip = null;
		ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) ip = request.getHeader("Proxy-Client-IP");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) ip = request.getHeader("WL-Proxy-Client-IP");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) ip = request.getHeader("HTTP_CLIENT_IP");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) ip = request.getHeader("X-Real-IP");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) ip = request.getHeader("X-RealIP");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) ip = request.getHeader("REMOTE_ADDR");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) ip = request.getRemoteAddr();
		return ip;
	}
	
	@Around("PointcutBundle.serviceImplPointCut()")
	public Object aroundServiceImpl(ProceedingJoinPoint pjp) throws Throwable {
		log.info("-----{}.{}", Arrays.toString(pjp.getArgs()));
		
		long startMs = System.currentTimeMillis();
		Object obj = pjp.proceed();
		long endMs = System.currentTimeMillis();
		
		log.info("Running Time: {}ms - {}ms", endMs - startMs);
		log.info("--------------------------------------------");
		
		return obj;
	}
	
	
	@AfterThrowing(pointcut = "@annotation(org.springframework.transaction.annotation.Transactional)"
			, throwing = "ex" )
	public void transactionRollback(JoinPoint jp, Throwable ex) {
		log.info("트랜잭션이 롤백 됨!",jp.getSignature().getName());
		log.error("롤백 원인: {}",ex.getMessage());
		
	}
	
	
}
