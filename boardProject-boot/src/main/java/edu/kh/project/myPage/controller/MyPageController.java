package edu.kh.project.myPage.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.myPage.model.service.MyPageService;
import org.springframework.web.bind.annotation.RequestParam;




/*
 - 세션에 저장된 @sessionAttributes의 역할 => 모델에 추가된 속성 중 key값이 일치하는 속성을
 session scope로 변경한다
 - SessionStatus 이용 시 session에 등록된 완료 대상을 찾는 용도
 
 VS
 
 
 @SessionAttribute => 역으로 세션에 존재하는 값을 키로 반환시키는 역할 => 매개변수로서 대입
  
 
*/


@RequestMapping("myPage")
@Controller
public class MyPageController {
	
	@Autowired
	private MyPageService service;
    // MyPageService service = new  MyPageService();
	
	
	@GetMapping("info")
	public String info(@SessionAttribute("loginMember") Member loginMember,Model model)
		{
		
		// (/myPage/info일 때 GET요청을 매핑)
		// 닉네임 눌렀을 때
		
		
		// 현재 로그인한 회원의 주소를 꺼내온다 => 현재 loginMember를 키로 하여 session scope에 등록
		String memberAddress = 
		loginMember.getMemberAddress();
		
		if(memberAddress!=null) {
			// 주소가 있긴 있을 경우에 동작
			// memberAddress값을  ^^^기준으로 문자열을 쪼개어 String[]로 반환한다
			String[] arr= memberAddress.split("\\^\\^\\^");
			// "@4540"^^^"서울시 중구 남대문로 120"^^^"3층, E강의실"과 같은 결과가 반환된다.
			// ["@4540", "서울시 중구 남대문로 120", "3층, E강의실"]과 같은 결과가 반환된다.
			
			model.addAttribute("postcode",arr[0]);
			model.addAttribute("address",arr[1]);
			model.addAttribute("detailAddress",arr[2]);
			
		}
		
		
		return "myPage/myPage-info";
		
	}
	
	
	
	@GetMapping("profile")
	// myPage/profile로 온 GET요청을 매핑한다
	public String profile() {
		
		return "myPage/myPage-profile";
	}
	
	
	//
	
	@GetMapping("changePw")
	public String changePw() {
		
		return "myPage/myPage-changePw";
	}
	
	
	

	@GetMapping("secession")
	public String secession () {
		
		return "myPage/myPage-secession";
	}
	
	
	@GetMapping("fileTest")
	public String fileTest () {
		
		return "myPage/myPage-fileTest";
	}
	
	
	
	
	
	
	/**
	 * @param @ModelAttribute Member속성 
	 * inputMember 커맨드 객체 (수정된 회원의 닉네임과 전화번호와 주소를 한번에 받아오려고) 
	 * 쉽게 말해 뷰의 name과 서버의 dto를 매칭시켜서 알아서 넣어주려고
	 * @param logMember: 로그인한 회원 정보를 가진 객체 (현재 닉네임한 사람의 닉네임과 전화번호를 바꾸려는데
	 * 회원번호를 가지고 그 번호를 업데이트 하기 위함 )
	 * @param memberAddress 
	 * 주소 부분만 따로 받은 String 배열 => 다시 ^^^로 변경하려고 가져왔다. 변경이 없다면
	 * @param ra
	 * @return
	 */
	@PostMapping("info")
	public String updateInfo(
			@ModelAttribute Member inputMember, 
			@SessionAttribute("loginMember") Member logMember,
			@RequestParam("memberAddress") String[] memberAddress,
			RedirectAttributes ra
			) {
		
		// inputMember에 로그인한 회원 번호를 추가
		
		inputMember.setMemberNo(logMember.getMemberNo());
		// 현재 로그인한 멤버의 숫자를 커맨드 객체에 넣는다 => 회원의 번호와 닉네임과 전화번호와 주소를 넣는다
		// 주소는 나중에 바꾼다
		
		
		// 회원정보 수정 서비스를 호출한다
		int result = service.updateInfo(inputMember,memberAddress);
		
		String message = null;
		
		if(result>0) { //회원정보 수정 성공 시
			// loginMember를 업데이트한 값으로 새로 세팅해야 한다
			// loginMember는 세션에 저장된 로그인한 회원정보가 저장된 객체를 참조하고 있따
			// 로그인 멤버를 직접 세터로 수정한다 => 세션에 저장된 로그인한 회원정보가 수정될 것이다.
			// 세션에 저장된 데이터와 DB 데이터를 동기화
		
			logMember.setMemberAddress(inputMember.getMemberNickname());
			logMember.setMemberTel(inputMember.getMemberTel());
			logMember.setMemberAddress(inputMember.getMemberAddress());
			

			message = "회원정보 수정 성공!!";
			
			
		}
		else{
			message = "회원정보 수정 실패!!";

		}
		
		ra.addFlashAttribute("message",message);
		
		return "redirect:info";
		// 상대경로 => 현재경로의 제일 마지막
	}
	
	
	
	
	
	
	
	
	
	
	
	
	

}
