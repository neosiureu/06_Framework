
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


// 1단계: 좋아요(하트) 아이콘 요소를 얻어온다

document.querySelector('#boardLike').addEventListener('click', (e) => {
  // 2단계: 로그인하지 않은 사용자의 클릭을 방지한다 (NPE 방지용)
  if (loginMemberNo == null) {
    alert('로그인 후 이용해주세요.')
    return;
  }

  // 3단계: 필요한 데이터들을 하나의 객체로 묶고, 서버에 보낼 JSON 형식으로 변환한다
  // loginMemberNo => 현재 로그인한 사람에 대한 정보를 넘겨줘야 테이블 컬럼에 맞출 수 있음
  // boardNo       => 현재 게시글 번호
  // likeCheck     => 정적인 값이 아니므로 클릭 시마다 동적으로 설정해야 함


  const obj = {
    "memberNo": loginMemberNo,
    "boardNo": boardNo,
    "likeCheck": likeCheck
  }

// 4단계: 서버로 좋아요(insert) 또는 좋아요 취소(delete) 요청을 보낸다
fetch("/board/like",{
  method:"POST",
  headers: {"Content-Type": "application/json"},
  body: JSON.stringify(obj)
}).then(resp => resp.text()).then((count)=>{

  if(count==-1) 
    {console.log('좋아요 처리 실패');
      return;
    }
likeCheck = likeCheck==1? 0 : 1;
// 5단계: 서버 응답이 성공일 경우, 현재 likeCheck 값을 반대로 토글한다 (0 <-> 1)
// 클릭될 때마다 insert와 delete가 번갈아 동작하도록 하기 위함


// 6단계: 하트 아이콘 모양을 채워진 하트 ↔ 빈 하트로 시각적으로 전환한다
e.target.classList.toggle("fa-regular"); // 빈 하트
// 없으면 넣고 있으면 빼게 됨
e.target.classList.toggle("fa-solid"); // 채워진 하트

e.target.nextElementSibling.innerText = count;

// 7단계: 서버에서 전달받은 총 좋아요 수를 화면의 span 요소에 반영한다
// e.target.nextElementSibling => 좋아요 수가 출력되는 요소
})

  

})











// ----------------------- 게시글 수정 버튼--------------------


const updateBtn = document.querySelector('#updateBtn');

if (updateBtn != null) {
  // <th:block th:if="${board.memberNo == session.loginMember?.memberNo}">가 아니면 랜더링 자체가 안 되기 때문

  updateBtn.addEventListener("click", () => {


    if (completionStatus === 'Y') {
      alert("나눔완료된 글은 수정할 수 없습니다.");
      e.preventDefault(); // ← 이동 방지
      return;
    }

    ///board/1/2004?cp=1 가 현재인데
    // get방식으로 /editBoard/1/2004/update?cp=1 과 같이 4계층과 cp까지

    // /1/2004만 똑같고 앞만 board를 editBoard로

    location.href = location.pathname.replace('board', 'editBoard') + "/update" + location.search;

    //   /editBoard/1/2004/update?cp=1 과 같이 변함
  })

}






// // 수정 막기 처리 => 이를 따로 처리해서는 안 됐었음
// if (updateBtn != null) {
//   updateBtn.addEventListener("click", (e) => {

//     if (completionStatus === 'Y') {
//       alert("나눔완료된 글은 수정할 수 없습니다.");
//       e.preventDefault();
//       return;
//     }

//     location.href = location.href;
//   });
// }








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

    if (loginMemberNo !== memberNo) {
      console.log("작성자가 아니므로 나눔 상태 변경 불가");
      e.preventDefault();
      return;
    }

    if (loginMemberNo == null) {
      alert('로그인 후 이용해주세요.');
      return;
    }

    const currentStatus = button.querySelector("input[name='completionStatus']").value;

    let confirmationMessage;
    if (currentStatus !== 'Y') {
      confirmationMessage = "나눔을 완료 상태로 변경하시겠습니까?";
    } else {
      confirmationMessage = "나눔을 진행 중 상태로 변경하시겠습니까?";
    }

    if (!confirm(confirmationMessage)) {
      // console.log("나눔 상태 변경 취소됨");
      return;
    }

    const newStatus = (currentStatus === 'Y') ? 'N' : 'Y';
    button.querySelector("input[name='completionStatus']").value = newStatus;

    // cp 값만 따로 추출
    const urlParams = new URLSearchParams(location.search);
    let cp = urlParams.get('cp');
    if (cp === null || cp.trim() === '') {
      cp = '1';
    }

    location.href = `/board/updateCompletionSync?boardCode=${boardCode}&boardNo=${boardNo}&completionStatus=${newStatus}&cp=${cp}`;
  });
});



// ----------------------- 게시글 삭제 버튼 GET방식--------------------




const deleteBtn = document.querySelector('#deleteBtn');

if (deleteBtn != null) {
  deleteBtn.addEventListener('click', () => {
    if (!confirm("삭제하시겠습니까?")) {
      alert('취소됨!');
      return;
    }

    // /board/1/2009?cp=1 <- 현재 주소

    // -> 목표: /editBoard/1/2009/delete?cp=1

    const url = location.pathname.replace("board", "editBoard") + "/delete";

    const quertyString = location.search; // ?cp=1 라는 쿼리스트링 부분이 저장 됨

    location.href = url + quertyString;


  });
}



// ----------------------- 게시글 삭제 버튼 POST방식--------------------

const deleteBtn2 = document.querySelector('#deleteBtn2');



if (deleteBtn != null) {
  deleteBtn2.addEventListener('click', () => {
    if (!confirm("삭제하시겠습니까?")) {
      alert('취소됨!');
      return;
    }

    const url = location.pathname.replace("board", "editBoard") + "/delete";
    // 목표: /editBoard/1/2004/delete?cp=1


    // JS에서 동기식으로 post요청을 보내는 것은 사실 불가능해서 form태그를 보낸다


    const form = document.createElement("form");
    //<form>

    form.action = url;
    form.method = "POST";

    // 쿼리 스트링을 주소에 보내지 못함. 
    // cp를 post로 보내고싶다면 input에 name속성값을 통해 전달해야 함


    // cp 값을 저장할 input태그 생성

    const input = document.createElement("input");
    // <input>

    input.type = "hidden";
    input.name = "cp"
    // 직접 입력할 수 없으니 1이 들어있어야 함
    // input.value = location.search // 쿼리스트링에서 원하는 파라미터 값을 얻어오는 방버

    const params = new URLSearchParams(location.search);
    // ?cp=1과 같이 나옴

    const cp = params.get("cp"); // => 1
    input.value = cp;
    // cp=1의 형태로 데이터가 제출 됨


    form.append(input);

    // 아무데나 붙여라 => 화면에 폼 태그를 추가한 후 제출하기

    document.querySelector('body').append(form); // 태그는 기호 없이 구해올 수 있음

    form.submit();
  });
}



