// 댓글 목록을 비동기로 조회하는 법 (ajax)

// 댓글 등록 (ajax)

// 댓글 삭제 (ajax)

// 댓글 수정 (ajax)





// RE(PRESNTATIONAL)S(TATE)T(RANSFER)API의 이용을 시도 



/* 

전부 comments만 자원의 이름으로 URL을 포함하고 메서드로 각각을 구분

자원의 상태 (데이터, 파일)을 이름(주소)로 구분하여 자원의 상태를 주고받는 것

자원의 이름을 명시하고 HTTP Method (GET, POST, PUT, DELETE)를 이용해 지정된 자원에 대한 CRUD를 진행한다

*/



// 댓글 목록을 비동기로 조회하는 법 (ajax)

const selectCommentList = () => {

  // REST API에서 GET

  // fetch (주소?쿼리스트링)

  // 다만 [POST, PUT, DELETE]는 fetch(주소, {method: "[필수]", header: "", body: " " })


  // 보드 디테일 페이지에서 이미 전역적으로 불러다 놨음

  fetch("/comments?boardNo=" + boardNo)
    .then(resp => resp.json())
    .then(commentList => {
      console.log(commentList);
      // 화면에 존재하는 기존 댓글 목록을 삭제한 후에 조회된 commentList를 이용하여 새로운 댓글 목록 출력

      // ul태그(댓글 목록 감싸는 요소)
      const ul = document.querySelector("#commentList");
      ul.innerHTML = ""; // 기존 댓글 목록 삭제


      /* ******* 조회된 commentList를 이용해 댓글 출력 ******* */
      for (let comment of commentList) {
        // 행(li) 생성 + 클래스 추가
        const commentRow = document.createElement("li");

        // parentCommentNo 필드는 => int형이었고 그래서 0이 위에서 출력된다. 이는 DB에서 null로 넘어왔다는 말 
        commentRow.classList.add("comment-row");
        // 대댓글(자식 댓글)인 경우 "child-comment" 클래스 추가
        
        
        if (comment.parentCommentNo != 0)
          commentRow.classList.add("child-comment");
        // 만약 삭제된 댓글이지만 자식 댓글이 존재하는 경우
        if (comment.commentDelFl == 'Y')
          // 자식 댓글이 존재하는 경우는 이미 SQL문에서 따짐
          commentRow.innerText = "삭제된 댓글 입니다";



        else { // 삭제되지 않은 댓글
          // 프로필 이미지, 닉네임, 날짜 감싸는 요소
          const commentWriter = document.createElement("p");
          commentWriter.classList.add("comment-writer");
          
          // 프로필 이미지
          const profileImg = document.createElement("img");
          
          if (comment.profileImg == null)
            profileImg.src = userDefaultIamge; // 기본 이미지 
          // (boardDetail에서 선언된 전역변수로 messages.properties에서 가져온 이미지)
          
          else
          profileImg.src = comment.profileImg; // 회원 이미지
          // 닉네임
          const nickname = document.createElement("span");
          nickname.innerText = comment.memberNickname;

          // 날짜(작성일)
          const commentDate = document.createElement("span");
          commentDate.classList.add("comment-date");
          commentDate.innerText = comment.commentWriteDate;
          // 작성자 영역(commentWriter)에 프로필, 닉네임, 날짜 추가
          commentWriter.append(profileImg, nickname, commentDate);



          // 댓글 행에 작성자 영역 추가
          commentRow.append(commentWriter);






          // ----------------------------------------------------
          // 댓글 내용
          const content = document.createElement("p");
          content.classList.add("comment-content");
          content.innerText = comment.commentContent;
          commentRow.append(content); // 행에 내용 추가





          // ----------------------------------------------------
      
          // 버튼 영역
          const commentBtnArea = document.createElement("div");
          commentBtnArea.classList.add("comment-btn-area");
          // 답글 버튼
          const childCommentBtn = document.createElement("button");
          childCommentBtn.innerText = "답글";  // 일단 아무런 동작도 하지 못함

          // 답글 버튼에 onclick 이벤트 리스너 추가
          childCommentBtn.setAttribute("onclick",
            `showInsertComment(${comment.commentNo}, this)`);
          // <button onclick="">답글</button> => 댓글의 번호와 해당 버튼 (e.target)을 함수쪽으로 가져간다



          // 버튼 영역에 답글 추가
          commentBtnArea.append(childCommentBtn);
      
      
          // 로그인한 회원 번호가 댓글 작성자 번호와 같을 때

      
          // 댓글 수정/삭제 버튼 출력
          if (loginMemberNo != null && loginMemberNo == comment.memberNo) {
            // 수정 버튼
            const updateBtn = document.createElement("button");
            updateBtn.innerText = "수정";
            // 수정 버튼에 onclick 이벤트 리스너 추가
            updateBtn.setAttribute("onclick",
              `showUpdateComment(${comment.commentNo}, this)`);


            // 삭제 버튼
            const deleteBtn = document.createElement("button");
            deleteBtn.innerText = "삭제";
            // 삭제 버튼에 onclick 이벤트 리스너 추가
            deleteBtn.setAttribute("onclick",
              `deleteComment(${comment.commentNo})`);
            // 버튼 영역에 수정, 삭제 버튼 추가
            commentBtnArea.append(updateBtn, deleteBtn);
          }
          // 행에 버튼 영역 추가
          commentRow.append(commentBtnArea);
        } // else 끝
        // 댓글 목록(ul)에 행(li) 추가
        ul.append(commentRow);
      } // for 끝


    })




};


// selectCommentList();




// 댓글 등록 (ajax)

const commentContent = document.querySelector('#commentContent');
// 댓글작성하는 textArea태그


const addComment  = document.querySelector('#addComment');
// 버튼 클릭




// 댓글 등록 버튼 클릭 시
addComment.addEventListener('click', (e)=>{


  // first) 로그인이 되어있지 않은 경우

  if(loginMemberNo == null){ // boardDetail.html에서 전역적으로 script태그 안에 선언해 놓음
    alert('로그인 후 이용해주세요')
    return;
  }

  // second) 댓글 내용이 빈 경우
  if(commentContent.value.trim().length==0){
    alert('내용 작성 후 등록버튼을 눌러 주세요');
    commentContent.focus();
    return;
  }

  // ajax를 이용해 댓글 등록 요청

  const data = {
    "boardNo" : boardNo,
    "memberNo" : loginMemberNo,
    "commentContent" : commentContent.value
  };


  fetch("/comments",{
    method : "POST",
    headers : {"Content-Type" : "application/json"},
    body : JSON.stringify(data)
  })
  .then(resp=> resp.text()) // 결과값이 어차피 댓글 내용이니 text뿐
  .then(result=>{
    if(result>0){
      alert('댓글이 등록 되었습니다');
      selectCommentList(); // 댓글 목록을 다시 조회하여 화면에 출력
      commentContent.value =""; // 작성하고 있던 댓글 내용 지우기. 새로고침이 안되므로 그대로 남아있음
    }

    else{
      alert('댓글등록 실패')
    }

  }).catch(err => console.log(err));



})




// 답글 등록 (ajax) 

/** 답글 작성 화면 추가
* @param {*} parentCommentNo
* @param {*} btn
*/
const showInsertComment = (parentCommentNo, btn) => {
  // ** 답글 작성 textarea가 한 개만 열릴 수 있도록 만들기 **
  const temp = document.getElementsByClassName("commentInsertContent");
  if(temp.length > 0){ // 답글 작성 textara가 이미 화면에 존재하는 경우
    if(confirm("다른 답글을 작성 중입니다. 현재 댓글에 답글을 작성 하시겠습니까?")){
      temp[0].nextElementSibling.remove(); // 버튼 영역부터 삭제
      temp[0].remove(); // textara 삭제 (기준점은 마지막에 삭제해야 된다!)
   
    } else{
      return; // 함수를 종료시켜 답글이 생성되지 않게함.
    }
  }
   // 답글을 작성할 textarea 요소 생성
  const textarea = document.createElement("textarea");
  textarea.classList.add("commentInsertContent");
   // 답글 버튼의 부모의 뒤쪽에 textarea 추가
  // after(요소) : 뒤쪽에 추가
  btn.parentElement.after(textarea);
  // 답글 버튼 영역 + 등록/취소 버튼 생성 및 추가
  const commentBtnArea = document.createElement("div");
  commentBtnArea.classList.add("comment-btn-area");
  const insertBtn = document.createElement("button");
  insertBtn.innerText = "등록";

   // +parentCommentNo+ 문자열 형태인 것을 숫자로 변환하여 전달해라
   // +문자열+는 그 안의 문자열을 숫자로 바꾸는 것이다.
   // Number(parentCommentNo)와 동치
  insertBtn.setAttribute("onclick", "insertChildComment("+parentCommentNo+", this)");
  const cancelBtn = document.createElement("button");
  cancelBtn.innerText = "취소";
  cancelBtn.setAttribute("onclick", "insertCancel(this)");
  // 답글 버튼 영역의 자식으로 등록/취소 버튼 추가
  commentBtnArea.append(insertBtn, cancelBtn);
  // 답글 버튼 영역을 화면에 추가된 textarea 뒤쪽에 추가
  textarea.after(commentBtnArea);
 }
 // ---------------------------------------
 /** 답글 (자식 댓글) 작성 취소
 * @param {*} cancelBtn : 취소 버튼
 */
 const insertCancel = (cancelBtn) => {
  // 취소 버튼 부모의 이전 요소(textarea) 삭제
  cancelBtn.parentElement.previousElementSibling.remove();
  // 취소 버튼이 존재하는 버튼영역 삭제
  cancelBtn.parentElement.remove();
 }
 



// 2레벨 이하인 자식 댓글을 등록하는 함수
const insertChildComment = (parentCommentNo, btn) =>{
  // 답글 내용이 작성된 textarea 요소를 얻어온다

  const textarea = btn.parentElement.previousElementSibling;


  // 유효성 검사

  if(textarea.value.trim().length==0){
    alert('내용작성 후 등록버튼을 클릭해주세요');
    textarea.focus();
    return;
  }

  // ajax를 이용해 전달할 데이터 (댓글작성시 넘길 것 + 부모의 번호)

  const data = {
    "boardNo" : boardNo, 
    "memberNo" : loginMemberNo, 
    "commentContent" : textarea.value, 
    "parentCommentNo" : parentCommentNo 
  };

  fetch("/comments",{

    method: "POST",
    headers : {"Content-Type" : "application/json"},
    body: JSON.stringify(data)
  })
  .then(resp => resp.text())
  .then(result =>{
    if(result>0){
      alert('답글이 등록되었습니다');
      selectCommentList();
    }

    else{
      alert('답글 등록 실패')
    }

  })

}




// 슬래시 별별 엔터



/** 
 * @param {*} commentNo (현재 댓글 번호) 
 */

// 댓글 삭제 (ajax)
const deleteComment = (commentNo) =>{
  // 바로 삭제시키지 않고 confirm으로 항상 물어봄
  
  
  // 취소 선택 시의 처리

  if(!confirm("삭제하시겠습니까?")) // false인 경우
  {
    return;
  }


  fetch("/comments",{
    method: "DELETE",
    headers : {"Content-Type" : "application/json"},
    body: commentNo
  })
  .then (resp=>resp.text()) //1이나 0이 옴
  .then (result =>{
    if(result>0){
      alert('삭제되었습니다');
      selectCommentList();
      // 바로 원래 댓글을 조회하여 뿌려줘야 함
    }
    else{
      alert('삭제 실패!');
    }

  });


}




// 댓글 수정 (ajax)

// 수정 취소 시 원래 댓글 형태로 돌아가기 위한 백업 변수
let beforeCommentRow;
/** 댓글 수정 화면 전환
* @param {*} commentNo
* @param {*} btn
*/
const showUpdateComment = (commentNo, btn) => {
 /* 댓글 수정 화면이 1개만 열릴 수 있게 하기 */
 const temp = document.querySelector(".update-textarea");
 // .update-textarea 존재 == 열려있는 댓글 수정창이 존재
 if(temp != null){
   if(confirm("수정 중인 댓글이 있습니다. 현재 댓글을 수정 하시겠습니까?")){
     const commentRow = temp.parentElement; // 기존 댓글 행
     commentRow.after(beforeCommentRow); // 기존 댓글 다음에 백업 추가
	  console.log(commentRow);
     commentRow.remove(); // 기존 삭제 -> 백업이 기존 행 위치로 이동
   } else{ // 취소
     return;
   }
 }
 // -------------------------------------------
 // 1. 댓글 수정이 클릭된 행 (.comment-row) 선택
 const commentRow = btn.closest("li");
 // 2. 행 전체를 백업(복제)
 // 요소.cloneNode(true) : 요소 복제,
 //           매개변수 true == 하위 요소도 복제
 beforeCommentRow = commentRow.cloneNode(true);
 // console.log(beforeCommentRow);
 // 3. 기존 댓글에 작성되어 있던 내용만 얻어오기
 let beforeContent = commentRow.children[1].innerText;
 // 4. 댓글 행 내부를 모두 삭제
 commentRow.innerHTML = "";
 // 5. textarea 생성 + 클래스 추가 + 내용 추가
 const textarea = document.createElement("textarea");
 textarea.classList.add("update-textarea");
 textarea.value = beforeContent;
 // 6. 댓글 행에 textarea 추가
 commentRow.append(textarea);
 // 7. 버튼 영역 생성
 const commentBtnArea = document.createElement("div");
 commentBtnArea.classList.add("comment-btn-area");
 // 8. 수정 버튼 생성
 const updateBtn = document.createElement("button");
 updateBtn.innerText = "수정";
 updateBtn.setAttribute("onclick", `updateComment(${commentNo}, this)`);
 // 9. 취소 버튼 생성
 const cancelBtn = document.createElement("button");
 cancelBtn.innerText = "취소";
 cancelBtn.setAttribute("onclick", "updateCancel(this)");
 // 10. 버튼 영역에 수정/취소 버튼 추가 후
 //     댓글 행에 버튼 영역 추가
 commentBtnArea.append(updateBtn, cancelBtn);
 commentRow.append(commentBtnArea);
}
// --------------------------------------------------------------------
/** 댓글 수정 취소
* @param {*} btn : 취소 버튼
*/
const updateCancel = (btn) => {
 if(confirm("취소 하시겠습니까?")){
   const commentRow = btn.closest("li"); // 기존 댓글 행
   commentRow.after(beforeCommentRow); // 기존 댓글 다음에 백업 추가
   commentRow.remove(); // 기존 삭제 -> 백업이 기존 행 위치로 이동
 }
}


// 댓글 수정 fetch 요청

const updateComment = (commentNo, btn) => {
  // 수정된 내용이 작성되어있는 textarea를 얻어온다
  // 수정을 위해서는 기존 내용을 select해야 하므로
  
  // 댓글의 번호 => 매개변수로 얻어옴 ${commentNo} 와 this (수정버튼 자체)를 얻어오기 때문

  const textarea = 
  // 일단 수정 버튼을 기준으로 얻어와야 한다. 여러 textArea를 뽑아올 때를 대비하여
  // 수정의 부모가 textArea이고 그것의 형제를 찾아야 함
  btn.parentElement.previousElementSibling;

  if(textarea.value.trim().length==0){
    alert('댓글작성 후 수정버튼을 클릭해주세요');
    textarea.focus();
    return;
  }

  // 수정 시 전달 데이터

  // commentNo 

  const data = {
    "commentNo": commentNo,
    "commentContent": textarea.value
  };

  fetch("/comments",{
    method: "PUT",
    headers : {"Content-Type" : "application/json"},
    body: JSON.stringify(data)
  })
  .then(resp=> resp.text())
  .then(result => {
    if(result>0){
      alert("댓글 수정 완료")
      selectCommentList();
    }

    else{
      alert('댓글 수정 실패')
    }

  });




}