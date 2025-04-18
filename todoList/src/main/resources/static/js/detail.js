const goToList = document.querySelector("#goToList");

goToList.addEventListener('click',()=>{
  location.href='/'; // 메인페이지인 "/"로 요청을 보낸다. 다만 location은 항상 get방식

});


// 일단 목록으로를 누르면 메인페이지로 이동하는 로직




// 완료 여부를 처리하는 동작 (Y->N)

const completeBtn = document.querySelector(".complete-btn");

completeBtn.addEventListener('click',(e)=>{
  // 데이터 셋 = 요소.dataset => 요소, 즉 버튼의 주소를 얻어오는 방법은 e.target
  //e.target.dataset 
  // // todoNo을 반환받는다. 즉 data-todo-no
  // 다만 js에서는 카멜케이스. html에서는 -로 얻어오기 때문에
  const todoNo = e.target.dataset.todoNo;
  // 해당 번호에 대한 내용을 일단 얻어온다

  let complete = e.target.innerText;// 기존 완료 값을 저장
  // N이냐 Y냐 todoComplete를 저장

  // Y라면 N으로 N이라면 Y로 바꿔 요청 보낸다 => SQL문에서 DECODE등의 복잡한 로직을 쓰고싶지 않아 요청 자체를 이렇게 보낸다


  complete = (complete==='Y') ? 'N' : 'Y';

  // 완료여부 수정 요청하기


  location.href = `/todo/changeComplete?todoNo=${todoNo}&complete=${complete}`;
  // get방식이므로 쿼리스트링을 통해 데이터를 전달한다
});






// 수정 버튼을 클릭하면 새로운 수정 화면으로 간다

const updateBtn = document.querySelector('#updateBtn');

updateBtn.addEventListener('click',(e)=>{

  const todoNo = e.target.dataset.todoNo; 
  location.href = `/todo/update?todoNo=${todoNo}`;
})


const deleteBtn = document.querySelector('#deleteBtn');

deleteBtn.addEventListener('click',(e)=>{

  const todoNo = e.target.dataset.todoNo; 



  

  if(confirm ("삭제하시겠습니까?")){
    location.href = `/todo/delete?todoNo=${todoNo}`;
  }
  

})



// fetch("/요청주소").then(resp => resp.json()).then().catch(e) 

