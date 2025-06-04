package service;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dao.ItemDao;
import logic.Item;

@Service
public class ShopService {
	@Autowired
	private ItemDao itemDao;
	
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
}
