package edu.kh.demo.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.kh.demo.model.dto.MemberDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import java.util.*;

@Controller // 요청 응답 제어 역할 명시 + Bean (객체)로서 이 클래스를 등록하겠다
@RequestMapping("param") // 슬래시 안 붙임!!
// "param"으로 시작하는 요청을 현재 컨트롤러로 매핑
@Slf4j //롬복에서 제공하는 어노테이션. 이는 log를 이용한 메시지를 콘솔창에 출력할 때 사용
public class ParameterController {
	
	// html을 보니 @main인 메서드를 따로 만들겠군
	
	@GetMapping("main") // param/main으로 온 GET 방식 요청을 매핑한다
	public String  paramName() {
	
		
	return "param/param-main"; // 이는 다음 경로와 똑같음
	// (src/main/resources/templates/) param /param-main (.html)
	// 포워드 시에는 파일의 경로 (리다이렉트 시에는 서버의 주소)
		
	}
	
	/* 1) HttpServletRequest는 요청된 클라이언트의 정보, 제출된 파라미터 등을 저장한 객체
	 * 클라이언트 요청 시 생성되며 원래는 extends받았었음
	 * 다만 @Controller과 같이 Spring의 컨트롤러에 대한 어노테이션이 달린 메서드 작성 시 
	 * 매개변수에 원하는 객체를 생성하면 
	 * 존재하는 객체를 바인딩한다. 또는 해당 객체가 없으면 스스로 생성하여 바인딩한다
	 * 이를 ArgumentResolver라 한다
	 * */
	
	// param/test1 Post방식 요청을 매핑했다는 의미
	
	@PostMapping("test1") 
	public String paramTest1(HttpServletRequest req) { // 이러한 객체는 기본적으로 bean으로 등록되어있지 않지만 알아서 만들어 줌
		// 주입을 받아서 get을 하게 된다. => 그냥 인자로 필요한 객체를 막 쓰면 된다. (by argument resolver)
		String inputName = req.getParameter("inputName");
		String inputAddress = req.getParameter("inputName");
		int inputAge = Integer.parseInt(req.getParameter("inputAge")); 
		
		log.debug("inputName:" + inputName );
		log.debug("inputAddress:" + inputAddress );
		log.debug("inputAge:" + inputAge);
		// 아까 application.properties에서 설정한 것
		
		//Spring에서 redirect하는 방법 logging.level.edu.kh.demo=debug
		// 쌍따옴표 안에 가장 먼저 redirect: 이라고 알려줘야 한다
		
		return "redirect:/param/main"; //리다이렉트 시에는 서버의 주소
	}
	
	// 컨트롤러에 의헤 이 메서드가 매핑 됨
	
	
	
	/*
	 * 2. RequestParam 어노에티션 = 낱개로 된 파라미터를 얻어온다
	 * - request 객체를 이용한 파라미터 전달 어노테이션
	 * - 매개변수 앞에 해당 어노테이션을 작성하면 매개변수에 값이 주입 됨
	 * - 주입되는 데이터는 매개변수의 타입에 맞게 알아서 형변환된다.
	 * 
	 * 기본 작성법
	 * @(RequestParam("key") 가지고오고싶은자료형 매개변수명(=내맘대로)) <- 여기까지의 어노테이션을 매개변수 자리에 둔다.
	 * 
	 * [속성을 추가하여 RequestParam작성하는 법]
	 * @(RequestParam(value = "key", required = false, defaultValue="1")
	 * 
	 * value: 전달받은 input태그의 name속성값, 즉 파라미터의 키
	 * 원래는 value = "key"가 없어도 됐지만 뒤에 인자를 넣으려면 반드시 추가해야 함
	 * 
	 * required: 입력된 name속성값 파라미터의 필수 여부를 지정한다 (안 쓰면 true)
	 * defaultValue: 파라미터 중 일치하는 name 속성 값이 없을 경우에 대입할 값을 지정한다. => required = false인 경우 한정으로 사용 가능하다
	 * => /pararm/test2라는 곳으로 요청한다.
	 * 
	 * 폼 태그 안에 있어도 input들 중에 name속성이 없다면 아예 그건 빼고 제출된다
	 * 
	 * required = true인 파라미터가 존재하지 않는다면 bad request 에러가 발생한다.
	 * 다만 빈 문자열일때는 에러가 발생하지 않는다. 빈 문자열 자체가 name속성값으로 넘어오기 때문이다. 
	 * => 해당 태그에 name이 없냐 있냐가 핵심
	 * */
	
	
	
	@PostMapping("test2") // ../test2라는 post방식 요청을 매핑하는 메서드
	public String paramTest2(@RequestParam("title") String title
			,@RequestParam("writer") String writer
			,@RequestParam("price") int price 
			,@RequestParam(value="publisher", required=false , defaultValue="kh") String publisher) { // 디폴트 값이 true라는 것이 중요. required=false로 설정해야 
							// 원래는 value=안써도 그냥 key만 딱 쓰면 됐었는데
		
		// "title"이라는 키를 즉 name을 인자로 주면  value 값이 타입 맞춰져 String title에 넣어진다.
		
		log.debug("title: " + title);
		log.debug("writer: " + writer);
		log.debug("price: " + price); // 알아서 int가 된 상태임 (원래는 html에서 저렇게 넘어오면 String인데) 
		// => 다만 파라미터 형변환이 되는 경우에는 파라미터 value값이 필수적으로 작성되어 있어야 한다.
		log.debug("publisher: " + publisher);
		
		
		return "redirect:/param/main"; 
	}
	
	
	
	// 3. @RequesParam 여러개 파라미터
	// - 똑같은 name, 즉 키를 갖는 파라미터를 가져온다 (배열, 리스트 둘 다 가능) => String[] 또는 List<String>
	// 제출된 파라미터를 한번에 묶어서 얻어오기 => 타입은 Key는 String Value는 Object => Map<String, Object>
	
	@PostMapping("test3") // ../test3라는 post방식 요청을 매핑하는 메서드
	public String paramTest3(@RequestParam("color") String[] colorArr
			,@RequestParam("fruit") List<String> fruitList
			,@RequestParam Map<String, Object> paramap ) {
		
		log.debug("colorArr: "+ Arrays.toString(colorArr) );		
		log.debug("colorArr: "+ fruitList );		
		log.debug("paramMap: " +paramap);
		// 제출된 모든 파라미터가 하나의 맵에 저장 된다. 다만 같은 name속성을 가진 파라미터는 String[]나 List로 저장되는 것이 아니라 
		// 무조건 처음 제출된 value의 값만 저장된다.
		
		return "redirect:/param/main";
	}
	

	
	// 4. @ModelAttribute에 대하여 => DTO, VO와 같이 사용하는 어노테이션
	
	// 전달받은 파라미터의 name속성 값이 함께 사용되는 DTO의 필드명과 같다면 자동으로 Setter를 호출하여 필드에 값을 저장한다
	
	// 주의사항: DTO에 기본 생성자와 Setter가 필수적으로 있어야 해당 어노테이션이 유효하다
	
	// @ModelAttribute를 이용해 필드에 세팅된 객체를 커맨드 객체라고 한다.
	
	@PostMapping("test4")
	public String paramTest4( /*@ModelAttribute*/ MemberDTO memberDTO) { // 컨트롤러단이고 DTO 클래스를 인자로 넣었으니 알아서 DTO인줄 안다.
		log.debug("멤버 DTO는 다음과 같다.\n" +memberDTO);
		return "redirect:main"; // 상대경로 /param/main 인데 상대경로이므로 맨 아래 경로를 갈아끼운다.
		// 상대경로는 현재 위치가 기준이된다 => 현재 경로의 가장 마지막 부분을 redirect:main으로 갈아끼우는 것
		// 여기서는 똑같이 main이라는 이름이므로 똑같은 경로로 ,즉 자기 웹주소로 리다이렉트 된다.		
		
		// 다만 DTO필드명과 제출한 폼 내에서의 변수 이름이 같아야만 한다
	
	}
	
	
}
