package controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


import logic.User;
import service.UserService;

@Controller
@RequestMapping("user")
public class UserController {
	@Autowired
	private UserService service;
	
	@GetMapping("*")
	public ModelAndView form() {
		ModelAndView mav = new ModelAndView();
		mav.addObject(new User());
		return mav;
	}
	
	@PostMapping("join")
	// BindingResult는 입력값(검증대상)의 다음에 와야 함.
	public ModelAndView userAdd(@Valid User user, BindingResult bresult) {
		ModelAndView mav = new ModelAndView();
		if(bresult.hasErrors()) { // globalError 추가
			bresult.reject("error.input.user");
			bresult.reject("error.input.check");
			return mav;
		}try {
			service.userInsert(user);
		} catch(DataIntegrityViolationException e) { // 기본키 중복
			e.printStackTrace();
			bresult.reject("error.duplicate.user");
			return mav;
		} catch(Exception e) {
			e.printStackTrace();
			return mav;
		}
		mav.setViewName("redirect:login");
		return mav;
	}
	
	@PostMapping("login")
	public ModelAndView login(User user, BindingResult bresult
			,HttpSession session) {
		// @Valid 어노테이션으로 등록한 방식과 같은 방식으로 처리
		if(user.getUserid().trim().length() < 3 ||
			user.getUserid().trim().length() > 10) {
			bresult.rejectValue("userid", "error.required");
		}
		if(user.getPassword().trim().length() < 3 ||
				user.getPassword().trim().length() > 10) {
			bresult.rejectValue("password", "error.required");
		}
		ModelAndView mav = new ModelAndView();
		if(bresult.hasErrors()) {
			bresult.reject("error.input.check");
			return mav;
		}
		
		User dbUser = service.selectUser(user.getUserid());
		if(dbUser == null) { // 아이디 없음
			bresult.reject("error.login.userid");
			return mav;
		}
		if(dbUser.getPassword().equals(user.getPassword())) { // 일치
			session.setAttribute("loginUser", dbUser);
			mav.setViewName("redirect:mypage?userid=" + user.getUserid());
			return mav;
		} else {
			bresult.reject("error.login.password");
			return mav;
		}
	}
	/*
	 * userid 맞는 User를 db에서 조회
	 * 비번 검증
	 * 	일치 : session.setAttribute(login, dbuser) 로긴 정보
	 * 	불일치 : 비번확인하셈 . 출력(error.login.password)
	 * mypage 이동 
	 */
	
	@RequestMapping("mypage")
	public ModelAndView idCheckMypage(String userid, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		User user = service.selectUser(userid);
		mav.addObject("user", user);
		return mav;
	}
	
	@RequestMapping("logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:login";
	}
	/*
	 * 	AOP 설정
	 * 	1. 로그인 여부 검증
	 * 		X => 로긴 후 거래 출력 후 login.jsp
	 * 	2. 본인 거래 여부 검증
	 * 		admin 아니면서 다른 사용자 정보는 출력 불가	
	 * 
	 */
	@RequestMapping({"update", "delete"})
	public ModelAndView idCheckUpdate(String userid, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		User user = service.selectUser(userid);
		mav.addObject("user", user);
		return mav;
	}
}
