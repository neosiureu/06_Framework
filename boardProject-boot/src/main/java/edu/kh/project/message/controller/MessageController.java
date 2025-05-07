package edu.kh.project.message.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.member.model.service.MemberService;
import edu.kh.project.message.model.dto.Message;
import edu.kh.project.message.model.service.MessageService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MemberService memberService; // 받는 사람 조회용

    @Autowired
    private MessageService messageService; // 쪽지 처리용

    /** 쪽지 작성 폼으로 이동
     *  URL: /message/send/{memberNo}
     */
    @GetMapping("/send/{memberNo}")
    public String sendMessagePage(@PathVariable("memberNo") int memberNo,
                                   Model model) {

        // 받는 사람 정보 조회
        Member recipient = memberService.selectMemberByNo(memberNo);

        if (recipient == null) {
            model.addAttribute("errorMessage", "존재하지 않는 회원입니다.");
            return "common/error";
        }

        model.addAttribute("recipient", recipient);
        return "message/send"; // templates/message/send.html
    }

    /** 쪽지 전송 처리 (POST)
     *  URL: /message/send
     */
    @PostMapping("/send")
    public String sendMessage(@ModelAttribute Message message,
                              HttpSession session,
                              Model model) {

        // 로그인 회원
        Member loginMember = (Member) session.getAttribute("loginMember");

        if (loginMember == null) {
            model.addAttribute("errorMessage", "로그인이 필요합니다.");
            return "common/error";
        }

        // 보내는 사람 번호 설정
        message.setSenderNo(loginMember.getMemberNo());

        // 쪽지 저장
        int result = messageService.sendMessage(message);

        if (result > 0) {
            return "redirect:/"; // 성공 시 홈 또는 쪽지함으로 이동
        } else {
            model.addAttribute("errorMessage", "쪽지 전송 실패");
            return "common/error";
        }
    }
    
    @GetMapping("/inbox")
    public String viewInbox(HttpSession session, Model model) {

        // 로그인 여부 확인
        Member loginMember = (Member) session.getAttribute("loginMember");

        if (loginMember == null) {
            model.addAttribute("errorMessage", "로그인이 필요합니다.");
            return "common/error";
        }

        // 받은 쪽지 목록 조회
        List<Message> messageList = messageService.getInboxMessages(loginMember.getMemberNo());

        model.addAttribute("messageList", messageList);
        return "message/inbox"; // templates/message/inbox.html
    }
    
    /** 쪽지 상세 조회
     *  URL: /message/detail/{messageNo}
     */
    @GetMapping("/detail/{messageNo:[0-9]+}")
    public String viewMessageDetail(@PathVariable("messageNo") int messageNo,
                                     HttpSession session,
                                     Model model) {

        Member loginMember = (Member) session.getAttribute("loginMember");

        if (loginMember == null) {
            model.addAttribute("errorMessage", "로그인이 필요합니다.");
            return "common/error";
        }

        // Map으로 파라미터 묶기
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("messageNo", messageNo);
        paramMap.put("memberNo", loginMember.getMemberNo());

        Message message = messageService.getMessageDetail(paramMap);

        if (message == null) {
            model.addAttribute("errorMessage", "쪽지를 찾을 수 없습니다.");
            return "common/error";
        }

        model.addAttribute("message", message);
        return "message/detail";
    
    }
    
    
    /** 보낸 쪽지함 이동 */
    @GetMapping("/outbox")
    public String outbox(HttpSession session, Model model) {

        Member loginMember = (Member) session.getAttribute("loginMember");

        if (loginMember == null) {
            model.addAttribute("errorMessage", "로그인이 필요합니다.");
            return "common/error";
        }

        List<Message> sentList = messageService.getSentMessages(loginMember.getMemberNo());
        model.addAttribute("sentList", sentList);

        return "message/outbox"; // templates/message/outbox.html
    }
}
