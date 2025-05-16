// console.log('웹소켓 테스터')

// 1. SockJS라이브러리 추가 (common.html 작성)


// 2. SockJS 객체를 생성
/*
 프로토콜이 다르므로 맨 앞이 애초에 http 자체가 아님!

 http://localhost가 아님
 ws://localhost와 같이 형성됨



 */


 const testSock = new SockJS("/testSock"); // handshake의 마무리
 //  ws://localhost/testSock



 // 생성된 SockJS객체를 이용한 메시지 전달


 const sendMessageFn = (name, str) => {

  /*JSON을 이용해서 서버에 데이터를 텍스트 형태로 전달
 const obj = { 
    "name": name,
    "str": str
  };
  */
  const obj = { 
    "name": name,
    "str": str
  };

  // 클라이언트가 sockJS를 이용해 서버에게 JSON으로 변환한 obj 객체를 전송함
  testSock.send(JSON.stringify(obj));

 }

 // 인터셉터 => handleTextMessage에서 처리한 내용이 이벤트 객체로서 넘어온다



 // 4. 서버로부터 현재 클라이언트에게 웹소켓을 이용한 메시지가 전달된 경우에 대해 처리한다


 testSock.addEventListener("message", e =>{
  // e.data란? 서버로부터 전달받은 메시지가 들어가있어야 한다.
  // 서버로부터 json으로 넘어오지만 js객체로 변환해야 사용할 수 있다.

  const msg = JSON.parse(e.data);

  console.log(`${msg.name}:${msg.str}`);



 });