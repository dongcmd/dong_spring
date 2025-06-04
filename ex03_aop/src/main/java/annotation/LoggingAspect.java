package annotation;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component 
@Aspect // AOP 처리 클래스
@Order(3) // AOP 처리 클래스의 순서 지정
public class LoggingAspect {
	/* AOP 관련 용어
	 * 	pointcut : 핵심 메서드 설정
	 * 	execution(public * annotation..*(..))
	 * 	execution(접근제한자 리턴타입 패키지명..*(매개변수) )
	 * 		=> annotation 패키지에 속한 모든 public 메서드
	 * 	Advice : 실행되는 시점 설정
	 * 		Before : 핵심메서드 실행 전
	 * 		After : 핵심메서드 실행 후
	 * 		AfterReturning : 핵심메서드 정상 실행 후
	 * 		AfterThrowing : 핵심메서드 오류 실행 후
	 * 		Around : 핵심메서드 실행 전, 후
	 */
	
	// pointcut 설정, annotation 패키지의 모든 public 메서드
	final String publicMethod = "execution(public * annotation..*(..))";
	
	// advice 설정
	@Before(publicMethod)
	public void before() {
		System.out.println("[Logging]Before() 실행 전 호출");
	}
	@AfterReturning(pointcut = publicMethod, returning = "ret")
	public void afterReturning(Object ret) {
		System.out.println("[Logging]AfterReturning() 정상 종료 후 호출, 리턴값 : " + ret);
	}
	@AfterThrowing(pointcut = publicMethod, throwing = "ex")
	public void afterThrowing(Throwable ex) {
		System.out.println("[Logging]AfterThrowing() 예외 종료 후 호출, 예외메세지 : " + ex);
	}
	@After(publicMethod)
	public void afterFinally() {
		System.out.println("[Logging]After() 종료 후 실행");
	}
	
}
