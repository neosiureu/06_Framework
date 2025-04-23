// 쿠키에 저장된 이메일 input 창에 입력해놓기
// 로그인이 안된 경우에 수행


// 쿠키에서 매개변수로 전달받은 key와 일치하는 value 
// 얻어오고 반환하는함수
const getCookie = (key) => {
 const cookies = document.cookie; // "k=v; k=v; ... "


 //console. log(cookies);
 // cookies 에 저장된 문자열을 배열 형태로 변환
 const cookieList = cookies.split("; ") // ["k=V", "k=V"]
                    .map(el => el.split("=")); 
                    //[ ["k","v"],["k","v"] ]
                    
                    //.map 은 = 을 기준으로 key , value 로 묶어주면서 
                     // 배열형태로 반환함.

      //배열.map(함수) : 배열의 각요소를 이용해 콜백함수 수행후
      //                 결과 값으로 새로운 배열을 만들어서 반환하는 JS 내장함수
      /*
       [
       ['saveId', 'user01@kh.or.kr'] ,
       ['test','teatValue']
       ]
       현재 2차원 배열형태..
      
      */      


     // 배열 -> JS 객체로 변환 (그래야 다루기 쉬움)  

     const obj = {}; // 비어있는 객체 선언
    for(let i =0; i< cookieList.length; i++){
      const k = cookieList[i][0];
      const v = cookieList[i][1]; 
      obj[k] = v;  // obj객체에 K : V 형태로 추가
      // obj["saveId"] = 'user01@kh.or.kr'  
    }
        //console.log(obj);   
        return obj[key]; // 매개변수로 전달받은 key와
                        // obj 객체에 저장된 key가 일치하는 요소의
                         // value 값 반환.   
}



// 이메일 작성 input 태그 요소
const loginEmail =document.querySelector("#loginForm input[name='memberEmail']");

if (loginEmail != null){ //로그인 폼의 이메일 input태그가 화면상에 존재할떄 

  //쿠키 중 key 값이 "saveId"인 요소의 value 얻어오기
  const saveId = getCookie("saveId"); // 이메일 또는 undefiend
  
  // saveId값이 있을경우
  if(saveId != undefined){
     loginEmail.value = saveId; // 쿠키에서 얻어온 이메일 값을 input 요소의
                                // value에 세팅
    // 아이디 저장 체크박스에 체크해두기
    document.querySelector("input[name='saveId']").checked = true;                              
  }

}

const loginForm = document.querySelector("#loginForm");
const memberPw = document.querySelector("[name='memberPw']");

loginForm.addEventListener("submit" , (e) => {
  
  
  if(loginEmail.value.trim().length == 0){
    alert("email칸을 작성해주세요");
    e.preventDefault();
    return;
  }
  
  if(memberPw.value.trim().length == 0){
    alert("비밀번호를 작성해주세요.");
    e.preventDefault();
    return;
  }
})
  

