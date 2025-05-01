package edu.kh.project.member.model.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.email.model.service.EmailService;
import edu.kh.project.member.model.dto.Member;
import edu.kh.project.member.model.mapper.MemberMapper;
import lombok.extern.slf4j.Slf4j;

@Service // 비즈니스 로직처리인 서비스 역할을 한다는 것을 프레임워크에 알리고 빈으로 등록
@Transactional(rollbackFor  = Exception.class)
@Slf4j
public class MemberServiceImpl implements MemberService {
   
   // 등록된 bean중 MemberMapper와 같은 타입이거나 상속관계인 Bean을 찾아서 주입해주겠다
   
   @Autowired
   private MemberMapper mapper;
   
   // bcrypt 암호화를 위한 객체를 의존성 주입받자 => config/SecurityConfig.java를 참고한다
   @Autowired
   private BCryptPasswordEncoder bcrypt;

   
   @Autowired
    private EmailService emailService;
   
   // 로그인 서비스
   @Override
   public Member login(Member inputMember) {
      
      // 비밀번호에 대한 암호화를 진행한다.
      // 암호화 진행에는 bcrypt라는 이름의 암호화 함수를 사용한다
      
      // bcrypt.encode(문자열) => 비크립트 형식에 따라 암호화된 문자열이 반환 됨
      
      
      
      //   String bcryptPassword = bcrypt.encode(inputMember.getMemberPw());
      
      //   log.debug("bcryptPassword로 암호화된 비밀번호:"+bcryptPassword);
      
      
      
      
      // 비교를 위해 bcrpyt.matches(평문, 암호화): 일치 여부에 따라 true false
      
//$2a$10$82haJV8j1wPHa/JNEywzSOMuYdnFjBdSJvhLPB6ahNHnc/ujwHbUC
      

      // 1. 이메일이 일치하면서 탈퇴하지 않은 회원을 조회한다
      
      Member loginMember = mapper.login(inputMember.getMemberEmail());
      
      
      // 2. 만약 일치하는 이메일이 없어 조회 결과가 null인 경우
      
      if(loginMember == null) return null;
      
      // 3. 입력받은 비밀번호, 평문은  서비스단에서 inputMemeber로 내려오고 있다
      // 암호문은 암호화된 loginMember.getMemberPw()로 DB에서 올라오고 있다. 
      // 두 비밀번호가 일치하는지 확인
      // 일치하지 않으면 그냥 null
      if (!bcrypt.matches(inputMember.getMemberPw(), loginMember.getMemberPw()))
      {
         return null;
      }
      
      
      // 로그인 결과에서 비밀번호를 제거한다 => 비밀번호는 여기서만 신경쓰고 세션에 싣지 않는다
      loginMember.setMemberPw(null); // 객체 중 비밀번호 부분만 null인 상태
      
      return loginMember;
   }




   /**
   이메일 중복 검사 서비스
    */
   @Override
   public int checkEmail(String memberEmail) {
      // TODO Auto-generated method stub
      return mapper.checkEmail(memberEmail);
   }




   @Override
   public int checkNickname(String memberNickname) {
      
      return mapper.checkNickname(memberNickname);
   }




   
   @Override
   public int signup(Member inputMember, String[] memberAddres) {
      // 1) memberAddres의 주소를 ^^^로 구분되도록 바꾸기
      // inputMember.getMemberAddress() => ",," 빈문자열 세개가 붙어서 온 셈
      // memberAddress => [,,] => 빈 문자열 셋이 들은 배열이 온 셈
      
      // 주소가 입력되어 넘어온 경우
      
      if(!inputMember.getMemberAddress().equals(",,")) {
         // 주소 칸에 뭐라도 채워져 있을 때
         // String.join("구분자",배열) 
         // 배열의 모든 요소 사이에 전달한 구분자 문자열을 추가하여 하나의 문자열로 반환
         
         String address = String.join("^^^", memberAddres);
         // [12345, 서울시중구 남대문로, 3층,E강의장] => "12345^^^서울시중구 남대문로^^^3층,E강의장"
         // "12345서울시중구 남대문로3층,E강의장"으로 그대로 넘어가면 콤마를 통해 나누는 것이 힘들기 때문
         
         // 주소나 상세주소에 안 쓸 것 같은 특수 문자로 작성
         // 나중에 마이페이지에서 주소 부분 수정 페이지를 만들 시 DB에 저장된 기존 주소를 화면상에 출력해 줘야 한다
         // 우편번호, 도로명, 상세주소를 삼분할하여 보여줘야 하는데 그 구분자로 쉼표를 이용할 수가 없다
         // 구분자가 기본 형태인 콤마로 작성되어 있으면 주소나 상세주소에 콤마가 들어오는 경우
         // 3분할 이상으로 잘라질 수 있기 때문이다.
         
         inputMember.setMemberAddress(address);
         
         
         
      }
      
      // 주소가 입력되지 않고 넘어온 경우
      
      else {
         
         inputMember.setMemberAddress(null);
         
         
      }
      

      
      
      // 2) 비밀번호 암호화 => inputMember내 memberPw는 평문이지만 암호화하여 다시 inputMember에 세팅
      
      // inputMember.setMemberAddress(bcrypt.encode(inputMember.getMemberAddress()) ;
      
      String encPw = bcrypt.encode(inputMember.getMemberPw());
      
      inputMember.setMemberPw(encPw);
      
      
      // 회원가입 매퍼 메서드 호출
      return mapper.signup(inputMember);
   }




   @Override
   public String selectID(Member member) {
   
      return mapper.selectID(member) ;
   }


    // MemberService 인터페이스에 추가한 processPasswordFind 메서드 구현
    @Override
    public String processPasswordFind(Member member) {
        String resultStatus = "ERROR"; // 최종 반환할 결과 상태. 기본값은 오류.

        try {
            // 1. 입력된 정보(닉네임, 이메일, 전화번호)와 일치하는 회원이 있는지 DB에서 조회
            //    MemberMapper에 추가한 selectMemberByNicknameEmailTel 메서드 호출

            // --- 실제 Mapper 호출 ---
            // 임시 조회 로직 (if/else 블록) 전체를 삭제하고 아래 실제 Mapper 호출 코드를 사용하세요.
            Member foundMember = mapper.selectMemberByNicknameEmailTel(member); // <-- Mapper 호출!


             log.debug("회원 조회 결과: " + (foundMember != null ? "회원 번호: " + foundMember.getMemberNo() + ", 이메일: " + foundMember.getMemberEmail() : "없음"));


            // 2. 회원이 존재하지 않는 경우
            if (foundMember == null) {
                resultStatus = "NOT_FOUND"; // 사용자 없음 상태 반환
                log.info("비밀번호 찾기 실패: 일치하는 사용자 없음");
            } else {
                // 3. 회원이 존재하는 경우 -> 비밀번호 재설정 처리
                //    보안상 절대 기존 비밀번호를 가져오면 안 됩니다.
                //    새로운 임시 비밀번호 생성 -> DB 업데이트 -> 이메일 발송

                // 3-1. 새로운 임시 비밀번호 생성
                String tempPassword = createTemporaryPassword(); // 이 클래스 내부에 구현된 메서드
                 log.debug("생성된 임시 비밀번호: " + tempPassword);


                // 3-2. 임시 비밀번호를 암호화하여 DB에 업데이트
                String encTempPw = bcrypt.encode(tempPassword);
                // MemberMapper에 추가한 updatePasswordByMemberNo 메서드 호출
                int updateCount = mapper.updatePasswordByMemberNo(foundMember.getMemberNo(), encTempPw);
                 log.debug("비밀번호 업데이트 결과 (행 수): " + updateCount);


                if (updateCount > 0) { // DB 업데이트 성공 시
                     // 3-3. 사용자 이메일 주소(foundMember.getMemberEmail())로 임시 비밀번호 발송
                     //     EmailService의 메서드를 호출합니다.
                     //     EmailService에 임시 비밀번호 발송용 메서드를 추가해야 합니다.

                     // TODO: EmailService 인터페이스에 임시 비밀번호 발송 메서드 (예: boolean sendTemporaryPasswordEmail(String email, String tempPassword)) 선언 필요
                     // TODO: EmailServiceImpl 구현체에 위 메서드 구현 필요
                     //       이 메서드는 이메일 주소와 임시 비밀번호를 받아서 이메일을 발송하고 성공 여부(boolean)를 반환하도록 합니다.

                     // 실제 EmailService 호출:
                     boolean emailSent = emailService.sendTemporaryPasswordEmail(foundMember.getMemberEmail(), tempPassword); // <-- EmailService 호출!
                       log.debug("임시 비번 이메일 발송 결과: " + emailSent);

                     if (emailSent) {
                         resultStatus = "EMAIL_SENT"; // 이메일 발송 성공 상태 반환
                         log.info("비밀번호 찾기 및 임시 비번 이메일 발송 성공");
                     } else {
                         resultStatus = "EMAIL_SEND_FAILED"; // 이메일 발송 실패 상태 반환
                          log.error("비밀번호 찾기 성공, 임시 비번 이메일 발송 실패");
                         // NOTE: 이 경우 DB 업데이트는 롤백되지 않음. 재시도 시 같은 임시 비번 발송 필요.
                         //       또는 이메일 발송 실패 시 DB 업데이트도 롤백하도록 로직 설계 가능.
                     }

                } else { // DB 업데이트 실패 시 (발생 가능성은 낮지만, 혹시 모를 상황 대비)
                    resultStatus = "DB_UPDATE_FAILED"; // DB 업데이트 실패 상태 반환
                    log.error("비밀번호 찾기 성공, DB 업데이트 실패 (0행 수정)");
                }

            }

        } catch (Exception e) {
            // 로직 처리 중 예상치 못한 예외 발생 시 (DB 오류, 이메일 서버 오류 등)
            log.error("processPasswordFind 서비스 로직 실행 중 오류 발생", e);
            // @Transactional에 의해 예외 발생 시 자동 롤백될 수 있습니다.
            resultStatus = "ERROR"; // 오류 상태 반환
        }

        // Service 메서드는 최종 결과 상태 문자열을 반환합니다.
        return resultStatus;
    }
   // private String createTemporaryPassword() 메서드는 이 아래에 그대로 둡니다.
   // TODO: MemberMapper에 selectMemberByNicknameEmailTel 구현 필요 (XML 작성 완료) - 이미 하셨습니다.
   // TODO: MemberMapper에 updatePasswordByMemberNo 구현 필요 (XML 작성 완료) - 이미 하셨습니다.
   // TODO: EmailService에 임시 비밀번호 이메일 발송 메서드 (sendTemporaryPasswordEmail) 추가 및 구현 필요
       // 임시 비밀번호 생성 사용자 정의 메서드 (예시)
       private String createTemporaryPassword() {
           // UUID나 랜덤 문자열 생성 로직
           // 예: return UUID.randomUUID().toString().substring(0, 8); // 8자리 랜덤 문자열
            return "Random" + (int)(Math.random() * 10000); // 더 간단한 예시
       }


   
   

}
