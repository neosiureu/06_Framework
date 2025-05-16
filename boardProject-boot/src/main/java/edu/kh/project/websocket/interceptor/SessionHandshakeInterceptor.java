package edu.kh.project.websocket.interceptor;

import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import jakarta.servlet.http.HttpSession;

/*
  SessionHandshakeInterceptor 클래스
 
  WebSocketHandler가 동작하기 전 후에 연결된 클라이언트 세션을 가로채는 동작을 작성할 클래스
  
  세션 = 클라이언트를 구분하기 위해 쓰인다.
  
 */



// Handshake = 클라이언트와 서버가 WebSocket 연결을 수립하기 위해 Http 프로토콜을 통해 수립하는 초기단계
// 기존 HTTP연결을 웹 소캣 연결로 변경한다.


@Component

public class SessionHandshakeInterceptor implements HandshakeInterceptor{

	// 핸들러 동작 전에 수행되는 메서드 
	
	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Map<String, Object> attributes) throws Exception {
		

		/* 
		ServerHttpRequest => HttpServletRequest의 부모 
		 ServerHttpResponse => HttpServletResponse의 부모
		존재이유? 세션을 가로채기 위함
		req객체에서 더 넓은 범위인 세션 객체를 뽑아올 수 있기 때문에 (다운캐스팅) 뽑아오는 셈
		*/
		
		// request가 참조하는 객체가 다운캐스팅이 가능하긴 한지 따진다. instanceof
		
		// ServerHttpRequest  => ServletServerHttpRequest =>  HttpServletRequest 이런식으로 다운캐스팅 됨
		
		if(request instanceof ServletServerHttpRequest) {
			ServletServerHttpRequest serveltRequest = (ServletServerHttpRequest) request; // 다운 캐스팅 수행
			
		
		// 웹소켓 동작을 요청했을 클라이언트의 세션을 얻어온 셈. 세션을 가로챈 셈
		HttpSession session = serveltRequest.getServletRequest().getSession();			
			                               // HttpServletRequest
		
		// 가로챈 세션을 핸들러에게 전달할 수 있게 키와 값의 형태로 세팅
		attributes.put("session", session);
		// 세션 객체가 일단 attributes에 들어가서 핸들러에게 전달된 셈
		
		}
		
		
		// 가로채기의 진행 여부를 지정
		return true;
		// true로 작성해야 세션을 가로채서 Handler에게 값을 전달 가능
		
	}

	
	
	
	// 핸들러 동작 후에 수행되는 메서드 => 할거 없음
	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception exception) {}
	
	
	
	
	
}