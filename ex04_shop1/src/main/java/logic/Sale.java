package logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data 
public class Sale {
	
	private int saleid; // 주문번호
	private String userid; // 주문 고객 아이디
	private Date saledate; // 주문 일자
	private User user; // 고객 정보
	private List<SaleItem> itemList = new ArrayList<>(); // 주문상품 목록
	public int getTotal() {
		return itemList.stream().mapToInt
				(s -> s.getItem().getPrice() * s.getQuantity()).sum();
	}

}
