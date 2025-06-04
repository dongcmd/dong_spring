package controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import logic.Item;
import service.ShopService;

@Controller
@RequestMapping("item")
public class ItemController {
	@Autowired
	private ShopService service;
	
	// http://localhost:8080/ex04_shop1/item/list
	@RequestMapping("list") // Get, Post 모두 받음
	public ModelAndView list() {
		// ModelAndView : View의 이름과 전달 데이터를 저장
		ModelAndView mav = new ModelAndView();
		List<Item> itemList = service.itemList();
		mav.addObject("itemList", itemList);
//		mav.setViewName("item/list"); view 이름이 null인 경우 요청 url에서 가져 옴.
		return mav;
	}
	
	// http://localhost:8080/ex04_shop1/item/detail?id=1
	@GetMapping({"detail", "update", "delete"}) // Get만 받음
	// 인자의 변수명과 파라미터명이 같아야 함.
	public ModelAndView detail(Integer id) {
		ModelAndView mav = new ModelAndView();
		Item item = service.getItem(id);
		mav.addObject("item", item);
		return mav;
	}
	
	@GetMapping("create")
	public ModelAndView create() {
		ModelAndView mav = new ModelAndView();
		mav.addObject(new Item());
		return mav;
	}
	
	@PostMapping("create")
	/*
	 *	Item item : 파라미터의 이름과 같은 item 클래스의 setProperty를 이용해
	 *			파라미터 값 저장
	 * 		Item item = new Item().setName(request.getParameter("name"));
	 * 
	 *	@Valid : Item 클래스에 정의된 내용으로 입력값 검증해 결과를 bresult 저장
	 *	request : 파일 업로드 위치 선정에 사용
	 */
	public ModelAndView register(@Valid Item item, BindingResult bresult,
			HttpServletRequest request) {
		// bresult : 유효성 검사의 결과
		ModelAndView mav = new ModelAndView();
		if(bresult.hasErrors()) { // 입력값 검증시 오류 발생
			return mav; // item/create.jsp 이동
		}
		// 입력값 정상
		service.itemCreate(item, request);
		mav.setViewName("redirect:list"); // list 재 요청
		return mav;
	}
	
	@PostMapping("update")
	public ModelAndView update(@Valid Item item, BindingResult bresult,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		if(bresult.hasErrors()) {
			return mav;
		}
		service.itemUpdate(item, request);
		mav.setViewName("redirect:list");
		return mav;
	}
	
	@PostMapping("delete")
	public String delete(Integer id) {
		service.itemDelete(id);
		return "redirect:list"; // View 선택
	}
}
