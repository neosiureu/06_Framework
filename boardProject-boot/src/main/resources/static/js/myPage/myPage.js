
// 다음주소 API 다루기
function execDaumPostcode() {
    new daum.Postcode({
        oncomplete: function (data) {
            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분. (처음 화면에서 뭘 클릭했는가에 따라 어떻게 변할래)

            // 각 주소의 노출 규칙에 따라 주소를 조합한다.
            // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
            var addr = ''; // 주소 변수

            //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
            // userSelectedType이 클릭한 대상에 따라 반환하는 값이 다름
            if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                addr = data.roadAddress;
            } else { // 사용자가 지번 주소를 선택했을 경우(J)
                addr = data.jibunAddress;
            }


            // 우편번호와 주소 정보를 해당 필드에 넣는다.
            document.getElementById('postcode').value = data.zonecode; // html상에 넣는다
            document.getElementById("address").value = addr;
            // 커서를 상세주소 필드로 이동한다.
            document.getElementById("detailAddress").focus();
        }
    }).open();
}


const searchAddress = document.querySelector("#searchAddress");

if (searchAddress) {
    searchAddress.addEventListener('click', execDaumPostcode);
}


/* 회원 정보 수정 페이지 */
const updateInfo = document.querySelector("#updateInfo"); // form 태그

// #updateInfo 요소가 존재 할 때만 수행
if (updateInfo != null) {




    // form 제출 시
    updateInfo.addEventListener("submit", async e => {

        e.preventDefault(); // 제출을 처음에 막고

        const memberNickname = document.querySelector("#memberNickname");
        const memberTel = document.querySelector("#memberTel");
        const memberAddress = document.querySelectorAll("[name='memberAddress']");

        // 닉네임 유효성 검사
        if (memberNickname.value.trim().length === 0) {
            alert("닉네임을 입력해주세요");
            // e.preventDefault(); // 제출 막기
            return;
        }


        // 닉네임 정규식에 맞지 않으면
        let regExp = /^[가-힣\w\d]{2,10}$/;
        if (!regExp.test(memberNickname.value)) {
            alert("닉네임이 유효하지 않습니다.");
            // e.preventDefault(); // 제출 막기
            return;
        }

        // *********** 닉네임 중복검사는 같이 하자. 비동기 요청을 fetch말고 다르게 해보려고 ***********
        // ***********************************************************


        // 기존 닉네임이 저장된 요소에 value값 얻어오기
        const currentNickname = document.querySelector('#currentNickname').value;

        if (currentNickname !== memberNickname.value) {
            // 중복검사 로직의 작성
            // 비동기 요청 (fetch API)

            /*
            async await 사용 
            async: 비동기함수를 만들 때 사용되는 키워드 ("이함수 내에는 시간이 걸리는 작업이 존재합니다")
            await: 비동기작업의 결과를 기다릴 떄 사용하는 키워드

            반드시 async 함수 안에서만 사용 가능하며 await이라는 이름과 같이 해당 작업이 끝날때까지 ??????
            .then과 같은 메서드 체이닝이 필요 없고 변수에 값을 담아두는 식으로 진행

            */



            const resp = await fetch("/member/checkNickname?memberNickname" + memberNickname.value);


            const count = await resp.text(); // 0이나 1이 반환 됨


            if (count == 1) {
                alert('이미 사용중인 닉네임입니다');
                // e.preventDefault();
                return;
            }



            alert('수정 되었습니다!');

        }


        // 기존 닉네임과 새로 입력된 닉네임이 다르면 중복검사를 시도하지만
        // 같으면 변경된 적이 없으므로 중복검사를 진행하지 않는다


        /* 
             <div class="myPage-row">
            <label>닉네임</label>

            <input type="text" name="memberNickname" 
              maxlength="10" id="memberNickname"
              th:value="${session.loginMember.memberNickname}"
              >

              <!-- @SessionAttributes({"loginMember"})를 멤버 컨트롤러에서 했기 때문에 이렇게 가능함-->

          </div>
        
        */





        // 전화번호 유효성 검사
        if (memberTel.value.trim().length === 0) {
            alert("전화번호를 입력해 주세요");
            // e.preventDefault();
            return;
        }

        // 전화번호 정규식에 맞지 않으면
        regExp = /^01[0-9]{1}[0-9]{3,4}[0-9]{4}$/;
        if (!regExp.test(memberTel.value)) {
            alert("전화번호가 유효하지 않습니다");
            // e.preventDefault(); //  e.preventDefault()를 비동기요청과 같이 사용해선 안 됨
            // 비동기 요청이 자기 갈길을 가기 때문에 prevent가 되지 않는다.
            return;
        }


        // 주소 유효성 검사
        // 입력을 안하면 전부 안해야되고
        // 입력하면 전부 해야된다

        const addr0 = memberAddress[0].value.trim().length == 0; // t/f
        const addr1 = memberAddress[1].value.trim().length == 0; // t/f
        const addr2 = memberAddress[2].value.trim().length == 0; // t/f

        // 모두 true 인 경우만 true 저장
        const result1 = addr0 && addr1 && addr2; // 아무것도 입력 X

        // 모두 false 인 경우만 true 저장
        const result2 = !(addr0 || addr1 || addr2); // 모두 다 입력

        // 모두 입력 또는 모두 미입력이 아니면
        if (!(result1 || result2)) {
            alert("주소를 모두 작성 또는 미작성 해주세요");
            // e.preventDefault();
            return;
        }

        updateInfo.submit();



        /* 

        submit이 일어나는 이벤트 함수 내에서 fetch를 이용한 비동기 함수를 실행중일 때 
        e.preventDefault()가 비동기 함수 이후에 호출되면 이미 form이 제출되어버린 다음에, 
        즉 비동기로 할거 다 한 후일 수 있기 떼문에 prevent가 안된다

        -> 비동기 작업 중에 form의 기본 제출 동작이 일어날 가능성이 생긴다

        
        
        */


    });
}



// ------------------------------------------

/* 비밀번호 수정 */

// 비밀번호 변경 form 태그
const changePw = document.querySelector("#changePw");

if (changePw != null) { // 전역에서 선언된 변수인데 다른 파일에서 이 JS코드들에 접근했을 때 NPE가 나지 않게 하기 위함
    // 현재 페이지에서 changePw요소가 존재할때만 수행

    // 제출 되었을 때
    changePw.addEventListener("submit", e => {

        const currentPw = document.querySelector("#currentPw");
        const newPw = document.querySelector("#newPw");
        const newPwConfirm = document.querySelector("#newPwConfirm");

        // - 값을 모두 입력했는가


        let str; // undefined 상태
        if (currentPw.value.trim().length == 0) str = "현재 비밀번호를 입력해주세요";
        else if (newPw.value.trim().length == 0) str = "새 비밀번호를 입력해주세요";
        else if (newPwConfirm.value.trim().length == 0) str = "새 비밀번호 확인을 입력해주세요";

        if (str != undefined) { // str에 값이 대입됨 == if 중 하나 실행됨
            alert(str);
            e.preventDefault();
            return;
        }

        // 새 비밀번호 정규식
        const regExp = /^[a-zA-Z0-9!@#_-]{6,20}$/;

        if (!regExp.test(newPw.value)) {
            alert("새 비밀번호가 유효하지 않습니다");
            e.preventDefault();
            return;
        }

        // 새 비밀번호 == 새 비밀번호 확인
        if (newPw.value != newPwConfirm.value) {
            alert("새 비밀번호가 일치하지 않습니다");
            e.preventDefault();
            return;
        }
    });
};

// -------------------------------------
/* 탈퇴 유효성 검사 */

// 탈퇴 form 태그
const secession = document.querySelector("#secession");
// 

if (secession != null) {

    secession.addEventListener("submit", e => {

        const memberPw = document.querySelector("#memberPw");
        const agree = document.querySelector("#agree");

        // - 비밀번호 입력 되었는지 확인
        if (memberPw.value.trim().length == 0) {
            alert("비밀번호를 입력해주세요.");
            e.preventDefault(); // 제출막기
            return;
        }

        // 약관 동의 체크 확인
        // checkbox 또는 radio checked 속성
        // - checked -> 체크 시 true, 미체크시 false 반환


        // HTML에서 checked라고 하면 체크가 되었을 때 true false 판단 가능

        if (!agree.checked) { // 체크 안됐을 때
            alert("약관에 동의해주세요");
            e.preventDefault();
            return;
        }



        // 정말 탈퇴하시겠습니까? 물어보기 => 예 아니오 = confirm
        if (!confirm("정말 탈퇴 하시겠습니까?")) {
            alert("취소 되었습니다.");
            e.preventDefault();
            return;
        }

        // 여기까지 오면 
    });
}



// -------------------------------------------------------
// 이미지 업로드 구간

/*  [input type="file" 사용 시 유의 사항]

  1. 파일 선택 후 취소를 누르면 
    선택한 파일이 사라진다  (value == '')


  2. value로 대입할 수 있는 값은  '' (빈칸)만 가능하다



  3. 선택된 파일 정보를 저장하는 속성은
    value가 아니라 files이다
*/








// 폼태그가 있을 때만으로 한정


const profileForm = document.getElementById("profile");
const profileImg = document.getElementById("profileImg");  // 미리보기 이미지 img
const imageInput = document.getElementById("imageInput");  // 이미지 파일 선택 input
const deleteImage = document.getElementById("deleteImage");  // 이미지 삭제 버튼
const MAX_SIZE = 1024 * 1024 * 5;  // 최대 파일 크기 설정 (5MB)


if (profile != null) {
    // 요소 참조
    //   // 프로필 form
   

    // 절대경로 방식으로 정적 리소스에 대한 기본 이미지 URL 설정
    const defaultImgUrl = `${window.location.origin}/images/user.png`;
    // http://localhost/images/user.png까지 origin주소가 나옴 (원래 포트번호도)



    // 관련 변수 셋

    let statusCheck = -1; // 변경 상태가 있긴 한지. 
    // 0이면 이미지를 삭제했다. 1이면 새 이미지를 선택했다

    let previousImage = profileImg.src;  
    // 이전 이미지의 URL 기록 => 취소해도 비어있지 않고 원래대로 돌아가라고
    // img태그 src에 작성하는 값은 미리보기 이미지를 띄울 URL주소 (선택 전에 취소 가능)

    let previousFile = null; 
    // type= file인 input 태그가 작성할 값 = 서버에  실제로 제출될 파일 객체



    // img태그 src에 작성하는 값은 미리보기 이미지를 띄울 URL주소 (선택 전에 취소 가능)
    // type= file인 input 태그가 작성할 값 = 서버에  실제로 제출될 파일 객체
    // 이 둘은 완전히 다름!!


    // 변했다면
    imageInput.addEventListener('change' , ()=> {
        // change이벤트: 기존에 있던 값과 달라지면 발생 됨
        // console.log('imgaeInput.files');


        const file=imageInput.files[0];

        console.log(file);
    // 이미지 선택 시 미리보기 및 파일 크기를 검사한다.


        if(file){
            // 파일이 취소되지 않고 선택된 경우

            if(file.size <=  MAX_SIZE){ // 현재 선택한 파일의 크기가 5MB 이하인 경우

                // 임시 URL을 생성해주는 URL인터페이스

                const newImageUrl = URL.createObjectURL(file);
                // 이 파일의 임시 URL을 생성해줌
                console.log(newImageUrl);

                profileImg.src = newImageUrl; // 미리보기 이미지 설정 


                // 후처리 작업: 잘 선택한 경우 previous에 기록
                previousImage = newImageUrl; // 현재 선택된 이미지를 이전 이미지로 (다음에 바뀔 일에 대비해) 미리 저장해둔다.
                previousFile = file; // 현재 선택된 파일 객체를 이전 파일로   (다음에 바뀔 일에 대비해) 다시 저장해둔다.
                statusCheck=1; // 새 이미지 선택 상태 기록


            }

            else{ // 현재 선택한 파일의 크기가 5MB 초과인 경우
                alert('5MB이하의 이미지를 선택해주세요!');

                imageInput.value = ""; // 파일 선택을 초기화 함
                profileImg.src = previousImage; //이전 미리보기 이미지로 복원
                // 파일 입력 복구 (이전 파일이 존재하면 다시 할당)

                if(previousFile){
                    const dataTransfer = new DataTransfer();
                    dataTransfer.items.add(previousFile);
                    imageInput.files = dataTransfer.files;
                }



                
            }


        }


        else{
            // 취소를 누른 경우
            // 이전 이미지를 복원하기  (img태그) + 이전 선택한 파일(input)로 복원 
            profileImg.src = previousImage;
            
            if(previousFile) // 이전파일이 존재하긴 한다면
            {
                const dataTransfer = new DataTransfer();
                // DataTransfer() => 자바스크립트로 파일을 조작할 때 사용하는 인터페이스 
                // file들을 모아 input에 대입할 것


                // => <input type = "file">요소에 파일을 동적으로 설정
                // input태그 속성을 files로 두면 FileList만 저장 가능하다
                // DataTransfer를 이용하여 현재 File객체를 FileList로 변환하여 할당하기로 한다

                // DataTransfer.files: FileList 객체를 반환
                // DataTransfer.items.add(): 실제로 파일을 추가
                // DataTransfer.items.remove(): 실제로 파일을 추가

                dataTransfer.items.add(previousFile); // 복원될 파일
                imageInput.files = dataTransfer.files;
                // value로는 넣을 수 없다.



            }




        }

    });
    


    /* 
 <form action="profile" method="POST" name="myPageFrm" id="profile" enctype="multipart/form-data">
          <div class="profile-image-area">

            <img th:with="user=#{user.default.image}"
                th:src="${session.loginMember.profileImg ?: user}"
                id="profileImg" >
                // 프로필 사진 자체
          </div>
          <span id="deleteImage">x</span>
          <div class="profile-btn-area">
            <label for="imageInput">이미지 선택</label>

            <input type="file" name="profileImg" id="imageInput" accept="image/*">
           // 실제로 파일을 기억할 놈
            <button>변경하기</button>
          </div>
        </form>


*/

    
deleteImage.addEventListener("click",()=>{
    if(profileImg.src !== defaultImgUrl) // 기본이미지 상태가 아닐때만
    {
        imageInput.value = ""; // 서버에 제출할 input태그 파일 값을 초기화 
        profileImg.src= defaultImgUrl; // 현재 미리보기를 기본으로 변경
        statusCheck = 0;
        previousImage = defaultImgUrl; // 초기값으로 => 이전 이미지 기록하는 변수를 기본이미지로
        previousFile = null; // 초기값으로 => 이전 파일 기록하는 변수를 null로
    }

    else
    {
        statusCheck = -1; // 기본 이미지 상태에서 삭제 버튼 클릭 시 상태를 변경하지 않는다.
    }


}); // 클릭 이벤트 발생 시


// 폼 제출 시 유효성 검사 
profileForm.addEventListener("submit", e=>{
    if(statusCheck==-1){
        // 변경사항 없는 경우 제출
        e.preventDefault();
        alert('이미지 변경 후 제출하세요!')
    }
})

}



// 1. 파일 선택 후 취소를 누르면 선택한 파일이 사라진다  (value == '')


// 2. value로 대입할 수 있는 값은  '' (빈칸)만 가능하다




// 3. 선택된 파일 정보를 저장하는 속성은 value가 아니라 files이다