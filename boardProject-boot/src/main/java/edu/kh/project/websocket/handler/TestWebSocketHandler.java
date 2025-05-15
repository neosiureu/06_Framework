package edu.kh.project.websocket.handler;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import lombok.extern.slf4j.Slf4j;

/** OOOWebSocketHandler 클래스
 * 
 * 웹소켓 동작 시 수행할 구문을 작성하는 클래스 
 * 
 */

@Slf4j
@Component // bean 등록
public class TestWebSocketHandler extends TextWebSocketHandler{
	
	// WebSocketSession : 
	// 클라이언트 - 서버 간 전이중 통신을 담당하는 객체
	// SessionHandshakeInterceptor가 가로챈
	// 연결한 클라이언트의 HttpSession 을 가지고 있음
	//  (attributes에 추가한 값)
	
	// 동기화 된 Set 생성
	private Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());
	// -> 여러가지 스레드가 동작하는 환경에서 하나의 컬렉션에 여러 스레드가 접근하여 의도치 않은 문제가 발생되지 않게
	//    하기 위해 동기화를 진행하여 스레드가 순서대로 한 컬렉션에 접근할 수 있도록 변경.
	
	
	// 클라이언트와 연결이 완료되고, 통신할 준비가 되면 실행
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		
		// 연결된 클라이언트의 WebSocketSeesion 정보를 Set에 추가
		// -> 웹소켓에 연결된 클라이언트 정보를 모아둠
		sessions.add(session);
		
	}
	
	// 클라이언트와 연결이 종료되면 실행
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		
		// 웹소켓 연결이 끊긴 클라이언트 정보를 Set에서 제거
		sessions.remove(session);
		
	}
	
	
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		
		// TextMessage : 웹소켓으로 연결된 클라이언트가 전달한
		//				텍스트(내용)가 담겨있는 객체
		
		// message.getPayload() : 통신 시 탑재된 데이터 자체
		log.info("전달받은 메시지 : {}", message.getPayload());
		
		// 전달 받은 메시지를
		// 현재 해당 웹소켓에 연결된 모든 클라이언트에게 보내기
		for(WebSocketSession s : sessions) {
			s.sendMessage(message);
		}
		
	}
	
	
	
}


/*
WebSocketHandler 인터페이스 : 
	웹소켓을 위한 메소드를 지원하는 인터페이스
   -> WebSocketHandler 인터페이스를 상속받은 클래스를 이용해
     웹소켓 기능을 구현



WebSocketHandler 주요 메소드
       
   void handlerMessage(WebSocketSession session, WebSocketMessage message)
   - 클라이언트로부터 메세지가 도착하면 실행
   
   void afterConnectionEstablished(WebSocketSession session)
   - 클라이언트와 연결이 완료되고, 통신할 준비가 되면 실행

   void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus)
   - 클라이언트와 연결이 종료되면 실행

   void handleTransportError(WebSocketSession session, Throwable exception)
   - 메세지 전송중 에러가 발생하면 실행 


----------------------------------------------------------------

TextWebSocketHandler :  
	WebSocketHandler 인터페이스를 상속받아 구현한 
	텍스트 메세지 전용 웹소켓 핸들러 클래스

   handlerTextMessage(WebSocketSession session, TextMessage message)
   - 클라이언트로부터 텍스트 메세지를 받았을때 실행
    

BinaryWebSocketHandler:
	WebSocketHandler 인터페이스를 상속받아 구현한
	이진 데이터 메시지를 처리하는 데 사용. 
	주로 바이너리 데이터(예: 이미지, 파일)를 주고받을 때 사용.
*/