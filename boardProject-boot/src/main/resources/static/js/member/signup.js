// -----------------------------------------------
// ******************회원 가입 유효성 검사 *************

// 필수 입력 항목의 유효성 검사 여부를 체크하기 위한 JS 객체
// - false : 해당 항목은 유효하지 않은 형식으로 작성됨.
// - true : 해당 항목은 유효한 형식으로 작성됨
const checkObj = {
  "memberEmail"     : false,
  "memberPw"        : false,
  "memberPwConfirm" : false,
  "memberNickname"  : false,
  "memberTel"       : false,
  "authKey"         : false
};
  


//인증번호 받기 버튼 요소 
const sendAuthKeyBtn = document.querySelector("#sendAuthKeyBtn");

//인증번호 입력 input 요소
const authKey = document.querySelector("#authKey");

// 인증번호 입력후 확인버튼 요소
const checkAuthKeyBtn = document.querySelector("#checkAuthKeyBtn");

//인증 번호 관련 메시지 출력 span 요소
const authKeyMessage = document.querySelector('#authKeyMessage');

let authTimer; // 타이머 역할을 할  setInertval 함수를 저장할 변수

const initMin = 4; // 타이머 초기값 (분)
const initSec = 59; // 타이머 초기값 (초)
const initTime = "05:00";

// 실제 줄어드는 시간을 저장할 변수
let min = initMin;
let sec = initSec;


// -----------------------------

// 이메일 유효성 검사
// 1) 이메일 유효성 검사에 사용될 요소 얻어오기
const memberEmail = document.querySelector("#memberEmail");// input
// 이메일 유효 메시지 띄어줄 요소
const emailMessage = document.querySelector("#emailMessage"); //span

//2) 이메일이 입력(input)될 때마다 유효성 검사 수행
memberEmail.addEventListener("input", (e) => {

  // 이메일 인증 후 이메일이 변경된 경우
  checkObj.authKey = false;
  authKeyMessage.innerText = "";
  clearInterval(authTimer);

  // 작성된 이메일 값 얻어오기
  const inputEmail = e.target.value;

  //3) 입력된 이메일이 없을경우 
  if(inputEmail.trim().length === 0){
    emailMessage.innerText = "메일을 받을 수 있는 이메일을 입력해주세요.";

    //메시지에 색상을 추가하는 클래스 모두 제거
    emailMessage.classList.remove('comfirm','error');

    //이메일 유효성 검사여부를 false 변경
    checkObj.memberEmail = false;
  
    // 잘못 입력한 띄어쓰기가 있을경우 없앰
    memberEmail.value="";

    return;
  }

  //4) 입력된 이메일 있을 경우 정규식 검사
  //      (알맞ㅇ느 형태로 이메일을 작성했는가검사)
  const regExp = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
  // 영어, 숫자, 특수문자(. _ % + - ) 를 허용
  // 이메일 형식으로 작성
  // 도메인 부분은 최소 2개의 문자로 끝나야함 (.com, .net, .org, .kr)

  // 입력받은 이메일이 정규식과 일치하지 않는경우
  // == 알맞은 이메일 형태가 아닌경우
  if( !regExp.test(inputEmail) ){
    emailMessage.innerText = "알맞은 이메일형식으로 작성해주세요.";
    emailMessage.classList.add("error"); // 글자를 빨간색으로 변경
    emailMessage.classList.remove("confirm"); // 초록색 글씨 제거
    checkObj.memberEmail = false; //유효하지 않은 이메일임을 기록
    return;
  }
  
  //5) 유효한 이메일 형식은 경우 중복 검사 수행
  // 비동기 ( ajax)
  fetch("/member/checkEmail?memberEmail="+inputEmail)
  .then(resp => resp.text())
  .then(count => {
    // count : 1이면 중복, 0이면 중복 X
    // == : 값이 같은지                   ex) "1" == 1 -> true
    // === : 값과 타입까지 같은지          ex) "1" === 1 -> false
    
    if(count == 1){// 중복
      emailMessage.innerText = "이미 사용중인 이메일 입니다";
      emailMessage.classList.add("error")  
      emailMessage.classList.remove("confirm");
      checkObj.memberEmail = false;
      return;
    }

    // 중복 X 인경우
    emailMessage.innerText = "사용가능한 이메일 입니다.";
    emailMessage.classList.add("confirm");
    emailMessage.classList.remove("error");
    checkObj.memberEmail = true; // 유효한 이메일 

  })
  
});

// 인증번호 받기 버튼 클릭 시 
sendAuthKeyBtn.addEventListener("click", () => {

  //새로운 인증번호 발급 원하는 것이기 때문에 
  // 새로운 발급 받은 인증번호 확인 checkObj.authKey는 false
  checkObj.authKey = false;
  authKeyMessage.innerText ="";
  // 인증번호 발급관련 메시지 지우기

  //중복되지 않은 유효한 이메일 입력한 경우가 아니면 
  if(!checkObj.memberEmail ){
    alert("유효한 이메일 작성 후 클릭해주세요");
    return;
  }

  //클릭 시 타이머 숫자 초기화
  min = initMin;
  sec = initSec;
  
  // 이전 동작중인 인터벌 클리어 (없애기)
  clearInterval(authTimer);

  //****************************************** */
  // 비동기로 서버에서 메일 보내기
  fetch("/email/signup",{
    method : "post",
    headers : {"Content-type" : "application/json"},
    body : memberEmail.value
  })
  .then (resp => resp.text())
  .then (result => {
    if(result == 1 ){
      console.log("인증 번호 발송 성공");
    }else{
      console.log("인증 번호 발송 실패");
    }
  });

  // *********************************************
  // 메일은 비동기로 서버에서 보내라고 놔두고
  // 화면에서는 타이머 시작하기
  authKeyMessage.innerText = initTime;
  authKeyMessage.classList.remove("confirm","error");

  alert("인증번호가 발송되었습니다.");

  // setInterval(콜백함수, 지연시간(ms))
  // - 지연시간(ms)만큼 시간이 지날때마다 함수 수행

  // 인증 시간 출력 (1초 마다 동작)
  authTimer = setInterval( () => {
    authKeyMessage.innerText = `${addZero(min)}:${addZero(sec)}`;
    
    // 0 분 0 초인 경우 ("00 :00 출력 후")
    if(min == 0 && sec == 0){
      checkObj.authKey = false; // 인증못함
      clearInterval(authTimer); // interval 멈춤
      authKey.classList.add("error");
      authKey.classList.remove("confirm");
      return;
    }
    
    // 0 초인 경우 ()
    if(sec == 0){
      sec = 60;
      min --;
    }

    sec--; // 1초 감소
    
  }, 1000); // 1초 지연시간


});

//매개변수 전달받은 숫자가 10 미만인 경우 (한자리) 앞에 0 붙어서 반환
function addZero (number) {

  if (number < 10) return "0" + number;
  else             return number; 
  
};





