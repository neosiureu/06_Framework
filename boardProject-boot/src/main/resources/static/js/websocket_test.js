// 웹소켓 테스트

// 클라이언트 과정
// 1. SockJS 라이브러리 추가(common.html 작성)

// 2. SockJS 객체를 생성
// ws:// -> 웹소켓 프로토콜
// -> ws://localhost(또는 ip)

const testSock = new SockJS("/testSock");
// ws://localshost/testSock

//3. 생성된 SockJS 객체를 이용해서 메시지 전달
const sendMessageFn = (name, str) => {
  
  // JSON을 이용해서 데이터를 TEXT 형태로 전달
  const obj = {
    "name" : name ,
     "str" : str
  };

  //클라이언트가 sockJS를 이용해 서버에게 obj(json)을 전송함
  testSock.send(JSON.stringify(obj));
}

//4. 서버로 부터 현재 클라이언트에게
// 웹소켓을 이용한 메시지가 전달된 경우
testSock.addEventListener("message" , (e)=>{

  //e.data : 서버로 전달받은 message 
  // JSON -> JS Object 형태로 변환
  const msg = JSON.parse(e.data);
  console.log(`${msg.name} : ${msg.str}`);
})



