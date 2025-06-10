package controller;


import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import exception.ShopException;
import logic.Cart;
import logic.Item;
import logic.ItemSet;
import logic.Sale;
import logic.User;
import service.ShopService;

@Controller
@RequestMapping("cart")
public class CartController {
	
	@Autowired
	private ShopService service;
	
	@RequestMapping("cartAdd")
	public ModelAndView add(Integer id, Integer quantity,
			HttpSession session) {
		// new MAV(뷰 이름) : /WEB-INF/view/cart/cart.jsp
		ModelAndView mav = new ModelAndView("cart/cart");
		Item item = service.getItem(id);
		Cart cart = (Cart)session.getAttribute("CART");
		if(cart == null) {
			cart = new Cart();
			session.setAttribute("CART", cart);
		}
		cart.push(new ItemSet(item, quantity));
		mav.addObject("message", item.getName()+" : " + quantity + "개 장바구니 추가");
		mav.addObject("cart", cart);
		return mav;
	}
	
	@RequestMapping("cartDelete")
	public ModelAndView delete(Integer id, HttpSession session) {
		ModelAndView mav = new ModelAndView("cart/cart");
		Cart cart = (Cart)session.getAttribute("CART");
		ItemSet removeObj = cart.getItemSetMap().remove(id);
		mav.addObject("message", removeObj.getItem().getName() + "삭제");
		mav.addObject("cart", cart);
		return mav;
	}
	
	@RequestMapping("cartView")
	public ModelAndView view(HttpSession session) {
		ModelAndView mav = new ModelAndView("cart/cart");
		mav.addObject("message", "장바구니 상품 조회");
		mav.addObject("cart", session.getAttribute("CART"));
		return mav;
	}
	@RequestMapping("checkout")
	public String checkout(HttpSession session) {
		return null;
	}
	
	@RequestMapping("end")
	public ModelAndView checkend(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		Cart cart = (Cart)session.getAttribute("CART");
		User loginUser = (User)session.getAttribute("loginUser");
		Sale sale = service.checkend(loginUser, cart);
		session.removeAttribute("CART");
		mav.addObject("sale", sale);
		return mav;
	}
}
