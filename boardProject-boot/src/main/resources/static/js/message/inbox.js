document.querySelector('.delete').addEventListener('click', (e)=>{

  const isconfirm = confirm('이 쪽찌를 삭제하시겠습니까?');
  if(!isconfirm)
  e.preventDefault();



})