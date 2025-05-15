// /js/member/findPw.js 파일 내용

// --- 비밀번호 찾기 관련 JavaScript ---

// 필요한 요소들 가져오기
// 이 스크립트가 <body> 끝에서 로드되므로, 페이지의 모든 HTML 요소에 접근 가능합니다.
const findPwForm = document.querySelector('#findPwForm'); // 비밀번호 찾기 폼
const findPwMessage = document.querySelector('#findPwMessage'); // 비밀번호 찾기 메시지 영역

// 결과 표시 영역 및 닫기 버튼 (아이디/비밀번호 찾기 결과 모두 표시)
const findedResultArea = document.querySelector('#finded'); // 결과 표시 영역 (#finded는 이제 class="hidden"으로 항상 존재)
const findedResultTextElement = document.querySelector('#findedResultText'); // 결과 영역 안의 텍스트 (<p>)
const closeFindIDPWButton = document.querySelector('#closeFindIDPW'); // 결과 영역의 X 버튼

// #hidden 영역 전체 (폼과 메시지 영역을 모두 포함하는 부모)
// 이 영역은 보통 페이지 로드 시 숨겨져 있다가 'ID/PW 찾기' 링크 클릭 시 보임
// findPw.js 자체는 이 #hidden을 보이게 하는 로직은 처리하지 않지만,
// X 버튼 클릭 시 #hidden을 숨기거나, 결과 표시 후 #hidden을 숨길 때 필요합니다.
const hiddenArea = document.querySelector('#hidden'); // 이 요소는 main.js 등에서 보이게 관리할 것입니다.


// --- 비밀번호 찾기 폼 제출(submit) 시 AJAX 요청 보내기 ---
// findPwForm 요소가 HTML에 있다면 (즉, th:if 조건과 상관없이 form이 있다면) 리스너 추가
if (findPwForm) {
    findPwForm.addEventListener('submit', (e) => {
        e.preventDefault(); // **폼의 기본 제출 동작(페이지 이동)을 막습니다.**

        // 메시지 초기화
        if (findPwMessage) { findPwMessage.textContent = ''; }
        // 결과를 새로 표시하기 전에 기존 결과는 숨깁니다.
        if (findedResultArea) { findedResultArea.classList.add('hidden'); }


        // 폼 데이터 수집 (FormData 객체 사용이 편리)
        const formData = new FormData(findPwForm);

        // fetch API를 이용한 POST 요청
        // form의 action과 method 속성을 fetch 옵션으로 그대로 사용합니다.
        fetch(findPwForm.action, {
            method: findPwForm.method,
            body: formData
        })
        .then(response => {
            // 서버 응답의 상태 코드를 확인하여 오류 처리
            if (!response.ok) {
                 // 예: 400번대, 500번대 오류 발생 시
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            // 응답 본문을 텍스트로 변환하여 다음 .then으로 전달
            return response.text(); // 컨트롤러에서 String을 반환할 것이므로 text() 사용
        })
        .then(resultText => {
            // 서버 응답 (비밀번호 찾기 결과 문자열 또는 상태 메시지)
            console.log("서버 응답 (비밀번호 찾기 결과):", resultText);

            // 서버 응답 (resultText) 값을 확인하여 성공/실패 판단
            // 서버에서 "NOT_FOUND" 또는 null 등을 반환한다고 가정
            if (resultText.trim() === 'EMAIL_SENT') {

                alert("임시 비밀번호가 메일로 발송되었습니다. 이메일을 확인해주세요.");
                // 결과 영역 (#findedResultTextElement) 내용 업데이트
                if (findedResultTextElement) {
                    // <p id="findedResultText"> 태그의 내용을 전체 변경
                    // 서버에서 임시 비밀번호 ("TEMP_PW_12345") 등을 받았다면:
                    findedResultTextElement.textContent = "비밀번호가 발송되었습니다. 해당 이메일을 확인하세요.";
                    // 서버에서 '이메일 발송 성공' 등 상태 메시지를 받았다면:
                    // findedResultTextElement.textContent = resultText;
                } else {
                     console.error("#findedResultTextElement 요소를 찾을 수 없습니다. HTML을 확인해주세요.");
                }


                // 결과 영역 (#finded)을 화면에 보이게 합니다.
                if (findedResultArea) {
                     findedResultArea.classList.remove('hidden');
                } else {
                     console.error("#finded 요소를 찾을 수 없습니다. HTML을 확인해주세요.");
                }


                // 비밀번호 찾기 폼 영역 (#hidden)은 숨깁니다. (결과를 보여주니 폼은 숨김)
                if (hiddenArea) { // hiddenArea가 존재할 때만
                     hiddenArea.classList.add('hidden');
                }


            } else { // 서버가 'null', 'NOT_FOUND' 등 찾지 못했음을 알리는 문자열을 반환한 경우
                // **일치하는 회원 정보를 찾지 못한 경우**

                // 실패 메시지 영역 (#findPwMessage)에 메시지 표시
                if (findPwMessage) {
                     findPwMessage.textContent = '일치하는 회원 정보를 찾을 수 없습니다.'; // 또는 서버에서 온 오류 메시지 사용
                }

                // 결과 영역 (#finded)은 숨깁니다. (혹시 보이고 있었다면)
                if (findedResultArea) {
                     findedResultArea.classList.add('hidden');
                }

                 // 폼 영역 (#hidden)은 계속 보이도록 둡니다.
                 if (hiddenArea) { // hiddenArea가 존재할 때만
                     hiddenArea.classList.remove('hidden');
                }
            }
        })
        .catch(error => {
            // 네트워크 오류 등 요청 자체 실패 시
            console.error('비밀번호 찾기 중 오류 발생:', error);
            if (findPwMessage) {
                findPwMessage.textContent = '비밀번호 찾기 중 오류가 발생했습니다.';
            }
             if (findedResultArea) {
                findedResultArea.classList.add('hidden');
            }
             if (hiddenArea) {
                 hiddenArea.classList.remove('hidden');
            }
        });
    });
}


// --- X 버튼 (#closeFindIDPW) 클릭 시 hidden과 finded 영역 숨기기 ---
// 이 로직은 아이디 찾기 결과든 비밀번호 찾기 결과든 동일하게 동작해야 합니다.
// 이 JS 파일 (findPw.js)에서 필요한 요소들을 가져와 처리합니다.
// closeFindIDPWButton은 #finded 안에 있지만, finded가 class="hidden"으로 항상 존재하므로 이 스크립트에서 접근 가능합니다.
if(closeFindIDPWButton){ // X 버튼이 HTML에 존재할 때만 리스너 추가
    closeFindIDPWButton.addEventListener('click', () => {
        // hidden 폼 전체 영역 숨기기
        if (hiddenArea) {
             hiddenArea.classList.add('hidden');
        }
        // 결과 영역 숨기기
        if (findedResultArea) {
             findedResultArea.classList.add('hidden');
        }
         // 메시지 초기화
        if (findPwMessage) { findPwMessage.textContent = ''; } // 비밀번호 찾기 메시지
        // if (findIdMessage) { findIdMessage.textContent = ''; } // 아이디 찾기 메시지도 필요하다면 여기서 초기화 (main.js에 있을 수도 있음)
    });
}