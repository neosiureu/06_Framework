package edu.kh.project.message.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.message.model.dto.Message;
import edu.kh.project.message.model.mapper.MessageMapper;
import lombok.extern.slf4j.Slf4j;

@Transactional(rollbackFor = Exception.class)
@Service
@Slf4j
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Override
    public int sendMessage(Message message) {
        return messageMapper.insertMessage(message);
    }

    @Override
    public List<Message> getInboxMessages(int memberNo) {
        return messageMapper.selectInboxMessages(memberNo);
    }

    @Override
    public Message getMessageDetail(Map<String, Object> paramMap) {
        messageMapper.updateReadFlag(paramMap);
        return messageMapper.selectMessageDetail(paramMap);
    }

    @Override
    public List<Message> getSentMessages(int memberNo) {
        return messageMapper.selectSentMessages(memberNo);
    }

	@Override
	public List<Map<String, Object>> getConversationList(int memberNo) {
		// TODO Auto-generated method stub
		return messageMapper.getConversationList(memberNo);
	}

    
}