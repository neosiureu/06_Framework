package edu.kh.project.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
	 * (@ModelAttribute로 불러온 놈 => memberEmail, memberPw가 이미 세팅된 상태)
	 * @param inputMember 
	 * @param ra: 리다이렉트 시 일시적으로 세션 스코프로 바꾸지만 리다이렉트가 끝나면 사라지는 정보
	 * @param model: 데이터 전달용 객체로 기본적으로 request 범위의 객체이지만 
	 * SessionAttribute라는 어노테이션과 함께 사용 시 session scope에 객체를 담을 수 있다.
	 * @return 
	 */
	
	@PostMapping("login")
	// 동기식 요청
	public String login(Member inputMember, RedirectAttributes ra, Model model, 
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

			// 여기까지가 1단계로 model을 이용하여 request scope에 세팅
			
			
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
		// 회원가입 페이지로 이동하는 요청을 받음
		
		
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

}
