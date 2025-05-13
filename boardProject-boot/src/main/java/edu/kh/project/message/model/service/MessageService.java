package edu.kh.project.message.model.service;

import java.util.List;
import java.util.Map;

import edu.kh.project.message.model.dto.Message;

public interface MessageService {

	int sendMessage(Message message);

	List<Message> getInboxMessages(int memberNo);

	Message getMessageDetail(Map<String, Object> paramMap);

	List<Message> getSentMessages(int memberNo);


	
	
	/** 쪽지 삭제
	 * @param messages 
	 * @return
	 */
	int deleteMessagePage(Message messages);

	
	
	
	/** 대화방 리스트
	 * @param memberNo
	 * @return
	 */
	List<Map<String, Object>> getConversationList(int memberNo);
	
	
	
	/** 대화방에서의 쪽지 모음을 상세조회
	 * @param paramMap
	 * @return
	 */
	List<Message> getMessageThread(Map<String, Object> paramMap);

}
