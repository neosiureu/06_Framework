package edu.kh.project.common.scheduling;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import edu.kh.project.board.model.service.BoardService;
import lombok.extern.slf4j.Slf4j;


@Component
// Controller는 해당 클래스는 컨트롤러임을 명시하고 bean으로 등록
// Component는 그냥 빈으로만 등록하는 것
@Slf4j
@PropertySource("classpath:/config.properties")
// 이 설정 파일을 참조할거야
public class ImageDeleteScheduling {
	
	
	@Value("${my.profile.folder-path}")
	private String profileFolderPath;
	// 회원 프로필 이미지 파일을 저장하는 경로
	
	
	
	@Value("${my.board.folder-path}")
	private String boardFolderPath;
	// 게시판 이미지 파일을 저장하는 경로
	
	@Autowired
	private BoardService service;
	
	
	
	@Scheduled(cron = "0 0 12 * * *") // 정오마다
	public void scheduling() {
		log.info("스케줄러 동작!!"); // log.debug가 아님
		// 다만 메서드 상단에 @Scheduled가 나옴
		
		
		
		
		
		// 할일: DB와 서버에서 파일 목록을 비교한 후 DB에는 없는 서버 이미지 파일을 삭제하는 동작
		
		// 1. 서버에 저장된 파일에 대한 목록을 조회한다. C:/uploadFiles
		File memberFolder = new File(profileFolderPath);
		//C:/uploadFiles/profile/와 연결되는 스트림이 생성

		File boardFolder = new File(boardFolderPath); 
		//C:/uploadFiles/board/와 연결되는 스트림이 생성

		
		
		// 참조하는 폴더에 존재하는 파일 목록 얻어오기
		
		File[] memberArr = memberFolder.listFiles();
		File[] boardArr = boardFolder.listFiles() ; //listFiles()메서드가 파일 배열을 반환하기 때문에 File[] boardArr
		
				
		// 경로가 다른 두 파일에 대한 배열을 하나로 합친다
		// imageArr이라는 빈 배열을 만든다 boardArr과 memberArr의 길이만큼의 크기로 만든다
		
		// 배열은 한번 크기를 지정하면 변경 불가 => 둘을 합친 크기만큼 길이를 만들어야 함
		
		File[] imageArr  = new File[boardArr.length+ memberArr.length];
		
		
		// 배열의 내용을 복사 (깊은 복사 => 주소 값 뿐만 아니라 아예 다른 주소를 참조하는 것처럼 만들겠다)
		
		// System.arraycopy()를 이용하여 배열을 깊은복사
		//  System.arraycopy(복사할 배열
		// , 몇 번 인덱스부터 복사할지
		// , 새로운 배열
		// , 새로운 배열의 몇번 인덱스부터 넣을지
		// , 복사를 어느 길이까지 할 건지)
		
		
		System.arraycopy(memberArr, 0, imageArr, 0, memberArr.length);
		System.arraycopy(boardArr, 0, imageArr, memberArr.length , boardArr.length);
		
		// 배열에서 리스트로 변환 => 지금부터는 사용자 마음대로 해도 되기 때문에 다루기 쉬운 컬렉션 구조로 바꾼다
		
		List<File> serverImageList = Arrays.asList(imageArr);
		
		
		
		// 2. DB 이미지 파일 이름만 모두 조회
		
		List<String> dbImageList = service.selectDBImageList();
		
		
		// 3. 서버와 DB이미지 파일명을 비교하여 서버에는 있지만 DB에 없는 파일을 DB에서 삭제한다.
		
		if(!serverImageList.isEmpty()) {
			for(File serverImage :serverImageList) { // 서버 저장 파일 객체 => DB에는 존재하지 않은 것을 구분하겠다
				// File.getName(): 서버에 저장된 파일의 진짜 이름을 반환하는 메서드
				
				// List.indexof(객체): List에 전달한 객체가 존재하면 존재하는 인덱스 번호 반환. 존재하지 않으면 -1반환
				if(dbImageList.indexOf(serverImage.getName()
						)==-1) 
				
				{
				// db에 실제로 존재하지 않는다 => 일단 로그부터
					log.info(serverImage.getName()+"이라는 파일이 삭제됨!!");
					serverImage.delete(); // 파일객체에서 제공하는 메서드
					
				}
				
				
			}
		}
		
	}

	
	
	
}
/*
* @Scheduled
Spring에서 제공하는 스케줄러 : 시간에 따른 특정 작업(Job)의 순서를 지정하는 방법.


* 설정 방법
1) XXXAPPlication.java 파일에 @EnableScheduling 어노테이션 추가



2) 스케쥴링 동작을 위한 클래스 작성 (이 클래스)


BoardProjectBootApplication.java (로그인등을 이상한 화면 안 뜨게)




 @Scheduled 속성 (메서드에 대한 어노테이션)
 
 
- fixedDelay : 이전 작업이 끝난 시점으로 부터 고정된 시간(ms)을 설정.
 @Scheduled(fixedDelay = 10000) // 이전 작업이 끝난 후 10초 뒤에 실행
  
- fixedRate : 이전 작업이 수행되기 시작한 시점으로 부터 고정된 시간(ms)을 설정.
@Scheduled(fixedRate  = 10000) // 이전 작업이 시작된 후 10초 뒤에 실행


=> 정해진 시간에 작업을 수행할 수 없음. 따라서 cron을 이용하여 정해진 시간마다 자동실행 하도록 함




* cron 속성 : UNIX계열 잡 스케쥴러 표현식으로 작성 - cron="초 분 시 일 월 요일 [년도]" - 요일 : 1(SUN) ~ 7(SAT)
ex) 2019년 9월 16일 월요일 10시 30분 20초 cron="20 30 10 16 9 2 " // 연도 생략 가능

*  @Scheduled(cron = "30 * * * * *") //매분 30초마다
*  @Scheduled(cron = "0 0 12 * * *") // 정오마다
*  @Scheduled(cron = "0,30  * * * * *") // 모든 분들 중 0초와 30초마다
*  @Scheduled(cron = "0 0  * * * *") // 정시마다
*  @Scheduled(cron = "0 0  * * * *") // 매달 1일마다





 - 특수문자
* : 모든 수.
- : 두 수 사이의 값. ex) 10-15 -> 10이상 15이하
, : 특정 값 지정. ex) 3,4,7 -> 3,4,7 지정
/ : 값의 증가. ex) 0/5 -> 0부터 시작하여 5마다
? : 특별한 값이 없음. (월, 요일만 해당)
L : 마지막. (월, 요일만 해당)


@Scheduled(cron="0 * * * * *") // 모든 0초 마다 -> 매 분마다 실행


*/
