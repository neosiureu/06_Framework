/* 회원 정보 수정 페이지 */
const updateInfo = document.querySelector("#updateInfo"); // form 태그

// #updateInfo 요소가 존재 할 때만 수행
if(updateInfo != null) {

    // form 제출 시
    updateInfo.addEventListener("submit", async e => {

      // 가장 먼저 기본 이벤트 동작 중단 .
       e.preventDefault();

        const memberNickname = document.querySelector("#memberNickname");
        const memberTel = document.querySelector("#memberTel");
        const memberAddress = document.querySelectorAll("[name='memberAddress']");

        // 닉네임 유효성 검사
        if(memberNickname.value.trim().length === 0) {
            alert("닉네임을 입력해주세요");
           // e.preventDefault(); // 제출 막기
            return;
        }

        // 닉네임 정규식에 맞지 않으면
        let regExp = /^[가-힣\w\d]{2,10}$/;
        if( !regExp.test(memberNickname.value)) {
            alert("닉네임이 유효하지 않습니다.");
           // e.preventDefault(); // 제출 막기
            return;
        }

        // 기존 닉네임이 저장되어있는 요소의 value 값 얻어오기 hidden 되어있음
        const currentNickname = document.querySelector("#currentNickname").value;

        // 기존 닉네임과 새로 입력된 닉네임이 다르면 중복검사 시도하기
        // -> 같으면 변경된적 없다 -> 중복검사 진행 안함

        if(currentNickname !== memberNickname.value){
          
          //비동기 요청(fetch() API 이용)
          // async / await 사용
          // async : 비동기 함수를 만들때 사용되는 키워드("이 함수 내에는 시간이 걸리는 작업 이있따!")
          // await : 비동기 작업의 결과를 기다릴 떄 사용 키워드
          // -> !!! 반드시 async 함수 안에서만 사용 가능 !!!
          // -> "이 작업이 끝날때까지 기다려주세요.."
          const resp = await fetch("/member/checkNickname?memberNickname="+ memberNickname.value);
          const count = await resp.text();

          if(count == 1 ){
            alert("이미 사용중인 닉네임 입니다.");
            //e.preventDefault();
            return;
          }

          
        }



        // *********** 닉네임 중복검사는 개별적으로 해보기 ***********



        // ***********************************************************

        // 전화번호 유효성 검사
        if(memberTel.value.trim().length === 0) {
            alert("전화번호를 입력해 주세요");
            //e.preventDefault();
            return;
        }

        // 전화번호 정규식에 맞지 않으면
        regExp = /^01[0-9]{1}[0-9]{3,4}[0-9]{4}$/;
        if( !regExp.test(memberTel.value)) {
            alert("전화번호가 유효하지 않습니다");
            //e.preventDefault();
            return;
        }


        // 주소 유효성 검사
        // 입력을 안하면 전부 안해야되고
        // 입력하면 전부 해야된다

        const addr0 = memberAddress[0].value.trim().length == 0; // t/f
        const addr1 = memberAddress[1].value.trim().length == 0; // t/f
        const addr2 = memberAddress[2].value.trim().length == 0; // t/f

        // 모두 true 인 경우만 true 저장
        const result1 = addr0 && addr1 && addr2; // 아무것도 입력 X

        // 모두 false 인 경우만 true 저장
        const result2 = !(addr0 || addr1 || addr2); // 모두 다 입력

        // 모두 입력 또는 모두 미입력이 아니면
        if( !(result1 || result2) ) {
            alert("주소를 모두 작성 또는 미작성 해주세요");
           // e.preventDefault();
           return;
        }
        
        // 위의 모든 검증을 통과했을때만 폼제출
        updateInfo.submit();

        /*
        submit 이벤트 핸들러 함수 내에서 fetch를 이용한 비동기 함수를 실행중 일 때
        e.preventDefault()가 비동기 함수 이후에 호출되면 이미 form 이 submit 되어버린
        다음일수 있음..
        -> 비동기 작업 중에 form 의 기본 제출 동작 일어나는 가능성이 생김!

        */
        
    });
}



// ------------------------------------------

/* 비밀번호 수정 */

// 비밀번호 변경 form 태그
const changePw = document.querySelector("#changePw");

if(changePw != null) {
    // 제출 되었을 때
    changePw.addEventListener("submit", e => {

        const currentPw = document.querySelector("#currentPw");
        const newPw = document.querySelector("#newPw");
        const newPwConfirm = document.querySelector("#newPwConfirm");

        // - 값을 모두 입력했는가

        let str; // undefined 상태
        if( currentPw.value.trim().length == 0 ) str = "현재 비밀번호를 입력해주세요";
        else if( newPw.value.trim().length == 0 ) str = "새 비밀번호를 입력해주세요";
        else if( newPwConfirm.value.trim().length == 0 ) str = "새 비밀번호 확인을 입력해주세요";

        if(str != undefined) { // str에 값이 대입됨 == if 중 하나 실행됨
            alert(str);
            e.preventDefault();
            return;
        }

        // 새 비밀번호 정규식
        const regExp = /^[a-zA-Z0-9!@#_-]{6,20}$/;

        if( !regExp.test(newPw.value) ) {
            alert("새 비밀번호가 유효하지 않습니다");
            e.preventDefault();
            return;
        }

        // 새 비밀번호 == 새 비밀번호 확인
        if( newPw.value != newPwConfirm.value ) {
            alert("새 비밀번호가 일치하지 않습니다");
            e.preventDefault();
            return;
        } 
    });
};

// -------------------------------------
/* 탈퇴 유효성 검사 */

// 탈퇴 form 태그
const secession = document.querySelector("#secession");

if(secession != null) {

    secession.addEventListener("submit", e => {

        const memberPw = document.querySelector("#memberPw");
        const agree = document.querySelector("#agree");

        // - 비밀번호 입력 되었는지 확인
        if(memberPw.value.trim().length == 0) {
            alert("비밀번호를 입력해주세요.");
            e.preventDefault(); // 제출막기
            return;
        }

        // 약관 동의 체크 확인
        // checkbox 또는 radio checked 속성
        // - checked -> 체크 시 true, 미체크시 false 반환

        if(!agree.checked) { // 체크 안됐을 때
            alert("약관에 동의해주세요");
            e.preventDefault();
            return;
        }

        // 정말 탈퇴? 물어보기
        if( !confirm("정말 탈퇴 하시겠습니까?") ) {
            alert("취소 되었습니다.");
            e.preventDefault();
            return;
        }
    });
}



// -------------------------------------------------------
// 이미지 업로드 구간

/*  [input type="file" 사용 시 유의 사항]

  1. 파일 선택 후 취소를 누르면 
    선택한 파일이 사라진다  (value == '')

  2. value로 대입할 수 있는 값은  '' (빈칸)만 가능하다

  3. 선택된 파일 정보를 저장하는 속성은
    value가 아니라 files이다
*/

// 요소 참조
const profileForm = document.getElementById("profile");  // 프로필 form
const profileImg = document.getElementById("profileImg");  // 미리보기 이미지 img
const imageInput = document.getElementById("imageInput");  // 이미지 파일 선택 input
const deleteImage = document.getElementById("deleteImage");  // 이미지 삭제 버튼
const MAX_SIZE = 1024 * 1024 * 5;  // 최대 파일 크기 설정 (5MB)