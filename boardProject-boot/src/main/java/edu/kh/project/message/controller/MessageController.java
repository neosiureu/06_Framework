package edu.kh.project.message.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.member.model.service.MemberService;
import edu.kh.project.message.model.dto.Message;
import edu.kh.project.message.model.service.MessageService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
                                   Model model, @RequestParam(value = "boardNo", required = false) Integer boardNo,
                                   @RequestParam(value = "boardCode", defaultValue = "1") int boardCode, // 게시판 코드 (기본값 1)
                                   @RequestParam(value = "cp", defaultValue = "1") int cp                       
    		) {

        // 받는 사람 정보 조회
        Member recipient = memberService.selectMemberByNo(memberNo);

        if (recipient == null) {
            model.addAttribute("errorMessage", "존재하지 않는 회원입니다.");
            return "redirect:/";
        }

        model.addAttribute("recipient", recipient);
        model.addAttribute("receiverNo", memberNo); // 받는 사람 회원 번호도 모델에 담아 폼 hidden 필드에 사용
        model.addAttribute("boardNo", boardNo); // 받은 boardNo 값을 모델에 담아 View로 전달 (hidden 필드에 사용)
        model.addAttribute("boardCode", boardCode);
        model.addAttribute("cp", cp);
        return "message/send"; // templates/message/send.html
    }

    /** 쪽지 전송 처리 (POST)
     *  URL: /message/send
     */
    @PostMapping("/send")
    public String sendMessage(@ModelAttribute Message message,
                              HttpSession session,
                              Model model, RedirectAttributes ra, @RequestParam(value = "boardCode", defaultValue = "1") int boardCode,
                              @RequestParam(value = "cp", defaultValue = "1") int cp) {

        // 로그인 회원
        Member loginMember = (Member) session.getAttribute("loginMember");

        if (loginMember == null) {
            model.addAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/member/login"; // 예: 로그인 페이지로 리다이렉트

        }

        // 보내는 사람 번호 설정
        message.setSenderNo(loginMember.getMemberNo());

        // 쪽지 저장
        int result = messageService.sendMessage(message);

        if (result > 0) {
        	return "redirect:/board/" + boardCode + "?cp=" + cp;
        } else {
//            model.addAttribute("errorMessage", "쪽지 전송 실패");
        	ra.addFlashAttribute("message", "쪽지 전송 실패.");
        	// 간단하게 받는 사람 번호와 boardNo를 URL에 다시 붙여서 sendForm으로 리다이렉트
            String redirectPath = "redirect:/message/send/" + message.getReceiverNo();
            if (message.getBoardNo() != 0) {
                redirectPath += "?boardNo=" + message.getBoardNo();
            }
            redirectPath += "&boardCode=" + boardCode + "&cp=" + cp;
        	return redirectPath;
        }
    }
    
    @GetMapping("/inbox")
    public String viewInbox(HttpSession session, Model model) {

        // 로그인 여부 확인
        Member loginMember = (Member) session.getAttribute("loginMember");

        if (loginMember == null) {
            model.addAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/member/login";
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
           return "redirect:/member/login";
        }

        // Map으로 파라미터 묶기
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("messageNo", messageNo);
        paramMap.put("memberNo", loginMember.getMemberNo());

        Message message = messageService.getMessageDetail(paramMap);

        if (message == null) {
            model.addAttribute("errorMessage", "쪽지를 찾을 수 없습니다.");
            return "redirect:/message/inbox";
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
            return "redirect:/member/login";
        }

        List<Message> sentList = messageService.getSentMessages(loginMember.getMemberNo());
        model.addAttribute("sentList", sentList);

        return "message/outbox"; // templates/message/outbox.html
    }
    
    
    
    
    
    
    /**대화방 목록 조회
     * URL: /message/conversations
     * 로그인 필요. 로그인 안 되어 있으면 로그인 페이지로 리다이렉트
     * @param session
     * @param model
     * @param ra
     * @return
     */
    @GetMapping("/conversations")
    public String getConversationList(
            HttpSession session, // 세션에서 로그인 회원 정보 얻어옴
            Model model, // View로 데이터 전달
            RedirectAttributes ra // 리다이렉트 시 메시지 전달
        ) {

        // 1. 로그인 여부 확인
        Member loginMember = (Member) session.getAttribute("loginMember");

        if (loginMember == null) {
            // 로그인 안 되어 있으면 로그인 페이지로 리다이렉트와 RedirectAttributes 사용
            ra.addFlashAttribute("message", "로그인이 필요합니다.");
            return "redirect:/member/login";
        }

        // 2. 로그인 회원 번호로 대화방 목록 조회 서비스 호출 (새로운 서비스 메소드)
        // 이 메소드는 '보낸사람-받는사람-게시글' 조합별 고유한 대화방 목록 요약 정보를 반환해야 합니다.
        // ConversationSummary DTO를 사용하거나 List<Map<String, Object>> 형태로 데이터를 받아 처리할 수 있습니다.
        // 여기서는 List<Map<String, Object>> 형태로 받는다고 가정하고 View에 그대로 전달합니다.
        List<Map<String, Object>> conversationList = messageService.getConversationList(loginMember.getMemberNo());
        // 만약 ConversationSummary DTO를 사용한다면:
        // List<ConversationSummary> conversationList = messageService.getConversationList(loginMember.getMemberNo());


        log.info("대화방 목록 조회 결과 (Service로부터 받은 데이터)");
        if (conversationList != null && !conversationList.isEmpty()) {
            log.debug("--- conversationList Map Keys Check ---");
            // 리스트의 첫 번째 몇 개 Map의 키 목록과 값 확인
            for (int i = 0; i < Math.min(conversationList.size(), 5); i++) { // 최대 5개 Map 확인
                Map<String, Object> conversationMap = conversationList.get(i);
                log.debug("Map {} keys: {}", i, conversationMap.keySet()); // <<< 이 줄의 출력을 확인하세요! Map이 가진 모든 키 이름이 출력됩니다.
                // "otherUserNickname" 키가 있는지, 있다면 값은 무엇인지 확인
                if (conversationMap.containsKey("otherUserNickname")) {
                    log.debug("Map {} otherUserNickname value: {}", i, conversationMap.get("otherUserNickname"));
                } else {
                    log.debug("Map {} DOES NOT contain key 'otherUserNickname'", i);
                }
                // 다른 키들도 확인 가능:
                // log.debug("Map {} boardNo value: {}", i, conversationMap.get("boardNo"));
                // log.debug("Map {} lastMessageContent value: {}", i, conversationMap.get("lastMessageContent"));
                // log.debug("Map {} notReadCount value: {}", i, conversationMap.get("notReadCount"));
            }
            log.debug("------------------------------------");
        } else {
            log.info("conversationList is null or empty. No maps to check.");
        }
        // ===================================================================
        
        
        // 3. 조회된 대화방 목록을 모델에 담아 View로 전달 (conversations.html에서 사용)
        model.addAttribute("conversationList", conversationList);

        // 4. 대화방 목록 View 이름 반환
        return "message/conversations"; // templates/message/conversations.html
    }
    
    
}
