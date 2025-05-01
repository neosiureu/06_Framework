/* 선택된 이미지 미리보기 관련 요소 모두 얻어오기 */

const previewList = document.querySelectorAll(".preview"); // img 태그 5개
const inputImageList = document.querySelectorAll(".inputImage"); // input 태그 5개
const deleteImageList = document.querySelectorAll(".delete-image"); // x버튼 5개

// 마지막으로 선택된 파일을 저장할 배열
const lastValidFiles = [null, null, null, null, null];


/** 미리보기 함수
 * @param  file : <input type="file"> 에서 선택된 파일
 * @param  order : 이미지 순서
 */
const updatePreview = (file, order) => {

  // 선택된 파일이 지정된 크기를 초과한 경우 선택 막기
  const maxSize = 1024 * 1024 * 10; // 10MB를 byte 단위로 작성

  if(file.size > maxSize){ // 파일 크기 초과 시
    alert("10MB 이하의 이미지만 선택해 주세요");

    // 미리보기는 안되어도 크기가 초과된 파일이 선택되어 있음!!

    // 이전 선택된 파일이 없는데 크기 초과 파일을 선택한 경우
    if(lastValidFiles[order] === null){
      inputImageList[order].value = ""; // 선택 파일 삭제
      return;
    }

    // 이전 선택된 파일이 있는데 크기 초과 파일을 선택한 경우
    const dataTransfer = new DataTransfer();
    dataTransfer.items.add(lastValidFiles[order]);
	// 이전에 유효했던 파일을 저장한 배열(lastValidFiles)에서 특정 순서(order)의 파일을 꺼내어 추가함
    inputImageList[order].files = dataTransfer.files;
	// DataTransfer 객체에 추가된 파일 리스트를 input태그(파일) 리스트에 대입
	
    return;
  }

  // 파일 크기 초과 안했을 때 (유효한 크기인 경우)
  // 현재 선택된 이미지 백업(기록해두기)
  lastValidFiles[order] = file;

  // 현재 선택 파일 임지 URL 생성 후 미리보기 img 태그에 대입
  const newImageUrl = URL.createObjectURL(file) // 임시 URL 생성
  previewList[order].src = newImageUrl; // 미리보기 img 태그에 대입
  
}



// ----------------------------------------------------------------

/* input태그, x버튼에 이벤트 리스너 추가 */
for (let i = 0; i < inputImageList.length; i++) {

  // input 태그에 이미지 선택 시 미리보기 함수 호출
  inputImageList[i].addEventListener("change", e => {
    const file = e.target.files[0];

    if (file === undefined) { // 선택 취소 시

      // 이전에 선택한 파일이 없는 경우
      if (lastValidFiles[i] === null) return;


      //***  이전에 선택한 파일이 "있을" 경우 (== 이전에 정상 선택 후 재선택을 취소하는 경우) ***
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

	// 파일을 재선택한 경우 updatePreview 이용하여 업데이트
    updatePreview(file, i);
  })



  /* X 버튼 클릭 시 미리보기, 선택된 파일 삭제 */
  deleteImageList[i].addEventListener("click", () => {

    previewList[i].src      = ""; // 미리보기 삭제
    inputImageList[i].value = ""; // 선택된 파일 삭제
    lastValidFiles[i]       = null; // 백업 파일 삭제
  })


} // for end


/* 제목, 내용 미작성 시 제출 불가 */
const form = document.querySelector("#boardWriteFrm");
form.addEventListener("submit", e => {

  // 제목, 내용 input 얻어오기
  const boardTitle   = document.querySelector("[name=boardTitle]");
  const boardContent = document.querySelector("[name=boardContent]");

  if(boardTitle.value.trim().length === 0){
    alert("제목을 작성해주세요");
    boardTitle.focus();
    e.preventDefault();
    return;
  }

  if(boardContent.value.trim().length === 0){
    alert("내용을 작성해주세요");
    boardContent.focus();
    e.preventDefault();
    return;
  }
})