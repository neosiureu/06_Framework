package edu.kh.project.websocket.handler;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.kh.project.chatting.model.dto.Messages;
import edu.kh.project.chatting.model.service.ChattingService;
import edu.kh.project.member.model.dto.Member;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;



@Slf4j
@Component
@RequiredArgsConstructor
public class ChattingWebsocketHandler extends TextWebSocketHandler{
	private Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());
	
	
	private final ChattingService service;
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		sessions.add(session);
		log.info("{} 님과 연결됨", session.getId());
	}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		sessions.remove(session);
		log.info("{} 님과의 연결이 끊김", session.getId());
		
	}
	
	
	
	// 클라이언트로부터 텍스트 메시지를 받았을 때 실행됨
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		// message- JS를 통해 클라이언트로부터 전달받은 내용이 들어감
		// { } 
		// var obj로 선언한 데이터가 네가지 JSON형태로 변환하여 이 위치에 들어와 있음
		
		
		/*
		  var obj = {
			"senderNo": loginMemberNo,
			"targetNo": selectTargetNo,
			"chattingRoomNo": selectChattingNo,
			"messageContent": inputChatting.value,};
		*/
	
		
		// 자바에서의 객체 형태로 JSON을 가져올 수 없을까?
		// Jackson이라는 모듈이 있어 JSON과 관련된 객체를 다뤄준다. ObjectMapper를 선언
		
		
		ObjectMapper objectMapper = new ObjectMapper();
		// Messages라는 DTO => DB에 만든 Messages.dto를 사용
		// json => DTO 그 사이에 ObjectMapper를 이용하겠다는 것
		
		Messages msg = 	objectMapper.readValue(message.getPayload(), Messages.class); 
		// 첫 인자는 읽고싶은 내용 + 두번째 인자는 어떤 DTO에 담고싶은지 여부
		

		// 클라이언트로부터 얻은 메시지를  db에 저장하기 위한 로직
		// db에 삽입하는 서비스 호출
		
		int result = service.insertMessage(msg);
		
		
		if(result>0) {
			SimpleDateFormat sdf  = new SimpleDateFormat("yyyy.MM.dd hh:mm");
			msg.setSendTime(sdf.format(new Date() ));
			
			// 필드에 있는 sesssions에는 접속중인 모든 회원의 세션 정보가 담겨있음
			
			for(WebSocketSession  s : sessions) {
				
				// 로그인된 회원의 정보 중 회원의 정보가 필요하다.
				// 필요한 이유? 조건문을 써야하기 때문. 메시지는 나 혹은 내가 보낸 상대인 target에게만 가야 한다
				// 로그인상태 회원 중 targetNo 또는 senderNo에게 일치하는 회원에게 메시지를 전달하겠다는 코드가 필요
				// 따라서 로그인 상태인 회원을 항상 가져와야 함
				
				
				// s가 바로 인터셉터를 이용해 빼앗은 세션 정보
				// 그러나 WebSocketSession 이 아닌 HttpSession
				
				
				// 가로챈 session 꺼내기
				HttpSession temp = (HttpSession) s.getAttributes().get("session");
				
				// 로그인된 로그인멤버 넘버 꺼내기
				int loginMemberNo  = ((Member) temp.getAttribute("loginMember")).getMemberNo();
				
				
				if(loginMemberNo == msg.getTargetNo() || loginMemberNo ==msg.getSenderNo()) {
					
					// 다시 서버가 가진 MESSAGES DTO인 msg 변수에 대해 JSON으로 변환하여 클라이언트에게 제공
					// 클라이언트는 JS이기 때문에 다시 보내기 위해서는 반드시 JSON으로 변환해야 한다. 
					// MESSAGES DTO를 JS가 이해할 수 없기 때문
					
					String jsonData = objectMapper.writeValueAsString(msg);
					// 자바에서만 이해가능한 MESSAGES 타입을 json으로 바꾼다
					
					s.sendMessage(new TextMessage(jsonData));
					
				}
			}
		}
	}
	
}