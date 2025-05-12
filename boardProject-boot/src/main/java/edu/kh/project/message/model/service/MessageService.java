package edu.kh.project.message.model.service;

import java.util.List;
import java.util.Map;

import edu.kh.project.message.model.dto.Message;

public interface MessageService {

	int sendMessage(Message message);

	List<Message> getInboxMessages(int memberNo);

	Message getMessageDetail(Map<String, Object> paramMap);

	List<Message> getSentMessages(int memberNo);

	List<Map<String, Object>> getConversationList(int memberNo);


}
