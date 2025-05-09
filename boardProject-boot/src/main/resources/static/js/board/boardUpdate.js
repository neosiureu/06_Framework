// boardUpdate.html에 작성된 전역 변수들
//const imageList = /*[[${board.imageList}]]*/ [];
//const previewList = document.querySelectorAll('img.preview');
//const orderList = [];  기존에 존재하던 이미지의 순서(order)를 기록할 배열

/* X버튼이 눌러져 삭제되는 이미지의 
 순서(order)를 기록하는 Set
 기존 이미지를 x버튼 눌러 삭제한다면

 X버튼 누를 떄마다 중복된 데이터가 들어가서는 안 된다.
 몇 번을 X버튼 누르던지 한번만 들어가도록  하기 위해 Set객체를 만든다
*/



const deleteOrderList = new Set();
// Set : 중복된 값을 저장 못하게하는 객체(Java Set 똑같음)
// * Set을 사용하는 이유 : 
// X 버튼이 눌러질 때 마다 order가 저장될 예정인데
// 중복되는 값을 저장 못하게 하기 위해서

// input type="file" 태그들
const inputImageList = document.getElementsByClassName("inputImage");

// X 버튼들
const deleteImageList = document.getElementsByClassName("delete-image");

// 마지막으로 선택된 파일을 저장할 배열
const lastValidFiles = [null, null, null, null, null];

/*
백업 저장 용 => 기존에 작성된 파일이 어딘가에 백업 되어있어야 함.
취소를 누르거나 문제가 있어 업로드되지 않는다면
기존 파일이 들어와야해서 백업이 필요
*/



/** 미리보기 함수
 * @param  file : <input type="file"> 에서 선택된 파일
 * @param {*} order : 이미지 순서
 */
const updatePreview = (file, order) => {

	// 선택된 파일이 지정된 크기를 초과한 경우 선택 막기
	const maxSize = 1024 * 1024 * 5; // 10MB를 byte 단위로 작성

	if (file.size > maxSize) { // 파일 크기 초과 시
		alert("10MB 이하의 이미지만 선택해 주세요");

		// 미리보기는 안되어도 크기가 초과된 파일이 선택되어 있음!!

		// 이전 선택된 파일이 없는데 크기 초과 파일을 선택한 경우
		if (lastValidFiles[order] === null) {
			inputImageList[order].value = ""; // 선택 파일 삭제
			return;
		}

		// 이전 선택된 다른 파일이 있을 때
		const dataTransfer = new DataTransfer();
		dataTransfer.items.add(lastValidFiles[order]);
		inputImageList[order].files = dataTransfer.files;

		return;
	}


	// 선택된 이미지 백업
	lastValidFiles[order] = file;

	// 현재 선택 파일 임지 URL 생성 후 미리보기 img 태그에 대입
	const newImageUrl = URL.createObjectURL(file) // 임시 URL 생성
	previewList[order].src = newImageUrl; // 미리보기 img 태그에 대입
	
	// deleteOrderList에서 해당 이미지 순서를 삭제
	// -> 왜?? 이전에 X 버튼을 눌러 삭제 기록이 있을 수도 있기 때문에
	deleteOrderList.delete(order);
/*
	updatePreview함수 내에 작성 => 업데이트를 일으킨 당시
	ex) 3번과 4번 사진을 삭제하면	deleteOrderList Set객체에 3과 4가 들어감 
	그런데 빠지는 것이 아니라 다른 사진으로 수정하려면 기존 Set객체 3과 4는 없애야 함
	다만 자바에서 set에 있는 요소를 삭제하기 위해서는 remove를 사용하지만 JS에서는 delete메서드를 통해 삭제한다
*/

}


/* input태그, x버튼에 이벤트 리스너 추가 */
for (let i = 0; i < inputImageList.length; i++) {

	// input 태그에 이미지 선택 시 미리보기 함수 호출
	inputImageList[i].addEventListener("change", e => {
		const file = e.target.files[0];

		if (file === undefined) { // 선택 취소 시

			// 이전에 선택한 파일이 없는 경우
			if (lastValidFiles[i] === null) return;


			//***  이전에 선택한 파일이 "있을" 경우 ***
			const dataTransfer = new DataTransfer();

			// DataTransfer가 가지고 있는 files 필드에 
			// lastValidFiles[i] 추가 
			dataTransfer.items.add(lastValidFiles[i]);

			// input의 files 변수에 lastVaildFile이 추가된 files 대입
			inputImageList[i].files = dataTransfer.files;

			// 이전 선택된 파일로 미리보기 되돌리기
			updatePreview(lastValidFiles[i], i);

			return;
		}

		updatePreview(file, i);
	})



	/* X 버튼 클릭 시 미리보기, 선택된 파일 삭제  DB랑 똑같은 순서*/
	deleteImageList[i].addEventListener("click", () => {

		previewList[i].src = ""; // 미리보기 삭제
		inputImageList[i].value = ""; // 선택된 파일 삭제
		lastValidFiles[i] = null; // 백업 파일 삭제

		// 기존에 존재하던 이미지가 있는 상태에서
		// X 버튼이 눌러 졌을 때
		// --> 기존에 이미지가 있었는데 
		//     i번째 이미지 X버튼 눌러서 삭제함 --> DELETE 수행
		if (orderList.includes(i)) { // 기존에 이미지가 있는 파트에서 X를 눌렀을 때
			deleteOrderList.add(i);
		} // 아무것도 없을 때 X버튼을 눌러도 아무것도 없음

	})

} // for end

// -----------------------------------------------------------------------


/* 제목, 내용 미작성 시 제출 불가 */
const form = document.querySelector("#boardUpdateFrm");
form.addEventListener("submit", e => {

	// 제목, 내용 input 얻어오기
	const boardTitle = document.querySelector("[name=boardTitle]");
	const boardContent = document.querySelector("[name=boardContent]");

	if (boardTitle.value.trim().length === 0) {
		alert("제목을 작성해주세요");
		boardTitle.focus();
		e.preventDefault();
		return;
	}

	if (boardContent.value.trim().length === 0) {
		alert("내용을 작성해주세요");
		boardContent.focus();
		e.preventDefault();
		return;
	}


	// 제출 전에 form 태그 마지막 자식으로
	// input을 동적으로 추가 한 후 제출
	// -> 해당 input에는
	// 위에서 Set객체로 만든 삭제된 이미지 순서(deleteOrderList)를 추가 => 파라미터로 보내자.

	const input = document.createElement("input");

	// 파라미터로 보내기 위해 input태그로 추가한다.


	// Array.from() : Set -> Array로 변환
	// 배열.toString() : [1,2,3] --> "1,2,3" 변환
	input.value = Array.from(deleteOrderList).toString();

	console.log("삭제된 이미지 리스트 : " + input.value);

	input.name = "deleteOrderList";
	input.type = "hidden";

	form.append(input); // 자식으로 input 추가

})






/*
[ * 게시글 수정 페이지 (boardUpdate.html) ]
┌────────────────────────────────────────────────────────────────────────────┐
│ <main>                                                                     │
│                                                                            │
│ ┌─ Header (공통) ──────────────────────────────────────────────────────┐ │
│ │ <th:block th:replace="~{common/header}">                              │ │
│ └──────────────────────────────────────────────────────────────────────┘ │
│                                                                            │
│ ┌─ <form id="boardUpdateFrm"> (게시글 수정 폼) ─────────────────────────┐ │
│ │  method="POST" action="update" th:object="${board}"                    │ │
│ │  enctype="multipart/form-data"                                         │ │
│ │                                                                        │ │
│ │  <h1 class="board-name" th:text="${boardName}">게시판 이름</h1>       │ │
│ │                                                                        │ │
│ │  ─ 제목 입력 영역 ────────────────────────────────────────────────┐ │
│ │  │ <input type="text" name="boardTitle" th:value="${board.boardTitle}">│ │
│ │  └─────────────────────────────────────────────────────────────────┘ │ │
│ │                                                                        │ │
│ │  ─ 썸네일 이미지 업로드 영역 (img0) ──────────────────────────────┐ │
│ │  │ <input type="file" id="img0" name="images"> (썸네일 전용)         │ │
│ │  │ <img class="preview"> <span class="delete-image">×</span>          │ │
│ │  └─────────────────────────────────────────────────────────────────┘ │ │
│ │                                                                        │ │
│ │  ─ 일반 이미지 업로드 영역 (img1~img4) ───────────────────────────┐ │
│ │  │ 반복적으로 img1~img4 업로드용 input 존재                         │ │
│ │  │ 각 이미지마다 preview + delete 버튼 존재                         │ │
│ │  └─────────────────────────────────────────────────────────────────┘ │ │
│ │                                                                        │ │
│ │  ─ 게시글 내용 입력 영역 ───────────────────────────────────────┐ │
│ │  │ <textarea name="boardContent" th:text="*{boardContent}">        │ │
│ │  └─────────────────────────────────────────────────────────────────┘ │ │
│ │                                                                        │ │
│ │  ─ 제출 버튼 영역 ───────────────────────────────────────────────┐ │
│ │  │ <button id="writebtn">등록</button>                            │ │
│ │  └─────────────────────────────────────────────────────────────────┘ │ │
│ │                                                                        │ │
│ │  ─ 현재 페이지(cp) hidden 처리 ──────────────────────────────────┐ │
│ │  │ <input type="hidden" name="cp" th:value="${param.cp}">         │ │
│ │  └─────────────────────────────────────────────────────────────────┘ │ │
│ └────────────────────────────────────────────────────────────────────────┘ │
│                                                                            │
│ ┌─ Footer (공통) ──────────────────────────────────────────────────────┐ │
│ │ <th:block th:replace="~{common/footer}">                              │ │
│ └──────────────────────────────────────────────────────────────────────┘ │
│                                                                            │
│ ┌─ 이미지 미리보기 스크립트 ─────────────────────────────────────────┐ │
│ │ <script th:inline="javascript">                                       │ │
│ │   이미지 목록(imageList) 받아서 previewList[imgOrder].src에 출력    │ │
│ └──────────────────────────────────────────────────────────────────────┘ │
│                                                                            │
└────────────────────────────────────────────────────────────────────────────┘

*/




document.addEventListener('DOMContentLoaded', () => {
  const mainSelect = document.getElementById('mainCategory');
  const chickenBox = document.getElementById('chickenBox');
  const pizzaBox = document.getElementById('pizzaBox');
  const chickenType = document.getElementById('chickenType');
  const pizzaType = document.getElementById('pizzaType');
  const resultSpan = document.getElementById('selectedResult');

  // 한글 매핑 객체
  const categoryMap = {
    chicken: '치킨',
    pizza: '피자'
  };

  const detailMap = {
    fried: '후라이드 치킨',
    yangnyeom: '양념 치킨',
    garlic: '갈릭 치킨',
    soy: '간장 치킨',
    cheese: '치즈 피자',
    pepperoni: '페퍼로니 피자',
    bulgogi: '불고기 피자',
    combination: '콤비네이션 피자'
  };

  // 선택 항목 업데이트 함수
  function updateResult() {
    const category = mainSelect.value;
    let detail = '';

    // 상세 항목 추출
    if (category === 'chicken') {
      detail = chickenType.value;
    } else if (category === 'pizza') {
      detail = pizzaType.value;
    }

    if (category && detail) {
      const categoryName = categoryMap[category] || category;
      const detailName = detailMap[detail] || detail;
      resultSpan.textContent = `${categoryName} - ${detailName}`;
    } else {
      resultSpan.textContent = '없음';
    }
  }

  // 카테고리 변경 시 처리
  mainSelect.addEventListener('change', () => {
    const value = mainSelect.value;

    chickenBox.style.display = 'none';
    pizzaBox.style.display = 'none';
    chickenType.disabled = true;
    pizzaType.disabled = true;
    chickenType.value = '';
    pizzaType.value = '';
    resultSpan.textContent = '없음';

    if (value === 'chicken') {
      chickenBox.style.display = 'block';
      chickenType.disabled = false;
    } else if (value === 'pizza') {
      pizzaBox.style.display = 'block';
      pizzaType.disabled = false;
    }
  });

  // 상세 항목 선택 시 결과 갱신
  chickenType.addEventListener('change', updateResult);
  pizzaType.addEventListener('change', updateResult);
});
