package edu.kh.project.myPage.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.myPage.model.dto.UploadFile;
import edu.kh.project.myPage.model.service.MyPageService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.RequestParam;

/*
 - 세션에 저장된 @sessionAttributes의 역할 => 클래스 위에 써서 모델에 추가된 속성 중 key값이 일치하는 속성을
 session scope로 변경한다
 - SessionStatus 이용 시 session에 등록된 완료 대상을 찾는 용도
 
 
 public String secession(...
			SessionStatus status =>  물론 인자로 이걸 넣어야 함 
			) {  status.setComplete(); // 탈퇴되었으면 세션을 만료해야 함

 
 
 VS
 
 
 @SessionAttribute => 역으로 세션에 존재하는 값을 키로 반환시키는 역할 => 매개변수로서 대입
  
 
*/

@SessionAttributes({ "loginMember" }) // 이것과 모델을 같이 쓰던가 (아니면 @SessionAttributes와 @SessionAttribute )
// 나중에 이것도 추가됨 => 탈퇴하면 정보가 없어져야하기 떄문
@RequestMapping("myPage")
@Controller
@Slf4j
public class MyPageController {

	@Autowired
	private MyPageService service;
	// MyPageService service = new MyPageService();

	@GetMapping("info")
	public String info(@SessionAttribute("loginMember") Member loginMember, Model model) { // @ReaquesParam이랑
																							// @SessionAttribute 범위만 다르고
																							// 비슷한건가???

		// (/myPage/info일 때 GET요청을 매핑)
		// 닉네임 눌렀을 때

		// 현재 로그인한 회원의 주소를 꺼내온다 => 현재 loginMember를 키로 하여 session scope에 등록
		String memberAddress = loginMember.getMemberAddress();

		if (memberAddress != null) {
			// 주소가 있긴 있을 경우에 동작
			// memberAddress값을 ^^^기준으로 문자열을 쪼개어 String[]로 반환한다
			String[] arr = memberAddress.split("\\^\\^\\^");
			// "@4540"^^^"서울시 중구 남대문로 120"^^^"3층, E강의실"과 같은 결과가 반환된다.
			// ["@4540", "서울시 중구 남대문로 120", "3층, E강의실"]과 같은 결과가 반환된다.

			model.addAttribute("postcode", arr[0]);
			model.addAttribute("address", arr[1]);
			model.addAttribute("detailAddress", arr[2]);

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
	public String secession() {

		return "myPage/myPage-secession";
	}

	@GetMapping("fileTest")
	public String fileTest() {

		return "myPage/myPage-fileTest";
	}

	/**
	 * @param @ModelAttribute Member속성 inputMember 커맨드 객체 (수정된 회원의 닉네임과 전화번호와 주소를
	 *                        한번에 받아오려고) 쉽게 말해 뷰의 name과 서버의 dto를 매칭시켜서 알아서 넣어주려고
	 * @param logMember:      로그인한 회원 정보를 가진 객체 (현재 닉네임한 사람의 닉네임과 전화번호를 바꾸려는데 회원번호를
	 *                        가지고 그 번호를 업데이트 하기 위함 )
	 * @param memberAddress   주소 부분만 따로 받은 String 배열 => 다시 ^^^로 변경하려고 가져왔다. 변경이 없다면
	 * @param ra
	 * @return
	 */
	@PostMapping("info")
	public String updateInfo(@ModelAttribute Member inputMember, @SessionAttribute("loginMember") Member logMember,
			@RequestParam("memberAddress") String[] memberAddress, RedirectAttributes ra) {

		// inputMember에 로그인한 회원 번호를 추가

		inputMember.setMemberNo(logMember.getMemberNo());
		// 현재 로그인한 멤버의 숫자를 커맨드 객체에 넣는다 => 회원의 번호와 닉네임과 전화번호와 주소를 넣는다
		// 주소는 나중에 바꾼다

		// 회원정보 수정 서비스를 호출한다
		int result = service.updateInfo(inputMember, memberAddress);

		String message = null;

		if (result > 0) { // 회원정보 수정 성공 시
			// loginMember를 업데이트한 값으로 새로 세팅해야 한다
			// loginMember는 세션에 저장된 로그인한 회원정보가 저장된 객체를 참조하고 있따
			// 로그인 멤버를 직접 세터로 수정한다 => 세션에 저장된 로그인한 회원정보가 수정될 것이다.
			// 세션에 저장된 데이터와 DB 데이터를 동기화

			logMember.setMemberAddress(inputMember.getMemberNickname());
			logMember.setMemberTel(inputMember.getMemberTel());
			logMember.setMemberAddress(inputMember.getMemberAddress());

			message = "회원정보 수정 성공!!";

		} else {
			message = "회원정보 수정 실패!!";

		}

		ra.addFlashAttribute("message", message);

		return "redirect:info";
		// 상대경로 => 현재경로의 제일 마지막
	}

	// currentPw newPw newPwConfirm가 넘어옴

	/**
	 * 비밀번호 변경하는 메서드
	 * 
	 * @param paramMap:    요청보낸 곳에서 보낸 데이터를 맵으로 저장
	 * @param loginMember: 세션에 등록된 현재 로그인한 회원의 정보
	 * @param ra:          flashAttribute
	 * @return
	 */
	@PostMapping("changePw")
	public String changePw(@RequestParam Map<String, String> paramMap,
			@SessionAttribute("loginMember") Member loginMember, RedirectAttributes ra) {

		/*
		 * 1)paramMap에는 알아서 = {currentPw = 현재비밀번호, newPw = 새로운비밀번호, newPwConfirm =
		 * 새로운비밀번호확인}
		 */

		/*
		 * 2)@SessionAttribute("loginMember") Member loginMember 결국 where절에서 어떤 멤버의 정보를
		 * 업데이트할지 알아야 하므로
		 * 
		 * 3) 플래시 어트리뷰트를 위한 ra
		 * 
		 */

		int memberNo = loginMember.getMemberNo();

		int result = service.changePw(paramMap, memberNo);
		// (뭘로 변경할지, where절에 뭘 쓸지)

		// paramMap = 현재비번 새비번 확인까지 총 세개

		String path = null;

		String message = null;

		if (result > 0) { // 변경에 성공

			message = "비밀번호가 변경되었습니다";
			path = "/myPage/info";

		}

		else { // 변경에 실패
			message = "현재 비밀번호가 일치하지 않습니다";

			path = "/myPage/changePw";

		}

		ra.addFlashAttribute("message", message);

		return "redirect:" + path;

	}

	/**
	 * 회원탈퇴
	 * 
	 * @param memberPw:  입력받은 비밀번호
	 * @param logMember: 현재 로그인한 회원의 정보를 뷰 단의 세션 스코프로부터 가져옴
	 * @param ra
	 * @param status:    @@SessionAttribute로 등록된 세션 범위 변수에 한해 해당 세션을 만료
	 * @return
	 */
	@PostMapping("secession")
	public String secession(@RequestParam("memberPw") String memberPw,
			@SessionAttribute("loginMember") Member logMember, RedirectAttributes ra, SessionStatus status) {

		int memberNo = logMember.getMemberNo();

		int result = service.secession(memberPw, memberNo);

		// 회원의 번호와 입력받은 비밀번호

		// 하지만 진짜 삭제하는게 아니라 MEMBER_DEL_FL을 Y로 바꿈

		String message = null;
		String path = null;

		if (result > 0) {
			message = "탈퇴 되었습니다";
			path = "/";
			status.setComplete(); // 탈퇴되었으면 세션을 만료해야 함
			// 로그아웃 상태로

		}

		else {
			message = "비밀번호가 일치하지 않습니다";
			path = "secession"; // 상대경로
		}

		ra.addFlashAttribute("message", message);

		return "redirect:" + path;

		// redirect:secession으로 상대로경로작성 => 현재경롤를 POST로 보여줌 => 결과적으로 리다이렉트되어 GET요청이 감

	}

	/*
	 * Spring에서 파일 업로드를처리하는 방법
	 * 
	 * - encType = "multipart/form-data"로 클라이언트 요청을 받으면 (다 섞임)
	 * 
	 * 이를 MultipartResolver를 이용하여 파일을 이진데이터로 바꾸고 나머지는 문자로 바꿈
	 * 
	 * 문자열과 숫자는 String형으로 파일은 MultipartFile객체로 분리
	 * 
	 */

	@PostMapping("file/test1")
	public String fileUpload1(@RequestParam("uploadFile") MultipartFile uploadFile, RedirectAttributes ra)
			throws Exception {

		/*
		 * <form action="/myPage/file/test1" method="POST" enctype="multipart/form-data"
		 * > <h3>업로드 테스트 1</h3>
		 * 
		 * <!-- type="file" 도 결국에는 Parameter -> @RequestParam으로 처리 가능 --> <input
		 * type="file" name="uploadFile">
		 * 
		 * 
		 * <button class="myPage-submit">제출하기</button> </form>
		 */

		String path = service.fileUpload1(uploadFile); // 웹에서 접근하여 파일이 저장된 경로를 반환

		// path = /myPage/file/A.jpg

		// 파일이 저장되어 웹에서 접근할 수 있는 경로가 반환되었을 때

		if (path != null) {
			ra.addFlashAttribute("path", path);
		}

		return "redirect:/myPage/fileTest";
	}

	/**
	 * 업로드한 이미지 파일을 DB저장+ 서버 저장 + 조회까지
	 * 
	 * @param uploadFile
	 * @param loginMember
	 * @param ra
	 * @return
	 * @throws Exception
	 */
	@PostMapping("file/test2")
	public String fileUpload2(@RequestParam("uploadFile") MultipartFile uploadFile,
			@SessionAttribute("loginMember") Member loginMember, RedirectAttributes ra) throws Exception {

		int memberNo = loginMember.getMemberNo();

		// 업로드된 파일 정보를 DB에 insert 후 결과 행의 개수를 반환받는다.

		int result = service.fileUpload2(uploadFile, memberNo);

		String message = null;

		if (result > 0) {
			message = "파일 업로드 성공";

		}

		else {
			message = "파일 업로드 실패";

		}

		ra.addFlashAttribute("message", message);

		return "redirect:/myPage/fileTest";
	}

	/**
	 * 파일목록 조회하는 화면으로 이동
	 * 
	 * @param model
	 * @param loginMember => 현재 로그인한 회원의 번호가 필요
	 * @return
	 */
	@GetMapping("fileList")
	public String fileList(Model model, @SessionAttribute("loginMember") Member loginMember) {
		// 파일화면을 보여주는 포워드할 때 DB에서 조회한 파일 목록을 조회하기 위함

		// 파일 목록 조회하는 서비스 호출. 현재 로그인한 회원이 올린 이미지만을 조회
		int memberNo = loginMember.getMemberNo();
		List<UploadFile> list = service.fileList(memberNo);
		
		// model에 담아서 forward
		
		model.addAttribute("list",list);

		return "myPage/myPage-fileList";
	}
	
	
	@PostMapping("file/test3")
	public String fileUpload3(@RequestParam("aaa") List<MultipartFile> aaaList,
			@RequestParam("bbb") List<MultipartFile> bbbList, // 아무것도 파일 선택 안 해도 들어가긴 함
			@SessionAttribute("loginMember") Member member,
			RedirectAttributes ra
			) throws Exception 
	{
		log.debug("aaaList: " + aaaList); // 아무것도 파일 선택 안 해도 들어가긴 함.  MultipartFile이 isEmpty일 뿐 주소는 출력됨
		// 0번과 1번 인덱스는 존재하는 리스트가 있음
		// 0번과 1번 인덱스에는 MultipartFile객체가 존재하나 둘다 비어있는 객체인 상태 => 
		// 두개인 이유는 name속성값이 aaa인 키가 두개라서
		
		log.debug("bbbList"+bbbList); // 아무것도 파일 선택 안 해도 들어가긴 함. MultipartFile이 isEmpty일 뿐 주소는 출력됨
		//  0번인덱스에 있는  MultipartFile 객체가 비어있는 상태다. 
		
		
		// 여러 파일을 업로드하는 서비스를 호출
		
		int memberNo = member.getMemberNo();
		
		
		int result = service.fileUpload3(aaaList,bbbList,memberNo);
		// result == 업로드된 파일의 개수
		
		
		String message = null;
		
		if(result==0) {
			message = "업로드된 파일이 없습니다";
			
		}
		
		else {
			message  = result + "개의 파일이 업로드 되었습니다";	
		}
		
		ra.addFlashAttribute("message",message);
		
		return "redirect:/myPage/fileTest";
	}
	
	
	@PostMapping("profile")
	public String profile(@RequestParam("profileImg") MultipartFile profileImg, // by multipartResolver
			@SessionAttribute("loginMember") Member loginMember,
			RedirectAttributes ra
			) throws Exception {
		
		
		String message =null;
		
		// 업로드 된 파일 정보를 DB에 insert 후 행의 개수를 반환받겠다
		
		int result = service.profile(profileImg, loginMember );
		
		if(result>0) message="변경성공!";
		else message = "변경실패";
		
		ra.addFlashAttribute("message",message);
		
		return "redirect:profile";
	}

}
