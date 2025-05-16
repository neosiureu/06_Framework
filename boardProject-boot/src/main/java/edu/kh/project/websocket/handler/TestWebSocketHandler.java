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

// ???WebSocketHandler클래스 = 웹소켓 동작 시 수행할 구문을 작성하는 클래스




@Slf4j
// 로그 용
@Component
// 빈으로 등록
public class TestWebSocketHandler extends TextWebSocketHandler {
	// 뭘 extends하느냐에 따라 텍스트, 파일, 또는 뭘 주고받을지를 알 수 있다.
	
	private Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>())  ;
	/* 필드에 복잡한 것을 만들어야 한다. 
	
	 WebSocketSession: 클라이언트와 서버간의 양방향 통신을 담당하는 객체
	 attributes.put("session", session);을 통해  SessionHandshakeInterceptor가 가로챈 
	 연결한 클라이언트의 HttpSession을 가지고 있다.
	 이를 제네릭으로 가지는 Set을 생성한 것(중복허용X 순서X)
	
	*/
	
	
/*
  synchronizedSet = 동기화된 set => 이게 왜 필요?

여러 클라이언트가 서버에 요청을 보내면 일꾼들이 많음 
=> 실시간통신과 같은 경우 비동기식이라면 줄을 서지 않아 충돌 가능성 증가 
=> 잘못된 응답이 수신될 수 있음 => 동기화된 Set이 필요

여러가지 스레드가 동작하는 환경에서 하나의 컬렉션에 대해 여러 스레드가 접근하여 의도치 않은 문제가 발생되지 않게하기 위해
동기화를 진행하여 스레드가 순서대로 한 컬렉션에 접근할 수 있도록 변경한 것이다. 
(다만 처음에는 동기화된 빈 Set이다. WebSocketSession 으로 제한된다)

*/
	
	
	
	// 클라이언트와 서버가 연결이 완료되고, 통신할 준비가 완료되면 실행되는 메서드
	// attirubtes.put해둔 것 = sessions
	// 주머니에 넣는 행위를 하는 메서드
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {

		// 연결된 클라이언트의 WebSocketSession 정보를 Set에 추가한다 = 웹소켓에 연결된 클라이언트 정보를 주머니에 모아둔다.
		
		sessions.add(session);
		
	}
	
	
	// 클라이언트와의 연결이 끊기면, 종료되면 실행되는 메섣,
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		// 동기set 상태에서 뽑아온다.
		
		// 웹소켓 연결이 끊긴 클라이언트 정보를 Set에서 제거한다.
		sessions.remove(session); 		
	}
	
	
	// 클라이언트가 서버로 요청을 보냈을 때 텍스트 메시지를 보냈는데 서버가 응답할 때 쓰는 메인 메서드 (진짜 어떤 일을 하는지)
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		// TextMessage = 웹소캣으로 연결 된 클라이언트가 전달한 텍스트 (내용)이 담긴 객체다.
		log.info("전달받은 메시지: {}", message.getPayload() ); //TextMessage중에서 message 자체만 꺼내온다.
		// Payload = 통신 시 탑재된 데이터 그 자체
		
		// 전달받은 메시지 를 현재 해당 웹소켓에 연결되어있는 모든 클라이언트에게 보낼 것
		 
		/*
		 const obj = { 
		    "name": name,
		    "str": str
		  };
		*/
		
		
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


WebSocketHandler 인터페이스 내 주요 메소드
     
  void handlerMessage(WebSocketSession session, WebSocketMessage message)
  - 클라이언트로부터 메세지가 도착하면 실행
 
  void afterConnectionEstablished(WebSocketSession session)
  - 클라이언트와 연결이 완료되고, 통신할 준비가 되면 실행
  void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus)
  - 클라이언트와 연결이 종료되면 실행
  void handleTransportError(WebSocketSession session, Throwable exception)
  - 메세지 전송중 에러가 발생하면 실행
  
  
  
----------------------------------------------------------------

구현체에 대한 설명과 그에 해당하는 메서드

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
