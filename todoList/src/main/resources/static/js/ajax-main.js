console.log('ajax-main.js loaded')


// HTML상에서 얻어올 요소를 다 얻어와

// 1) 할일 개수 요소

const totalCount = document.querySelector('#totalCount')


//2) 완료 관련 요소
const completeCount = document.querySelector("#completeCount");

// 2.5) 새로고침 관련요소
const reloadBtn = document.querySelector("#reloadBtn");



// 3) 할 일 추가 관련 요소
const todoTitle = document.querySelector("#todoTitle");
const todoContent = document.querySelector("#todoContent");
const addBtn = document.querySelector("#addBtn");


// 4) 할 일 목록 조회 관련 요소
const tbody = document.querySelector("#tbody");


// 5) 할 일 상세 조회 관련 요소
const popupLayer = document.querySelector("#popupLayer");
const popupTodoNo = document.querySelector("#popupTodoNo");
const popupTodoTitle = document.querySelector("#popupTodoTitle");
const popupComplete = document.querySelector("#popupComplete");
const popupRegDate = document.querySelector("#popupRegDate");
const popupTodoContent = document.querySelector("#popupTodoContent");
const popupClose = document.querySelector("#popupClose");



// 6)  상세 조회 팝업레이어 관련 버튼 요소
const changeComplete = document.querySelector("#changeComplete");
const updateView = document.querySelector("#updateView");
const deleteBtn = document.querySelector("#deleteBtn");


// 7) 수정 레이어 관련 요소
const updateLayer = document.querySelector("#updateLayer");
const updateTitle = document.querySelector("#updateTitle");
const updateContent = document.querySelector("#updateContent");
const updateBtn = document.querySelector("#updateBtn");
const updateCancel = document.querySelector("#updateCancel");








/*
fetch() API
비동기 요청을 수행하는 최신 JS API중 하나

- Promise는 비동기 작업의 결과를 처리하는 방법 중 하나로
어떤 결과가 올지 모르지만 (성패에 대한 응답 처리는 잘 몰라도) 반드시 결과는 보내주겠다는 약속.


Promise는 비동기 작업이 맞이할 완료 또는 실패와 그 결과값을 나타내고 있는 객체이다.
비동기 작업이 완료되었을 때 실행할 콜백함수를 지정하고, 해당 작업의 성패 여부를 처리할 수 있도록 함




Promise 객체의 세가지 상태를 가짐

1) Pending (대기 중): 비동기 요청이 가긴 갔지만 서버로부터 응답을 대기 중

2) Fullfilled (이행 됨): 비동기 작업이 성공적으로 완료된 상태

3) Rejected (거부 됨): 비동기 작업이 실패한 상태. 서버로부터 거부당한 것


*/

// 전체 todo 개수 (DB에서 비동기 요청보내서)조회 및 출력하기

function getTotalCount() { // 함수의 정의

  // 비동기로 서버에 전체 todo 개수를 조회하는 요청을 보낸다
  // 이 때 fetch() API를 이용한다


  fetch("/ajax/totalCount")
    // /ajax/totalCount라는 컨트롤러가 있는 서버로 get방식 요청을 보내겠다
    // fetch한줄을 통해 서버에서 응답을 받으면 response라는 매개변수로 넘어오는데 응답을 text형식으로 변환하는 콜백함수임
    // 매개변수 response => 서버가 클라이언트에게 돌려준 비동기 요청에 대한 응답이 담긴 객체
    .then((response) => {
      console.log(response)
      return response.text()
    }) // 응답 데이터를 문자 또는 숫자 형태로 변환한 결과를 가지는 promise 객체를 반환한다. 다만 서버에서 온 것이 단일 값이기 때문에 text라는 함수로 소환할 수 있다
    // response.text()라 하면 아직 그 개수를 알 수 없음. promise안에 담긴 상태이기 떄문에 상자에 담겨있는 셈
    // 첫 요청에 대한 응답 데이터를 가지고 {}를 한다. 
    // 주로 return으로 response.text() response.json()
    .then((result) => {
      console.log(result)

      totalCount.innerText = result;
    })
  // 첫번째 then에서 return한 값이 두번째 then으로 들어간다. 두번째 then은 첫번쨰 return, 즉 변환된 데이터를 활용되는 역할
  // 두번째 then은 첫번째 콜백함수가 완료된 후 호출되는 콜백함수 => 변환된 텍스트 데이터인 result를 받아서 단순히 콘솔에 출력하는 로직
  // 매개변수 result => 첫번째 콜백함수에서 반환된 Promise객체의 PromiseResult값 
  // result 매개변수로 받아서 처리
}



// 완료된 할 일 개수 조회 및 출력하는 함수
function getCompleteCount() {
  fetch("/ajax/completeCount").then(response => response.text())
    .then(result => {
      // id가 completeCount인 요소에 내용으로 얻어온 result값을 출력
      completeCount.innerText = result;
    })
}



// 새로고침 버튼이 클릭되었을 때 
reloadBtn.addEventListener('click', () => {
  getTotalCount();
  getCompleteCount();
  // 전체 목록 함수 호출 예정 => 완료
  selectTodoList();
});


// 할일 추가 버튼 클릭 시 동작 
addBtn.addEventListener('click', () => {
  if (todoTitle.value.trim().length === 0 || todoContent.value.trim().length === 0) {
    alert('제목이나 내용은 비어있을 수 없습니다');
    return;
  }

  // Post방식으로 fetch요청을 보낼 수 있다.




  // 요청주소: "/ajax/add"
  // 데이터 전달방식: (method): "post"
  // 전달 데이터: todoTitle의 값, todoContent값

  // JS와 Java (Spring)가 데이터를 주고받으려면 동일한 타입을 사용해야 하는데 이를 묶어서 보낼 수 있다

  // { "String" : "String", 
  // "age" : 20 
  // "skill" : ["javaScript", 15.2]
  // }

  // 이를 이르러 JSON이라 한다 (Java Script Object Notation)


  // js객체로 일단 todoTitle과 todoContent를 포장하고 나중에 json으로 보냄

  const param = {
    "todoTitle": todoTitle.value,
    "todoContent": todoContent.value
  }

  fetch("/ajax/addTodo", {
    // 옵션 자체도 키와 값
    method: "POST", // 방식이 get이 아닌 post
    headers: { "Content-Type": "application/json" }, // 요청 데이터의 형식을 서버로 보낼 때 JSON으로 지정
    body: JSON.stringify(param) // param자체를 JSON으로 변화시킴. JSON은 String
  })
    .then(response => response.text()) // 반환된 값을 text로 반환
    .then(result => {
      // 첫 then에서 반환한 값을 result에 저장함

      console.log(result);

      if (result > 0) {
        alert('추가 성공!')
        todoTitle.value = "";
        todoContent.value = "";
        getTotalCount(); // 할일이 추가되었으므로 전체 추가하는 함수 재호출
        // 전체 목록 함수 호출 예정 => 완료
        selectTodoList();

      }


      else {
        alert('추가 실패')
      }
    });

});


// 비동기로 함수 전체 목록을 조회하는 함수 작성

const selectTodoList = () => {

  fetch("/ajax/selectList")
    .then(resp => resp.json()) // <- 이 부분에서
    .then(todoList => {
      // todoList는 첫 then에서 resp.text를 했냐 resp.JSON을 했냐에 따라 
      // 단순 text가 될 수도 있고 JS Object 객체처럼 넘어올 수 있다

      // 만약 resp.text()를 사용했다면 문자열 형태로 JSON의 내용이 알아보지 못하게 만들어졌을 것이다.
      // 
      // 
      // 이때는 JSON.parse(String)를 이용하여 JS Object타입으로 변환이 가능하다.
      // Strng => JS Object

      // 반대로 JSON.stringify(JS Object) 
      // JS Object => String



      console.log(todoList);


      /////////////////////////////////////////////////////////////////////////////


      // 기존에 출력되어있던 할일 목록은 사라져야 한다


      tbody.innerHTML = "";




      // tbody에 tr/td 요소를 생성하여 내용을 삽입

      for (let todo of todoList) {
        // of가 자바에서 :으로 생각하면 됨

        // tr태그 생성
        const tr = document.createElement("tr"); // <tr></tr>
        // JS객체에 존재하는 key를 모아둔 배열을 만든다

        /* complete regDate todoNo todoTitle*/


        const arr = ['todoNo', 'todoTitle', 'complete', 'regDate'];

        for (let key of arr) {
          // 제목을 append하고  'todoNo', 'todoTitle', 'complete','regDate'라는 한 행을 돌면서 td에 넣자
          const td = document.createElement("td"); // <td></td>


          // 제목인 경우: 너무 할일이 많음

          if (key === 'todoTitle') {
            const a = document.createElement("a") // <a>를 생성
            a.innerText = todo[key]; //todo["todoTitle"]
            a.href = "/ajax/detail?todoNo=" + todo.todoNO;
            td.append(a);
            tr.append(td);



            // a태그 클릭 시 페이지 이동을 막고 비동기 요청으로 돌릴 수 있다.
            a.addEventListener("click", e => {
            e.preventDefault(); // 기본적으로 주어지는 이벤트를 막는다.


              // 미구현: 할일을 상세 조회하는 비동기 요청 함수를 호출한다

              selectTodo(e.target.href) // a태그가 가진 속성 값을 매개변수로 전달

            })
            continue;

          }

          // 제목이 아닌 경우
          td.innerText = todo[key]; // JS객체에서 key를 통해 value를 호출하는 방법
          // td가 가지는 값으로 가령 2라는 숫자를 넣자
          // td가 가지는 값으로 가령 2025-4:34를 넣자...

          tr.append(td) // 한 줄이 만들어졌으면 tr에 넣어서 <tr> <td> </td> </tr>

          //tr의 마지막 요소로 현재 todo 추가하기

        }


        tbody.append(tr);

      }




    })


}


// url = e.target.href = '/ajax...'
// 비동기로 할일을 상세 조회하는 함수
// const selectTodo(url) => {
//   // 매개변수 url = "/ajax/detail/todoNo=1"과 같은 문자열

  


//   // fetch 요청 시 url을 이용하게 된다.


//   // 두번째 then에서 popUpLayer 77번째

// }


const selectTodo = (url) => {
  url = `/ajax/detail?todoNo=${todoNo}` 
  
  
}


getTotalCount();
getCompleteCount();
selectTodoList();
//selectTodo();







