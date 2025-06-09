package logic;

import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class User {
	@Size(min=3, max=10, message="아이디는 3~10자")
	private String userid;
	private String channel;
	@Size(min=3, max=10, message="비번은 3~10자")
	private String password;
	@NotEmpty(message="사용자 이름은 필수")
	private String username;
	private String phoneno;
	private String postcode;
	private String address;
	@NotEmpty(message="이메일 입력 필수")
	@Email(message="이메일 형식으로 입력")
	private String email;
//	@NotNull(message="생일 입력 필수")
	@Past(message="생일은 과거 날짜만 가능")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date birthday; 
	
}
