package edu.kh.project.myPage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import edu.kh.project.main.controller.MainController;
import edu.kh.project.member.model.dto.Member;
import edu.kh.project.myPage.model.service.MyPageService;

/*
 * @SessionAttributes 의 역할
 *   - Model에 추가된 속성 중 key 값이 일치하는 속성을 session scope로 변경
 *   - SessionStatus 이용시 session에 등록된 완료할 대상을 찾는 용도
 * 
 * @SessionAttribute 의 역할 ... s만빠졋음 ..
 *  -Session에 존재하는 값을 Key로 얻어오는 역할
 *  -메서드 매개변수로 쓰면 됨  @SessionAttribute("key") 타입 변수명
 * */


@Controller
@RequestMapping("myPage")

public class MyPageController {

    private final MainController mainController;
	
	@Autowired
	private MyPageService service;

    MyPageController(MainController mainController) {
        this.mainController = mainController;
    }
	
	@GetMapping("info") //  /myPage/info GET 요청 매핑  //닉넴을 눌럿을때
	public String info(@SessionAttribute("loginMember") Member loginMember ,
						Model model) {
		
		// 현재 로그인 한 회원의 주소를 꺼내옴
		// 현재 로그인 한 회원정보 -> session에 등록된 상태 (loginMember)
		
		String memberAddress = loginMember.getMemberAddress();
		// 주소가 있따면 0150^^^ 서울시 남대문로 120 ^^^ 3층, E강의장
		// 주소가 없다면 null
		
		// 주소가 있을경우에만 동작
		if(memberAddress != null) {
			
			//구분자 "^^^" 를 기준으로
			// memberAddress 값을 쪼개어 String[] 로 반환
			String[] arr = memberAddress.split("\\^\\^\\^");
			// ->0150^^^ 서울시 남대문로 120 ^^^ 3층, E강의장
			// -> ["0150" , "서울시 남대물로~" , "3층,E강의장"]
			//        0           1                    2
			
			model.addAttribute("postcode",      arr[0]);
			model.addAttribute("address",       arr[1]);
			model.addAttribute("detailAddress", arr[2]);
			
			
		}
		
		
		return "myPage/myPage-info";
	}
	
	    //프로필 이미지 변경 화면 이동
	    @GetMapping("profile") // //mypage/profile GET 요청 매핑
	    public String profile() {
		
		return "mypage/mypage-profile";
	}
	
	    //비밀번호 변경
		@GetMapping("changePw") // //mypage/changPw GET 요청 매핑
		public String changPw() {
			
			return "mypage/mypage-changePw";
		}
		
		//회원 탈퇴
		@GetMapping("secession") // //mypage/secessgion GET 요청 매핑
		public String secessgion() {
			
			return "mypage/mypage-secession";
		}
		
		//파일 테스트
		@GetMapping("fileTest") // //mypage/fileTest GET 요청 매핑
		public String fileTest() {
			
			return "mypage/mypage-fileTest";
		}
		
		//프로필 이미지 변경 화면 이동
		@GetMapping("fileList") // //mypage/fileList GET 요청 매핑
		public String fileList() {
			
			return "mypage/mypage-fileList";
		}
	
		/**
		 * @param inputMember : 커맨드 객체 (@ModelAttribute 가 생략된 상태)
		 *  					제출된 수정된 회원 닉네임, 전화번호, 주소
		 * @param loginMember : 현재 로그인 된 회원을 알기위해
		 * @param memberAddress : 3개로 나눠진 주소값을 다시 합치기위해 
		 *  					구분자 ^^^ 변경 예정
		 * @param ra            : 결과값을 알려줄 메시지
		 * @return
		 */
		@PostMapping("info")
		public String updateInfo(Member inputMember,
					 			@SessionAttribute("loginMember") Member loginMember,
					 			@RequestParam("memberAddress") String[] memberAddress,
					 			RedirectAttributes ra) {
			// inputMember에 로그인 한 회원 번호 추가 
		inputMember.setMemberNo(loginMember.getMemberNo());
		// inputMember : 회원 번호, 회원 닉넴, 전화번호 , 주소
		
		//회원 정보 수정 서비스 호출 
		int result = service.updateInfo(inputMember,memberAddress);
		String message = "";
		if(result > 0) { //회원정보 수정 성공
			
			
			//loginMember 새로 세팅
			// 우리가 방금 바꾼 값으로 세팅
			
			// loginMember는 세션에 저장된 로그인한 회원정보가 
			// 저장된 객체를 참조하고있따!
			
			// ->loginMember 를 수정하면
			//  세션에 저장된 로그인한 회원 정보가 수정된다.
			// == 세션 데이터와 DB 데이터를 동기화
			
			loginMember.setMemberNickname(inputMember.getMemberNickname());
			loginMember.setMemberTel(inputMember.getMemberTel());
			loginMember.setMemberAddress(inputMember.getMemberAddress());
			
			message = "회원 정보 수정 성공 !!";
			
		}else {
			message = "수정 실패";
		}
		
		ra.addFlashAttribute("message",message);
			
			return"redirect:info";
		}
				
			
		
		
}
