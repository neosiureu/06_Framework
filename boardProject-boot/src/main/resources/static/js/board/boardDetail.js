console.log('잘 연결됨');

// 하트버튼 클릭 시 비동기로 좋아요를 insert delete 하겠다

// loginMemberNo => 현재 로그인한 사람에 대한 정보를 넘겨줘야 테이블 컬럼에 맞출 수 있음
// boardNo  => 현재 게시글의 번호
// likeCheck => 정적 정보로는 얻어올 수 없음



/*

[ * 좋아요 영역 (class="like-area") ]
┌────────────────────────────────────────────────────────────┐
│ <span class="like-area">                                   │
│                                                            │
│   ♥ <i id="boardLike" class="fa-heart + (조건부 클래스)">   │
│      └─ 클래스 추가:                                        │
│          - likeCheck == 1 → fa-solid                       │
│          - likeCheck != 1 → fa-regular                     │
│                                                            │
│   <span th:text="*{likeCount}">1</span> ← 좋아요 개수      │
│                                                            │
│ </span>                                                    │
└────────────────────────────────────────────────────────────┘






  <script th:inline="javascript">


        // - loginMember가 null 인 경우 null 반환
        const loginMemberNo = /*[[${session.loginMember?.memberNo}]] "로그인 회원 번호";

        // 현재 게시글 번호를 전역 변수로 저장
       // const boardNo = /*[[${board.boardNo}]]"게시글 번호";

        // 현재 게시글 좋아요 여부를 전역 변수로 저장
        //let likeCheck = /*[[${board.likeCheck}]]"좋아요 여부";

        //const userDefaultIamge = /*[[#{user.default.image}]] "기본이미지";

        
    //</script>

// likecheck가 0이라면 delete하겠다
// likecheck가 1이라면 insert하겠다

// request와 달리 session scope 객체는 JS에서 얻어올 순 없다. 
// => 그냥 타임리프를 이용해 session으로 뿌려진 것을 얻어오는 수밖에. script를 html에서 가져오고 변수만 가져와
// 값은 세팅하고 JS에서는 이용만 하자


 타임리프의 코드 해석 순서

1) th:코드
2) html코드

*/ 


/*
 ─── ② 본문 내용 ───────────────────────────────────────│
│   <div class="board-content" th:text="*{boardContent}">   │
│       (게시글 내용)                                        │
│   </div>                                                   │
│                                                            │
│   ─── ③ 버튼 영역 ───────────────────────────────────────│
│   <div class="board-btn-area">                            │
│                                                            │
│     ┌─ 작성자 전용 (memberNo == loginMember) ────────────┐│
│     │ <button id="updateBtn">수정</button>                ││
│     │ <button id="deleteBtn">삭제(GET)</button>           ││
│     │ <button id="deleteBtn2">삭제(POST)</button>         ││
│     └──────────────────────────────────────────────────────┘│
│                                                            │
│     <button id="goToListBtn">목록으로</button>            │
│   </div>                                                   │
│                                                            │
│ </section>                                                 │
└────────────────────────────────────────────────────────────┘



[ * 댓글 영역 (#commentArea) ]
┌──────────────────────────────────────────────────────────────────────┐
│ <div id="commentArea">                                              │
│                                                                      │
│   ─── ① 댓글 목록 영역 (.comment-list-area) ────────────────────────│
│   <div class="comment-list-area">                                    │
│                                                                      │
│     <ul id="commentList">                                            │
│                                                                      │
│       ┌─ (li) 댓글 한 줄 (th:each="comment : ${board.commentList}") ─┐
│       │ <li class="comment-row                                        │
│            + (comment.parentCommentNo != 0 ? child-comment)"          │
│            th:object="${comment}">                                    │
│                                                                      │
│         ┌─ 삭제된 댓글일 때 (commentDelFl == 'Y') ────────────────┐ │
│         │ <th:block> 삭제된 댓글 입니다 </th:block>                  │ │
│         └────────────────────────────────────────────────────────────┘ │
│                                                                      │
│         ┌─ 정상 댓글 (commentDelFl == 'N') ────────────────────────┐ │
│         │ <p class="comment-writer">                                 │ │
│         │   [프로필]                                                 │ │
│         │     • th:unless="*{profileImg}" → 기본 이미지              │ │
│         │     • th:if="*{profileImg}"    → 업로드 이미지             │ │
│         │   <span th:text="*{memberNickname}">닉네임</span>          │ │
│         │   <span class="comment-date"                                │ │
│         │         th:text="*{commentWriteDate}">작성일</span>        │ │
│         │ </p>                                                       │ │
│         │                                                            │ │
│         │ <p class="comment-content"                                 │ │
│         │    th:text="*{commentContent}">댓글 내용</p>               │ │
│         │                                                            │ │
│         │ <div class="comment-btn-area">                             │ │
│         │   <button onClick="showInsertComment(*{commentNo}, this)"> │ │
│         │           답글                                             │ │
│         │   </button>                                                │ │
│         │                                                            │ │
│         │   (로그인 회원 == 작성자)                                  │ │
│         │   <th:block th:if="loginMember.memberNo == comment.memberNo">││
│         │       <button onClick="showUpdateComment(*{commentNo},this)">││
│         │               수정                                        │ │
│         │       </button>                                            │ │
│         │       <button onClick="deleteComment(*{commentNo})">삭제</button>││
│         │   </th:block>                                              │ │
│         │ </div>                                                     │ │
│         └────────────────────────────────────────────────────────────┘ │
│                                                                      │
│       </li>   ← 여러 개가 th:each 로 반복                            │
│       └───────────────────────────────────────────────────────────────┘
│                                                                      │
│     </ul>                                                            │
│   </div>                                                             │
│                                                                      │
│   ─── ② 댓글 작성 영역 (.comment-write-area) ───────────────────────│
│   <div class="comment-write-area">                                   │
│     <textarea id="commentContent"></textarea>                        │
│     <button id="addComment">                                         │
│         댓글<br>등록                                                 │
│     </button>                                                        │
│   </div>                                                             │
│                                                                      │
│ </div> <!-- #commentArea -->                                         │
└──────────────────────────────────────────────────────────────────────┘
  
  */


// 1단계: boardLike라는 하트를 얻어온다.

document.querySelector('#boardLike').addEventListener('click',(e)=>{

  // 2. 로그인이 아닐 때에 대한 NPE (클릭했을 때 서버로 제출 자체를 막아야 함)


  if(loginMemberNo == null){
    alert('로그인 후 이용해주세요');
    return;
  }


  // 3. 준비된 3개의 변수를 객체로 저장해야 json으로 변환하여 서버로 제출

  const obj = {
    // loginMemberNo => 현재 로그인한 사람에 대한 정보를 넘겨줘야 테이블 컬럼에 맞출 수 있음
    // boardNo  => 현재 게시글의 번호
    // likeCheck => 정적 정보로는 얻어올 수 없음

    "memberNo" : loginMemberNo, // 키를 약간 변경 (서버의 board필드에 맞게)
    "boardNo" : boardNo,
    "likeCheck" : likeCheck
  };


  // 4. 좋아요를 insert하거나 delete하는 요청을 저 세변수를 담아 보낸다




  /* 
  






  

*/


  fetch("/board/like",{ 
    method :"POST",
    headers : {"Content-Type": "application/json"},
    body: JSON.stringify(obj)
  })
  .then(resp=> resp.text()) // count는 -1또는 총 좋아요의 개수
  .then(count => {


    if(count == -1){
      console.log('좋아요 처리 실패');
      return;
    }


      // 5. 가장먼저 likecheck값을 1 <-> 0으로 변환한다. 클릭할 때마다 변경이 되어야 하기 때문
      // 클릭 될 때마다 insert와 delete동작을 번갈아가면서 하게 하려고
      
      likeCheck = likeCheck == 0? 1: 0; // 재호출을 고려

      // 6. 하트를 채웠다 비웠다 바꾸기
      e.target.classList.toggle("fa-regular"); // 빈 하트
      // 없으면 넣고 있으면 빼게 됨
      e.target.classList.toggle("fa-solid"); // 채워진 하트

      // 7. 좋아요 수를 화면상에서 수정

     //<i id="boardLike" class="fa-heart + (조건부 클래스)"> 의 nextElementSibling에 해당하는 span으로 가져옴

     e.target.nextElementSibling.innerText = count; // 서버에서 온 값을 그대로 span에

  });

})