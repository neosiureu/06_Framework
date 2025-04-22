// console.log('잘 연결됨');

// 쿠키에 저장된 이메일을 input창에 입력 
// 
// => 로그인이 안된 경우에 수행해야 한다 (로그인창 자체가 )

// 먼저 이메일 작성 자리 input태그 요소부터 


// 쿠키에서 매개변수로 전달받은 key와 일치하는 value를 얻어와 반환하는 함수
const getCookie = (key) => {
  const cookies = document.cookie; // 현재 등록된 쿠키를 "키"="값" 형태로 문자열로 만들어 준다. 가령 test=test01; saveId=user01@kh.or.kr; 와 같은 식으로

  // console.log(cookies);
  // cookies에 저장된 문자열을 배열 형태로 일단 변환

  // 배열.map(함수): 배열의 각 요소를 이용해 콜백함수 수행 후 결과 값으로 새로운 배열을 만들어 반환하는 JS내장함수


  const cookieList = cookies.split("; ").map(el => el.split("="));  // 띄어쓰기 세미콜론을 기준으로 쪼개어 배열 형태로 반환 ["키"="값", "키"="값"]과 같이 나타남 

  console.log(cookieList)

  //  [ ['test' = 'test01'] , ['saveId'= 'user01@kh.or.kr'] ]과 같은 이차원배열의 형태
  // 배열.map(콜백함수) => 대상인 배열을 순회하며 첫 요소인 "K"="V"를 쪼개서 ["K", "V"]로 바꾼다. 





  // 이 2차원배열의 형태를 JS객체형태로 바꾸기로 한다 => 다루기 쉬워 짐

  /*

[ ['saveId', 'user@01@kh.or.kr'],
['test', 'test01']]
  => JS객체로 변환

  */

  const obj = {};  // 빈 JS객체 => for문을 통해 js를 채운다

  for (let i = 0; i < cookieList.length; i++) {
    const k = /* 현재 접근중인 요소의 키 값 */ cookieList[i][0];
    const v = /* 현재 접근중인 요소의 벨류 값 */ cookieList[i][1];
    obj[k] = v; //obj라는 JS객체에 k:v형태로 추가하려면 그냥 이렇게 하면 됨
    // 즉 obj['saveId'] =  'user@01@kh.or.kr'
    // obj['test'] =  'test01'
  }

  console.log(obj);

  return obj[key]; // 매개변수로 전달받은 key에 대해 
  // obj객체에 저장된 key가 일치하는 요소의 벨류에 해당하는 값이 반환 됨.

  // 만약 쿠키에 없는 키를 반환하면 undefined객체가 반환 됨

}



// getCookie(); 
// 가령 saveId=user01@kh.or.kr와 같은 식으로 저장 되어있음

const loginEmail = document.querySelector("#loginForm input[name='memberEmail']");
const loginPw = document.querySelector("#loginForm input[name='memberPw']");

//전역변수로 선언되었기 때문에 로그인창이 안 보일 때 랜더링이 되지 않는다. 
// 따라서 null임에도 접근하려하면 오류가 난다 => nullPointerExcetion 방지

if (loginEmail != null) {
  // 로그인창의 이메일 input태그가 화면상에 존재할 때 이 코드들을 수행하겠다

  // 쿠키 중 key 값이 saveId인 요소의 value, 즉 user의 이메일을 얻어온다
  const saveId = getCookie("saveId");

  if (saveId != undefined) {
    loginEmail.value = saveId; // 쿠키에서 얻어온 이메일 값을 input요소 value에 세팅. 
    // 즉 자동으로 아이디를 기억해두겠다는 의미


    // 아이디 저장 체크박스에 계속 체크된 상태로 두기
    document.querySelector("input[name='saveId']").checked = true;
  }


  const loginner = document.querySelector('.loginner'); 


  loginner.addEventListener('click',(e) =>{
    if (loginEmail.value.trim().length === 0) {
      alert('아이디는 비어있을 수 없습니다');
      e.preventDefault();
      
    }
  
  
    if (loginPw.value.trim().length === 0) {
      alert('비밀번호는 비어있을 수 없습니다');
      e.preventDefault();
    }
  })



}


