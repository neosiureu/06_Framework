package edu.kh.project.common.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/*
 스프링에서 예외처리 하는 방법 (우선순위별로 작성)
 
 1. 메서드 단에서 직접 처리- 최우선순위) throws + try-catch
 
 2. 컨트롤러 클래스에서 클래스 단위로 모아 처리
  (@ExceptionHandler 어노테이션을 지닌 메서드를 작성한다)
  
 3. 별도 클래스를 만들어 프로젝트 단위로 모아 처리 
 (@ControllerAdvice 어노테이션을 지닌 클래스를 작성 => 해당 클래스 내부에서 @ExceptionHandler 어노테이션을 지닌 메서드 작성) 
 
 
 */



@ControllerAdvice 
// 전역적으로 예외처리를 활성화하는 어노테이션

public class ExceptionController {
	// @ExceptionHandler(예외종류): 어떤 예외를 다룰지 작성
	
	// 예외 종류: 메서드 별로 처리할 예외를 지정
	// SQLExeption.class- SQL 관련 예외만 처리 (최상위 부모)
	// IOExeption.class- 입출력 관련 예외만 처리
	// NoResourceFoundException.class- 404에러 (요청한 주소를 찾을 수 없을 때) => 404.html로 forward시킨다
	
	
	@ExceptionHandler(NoResourceFoundException.class)
	public String notFound() {
		return "error/404"; // resources/template/error/404.html
	}
	
	
	// 프로젝트에서 발생하는 모든 종류의 예외를 잡아서 처리한다.
	
	@ExceptionHandler(Exception.class)
	public String allExeptionHandler(Exception e, Model model) {
		e.printStackTrace();
		model.addAttribute("e", e);
		return "error/500";
	}
	

	

}


// 서버에서의 http 응답상태에 대해 꼭 알아야하는 정보

/*
 
 400: 클라이언트가 서버쪽으로 bad request가 왔을 때
 
 403: 서버에서 외부에 접근 거부 (forbidden error)
 
 404: 요청 주소를 찾을 수 없음
 
 405: 허용되지 않는 메서드 요청방식 (Method Not Allowed)
 
 500: sts 콘솔창을 보면 해결되는 인터널 서버 에러
 
 */
 