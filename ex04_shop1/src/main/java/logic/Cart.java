package logic;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;

@Getter
public class Cart {

//	private List<ItemSet> itemSetList = new ArrayList<ItemSet>();
	private Map<Integer, ItemSet> itemSetMap = new LinkedHashMap<>();
	// 상품번호, ItemSet
	
	public void push(ItemSet itemSet) {
//		itemSetList.add(itemSet);
		if(itemSetMap.get(itemSet.getItem().getId()) == null) {
			itemSetMap.put(itemSet.getItem().getId(), itemSet);
		} else {
			itemSetMap.get(itemSet.getItem().getId())
			.setQuantity(itemSetMap.get(itemSet.getItem().getId())
				.getQuantity() + itemSet.getQuantity()
			);
		}
	}
	public int getTotal() {
//		return itemSetList.stream()
//				.mapToInt(s -> s.getItem().getPrice() * s.getQuantity())
//				.sum();
		return itemSetMap.entrySet().stream()
				.mapToInt(e -> e.getValue().getItem().getPrice() * e.getValue().getQuantity())
				.sum();
	}
}
