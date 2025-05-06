
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

  // 2단계. 로그인이 아닐 때에 대한 NPE (클릭했을 때 서버로 제출 자체를 막아야 함)


  if(loginMemberNo == null){
    alert('로그인 후 이용해주세요');
    return;
  }


  // 3. 준비된 3개의 변수를 객체로 저장한 후 json으로 변환하여 서버로 제출

  const obj = {
    // loginMemberNo => 현재 로그인한 사람에 대한 정보를 넘겨줘야 테이블 컬럼에 맞출 수 있음
    // boardNo  => 현재 게시글의 번호
    // likeCheck => 정적 정보로는 얻어올 수 없음

    "memberNo" : loginMemberNo, // 키를 약간 변경 (서버의 board필드에 맞게)
    "boardNo" : boardNo,
    "likeCheck" : likeCheck
  };


  // 4단계 좋아요를 insert하거나 delete하는 요청을 저 세변수를 담아 보낸다




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


      // 5단계: 가장먼저 likecheck값을 1 <-> 0으로 변환한다. 클릭할 때마다 변경이 되어야 하기 때문
      // 클릭 될 때마다 insert와 delete동작을 번갈아가면서 하게 하려고
      
      likeCheck = likeCheck == 0? 1: 0; // 재호출을 고려

      // 6단계: 하트를 채웠다 비웠다 바꾸기
      e.target.classList.toggle("fa-regular"); // 빈 하트
      // 없으면 넣고 있으면 빼게 됨
      e.target.classList.toggle("fa-solid"); // 채워진 하트

      // 7단계: 좋아요 수를 화면상에서 수정

     //<i id="boardLike" class="fa-heart + (조건부 클래스)"> 의 nextElementSibling에 해당하는 span으로 가져옴

     e.target.nextElementSibling.innerText = count; // 서버에서 온 값을 그대로 span에

  });

})



/*
// completionToggleButton은 HTML에서 이미 정의

const completionToggleButton = document.getElementById('completionToggleBtn');

if (completionToggleButton) { // 완료 토글 버튼이 존재하는 경우
    const completionIcon = document.getElementById('completionIcon'); // 아이콘 요소 선택
    const completionText = document.getElementById('completionText'); // 텍스트 요소 선택

    completionToggleButton.addEventListener('click', () => {

        // 로그인 여부 확인 (완료 상태 변경 권한에 따라 필요)
        if (loginMemberNo == null) {
            alert('로그인 후 이용해주세요.');
            return;
        }

        // 현재 상태 읽기 (data-* 속성 사용)
        const currentStatus = completionToggleButton.dataset.currentStatus;
        // 반대 상태 계산
        const newStatus = (currentStatus === 'Y') ? 'N' : 'Y';

        // 서버에 완료 상태 변경 요청 전송
        fetch('/board/updateCompletion', { // <<=== 실제 서버 API 엔드포인트로 변경!
            method: 'POST', // 또는 'PUT', 'PATCH' 등 API 설계에 맞게
            headers: {
                'Content-Type': 'application/json',
                // 필요시 CSRF 토큰 등 추가 헤더 전송 (예: X-CSRF-TOKEN 헤더)
            },
            body: JSON.stringify({ // 요청 본문에 boardNo와 새 상태 담기
                boardNo: boardNo, // 게시글 번호 (전역 변수)
                completionStatus: newStatus
            })
        })
        .then(response => {
            if (!response.ok) {
                // 응답 상태 코드가 200번대가 아닐 경우 에러 처리
                throw new Error('서버 응답 오류: ' + response.statusText);
            }
            // 서버 응답이 JSON 형식이라고 가정 (서버 구현에 맞게 변경 필요)
            return response.json();
            // 서버에서 단순 성공/실패만 반환한다면 response.text() 또는 response.ok만 확인해도 됩니다.
        })
        .then(result => {
            // 서버 처리 성공 시 UI 업데이트
            console.log("서버 응답:", result); // 서버 응답 확인 (성공 여부 등)

            // UI 업데이트: 아이콘, 텍스트, data 속성 변경
            // Font Awesome 클래스 토글 (fa-regular <-> fa-solid, fa-circle <-> fa-circle-check)
            completionIcon.classList.toggle("fa-regular");
            completionIcon.classList.toggle("fa-solid");
            completionIcon.classList.toggle("fa-circle");
            completionIcon.classList.toggle("fa-circle-check");

            // 텍스트 업데이트
            completionText.textContent = (newStatus === 'Y') ? '완료' : '미완료';

            // 버튼의 data 속성 업데이트 (다음 클릭 시 현재 상태를 알기 위해 중요)
            completionToggleButton.dataset.currentStatus = newStatus;

            // 필요에 따라 성공 메시지 표시
            // alert(`게시글 상태가 "${completionText.textContent}"(으)로 변경되었습니다.`);

        })
        .catch(error => {
            // 서버 통신 실패 또는 응답 오류 발생 시 처리
            console.error('완료 상태 업데이트 실패:', error);
            alert('완료 상태 변경 중 오류가 발생했습니다.\n' + error.message);
            // 실패 시 UI 변경을 되돌리거나 사용자에게 알리는 추가 처리가 필요할 수 있습니다.
        });
    });
}

*/

const completionToggleButtons = document.querySelectorAll('.completionToggleBtn');

completionToggleButtons.forEach((button) => {
    button.addEventListener('click', (e) => { 
        // 로그인하지 않았거나 (loginMemberNo == null)  로그인했지만 작성자가 아닌 경우 (loginMemberNo !== authorMemberNo)
        // 즉, 로그인 회원 번호가 작성자 회원 번호와 다르면 클릭 막음
        if (loginMemberNo !== memberNo) {            
          console.log("작성자가 아니므로 나눔 상태 변경 불가");
            // 클릭 이벤트의 기본 동작(예: 링크 이동, 버튼 동작)을 중지
            e.preventDefault();
            return; // 이벤트 핸들러 종료
        }


        // 로그인 상태 확인 (작성자는 로그인 되어 있어야 하므로 사실상 글쓴이이 != null과 같은 조건)
        if (loginMemberNo == null) {
            // 이 부분은 위 작성자 체크에서 걸러지므로 사실상 실행되지 않을듯?
            alert('로그인 후 이용해주세요.'); 
            return;
        }

        const currentStatus = button.dataset.currentStatus;

        // 상태 변경 확인 메시지 설정
        let confirmationMessage;
        // 현재 상태가 'Y'가 아닌 경우 (즉, 'N'이라면) -> '완료'로 변경
        if (currentStatus !== 'Y') {
            confirmationMessage = "나눔을 완료 상태로 변경하시겠습니까?";
        } else { // 현재 상태가 'Y'라면 -> '진행 중'으로 변경
            confirmationMessage = "나눔을 진행 중 상태로 변경하시겠습니까?";
        }

        // 사용자가 '취소' 선택 시 종료
        if (!confirm(confirmationMessage)) {
            console.log("나눔 상태 변경 취소됨");
            return; // 함수 종료
        }

        // 상태 변경 실행
        const newStatus = (currentStatus === 'Y') ? 'N' : 'Y';

        // 현재 URL에서 쿼리 문자열 가져오기 및 cp 추출 (기존 코드)
        const queryString = window.location.search;
        const urlParams = new URLSearchParams(queryString);
        let cp = urlParams.get('cp');
        if (cp === null || cp.trim() === '') {
            cp = '1';
        }

        // 상태 변경 요청과 페이지 이동을 한 번에 처리 (기존 코드)
        window.location.href = `/board/updateCompletionSync?boardCode=${boardCode}&boardNo=${boardNo}&completionStatus=${newStatus}&cp=${cp}`;

    });
});