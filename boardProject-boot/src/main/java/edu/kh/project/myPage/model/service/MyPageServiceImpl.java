package edu.kh.project.myPage.model.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.common.util.Utility;
import edu.kh.project.member.model.dto.Member;
import edu.kh.project.myPage.model.dto.UploadFile;
import edu.kh.project.myPage.model.mapper.MyPageMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service

public class MyPageServiceImpl implements MyPageService {

	@Autowired
	private BCryptPasswordEncoder bcrypt;
	// 비크립트 암호화 객체 의존성주입
	// (프로젝트 SecurityConfig에서 이미 해놓음)

	@Autowired
	private MyPageMapper mapper;

	public int updateInfo(Member inputMember, String[] memberAddress) {

		// 입력된 주소가 있을 경우와 없을 경우로 나눔

		if (!inputMember.getMemberAddress().equals(",,")) {
			// 주소에 입력된 값이 있을 때의 로직
			String address = String.join("^^^", memberAddress);
			inputMember.setMemberAddress(address);
		}

		else {
			inputMember.setMemberAddress(null);
		}

		return mapper.updateInfo(inputMember);
	}

	/**
	 * 비밀번호 변경 서비스
	 */
	@Override
	public int changePw(Map<String, String> paramMap, int memberNo) {
		/*
		 * 1. 현재 비밀번호가 일치하는가? DB단부터 현재 회원의 비밀번호를 끌어와서 조회 암호화된 것을 끌어와서 paramMap에서 꺼낸 비밀번호
		 * 평문과 matches
		 * 
		 */

		String originPw = mapper.selectPw(memberNo);

		// 입력받은 현재 비밀번호가 내려오고 있는데 그는 평문
		// DB로부터 No에 맞는 암호화된 비밀번호가 올라오는 중

		if (!bcrypt.matches(paramMap.get("currentPw"), originPw)) {
			return 0;
		}

		// 그 둘을 비교하여 다르다면 0을 반환

		/*
		 * 2. 평문과 암호화된 비밀번호가 같다면 새로운 비밀번호를 암호화해야함
		 */

		String encPw = bcrypt.encode(paramMap.get("newPw"));

		// 새로운 암호화된 비밀번호 (encPw)와 회원 번호(memberNo)를 DB에 전달한다

		// -> member에 전달할 수 있는 전달인자는 1개

		// 맵으로 묶되 객체를 만드는 것이 아니라 paramMap을 재활용하기로 한다

		paramMap.put("encPw", encPw);
		paramMap.put("memberNo", memberNo + " "); // 숫자 + " " => 전부 문자열로 형변환

		return mapper.changePw(paramMap);

	}

	// 회원 탈퇴 서비스
	@Override
	public int secession(String memberPw, int memberNo) {

		// 현재 로그인한 회원의 암호화된 비밀번호 DB에서 조회하여
		String originPw = mapper.selectPw(memberNo);
		// 위에서 이미 비밀번호 조회를 만들어놨었음

		if (!bcrypt.matches(memberPw, originPw)) {
			return 0;
		}

		return mapper.secession(memberNo);
	}

	// 파일 업로드 파일 1
	@Override
	public String fileUpload1(MultipartFile uploadFile) throws Exception {

		// MultipartFile이 제공하는 다양한 메서드가 존재
		/*
		 * 1) getSize() = 파일의 크기 2) isEmpty() 3) getOriginalFileName() = 원본 파일명 
		 * 4)transferTo(경로) 메모리 또는 임시저장경로에 업로드만 되어있는 파일을 원하는 경로에 실제로 전송하겠다 실제로 서버 메모리에
		 * 50MB까지는 임시저장 원래 temp에 저장 되지만 이 메서드를 이용하면 실제 서버 하드에 저장되는 셈 서버의 어떤 경로 폴더에 저장할지가
		 * 인자로 들어 옴
		 * 
		 */

		// 업로드한 파일이 실재하는가?
		
		if(uploadFile.isEmpty()) 
		{ return null;}
		
		//C:uploadFiles/test/파일명 으로 서버 하드에 저장
		
		
		uploadFile.transferTo(new File("C:/uploadFiles/test/"+uploadFile.getOriginalFilename()));
		// 실제로 이 이후로 test폴더 안에 이미지가 저장 됨
		
		// 웹에서 해당 파일에 접근할 수 있는 경로 반환
		// 즉 서버의 경로 저장  C:uploadFiles/test/"+uploadFile 
		// 다만 웹에서 접근할 수 있는 주소는 /myPage/file/A.jpg꼴일 것
		return "/myPage/file/"+uploadFile.getOriginalFilename();
	}

	/*
	
	 파일업로드테스트2 => DB에 저장
	 DB에 파일 저장이 가능은 하나 무거워짐
	 
	  1) DB에는 서버에 저장할 파일 경로만을 저장한다
	  
	  2) DB에 삽입 / 수정 완료된 후 성공 되고 서버에 실제로 파일을 저장한다
	  
	  3) 파일 저장 실패 시 예외 발생 => transactional 어노테이션을 통해 rollback;
	 
	 */
	
	@Override
	public int fileUpload2(MultipartFile uploadFile, int memberNo) throws Exception {
		
		
		if(uploadFile.isEmpty()) {
			
			return 0;
		}
		
		

		// 1. 실제 서버에 저장할 파일 경로 만들기
		
		String folderPath = "C:/uploadFiles/test/";
		
		
		// 2. 클라이언트가 파일이 저장된 폴더에 접근할 수 있는 주소 (img태그 src태그에 들어갈 주소)
		
		// fileConfig를 보면 됨
		
		String webPath = "/myPage/file/";
		
		// 3. DB에 전달할 데이터를 DTO하나를 만들어 묶는다.
		// webPath memberNo, 원본 파일명, 변경된 파일명 => 유틸리티라는 클래스를 하나 만들어 
		// 원본 파일명을 가지고 원하는대로 만드는 기능을 수행한다
		
		
		String fileRename = Utility.fileRename(uploadFile.getOriginalFilename());
		
		
		
		/*
		 	private int fileNo;
			private String filePath;
			private String fileOriginalName;
			private String fileRename;
			private String fileUploadDate;
			private int memberNo;
		*/

		// 1. 반복되는 참조변수명 생략 가능, set구문 생략 가능
		// 2. method chaining 을 통해 한줄로 작성 가능
		// 3. 매개변수 생성자의 개수를 맞출 필요가 없음
		
		UploadFile uf = UploadFile.builder().memberNo(memberNo)
				.filePath(webPath).fileOriginalName(uploadFile.getOriginalFilename())
				.fileRename(fileRename).build();
		
		int result = mapper.insertUploadFile(uf);

		
		
		// 4. 삽입에는 성공했지만 서버에도 이미지를 저장해야 한다
		
		if(result==0) return 0; // 실패시 아무것도 안 함
		
		uploadFile.transferTo( new File(folderPath+fileRename));
		
		// folderPath경로쪽으로 파일을 실제 서버 컴퓨터에 저장한다
		
		return result;
	
	}

	@Override
	public List<UploadFile> fileList(int memberNo) {
		
		return mapper.fileList(memberNo);
	}

	/**
	 * 여러파일 업로드 서비스
	 */
	@Override
	public int fileUpload3(List<MultipartFile> aaaList, List<MultipartFile> bbbList, int memberNo) throws Exception {
		// List에서 꺼내다가 DB와 서버에 등록
		
		
		// 1. aaaList처리 
		// 제대로 DB에 저장됐을 때 그 행의 개수를 반환 

		int result1= 0;
		//일종의 결과 저장용 변수
		
		// 1.5 업로드된 파일이 없을 경우를 제외하고 업로드한다
		// 리스트를 순회하면서 [MultipartFile, MultipartFile,MultipartFile.... ]에서 비었는지 확인
		
		for(MultipartFile file :aaaList) {
			if(file.isEmpty()) {// 파일이 없으면 다음 파일을 검사
				continue;
			}
			
			// DB에 저장 + 서버에 실제로 저장
			result1 +=	fileUpload2(file, memberNo);
			// 위에서 만든 파일업로드 메서드를 호출해서 썼다.
			
		}
		
		
		
		// 2. bbb리스트 처리
		
		int result2= 0;
		
		
		for(MultipartFile file :bbbList) {
			if(file.isEmpty()) {// 파일이 없으면 다음 파일을 검사
				continue;
			}
			
			// DB에 저장 + 서버에 실제로 저장
			result2 +=	fileUpload2(file, memberNo);
			// 위에서 만든 파일업로드 메서드를 호출해서 썼다.
			
		}
		
		
		return result1+result2;
		
	}

	
	

}
