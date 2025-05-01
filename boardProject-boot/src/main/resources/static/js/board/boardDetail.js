//좋아요 버튼 클릭 시 비동기로 좋아요 INSERT/DELETE

// 타임리프 코드 해석 순서
// 1. th: 코드(java) + Spring El
// 2. html 코드 (+css / js)


//1) 로그인 한 회원 번호 준비
//loginMemberNo -> 현재 로그인 한 사람의 memberNo

//2) 현재 게시글 번호 준비
//boardNo -> 현재 게시글의 번호

// 3) 좋아요 여부 준비
// lickeCheck -> 현재 이 게시글의 likeCHeck 값

document.querySelector("#boardLike").addEventListener("click", (e) => {

  //2. 로그인 상태가 아닌 경우 동작 

  if (loginMemberNo == null){
    alert ("로그인부터 해주세요");
    return;
  }

  //3. 준비된 3개의 변수를 객체로 저장 (json 변환 예정)
  
  
  const obj = {
    "memberNo" : loginMemberNo,
    "boardNo" : boardNo,
    "likeCheck" : likeCheck 
  };


  //4. 좋아요 INSERT /DELETE 비동기 요청
  fetch("/board/like" , {
    method : "POST" ,
    headers : {"Content-Type" : "application/json"},
    body : JSON.stringify(obj)
  })
  .then(resp => resp.text())
  .then(count => {
    
    if(count == -1) {
      console.log("좋아요 처리 실패");
      return;
    }

    //5. likeCheck 값 0 <-> 1 변환
    // -> 클릴 될때마다 INSERT/ DELETE 동작을 번갈아 가면서 하게끔.
    likeCheck = likeCheck == 0 ? 1 : 0;
    
    //6. 하트를 채웠다 / 비웟다 바꾸기
    e.target.classList.toggle("fa-regular"); //빈 하트
    e.target.classList.toggle("fa-solid");   // 채워진 하트

    // 7. 게시글 좋아요 수 수정
    e.target.nextElementSibling.innerText = count;
  });
})