
// 목록으로 버튼 동작 (메인페이지로 이동)
const goToList = document.querySelector("#goToList");
goToList.addEventListener("click", () => {
  location.href = "/"; // 메인페이지 ("/") 요청 GET 방식
});

// 완료 여부 변경 버튼에 대한 동작
const completeBtn = document.querySelector(".complete-btn");

completeBtn.addEventListener("click", (e) => {
  const todoNo = e.target.dataset.todoNo;

  // todoNo 값이 undefined인지 확인
  if (!todoNo) {
    console.error("todoNo 값이 없습니다!");
    return;
  }

  let complete = e.target.innerText; // 기존 완료 여부값 얻어오기 ("Y"/"N")
  complete = complete === "Y" ? "N" : "Y";

  location.href = `/todo/changeComplete?todoNo=${todoNo}&complete=${complete}`;
});

// 수정 버튼 클릭 시 동작
const updateBtn = document.querySelector("#updateBtn");

updateBtn.addEventListener("click", (e) => {
  const todoNo = e.target.dataset.todoNo;

  // todoNo 값이 undefined인지 확인
  if (!todoNo) {
    console.error("todoNo 값이 없습니다!");
    return;
  }

  location.href = `/todo/update?todoNo=${todoNo}`;
});

// 삭제 버튼 클릭 시 동작
const deleteBtn = document.querySelector("#deleteBtn");

deleteBtn.addEventListener("click", (e) => {
  const todoNo = e.target.dataset.todoNo;

  const isConfirmed = confirm("삭제하시겠습니까?");
  if (isConfirmed) {
    location.href = `/todo/delete?todoNo=${todoNo}`;
  }
});


