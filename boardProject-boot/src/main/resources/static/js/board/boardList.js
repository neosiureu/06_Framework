//글쓰기 버튼 클릭시

const insertBtn = document.querySelector("#insertBtn");

// 로그인이 안될때는 글쓰기 버튼이 비활성됨

if(insertBtn != null){
  insertBtn.addEventListener("click" , (e)=> {
    
    //get 방식 요청(글작성 할수 있는 페이지로 이동 시키기)
    //  /editBoard/1/insert

    location.href = `/editBoard/${boardCode}/insert`;

  })
  
  
}
