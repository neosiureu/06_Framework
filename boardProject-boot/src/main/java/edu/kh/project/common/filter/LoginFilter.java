package edu.kh.project.common.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/*
 * Filter : 요청, 응답 시 걸러내거나 추가할 수 있는 객체
 * 
 * [필터 클래스 생성 방법]
 * 1. jakarta.servlet.Filter 인터페이스 상속 받기
 * 2. doFilter() 메서드 오버라이딩
 * 
 *
 * */
// 로그인이 되어있지 않은 경우 특정 페이지로 돌아가게함
public class LoginFilter implements Filter {
	
	//  필터 동작을 정의 하는 메서드
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		// ServletRequest : HttpServletRequest 의 부모타입
		// ServletResposns :HttpServletResposns 의 부모 타입
		
		// Session 필요함 -> 왜? -> loginMeber가 session 에 담김
		
		// Http 통신이 가능한 형태로 (자식 형태) 다운 캐스팅
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse resp =(HttpServletResponse)response;
		
		//****************** 게시판 부분 할 때 확인 !!! ********************************
		// 현재 요청의 URI를 가져옴
		String path = req.getRequestURI();
		
		//요청 URI 가 "/myPage/profile/" 로 시작하는지 확인
		// - > 게시판 등에서 로그인 안해도 프로필 이미지 요청을 보내는 곳이 있는데,
		// 그때는 필터링 하지 않도록 함
		if(path.startsWith("/myPage/profile/")) {
			//필터를 통과하도록함
			chain.doFilter(request, response);
			return; // 핕터 통과 후 아래 코드 수행하지 않도록 return
			
		}
		
		
		
		// *******************************************************************************
		
		// Session 객체 얻어오기
	    HttpSession session = req.getSession();
	    
	    // 세션에서 로그인한 회원 정보를 얻어옴
	    // 얻어왔으나, 없을때 (null) -> 로그인이 되어있지 않은 상태
	    
	    
	    if(session.getAttribute("loginMember") == null) {
	    	
	    	// /loginError 재요청
	    	// resp를 이용해서 원하는 곳으로 리다이렉튼
	    	resp.sendRedirect("/loginError");
	    	
	    	
	    }else {//로그인 되어있는 상태
	    	
	    	// FilterChain
	    	// - 다음 필터 또는 dispatcherServlet과 연결된 객체
	    	
	    	// 다음 필터로 요청/응답 객체 전달
	    	// 만약에 다음 필터가 없으면 DispatcherServlet로 요청/ 응답 객체 전달
	    	
	    	chain.doFilter(request, response);
	    	
	   
	    }

	    
}


}




































