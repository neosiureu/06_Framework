package edu.kh.project.common.interceptor;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import edu.kh.project.board.model.service.BoardService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/*
 
 요청, 응답, 뷰 완성 후 가로채는 객체 (Spring 스펙에서 지원)

 HandlerIntercpetor 인터페이스를 상속받아 해당 클래스를 구현
 
 
 preHandle: 전처리 (DispatcherServlet이 요청 객체를 받아서 컨트롤러로 전달하는 그 사이에 3번에서)
 
 postHandle: 후처리 (컨트롤러에서 DispatcherServlet으로 돌려줄 때 5번에서 동작)
 
 afterCompletion: 완성 후 = 뷰를 완성 후 forward의 코드를 다 해석한 다음 => view resolver가 응답에 맞춰 DispatcherServlet에 돌려줄 때 (6번에서 위로 갈 때)
 
 
 
 */



@Slf4j
public class BoardTypeInterceptor implements HandlerInterceptor {
	// 요청이 컨트롤러로 가기 전에 보드에 세팅한다
	
	@Autowired
	private BoardService service;
	
	
	// 전처리: 컨트롤러로 쪽을 요청이 들어오기 전에 실행되는 메서드 => 이 때 boardTypeList를 DB로부터 얻어온다
	// 꼭 3번에서 해야하는 이유: 
	// 처음으로 루트 주소로 요청받았을 때 부터 먼저 DB로 가서 내용을 가지고 와야 함
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
	/*	
	 
	 page < request < session < application
	 application scope는 서버 시작 시부터 서버 종료시까지 유지되는 서블릿 내장 객체
	 서버 내에 오직 하나만 존재하여 모든 클라이언트가 공용으로 사용한다	
	
	
	
	얻어오는 방법?
	request로부터 얻어옴 
	*/
	
	ServletContext application = request.getServletContext();
	
	
	// application scope에 "boardTyleList"가 없을 경우 => DB에 이 때만 가서 가져온다
	
	if(application.getAttribute("boardTypeList")==null) {
		// DB로 향하려면 컨트롤러는 아니지만 여기서 서비스 자체를 호출한다.
		// 컨트롤러로 가는 요청을 가로챘는데 컨트롤러로 가면 이상함
		
		List<Map<String, Object> > boardTypeList = service.selectBoardTypeList();
		// DTO를 안 만들기 위해 Map을 쓰겠다
		
		
		// applicationScope에 이 조회 결과를 추가하여 항상 사용할 수 있게 함
		
		application.setAttribute("boardTypeList", boardTypeList);
		
		log.debug("boradTypeList의 내용:" + boardTypeList);
		
	}
	
	// 있으면 안가도 된다
	

		
		
		return HandlerInterceptor.super.preHandle(request, response, handler); 	// true를 뱉어 줌
	}
	
	
	
	
	// 후처리: 요청이 처리된 후, 뷰가 랜더링 되기 전에 실행되는 메서드 => 즉 응답을 가지고 DispatcherServlet에게 돌려보내기 전에 수행된다.
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}
	
	
	// 뷰 랜더링이 다 끝난 후에 실행되는 메서드 
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}
	
	
}
