package edu.kh.demo.controller;



import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.kh.demo.model.dto.MemberDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Controller // 요청 / 응답 제어 역할 명시 + Bean 등록
@RequestMapping("param")  // /param으로 시작하는 요청을 현재 컨트롤러로 매핑
@Slf4j // log를 이용한 메시지 콘솔창에 출력할 때 사용 (lombok 제공)
public class ParameterController {
	
	
	
	
	@GetMapping("main") //      /param/main GET 방식 요청 매핑
	public String paramMain() {
		
		//src/main/resources/templates/param/param-main.html 로 forward
				                  //접두사 는 templates 까지 
		                          // 접미사는 .html
		
		return"param/param-main";
	}
	
	/*
	 * 1. HttpServletRequest : 
	 * - 요청 클라이언트의 정보 , 제출된 파라미터 등을 저장한 객체
	 * - 클라이언트 요청 시 생성
	 * 
	 * Spring 의 Controller 메서드 작성시
	 * 매개변수에 원하는 객체를 작성하면
	 * 존재하는 객체를 바인딩 또는 없으면 생성해서 바인딩
	 * 
	 * --> ArgumentResolver(전달 인자 해결사) 
	 * */
	
	@PostMapping("test1") // /param/test1 의 post 방식 요청 매핑
	public String paramTest1(HttpServletRequest req) { // -< bean으로 등록된건 아님 .
		
		
	String inputName = req.getParameter("inputName");
	String inputAddress = req.getParameter("inputAddress");
	int inputAge = Integer.parseInt(req.getParameter("inputAge"));
	
	log.debug("inputName : "+inputName);
	log.debug("inputAddres : "+inputAddress);
	log.debug("inputAge : "+inputAge);
	
	/*
	 * Spring에서 Redirect(재요청) 하는 방법!
	 * 
	 * - Controller 메서드 반환값에
	 *  "redirect:요청주소"; 작성
	 * 
	 * */
	
		return "redirect:/param/main";
	}

	
	/*2. @RequestParam 어노테이션 - 낱개 파라미터 얻어오기
	 * 
	 * - request 객체를 이용한 파라미터 전달 어노테이션
	 * - 매개변수 앞에 해당 어노테이션을 작성하면 , 매개변수에 값이 주입됨.
	 * - 주입되는 데이터는 매개변수의 타입에 맞게 형변환이 자동으로 수행됨.
	 * 
	 * [기본 작성법]
	 * @RequestParam("key") 자료형 매개변수명
	 * 
	 * [속성 추가 작성법]
	 * @Requestparam(value="key" , required=false, defaultValue="1")
	 * 
	 * value : 전달받은 input 태그의 name속성 값 (파라미터 key)
	 * required : 입력된 name 속성 값 파라미터 필수 여부 지정(기본 값 true)
	 * -> required = true 인 파라미터가 존재하지 않는다면 400 (Bad Request) 에러 발생
	 * -> " " (빈문자열) 일 때는 에러 발생 X (파라미터가 존재하지 않는것이 아니라)
	 *              name 속성값 = "" 로 넘어오기때문에 )
	 * 
	 * defaultValue : 파라미터 중 일치하는 name 속성값이 없을 경우에 대입할 값 지정.
	 * -> required = false 인 경우 사용 
	 * 
	 * 
 	 * */
	
	@PostMapping("test2") //  /param/test2  POST 방식 요청 매핑하는 메서드다!.
	public String paramTest2(@RequestParam("title") String title, 
			                 @RequestParam("writer") String writer ,
		                     @RequestParam("price") int price, // 형변환이 필요한 경우에는 파라미터 value 값 필수 작성되어야함!
		                     @RequestParam(value ="publisher", required=false , defaultValue="") String publisher) {
		
		log.debug("title :"+title);
		log.debug("writer : "+writer);
		log.debug("price:"+price);
		log.debug("publisher : "+publisher);
		
		return"redirect:/param/main";// 리다이렉트할때는 서버주소를 입력해야함.
	}

	// 3. @RequestParam 여러개 파라미터
	// - 같은 name 속성값을 가진 파라미터 얻어오기 ( String[], List<String>)
	// - 제출된 파라미터 한번에 묶어서 얻어오기 ( Map<String,Object> )

	@PostMapping("test3")
	public String paramTest3(@RequestParam("color") String[] colorArr, 
	                         @RequestParam("fruit") List<String> fruitList,
	                         @RequestParam Map<String,Object> paramMap)
							//MultiValueMap 을 쓰면 여러개의값도 가지고올수잇음.
	{ 
           log.debug("colorArr : " + Arrays.toString(colorArr));
           log.debug("fruitList :"+ fruitList); 
           
           //@RequestParam Map<Sring,Object> paramMap
           // -> 제출된 모든 파라미터가 Map에 저장된다 
           // -> 같은 name 속성을 가진 파라미터는 String[], List로 저장 X
           //     첫번째로 제출된 value 값이 저장됨
           log.debug("paramMap : " + paramMap);

		return"redirect:/param/main";
	}

	//4. @ModelAttribute 를 이용한 파라미터 얻어오기
	// @ModelAttribute
	// - DTO (또는VO)와 같이 사용하는 어노테이션
	
	// 전달받은 파라미터의 name 속성값이
	// 함께 사용되는 DTO의 필드명과 같다면
	// 자동으로 Setter 를 호출해서 필드에 값을 저장
	
	// **주의사항 **
	// - DTO 기본 생성자 필수로 존재해야한다!
	// - DTO setter가 필수로 존재해야한다!
	
	// @ModelAttribute 를 이용해 값이 필드에 세팅된 객체를
	// "커맨드 객체" 라고 부른다.
	
	// @ModelAttribute 생략 가능 ! ???
	
	
	@PostMapping("test4")
	public String paramTest4(/*@ModelAttribute */MemberDTO member) {
		
		log.debug("member :" + member);
		
		
		return"redirect:main"; //상대경로 작성법 (현재위치가 중요!)
		     //현재경로의 가장 마지막 부분을 redirect:이주소 로 변경하기 때문에
		            
	}
	
	
}

