package kr.gdu.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.gdu.exception.ShopException;
import kr.gdu.logic.User;

public class BoardInterceptor implements HandlerInterceptor{
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		String boardid = request.getParameter("boardid");
		HttpSession session = request.getSession();
		User login = (User)session.getAttribute("loginUser");
		if(boardid == null || boardid.equals("1")) {
			if(login == null || !login.getUserid().equals("admin")) {
				String msg = "[Interceptor]관리자만 등록 가능";
				String url = request.getContextPath() + "/board/list?boardid=1";
				throw new ShopException(msg, url);
			}
		}
		return true;
	}
}
