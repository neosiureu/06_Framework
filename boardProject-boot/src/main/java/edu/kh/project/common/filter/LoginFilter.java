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

필터: 요청과 응답 시 무언가를 걸러내거나 추가할 수 있는 객체
필터 클래스로서의 역할을 맞게 하는 방법 

1) jakarta.servlet.Filter 인터페이스 상속 받음 (import jakarta.servlet.Filter;)

2) doFilter()메서드를 오버라이딩
 
*/


// 로그인이 되어있지 않은 경우 메인 페이지로 돌아가게 함

public class LoginFilter implements Filter {

	// 필터의 동작을 정의하는 메서드
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		
		// ServletRequest은 HttpServletRequest의 부모 타입 객체
		// ServletResponse는 HttpServletResponse의 부모 타입 객체
		
		// Session 필요함 => loginMember라는 변수가 세션 스코프 객체로 존재하기 때문
		// HttpServletRequest로부터 자식 객체 형태로 다운 캐스팅하여 사용하겠다
		
		// HTTP 통신이 가능한 자식의 형태로 다운캐스팅하여 사용한다
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		
		
		
		// **********************  게시판 사용 시 이 파일을 다시 볼 것임 ********************** 
		
		
		// 세션객체 얻어오기
		HttpSession session = req.getSession();
		
		// 세션에서 로그인한 회원의 정보를 얻어왔는데 null일 때는 로그인이 안 되어있음
		// 따라서 메인 페이지로 돌려보내야 함
		
		if(session.getAttribute("loginMember")==null) {
			// 로그인 안 돼있을 때 => /loginError라는 주소로 재요청
			// 즉 redirect 하자 => 서블릿에서는 resp.sendRedirect("/loginError")
			resp.sendRedirect("/loginError"); // 이 컨트롤러를 만들어야 함
		}
		
		else {
			// 로그인 된 상태
			
			// 다음 필터 또는 DispatcherServlet 과 연결된 객체 
			// (1을 통과하면 DispatcherServlet이 나오므로)
			// 필터는 다중으로 줄세울 수도 있음. 지금은 아님
			
			// 다음 필터가 없으니 DispatcherServlet로 요청 객체와 응답 객체를 전달한다
			
			chain.doFilter(request, response); 
			
			
		}
		

		// 다만 필터용 설정 클래스 FilterConfig에서 해줌
		
	}
	
	
	

}
