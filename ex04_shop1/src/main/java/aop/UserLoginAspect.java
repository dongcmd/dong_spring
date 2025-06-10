package aop;

import javax.servlet.http.HttpSession;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import service.ShopService;
import exception.ShopException;
import logic.User;

/*		핵심 메서드 선택
 * 1. pointcut : UserController.idCheck 메서드로 시작하고
 * 				마지막 매개변수가 String, HttpSession인 메서드
 * 2. advice : Around로 이용
 */
@Component
@Aspect
public class UserLoginAspect {

    private final ShopService shopService;

    UserLoginAspect(ShopService shopService) {
        this.shopService = shopService;
    }
	
	public ShopException loginCheckMethod() {
		return new ShopException("로그인하세요", "login");
	}
	
	@Around(
"execution(* controller.User*.idCheck*(..)) && args(.., userid, session)")
	public Object userIdCheck(ProceedingJoinPoint joinPoint, String userid,
			HttpSession session) throws Throwable {
		User loginUser = (User)session.getAttribute("loginUser");
		if(loginUser == null || !(loginUser instanceof User)) { // 로그아웃 상태
			throw loginCheckMethod();
		}
		if(!loginUser.getUserid().equals("admin")
			&& !loginUser.getUserid().equals(userid)) {
			throw new ShopException(
					"[idCheck]본인 정보만 거래 가능", "../item/list");
		}
		return joinPoint.proceed();
	}
	
	@Around(
"execution(* controller.UserController.loginCheck*(..)) && args(.., session)")
	public Object loginCheck(ProceedingJoinPoint joinPoint,
			HttpSession session) throws Throwable {
		User loginUser = (User)session.getAttribute("loginUser");
		if(loginUser == null || !(loginUser instanceof User)) { // 로그아웃 상태
			throw loginCheckMethod();
		}
		return joinPoint.proceed();
	}
	
	@Before("execution(* controller.*.adminCheck*(..)) && args(.., session)")
	public void adminCheck(HttpSession session) throws Throwable {
		User loginUser = (User)session.getAttribute("loginUser");
		if(loginUser == null || !(loginUser instanceof User)) {
			throw loginCheckMethod();
		}
		if(!loginUser.getUserid().equals("admin")) {
			throw new ShopException("관리자만 가능",
				"../user/mypage?userid=" + loginUser.getUserid());
		}
	}
	
}