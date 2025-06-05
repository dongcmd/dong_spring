package aop;

import javax.servlet.http.HttpSession;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

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
	@Around(
"execution(* controller.User*.idCheck*(..)) && args(.., userid, session)")
	public Object userIdCheck(ProceedingJoinPoint joinPoint, String userid,
			HttpSession session) throws Throwable {
		User loginUser = (User)session.getAttribute("loginUser");
		if(loginUser == null || !(loginUser instanceof User)) { // 로그아웃 상태
			throw new ShopException("[idCheck]로그인 필요", "login");
		}
		if(!loginUser.getUserid().equals("admin")
			&& !loginUser.getUserid().equals(userid)) {
			throw new ShopException(
					"[idCheck]본인 정보만 거래 가능", "../item/list");
		}
		return joinPoint.proceed();
	}
	
}