package edu.kh.project.message.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import edu.kh.project.message.model.dto.Message;

@Mapper
public interface MessageMapper {

	int insertMessage(Message message);

	List<Message> selectInboxMessages(int memberNo);

	void updateReadFlag(Map<String, Object> paramMap);

	Message selectMessageDetail(Map<String, Object> paramMap);

	List<Message> selectSentMessages(int memberNo);
	
	
	/** 메시지 삭제 쿼리
	 * @return
	 */
	int deleteMessagePage(Message messages);

	

	List<Map<String, Object>> getConversationList(@Param("memberNo") int memberNo);

	List<Message> selectMessageThread(Map<String, Object> paramMap);


}
