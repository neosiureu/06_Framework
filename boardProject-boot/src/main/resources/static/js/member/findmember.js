const findMember = document.querySelector("#findMember");


findMember.addEventListener("submit" , (e) => {
  
  const memberNickname = document.querySelector("#memberNickname");
  const memberTel = document.querySelector("#memberTel");
 
  if(memberNickname.value.trim().length == 0){
    alert("닉네임을 작성해주세요.");
    e.preventDefault();
    return;
  }

  if(memberTel.value.trim().length == 0){
    alert("email칸을 작성해주세요");
    e.preventDefault();
    return;
  }


});
  
