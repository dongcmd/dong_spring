package controller;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import exception.ShopException;
import logic.Sale;
import logic.User;
import service.ShopService;
import service.UserService;

@Controller
@RequestMapping("user")
public class UserController {
	
	@Autowired
	private UserService service;
	@Autowired
	private ShopService shopService;
	
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
		/*
		 * userid 맞는 User를 db에서 조회
		 * 비번 검증
		 * 	일치 : session.setAttribute(login, dbuser) 로긴 정보
		 * 	불일치 : 비번확인하셈 . 출력(error.login.password)
		 * mypage 이동
		 */
	}
	
	@RequestMapping("mypage")
	public ModelAndView idCheckMypage(String userid, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		User user = service.selectUser(userid);
		List<Sale> saleList = shopService.saleList(userid);
		mav.addObject("user", user);
		mav.addObject("saleList", saleList);
		return mav;
	}
	
	@RequestMapping("logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:login";
	}
	// 로그인 상태, 본인 정보만 조회 검증 => AOP 클래스 처리 User.LoginAspect.userIdCheck()
	@RequestMapping({"update", "delete"})
	public ModelAndView idCheckUser(String userid, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		User user = service.selectUser(userid);
		mav.addObject("user", user);
		return mav;
	}
	
	@PostMapping("update")
	public ModelAndView idCheckUpdate(@Valid User user,
			BindingResult bresult, String userid, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		if(bresult.hasErrors()) {
			bresult.reject("error.update.user");
			return mav;
		}
		User loginUser = (User)session.getAttribute("loginUser");
		if(!loginUser.getPassword().equals(user.getPassword())) {
			bresult.reject("error.login.password");
			return mav;
		}
		try {
			service.userUpdate(user);
			if(loginUser.getUserid().equals(user.getUserid())) {
				// 로그인 정보의 데이터를 수정된 데이터로 변경
				session.setAttribute("loginUser", user);
			}
			mav.setViewName("redirect:mypage?userid=" + user.getUserid());
		} catch(Exception e) {
			e.printStackTrace();
			throw new ShopException("고객 정보 수정 실패",
					"update?userid=" + user.getUserid());
		}
		return mav;		
	}
	@PostMapping("delete")
	public ModelAndView idCheckDelete(String password, String userid,
			HttpSession session) {
		ModelAndView mav = new ModelAndView();
		// 관리자 탈퇴 불가
		if(userid.equals("admin")) {
			throw new ShopException("관리자 탈퇴는 불가합니다.", "mypage?userid=" + userid);
		}
		User loginUser = (User)session.getAttribute("loginUser");
		// 비번 불일치
		if(!password.equals(loginUser.getPassword())) {
			throw new ShopException("비밀번호 확인필요.", "delete?userid=" + userid);
		}
		// 비번 일치
		try {
			service.userDelete(userid);
		} catch(Exception e) {
			e.printStackTrace();
			throw new ShopException("탈퇴시 오류발생.", "delete?userid=" + userid);
		}
		// 탈퇴 성공
		String url = null;
		if(loginUser.getUserid().equals("admin")) { // 관리자
			url = "redirect:../user/list";
		} else { // 일반회원
			session.invalidate();
			url = "redirect:login";
		}
		mav.setViewName(url);
		return mav;
		/*
		 * UserLoginaApect.userIdCheck() 메서드 실행 설정
		 * 탈퇴 검증
		 * 1. 관리자의 경우 탈퇴 불가
		 * 2. 비밀번호 검증 => 로그인된 비번과 비교
		 * 		관리자가 타인 탈퇴 : 관리자 비번 검증
		 * 		본인 탈퇴시 : 본인 비번 검증
		 * 3-1. 비번 불일치
		 * 		메세지 출력 후 delete 페이지로 이동
		 * 3-2. 비번 일치
		 * 		db에서 사용자 정보 삭제
		 * 3-2-1. 본인 탈퇴시 로그아웃 후 로긴페이지
		 * 3-2-2. 타인 탈퇴시 user/list 페이지 이동
		 * 
		 */
	}
	
	@PostMapping("password")
	public String loginCheckPassword(String password, String chgpass,
			HttpSession session) {
		User loginUser = (User)session.getAttribute("loginUser");
		if(password.equals(loginUser.getPassword())) {
			try {
				service.userChgpass(loginUser.getUserid(), chgpass);
			} catch (Exception e) {
				e.printStackTrace();
				throw new ShopException("비밀번호 db 수정 오류", "password");
			}
			return "redirect:mypage?userid=" + loginUser.getUserid();
		} else {
			throw new ShopException("입력한 비밀번호가 다릅니다.", "password");
		}
		/* 
		 * 본인의 비번만 변경 가능.
		 * 1. 로그인 검증 => AOP 클래스
		 * 	UserLoginAspect.loginCheck()
		 * 	 pointcut : UserController.loginCheck*(..)인 메서드이고,
		 * 					마지막 변수가 HttpSession인 메서드
		 *   advice : Around
		 * 2. db 비밀번호와 입력한 현재 비번과 일치 검증
		 * 3-1. 일치 :  db를 수정. session 정보 변경. mypage 이동
		 * 3-2. 불일치 : 오류 메세지 출력 후 password 페이지 이동(새로고침)  
		 */
	}
	
	@PostMapping("{url}search")
	public ModelAndView search(User user, BindingResult bresult
			, @PathVariable String url) {
		// @PathVariable : {url}의 값을 매개변수로 전달
		// idsearch 요청시 url : id
		// pwsearch 요청시 url : pw
		ModelAndView mav = new ModelAndView();
		String title = "아이디";
		String code = "error.userid.search";
		if(url.equals("pw")) {
			title = "비번 초기화";
			code = "error.password.search";
			if(user.getUserid() == null ||
					user.getUserid().trim().equals("")) {
				bresult.rejectValue("userid", "error.required");
			}
		}
		if(user.getEmail() == null || user.getEmail().trim().equals("")) {
			bresult.rejectValue("email", "error.required");
		}
		if(user.getPhoneno() == null || user.getPhoneno().trim().equals("")) {
			bresult.rejectValue("phoneno", "error.required");
		}
		if(bresult.hasErrors()) {
			bresult.reject("error.input.check");
		return mav;
		}
		
		// 입력값 정상인 경우
		if(user.getUserid() != null && user.getUserid().trim().equals("")) {
			user.setUserid(null);
		}
		
		String result = service.getSearch(user);
		if(result == null) {
			bresult.reject(code);
			return mav;
		} else if(url.equals("pw")) { // 비밀번호 초기화
			result = service.resetPw(user);
		}
		mav.addObject("result", result);
		mav.addObject("title", title);
		mav.setViewName("search");
		return mav;
	}
	
	@RequestMapping("list")
	public ModelAndView adminCheckUserList(HttpSession session) {
		ModelAndView mav = new ModelAndView("user/list");
		List<User> list = service.list();
		
		mav.addObject("list", list);
		return mav;
	}
}
