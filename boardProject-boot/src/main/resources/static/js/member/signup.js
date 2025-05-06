
// 다음주소 API 다루기
function execDaumPostcode() {
  new daum.Postcode({
      oncomplete: function(data) {
          // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분. (처음 화면에서 뭘 클릭했는가에 따라 어떻게 변할래)

          // 각 주소의 노출 규칙에 따라 주소를 조합한다.
          // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
          var addr = ''; // 주소 변수

          //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
          // userSelectedType이 클릭한 대상에 따라 반환하는 값이 다름
          if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
              addr = data.roadAddress; 
          } else { // 사용자가 지번 주소를 선택했을 경우(J)
              addr = data.jibunAddress;
          }


          // 우편번호와 주소 정보를 해당 필드에 넣는다.
          document.getElementById('postcode').value = data.zonecode; // html상에 넣는다
          document.getElementById("address").value = addr;
          // 커서를 상세주소 필드로 이동한다.
          document.getElementById("detailAddress").focus();
      }
  }).open();
}

// 퍼온거 끝




/*
[ * 주소 입력 ]
┌───────────────────────────────────────────────┬───────────────────────┐
│ <input id="postcode"   name="memberAddress"   │ <button id="searchAddress"│
│        type="text" placeholder="우편번호" maxlength="6"│        type="button">검색    │
└───────────────────────────────────────────────┴───────────────────────┘

[ * 도로명/지번 주소 입력 ]
┌──────────────────────────────────────────────────────────────┐
│ <input id="address"    name="memberAddress"                  │
│        type="text" placeholder="도로명/지번 주소">           │
└──────────────────────────────────────────────────────────────┘

[ * 상세 주소 입력 ]
┌──────────────────────────────────────────────────────────────┐
│ <input id="detailAddress" name="memberAddress"               │
│        type="text" placeholder="상세 주소">                  │
└──────────────────────────────────────────────────────────────┘
*/








// 인증번호 받기라는 요소를 얻어온다
const sendAuthKeyBtn = document.querySelector('#sendAuthKeyBtn'); // 첫번쨰줄 오른쪽 버튼

// 인증번호 입력 input요소

const authKey = document.querySelector('#authKey'); // 두번째줄 input창


// 인증번호 입력 후 확인 버튼을 얻어오는 요소
const checkAuthKeyBtn = document.querySelector('#checkAuthKeyBtn'); // 두번째줄 오른쪽



// 인증번호 관련 메시지 출력 span요소
const authKeyMessage = document.querySelector('#authKeyMessage'); // 화면상에 보이지 않지만 인증이 일치하는지 여부를 표시하는 메시지






// 타이머 역할을 할 setInterval함수를 저장할 변수
let authTimer;

const initMin = 4; // 타이머의 초기값 중 분을 의미
const initSec = 59; // 타이머의 초기값 중 초를 의미
const initTime = "05:00"; // 맨 처음에 화면에 띄워줄 값



// 실제 줄어드는 시간을 매번 저장할 변수 

let min = initMin;
let sec = initSec;

// 타이머가 초기화 될 때가 있어서 애초에 초기 값을 따로 써야 한다 (다시 인증번호를 발급 받을 수 있잖아) 

// -----------------


// 회원가입 유효성 검사: 
// 아이디, 인증번호, 비밀번호, 닉네임, 전화번호에 대한 유효성검사
// 필수 입력 항목의 유효성검사 여부를 체크하기 위한 객체를 JS객체꼴로 만든다



// 키가 false다 -> 해당 항목은 유효하지 않은 형식으로 작성되어 있다. 
// true로 바뀌면 해당 항목의 작성 방식이 유효하다고 인식하게 된다
const checkObj = {
  "memberEmail": false, //true일 때만 인증번호 발급 버튼을 눌럿 보낼 수 있음
  "authKey": false, // 보낸 인증번호가 일치하면 true
  "memberPw": false,
  "memberPwConfirm": false,
  "memberNickname": false,
  "memberTel": false
};


// 가입하기 버튼을 눌렀을 때 checkObj를 순회하며 전부 true일 때만 가입
// memberEmail이 이메일 형식이 맞는지 => 이메일이 중복되지 않는지 매번 체크 => 인증번호 받고 인증번호가 맞는지 확인


/* 이메일 유효성 검사 */


// 1) 이메일 유효성 검사에 사용될 요소를 얻어온다. 이메일에 대한 input + 계속 변경되는 메시지 (유효하지않은 아이디 또는 중복되는 아이디라는 메시지)
// #memberEmail #emailMessage

const memberEmail = document.querySelector('#memberEmail'); // input 부분

const emailMessage = document.querySelector('#emailMessage') // span태그로 처음에는 안 보임


// 2) email input 창이 동작할 때마다 유효성검사

memberEmail.addEventListener("input", (e) => {


  // 입력이 될 때마다 생각할 것
  // 입력이 유효하지 않아지면 타이머를 없애며 
  // 인증번호가 전송하는데 성공하더라도 이메일이 유효하지 않아지면 




  // 이메일인증 후 이메일이 변경된 경우

  checkObj.authKey = false;
  authKeyMessage.innerText = ''; // 이메일에대해 경고하는 문구
  clearInterval(authTimer);



  // 작성된 이메일 값, 즉 내용을 js단으로 얻어와야한다
  const inputEmail = e.target.value; // input타입은 value속성으로 얻어온다

  // 3) 입력된 이메일이 비어있는 경우
  if (inputEmail.trim().length === 0) {
    emailMessage.innerText = "메일을 받을 수 있는 이메일을 입력해주세요"

    // case) 중복도 안되고 이메일도 유효한데 그 유효성 검사가 통과된 이메일을 지우는 경우

    // 메시지에 색상을 추가하는 클래스가 존재한다면 전부 지우기
    emailMessage.classList.remove('confirm', 'error');
    // confirm은 초록색글씨, error는 빨간색 글씨를 주는 css 클래스

    // 이메일 유효성 역시 역으로 바꿔준다.
    checkObj.memberEmail = false;

    // 잘못입력한 띄어쓰기가 있을 경우 없앰 
    memberEmail.value = '';

    return;

  }



  // 4) 입력된 이메일이 있을 경우 정규식 검사 => 알맞은 형태로 이메일을 작성했는가 검사

  // 영어 숫자 + 특수문자중 . _ % + - 를 허용한다 
  // 이메일 형식의 경우 골뱅이 뒤애 최소 2개의 문자로 끝나야 한다.  

  const regExp = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

  // 정규표현식 test()함수
  // 입력받은 이메일이 정규식과 일치하지않는 경우를 따진다.
  // 알맞은 이메일 형태가 아닌 경우

  if (!regExp.test(inputEmail)) {
    emailMessage.innerText = "알맞은 이메일 형식으로 작성해주세요"
    emailMessage.classList.add('error'); // 글자를 빨간색으로 설정
    emailMessage.classList.remove('confirm'); // 글자의 초록색을 뺌
    checkObj.memberEmail = false; // 원래 유효했어도 유효하지 않은 이메일이었음을 기록
    return;
  }


  // 5) 여기까지 왔으면 비어있지도 않고 정규표현식도 통과했는데 해야할 것=> 중복검사

  // ajax를 이용한 비동기 요청으로 중복검사 처리 => 서버와 DB로 나가야 한다

  fetch("/member/checkEmail?memberEmail=" + inputEmail) // get방식의 쿼리스트링으로 서버로 input으로 들어온 이메일을 서버로 보낸다 => memberEmail이라는 키를 가지고 해당 주소로 간다.
    .then(resp => resp.text())
    .then(count => {
      // count라는 숫자가 1이면 중복 0이면 중복 아님

      if (count == 1) { //중복이 맞는 경우 resp에서 text 메서드로 풀어준 것이 1이라는 문자열 또는 0이라는 문자열

        // 실제 쓴 1은 숫자지만 count는 문자열 (text함수를 써서)
        // ==은 타입이 달라도 값이 같은지만 따지지만 ===은 값 뿐만 아니라 타입까지 같은지를 비교한다
        // 지금은 저 내용이 true여야 하므로 ==가 더 적절하다

        emailMessage.innerText = "이미 사용중인 이메일입니다.";
        emailMessage.classList.add('error');
        emailMessage.classList.remove('confirm');
        checkObj.memberEmail = false;
        return;
      }

      // 이메일의 중복이 없는 경우

      emailMessage.innerText = "사용가능한 이메일입니다";
      emailMessage.classList.add('confirm');
      emailMessage.classList.remove('error');
      checkObj.memberEmail = true;
    });




});





// 인증번호 받기 버튼을 클릭 시 

sendAuthKeyBtn.addEventListener("click", () => {

  // 새로운 인증번호의 발급을 원한다 => 새로 발급받은 인증번호를 다시 확인하기 전까지 
  // checkObj의 authKey부분을 false로 만든다

  checkObj.authKey = false;
  authKeyMessage.innerText = '';
  // 아직 안했지만 인증번호 발급관련 메시지를 지워야 함





  // 중복되지 않은 유효한 이메일을 입력한 경우가 아니면 
  if (!checkObj.memberEmail) {
    alert('유효한 이메일 작성 후 클릭해주세요');
    return;
  }

  // 숨어있던 타이머를 만든다


  // 클릭 시 타이머 숫자를 초기화한다. 이메일을 바꾸고 다시 인증번호 버튼을 누를 수 있다

  min = initMin;
  sec = initSec;

  // 이전에 동작하고 있던 타이머의 인터벌을 없애버린다

  clearInterval(authTimer);



  /* 

  위에거 복습:
  
  // 타이머 역할을 할 setInterval함수를 저장할 변수
let authTimer; 

const initMin = 4; // 타이머의 초기값 중 분을 의미
const initSec = 59; // 타이머의 초기값 중 초를 의미
const initTime = "05:00"; // 맨 처음에 화면에 띄워줄 값



// 실제 줄어드는 시간을 매번 저장할 변수 

let min = initMin;
let sec = initSec;
  
  */




  fetch("/email/signup", {
    method: "post",
    headers: { "Content-Type": "application/json" },
    body: memberEmail.value
  })

    // 메일은 비동기로 서버에서 보내라고 놔두고
    // 화면에서는 타이머를 시작한다

    .then(resp => resp.text())
    .then(result => {
      if (result == 1) {
        console.log('인증번호 발송 성공!');
      }

      else {
        console.log('인증번호 발송 실패');
      }

      // 이거 하는동안 서버는 인증번호를 발급하고 DB와 이 인증번호를 세트로 묶어 테이블에 한 행을 추가한다.

    });

  // 이메일을 보냈다면 성공이든 실패든 무조건 진행하는  코드
  authKeyMessage.innerText = initTime; // "05:00";
  authKeyMessage.classList.remove("confirm", "error"); //검정글씨로 돌려놓기

  alert('인증번호가 발송되었습니다.');

  // 진짜 타이머를 작동시키자 => setInterval함수를 생성
  // ms로 작성한 지연시간만큼 시간이 지날때마다 callback함수를 수행


  // * setInterval(콜백함수, 지연시간(ms)) 을 통해

  // 인증 남은 시간 출력 => 1초마다 동작하게끔


  // authTimer를 따로 변수로 만든 이유는 clearInterval을 이용하기 위함

  authTimer = setInterval(() => {

    authKeyMessage.innerText = `${addZero(min)}:${addZero(sec)}`;

    // 00:00출력 후 
    if (min == 0 && sec == 0) { //0분 0초라 만료된 상태
      checkObj.authKey = false;
      clearInterval(authTimer); // interval 함수 멈춤

      authKeyMessage.classList.add('error');
      authKeyMessage.classList.remove('confirm');
      return; // 인터벌 함수 자체를 아예 종료
    }

    // 분은 0이 아니지만 초는 0인 경우 
    // 0초를 출력하고 초는 60이되며 분은 하나를 빼야 함

    if (sec == 0) {
      sec = 60;
      min--;
    }


    sec--;

  }, 1000); //1초의 지연시간


});



// 매개변수로 전달받은 숫자가 10 미만인 경우에만 실행 앞에 0을 추가하여 반환한다
function addZero(number) {
  if (number < 10) return "0" + number;
  else return number;
}



//-----------------------------------------------------






// 인증하기 버튼 클릭 시 입력된 인증번호를 비동기요청으로 서버에 보낸다 => 입력된 인증번호와 DB에 저장된 인증번호가 같은지 비교

// 같으면 1, 아니면 0을 반환 => 단 타이머가 00:00이 아닐 때만 이를 수행
checkAuthKeyBtn.addEventListener("click", () => {
  if (min === 0 && sec === 0) {
    // 타이머가 0분 0초이면 alert로 유저에게 알려주고 끝내기
    alert('인증번호 입력 제한시간을 초과하셨습니다. 다시 발급 해주세요');
    return;
  }

  // 인증시간이 남아있을 때만 이 아래로 통과


  // 인증번호의 길이가 정확히 6이 아닐떄는 통과하지 못하게 해야함

  if (authKey.value.length < 6 || authKey.value.length >= 7) {
    alert('인증번호를 정학히 입력해주세요');
    return;
  }


  // 인증 시간도 남았고, 정확히 6자리가 입력된 경우 이 아래로 통과

  // 입력받은 이메일과 인증번호 authkey를 보내야 함=> js객체를 body로 실어서 json으로 보낸다

  const obj = {
    "email": memberEmail.value,
    "authKey": authKey.value
  } // 바디에 담을 값


  fetch("/email/checkAuthKey", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(obj)
    // 한 값만 보낼 떄는 이렇게 써도 됐었는데  => body: memberEmail.value
    // 여러 값을 js객체로 보낼때는 stringify
  })
    .then(resp => resp.text())
    .then(result => {
      // 1이나 0 이 result로 온다

      if (result == 0) {
        alert('인증번호가 일치하지 않습니다');
        checkObj.authKey = false; //이전에 통과했음에도 실패로 바꿔야 함
        return;
      }

      // 여기까지 통과하면 인증번호도 일치한다는 말

      clearInterval(authTimer); //타이머 없애기

      authKeyMessage.innerText = "인증되었습니다";
      authKeyMessage.classList.remove('error');
      authKeyMessage.classList.add('confirm');

      checkObj.authKey = true; // 인증번호 검사 여부 true로 변경




    })
})







// 비밀번호 + 비밀번호 확인 유효성검사


// 1단계: 요소 얻어오기
const memberPw = document.querySelector('#memberPw');
const memberPwConfirm = document.querySelector('#memberPwConfirm');
const pwMessage = document.querySelector('#pwMessage ')



// 5.5단계: 비밀번호와 확인이 같다는 것을 확인하는 함수


const checkPw = () => {

  if (memberPw.value === memberPwConfirm.value) {
    pwMessage.innerText = "비밀번호가 일치합니다";
    pwMessage.classList.add('confirm');
    pwMessage.classList.remove('error');
    checkObj.memberPwConfirm = true;
    return;
  }

  // 다를 경우

  pwMessage.innerText = "비밀번호가 일치하지 않습니다";
  pwMessage.classList.add('error');
  pwMessage.classList.remove('confirm');
  checkObj.memberPwConfirm = false;

};



memberPw.addEventListener("input", (e) => {

  const inputPw = e.target.value // 2단계: 현재 입력된 값 얻어오기

  // 3단계 입력 자체가 안 된 경우의 처리

  if (inputPw.trim().length === 0) {
    pwMessage.innerText = "영어,숫자,특수문자(!,@,#,-,_) 6~20글자 사이로 입력해주세요.";
    pwMessage.classList.remove('confirm', 'error');
    checkObj.memberPw = false;  // 비밀번확 유효하지 않다고 표시
    memberPw.value = ""; // 빈공간이 들어온다면 아예 막아버리는 방법. 첫 글자를 띄어쓰기하면 아예 빈문자열 해버린다
    return;
  }

  // 4단계: 입력받은 비밀번호 정규식 검사

  const regExp = /^[a-zA-Z0-9!@#_-]{6,20}$/;

  if (!regExp.test(inputPw)) // 유효하지 않을 때 거르고 return
  {
    pwMessage.innerText = "비밀번호가 유효하지 않습니다.영어,숫자,특수문자(!,@,#,-,_) 6~20글자 사이로 입력해주세요";
    pwMessage.classList.add('error');
    pwMessage.classList.remove('confirm');
    checkObj.memberPw = false;
    return;
  }

  // 5단계: 입력도 됐고 정규표현식도 통과된 상태

  pwMessage.innerText = "유효한 비밀번호 입니다";

  pwMessage.classList.add('confirm');
  pwMessage.classList.remove('error');
  checkObj.memberPw = true;


  // 비밀번호와 비밀번호확인 중 하나가 변경될 수 있다
  // 비밀번호 입력 시 비밀번호 확인란의 값과 비교하는 코드 부분 추가
  // 비밀번호확인에 하나라도 들어올 때부터만 메시지를 출력한다

  if (memberPwConfirm.value.length > 0) {
    checkPw();
  }



});


// 6단계: 비밀번호확인에 대한 유효성검사

memberPwConfirm.addEventListener("input", () => {
  if (checkObj.memberPw) { // 비밀번호가 모든 조건을 통과했을 때만 비밀번호확인을 비교한다
    checkPw(); // 비교 함수 수행
    return;
  }

  checkObj.memberPwConfirm = false;

})





const memberNickname = document.querySelector('#memberNickname');
const nickMessage = document.querySelector('#nickMessage');

memberNickname.addEventListener("input", e => {
  const inputNickName = e.target.value;

  // 1) 닉네임  입력 안 한 경우

  if (inputNickName.trim().length === 0) {
    nickMessage.innerText = "한글,영어,숫자로만 2~10글자";
    nickMessage.classList.remove('confirm', 'error');
    checkObj.memberNickname = false;
    memberNickname.value = "";
    return;
  }



  // 2) 닉네임 정규식 검시: 

  const regExp = /^[가-힣\w\d]{2,10}$/;
  if (!regExp.test(inputNickName)) {
    nickMessage.innerText = "유효하지 않은 닉네임 형식입니다";
    nickMessage.classList.add('error');
    nickMessage.classList.remove('confirm');
    checkObj.memberNickname = false;
    return;
  };



  // 입력도 했고 정규표햔식도 통과함 
  // 중복검사

  fetch("/member/checkNickName?memberNickName=" + inputNickName)
    .then(resp => resp.text())
    .then(count => {

      if (count == 1) { //중복이 있다  
        nickMessage.innerText = "이미 사용중인 닉네임입니다.";
        nickMessage.classList.add('error');
        nickMessage.classList.remove('confirm');
        checkObj.memberNickname = false;
        return;

      }


      nickMessage.innerText = "사용가능한 닉네임입니다";
      nickMessage.classList.add('confirm');
      nickMessage.classList.remove('error');
      checkObj.memberNickname = true;

    })
    .catch(err => {
      console.log(err); // 개발자도구에서 오류의 종류를 확인한다.
    })

});






const memberTel = document.querySelector('#memberTel');
const telMessage = document.querySelector('#telMessage');

memberTel.addEventListener('input', e => {

  const inputTel = e.target.value;

  if (inputTel.trim().length === 0) {
    telMessage.innerText = '전화번호를 입력해주세요.(- 제외)';
    telMessage.classList.remove('error', 'confirm');
    checkObj.memberTel = false;
    memberTel.value = "";
    return;
  }


  // 휴대폰 번호 정규 표현식
  const regExp = /^01[0-9]{1}[0-9]{3,4}[0-9]{4}$/;

  if(!regExp.test(inputTel)){
    telMessage.innerText = '유효하지 않은 전화번호 형식입니다';
    telMessage.classList.add('error');
    telMessage.classList.remove('confirm');
    checkObj.memberTel =false;
    return;
  }

  telMessage.innerText = '전화번호 형식이 유효합니다';
  telMessage.classList.add('confirm');
  telMessage.classList.remove('error');
  checkObj.memberTel =true;
  

});


/* 

[ * 주소 입력 ]
┌───────────────────────────────────────────────┬───────────────────────┐
│ <input id="postcode"   name="memberAddress"   │ <button id="searchAddress"│
│        type="text" placeholder="우편번호" maxlength="6"│        type="button">검색    │
└───────────────────────────────────────────────┴───────────────────────┘

[ * 도로명/지번 주소 입력 ]
┌──────────────────────────────────────────────────────────────┐
│ <input id="address"    name="memberAddress"                  │
│        type="text" placeholder="도로명/지번 주소">           │
└──────────────────────────────────────────────────────────────┘

[ * 상세 주소 입력 ]
┌──────────────────────────────────────────────────────────────┐
│ <input id="detailAddress" name="memberAddress"               │
│        type="text" placeholder="상세 주소">                  │
└──────────────────────────────────────────────────────────────┘


*/



// 주소 검색버튼 클릭 시 
document.querySelector("#searchAddress").addEventListener('click', execDaumPostcode);








/* 

[ * 가입하기 버튼 ]
┌──────────────────────────────────────────────────────────────┐
│ <button id="signUpBtn" type="submit">가입하기</button>         │
└──────────────────────────────────────────────────────────────┘

*/


// 최종 가입 => 회원가입 버튼 클릭 시 전체 유효성 검사 여부를 확인한다

// document.querySelector('#signUpBtn').addEventListener

// 전체 폼이 signUpForm이라는 이름인데 이것이 일어났을 때 회원가입이 일어나도록 하는 것이 더 적합하다
const signUpForm = document.querySelector('#signUpForm');

signUpForm.addEventListener("submit", e=>{
  // submit이라는 이벤트를 막아야 한다 => checkobj에 저장된 값 중 하나라도 false가 있으면 preventDefault할 것이다.

  // js객체를 전용으로 for로 돌 수 있음.  (for of문은 배열이나 리스트만 가능하다)

  for(let key in checkObj){
    // checkObj 요소의 key값을 순서대로 꺼내와 in 앞 문자에 붙임

    if(!checkObj[key]){
      // 현재 접근중인 checkObj[key]의 value값이 false인 경우

      let str =""; // 출력할 메시지를 각각 저장할 변수

      switch(key){

        case "memberEmail":
        str = "이메일이 유효하지 않습니다"; break;


        case  "authKey":
        str = "이메일이 인증되지 않았습니다"; break;


        case  "memberPw":
        str = "비밀번호가 유효하지 않습니다"; break;


        case   "memberPwConfirm":
        str = "비밀번호가 일치하지 않습니다"; break;

        case   "memberNickname":
        str = "닉네임이 유효하지 않습니다"; break;

        case "memberTel":
        str = "전화번호가 유효하지 않습니다"; break;
      }
      alert(str);

      document.getElementById(key).focus(); // 해당 input에 초점을 맞춤
      // html 요소의 아이디와 (getElementById) 모든 key들이 똑같다
  
      e.preventDefault(); // 폼 태그의 있는 submit 이벤트를 막는다
      return;
    
    }


  
  } //for문 끝
  

})




/* 
const checkObj = {
  "memberEmail": false, //true일 때만 인증번호 발급 버튼을 눌럿 보낼 수 있음
  "memberPw": false,
  "memberPwConfirm": false,
  "memberNickname": false,
  "memberTel": false,
  "authKey": false // 보낸 인증번호가 일치하면 true
};

*/








/*
[ * 아이디(이메일) ]
┌──────────────────────────────────────────────────────────────┬──────────────────────────┐
│ <input  id="memberEmail" name="memberEmail"                  │ <button id="sendAuthKeyBtn" │
│         type="text" placeholder="아이디(이메일)" maxlength="30"│        type="button">인증번호 받기 │
└──────────────────────────────────────────────────────────────┴──────────────────────────┘
<span id="emailMessage">메일을 받을 수 있는 이메일을 입력해주세요.</span>


[ * 인증번호 ]
┌──────────────────────────────────────────────────────────────┬────────────────────────┐
│ <input  id="authKey"      name="authKey"                     │ <button id="checkAuthKeyBtn"│
│         type="text" placeholder="인증번호 입력" maxlength="6"  │        type="button">인증하기 │
└──────────────────────────────────────────────────────────────┴────────────────────────┘
<span id="authKeyMessage"></span>


[ * 비밀번호 ]
┌──────────────────────────────────────────────────────────────┐
│ <input  id="memberPw"    name="memberPw"                    │
│         type="password" placeholder="비밀번호" maxlength="20">│
└──────────────────────────────────────────────────────────────┘


[ * 비밀번호 확인 ]
┌──────────────────────────────────────────────────────────────┐
│ <input  id="memberPwConfirm" name="memberPwConfirm"         │
│         type="password" placeholder="비밀번호 확인" maxlength="20">│
└──────────────────────────────────────────────────────────────┘
<span id="pwMessage">영어,숫자,특수문자(!,@,#,-,_) 6~20글자 사이로 입력해주세요.</span>


[ * 닉네임 ]
┌──────────────────────────────────────────────────────────────┐
│ <input id="memberNickname" name="memberNickname"            │
│        type="text" placeholder="닉네임" maxlength="10">      │
└──────────────────────────────────────────────────────────────┘
<span id="nickMessage">한글,영어,숫자로만 2~10글자</span>


[ * 전화번호 ]
┌──────────────────────────────────────────────────────────────┐
│ <input id="memberTel"     name="memberTel"                  │
│        type="text" placeholder="(- 없이 숫자만 입력)" maxlength="11">│
└──────────────────────────────────────────────────────────────┘
<span id="telMessage">전화번호를 입력해주세요.(- 제외)</span>


[ * 주소 입력 ]
┌───────────────────────────────────────────────┬───────────────────────┐
│ <input id="postcode"   name="memberAddress"   │ <button id="searchAddress"│
│        type="text" placeholder="우편번호" maxlength="6"│        type="button">검색    │
└───────────────────────────────────────────────┴───────────────────────┘

[ * 도로명/지번 주소 입력 ]
┌──────────────────────────────────────────────────────────────┐
│ <input id="address"    name="memberAddress"                  │
│        type="text" placeholder="도로명/지번 주소">           │
└──────────────────────────────────────────────────────────────┘

[ * 상세 주소 입력 ]
┌──────────────────────────────────────────────────────────────┐
│ <input id="detailAddress" name="memberAddress"               │
│        type="text" placeholder="상세 주소">                  │
└──────────────────────────────────────────────────────────────┘


[ * 가입하기 버튼 ]
┌──────────────────────────────────────────────────────────────┐
│ <button id="signUpBtn" type="submit">가입하기</button>         │
└──────────────────────────────────────────────────────────────┘

*/