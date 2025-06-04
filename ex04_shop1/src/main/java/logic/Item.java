package logic;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class Item {
	private int id;
	// @NotEmpty : null || ""인 경우 오류
	@NotEmpty(message="상품명을 입력하세요")
	private String name;
	@Min(value=10, message="10원 이상 가능")
	@Max(value=100000, message="10만원 이하 가능")
	private int price;
	@NotEmpty(message="상품설명을 입력하세요")
	private String description;
	private String pictureUrl;
	private MultipartFile picture; // 업로드된 파일
}
