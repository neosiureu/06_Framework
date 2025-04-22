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
  "memberPw": false,
  "memberPwConfirm": false,
  "memberNickname": false,
  "memberTel": false,
  "authKey": false // 보낸 인증번호가 일치하면 true
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

  checkObj.authKey=false;
  authKeyMessage.innerText=''; // 이메일에대해 경고하는 문구
  clearInterval(authTimer);

  /*

  [ * 아이디(이메일) ]
┌──────────────────────────────┬──────────────┐
│ <input id="memberEmail" name="memberEmail"> │ <button id="sendAuthKeyBtn">인증번호 받기</button> │
└──────────────────────────────┴──────────────┘

<span id="emailMessage">메일을 받을 수 있는 이메일을 입력해주세요</span>



[ * 인증번호 ]
┌──────────────────────────────┬──────────────┐
│ <input id="authKey" name="authKey">          │ <button id="checkAuthKeyBtn">인증하기</button> │
└──────────────────────────────┴──────────────┘

<span id="authKeyMessage"></span>
  

*/

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

  if(!regExp.test(inputEmail)){
    emailMessage.innerText= "알맞은 이메일 형식으로 작성해주세요"
    emailMessage.classList.add('error'); // 글자를 빨간색으로 설정
    emailMessage.classList.remove('confirm'); // 글자의 초록색을 뺌
    checkObj.memberEmail = false; // 원래 유효했어도 유효하지 않은 이메일이었음을 기록
    return;
  }


  // 5) 여기까지 왔으면 비어있지도 않고 정규표현식도 통과했는데 => 중복검사

  // ajax를 이용한 비동기 요청으로 중복검사 처리 => 서버와 DB로 나가야 한다

  fetch("/member/checkEmail?memberEmail="+inputEmail) // get방식의 쿼리스트링으로 서버로 input으로 들어온 이메일을 서버로 보낸다 => memberEmail이라는 키를 가지고 해당 주소로 간다.
  .then(resp => resp.text())
  .then(count => {
    // count라는 숫자가 1이면 중복 0이면 중복 아님
    
    if(count ==1){ //중복이 맞는 경우 resp에서 text 메서드로 풀어준 것이 1이라는 문자열 또는 0이라는 문자열

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

    emailMessage.innerText= "사용가능한 이메일입니다";
    emailMessage.classList.add('confirm');
    emailMessage.classList.remove('error');
    checkObj.memberEmail = true;
  });




});





// 인증번호 받기 버튼을 클릭 시 

sendAuthKeyBtn.addEventListener("click", ()=>{

  // 새로운 인증번호의 발급을 원한다 => 새로 발급받은 인증번호를 다시 확인하기 전까지 
  // checkObj의 authKey부분을 false로 만든다

  checkObj.authKey = false;
  authKeyMessage.innerText='';
  // 아직 안했지만 인증번호 발급관련 메시지를 지워야 함





  // 중복되지 않은 유효한 이메일을 입력한 경우가 아니면 
  if(!checkObj.memberEmail){
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
  
  // 타이머 역할을 할 setInterval함수를 저장할 변수
let authTimer; 

const initMin = 4; // 타이머의 초기값 중 분을 의미
const initSec = 59; // 타이머의 초기값 중 초를 의미
const initTime = "05:00"; // 맨 처음에 화면에 띄워줄 값



// 실제 줄어드는 시간을 매번 저장할 변수 

let min = initMin;
let sec = initSec;
  
  */




fetch("/email/signup",{
  method: "post",
  headers: {"Content-Type": "application/json"},
  body : memberEmail.value
})

// 메일은 비동기로 서버에서 보내라고 놔두고
// 화면에서는 타이머를 시작한다

.then(resp =>resp.text())
.then(result => {
  if(result==1){
    console.log('인증번호 발송 성공!');
  }

  else{
    console.log('인증번호 발송 실패');
  }

  // 이거 하는동안 서버는 인증번호를 발급하고 DB와 이 인증번호를 세트로 묶어 테이블에 한 행을 추가한다.

});

  // 성공이든 실패든 무조건 진행하는  코드
  authKeyMessage.innerText = initTime; // "05:00";
  authKeyMessage.classList.remove("confirm","error"); //검정글씨로 돌려놓기

  alert('인증번호가 발송되었습니다.');

  // 진짜 타이머를 작동시키자 => setInterval함수를 생성
  // ms로 작성한 지연시간만큼 시간이 지날때마다 callback함수를 수행


  // * setInterval(콜백함수, 지연시간(ms)) 을 통해

  // 인증 남은 시간 출력 => 1초마다 동작하게끔


  // authTimer를 따로 변수로 만든 이유는 clearInterval을 이용하기 위함

  authTimer = setInterval( () =>{ 
    
    authKeyMessage.innerText = `${addZero(min)}:${addZero(sec)}`;

    // 00:00출력 후 
    if(min==0 && sec ==0){ //0분 0초라 만료된 상태
      checkObj.authKey = false;
      clearInterval(authTimer); // interval 함수 멈춤

      authKeyMessage.classList.add('error');
      authKeyMessage.classList.remove('confirm');
      return; // 인터벌 함수 자체를 아예 종료
    }

    // 분은 0이 아니지만 초는 0인 경우 
    // 0초를 출력하고 초는 60이되며 분은 하나를 빼야 함

    if(sec==0){
      sec=60;
      min--;
    }


    sec--;

  },1000); //1초의 지연시간


});



// 매개변수로 전달받은 숫자가 10 미만인 경우에만 실행 앞에 0을 추가하여 반환한다
function addZero(number){
  if(number<10) return "0"+number;
  else return number;
}