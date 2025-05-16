package edu.kh.project.chatting.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.project.chatting.model.dto.ChattingRoom;
import edu.kh.project.chatting.model.dto.Message;
import edu.kh.project.member.model.dto.Member;

@Mapper
public interface ChattingMapper {

	/** 채팅방 목록 조회
	 * @param memberNo
	 * @return roomList
	 */
	public List<ChattingRoom> selectRoomList(int memberNo);
	
	/** 채팅 상대 검색
	 * @param map
	 * @return memberList
	 */
	public List<Member> selectTarget(Map<String, Object> map);

    /** 채팅방번호 체크(기존에 있는지)
     * @param map
     * @return chattingNo
     */
    public int checkChattingRoomNo(Map<String, Integer> map);
    
    /** 새로운 채팅방 생성
     * @param map
     * @return chattingNo
     */
    public int createChattingRoom(Map<String, Integer> map);

    /** 메시지 조회
     * @param object
     * @return
     */
    public List<Message> selectMessageList(Object object);
 
    /** 읽음표시 업데이트
     * @param paramMap
     * @return
     */
    public int updateReadFlag(Map<String, Integer> paramMap);

    
    /** 채팅 입력
     * @param msg
     * @return
     */
    public int insertMessage(Message msg);

	
}
