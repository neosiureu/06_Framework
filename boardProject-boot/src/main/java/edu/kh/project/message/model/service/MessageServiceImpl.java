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

	public List<Message> getMessageThread(Map<String, Object> paramMap) { // Controller로부터 Map<String, Object> 받음
		log.info("getMessageThread ServiceImpl 실행 - 파라미터: {}", paramMap);
        // paramMap 안에는 {"loggedInUserNo": 로그인회원번호, "otherUserNo": 상대방회원번호, "boardNo": 게시글번호} 형태의 데이터가 담겨 있을 것입니다.

		// Mapper의 메시지 쓰레드 조회 메소드 호출
        // 이 메소드의 SQL은 주어진 두 사용자 및 boardNo에 해당하는 모든 메시지를 조회해야 합니다.
        // Service에서 받은 paramMap을 그대로 Mapper로 전달합니다.
		return messageMapper.selectMessageThread(paramMap); // Assuming messageMapper has a method named selectMessageThread
	}



	@Override
	public int deleteMessagePage(Message messages) {
		// TODO Auto-generated method stub
		return messageMapper.deleteMessagePage(messages);

	}

    
}