const findPwTest = document.querySelector("#findPwTest");
const pwMessage = document.querySelector("#pwMessage");
console.log(findPwTest);
const memberPwConfirm = document.querySelector("#memberPwConfirm");

const memberPw = document.querySelector("#memberPw");


console.log(memberPw);

if(findPwTest != null){

 findPwTest.addEventListener("submit", e => {

  const memberEmail = document.querySelector("#memberEmail");
  const memberNickname = document.querySelector("#memberNickname");
  const memberTel =document.querySelector("#memberTel");

    if (memberNickname.value.trim().length === 0 ||
        memberEmail.value.trim().length === 0 || 
        memberTel.value.trim().length === 0) {
        e.preventDefault();
        alert("빈칸들을 다 작성해주세요.");
        return;
    }
  });

}
const checkObj = {
  "memberPw"        : false,
  "memberPwConfirm" : false, 
};

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
if(memberPw != null){
 

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
}
// --------------------------------


//6) 비밀번호 확인 유효성 검사
if(memberPwConfirm != null){

memberPwConfirm.addEventListener("input", ()=>{

  if(checkObj.memberPw){ // memberPw 가 유효한 경우

    checkPw(); // 비교하는 함수 수행
    return;
  }

  // memberPw가 유효하지 않은경우
  // memberPwconfrim 유효하지 않아야함

  checkObj.memberPwConfirm = false;

});
}

console.log(memberNo);