package aop;

import javax.servlet.http.HttpSession;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import exception.ShopException;
import logic.Cart;
import logic.User;

@Component
@Aspect
public class CartAspect {
	/*
	 *  pointcut : CartController 클래스의 매개변수의 마지막이 session
	 *  		&& check* 로 시작하는 메서드
	 */
	@Before("execution(* controller.Cart*.check*(..))"
			+ " && args(.., session)")
	public void cartCheck(HttpSession session) throws Throwable {
		User loginUser = (User)session.getAttribute("loginUser");
		if(loginUser == null) {
			throw new ShopException("로그인 하세요", "../user/login");
		}
		Cart cart = (Cart)session.getAttribute("CART");
		if(cart == null || cart.getItemSetMap().size() == 0) {
			throw new ShopException("상품을 담아주세요", "../item/list");
		}
	}

}
