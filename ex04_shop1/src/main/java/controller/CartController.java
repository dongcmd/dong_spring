package controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import logic.Cart;
import logic.Item;
import logic.ItemSet;
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

}
