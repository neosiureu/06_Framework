//다음 주소 API 다루기
function execDaumPostcode() {
  new daum.Postcode({
      oncomplete: function(data) {
          // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

          // 각 주소의 노출 규칙에 따라 주소를 조합한다.
          // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
          var addr = ''; // 주소 변수
        

          //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
          if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
              addr = data.roadAddress;
          } else { // 사용자가 지번 주소를 선택했을 경우(J)
              addr = data.jibunAddress;
          }
            
          // 우편번호와 주소 정보를 해당 필드에 넣는다.
          document.getElementById('postcode').value = data.zonecode;
          document.getElementById("address").value = addr;
          // 커서를 상세주소 필드로 이동한다.
          document.getElementById("detailAddress").focus();
      }
  }).open();
}
// 주소 검색 버튼 클릭시
document.querySelector("#searchAddress").addEventListener("click" , execDaumPostcode);
                                                                              //뒤에다가 () 붙이면 
                                                                              // 페이지가 리로드됫을떄 실행됨
                                                                              // 안붙여야 클릭됫을떄 실행됨.




// -----------------------------------------------
// ******************회원 가입 유효성 검사 *************

// 필수 입력 항목의 유효성 검사 여부를 체크하기 위한 JS 객체
// - false : 해당 항목은 유효하지 않은 형식으로 작성됨.
// - true : 해당 항목은 유효한 형식으로 작성됨
const checkObj = {
  "memberEmail"     : false,
  "authKey"         : false,
  "memberPw"        : false,
  "memberPwConfirm" : false,
  "memberNickname"  : false,
  "memberTel"       : false,
  
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

// -----------------------------------------------------------
// 인증하기 버튼 클릭시
// 입력된 인증번호를 비동기 로 서버에 전달
// -> 입력된 인증번호와 발급된 인증번호가 같은지비교
//     같으면 1 , 아니면 0 반환
// 단 ,타이머가 00: 00 초가 아닐경우에만

checkAuthKeyBtn.addEventListener("click", ()=>{

  if ( min ===0 && sec === 0) {
     alert("인증번호 입력 제한시간을 초과하였습니다. 다시 발급해주세요");
    return;
    }

    if(authKey.value.length < 6 || // 인증번호가 제대로 입력안된 경우 (길이가 6미만 또는 7이상)
      authKey.value.length >=7)     
    {alert("인증번호를 정확히 입력해주세요");
      return;
    }
    
    //문제 없는경우 (제한시간, 인증번호 길이 유효시)
    // 입력받은 이메일, 인증번호로 JS 객체 생성
    const obj = {
      "email" : memberEmail.value,
      "authKey" : authKey.value
    };
      
    // 인증번호 확인용 비동기 요청 보내기
    fetch("/email/checkAuthKey" , {
      method : "POST" ,
      headers : {"Content-Type" : "application/json" },
      body : JSON.stringify(obj) // obj 라는 JS 객체를 JSON 으로 변경
    })
    .then(resp => resp.text())
    .then(result => {
       // 1 or 0
      if(result == 0){
        alert("인증번호가 일치하지않습니다");
        checkObj.authKey = false;
        return;
      }

      // 일치 할때 
      clearInterval(authTimer); // 타이머멈춤
      authKeyMessage.innerText = "인증되었습니다";
      authKeyMessage.classList.remove("error");
      authKeyMessage.classList.add("confirm");
      checkObj.authKey = true; // 인증번호 검사 여부 true 변경

    });
        
    
   

    

});
// ------------------------------------------------------------

//비밀번호 / 비밀번호 확인 유효성 검사

//1) 비밀번호 관련 요소 얻어오기

const memberPw = document.querySelector("#memberPw"); // 비밀번호

const memberPwConfirm = document.querySelector("#memberPwConfirm"); //비밀번호확인

const pwMessage = document.querySelector("#pwMessage");//비번메시지

//5) 비밀번호, 비밀번호 확인이 같은지 검사하는 함수
const checkPw = () =>{

  // 같을 경우 
  if(memberPw.value === memberPwConfirm.value){
    pwMessage.innerText = "비밀번호가 일치합니다.";
    pwMessage.classList.add("confirm");
    pwMessage.classList.remove("error");
    checkObj.memberPwConfirm = true; // 비밀번호 확인 true
    return;
  }

  // 다를 경우 
  pwMessage.innerText = "비밀번호가 일치하지 않습니다.";
  pwMessage.classList.add("error");
  pwMessage.classList.remove("confirm");
  checkObj.memberPwConfirm = false; // 비밀번호 확인 false
  


};


//2) 비밀번호 유효성 검사
memberPw.addEventListener("input", e => {

  //입력 받은 비밀번호 값
  const inputPw = e.target.value;

  //3 ) 입력되지 않은 경우

  if(inputPw.trim().length === 0){
    pwMessage.innerText = "영어,숫자,특수문자(!,@,#,-,_) 6~20글자 사이로 입력해주세요.";
    pwMessage.classList.remove("confirm" , "error");
    checkObj.memberPw = false ; // 비밀번호가 유효하지 않다고 표시
    memberPw.value = ""; // 첫글자 띄어쓰기 입력못하게 막기
    return;
  }
   
  //4) 입력 받은 비밀번호 정규식 검사
  const regExp = /^[a-zA-Z0-9!@#\-_]{6,20}$/;
  
  if (!regExp.test(inputPw)) { // 유효하지 않으면
    pwMessage.innerText ="비밀번호가 유효하지 않습니다.";
    pwMessage.classList.add("error");
    pwMessage.classList.remove("confirm");
    checkObj.memberPw = false;
    return;
  }

  // 유효한 경우
  pwMessage.innerText = "유효한 비밀번호 형식입니다.";
  pwMessage.classList.add("confirm");
  pwMessage.classList.remove("error");
  checkObj.memberPw = true;

  // 비밀번호 입력 시 비밀번호 확인란의 값과 비교하는 코드 추가
  
  // 비밀번호 확인에 값이 작성되었을때
  
  if(memberPwConfirm.value.length > 0){
    checkPw();
  }
    

});



//6) 비밀번호 확인 유효성 검사
memberPwConfirm.addEventListener("input", ()=>{

  if(checkObj.memberPw){ // memberPw 가 유효한 경우

    checkPw(); // 비교하는 함수 수행
    return;
  }

  // memberPw가 유효하지 않은경우
  // memberPwconfrim 유효하지 않아야함

  checkObj.memberPwConfirm = false;

});

// -----------------------------------------------------

// 닉네임 유효성 검사

const memberNickname = document.querySelector("#memberNickname");
const nickMessage = document.querySelector("#nickMessage");

memberNickname.addEventListener("input" , (e) =>{

  const inputNickname = e.target.value;

  //1) 입력 안한경우
  
  if(inputNickname.trim().length === 0){
    nickMessage.innerText = "한글,영어,숫자로만 2~10글자";
    nickMessage.classList.remove("confirm","error");
    checkObj.memberNickname = false;
    memberNickname.value = "";
    return;
  }

  //2) 정규식 검사
  const regExp = /^[가-힣\w\d]{2,10}$/;
  
  if(!regExp.test(inputNickname)){
    nickMessage.innerText = "유효하지 않은 닉네임 형식입니다";
    nickMessage.classList.remove("confirm");
    nickMessage.classList.add("error");
    checkObj.memberNickname = false;
    return;
  }
  
  //3) 중복 검사
  fetch("/member/checkNickname?memberNickname=" + inputNickname)
  .then(resp => resp.text())
  .then(count => {

    if(count == 1){ //중복
      nickMessage.innerText = "중복된 아이디가있습니다."
      nickMessage.classList.remove("confirm");
      nickMessage.classList.add("error");
      checkObj.memberNickname = false;
      return;
    }
    
    nickMessage.innerText = "허용가능한 아이디입니다."
    nickMessage.classList.remove("error");
    nickMessage.classList.add("confirm");
    checkObj.memberNickname = true;
 
  })
  .catch(err => console.log(err));

});

// -------------------------------------------------
// 전화번호 유효성 검사

const memberTel = document.querySelector("#memberTel");
const telMessage = document.querySelector("#telMessage");

memberTel.addEventListener("input", (e)=>{
  
  const inputTel = e.target.value;

  // 아무것도 입력안했을떄
  if(inputTel.trim().length === 0){
    telMessage.innerText = "전화번호를 입력해주세요.(- 제외)";
    telMessage.classList.remove("error","confirm");
    checkObj.memberTel = false;
    memberTel.value = "";
    return;
  }
   
  //정규식 유효성검사
  const regExp =  /^01[0-9]{1}[0-9]{3,4}[0-9]{4}$/;
  
  if(!regExp.test(inputTel)){
    telMessage.innerText = "유요하지않은 전화번호입니다.";
    telMessage.classList.remove("confirm");
    telMessage.classList.add("error");
    checkObj.memberTel = false;
    return;
  }

  // 중복 검사
  fetch("/member/checkTel?memberTel=" + inputTel )
  .then(resp => resp.text())
  .then(count => {
    
    if(count == 1){
      telMessage.innerText = "중복된 전화번호입니다.";
      telMessage.classList.remove("confirm");
      telMessage.classList.add("error");
      checkObj.memberTel = false;
      return;
    }

    telMessage.innerText = "허용가능한 전화번호입니다.";
    telMessage.classList.remove("error");
    telMessage.classList.add("confirm");
    checkObj.memberTel = true;
    
  }).catch(err => console.log(err))


});

// -----------------------------------------------------------------------------

//  회원 가입 버튼 클릭시 전체 유효성 검사 여부 확인


const signupForm = document.querySelector("#signUpForm"); // form 태그

// 회원 가입 폼 제출 시 

signupForm.addEventListener("submit" , (e) => {

  // checkObj의 저장된 값 중 
  // 하나라도 false 가 있으면 제출 X
  // for ~ in (객체 전용 향상된 for 문) 
  // for ~ of (배열 전용 향상된 for 문)

  for(let key in checkObj){// checkobj 요소의 key값을 순서대로 꺼내옴

    if(!checkObj[key]){ //현재 접근중인 checkObj[key] 의 value 가 false인경우
      
      
      let str ; // 출력할 메시지 저장할 변수

      switch(key){
        case"memberEmail" :
         str = "이메일이 유효하지 않습니다.";  break;
        case"authKey" :
         str = "이메일이 인증되지 않았습니다."; break;
        case "memberPw" :
          str = "비밀번호가 유효하지 않습니다."; break;
        case "memberPwConfirm" :
          str = " 비밀번호가 일치하지 않습니다."; break;
        case "memberNickname" :
          str = "닉네임이 유효하지 않습니다."; break;
        case "memberTel" :
          str = "전화번호가 유효하지 않습니다."; break;  
      }
      alert(str);

      document.getElementById(key).focus(); //해당 input 초점 이동

      e.preventDefault(); // form 태그 기본 이벤트 (제출) 막기

      return;
    }
 
  }

    
})


