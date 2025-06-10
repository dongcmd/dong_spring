package service;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dao.ItemDao;
import dao.SaleDao;
import dao.SaleItemDao;
import logic.Cart;
import logic.Item;
import logic.ItemSet;
import logic.Sale;
import logic.SaleItem;
import logic.User;

@Service
public class ShopService {
	@Autowired
	private ItemDao itemDao;
	@Autowired
	private SaleDao saleDao;
	@Autowired
	private SaleItemDao saleItemDao;
	
	
	public List<Item> itemList() {
		return itemDao.list();
	}
	
	public Item getItem(Integer id) {
		return itemDao.select(id);
	}

	public void itemCreate(Item item, HttpServletRequest request) {
		if(item.getPicture() != null && !item.getPicture().isEmpty()) { // 업로드된 파일 존재
			String path = request.getServletContext().getRealPath("/") + "img/"; // 폴더 지정
			uploadFileCreate(item.getPicture(), path);
			item.setPictureUrl(item.getPicture().getOriginalFilename());
		}
		int maxId = itemDao.maxId();
		item.setId(maxId+1);
		itemDao.insert(item);
	}
	
	// 파일 업로드
	private void uploadFileCreate(MultipartFile picture, String path) {
		String orgFile = picture.getOriginalFilename(); // 원본 파일 이름
		File f = new File(path);
		if(!f.exists()) f.mkdirs();
		
		try {
			// picture : 파일의 내용
			// transferTo : picture의 내용을 new File의 위치로 저장
			picture.transferTo(new File(path + orgFile));
		} catch(Exception e) { e.printStackTrace(); }
	}

	public void itemUpdate(Item item, HttpServletRequest request) {
		if(item.getPicture() != null && !item.getPicture().isEmpty()) {
			String path = request.getServletContext().getRealPath("/") + "img/";
			uploadFileCreate(item.getPicture(), path);
			item.setPictureUrl(item.getPicture().getOriginalFilename());
		}
		itemDao.update(item);
	}

	public void itemDelete(Integer id) {
		itemDao.delete(id);
	}
	
	public Sale checkend(User loginUser, Cart cart) {
		int maxsaleid = saleDao.getMaxSaleId();
		Sale sale = new Sale();
		sale.setSaleid(maxsaleid+1);
		sale.setUser(loginUser);
		sale.setUserid(loginUser.getUserid());
		saleDao.insert(sale);
		int seq = 0;
		for(ItemSet is : cart.getItemSetMap().values()) {
			SaleItem saleItem = new SaleItem(sale.getSaleid(), ++seq, is);
			sale.getItemList().add(saleItem);
			saleItemDao.insert(saleItem);
		}
		return sale; // 주문정보, 고객정보, 주문상품 정보
	}

	public List<Sale> saleList(String userid) {
		List<Sale> list = saleDao.list(userid);
		for(Sale sale : list) {
			List<SaleItem> saleItemList = saleItemDao.list(sale.getSaleid());
			for(SaleItem si : saleItemList) {
				Item item = itemDao.select(si.getItemid());
				si.setItem(item);
			}
			sale.setItemList(saleItemList);
		}
		return list;
	}
}
