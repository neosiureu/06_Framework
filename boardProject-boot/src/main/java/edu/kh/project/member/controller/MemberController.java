package edu.kh.project.member.controller;

import java.io.Console;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.member.model.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@SessionAttributes({"loginMember"})
@Controller
@RequestMapping("member")
@Slf4j
public class MemberController {
   
   @Autowired
   private MemberService service;
   
   /*
    * [로그인]
    * 정의: 특정 사이트에 아이디/ 비밀번호 등을 입력하여 해당 정보가 DB에 있으면 조회하거나 서비스를 이용가능하게 함
    * 로그인한 회원의 정보는 session객체에 기록하여 
    * 로그아웃 (세션 만료) 또는 브라우저 종료 시 까지 해당 정보를 계속 이용할 수 있게 해야 한다 
    * */
   
   
   
   
   /** login method: command 객체 
    * (@ModelAttribute로 불러온 것이라 => memberEmail, memberPw가 이미 세팅된 상태)
    * @param inputMember 
    * @param ra: 리다이렉트 시 일시적으로 세션 스코프로 바꾸지만 리다이렉트가 끝나면 사라지는 정보
    * @param model: 데이터 전달용 객체로 기본적으로 request 범위의 객체이지만 
    * SessionAttribute라는 어노테이션과 함께 사용 시 session scope에 객체를 담을 수 있다.
    * @return 
    */
   
   @PostMapping("login")
   // 동기식 요청
   public String login(@ModelAttribute Member inputMember, RedirectAttributes ra, Model model, 
         @RequestParam(value = "saveId", required = false)  String saveId,
         HttpServletResponse resp) {
      
      // 체크박스는 체크가 된 경우 on, 안 된 경우에는 null로 넘어온다.
      
      
      // 로그인 서비스 호출
      Member loginMember = service.login(inputMember);
      
      
      
      // 이메일 창과 비밀번호 창을 trim해서 0일 때 alert창으로 띄워주는 것
      
      
      
      // 로그인 실패시
      
      if(loginMember ==null) {
         ra.addFlashAttribute("message", "아이디 또는 비밀번호가 일치하지 않습니다");
      }
      
   
      // 위 조건을 통과한 성공 시 
      
      else {
         // 원래 model.이면 request이지만 
         model.addAttribute("loginMember",loginMember);
         // 이름을 html에서 이렇게 해놨기 때문에 
         //  <th:block th:if="${session.loginMember==null}">

         // 여기까지가 1단계로 model을 이용하여 request scope에 세팅한 것이다.
         
         
         // 2단계: 클래스 위에 @SessionAttributes() 어노테이션을 사용하여 loginMember를
         // 리퀘스트에서 세션 스코프로 바꿈
         

         
         
         
         
         //--------------쿠키를 기억하는 방법--------------//
         
         
         // 로그인 시 작성한 이메일을 쿠키에 저장
         // 서버가 쿠키를 생성하고 브라우저로 보내 둘 다 보낼 수 있다
         // 따라서 서버에서 쿠키를 만든다. 정확히는 쿠키 객체를 생성한다
         
         // 1단계: key: value꼴로 쿠키를 객체로 생성한다.
         Cookie cookie = new Cookie("saveId", loginMember.getMemberEmail());
         // saveId = user01@kh.or.kr
   
         
         // 2단계: 클라이언트가 어떤 요청을 할 때 쿠키를 첨부할지 지정 (path) 
         // => 쿠키가 적용될 경로 설정
         
         // 클라이언트는 쿠키를 가지고 있다가 저장하고 있던 쿠키를 보내는데
         // 서버 입장에서 언제 클라이언트로부터 쿠키를 받을지 여기서 지정하겠다는 것
         
         // ex) "/"라고 지정하면 IP또는 도메인 또는 localhost라는 뜻으로
         // 메인페이지부터 그 하위 주소 모두 쿠키를 받아주겠다는 뜻
         
         cookie.setPath("/");
         
         // 3단계: 쿠키의 만료 기간 지정
         
         if(saveId !=null) {
            // 체크박스를 체크하면 on으로 넘어옴. 즉 아이디를 저장하려는 의도를 보일 때
            // 초 단위로 얼마나 기억할지 체크한다.
            
            cookie.setMaxAge(60*60*24*30); //30일을 초단위로 계산하여 넣는다
            
            
         }
         
         else {
            // 체크를 한번 했지만 다음에는 안하는것
            cookie.setMaxAge(0); // 0초만 저장하겠다 => 클라이언트측의 쿠키를 바로 삭제
         }
         
         // 4단계: resp라는 인자를 알아서 추가하여 cookie를 보낸다.
         resp.addCookie(cookie);
         
         
         
         
         
         
         
      }
      
      
      
      return "redirect:/"; //메인페이지 재요청
   }
   
   
   /** 로그아웃: 로그인된 사람의 정보는 세션에 실려있는데 
    * 해당 세션의 정보를 만료시킨다. (없앤다)
    * @param SessionStatus: @SessionAttribute로 지정된 특정 속성을 세션에서 제거하는 기능
    * @return
    */
   @GetMapping("logout")
   public String logout(SessionStatus status) {
      // 세션을 만료시킨다.
      
      status.setComplete(); // 세션을 완료시킴. (== @SessionAttribute로 등록된 세션을 제거한다)
      
      return "redirect:/";
      
   }
   
   
   
   
   /** 회원가입 페이지로 이동
    * @param status
    * @return
    */
   @GetMapping("signup")
   public String sigupPage() {
      // 회원가입 페이지로 이동하는 요청을 받
      
      return "member/signup";
   }
   
   
   /** 이메일 중복검사 => 비동기요청 fetch로
    * @return inputEmail이랑 일치하는 이메일의 개수를 세서 반환한다.
    */
   @GetMapping("checkEmail") // get으로 온 /member/checkEmail과 연결해주세요.
   @ResponseBody
   public int checkEmail(@RequestParam("memberEmail") String memberEmail) {
      
      return service.checkEmail(memberEmail);
   }
   
   
   
   
   /** 닉네임 중복 검사
    * @param memberNickname
    * @return
    */
   @ResponseBody
   @GetMapping("checkNickname")
   public int checkNickname(@RequestParam("memberNickname") String memberNickname) {
      
      return service.checkNickname(memberNickname);
   }

   
   /** 회원가입 
    * @param inputMember는 입력된 회원 정보가 세팅된 객체 (memberEmail, memberPw, 
    * memberNickname, memberTel, memberAddress[])
    * memberAddress => 입력한 주소 input 3개의 값을 배열로 전달한다 [우편번호/ 도로명 및 지번주소 / 상세주소]
    * @param ra : 리다이렉트 시 잠깐 session 스코프로 범위가 변했다가 다시 request로
    * @param memberAddres
    * @return 
    */
   
   @PostMapping("signup")
   public String signUp(Member inputMember , RedirectAttributes ra, 
         @RequestParam("memberAddress") String [] memberAddres ) {
   
      log.debug("inputMember:" + inputMember);

      log.debug("memberAddress:" + memberAddres.toString());
      
      // 이  memberAddres.toString()를 각각 꺼내와서 
      
            
   // 우편번호, 도로명주소, 상세주소 => memberAddress로 들어옴
   // 도로명주소는 콤마가 포함되기에 문자열로 받을 수 없음 (콤마 기준으로 문자열이 나눠지기에)
   //memberAddress=01486,노해로 쌍문역 시티프라디움,101동 708호, 이렇게 나옴
   // 따라서 콤마 말고 이상한 특수문자를 넣어 문자열 자체를 가공해야 한다
      
   /*
    private int memberNo; //회원번호
   private String memberEmail; // 회원 이메일
   private String memberPw; // 회원 비밀번호
   private String memberNickname; //회원 닉네임
   private String memberTel; // 회원 전화번호
   private String memberAddress; // 주소
   */
      
      // 회원가입 서비스
      int result = service.signup(inputMember, memberAddres);
      
      String path = null;
      String message = null;
      
      if(result>0) {
         message = inputMember.getMemberNickname() +"님의 가입을 환영합니다";
         path="/";
      }
      
      else {
         message = "가입에 실패하셨습니다.";
         path="signup";
      }
      
      ra.addFlashAttribute("message",message);
      
      
      return "redirect:"+path ;
      // 특히 실패했을 때 redirect:signup => 
      // 상대경로 주소로서 
      // signup으로 get방식 요청을 보내게 된다 (회원가입페이지로 이동이라는 메서드로)
   }
   
   
   @GetMapping("selectID")
   public String selectID(@ModelAttribute Member member
         , RedirectAttributes ra
         ) {
      
       log.debug("닉네임: " + member.getMemberNickname());
       log.debug("전화번호: " + member.getMemberTel());

      
      String selected = service.selectID(member);
            

      
      
      if(selected==null) {
         ra.addFlashAttribute("message", "닉네임과 전화번호에 일치하는 아이디가 없습니다.");
      }
         
      else {
//         String email=  selected.getMemberEmail();

         ra.addFlashAttribute("findEmail", selected);
         
      }

      return "redirect:/";
   }
   
   
   
   
   @GetMapping("selectPW")
   public String selectPw() {
         
      return "member/findPw";
   }
   
   
   @PostMapping("selectPW") // <-- findPwForm의 method="POST", action="/member/selectPW" 와 일치
   @ResponseBody // <-- 이 메서드의 반환 값을 HTTP 응답 본문에 직접 담아 클라이언트로 보냅니다. (findPw.js에서 response.text()로 받게 됩니다)
   public String selectPwPost(@ModelAttribute Member member) { // @ModelAttribute로 폼 데이터(name 속성 일치)를 Member 객체로 바로 받을 수 있습니다.

      log.info("AJAX 비밀번호 찾기 (POST) 요청 수신");
      log.debug("수신 데이터 - 닉네임: " + member.getMemberNickname());
      log.debug("수신 데이터 - 이메일: " + member.getMemberEmail());
      log.debug("수신 데이터 - 전화번호: " + member.getMemberTel());

      String findPwResult = null; // 서비스 호출 결과를 저장할 변수

      try {
         // --- 실제 서비스 호출 위치 ---
         // TODO: MemberService에 정의된 비밀번호 찾기 처리 메서드를 호출하세요.
         // 이 메서드는 member 객체를 받아서 로직 수행 후 결과 문자열을 반환해야 합니다.
         // 이전에 MemberService 인터페이스와 MemberServiceImpl에 추가한 processPasswordFind 메서드입니다.

         findPwResult = service.processPasswordFind(member); // <-- 이 줄을 활성화하고 사용하면 됩니다!
            // findPwResult 변수에 Service 메서드가 반환한 결과 문자열 (예: "EMAIL_SENT", "NOT_FOUND")이 담깁니다.


      } catch (Exception e) {
         // Service, Mapper, EmailService 등 하위 레이어에서 예상치 못한 예외 발생 시
         log.error("비밀번호 찾기 서비스 실행 중 오류 발생", e);
         // @Transactional에 의해 예외 발생 시 자동 롤백될 수 있습니다.
         findPwResult = "ERROR"; // 클라이언트에 오류 상태를 알림
      }

      log.info("비밀번호 찾기 서비스 결과 문자열 반환: " + findPwResult);
      // @ResponseBody 덕분에 findPwResult 문자열이 HTTP 응답 본문으로 그대로 클라이언트에게 전달됩니다.
      return findPwResult; // findPw.js의 fetch().then(response => response.text()).then(resultText => ...) 에서 이 값을 받게 됩니다.
   }

   
}
