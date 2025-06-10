package logic;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SaleItem {
	
	private int saleid; // 주문번호
	private int seq; // 주문 상품번호
	private int itemid; // 상품아이디
	private int quantity; // 주문상품수량
	private Item item; // 상품아이디에 해당하는 상품번호
	public SaleItem(int saleid, int seq, ItemSet itemSet) {
		this.saleid = saleid;
		this.seq = seq;
		this.item = itemSet.getItem();
		this.itemid = itemSet.getItem().getId(); // 상품 id
		this.quantity = itemSet.getQuantity(); // 주문수량
	}
}
