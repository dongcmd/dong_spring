package logic;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;

@Getter
public class Cart {

//	private List<ItemSet> itemSetList = new ArrayList<ItemSet>();
	private Map<String, ItemSet> itemSetMap = new LinkedHashMap<String, ItemSet>();
	// 상품명, ItemSet
	
	public void push(ItemSet itemSet) {
//		itemSetList.add(itemSet);
		if(itemSetMap.get(itemSet.getItem().getName()) == null) {
			itemSetMap.put(itemSet.getItem().getName(), itemSet);
		} else {
			itemSetMap.get(itemSet.getItem().getName()).setQuantity(
			  itemSetMap.get(itemSet.getItem().getName()).getQuantity()
			   + itemSet.getQuantity()
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
