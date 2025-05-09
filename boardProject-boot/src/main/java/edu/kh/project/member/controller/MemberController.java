package edu.kh.project.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.member.model.service.MemberService;
import jakarta.mail.Session;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@SessionAttributes({"loginMember","memberNo1"}) // 여러개를 session에다가 실을수있어서 {} 안에서 쓰면됨.
@Controller
@RequestMapping("member")
@Slf4j
public class MemberController {
	
	@Autowired // Spring에서 제공하는 의존성 주입 어노테이션
	private MemberService service;
	
	/* [로그인]
	 * -특정 사이트에 아이디/ 비밀번호 입력 후
	 *  해당 정보가 DB 에 있으면 조회/ 서비스 이용
	 *  
	 *  - 로그인 한 회원 정보는 session에 기록하여 
	 *  로그아웃 또는 브라우저 종료 시 까지 
	 *  해당 정보를 계속 이용할 수 잇게 함.
	 * 
	 * */
	
	
	
	
	/** 로그인
	 * @param inputMember : 커맨드 객체 (@ModelAttribute 생략)
	 *                      memberEmail, memberPw 세팅된 상태
	 * 
	 * @param ra : 리다이렉트시 request scope -> session scope -> request 로 데이터 전달 
	 * @param model : 데이터 전달용 객체 (기본 request scope)
	 *					/ (@SessionAttributes 어노테이션과 함께 사용시 session scope 이동)
	 * @return 
	 */
	
	@PostMapping("login")
	public String login(Member inputMember , RedirectAttributes ra , Model model,
						@RequestParam(value="saveId", required = false) String saveId,
						 HttpServletResponse resp) {
		
		// 체크박스
		// - 체크가 된경우 : "on"
		// - 체크가 안된경우 : null
		
		
		// 로그인 서비스 호출
		Member loginMember = service.login(inputMember);
		
		// 로그인 실패시
		
		if (loginMember == null) {
			ra.addFlashAttribute("message" , "아이디 또는 비밀번호가 일치하지 않습니다.");
			
			
		}else {// 로그인 성공시
			
			//session scope에 loginMember 추가
			
			model.addAttribute("loginMember",loginMember);
			// 1단계 : model을 이용하여 request scope에 세팅됨
			// 2단계 : 클래스위에 @SessionAttributes() 어노테이션 작성하여
			//                    session scopel loginMember를 이동
			
			//************ Cookie *********************
			// 로그인 시 작성한 이메일 저장 (쿠키에) 
			
			// 쿠키 객체 생성 (K:V)
			Cookie cookie = new Cookie("saveId",loginMember.getMemberEmail());
			// saveId = user01@kh.or.kr
			
			// 쿠키가 적용될 경로 설정
			// -> 클라이언트가 어떤 요청을 할때 쿠키를 첨부할지 지정
			// ex) "/" : IP 또는 도메인 또는 localhost
			//          --> 메인페이지 + 그하위 주소 모두
			
			cookie.setPath("/");
			
			// 쿠키의 만료 기간 지정
			if(saveId != null) { // 아이디 저장 체크박스 체크했을때
		       cookie.setMaxAge(60 * 60 * 24 * 30) ;	// 30일 (초단위)
				
			}else {  // 체크 안했을때
				cookie.setMaxAge(0); // 0초 (클라이언트 쿠키 삭제)
			}
			 
			// 응답 객체에 쿠키 추가 -> 클라이언트에게 전달
			resp.addCookie(cookie);
			
		}
		
		return "redirect:/"; // 메인 페이지로 리다이렉트
	}

	/** 로그아웃 : session에 저장된 로그인 된 회원 정보를 없앰
	 * @param SessionStatus : @sessionAttributes로 지정된 특정 속성을 세션에서 제거기능 제공 객체
	 * @return
	 */
	@GetMapping("logout")
	public String logout(SessionStatus status) {
		
		status.setComplete(); // 세션을 완료시킴 (== @SessionAttributes로 등록된 세션 제거)
		return "redirect:/";
	}
	/** 회원 가입 페이지로 이동
	 * @return
	 */
	@GetMapping("signup")
	public String signupPage() {
		
		return"member/signup";
	}
	
	// 아이디 비번찾기 로 
	@GetMapping("findMember")
	public String findMember() {
		
		return"member/findMember";
	}
	
	
	
	@PostMapping("findMember")
	public String findMember(Member inputMember, Model model, RedirectAttributes ra) {
	   
		Member findmember = service.findMember(inputMember);
	   
	    String findId = null;
	    String message = null;

	    if (findmember != null) {
	       
	    	findId = findmember.getMemberEmail();
	    }

	    if (findId == null) {
	        message = "아이디 찾기에 실패했습니다.";
	        ra.addFlashAttribute("message", message);
	        return "redirect:/member/findMember";
	    } else {
	        model.addAttribute("findId", findId);
	        return "member/findMember";
	    }
	}
	
	
	
	//비번 찾기
	@GetMapping("findPw")
	public String findPw() {
		
		return"member/findPw";
	}
	
	/** 비밀번호를 재설정 하기전 가입된회원이 찾는지 
	 * @param model
	 * @param inputMember
	 * @param ra
	 * @return
	 */
	@PostMapping("findPw")
	public String findPw(Model model, Member inputMember, RedirectAttributes ra) {
	    String message = null;

	    Member findmember = service.findPw(inputMember);

	    if (findmember != null) {
	        int memberNo = findmember.getMemberNo();
	        model.addAttribute("memberNo1", memberNo); // @SessionAttributes에 의해 세션에 저장됨
	        return "/member/findPw";
	    } else {
	        message = "가입된 회원이 아닙니다.";
	        ra.addFlashAttribute("message", message);
	        return "redirect:/member/findPw";
	    }
	}
	
	@PostMapping("findPwConfirm")
	public String findPwConfirm(Member inputMember,
			RedirectAttributes ra , @SessionAttribute("memberNo1") int memberNo,
			                        SessionStatus status) {
	                            
	                            
		inputMember.setMemberNo(memberNo);
	    
		int result = service.findPwConfirm(inputMember);
		

	    String message = null;
	    String path = null;

	    if (result != 1) {
	        message = "비밀번호가 재설정되지 않았습니다.!!";
	        path = "/member/findPw";
	        status.setComplete();
	    } else {
	        message = "비밀번호 재설정완료 !! 메인페이지에서 다시로그인 해주세요.";
	        path = "/";
	        status.setComplete();
	    }

	    ra.addFlashAttribute("message", message);
	    return "redirect:" + path;
	}
		
		
	
	
	/** 이메일 중복검사 (비동기 요청)
	 * @return 중복된 데이터의 개수
	 */
	@GetMapping("checkEmail") // GET 방식 요청 /member/checkEmail
	@ResponseBody  //응답 본문 (fetch) 으로 돌려보냄
	public int checkEmail(@RequestParam("memberEmail") String memberEmail) {
		
		return service.checkEmail(memberEmail);
		
		
	}
	/**닉네임 중복검사 (비동기)
	 * @param memberNickname
	 * @return 중복이면 1 아니면 0
	 */
	@ResponseBody
	@GetMapping("checkNickname")
	public int checkNickname(@RequestParam("memberNickname") String memberNickname) {
		
		return service.checkNickname(memberNickname);
		
	}
	@ResponseBody
	@GetMapping("checkTel")
	public int checkTel(@RequestParam("memberTel") String memberTel) {
		
		return service.checkTel(memberTel);
	}
	
	/**회원가입
	 * @param inputMember : 입력된 회원정보 (memberEmail, memberPw , 
	 *  memberNickname, memberTel ,  
	 *  membeAddress => 따로 배열로 받아 처리)
	 * @param memberAddress : 입력한 주소 input 3개의 값을 배열로 전달 [우편번호, 도로명/지번주소, 상세주소]
	 * @param ra : 리다이렉트 시 request -> session -> request 로 데이터 전달하는 객체
	 * @return
	 */
	@PostMapping("signup")
	public String signup(Member inputMember, @RequestParam("memberAddress") String[] memberAddress, RedirectAttributes ra  
			
			                 ) {
		    //log.debug("inputMember :" + inputMember);
			//log.debug("memberAddress :" + memberAddress.toString());
		
			//회원 가입 서비스 호출
		int result = service.signup(inputMember, memberAddress);
		
		String path = null;
		String message = null;
		
		if(result > 0) {// 성공
			
			message = inputMember.getMemberNickname() + "님의 가입을 환영합니다!";
			path = "/";
			
		}else{ // 실패
			message = "회원가입 실패..";
			path = "signup";
		}
		ra.addFlashAttribute("message",message);
		
		return "redirect:"+ path;
		//성공 -> redirect:/
		//실패 -> redirect:signup (상대경로)
		//        현재주소 /member/signup/ GET 요청
	}
	
}
