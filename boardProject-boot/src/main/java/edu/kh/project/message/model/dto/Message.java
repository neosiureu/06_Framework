package edu.kh.project.message.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 쪽지 DTO (날짜를 문자열로 받는 방식)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {

    private int messageNo;      // 쪽지 번호
    private int senderNo;       // 보낸 사람 회원 번호
    private int receiverNo;     // 받은 사람 회원 번호
    private String content;     // 쪽지 내용
    private String sendDate;    // 보낸 날짜 (YYYY-MM-DD 형태의 문자열)
    private String readFl;      // 읽음 여부 (Y/N)
    private String senderNickname;   // 보낸 사람 닉네임
    private String receiverNickname; // 받은 사람 닉네임
}